/***********************************************************************
 * $Id: OltPonOptInfoPerf.java,v1.0 2013-7-19 下午02:16:29 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2013-7-19-下午02:16:29
 * 
 */
public class OltPonOptInfoPerf implements Serializable {

    private static final long serialVersionUID = 884050920433609695L;
    private Long entityId;
    private Long ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.1", index = true)
    private Integer topPonOptInfoCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.2", index = true)
    private Integer topPonOptInfoPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.9")
    private Long topPonOptInfoWorkingTemp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.10")
    private Long topPonOptInfoWorkingVoltage;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.11")
    private Long topPonOptInfoBiasCurrent;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.12")
    private Long topPonOptInfoTxPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.5.1.13")
    private Long topPonOptInfoRxPower;
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
     * @return the ponIndex
     */
    public Long getPonIndex() {
        if (ponIndex == null) {
            ponIndex = EponIndex.getPonIndex(topPonOptInfoCardIndex, topPonOptInfoPortIndex);
        }
        return ponIndex;
    }

    /**
     * @param ponIndex
     *            the ponIndex to set
     */
    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    /**
     * @return the topPonOptInfoCardIndex
     */
    public Integer getTopPonOptInfoCardIndex() {
        return topPonOptInfoCardIndex;
    }

    /**
     * @param topPonOptInfoCardIndex
     *            the topPonOptInfoCardIndex to set
     */
    public void setTopPonOptInfoCardIndex(Integer topPonOptInfoCardIndex) {
        this.topPonOptInfoCardIndex = topPonOptInfoCardIndex;
    }

    /**
     * @return the topPonOptInfoPortIndex
     */
    public Integer getTopPonOptInfoPortIndex() {
        return topPonOptInfoPortIndex;
    }

    /**
     * @param topPonOptInfoPortIndex
     *            the topPonOptInfoPortIndex to set
     */
    public void setTopPonOptInfoPortIndex(Integer topPonOptInfoPortIndex) {
        this.topPonOptInfoPortIndex = topPonOptInfoPortIndex;
    }

    /**
     * @return the topPonOptInfoWorkingTemp
     */
    public Long getTopPonOptInfoWorkingTemp() {
        return topPonOptInfoWorkingTemp;
    }

    /**
     * @param topPonOptInfoWorkingTemp
     *            the topPonOptInfoWorkingTemp to set
     */
    public void setTopPonOptInfoWorkingTemp(Long topPonOptInfoWorkingTemp) {
        this.topPonOptInfoWorkingTemp = topPonOptInfoWorkingTemp;
    }

    /**
     * @return the topPonOptInfoWorkingVoltage
     */
    public Long getTopPonOptInfoWorkingVoltage() {
        return topPonOptInfoWorkingVoltage;
    }

    /**
     * @param topPonOptInfoWorkingVoltage
     *            the topPonOptInfoWorkingVoltage to set
     */
    public void setTopPonOptInfoWorkingVoltage(Long topPonOptInfoWorkingVoltage) {
        this.topPonOptInfoWorkingVoltage = topPonOptInfoWorkingVoltage;
    }

    /**
     * @return the topPonOptInfoBiasCurrent
     */
    public Long getTopPonOptInfoBiasCurrent() {
        return topPonOptInfoBiasCurrent;
    }

    /**
     * @param topPonOptInfoBiasCurrent
     *            the topPonOptInfoBiasCurrent to set
     */
    public void setTopPonOptInfoBiasCurrent(Long topPonOptInfoBiasCurrent) {
        this.topPonOptInfoBiasCurrent = topPonOptInfoBiasCurrent;
    }

    /**
     * @return the topPonOptInfoTxPower
     */
    public Long getTopPonOptInfoTxPower() {
        return topPonOptInfoTxPower;
    }

    /**
     * @param topPonOptInfoTxPower
     *            the topPonOptInfoTxPower to set
     */
    public void setTopPonOptInfoTxPower(Long topPonOptInfoTxPower) {
        this.topPonOptInfoTxPower = topPonOptInfoTxPower;
    }

    /**
     * @return the topPonOptInfoRxPower
     */
    public Long getTopPonOptInfoRxPower() {
        return topPonOptInfoRxPower;
    }

    /**
     * @param topPonOptInfoRxPower
     *            the topPonOptInfoRxPower to set
     */
    public void setTopPonOptInfoRxPower(Long topPonOptInfoRxPower) {
        this.topPonOptInfoRxPower = topPonOptInfoRxPower;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltPonOptInfoPerf [entityId=");
        builder.append(entityId);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", topPonOptInfoCardIndex=");
        builder.append(topPonOptInfoCardIndex);
        builder.append(", topPonOptInfoPortIndex=");
        builder.append(topPonOptInfoPortIndex);
        builder.append(", topPonOptInfoWorkingTemp=");
        builder.append(topPonOptInfoWorkingTemp);
        builder.append(", topPonOptInfoWorkingVoltage=");
        builder.append(topPonOptInfoWorkingVoltage);
        builder.append(", topPonOptInfoBiasCurrent=");
        builder.append(topPonOptInfoBiasCurrent);
        builder.append(", topPonOptInfoTxPower=");
        builder.append(topPonOptInfoTxPower);
        builder.append(", topPonOptInfoRxPower=");
        builder.append(topPonOptInfoRxPower);
        builder.append("]");
        return builder.toString();
    }

}
