/***********************************************************************
 * $Id: PingConfigAction.java,v1.0 2013-3-19 下午4:49:48 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * Ping选项配置
 * 
 * @author flack
 * @created @2013-3-19-下午4:49:48
 * 
 */
@Controller("pingConfig")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PingConfigAction extends BaseAction {
    private static final long serialVersionUID = -8713162072950867040L;

    private String pingCount;
    private String pingRetry;
    private String pingTimeout;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    /**
     * 从数据库中获取Ping重试次数和超时时间
     * 
     * @return
     */
    public String showPingConfig() {
        // get data from database
        Properties properties = systemPreferencesService.getModulePreferences("Ping");
        pingCount = properties.getProperty("Ping.count");
        pingTimeout = properties.getProperty("Ping.timeout");
        pingRetry = properties.getProperty("Ping.retry");
        return SUCCESS;
    }

    /**
     * 将用户设置的Ping重试次数和超时时间保存到数据库
     * 
     * @return
     */
    public String savePingConfig() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences pingPreference = new SystemPreferences();
        // save Ping.retries
        pingPreference.setName("Ping.count");
        pingPreference.setValue(pingCount);
        pingPreference.setModule("Ping");
        preferences.add(pingPreference);
        // save Ping.timeout
        pingPreference = new SystemPreferences();
        pingPreference.setName("Ping.timeout");
        pingPreference.setValue(pingTimeout);
        pingPreference.setModule("Ping");
        preferences.add(pingPreference);
        // save Ping.retry
        pingPreference = new SystemPreferences();
        pingPreference.setName("Ping.retry");
        pingPreference.setValue(pingRetry);
        pingPreference.setModule("Ping");
        preferences.add(pingPreference);
        // save to database
        systemPreferencesService.savePreferences(preferences);
        return NONE;
    }
    
    public String getPingCount() {
        return pingCount;
    }

    public void setPingCount(String pingCount) {
        this.pingCount = pingCount;
    }

    public String getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(String pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public SystemPreferencesService getSystemPreferencesService() {
        return systemPreferencesService;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    public String getPingRetry() {
        return pingRetry;
    }

    public void setPingRetry(String pingRetry) {
        this.pingRetry = pingRetry;
    }
}
