/***********************************************************************
 * $Id: OltSlotBasicPerf.java,v1.0 2013-7-19 上午09:23:16 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Rod John
 * @created @2013-7-19-上午09:23:16
 * 
 */
public class OltSlotBasicPerf implements Serializable {

    private static final long serialVersionUID = -655717929404458023L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.1", index = true)
    private Integer slotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.3")
    private Integer slotType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.4")
    private Integer slotCpuRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.5")
    private Integer slotMemSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.6")
    private Integer slotFreeMemSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.7")
    private Integer slotTotalFlash;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.8")
    private Integer slotFreeFlash;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.9")
    private Integer slotCurrentTemperature;

    /**
     * @return the slotIndex
     */
    public Integer getSlotIndex() {
        return slotIndex;
    }

    /**
     * @param slotIndex
     *            the slotIndex to set
     */
    public void setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    /**
     * @return the slotType
     */
    public Integer getSlotType() {
        return slotType;
    }

    /**
     * @param slotType
     *            the slotType to set
     */
    public void setSlotType(Integer slotType) {
        this.slotType = slotType;
    }

    /**
     * @return the slotCpuRatio
     */
    public Integer getSlotCpuRatio() {
        return slotCpuRatio;
    }

    /**
     * @param slotCpuRatio
     *            the slotCpuRatio to set
     */
    public void setSlotCpuRatio(Integer slotCpuRatio) {
        this.slotCpuRatio = slotCpuRatio;
    }

    /**
     * @return the slotMemSize
     */
    public Integer getSlotMemSize() {
        return slotMemSize;
    }

    /**
     * @param slotMemSize
     *            the slotMemSize to set
     */
    public void setSlotMemSize(Integer slotMemSize) {
        this.slotMemSize = slotMemSize;
    }

    /**
     * @return the slotFreeMemSize
     */
    public Integer getSlotFreeMemSize() {
        return slotFreeMemSize;
    }

    /**
     * @param slotFreeMemSize
     *            the slotFreeMemSize to set
     */
    public void setSlotFreeMemSize(Integer slotFreeMemSize) {
        this.slotFreeMemSize = slotFreeMemSize;
    }

    /**
     * @return the slotTotalFlash
     */
    public Integer getSlotTotalFlash() {
        return slotTotalFlash;
    }

    /**
     * @param slotTotalFlash
     *            the slotTotalFlash to set
     */
    public void setSlotTotalFlash(Integer slotTotalFlash) {
        this.slotTotalFlash = slotTotalFlash;
    }

    /**
     * @return the slotFreeFlash
     */
    public Integer getSlotFreeFlash() {
        return slotFreeFlash;
    }

    /**
     * @param slotFreeFlash
     *            the slotFreeFlash to set
     */
    public void setSlotFreeFlash(Integer slotFreeFlash) {
        this.slotFreeFlash = slotFreeFlash;
    }

    /**
     * @return the slotCurrentTemperature
     */
    public Integer getSlotCurrentTemperature() {
        return slotCurrentTemperature;
    }

    /**
     * @param slotCurrentTemperature
     *            the slotCurrentTemperature to set
     */
    public void setSlotCurrentTemperature(Integer slotCurrentTemperature) {
        this.slotCurrentTemperature = slotCurrentTemperature;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSlotBasicPerf [slotIndex=");
        builder.append(slotIndex);
        builder.append(", slotType=");
        builder.append(slotType);
        builder.append(", slotCpuRatio=");
        builder.append(slotCpuRatio);
        builder.append(", slotMemSize=");
        builder.append(slotMemSize);
        builder.append(", slotFreeMemSize=");
        builder.append(slotFreeMemSize);
        builder.append(", slotTotalFlash=");
        builder.append(slotTotalFlash);
        builder.append(", slotFreeFlash=");
        builder.append(slotFreeFlash);
        builder.append(", slotCurrentTemperature=");
        builder.append(slotCurrentTemperature);
        builder.append("]");
        return builder.toString();
    }

}
