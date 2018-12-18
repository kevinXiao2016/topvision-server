/***********************************************************************
 * $Id: TopOltPppoeStatisticsObjects.java,v1.0 2017年11月16日 下午4:10:48 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2017年11月16日-下午4:10:48
 *
 */
public class TopOltPppoeStatisticsObjects implements AliasesSuperType {
    private static final long serialVersionUID = 2320879638305308138L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.9.1.0")
    private Long topOltPppoeStatReceive;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.9.2.0")
    private Long topOltPppoeStatDrop;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.9.3.0")
    private Long topOltPppoeStatTransmit;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.9.4.0", writable = true, type = "Integer32")
    private Integer topOltPppoeStatStatusAndAction;// clean(2)

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTopOltPppoeStatReceive() {
        return topOltPppoeStatReceive;
    }

    public void setTopOltPppoeStatReceive(Long topOltPppoeStatReceive) {
        this.topOltPppoeStatReceive = topOltPppoeStatReceive;
    }

    public Long getTopOltPppoeStatDrop() {
        return topOltPppoeStatDrop;
    }

    public void setTopOltPppoeStatDrop(Long topOltPppoeStatDrop) {
        this.topOltPppoeStatDrop = topOltPppoeStatDrop;
    }

    public Long getTopOltPppoeStatTransmit() {
        return topOltPppoeStatTransmit;
    }

    public void setTopOltPppoeStatTransmit(Long topOltPppoeStatTransmit) {
        this.topOltPppoeStatTransmit = topOltPppoeStatTransmit;
    }

    public Integer getTopOltPppoeStatStatusAndAction() {
        return topOltPppoeStatStatusAndAction;
    }

    public void setTopOltPppoeStatStatusAndAction(Integer topOltPppoeStatStatusAndAction) {
        this.topOltPppoeStatStatusAndAction = topOltPppoeStatStatusAndAction;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltPppoeStatisticsObjects [entityId=");
        builder.append(entityId);
        builder.append(", topOltPppoeStatReceive=");
        builder.append(topOltPppoeStatReceive);
        builder.append(", topOltPppoeStatDrop=");
        builder.append(topOltPppoeStatDrop);
        builder.append(", topOltPppoeStatTransmit=");
        builder.append(topOltPppoeStatTransmit);
        builder.append(", topOltPppoeStatStatusAndAction=");
        builder.append(topOltPppoeStatStatusAndAction);
        builder.append("]");
        return builder.toString();
    }

}
