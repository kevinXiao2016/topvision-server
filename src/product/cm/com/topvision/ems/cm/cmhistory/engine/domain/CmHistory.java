package com.topvision.ems.cm.cmhistory.engine.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmHistory implements AliasesSuperType {
    private static final long serialVersionUID = -1887486416163882446L;
    private Long cmId;
    private Timestamp collectTime;
    private String collectTimeString;
    private Integer statusValue;
    private String statusValueString;
    private Integer checkStatus;
    private String checkStatusString;
    private String upChannelId;
    private String downChannelId;
    private String upChannelFreq;
    private String downChannelFreq;
    private String upRecvPower;
    private String upSnr;
    private String downSnr;
    private String upSendPower;
    private String downRecvPower;

    public String getCollectTimeString() {
        return collectTimeString;
    }

    public void setCollectTimeString(String collectTimeString) {
        this.collectTimeString = collectTimeString;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public String getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(String upChannelId) {
        this.upChannelId = upChannelId;
    }

    public String getDownChannelId() {
        return downChannelId;
    }

    public void setDownChannelId(String downChannelId) {
        this.downChannelId = downChannelId;
    }

    public String getUpChannelFreq() {
        return upChannelFreq;
    }

    public void setUpChannelFreq(String upChannelFreq) {
        this.upChannelFreq = upChannelFreq;
    }

    public String getDownChannelFreq() {
        return downChannelFreq;
    }

    public void setDownChannelFreq(String downChannelFreq) {
        this.downChannelFreq = downChannelFreq;
    }

    public String getUpRecvPower() {
        return upRecvPower;
    }

    public void setUpRecvPower(String upRecvPower) {
        this.upRecvPower = upRecvPower;
    }

    public String getUpSnr() {
        return upSnr;
    }

    public void setUpSnr(String upSnr) {
        this.upSnr = upSnr;
    }

    public String getDownSnr() {
        return downSnr;
    }

    public void setDownSnr(String downSnr) {
        this.downSnr = downSnr;
    }

    public String getUpSendPower() {
        return upSendPower;
    }

    public void setUpSendPower(String upSendPower) {
        this.upSendPower = upSendPower;
    }

    public String getDownRecvPower() {
        return downRecvPower;
    }

    public void setDownRecvPower(String downRecvPower) {
        this.downRecvPower = downRecvPower;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStatusValueString() {
        return statusValueString;
    }

    public void setStatusValueString(String statusValueString) {
        this.statusValueString = statusValueString;
    }

    public String getCheckStatusString() {
        return checkStatusString;
    }

    public void setCheckStatusString(String checkStatusString) {
        this.checkStatusString = checkStatusString;
    }

}
