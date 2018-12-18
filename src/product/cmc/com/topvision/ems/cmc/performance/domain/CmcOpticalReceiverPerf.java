/***********************************************************************
 * $Id: CmcOpticalReceiverPerf.java,v1.0 2013-12-16 上午11:59:54 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author dosion
 * @created @2013-12-16-上午11:59:54
 * 
 */
@Scope("prototype")
@Service("cmcOpticalReceiverPerf")
public class CmcOpticalReceiverPerf extends OperClass {
    private static final long serialVersionUID = -2580163783691003163L;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmcType;
    private Boolean isNecessary = false;

    public CmcOpticalReceiverPerf() {
        super("cmcOpticalReceiverPerfSaver", "cmcOpticalReceiverPerfScheduler", "cmcOpticalReceiverPerf");
    }

    @Override
    public boolean isTaskCancle() {
        return !isNecessary;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        isNecessary = false;
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
        @SuppressWarnings("unchecked")
        List<Long> indexList = (List<Long>) data;
        if (indexList != null && indexList.size() > 0) {
            cmcIndex = indexList.get(0);
        }
        isNecessary = true;
    }

    @Override
    public long getIdentifyKey() {
        return cmcId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        cmcId = identifyKey;
    }

    @Override
    public String[] makeOids() {
        return null;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getCmcType() {
        return cmcType;
    }

    public void setCmcType(Long cmcType) {
        this.cmcType = cmcType;
    }

    public Boolean getIsNecessary() {
        return isNecessary;
    }

    public void setIsNecessary(Boolean isNecessary) {
        this.isNecessary = isNecessary;
    }
}
