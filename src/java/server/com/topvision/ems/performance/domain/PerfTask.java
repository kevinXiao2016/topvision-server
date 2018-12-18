/***********************************************************************
 * $Id: PerfTask.java,v1.0 2013-8-5 上午10:03:19 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.ems.facade.domain.OperClass;

/**
 * @author Rod John
 * @created @2013-8-5-上午10:03:19
 * 
 * 
 *          采集对象标示，用来标示一个采集任务
 */
public class PerfTask {

    // 采集对象标示
    private Long entityId;
    // 采集指标组
    private String groupName;
    // 采集间隔
    private Integer interval;
    // 采集任务
    private OperClass operClass;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName
     *            the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the interval
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * @param interval
     *            the interval to set
     */
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    /**
     * @return the operClass
     */
    public OperClass getOperClass() {
        return operClass;
    }

    /**
     * @param operClass
     *            the operClass to set
     */
    public void setOperClass(OperClass operClass) {
        this.operClass = operClass;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfTask [entityId=");
        builder.append(entityId);
        builder.append(", groupName=");
        builder.append(groupName);
        builder.append(", interval=");
        builder.append(interval);
        builder.append("]");
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        try {
            PerfTask task = (PerfTask) obj;
            if ((task.getEntityId().equals(this.entityId)) && (task.getGroupName().equals(this.groupName))
                    && (task.getInterval().equals(this.interval))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 构造方法
     * 
     * @param entityId
     * @param groupName
     * @param interval
     * @param operClass
     */
    public PerfTask(Long entityId, String groupName, Integer interval, OperClass operClass) {
        this.entityId = entityId;
        this.groupName = groupName;
        this.interval = interval;
        this.operClass = operClass;
    }

}
