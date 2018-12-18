/***********************************************************************
 * $Id: TopOltDhcpVLANCfg.java,v1.0 2017年11月16日 下午4:07:58 $
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
 * @created @2017年11月16日-下午4:07:58
 *
 */
public class TopOltDhcpVLANCfg implements AliasesSuperType {
    private static final long serialVersionUID = 4096284760078335571L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.2.1.1", index = true)
    private Integer topOltDhcpVLANIndex;// 1-1024
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.2.1.2", writable = true, type = "Integer32")
    private Integer topOltDhcpVLANMode;// snooping(1) relay(2)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.2.1.3", writable = true, type = "Integer32")
    private Integer topOltDhcpVLANRelayMode;// standard(1) option60(2)

    private Integer option60Count;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopOltDhcpVLANIndex() {
        return topOltDhcpVLANIndex;
    }

    public void setTopOltDhcpVLANIndex(Integer topOltDhcpVLANIndex) {
        this.topOltDhcpVLANIndex = topOltDhcpVLANIndex;
    }

    public Integer getTopOltDhcpVLANMode() {
        return topOltDhcpVLANMode;
    }

    public void setTopOltDhcpVLANMode(Integer topOltDhcpVLANMode) {
        this.topOltDhcpVLANMode = topOltDhcpVLANMode;
    }

    public Integer getTopOltDhcpVLANRelayMode() {
        return topOltDhcpVLANRelayMode;
    }

    public void setTopOltDhcpVLANRelayMode(Integer topOltDhcpVLANRelayMode) {
        this.topOltDhcpVLANRelayMode = topOltDhcpVLANRelayMode;
    }

    public Integer getOption60Count() {
        return option60Count;
    }

    public void setOption60Count(Integer option60Count) {
        this.option60Count = option60Count;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltDhcpVLANCfg [entityId=");
        builder.append(entityId);
        builder.append(", topOltDhcpVLANIndex=");
        builder.append(topOltDhcpVLANIndex);
        builder.append(", topOltDhcpVLANMode=");
        builder.append(topOltDhcpVLANMode);
        builder.append(", topOltDhcpVLANRelayMode=");
        builder.append(topOltDhcpVLANRelayMode);
        builder.append(", option60Count=");
        builder.append(option60Count);
        builder.append("]");
        return builder.toString();
    }

}
