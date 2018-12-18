/***********************************************************************
 * $ CmtsSystem.java,v1.0 2013-8-15 15:07:14 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import java.io.Serializable;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author jay
 * @created @2013-8-15-15:07:14
 */
public class CmtsSystemResult extends PerformanceResult<OperClass> implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private SystemAttribute systemAttribute;
    private CpuAndMemAttribute cpuAndMemAttribute;
    private CmtsServiceQualityResult serviceResult;

    public CpuAndMemAttribute getCpuAndMemAttribute() {
        return cpuAndMemAttribute;
    }

    public void setCpuAndMemAttribute(CpuAndMemAttribute cpuAndMemAttribute) {
        this.cpuAndMemAttribute = cpuAndMemAttribute;
    }

    public CmtsSystemResult(OperClass domain) {
        super(domain);
    }

    public SystemAttribute getSystemAttribute() {
        return systemAttribute;
    }

    public void setSystemAttribute(SystemAttribute systemAttribute) {
        this.systemAttribute = systemAttribute;
    }

    public CmtsServiceQualityResult getServiceResult() {
        return serviceResult;
    }

    public void setServiceResult(CmtsServiceQualityResult serviceResult) {
        this.serviceResult = serviceResult;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmtsSystemResult [systemAttribute=");
        builder.append(systemAttribute);
        builder.append(", cpuAndMemAttribute=");
        builder.append(cpuAndMemAttribute);
        builder.append(", serviceResult=");
        builder.append(serviceResult);
        builder.append("]");
        return builder.toString();
    }
}
