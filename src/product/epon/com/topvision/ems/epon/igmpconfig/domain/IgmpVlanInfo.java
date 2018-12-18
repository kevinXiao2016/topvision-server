/***********************************************************************
 * $Id: IgmpVlanInfo.java,v1.0 2016-6-7 下午3:12:43 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016-6-7-下午3:12:43
 * 组播VLAN配置
 */
public class IgmpVlanInfo implements AliasesSuperType {
    private static final long serialVersionUID = 8225546213721174768L;

    private Long entityId;
    private Long portIndex;
    private String portName;
    //组播vlanID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.1.1.1", index = true)
    private Integer vlanId;
    //组播vlan上行端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.1.1.2", writable = true, type = "Integer32")
    private Integer portType;
    //组播vlan上行端口槽位号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.1.1.3", writable = true, type = "Integer32")
    private Integer slotNo;
    //组播vlan上行端口ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.1.1.4", writable = true, type = "Integer32")
    private Integer portNo;
    //组播vlan上行端口聚合组ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.1.1.5", writable = true, type = "Integer32")
    private Integer uplinkAggId;
    //组播vlan所属VRF name
    //@SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.1.1.6", writable = true, type = "OctetString")
    private String vlanVrfName;
    //行创建状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.4.1.1.7", writable = true, type = "Integer32")
    private Integer rowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPortIndex() {
        if (portType.equals(IgmpConstants.IGMP_PORTTYPE_GE) || portType.equals(IgmpConstants.IGMP_PORTTYPE_XE)) {
    		if(portIndex == null){
    			portIndex = EponIndex.getSniIndex(slotNo, portNo);
    		}
    	}
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPortNo() {
        return portNo;
    }

    public void setPortNo(Integer portNo) {
        this.portNo = portNo;
    }

    public Integer getUplinkAggId() {
        return uplinkAggId;
    }

    public void setUplinkAggId(Integer uplinkAggId) {
        this.uplinkAggId = uplinkAggId;
    }

    public String getVlanVrfName() {
        return vlanVrfName;
    }

    public void setVlanVrfName(String vlanVrfName) {
        this.vlanVrfName = vlanVrfName;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpVlanInfo [entityId=");
        builder.append(entityId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", vlanId=");
        builder.append(vlanId);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", uplinkAggId=");
        builder.append(uplinkAggId);
        builder.append(", vlanVrfName=");
        builder.append(vlanVrfName);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append("]");
        return builder.toString();
    }

}
