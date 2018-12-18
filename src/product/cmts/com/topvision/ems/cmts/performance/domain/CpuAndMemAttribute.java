/***********************************************************************
 * $Id: CpuAndMemAttribute.java,v1.0 2014-3-24 上午10:54:24 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.io.Serializable;

/**
 * @author YangYi
 * @created @2014-3-24-上午10:54:24
 * 
 */
public class CpuAndMemAttribute implements Serializable {
    private static final long serialVersionUID = 4815165518026759475L;
    private Long entityId;
    private Long cmcId;
    private Long cpuUtilization;
    private Long memAllocated;
    private Long allocatableMem;
    private Double memUsagePercent;

    public Double getMemUsagePercent() {
        return memUsagePercent;
    }

    public void setMemUsagePercent(Double memUsagePercent) {
        this.memUsagePercent = memUsagePercent;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(Long cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    public Long getMemAllocated() {
        return memAllocated;
    }

    public void setMemAllocated(Long memAllocated) {
        this.memAllocated = memAllocated;
    }

    public Long getAllocatableMem() {
        return allocatableMem;
    }

    public void setAllocatableMem(Long allocatableMem) {
        this.allocatableMem = allocatableMem;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CpuAndMemAttribute [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cpuUtilization=");
        builder.append(cpuUtilization);
        builder.append(", memAllocated=");
        builder.append(memAllocated);
        builder.append(", allocatableMem=");
        builder.append(allocatableMem);
        builder.append(", memUsagePercent=");
        builder.append(memUsagePercent);
        builder.append("]");
        return builder.toString();
    }

}
