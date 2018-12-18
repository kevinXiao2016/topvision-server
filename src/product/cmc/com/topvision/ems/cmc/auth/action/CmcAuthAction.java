/***********************************************************************
 * $Id: CmcAuthAction.java,v1.0 2012-10-8 上午10:19:05 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.auth.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.auth.facade.domain.CcmtsAuthManagement;
import com.topvision.ems.cmc.auth.service.CmcAuthService;
import com.topvision.ems.cmc.exception.SetValueFailException;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author dosion
 * @created @2012-10-8-上午10:19:05
 * 
 */
@Controller("cmcAuthAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcAuthAction extends BaseAction {
    private static final long serialVersionUID = -9003803477819282697L;
    private Logger logger = LoggerFactory.getLogger(CmcAuthAction.class);
    @Resource(name = "cmcAuthService")
    private CmcAuthService cmcAuthService;
    private Long cmcId;
    private CcmtsAuthManagement authMgmt;
    private JSONObject json;

    /**
     * 显示授权管理配置页面
     *
     * @return  String
     */
    public String showCmcAuthMgmtConfig() {
        //获取全局授权状态与当前CCMTS授权信息，并返回前台
        authMgmt = cmcAuthService.getCcmtsAuthInfo(cmcId);
        json = JSONObject.fromObject(authMgmt);
        return SUCCESS;
    }

    /**
     * 授权管理设置
     *
     * @return  String
     */
    public String cmcAuthMgmtSet() {
        // 将授权信息设置到设备上，并返回设置结果
        boolean b;
        Map<String, String> result = new HashMap<String, String>();
        try {
            b = cmcAuthService.setCcmtsAuthInfo(cmcId, authMgmt);
            if (!b) {
                result.put("message", "failuer");
                result.put("errMessage", "Other CCMTS is authorizing.");
            } else {
                result.put("message", "success");
            }
        } catch (SetValueFailException e) {
            result.put("message", "failure");
            result.put("errMessage", "Set Failure.");
            logger.debug("", e);
        } catch (Exception e) {
            result.put("message", "failure");
            logger.debug("", e);
        }
        writeDataToAjax(JSONObject.fromObject(result));
        return NONE;
    }

    /**
     * 获取授权状态信息
     *
     * @return  String
     */
    public String getCmcAuthStatus() {
        // 从设备上读取授权信息，并返回前台
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            CcmtsAuthManagement cmcAuthMgmt = cmcAuthService.getCcmtsAuthInfoFromEntity(cmcId);
            result.put("message", "success");
            result.put("cmcAuthMgmt", cmcAuthMgmt);
        } catch (SnmpException e) {
            result.put("message", "failure");
            result.put("cmcAuthMgmt", new CcmtsAuthManagement());
            logger.debug("", e);
        }
        writeDataToAjax(JSONObject.fromObject(result));
        return NONE;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the authMgmt
     */
    public CcmtsAuthManagement getAuthMgmt() {
        return authMgmt;
    }

    /**
     * @param authMgmt
     *            the authMgmt to set
     */
    public void setAuthMgmt(CcmtsAuthManagement authMgmt) {
        this.authMgmt = authMgmt;
    }

    /**
     * @return the cmcAuthService
     */
    public CmcAuthService getCmcAuthService() {
        return cmcAuthService;
    }

    /**
     * @param cmcAuthService the cmcAuthService to set
     */
    public void setCmcAuthService(CmcAuthService cmcAuthService) {
        this.cmcAuthService = cmcAuthService;
    }

    /**
     * @return the json
     */
    public JSONObject getJson() {
        return json;
    }

    /**
     * @param json the json to set
     */
    public void setJson(JSONObject json) {
        this.json = json;
    }

}
