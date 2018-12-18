/***********************************************************************
 * $Id: CmcServiceQualityPerf.java,v1.0 2013-8-8 下午02:10:21 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.performance.domain.PerfTargetConstants;

/**
 * @author Rod John
 * @created @2013-8-8-下午02:10:21
 * 
 */
@Scope("prototype")
@Service("cmcServiceQualityPerf")
public class CmcServiceQualityPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = -9056266995215651511L;
    private Long cmcId;
    private Long cmcIndex;
    private Boolean isNecessary = false;
    private Boolean isCpuPerf = false;
    private Boolean isMemPerf = false;
    private Boolean isFlashPerf = false;

    public CmcServiceQualityPerf() {
        super("cmcServiceQualityPerfSaver", "cmcServiceQualityPerfScheduler", "cmcServiceQualityPerf");
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
     * @return the isNecessary
     */
    public Boolean getIsNecessary() {
        return isNecessary;
    }

    /**
     * @param isNecessary
     *            the isNecessary to set
     */
    public void setIsNecessary(Boolean isNecessary) {
        this.isNecessary = isNecessary;
    }

    /**
     * @return the isCpuPerf
     */
    public Boolean getIsCpuPerf() {
        return isCpuPerf;
    }

    /**
     * @param isCpuPerf
     *            the isCpuPerf to set
     */
    public void setIsCpuPerf(Boolean isCpuPerf) {
        this.isCpuPerf = isCpuPerf;
    }

    /**
     * @return the isMemPerf
     */
    public Boolean getIsMemPerf() {
        return isMemPerf;
    }

    /**
     * @param isMemPerf
     *            the isMemPerf to set
     */
    public void setIsMemPerf(Boolean isMemPerf) {
        this.isMemPerf = isMemPerf;
    }

    /**
     * @return the isFlashPerf
     */
    public Boolean getIsFlashPerf() {
        return isFlashPerf;
    }

    /**
     * @param isFlashPerf
     *            the isFlashPerf to set
     */
    public void setIsFlashPerf(Boolean isFlashPerf) {
        this.isFlashPerf = isFlashPerf;
    }

    @Override
    public long getIdentifyKey() {
        return cmcId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.cmcId = identifyKey;
    }

    @Override
    public String[] makeOids() {
        return null;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
    }

    @Override
    public boolean isTaskCancle() {
        return !(isCpuPerf || isFlashPerf || isMemPerf);
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.CMC_CPUUSED)) {
            this.isCpuPerf = false;
        } else if (targetName.equals(PerfTargetConstants.CMC_FLASHUSED)) {
            this.isFlashPerf = false;
        } else if (targetName.equals(PerfTargetConstants.CMC_MEMUSED)) {
            this.isMemPerf = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        List<Long> index = (List<Long>) data;
        if (index != null && index.size() == 1) {
            this.cmcIndex = index.get(0);
        }
        if (targetName.equals(PerfTargetConstants.CMC_CPUUSED)) {
            this.isCpuPerf = true;
        } else if (targetName.equals(PerfTargetConstants.CMC_FLASHUSED)) {
            this.isFlashPerf = true;
        } else if (targetName.equals(PerfTargetConstants.CMC_MEMUSED)) {
            this.isMemPerf = true;
        }
    }
}
