/***********************************************************************
 * $Id: NbiBaseConfig.java,v1.0 2016-3-16 上午10:30:14 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2016-3-16-上午10:30:14
 *
 */
public class NbiBaseConfig implements AliasesSuperType {
    public static final int NBI_SWITCH_ON = 1;
    public static final int NBI_SWITCH_OFF = 0;
    private static final long serialVersionUID = 5639897603702753050L;
    private Integer mode = 1;// 1:PULL 2:PUSH
    private String ftpAddr;
    private Integer ftpPort = 21;
    private String ftpUser;
    private String ftpPwd;
    private String filePath = "dd_out";
    private Integer recordMax = 10000;
    private Integer fileSaveTime = 1;// 单位天
    private String nbiAddr;
    private Integer nbiPort;
    private String encoding = "GBK";
    private Integer nbiSwitch = 0;

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getFtpAddr() {
        return ftpAddr;
    }

    public void setFtpAddr(String ftpAddr) {
        this.ftpAddr = ftpAddr;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPwd() {
        return ftpPwd;
    }

    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getRecordMax() {
        return recordMax;
    }

    public void setRecordMax(Integer recordMax) {
        this.recordMax = recordMax;
    }

    public Integer getFileSaveTime() {
        return fileSaveTime;
    }

    public void setFileSaveTime(Integer fileSaveTime) {
        this.fileSaveTime = fileSaveTime;
    }

    public String getNbiAddr() {
        return nbiAddr;
    }

    public void setNbiAddr(String nbiAddr) {
        this.nbiAddr = nbiAddr;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    public Integer getNbiPort() {
        return nbiPort;
    }

    public void setNbiPort(Integer nbiPort) {
        this.nbiPort = nbiPort;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Integer getNbiSwitch() {
        return nbiSwitch;
    }

    public void setNbiSwitch(Integer nbiSwitch) {
        this.nbiSwitch = nbiSwitch;
    }

}
