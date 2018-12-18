/***********************************************************************
 * $Id: OnuAuthInfo.java,v1.0 2015年4月20日 上午11:51:09 $
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
 * @created @2015年4月20日-上午11:51:09
 * 
 */
public class OnuAuthInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8012956112514713406L;

    private Long entityId;
    private Long ponId;
    private Long ponIndex;
    private Long onuIndex;
    private String mac;
    private Integer action;
    private String sn;
    private String password;
    private Integer onuPreType;
    private Integer authType;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
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

    public Integer getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(Integer onuPreType) {
        this.onuPreType = onuPreType;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }
}
