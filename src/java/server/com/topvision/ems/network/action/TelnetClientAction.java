/***********************************************************************
 * $Id: TelnetClientAction.java,v1.0 2017年1月8日 上午9:51:56 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.domain.TelnetRecord;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TelnetClientService;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.utils.HttpUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.TftpClientInfo;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONObject;

/**
 * @author vanzand
 * @created @2017年1月8日-上午9:51:56
 *
 */
@Controller("telnetClientAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TelnetClientAction extends BaseAction {

    private static final long serialVersionUID = 8704720245060732122L;

    private String command;
    private String entityIp;
    private Long entityId;
    private String recordState;
    private String clientType;
    private String timeout;
    private String userName;
    private String startTime;
    private String endTime;
    private String interfaceStr;

    @Autowired
    private TelnetClientService telnetClientService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private TelnetLoginService telnetLoginService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;

    public String showTelnetClientMgt() {
        // 获取客户端类型/记录是否开启
        Properties properties = systemPreferencesService.getModulePreferences(TftpClientInfo.SYSTEM_MODULE_NAME);
        recordState = properties.getProperty(TftpClientInfo.RECORD_STATE);
        clientType = properties.getProperty(TftpClientInfo.CLIENT_TYPE);
        timeout = properties.getProperty(TftpClientInfo.TIMEOUT);
        return SUCCESS;
    }

    public String loadTelnetClientType() {
        Properties properties = systemPreferencesService.getModulePreferences(TftpClientInfo.SYSTEM_MODULE_NAME);
        clientType = properties.getProperty(TftpClientInfo.CLIENT_TYPE);
        writeDataToAjax(clientType);
        return NONE;
    }

    public String showTelnetRecord() {
        return SUCCESS;
    }

    public String saveClientConfig() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        // 客户端类型
        SystemPreferences clientTypePreference = new SystemPreferences();
        clientTypePreference.setModule(TftpClientInfo.SYSTEM_MODULE_NAME);
        clientTypePreference.setName(TftpClientInfo.CLIENT_TYPE);
        clientTypePreference.setValue(clientType);
        preferences.add(clientTypePreference);

        // 记录是否开启
        SystemPreferences recordPreference = new SystemPreferences();
        recordPreference.setModule(TftpClientInfo.SYSTEM_MODULE_NAME);
        recordPreference.setName(TftpClientInfo.RECORD_STATE);
        recordPreference.setValue(recordState);
        preferences.add(recordPreference);
        
        // 超时时间
        SystemPreferences timeoutPreference = new SystemPreferences();
        timeoutPreference.setModule(TftpClientInfo.SYSTEM_MODULE_NAME);
        timeoutPreference.setName(TftpClientInfo.TIMEOUT);
        timeoutPreference.setValue(timeout);
        preferences.add(timeoutPreference);

        systemPreferencesService.savePreferences(TftpClientInfo.SYSTEM_MODULE_NAME, preferences);
        return NONE;
    }

    public String loadTelnetRecord() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (entityIp != null && !"".equals(entityIp)) {
            queryMap.put("ip", entityIp);
        }
        if (command != null && !"".equals(command)) {
            queryMap.put("command", command);
        }
        if (userName != null && !"".equals(userName)) {
            queryMap.put("userName", userName);
        }
        if (startTime != null && !"".equals(startTime)) {
            queryMap.put("startTime", startTime);
        }
        if (endTime != null && !"".equals(endTime)) {
            queryMap.put("endTime", endTime);
        }
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        List<TelnetRecord> recordList = telnetClientService.loadTelnetRecord(queryMap);
        Integer rowCount = telnetClientService.loadTelnetRecordCount(queryMap);
        JSONObject ret = new JSONObject();
        ret.put("data", recordList);
        ret.put("rowCount", rowCount);
        writeDataToAjax(ret);
        return NONE;
    }

    public String showTelnetClient() {
        // 根据entityId获取设备详细信息
        if (entityId != null) {
            try {
                Entity entity = entityService.getEntity(entityId);
                Long typeId = entity.getTypeId();
                // 如果是集中型的CC或者ONU，解析interface
                if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
                    Long index = entityService.getEntityIndexOfOlt(entityId);
                    String indexStr = EponIndex.getCcmtsStringByIndex(index);
                    interfaceStr = "interface ccmts " + indexStr;
                } else if (entityTypeService.isOnu(typeId)) {
                    // 需要判断是否为GPON ONU，GPON ONU不需要进入ONU视图，进入对应的PON视图
                    String eOrG = entityService.getOnuEorG(entityId);
                    Long index = entityService.getEntityIndexOfOlt(entityId);
                    String indexStr = EponIndex.getOnuStringByIndex(index).toString();
                    if(eOrG != null && "E".equals(eOrG.toUpperCase())) {
                        interfaceStr = "interface onu " + indexStr;
                    } else if (eOrG != null && "G".equals(eOrG.toUpperCase())) {
                        indexStr = indexStr.substring(0, indexStr.indexOf(":"));
                        interfaceStr = "interface gpon " + indexStr;
                    }
                }
            } catch (Exception e) {
                logger.debug("telnet clinet error: " + e.getMessage());
            }
        }
        return SUCCESS;
    }

    public String loadLoginConfig() {
        // 根据entityId获取设备信息
        TelnetLogin loginInfo = telnetLoginService.getEntityTelnetLogin(entityIp);
        loginInfo.setIpString(entityIp);
        writeDataToAjax(loginInfo);
        return NONE;
    }

    public String connect() throws Exception {
        write(telnetClientService.connect(request.getSession().getId(), entityIp), HttpUtils.CONTENT_TYPE_PLAIN);
        return NONE;
    }
    
    public String close() throws Exception {
        telnetClientService.close(request.getSession().getId(), entityIp);
        return NONE;
    }

    public String sendUsername() throws Exception {
        write(telnetClientService.sendUsername(request.getSession().getId(), entityIp, command),
                HttpUtils.CONTENT_TYPE_PLAIN);
        return NONE;
    }

    public String sendPassword() throws Exception {
        write(telnetClientService.sendPassword(request.getSession().getId(), entityIp, command),
                HttpUtils.CONTENT_TYPE_PLAIN);
        return NONE;
    }

    public String sendCommand() throws Exception {
        write(telnetClientService.sendCommand(request.getSession().getId(), entityIp, command),
                HttpUtils.CONTENT_TYPE_PLAIN);
        return NONE;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRecordState() {
        return recordState;
    }

    public void setRecordState(String recordState) {
        this.recordState = recordState;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getInterfaceStr() {
        return interfaceStr;
    }

    public void setInterfaceStr(String interfaceStr) {
        this.interfaceStr = interfaceStr;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

}
