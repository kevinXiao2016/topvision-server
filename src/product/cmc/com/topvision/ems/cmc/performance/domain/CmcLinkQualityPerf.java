/***********************************************************************
 * $Id: CmcLinkQualityPerf.java,v1.0 2013-8-8 下午08:14:21 $
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
 * @created @2013-8-8-下午08:14:21
 *
 */
@Scope("prototype")
@Service("cmcLinkQualityPerf")
public class CmcLinkQualityPerf extends OperClass implements Serializable {
    private static final long serialVersionUID = 4455706597008481926L;
    private Long cmcId;
    private Long cmcIndex;
    private Long onuIndex;
    private Boolean isNecessary = false;
    private Boolean isCcmtSwithoutAgent = false;
    private Long typeId;

    public CmcLinkQualityPerf() {
        super("cmcLinkQualityPerfSaver", "cmcLinkQualityPerfScheduler", "cmcLinkQualityPerf");
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
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    @Override
    public boolean isTaskCancle() {
        return !isNecessary;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        isNecessary = false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        List<Long> index = (List<Long>) data;
        if (index != null && index.size() == 3) {
            this.onuIndex = index.get(0);
            this.cmcIndex = index.get(1);
            this.isCcmtSwithoutAgent = index.get(2).equals(1L);
        }
        isNecessary = true;
    }

    public Boolean getCcmtSwithoutAgent() {
        return isCcmtSwithoutAgent;
    }

    public void setCcmtSwithoutAgent(Boolean isCcmtSwithoutAgent) {
        this.isCcmtSwithoutAgent = isCcmtSwithoutAgent;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

}
