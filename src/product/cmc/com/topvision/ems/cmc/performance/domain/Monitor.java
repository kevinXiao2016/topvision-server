/***********************************************************************
 * $ Monitor.java,v1.0 2013-6-28 1:07:47 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.sql.Timestamp;

/**
 * @author jay
 * @created @2013-6-28-1:07:47
 */
public class Monitor {
    private Long monitorId;
    private Timestamp lastCollectTime;

    public Timestamp getLastCollectTime() {
        return lastCollectTime;
    }

    public void setLastCollectTime(Timestamp lastCollectTime) {
        this.lastCollectTime = lastCollectTime;
    }

    public Long getLastCollectTimeLong() {
        return lastCollectTime != null ? lastCollectTime.getTime() : 0;
    }

    public void setLastCollectTimeLong(Long lastCollectTimeLong) {
        this.lastCollectTime = new Timestamp(lastCollectTimeLong);
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }
}
