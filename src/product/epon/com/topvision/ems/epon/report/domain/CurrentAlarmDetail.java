/***********************************************************************
 * $Id: CurrentAlarmDetail.java,v1.0 2013-6-10 下午1:32:57 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.domain;

import java.util.Date;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Administrator
 * @created @2013-6-10-下午1:32:57
 * 
 */
public class CurrentAlarmDetail implements AliasesSuperType {
    private static final long serialVersionUID = 4936574701942769098L;

    private String message;
    private String displayName;
    private String entityName;
    private Date firstTime;
    private Date lastTime;
    private String firstTimeStr;
    private String lastTimeStr;
    private int status;
    private String statusStr;
    private String confirmUser;
    private Date confirmTime;
    private String confirmTimeStr;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getConfirmUser() {
        return confirmUser;
    }

    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getFirstTimeStr() {
        return firstTimeStr;
    }

    public void setFirstTimeStr(String firstTimeStr) {
        this.firstTimeStr = firstTimeStr;
    }

    public String getConfirmTimeStr() {
        return confirmTimeStr;
    }

    public void setConfirmTimeStr(String confirmTimeStr) {
        this.confirmTimeStr = confirmTimeStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CurrentAlarmDetail [message=");
        builder.append(message);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", firstTime=");
        builder.append(firstTime);
        builder.append(", firstTimeStr=");
        builder.append(firstTimeStr);
        builder.append(", status=");
        builder.append(status);
        builder.append(", statusStr=");
        builder.append(statusStr);
        builder.append(", confirmUser=");
        builder.append(confirmUser);
        builder.append(", confirmTime=");
        builder.append(confirmTime);
        builder.append(", confirmTimeStr=");
        builder.append(confirmTimeStr);
        builder.append("]");
        return builder.toString();
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastTimeStr() {
        return lastTimeStr;
    }

    public void setLastTimeStr(String lastTimeStr) {
        this.lastTimeStr = lastTimeStr;
    }

}
