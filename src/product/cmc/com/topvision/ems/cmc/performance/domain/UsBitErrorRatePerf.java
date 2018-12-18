/***********************************************************************
 * $Id: UsBitErrorRatePerf.java,v1.0 2012-7-12 下午04:01:53 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author loyal
 * @created @2012-7-12-下午04:01:53
 * 
 */
public class UsBitErrorRatePerf extends OperClass {
    private static final long serialVersionUID = 1447878151027232908L;
    private Long cmcId;
    private List<Long> channelIndex = new ArrayList<Long>();

    public UsBitErrorRatePerf() {
        super("usBitErrorRatePerfDBSaver", "usBitErrorRatePerfScheduler", "usBitErrorRatePerf");
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
    public long getIdentifyKey() {
        return cmcId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.cmcId = identifyKey;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UsBitErrorRatePerf [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", perfService=");
        builder.append(perfService);
        builder.append(", scheduler=");
        builder.append(scheduler);
        builder.append(", category=");
        builder.append(category);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

    @Override
    public boolean isTaskCancle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void shutdownTarget(String targetName, Object data) {
        List<Long> channelIndexList = (List<Long>) data;
        channelIndex.removeAll(channelIndexList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        List<Long> channelIndexList = (List<Long>) data;
        channelIndex.addAll(channelIndexList);
    }
}
