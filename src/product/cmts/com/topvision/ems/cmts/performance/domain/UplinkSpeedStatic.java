/***********************************************************************
 * $Id: UplinkSpeedStatic.java,v1.0 2012-7-15 上午11:31:14 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import com.topvision.framework.constants.Symbol;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author jay
 * @created @2012-7-15-上午11:31:14
 */
public class UplinkSpeedStatic implements Serializable {
    private static final long serialVersionUID = 896338589355551319L;
    private Long entityId;
    private Timestamp dt;
    private Long ifIndex;
    private Long ifInOctets;
    private Long ifOutOctets;
    private float ifInOctetsRate;
    private float ifOutOctetsRate;
    private float ifOctetsRate;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the dt
     */
    public Timestamp getDt() {
        return dt;
    }

    /**
     * @param dt the dt to set
     */
    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    /**
     * @return the channelIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex the channelIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the channelInOctetsRate
     */
    public float getIfInOctetsRate() {
        return ifInOctetsRate;
    }

    /**
     * @param ifInOctetsRate the channelInOctetsRate to set
     */
    public void setIfInOctetsRate(float ifInOctetsRate) {
        this.ifInOctetsRate = ifInOctetsRate;
    }

    /**
     * @return the channelOutOctetsRate
     */
    public float getIfOutOctetsRate() {
        return ifOutOctetsRate;
    }

    /**
     * @param ifOutOctetsRate the channelOutOctetsRate to set
     */
    public void setIfOutOctetsRate(float ifOutOctetsRate) {
        this.ifOutOctetsRate = ifOutOctetsRate;
    }

    /**
     * @return the channelInOctets
     */
    public Long getIfInOctets() {
        return ifInOctets;
    }

    /**
     * @param ifInOctets the channelInOctets to set
     */
    public void setIfInOctets(Long ifInOctets) {
        this.ifInOctets = ifInOctets;
    }

    /**
     * @return the channelOutOctets
     */
    public Long getIfOutOctets() {
        return ifOutOctets;
    }

    /**
     * @param ifOutOctets the channelOutOctets to set
     */
    public void setIfOutOctets(Long ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }

    /**
     * @return the channelOctetsRate
     */
    public float getIfOctetsRate() {
        return ifOctetsRate;
    }

    /**
     * @param ifOctetsRate the channelOctetsRate to set
     */
    public void setIfOctetsRate(float ifOctetsRate) {
        this.ifOctetsRate = ifOctetsRate;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UplinkSpeedStatic [entityId=");
        builder.append(entityId);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifInOctets=");
        builder.append(ifInOctets);
        builder.append(", ifOutOctets=");
        builder.append(ifOutOctets);
        builder.append(", ifInOctetsRate=");
        builder.append(ifInOctetsRate);
        builder.append(", ifOutOctetsRate=");
        builder.append(ifOutOctetsRate);
        builder.append(", ifOctetsRate=");
        builder.append(ifOctetsRate);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}