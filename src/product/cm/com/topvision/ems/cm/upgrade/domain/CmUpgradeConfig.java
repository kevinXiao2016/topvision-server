/***********************************************************************
 * $Id: CmUpgradeConfig.java,v1.0 2016年12月5日 下午6:43:40 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2016年12月5日-下午6:43:40
 *
 */
public class CmUpgradeConfig implements AliasesSuperType {

    private static final long serialVersionUID = 8583879234143805579L;
    private Integer configId;
    private String modulNum;
    private String softVersion;
    private String versionFileName;
    private Integer fileSize;

    public CmUpgradeConfig() {
    }

    public CmUpgradeConfig(Integer configId, String modulNum, String softVersion) {
        this.configId = configId;
        this.modulNum = modulNum;
        this.softVersion = softVersion;
    }

    public CmUpgradeConfig(Integer configId, String modulNum, String softVersion, String versionFileName, Integer fileSize) {
        this.configId = configId;
        this.modulNum = modulNum;
        this.softVersion = softVersion;
        this.versionFileName = versionFileName;
        this.fileSize = fileSize;
    }

    /**
     * @return the configId
     */
    public Integer getConfigId() {
        return configId;
    }

    /**
     * @param configId the configId to set
     */
    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    /**
     * @return the modulNum
     */
    public String getModulNum() {
        return modulNum;
    }

    /**
     * @param modulNum the modulNum to set
     */
    public void setModulNum(String modulNum) {
        this.modulNum = modulNum;
    }

    /**
     * @return the softVersion
     */
    public String getSoftVersion() {
        return softVersion;
    }

    /**
     * @param softVersion the softVersion to set
     */
    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    /**
     * @return the versionFileName
     */
    public String getVersionFileName() {
        return versionFileName;
    }

    /**
     * @param versionFileName the versionFileName to set
     */
    public void setVersionFileName(String versionFileName) {
        this.versionFileName = versionFileName;
    }

    /**
     * @return the fileSize
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

}
