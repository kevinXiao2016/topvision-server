/***********************************************************************
 * $Id: CmcLinkQualityFor8800A.java,v1.0 2013-8-14 下午04:37:21 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2013-8-14-下午04:37:21
 * 
 */
@Alias("cmcLinkQualityFor8800A")
public class CmcLinkQualityFor8800A implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8082367002309942856L;
    private Long cmcId;
    private Long cmcIndex;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.1", index = true)
    private Long onuPonPortOpticalTransmissionPropertyDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.2", index = true)
    private Long onuPonPortOpticalTransmissionPropertyCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.3", index = true)
    private Long onuPonPortOpticalTransmissionPropertyPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.4")
    private Long onuReceivedOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.5")
    private Long onuTramsmittedOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.6")
    private Long onuBiasCurrent;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.7")
    private Long onuWorkingVoltage;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.8")
    private Long onuWorkingTemperature;
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

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the onuPonPortOpticalTransmissionPropertyDeviceIndex
     */
    public Long getOnuPonPortOpticalTransmissionPropertyDeviceIndex() {
        return onuPonPortOpticalTransmissionPropertyDeviceIndex;
    }

    /**
     * @param onuPonPortOpticalTransmissionPropertyDeviceIndex
     *            the onuPonPortOpticalTransmissionPropertyDeviceIndex to set
     */
    public void setOnuPonPortOpticalTransmissionPropertyDeviceIndex(
            Long onuPonPortOpticalTransmissionPropertyDeviceIndex) {
        this.onuPonPortOpticalTransmissionPropertyDeviceIndex = onuPonPortOpticalTransmissionPropertyDeviceIndex;
    }

    /**
     * @return the onuPonPortOpticalTransmissionPropertyCardIndex
     */
    public Long getOnuPonPortOpticalTransmissionPropertyCardIndex() {
        return onuPonPortOpticalTransmissionPropertyCardIndex;
    }

    /**
     * @param onuPonPortOpticalTransmissionPropertyCardIndex
     *            the onuPonPortOpticalTransmissionPropertyCardIndex to set
     */
    public void setOnuPonPortOpticalTransmissionPropertyCardIndex(Long onuPonPortOpticalTransmissionPropertyCardIndex) {
        this.onuPonPortOpticalTransmissionPropertyCardIndex = onuPonPortOpticalTransmissionPropertyCardIndex;
    }

    /**
     * @return the onuPonPortOpticalTransmissionPropertyPortIndex
     */
    public Long getOnuPonPortOpticalTransmissionPropertyPortIndex() {
        return onuPonPortOpticalTransmissionPropertyPortIndex;
    }

    /**
     * @param onuPonPortOpticalTransmissionPropertyPortIndex
     *            the onuPonPortOpticalTransmissionPropertyPortIndex to set
     */
    public void setOnuPonPortOpticalTransmissionPropertyPortIndex(Long onuPonPortOpticalTransmissionPropertyPortIndex) {
        this.onuPonPortOpticalTransmissionPropertyPortIndex = onuPonPortOpticalTransmissionPropertyPortIndex;
    }

    /**
     * @return the onuReceivedOpticalPower
     */
    public Long getOnuReceivedOpticalPower() {
        return onuReceivedOpticalPower;
    }

    /**
     * @param onuReceivedOpticalPower
     *            the onuReceivedOpticalPower to set
     */
    public void setOnuReceivedOpticalPower(Long onuReceivedOpticalPower) {
        this.onuReceivedOpticalPower = onuReceivedOpticalPower;
    }

    /**
     * @return the onuTramsmittedOpticalPower
     */
    public Long getOnuTramsmittedOpticalPower() {
        return onuTramsmittedOpticalPower;
    }

    /**
     * @param onuTramsmittedOpticalPower
     *            the onuTramsmittedOpticalPower to set
     */
    public void setOnuTramsmittedOpticalPower(Long onuTramsmittedOpticalPower) {
        this.onuTramsmittedOpticalPower = onuTramsmittedOpticalPower;
    }

    /**
     * @return the onuBiasCurrent
     */
    public Long getOnuBiasCurrent() {
        return onuBiasCurrent;
    }

    /**
     * @param onuBiasCurrent
     *            the onuBiasCurrent to set
     */
    public void setOnuBiasCurrent(Long onuBiasCurrent) {
        this.onuBiasCurrent = onuBiasCurrent;
    }

    /**
     * @return the onuWorkingVoltage
     */
    public Long getOnuWorkingVoltage() {
        return onuWorkingVoltage;
    }

    /**
     * @param onuWorkingVoltage
     *            the onuWorkingVoltage to set
     */
    public void setOnuWorkingVoltage(Long onuWorkingVoltage) {
        this.onuWorkingVoltage = onuWorkingVoltage;
    }

    /**
     * @return the onuWorkingTemperature
     */
    public Long getOnuWorkingTemperature() {
        return onuWorkingTemperature;
    }

    /**
     * @param onuWorkingTemperature
     *            the onuWorkingTemperature to set
     */
    public void setOnuWorkingTemperature(Long onuWorkingTemperature) {
        this.onuWorkingTemperature = onuWorkingTemperature;
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

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuPonPortOpticalTransmissionPropertyDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        this.onuPonPortOpticalTransmissionPropertyCardIndex = 0L;
        this.onuPonPortOpticalTransmissionPropertyPortIndex = 0L;
        this.onuIndex = onuIndex;
    }

}
