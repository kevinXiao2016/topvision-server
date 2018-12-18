/***********************************************************************
 * $Id: CmcSyslogConfigAction.java,v1.0 2013-4-26 下午9:05:17 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordTypeII;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordType;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfig;
import com.topvision.ems.cmc.syslog.service.CmcSyslogConfigService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.platform.ResourceManager;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author fanzidong
 * @created @2013-4-26-下午9:05:17
 *
 */
@Controller("cmcSyslogConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcSyslogConfigAction extends BaseAction {

    private static final long serialVersionUID = -5996494542812719623L;

    @Resource(name = "cmcSyslogConfigService")
    private CmcSyslogConfigService cmcSyslogConfigService;
    @Resource(name = "cmcService")
    private CmcService cmcService;

    private Long entityId;
    private Long cmcId;
    private Long cmcType;
    private Boolean syslogII;
    private String topCcmtsSyslogRecordType;
    private Integer topCcmtsSyslogMinEvtLvl;
    private Integer topCcmtsSyslogSwitch;
    private Integer topCcmtsSyslogMaxnum;
    private Integer topCcmtsSyslogTrotInvl;
    private Integer topCcmtsSyslogTrotTrhd;
    private Integer topCcmtsSyslogTrotMode;
    private Integer evPriority;
    private String evReporting;
    private Boolean local;
    private Boolean traps;
    private Boolean syslog;
    private Boolean localVolatile;

    private JSONObject recordTypeJson;
    private JSONObject syslogConfigJson;

    /**
     * 展示Syslog Config管理页面
     * 
     * @return String
     */
    public String showSyslogConfig() {
        cmcType = cmcService.getCmcAttributeByCmcId(cmcId).getCmcDeviceStyle();
        // 需要获取syslog是用syslog还是syslogII
        syslogII = cmcSyslogConfigService.isSupportSyslogII(entityId);
        return SUCCESS;
    }

    /**
     * 获取syslog记录方式
     * 
     * @return
     */
    public String loadSyslogRecordType() {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            List<CmcSyslogRecordType> syslogRecordTypes = cmcSyslogConfigService.getCmcEventLevel(entityId);
            json.put("recordTypeNumber", syslogRecordTypes.size());
            json.put("data", syslogRecordTypes);
            json.put("success", true);
        } catch (Exception e) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取syslog记录方式 重构后
     * 
     * @return
     */
    public String loadSyslogRecordTypeII() {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            List<CmcSyslogRecordTypeII> syslogRecordTypes = cmcSyslogConfigService.getCmcRecordTypeMinLvlII(entityId);
            json.put("recordTypeNumber", syslogRecordTypes.size());
            json.put("data", syslogRecordTypes);
            json.put("success", true);
        } catch (Exception e) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 展示修改记录方式最低事件等级页面
     *
     * @return String
     */
    public String showUpdateRecordTypeLvl() {
        recordTypeJson = new JSONObject();
        recordTypeJson.put("entityId", entityId);
        recordTypeJson.put("topCcmtsSyslogRecordType", topCcmtsSyslogRecordType);
        recordTypeJson.put("topCcmtsSyslogMinEvtLvl", topCcmtsSyslogMinEvtLvl);
        writeDataToAjax(JSONObject.fromObject(recordTypeJson));
        return SUCCESS;
    }

    /**
     * 展示修改事件等级记录方式页面
     *
     * @return String
     */
    public String showUpdateRecordTypeLvlII() {
        recordTypeJson = new JSONObject();
        recordTypeJson.put("entityId", entityId);
        recordTypeJson.put("evPriority", evPriority);
        recordTypeJson.put("local", local);
        recordTypeJson.put("traps", traps);
        recordTypeJson.put("syslog", syslog);
        recordTypeJson.put("localVolatile", localVolatile);
        writeDataToAjax(JSONObject.fromObject(recordTypeJson));
        return SUCCESS;
    }

    /**
     * 将4种记录方式的事件等级恢复为默认配置
     * 
     * @return
     */
    public String undoAllSyslogEvtLvls() {
        cmcSyslogConfigService.undoAllEventLevels(entityId);
        return NONE;
    }

    /**
     * 获取所有的事件记录方式
     * 
     * @return
     */
    public String loadAllEventLevel() {
        JSONArray json = getAllEventLevels();
        // 向页面传值
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取所有的越界处理方式
     * 
     * @return
     */
    public String loadAllTrotMode() {
        JSONArray json = getAllTrotModes();
        // 向页面传值
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 修改记录方式最低事件等级
     *
     * @return
     */
    public String updateSyslogEvtLvl() {
        cmcSyslogConfigService.updateCmcRecordEventLevel(entityId, topCcmtsSyslogRecordType, topCcmtsSyslogMinEvtLvl);
        return NONE;
    }

    /**
     * 修改事件等级的记录方式
     *
     * @return
     */
    public String updateSyslogEvtLvlII() {
        CmcSyslogRecordTypeII cmcSyslogRecordTypeII = new CmcSyslogRecordTypeII();
        cmcSyslogRecordTypeII.setEntityId(entityId);
        cmcSyslogRecordTypeII.setEvPriority(evPriority);
        cmcSyslogRecordTypeII.setLocal(local);
        cmcSyslogRecordTypeII.setTraps(traps);
        cmcSyslogRecordTypeII.setSyslog(syslog);
        cmcSyslogRecordTypeII.setLocalVolatile(localVolatile);
        cmcSyslogConfigService.updateCmcRecordEventLevelII(cmcSyslogRecordTypeII);
        return NONE;
    }

    /**
     * 修改8800B的syslog配置参数
     * 
     * @return
     */
    public String updateCmcSyslogConfig() {
        CmcSyslogConfig cmcSyslogConfig = new CmcSyslogConfig();
        cmcSyslogConfig.setEntityId(entityId);
        cmcSyslogConfig.setTopCcmtsSyslogSwitch(topCcmtsSyslogSwitch);
        cmcSyslogConfig.setTopCcmtsSyslogMaxnum(topCcmtsSyslogMaxnum);
        cmcSyslogConfig.setTopCcmtsSyslogTrotInvl(topCcmtsSyslogTrotInvl);
        cmcSyslogConfig.setTopCcmtsSyslogTrotTrhd(topCcmtsSyslogTrotTrhd);
        cmcSyslogConfig.setTopCcmtsSyslogTrotMode(topCcmtsSyslogTrotMode);
        cmcSyslogConfigService.updateCmcSyslogConfig(cmcSyslogConfig);
        return NONE;
    }

    /**
     * 修改8800A的syslog配置参数
     * 
     * @return
     */
    public String update8800ACmcSyslogConfig() {
        CmcSyslogConfig cmcSyslogConfig = new CmcSyslogConfig();
        cmcSyslogConfig.setEntityId(entityId);
        cmcSyslogConfig.setTopCcmtsSyslogMaxnum(topCcmtsSyslogMaxnum);
        cmcSyslogConfig.setTopCcmtsSyslogTrotInvl(topCcmtsSyslogTrotInvl);
        cmcSyslogConfig.setTopCcmtsSyslogTrotTrhd(topCcmtsSyslogTrotTrhd);
        cmcSyslogConfig.setTopCcmtsSyslogTrotMode(topCcmtsSyslogTrotMode);
        cmcSyslogConfigService.updateCmcSyslogConfig(cmcSyslogConfig);
        return NONE;
    }

    /**
     * 刷新设备数据
     * 
     * @return
     */
    public String refreshEntityData() {
        CmcSyslogConfig cmcSyslogConfig = cmcSyslogConfigService.getSyslogConfigFromEntity(entityId);
        writeDataToAjax(JSONObject.fromObject(cmcSyslogConfig));
        return NONE;
    }

    public String loadSyslogParams() {
        CmcSyslogConfig cmcSyslogConfig = cmcSyslogConfigService.getCmcSyslogConfig(entityId);
        writeDataToAjax(JSONObject.fromObject(cmcSyslogConfig));
        return NONE;
    }

    private JSONArray getAllEventLevels() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.cmc.resources");
        JSONArray json = new JSONArray();
        JSONObject o1 = new JSONObject();
        o1.put("eventIndex", CmcConstants.EVTLVL_EMERGENCY);
        o1.put("event", resourceManager.getString("syslog.emergencyLevel"));
        json.add(o1);
        JSONObject o2 = new JSONObject();
        o2.put("eventIndex", CmcConstants.EVTLVL_ALERT);
        o2.put("event", resourceManager.getString("syslog.alertLevel"));
        json.add(o2);
        JSONObject o3 = new JSONObject();
        o3.put("eventIndex", CmcConstants.EVTLVL_CRITICAL);
        o3.put("event", resourceManager.getString("syslog.criticalLevel"));
        json.add(o3);
        JSONObject o4 = new JSONObject();
        o4.put("eventIndex", CmcConstants.EVTLVL_ERROR);
        o4.put("event", resourceManager.getString("syslog.errorLevel"));
        json.add(o4);
        JSONObject o5 = new JSONObject();
        o5.put("eventIndex", CmcConstants.EVTLVL_WARNING);
        o5.put("event", resourceManager.getString("syslog.warningLevel"));
        json.add(o5);
        JSONObject o6 = new JSONObject();
        o6.put("eventIndex", CmcConstants.EVTLVL_NOTIFICATION);
        o6.put("event", resourceManager.getString("syslog.notificationLevel"));
        json.add(o6);
        JSONObject o7 = new JSONObject();
        o7.put("eventIndex", CmcConstants.EVTLVL_INFORMATIONAL);
        o7.put("event", resourceManager.getString("syslog.informationalLevel"));
        json.add(o7);
        JSONObject o8 = new JSONObject();
        o8.put("eventIndex", CmcConstants.EVTLVL_DEBUG);
        o8.put("event", resourceManager.getString("syslog.debugLevel"));
        json.add(o8);
        JSONObject o28 = new JSONObject();
        o28.put("eventIndex", CmcConstants.EVTLVL_NONE);
        o28.put("event", resourceManager.getString("syslog.noneLevel"));
        json.add(o28);
        return json;
    }

    private JSONArray getAllTrotModes() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.cmc.resources");
        JSONArray json = new JSONArray();
        JSONObject o1 = new JSONObject();
        o1.put("trotModeIndex", CmcConstants.UNCONSTRAINED);
        o1.put("trotMode", resourceManager.getString("syslog.unconstranined"));
        json.add(o1);
        JSONObject o2 = new JSONObject();
        o2.put("trotModeIndex", CmcConstants.MAINTAINBELOWTHRESHOLD);
        o2.put("trotMode", resourceManager.getString("syslog.belowThreshold"));
        json.add(o2);
        JSONObject o3 = new JSONObject();
        o3.put("trotModeIndex", CmcConstants.STOPATTHRESHOLD);
        o3.put("trotMode", resourceManager.getString("syslog.stopAtThreshold"));
        json.add(o3);
        JSONObject o4 = new JSONObject();
        o4.put("trotModeIndex", CmcConstants.INHIBITED);
        o4.put("trotMode", resourceManager.getString("syslog.inhibited"));
        json.add(o4);
        return json;
    }

    public CmcSyslogConfigService getCmcSyslogConfigService() {
        return cmcSyslogConfigService;
    }

    public void setCmcSyslogConfigService(CmcSyslogConfigService cmcSyslogConfigService) {
        this.cmcSyslogConfigService = cmcSyslogConfigService;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getTopCcmtsSyslogRecordType() {
        return topCcmtsSyslogRecordType;
    }

    public void setTopCcmtsSyslogRecordType(String topCcmtsSyslogRecordType) {
        this.topCcmtsSyslogRecordType = topCcmtsSyslogRecordType;
    }

    public JSONObject getRecordTypeJson() {
        return recordTypeJson;
    }

    public void setRecordTypeJson(JSONObject recordTypeJson) {
        this.recordTypeJson = recordTypeJson;
    }

    public int getTopCcmtsSyslogMinEvtLvl() {
        return topCcmtsSyslogMinEvtLvl;
    }

    public void setTopCcmtsSyslogMinEvtLvl(int topCcmtsSyslogMinEvtLvl) {
        this.topCcmtsSyslogMinEvtLvl = topCcmtsSyslogMinEvtLvl;
    }

    public JSONObject getSyslogConfigJson() {
        return syslogConfigJson;
    }

    public void setSyslogConfigJson(JSONObject syslogConfigJson) {
        this.syslogConfigJson = syslogConfigJson;
    }

    public int getTopCcmtsSyslogSwitch() {
        return topCcmtsSyslogSwitch;
    }

    public Integer getTopCcmtsSyslogMaxnum() {
        return topCcmtsSyslogMaxnum;
    }

    public void setTopCcmtsSyslogMaxnum(Integer topCcmtsSyslogMaxnum) {
        this.topCcmtsSyslogMaxnum = topCcmtsSyslogMaxnum;
    }

    public Integer getTopCcmtsSyslogTrotInvl() {
        return topCcmtsSyslogTrotInvl;
    }

    public void setTopCcmtsSyslogTrotInvl(Integer topCcmtsSyslogTrotInvl) {
        this.topCcmtsSyslogTrotInvl = topCcmtsSyslogTrotInvl;
    }

    public Integer getTopCcmtsSyslogTrotTrhd() {
        return topCcmtsSyslogTrotTrhd;
    }

    public void setTopCcmtsSyslogTrotTrhd(Integer topCcmtsSyslogTrotTrhd) {
        this.topCcmtsSyslogTrotTrhd = topCcmtsSyslogTrotTrhd;
    }

    public Integer getTopCcmtsSyslogTrotMode() {
        return topCcmtsSyslogTrotMode;
    }

    public void setTopCcmtsSyslogTrotMode(Integer topCcmtsSyslogTrotMode) {
        this.topCcmtsSyslogTrotMode = topCcmtsSyslogTrotMode;
    }

    public void setTopCcmtsSyslogMinEvtLvl(Integer topCcmtsSyslogMinEvtLvl) {
        this.topCcmtsSyslogMinEvtLvl = topCcmtsSyslogMinEvtLvl;
    }

    public void setTopCcmtsSyslogSwitch(Integer topCcmtsSyslogSwitch) {
        this.topCcmtsSyslogSwitch = topCcmtsSyslogSwitch;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public Long getCmcType() {
        return cmcType;
    }

    public void setCmcType(Long cmcType) {
        this.cmcType = cmcType;
    }

    public Integer getEvPriority() {
        return evPriority;
    }

    public void setEvPriority(Integer evPriority) {
        this.evPriority = evPriority;
    }

    public String getEvReporting() {
        return evReporting;
    }

    public void setEvReporting(String evReporting) {
        this.evReporting = evReporting;
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public Boolean getTraps() {
        return traps;
    }

    public void setTraps(Boolean traps) {
        this.traps = traps;
    }

    public Boolean getSyslog() {
        return syslog;
    }

    public void setSyslog(Boolean syslog) {
        this.syslog = syslog;
    }

    public Boolean getLocalVolatile() {
        return localVolatile;
    }

    public void setLocalVolatile(Boolean localVolatile) {
        this.localVolatile = localVolatile;
    }

    public Boolean getSyslogII() {
        return syslogII;
    }

    public void setSyslogII(Boolean syslogII) {
        this.syslogII = syslogII;
    }

}
