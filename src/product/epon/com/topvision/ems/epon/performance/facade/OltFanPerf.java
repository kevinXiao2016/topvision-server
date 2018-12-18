/***********************************************************************
 * $Id: OltFanPerf.java,v1.0 2013-7-19 下午02:03:16 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Rod John
 * @created @2013-7-19-下午02:03:16
 * 
 */
public class OltFanPerf implements Serializable {

    private static final long serialVersionUID = -590146108892839325L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.1", index = true)
    private Integer topSysFanCardIndex;
    private Long fanIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.2", index = true)
    private Integer topSysFanIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.5.1.1.4")
    private Integer fanSpeed;
    private Timestamp collectTime;

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
     * @return the topSysFanCardIndex
     */
    public Integer getTopSysFanCardIndex() {
        return topSysFanCardIndex;
    }

    /**
     * @param topSysFanCardIndex
     *            the topSysFanCardIndex to set
     */
    public void setTopSysFanCardIndex(Integer topSysFanCardIndex) {
        this.topSysFanCardIndex = topSysFanCardIndex;
    }

    /**
     * @return the fanIndex
     */
    public Long getFanIndex() {
        if (fanIndex == null) {
            fanIndex = EponIndex.getSlotIndex(topSysFanCardIndex);
        }
        return fanIndex;
    }

    /**
     * @param fanIndex
     *            the fanIndex to set
     */
    public void setFanIndex(Long fanIndex) {
        this.fanIndex = fanIndex;
    }

    /**
     * @return the topSysFanIndex
     */
    public Integer getTopSysFanIndex() {
        return topSysFanIndex;
    }

    /**
     * @param topSysFanIndex
     *            the topSysFanIndex to set
     */
    public void setTopSysFanIndex(Integer topSysFanIndex) {
        this.topSysFanIndex = topSysFanIndex;
    }

    /**
     * @return the fanSpeed
     */
    public Integer getFanSpeed() {
        return fanSpeed;
    }

    /**
     * @param fanSpeed
     *            the fanSpeed to set
     */
    public void setFanSpeed(Integer fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltFanPerf [topSysFanCardIndex=");
        builder.append(topSysFanCardIndex);
        builder.append(", fanIndex=");
        builder.append(fanIndex);
        builder.append(", topSysFanIndex=");
        builder.append(topSysFanIndex);
        builder.append(", fanSpeed=");
        builder.append(fanSpeed);
        builder.append("]");
        return builder.toString();
    }

}
