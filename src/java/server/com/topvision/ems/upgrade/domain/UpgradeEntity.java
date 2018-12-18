/***********************************************************************
 * $Id: UpgradEntity.java,v1.0 2014年10月11日 下午1:55:21 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.domain;

import java.sql.Timestamp;

import com.topvision.framework.snmp.SnmpParam;
import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2014年10月11日-下午1:55:21
 * 
 */
@Alias("upgradeEntity")
public class UpgradeEntity implements AliasesSuperType {
    private static final long serialVersionUID = -5109363872860656830L;
    private Long jobId;
    private Long entityId;
    private Long typeId;
    private String ip;
    private String mac;
    private String name;
    private String typeName;
    private SnmpParam param;
    private String note;
    private Long retryTimes;
    private Boolean retry;
    private Timestamp upgradeTime;
    private String upgradeTimeString;
    private String upgradeVersion;
    private Integer upgradeStatus;
    private Timestamp startTime;
    private Timestamp endTime;
    private String startTimeString;
    private String endTimeString;
    private String upgradeNote;
    private Boolean forceUpgrade = false;
    private String upgradeStatusString;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Boolean getRetry() {
        return retry;
    }

    public void setRetry(Boolean retry) {
        this.retry = retry;
    }

    public Timestamp getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(Timestamp upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public String getUpgradeTimeString() {
        return upgradeTimeString;
    }

    public void setUpgradeTimeString(String upgradeTimeString) {
        this.upgradeTimeString = upgradeTimeString;
    }

    public String getUpgradeVersion() {
        return upgradeVersion;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public Integer getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
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

    public String getUpgradeNote() {
        return upgradeNote;
    }

    public void setUpgradeNote(String upgradeNote) {
        this.upgradeNote = upgradeNote;
    }

    public Boolean getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(Boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getUpgradeStatusString() {
        return upgradeStatusString;
    }

    public void setUpgradeStatusString(String upgradeStatusString) {
        this.upgradeStatusString = upgradeStatusString;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public SnmpParam getParam() {
        return param;
    }

    public void setParam(SnmpParam param) {
        this.param = param;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpgradeEntity that = (UpgradeEntity) o;

        if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
        if (jobId != null ? !jobId.equals(that.jobId) : that.jobId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = jobId != null ? jobId.hashCode() : 0;
        result = 31 * result + (entityId != null ? entityId.hashCode() : 0);
        return result;
    }
}
