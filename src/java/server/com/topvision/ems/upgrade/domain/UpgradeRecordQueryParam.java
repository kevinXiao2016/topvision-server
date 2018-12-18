/***********************************************************************
 * $Id: UpgradeRecordQueryParam.java,v1.0 2014年9月23日 下午3:04:08 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:04:08
 * 
 */
@Alias("upgradeRecordQueryParam")
public class UpgradeRecordQueryParam implements AliasesSuperType {
    private static final long serialVersionUID = 5649727383097812075L;

    private String entityName;
    private String manageIp;
    private String mac;
    private Long status;
    private Long typeId;
    private String upLinkEntityName;
    private String jobName;
    private String startTime;
    private String endTime;
    private Integer start;
    private Integer limit;
    private String sort;
    private String dir;
    private String uplinkDevice;

    public UpgradeRecordQueryParam(String entityName, String manageIp, String mac, Long status, Long typeId,
            String uplinkDevice, String upLinkEntityName, String jobName, String startTime, String endTime,
            Integer start, Integer limit, String sort, String dir) {
        this.entityName = entityName;
        this.manageIp = manageIp;
        this.mac = mac;
        this.status = status;
        this.typeId = typeId;
        this.upLinkEntityName = upLinkEntityName;
        this.jobName = jobName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.start = start;
        this.limit = limit;
        this.sort = sort;
        this.dir = dir;
        this.uplinkDevice = uplinkDevice;
    }

    public UpgradeRecordQueryParam() {
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getUpLinkEntityName() {
        return upLinkEntityName;
    }

    public void setUpLinkEntityName(String upLinkEntityName) {
        this.upLinkEntityName = upLinkEntityName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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
        builder.append("UpgradeRecordQueryParam [entityName=");
        builder.append(entityName);
        builder.append(", manageIp=");
        builder.append(manageIp);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", status=");
        builder.append(status);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", upLinkEntityName=");
        builder.append(upLinkEntityName);
        builder.append(", jobName=");
        builder.append(jobName);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append(", start=");
        builder.append(start);
        builder.append(", limit=");
        builder.append(limit);
        builder.append(", sort=");
        builder.append(sort);
        builder.append(", dir=");
        builder.append(dir);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append("]");
        return builder.toString();
    }

}
