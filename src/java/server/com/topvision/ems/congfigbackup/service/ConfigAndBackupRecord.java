package com.topvision.ems.congfigbackup.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.congfigbackup.exception.FTPDownloadConfigFileException;
import com.topvision.ems.congfigbackup.exception.UploadConfigFileException;
import com.topvision.ems.upgrade.exception.ExceedMaxTelnetNumberException;
import com.topvision.ems.upgrade.exception.TelnetConnectException;
import com.topvision.ems.upgrade.exception.TelnetLoginException;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.exception.FtpConnectException;

/**
 * 批量配置操作日志Bean
 * 
 * @author Bravin
 * 
 */
@Alias("configAndBackupRecord")
public class ConfigAndBackupRecord implements AliasesSuperType {
    private static final long serialVersionUID = 4722862555125532228L;
    public static final int APPLY_CONFIG = 1;
    public static final int SAVE_CONFIG = 2;
    public static final int BACKUP_CONFIG = 3;
    public static final int SUCCESS = 0;
    public static final int FAILD = 1;
    public static final int TELNET_LOGIN_ER = 2;
    public static final int TELNET_CONNECT_ER = 3;
    public static final int EXCEED_MAX_TELNET_NUMBER_ER = 4;
    public static final int FTP_CONNECT_ER = 5;

    private Long userId;
    private int operationType;
    private String clientIp;
    private int result;
    private String fileName;
    private long entityId;
    private Date operationTime;
    private String ip;
    //界面展示的时候用
    private String username;
    private String name;
    private String operationName;

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the operationName
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * @param operationName
     *            the operationName to set
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * @return the clientIp
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @param clientIp
     *            the clientIp to set
     */
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the result
     */
    public int getResult() {
        return result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @return the entityId
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the deviceName
     */
    public String getDeviceName() {
        return this.name + "/" + this.ip;
    }

    /**
     * @return the operationTime
     */
    public String getOperationTime() {
        DateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // return operationTime.toString();
        return formate.format(this.operationTime);
    }

    /**
     * @param operationTime
     *            the operationTime to set
     */
    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public void setResult(Exception e) {
        if (e instanceof ExceedMaxTelnetNumberException) {
            setResult(ConfigAndBackupRecord.EXCEED_MAX_TELNET_NUMBER_ER);
        } else if (e instanceof TelnetConnectException) {
            setResult(ConfigAndBackupRecord.TELNET_CONNECT_ER);
        } else if (e instanceof TelnetLoginException) {
            setResult(ConfigAndBackupRecord.TELNET_LOGIN_ER);
        } else if (e instanceof FtpConnectException || e instanceof UploadConfigFileException
                || e instanceof FTPDownloadConfigFileException) {
            setResult(ConfigAndBackupRecord.FTP_CONNECT_ER);
        } else {
            setResult(ConfigAndBackupRecord.FAILD);
        }
    }
}
