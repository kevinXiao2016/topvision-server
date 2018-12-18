/***********************************************************************
 * $Id: CmcSingleQualityPerf.java,v1.0 2013-8-10 下午01:43:45 $
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
 * @created @2013-8-10-下午01:43:45
 * 
 */
@Scope("prototype")
@Service("cmcSignalQualityPerf")
public class CmcSignalQualityPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = -8128359058358519306L;
    private Long cmcId;
    private Long cmcIndex;
    private List<Long> cmcChannelIndexs;
    private Boolean isUnerror = false;
    private Boolean isNoise = false;
    private Boolean isNecessary = false;

    public CmcSignalQualityPerf() {
        super("cmcSignalQualityPerfSaver", "cmcSignalQualityPerfScheduler", "cmcSignalQualityPerf");
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
     * @return the isUnerror
     */
    public Boolean getIsUnerror() {
        return isUnerror;
    }

    /**
     * @param isUnerror
     *            the isUnerror to set
     */
    public void setIsUnerror(Boolean isUnerror) {
        this.isUnerror = isUnerror;
    }

    /**
     * @return the isNoise
     */
    public Boolean getIsNoise() {
        return isNoise;
    }

    /**
     * @param isNoise
     *            the isNoise to set
     */
    public void setIsNoise(Boolean isNoise) {
        this.isNoise = isNoise;
    }

    /**
     * @return the cmcChannelIndexs
     */
    public List<Long> getCmcChannelIndexs() {
        return cmcChannelIndexs;
    }

    /**
     * @param cmcChannelIndexs
     *            the cmcChannelIndexs to set
     */
    public void setCmcChannelIndexs(List<Long> cmcChannelIndexs) {
        this.cmcChannelIndexs = cmcChannelIndexs;
    }

    @Override
    public boolean isTaskCancle() {
        return !(isUnerror || isNoise);
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.CMC_BER)) {
            isUnerror = false;
        } else if (targetName.equals(PerfTargetConstants.CMC_SNR)) {
            isNoise = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        List<Long> channelIndexList = (List<Long>) data;
        if (targetName.equals(PerfTargetConstants.CMC_BER)) {
            isUnerror = true;
        } else if (targetName.equals(PerfTargetConstants.CMC_SNR)) {
            isNoise = true;
        }
        if (cmcChannelIndexs == null) {
            cmcChannelIndexs = channelIndexList;
        }
    }
}
