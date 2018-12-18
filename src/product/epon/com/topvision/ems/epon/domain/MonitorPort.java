/***********************************************************************
 * $Id: MonitorPort.java,v1.0 2012-5-27 下午05:39:51 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2012-5-27-下午05:39:51
 * 
 */
public class MonitorPort implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7625475590965979515L;
    private Long monitorId;
    private Long entityId;
    private String entityIp;
    private Long portId;
    private Long portIndex;
    private String deviceType;
    private String entityName;
    private String portType;

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName
     *            the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

}
