/***********************************************************************
 * $Id: CmtsSignalQualityPerf.java,v1.0 2014-4-16 上午10:22:34 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.performance.domain.PerfTargetConstants;

/**
 * @author Rod John
 * @created @2014-4-16-上午10:22:34
 *
 */
@Scope("prototype")
@Service("cmtsSignalQualityPerf")
public class CmtsSignalQualityPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = -8128359058358519306L;
    private List<Long> channelIndexs;
    private Boolean isUnerror = false;
    private Boolean isNoise = false;
    private Boolean isNecessary = false;

    public CmtsSignalQualityPerf() {
        super("cmtsSignalQualitySaver", "cmtsSignalQualityScheduler", "cmtsSignalQualityPerf");
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
    public List<Long> getChannelIndexs() {
        return channelIndexs;
    }

    /**
     * @param cmcChannelIndexs
     *            the cmcChannelIndexs to set
     */
    public void setChannelIndexs(List<Long> channelIndexs) {
        this.channelIndexs = channelIndexs;
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
        if (channelIndexs == null) {
            channelIndexs = channelIndexList;
        }
    }
}
