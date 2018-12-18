/***********************************************************************
 * $Id: Cmc_bRouteAction.java,v1.0 2013-8-6 下午4:40:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.sql.Timestamp;
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

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.domain.CmcCmNumStatic;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;
import com.topvision.platform.util.highcharts.HighChartsUtils;
import com.topvision.platform.util.highcharts.domain.Highcharts;

import net.sf.json.JSONObject;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcPortalAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcAction.class);
    @Autowired
    private CmcService cmcService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcPerfService cmcPerfService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    private CmcAttribute cmcAttribute;
    private Entity entity;
    private Long entityId;
    private Long cmcId;
    private SnmpParam snmpParam;
    private Integer productType;
    private Entity cmts;

    private Integer cmcType;
    private String cmcTypeString;
    private Long sysUpTime;
    private Boolean isSurportedGoogleMap;
    private JSONObject chartParam;
    private JSONObject cmcAttrJson;

    private JSONObject viewerParam;
    private String nodePath;
    private String timeType;
    private String perfType;
    private Long index;
    private String st;
    private String et;
    private CmcCmNumStatic cmcCmNumStatic;
    private String cmcUpLinkPonPort;
    private String redirectURL;
    private String filename;// 进行下载配置文件时的文件名称
    // 用于设备快照页用户视图保存
    private String leftPartItems;
    private String rightPartItems;
    private String cmcIcon;// add by loyal cmc设备图标

    private JSONObject supportFuncs = new JSONObject();

    private String formatedMac;

    /**
     * 显示拆分型cmc快照
     * 
     * @return String
     */
    public String entityPortal() {
        cmcId = entityId;
        snmpParam = cmcService.getSnmpParamByCmcId(cmcId);
        if (cmcId == null) {
            return "disconnect";
        }
        return SUCCESS;
    }

    /**
     * 显示cmc快照,整理后的
     * 
     * @return String
     */
    public String showCmcPortal() {
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        entity = entityService.getEntity(entityId);
        cmts=entityService.getEntity(cmcId);
        cmcIcon = "/images/" + entityService.getEntity(cmcId).getIcon64();// add by loyal
        // 对于拆分型，entity代表olt，故需要单独传递icon到前台
        if (!entity.isStatus()) {
            redirectURL = String.format("%s/entityPortalCancel.tv?module=1&entityId=%d", entity.getModulePath(),
                    entityId);
            return "unManaged";
        } else {
            // add by fanzidong, 格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
            Timestamp statusChangeTime = cmcAttribute.getStatusChangeTime();
            if (statusChangeTime != null) {
                String statusChangeTimeStr = DateUtils.getTimeDesInObscure(
                        System.currentTimeMillis() - statusChangeTime.getTime(), uc.getUser().getLanguage());
                cmcAttribute.setStatusChangeTimeStr(statusChangeTimeStr);
            }
            String displayRule = uc.getMacDisplayStyle();
            formatedMac = MacUtils.convertMacToDisplayFormat(cmcAttribute.getTopCcmtsSysMacAddr(), displayRule);

            getToolsSupportFunc(cmcId, cmcService);
            if (cmcAttribute != null) {
                cmcAttrJson = JSONObject.fromObject(cmcAttribute);
                cmcCmNumStatic = cmcPerfService.getCmcCmNumStatic(cmcId);
                try {
                    if (cmcAttribute.getTopCcmtsSysStatus() != null && cmcAttribute.getTopCcmtsSysUpTime() != null) {
                        Long tempTime = System.currentTimeMillis();
                        Long dt = cmcAttribute.getDt().getTime();
                        if (CmcConstants.TOPCCMTSSYSSTATUS_OFFLINE.equals(cmcAttribute.getTopCcmtsSysStatus())) {
                            sysUpTime = (long) -1;
                        } else {
                            // TODO merge by Victor@20130816需要删除，以后代码不要这样写死，类似需要做到可配
                            if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
                                /*
                                 * Long entityId = cmcService.getEntityIdByCmcId(cmcId); String
                                 * oltVersion = entityService.getDeviceVersion(entityId); if
                                 * (oltVersion.toUpperCase().indexOf("1.6.9.9-P4") > 0) {
                                 * cmcAttribute
                                 * .setTopCcmtsSysUpTime(cmcAttribute.getTopCcmtsSysUpTime() * 100);
                                 * }
                                 */
                            }
                            sysUpTime = cmcAttribute.getTopCcmtsSysUpTime() / 100 + (tempTime - dt) / 1000;
                        }
                    } else {
                        sysUpTime = (long) -1;
                    }
                } catch (Exception e) {
                    logger.debug("CMC SysUpTime error:{}", e);
                }
                cmcType = cmcAttribute.getCmcDeviceStyle().intValue();
                // 如果设备为8800A，则取得其上联端口在portal页显示
                if (entityTypeService.isCcmtsWithoutAgent(cmcAttribute.getCmcDeviceStyle())) {
                    Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
                    Long slotNo = CmcIndexUtils.getSlotNo(cmcIndex);
                    Long ponNo = CmcIndexUtils.getPonNo(cmcIndex);
                    cmcUpLinkPonPort = slotNo.toString() + Symbol.SLASH + ponNo.toString();
                }
                productType = cmcType;
                cmcTypeString = entityTypeService.getEntityType(cmcType).getDisplayName();
                isSurportedGoogleMap = true;

                timeType = ViewerParam.TODAY;
                entityId = cmcService.getEntityIdByCmcId(cmcId);
                viewerParam = JSONObject.fromObject(getPerfViewerParam());
                entity = entityService.getEntity(entityId);
                nodePath = entity.getDisplayName();

                // request.setAttribute("xxx", versionFunction);
                Highcharts highcharts = HighChartsUtils.createDefaultLineXdateTimeChart("cpuAndMemHis",
                        ResourcesUtil.getString("CCMTS.todayCPUMemUtilizationGraph"),
                        ResourcesUtil.getString("CCMTS.CPUMemUtilization") + "(%)", null, 300);
                highcharts.getChart().setMarginRight(50);
                highcharts.getCredits().setEnabled(false);
                highcharts.getyAxis().get(0).setMax(100D);
                highcharts.getyAxis().get(0).setMin(0D);
                highcharts.getxAxis().get(0).setMin(getPerfViewerParam().getStLong().doubleValue());
                highcharts.getxAxis().get(0).setMax(getPerfViewerParam().getEtLong().doubleValue());
                chartParam = HighChartsUtils.toJSONObject(highcharts);
                // 加载用户保存的设备快照视图
                leftPartItems = this.getPortalView(cmcType).getProperty("portalLeftPart");
                rightPartItems = this.getPortalView(cmcType).getProperty("portalRightPart");
                return SUCCESS;
            } else { // 设备信息未加载完
                return "notComplete";
            }
        }
    }

    /**
     * 显示cmc基本信息tab页面
     * 
     * @return String
     */
    public String showCmcEntityById() {
        entity = entityService.getEntity(entityId);
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        return SUCCESS;
    }

    /**
     * 取消管理后CMC设备快照
     * 
     * @return String
     */
    public String showCmcCancelJsp() {
        entity = entityService.getEntity(entityId);
        cmcId = cmcService.getCmcIdByEntityId(entityId);
        cmcType = cmcService.getCmcTypeByCmcId(cmcId);
        cmcTypeString = entityTypeService.getEntityType(cmcType).getDisplayName();
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        return SUCCESS;
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

    private void getToolsSupportFunc(Long cmcId, CmcService cmcService) {
        String[] functions = { "baseInfoCfg", "syslog", "docsisConfig", "upgrade", "configFile", "clearConfig",
                "trapServer", "flap", "systemIp", "systemTime", "staticRoute", "opticalReceiverRead", "ipqam",
                "opticalReceiverSet", "cmUpgrade", "autoClearCm" };
        Map<String, Boolean> map = deviceVersionService.isFunctionSupported(cmcId, functions);
        supportFuncs.putAll(map);

    }

    /**
     * 保存用户设置的设备快照页面视图
     * 
     * @author flackyang
     * @since 2013-11-07
     * @return
     */
    public String savePortalView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties portalView = new Properties();
        portalView.setProperty("portalLeftPart", leftPartItems);
        portalView.setProperty("portalRightPart", rightPartItems);
        userPreferencesService.batchSaveModulePreferences(Integer.toString(cmcType), uc.getUserId(), portalView);
        return NONE;
    }

    /**
     * 获取用户保存的设备快照页面视图
     * 
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

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
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

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public Boolean getIsSurportedGoogleMap() {
        return isSurportedGoogleMap;
    }

    public void setIsSurportedGoogleMap(Boolean isSurportedGoogleMap) {
        this.isSurportedGoogleMap = isSurportedGoogleMap;
    }

    public JSONObject getChartParam() {
        return chartParam;
    }

    public void setChartParam(JSONObject chartParam) {
        this.chartParam = chartParam;
    }

    public JSONObject getCmcAttrJson() {
        return cmcAttrJson;
    }

    public void setCmcAttrJson(JSONObject cmcAttrJson) {
        this.cmcAttrJson = cmcAttrJson;
    }

    public JSONObject getViewerParam() {
        return viewerParam;
    }

    public void setViewerParam(JSONObject viewerParam) {
        this.viewerParam = viewerParam;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
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

    public CmcCmNumStatic getCmcCmNumStatic() {
        return cmcCmNumStatic;
    }

    public void setCmcCmNumStatic(CmcCmNumStatic cmcCmNumStatic) {
        this.cmcCmNumStatic = cmcCmNumStatic;
    }

    public String getCmcUpLinkPonPort() {
        return cmcUpLinkPonPort;
    }

    public void setCmcUpLinkPonPort(String cmcUpLinkPonPort) {
        this.cmcUpLinkPonPort = cmcUpLinkPonPort;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Logger getLogger() {
        return logger;
    }

    public JSONObject getSupportFuncs() {
        return supportFuncs;
    }

    public void setSupportFuncs(JSONObject supportFuncs) {
        this.supportFuncs = supportFuncs;
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

    public String getFormatedMac() {
        return formatedMac;
    }

    public void setFormatedMac(String formatedMac) {
        this.formatedMac = formatedMac;
    }

    public String getCmcIcon() {
        return cmcIcon;
    }

    public void setCmcIcon(String cmcIcon) {
        this.cmcIcon = cmcIcon;
    }

    public Entity getCmts() {
        return cmts;
    }

    public void setCmts(Entity cmts) {
        this.cmts = cmts;
    }

}