/***********************************************************************
 * $Id: ChannelUtilization.java,v1.0 2012-5-8 下午01:41:57 $
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
 * @created @2012-5-8-下午01:41:57
 * 
 */
@Alias("channelUtilization")
public class ChannelUtilization implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1157132947186604778L;
    private Long entityId;
    private Long cmcId;
    private Long parentId;
    private Long channelIndex;
    private Integer channelUtilization;
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
     * @return the channelUtilization
     */
    public Integer getChannelUtilization() {
        return channelUtilization;
    }

    /**
     * @param channelUtilization
     *            the channelUtilization to set
     */
    public void setChannelUtilization(Integer channelUtilization) {
        this.channelUtilization = channelUtilization;
        this.y = channelUtilization;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelUtilization [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", channelUtilization=");
        builder.append(channelUtilization);
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
