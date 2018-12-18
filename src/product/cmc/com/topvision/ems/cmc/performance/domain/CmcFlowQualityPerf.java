/***********************************************************************
 * $Id: CmcFlowQualityPerf.java,v1.0 2013-8-13 下午02:36:32 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author Rod John
 * @created @2013-8-13-下午02:36:32
 * 
 */
@Scope("prototype")
@Service("cmcFlowQualityPerf")
public class CmcFlowQualityPerf extends OperClass {
    private static final long serialVersionUID = 3012228443646395491L;
    private Long cmcId;
    private List<Long> ifIndex = new ArrayList<Long>();
    private Boolean isNecessary = false;
    private Boolean isMacFlow = false;
    private Boolean isUpLinkFlow = false;
    private Boolean isChannelFlow = false;

    public CmcFlowQualityPerf() {
        super("cmcFlowQualityPerfSaver", "cmcFlowQualityPerfScheduler", "cmcFlowQualityPerf");
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
     * @return the ifIndex
     */
    public List<Long> getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(List<Long> ifIndex) {
        this.ifIndex = ifIndex;
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

    public void addIndex(Long index) {
        if (ifIndex == null) {
            ifIndex = new ArrayList<Long>();
        }
        ifIndex.add(index);
    }

    public void addIndex(List<Long> iList) {
        if (ifIndex == null) {
            ifIndex = new ArrayList<Long>();
        }
        ifIndex.addAll(iList);
    }

    @Override
    public boolean isTaskCancle() {
        if (ifIndex.size() == 0) {
            return true;
        }
        return false;
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

    public Boolean getIsMacFlow() {
        for (Long index : ifIndex) {
            if ((index & 0xFFFF) == 0) {
                return true;
            }
        }
        return isMacFlow;
    }

    public void setIsMacFlow(Boolean isMacFlow) {
        this.isMacFlow = isMacFlow;
    }

    public Boolean getIsUpLinkFlow() {
        for (Long index : ifIndex) {
            if (index == 1L || index == 2L) {
                return true;
            }
        }
        return isUpLinkFlow;
    }

    public void setIsUpLinkFlow(Boolean isUpLinkFlow) {
        this.isUpLinkFlow = isUpLinkFlow;
    }

    public Boolean getIsChannelFlow() {
        for (Long index : ifIndex) {
            if ((index & 0xFFFF) == 0) {
                continue;
            }
            if (index == 1L || index == 2L) {
                continue;
            }
            return true;
        }
        return isChannelFlow;
    }

    public void setIsChannelFlow(Boolean isChannelFlow) {
        this.isChannelFlow = isChannelFlow;
    }
}
