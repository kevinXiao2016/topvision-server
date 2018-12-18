/***********************************************************************
 * $Id: CmcDownPortMonitorDomain.java,v1.0 2013-12-6 下午1:33:02 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author loyal
 * @created @2013-12-6-下午1:33:02
 *
 */
public class CmcDownPortMonitorDomain implements Serializable{
    private static final long serialVersionUID = -5100875655332186679L;
    private Long cmcPortId;
    private Long cmcId;
    private Long monitorId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.3")
    private Long ifType;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.5")
    private Long ifSpeed;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.7")
    private Long ifAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.8")
    private Long ifOperStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.16")
    private Long ifOutOctets;
    public Long getCmcPortId() {
        return cmcPortId;
    }
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
    public Long getMonitorId() {
        return monitorId;
    }
    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }
    public Long getIfIndex() {
        return ifIndex;
    }
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }
    public Long getIfType() {
        return ifType;
    }
    public void setIfType(Long ifType) {
        this.ifType = ifType;
    }
    public Long getIfSpeed() {
        return ifSpeed;
    }
    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }
    public Long getIfAdminStatus() {
        return ifAdminStatus;
    }
    public void setIfAdminStatus(Long ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }
    public Long getIfOperStatus() {
        return ifOperStatus;
    }
    public void setIfOperStatus(Long ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }
    public Long getIfOutOctets() {
        return ifOutOctets;
    }
    public void setIfOutOctets(Long ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDownPortMonitorDomain [cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", monitorId=");
        builder.append(monitorId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifType=");
        builder.append(ifType);
        builder.append(", ifSpeed=");
        builder.append(ifSpeed);
        builder.append(", ifAdminStatus=");
        builder.append(ifAdminStatus);
        builder.append(", ifOperStatus=");
        builder.append(ifOperStatus);
        builder.append(", ifInOctets=");
        builder.append(ifOutOctets);
        builder.append("]");
        return builder.toString();
    }
    
}
