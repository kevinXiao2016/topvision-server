/***********************************************************************
 * $Id: CmtsBaseAction.java,v1.0 2013-7-20 上午10:54:07 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

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
import com.topvision.ems.cmts.service.CmtsBaseService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;

/**
 * 基础功能
 * 
 * @author jay
 * @created @2013-7-20-上午10:54:07
 */
@Controller("cmtsBaseAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsBaseAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private Logger logger = LoggerFactory.getLogger(CmtsBaseAction.class);
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "cmtsBaseService")
    private CmtsBaseService cmtsBaseService;
    @Autowired
    private UserPreferencesService userPreferencesService;

    private Long entityId;
    private Long cmcId;
    private Entity entity;
    private CmcAttribute cmcAttribute;
    private JSONObject cmcAttrJson;
    private Long sysUpTime;
    private Long cmtsType;
    private String cmcTypeString;
    private String connectPerson;
    private String sysLocation;
    private String cmcName;
    private String cmcIp;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;
    private int start;
    private int limit;
    private SnmpParam snmpParam;
    private JSONObject snmpParamJson;
    private int version;
    private String community;
    private String writeCommunity;
    private String entityName;
    private Long cmcIndex;
    private Long deviceType;
    private String authProtocol;
    private String privProtocol;
    private String authPassword;
    private String privPassword;
    private String userName;
    private CmcCmNumStatic cmcCmNumStatic;
    private String source = "";
    // 用于设备快照页用户视图保存
    private String cmtsPortalLeft;
    private String cmtsProtalRight;
    private Integer operationResult;
 
    private Long channelIndex;//added by wubo  2017.01.22 

	/**
     * 显示拆分型cmc快照
     * 
     * @return String
     */
    public String entityPortal() {
        cmcId = entityId;
        snmpParam = entityService.getSnmpParamByEntity(entityId);
        return SUCCESS;
    }

    /**
     * 显示Cmts列表页面
     * 
     * @return String
     */
    public String showAllCmtsList() {
        return SUCCESS;
    }

    /**
     * 显示Cmts配置页面
     * 
     * @return String
     */
    public String showCmtsConfig() {
        snmpParam = entityService.getSnmpParamByEntity(cmcId);
        snmpParamJson = JSONObject.fromObject(snmpParam);
        return SUCCESS;
    }

    /**
     * 显示Cmts基本信息配置页面
     * 
     * @return String
     */
    public String showCmtsBasicInfoConfig() {
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        entityName = entityService.getEntity(cmcId).getName();
        cmcAttrJson = JSONObject.fromObject(cmcAttribute);
        return SUCCESS;
    }

    /**
     * 显示cmc快照
     * 
     * @return String
     */
    public String showCmtsPortal() {
        entity = entityService.getEntity(cmcId);
        entity.setIcon("/images/" + entity.getIcon64());
        cmcAttribute = cmtsBaseService.getCmtsSystemBasicInfoByCmcId(cmcId);
        EntitySnap snap = cmtsBaseService.getCmtsSnapByEntityId(cmcId);
        cmcCmNumStatic = cmcPerfService.getCmcCmNumStatic(cmcId);
        if (cmcAttribute != null) {
            cmcAttrJson = JSONObject.fromObject(cmcAttribute);
            try {
                if (snap.isState() != null && cmcAttribute.getTopCcmtsSysUpTime() != null) {
                    Long tempTime = System.currentTimeMillis();
                    Long dt = cmcAttribute.getDt().getTime();
                    sysUpTime = cmcAttribute.getTopCcmtsSysUpTime() * 10 + (tempTime - dt);
                } else {
                    sysUpTime = (long) -1;
                }
            } catch (Exception e) {
                logger.debug("CMC SysUpTime error:{}", e);
            }
            // 获取用户设置的设备快照页自定义视图
            cmtsPortalLeft = this.getCmtsPortalView().getProperty("cmtsPortalLeft");
            cmtsProtalRight = this.getCmtsPortalView().getProperty("cmtsProtalRight");
            return SUCCESS;
        } else {
            return "notComplete";
        }
    }

    /**
     * 查询cmc列表
     * 
     * @return String
     */
    public String queryCmtsList() {
        Map<String, Object> cmcQueryMap = new HashMap<String, Object>();
        cmcQueryMap.put("entityType", entityTypeService.getCmtsType());
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmcAttribute> cmcAttributeList = new ArrayList<CmcAttribute>();
        Long cmcNum = 0l;
        cmcQueryMap.put("sort", sort);
        cmcQueryMap.put("dir", dir);
        if (connectPerson != null) {
            cmcQueryMap.put("connectPerson", connectPerson);
        }
        if (cmcName != null) {
            cmcQueryMap.put("cmcName", cmcName);
        }
        if (cmcIp != null) {
            cmcQueryMap.put("cmcIp", cmcIp);
        }
        cmcAttributeList = cmtsBaseService.queryCmtsList(cmcQueryMap, start, limit);
        // add by fanzidong, 需要在展示之前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmcAttribute cmc : cmcAttributeList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmc.getTopCcmtsSysMacAddr(), macRule);
            cmc.setTopCcmtsSysMacAddr(formatedMac);
            cmtsBaseService.analyseSofewareVersion(cmc);
        }
        cmcNum = cmtsBaseService.queryCmtsNum(cmcQueryMap);
        json.put("data", cmcAttributeList);
        json.put("rowCount", cmcNum);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 刷新CC设备和设备上的相关信息
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmtsBaseAction", operationName = "refreshCmts")
    public String refreshCmts() {
        String message;
        try {
            cmtsBaseService.refreshCmts(entityId);
            operationResult = OperationLog.SUCCESS;
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取CMC运行时长
     * 
     * @return String
     * @throws java.io.IOException
     *             Response流异常
     */
    public String getCmtsUptimeByEntity() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            CmcAttribute cmcAttribute = cmtsBaseService.getCmtsSystemBasicInfoByCmcId(cmcId);
            EntitySnap snap = cmtsBaseService.getCmtsSnapByEntityId(cmcId);
            if (snap.isState() != null && snap.isState() && cmcAttribute.getTopCcmtsSysUpTime() != null) {
                Long tempTime = System.currentTimeMillis();
                Long dt = cmcAttribute.getDt().getTime();
                sysUpTime = cmcAttribute.getTopCcmtsSysUpTime() * 10 + (tempTime - dt);
            } else {
                sysUpTime = (long) -1;
            }
        } catch (Exception e) {
            sysUpTime = (long) -1;
            logger.error("", e);
        }
        json.put("sysUpTime", sysUpTime);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 修改cmts基本信息
     * 
     * @return
     */
    public String modifyCmtsBasicInfo() {
        String message;
        try {
            cmtsBaseService.renameEntityName(cmcId, entityName);
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
     * 修改cmts snmp参数
     * 
     * @return
     */
    public String modifyCmtsSnmpParam() {
        String message;
        try {
            SnmpParam param = new SnmpParam();
            param.setEntityId(cmcId);
            param.setVersion(version);
            param.setCommunity(community);
            param.setWriteCommunity(writeCommunity);
            param.setAuthProtocol(authProtocol);
            param.setPrivProtocol(privProtocol);
            param.setPrivPassword(privPassword);
            param.setAuthPassword(authPassword);
            param.setUsername(userName);
            entityService.updateSnmpParam(param);
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
     * 显示性能页面
     * 
     * @return
     */
    public String showCmtsCurPerf() {
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Entity entity = entityService.getEntity(cmcId);
        entityName = entity.getName();
        deviceType = entity.getTypeId();
        return SUCCESS;
    }

    /**
     * 保存用户设置的设备快照页面视图
     * 
     * @author flackyang
     * @since 2013-11-12
     * @return
     */
    public String saveCmtsPortalView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        Properties portalView = new Properties();
        portalView.setProperty("cmtsPortalLeft", cmtsPortalLeft);
        portalView.setProperty("cmtsProtalRight", cmtsProtalRight);
        userPreferencesService.batchSaveModulePreferences("cmtsPortalView", uc.getUserId(), portalView);
        return NONE;
    }

    /**
     * 获取用户保存的设备快照页面视图
     * 
     * @author flackyang
     * @since 2013-11-12
     * @return
     */
    private Properties getCmtsPortalView() {
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences userPre = new UserPreferences();
        userPre.setModule("cmtsPortalView");
        userPre.setUserId(uc.getUserId());
        Properties portalView = userPreferencesService.getModulePreferences(userPre);
        return portalView;
    }

    public CmtsBaseService getCmtsBaseService() {
        return cmtsBaseService;
    }

    public void setCmtsBaseService(CmtsBaseService cmtsBaseService) {
        this.cmtsBaseService = cmtsBaseService;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public JSONObject getCmcAttrJson() {
        return cmcAttrJson;
    }

    public void setCmcAttrJson(JSONObject cmcAttrJson) {
        this.cmcAttrJson = cmcAttrJson;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmcTypeString() {
        return cmcTypeString;
    }

    public void setCmcTypeString(String cmcTypeString) {
        this.cmcTypeString = cmcTypeString;
    }

    public Long getCmtsType() {
        return cmtsType;
    }

    public void setCmtsType(Long cmtsType) {
        this.cmtsType = cmtsType;
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

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public String getConnectPerson() {
        return connectPerson;
    }

    public void setConnectPerson(String connectPerson) {
        this.connectPerson = connectPerson;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public JSONObject getSnmpParamJson() {
        return snmpParamJson;
    }

    public void setSnmpParamJson(JSONObject snmpParamJson) {
        this.snmpParamJson = snmpParamJson;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public String getAuthProtocol() {
        return authProtocol;
    }

    public void setAuthProtocol(String authProtocol) {
        this.authProtocol = authProtocol;
    }

    public String getPrivProtocol() {
        return privProtocol;
    }

    public void setPrivProtocol(String privProtocol) {
        this.privProtocol = privProtocol;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getPrivPassword() {
        return privPassword;
    }

    public void setPrivPassword(String privPassword) {
        this.privPassword = privPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CmcCmNumStatic getCmcCmNumStatic() {
        return cmcCmNumStatic;
    }

    public void setCmcCmNumStatic(CmcCmNumStatic cmcCmNumStatic) {
        this.cmcCmNumStatic = cmcCmNumStatic;
    }

    public CmcPerfService getCmcPerfService() {
        return cmcPerfService;
    }

    public void setCmcPerfService(CmcPerfService cmcPerfService) {
        this.cmcPerfService = cmcPerfService;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCmtsPortalLeft() {
        return cmtsPortalLeft;
    }

    public void setCmtsPortalLeft(String cmtsPortalLeft) {
        this.cmtsPortalLeft = cmtsPortalLeft;
    }

    public String getCmtsProtalRight() {
        return cmtsProtalRight;
    }

    public void setCmtsProtalRight(String cmtsProtalRight) {
        this.cmtsProtalRight = cmtsProtalRight;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public Long getChannelIndex() {
		return channelIndex;
	}

	public void setChannelIndex(Long channelIndex) {
		this.channelIndex = channelIndex;
	}
}