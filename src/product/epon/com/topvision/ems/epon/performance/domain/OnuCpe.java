/***********************************************************************
 * $Id: OnuCpe.java,v1.0 2013-8-6 下午04:45:40 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2013-8-6-下午04:45:40
 * 
 */
public class OnuCpe implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7135580713841469808L;
    private Long entityId;
    private Long slotIndex;
    private Long portIndex;
    private Long onuIndex;
    private Long uniIndex;
    private Long cpeNo;
    private String mac;
    private Integer type;
    private Integer vlan;
    private Timestamp realtime;
    private String ipAddress;
    private Integer cpeType;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Long getCpeNo() {
        return cpeNo;
    }

    public void setCpeNo(Long cpeNo) {
        this.cpeNo = cpeNo;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getVlan() {
        return vlan;
    }

    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    public Timestamp getRealtime() {
        return realtime;
    }

    public void setRealtime(Timestamp realtime) {
        this.realtime = realtime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getCpeType() {
        return cpeType;
    }

    public void setCpeType(Integer cpeType) {
        this.cpeType = cpeType;
    }

}
