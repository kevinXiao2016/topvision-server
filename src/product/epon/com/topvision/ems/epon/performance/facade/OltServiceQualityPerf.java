/***********************************************************************
 * $Id: OltServiceQualityPerf.java,v1.0 2013-8-2 上午10:55:41 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2013-8-2-上午10:55:41
 * 
 */
@Alias("oltServiceQualityPerf")
public class OltServiceQualityPerf implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8010214763534982720L;
    private Long entityId;
    private Long slotId;
    private Long slotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.1", index = true)
    private Integer topSysBdSlotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.4")
    private Integer topSysBdCPUUseRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.5")
    private Integer topSysBdlMemSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.6")
    private Integer topSysBdFreeMemSize;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.7")
    private Integer topSysBdTotalFlashOctets;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.8")
    private Integer topSysBdFreeFlashOctets;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.9")
    private Integer topSysBdCurrentTemperature;
    private Integer bAttribute;
    private Timestamp collectTime;
    private Float cpuUsed;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.2.1.1")
    private Float flashUsed;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.2.1.2")
    private Float memUsed;

    /**
     * @return the slotId
     */
    public Long getSlotId() {
        return slotId;
    }

    /**
     * @param slotId
     *            the slotId to set
     */
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    /**
     * @return the topSysBdSlotIndex
     */
    public Integer getTopSysBdSlotIndex() {
        return topSysBdSlotIndex;
    }

    /**
     * @param topSysBdSlotIndex
     *            the topSysBdSlotIndex to set
     */
    public void setTopSysBdSlotIndex(Integer topSysBdSlotIndex) {
        this.topSysBdSlotIndex = topSysBdSlotIndex;
    }

    /**
     * @return the topSysBdCPUUseRatio
     */
    public Integer getTopSysBdCPUUseRatio() {
        return topSysBdCPUUseRatio;
    }

    /**
     * @param topSysBdCPUUseRatio
     *            the topSysBdCPUUseRatio to set
     */
    public void setTopSysBdCPUUseRatio(Integer topSysBdCPUUseRatio) {
        this.topSysBdCPUUseRatio = topSysBdCPUUseRatio;
    }

    /**
     * @return the topSysBdlMemSize
     */
    public Integer getTopSysBdlMemSize() {
        return topSysBdlMemSize;
    }

    /**
     * @param topSysBdlMemSize
     *            the topSysBdlMemSize to set
     */
    public void setTopSysBdlMemSize(Integer topSysBdlMemSize) {
        this.topSysBdlMemSize = topSysBdlMemSize;
    }

    /**
     * @return the topSysBdFreeMemSize
     */
    public Integer getTopSysBdFreeMemSize() {
        return topSysBdFreeMemSize;
    }

    /**
     * @param topSysBdFreeMemSize
     *            the topSysBdFreeMemSize to set
     */
    public void setTopSysBdFreeMemSize(Integer topSysBdFreeMemSize) {
        this.topSysBdFreeMemSize = topSysBdFreeMemSize;
    }

    /**
     * @return the topSysBdTotalFlashOctets
     */
    public Integer getTopSysBdTotalFlashOctets() {
        return topSysBdTotalFlashOctets;
    }

    /**
     * @param topSysBdTotalFlashOctets
     *            the topSysBdTotalFlashOctets to set
     */
    public void setTopSysBdTotalFlashOctets(Integer topSysBdTotalFlashOctets) {
        this.topSysBdTotalFlashOctets = topSysBdTotalFlashOctets;
    }

    /**
     * @return the topSysBdFreeFlashOctets
     */
    public Integer getTopSysBdFreeFlashOctets() {
        return topSysBdFreeFlashOctets;
    }

    /**
     * @param topSysBdFreeFlashOctets
     *            the topSysBdFreeFlashOctets to set
     */
    public void setTopSysBdFreeFlashOctets(Integer topSysBdFreeFlashOctets) {
        this.topSysBdFreeFlashOctets = topSysBdFreeFlashOctets;
    }

    /**
     * @return the topSysBdCurrentTemperature
     */
    public Integer getTopSysBdCurrentTemperature() {
        return topSysBdCurrentTemperature;
    }

    /**
     * @param topSysBdCurrentTemperature
     *            the topSysBdCurrentTemperature to set
     */
    public void setTopSysBdCurrentTemperature(Integer topSysBdCurrentTemperature) {
        this.topSysBdCurrentTemperature = topSysBdCurrentTemperature;
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
     * @return the slotIndex
     */
    public Long getSlotIndex() {
        if (slotIndex == null) {
            if (this.bAttribute == 1) {
                slotIndex = 0L;
            } else if (this.bAttribute == 2) {
                slotIndex = 1095216660480L;
            } else {
                slotIndex = EponIndex.getSlotIndex(this.topSysBdSlotIndex);
            }
        }
        return slotIndex;
    }

    /**
     * @param slotIndex
     *            the slotIndex to set
     */
    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
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

    public Float getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Float memUsed) {
        this.memUsed = memUsed;
    }

    public Float getFlashUsed() {
        return flashUsed;
    }

    public void setFlashUsed(Float flashUsed) {
        this.flashUsed = flashUsed;
    }

    public Float getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(Float cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    /**
     * @return the bAttribute
     */
    public Integer getbAttribute() {
        return bAttribute;
    }

    /**
     * @param bAttribute
     *            the bAttribute to set
     */
    public void setbAttribute(Integer bAttribute) {
        this.bAttribute = bAttribute;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltServiceQualityPerf [topSysBdSlotIndex=");
        builder.append(topSysBdSlotIndex);
        builder.append(", topSysBdCPUUseRatio=");
        builder.append(topSysBdCPUUseRatio);
        builder.append(", topSysBdlMemSize=");
        builder.append(topSysBdlMemSize);
        builder.append(", topSysBdFreeMemSize=");
        builder.append(topSysBdFreeMemSize);
        builder.append(", topSysBdTotalFlashOctets=");
        builder.append(topSysBdTotalFlashOctets);
        builder.append(", topSysBdFreeFlashOctets=");
        builder.append(topSysBdFreeFlashOctets);
        builder.append(", topSysBdCurrentTemperature=");
        builder.append(topSysBdCurrentTemperature);
        builder.append("]");
        return builder.toString();
    }

}
