/***********************************************************************
 * $Id: IgmpSnpUplinkPort.java,v1.0 2016-6-7 上午14:35:07 $
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
 * 
 * @author flack
 * @created @2016-6-7-下午2:45:12
 * Snooping模式上行端口配置对象
 */
public class IgmpSnpUplinkPort implements AliasesSuperType {
    private static final long serialVersionUID = -2885448163814110344L;

    private Long entityId;
    //网管端口INDEX,网管使用端口INDEX进行与业务处理
    private Long portIndex;
    //Snooping模式上行端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.1.1.0", writable = true, type = "Integer32")
    private Integer portType;
    //Snooping模式上行端口槽位号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.1.2.0", writable = true, type = "Integer32")
    private Integer slotNo;
    //Snooping模式上行端口ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.1.3.0", writable = true, type = "Integer32")
    private Integer portNo;
    //Snooping模式上行端口聚合组ID
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.3.1.4.0", writable = true, type = "Integer32")
    private Integer uplinkAggId;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpSnpUplinkPort [entityId=");
        builder.append(entityId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", uplinkAggId=");
        builder.append(uplinkAggId);
        builder.append("]");
        return builder.toString();
    }

}
