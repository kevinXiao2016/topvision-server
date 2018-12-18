/***********************************************************************
 * $Id: TopOltDhcpPortAttribute.java,v1.0 2017年11月16日 下午4:09:20 $
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
 * @created @2017年11月16日-下午4:09:20
 *
 */
public class TopOltDhcpPortAttribute implements AliasesSuperType {
    private static final long serialVersionUID = -3142573996199366209L;
    private static final String[] PORT_TYPE = { "UNKOWN", "TRUNK", "FE", "GE", "XE" };
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.5.1.1", index = true)
    private Integer topOltDhcpPortProtIndex;// dhcp(1) pppoe(2)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.5.1.2", index = true)
    private Integer topOltDhcpPortTypeIndex;// trunk(1) fe(2) ge(3) xe(4)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.5.1.3", index = true)
    private Integer topOltDhcpSlotIndex;// 0-255
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.5.1.4", index = true)
    private Integer topOltDhcpPortIndex;// 0-255
    private String portName;
    private String portTypeName;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.5.1.5", writable = true, type = "Integer32")
    private Integer topOltDhcpPortCascade;// true(1) false(2)
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.5.1.6", writable = true, type = "Integer32")
    private Integer topOltDhcpPortTrans;// transparent(1) captured(2)

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.5.1.7", writable = true, type = "Integer32")
    private Integer topOltDhcpPortTrust;// true(1) false(2)

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopOltDhcpPortProtIndex() {
        return topOltDhcpPortProtIndex;
    }

    public void setTopOltDhcpPortProtIndex(Integer topOltDhcpPortProtIndex) {
        this.topOltDhcpPortProtIndex = topOltDhcpPortProtIndex;
    }

    public Integer getTopOltDhcpPortTypeIndex() {
        return topOltDhcpPortTypeIndex;
    }

    public void setTopOltDhcpPortTypeIndex(Integer topOltDhcpPortTypeIndex) {
        this.topOltDhcpPortTypeIndex = topOltDhcpPortTypeIndex;
    }

    public Integer getTopOltDhcpSlotIndex() {
        return topOltDhcpSlotIndex;
    }

    public void setTopOltDhcpSlotIndex(Integer topOltDhcpSlotIndex) {
        this.topOltDhcpSlotIndex = topOltDhcpSlotIndex;
    }

    public Integer getTopOltDhcpPortIndex() {
        return topOltDhcpPortIndex;
    }

    public void setTopOltDhcpPortIndex(Integer topOltDhcpPortIndex) {
        this.topOltDhcpPortIndex = topOltDhcpPortIndex;
    }

    public Integer getTopOltDhcpPortCascade() {
        return topOltDhcpPortCascade;
    }

    public void setTopOltDhcpPortCascade(Integer topOltDhcpPortCascade) {
        this.topOltDhcpPortCascade = topOltDhcpPortCascade;
    }

    public Integer getTopOltDhcpPortTrans() {
        return topOltDhcpPortTrans;
    }

    public void setTopOltDhcpPortTrans(Integer topOltDhcpPortTrans) {
        this.topOltDhcpPortTrans = topOltDhcpPortTrans;
    }

    public Integer getTopOltDhcpPortTrust() {
        return topOltDhcpPortTrust;
    }

    public void setTopOltDhcpPortTrust(Integer topOltDhcpPortTrust) {
        this.topOltDhcpPortTrust = topOltDhcpPortTrust;
    }

    public String getPortName() {
        if (this.topOltDhcpPortTypeIndex != 1) {
            portName = PORT_TYPE[this.topOltDhcpPortTypeIndex] + this.topOltDhcpSlotIndex + "/"
                    + this.topOltDhcpPortIndex;
        } else {
            portName = this.topOltDhcpPortIndex.toString();
        }
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPortTypeName() {
        if (this.topOltDhcpPortTypeIndex != null) {
            portTypeName = PORT_TYPE[this.topOltDhcpPortTypeIndex];
        }
        return portTypeName;
    }

    public void setPortTypeName(String portTypeName) {
        this.portTypeName = portTypeName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltDhcpPortAttribute [entityId=");
        builder.append(entityId);
        builder.append(", topOltDhcpPortProtIndex=");
        builder.append(topOltDhcpPortProtIndex);
        builder.append(", topOltDhcpPortTypeIndex=");
        builder.append(topOltDhcpPortTypeIndex);
        builder.append(", topOltDhcpSlotIndex=");
        builder.append(topOltDhcpSlotIndex);
        builder.append(", topOltDhcpPortIndex=");
        builder.append(topOltDhcpPortIndex);
        builder.append(", portName=");
        builder.append(portName);
        builder.append(", portTypeName=");
        builder.append(portTypeName);
        builder.append(", topOltDhcpPortCascade=");
        builder.append(topOltDhcpPortCascade);
        builder.append(", topOltDhcpPortTrans=");
        builder.append(topOltDhcpPortTrans);
        builder.append(", topOltDhcpPortTrust=");
        builder.append(topOltDhcpPortTrust);
        builder.append("]");
        return builder.toString();
    }

}
