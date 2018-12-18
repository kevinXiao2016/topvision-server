/***********************************************************************
 * $Id: DevicePerfTaskUpdateInfo.java,v1.0 2014-3-18 上午11:45:45 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-3-18-上午11:45:45
 *
 */
public class DevicePerfTaskUpdateInfo implements AliasesSuperType {
    private static final long serialVersionUID = 2556653409785587299L;

    private String category;
    private String targetName;
    //entityId
    private Long identifyKey;
    private Integer lastInterval;
    private Integer newInterval;
    private Integer newEnable;
    private boolean changeStatus;
    //indexsList
    private Object data;

    public DevicePerfTaskUpdateInfo() {
        super();
    }

    public DevicePerfTaskUpdateInfo(String category, String targetName, Long identifyKey, Integer lastInterval,
            Integer newInterval, Integer newEnable, boolean changeStatus, Object data) {
        super();
        this.category = category;
        this.targetName = targetName;
        this.identifyKey = identifyKey;
        this.lastInterval = lastInterval;
        this.newInterval = newInterval;
        this.newEnable = newEnable;
        this.changeStatus = changeStatus;
        this.data = data;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getIdentifyKey() {
        return identifyKey;
    }

    public void setIdentifyKey(Long identifyKey) {
        this.identifyKey = identifyKey;
    }

    public Integer getLastInterval() {
        return lastInterval;
    }

    public void setLastInterval(Integer lastInterval) {
        this.lastInterval = lastInterval;
    }

    public Integer getNewInterval() {
        return newInterval;
    }

    public void setNewInterval(Integer newInterval) {
        this.newInterval = newInterval;
    }

    public Integer getNewEnable() {
        return newEnable;
    }

    public void setNewEnable(Integer newEnable) {
        this.newEnable = newEnable;
    }

    public boolean getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(boolean changeStatus) {
        this.changeStatus = changeStatus;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DevicePerfTaskUpdateInfo [category=");
        builder.append(category);
        builder.append(", targetName=");
        builder.append(targetName);
        builder.append(", identifyKey=");
        builder.append(identifyKey);
        builder.append(", lastInterval=");
        builder.append(lastInterval);
        builder.append(", newInterval=");
        builder.append(newInterval);
        builder.append(", newEnable=");
        builder.append(newEnable);
        builder.append(", changeStatus=");
        builder.append(changeStatus);
        builder.append(", data=");
        builder.append(data);
        builder.append("]");
        return builder.toString();
    }

}
