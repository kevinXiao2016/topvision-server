/***********************************************************************
 * $Id: IfXTable.java,v1.0 2013-6-20 下午02:29:49 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Rod John
 * @created @2013-6-20-下午02:29:49
 * 
 */
public class IfXTable implements Serializable {
    private static final long serialVersionUID = 781359372056959862L;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.6")
    private Long ifHCInOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.10")
    private Long ifHCOutOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.15")
    private Long ifHighSpeed;
    private Timestamp collectTime;

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
     * @return the ifHCInOctets
     */
    public Long getIfHCInOctets() {
        return ifHCInOctets;
    }

    /**
     * @param ifHCInOctets
     *            the ifHCInOctets to set
     */
    public void setIfHCInOctets(Long ifHCInOctets) {
        this.ifHCInOctets = ifHCInOctets;
    }

    /**
     * @return the ifHCOutOctets
     */
    public Long getIfHCOutOctets() {
        return ifHCOutOctets;
    }

    /**
     * @param ifHCOutOctets
     *            the ifHCOutOctets to set
     */
    public void setIfHCOutOctets(Long ifHCOutOctets) {
        this.ifHCOutOctets = ifHCOutOctets;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the ifHighSpeed
     */
    public Long getIfHighSpeed() {
        return ifHighSpeed;
    }

    /**
     * @param ifHighSpeed the ifHighSpeed to set
     */
    public void setIfHighSpeed(Long ifHighSpeed) {
        this.ifHighSpeed = ifHighSpeed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IfXTable [ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifHCInOctets=");
        builder.append(ifHCInOctets);
        builder.append(", ifHCOutOctets=");
        builder.append(ifHCOutOctets);
        builder.append("]");
        return builder.toString();
    }

}
