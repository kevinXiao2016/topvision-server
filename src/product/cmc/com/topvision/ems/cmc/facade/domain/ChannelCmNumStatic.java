/***********************************************************************
 * $Id: ChannelCmNumStatic.java,v1.0 2012-7-11 下午02:32:05 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author loyal
 * @created @2012-7-11-下午02:32:05
 * 
 */
public class ChannelCmNumStatic implements Serializable {
    private static final long serialVersionUID = -1348183609901117635L;
    private Long cmcId;
    private Long entityId;
    private Long cmcPortId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.1")
    private Integer cmNumTotal;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.2")
    private Integer cmNumOnline;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.3")
    private Integer cmNumActive;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.4")
    private Integer cmNumUnregistered;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.5")
    private Integer cmNumOffline;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.3.7.1.6")
    private Integer CmNumRregistered;// 注册cm数

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
     * @return the cmNumTotal
     */
    public Integer getCmNumTotal() {
        return cmNumTotal;
    }

    /**
     * @param cmNumTotal
     *            the cmNumTotal to set
     */
    public void setCmNumTotal(Integer cmNumTotal) {
        this.cmNumTotal = cmNumTotal;
    }

    /**
     * @return the cmNumOnline
     */
    public Integer getCmNumOnline() {
        return cmNumOnline;
    }

    /**
     * @param cmNumOnline
     *            the cmNumOnline to set
     */
    public void setCmNumOnline(Integer cmNumOnline) {
        this.cmNumOnline = cmNumOnline;
    }

    /**
     * @return the cmNumActive
     */
    public Integer getCmNumActive() {
        return cmNumActive;
    }

    /**
     * @param cmNumActive
     *            the cmNumActive to set
     */
    public void setCmNumActive(Integer cmNumActive) {
        this.cmNumActive = cmNumActive;
    }

    /**
     * @return the cmNumUnregistered
     */
    public Integer getCmNumUnregistered() {
        return cmNumUnregistered;
    }

    /**
     * @param cmNumUnregistered
     *            the cmNumUnregistered to set
     */
    public void setCmNumUnregistered(Integer cmNumUnregistered) {
        this.cmNumUnregistered = cmNumUnregistered;
    }

    /**
     * @return the cmNumOffline
     */
    public Integer getCmNumOffline() {
        return cmNumOffline;
    }

    /**
     * @param cmNumOffline
     *            the cmNumOffline to set
     */
    public void setCmNumOffline(Integer cmNumOffline) {
        this.cmNumOffline = cmNumOffline;
    }

    /**
     * @return the cmNumRregistered
     */
    public Integer getCmNumRregistered() {
        return CmNumRregistered;
    }

    /**
     * @param cmNumRregistered
     *            the cmNumRregistered to set
     */
    public void setCmNumRregistered(Integer cmNumRregistered) {
        CmNumRregistered = cmNumRregistered;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelCmNumStatic [cmcId=");
        builder.append(cmcId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", cmNumTotal=");
        builder.append(cmNumTotal);
        builder.append(", cmNumOnline=");
        builder.append(cmNumOnline);
        builder.append(", cmNumActive=");
        builder.append(cmNumActive);
        builder.append(", cmNumUnregistered=");
        builder.append(cmNumUnregistered);
        builder.append(", cmNumOffline=");
        builder.append(cmNumOffline);
        builder.append(", CmNumRregistered=");
        builder.append(CmNumRregistered);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
    
}
