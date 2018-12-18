/***********************************************************************
 * $Id: CmcLinkQuality.java,v1.0 2013-8-8 下午08:09:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Rod John
 * @created @2013-8-8-下午08:09:06
 * 
 */
public class CmcLinkQuality implements Serializable {

    private static final long serialVersionUID = 8082367002309942856L;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.1", index = true)
    private Long opticalTransceiverIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.9")
    private Long opticalTransceiverRxPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.10")
    private Long opticalTransceiverTxPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.11")
    private Long opticalTransceiverBiasCurrent;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.12")
    private Long opticalTransceiverVoltage;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.13")
    private Long opticalTransceiverTemperature;
    private Timestamp collectTime;

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

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the opticalTransceiverIndex
     */
    public Long getOpticalTransceiverIndex() {
        return opticalTransceiverIndex;
    }

    /**
     * @param opticalTransceiverIndex
     *            the opticalTransceiverIndex to set
     */
    public void setOpticalTransceiverIndex(Long opticalTransceiverIndex) {
        this.opticalTransceiverIndex = opticalTransceiverIndex;
    }

    /**
     * @return the opticalTransceiverRxPower
     */
    public Long getOpticalTransceiverRxPower() {
        return opticalTransceiverRxPower;
    }

    /**
     * @param opticalTransceiverRxPower
     *            the opticalTransceiverRxPower to set
     */
    public void setOpticalTransceiverRxPower(Long opticalTransceiverRxPower) {
        this.opticalTransceiverRxPower = opticalTransceiverRxPower;
    }

    /**
     * @return the opticalTransceiverTxPower
     */
    public Long getOpticalTransceiverTxPower() {
        return opticalTransceiverTxPower;
    }

    /**
     * @param opticalTransceiverTxPower
     *            the opticalTransceiverTxPower to set
     */
    public void setOpticalTransceiverTxPower(Long opticalTransceiverTxPower) {
        this.opticalTransceiverTxPower = opticalTransceiverTxPower;
    }

    /**
     * @return the opticalTransceiverBiasCurrent
     */
    public Long getOpticalTransceiverBiasCurrent() {
        return opticalTransceiverBiasCurrent;
    }

    /**
     * @param opticalTransceiverBiasCurrent
     *            the opticalTransceiverBiasCurrent to set
     */
    public void setOpticalTransceiverBiasCurrent(Long opticalTransceiverBiasCurrent) {
        this.opticalTransceiverBiasCurrent = opticalTransceiverBiasCurrent;
    }

    /**
     * @return the opticalTransceiverVoltage
     */
    public Long getOpticalTransceiverVoltage() {
        return opticalTransceiverVoltage;
    }

    /**
     * @param opticalTransceiverVoltage
     *            the opticalTransceiverVoltage to set
     */
    public void setOpticalTransceiverVoltage(Long opticalTransceiverVoltage) {
        this.opticalTransceiverVoltage = opticalTransceiverVoltage;
    }

    /**
     * @return the opticalTransceiverTemperature
     */
    public Long getOpticalTransceiverTemperature() {
        return opticalTransceiverTemperature;
    }

    /**
     * @param opticalTransceiverTemperature
     *            the opticalTransceiverTemperature to set
     */
    public void setOpticalTransceiverTemperature(Long opticalTransceiverTemperature) {
        this.opticalTransceiverTemperature = opticalTransceiverTemperature;
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
        builder.append("CmcLinkQuality [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", opticalTransceiverIndex=");
        builder.append(opticalTransceiverIndex);
        builder.append(", opticalTransceiverRxPower=");
        builder.append(opticalTransceiverRxPower);
        builder.append(", opticalTransceiverTxPower=");
        builder.append(opticalTransceiverTxPower);
        builder.append(", opticalTransceiverBiasCurrent=");
        builder.append(opticalTransceiverBiasCurrent);
        builder.append(", opticalTransceiverVoltage=");
        builder.append(opticalTransceiverVoltage);
        builder.append(", opticalTransceiverTemperature=");
        builder.append(opticalTransceiverTemperature);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
