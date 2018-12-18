/***********************************************************************
 * $Id: OnuOfflineAlarm.java,v1.0 2016年6月17日 下午5:33:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年6月17日-下午5:33:43
 *
 */
public class OnuOfflineAlarm implements AliasesSuperType {
    private static final long serialVersionUID = 4884611954404852939L;
    private Long entityId;
    private Long onuIndex;
    private Integer alertType;
    private String mac;
    private String onuAlias;
    private Long onuType;
    private String message;
    private Timestamp fireTime;
    private Long parentId;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getAlertType() {
        return alertType;
    }

    public void setAlertType(Integer alertType) {
        this.alertType = alertType;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOnuAlias() {
        return onuAlias;
    }

    public void setOnuAlias(String onuAlias) {
        this.onuAlias = onuAlias;
    }

    public Long getOnuType() {
        return onuType;
    }

    public void setOnuType(Long onuType) {
        this.onuType = onuType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getFireTime() {
        return fireTime;
    }

    public void setFireTime(Timestamp fireTime) {
        this.fireTime = fireTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
