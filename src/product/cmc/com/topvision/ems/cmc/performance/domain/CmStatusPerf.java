/***********************************************************************
 * $Id: CmStatusPerf.java,v1.0 2012-7-17 上午10:33:26 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author loyal
 * @created @2012-7-17-上午10:33:26
 * 
 */
public class CmStatusPerf extends OperClass {
    private static final long serialVersionUID = -7183256135929742669L;
    private Long cmcId;
    private Long cmId;
    private List<Long> cmIndex;

    public CmStatusPerf() {
        super("cmStatusDBSaver", "cmStatusScheduler", "CC_CMSTATUS");
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
     * @return the cmIndex
     */
    public List<Long> getCmIndex() {
        return cmIndex;
    }

    /**
     * @param cmIndex
     *            the cmIndex to set
     */
    public void setCmIndex(List<Long> cmIndex) {
        this.cmIndex = cmIndex;
    }

    /**
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }

    /**
     * @param cmId
     *            the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmStatusPerf [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append(", cmIndex=");
        builder.append(cmIndex);
        builder.append(", perfService=");
        builder.append(perfService);
        builder.append(", scheduler=");
        builder.append(scheduler);
        builder.append(", category=");
        builder.append(category);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

    @Override
    public boolean isTaskCancle() {
        return false;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
    }
}
