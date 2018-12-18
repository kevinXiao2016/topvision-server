/***********************************************************************
 * $Id: IgmpSnpStaticFwd.java,v1.0 2016-6-7 下午2:54:22 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.IpsAddress;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016-6-7-下午2:54:22
 * Snooping模式静态转发组端口配置
 */
public class IgmpSnpStaticFwd implements AliasesSuperType {
    private static final long serialVersionUID = 4284905994840079320L;

    private Long entityId;
    private Long portIndex;
    private String portName;
    //Snooping模式静态转发组端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.2.1.1", index = true)
    private Integer portType;
    //Snooping模式静态转发组端口槽位
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.2.1.2", index = true)
    private Integer slotNo;
    //Snooping模式静态转发组端口ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.2.1.3", index = true)
    private Integer portNo;
    //Snooping模式静态转发组组IP地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.2.1.4", index = true)
    private IpsAddress groupIpIndex;
    private String groupIp;
    //Snooping模式静态转发组Vlan
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.2.1.5", index = true)
    private Integer groupVlan;
    //Snooping模式静态转发组源IP地址
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.2.1.6", index = true)
    private IpsAddress srcIpIndex;
    private String groupSrcIp;
    //行创建状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.2.1.7", writable = true, type = "Integer32")
    private Integer rowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getSlotNo() {
    	if(slotNo == null && portIndex != null){
    		slotNo = EponIndex.getSlotNo(portIndex).intValue();
    	}
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPortNo() {
    	if(portNo == null && portIndex != null){
    		portNo = EponIndex.getPonNo(portIndex).intValue();
    	}
        return portNo;
    }

    public void setPortNo(Integer portNo) {
        this.portNo = portNo;
    }

    public String getGroupIp() {
        if (groupIp == null && groupIpIndex != null) {
            this.groupIp = groupIpIndex.toString();
        }
        return groupIp;
    }

    public void setGroupIp(String groupIp) {
        this.groupIp = groupIp;
    }

    public Integer getGroupVlan() {
        return groupVlan;
    }

    public void setGroupVlan(Integer groupVlan) {
        this.groupVlan = groupVlan;
    }

    public String getGroupSrcIp() {
        if (groupSrcIp == null && srcIpIndex != null) {
            this.groupSrcIp = srcIpIndex.toString();
        }
        return groupSrcIp;
    }

    public void setGroupSrcIp(String groupSrcIp) {
        this.groupSrcIp = groupSrcIp;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Long getPortIndex() {
        if (portIndex == null) {
            portIndex = EponIndex.getSniIndex(slotNo, portNo);
        }
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public IpsAddress getGroupIpIndex() {
        if (groupIpIndex == null && groupIp != null) {
            this.groupIpIndex = new IpsAddress(groupIp);
        }
        return groupIpIndex;
    }

    public void setGroupIpIndex(IpsAddress groupIpIndex) {
        this.groupIpIndex = groupIpIndex;
    }

    public IpsAddress getSrcIpIndex() {
        if (srcIpIndex == null && groupSrcIp != null) {
            this.srcIpIndex = new IpsAddress(groupSrcIp);
        }
        return srcIpIndex;
    }

    public void setSrcIpIndex(IpsAddress srcIpIndex) {
        this.srcIpIndex = srcIpIndex;
    }

    public String getPortName() {
        if (portName == null && portIndex != null) {
            portName = EponIndex.getPortStringByIndex(portIndex).toString();
        }
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpSnpStaticFwd [entityId=");
        builder.append(entityId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", groupIp=");
        builder.append(groupIp);
        builder.append(", groupVlan=");
        builder.append(groupVlan);
        builder.append(", groupSrcIp=");
        builder.append(groupSrcIp);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append("]");
        return builder.toString();
    }

}
