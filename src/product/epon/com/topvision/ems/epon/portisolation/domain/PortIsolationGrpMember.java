/***********************************************************************
 * $Id: PortIsolationGrpMember.java,v1.0 2014-12-18 上午11:44:26 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2014-12-18-上午11:44:26
 *
 */
public class PortIsolationGrpMember implements AliasesSuperType {
    private static final long serialVersionUID = -7542331335477535372L;

    private Long entityId;
    private Long portIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.16.2.1.1", index = true)
    private Long slotId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.16.2.1.2", index = true)
    private Long portId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.16.2.1.3", index = true)
    private Integer groupIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.16.2.1.4", writable = true, type = "Integer32")
    private Integer memberRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPortIndex() {
        if (portIndex == null) {
            portIndex = new EponIndex(slotId.intValue(), portId.intValue()).getPonIndex();
        }
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        this.slotId = EponIndex.getSlotNo(portIndex);
        this.portId = EponIndex.getPonNo(portIndex);
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

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public Integer getMemberRowStatus() {
        return memberRowStatus;
    }

    public void setMemberRowStatus(Integer memberRowStatus) {
        this.memberRowStatus = memberRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortIsolationGrpMember [entityId=");
        builder.append(entityId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", slotId=");
        builder.append(slotId);
        builder.append(", portId=");
        builder.append(portId);
        builder.append(", groupIndex=");
        builder.append(groupIndex);
        builder.append(", memberRowStatus=");
        builder.append(memberRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
