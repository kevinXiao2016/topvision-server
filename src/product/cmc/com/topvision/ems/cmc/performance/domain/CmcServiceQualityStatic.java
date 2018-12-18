/***********************************************************************
 * $Id: CmcServiceQualityStatic.java,v1.0 2013-12-3 上午09:46:17 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-12-3-上午09:46:17
 * 
 */
public class CmcServiceQualityStatic implements AliasesSuperType {
    private static final long serialVersionUID = -4212289124890610980L;
    public static final String CPU = "cpuUsed";
    public static final String MEM = "memUsed";
    public static final String FLASH = "flashUsed";
    private Long cmcId;
    private Long cmcIndex;
    private Float cpuValue;
    private Float memValue;
    private Float flashValue;
    private String cpuCollectTime;
    private String memCollectTime;
    private String flashCollectTime;

    public void setPerfCommons(List<CmcPerfCommon> perfCommons) {
        for (CmcPerfCommon perfCommon : perfCommons) {
            if (CPU.equals(perfCommon.getTargetName())) {
                this.cpuValue = perfCommon.getTargetValue();
                this.cpuCollectTime = perfCommon.getCollectTime();
            } else if (MEM.equals(perfCommon.getTargetName())) {
                this.memValue = perfCommon.getTargetValue();
                this.memCollectTime = perfCommon.getCollectTime();
            } else if (FLASH.equals(perfCommon.getTargetName())) {
                this.flashValue = perfCommon.getTargetValue();
                this.flashCollectTime = perfCommon.getCollectTime();
            }
        }
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
     * @return the cpuValue
     */
    public Float getCpuValue() {
        return cpuValue;
    }

    /**
     * @param cpuValue
     *            the cpuValue to set
     */
    public void setCpuValue(Float cpuValue) {
        this.cpuValue = cpuValue;
    }

    /**
     * @return the memValue
     */
    public Float getMemValue() {
        return memValue;
    }

    /**
     * @param memValue
     *            the memValue to set
     */
    public void setMemValue(Float memValue) {
        this.memValue = memValue;
    }

    /**
     * @return the flashValue
     */
    public Float getFlashValue() {
        return flashValue;
    }

    /**
     * @param flashValue
     *            the flashValue to set
     */
    public void setFlashValue(Float flashValue) {
        this.flashValue = flashValue;
    }

    /**
     * @return the cpuCollectTime
     */
    public String getCpuCollectTime() {
        return cpuCollectTime;
    }

    /**
     * @param cpuCollectTime
     *            the cpuCollectTime to set
     */
    public void setCpuCollectTime(String cpuCollectTime) {
        this.cpuCollectTime = cpuCollectTime;
    }

    /**
     * @return the memCollectTime
     */
    public String getMemCollectTime() {
        return memCollectTime;
    }

    /**
     * @param memCollectTime
     *            the memCollectTime to set
     */
    public void setMemCollectTime(String memCollectTime) {
        this.memCollectTime = memCollectTime;
    }

    /**
     * @return the flashCollectTime
     */
    public String getFlashCollectTime() {
        return flashCollectTime;
    }

    /**
     * @param flashCollectTime
     *            the flashCollectTime to set
     */
    public void setFlashCollectTime(String flashCollectTime) {
        this.flashCollectTime = flashCollectTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcServiceQualityStatic [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cpuValue=");
        builder.append(cpuValue);
        builder.append(", memValue=");
        builder.append(memValue);
        builder.append(", flashValue=");
        builder.append(flashValue);
        builder.append(", cpuCollectTime=");
        builder.append(cpuCollectTime);
        builder.append(", memCollectTime=");
        builder.append(memCollectTime);
        builder.append(", flashCollectTime=");
        builder.append(flashCollectTime);
        builder.append("]");
        return builder.toString();
    }

}
