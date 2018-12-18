/***********************************************************************
 * $Id: UBRAttribute.java,v1.0 2015-5-20 下午4:39:51 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author flack
 * @created @2015-5-20-下午4:39:51
 *
 */
public class UBRAttribute extends CpuAndMemAttribute {
    private static final long serialVersionUID = 88691792516555527L;
    
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.9.2.1.57.0")
    private Long cpuUtilization;
    @SnmpProperty(oid = "1.3.6.1.4.1.9.9.48.1.1.1.6.1")
    private Long memFree;
    @SnmpProperty(oid = "1.3.6.1.4.1.9.9.48.1.1.1.5.1")
    private Long memUsed;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(Long cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    public Long getMemFree() {
        return memFree;
    }

    public void setMemFree(Long memFree) {
        this.memFree = memFree;
    }

    public Long getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Long memUsed) {
        this.memUsed = memUsed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UBRAttribute [entityId=");
        builder.append(entityId);
        builder.append(", cpuUtilization=");
        builder.append(cpuUtilization);
        builder.append(", memFree=");
        builder.append(memFree);
        builder.append(", memUsed=");
        builder.append(memUsed);
        builder.append("]");
        return builder.toString();
    }

}
