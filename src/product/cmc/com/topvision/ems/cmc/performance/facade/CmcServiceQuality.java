/***********************************************************************
 * $Id: CmcServiceQualityPerf.java,v1.0 2013-8-8 下午02:02:04 $
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

/**
 * @author Rod John
 * @created @2013-8-8-下午02:02:04
 * 
 */
@Alias("cmcServiceQuality")
public class CmcServiceQuality implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 3340555848830258670L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;// 是表的ifIndex，Mac域
    //@EMS-10489 把类A型设备的运行时长采集从sysUptime改为topCcmtsSysRunTime获取
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.19")
    private Long topCcmtsSysRunTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.3")
    private Long topCcmtsSysUpTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.10")
    private Integer topCcmtsSysRAMRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.11")
    private Integer topCcmtsSysCPURatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.13")
    private Integer topCcmtsSysFlashRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.14")
    private Integer topCcmtsSysStatus;
    private Timestamp collectTime;

    //用作性能阈值处理使用
    private Float cpuUsed;
    private Float memUsed;

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
     * @return the topCcmtsSysRAMRatio
     */
    public Integer getTopCcmtsSysRAMRatio() {
        return topCcmtsSysRAMRatio;
    }

    /**
     * @param topCcmtsSysRAMRatio
     *            the topCcmtsSysRAMRatio to set
     */
    public void setTopCcmtsSysRAMRatio(Integer topCcmtsSysRAMRatio) {
        this.topCcmtsSysRAMRatio = topCcmtsSysRAMRatio;
        if (topCcmtsSysRAMRatio != null) {
            this.memUsed = topCcmtsSysRAMRatio.floatValue();
        }
    }

    /**
     * @return the topCcmtsSysCPURatio
     */
    public Integer getTopCcmtsSysCPURatio() {
        return topCcmtsSysCPURatio;
    }

    /**
     * @param topCcmtsSysCPURatio
     *            the topCcmtsSysCPURatio to set
     */
    public void setTopCcmtsSysCPURatio(Integer topCcmtsSysCPURatio) {
        this.topCcmtsSysCPURatio = topCcmtsSysCPURatio;
        if (topCcmtsSysCPURatio != null) {
            this.cpuUsed = topCcmtsSysCPURatio.floatValue();
        }
    }

    /**
     * @return the topCcmtsSysFlashRatio
     */
    public Integer getTopCcmtsSysFlashRatio() {
        return topCcmtsSysFlashRatio;
    }

    /**
     * @param topCcmtsSysFlashRatio
     *            the topCcmtsSysFlashRatio to set
     */
    public void setTopCcmtsSysFlashRatio(Integer topCcmtsSysFlashRatio) {
        this.topCcmtsSysFlashRatio = topCcmtsSysFlashRatio;
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
     * @return the topCcmtsSysUpTime
     */
    public Long getTopCcmtsSysUpTime() {
        if (topCcmtsSysRunTime != null) {
            topCcmtsSysUpTime = topCcmtsSysRunTime;
        }
        return topCcmtsSysUpTime;
    }

    /**
     * @param topCcmtsSysUpTime the topCcmtsSysUpTime to set
     */
    public void setTopCcmtsSysUpTime(Long topCcmtsSysUpTime) {
        if (topCcmtsSysRunTime != null) {
            this.topCcmtsSysUpTime = topCcmtsSysRunTime;
        } else {
            this.topCcmtsSysUpTime = topCcmtsSysUpTime;
        }
    }
    
    /**
     * @return the topCcmtsSysRunTime
     */
    public Long getTopCcmtsSysRunTime() {
        return topCcmtsSysRunTime;
    }

    /**
     * @param topCcmtsSysRunTime the topCcmtsSysRunTime to set
     */
    public void setTopCcmtsSysRunTime(Long topCcmtsSysRunTime) {
        this.topCcmtsSysRunTime = topCcmtsSysRunTime;
    }

    /**
     * @return the topCcmtsSysStatus
     */
    public Integer getTopCcmtsSysStatus() {
        return topCcmtsSysStatus;
    }

    /**
     * @param topCcmtsSysStatus the topCcmtsSysStatus to set
     */
    public void setTopCcmtsSysStatus(Integer topCcmtsSysStatus) {
        this.topCcmtsSysStatus = topCcmtsSysStatus;
    }

    public Float getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(Float cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public Float getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Float memUsed) {
        this.memUsed = memUsed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcServiceQuality [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", topCcmtsSysRunTime=");
        builder.append(topCcmtsSysRunTime);
        builder.append(", topCcmtsSysUpTime=");
        builder.append(topCcmtsSysUpTime);
        builder.append(", topCcmtsSysRAMRatio=");
        builder.append(topCcmtsSysRAMRatio);
        builder.append(", topCcmtsSysCPURatio=");
        builder.append(topCcmtsSysCPURatio);
        builder.append(", topCcmtsSysFlashRatio=");
        builder.append(topCcmtsSysFlashRatio);
        builder.append(", topCcmtsSysStatus=");
        builder.append(topCcmtsSysStatus);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", cpuUsed=");
        builder.append(cpuUsed);
        builder.append(", memUsed=");
        builder.append(memUsed);
        builder.append("]");
        return builder.toString();
    }

}
