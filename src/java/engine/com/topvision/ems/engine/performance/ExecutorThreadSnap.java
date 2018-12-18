/***********************************************************************
 * $Id: ExecutorThreadSnap.java,v1.0 2017年6月15日 下午6:45:54 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author vanzand
 * @created @2017年6月15日-下午6:45:54
 *
 */
public class ExecutorThreadSnap {
    private Integer engineId;
    private Integer activeCount;
    private Integer poolSize;
    private Long completedTaskCount;
    private Timestamp collectTime;

    public ExecutorThreadSnap() {
        super();
    }

    public ExecutorThreadSnap(Integer engineId, Integer activeCount, Integer poolSize, Long completedTaskCount) {
        super();
        this.engineId = engineId;
        this.activeCount = activeCount;
        this.poolSize = poolSize;
        this.completedTaskCount = completedTaskCount;
        this.collectTime = new Timestamp(System.currentTimeMillis());
    }

    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(Long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

}
