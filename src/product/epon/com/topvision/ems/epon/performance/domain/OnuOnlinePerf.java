/***********************************************************************
 * $Id: OnuOnlinePerf.java,v1.0 2015-4-22 上午11:38:49 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author flack
 * @created @2015-4-22-上午11:38:49
 *
 */
@Scope("prototype")
@Service("onuOnlinePerf")
public class OnuOnlinePerf extends OperClass {
    private static final long serialVersionUID = 1762494243367625348L;
    private Long onuId;
    private Long onuIndex;
    private String onuEorG;
    private Boolean isNecessary = false;

    public OnuOnlinePerf() {
        super("onuOnlineSaver", "onuOnlineScheduler", "onuOnlinePerf");
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

    public Boolean getIsNecessary() {
        return isNecessary;
    }

    public void setIsNecessary(Boolean isNecessary) {
        this.isNecessary = isNecessary;
    }

    @Override
    public boolean isTaskCancle() {
        return !isNecessary;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        this.isNecessary = false;
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
        OnuInfo onuInfo = (OnuInfo) data;
        this.entityId = onuInfo.getEntityId();
        this.onuIndex = onuInfo.getOnuIndex();
        this.onuEorG = onuInfo.getOnuEorG();
        this.isNecessary = true;
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
}
