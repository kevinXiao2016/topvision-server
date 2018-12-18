/***********************************************************************
 * $Id: CmtsFlowQualityPerf.java,v1.0 2014-4-16 下午3:24:11 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.performance.domain.PerfTargetConstants;

/**
 * @author Rod John
 * @created @2014-4-16-下午3:24:11
 * 
 */
@Scope("prototype")
@Service("cmtsFlowQualityPerf")
public class CmtsFlowQualityPerf extends OperClass {
    private static final long serialVersionUID = 3012228443646395491L;
    private List<Long> upLinkIndexs = new ArrayList<>();
    private List<Long> channelIndexs = new ArrayList<>();
    private Boolean isNecessary = false;
    private Boolean isUpLinkFlow = false;
    private Boolean isChannelFlow = false;
    //在采集时需要根据类型判断是否进行速率采样
    private long typeId;

    public CmtsFlowQualityPerf() {
        super("cmtsFlowQualitySaver", "cmtsFlowQualityScheduler", "cmtsFlowQualityPerf");
    }

    /**
     * @return the upLinkIndexs
     */
    public List<Long> getUpLinkIndexs() {
        return upLinkIndexs;
    }

    /**
     * @param upLinkIndexs
     *            the upLinkIndexs to set
     */
    public void setUpLinkIndexs(List<Long> upLinkIndexs) {
        this.upLinkIndexs = upLinkIndexs;
    }

    /**
     * @return the channelIndex
     */
    public List<Long> getChannelIndexs() {
        return channelIndexs;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndexs(List<Long> channelIndexs) {
        this.channelIndexs = channelIndexs;
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

    @Override
    public boolean isTaskCancle() {
        return !(this.isUpLinkFlow || this.isChannelFlow);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void shutdownTarget(String targetName, Object data) {
        List<Long> index = (List<Long>) data;
        if (targetName.equals(PerfTargetConstants.CMC_UPLINKFLOW)) {
            this.isUpLinkFlow = false;
            upLinkIndexs.removeAll(index);
        } else if (targetName.equals(PerfTargetConstants.CMC_CHANNELSPEED)) {
            this.isChannelFlow = false;
            channelIndexs.removeAll(index);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        List<Long> index = (List<Long>) data;
        if (targetName.equals(PerfTargetConstants.CMC_UPLINKFLOW)) {
            this.isUpLinkFlow = true;
            upLinkIndexs.addAll(index);
        } else if (targetName.equals(PerfTargetConstants.CMC_CHANNELSPEED)) {
            this.isChannelFlow = true;
            channelIndexs.addAll(index);
        }
    }

    public Boolean getIsUpLinkFlow() {
        return isUpLinkFlow;
    }

    public void setIsUpLinkFlow(Boolean isUpLinkFlow) {
        this.isUpLinkFlow = isUpLinkFlow;
    }

    public Boolean getIsChannelFlow() {
        return isChannelFlow;
    }

    public void setIsChannelFlow(Boolean isChannelFlow) {
        this.isChannelFlow = isChannelFlow;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    @Override
    public void setDeviceTypeId(Long typeId) {
        this.typeId = typeId;
    }

}
