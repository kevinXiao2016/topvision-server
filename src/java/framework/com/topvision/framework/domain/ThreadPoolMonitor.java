/***********************************************************************
 * $Id: ThreadPoolMonitor.java,v1.0 Sep 20, 2016 9:14:32 AM $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

/**
 * @author Victorli
 * @created @Sep 20, 2016-9:14:32 AM
 *
 */
public class ThreadPoolMonitor implements java.io.Serializable {
    private static final long serialVersionUID = 8814218650446502045L;
    // engine id
    private Integer engineId;
    // 采集时间
    private Long collectTime;
    // 名称
    private String name;
    private Integer poolSize;
    private Integer activeCount;
    private Integer corePoolSize;
    private Integer queueSize;
    private Integer largestPoolSize;
    private Long taskCount;
    private Integer maximumPoolSize;
    private Long completedTaskCount;

    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getLargestPoolSize() {
        return largestPoolSize;
    }

    public void setLargestPoolSize(Integer largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(Long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ThreadPoolMonitor [engineId=");
        builder.append(engineId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", poolSize=");
        builder.append(poolSize);
        builder.append(", activeCount=");
        builder.append(activeCount);
        builder.append(", corePoolSize=");
        builder.append(corePoolSize);
        builder.append(", queueSize=");
        builder.append(queueSize);
        builder.append(", largestPoolSize=");
        builder.append(largestPoolSize);
        builder.append(", taskCount=");
        builder.append(taskCount);
        builder.append(", maximumPoolSize=");
        builder.append(maximumPoolSize);
        builder.append(", completedTaskCount=");
        builder.append(completedTaskCount);
        builder.append("]");
        return builder.toString();
    }
}
