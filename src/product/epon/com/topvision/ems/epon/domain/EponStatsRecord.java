/***********************************************************************
 * $ PerfRecord.java,v1.0 2012-6-11 8:54:44 $
 *
 * @author: yq
 *
 * (c)Copyright 2012 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author yq
 * @created @2012-6-7-8:54:44
 */
public class EponStatsRecord implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = -5450225254023515137L;
    private Long entityId;
    private Long portIndex;
    private Integer portType;
    private Integer collector;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getCollector() {
        return collector;
    }

    public void setCollector(Integer collector) {
        this.collector = collector;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EponStatsRecord [entityId=");
        builder.append(entityId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", collector=");
        builder.append(collector);
        builder.append("]");
        return builder.toString();
    }

}
