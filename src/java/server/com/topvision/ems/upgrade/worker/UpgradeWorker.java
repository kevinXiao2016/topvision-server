/***********************************************************************
 * $Id: UpgradeWorker.java,v1.0 2014年9月23日 下午3:51:49 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.worker;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:51:49
 * 
 */
public abstract class UpgradeWorker implements Callable<UpgradeEntity> {
    private UpgradeEntity upgradeEntity;
    protected String imageFile;
    private int upgradeStatus = 0; // 0--未开始升级 1--ftp/tftp服务器不通 2--ping不通
    protected SnmpParam snmpParam;
    protected String ubootVersion;
    protected String tftpIp;
    protected String ftpIp;
    protected String ftpUserName;
    protected String ftpPassword;
    protected UpgradeRecord upgradeRecord;
    protected String subType;
    protected Long writeConfig;

    protected BeanFactory beanFactory;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UpgradeEntity call() {
        return upgradeEntity;
    }

    public UpgradeEntity getUpgradeEntity() {
        return upgradeEntity;
    }

    public void setUpgradeEntity(UpgradeEntity upgradeEntity) {
        this.upgradeEntity = upgradeEntity;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(int upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public String getUbootVersion() {
        return ubootVersion;
    }

    public void setUbootVersion(String ubootVersion) {
        this.ubootVersion = ubootVersion;
    }

    public String getTftpIp() {
        return tftpIp;
    }

    public void setTftpIp(String tftpIp) {
        this.tftpIp = tftpIp;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public UpgradeRecord getUpgradeRecord() {
        return upgradeRecord;
    }

    public void setUpgradeRecord(UpgradeRecord upgradeRecord) {
        this.upgradeRecord = upgradeRecord;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Long isWriteConfig() {
        return writeConfig;
    }

    public void setWriteConfig(Long writeConfig) {
        this.writeConfig = writeConfig;
    }

}
