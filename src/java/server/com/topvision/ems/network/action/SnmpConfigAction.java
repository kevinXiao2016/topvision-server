/***********************************************************************
 * $Id: SnmpConfigAction.java,v1.0 2013-3-19 下午5:11:39 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * SNMP选项配置
 * 
 * @author flack
 * @created @2013-3-19-下午5:11:39
 * 
 */
@Controller("snmpConfig")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SnmpConfigAction extends BaseAction {
    private static final long serialVersionUID = -525998312375803524L;

    private String snmpPort;
    private String snmpRetries;
    private String snmpTimeout;
    private String snmpConnectivityOid;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private EntityService entityService;

    /**
     * 从数据库获取SNMP系统选项的值
     * 
     * @return
     */
    public String showSnmpConfig() {
        // get data from database
        Properties properties = systemPreferencesService.getModulePreferences("Snmp");
        snmpPort = properties.getProperty("Snmp.port");
        snmpRetries = properties.getProperty("Snmp.retries");
        snmpTimeout = properties.getProperty("Snmp.timeout");
        snmpConnectivityOid = properties.getProperty("Snmp.connectivityOid");
        return SUCCESS;
    }

    /**
     * 将用户设置的SNMP系统选项的值保存到数据库
     * 
     * @return
     * @throws IOException
     */
    public String saveSnmpConfig() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences snmpPreference = new SystemPreferences();
        // save Snmp.port
        snmpPreference.setName("Snmp.port");
        snmpPreference.setValue(snmpPort);
        snmpPreference.setModule("Snmp");
        preferences.add(snmpPreference);
        // save Snmp.retries
        snmpPreference = new SystemPreferences();
        snmpPreference.setName("Snmp.retries");
        snmpPreference.setValue(snmpRetries);
        snmpPreference.setModule("Snmp");
        preferences.add(snmpPreference);
        // save Snmp.timeout
        snmpPreference = new SystemPreferences();
        snmpPreference.setName("Snmp.timeout");
        snmpPreference.setValue(snmpTimeout);
        snmpPreference.setModule("Snmp");
        preferences.add(snmpPreference);
        // save Snmp.connectivityOid
        snmpPreference = new SystemPreferences();
        snmpPreference.setName("Snmp.connectivityOid");
        snmpPreference.setValue(snmpConnectivityOid);
        snmpPreference.setModule("Snmp");
        preferences.add(snmpPreference);
        // save to database
        systemPreferencesService.savePreferences(preferences);
        // apply to entity
        entityService.applySnmpConfig(Integer.parseInt(snmpTimeout), Byte.parseByte(snmpRetries),
                Integer.parseInt(snmpPort));
        return NONE;
    }

    public String getSnmpPort() {
        return snmpPort;
    }

    public void setSnmpPort(String snmpPort) {
        this.snmpPort = snmpPort;
    }

    public String getSnmpRetries() {
        return snmpRetries;
    }

    public void setSnmpRetries(String snmpRetries) {
        this.snmpRetries = snmpRetries;
    }

    public String getSnmpTimeout() {
        return snmpTimeout;
    }

    public void setSnmpTimeout(String snmpTimeout) {
        this.snmpTimeout = snmpTimeout;
    }

    public SystemPreferencesService getSystemPreferencesService() {
        return systemPreferencesService;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    /**
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService
     *            the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public String getSnmpConnectivityOid() {
        return snmpConnectivityOid;
    }

    public void setSnmpConnectivityOid(String snmpConnectivityOid) {
        this.snmpConnectivityOid = snmpConnectivityOid;
    }

}
