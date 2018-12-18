/***********************************************************************
 * $Id: CmcCmStatusPerf.java,v1.0 2013-8-12 下午01:47:51 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author Rod John
 * @created @2013-8-12-下午01:47:51
 * 
 */
public class CmcCmNumberPerf extends OperClass {
    private static final long serialVersionUID = 6993718666458678419L;
    private Long cmcId;
    private List<Long> channelIndex;

    public CmcCmNumberPerf() {
        super("cmcCmNumberPerfSaver", "cmcCmNumberPerfScheduler", "cmcCmNumberQualityPerf");
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
     * @return the channelIndex
     */
    public List<Long> getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(List<Long> channelIndex) {
        this.channelIndex = channelIndex;
    }

    @Override
    public boolean isTaskCancle() {
        return false;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
    }
}
