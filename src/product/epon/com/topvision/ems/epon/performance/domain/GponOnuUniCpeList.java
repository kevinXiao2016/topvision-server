/***********************************************************************
 * $Id: EponLinkQualityData.java,v1.0 2013-8-6 下午04:45:40 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author jay
 * @created @2013-8-6-下午04:45:40
 * 
 */
public class GponOnuUniCpeList implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7135580713841469808L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.2.1.1", index = true)
    private Long infoSlotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.2.1.2", index = true)
    private Long infoPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.2.1.3", index = true)
    private Long infoOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.2.1.4", index = true)
    private Long infoUniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.2.2.2.1.5")
    private String uniMacInfoTotal;
    private String dhcpIpInfoTotal;
    private Timestamp realTime;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getInfoSlotIndex() {
        return infoSlotIndex;
    }

    public void setInfoSlotIndex(Long infoSlotIndex) {
        this.infoSlotIndex = infoSlotIndex;
    }

    public Long getInfoPortIndex() {
        return infoPortIndex;
    }

    public void setInfoPortIndex(Long infoPortIndex) {
        this.infoPortIndex = infoPortIndex;
    }

    public Long getInfoOnuIndex() {
        return infoOnuIndex;
    }

    public void setInfoOnuIndex(Long infoOnuIndex) {
        this.infoOnuIndex = infoOnuIndex;
    }

    public Long getInfoUniIndex() {
        return infoUniIndex;
    }

    public void setInfoUniIndex(Long infoUniIndex) {
        this.infoUniIndex = infoUniIndex;
    }

    public String getUniMacInfoTotal() {
        return uniMacInfoTotal;
    }

    public void setUniMacInfoTotal(String uniMacInfoTotal) {
        this.uniMacInfoTotal = uniMacInfoTotal;
    }

    public Timestamp getRealTime() {
        return realTime;
    }

    public void setRealTime(Timestamp realTime) {
        this.realTime = realTime;
    }

    public String getDhcpIpInfoTotal() {
        return dhcpIpInfoTotal;
    }

    public void setDhcpIpInfoTotal(String dhcpIpInfoTotal) {
        this.dhcpIpInfoTotal = dhcpIpInfoTotal;
    }

    /**
     * @param uniIndex
     */
    public void setPortIndex(Long uniIndex) {
        infoSlotIndex = EponIndex.getSlotNo(uniIndex);
        infoPortIndex = EponIndex.getPonNo(uniIndex);
        infoOnuIndex = EponIndex.getOnuNo(uniIndex);
        infoUniIndex = EponIndex.getUniNo(uniIndex);
    }
}
