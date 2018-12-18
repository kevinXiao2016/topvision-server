/***********************************************************************
 * $Id: ChannelSpeedStatic.java,v1.0 2012-7-15 上午11:31:14 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2012-7-15-上午11:31:14
 * 
 */
@Alias("channelSpeedStatic")
public class ChannelSpeedStatic implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 896338589355551319L;
    private Long entityId;
    private Long cmcId;
    private Timestamp dt;
    private Long channelIndex;
    private int channelType;
    private Long channelInOctets;
    private Long channelOutOctets;
    private float channelInOctetsRate;
    private float channelOutOctetsRate;
    private float channelOctetsRate;

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
     * @return the dt
     */
    public Timestamp getDt() {
        return dt;
    }

    /**
     * @param dt
     *            the dt to set
     */
    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    /**
     * @return the channelIndex
     */
    public Long getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    /**
     * @return the channelType
     */
    public int getChannelType() {
        return channelType;
    }

    /**
     * @param channelType
     *            the channelType to set
     */
    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    /**
     * @return the channelInOctetsRate
     */
    public float getChannelInOctetsRate() {
        return channelInOctetsRate;
    }

    /**
     * @param channelInOctetsRate
     *            the channelInOctetsRate to set
     */
    public void setChannelInOctetsRate(float channelInOctetsRate) {
        this.channelInOctetsRate = channelInOctetsRate;
    }

    /**
     * @return the channelOutOctetsRate
     */
    public float getChannelOutOctetsRate() {
        return channelOutOctetsRate;
    }

    /**
     * @param channelOutOctetsRate
     *            the channelOutOctetsRate to set
     */
    public void setChannelOutOctetsRate(float channelOutOctetsRate) {
        this.channelOutOctetsRate = channelOutOctetsRate;
    }

    /**
     * @return the channelInOctets
     */
    public Long getChannelInOctets() {
        return channelInOctets;
    }

    /**
     * @param channelInOctets
     *            the channelInOctets to set
     */
    public void setChannelInOctets(Long channelInOctets) {
        this.channelInOctets = channelInOctets;
    }

    /**
     * @return the channelOutOctets
     */
    public Long getChannelOutOctets() {
        return channelOutOctets;
    }

    /**
     * @param channelOutOctets
     *            the channelOutOctets to set
     */
    public void setChannelOutOctets(Long channelOutOctets) {
        this.channelOutOctets = channelOutOctets;
    }

    /**
     * @return the channelOctetsRate
     */
    public float getChannelOctetsRate() {
        return channelOctetsRate;
    }

    /**
     * @param channelOctetsRate
     *            the channelOctetsRate to set
     */
    public void setChannelOctetsRate(float channelOctetsRate) {
        this.channelOctetsRate = channelOctetsRate;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelSpeedStatic [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", channelType=");
        builder.append(channelType);
        builder.append(", channelInOctets=");
        builder.append(channelInOctets);
        builder.append(", channelOutOctets=");
        builder.append(channelOutOctets);
        builder.append(", channelInOctetsRate=");
        builder.append(channelInOctetsRate);
        builder.append(", channelOutOctetsRate=");
        builder.append(channelOutOctetsRate);
        builder.append(", channelOctetsRate=");
        builder.append(channelOctetsRate);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
    
}
