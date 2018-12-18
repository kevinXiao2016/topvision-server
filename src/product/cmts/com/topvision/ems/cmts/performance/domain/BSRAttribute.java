/***********************************************************************
 * $Id: BSRAttribute.java,v1.0 2014-3-24 上午9:53:14 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author YangYi
 * @created @2014-3-24-上午9:53:14
 * 
 */
public class BSRAttribute extends CpuAndMemAttribute {
    private static final long serialVersionUID = 758690767245699168L;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4981.1.20.1.1.1.8.1.1")
    private Long cpuUtilization;
    @SnmpProperty(oid = "1.3.6.1.4.1.4981.1.20.1.1.1.5.1.1")
    private Long memAllocated;
    @SnmpProperty(oid = "1.3.6.1.4.1.4981.1.20.1.1.1.4.1.1")
    private Long allocatableMem;

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

}
