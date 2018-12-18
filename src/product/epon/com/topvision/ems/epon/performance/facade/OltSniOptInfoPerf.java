/***********************************************************************
 * $Id: OltSniOptInfoPerf.java,v1.0 2013-7-19 下午02:17:39 $
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
 * @created @2013-7-19-下午02:17:39
 * 
 */
public class OltSniOptInfoPerf implements Serializable {

    private static final long serialVersionUID = -8386465491306715335L;
    private Long entityId;
    private Long sniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.1", index = true)
    private Integer topSniOptInfoCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.2", index = true)
    private Integer topSniOptInfoPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.9")
    private Long topSniOptInfoWorkingTemp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.10")
    private Long topSniOptInfoWorkingVoltage;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.11")
    private Long topSniOptInfoBiasCurrent;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.12")
    private Long topSniOptInfoTxPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.3.1.13")
    private Long topSniOptInfoRxPower;
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
     * @return the sniIndex
     */
    public Long getSniIndex() {
        if (sniIndex == null) {
            sniIndex = EponIndex.getSniIndex(topSniOptInfoCardIndex, topSniOptInfoPortIndex);
        }
        return sniIndex;
    }

    /**
     * @param sniIndex
     *            the sniIndex to set
     */
    public void setSniIndex(Long sniIndex) {
        this.sniIndex = sniIndex;
    }

    /**
     * @return the topSniOptInfoCardIndex
     */
    public Integer getTopSniOptInfoCardIndex() {
        return topSniOptInfoCardIndex;
    }

    /**
     * @param topSniOptInfoCardIndex
     *            the topSniOptInfoCardIndex to set
     */
    public void setTopSniOptInfoCardIndex(Integer topSniOptInfoCardIndex) {
        this.topSniOptInfoCardIndex = topSniOptInfoCardIndex;
    }

    /**
     * @return the topSniOptInfoPortIndex
     */
    public Integer getTopSniOptInfoPortIndex() {
        return topSniOptInfoPortIndex;
    }

    /**
     * @param topSniOptInfoPortIndex
     *            the topSniOptInfoPortIndex to set
     */
    public void setTopSniOptInfoPortIndex(Integer topSniOptInfoPortIndex) {
        this.topSniOptInfoPortIndex = topSniOptInfoPortIndex;
    }

    /**
     * @return the topSniOptInfoWorkingTemp
     */
    public Long getTopSniOptInfoWorkingTemp() {
        return topSniOptInfoWorkingTemp;
    }

    /**
     * @param topSniOptInfoWorkingTemp
     *            the topSniOptInfoWorkingTemp to set
     */
    public void setTopSniOptInfoWorkingTemp(Long topSniOptInfoWorkingTemp) {
        this.topSniOptInfoWorkingTemp = topSniOptInfoWorkingTemp;
    }

    /**
     * @return the topSniOptInfoWorkingVoltage
     */
    public Long getTopSniOptInfoWorkingVoltage() {
        return topSniOptInfoWorkingVoltage;
    }

    /**
     * @param topSniOptInfoWorkingVoltage
     *            the topSniOptInfoWorkingVoltage to set
     */
    public void setTopSniOptInfoWorkingVoltage(Long topSniOptInfoWorkingVoltage) {
        this.topSniOptInfoWorkingVoltage = topSniOptInfoWorkingVoltage;
    }

    /**
     * @return the topSniOptInfoBiasCurrent
     */
    public Long getTopSniOptInfoBiasCurrent() {
        return topSniOptInfoBiasCurrent;
    }

    /**
     * @param topSniOptInfoBiasCurrent
     *            the topSniOptInfoBiasCurrent to set
     */
    public void setTopSniOptInfoBiasCurrent(Long topSniOptInfoBiasCurrent) {
        this.topSniOptInfoBiasCurrent = topSniOptInfoBiasCurrent;
    }

    /**
     * @return the topSniOptInfoTxPower
     */
    public Long getTopSniOptInfoTxPower() {
        return topSniOptInfoTxPower;
    }

    /**
     * @param topSniOptInfoTxPower
     *            the topSniOptInfoTxPower to set
     */
    public void setTopSniOptInfoTxPower(Long topSniOptInfoTxPower) {
        this.topSniOptInfoTxPower = topSniOptInfoTxPower;
    }

    /**
     * @return the topSniOptInfoRxPower
     */
    public Long getTopSniOptInfoRxPower() {
        return topSniOptInfoRxPower;
    }

    /**
     * @param topSniOptInfoRxPower
     *            the topSniOptInfoRxPower to set
     */
    public void setTopSniOptInfoRxPower(Long topSniOptInfoRxPower) {
        this.topSniOptInfoRxPower = topSniOptInfoRxPower;
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
        builder.append("OltSniOptInfoPerf [entityId=");
        builder.append(entityId);
        builder.append(", sniIndex=");
        builder.append(sniIndex);
        builder.append(", topSniOptInfoCardIndex=");
        builder.append(topSniOptInfoCardIndex);
        builder.append(", topSniOptInfoPortIndex=");
        builder.append(topSniOptInfoPortIndex);
        builder.append(", topSniOptInfoWorkingTemp=");
        builder.append(topSniOptInfoWorkingTemp);
        builder.append(", topSniOptInfoWorkingVoltage=");
        builder.append(topSniOptInfoWorkingVoltage);
        builder.append(", topSniOptInfoBiasCurrent=");
        builder.append(topSniOptInfoBiasCurrent);
        builder.append(", topSniOptInfoTxPower=");
        builder.append(topSniOptInfoTxPower);
        builder.append(", topSniOptInfoRxPower=");
        builder.append(topSniOptInfoRxPower);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
