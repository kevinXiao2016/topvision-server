package com.topvision.platform.domain;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.domain.BaseEntity;

@Alias("systemLog")
public class SystemLog extends BaseEntity implements com.topvision.framework.dao.mybatis.AliasesSuperType {
    private static final long serialVersionUID = -8819460962572070113L;

    public static final int SUCCESS = 1;
    public static final int FAILURE = 0;
    private long logId;
    private String ip;
    private String userName;
    private String description;
    private Timestamp createTime;
    private String createTimeString;
    private String operationType;
    private String appName;
    private int success = SUCCESS;

    public String getAppName() {
        return appName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public String getDescription() {
        return description;
    }

    public String getIp() {
        return ip;
    }

    public long getLogId() {
        return logId;
    }

    public String getOperationType() {
        return operationType;
    }

    public int getSuccess() {
        return success;
    }

    public String getUserName() {
        return userName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
        this.createTimeString = DateUtils.format(createTime);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the createTimeString
     */
    public String getCreateTimeString() {
        return createTimeString;
    }

    /**
     * @param createTimeString
     *            the createTimeString to set
     */
    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    @Override
    public String toString() {
        return description;
    }
}
