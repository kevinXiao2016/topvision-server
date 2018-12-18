/***********************************************************************
 * $Id: OnuFlowQualityPerf.java,v1.0 2015-5-7-上午11:18:32 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * 
 * @author flack
 * @created @2015-5-7-上午11:18:32
 *
 */
@Scope("prototype")
@Service("onuFlowQualityPerf")
public class OnuFlowQualityPerf extends OperClass {
    private static final long serialVersionUID = 29009501202263706L;

    private Long onuId;
    private Long onuIndex;
    private String onuEorG;
    private List<Long> uniIndexList = new ArrayList<Long>();
    private List<Long> ponIndexList = new ArrayList<Long>();
    private Boolean isNecessary = false;

    public OnuFlowQualityPerf() {
        super("onuFlowQualitySaver", "onuFlowQualityScheduler", "onuFlowQualityPerf");
    }

    @Override
    public long getIdentifyKey() {
        return this.onuId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.onuId = identifyKey;
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
     * @return the uniIndexList
     */
    public List<Long> getUniIndexList() {
        return uniIndexList;
    }

    /**
     * @param uniIndexList
     *            the uniIndexList to set
     */
    public void setUniIndexList(List<Long> uniIndexList) {
        this.uniIndexList = uniIndexList;
    }

    public void addUniIndex(Long uniIndex) {
        if (uniIndexList == null) {
            uniIndexList = new ArrayList<Long>();
        }
        uniIndexList.add(uniIndex);
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
        return !isNecessary;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        this.isNecessary = false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        Map<String, Object> dataMap = (Map<String, Object>) data;
        OnuInfo onuInfo = (OnuInfo) dataMap.get("onuInfo");
        this.entityId = onuInfo.getEntityId();
        this.onuIndex = onuInfo.getOnuIndex();
        this.onuEorG = onuInfo.getOnuEorG();
        this.ponIndexList = (List<Long>) dataMap.get("ponIndex");
        this.uniIndexList = (List<Long>) dataMap.get("uniIndex");
        this.isNecessary = true;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }
    
    /**
     * @return the onuEorG
     */
    public String getOnuEorG() {
        return onuEorG;
    }

    /**
     * @param onuEorG the onuEorG to set
     */
    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
    }
}
