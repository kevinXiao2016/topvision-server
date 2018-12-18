/***********************************************************************
 * $Id: EponLinkQualityPerf.java,v1.0 2013-7-19 上午09:19:31 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author Rod John
 * @created @2013-7-19-上午09:19:31
 * 
 */
@Scope("prototype")
@Service("eponLinkQualityPerf")
public class EponLinkQualityPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = -2464978682944661023L;
    private Boolean isNecessary = false;

    public EponLinkQualityPerf() {
        super("eponLinkQualityPerfSaver", "eponLinkQualityPerfScheduler", "eponLinkQualityPerf");
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
        return !isNecessary;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        isNecessary = false;
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
        isNecessary = true;
    }
}
