/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

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

import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcChartAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcChartAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcAction.class);
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcPerfService cmcPerfService;
    @Autowired
    private CmcChannelService cmcChannelService;
    private CmcAttribute cmcAttribute;
    private Entity entity;
    private Long entityId;
    private Long cmcId;
    private Long cmcPortId;
    private Integer cmcType;
    private String cmcTypeString;
    private String timeType;
    private String perfType;
    private Long index;
    private String st;
    private String et;

    /**
     * 获取flush使用率
     * 
     * @return String
     */
    public String getFlashUsageByEntityId() {
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        double flushRate;
        if (cmcAttribute != null && cmcAttribute.getTopCcmtsSysFlashRatio() != null) {
            flushRate = cmcAttribute.getTopCcmtsSysFlashRatio();
        } else {
            flushRate = 0;
        }
        try {
            ChartUtilities.writeChartAsPNG(response.getOutputStream(),
                    createChart(ResourcesUtil.getString("CCMTS.flashUtility"), new DefaultValueDataset(flushRate)),
                    150, 150);
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
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        double cpu;
        if (cmcAttribute != null && cmcAttribute.getTopCcmtsSysCPURatio() != null) {
            cpu = cmcAttribute.getTopCcmtsSysCPURatio();
        } else {
            cpu = 0;
        }
        try {
            ChartUtilities.writeChartAsPNG(response.getOutputStream(),
                    createChart(ResourcesUtil.getString("WorkBench.CpuRanking"), new DefaultValueDataset(cpu)), 150,
                    150);
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
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        double memoryRate;
        if (cmcAttribute != null && cmcAttribute.getTopCcmtsSysRAMRatio() != null) {
            memoryRate = cmcAttribute.getTopCcmtsSysRAMRatio();
        } else {
            memoryRate = 0;
        }
        try {
            ChartUtilities.writeChartAsPNG(response.getOutputStream(),
                    createChart(ResourcesUtil.getString("WorkBench.MemRanking"), new DefaultValueDataset(memoryRate)),
                    150, 150);
        } catch (IOException e) {
            logger.error("MemoryUsage chart error:{}", e);
        }
        return NONE;
    }

    /**
     * 获取可纠错误码率,重构时增加废弃声明 2013-10-28
     * 
     * @return String
     */
    @Deprecated
    public String getBitErrorRateByCmcPortId() {
        //List<UsBitErrorRate> bitErrorRates = cmcPerfService.getTopPortletErrorCodesLoading();
        return NONE;
    }

    /**
     * 获取不可纠错误码率
     * 
     * @return String
     */
    public String getBitUnErrorRateByCmcPortId() {
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        List<UsBitErrorRate> usBitErrorRates = cmcPerfService.getTopPortletErrorCodesLoading(null);
        Integer unBitErrorRate = 0;
        for (UsBitErrorRate usBitErrorRate : usBitErrorRates) {
            if (usBitErrorRate.getCmcPortId().equals(cmcPortId)) {
                String formatedMac = MacUtils.convertMacToDisplayFormat(usBitErrorRate.getMacAddress(), macRule);
                usBitErrorRate.setMacAddress(formatedMac);
                unBitErrorRate = usBitErrorRate.getUnBitErrorRate();
            }
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            ChartUtilities.writeChartAsPNG(
                    response.getOutputStream(),
                    createChart(ResourcesUtil.getString("WorkBench.unBitErrorRate"), new DefaultValueDataset(
                            unBitErrorRate)), 150, 150);
        } catch (IOException e) {
            logger.error("usBitErrorRate chart error:{}", e);
        }
        return NONE;
    }

    /**
     * 获取利用率
     * 
     * @return String
     */
    public String getChannelUtilizationByCmcPortId() {
        List<ChannelPerfInfo> cmcChannelPerfInfoList = cmcChannelService.getCmcChannelPerfInfoList(cmcId);
        float channelUtilization = 0;
        for (ChannelPerfInfo aCmcChannelPerfInfoList : cmcChannelPerfInfoList) {
            if (aCmcChannelPerfInfoList.getCmcPortId().equals(cmcPortId)) {
                channelUtilization = aCmcChannelPerfInfoList.getChannelUtilization();
            }
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            ChartUtilities.writeChartAsPNG(
                    response.getOutputStream(),
                    createChart(ResourcesUtil.getString("CCMTS.channelUtilization"), new DefaultValueDataset(
                            channelUtilization)), 150, 150);
        } catch (IOException e) {
            logger.error("usBitErrorRate chart error:{}", e);
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
        valueFormatter.setPositiveSuffix(Symbol.PERCENT);
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
        scaleFormatter.setPositiveSuffix(Symbol.PERCENT);
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

    public ViewerParam getPerfViewerParam() {
        ViewerParam viewerParam = new ViewerParam();
        viewerParam.setPerfType(perfType);
        viewerParam.setEntityId(entityId);
        viewerParam.setCmcId(cmcId);
        viewerParam.setIndex(index);
        if (ViewerParam.CUSTOM.equalsIgnoreCase(timeType)) {
            viewerParam.setEt(et);
            viewerParam.setSt(st);
        }
        viewerParam.setTimeType(timeType);
        return viewerParam;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public CmcPerfService getCmcPerfService() {
        return cmcPerfService;
    }

    public void setCmcPerfService(CmcPerfService cmcPerfService) {
        this.cmcPerfService = cmcPerfService;
    }

    public CmcChannelService getCmcChannelService() {
        return cmcChannelService;
    }

    public void setCmcChannelService(CmcChannelService cmcChannelService) {
        this.cmcChannelService = cmcChannelService;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    public Integer getCmcType() {
        return cmcType;
    }

    public void setCmcType(Integer cmcType) {
        this.cmcType = cmcType;
    }

    public String getCmcTypeString() {
        return cmcTypeString;
    }

    public void setCmcTypeString(String cmcTypeString) {
        this.cmcTypeString = cmcTypeString;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getPerfType() {
        return perfType;
    }

    public void setPerfType(String perfType) {
        this.perfType = perfType;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public Logger getLogger() {
        return logger;
    }

}
