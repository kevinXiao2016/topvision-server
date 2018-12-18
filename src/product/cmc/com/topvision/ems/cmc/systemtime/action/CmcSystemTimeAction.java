/***********************************************************************
 * $Id: CmcSystemTimeAction.java,v1.0 2013-7-17 上午11:05:53 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.systemtime.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.systemtime.facade.domain.CmcSystemTimeConfig;
import com.topvision.ems.cmc.systemtime.service.CmcSystemTimeService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author dosion
 * @created @2013-7-17-上午11:05:53
 *
 */
@Controller("cmcSystemTimeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcSystemTimeAction extends BaseAction {
    private static final long serialVersionUID = -4705412189126151002L;
    private final Logger logger = LoggerFactory.getLogger(CmcSystemTimeAction.class);
    @Resource(name = "cmcSystemTimeService")
    private CmcSystemTimeService cmcSystemTimeService;
    private Long entityId;
    private JSONObject currentSystemTimeConfig;
    private CmcSystemTimeConfig systemTimeConfig;

    /**
     * 显示系统时间配置页面
     * @return
     */
    public String showSystemTimeConfig() {
        CmcSystemTimeConfig cmcSystemTimeConfig = cmcSystemTimeService.getCmcSystemTimeConfig(entityId);
        currentSystemTimeConfig = JSONObject.fromObject(cmcSystemTimeConfig);
        return SUCCESS;
    }

    /**
     * 修改系统时间配置
     * @return
     */
    public String modifySystemTimeConfig() {
        systemTimeConfig.setEntityId(entityId);
        if (systemTimeConfig.getTopCcmtsNtpserverAddress() != null
                && systemTimeConfig.getTopCcmtsNtpserverAddress().equals("")) {
            systemTimeConfig.setTopCcmtsSysTimeSynInterval(null);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            CmcSystemTimeConfig cmcSystemTimeConfig = cmcSystemTimeService.modifyCmcSystemTimeConfig(systemTimeConfig);
            map.put("systemTimeConfig", cmcSystemTimeConfig);
            map.put("message", "success");
        } catch (Exception e) {
            map.put("message", "failure");
            logger.error("", e);
        } finally {
            writeDataToAjax(JSONObject.fromObject(map));
        }
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public JSONObject getCurrentSystemTimeConfig() {
        return currentSystemTimeConfig;
    }

    public void setCurrentSystemTimeConfig(JSONObject currentSystemTimeConfig) {
        this.currentSystemTimeConfig = currentSystemTimeConfig;
    }

    public CmcSystemTimeConfig getSystemTimeConfig() {
        return systemTimeConfig;
    }

    public void setSystemTimeConfig(CmcSystemTimeConfig systemTimeConfig) {
        this.systemTimeConfig = systemTimeConfig;
    }

    public CmcSystemTimeService getCmcSystemTimeService() {
        return cmcSystemTimeService;
    }

    public void setCmcSystemTimeService(CmcSystemTimeService cmcSystemTimeService) {
        this.cmcSystemTimeService = cmcSystemTimeService;
    }

}
