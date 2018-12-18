/***********************************************************************
 * $Id: RestartCount.java,v1.0 2013-2-21 下午5:08:08 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-2-21-下午5:08:08
 * 
 */
public class RestartCount implements AliasesSuperType {
    private static final long serialVersionUID = -5164744192792864088L;
    private Long entityId;
    private Long deviceIndex;
    private Integer count;
    private Integer y;
    private String Color;
    private Long maxTime;
    private Long minTime;
    private String ip;
    private String displayName;
    private String entityAlias;
    private Date createTime;
    private String createTimeString;
    private Date lastRestartTime;
    private String lastRestartTimeString;
    private static final Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
     * @return the deviceIndex
     */
    public Long getDeviceIndex() {
        return deviceIndex;
    }

    /**
     * @param deviceIndex
     *            the deviceIndex to set
     */
    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        if (count == null || count < 0) {
            return 0;
        } else {
            return count;
        }
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the y
     */
    public Integer getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return Color;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(String color) {
        Color = color;
    }

    /**
     * @return the maxTime
     */
    public Long getMaxTime() {
        return maxTime;
    }

    /**
     * @param maxTime
     *            the maxTime to set
     */
    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(maxTime);
        setLastRestartTime(c.getTime());
    }

    /**
     * @return the minTime
     */
    public Long getMinTime() {
        return minTime;
    }

    /**
     * @param minTime
     *            the minTime to set
     */
    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the entityAlias
     */
    public String getEntityAlias() {
        return entityAlias;
    }

    /**
     * @param entityAlias the entityAlias to set
     */
    public void setEntityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        this.createTimeString = format.format(createTime);
    }

    /**
     * @return the createTimeString
     */
    public String getCreateTimeString() {
        return createTimeString;
    }

    /**
     * @param createTimeString the createTimeString to set
     */
    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    /**
     * @return the lastRestartTime
     */
    public Date getLastRestartTime() {
        return lastRestartTime;
    }

    /**
     * @param lastRestartTime the lastRestartTime to set
     */
    public void setLastRestartTime(Date lastRestartTime) {
        this.lastRestartTime = lastRestartTime;
        this.lastRestartTimeString = format.format(lastRestartTime);
    }

    /**
     * @return the lastRestartTimeString
     */
    public String getLastRestartTimeString() {
        return lastRestartTimeString;
    }

    /**
     * @param lastRestartTimeString the lastRestartTimeString to set
     */
    public void setLastRestartTimeString(String lastRestartTimeString) {
        this.lastRestartTimeString = lastRestartTimeString;
    }
}
