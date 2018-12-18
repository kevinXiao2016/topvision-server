/***********************************************************************
 * $Id: CmtsSystemPerf.java,v1.0 2012-7-11 下午02:15:40 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.performance.domain.PerfTargetConstants;

/**
 * @author jay
 * @created @2012-7-11-下午02:15:40
 * 
 */
@Scope("prototype")
@Service("cmtsSystemPerf")
public class CmtsSystemPerf extends OperClass {
    private static final long serialVersionUID = -4923315755347924759L;
    private long cmcId;
    private long typeId;
    //内蒙分支,增加CMTS内存和CPU的采集
    private Boolean isCpuPerf = false;
    private Boolean isMemPerf = false;

    public CmtsSystemPerf() {
        super("cmtsSystemPerfDBSaver", "cmtsSystemPerfScheduler", "cmtsSystemPerf");
    }

    @Override
    public long getIdentifyKey() {
        return cmcId;
    }

    @Override
    public String[] makeOids() {
        return null;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the cmcId
     */
    public long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(long cmcId) {
        this.cmcId = cmcId;
    }

    // TODO以下是合并性能分支导致需要修改
    @Override
    public boolean isTaskCancle() {
        return !(isCpuPerf || isMemPerf);
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.CMC_CPUUSED)) {
            this.isCpuPerf = false;
        } else if (targetName.equals(PerfTargetConstants.CMC_MEMUSED)) {
            this.isMemPerf = false;
        }
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.CMC_CPUUSED)) {
            this.isCpuPerf = true;
        } else if (targetName.equals(PerfTargetConstants.CMC_MEMUSED)) {
            this.isMemPerf = true;
        }
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.cmcId = identifyKey;
        this.entityId = identifyKey;
    }

    @Override
    public void setDeviceTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Boolean getIsCpuPerf() {
        return isCpuPerf;
    }

    public void setIsCpuPerf(Boolean isCpuPerf) {
        this.isCpuPerf = isCpuPerf;
    }

    public Boolean getIsMemPerf() {
        return isMemPerf;
    }

    public void setIsMemPerf(Boolean isMemPerf) {
        this.isMemPerf = isMemPerf;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmtsSystemPerf [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", perfService=");
        builder.append(perfService);
        builder.append(", scheduler=");
        builder.append(scheduler);
        builder.append(", category=");
        builder.append(category);
        builder.append(", isCpuPerf=");
        builder.append(isCpuPerf);
        builder.append(", isMemPerf=");
        builder.append(isMemPerf);
        builder.append("]");
        return builder.toString();
    }
}