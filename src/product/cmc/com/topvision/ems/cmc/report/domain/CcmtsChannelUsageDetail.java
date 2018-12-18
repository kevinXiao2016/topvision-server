package com.topvision.ems.cmc.report.domain;

/**
 * CCMTS设备信道使用情况，用于1、从数据库中读取的原始数据，以信道为单位，地域、OLT、PON、CCMTS、每一个信道的为单位。
 * 2、经过Servcie层处理之后，形成信道数量和主要信道调制方式，以一个CCMTS为单位
 * 
 * @author YangYi
 * @created @2013-9-12
 * 
 */
public class CcmtsChannelUsageDetail {
    private String locationId;// 地域ID
    private String locationName;// 地域名称
    private String oltId;// OLT的Id
    private String oltName;// OLT名称
    private String oltIp;// OLT的IP
    private String ponId;// PON口Id
    private String ponIndex;// PON口Index
    private String cmcId;// CCMTS的Id
    private String ccmtsName;// CCMTS名称
    private String ccmtsMAC;// CCMTS的MAC地址
    private String ccmtsType;// CCMTS的类型
    private String ccmtsIfAdminStatus;// CCMTS开启状态
    private String ccmtsDownChannelModule;// CCMTS开启下行信道的调制方式
    private String ccmtsUpChannelModule;// CCMTS开启上行信道的调制方式
    private String ccmtsDownChannelNum;// CCMTS开启下行信道的数量
    private String ccmtsUpChannelNum;// CCMTS开启上行信道的数量

    public String getOltId() {
        return oltId;
    }

    public void setOltId(String oltId) {
        this.oltId = oltId;
    }

    public String getCmcId() {
        return cmcId;
    }

    public void setCmcId(String cmcId) {
        this.cmcId = cmcId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public String getOltIp() {
        return oltIp;
    }

    public void setOltIp(String oltIp) {
        this.oltIp = oltIp;
    }

    public String getPonId() {
        return ponId;
    }

    public void setPonId(String ponId) {
        this.ponId = ponId;
    }

    public String getCcmtsName() {
        return ccmtsName;
    }

    public void setCcmtsName(String ccmtsName) {
        this.ccmtsName = ccmtsName;
    }

    public String getCcmtsMAC() {
        return ccmtsMAC;
    }

    public void setCcmtsMAC(String ccmtsMAC) {
        this.ccmtsMAC = ccmtsMAC;
    }

    public String getCcmtsType() {
        return ccmtsType;
    }

    public void setCcmtsType(String ccmtsType) {
        this.ccmtsType = ccmtsType;
    }

    public String getCcmtsIfAdminStatus() {
        return ccmtsIfAdminStatus;
    }

    public void setCcmtsIfAdminStatus(String ccmtsIfAdminStatus) {
        this.ccmtsIfAdminStatus = ccmtsIfAdminStatus;
    }

    public String getCcmtsDownChannelModule() {
        return ccmtsDownChannelModule;
    }

    public void setCcmtsDownChannelModule(String ccmtsDownChannelModule) {
        this.ccmtsDownChannelModule = ccmtsDownChannelModule;
    }

    public String getCcmtsUpChannelModule() {
        return ccmtsUpChannelModule;
    }

    public void setCcmtsUpChannelModule(String ccmtsUpChannelModule) {
        this.ccmtsUpChannelModule = ccmtsUpChannelModule;
    }

    public String getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(String ponIndex) {
        this.ponIndex = ponIndex;
    }

    public String getCcmtsDownChannelNum() {
        return ccmtsDownChannelNum;
    }

    public void setCcmtsDownChannelNum(String ccmtsDownChannelNum) {
        this.ccmtsDownChannelNum = ccmtsDownChannelNum;
    }

    public String getCcmtsUpChannelNum() {
        return ccmtsUpChannelNum;
    }

    public void setCcmtsUpChannelNum(String ccmtsUpChannelNum) {
        this.ccmtsUpChannelNum = ccmtsUpChannelNum;
    }

}
