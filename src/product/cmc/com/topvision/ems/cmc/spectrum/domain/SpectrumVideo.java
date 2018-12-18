/***********************************************************************
 * $Id: SpectrumVideo.java,v1.0 2014-2-19 下午2:02:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.framework.common.DateUtils;

/**
 * @author haojie
 * @created @2014-2-19-下午2:02:39
 * 
 */
public class SpectrumVideo implements Serializable {
    private static final long serialVersionUID = 19137662270122348L;
    public static final Integer REALTIME_VIDEO = 00000000001;
    public static final Integer HIS_VIDEO = 00000000002;
    private Long videoId;
    private String videoName;// 录像别名
    private Timestamp startTime;
    private String startTimeString;
    private Timestamp endTime;
    private String endTimeString;
    private Integer videoType;
    private String videoTypeString;
    private String entityIp;// 设备IP，如果是整合型就为上联OLT
    private Long entityType;// 设备类型
    private String oltAlias;// 如果为整合型，有OLT别名
    private String cmtsAlias;// CMTS别名
    private String userName;// 网管登录用户名
    private String terminalIp;// 终端IP
    private String url;// 录像的url
    private String cmcId; // cmc_id
    private Integer timeInterval;// 录像的步长
    private Long frameCount; // 录像的总帧数
    private Timestamp firstFrameTime; // 第一帧开始的时间
    private String firstFrameTimeString;

    public Long getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(Long frameCount) {
        this.frameCount = frameCount;
    }

    public String getCmcId() {
        return cmcId;
    }

    public void setCmcId(String cmcId) {
        this.cmcId = cmcId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        if (startTime != null) {
            this.startTimeString = DateUtils.FULL_FORMAT.format(startTime);
        }
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        if (endTime != null) {
            this.endTimeString = DateUtils.FULL_FORMAT.format(endTime);
        }
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public String getOltAlias() {
        return oltAlias;
    }

    public void setOltAlias(String oltAlias) {
        this.oltAlias = oltAlias;
    }

    public String getCmtsAlias() {
        return cmtsAlias;
    }

    public void setCmtsAlias(String cmtsAlias) {
        this.cmtsAlias = cmtsAlias;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public Integer getVideoType() {
        return videoType;
    }

    public void setVideoType(Integer videoType) {
        this.videoType = videoType;
        if (videoType != null) {
            switch (videoType) {
            case 1:
                this.videoTypeString = ResourcesUtil.getString("spectrum.realtimeRecord");
                break;
            case 2:
                this.videoTypeString = ResourcesUtil.getString("spectrum.historyRecord");
                break;
            default:
                this.videoTypeString = "";
                break;
            }
        }
    }

    public String getVideoTypeString() {
        return videoTypeString;
    }

    public void setVideoTypeString(String videoTypeString) {
        this.videoTypeString = videoTypeString;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpectrumVideo [videoId=");
        builder.append(videoId);
        builder.append(", videoName=");
        builder.append(videoName);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", startTimeString=");
        builder.append(startTimeString);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append(", endTimeString=");
        builder.append(endTimeString);
        builder.append(", videoType=");
        builder.append(videoType);
        builder.append(", videoTypeString=");
        builder.append(videoTypeString);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", entityType=");
        builder.append(entityType);
        builder.append(", oltAlias=");
        builder.append(oltAlias);
        builder.append(", cmtsAlias=");
        builder.append(cmtsAlias);
        builder.append(", userName=");
        builder.append(userName);
        builder.append(", terminalIp=");
        builder.append(terminalIp);
        builder.append(", url=");
        builder.append(url);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", timeInterval=");
        builder.append(timeInterval);
        builder.append("]");
        return builder.toString();
    }

    public Timestamp getFirstFrameTime() {
        return firstFrameTime;
    }

    public void setFirstFrameTime(Timestamp firstFrameTime) {
        this.firstFrameTime = firstFrameTime;
        if (firstFrameTime != null) {
            this.firstFrameTimeString = DateUtils.FULL_FORMAT.format(firstFrameTime);
        }
    }

    public String getFirstFrameTimeString() {
        return firstFrameTimeString;
    }

    public void setFirstFrameTimeString(String firstFrameTimeString) {
        this.firstFrameTimeString = firstFrameTimeString;
    }

}
