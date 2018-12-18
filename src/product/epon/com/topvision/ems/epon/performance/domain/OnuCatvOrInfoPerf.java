/***********************************************************************
 * $Id: OnuCatvOrInfoPerf.java,v1.0 2016-5-9 下午2:19:37 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author Rod John
 * @created @2016-5-9-下午2:19:37
 *
 */
@Scope("prototype")
@Service("onuCatvPerf")
public class OnuCatvOrInfoPerf extends OperClass {
    private static final long serialVersionUID = -3450406180318363807L;
    private Long onuId;
    private Long onuIndex;
    private Boolean isNecessary = false;

    /**
     * @param perfService
     * @param scheduler
     * @param category
     */
    public OnuCatvOrInfoPerf() {
        super("onuCatvOrInfoSaver", "onuCatvOrInfoScheduler", "onuCatvPerf");
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.domain.OperClass#isTaskCancle()
     */
    @Override
    public boolean isTaskCancle() {
        return !isNecessary;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.domain.OperClass#shutdownTarget(java.lang.String, java.lang.Object)
     */
    @Override
    public void shutdownTarget(String targetName, Object data) {
        this.isNecessary = false;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.domain.OperClass#startUpTarget(java.lang.String, java.lang.Object)
     */
    @Override
    public void startUpTarget(String targetName, Object data) {
        OnuInfo onuInfo = (OnuInfo) data;
        this.entityId = onuInfo.getEntityId();
        this.onuIndex = onuInfo.getOnuIndex();
        this.isNecessary = true;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.domain.OperClass#getIdentifyKey()
     */
    @Override
    public long getIdentifyKey() {
        return this.onuId;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.domain.OperClass#setIdentifyKey(java.lang.Long)
     */
    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.onuId = identifyKey;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.domain.OperClass#makeOids()
     */
    @Override
    public String[] makeOids() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.domain.OperClass#makeObjects()
     */
    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    /**
     * @return the isNecessary
     */
    public Boolean getIsNecessary() {
        return isNecessary;
    }

    /**
     * @param isNecessary the isNecessary to set
     */
    public void setIsNecessary(Boolean isNecessary) {
        this.isNecessary = isNecessary;
    }

}
