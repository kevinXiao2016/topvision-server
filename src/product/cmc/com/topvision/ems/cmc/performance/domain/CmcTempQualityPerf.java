/***********************************************************************
 * $Id: CmcTempQualityPerf.java,v1.0 2013-9-5 上午09:29:43 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author Rod John
 * @created @2013-9-5-上午09:29:43
 * 
 */
@Scope("prototype")
@Service("cmcTempQualityPerf")
public class CmcTempQualityPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = -3037550070965816129L;
    private Long cmcId;
    private Long cmcIndex;
    private Boolean isNecessary = false;

    public CmcTempQualityPerf() {
        super("cmcTempQualityPerfSaver", "cmcTempQualityPerfScheduler", "cmcTempQualityPerf");
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
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
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
}
