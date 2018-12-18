/***********************************************************************
 * $Id: OltPortalAction.java,v1.0 2013年10月28日 上午9:05:14 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer.Pointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.Fan;
import com.topvision.ems.epon.domain.Olt;
import com.topvision.ems.epon.domain.PonUsedInfo;
import com.topvision.ems.epon.domain.Power;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;

import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2013年10月28日-上午9:05:14
 *
 */
@Controller("oltPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltPortalAction extends AbstractEponAction {
    private static final long serialVersionUID = -7239741917355016604L;
    private final Logger logger = LoggerFactory.getLogger(OltPortalAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    private Olt olt;
    private Entity entity;
    private OltAttribute oltAttribute;
    private int slotNumInUse;
    private int fanNumInUse;
    private int powerNumInUse;
    private EntitySnap entitySnap;
    private String oltSoftVersion;
    private Long typeId;
    //用于设备快照页用户视图保存
    private String leftPartItems;
    private String rightPartItems;
    private String deviceIpList = "";
    private String cameraSwitch;
    private double memUsedRatio;
    private double flashUsedRatio;
    private double cpuUsedRatio;

    /**
     * 显示设备快照
     * 
     * @return String
     */
    public String showOltEntityById() {
        try {
            Properties properties = systemPreferencesService.getModulePreferences("camera");
            cameraSwitch = properties.getProperty("camera.switch");
            olt = oltService.getOltStructure(entityId);
            entity = entityService.getEntity(entityId);
            entity.setIcon64(getIconByType(entityId,entity));
            entity.setIcon("/images/" + entity.getIcon64());
            oltAttribute = oltService.getOltAttribute(entityId);
            EntitySnap snap = entityService.getEntitySnapById(entityId);
            //add by fanzidong,需要在展示前格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            String displayRule = uc.getMacDisplayStyle();
            String formatedOutbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getOutbandMac(), displayRule);
            oltAttribute.setOutbandMac(formatedOutbandMac);
            String formatedInbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getInbandMac(), displayRule);
            oltAttribute.setInbandMac(formatedInbandMac);

            Long tempTime = System.currentTimeMillis();
            if (snap != null && snap.isState()) {
                if (snap.getSysUpTime() != null) {
                    oltAttribute.setOltDeviceUpTime((tempTime - snap.getSnapTimeMillis()) / 1000
                            + Long.parseLong(snap.getSysUpTime()) / 100);
                } else {
                    oltAttribute.setOltDeviceUpTime(-1L);
                }
                memUsedRatio = snap.getMem();
                cpuUsedRatio = snap.getCpu();
                flashUsedRatio = snap.getDisk();
            } else {
                oltAttribute.setOltDeviceUpTime(-1L);
                memUsedRatio = -1;
                cpuUsedRatio = -1;
                flashUsedRatio = -1;
            }
            List<OltSlotAttribute> oltSlotList = oltSlotService.getOltSlotList(entityId);
            slotNumInUse = 0;
            fanNumInUse = 0;
            powerNumInUse = 0;
            for (OltSlotAttribute oltSlotAttribute : oltSlotList) {
                if (oltSlotAttribute.getTopSysBdActualType() != null && oltSlotAttribute.getTopSysBdActualType() > 0) {
                    slotNumInUse++;
                    // 根据不同的OLT类型，来获取OLT的软件版本
                    // modify lzt 2013-4-11
                    if (entityTypeService.isPN8603Type(entity.getTypeId())
                            && EponConstants.BOARD_PRECONFIG_MPUA.equals(oltSlotAttribute.getTopSysBdPreConfigType())) {
                        oltSoftVersion = oltSlotAttribute.getbSoftwareVersion();
                    } else if (entityTypeService.isPN8602Type(entity.getTypeId())
                            && EponConstants.BOARD_PRECONFIG_GEUA.equals(oltSlotAttribute.getTopSysBdPreConfigType())) {
                        oltSoftVersion = oltSlotAttribute.getbSoftwareVersion();
                    } else if (entityTypeService.isPN8602_EType(entity.getTypeId())
                            && EponConstants.BOARD_PRECONFIG_MEUA.equals(oltSlotAttribute.getTopSysBdPreConfigType())) {
                        oltSoftVersion = oltSlotAttribute.getbSoftwareVersion();
                    }
                }
            }
            for (Fan fan : olt.getFanList()) {
                if (fan.getFanCardPresenceStatus() == 1) {
                    fanNumInUse++;
                }
            }
            for (Power power : olt.getPowerList()) {
                if (power.getPowerCardPresenceStatus() == 1) {
                    powerNumInUse++;
                }
            }
            List<String> entityIpList = entityService.getEntityIpList(entityId);
            for (String ipAddress : entityIpList) {
                if (ipAddress != "") {
                    deviceIpList += "," + ipAddress;
                }
            }
            deviceIpList = deviceIpList.substring(1);
            //加载用户保存的设备快照视图
            leftPartItems = this.getPortalView(entity.getTypeId()).getProperty("portalLeftPart");
            rightPartItems = this.getPortalView(entity.getTypeId()).getProperty("portalRightPart");
        } catch (Exception e) {
            logger.error("olt data load is error:{}", e);
        }
        return SUCCESS;
    }

    /**
     * 取消管理后olt设备快照
     * 
     * @return String
     */
    public String showOltCancelJsp() {
        olt = oltService.getOltStructure(entityId);
        entity = entityService.getEntity(entityId);
        oltAttribute = oltService.getOltAttribute(entityId);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        String formatedOutbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getOutbandMac(), displayRule);
        oltAttribute.setOutbandMac(formatedOutbandMac);
        String formatedInbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getInbandMac(), displayRule);
        oltAttribute.setInbandMac(formatedInbandMac);

        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltSlotList(entityId);
        slotNumInUse = 0;
        fanNumInUse = 0;
        powerNumInUse = 0;
        for (OltSlotAttribute oltSlotAttribute : oltSlotList) {
            if (oltSlotAttribute.getTopSysBdActualType() > 0) {
                slotNumInUse++;
            }
        }
        for (Fan fan : olt.getFanList()) {
            if (fan.getFanCardPresenceStatus() == 1) {
                fanNumInUse++;
            }
        }
        for (Power power : olt.getPowerList()) {
            if (power.getPowerCardPresenceStatus() == 1) {
                powerNumInUse++;
            }
        }
        return SUCCESS;
    }

    
    /**
     * 显示机房信息
     * 
     * @return String String
     * @throws java.io.IOException
     */
    public String showOltLocation() throws IOException {
        entity = entityService.getEntity(entityId);
        oltAttribute = oltService.getOltAttribute(entityId);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        String formatedOutbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getOutbandMac(), displayRule);
        oltAttribute.setOutbandMac(formatedOutbandMac);
        String formatedInbandMac = MacUtils.convertMacToDisplayFormat(oltAttribute.getInbandMac(), displayRule);
        oltAttribute.setInbandMac(formatedInbandMac);
        return SUCCESS;
    }

    /**
     * 运行时长显示界面
     * 
     * @return String
     */
    public String showOltUptimeByEntity() {
        return SUCCESS;
    }

    /**
     * 获取OLT运行时长
     * 
     * @return String
     * @throws java.io.IOException
     *             Response流异常
     */
    public String getOltUptimeByEntity() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        Long tempTime = System.currentTimeMillis();
        try {
            EntitySnap snap = entityService.getEntitySnapById(entityId);
            oltAttribute = oltService.getOltAttribute(entityId);
            if (snap != null && snap.isState()) {
                if (snap.getSysUpTime() != null) {
                    oltAttribute.setOltDeviceUpTime((tempTime - snap.getSnapTimeMillis()) / 1000
                            + Long.parseLong(snap.getSysUpTime()) / 100);
                } else {
                    oltAttribute.setOltDeviceUpTime(-1L);
                }
            } else {
                oltAttribute = new OltAttribute();
                oltAttribute.setOltDeviceUpTime(-1L);
            }
        } catch (Exception e) {
            oltAttribute = new OltAttribute();
            oltAttribute.setOltDeviceUpTime(-1L);
            logger.error("", e);
        }
        json.put("sysUpTime", oltAttribute.getOltDeviceUpTime());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取flush使用率
     * 
     * @return String
     */
    public String getFlashUsageByEntityId() {
        entitySnap = oltService.getOltCurrentPerformance(entityId);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.epon.resources");
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        double flushRate = 0;
        if (entitySnap != null && entitySnap.getDisk() != null && entitySnap.getDisk() > 0) {
            flushRate = entitySnap.getDisk() * 100;
        }
        try {
            response.getCharacterEncoding();
            ChartUtilities.writeChartAsPNG(
                    response.getOutputStream(),
                    createChart(resourceManager.getNotNullString("Business.flushUtility", "FLUSH Usage"),
                            new DefaultValueDataset(flushRate)), 150, 150);
        } catch (IOException e) {
            logger.error("FlashUsage chart error:{}", e);
        }
        return NONE;
    }

    /**
     * 获取cpu使用率
     * 
     * @return String
     */
    public String getCpuUsageByEntityId() {
        entitySnap = oltService.getOltCurrentPerformance(entityId);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.epon.resources");
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        double cpu = 0;
        if (entitySnap != null && entitySnap.getCpu() != null && entitySnap.getCpu() > 0) {
            cpu = entitySnap.getCpu() * 100;
        }
        try {
            ChartUtilities.writeChartAsPNG(
                    response.getOutputStream(),
                    createChart(resourceManager.getNotNullString("Business.cpuUtility", "CPU Usage"),
                            new DefaultValueDataset(cpu)), 150, 150);
        } catch (IOException e) {
            logger.error("CPUUsage chart error:{}", e);
        }
        return NONE;
    }

    /**
     * 获取内存使用率
     * 
     * @return String
     */
    public String getMemoryUsageByEntityId() {
        entitySnap = oltService.getOltCurrentPerformance(entityId);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.epon.resources");
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        double memoryRate = 0;
        if (entitySnap != null && entitySnap.getMem() != null && entitySnap.getMem() > 0) {
            memoryRate = entitySnap.getMem() * 100;
        }
        try {
            ChartUtilities.writeChartAsPNG(
                    response.getOutputStream(),
                    createChart(resourceManager.getNotNullString("Business.memUtility", "MEM Usage"),
                            new DefaultValueDataset(memoryRate)), 150, 150);
        } catch (IOException e) {
            logger.error("MemoryUsage chart error:{}", e);
        }
        return NONE;
    }

    /**
     * 构造JFreeChart
     * 
     * @param title
     *            标题
     * @param valuedataset
     *            数据集
     * @return JFreeChart
     */
    private static JFreeChart createChart(String title, ValueDataset valuedataset) {
        DialPlot dialplot = new DialPlot();
        dialplot.setView(0.0D, 0.0D, 1.0D, 1.0D);
        // 设置数据集合
        dialplot.setDataset(valuedataset);
        dialplot.setOutlinePaint(Color.white);
        // 开始设置显示框架结构
        StandardDialFrame simpledialframe = new StandardDialFrame();
        simpledialframe.setBackgroundPaint(Color.lightGray);
        simpledialframe.setForegroundPaint(Color.darkGray);
        dialplot.setDialFrame(simpledialframe);
        // 结束设置显示框架结构
        GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(170, 170, 220), new Point(), new Color(
                51, 51, 68));
        DialBackground dialbackground = new DialBackground(gradientpaint);
        dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.VERTICAL));
        dialplot.setBackground(dialbackground);
        // 设置显示在表盘中央位置的信息
        DialTextAnnotation dialtextannotation = new DialTextAnnotation(title);
        dialtextannotation.setFont(new Font("Dialog", 1, 12));
        dialtextannotation.setPaint(Color.WHITE);
        dialtextannotation.setRadius(0.53999999999999996D);
        dialplot.addLayer(dialtextannotation);
        // 当前值的引用
        DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
        dialvalueindicator.setFont(new Font("Dialog", 1, 14));
        dialvalueindicator.setPaint(Color.WHITE);
        DecimalFormat valueFormatter = new DecimalFormat("0.00");
        valueFormatter.setPositiveSuffix("%");
        dialvalueindicator.setNumberFormat(valueFormatter);
        dialvalueindicator.setBackgroundPaint(new Color(0, 0, 0, 0f));
        dialvalueindicator.setOutlinePaint(new Color(0, 0, 0, 0f));
        dialplot.addLayer(dialvalueindicator);
        // 根据表盘的直径大小，设置总刻度范围
        StandardDialScale standarddialscale = new StandardDialScale();
        standarddialscale.setTickRadius(0.88D);
        standarddialscale.setTickLabelPaint(Color.white);
        standarddialscale.setTickLabelOffset(0.15999999999999999D);
        standarddialscale.setStartAngle(220f);
        standarddialscale.setExtent(-260f);
        DecimalFormat scaleFormatter = new DecimalFormat("0");
        scaleFormatter.setPositiveSuffix("%");
        standarddialscale.setTickLabelFormatter(scaleFormatter);
        standarddialscale.setMajorTickPaint(Color.WHITE);
        standarddialscale.setMinorTickPaint(Color.WHITE);
        standarddialscale.setLowerBound(0);
        standarddialscale.setTickLabelFont(new Font("Dialog", 0, 8));
        // 主意是 dialplot.addScale（）不是dialplot.addLayer（）
        dialplot.addScale(0, standarddialscale);
        // 设置刻度范围（红色）
        StandardDialRange standarddialrange = new StandardDialRange(85d, 100D, Color.red);
        standarddialrange.setInnerRadius(0.58999999999999996D);
        standarddialrange.setOuterRadius(0.59999999999999996D);
        standarddialrange.setPaint(Color.red);
        dialplot.addLayer(standarddialrange);
        // 设置刻度范围（橘黄色）
        StandardDialRange standarddialrange1 = new StandardDialRange(60D, 85D, Color.orange);
        standarddialrange1.setInnerRadius(0.58999999999999996D);
        standarddialrange1.setOuterRadius(0.59999999999999996D);
        standarddialrange1.setPaint(Color.orange);
        dialplot.addLayer(standarddialrange1);
        // 设置刻度范围（绿色）
        StandardDialRange standarddialrange2 = new StandardDialRange(0D, 60D, Color.green);
        standarddialrange2.setInnerRadius(0.58999999999999996D);
        standarddialrange2.setOuterRadius(0.59999999999999996D);
        standarddialrange2.setPaint(Color.green);
        dialplot.addLayer(standarddialrange2);

        Color valuePaintColor = Color.GREEN;
        if (valuedataset.getValue().doubleValue() > 85d) {
            valuePaintColor = Color.RED;
        } else if (valuedataset.getValue().doubleValue() > 60d) {
            valuePaintColor = Color.ORANGE;
        }
        // 设置指针
        Pointer pointer = new Pointer();
        pointer.setOutlinePaint(Color.GRAY);
        pointer.setFillPaint(valuePaintColor);
        dialplot.addLayer(pointer);
        // 实例化DialCap
        DialCap dialcap = new DialCap();
        dialcap.setRadius(0.05000000000000001D);
        dialcap.setOutlineStroke(new BasicStroke(1));
        GradientPaint capGradientpaint = new GradientPaint(0, 1000, Color.GRAY, 0, 0, valuePaintColor, false);
        dialcap.setFillPaint(capGradientpaint);
        dialcap.setOutlinePaint(Color.WHITE);
        dialplot.setCap(dialcap);
        // 生成chart对象
        JFreeChart jfreechart = new JFreeChart(dialplot);
        jfreechart.setBackgroundPaint(new Color(255, 255, 255, 0));
        return jfreechart;
    }

    /**
     * 保存用户设置的设备快照页面视图
     * @author flackyang
     * @since 2013-11-07
     * @return
     */
    public String savePortalView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties portalView = new Properties();
        portalView.setProperty("portalLeftPart", leftPartItems);
        portalView.setProperty("portalRightPart", rightPartItems);
        userPreferencesService.batchSaveModulePreferences(Long.toString(typeId), uc.getUserId(), portalView);
        return NONE;
    }

    /**
     * 获取用户保存的设备快照页面视图
     * @author flackyang
     * @since 2013-11-07
     * @param typeId
     * @return
     */
    private Properties getPortalView(long typeId) {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences userPre = new UserPreferences();
        userPre.setModule(Long.toString(typeId));
        userPre.setUserId(uc.getUserId());
        Properties portalView = userPreferencesService.getModulePreferences(userPre);
        return portalView;
    }

    /**
     * 获取Olt下级设备统计信息
     * 在面板图显示下级设备信息
     * @return
     * @throws IOException 
     */
    public String loadOltSubInfo() throws IOException {
        SubDeviceCount subCount = oltService.getSubCountInfo(entityId);
        JSONObject json = JSONObject.fromObject(subCount);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取Olt pon端口实用情况
     * @return
     * @throws IOException 
     */
    public String loadPonUsedInfo() throws IOException {
        PonUsedInfo ponUsed = oltService.getPonUsedInfo(entityId);
        writeDataToAjax(ponUsed);
        return NONE;
    }

    /**
     *  获取CPU、内存、Flash利用率
     * @return
     * @throws Exception 
     */
    public String getUsedRatio() throws Exception {
        JSONObject usedJson = new JSONObject();
        entitySnap = oltService.getOltCurrentPerformance(entityId);
        if (entitySnap != null && entitySnap.isState()) {
            if (entitySnap.getMem() != null) {
                usedJson.put("memUsedRatio", entitySnap.getMem());
            } else {
                usedJson.put("memUsedRatio", -1);
            }
            if (entitySnap.getCpu() != null) {
                usedJson.put("cpuUsedRatio", entitySnap.getCpu());
            } else {
                usedJson.put("cpuUsedRatio", -1);
            }
            if (entitySnap.getDisk() != null) {
                usedJson.put("flashUsedRatio", entitySnap.getDisk());
            } else {
                usedJson.put("flashUsedRatio", -1);
            }
        } else {
            usedJson.put("memUsedRatio", -1);
            usedJson.put("cpuUsedRatio", -1);
            usedJson.put("flashUsedRatio", -1);
        }
        usedJson.write(response.getWriter());
        return NONE;
    }
    //设备快照图片，8602E分A和B，B型图片地址另读;8602G也分8口和16口
    public String getIconByType(Long entityId,Entity entity){
        Long typeId=entity.getTypeId();
        if(entityTypeService.isPN8602_EType(typeId)){
            List<OltSlotAttribute> oltSlotList = oltSlotService.getOltSlotList(entityId);
            for(OltSlotAttribute oltSlot:oltSlotList){
                if(EponConstants.BOARD_ONLINE_MEUB.equals(oltSlot.getTopSysBdActualType())){
                    return "network/pn8602-EB_64.png";
                }
            }
        }
        if(entityTypeService.isPN8602_GType(typeId)){
            List<OltSlotAttribute> oltSlotList = oltSlotService.getOltSlotList(entityId);
            for(OltSlotAttribute oltSlot:oltSlotList){
                if(EponConstants.BOARD_ONLINE_MGUB.equals(oltSlot.getTopSysBdActualType())){
                    return "network/pn8602-G_8_64.png";
                }
            }
        }
        return entity.getIcon64();
    }

    public Olt getOlt() {
        return olt;
    }

    public void setOlt(Olt olt) {
        this.olt = olt;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public OltAttribute getOltAttribute() {
        return oltAttribute;
    }

    public void setOltAttribute(OltAttribute oltAttribute) {
        this.oltAttribute = oltAttribute;
    }

    public int getSlotNumInUse() {
        return slotNumInUse;
    }

    public void setSlotNumInUse(int slotNumInUse) {
        this.slotNumInUse = slotNumInUse;
    }

    public int getFanNumInUse() {
        return fanNumInUse;
    }

    public void setFanNumInUse(int fanNumInUse) {
        this.fanNumInUse = fanNumInUse;
    }

    public int getPowerNumInUse() {
        return powerNumInUse;
    }

    public void setPowerNumInUse(int powerNumInUse) {
        this.powerNumInUse = powerNumInUse;
    }

    public EntitySnap getEntitySnap() {
        return entitySnap;
    }

    public void setEntitySnap(EntitySnap entitySnap) {
        this.entitySnap = entitySnap;
    }

    public String getOltSoftVersion() {
        return oltSoftVersion;
    }

    public void setOltSoftVersion(String oltSoftVersion) {
        this.oltSoftVersion = oltSoftVersion;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getLeftPartItems() {
        return leftPartItems;
    }

    public void setLeftPartItems(String leftPartItems) {
        this.leftPartItems = leftPartItems;
    }

    public String getRightPartItems() {
        return rightPartItems;
    }

    public void setRightPartItems(String rightPartItems) {
        this.rightPartItems = rightPartItems;
    }

    public String getDeviceIpList() {
        return deviceIpList;
    }

    public void setDeviceIpList(String deviceIpList) {
        this.deviceIpList = deviceIpList;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

    public double getMemUsedRatio() {
        return memUsedRatio;
    }

    public void setMemUsedRatio(double memUsedRatio) {
        this.memUsedRatio = memUsedRatio;
    }

    public double getFlashUsedRatio() {
        return flashUsedRatio;
    }

    public void setFlashUsedRatio(double flashUsedRatio) {
        this.flashUsedRatio = flashUsedRatio;
    }

    public double getCpuUsedRatio() {
        return cpuUsedRatio;
    }

    public void setCpuUsedRatio(double cpuUsedRatio) {
        this.cpuUsedRatio = cpuUsedRatio;
    }

}
