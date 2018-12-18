/***********************************************************************
 * $Id: UplinkSpeedStaticPerf.java,v1.0 2012-7-15 上午11:21:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author jay
 * @created @2012-7-15-上午11:21:05
 */
@Service("uplinkSpeedStaticPerf")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UplinkSpeedStaticPerf extends OperClass {
    private static final long serialVersionUID = 4893697577582761898L;
    private List<Long> ifIndex = new ArrayList<Long>();
    private Integer sampleCollect;

    public UplinkSpeedStaticPerf() {
        super("uplinkSpeedStaticPerfDBSaver", "uplinkSpeedStaticPerfScheduler", "uplinkSpeedStaticPerf");
    }

    /**
     * @return the channelIndex
     */
    public List<Long> getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex the channelIndex to set
     */
    public void setIfIndex(List<Long> ifIndex) {
        this.ifIndex = ifIndex;
    }

    @Override
    public long getIdentifyKey() {
        return entityId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.entityId = identifyKey;
    }

    /**
     * @return the sampleCollect
     */
    public Integer getSampleCollect() {
        return sampleCollect;
    }

    /**
     * @param sampleCollect
     *            the sampleCollect to set
     */
    public void setSampleCollect(Integer sampleCollect) {
        this.sampleCollect = sampleCollect;
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
        builder.append("UplinkSpeedStaticPerf [entityId=");
        builder.append(entityId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
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
        ifIndex.removeAll(channelIndexList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        List<Long> channelIndexList = (List<Long>) data;
        ifIndex.addAll(channelIndexList);
    }
}