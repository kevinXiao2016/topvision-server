/***********************************************************************
 * $Id: ScheduleUpdateFuture.java,v1.0 2015-8-5 下午4:08:23 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * @author flack
 * @created @2015-8-5-下午4:08:23
 *
 */
public class ScheduleUpdateFuture implements Serializable {
    private static final long serialVersionUID = -8450939222459532258L;
    
    private Long entityId;
    private String deviceName;
    private String manageIp;
    private Future<?> updateFuture;

    public ScheduleUpdateFuture() {
    }

    public ScheduleUpdateFuture(Long entityId, String deviceName, String manageIp, Future<?> updateFuture) {
        this.entityId = entityId;
        this.deviceName = deviceName;
        this.manageIp = manageIp;
        this.updateFuture = updateFuture;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public Future<?> getUpdateFuture() {
        return updateFuture;
    }

    public void setUpdateFuture(Future<?> updateFuture) {
        this.updateFuture = updateFuture;
    }

}
