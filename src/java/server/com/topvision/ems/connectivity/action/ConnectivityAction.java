/***********************************************************************
 * $Id: ConnectivityAction.java,v1.0 2017年9月7日 下午3:49:38 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.connectivity.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.topvision.ems.connectivity.service.ConnectivityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author vanzand
 * @created @2017年9月7日-下午3:49:38
 *
 */
@Controller("connectivityAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConnectivityAction extends BaseAction{

    private static final long serialVersionUID = -1147413302160951472L;
    
    @Autowired
    private ConnectivityService connectivityService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    
    private String strategyStr;
    
    private String tcpPort;
    private String tcpTimeout;
    
    
    public String showConnectivityConfig() {
        return SUCCESS;
    }
    
    public String loadConnectivityStrategy() {
        Map<String, Integer> stratgeryMap = connectivityService.getStrategyState();
        writeDataToAjax(stratgeryMap);
        return NONE;
    }
    
    public String saveConnectivityStrategy() {
        @SuppressWarnings("unchecked")
        Map<String, Integer> stratgeryMap = JSONObject.parseObject(strategyStr, Map.class);
        connectivityService.saveConnectivityStrategy(stratgeryMap);
        return NONE;
    }
    
    public String showTcpConnectivityConfig() {
        Properties properties = systemPreferencesService.getModulePreferences("tcp");
        setTcpPort(properties.getProperty("port"));
        setTcpTimeout(properties.getProperty("timeout"));
        return SUCCESS;
    }
    
    public String savTcpConnectivityConfig() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences snmpPreference = new SystemPreferences();
        // save tcp port
        snmpPreference.setName("port");
        snmpPreference.setValue(tcpPort);
        snmpPreference.setModule("tcp");
        preferences.add(snmpPreference);
        // save tcp timeout
        snmpPreference = new SystemPreferences();
        snmpPreference.setName("timeout");
        snmpPreference.setValue(tcpTimeout);
        snmpPreference.setModule("tcp");
        preferences.add(snmpPreference);
        // save to database
        systemPreferencesService.savePreferences(preferences);
        return NONE;
    }

    public String getStrategyStr() {
        return strategyStr;
    }

    public void setStrategyStr(String strategyStr) {
        this.strategyStr = strategyStr;
    }

    public String getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(String tcpPort) {
        this.tcpPort = tcpPort;
    }

    public String getTcpTimeout() {
        return tcpTimeout;
    }

    public void setTcpTimeout(String tcpTimeout) {
        this.tcpTimeout = tcpTimeout;
    }

}
