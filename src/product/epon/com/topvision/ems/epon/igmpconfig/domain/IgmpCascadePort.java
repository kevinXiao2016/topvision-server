/***********************************************************************
 * $Id: IgmpCascadePort.java,v1.0 2016-6-7 上午11:53:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016-6-7-上午11:53:08
 * IGMP级联端口配置
 */
public class IgmpCascadePort implements AliasesSuperType {
    private static final long serialVersionUID = 1874802169813988026L;

    private Long entityId;
    //网管端口INDEX,网管使用端口INDEX进行与业务处理
    private Long portIndex;
    private String portName;
    //级联端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.1.1", index = true)
    private Integer portType;
    //级联端口槽位号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.1.2", index = true)
    private Integer slotNo;
    //级联端口号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.1.3", index = true)
    private Integer portNo;
    //行创建状态
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.2.1.4", writable = true, type = "Integer32")
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
        if (slotNo == null && portIndex != null) {
            slotNo = EponIndex.getSlotNo(portIndex).intValue();
        }
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPortNo() {
        if (portNo == null && portIndex != null) {
            portNo = EponIndex.getSniNo(portIndex).intValue();
        }
        return portNo;
    }

    public void setPortNo(Integer portNo) {
        this.portNo = portNo;
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

    public String getPortName() {
        if (portName == null && portIndex != null) {
            this.portName = EponIndex.getPortStringByIndex(portIndex).toString();
        }
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpCascadePort [entityId=");
        builder.append(entityId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", portNo=");
        builder.append(portNo);
        builder.append(", rowStatus=");
        builder.append(rowStatus);
        builder.append("]");
        return builder.toString();
    }

}
