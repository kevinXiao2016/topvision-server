/***********************************************************************
 * $Id: PonProtectConfig.java,v1.0 2012-11-1 上午10:45:26 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.util.Date;

/**
 * @author Bravin
 * @created @2012-11-1-上午10:45:26
 * 
 */
public class PonProtectConfig {
    private long entityId;
    /* 自定义，只存DB */
    private String alias;
    /* 组编号 */
    private int groupIndex;
    /* 组状态 */
    private boolean groupStatus;
    /* 主端口 */
    private long workPort;
    /* 备端口 */
    private long standbyPort;
    /* 激活端口，true:主端口 false:备端口 */
    private boolean activePort;
    /* 倒换次数 */
    private int switchCount;
    /* 上次倒换时间 */
    private Date lastSwitchTime;
    /* 倒换原因 */
    private String switchReason;

    /**
     * @return the entityId
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the groupIndex
     */
    public int getGroupIndex() {
        return groupIndex;
    }

    /**
     * @param groupIndex
     *            the groupIndex to set
     */
    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    /**
     * @return the workPort
     */
    public long getWorkPort() {
        return workPort;
    }

    /**
     * @param workPort
     *            the workPort to set
     */
    public void setWorkPort(long workPort) {
        this.workPort = workPort;
    }

    /**
     * @return the standbyPort
     */
    public long getStandbyPort() {
        return standbyPort;
    }

    /**
     * @param standbyPort
     *            the standbyPort to set
     */
    public void setStandbyPort(long standbyPort) {
        this.standbyPort = standbyPort;
    }

    /**
     * @return the activePort
     */
    public boolean isActivePort() {
        return activePort;
    }

    /**
     * @param activePort
     *            the activePort to set
     */
    public void setActivePort(boolean activePort) {
        this.activePort = activePort;
    }

    /**
     * @return the switchCount
     */
    public int getSwitchCount() {
        return switchCount;
    }

    /**
     * @param switchCount
     *            the switchCount to set
     */
    public void setSwitchCount(int switchCount) {
        this.switchCount = switchCount;
    }

    /**
     * @return the lastSwitchTime
     */
    public Date getLastSwitchTime() {
        return lastSwitchTime;
    }

    /**
     * @param lastSwitchTime
     *            the lastSwitchTime to set
     */
    public void setLastSwitchTime(Date lastSwitchTime) {
        this.lastSwitchTime = lastSwitchTime;
    }

    /**
     * @return the switchReason
     */
    public String getSwitchReason() {
        return switchReason;
    }

    /**
     * @param switchReason
     *            the switchReason to set
     */
    public void setSwitchReason(String switchReason) {
        this.switchReason = switchReason;
    }

    /**
     * @return the groupStatus
     */
    public boolean isGroupStatus() {
        return groupStatus;
    }

    /**
     * @param groupStatus
     *            the groupStatus to set
     */
    public void setGroupStatus(boolean groupStatus) {
        this.groupStatus = groupStatus;
    }

}
