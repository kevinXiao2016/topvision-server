/***********************************************************************
 * $Id: CmStatusPerf.java,v1.0 2012-7-17 上午10:33:26 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author jay
 * @created @2012-7-17-上午10:33:26
 * 
 */
@Service("cpeStatusPerf")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CpeStatusPerf extends OperClass {
    private static final long serialVersionUID = -7183256135929742669L;

    public CpeStatusPerf() {
        super("cpeStatusPerfDBSaver", "cpeStatusScheduler", "CPE_CMSTATUS");
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CpeStatusPerf [entityId=");
        builder.append(entityId);
        builder.append(", perfService=");
        builder.append(perfService);
        builder.append(", scheduler=");
        builder.append(scheduler);
        builder.append(", category=");
        builder.append(category);
        builder.append("]");
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