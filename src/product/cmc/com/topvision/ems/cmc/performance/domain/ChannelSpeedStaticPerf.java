/***********************************************************************
 * $Id: ChannelSpeedStaticPerf.java,v1.0 2012-7-15 上午11:21:05 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author loyal
 * @created @2012-7-15-上午11:21:05
 * 
 */
public class ChannelSpeedStaticPerf extends OperClass {
    private static final long serialVersionUID = 4893697577582761898L;
    private Long cmcId;
    private List<Long> channelIndex = new ArrayList<Long>();
    private List<Long> upChannelIndex = new ArrayList<Long>();
    private List<Long> downChannelIndex = new ArrayList<Long>();

    public ChannelSpeedStaticPerf() {
        super("channelSpeedStaticPerfDBSaver", "channelSpeedStaticPerfScheduler", "channelSpeedStaticPerf");
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
    public boolean isTaskCancle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void shutdownTarget(String targetName, Object data) {
        Map<String, List<Long>> indexMap = (Map<String, List<Long>>) data;
        upChannelIndex.removeAll(indexMap.get("upChannel"));
        downChannelIndex.removeAll(indexMap.get("downChannel"));
        channelIndex.removeAll(indexMap.get("channelIndex"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        Map<String, List<Long>> indexMap = (Map<String, List<Long>>) data;
        upChannelIndex.addAll(indexMap.get("upChannel"));
        downChannelIndex.addAll(indexMap.get("downChannel"));
        channelIndex.addAll(indexMap.get("channelIndex"));
    }

    public List<Long> getUpChannelIndex() {
        return upChannelIndex;
    }

    public void setUpChannelIndex(List<Long> upChannelIndex) {
        this.upChannelIndex = upChannelIndex;
    }

    public List<Long> getDownChannelIndex() {
        return downChannelIndex;
    }

    public void setDownChannelIndex(List<Long> downChannelIndex) {
        this.downChannelIndex = downChannelIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelSpeedStaticPerf [entityId=");
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
        builder.append(", upChannelIndex=");
        builder.append(upChannelIndex);
        builder.append(", downChannelIndex=");
        builder.append(downChannelIndex);
        builder.append("]");
        return builder.toString();
    }

}
