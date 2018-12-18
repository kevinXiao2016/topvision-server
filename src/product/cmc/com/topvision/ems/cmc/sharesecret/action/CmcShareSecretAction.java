/***********************************************************************
 * $Id: CmcShareSecretAction.java,v1.0 2013-7-23 下午1:57:03 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sharesecret.action;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.sharesecret.facade.domain.CmcShareSecretConfig;
import com.topvision.ems.cmc.sharesecret.service.CmcShareSecretService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author dosion
 * @created @2013-7-23-下午1:57:03
 * 
 */
//@Controller("cmcShareSecretAction")
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcShareSecretAction extends BaseAction {
    private static final long serialVersionUID = 1994715084452912944L;
    
    private final Logger logger = LoggerFactory.getLogger(CmcShareSecretAction.class);
    @Resource(name = "cmcShareSecretService")
    private CmcShareSecretService cmcShareSecretService;
    
    private Long cmcId;
    /**
     * 从前台传递的配置
     */
    private CmcShareSecretConfig cmcShareSecretSetting;
    /**
     * 从后台传递到前台的配置
     */
    private JSONObject cmcShareSecretConfig;
    
    /**
     * 显示share secret配置
     * @return
     */
    public String showCmcShareSecretConfig(){
        cmcShareSecretConfig = JSONObject.fromObject(cmcShareSecretService.getCmcShareSecretConfig(cmcId));
        return SUCCESS;
    }
    
    /**
     * 修改share secret配置
     * @return
     */
    public String modifyCmcShareSecretConfig(){
        String result;
        try {
            cmcShareSecretService.modifyCmcShareSecretConfig(cmcId, cmcShareSecretSetting);
            result = "true";
        } catch (Exception e) {
            result = "false";
            logger.error("modifyCmcShareSecretConfig",e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public CmcShareSecretConfig getCmcShareSecretSetting() {
        return cmcShareSecretSetting;
    }

    public void setCmcShareSecretSetting(CmcShareSecretConfig cmcShareSecretSetting) {
        this.cmcShareSecretSetting = cmcShareSecretSetting;
    }

    public JSONObject getCmcShareSecretConfig() {
        return cmcShareSecretConfig;
    }

    public void setCmcShareSecretConfig(JSONObject cmcShareSecretConfig) {
        this.cmcShareSecretConfig = cmcShareSecretConfig;
    }

    public CmcShareSecretService getCmcShareSecretService() {
        return cmcShareSecretService;
    }

    public void setCmcShareSecretService(CmcShareSecretService cmcShareSecretService) {
        this.cmcShareSecretService = cmcShareSecretService;
    }   
    
}
