/***********************************************************************
 * $Id: CmcBaseConfigAction.java,v1.0 2013-11-1 下午2:20:21 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcBaseConfigService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.exception.SaveConfigException;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.util.EscapeUtil;

/**
 * @author dosion
 * @created @2013-11-1-下午2:20:21
 * 
 */
@Controller("cmcBaseConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcBaseConfigAction extends BaseAction {
    private static final long serialVersionUID = -8377812944635438844L;
    private final Logger logger = LoggerFactory.getLogger(CmcBaseConfigAction.class);

    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "cmcBaseConfigService")
    private CmcBaseConfigService cmcBaseConfigService;

    private Long entityId;
    private Long cmcId;
    private Entity entity;
    private CmcAttribute cmcAttribute;
    private JSONObject cmcAttrJson;
    private String ccSysLocation;
    private String ccSysContact;
    private String ccSysNote;
    private String cmcName;
    private String alias;
    private String pageId;

    private Integer operationResult;
    public String showAutoClearOfflineCm(){
//      setTime=(cmcClearCmOnTimeService.getCmcClearTime(cmcId));
      return SUCCESS;
  }
    /**
     * 显示cc基本信息配置页面
     * 
     * @return
     */
    public String showCmcBasicInfoConfig() {
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        entity = entityService.getEntity(entityId);
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        cmcAttrJson = JSONObject.fromObject(cmcAttribute);
        return SUCCESS;
    }

    public String modifyCcmtsBasicInfo() {
        String message;
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        try {
            ccSysLocation = EscapeUtil.unescape(ccSysLocation);
            cmcBaseConfigService.modifyCcmtsBasicInfo(entityId, cmcId, cmcName, ccSysLocation, ccSysContact);
            alias = EscapeUtil.unescape(alias);
            entityService.renameEntity(cmcId, alias);
            entityService.modifyEntityContactAndLocation(cmcId,ccSysLocation,ccSysContact);
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
     * CC保存配置
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcAction", operationName = "ccmtsClearConfig")
    public String saveConfig() {
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Map<String, String> message = new HashMap<String, String>();
        try {
            cmcBaseConfigService.saveConfig(cmcId);
            message.put("message", "success");
            operationResult = OperationLog.SUCCESS;
        } catch (SaveConfigException sce) {
            message.put("message", sce.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.debug("CMC save config error:{}", sce);
        } catch (SnmpNoResponseException e) {
            message.put("message", "set timeout error");
            operationResult = OperationLog.FAILURE;
            logger.debug("CMC save config error:{}", e);
        } catch (Exception e) {
            message.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.debug("CMC save config error:{}", e);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 清除8800B启动配置
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcAction", operationName = "CMC.title.clearConfig")
    public String clearConfig() {
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Map<String, String> message = new HashMap<String, String>();
        try {
            cmcBaseConfigService.clearConfig(cmcId);
            message.put("message", "success");
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message.put("message", e.getMessage());
            operationResult = OperationLog.FAILURE;
            logger.debug("CMC save config error:{}", e);
        }
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
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

    public String getCcSysLocation() {
        return ccSysLocation;
    }

    public void setCcSysLocation(String ccSysLocation) {
        this.ccSysLocation = ccSysLocation;
    }

    public String getCcSysContact() {
        return ccSysContact;
    }

    public void setCcSysContact(String ccSysContact) {
        this.ccSysContact = ccSysContact;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

	public String getCcSysNote() {
		return ccSysNote;
	}

	public void setCcSysNote(String ccSysNote) {
		this.ccSysNote = ccSysNote;
	}
    
}
