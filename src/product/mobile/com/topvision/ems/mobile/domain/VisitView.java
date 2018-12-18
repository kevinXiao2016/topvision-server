package com.topvision.ems.mobile.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class VisitView implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 8055974033235652093L;

    private String uuid;
    private Long startupTime;
    private Long endTime;
    private String lastView;
    private String deviceId;// 设备唯一标识
    private String deviceModel;// 设备的型号
    private String deviceVendor;// 设备的生产厂商
    private String deviceVersion;// 设备系统版本信息

    public String getUuid() {
        return uuid;
    }

    public Long getStartupTime() {
        return startupTime;
    }

    public Timestamp getStartupTimeStamp() {
        return new Timestamp(startupTime);
    }

    public void setStartupTime(Long startupTime) {
        this.startupTime = startupTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Timestamp getEndTimeStamp() {
        return new Timestamp(endTime);
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getLastView() {
        return lastView;
    }

    public void setLastView(String lastView) {
        this.lastView = lastView;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceVendor() {
        return deviceVendor;
    }

    public void setDeviceVendor(String deviceVendor) {
        this.deviceVendor = deviceVendor;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

}
