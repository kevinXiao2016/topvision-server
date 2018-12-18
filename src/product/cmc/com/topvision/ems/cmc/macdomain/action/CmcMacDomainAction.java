/***********************************************************************
 * $Id: CmcMacDomainAction.java,v1.0 2012-2-12 下午05:24:58 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.macdomain.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainBaseInfo;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainStatusInfo;
import com.topvision.ems.cmc.macdomain.service.CmcMacDomainService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;

/**
 * MAC Domain相关功能
 * 
 * @author zhanglongyang
 * @created @2012-2-12-下午05:24:58
 * 
 */
@Controller("cmcMacDomainAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcMacDomainAction extends BaseAction {
    private static final long serialVersionUID = 3026476789916642929L;
    private Logger logger = LoggerFactory.getLogger(CmcMacDomainAction.class);
    @Resource(name = "cmcMacDomainService")
    private CmcMacDomainService cmcMacDomainService;
    private MacDomainBaseInfo macDomainBaseInfo;
    private MacDomainStatusInfo macDomainStatusInfo;
    private Long entityId;
    private Long cmcId;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    private Integer operationResult;


    /**
     * 显示Mac域tab页面
     * 刘占山增加废弃注解，若发现此方法有效，请删除注解。20130411
     * @return String
     */
    @Deprecated
    public String showCmcMacDomainInfo() {
        if (entityId != null) {
            cmcId = cmcService.getCmcIdByEntityId(entityId);
        }
        macDomainBaseInfo = cmcMacDomainService.getMacDomainBaseInfo(cmcId);
        macDomainStatusInfo = cmcMacDomainService.getMacDomainStatusInfo(cmcId);
        return SUCCESS;
    }

    /**
     * 修改Mac域基本信息
     *刘占山增加废弃注解，若发现此方法有效，请删除注解。20130411
     * @return String
     */
    @Deprecated
    @OperationLogProperty(actionName = "cmcAction", operationName = "modifyMacDomainBaseInfo")
    public String modifyMacDomainBaseInfo() {
        String result = null;
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Map<String, String> message = new HashMap<String, String>();
        MacDomainBaseInfo macDomainBaseSetInfo = cmcMacDomainService.getMacDomainBaseInfo(cmcId);
        macDomainBaseSetInfo.setDocsIfCmtsSyncInterval(macDomainBaseInfo.getDocsIfCmtsSyncInterval());
        macDomainBaseSetInfo.setDocsIfCmtsUcdInterval(macDomainBaseInfo.getDocsIfCmtsUcdInterval());
        macDomainBaseSetInfo.setInvitedRangingAttempts(macDomainBaseInfo.getInvitedRangingAttempts());
        macDomainBaseSetInfo.setDocsIfCmtsInsertInterval(macDomainBaseInfo.getDocsIfCmtsInsertInterval());
        try {
            cmcMacDomainService.modifyMacDomainBaseInfo(macDomainBaseSetInfo);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
        	result = "error";
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 刷新mac域信息
     *刘占山增加废弃注解，若发现此方法有效，请删除注解。20130411
     * @return String
     */
    @Deprecated
    public String refreshMacDomainInfo() {
        String message;
        try {
            cmcMacDomainService.refreshMacDomainInfo(cmcId);
            message = "true";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    public CmcMacDomainService getCmcMacDomainService() {
        return cmcMacDomainService;
    }

    public void setCmcMacDomainService(CmcMacDomainService cmcMacDomainService) {
        this.cmcMacDomainService = cmcMacDomainService;
    }

    public MacDomainBaseInfo getMacDomainBaseInfo() {
        return macDomainBaseInfo;
    }

    public void setMacDomainBaseInfo(MacDomainBaseInfo macDomainBaseInfo) {
        this.macDomainBaseInfo = macDomainBaseInfo;
    }

    public MacDomainStatusInfo getMacDomainStatusInfo() {
        return macDomainStatusInfo;
    }

    public void setMacDomainStatusInfo(MacDomainStatusInfo macDomainStatusInfo) {
        this.macDomainStatusInfo = macDomainStatusInfo;
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

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }
}
