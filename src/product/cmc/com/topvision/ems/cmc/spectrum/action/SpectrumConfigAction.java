/***********************************************************************
 * $Id: SpectrumConfigAction.java,v1.0 2014-1-13 上午9:18:05 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.spectrum.domain.CmtsSpectrumConfig;
import com.topvision.ems.cmc.spectrum.exception.OltSwitchOffException;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;
import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import com.topvision.ems.cmc.spectrum.service.SpectrumMonitorService;
import com.topvision.ems.cmc.spectrum.service.SpectrumRecordingService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * @author haojie
 * @created @2014-1-13-上午9:18:05
 * 
 */
@Controller("spectrumConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpectrumConfigAction extends BaseAction {
    private static final long serialVersionUID = -8321878394874453470L;
    private final Logger logger = LoggerFactory.getLogger(SpectrumConfigAction.class);
    @Resource(name = "spectrumConfigService")
    private SpectrumConfigService spectrumConfigService;
    @Resource(name = "spectrumRecordingService")
    private SpectrumRecordingService spectrumRecordingService;
    @Resource(name = "spectrumMonitorService")
    private SpectrumMonitorService spectrumMonitorService;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "entityService")
    private EntityService entityService;
    private Long hisCollectCycle; // 采集周期
    private Long hisCollectDuration; // 采集时长
    private Integer hisTimeInterval; // 历史采集步长
    private Integer timeInterval;// 实时采集步长
    private Long timeLimit;// 观看时限
    private String cmtsName;
    private String cmtsIp;
    private String typeId;
    private String oltCollectSwitch;
    private String cmtsCollectSwitch;
    private String hisVideoSwitch;
    private List<Long> cmcIds;
    private String oltName;
    private Long entityId;
    private String entityIp;
    private Long[] entityIds;
    private String dwrId;// 用于DWR推送的ID
    private Long cmcId;
    private Entity entity;
    private JSONArray cmcTypes = new JSONArray();

    /**
     * 批量开启OLT频谱开关
     * 
     * @return
     */
    public String startSpectrumSwitchOlt() {
        String result = "";
        try {
            spectrumConfigService.startSpectrumSwitchOlt(entityIds, dwrId);
            result = "success";
        } catch (Exception e) {
            logger.debug("startSpectrumSwitchOlt errer", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 批量关闭OLT频谱开关
     * 
     * @return
     */
    public String stopSpectrumSwitchOlt() {
        String result = "";
        try {
            spectrumConfigService.stopSpectrumSwitchOlt(entityIds, dwrId);
            result = "success";
        } catch (Exception e) {
            logger.debug("stopSpectrumSwitchOlt errer", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 批量刷新OLT频谱开关状态
     * 
     * @return
     */
    public String refreshSpectrumSwitchOlt() {
        String result = "";
        try {
            spectrumConfigService.refreshSpectrumSwitchOlt(entityIds, dwrId);
            result = "success";
        } catch (Exception e) {
            logger.debug("refreshSpectrumSwitchOlt errer", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 获取CMTS频谱配置信息，包括CMTS频谱开关列表，历史频谱参数，全局采集时间间隔和最长观看时间信息
     * 
     * @return
     */
    public String loadSpectrumCmts() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", super.getStart());
        map.put("limit", super.getLimit());
        if (this.cmtsName != null && this.cmtsName.length() > 0) {
            map.put("cmtsName", this.cmtsName);
        }
        if (this.cmtsIp != null && this.cmtsIp.length() > 0) {
            map.put("cmtsIp", this.cmtsIp);
        }
        if (this.typeId != null && !this.typeId.equals("-1")) {
            map.put("typeId", this.typeId);
        }
        if (this.oltCollectSwitch != null && !this.oltCollectSwitch.equals("-1")) {
            map.put("oltCollectSwitch", this.oltCollectSwitch);
        }
        if (this.cmtsCollectSwitch != null && !this.cmtsCollectSwitch.equals("-1")) {
            map.put("cmtsCollectSwitch", this.cmtsCollectSwitch);
        }
        if (this.hisVideoSwitch != null && !this.hisVideoSwitch.equals("-1")) {
            map.put("hisVideoSwitch", this.hisVideoSwitch);
        }
        List<CmtsSpectrumConfig> configs = spectrumConfigService.getCmtsSpectrumConfig(map);
        Long size = spectrumConfigService.getCmtsSpectrumConfigCount(map);
        json.put("data", configs);
        json.put("rowCount", size);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取OLT频谱配置信息，主要为OLT频谱开关和一些设备基本信息
     * 
     * @return
     */
    public String loadSpectrumOlt() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", super.getStart());
        map.put("limit", super.getLimit());
        if (this.oltName != null && this.oltName.length() > 0) {
            map.put("oltName", this.oltName);
        }
        if (this.entityIp != null && this.entityIp.length() > 0) {
            map.put("entityIp", this.entityIp);
        }
        if (this.oltCollectSwitch != null && !this.oltCollectSwitch.equals("-1")) {
            map.put("oltCollectSwitch", this.oltCollectSwitch);
        }
        List<SpectrumOltSwitch> configs = spectrumConfigService.getOltSpectrumConfig(map);
        Long size = spectrumConfigService.getOltSpectrumConfigCount(map);
        json.put("data", configs);
        json.put("rowCount", size);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 批量开启CMTS频谱采集开关（网管侧）
     * 
     * @return
     */
    public String startSpectrumSwitchCmts() {
        String message = "";
        try {
            spectrumConfigService.startSpectrumSwitchCmts(cmcIds);
            message = "success";
        } catch (Exception e) {
            logger.debug("startSpectrumSwitchCmts error!", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 批量关闭CMTS频谱采集开关（网管侧）
     * 
     * @return
     */
    public String stopSpectrumSwitchCmts() {
        String message = "";
        try {
            spectrumConfigService.stopSpectrumSwitchCmts(cmcIds);
            message = "success";
        } catch (Exception e) {
            logger.debug("stopSpectrumSwitchCmts error!", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 批量开启CMTS历史频谱录像开关
     * 
     * @return
     */
    public String startHisVideoSwitch() {
        String message = "";
        String dwrId = "startHisVideoSwitch1024";
        String seesionId = ServletActionContext.getRequest().getSession().getId();
        try {
            spectrumConfigService.startHisVideoSwitch(cmcIds, dwrId, seesionId);
            message = "success";
        } catch (OltSwitchOffException e) {
            message = "OltSwitchOff";
            logger.debug("startHisVideoSwitch error! OltSwichOff", e);
        } catch (Exception e) {
            logger.debug("startHisVideoSwitch error!", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 批量关闭CMTS历史频谱录像开关
     * 
     * @return
     */
    public String stopHisVideoSwitch() {
        String message = "";
        String dwrId = "stopHisVideoSwitch1024";
        String seesionId = ServletActionContext.getRequest().getSession().getId();
        try {
            spectrumConfigService.stopHisVideoSwitch(cmcIds, dwrId, seesionId);
            message = "success";
        } catch (Exception e) {
            logger.debug("stopHisVideoSwitch error!", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 历史频谱采集参数设置，包括采集周期和采集时长
     * 
     * @return
     */
    public String modifyHisSpectrum() {
        String message;
        Long oldHisCollectCycle = spectrumConfigService.getHisCollectCycle();
        try {
            if (hisCollectCycle != null) {
                hisCollectCycle = hisCollectCycle * 1000 * 3600;
            }
            if (hisCollectDuration != null) {
                hisCollectDuration = hisCollectDuration * 1000 * 60;
            }
            if (hisTimeInterval != null) {
                hisTimeInterval = hisTimeInterval * 1000;
            }
            spectrumConfigService.saveSystemPreference("hisCollectCycle", hisCollectCycle);
            spectrumConfigService.saveSystemPreference("hisCollectDuration", hisCollectDuration);
            spectrumConfigService.saveSystemPreference("hisTimeInterval", hisTimeInterval);
            if (hisCollectCycle != oldHisCollectCycle) {
                spectrumMonitorService.updateAllHisMonitor(hisCollectCycle);
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
     * 全局采集时间间隔和最长观看时间修改
     * 
     * @return
     */
    public String modifyTimeStrategy() {
        String message;
        try {
            if (timeLimit != null) {
                timeLimit = timeLimit * 1000 * 60;
            }
            spectrumConfigService.saveSystemPreference("timeInterval", timeInterval);
            spectrumConfigService.saveSystemPreference("timeLimit", timeLimit);
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
     * 显示CMTS频谱配置页面
     * 
     * @return
     */
    public String showCmtsSpectrumConfig() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        hisCollectCycle = spectrumConfigService.getHisCollectCycle() / 1000 / 3600;// 采集周期以小时为单位
        hisCollectDuration = spectrumConfigService.getHisCollectDuration() / 1000 / 60;// 采集时长以分钟为单位
        hisTimeInterval = spectrumConfigService.getHisTimeInterval() / 1000; // 采集步长以秒为单位
        timeInterval = spectrumConfigService.getTimeInterval();// 采集步长，以毫秒为单位
        timeLimit = spectrumConfigService.getTimeLimit() / 1000 / 60;// 观看时限，以分钟为单位
        List<EntityType> entityType;
        if (uc.hasSupportModule("olt")) {
            entityType = entityTypeService.loadSubType(entityTypeService.getCcmtsType());
        } else {
            entityType = entityTypeService.loadSubType(entityTypeService.getCcmtswithagentType());
        }
        cmcTypes = JSONArray.fromObject(entityType);
        return SUCCESS;
    }

    /**
     * 读取历史的全局配置，用于页面上的重置
     * 
     * @return
     */
    public String loadHisConfig() {
        Map<String, Object> json = new HashMap<String, Object>();
        hisCollectCycle = spectrumConfigService.getHisCollectCycle() / 1000 / 3600;// 采集周期以小时为单位
        hisCollectDuration = spectrumConfigService.getHisCollectDuration() / 1000 / 60;// 采集时长以分钟为单位
        hisTimeInterval = spectrumConfigService.getHisTimeInterval() / 1000; // 采集步长以秒为单位
        json.put("hisCollectCycle", hisCollectCycle);
        json.put("hisCollectDuration", hisCollectDuration);
        json.put("hisTimeInterval", hisTimeInterval);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 读取全局配置，用于页面上的重置
     * 
     * @return
     */
    public String loadGlobalConfig() {
        Map<String, Object> json = new HashMap<String, Object>();
        timeInterval = spectrumConfigService.getTimeInterval();// 采集步长，以秒为单位
        timeLimit = spectrumConfigService.getTimeLimit() / 1000 / 60;// 观看时限，以分钟为单位
        json.put("timeInterval", timeInterval);
        json.put("timeLimit", timeLimit);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示OLT频谱配置页面
     * 
     * @return
     */
    public String showOltSpectrumConfig() {
        // 对于整合型CCMTS，从实时频谱页面传递OLT上联entityId
        if (cmcId != null) {
            entityId = cmcService.getEntityIdByCmcId(cmcId);
            entity = entityService.getEntity(entityId);
            entityIp = entity.getIp();
        }
        return SUCCESS;
    }

    public Long getHisCollectCycle() {
        return hisCollectCycle;
    }

    public void setHisCollectCycle(Long hisCollectCycle) {
        this.hisCollectCycle = hisCollectCycle;
    }

    public Long getHisCollectDuration() {
        return hisCollectDuration;
    }

    public void setHisCollectDuration(Long hisCollectDuration) {
        this.hisCollectDuration = hisCollectDuration;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getCmtsName() {
        return cmtsName;
    }

    public void setCmtsName(String cmtsName) {
        this.cmtsName = cmtsName;
    }

    public String getCmtsIp() {
        return cmtsIp;
    }

    public void setCmtsIp(String cmtsIp) {
        this.cmtsIp = cmtsIp;
    }

    public String getOltCollectSwitch() {
        return oltCollectSwitch;
    }

    public void setOltCollectSwitch(String oltCollectSwitch) {
        this.oltCollectSwitch = oltCollectSwitch;
    }

    public String getCmtsCollectSwitch() {
        return cmtsCollectSwitch;
    }

    public void setCmtsCollectSwitch(String cmtsCollectSwitch) {
        this.cmtsCollectSwitch = cmtsCollectSwitch;
    }

    public String getHisVideoSwitch() {
        return hisVideoSwitch;
    }

    public void setHisVideoSwitch(String hisVideoSwitch) {
        this.hisVideoSwitch = hisVideoSwitch;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<Long> getCmcIds() {
        return cmcIds;
    }

    public void setCmcIds(List<Long> cmcIds) {
        this.cmcIds = cmcIds;
    }

    public Integer getHisTimeInterval() {
        return hisTimeInterval;
    }

    public void setHisTimeInterval(Integer hisTimeInterval) {
        this.hisTimeInterval = hisTimeInterval;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Long[] getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(Long[] entityIds) {
        this.entityIds = entityIds;
    }

    public String getDwrId() {
        return dwrId;
    }

    public void setDwrId(String dwrId) {
        this.dwrId = dwrId;
    }

    public JSONArray getCmcTypes() {
        return cmcTypes;
    }

    public void setCmcTypes(JSONArray cmcTypes) {
        this.cmcTypes = cmcTypes;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
