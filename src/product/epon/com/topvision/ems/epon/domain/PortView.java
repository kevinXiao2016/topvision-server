/***********************************************************************
 * $ PortView.java,v1.0 2011-9-28 18:10:47 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-9-28-18:10:47
 */
public class PortView implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6263828959566163784L;
    private Long entityId;
    private Long slotId;
    private Long slotIndex;
    private Long portId;
    private Long portIndex;
    private String portType;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PortView");
        sb.append("{entityId=").append(entityId);
        sb.append(", slotId=").append(slotId);
        sb.append(", slotIndex=").append(slotIndex);
        sb.append(", portId=").append(portId);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", portType='").append(portType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
