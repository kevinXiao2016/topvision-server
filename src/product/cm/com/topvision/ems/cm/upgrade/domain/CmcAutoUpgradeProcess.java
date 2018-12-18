/***********************************************************************
 * $Id: CmcAutoUpgradeProcess.java,v1.0 2016年12月8日 上午11:36:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.domain;

/**
 * @author Rod John
 * @created @2016年12月8日-上午11:36:06
 *
 */
public class CmcAutoUpgradeProcess {

    public static final Integer AUTO_UPGRADE_INIT = 0;
    // 下发配置成功
    public static final Integer AUTO_UPGRADE_SUCCESS = 1;
    // 下发配置失败
    public static final Integer AUTO_UPGRADE_FAILURE = 2;
    // 升级文件总大小超过限制
    public static final Integer AUTO_UPGRADE_FILEERROR = 3;
    // 正在清除自动升级配置...
    public static final Integer AUTO_UPGRADE_CLEANING_CONFIG = 4;
    // 正在清除CM升级文件...
    public static final Integer AUTO_UPGRADE_CLEANING_FILE = 5;
    // 清除自动升级配置失败
    public static final Integer AUTO_UPGRADE_CLEANING_CONFIG_FAILURE = 6;
    // 清除CM升级文件失败
    public static final Integer AUTO_UPGRADE_CLEANING_FILE_FAILURE = 7;
    // 正在下发自动升级配置...
    public static final Integer AUTO_UPGRADE_APPLYING = 8;
    // 正在上传升级文件
    public static final Integer AUTO_UPGRADE_UPLOADING_FILE = 9;
    // 当前设备版本不支持
    public static final Integer AUTO_UPGRADE_VERSION_FAILURE = 10;

    private Long entityId;
    private String name;
    private String mac;
    private Integer step;
    private Boolean result = false;

    public CmcAutoUpgradeProcess(Long entityId, String name, String mac) {
        this.entityId = entityId;
        this.name = name;
        this.mac = mac;
        this.step = AUTO_UPGRADE_INIT;
    }

    public CmcAutoUpgradeProcess(Long entityId, String name, String mac, Integer step) {
        this.entityId = entityId;
        this.name = name;
        this.mac = mac;
        this.step = step;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return the step
     */
    public Integer getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(Integer step) {
        this.step = step;
    }

    /**
     * @return the result
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Boolean result) {
        this.result = result;
    }

}
