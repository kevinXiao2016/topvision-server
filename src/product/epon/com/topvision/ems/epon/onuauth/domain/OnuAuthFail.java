/***********************************************************************
 * $Id: OnuAuthFail.java,v1.0 2015年4月18日 上午9:59:24 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2015年4月18日-上午9:59:24
 * 
 */
public class OnuAuthFail implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6278544986756547204L;

    private String recordId;
    private Long onuId;
    private Long entityId;
    private String name;
    private String manageIp;
    private Long onuIndex;
    private Long ponId;
    private Long ponIndex;
    private String mac;
    private String sn;
    private String password;
    private Long lastAuthTime;
    private Integer authMode;
    private Integer authAction;
    private String lastAuthTimeString;
    private Integer onuPreType;
    private String uplinkDevice;

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getLastAuthTime() {
        return lastAuthTime;
    }

    public void setLastAuthTime(Long lastAuthTime) {
        this.lastAuthTime = lastAuthTime;
    }

    public String getLastAuthTimeString() {
        return lastAuthTimeString;
    }

    public void setLastAuthTimeString(String lastAuthTimeString) {
        this.lastAuthTimeString = lastAuthTimeString;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Integer getAuthMode() {
        return authMode;
    }

    public void setAuthMode(Integer authMode) {
        this.authMode = authMode;
    }

    public Integer getAuthAction() {
        return authAction;
    }

    public void setAuthAction(Integer authAction) {
        this.authAction = authAction;
    }

    public Integer getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(Integer onuPreType) {
        this.onuPreType = onuPreType;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

}
