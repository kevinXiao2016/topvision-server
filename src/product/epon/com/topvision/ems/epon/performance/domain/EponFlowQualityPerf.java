/***********************************************************************
 * $Id: EponFlowQualityPerf.java,v1.0 2013-8-7 下午04:05:27 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.performance.domain.PerfTargetConstants;

/**
 * @author Rod John
 * @created @2013-8-7-下午04:05:27
 * 
 */
@Scope("prototype")
@Service("eponFlowQualityPerf")
public class EponFlowQualityPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = -6593915481826140284L;
    private List<Long> sniIndexList = new ArrayList<Long>();
    private List<Long> ponIndexList = new ArrayList<Long>();
    private Boolean isNecessary = false;

    public EponFlowQualityPerf() {
        super("eponFlowQualityPerfSaver", "eponFlowQualityPerfScheduler", "eponFlowQualityPerf");
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
     * @return the sniIndexList
     */
    public List<Long> getSniIndexList() {
        return sniIndexList;
    }

    /**
     * @param sniIndexList
     *            the sniIndexList to set
     */
    public void setSniIndexList(List<Long> sniIndexList) {
        this.sniIndexList = sniIndexList;
    }

    public void addSniIndex(Long sniIndex) {
        if (sniIndexList == null) {
            sniIndexList = new ArrayList<Long>();
        }
        sniIndexList.add(sniIndex);
    }

    /**
     * @return the ponIndexList
     */
    public List<Long> getPonIndexList() {
        return ponIndexList;
    }

    /**
     * @param ponIndexList
     *            the ponIndexList to set
     */
    public void setPonIndexList(List<Long> ponIndexList) {
        this.ponIndexList = ponIndexList;
    }

    public void addPonIndex(Long ponIndex) {
        if (ponIndexList == null) {
            ponIndexList = new ArrayList<Long>();
        }
        ponIndexList.add(ponIndex);
    }

    @Override
    public boolean isTaskCancle() {
        if (sniIndexList.size() + ponIndexList.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.OLT_SNIFLOW)) {
            this.sniIndexList.clear();
        } else if (targetName.equals(PerfTargetConstants.OLT_PONFLOW)) {
            this.ponIndexList.clear();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        List<Long> indexList = (List<Long>) data;
        if (targetName.equals(PerfTargetConstants.OLT_SNIFLOW)) {
            this.sniIndexList.addAll(indexList);
        } else if (targetName.equals(PerfTargetConstants.OLT_PONFLOW)) {
            this.ponIndexList.addAll(indexList);
        }
    }
}
