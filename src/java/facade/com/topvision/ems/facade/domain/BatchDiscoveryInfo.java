/***********************************************************************
 * $Id: BatchDiscoveryInfo.java,v1.0 2012-12-17 上午10:48:57 $
 * 
 * @author: RodJohnson
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author RodJohnson
 * @created @2012-12-17-上午10:48:57
 * 
 */
public class BatchDiscoveryInfo extends BaseEntity {
    private static final long serialVersionUID = 4417792530947769230L;
    private String ipAddress;
    // add by fanzidong@20170919 类B型CCMTS设备唯一性判断需要IP+MAC
    private String mac;
    private String typeName;
    private Long typeId;
    private Integer snmpParamIndex;
    private String sysObjectId;
    private String sysName;
    private String entityName;
    private String topoName;
    private String sysContact;
    private String sysLocation;
    private Integer topoFolderId;
    private String[] sysInfo;
    private Boolean flag = false;
    private Integer SnmpCounter;
    private DwrInfo dwrInfo;
    private SnmpParam snmpParam;
    private Object productInfo;

    /**
     * @return the dwrInfo
     */
    public DwrInfo getDwrInfo() {
        return dwrInfo;
    }

    /**
     * @param dwrInfo
     *            the dwrInfo to set
     */
    public void setDwrInfo(DwrInfo dwrInfo) {
        this.dwrInfo = dwrInfo;
    }

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
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName
     *            the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
        this.sysObjectId = sysInfo[0];
        this.sysName = sysInfo[1];
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
     * @return the snmpCounter
     */
    public Integer getSnmpCounter() {
        return SnmpCounter;
    }

    /**
     * @param snmpCounter
     *            the snmpCounter to set
     */
    public void setSnmpCounter(Integer snmpCounter) {
        SnmpCounter = snmpCounter;
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

    /**
     * @return the productInfo
     */
    public Object getProductInfo() {
        return productInfo;
    }

    /**
     * @param productInfo
     *            the productInfo to set
     */
    public void setProductInfo(Object productInfo) {
        this.productInfo = productInfo;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName
     *            the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the topoName
     */
    public String getTopoName() {
        return topoName;
    }

    /**
     * @param topoName
     *            the topoName to set
     */
    public void setTopoName(String topoName) {
        this.topoName = topoName;
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

    public String getSysContact() {
        return sysContact;
    }

    public void setSysContact(String sysContact) {
        this.sysContact = sysContact;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

}
