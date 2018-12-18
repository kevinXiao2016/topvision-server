/***********************************************************************
 * $Id: RestartRecord.java,v1.0 2013-2-21 下午5:05:39 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-2-21-下午5:05:39
 * 
 */
public class RestartRecord implements AliasesSuperType {
    private static final long serialVersionUID = -6857954709732577877L;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long entityId;
    private Long deviceIndex;
    private Long deviceLastOnlineTime;
    private String deviceLastOnline;
    private Long deviceReStartTime;
    private String deviceReStart;
    private Long collectTime;
    private String collect;
    private Long runningTime;
    private String runningTimeString;
    //Add by Victor@20140417为了算持续时间，原来deviceLastOnlineTime表示注册时间，在重启统计不再使用，最后一次重启的deviceNextStartTime填写当前时间
    private Long deviceNextStartTime;

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
     * @return the deviceLastOnlineTime
     */
    public Long getDeviceLastOnlineTime() {
        return deviceLastOnlineTime;
    }

    /**
     * @param deviceLastOnlineTime
     *            the deviceLastOnlineTime to set
     */
    public void setDeviceLastOnlineTime(Long deviceLastOnlineTime) {
        this.deviceLastOnlineTime = deviceLastOnlineTime;
        this.deviceLastOnline = formatter.format(deviceLastOnlineTime);
    }

    /**
     * @return the deviceLastOnline
     */
    public String getDeviceLastOnline() {
        return deviceLastOnline;
    }

    /**
     * @param deviceLastOnline
     *            the deviceLastOnline to set
     */
    public void setDeviceLastOnline(String deviceLastOnline) {
        this.deviceLastOnline = deviceLastOnline;
    }

    /**
     * @return the deviceReStartTime
     */
    public Long getDeviceReStartTime() {
        return deviceReStartTime;
    }

    /**
     * @param deviceReStartTime
     *            the deviceReStartTime to set
     */
    public void setDeviceReStartTime(Long deviceReStartTime) {
        this.deviceReStartTime = deviceReStartTime;
        this.deviceReStart = formatter.format(deviceReStartTime);
    }

    /**
     * @return the deviceReStart
     */
    public String getDeviceReStart() {
        return deviceReStart;
    }

    /**
     * @param deviceReStart
     *            the deviceReStart to set
     */
    public void setDeviceReStart(String deviceReStart) {
        this.deviceReStart = deviceReStart;
    }

    /**
     * @return the collectTime
     */
    public Long getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
        this.collect = formatter.format(collectTime);
    }

    /**
     * @return the collect
     */
    public String getCollect() {
        return collect;
    }

    /**
     * @param collect
     *            the collect to set
     */
    public void setCollect(String collect) {
        this.collect = collect;
    }

    /**
     * @return the runningTime
     */
    public Long getRunningTime() {
        return runningTime;
    }

    /**
     * @param runningTime
     *            the runningTime to set
     */
    public void setRunningTime(Long runningTime) {
        this.runningTime = runningTime;
    }

    /**
     * @return the runningTimeString
     */
    public String getRunningTimeString() {
        if (this.runningTimeString == null) {
            //Modify by Victor@20140417
            this.runningTime = this.deviceNextStartTime - this.deviceReStartTime;
            this.runningTimeString = DateUtils.getTimePeriod(runningTime, "zh_CN");
        }
        return runningTimeString;
    }

    /**
     * @param runningTimeString
     *            the runningTimeString to set
     */
    public void setRunningTimeString(String runningTimeString) {
        this.runningTimeString = runningTimeString;
    }

    /**
     * @return the deviceNextStartTime
     */
    public Long getDeviceNextStartTime() {
        return deviceNextStartTime;
    }

    /**
     * @param deviceNextStartTime the deviceNextStartTime to set
     */
    public void setDeviceNextStartTime(Long deviceNextStartTime) {
        this.deviceNextStartTime = deviceNextStartTime;
    }

}
