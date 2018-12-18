/***********************************************************************
 * $ OltSimplePort.java,v1.0 2011-11-21 13:56:39 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-21-13:56:39
 */
public class OltSimplePort implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * 设备ID
     */
    private Long entityId;
    /**
     * 端口ID
     */
    private Long portId;
    /**
     * 接口INDEX 用于生成端口名称
     */
    private Long portIndex;
    /**
     * 端口名称
     */
    private String portName;

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

    public String getPortName() {
        Integer onuNo = EponIndex.getOnuNo(portIndex).intValue();
        if (onuNo != null && onuNo != 0) {
            return new StringBuilder().append(EponIndex.getSlotNo(portIndex)).append("/")
                    .append(EponIndex.getPonNo(portIndex)).append(" ").append(EponIndex.getOnuNo(portIndex))
                    .append(":").append(EponIndex.getUniNo(portIndex)).toString();
        } else {
            return new StringBuilder().append(EponIndex.getSlotNo(portIndex)).append("/")
                    .append(EponIndex.getPonNo(portIndex)).toString();
        }
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltSimplePort");
        sb.append("{entityId=").append(entityId);
        sb.append(", portId=").append(portId);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", portName='").append(portName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
