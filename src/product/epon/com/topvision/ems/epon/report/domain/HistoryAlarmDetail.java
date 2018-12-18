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
public class HistoryAlarmDetail implements AliasesSuperType {
    private static final long serialVersionUID = 3292219454303523543L;

    private String message;
    private String displayName;
    private String entityName;
    private Date firstTime;
    private String firstTimeStr;
    private String clearUser;
    private Date clearTime;
    private String clearTimeStr;

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

    public String getFirstTimeStr() {
        return firstTimeStr;
    }

    public void setFirstTimeStr(String firstTimeStr) {
        this.firstTimeStr = firstTimeStr;
    }

    public String getClearUser() {
        return clearUser;
    }

    public void setClearUser(String clearUser) {
        this.clearUser = clearUser;
    }

    public Date getClearTime() {
        return clearTime;
    }

    public void setClearTime(Date clearTime) {
        this.clearTime = clearTime;
    }

    public String getClearTimeStr() {
        return clearTimeStr;
    }

    public void setClearTimeStr(String clearTimeStr) {
        this.clearTimeStr = clearTimeStr;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HistoryAlarmDetail [message=");
        builder.append(message);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", firstTime=");
        builder.append(firstTime);
        builder.append(", firstTimeStr=");
        builder.append(firstTimeStr);
        builder.append(", clearUser=");
        builder.append(clearUser);
        builder.append(", clearTime=");
        builder.append(clearTime);
        builder.append(", clearTimeStr=");
        builder.append(clearTimeStr);
        builder.append("]");
        return builder.toString();
    }

}
