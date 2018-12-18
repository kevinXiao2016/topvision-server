/***********************************************************************
 * $Id: CmcCpeAction.java,v1.0 2013-6-18 下午1:11:52 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.action;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.alert.service.CmcAlertService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.domain.CmFileNameChangeLog;
import com.topvision.ems.cmc.cm.domain.RealtimeCpe;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.CmServiceTypeService;
import com.topvision.ems.cmc.cpe.domain.CmFdbTable;
import com.topvision.ems.cmc.cpe.domain.CmFdbTableRemoteQuery;
import com.topvision.ems.cmc.cpe.domain.CmLocateInfo;
import com.topvision.ems.cmc.cpe.domain.CmServiceFlow;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmCmcRunningInfo;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.domain.CmCpeAct;
import com.topvision.ems.cmc.domain.CmCpeCollectDataPolicy;
import com.topvision.ems.cmc.domain.CmCpeNumInArea;
import com.topvision.ems.cmc.domain.CmMutipleInfo;
import com.topvision.ems.cmc.domain.CmOltRunningInfo;
import com.topvision.ems.cmc.domain.CmcCmReatimeNum;
import com.topvision.ems.cmc.domain.CmcPortCmRealTimeNum;
import com.topvision.ems.cmc.domain.CmcUserNumHisPerf;
import com.topvision.ems.cmc.domain.CpeCollectConfig;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.facade.domain.DocsIfUpstreamChannel;
import com.topvision.ems.cmc.flap.service.CmcFlapService;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CmCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.CollectTimeUtil;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.util.highcharts.domain.Point;
import com.topvision.platform.zetaframework.util.ZetaUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author loyal
 * @created @2013-6-18-下午1:11:52
 * 
 */
@Controller("cpeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CpeAction extends BaseAction {
    private static final long serialVersionUID = 6643159712943264100L;
    private final Logger logger = LoggerFactory.getLogger(CpeAction.class);
    @Resource(name = "cpeService")
    private CpeService cpeService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "alertService")
    private AlertService alertService;
    @Resource(name = "cmcAlertService")
    private CmcAlertService cmcAlertService;
    @Resource(name = "cmcFlapService")
    private CmcFlapService cmcFlapService;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "systemPreferencesService")
    private SystemPreferencesService systemPreferencesService;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Resource(name = "cmDao")
    private CmDao cmDao;
    @Resource(name = "cmServiceTypeService")
    private CmServiceTypeService cmServiceTypeService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @SuppressWarnings("rawtypes")
    @Autowired
    private PerformanceService performanceService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    private Long cmId;
    private Long cmcId;
    private Long cmIndex;
    private Long cmcIndex;
    private Long channelId;
    private Long channelIndex;
    private Long entityId;
    private Long ponId;
    private Long entityType;
    private Long initData;
    private Long statisticData;
    private Long actionData;
    private Long cmHistoryData;
    private Long cmCollectPeriod;
    private Integer cmCollectStatus;
    private Integer statisticSource;
    private Integer cmTypeStatisticStatus;
    private Integer cmActionStatisticStatus;
    private Integer cpeCollectStatus;
    private Integer cpeActionStatisticStatus;
    private Integer cpeNumStatisticStatus;
    private String cmMac;
    private String quotas;
    private String startTime;
    private String endTime;
    private String cmIp;
    private String cpeIp;
    private String cpeMac;
    private String cmLocation;
    private JSONArray areasJson;
    private JSONArray ipEntityJson;
    private JSONArray ccmtsJson;
    private JSONObject cmCollectConfigJson;
    private JSONObject cmCpeCollectDataPolicyJson;
    private JSONObject cpeCollectConfigJson;
    private JSONObject cmCpeNumInAreaJson = new JSONObject();
    private CmCollectConfig cmCollectConfig;
    private CpeCollectConfig cpeCollectConfig;
    private CmCpeCollectDataPolicy cmCpeCollectDataPolicy;
    private String queryMode; // CM查询方式,1为直接查询，2为使用RemoteQuery方式
    private Boolean versionSupport;
    private Boolean otherCmts;
    private JSONArray entityTypes = new JSONArray();
    private Integer cmPollStatus;
    private Long cmPollInterval;
    private String readCommunity;
    private String writeCommunity;

    private String sourcePage;
    private String name;
    // CMCPE页面用户视图
    private String cmCpeView;
    private String onuCpeInterval;
    private String onuCpeStatus;

    /**
     * 跳转到CM终端实时定位页面
     * 
     * @return
     */
    public String showCmLocate() {
        return SUCCESS;
    }

    /**
     * 查询终端的实时定位
     * 
     * @return
     * @throws IOException
     */
    public String queryCmLocate() throws IOException {
        cmMac = MacUtils.formatMac(cmMac);
        CmLocateInfo info = cpeService.queryCmLocate(cmMac);
        if (info != null) {
            writeDataToAjax(info);
        }
        return NONE;
    }

    /**
     * terminalConfig.jsp 跳转终端采集配置页面
     * 
     * @return
     */
    public String showTerminalCollectConfig() {
        cmCollectConfig = cpeService.getCmCollectConfig();
        cpeCollectConfig = cpeService.getCpeCollectConfig();
        cmCpeCollectDataPolicy = cpeService.getCmCpeCollectDataPolicy();
        cmCollectConfigJson = JSONObject.fromObject(cmCollectConfig);
        cpeCollectConfigJson = JSONObject.fromObject(cpeCollectConfig);
        cmCpeCollectDataPolicyJson = JSONObject.fromObject(cmCpeCollectDataPolicy);
        readCommunity = cpeService.getCmReadCommunity();
        writeCommunity = cpeService.getCmWriteCommunity();
        // TODO 需要判断权限
        cmPollInterval = cpeService.getCmPollInterval();
        if (cpeService.isCmPollStart()) {
            cmPollStatus = 1;
        } else {
            cmPollStatus = 0;
        }
        Properties preferences = systemPreferencesService.getModulePreferences("onuCpe");
        onuCpeInterval = preferences.getProperty("onuCpeInterval", "1800000");
        onuCpeStatus = preferences.getProperty("onuCpeStatus", "0");
        return SUCCESS;
    }

    /**
     * 加载CM属性,上下行信道
     * 
     * @return
     * @throws IOException
     */
    public String loadCmAttribute() throws IOException {
        JSONObject json = new JSONObject();
        CmStatus cmStatus = new CmStatus();
        if (cmService.getCmQueryMode() == CmcConstants.CM_IMMEDIATELY_QUERY) {
            boolean ping;
            try {
                IpUtils ipUtils = new IpUtils(cmIp);
                if (ipUtils.longValue() == 0) {
                    ping = false;
                } else {
                    ping = cmService.checkCmReachable(cmIp);
                }
            } catch (Exception ipe) {
                logger.error("", ipe);
                ping = false;
            }
            readCommunity = cpeService.getCmReadCommunity();
            writeCommunity = cpeService.getCmWriteCommunity();
            boolean snmpCheck = cmService.checkCmSnmp(cmIp, readCommunity, writeCommunity);
            if (ping && snmpCheck) {
                cmStatus = cmService.showCmStatus(cmIp, cmId);
            } else {
                cmStatus = new CmStatus();
            }
            // 获取质量数据后更新数据库
            cmStatus.setCmId(cmId);
            cmStatus.setCollectTime(new Timestamp(System.currentTimeMillis()));
            cmService.saveCmSignal(cmStatus);
            cmStatus.setPing(ping);
            cmStatus.setSnmpCheck(snmpCheck);
            cmStatus.setRemoteQuery(false);
        } else if (cmService.getCmQueryMode() == CmcConstants.CM_REMOTE_QUERY) {
            cmStatus = cmService.showCmStatus(cmIp, cmId);
            cmService.saveCmSignal(cmStatus);
            cmStatus.setPing(false);
            cmStatus.setSnmpCheck(false);
            cmStatus.setRemoteQuery(true);
        }
        json.put("cmStatus", cmStatus);
        json.write(response.getWriter());
        return NONE;
    }

    public String getPreviousState() {
        JSONObject json = new JSONObject();
        CmAttribute cmAttribute = cmService.getCmAttributeByCmId(cmId);
        Integer preStatus = cpeService.loadCmPreStatusOnCmts(cmAttribute.getCmcId(), cmAttribute.getStatusIndex());
        Integer cmStatus = cpeService.loadCmStatusOnCmts(cmAttribute.getCmcId(), cmAttribute.getStatusMacAddress());
        json.put("preState", preStatus);
        json.put("cmStatus", cmStatus);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 查询CPE列表
     *
     * @return
     * @throws IOException
     */
    public String loadCpeList() throws IOException {
        JSONObject json = new JSONObject();
        Long cmcId = cmService.getCmcIdByCmId(cmId);
        try {
            boolean isSupportRealtimeCpeQuery = deviceVersionService.isFunctionSupported(cmcId, "realtimecpe");
            json.put("isSupportRealtimeCpeQuery", isSupportRealtimeCpeQuery);
        } catch (Exception e) {
            logger.error("", e);
        }
        List<RealtimeCpe> cpeList = cpeService.getRealCpeListByCmId(cmId);
        json.put("cpeList", cpeList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取CM业务类型修改记录
     * 
     * @return
     * @throws IOException
     */
    public String loadCmServiceType() throws IOException {
        JSONObject json = new JSONObject();
        List<CmFileNameChangeLog> logs = cmServiceTypeService.getlogs(cmId);
        json.put("cmServiceType", logs);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 查询CM service flow
     *
     * @return
     * @throws IOException
     */
    public String loadCmSF() throws IOException {
        JSONObject json = new JSONObject();
        List<CmServiceFlow> cmsf = cpeService.getCmServiceFlowByCmId(cmId);
        json.put("cmsf", cmsf);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载OLT 告警列表
     * 
     * @return
     * @throws IOException
     */
    public String loadOltAlertList() throws IOException {
        List<Alert> oltAlert = alertService.getRecentAlertByEntityIdAndLimit(Integer.MAX_VALUE, entityId);
        for (Alert alert : oltAlert) {
            alert.setTypeName(ZetaUtil.getStaticString(alert.getTypeName(), "fault"));
            if (alert.getStatus() == null || alert.getStatus() == Alert.ALERT) {
                alert.setConfirmTimeStr("");
            }
        }
        JSONArray.fromObject(oltAlert).write(response.getWriter());
        return NONE;
    }

    /**
     * 加载CC 告警列表
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcAlertList() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        List<Alert> cmcAlert = cmcAlertService.getCmcAlertList(map, 0, Integer.MAX_VALUE);
        for (Alert alert : cmcAlert) {
            alert.setTypeName(ZetaUtil.getStaticString(alert.getTypeName(), "fault"));
            if (alert.getStatus() == null || alert.getStatus() == Alert.ALERT) {
                alert.setConfirmTimeStr("");
            }
        }
        JSONArray.fromObject(cmcAlert).write(response.getWriter());
        return NONE;
    }

    /**
     * 加载 CM上下线行为
     * 
     * @return
     * @throws IOException
     */
    public String loadCmActHistory() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmMac", new MacUtils(cmMac).longValue());
        map.put("startTime", DateUtils.format(new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000))));
        map.put("end", DateUtils.format(new Date(System.currentTimeMillis())));
        List<CmAct> cmActs = cmService.getCmActionInfo(map);
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CmAct cmAct : cmActs) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmAct.getCmmacString(), displayRule);
            cmAct.setCmmacString(formatedMac);
        }
        JSONArray.fromObject(cmActs).write(response.getWriter());
        return NONE;
    }

    /**
     * 加载 CPE上下线行为
     * 
     * @return
     * @throws IOException
     */
    public String loadCpeActHistory() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("startTime", DateUtils.format(new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000))));
        map.put("end", DateUtils.format(new Date(System.currentTimeMillis())));
        map.put("cmMac", new MacUtils(cmMac).longValue());
        List<CpeAct> cpeActs = cmService.getCpeActionInfoByCmMac(map);
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CpeAct cpeAct : cpeActs) {
            String formatedCmMac = MacUtils.convertMacToDisplayFormat(cpeAct.getCmmacString(), displayRule);
            cpeAct.setCmmacString(formatedCmMac);
            String formatedCpeMac = MacUtils.convertMacToDisplayFormat(cpeAct.getCpemacString(), displayRule);
            cpeAct.setCpemacString(formatedCpeMac);
        }
        List<CmCpeAct> cmCpeActs = new ArrayList<CmCpeAct>();
        if (cpeActs.size() > 0) {
            String mac = cpeActs.get(0).getCpemacString();
            CmCpeAct temp = new CmCpeAct();
            temp.setCpeMacString(mac);
            for (CpeAct cpeAct : cpeActs) {
                if (mac.equalsIgnoreCase(cpeAct.getCpemacString())) {
                    temp.addCpeAct(cpeAct);
                } else {
                    cmCpeActs.add(temp);
                    temp = new CmCpeAct();
                    temp.setCpeMacString(cpeAct.getCpemacString());
                    temp.addCpeAct(cpeAct);
                    mac = cpeAct.getCpemacString();
                }
            }
            cmCpeActs.add(temp);
        }
        JSONArray.fromObject(cmCpeActs).write(response.getWriter());
        return NONE;
    }

    /**
     * 加载CC运行信息
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcRunningInfo() throws IOException {
        // CC运行信息
        CmCmcRunningInfo cmCmcRunningInfo = new CmCmcRunningInfo();
        if (entityTypeService.isCmts(entityType)) {
            cmCmcRunningInfo = cpeService.getCmCmtsRunningInfo(cmId);
        } else {
            cmCmcRunningInfo = cpeService.getCmCmcRunningInfo(cmId);
        }
        writeDataToAjax(cmCmcRunningInfo);
        return NONE;
    }

    /**
     * 加载Olt运行信息
     * 
     * @return
     * @throws IOException
     */
    public String loadOltRunningInfo() throws IOException {
        CmOltRunningInfo cmOltRunningInfo = null;
        cmOltRunningInfo = cpeService.getCmOltRunningInfo(cmId);
        if (cmOltRunningInfo != null) {
            writeDataToAjax(cmOltRunningInfo);
        }
        return NONE;
    }

    /**
     * 加载CM Flap信息
     * 
     * @return
     * @throws IOException
     */
    public String loadCmFlap() throws IOException {
        CmFlap cmFlap = cmcFlapService.queryCmFlapInfo(cmId);
        if (cmFlap != null) {
            writeDataToAjax(cmFlap);
        }
        return NONE;
    }

    /**
     * terminalConfig.jsp 跳转终端采集配置页面
     * 
     * @return
     */
    public String showCmCpeQuery() {
        queryMode = String.valueOf(cmService.getCmQueryMode());// CM查询模式
        // 获取用户视图信息
        cmCpeView = this.getUserView();
        return SUCCESS;
    }

    /**
     * 保存用户设置的CMCPE页面视图
     * 
     * @return
     */
    public String saveCmCpeView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        userPreferencesService.saveModulePreferences("cmCpeView", "cmCpe", cmCpeView, uc);
        return NONE;
    }

    /**
     * 获取用户视图信息
     * 
     * @return
     */
    private String getUserView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences cpeView = userPreferencesService.getUserPreference(uc.getUserId(), "cmCpeView");
        if (cpeView != null) {
            return cpeView.getValue();
        } else {
            return "";
        }
    }

    /**
     * terminalConfig.jsp 修改cm cpe采集配置
     * 
     * @return
     */
    public String modifyCmCpeCollectConfig() {
        String message;
        try {
            CmCollectConfig cmCollectConfig = new CmCollectConfig();
            cmCollectConfig.setCmActionStatisticStatus(cmActionStatisticStatus);
            cmCollectConfig.setCmCollectPeriod(cmCollectPeriod);
            cmCollectConfig.setCmCollectStatus(cmCollectStatus);
            cmCollectConfig.setCmTypeStatisticStatus(cmTypeStatisticStatus);
            cmCollectConfig.setCmStatisticSource(1);
            cpeService.modifyCmCollectConfig(cmCollectConfig);
            CpeCollectConfig cpeCollectConfig = new CpeCollectConfig();
            cpeCollectConfig.setCpeActionStatisticStatus(cpeActionStatisticStatus);
            cpeCollectConfig.setCpeCollectPeriod(cmCollectPeriod);// cm 与cpe采集周期保持一致
            cpeCollectConfig.setCpeCollectStatus(cpeCollectStatus);
            cpeCollectConfig.setCpeNumStatisticStatus(cpeNumStatisticStatus);
            cpeService.modifyCpeCollectConfig(cpeCollectConfig);
            performanceService.modifyCollectTimeUtil(CollectTimeUtil.CmStatus, System.currentTimeMillis(),
                    cmCollectConfig.getCmCollectPeriod());
            CollectTimeUtil ctu = CollectTimeUtil.getCollectTimeUtil(CollectTimeUtil.CmStatus);
            ctu.setPeriod(cmCollectConfig.getCmCollectPeriod());
            ctu.setStartTime(System.currentTimeMillis());
            if (logger.isDebugEnabled()) {
                logger.debug("modifyCmCpeCollectConfig:" + ctu);
            }
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * terminalConfig.jsp 修改cm cpe数据保存策略
     * 
     * @return
     */
    public String modifyCmCpeCollectDataPolicy() {
        String message;
        try {
            CmCpeCollectDataPolicy cmCpeCollectDataPolicy = new CmCpeCollectDataPolicy();
            cmCpeCollectDataPolicy.setInitDataSavePolicy(initData);
            cmCpeCollectDataPolicy.setActionDataSavePolicy(actionData);
            cmCpeCollectDataPolicy.setStatisticDataSavePolicy(statisticData);
            cmCpeCollectDataPolicy.setCmHistorySavePolicy(cmHistoryData);
            cpeService.modifyCmCpeCollectDataPolicy(cmCpeCollectDataPolicy);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改全局cm snmp参数
     * 
     * @return
     */
    public String modifyCmSnmpParamConfig() {
        String message;
        try {
            cpeService.modifyCmSnmpParamConfig(readCommunity, writeCommunity);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
        }
        writeDataToAjax(message);
        return NONE;
    }

    public String refreshCpeListByCmMac() {
        List<RealtimeCpe> realtimeCpes = cpeService.getRealCpeListByCmMac(cmcId, cmMac);
        writeDataToAjax(JSONArray.fromObject(realtimeCpes));
        return NONE;
    }

    public String refreshCpeList() {
        List<RealtimeCpe> realtimeCpes = cpeService.getRealCpeListByCmId(cmId);
        writeDataToAjax(JSONArray.fromObject(realtimeCpes));
        return NONE;
    }

    public String refreshCpeByCmcAndCpeMac() {
        CmCpe cmCpe = cpeService.refreshCpeByCmcAndCpeMac(cmcId, cpeMac);
        writeDataToAjax(JSONObject.fromObject(cmCpe));
        return NONE;
    }

    public String refreshCpe() {
        CmCpe cmCpe = cpeService.getCpeByCpeMac(cmId, cpeMac);
        writeDataToAjax(JSONObject.fromObject(cmCpe));
        return NONE;
    }

    public String cpeDetail() {
        List<CmCpe> cpeList = cpeService.getCpeListByCmId(cmId);
        writeDataToAjax(JSONArray.fromObject(cpeList));
        return NONE;
    }

    /**
     * cmcRealTimeUserNum.jsp 跳转全网ccmts实时用户数查询页面
     * 
     * @return
     */
    public String showAllCcmtsRealTimeUserNum() {
        List<Entity> ipEntityList = cpeService.getEntityWithIp();
        ipEntityJson = JSONArray.fromObject(ipEntityList);
        List<EntityType> entityTypeList = entityTypeService.loadSubType(entityTypeService.getEntitywithipType());
        entityTypes = JSONArray.fromObject(entityTypeList);
        return SUCCESS;
    }

    /**
     * cmcRealTimeUserNum.jsp 跳转全网ccmts实时用户数查询页面
     * 
     * @return
     * @throws IOException
     */
    public String loadAllDeviceCmNum() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> cmQueryMap = new HashMap<String, Object>();
        if (entityId != null && entityId > 0) {
            cmQueryMap.put("entityId", entityId);
        }
        if (name != null && !"".equals(name)) {
            cmQueryMap.put("name", name);
        }
        if (cmMac != null && !"".equals(cmMac)) {
            cmQueryMap.put("cmMac", MacUtils.convertToMaohaoFormat(cmMac));
        }
        if (entityType != null && entityType > 0) {
            cmQueryMap.put("entityType", entityType);
        }

        List<CmNum> deviceMap = cpeService.getAllAllDeviceCmNum(cmQueryMap);
        json.put("data", deviceMap);
        json.put("rowCount", deviceMap.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * cmcRealTimeUserNum.jsp 获取CC cm用户数相关信息
     * 
     * @return
     * @throws IOException
     */
    public String loadCcmtsCmNumInfo() throws IOException {
        JSONObject realTimeCmNumJson = new JSONObject();
        List<CmcCmReatimeNum> cmcCmReatimeNumList = cpeService.getCcmtsCmNumInfo(entityId);
        Entity entity = entityService.getEntity(entityId);
        Long typeId = entity.getTypeId();
        List<CmcPortCmRealTimeNum> cmcPortCmRealTimeNumList = new ArrayList<CmcPortCmRealTimeNum>();
        if (entityTypeService.isCmts(typeId)) {
            if (cmcCmReatimeNumList.size() > 0) {
                CmcPortCmRealTimeNum temp = new CmcPortCmRealTimeNum();
                temp.setCmcIndex(cmcCmReatimeNumList.get(0).getCcIfIndex());
                temp.setCmcIndexString(entity.getName());
                for (CmcCmReatimeNum cmcCmReatimeNum : cmcCmReatimeNumList) {
                    cmcCmReatimeNum.setChannelString(CmcUtil.getCmtsUpChannelIndex(entity.getTypeId(),
                            cmcCmReatimeNum.getIfDescr(), entityTypeService));
                    temp.addCmcCmReatimeNum(cmcCmReatimeNum);
                }
                // temp 按照信道id排序
                temp.sort();
                cmcPortCmRealTimeNumList.add(temp);
            }
        } else {
            if (cmcCmReatimeNumList.size() > 0) {
                CmcPortCmRealTimeNum temp = new CmcPortCmRealTimeNum();
                temp.setCmcIndex(cmcCmReatimeNumList.get(0).getCcIfIndex());
                temp.setCmcIndexString(cmcCmReatimeNumList.get(0).getCcIfIndexString());
                int i = 0;
                for (CmcCmReatimeNum cmcCmReatimeNum : cmcCmReatimeNumList) {
                    cmcCmReatimeNum.setChannelString(cmcCmReatimeNum.getChannelId().toString());
                    if (i < 4) {
                        temp.addCmcCmReatimeNum(cmcCmReatimeNum);
                        i++;
                    } else {
                        // temp 按照信道id排序
                        temp.sort();
                        cmcPortCmRealTimeNumList.add(temp);
                        temp = new CmcPortCmRealTimeNum();
                        temp.setCmcIndex(cmcCmReatimeNum.getCcIfIndex());
                        temp.setCmcIndexString(cmcCmReatimeNum.getCcIfIndexString());
                        temp.addCmcCmReatimeNum(cmcCmReatimeNum);
                        i = 1;
                    }
                }
                // temp 按照信道id排序
                temp.sort();
                cmcPortCmRealTimeNumList.add(temp);
            }
        }
        realTimeCmNumJson.put("data", cmcPortCmRealTimeNumList);
        writeDataToAjax(realTimeCmNumJson);
        return NONE;
    }

    /**
     * dashboard.jsp 获取cm cpe数量统计信息
     * 
     * @return
     * @throws IOException
     */
    public String getCmCpeNumStatistic() throws IOException {
        List<CmCpeNumInArea> cmCpeNumInRegionList = cpeService.getCmCpeNumInRegion();
        JSONArray onlineCmNumJson = new JSONArray();
        JSONArray onlineCpeNumJson = new JSONArray();
        JSONArray regionsJson = new JSONArray();
        if (cmCpeNumInRegionList != null && cmCpeNumInRegionList.size() > 0) {
            for (CmCpeNumInArea cmCpeNumInArea : cmCpeNumInRegionList) {
                if (cmCpeNumInArea.getCmOnlineNum() != null) {
                    onlineCmNumJson.add(cmCpeNumInArea.getCmOnlineNum() * -1);
                }
                if (cmCpeNumInArea.getCpeOnLineNum() != null) {
                    onlineCpeNumJson.add(cmCpeNumInArea.getCpeOnLineNum());
                }
                if (cmCpeNumInArea.getAreaName() != null) {
                    regionsJson.add(cmCpeNumInArea.getAreaName());
                }
            }
        }
        cmCpeNumInAreaJson.put("onlineCmNum", onlineCmNumJson);
        cmCpeNumInAreaJson.put("onlineCpeNum", onlineCpeNumJson);
        cmCpeNumInAreaJson.put("regions", regionsJson);
        writeDataToAjax(cmCpeNumInAreaJson);
        return SUCCESS;
    }

    /**
     * 获取信道流量历史数据
     * 
     * @return
     * @throws IOException
     */
    public String showFlowHisPerf() throws IOException {
        JSONObject json = new JSONObject();
        JSONArray flowHisPerfJson = new JSONArray();
        List<Point> flowHisPerf = new ArrayList<Point>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (startTime != null) {
            queryMap.put("startTime", startTime);
        }
        if (endTime != null) {
            queryMap.put("endTime", endTime);
        }
        queryMap.put("cmcId", cmcId);
        queryMap.put("channelIndex", channelIndex);
        queryMap.put("entityType", entityType);
        if (CmcIndexUtils.getChannelType(channelIndex) == 0) {
            flowHisPerf = cpeService.getUpChannelFlowHisPerf(queryMap);
        } else {
            flowHisPerf = cpeService.getDownChannelFlowHisPerf(queryMap);
        }
        if (flowHisPerf != null && flowHisPerf.size() > 0) {
            // 采样，避免点过多导致前端脚本异常
            flowHisPerf = PerfTargetUtil.samplePoints(flowHisPerf);
            for (Point point : flowHisPerf) {
                JSONArray flowPoint = new JSONArray();
                /*
                 * flowPoint.add(point.getXTime().getTime()); flowPoint.add(new
                 * Float(NumberUtils.TWODOT_FORMAT.format(point.getY() / NumberUtils.Mbps)));
                 */
                flowPoint.add(point.getXTime().getTime());
                flowPoint.add(point.getY());

                flowHisPerfJson.add(flowPoint);
            }
        }
        json.put("flowHisPerf", flowHisPerfJson);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取信道snr历史数据
     * 
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public String showSnrHisPerf() throws IOException, ParseException {
        JSONObject json = new JSONObject();
        JSONArray snrHisPerfJson = new JSONArray();
        List<Point> snrHisPerf = new ArrayList<Point>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (startTime != null) {
            queryMap.put("startTime", startTime);
        }
        if (endTime != null) {
            queryMap.put("endTime", endTime);
        }
        queryMap.put("cmcId", cmcId);
        queryMap.put("channelIndex", channelIndex);
        queryMap.put("entityType", entityType);
        snrHisPerf = cpeService.getSnrHisPerf(queryMap);
        if (snrHisPerf != null && snrHisPerf.size() > 0) {
            // 采样，避免点过多导致前端脚本异常
            snrHisPerf = PerfTargetUtil.samplePoints(snrHisPerf);
            for (Point point : snrHisPerf) {
                JSONArray snrPoint = new JSONArray();
                snrPoint.add(point.getXTime().getTime());
                snrPoint.add(point.getY() / 10);
                snrHisPerfJson.add(snrPoint);
            }
        }
        json.put("snrHisPerf", snrHisPerfJson);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取CC cpu利用率历史数据
     * 
     * @return
     * @throws IOException
     */
    public String showCpuHisPerf() throws IOException {
        JSONObject json = new JSONObject();
        JSONArray cpuHisPerfJson = new JSONArray();
        List<Point> cpuHisPerf = new ArrayList<Point>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", endTime);
        queryMap.put("cmcId", cmcId);
        cpuHisPerf = cpeService.getCpuHisPerf(queryMap);
        if (cpuHisPerf != null && cpuHisPerf.size() > 0) {
            // 采样，避免点过多导致前端脚本异常
            cpuHisPerf = PerfTargetUtil.samplePoints(cpuHisPerf);
            for (Point point : cpuHisPerf) {
                JSONArray cpuPoint = new JSONArray();
                cpuPoint.add(point.getXTime().getTime());
                cpuPoint.add(point.getY());
                cpuHisPerfJson.add(cpuPoint);
            }
        }
        json.put("cpuHisPerf", cpuHisPerfJson);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 跳转cmc用户数历史查询页面
     * 
     * @return
     */
    public String showCmcHisUserNum() {
        return SUCCESS;
    }

    /**
     * 获取CC用户数历史性能
     * 
     * @return
     * @throws IOException
     */
    public String showCmHisUserPerf() throws IOException {
        JSONObject json = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", endTime);
        queryMap.put("entityId", entityId);
        // add by loyal 处理cmts
        // modify by jay 修改查询逻辑
        if (channelIndex != null && channelIndex != 0) {
            queryMap.put("channelIndex", channelIndex);
            List<CmcUserNumHisPerf> cmcUserNumHisPerf = cpeService.getUserNumHisByChannel(queryMap);
            json = createCmcUserNumHisPerfData(cmcUserNumHisPerf);
            writeDataToAjax(json);
            return NONE;
        } else if (cmcId != null && cmcId != 0) {
            Long cmcIndex = cpeService.getCmcIndexByCmcId(cmcId);
            queryMap.put("ccIfIndex", cmcIndex);
            List<CmcUserNumHisPerf> cmcUserNumHisPerf = cpeService.getUserNumHisByCmcNew(queryMap);
            json = createCmcUserNumHisPerfData(cmcUserNumHisPerf);
            writeDataToAjax(json);
            return NONE;
        } else if (ponId != null && ponId != 0) {
            Long ponIndex = cpeService.getPonIndexByPonId(ponId);
            queryMap.put("ponIndex", ponIndex);
            List<CmcUserNumHisPerf> cmcUserNumHisPerf = cpeService.getUserNumHisByPon(queryMap);
            json = createCmcUserNumHisPerfData(cmcUserNumHisPerf);
            writeDataToAjax(json);
            return NONE;
        } else if (entityId != null && entityId != -1) {
            queryMap.put("entityId", entityId);
            List<CmcUserNumHisPerf> cmcUserNumHisPerf = cpeService.getUserNumHisByDevice(queryMap);
            json = createCmcUserNumHisPerfData(cmcUserNumHisPerf);
            writeDataToAjax(json);
            return NONE;
        } else {
            List<CmcUserNumHisPerf> cmcUserNumHisPerf = cpeService.getAllUserNumHis(queryMap);
            json = createCmcUserNumHisPerfData(cmcUserNumHisPerf);
            writeDataToAjax(json);
            return NONE;
        }
    }

    @SuppressWarnings("unused")
    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
    }

    private JSONObject createCmcUserNumHisPerfData(List<CmcUserNumHisPerf> cmcUserNumHisPerfList) {
        String[] quotaArray = quotas.split(",");
        List<String> quotaList = new ArrayList<String>(Arrays.asList(quotaArray));
        JSONObject json = new JSONObject();
        List<Point> onlineNumJson = new ArrayList<Point>();
        List<Point> otherNumJson = new ArrayList<Point>();
        List<Point> offlineNumJson = new ArrayList<Point>();
        List<Point> interactiveNumJson = new ArrayList<Point>();
        List<Point> broadbandNumJson = new ArrayList<Point>();
        List<Point> cpeInteractiveNumJson = new ArrayList<Point>();
        List<Point> cpeBroadbandNumJson = new ArrayList<Point>();
        List<Point> cpeMtaNumJson = new ArrayList<Point>();
        List<Point> mtaNumJson = new ArrayList<Point>();
        List<Point> integratedNumJson = new ArrayList<Point>();
        if (cmcUserNumHisPerfList != null && cmcUserNumHisPerfList.size() > 0) {
            for (CmcUserNumHisPerf cmcUserNumHisPerf : cmcUserNumHisPerfList) {
                if (quotaList.contains("onlineQuota")) {
                    Point onlineNumPoint = new Point();
                    onlineNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    onlineNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getOnlineNum().toString()));
                    onlineNumJson.add(onlineNumPoint);

                    /*
                     * onlineNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * onlineNumPoint.add(cmcUserNumHisPerf.getOnlineNum());
                     * onlineNumJson.add(onlineNumPoint);
                     */
                }
                if (quotaList.contains("otherQuota")) {
                    /*
                     * JSONArray otherNumPoint = new JSONArray();
                     * otherNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * otherNumPoint.add(cmcUserNumHisPerf.getOtherNum());
                     * otherNumJson.add(otherNumPoint);
                     */
                    Point otherNumPoint = new Point();
                    otherNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    otherNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getOtherNum().toString()));
                    otherNumJson.add(otherNumPoint);
                }
                if (quotaList.contains("offlineQuota")) {
                    /*
                     * JSONArray offlineNumPoint = new JSONArray();
                     * offlineNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * offlineNumPoint.add(cmcUserNumHisPerf.getOfflineNum());
                     * offlineNumJson.add(offlineNumPoint);
                     */
                    Point offlineNumPoint = new Point();
                    offlineNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    offlineNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getOfflineNum().toString()));
                    offlineNumJson.add(offlineNumPoint);
                }
                if (quotaList.contains("interactiveQuota")) {
                    /*
                     * JSONArray interactiveNumPoint = new JSONArray();
                     * interactiveNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * interactiveNumPoint.add(cmcUserNumHisPerf.getInteractiveNum());
                     * interactiveNumJson.add(interactiveNumPoint);
                     */
                    Point interactiveNumPoint = new Point();
                    interactiveNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    interactiveNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getInteractiveNum().toString()));
                    interactiveNumJson.add(interactiveNumPoint);
                }
                if (quotaList.contains("broadbandQuota")) {
                    /*
                     * JSONArray broadbandNumPoint = new JSONArray();
                     * broadbandNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * broadbandNumPoint.add(cmcUserNumHisPerf.getBroadbandNum());
                     * broadbandNumJson.add(broadbandNumPoint);
                     */
                    Point broadbandNumPoint = new Point();
                    broadbandNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    broadbandNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getBroadbandNum().toString()));
                    broadbandNumJson.add(broadbandNumPoint);
                }
                if (quotaList.contains("cpeInteractiveQuota")) {
                    /*
                     * JSONArray cpeNumPoint = new JSONArray();
                     * cpeNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * cpeNumPoint.add(cmcUserNumHisPerf.getCpeNum()); cpeNumJson.add(cpeNumPoint);
                     */
                    Point cpeNumPoint = new Point();
                    cpeNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    cpeNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getCpeInteractiveNum().toString()));
                    cpeInteractiveNumJson.add(cpeNumPoint);
                }
                if (quotaList.contains("cpeBroadbandQuota")) {
                    /*
                     * JSONArray cpeNumPoint = new JSONArray();
                     * cpeNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * cpeNumPoint.add(cmcUserNumHisPerf.getCpeNum()); cpeNumJson.add(cpeNumPoint);
                     */
                    Point cpeNumPoint = new Point();
                    cpeNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    cpeNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getCpeBroadbandNum().toString()));
                    cpeBroadbandNumJson.add(cpeNumPoint);
                }
                if (quotaList.contains("cpeMtaQuota")) {
                    /*
                     * JSONArray cpeNumPoint = new JSONArray();
                     * cpeNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * cpeNumPoint.add(cmcUserNumHisPerf.getCpeNum()); cpeNumJson.add(cpeNumPoint);
                     */
                    Point cpeNumPoint = new Point();
                    cpeNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    cpeNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getCpeMtaNum().toString()));
                    cpeMtaNumJson.add(cpeNumPoint);
                }
                if (quotaList.contains("mtaQuota")) {
                    /*
                     * JSONArray mtaNumPoint = new JSONArray();
                     * mtaNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * mtaNumPoint.add(cmcUserNumHisPerf.c()); mtaNumJson.add(mtaNumPoint);
                     */
                    Point mtaNumPoint = new Point();
                    mtaNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    mtaNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getMtaNum().toString()));
                    mtaNumJson.add(mtaNumPoint);
                }
                if (quotaList.contains("integratedQuota")) {
                    /*
                     * JSONArray integratedNumPoint = new JSONArray();
                     * integratedNumPoint.add(cmcUserNumHisPerf.getCollectTime().getTime());
                     * integratedNumPoint.add(cmcUserNumHisPerf.getIntegratedNum());
                     * integratedNumJson.add(integratedNumPoint);
                     */
                    Point integratedNumPoint = new Point();
                    integratedNumPoint.setXTime(cmcUserNumHisPerf.getCollectTime());
                    integratedNumPoint.setY(Double.valueOf(cmcUserNumHisPerf.getIntegratedNum().toString()));
                    integratedNumJson.add(integratedNumPoint);
                }
            }
        }
        // 需要采样
        onlineNumJson = PerfTargetUtil.samplePoints(onlineNumJson, 100);
        otherNumJson = PerfTargetUtil.samplePoints(otherNumJson, 100);
        offlineNumJson = PerfTargetUtil.samplePoints(offlineNumJson, 100);
        interactiveNumJson = PerfTargetUtil.samplePoints(interactiveNumJson, 100);
        broadbandNumJson = PerfTargetUtil.samplePoints(broadbandNumJson, 100);
        mtaNumJson = PerfTargetUtil.samplePoints(mtaNumJson, 100);
        integratedNumJson = PerfTargetUtil.samplePoints(integratedNumJson, 100);
        cpeInteractiveNumJson = PerfTargetUtil.samplePoints(cpeInteractiveNumJson, 100);
        cpeBroadbandNumJson = PerfTargetUtil.samplePoints(cpeBroadbandNumJson, 100);
        cpeMtaNumJson = PerfTargetUtil.samplePoints(cpeMtaNumJson, 100);

        json.put("onlineNum", onlineNumJson);
        json.put("otherNum", otherNumJson);
        json.put("offlineNum", offlineNumJson);
        json.put("interactiveNum", interactiveNumJson);
        json.put("broadbandNum", broadbandNumJson);
        json.put("mtaNum", mtaNumJson);
        json.put("integratedNum", integratedNumJson);
        json.put("cpeInteractiveNum", cpeInteractiveNumJson);
        json.put("cpeBroadbandNum", cpeBroadbandNumJson);
        json.put("cpeMtaNum", cpeMtaNumJson);
        return json;
    }

    /**
     * cmCpeQuery.jsp 修改cm cpe采集配置
     * 
     * @return
     */
    public String cmCpeListByCondition() {
        List<CmCpeAttribute> cmCpeAttributes;
        Long cpeIpLong = null;
        Long cpeMacLong = null;
        if ((cpeIp == null || cpeIp.equalsIgnoreCase("")) && (cpeMac == null || cpeMac.equalsIgnoreCase(""))) {
            cmCpeAttributes = cpeService.queryCmCpeListByCondition(cmMac, cpeMac, cpeIp, cmLocation);
        } else {
            if (cpeIp != null && !cpeIp.equalsIgnoreCase("")) {
                try {
                    IpUtils cpeIpUtils = new IpUtils(cpeIp);
                    cpeIpLong = cpeIpUtils.longValue();
                } catch (Exception e) {
                    cpeIpLong = 0L;
                }
            } else {
                cpeIpLong = 0L;
            }
            if (cpeMac != null && !cpeMac.equalsIgnoreCase("") && MacUtils.isMac(cpeMac)) {
                cpeMacLong = new MacUtils(cpeMac).longValue();
            } else {
                cpeMacLong = 0L;
            }
            if (cpeIpLong == 0 && cpeMacLong == 0) {
                cmCpeAttributes = cpeService.queryCmCpeListByCondition(cmMac, cpeMac, cpeIp, cmLocation);
            } else {
                cmCpeAttributes = cpeService.queryCmCpeListByConditionHis(cmMac, cpeMacLong, cpeIpLong, cmLocation);
            }
        }
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmCpeAttribute cmCpeAttribute : cmCpeAttributes) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmCpeAttribute.getTopCmCpeMacAddress(), macRule);
            cmCpeAttribute.setTopCmCpeMacAddress(formatedMac);
            String formatedCmMac = MacUtils.convertMacToDisplayFormat(cmCpeAttribute.getStatusMacAddress(), macRule);
            cmCpeAttribute.setStatusMacAddress(formatedCmMac);
            if (entityTypeService.isCmts(cmCpeAttribute.getTypeId())) {
                cmCpeAttribute.setCmtsUpDescr(cmCpeAttribute.getIfname());
                cmCpeAttribute.setCmtsDownDescr(cmCpeAttribute.getIfname());
            }
        }
        writeDataToAjax(JSONArray.fromObject(cmCpeAttributes));
        return NONE;
    }

    /**
     * cmCpeQuery.jsp 获取cm 综合信息
     * 
     * @return
     */
    public String loadCmStatus() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
            CmMutipleInfo cmMultipleInfo = new CmMutipleInfo();
            CmStatus cmStatus;
            if (cmService.getCmQueryMode() == CmcConstants.CM_IMMEDIATELY_QUERY) {
                boolean ping;
                try {
                    IpUtils ipUtils = new IpUtils(cmIp);
                    if (ipUtils.longValue() == 0) {
                        ping = false;
                    } else {
                        ping = cmService.checkCmReachable(cmIp);
                    }
                } catch (Exception ipe) {
                    logger.error("", ipe);
                    ping = false;
                }
                readCommunity = cpeService.getCmReadCommunity();
                writeCommunity = cpeService.getCmWriteCommunity();
                boolean snmpCheck = cmService.checkCmSnmp(cmIp, readCommunity, writeCommunity);
                if (ping && snmpCheck) {
                    cmStatus = cmService.showCmStatus(cmIp, cmId);
                } else {
                    cmStatus = new CmStatus();
                }
                // 获取质量数据后更新数据库
                cmStatus.setCmId(cmId);
                cmStatus.setCollectTime(new Timestamp(System.currentTimeMillis()));

                Long cmcId = cmService.getCmcIdByCmId(cmId);

                // Long upChannelId = cmService.getTopologyInfo(cmId).getDocsIfUpChannelId();
                Long upChannelId = cmService.getCmAttributeByCmId(cmId).getUpChannelId();
                Long upperEntityType = entityService.getEntity(cmcId).getTypeId();
                if (cmStatus.getDocsIfSignalQualityList() != null) {
                    // TODO 由于设备的index错误，导致无法精确到信道，故将第一个信道的值赋给cm
                    if (!cmStatus.getDocsIfSignalQualityList().isEmpty()) {
                        cmStatus.setDownChannelSnr(cmStatus.getDocsIfSignalQualityList().get(0)
                                .getDocsIfSigQSignalNoiseForUnit());// 下行信道信噪比

                    }
                }
                if (cmStatus.getDocsIfDownstreamChannelList() != null) {
                    // TODO 由于设备的index错误，导致无法精确到信道，故将第一个信道的值赋给cm
                    if (!cmStatus.getDocsIfDownstreamChannelList().isEmpty()) {
                        cmStatus.setDownChannelTx(cmStatus.getDocsIfDownstreamChannelList().get(0)
                                .getDownChannelPower()); // 下行信道电平
                        cmStatus.setDownChannelFrequency(cmStatus.getDocsIfDownstreamChannelList().get(0)
                                .getDocsIfDownChannelFrequencyForUnit()); // 下行信道频率
                    }
                }
                if (cmStatus.getDocsIfUpstreamChannelList() != null) {
                    if (entityTypeService.isCcmts(upperEntityType)) {
                        for (DocsIfUpstreamChannel docsIfUpstreamChannel : cmStatus.getDocsIfUpstreamChannelList()) {
                            if (docsIfUpstreamChannel.getDocsIfUpChannelId().toString().equals(upChannelId.toString())) {
                                // 上行信道电平
                                cmStatus.setUpChannelTx(docsIfUpstreamChannel.getUpChannelPower());
                                // 上行信道频率
                                cmStatus.setUpChannelFrequency(docsIfUpstreamChannel
                                        .getDocsIfUpChannelFrequencyForUnit());
                            }
                        }
                    } else if (entityTypeService.isCmts(upperEntityType)) {
                        if (!cmStatus.getDocsIfUpstreamChannelList().isEmpty()) {
                            // 上行信道电平
                            cmStatus.setUpChannelTx(cmStatus.getDocsIfUpstreamChannelList().get(0).getTxPower());
                            // 上行信道频率
                            cmStatus.setUpChannelFrequency(cmStatus.getDocsIfUpstreamChannelList().get(0)
                                    .getDocsIfUpChannelFrequencyForUnit());
                        }
                    }
                }
                cmService.saveCmSignal(cmStatus);
                cmStatus.setPing(ping);
                cmStatus.setSnmpCheck(snmpCheck);
            } else if (cmService.getCmQueryMode() == CmcConstants.CM_REMOTE_QUERY) {
                cmStatus = cmService.showCmStatus(cmIp, cmId);
                cmStatus.setPing(true);
                cmStatus.setSnmpCheck(true);
            } else {
                cmStatus = new CmStatus();
            }

            cmMultipleInfo.setCmStatus(cmStatus);
            try {
                boolean isSupportRealtimeCpeQuery = deviceVersionService.isFunctionSupported(cmcId, "realtimecpe");
                cmMultipleInfo.setIsSupportRealtimeCpeQuery(isSupportRealtimeCpeQuery);
            } catch (Exception e) {
                logger.error("", e);
            }

            try {
                // 获取实时CPE列表
                List<RealtimeCpe> cpeList = cpeService.getRealCpeListByCmId(cmId);
                cmMultipleInfo.setCpeInfo(cpeList);
            } catch (Exception e) {
                logger.debug("", e);
                cmMultipleInfo.setCpeInfo(new ArrayList<RealtimeCpe>());
            }

            if (entityTypeService.isOlt(entityType)) {
                List<Alert> oltAlert = alertService.getRecentAlertByEntityIdAndLimit(Integer.MAX_VALUE, entityId);
                for (Alert alert : oltAlert) {
                    alert.setTypeName(resourceManager.getNotNullString(alert.getTypeName()));
                    if (alert.getStatus() == null || alert.getStatus() == Alert.ALERT) {
                        alert.setConfirmTimeStr("");
                    }
                }
                cmMultipleInfo.setOltAlert(oltAlert);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cmcId", cmcId);
            List<Alert> cmcAlert = cmcAlertService.getCmcAlertList(map, 0, Integer.MAX_VALUE);
            for (Alert alert : cmcAlert) {
                alert.setTypeName(resourceManager.getNotNullString(alert.getTypeName()));
                if (alert.getStatus() == null || alert.getStatus() == Alert.ALERT) {
                    alert.setConfirmTimeStr("");
                }
            }
            cmMultipleInfo.setCmcAlert(cmcAlert);
            map.clear();
            map.put("cmMac", new MacUtils(cmMac).longValue());
            map.put("startTime", df.format(new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000))));
            map.put("end", df.format(new Date(System.currentTimeMillis())));
            List<CmAct> cmActs = cmService.getCmActionInfo(map);
            // add by fanzidong,需要在展示前格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            String displayRule = uc.getMacDisplayStyle();
            for (CmAct cmAct : cmActs) {
                String formatedMac = MacUtils.convertMacToDisplayFormat(cmAct.getCmmacString(), displayRule);
                cmAct.setCmmacString(formatedMac);
            }
            cmMultipleInfo.setCmActs(cmActs);
            map.put("startTime", df.format(new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000))));
            map.put("end", df.format(new Date(System.currentTimeMillis())));
            List<CpeAct> cpeActs = cmService.getCpeActionInfoByCmMac(map);
            for (CpeAct cpeAct : cpeActs) {
                String formatedCmMac = MacUtils.convertMacToDisplayFormat(cpeAct.getCmmacString(), displayRule);
                cpeAct.setCmmacString(formatedCmMac);
                String formatedCpeMac = MacUtils.convertMacToDisplayFormat(cpeAct.getCpemacString(), displayRule);
                cpeAct.setCpemacString(formatedCpeMac);
            }
            List<CmCpeAct> cmCpeActs = new ArrayList<CmCpeAct>();
            if (cpeActs.size() > 0) {
                String mac = cpeActs.get(0).getCpemacString();
                CmCpeAct temp = new CmCpeAct();
                temp.setCpeMacString(mac);
                for (CpeAct cpeAct : cpeActs) {
                    if (mac.equalsIgnoreCase(cpeAct.getCpemacString())) {
                        temp.addCpeAct(cpeAct);
                    } else {
                        cmCpeActs.add(temp);
                        temp = new CmCpeAct();
                        temp.setCpeMacString(cpeAct.getCpemacString());
                        temp.addCpeAct(cpeAct);
                        mac = cpeAct.getCpemacString();
                    }
                }
                cmCpeActs.add(temp);
            }
            cmMultipleInfo.setCpeActs(cpeActs);
            cmMultipleInfo.setCmCpeActs(cmCpeActs);
            CmCmcRunningInfo cmCmcRunningInfo = new CmCmcRunningInfo();
            if (entityTypeService.isCmts(entityType)) {
                cmCmcRunningInfo = cpeService.getCmCmtsRunningInfo(cmId);
                cmMultipleInfo.setCmCmcRunningInfo(cmCmcRunningInfo);
            } else {
                cmCmcRunningInfo = cpeService.getCmCmcRunningInfo(cmId);
                cmMultipleInfo.setCmCmcRunningInfo(cmCmcRunningInfo);
            }
            if (entityTypeService.isOlt(entityType)) {
                CmOltRunningInfo cmOltRunningInfo = cpeService.getCmOltRunningInfo(cmId);
                cmMultipleInfo.setCmOltRunningInfo(cmOltRunningInfo);
            }
            CmFlap cmFlap = cmcFlapService.queryCmFlapInfo(cmId);
            cmMultipleInfo.setCmFlap(cmFlap);

            writeDataToAjax(JSONObject.fromObject(cmMultipleInfo));
        } catch (SnmpNoResponseException e) {
            logger.error("", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("error", "SnmpNoResponseException");
            writeDataToAjax(JSONObject.fromObject(map));
        } catch (Exception e) {
            logger.error("", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("error", e.getMessage());
            writeDataToAjax(JSONObject.fromObject(map));
        }
        return NONE;
    }

    /**
     * cmCpeQuery.jsp 获取信道可纠错误码率历史数据
     * 
     * @return
     * @throws IOException
     */
    public String showBitErrorRateHisPerf() throws IOException {
        JSONObject json = new JSONObject();
        JSONArray bitErrorHisPerfJson = new JSONArray();
        List<Point> bitErrorHisPerf = new ArrayList<Point>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (startTime != null) {
            queryMap.put("startTime", startTime);
        }
        if (endTime != null) {
            queryMap.put("endTime", endTime);
        }
        Long channelIndex;
        Long typeId = entityService.getEntity(entityId).getTypeId();
        if (entityTypeService.isCmts(typeId)) {
            channelIndex = channelId;
            cmcId = entityId;
            queryMap.put("cmcId", cmcId);
            queryMap.put("channelIndex", channelIndex);
            bitErrorHisPerf = cpeService.getCmtsBitErrorRateHisPerf(queryMap);
        } else {
            channelIndex = CmcIndexUtils.getUpChannelIndex(cmcIndex, channelId);
            cmcId = cpeService.getCmcByIndexAndEntityId(cmcIndex, entityId).getCmcId();
            queryMap.put("cmcId", cmcId);
            queryMap.put("channelIndex", channelIndex);
            bitErrorHisPerf = cpeService.getBitErrorRateHisPerf(queryMap);
        }
        if (bitErrorHisPerf != null && bitErrorHisPerf.size() > 0) {
            for (Point point : bitErrorHisPerf) {
                JSONArray tempPoint = new JSONArray();
                tempPoint.add(point.getXTime().getTime());
                tempPoint.add(point.getY());
                bitErrorHisPerfJson.add(tempPoint);
            }
        }
        json.put("bitErrorHisPerf", bitErrorHisPerfJson);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * cmCpeQuery.jsp 获取信道不可纠错误码率历史数据
     * 
     * @return
     * @throws IOException
     */
    public String showUnBitErrorRateHisPerf() throws IOException {
        JSONObject json = new JSONObject();
        JSONArray unBitErrorHisPerfJson = new JSONArray();
        List<Point> unBitErrorHisPerf = new ArrayList<Point>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (startTime != null) {
            queryMap.put("startTime", startTime);
        }
        if (endTime != null) {
            queryMap.put("endTime", endTime);
        }
        Long channelIndex;
        Long typeId = entityService.getEntity(entityId).getTypeId();
        if (entityTypeService.isCmts(typeId)) {
            channelIndex = channelId;
            cmcId = entityId;
            queryMap.put("cmcId", cmcId);
            queryMap.put("channelIndex", channelIndex);
            unBitErrorHisPerf = cpeService.getCmtsUnBitErrorRateHisPerf(queryMap);
        } else {
            channelIndex = CmcIndexUtils.getUpChannelIndex(cmcIndex, channelId);
            cmcId = cpeService.getCmcByIndexAndEntityId(cmcIndex, entityId).getCmcId();
            queryMap.put("cmcId", cmcId);
            queryMap.put("channelIndex", channelIndex);
            unBitErrorHisPerf = cpeService.getUnBitErrorRateHisPerf(queryMap);
        }
        if (unBitErrorHisPerf != null && unBitErrorHisPerf.size() > 0) {
            for (Point point : unBitErrorHisPerf) {
                JSONArray tempPoint = new JSONArray();
                tempPoint.add(point.getXTime().getTime());
                tempPoint.add(point.getY());
                unBitErrorHisPerfJson.add(tempPoint);
            }
        }
        json.put("unBitErrorHisPerf", unBitErrorHisPerfJson);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * cmCpeQuery.jsp 获取信道CM下线数历史数据
     * 
     * @return
     * @throws IOException
     */
    public String showCmOfflineNumHisPerf() throws IOException {
        JSONObject json = new JSONObject();
        JSONArray cmOfflineNumHisPerfJson = new JSONArray();
        List<Point> cmOfflineNumHisPerf = new ArrayList<Point>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (startTime != null) {
            queryMap.put("startTime", startTime);
        }
        if (endTime != null) {
            queryMap.put("endTime", endTime);
        }
        Long channelIndex;
        Long typeId = entityService.getEntity(entityId).getTypeId();
        if (entityTypeService.isCmts(typeId)) {
            channelIndex = channelId;
        } else {
            channelIndex = CmcIndexUtils.getUpChannelIndex(cmcIndex, channelId);
        }
        queryMap.put("entityId", entityId);
        queryMap.put("channelIndex", channelIndex);
        cmOfflineNumHisPerf = cpeService.getCmOfflineNumHisPerf(queryMap);
        if (cmOfflineNumHisPerf != null && cmOfflineNumHisPerf.size() > 0) {
            for (Point point : cmOfflineNumHisPerf) {
                JSONArray tempPoint = new JSONArray();
                tempPoint.add(point.getXTime().getTime());
                tempPoint.add(point.getY());
                cmOfflineNumHisPerfJson.add(tempPoint);
            }
        }
        json.put("cmOfflineNumHisPerf", cmOfflineNumHisPerfJson);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * cmCpeQuery.jsp 获取信道CM在线数历史数据
     *
     * @return
     * @throws IOException
     */
    public String showCmOnlineNumHisPerf() throws IOException {
        JSONObject json = new JSONObject();
        JSONArray cmOnlineNumHisPerfJson = new JSONArray();
        List<Point> cmOnlineNumHisPerf = new ArrayList<Point>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (startTime != null) {
            queryMap.put("startTime", startTime);
        }
        if (endTime != null) {
            queryMap.put("endTime", endTime);
        }
        Long channelIndex;
        Long typeId = entityService.getEntity(entityId).getTypeId();
        if (entityTypeService.isCmts(typeId)) {
            channelIndex = channelId;
        } else {
            channelIndex = CmcIndexUtils.getUpChannelIndex(cmcIndex, channelId);
        }
        queryMap.put("entityId", entityId);
        queryMap.put("channelIndex", channelIndex);
        cmOnlineNumHisPerf = cpeService.getCmOnlineNumHisPerf(queryMap);
        if (cmOnlineNumHisPerf != null && cmOnlineNumHisPerf.size() > 0) {
            for (Point point : cmOnlineNumHisPerf) {
                JSONArray tempPoint = new JSONArray();
                tempPoint.add(point.getXTime().getTime());
                tempPoint.add(point.getY());
                cmOnlineNumHisPerfJson.add(tempPoint);
            }
        }
        json.put("cmOnlineNumHisPerf", cmOnlineNumHisPerfJson);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取CM实时状态
     *
     * @return
     * @throws IOException
     */
    public String loadCmPreStatusOnCmts() throws IOException {
        Integer status = cpeService.loadCmPreStatusOnCmts(cmcId, cmIndex);
        writeDataToAjax(status.toString());
        return NONE;
    }

    /**
     * 获取CM实时状态
     *
     * @return
     * @throws IOException
     */
    public String loadCmStatusOnCmts() throws IOException {
        Integer status = cpeService.loadCmStatusOnCmts(cmcId, cmMac);
        writeDataToAjax(status.toString());
        return NONE;
    }

    /**
     * 展示FdbAddress页面
     * 
     * @return
     */
    public String showFdbAddress() {
        return SUCCESS;
    }

    /**
     * 加载IGMP业务模板
     * 
     * @return
     * @throws IOException
     */
    public String loadFdbAddress() throws IOException {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        Integer queryMode = cmService.getCmQueryMode();
        if (CmcConstants.CM_IMMEDIATELY_QUERY.equals(queryMode)) {
            readCommunity = cpeService.getCmReadCommunity();
            writeCommunity = cpeService.getCmWriteCommunity();
            SnmpParam snmpParam = new SnmpParam();
            snmpParam.setIpAddress(cmIp);
            snmpParam.setCommunity(readCommunity);
            snmpParam.setWriteCommunity(writeCommunity);
            snmpParam.setTimeout(5000);
            snmpParam.setMibs("BRIDGE-MIB");
            List<CmFdbTable> fdbTables = cpeService.getFdbTable(snmpParam);
            List<CmFdbTable> re = new ArrayList<>();
            for (CmFdbTable attribute : fdbTables) {
                switch (attribute.getFdbStatus()) {
                case 3:
                case 5:
                    String formatedMac = MacUtils.convertMacToDisplayFormat(attribute.getFdbAddress().toString(),
                            macRule);
                    attribute.setFdbAddressString(formatedMac);
                    re.add(attribute);
                }
            }
            JSONArray fdbAddresss = JSONArray.fromObject(re);
            writeDataToAjax(fdbAddresss);
        } else {
            List<CmFdbTableRemoteQuery> cmFdbAddresses = cpeService.getFdbTableRemoteQuery(cmId);
            List<CmFdbTableRemoteQuery> re = new ArrayList<>();
            for (CmFdbTableRemoteQuery attribute : cmFdbAddresses) {
                switch (attribute.getFdbStatus()) {
                case 3:
                case 5:
                    String formatedMac = MacUtils.convertMacToDisplayFormat(attribute.getFdbAddress().toString(),
                            macRule);
                    attribute.setFdbAddressString(formatedMac);
                    re.add(attribute);
                }
            }
            JSONArray fdbAddresss = JSONArray.fromObject(re);
            writeDataToAjax(fdbAddresss);
        }
        return NONE;
    }

    public CmCollectConfig getCmCollectConfig() {
        return cmCollectConfig;
    }

    public void setCmCollectConfig(CmCollectConfig cmCollectConfig) {
        this.cmCollectConfig = cmCollectConfig;
    }

    public CpeCollectConfig getCpeCollectConfig() {
        return cpeCollectConfig;
    }

    public void setCpeCollectConfig(CpeCollectConfig cpeCollectConfig) {
        this.cpeCollectConfig = cpeCollectConfig;
    }

    public CmCpeCollectDataPolicy getCmCpeCollectDataPolicy() {
        return cmCpeCollectDataPolicy;
    }

    public void setCmCpeCollectDataPolicy(CmCpeCollectDataPolicy cmCpeCollectDataPolicy) {
        this.cmCpeCollectDataPolicy = cmCpeCollectDataPolicy;
    }

    public JSONObject getCmCollectConfigJson() {
        return cmCollectConfigJson;
    }

    public void setCmCollectConfigJson(JSONObject cmCollectConfigJson) {
        this.cmCollectConfigJson = cmCollectConfigJson;
    }

    public JSONObject getCmCpeCollectDataPolicyJson() {
        return cmCpeCollectDataPolicyJson;
    }

    public void setCmCpeCollectDataPolicyJson(JSONObject cmCpeCollectDataPolicyJson) {
        this.cmCpeCollectDataPolicyJson = cmCpeCollectDataPolicyJson;
    }

    public JSONObject getCpeCollectConfigJson() {
        return cpeCollectConfigJson;
    }

    public void setCpeCollectConfigJson(JSONObject cpeCollectConfigJson) {
        this.cpeCollectConfigJson = cpeCollectConfigJson;
    }

    public Long getInitData() {
        return initData;
    }

    public void setInitData(Long initData) {
        this.initData = initData;
    }

    public Long getStatisticData() {
        return statisticData;
    }

    public void setStatisticData(Long statisticData) {
        this.statisticData = statisticData;
    }

    public Long getActionData() {
        return actionData;
    }

    public void setActionData(Long actionData) {
        this.actionData = actionData;
    }

    public Integer getCmCollectStatus() {
        return cmCollectStatus;
    }

    public void setCmCollectStatus(Integer cmCollectStatus) {
        this.cmCollectStatus = cmCollectStatus;
    }

    public Integer getStatisticSource() {
        return statisticSource;
    }

    public void setStatisticSource(Integer statisticSource) {
        this.statisticSource = statisticSource;
    }

    public Integer getCmTypeStatisticStatus() {
        return cmTypeStatisticStatus;
    }

    public void setCmTypeStatisticStatus(Integer cmTypeStatisticStatus) {
        this.cmTypeStatisticStatus = cmTypeStatisticStatus;
    }

    public Integer getCmActionStatisticStatus() {
        return cmActionStatisticStatus;
    }

    public void setCmActionStatisticStatus(Integer cmActionStatisticStatus) {
        this.cmActionStatisticStatus = cmActionStatisticStatus;
    }

    public Integer getCpeCollectStatus() {
        return cpeCollectStatus;
    }

    public void setCpeCollectStatus(Integer cpeCollectStatus) {
        this.cpeCollectStatus = cpeCollectStatus;
    }

    public Integer getCpeActionStatisticStatus() {
        return cpeActionStatisticStatus;
    }

    public void setCpeActionStatisticStatus(Integer cpeActionStatisticStatus) {
        this.cpeActionStatisticStatus = cpeActionStatisticStatus;
    }

    public Integer getCpeNumStatisticStatus() {
        return cpeNumStatisticStatus;
    }

    public void setCpeNumStatisticStatus(Integer cpeNumStatisticStatus) {
        this.cpeNumStatisticStatus = cpeNumStatisticStatus;
    }

    public Long getCmCollectPeriod() {
        return cmCollectPeriod;
    }

    public void setCmCollectPeriod(Long cmCollectPeriod) {
        this.cmCollectPeriod = cmCollectPeriod;
    }

    public JSONArray getAreasJson() {
        return areasJson;
    }

    public void setAreasJson(JSONArray areasJson) {
        this.areasJson = areasJson;
    }

    public JSONArray getIpEntityJson() {
        return ipEntityJson;
    }

    public void setIpEntityJson(JSONArray ipEntityJson) {
        this.ipEntityJson = ipEntityJson;
    }

    public JSONArray getCcmtsJson() {
        return ccmtsJson;
    }

    public void setCcmtsJson(JSONArray ccmtsJson) {
        this.ccmtsJson = ccmtsJson;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public JSONObject getCmCpeNumInAreaJson() {
        return cmCpeNumInAreaJson;
    }

    public void setCmCpeNumInAreaJson(JSONObject cmCpeNumInAreaJson) {
        this.cmCpeNumInAreaJson = cmCpeNumInAreaJson;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public String getQuotas() {
        return quotas;
    }

    public void setQuotas(String quotas) {
        this.quotas = quotas;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public String getCpeIp() {
        return cpeIp;
    }

    public void setCpeIp(String cpeIp) {
        this.cpeIp = cpeIp;
    }

    public String getCpeMac() {
        return cpeMac;
    }

    public void setCpeMac(String cpeMac) {
        this.cpeMac = cpeMac;
    }

    public String getCmLocation() {
        return cmLocation;
    }

    public void setCmLocation(String cmLocation) {
        this.cmLocation = cmLocation;
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(String sourcePage) {
        this.sourcePage = sourcePage;
    }

    public String getQueryMode() {
        return queryMode;
    }

    public void setQueryMode(String queryMode) {
        this.queryMode = queryMode;
    }

    public Boolean getVersionSupport() {
        return versionSupport;
    }

    public void setVersionSupport(Boolean versionSupport) {
        this.versionSupport = versionSupport;
    }

    public Boolean getOtherCmts() {
        return otherCmts;
    }

    public void setOtherCmts(Boolean otherCmts) {
        this.otherCmts = otherCmts;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArray getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(JSONArray entityTypes) {
        this.entityTypes = entityTypes;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Integer getCmPollStatus() {
        return cmPollStatus;
    }

    public void setCmPollStatus(Integer cmPollStatus) {
        this.cmPollStatus = cmPollStatus;
    }

    public Long getCmPollInterval() {
        return cmPollInterval;
    }

    public void setCmPollInterval(Long cmPollInterval) {
        this.cmPollInterval = cmPollInterval;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public String getCmCpeView() {
        return cmCpeView;
    }

    public void setCmCpeView(String cmCpeView) {
        this.cmCpeView = cmCpeView;
    }

    public Long getCmHistoryData() {
        return cmHistoryData;
    }

    public void setCmHistoryData(Long cmHistoryData) {
        this.cmHistoryData = cmHistoryData;
    }

    public String getOnuCpeInterval() {
        return onuCpeInterval;
    }

    public void setOnuCpeInterval(String onuCpeInterval) {
        this.onuCpeInterval = onuCpeInterval;
    }

    public String getOnuCpeStatus() {
        return onuCpeStatus;
    }

    public void setOnuCpeStatus(String onuCpeStatus) {
        this.onuCpeStatus = onuCpeStatus;
    }

}
