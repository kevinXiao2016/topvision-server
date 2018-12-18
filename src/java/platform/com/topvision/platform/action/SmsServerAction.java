/***********************************************************************
 * $Id: SmsServerAction.java,v 1.1 Sep 24, 2008 12:53:33 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.util.Properties;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.ActionService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.impl.SmsActionServiceImpl;

@Controller("smsServerAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SmsServerAction extends BaseAction {
    private static final long serialVersionUID = -591905277160411193L;

    private String port;
    private String smsServerIp;
    private String smsServerPort;
    private String smsServerState;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    @Qualifier(value = "smsAlertService")
    private ActionService smsAlertService;

    /**
     * 保存短信息服务器配置信息
     * 
     * @return
     */
    public String saveSmsServer() {
        Properties props=new Properties();
        props.setProperty("smsServerIp", smsServerIp);
        props.setProperty("smsServerPort", smsServerPort);
        systemPreferencesService.savePreferences("smsServer", props);
        return NONE;
    }

    /**
     * 显示短信息服务器配置信息
     * 
     * @return
     */
    public String showSmsServer() {
        SystemPreferences smsIp = systemPreferencesService.getSystemPreference("smsServerIp");
        SystemPreferences smsPort = systemPreferencesService.getSystemPreference("smsServerPort");
        if(smsIp!=null){
            smsServerIp=smsIp.getValue();
        }
        if(smsPort!=null){
            smsServerPort=smsPort.getValue();
        }
        return SUCCESS;
    }

    public String testSmsServer(){
        JSONObject json = new JSONObject();
        SystemPreferences smsIp = systemPreferencesService.getSystemPreference("smsServerIp");
        SystemPreferences smsPort = systemPreferencesService.getSystemPreference("smsServerPort");
        if(smsIp!=null){
            smsServerIp=smsIp.getValue();
        }
        if(smsPort!=null){
            smsServerPort=smsPort.getValue();
        }
        if(smsServerIp!=null&&smsServerIp.length()!=0&&smsServerPort!=null&&smsServerPort.length()!=0){
            smsServerState=smsAlertService.checkConnection(smsServerIp, Integer.parseInt(smsServerPort));
        }else{
            smsServerState=null;
        }
        json.put("smsServerState", smsServerState);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSmsServerIp() {
        return smsServerIp;
    }

    public void setSmsServerIp(String smsServerIp) {
        this.smsServerIp = smsServerIp;
    }

    public String getSmsServerPort() {
        return smsServerPort;
    }

    public void setSmsServerPort(String smsServerPort) {
        this.smsServerPort = smsServerPort;
    }

    public String getSmsServerState() {
        return smsServerState;
    }

    public void setSmsServerState(String smsServerState) {
        this.smsServerState = smsServerState;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }
}
