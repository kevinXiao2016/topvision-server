/***********************************************************************
 * $Id: CmcPortPerf.java,v1.0 2011-10-28 下午02:39:39 $
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
 * @created @2011-10-28-下午02:39:39
 *
 */
public class CmcPortMonitorDomain implements Serializable {
    private static final long serialVersionUID = -7830813196963179988L;
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
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.10")
    private Long ifInOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.16")
    private Long ifOutOctets;

    /**
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the monitorId
     */
    public Long getMonitorId() {
        return monitorId;
    }

    /**
     * @param monitorId
     *            the monitorId to set
     */
    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the ifSpeed
     */
    public Long getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @return the ifInOctets
     */
    public Long getIfInOctets() {
        return ifInOctets;
    }

    /**
     * @param ifInOctets
     *            the ifInOctets to set
     */
    public void setIfInOctets(Long ifInOctets) {
        this.ifInOctets = ifInOctets;
    }

    /**
     * @return the ifOutOctets
     */
    public Long getIfOutOctets() {
        return ifOutOctets;
    }

    /**
     * @param ifOutOctets
     *            the ifOutOctets to set
     */
    public void setIfOutOctets(Long ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }

    public Long getIfOperStatus() {
        return ifOperStatus;
    }

    public void setIfOperStatus(Long ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    public Long getIfAdminStatus() {
        return ifAdminStatus;
    }

    public void setIfAdminStatus(Long ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    public Long getIfType() {
        return ifType;
    }

    public void setIfType(Long ifType) {
        this.ifType = ifType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CmcPortMonitorDomain");
        sb.append("{cmcId=").append(cmcId);
        sb.append(", cmcPortId=").append(cmcPortId);
        sb.append(", monitorId=").append(monitorId);
        sb.append(", ifIndex=").append(ifIndex);
        sb.append(", ifType=").append(ifType);
        sb.append(", ifSpeed=").append(ifSpeed);
        sb.append(", ifAdminStatus=").append(ifAdminStatus);
        sb.append(", ifOperStatus=").append(ifOperStatus);
        sb.append(", ifInOctets=").append(ifInOctets);
        sb.append(", ifOutOctets=").append(ifOutOctets);
        sb.append('}');
        return sb.toString();
    }

}