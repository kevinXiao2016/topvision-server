/***********************************************************************
 * $Id: UpgradeJobInfo.java,v1.0 2014年9月23日 下午3:09:30 $
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
 * @created @2014年9月23日-下午3:09:30
 * 
 */
@Alias("upgradeJobInfo")
public class UpgradeJobInfo implements AliasesSuperType {
    private static final long serialVersionUID = 3304264071260782441L;

    private Long jobId;
    private String name;
    private String versionName;
    private Timestamp createTime;
    private Timestamp startTime;
    private Timestamp updateTime;
    private String imageFile;
    private String workerClass;
    private String jobClass;
    private Integer type;// 升级方式 0 立即升级 1 定时升级
    private String transferType;// FTP - 类A ; TFTP- 类B
    private String subType;
    private String entityIds;

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getWorkerClass() {
        return workerClass;
    }

    public void setWorkerClass(String workerClass) {
        this.workerClass = workerClass;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpgradeJobInfo [jobId=");
        builder.append(jobId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", versionName=");
        builder.append(versionName);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", updateTime=");
        builder.append(updateTime);
        builder.append(", imageFile=");
        builder.append(imageFile);
        builder.append(", workerClass=");
        builder.append(workerClass);
        builder.append("]");
        return builder.toString();
    }

}
