/***********************************************************************
 * $Id: DsUserNumPerf.java,v1.0 2012-7-11 下午01:37:56 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author loyal
 * @created @2012-7-11-下午01:37:56
 * 
 */
public class ChannelCmNumPerf extends OperClass {
    private static final long serialVersionUID = -2886671948368167241L;
    private Long cmcId;
    private List<Long> channelIndex;

    public ChannelCmNumPerf() {
        super("channelCmNumPerfDBSaver", "channelCmNumPerfScheduler", "CC_CHANNELCMNUM");
    }

    @Override
    public long getIdentifyKey() {
        return cmcId;
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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelCmNumPerf [entityId=");
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
        return false;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.cmcId = identifyKey;
    }
}
