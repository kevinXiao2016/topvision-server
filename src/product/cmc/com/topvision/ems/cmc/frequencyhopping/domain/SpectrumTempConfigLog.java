/***********************************************************************
 * $Id: SpectrumTempConfigLog.java,v1.0 2013-8-17 下午3:36:00 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author flack
 * @created @2013-8-17-下午3:36:00
 * 
 */
public class SpectrumTempConfigLog implements Serializable {
    private static final long serialVersionUID = 364184045599268037L;

    private Long configLogId;
    private Long tempLateId;
    private String templateName;
    private Integer deviceNum;
    private Long userId;
    private String userName;
    private Date configTime;
    private Integer configStatus;// 0 表示正在配置 1 表示配置成功

    public Long getConfigLogId() {
        return configLogId;
    }

    public void setConfigLogId(Long configLogId) {
        this.configLogId = configLogId;
    }

    public Long getTempLateId() {
        return tempLateId;
    }

    public void setTempLateId(Long tempLateId) {
        this.tempLateId = tempLateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getConfigTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(configTime);
    }

    public void setConfigTime(Date configTime) {
        this.configTime = configTime;
    }

    public Integer getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(Integer configStatus) {
        this.configStatus = configStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpectrumTempConfigLog [configLogId=");
        builder.append(configLogId);
        builder.append(", tempLateId=");
        builder.append(tempLateId);
        builder.append(", templateName=");
        builder.append(templateName);
        builder.append(", deviceNum=");
        builder.append(deviceNum);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", userName=");
        builder.append(userName);
        builder.append(", configTime=");
        builder.append(configTime);
        builder.append(", configStatus=");
        builder.append(configStatus);
        builder.append("]");
        return builder.toString();
    }
}
