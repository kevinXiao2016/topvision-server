/***********************************************************************
 * $Id: UpgradeRecord.java,v1.0 2014年9月23日 下午3:06:01 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:06:01
 * 
 */
@Alias("upgradeRecord")
public class UpgradeRecord implements AliasesSuperType {
    private static final long serialVersionUID = -860367189390734419L;

    private Long recordId;
    private Long entityId;
    private String entityName;
    private String manageIp;
    private String mac;
    private String originVersion;
    private String upLinkEntityName;
    private Long retryTimes;
    private String upgradeVersion;
    private Integer status;
    private Timestamp startTime;
    private Timestamp endTime;
    private String startTimeString;
    private String endTimeString;
    private Long typeId;
    private String typeName;
    private String jobName;
    private String statusString;
    private String uplinkDevice;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOriginVersion() {
        return originVersion;
    }

    public void setOriginVersion(String originVersion) {
        this.originVersion = originVersion;
    }

    public String getUpLinkEntityName() {
        return upLinkEntityName;
    }

    public void setUpLinkEntityName(String upLinkEntityName) {
        this.upLinkEntityName = upLinkEntityName;
    }

    public Long getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getUpgradeVersion() {
        return upgradeVersion;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpgradeRecord [recordId=");
        builder.append(recordId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", manageIp=");
        builder.append(manageIp);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", originVersion=");
        builder.append(originVersion);
        builder.append(", upLinkEntityName=");
        builder.append(upLinkEntityName);
        builder.append(", retryTimes=");
        builder.append(retryTimes);
        builder.append(", upgradeVersion=");
        builder.append(upgradeVersion);
        builder.append(", status=");
        builder.append(status);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append(", startTimeString=");
        builder.append(startTimeString);
        builder.append(", endTimeString=");
        builder.append(endTimeString);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", jobName=");
        builder.append(jobName);
        builder.append(", statusString=");
        builder.append(statusString);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append("]");
        return builder.toString();
    }

}
