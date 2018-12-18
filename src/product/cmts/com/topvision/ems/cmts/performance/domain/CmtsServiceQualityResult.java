/***********************************************************************
 * $Id: CmtsServiceQualityResult.java,v1.0 2015-5-28 下午2:44:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2015-5-28-下午2:44:09
 *
 */
public class CmtsServiceQualityResult implements AliasesSuperType {
    private static final long serialVersionUID = -6207760226368996509L;

    private Long entityId;
    private Double cpuUtilization;
    private Double memUtilization;
    private Timestamp collectTime;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Double getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(Double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    public Double getMemUtilization() {
        return memUtilization;
    }

    public void setMemUtilization(Double memUtilization) {
        this.memUtilization = memUtilization;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmtsServiceQualityResult [entityId=");
        builder.append(entityId);
        builder.append(", cpuUtilization=");
        builder.append(cpuUtilization);
        builder.append(", memUtilization=");
        builder.append(memUtilization);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
