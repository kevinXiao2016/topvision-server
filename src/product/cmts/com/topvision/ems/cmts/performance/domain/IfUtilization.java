/***********************************************************************
 * $Id: ChannelUtilization.java,v1.0 2012-5-8 下午01:41:57 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.constants.Symbol;

/**
 * @author loyal
 * @created @2012-5-8-下午01:41:57
 *
 */
public class IfUtilization implements Serializable {
    private static final long serialVersionUID = -1157132947186604778L;
    private Long entityId;
    private Long ifIndex;
    private Double ifUtilization;
    private Timestamp dt;
    private Long x;
    private Integer y;

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
     * @return the channelUtilization
     */
    public Double getIfUtilization() {
        return ifUtilization;
    }

    /**
     * @param ifUtilization
     *            the channelUtilization to set
     */
    public void setIfUtilization(Double ifUtilization) {
        this.ifUtilization = ifUtilization;
        this.y = (int)Math.rint(ifUtilization);
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
        this.x = dt.getTime();
    }

    /**
     * @return the channelIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the channelIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the x
     */
    public Long getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(Long x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public Integer getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelUtilization [entityId=");
        builder.append(entityId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifUtilization=");
        builder.append(ifUtilization);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(", x=");
        builder.append(x);
        builder.append(", y=");
        builder.append(y);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}