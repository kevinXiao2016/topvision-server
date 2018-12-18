/***********************************************************************
 * $Id: AutoDiscoveryInfo.java,v1.0 2014-5-11 下午3:54:05 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2014-5-11-下午3:54:05
 * 
 */
public class AutoDiscoveryInfo extends BatchDiscoveryInfo {
    private static final long serialVersionUID = -5696123257530247445L;
    private String ipAddress;
    private Integer topoFolderId;
    private Long typeId;
    private Integer snmpParamIndex;
    private String sysObjectId;
    private String sysName;
    private String[] sysInfo;
    private Boolean flag = false;
    private SnmpParam snmpParam;

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress
     *            the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the topoFolderId
     */
    public Integer getTopoFolderId() {
        return topoFolderId;
    }

    /**
     * @param topoFolderId
     *            the topoFolderId to set
     */
    public void setTopoFolderId(Integer topoFolderId) {
        this.topoFolderId = topoFolderId;
    }

    /**
     * @return the typeId
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the snmpParamIndex
     */
    public Integer getSnmpParamIndex() {
        return snmpParamIndex;
    }

    /**
     * @param snmpParamIndex
     *            the snmpParamIndex to set
     */
    public void setSnmpParamIndex(Integer snmpParamIndex) {
        this.snmpParamIndex = snmpParamIndex;
    }

    /**
     * @return the sysObjectId
     */
    public String getSysObjectId() {
        return sysObjectId;
    }

    /**
     * @param sysObjectId
     *            the sysObjectId to set
     */
    public void setSysObjectId(String sysObjectId) {
        this.sysObjectId = sysObjectId;
    }

    /**
     * @return the sysName
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * @param sysName
     *            the sysName to set
     */
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    /**
     * @return the sysInfo
     */
    public String[] getSysInfo() {
        return sysInfo;
    }

    /**
     * @param sysInfo
     *            the sysInfo to set
     */
    public void setSysInfo(String[] sysInfo) {
        this.sysInfo = sysInfo;
    }

    /**
     * @return the flag
     */
    public Boolean getFlag() {
        return flag;
    }

    /**
     * @param flag
     *            the flag to set
     */
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    /**
     * @return the snmpParam
     */
    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    /**
     * @param snmpParam
     *            the snmpParam to set
     */
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

}
