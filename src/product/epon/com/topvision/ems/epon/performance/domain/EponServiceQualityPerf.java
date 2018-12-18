/***********************************************************************
 * $Id: EponServiceQualityPerf.java,v1.0 2013-7-18 下午07:10:15 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.performance.domain.PerfTargetConstants;

/**
 * @author Rod John
 * @created @2013-7-18-下午07:10:15
 * 
 */
@Scope("prototype")
@Service("eponServiceQualityPerf")
public class EponServiceQualityPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = 8850911711522154599L;
    private Boolean isNecessary = false;
    private Boolean isCpuPerf = false;
    private Boolean isMemPerf = false;
    private Boolean isFlashPerf = false;
    private Boolean isBoardTemp = false;
    private Boolean isFanSpeed = false;

    public EponServiceQualityPerf() {
        super("eponServiceQualityPerfSaver", "eponServiceQualityPerfScheduler", "eponServiceQualityPerf");
    }


    @Override
    public long getIdentifyKey() {
        return entityId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.entityId = identifyKey;
    }

    @Override
    public String[] makeOids() {
        return null;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
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

    /**
     * @return the isBoardTemp
     */
    public Boolean getIsBoardTemp() {
        return isBoardTemp;
    }

    /**
     * @param isBoardTemp
     *            the isBoardTemp to set
     */
    public void setIsBoardTemp(Boolean isBoardTemp) {
        this.isBoardTemp = isBoardTemp;
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
     * @return the isFanSpeed
     */
    public Boolean getIsFanSpeed() {
        return isFanSpeed;
    }

    /**
     * @param isFanSpeed
     *            the isFanSpeed to set
     */
    public void setIsFanSpeed(Boolean isFanSpeed) {
        this.isFanSpeed = isFanSpeed;
    }

    @Override
    public boolean isTaskCancle() {
        return !(isCpuPerf || isFanSpeed || isMemPerf || isBoardTemp || isFlashPerf);
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.OLT_CPUUSED)) {
            this.isCpuPerf = false;
        } else if (targetName.equals(PerfTargetConstants.OLT_BOARDTEMP)) {
            this.isBoardTemp = false;
        } else if (targetName.equals(PerfTargetConstants.OLT_FANSPEED)) {
            this.isFanSpeed = false;
        } else if (targetName.equals(PerfTargetConstants.OLT_FLASHUSED)) {
            this.isFlashPerf = false;
        } else if (targetName.equals(PerfTargetConstants.OLT_MEMUSED)) {
            this.isMemPerf = false;
        }
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.OLT_CPUUSED)) {
            this.isCpuPerf = true;
        } else if (targetName.equals(PerfTargetConstants.OLT_BOARDTEMP)) {
            this.isBoardTemp = true;
        } else if (targetName.equals(PerfTargetConstants.OLT_FANSPEED)) {
            this.isFanSpeed = true;
        } else if (targetName.equals(PerfTargetConstants.OLT_FLASHUSED)) {
            this.isFlashPerf = true;
        } else if (targetName.equals(PerfTargetConstants.OLT_MEMUSED)) {
            this.isMemPerf = true;
        }
    }
}
