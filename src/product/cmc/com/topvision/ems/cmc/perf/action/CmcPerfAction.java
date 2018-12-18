/***********************************************************************
 * $Id: CmcPerfAction.java,v1.0 2012-5-6 下午01:45:41 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.domain.FloatTimePoint;
import com.topvision.ems.cmc.domain.PortalChannelUtilizationShow;
import com.topvision.ems.cmc.domain.SnrCurrentPerf;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.performance.service.Viewer;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.WrongPerfViewerParamException;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.highcharts.HighChartsUtils;
import com.topvision.platform.util.highcharts.LabelsUtils;
import com.topvision.platform.util.highcharts.SeriesUtils;
import com.topvision.platform.util.highcharts.domain.Highcharts;
import com.topvision.platform.util.highcharts.domain.Labels;
import com.topvision.platform.util.highcharts.domain.Point;
import com.topvision.platform.util.highcharts.domain.Series;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author dosion
 * @created @2012-5-6-下午01:45:41
 * 
 */
@Controller("cmcPerfAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcPerfAction extends BaseAction {
    private static final long serialVersionUID = -8059600689365750698L;
    private final Logger logger = LoggerFactory.getLogger(CmcPerfAction.class);
    public static String CC_SNR = "CC_SNR";
    public static String CC_DOL = "CC_DOL";
    public static String CC_SYSTEM = "CC_SYSTEM";
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;
    @Resource(name = "perfTargetService")
    private PerfTargetService perfTargetService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @SuppressWarnings("rawtypes")
    @Resource(name = "performanceService")
    private PerformanceService performanceService;
    private JSONArray perfdata = new JSONArray();
    private JSONArray utilizationData = new JSONArray();
    private JSONObject supportPerf = new JSONObject();
    private JSONObject cmcPerfTargetCycleObject;
    private boolean status;
    private int period;
    private String perfType;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    private Integer perfCycle;
    // 在页面上显示的曲线图的周期，先写死为15分钟，单位：million seconds
    private Integer showInterval = 900000;
    // 曲线图的显示时间
    private Long showTime;
    private boolean snrStatus;
    private boolean perfCollect;
    private boolean utilizationStatus;
    private boolean channelCmNumStatic;
    private boolean usBitErrorRate;// 上行信道误码率
    private int snrPeriod;// 信噪比采集周期
    private int perfCollectPeriod;
    private int utilizationPeriod;
    private boolean systemStatus;
    private int systemPeriod;
    private int channelCmNumStaticPeriod;
    private int usBitErrorRatePeriod;// 误码率采集周期
    private int speedPeriod;// 速率采集周期

    private JSONObject chartParam;
    private JSONObject viewerParam;
    private JSONArray cmcChanelNames;
    private String nodePath;
    private String timeType;
    private Long index;
    private String st;
    private String et;
    private Boolean showLabel;
    private Double ymin;
    private Double ymax;
    private String ytitle;
    private String title;

    private String nmName;
    private String connectPerson;
    private String cmcMac;
    private String cmcIp;
    private Integer cmcType;
    private Long deviceType;
    private Long ponId;

    // CCMTS性能指标
    private JSONObject cmcPerfTargetCycleJson;
    private String cmcName;
    private String manageIp;

    private Integer upLinkFlow; // 上联口流量
    private Integer macFlow; // Mac域流量

    private Integer cpuUsed; // CPU利用率
    private Integer memUsed; // 内存利用率
    private Integer flashUsed; // Flash利用率

    private Integer memTemp; // 内存温度
    private Integer rfTemp; // 射频模块温度
    private Integer upTemp; // 上行模块温度
    private Integer bcmTemp; // 芯片温度
    private Integer powerTemp; // 电源模块温度

    private Integer optTxPower; // 光口发送功率
    private Integer optRePower; // 光口接收功率
    private Integer optCurrent; // 光口偏置电流
    private Integer optVoltage; // 光口电压
    private Integer optTemp; // 光口温度

    private Integer snr; // 信噪比
    private Integer bER; // 误码率
    private Integer ccer; // 可纠错码
    private Integer ucer; // 不可纠错码
    private Integer channelSpeed; // 信道速率
    private Integer channnelUsed; // 信道利用率
    private Integer macUsed; // Mac域利用率

    private Integer totalCm; // CM数总数
    private Integer onlineCm; // CM在线数
    private Integer offlineCm; // CM离线数
    private Integer cmflap;//
    private String direction;
    private CmcAttribute cmcAttribute;

    /**
     * 显示性能展示页面
     * 
     * @return String
     */
    public String showCmcCurPerf() {
    	setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        cmcChanelNames = JSONArray.fromObject(cmcPerfService.getCmcChanelNames(cmcId));
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Entity entity = entityService.getEntity(entityId);
        viewerParam = JSONObject.fromObject(getPerfViewerParam());
        nodePath = entity.getDisplayName();
        Viewer viewer = getPerfViewer();
        ytitle = null;
        title = null;
        String subNodePath = "";
        String[] perfTypes = perfType.split(" ");
        for (String perfType1 : perfTypes) {
            Viewer v = getPerfViewer(perfType1);
            ytitle = ytitle == null ? v.getYTitle() : ytitle + "\\" + v.getYTitle();
            title = title == null ? v.getViewerName() : title + "\\" + v.getViewerName();
            subNodePath = subNodePath + "-" + v.getSeriesName(getPerfViewerParam());
        }
        nodePath = nodePath + subNodePath;
        Highcharts highcharts = HighChartsUtils.createDefaultLineXdateTimeChart("highcharts-0", title, ytitle, null,
                null);
        highcharts.getChart().setMarginTop(100);
        highcharts.getTitle().setY(40);
        highcharts.getSubtitle().setY(55);
        highcharts.getCredits().setEnabled(false);
        highcharts.getyAxis().get(0).setMin(viewer.getYMin());
        highcharts.getyAxis().get(0).setMax(viewer.getYMax());
        ymin = viewer.getYMin();
        ymax = viewer.getYMax();
        highcharts.getxAxis().get(0).setMin(getPerfViewerParam().getStLong().doubleValue());
        highcharts.getxAxis().get(0).setMax(getPerfViewerParam().getEtLong().doubleValue());
        chartParam = HighChartsUtils.toJSONObject(highcharts);
        return SUCCESS;
    }

    /**
     * 显示CPU和内存利用率曲线图
     * 
     * @return String
     */
    public String showCmcCpuAndMemGraph() {
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        viewerParam = JSONObject.fromObject(getPerfViewerParam());
        Entity entity = entityService.getEntity(entityId);
        nodePath = entity.getDisplayName();

        Highcharts highcharts = HighChartsUtils.createDefaultLineXdateTimeChart(null,
                ResourcesUtil.getString("CCMTS.todayCPUMemUtilizationGraph"),
                ResourcesUtil.getString("CCMTS.CPUMemUtilization") + "(%)", null, 300);
        highcharts.getChart().setMarginRight(50);
        highcharts.getCredits().setEnabled(false);
        chartParam = HighChartsUtils.toJSONObject(highcharts);
        return SUCCESS;
    }

    /**
     * 读取CPU和内存利用率曲线数据 必须的参数 entityId/cmcId/channelIndex/perfType/timeType timeType
     * 为ViewerParam.CUSTOM的时候必须有st/et timeType必须为ViewerParam中的常量
     * 
     * @return String
     */
    public String seriesCpuAndMemRead() {
        perfType = "cpu";
        Viewer viewer = getPerfViewer();
        String unit = viewer.getViewerUnit();
        List<Point> list = viewer.read(getPerfViewerParam());
        List<Series> series = SeriesUtils.createSeriesArray(null, list, ResourcesUtil.getString("type.Cpu"), "green",
                null, unit);
        perfType = "mem";
        viewer = getPerfViewer();
        unit = viewer.getViewerUnit();
        list = viewer.read(getPerfViewerParam());
        series = SeriesUtils.createSeriesArray(series, list, ResourcesUtil.getString("type.Mem"), "blue", null, unit);
        JSONArray jsonArray = HighChartsUtils.toJSONArray(series);
        writeDataToAjax(jsonArray);
        return NONE;
    }

    /**
     * 读取信噪比参数
     * 
     * @return String
     */
    public String seriesNoiseRead() {
        perfType = "noise";
        Viewer viewer = getPerfViewer();
        String unit = viewer.getViewerUnit();
        List<Point> list = viewer.read(getPerfViewerParam());
        List<Series> series = SeriesUtils.createSeriesArray(null, list, ResourcesUtil.getString("WorkBench.Noise"),
                "green", null, unit);
        JSONArray jsonArray = HighChartsUtils.toJSONArray(series);
        writeDataToAjax(jsonArray);
        return NONE;
    }

    /**
     * 读取误码率
     * 
     * @return String
     */
    public String seriesErrorRateRead() {
        perfType = "bitErrorRate";
        Viewer viewer = getPerfViewer();
        String unit = viewer.getViewerUnit();
        List<Point> list = viewer.read(getPerfViewerParam());
        List<Series> series = SeriesUtils.createSeriesArray(null, list,
                ResourcesUtil.getString("WorkBench.bitErrorRate"), "green", null, unit);
        perfType = "unBitErrorRate";
        viewer = getPerfViewer();
        list = viewer.read(getPerfViewerParam());
        series = SeriesUtils.createSeriesArray(series, list, ResourcesUtil.getString("WorkBench.unBitErrorRate"),
                "blue", null, unit);
        JSONArray jsonArray = HighChartsUtils.toJSONArray(series);
        writeDataToAjax(jsonArray);
        return NONE;
    }

    /**
     * 读取用户数
     * 
     * @return String
     */
    public String seriesChannelCmNumRead() {
        perfType = "cmNumOnline";
        Viewer viewer = getPerfViewer();
        String unit = viewer.getViewerUnit();
        List<Point> list = viewer.read(getPerfViewerParam());
        List<Series> series = SeriesUtils.createSeriesArray(null, list,
                ResourcesUtil.getString("CCMTS.view.cmNumOnline.ytitle"), "green", null, unit);
        perfType = "cmNumOffline";
        viewer = getPerfViewer();
        unit = viewer.getViewerUnit();
        list = viewer.read(getPerfViewerParam());
        series = SeriesUtils.createSeriesArray(series, list, ResourcesUtil.getString("CCMTS.view.cmNumOffline.ytitle"),
                "blue", null, unit);
        JSONArray jsonArray = HighChartsUtils.toJSONArray(series);
        writeDataToAjax(jsonArray);
        return NONE;
    }

    /**
     * 读取通道利用率参数
     * 
     * @return String
     */
    public String seriesChannelUtilizationRead() {
        perfType = "channelUtilization";
        Viewer viewer = getPerfViewer();
        String unit = viewer.getViewerUnit();
        List<Point> list = viewer.read(getPerfViewerParam());
        List<Series> series = SeriesUtils.createSeriesArray(null, list,
                ResourcesUtil.getString("CCMTS.channelUtilization"), "green", null, unit);
        JSONArray jsonArray = HighChartsUtils.toJSONArray(series);
        writeDataToAjax(jsonArray);
        return NONE;
    }

    /**
     * 显示性能展示页面
     * 
     * @return String
     */
    public String showCmcHisPerf() {
        if (entityId == null) {
            entityId = cmcService.getEntityIdByCmcId(cmcId);
        }
        Entity entity = entityService.getEntity(entityId);
        viewerParam = JSONObject.fromObject(getPerfViewerParam());
        nodePath = entity.getDisplayName();
        Viewer viewer = getPerfViewer();
        String yTitle = null;
        String title = null;
        String subNodePath = "";
        String[] perfTypes = perfType.split(" ");
        for (String perfType1 : perfTypes) {
            Viewer v = getPerfViewer(perfType1);
            yTitle = yTitle == null ? v.getYTitle() : yTitle + "\\" + v.getYTitle();
            title = title == null ? v.getViewerName() : title + "\\" + v.getViewerName();
            subNodePath = subNodePath + "-" + v.getSeriesName(getPerfViewerParam());
        }
        nodePath = nodePath + subNodePath;
        Highcharts highcharts = HighChartsUtils.createDefaultLineXdateTimeChart("highcharts-0", title, yTitle, null,
                null);
        highcharts.getChart().setMarginTop(100);
        highcharts.getTitle().setY(40);
        highcharts.getSubtitle().setY(60);
        Labels labels = LabelsUtils.createDefaultLabels(null, nodePath + "", -10, -100, null);
        highcharts.setLabels(labels);
        highcharts.getCredits().setEnabled(false);
        highcharts.getyAxis().get(0).setMin(viewer.getYMin());
        highcharts.getyAxis().get(0).setMax(viewer.getYMax());
        highcharts.getxAxis().get(0).setMin(getPerfViewerParam().getStLong().doubleValue());
        highcharts.getxAxis().get(0).setMax(getPerfViewerParam().getEtLong().doubleValue());
        chartParam = HighChartsUtils.toJSONObject(highcharts);
        return SUCCESS;
    }

    /**
     * 读取曲线数据 必须的参数 entityId/cmcId/channelIndex/perfType/timeType timeType
     * 为ViewerParam.CUSTOM的时候必须有st/et timeType必须为ViewerParam中的常量
     * 
     * @return String
     */
    public String seriesRead() {
        List<Series> series = null;
        String[] perfTypes = perfType.split(" ");
        for (String perfType1 : perfTypes) {
            Viewer viewer = null;
            try {
                viewer = getPerfViewer(perfType1);
            } catch (WrongPerfViewerParamException e) {
                logger.debug("", e);
            }
            String unit = viewer.getViewerUnit();
            String seriesName = viewer.getSeriesName(getPerfViewerParam());

            List<Point> list = viewer.read(getPerfViewerParam());
            series = SeriesUtils.createSeriesArray(series, list, seriesName, null, null, unit);
        }
        JSONArray jsonArray = HighChartsUtils.toJSONArray(series);
        writeDataToAjax(jsonArray);
        return NONE;
    }

    /**
     * 刷新性能曲线图上显示的数据
     * 
     * @return String
     */
    public String refreshCmcSnrPerf() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 更新性能参数曲线
        Map<String, Object> map = new HashMap<String, Object>();
        Timestamp startTime = new Timestamp(showTime);
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        json.put("showTime", endTime.getTime());
        if (cmcPerfService.hasSNRMonitor(cmcId)) {
            map.put(CC_SNR, true);
            json.put("snrList", getSnrPerfData(startTime, endTime));
        } else {
            map.put(CC_SNR, false);
        }
        json.put("supportPerf", map);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取某个CC上SNR性能参数统计列表
     * 
     * @return String
     */
    public String getSnrPerfStatic() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<SnrCurrentPerf> snrCurPerf = new ArrayList<SnrCurrentPerf>();
        for (int i = 0; i < 2; i++) {
            SnrCurrentPerf snr = new SnrCurrentPerf();
            snr.setCmcPortId((long) i);
            snr.setCurrentPerf(30);
            snr.setMaxCurrentPerf(40);
            snr.setMinCurrentPerf(20);
            snrCurPerf.add(snr);
        }
        json.put("data", snrCurPerf);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示SNR性能配置页面
     * 
     * @return String
     */
    public String showSnrPerfConf() {
        perfType = CC_SNR;
        status = cmcPerfService.hasSNRMonitor(cmcId);
        if (!status) {
            period = 0;
        } else {
            period = cmcPerfService.getSnrPeriod(cmcId);
        }
        return SUCCESS;
    }

    /**
     * 显示CCMTS性能配置页面
     * 
     * @return String
     */
    public String showCcmtsPerfConf() {
        snrStatus = cmcPerfService.hasSNRMonitor(cmcId);
        // utilizationStatus = cmcPerfService.hasChannelUtilizationMonitor(cmcId);
        systemStatus = cmcPerfService.hasCcmtsSystemMonitor(cmcId);
        // channelCmNumStatic = cmcPerfService.hasChannelCmNumMonitor(cmcId);
        usBitErrorRate = cmcPerfService.hasUsBitErrorRateMonitor(cmcId);
        if (snrStatus && systemStatus && usBitErrorRate) {
            perfCollect = true;
        }
        if (perfCollect) {
            this.snrPeriod = cmcPerfService.getSnrPeriod(cmcId);
            this.speedPeriod = cmcPerfService.getChannelSpeedStaticPeriod(cmcId);
            this.usBitErrorRatePeriod = cmcPerfService.getUsBitErrorRatePeriod(cmcId);
        }
        return SUCCESS;
    }

    /**
     * 显示DOL性能配置页面
     * 
     * @return String
     */
    public String showDolPerfConf() {
        perfType = CC_DOL;
        Integer docsIfCmtsChannelUtilizationInterval;
        docsIfCmtsChannelUtilizationInterval = cmcService.getCmtsChannelUtilizationInterval(entityId, cmcId);
        if (docsIfCmtsChannelUtilizationInterval == 0) {
            status = false;
            period = 0;
        } else {
            status = true;
            period = docsIfCmtsChannelUtilizationInterval;
        }
        return SUCCESS;
    }

    private SnmpParam getSnmpParamByIdOrType() {
        SnmpParam snmpParam = null;
        if (cmcId != null) {
            snmpParam = cmcService.getSnmpParamByCmcId(cmcId);
        } else if (entityId != null) {
            snmpParam = cmcService.getSnmpParamByEntityId(entityId);
        }
        return snmpParam;
    }

    /**
     * 配置一个CC上的perfType指定参数的性能配置
     * 
     * @return String
     */
    public String perfConf() {
        // 首先判断设备是OLT还是8800B
        SnmpParam snmpParam = getSnmpParamByIdOrType();
        if (CC_SNR.equals(perfType)) {
            if (status && !cmcPerfService.hasSNRMonitor(cmcId)) {
                cmcPerfService.startSNRMonitor(cmcId, (long) period, snmpParam);
            } else if (status && cmcPerfService.hasSNRMonitor(cmcId)) {
                cmcPerfService.resetSNRMonitor(cmcId, (long) period, snmpParam);
            } else {
                cmcPerfService.stopSNRMonitor(cmcId, snmpParam);
            }
        } else if (CC_DOL.equals(perfType)) {
            if (!status) {
                cmcService.setCmtsChannelUtilizationInterval(entityId, 0l, snmpParam);
            } else {
                cmcService.setCmtsChannelUtilizationInterval(entityId, (long) period, snmpParam);
            }
        }
        Map<String, String> message = new HashMap<String, String>();
        message.put("message", "success");
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 进行性能采集设置
     * 
     * @return String
     */
    public String ccmtsPerfConf() {
        Map<String, String> message = new HashMap<String, String>();
        boolean isExistCmc = cmcPerfService.isExistCmc(cmcId);
        if (isExistCmc) {
            // 首先判断设备是OLT还是8800B
            SnmpParam snmpParam = getSnmpParamByIdOrType();
            boolean hasMonitor = cmcPerfService.hasSNRMonitor(cmcId);
            if (perfCollect && !hasMonitor) {
                cmcPerfService.startSNRMonitor(cmcId, (long) this.snrPeriod, snmpParam);
            } else if (perfCollect && hasMonitor) {
                cmcPerfService.resetSNRMonitor(cmcId, (long) this.snrPeriod, snmpParam);
            } else if (hasMonitor) {
                cmcPerfService.stopSNRMonitor(cmcId, snmpParam);
            }

            // 不再采用设备计算通道利用率的方式，在进行通道速率统计时计算网管通道利用率
            // if (perfCollect && !cmcPerfService.hasChannelUtilizationMonitor(cmcId)) {
            // cmcPerfService.startChannelUtilizationMonitor(cmcId, (long) perfCollectPeriod, null);
            // } else if (perfCollect && cmcPerfService.hasChannelUtilizationMonitor(cmcId)) {
            // cmcPerfService.resetChannelUtilizationMonitor(cmcId, (long) perfCollectPeriod,
            // snmpParam);
            // } else if (cmcPerfService.hasChannelUtilizationMonitor(cmcId)) {
            // cmcPerfService.stopChannelUtilizationMonitor(cmcId, snmpParam);
            // }
            //
            // if (perfCollect && !cmcPerfService.hasCcmtsSystemMonitor(cmcId)) {
            // cmcPerfService.startSystemPerfMonitor(cmcId, (long) perfCollectPeriod, null);
            // } else if (perfCollect && hasMonitor) {
            // cmcPerfService.resetCcmtsSystemMonitor(cmcId, (long) perfCollectPeriod, snmpParam);
            // } else if (hasMonitor) {
            // cmcPerfService.stopCcmtsSystemMonitor(cmcId, snmpParam);
            // }
            // hasMonitor = cmcPerfService.hasChannelCmNumMonitor(cmcId);
            // if (perfCollect && !hasMonitor) {
            // cmcPerfService.startChannelCmMonitor(cmcId, (long) perfCollectPeriod, null);
            // } else if (perfCollect && hasMonitor) {
            // cmcPerfService.resetChannelCmMonitor(cmcId, (long) perfCollectPeriod, snmpParam);
            // } else if (hasMonitor) {
            // cmcPerfService.stopChannelCmMonitor(cmcId, snmpParam);
            // }

            hasMonitor = cmcPerfService.hasUsBitErrorRateMonitor(cmcId);
            if (perfCollect && !hasMonitor) {
                cmcPerfService.startUsBitErrorRateMonitor(cmcId, (long) this.usBitErrorRatePeriod, null);
            } else if (perfCollect && hasMonitor) {
                cmcPerfService.resetUsBitErrorRateMonitor(cmcId, (long) this.usBitErrorRatePeriod, snmpParam);
            } else if (hasMonitor) {
                cmcPerfService.stopUsBitErrorRateMonitor(cmcId, snmpParam);
            }

            hasMonitor = cmcPerfService.hasChannelSpeedStaticMonitor(cmcId);
            if (perfCollect && !hasMonitor) {
                cmcPerfService.startChannelSpeedStaticMonitor(cmcId, (long) this.speedPeriod, null);
            } else if (perfCollect && hasMonitor) {
                cmcPerfService.resetChannelSpeedStaticMonitor(cmcId, (long) this.speedPeriod, snmpParam);
            } else if (hasMonitor) {
                cmcPerfService.stopChannelSpeedStaticMonitor(cmcId, snmpParam);
            }
            message.put("message", "success");
        } else {
            message.put("message", "cmcnotexits");
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 通道利用率TOP 10
     * 
     * @return String
     */
    public String getTopChnlUtiliLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());

        sb.append("<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopChnlUtiliLoading'><thead><tr><th width='36'>");
        sb.append(ResourcesUtil.getString("WorkBench.Ranking"));
        sb.append("</th><th>");
        sb.append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><th style=\"text-align:center\">");
        sb.append(ResourcesUtil.getString("CMC.label.entitytype"));
        sb.append("</th><Th>");
        sb.append(ResourcesUtil.getString("WorkBench.PortName"));
        sb.append("</th><th style=\"text-align:center\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>");
        sb.append(ResourcesUtil.getString("WorkBench.Utilization"));
        sb.append("</th><th>");
        sb.append(ResourcesUtil.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr>");
        try {
            List<PortalChannelUtilizationShow> cmcChnlSnaps = cmcPerfService.getNetworkCcmtsDeviceLoadingTop(paramMap);
            if (cmcChnlSnaps != null) {
                int i = 0;
                for (PortalChannelUtilizationShow snap : cmcChnlSnaps) {
                    if (snap.getCmcType() != null) {
                        String formatedMac = MacUtils.convertMacToDisplayFormat(snap.getCmcMac(), macRule);
                        String cmcPortName = makePortName(snap.getChannelIndex(), snap.getCmcType(), snap.getIfDescr(),
                                snap.getIfType());
                        snap.setCmcMac(formatedMac);
                        if (i >= 10) {
                            sb.append("<tr><td><div class='topClsBg'>");
                            if (i >= 100) {
                                sb.append("<div>");
                                sb.append(++i);
                                sb.append("</div>");
                            } else {
                                sb.append(++i);
                            }
                            sb.append("</div></td>");
                        } else {
                            sb.append("<tr><Td><div class=topCls");
                            sb.append(++i);
                            sb.append("></div></Td>");
                        }
                        sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                        sb.append(snap.getCmcId());
                        sb.append(",'");
                        sb.append(snap.getCmcMac());
                        sb.append("','");
                        sb.append(snap.getCmcName());
                        sb.append("');\">");
                        sb.append(snap.getCmcName());
                        sb.append("</a></Td>");

                        sb.append("<td class='txtCenter'>");
                        sb.append(entityTypeService.getEntityType(Long.valueOf(snap.getCmcType())).getDisplayName());
                        sb.append("</td>");

                        sb.append("<Td><a class=my-link href=\"javascript:showChannel(");
                        sb.append(snap.getCmcPortId());
                        sb.append(",'");
                        sb.append(snap.getCmcId());
                        sb.append("','");
                        sb.append(cmcPortName);
                        sb.append("','");
                        sb.append(snap.getChannelIndex());
                        sb.append("','");
                        sb.append(snap.getDirection());
                        sb.append("');\">");
                        sb.append(cmcPortName);
                        sb.append("</Td>");
                        sb.append("<Td  class='txtCenter'>");
                        sb.append("<div id='channelUtilization'  class='percentBarBg'>")
                                .append("<div class='percentBar' style='width:" + snap.getChannelUsed()
                                        + "%;'><div class='percentBarLeftConner'></div></div>")
                                .append("<div class='percentBarTxt'><div class='noWidthCenterOuter clearBoth'><ol class='noWidthCenter leftFloatOl pT1'>")
                                .append(splitPercentNum(snap.getChannelUsed()))
                                .append("<li><div class='percentNumPercent'></div></li></ol></div></div>")
                                .append("</div>");
                        sb.append("</Td><Td align=center>");
                        // sb.append(DateUtils.FULL_FORMAT.format(snap.getDt()));
                        sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - snap.getDt().getTime(), uc
                                .getUser().getLanguage()));
                        sb.append("</Td></tr>");
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String getMoreChannelUsed() {
        return SUCCESS;
    }

    /**
     * 获取CCMTS的信道利用率
     * 
     * @return
     */
    public String loadChannelUsed() {
        JSONObject ret = new JSONObject();
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        if (nmName != null && !"".equals(nmName)) {
            if (nmName.contains("_")) {
                nmName = nmName.replace("_", "\\_");
            }
        }
        List<PortalChannelUtilizationShow> channelUseds = cmcPerfService.getChannelUsed(nmName, deviceType,
                this.getSort(), this.getDir(), start, limit);
        for (PortalChannelUtilizationShow channelUsed : channelUseds) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(channelUsed.getCmcMac(), macRule);
            String cmmPortName = makePortName(channelUsed.getChannelIndex().longValue(),
                    new Long(channelUsed.getCmcType()), channelUsed.getIfDescr(), channelUsed.getIfType());
            channelUsed.setCmcMac(formatedMac);
            channelUsed.setChannelName(cmmPortName);
        }
        ret.put("data", channelUseds);
        ret.put("rowCount", cmcPerfService.getChannelUsedCount(nmName, deviceType));
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * CCMTS设备cpu
     * 
     * @return String
     */
    public String getTopCcmtsCpuLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        sb.append("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">");
        sb.append(ResourcesUtil.getString("WorkBench.Ranking"));
        sb.append("</th><th>");
        sb.append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"));
        sb.append("</th><th style=\"text-align:center\">");
        sb.append(ResourcesUtil.getString("CMC.label.entitytype"));
        sb.append("</th><Th style=\"text-align:center\">");
        sb.append(ResourcesUtil.getString("WorkBench.MacAddress"));
        sb.append("</th><th style=\"text-align:center\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>");
        sb.append(ResourcesUtil.getString("CCMTS.cmcCurPerf.CPUUtilization"));
        sb.append("</th><th style=\"text-align:right\">");
        sb.append(ResourcesUtil.getString("WorkBench.fromUpdateTime"));
        sb.append("</th></tr>");
        try {
            List<Cmc> cmcSnaps = cmcPerfService.getNetworkCcmtsDeviceLoadingTop("cpu");
            if (cmcSnaps != null) {
                int i = 0;
                for (Cmc snap : cmcSnaps) {
                    sb.append("<tr><Td><div class=topCls");
                    sb.append(++i);
                    sb.append("></div></Td>");
                    sb.append("<Td><a class=my-link href=\"javascript:showCcmtsPortal(");
                    sb.append(snap.getCmcId());
                    sb.append(",'");
                    sb.append(snap.getTopCcmtsSysMacAddr());
                    sb.append("','");
                    sb.append(snap.getTopCcmtsSysName());
                    sb.append("');\">");
                    sb.append(snap.getTopCcmtsSysName());
                    sb.append("</a></Td>");

                    sb.append("<td style=\"text-align:center\">");
                    sb.append(entityTypeService.getEntityType(Long.valueOf(snap.getCmcDeviceStyle())).getDisplayName());
                    sb.append("</td>");

                    sb.append("<Td style=\"text-align:center\">");
                    sb.append(snap.getTopCcmtsSysMacAddr());
                    sb.append("</Td>");
                    sb.append("<Td align=center\">");
                    sb.append("<div style=\"padding-top:3px;align=absmiddle\">")
                            .append("<div style=\"float:left;width:90px;height:10px;border:1px solid yellow;")
                            .append("background-color:gray;font-size:1px;\">").append("<div style=\"float:left;width:")
                            .append(snap.getTopCcmtsSysCPURatiotoString())
                            .append(";height:10px;font-size:1px;background-color:red\"></div></div>")
                            .append("<div style=\"float:left;margin-left:-50;margin-top:-1px;color:white\">")
                            .append(snap.getTopCcmtsSysCPURatiotoString()).append("</div></div>");
                    sb.append("</Td><Td align=right>");
                    sb.append(formatDuring(System.currentTimeMillis() - snap.getSnapTime().getTime()));
                    // sb.append(DateUtils.FULL_FORMAT.format(snap.getSnapTime()));
                    sb.append("</Td></tr>");
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * CCMTS设备误码率
     * 
     * @return String
     */
    public String getTopPortletErrorCodesLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopPortletErrorCodesLoading'><thead><tr><th width='36'>")
                .append(ResourcesUtil.getString("WorkBench.Ranking"))
                .append("</th><th>")
                .append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"))
                .append("</th><th width='80'>")
                .append(ResourcesUtil.getString("CMC.label.entitytype"))
                .append("</th><Th width='69'>")
                .append(ResourcesUtil.getString("WorkBench.PortName"))
                .append("</th><th class='wordBreak'><img src=\'../images/desc.gif\' border=0 align=absmiddle hspace=5>")
                .append(ResourcesUtil.getString("CM.correctedRate")).append("</th><th class='wordBreak'>")
                .append(ResourcesUtil.getString("CM.uncorrectRate")).append("</th><th width='126'>")
                .append(ResourcesUtil.getString("WorkBench.fromUpdateTime")).append("</th></tr></thead><tobdy>");
        try {
            List<UsBitErrorRate> usBitErrorRates = cmcPerfService.getTopPortletErrorCodesLoading(paramMap);
            if (usBitErrorRates != null) {
                int i = 0;
                for (UsBitErrorRate usBitErrorRate : usBitErrorRates) {
                    if (usBitErrorRate.getCmcType() != null) {
                        String formatedMac = MacUtils
                                .convertMacToDisplayFormat(usBitErrorRate.getMacAddress(), macRule);
                        usBitErrorRate.setMacAddress(formatedMac);
                        String cmcPortName = makePortName(usBitErrorRate.getChannelIndex(),
                                new Long(usBitErrorRate.getCmcType()), usBitErrorRate.getIfDescr(),
                                usBitErrorRate.getIfType());
                        if (i >= 10) {
                            sb.append("<tr><td><div class='topClsBg'>");
                            if (i >= 100) {
                                sb.append("<div>");
                                sb.append(++i);
                                sb.append("</div>");
                            } else {
                                sb.append(++i);
                            }
                            sb.append("</div></td>");
                        } else {
                            sb.append("<tr><Td><div class=topCls");
                            sb.append(++i);
                            sb.append("></div></Td>");
                        }

                        sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                        sb.append(usBitErrorRate.getCmcId());
                        sb.append(",'");
                        sb.append(usBitErrorRate.getMacAddress());
                        sb.append("','");
                        sb.append(usBitErrorRate.getCmcName());
                        sb.append("');\">");
                        sb.append(usBitErrorRate.getCmcName());
                        sb.append("</a></Td>");

                        sb.append("<td class='txtCenter'>");
                        sb.append(entityTypeService.getEntityType(Long.valueOf(usBitErrorRate.getCmcType()))
                                .getDisplayName());
                        sb.append("</td>");

                        sb.append("<Td class='txtCenter'><a class=my-link href=\"javascript:showChannel(");
                        sb.append(usBitErrorRate.getCmcPortId());
                        sb.append(",'");
                        sb.append(usBitErrorRate.getCmcId());
                        sb.append("','");
                        sb.append(cmcPortName);
                        sb.append("','");
                        sb.append(usBitErrorRate.getChannelIndex());
                        sb.append("','");
                        sb.append(usBitErrorRate.getChannelTypeString());
                        sb.append("');\">");
                        sb.append(cmcPortName);
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(usBitErrorRate.getCcerRate() + "%");
                        sb.append("</Td>");

                        sb.append("<Td align=center>");
                        sb.append(usBitErrorRate.getUcerRate() + "%");
                        sb.append("</Td>");
                        sb.append("<Td  class='txtCenter'>");
                        sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                - usBitErrorRate.getDt().getTime(), uc.getUser().getLanguage()));
                        // sb.append(DateUtils.FULL_FORMAT.format(usBitErrorRate.getDt()));
                        sb.append("</Td></tr>");
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showCmtsBerRate() {
        return SUCCESS;
    }

    /**
     * 获取CCMTS的上行信道信噪比
     * 
     * @return
     */
    public String loadCmtsBerRate() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        paramMap.put("sort", sort);
        paramMap.put("dir", dir);
        if (nmName != null && !"".equals(nmName)) {
            if (nmName.contains("_")) {
                nmName = nmName.replace("_", "\\_");
            }
        }
        paramMap.put("name", nmName);
        paramMap.put("deviceType", deviceType);
        JSONObject ret = new JSONObject();
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        List<UsBitErrorRate> usBerRates = cmcPerfService.getChannelBerRate(paramMap);
        for (UsBitErrorRate usBerRate : usBerRates) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(usBerRate.getMacAddress(), macRule);
            String cmmPortName = makePortName(usBerRate.getChannelIndex().longValue(),
                    new Long(usBerRate.getCmcType()), usBerRate.getIfDescr(), usBerRate.getIfType());
            usBerRate.setMacAddress(formatedMac);
            usBerRate.setCmcPortName(cmmPortName);
        }
        ret.put("data", usBerRates);
        ret.put("rowCount", cmcPerfService.getChannelBerRateCount(paramMap));
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * CCMTS上CM flap的ins异常次数
     * @return
     */
    public String getTopPortletFlapInsGrowthLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        @SuppressWarnings("unused")
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());

        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopPortletFlapInsGrowthLoading'><thead><tr><th width='36'>")
                .append(ResourcesUtil.getString("WorkBench.Ranking")).append("</th><th>")
                .append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"))
                .append("</th><th width='80'>")
                .append(ResourcesUtil.getString("CMC.label.entitytype"))
                .append("</th><Th width='69'>")
                .append("CM MAC")
                //.append("</th><th class='wordBreak'>").append(ResourcesUtil.getString("CM.correcteds"))
                .append("</th><th class='wordBreak'>")
                .append("<img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>")
                .append(ResourcesUtil.getString("cmc.flap.failOnlineCounter")).append("</th><th width='126'>")
                .append(ResourcesUtil.getString("WorkBench.fromUpdateTime")).append("</th></tr></thead><tobdy>");
        try {
            List<CmFlap> cmflapList = cmcPerfService.getTopPortletFlapInsGrowthLoading(paramMap);
            if (cmflapList != null) {
                int i = 0;
                for (CmFlap cmFlap : cmflapList) {
                    //if (cmFlap.getCmcType() != null) {
                    //String formatedMac =  cmFlap.getTopCmFlagMacAddrString();//MacUtils.convertMacToDisplayFormat(usBitErrorRate.getMacAddress(), macRule);
                    //usBitErrorRate.setMacAddress(formatedMac);
                    if (i >= 10) {
                        sb.append("<tr><td><div class='topClsBg'>");
                        if (i >= 100) {
                            sb.append("<div>");
                            sb.append(++i);
                            sb.append("</div>");
                        } else {
                            sb.append(++i);
                        }
                        sb.append("</div></td>");
                    } else {
                        sb.append("<tr><Td><div class=topCls");
                        sb.append(++i);
                        sb.append("></div></Td>");
                    }
                    sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                    sb.append(cmFlap.getCmcId());
                    sb.append(",'");
                    sb.append(cmFlap.getCmcMac());
                    sb.append("','");
                    sb.append(cmFlap.getCmcName());
                    sb.append("');\">");
                    sb.append(cmFlap.getCmcName());
                    sb.append("</a></Td>");

                    sb.append("<td class='txtCenter'>");
                    sb.append(entityTypeService.getEntityType(Long.valueOf(cmFlap.getCmcType())).getDisplayName());
                    sb.append("</td>");
                    sb.append("<Td class='txtCenter'>");
                    sb.append(cmFlap.getTopCmFlapMacAddr());
                    sb.append("</Td>");
                    sb.append("<Td class='txtCenter'>");
                    sb.append(cmFlap.getIncreaseInsNum());
                    sb.append("</Td>");
                    sb.append("<Td  class='txtCenter'>");
                    sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - cmFlap.getDt().getTime(), uc
                            .getUser().getLanguage()));
                    sb.append("</Td></tr>");
                }
            }
            //}
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showCmFlapIns() {
        return SUCCESS;
    }

    public String loadCmFlapIns() {
        JSONObject ret = new JSONObject();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        paramMap.put("sort", sort);
        paramMap.put("dir", dir);
        if (nmName != null && !"".equals(nmName)) {
            if (nmName.contains("_")) {
                nmName = nmName.replace("_", "\\_");
            }
        }
        paramMap.put("name", nmName);
        paramMap.put("deviceType", deviceType);
        List<CmFlap> cmflapList = cmcPerfService.loadCmFlapIns(paramMap);
        ret.put("data", cmflapList);
        ret.put("rowCount", cmcPerfService.getCmFlapInsCount(paramMap));
        writeDataToAjax(ret);
        return NONE;
    }

    public String showNoiseRate() {
        return SUCCESS;
    }

    /**
     * 获取CCMTS的上行信道信噪比
     * 
     * @return
     */
    public String loadNoiseRate() {
        JSONObject ret = new JSONObject();
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        if (nmName != null && !"".equals(nmName)) {
            if (nmName.contains("_")) {
                nmName = nmName.replace("_", "\\_");
            }
        }
        List<SingleNoise> singleNoises = cmcPerfService.getNoiseRate(nmName, deviceType, this.getSort(), this.getDir(),
                start, limit);
        for (SingleNoise singleNoise : singleNoises) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(singleNoise.getMacAddress(), macRule);
            String cmmPortName = makePortName(singleNoise.getIfIndex().longValue(), new Long(singleNoise.getCmcType()),
                    singleNoise.getIfDescr(), singleNoise.getIfType());
            Entity entity = entityService.getEntity(singleNoise.getEntityId());
            singleNoise.setMacAddress(formatedMac);
            singleNoise.setCmcPortName(cmmPortName); 
            singleNoise.setLocation(entity.getLocation());
            singleNoise.setNote(entity.getNote());
            //start -- added by wubo  2017.01.23
            if(entityTypeService.isCcmts(singleNoise.getTypeId())){// 只用判断CC版本是否支持频谱展示
                Boolean spectrum = deviceVersionService.isFunctionSupported(singleNoise.getCmcId(), "spectrumII");
                singleNoise.setSpectrum(spectrum);
            }            
            //end -- added by wubo  2017.01.23
        }
        ret.put("data", singleNoises);
        ret.put("rowCount", cmcPerfService.getNoiseRateCount(nmName, deviceType));
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * CCMTS设备低信燥比Top10
     * 
     * @return String
     */
    public String getTopLowNoiseLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopLowNoiseLoading'><thead><tr><th width='36'>")
                .append(ResourcesUtil.getString("WorkBench.Ranking")).append("</th><th>")
                .append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"))
                .append("</th><th width='80'>").append(ResourcesUtil.getString("CMC.label.entitytype"))
                .append("</th><Th style=\"text-align:center\" width='69'>")
                .append(ResourcesUtil.getString("WorkBench.PortName")).append("</th><th width='80'>")
                .append("<img src=\"../images/moreup.gif\" border=0 align=absmiddle hspace=5>")
                .append(ResourcesUtil.getString("CHANNEL.snr")).append("</th><th width='126'>")
                .append(ResourcesUtil.getString("WorkBench.fromUpdateTime")).append("</th></tr></thead><tbody>");
        try {
            List<SingleNoise> singleNoises = cmcPerfService.getTopLowNoiseLoading(paramMap); 
            if (singleNoises != null) {
                int i = 0;
                for (SingleNoise singleNoise : singleNoises) {
                    Long typeId = singleNoise.getTypeId();
                    if (singleNoise.getCmcType() != null) {
                        if (entityTypeService.isCmts(typeId)) {
                            singleNoise.setCmcPortName(singleNoise.getIfName());
                        }
                        String formatedMac = MacUtils.convertMacToDisplayFormat(singleNoise.getMacAddress(), macRule);
                        singleNoise.setMacAddress(formatedMac);
                        String cmmPortName = makePortName(singleNoise.getIfIndex().longValue(),
                                new Long(singleNoise.getCmcType()), singleNoise.getIfDescr(), singleNoise.getIfType());
                        if (i >= 10) {
                            sb.append("<tr><td><div class='topClsBg'>");
                            if (i >= 100) {
                                sb.append("<div>");
                                sb.append(++i);
                                sb.append("</div>");
                            } else {
                                sb.append(++i);
                            }
                            sb.append("</div></td>");
                        } else {
                            sb.append("<tr><Td><div class=topCls");
                            sb.append(++i);
                            sb.append("></div></Td>");
                        }

                        sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                        sb.append(singleNoise.getCmcId());
                        sb.append(",'");
                        sb.append(singleNoise.getMacAddress());
                        sb.append("','");
                        sb.append(singleNoise.getCmcName());
                        sb.append("','");
                        sb.append(singleNoise.getTypeId());
                        sb.append("');\">");
                        sb.append(singleNoise.getCmcName());
                        sb.append("</a></Td>");

                        sb.append("<td class='txtCenter'>");
                        sb.append(entityTypeService.getEntityType(Long.valueOf(singleNoise.getCmcType()))
                                .getDisplayName());
                        sb.append("</td>");

                        sb.append("<Td  class='txtCenter'><a class=my-link href=\"javascript:showChannel(");
                        sb.append(singleNoise.getCmcPortId());
                        sb.append(",'");
                        sb.append(singleNoise.getCmcId());
                        sb.append("','");
                        sb.append(cmmPortName);
                        sb.append("','");
                        sb.append(singleNoise.getIfIndex());
                        sb.append("','");
                        sb.append(singleNoise.getChannelTypeString());
                        sb.append("');\">");
                        sb.append(cmmPortName);
                        sb.append("</Td>");
                        sb.append("<Td  class='txtCenter'>");
                        sb.append(singleNoise.getNoiseString());
                        sb.append("</Td>");
                        sb.append("<Td  class='txtCenter'>");
                        sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                - singleNoise.getDt().getTime(), uc.getUser().getLanguage()));
                        // sb.append(DateUtils.FULL_FORMAT.format(singleNoise.getDt()));
                        sb.append("</Td></tr>");
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * CCMTS设备memory
     * 
     * @return String
     */
    public String getTopCcmtsMemLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        sb.append("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">")
                .append(ResourcesUtil.getString("WorkBench.Ranking")).append("</th><th>")
                .append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"))
                .append("</th><th style=\"text-align:center\">")
                .append(ResourcesUtil.getString("CMC.label.entitytype")).append("</th><Th style=\"text-align:right\">")
                .append(ResourcesUtil.getString("WorkBench.MacAddress")).append("</th><th style=\"text-align:right\">")
                .append("<img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>")
                .append(ResourcesUtil.getString("WorkBench.MemRanking")).append("</th><th style=\"text-align:right\">")
                .append(ResourcesUtil.getString("WorkBench.fromUpdateTime")).append("</th></tr>");
        try {
            List<Cmc> cmcSnaps = cmcPerfService.getNetworkCcmtsDeviceLoadingTop("mem");
            if (cmcSnaps != null) {
                int i = 0;
                for (Cmc snap : cmcSnaps) {
                    sb.append("<tr><Td><div class=topCls");
                    sb.append(++i);
                    sb.append("></div></Td>");
                    sb.append("<Td><a class=my-link href=\"javascript:showCcmtsPortal(");
                    sb.append(snap.getCmcId());
                    sb.append(",'");
                    sb.append(snap.getTopCcmtsSysMacAddr());
                    sb.append("','");
                    sb.append(snap.getTopCcmtsSysName());
                    sb.append("');\">");
                    sb.append(snap.getTopCcmtsSysName());
                    sb.append("</a></Td>");

                    sb.append("<td style=\"text-align:center\">");
                    sb.append(entityTypeService.getEntityType(Long.valueOf(snap.getCmcDeviceStyle())).getDisplayName());
                    sb.append("</td>");

                    sb.append("<Td style=\"text-align:center\">");
                    sb.append(snap.getTopCcmtsSysMacAddr());
                    sb.append("</Td>");
                    sb.append("<Td align=center>");
                    sb.append("<div style=\"padding-top:3px;align=absmiddle\">")
                            .append("<div style=\"float:left;width:90px;height:10px;border:1px solid yellow;")
                            .append("background-color:gray;font-size:1px;\">").append("<div style=\"float:left;width:")
                            .append(snap.getTopCcmtsSysRAMRatiotoString())
                            .append(";height:10px;font-size:1px;background-color:red\"></div></div>")
                            .append("<div style=\"float:left;margin-left:-50;margin-top:-1px;color:white\">")
                            .append(snap.getTopCcmtsSysRAMRatiotoString()).append("</div></div>");
                    sb.append("</Td><Td align=right>");
                    // sb.append(DateUtils.FULL_FORMAT.format(snap.getSnapTime()));
                    sb.append(formatDuring(System.currentTimeMillis() - snap.getSnapTime().getTime()));
                    sb.append("</Td></tr>");
                }
            }

        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * CC上行通道用户数 TOP
     * 
     * @return String
     */
    public String getTopUpChnUsersLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        //add by fanzidong,需要在展示前格式化MAC地址
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "up");
        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopUpChnUsersLoading'><thead><tr><th width='32'>")
                .append(ResourcesUtil.getString("WorkBench.Ranking")).append("</th><th>")
                .append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"))
                .append("</th><th width='80'>").append(ResourcesUtil.getString("CMC.label.entitytype"))
                .append("</th><Th width='69'>").append(ResourcesUtil.getString("WorkBench.PortName"))
                .append("</th><th  class='wordBreak'>")
                .append("<img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>")
                .append(ResourcesUtil.getString("WorkBench.AllUserNum")).append("</th><Th width='70'>")
                .append(ResourcesUtil.getString("WorkBench.OfflionUserNum")).append("</th><th width='126'>")
                .append(ResourcesUtil.getString("WorkBench.fromUpdateTime")).append("</th></tr></thead><tbody>");
        try {
            List<ChannelCmNum> cmcChnlUsersSnaps = cmcPerfService.getNetworkCcmtsDeviceUsersLoadingTop(paramMap);
            if (cmcChnlUsersSnaps != null) {
                int i = 0;
                for (ChannelCmNum snap : cmcChnlUsersSnaps) {
                    Long typeId = snap.getCmcType().longValue();
                    String cmtsPortName = "";
                    if (typeId != null) {
                        if (entityTypeService.isCmts(typeId)) {
                            cmtsPortName = snap.getIfName();
                        }
                        String cmcPortName = makePortName(snap.getChannelIndex(), snap.getCmcType().longValue(),
                                snap.getIfDescr(), snap.getIfType());
                        String formatedMac = MacUtils.convertMacToDisplayFormat(snap.getCmcMac(), macRule);
                        snap.setCmcMac(formatedMac);
                        if (i >= 10) {
                            sb.append("<tr><td><div class='topClsBg'>");
                            if (i >= 100) {
                                sb.append("<div>");
                                sb.append(++i);
                                sb.append("</div>");
                            } else {
                                sb.append(++i);
                            }
                            sb.append("</div></td>");
                        } else {
                            sb.append("<tr><Td><div class=topCls");
                            sb.append(++i);
                            sb.append("></div></Td>");
                        }
                        sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                        sb.append(snap.getCmcId());
                        sb.append(",'");
                        sb.append(snap.getCmcMac());
                        sb.append("','");
                        sb.append(snap.getCmcName());
                        sb.append("','");
                        sb.append(typeId);
                        sb.append("');\">");
                        sb.append(snap.getCmcName());
                        sb.append("</a></Td>");

                        sb.append("<td>");
                        sb.append(entityTypeService.getEntityType(Long.valueOf(snap.getCmcType())).getDisplayName());
                        sb.append("</td>");

                        sb.append("<Td><a class=my-link href=\"javascript:showChannel(");
                        sb.append(snap.getCmcPortId());
                        sb.append(",");
                        sb.append(snap.getCmcId());
                        sb.append(",'");
                        if (entityTypeService.isCmts(typeId)) {
                            sb.append(cmtsPortName);
                        } else {
                            sb.append(cmcPortName);
                        }
                        sb.append("','");
                        sb.append(snap.getChannelIndex());
                        sb.append("','");
                        sb.append(snap.getChannelTypeString());
                        sb.append("');\">");
                        if (entityTypeService.isCmts(typeId)) {
                            sb.append(cmtsPortName);
                        } else {
                            sb.append(snap.getCmcPortName());
                        }
                        sb.append("</a></Td>");
                        sb.append("<Td class='txtCenter'><a class=my-link href=\"javascript:showPortInfo(");
                        sb.append(snap.getCmcId());
                        sb.append(",");
                        sb.append(snap.getChannelIndex());
                        sb.append(",'");
                        sb.append("cmNumTotal");
                        sb.append("');\">");
                        sb.append(snap.getCmNumTotal());
                        sb.append("</a></Td>");
                        sb.append("<Td class='txtCenter'><a class=my-link href=\"javascript:showPortInfo(");
                        sb.append(snap.getCmcId());
                        sb.append(",");
                        sb.append(snap.getChannelIndex());
                        sb.append(",'");
                        sb.append("cmNumOffline");
                        sb.append("');\">");
                        sb.append(snap.getCmNumOffline());
                        sb.append("</Td><Td  class='txtCenter'>");
                        sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - snap.getDt().getTime(), uc
                                .getUser().getLanguage()));
                        sb.append("</Td></tr>");
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * 下行通道 用户数 TOP
     * 
     * @return String
     */
    public String getTopDownChnUsersLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        //add by fanzidong,需要在展示前格式化MAC地址
        String macRule = uc.getMacDisplayStyle();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "down");
        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopDownChnUsersLoading'><thead><tr><th width='36'>")
                .append(ResourcesUtil.getString("WorkBench.Ranking")).append("</th><th>")
                .append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"))
                .append("</th><th width='80'>").append(ResourcesUtil.getString("CMC.label.entitytype"))
                .append("</th><Th width='69'>").append(ResourcesUtil.getString("WorkBench.PortName"))
                .append("</th><th  class='wordBreak'>")
                .append("<img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>")
                .append(ResourcesUtil.getString("WorkBench.AllUserNum")).append("</th><Th width='70'>")
                .append(ResourcesUtil.getString("WorkBench.OfflionUserNum")).append("</th><th width='126'>")
                .append(ResourcesUtil.getString("WorkBench.fromUpdateTime")).append("</th></tr></thead><tbody>");
        try {
            List<ChannelCmNum> cmcChnlUsersSnaps = cmcPerfService.getNetworkCcmtsDeviceUsersLoadingTop(paramMap);
            if (cmcChnlUsersSnaps != null) {
                int i = 0;
                for (ChannelCmNum snap : cmcChnlUsersSnaps) {
                    Long typeId = snap.getCmcType().longValue();
                    String cmtsPortName = "";
                    if (typeId != null) {
                        if (entityTypeService.isCmts(typeId)) {
                            cmtsPortName = snap.getIfName();
                        }
                        String cmcPortName = makePortName(snap.getChannelIndex(), snap.getCmcType().longValue(),
                                snap.getIfDescr(), snap.getIfType());
                        String formatedMac = MacUtils.convertMacToDisplayFormat(snap.getCmcMac(), macRule);
                        snap.setCmcMac(formatedMac);
                        if (i >= 10) {
                            sb.append("<tr><td><div class='topClsBg'>");
                            if (i >= 100) {
                                sb.append("<div>");
                                sb.append(++i);
                                sb.append("</div>");
                            } else {
                                sb.append(++i);
                            }
                            sb.append("</div></td>");
                        } else {
                            sb.append("<tr><Td><div class=topCls");
                            sb.append(++i);
                            sb.append("></div></Td>");
                        }
                        sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                        sb.append(snap.getCmcId());
                        sb.append(",'");
                        sb.append(snap.getCmcMac());
                        sb.append("','");
                        sb.append(snap.getCmcName());
                        sb.append("','");
                        sb.append(typeId);
                        sb.append("');\">");
                        sb.append(snap.getCmcName());
                        sb.append("</a></Td>");

                        sb.append("<td align='center'>");
                        sb.append(entityTypeService.getEntityType(Long.valueOf(snap.getCmcType())).getDisplayName());
                        sb.append("</td>");

                        sb.append("<Td align='center'><a class=my-link href=\"javascript:showChannel(");
                        sb.append(snap.getCmcPortId());
                        sb.append(",");
                        sb.append(snap.getCmcId());
                        sb.append(",'");
                        if (entityTypeService.isCmts(typeId)) {
                            sb.append(cmtsPortName);
                        } else {
                            sb.append(cmcPortName);
                        }
                        sb.append("','");
                        sb.append(snap.getChannelIndex());
                        sb.append("','");
                        sb.append(snap.getChannelTypeString());
                        sb.append("');\">");
                        if (entityTypeService.isCmts(typeId)) {
                            sb.append(cmtsPortName);
                        } else {
                            sb.append(snap.getCmcPortName());
                        }
                        sb.append("</a></Td>");
                        sb.append("<Td align=center><a class=my-link href=\"javascript:showPortInfo(");
                        sb.append(snap.getCmcId());
                        sb.append(",");
                        sb.append(snap.getChannelIndex());
                        sb.append(",'");
                        sb.append("cmNumTotal");
                        sb.append("');\">");
                        sb.append(snap.getCmNumTotal());
                        sb.append("</a></Td>");
                        sb.append("<Td align=center><a class=my-link href=\"javascript:showPortInfo(");
                        sb.append(snap.getCmcId());
                        sb.append(",");
                        sb.append(snap.getChannelIndex());
                        sb.append(",'");
                        sb.append("cmNumOffline");
                        sb.append("');\">");
                        sb.append(snap.getCmNumOffline());
                        sb.append("</Td><Td align=center>");
                        sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis() - snap.getDt().getTime(), uc
                                .getUser().getLanguage()));
                        sb.append("</Td></tr>");
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    /**
     * CC设备用户数 TOP
     * 
     * @return String
     */
    public String getTopCcUsersLoading() {
        // TODO 此方法重构过 需要重新测试
        StringBuilder sb = new StringBuilder();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("start", 0);
        paramMap.put("limit", uc.getTopNumber());
        paramMap.put("target", "");
        sb.append(
                "<table class='dataTable zebra noWrap' width='100%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' rules='all' id='getTopCcUsersLoading'><thead><tr><th width='36'>")
                .append(ResourcesUtil.getString("WorkBench.Ranking")).append("</th><th>")
                .append(ResourcesUtil.getString("Config.oltConfigFileImported.deviceName"))
                .append("</th><th width='80'>").append(ResourcesUtil.getString("CMC.label.entitytype"))
                .append("</th><Th width='106'>").append(ResourcesUtil.getString("WorkBench.MacAddress"))
                .append("</th><th  class='wordBreak'>")
                .append("<img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>")
                .append(ResourcesUtil.getString("WorkBench.deviceUserNum")).append("</th><th width='126'>")
                .append(ResourcesUtil.getString("WorkBench.fromUpdateTime")).append("</th></tr></thead><tbody>");
        try {
            List<ChannelCmNum> cmcChnlUsersSnaps = cmcPerfService.getNetworkCcmtsDeviceUsersLoadingTop(paramMap);
            if (cmcChnlUsersSnaps != null) {
                //add by fanzidong,需要在展示前格式化MAC地址
                String macRule = uc.getMacDisplayStyle();
                int i = 0;
                for (ChannelCmNum snap : cmcChnlUsersSnaps) {
                    if (snap.getCmcType() != null) {
                        Long typeId = snap.getCmcType().longValue();
                        if (typeId != null) {
                            String formatedMac = MacUtils.convertMacToDisplayFormat(snap.getCmcMac(), macRule);
                            if (formatedMac == null) {
                                formatedMac = "-";
                            }
                            snap.setCmcMac(formatedMac);
                            if (i >= 10) {
                                sb.append("<tr><td><div class='topClsBg'>");
                                if (i >= 100) {
                                    sb.append("<div>");
                                    sb.append(++i);
                                    sb.append("</div>");
                                } else {
                                    sb.append(++i);
                                }
                                sb.append("</div></td>");
                            } else {
                                sb.append("<tr><Td><div class=topCls");
                                sb.append(++i);
                                sb.append("></div></Td>");
                            }
                            sb.append("<Td class='txtLeft wordBreak'><a class=my-link href=\"javascript:showCcmtsPortal(");
                            sb.append(snap.getCmcId());
                            sb.append(",'");
                            sb.append(snap.getCmcMac());
                            sb.append("','");
                            sb.append(snap.getCmcName());
                            sb.append("','");
                            sb.append(typeId);
                            sb.append("');\">");
                            sb.append(snap.getCmcName());
                            sb.append("</a></Td>");

                            sb.append("<td class='txtCenter'>");
                            sb.append(entityTypeService.getEntityType(Long.valueOf(snap.getCmcType())).getDisplayName());
                            sb.append("</td>");

                            sb.append("<Td align=center>");
                            sb.append(snap.getCmcMac());
                            sb.append("</Td>");
                            sb.append("<Td align=center>");
                            sb.append(snap.getCmNumTotal());
                            sb.append("</Td><Td  class='txtCenter'>");
                            if (snap.getDt() != null) {
                                sb.append(DateUtils.getTimeDesInObscure(System.currentTimeMillis()
                                        - snap.getDt().getTime(), uc.getUser().getLanguage()));
                            } else {
                                sb.append("--");
                            }
                            sb.append("</Td></tr>");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("", ex);
        } finally {
            sb.append("</tbody></table>");
        }
        writeDataToAjax(sb.toString());
        return NONE;
    }

    public String showCmtsUser() {
        return SUCCESS;
    }

    public String showCmtsUpChannelUser() {
        return SUCCESS;
    }

    public String showCmtsDownChannelUser() {
        return SUCCESS;
    }

    /**
     * 获取CCMTS的上行信道用户数
     * 
     * @return
     */
    public String loadCmtsUsers() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        JSONObject ret = new JSONObject();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (nmName != null && !"".equals(nmName)) {
            if (nmName.contains("_")) {
                nmName = nmName.replace("_", "\\_");
            }
        }
        paramMap.put("name", nmName);
        paramMap.put("deviceType", deviceType);
        paramMap.put("start", start);
        paramMap.put("sort", sort);
        paramMap.put("dir", dir);
        paramMap.put("limit", limit);
        paramMap.put("target", direction);
        String macRule = uc.getMacDisplayStyle();
        List<ChannelCmNum> cmcUsers = cmcPerfService.getCcmtsDeviceUsersList(paramMap);
        for (ChannelCmNum channelNum : cmcUsers) {
            String macAddr = MacUtils.convertMacToDisplayFormat(channelNum.getCmcMac(), macRule);
            channelNum.setCmcMac(macAddr);
        }
        ret.put("data", cmcUsers);
        ret.put("rowCount", cmcPerfService.getCcmtsDeviceUsersCount(paramMap));
        writeDataToAjax(ret);
        return NONE;
    }

    private List<Map<String, Object>> getSnrPerfData(final Timestamp startTime, final Timestamp endTime) {
        // 获取曲线条数
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 刷新曲线图数据
        for (int ifIndex : cmcPerfService.getIfIndexByCmcId(cmcId, 129)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cmcId", cmcId);
            map.put("ifIndex", ifIndex);
            map.put("name", "Port " + ((ifIndex >> 8) & 0xff));
            // 获取最近采集的50个数据点
            map.put("data", singleNoiseToTimePoint(cmcPerfService.getSnrData(map, startTime, endTime)));
            map.remove("cmcId");
            map.remove("ifIndex");
            list.add(map);
        }
        return list;
    }

    /**
     * 将信噪比数据转换为页面显示的点
     * 
     * @param list
     *            List<SingleNoise>
     * @return List<FloatTimePoint>
     */
    private List<FloatTimePoint> singleNoiseToTimePoint(List<SingleNoise> list) {
        // TODO 暂时使用在Action中处理的方式，以后进行优化
        List<FloatTimePoint> tps = new ArrayList<FloatTimePoint>();
        for (SingleNoise cu : list) {
            FloatTimePoint tp = new FloatTimePoint();
            tp.setX(cu.getX());
            tp.setY(cu.getY());
            tps.add(tp);
        }
        return tps;
    }

    public ViewerParam getPerfViewerParam(String perfType) {
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

    private Viewer getPerfViewer(String perfType) {
        ViewerParam viewerParam = getPerfViewerParam(perfType);
        return performanceService.getViewerByType(viewerParam);
    }

    private Viewer getPerfViewer() {
        ViewerParam viewerParam = getPerfViewerParam();
        String[] perfTypes = perfType.split(" ");
        viewerParam.setPerfType(perfTypes[0]);
        return performanceService.getViewerByType(viewerParam);
    }

    /**
     * 将毫秒数转换为x天x小时x分x秒形式
     * 
     * @param time
     * @return
     */
    private String formatDuring(long time) {
        StringBuilder duringTime = new StringBuilder();
        long days = time / (1000 * 60 * 60 * 24);
        long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (time % (1000 * 60)) / 1000;
        if (days > 0) {
            duringTime.append(days + ResourcesUtil.getString("cmc", "CMC.label.day"));
        }
        if (hours > 0) {
            duringTime.append(hours + ResourcesUtil.getString("cmc", "CMC.label.hour"));
        }
        if (minutes > 0) {
            duringTime.append(minutes + ResourcesUtil.getString("cmc", "CMC.label.minutes"));
        }
        if (seconds > 0) {
            duringTime.append(seconds + ResourcesUtil.getString("cmc", "CMC.label.seconds"));
        }
        return duringTime.toString();
    }

    /**
     * 组装channelName
     * 
     * @param channelIndex
     *            Long
     * @return String
     */
    private String makePortName(Long channelIndex, Long typeId, String ifDescr, Long ifType) {
        if (entityTypeService.isCmts(typeId)) {
            int channelType = CmcUtil.getCmtsChannelType(typeId, ifType, entityTypeService);
            if (channelType > 0) {
                return CmcUtil.getCmtsDownChannelIndex(typeId, ifDescr, entityTypeService);
            } else {
                return CmcUtil.getCmtsUpChannelIndex(typeId, ifDescr, entityTypeService);
            }
        } else {
            String type = CmcIndexUtils.getChannelType(channelIndex) == 0 ? "US" : "DS";
            Long slotNo = CmcIndexUtils.getSlotNo(channelIndex);
            Long ponNo = CmcIndexUtils.getPonNo(channelIndex);
            Long cmcNo = CmcIndexUtils.getCmcId(channelIndex);
            Long chNo = CmcIndexUtils.getChannelId(channelIndex);
            StringBuilder sb = new StringBuilder();
            sb.append(type).append(slotNo).append(Symbol.SLASH).append(ponNo).append(Symbol.SLASH).append(cmcNo)
                    .append(Symbol.SLASH).append(chNo);
            return sb.toString();
        }

    }

    /**
     * @return the perfdata
     */
    public JSONArray getPerfdata() {
        return perfdata;
    }

    /**
     * @param perfdata
     *            the perfdata to set
     */
    public void setPerfdata(JSONArray perfdata) {
        this.perfdata = perfdata;
    }

    /**
     * @return the cmcPerfService
     */
    public CmcPerfService getCmcPerfService() {
        return cmcPerfService;
    }

    /**
     * @param cmcPerfService
     *            the cmcPerfService to set
     */
    public void setCmcPerfService(CmcPerfService cmcPerfService) {
        this.cmcPerfService = cmcPerfService;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return the period
     */
    public int getPeriod() {
        return period;
    }

    /**
     * @param period
     *            the period to set
     */
    public void setPeriod(int period) {
        this.period = period;
    }

    /**
     * @return the perfType
     */
    public String getPerfType() {
        return perfType;
    }

    /**
     * @param perfType
     *            the perfType to set
     */
    public void setPerfType(String perfType) {
        this.perfType = perfType;
    }

    /**
     * @return the utilizationData
     */
    public JSONArray getUtilizationData() {
        return utilizationData;
    }

    /**
     * @param utilizationData
     *            the utilizationData to set
     */
    public void setUtilizationData(JSONArray utilizationData) {
        this.utilizationData = utilizationData;
    }

    /**
     * @return the supportPerf
     */
    public JSONObject getSupportPerf() {
        return supportPerf;
    }

    /**
     * @param supportPerf
     *            the supportPerf to set
     */
    public void setSupportPerf(JSONObject supportPerf) {
        this.supportPerf = supportPerf;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the perfCycle
     */
    public Integer getPerfCycle() {
        return perfCycle;
    }

    /**
     * @param perfCycle
     *            the perfCycle to set
     */
    public void setPerfCycle(Integer perfCycle) {
        this.perfCycle = perfCycle;
    }

    /**
     * @return the showInterval
     */
    public Integer getShowInterval() {
        return showInterval;
    }

    /**
     * @param showInterval
     *            the showInterval to set
     */
    public void setShowInterval(Integer showInterval) {
        this.showInterval = showInterval;
    }

    /**
     * @return the showTime
     */
    public Long getShowTime() {
        return showTime;
    }

    /**
     * @param showTime
     *            the showTime to set
     */
    public void setShowTime(Long showTime) {
        this.showTime = showTime;
    }

    /**
     * @return the snrPeriod
     */
    public int getSnrPeriod() {
        return snrPeriod;
    }

    /**
     * @param snrPeriod
     *            the snrPeriod to set
     */
    public void setSnrPeriod(int snrPeriod) {
        this.snrPeriod = snrPeriod;
    }

    /**
     * @return the utilizationPeriod
     */
    public int getUtilizationPeriod() {
        return utilizationPeriod;
    }

    /**
     * @param utilizationPeriod
     *            the utilizationPeriod to set
     */
    public void setUtilizationPeriod(int utilizationPeriod) {
        this.utilizationPeriod = utilizationPeriod;
    }

    /**
     * @return the snrStatus
     */
    public boolean isSnrStatus() {
        return snrStatus;
    }

    /**
     * @param snrStatus
     *            the snrStatus to set
     */
    public void setSnrStatus(boolean snrStatus) {
        this.snrStatus = snrStatus;
    }

    /**
     * @return the utilizationStatus
     */
    public boolean isUtilizationStatus() {
        return utilizationStatus;
    }

    /**
     * @return the channelCmNumStatic
     */
    public boolean isChannelCmNumStatic() {
        return channelCmNumStatic;
    }

    /**
     * @param channelCmNumStatic
     *            the channelCmNumStatic to set
     */
    public void setChannelCmNumStatic(boolean channelCmNumStatic) {
        this.channelCmNumStatic = channelCmNumStatic;
    }

    /**
     * @return the channelCmNumStaticPeriod
     */
    public int getChannelCmNumStaticPeriod() {
        return channelCmNumStaticPeriod;
    }

    /**
     * @param channelCmNumStaticPeriod
     *            the channelCmNumStaticPeriod to set
     */
    public void setChannelCmNumStaticPeriod(int channelCmNumStaticPeriod) {
        this.channelCmNumStaticPeriod = channelCmNumStaticPeriod;
    }

    /**
     * @param utilizationStatus
     *            the utilizationStatus to set
     */
    public void setUtilizationStatus(boolean utilizationStatus) {
        this.utilizationStatus = utilizationStatus;
    }

    /**
     * @return the systemStatus
     */
    public boolean isSystemStatus() {
        return systemStatus;
    }

    /**
     * @param systemStatus
     *            the systemStatus to set
     */
    public void setSystemStatus(boolean systemStatus) {
        this.systemStatus = systemStatus;
    }

    /**
     * @return the systemPeriod
     */
    public int getSystemPeriod() {
        return systemPeriod;
    }

    /**
     * @param systemPeriod
     *            the systemPeriod to set
     */
    public void setSystemPeriod(int systemPeriod) {
        this.systemPeriod = systemPeriod;
    }

    /**
     * @return the usBitErrorRate
     */
    public boolean isUsBitErrorRate() {
        return usBitErrorRate;
    }

    /**
     * @param usBitErrorRate
     *            the usBitErrorRate to set
     */
    public void setUsBitErrorRate(boolean usBitErrorRate) {
        this.usBitErrorRate = usBitErrorRate;
    }

    /**
     * @return the usBitErrorRatePeriod
     */
    public int getUsBitErrorRatePeriod() {
        return usBitErrorRatePeriod;
    }

    /**
     * @param usBitErrorRatePeriod
     *            the usBitErrorRatePeriod to set
     */
    public void setUsBitErrorRatePeriod(int usBitErrorRatePeriod) {
        this.usBitErrorRatePeriod = usBitErrorRatePeriod;
    }

    public JSONObject getChartParam() {
        return chartParam;
    }

    public void setChartParam(JSONObject chartParam) {
        this.chartParam = chartParam;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    @SuppressWarnings("rawtypes")
    public PerformanceService getPerformanceService() {
        return performanceService;
    }

    @SuppressWarnings("rawtypes")
    public void setPerformanceService(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public JSONObject getViewerParam() {
        return viewerParam;
    }

    public void setViewerParam(JSONObject viewerParam) {
        this.viewerParam = viewerParam;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Boolean getShowLabel() {
        return showLabel;
    }

    public void setShowLabel(Boolean showLabel) {
        this.showLabel = showLabel;
    }

    public JSONArray getCmcChanelNames() {
        return cmcChanelNames;
    }

    public void setCmcChanelNames(JSONArray cmcChanelNames) {
        this.cmcChanelNames = cmcChanelNames;
    }

    public boolean isPerfCollect() {
        return perfCollect;
    }

    public void setPerfCollect(boolean perfCollect) {
        this.perfCollect = perfCollect;
    }

    public int getPerfCollectPeriod() {
        return perfCollectPeriod;
    }

    public void setPerfCollectPeriod(int perfCollectPeriod) {
        this.perfCollectPeriod = perfCollectPeriod;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public Double getYmin() {
        return ymin;
    }

    public void setYmin(Double ymin) {
        this.ymin = ymin;
    }

    public Double getYmax() {
        return ymax;
    }

    public void setYmax(Double ymax) {
        this.ymax = ymax;
    }

    public String getYtitle() {
        return ytitle;
    }

    public void setYtitle(String ytitle) {
        this.ytitle = ytitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSpeedPeriod() {
        return speedPeriod;
    }

    public void setSpeedPeriod(int speedPeriod) {
        this.speedPeriod = speedPeriod;
    }

    public String getNmName() {
        return nmName;
    }

    public void setNmName(String nmName) {
        this.nmName = nmName;
    }

    public String getConnectPerson() {
        return connectPerson;
    }

    public void setConnectPerson(String connectPerson) {
        this.connectPerson = connectPerson;
    }

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public Integer getCmcType() {
        return cmcType;
    }

    public void setCmcType(Integer cmcType) {
        this.cmcType = cmcType;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public Integer getUpLinkFlow() {
        return upLinkFlow;
    }

    public void setUpLinkFlow(Integer upLinkFlow) {
        this.upLinkFlow = upLinkFlow;
    }

    public Integer getMacFlow() {
        return macFlow;
    }

    public void setMacFlow(Integer macFlow) {
        this.macFlow = macFlow;
    }

    public Integer getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(Integer cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public Integer getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Integer memUsed) {
        this.memUsed = memUsed;
    }

    public Integer getFlashUsed() {
        return flashUsed;
    }

    public void setFlashUsed(Integer flashUsed) {
        this.flashUsed = flashUsed;
    }

    public Integer getMemTemp() {
        return memTemp;
    }

    public void setMemTemp(Integer memTemp) {
        this.memTemp = memTemp;
    }

    public Integer getRfTemp() {
        return rfTemp;
    }

    public void setRfTemp(Integer rfTemp) {
        this.rfTemp = rfTemp;
    }

    public Integer getUpTemp() {
        return upTemp;
    }

    public void setUpTemp(Integer upTemp) {
        this.upTemp = upTemp;
    }

    public Integer getBcmTemp() {
        return bcmTemp;
    }

    public void setBcmTemp(Integer bcmTemp) {
        this.bcmTemp = bcmTemp;
    }

    public Integer getPowerTemp() {
        return powerTemp;
    }

    public void setPowerTemp(Integer powerTemp) {
        this.powerTemp = powerTemp;
    }

    public Integer getOptTxPower() {
        return optTxPower;
    }

    public void setOptTxPower(Integer optTxPower) {
        this.optTxPower = optTxPower;
    }

    public Integer getOptRePower() {
        return optRePower;
    }

    public void setOptRePower(Integer optRePower) {
        this.optRePower = optRePower;
    }

    public Integer getOptCurrent() {
        return optCurrent;
    }

    public void setOptCurrent(Integer optCurrent) {
        this.optCurrent = optCurrent;
    }

    public Integer getOptVoltage() {
        return optVoltage;
    }

    public void setOptVoltage(Integer optVoltage) {
        this.optVoltage = optVoltage;
    }

    public Integer getOptTemp() {
        return optTemp;
    }

    public void setOptTemp(Integer optTemp) {
        this.optTemp = optTemp;
    }

    public Integer getSnr() {
        return snr;
    }

    public void setSnr(Integer snr) {
        this.snr = snr;
    }

    public Integer getbER() {
        return bER;
    }

    public void setbER(Integer bER) {
        this.bER = bER;
    }

    public Integer getCcer() {
        return ccer;
    }

    public void setCcer(Integer ccer) {
        this.ccer = ccer;
    }

    public Integer getUcer() {
        return ucer;
    }

    public void setUcer(Integer ucer) {
        this.ucer = ucer;
    }

    public Integer getChannelSpeed() {
        return channelSpeed;
    }

    public void setChannelSpeed(Integer channelSpeed) {
        this.channelSpeed = channelSpeed;
    }

    public Integer getChannnelUsed() {
        return channnelUsed;
    }

    public void setChannnelUsed(Integer channnelUsed) {
        this.channnelUsed = channnelUsed;
    }

    public Integer getMacUsed() {
        return macUsed;
    }

    public void setMacUsed(Integer macUsed) {
        this.macUsed = macUsed;
    }

    public Integer getTotalCm() {
        return totalCm;
    }

    public void setTotalCm(Integer totalCm) {
        this.totalCm = totalCm;
    }

    public Integer getOnlineCm() {
        return onlineCm;
    }

    public void setOnlineCm(Integer onlineCm) {
        this.onlineCm = onlineCm;
    }

    public Integer getOfflineCm() {
        return offlineCm;
    }

    public void setOfflineCm(Integer offlineCm) {
        this.offlineCm = offlineCm;
    }

    public Logger getLogger() {
        return logger;
    }

    public JSONObject getCmcPerfTargetCycleJson() {
        return cmcPerfTargetCycleJson;
    }

    public void setCmcPerfTargetCycleJson(JSONObject cmcPerfTargetCycleJson) {
        this.cmcPerfTargetCycleJson = cmcPerfTargetCycleJson;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public PerfTargetService getPerfTargetService() {
        return perfTargetService;
    }

    public void setPerfTargetService(PerfTargetService perfTargetService) {
        this.perfTargetService = perfTargetService;
    }

    public JSONObject getCmcPerfTargetCycleObject() {
        return cmcPerfTargetCycleObject;
    }

    public void setCmcPerfTargetCycleObject(JSONObject cmcPerfTargetCycleObject) {
        this.cmcPerfTargetCycleObject = cmcPerfTargetCycleObject;
    }

    public Integer getCmflap() {
        return cmflap;
    }

    public void setCmflap(Integer cmflap) {
        this.cmflap = cmflap;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * 将百分比数字转换为前台所需要的数据格式
     * 
     * @param num
     * @return
     */
    private String splitPercentNum(double num) {
        // DecimalFormat df = new DecimalFormat("#.00");
        String[] numArray = String.valueOf(num).split("\\.");
        StringBuilder sb = new StringBuilder();
        if (num > 0) {
            for (int i = 0; i < numArray[0].length(); i++) {
                sb.append("<li><div class='percentNum" + numArray[0].charAt(i) + "'></div></li>");
            }
            sb.append("<li><div class='percentNumDot'></div></li>");
            for (int j = 0; j < numArray[1].length(); j++) {
                sb.append("<li><div class='miniPercentNum" + numArray[1].charAt(j) + "'></div></li>");
            }
        } else {
            sb.append("<li><div class='percentNum0'></div></li>");
        }
        return sb.toString();
    }

	public CmcAttribute getCmcAttribute() {
		return cmcAttribute;
	}

	public void setCmcAttribute(CmcAttribute cmcAttribute) {
		this.cmcAttribute = cmcAttribute;
	}
}