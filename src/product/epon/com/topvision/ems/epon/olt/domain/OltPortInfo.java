/***********************************************************************
 * $Id: OltPortInfo.java,v1.0 2014-11-25 下午2:38:36 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-11-25-下午2:38:36
 * OLT 端口基本属性
 */
public class OltPortInfo implements AliasesSuperType {
    private static final long serialVersionUID = -2963759531905981054L;

    private Long entityId;
    private Long slotId;
    private Long portId;
    private Long portIndex;
    private Integer portStatus;
    private String displayName;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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

    public Integer getPortStatus() {
        return portStatus;
    }

    public void setPortStatus(Integer portStatus) {
        this.portStatus = portStatus;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltPortInfo [entityId=");
        builder.append(entityId);
        builder.append(", slotId=");
        builder.append(slotId);
        builder.append(", portId=");
        builder.append(portId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portStatus=");
        builder.append(portStatus);
        builder.append("]");
        return builder.toString();
    }

}
