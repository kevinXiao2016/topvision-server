/***********************************************************************
 * $Id: UsmSnmpV3Access.java,v1.0 2013-1-9 上午9:25:22 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:25:22
 * 
 */
@Alias("vacmSnmpV3Access")
public class VacmSnmpV3Access implements AliasesSuperType {
    private static final long serialVersionUID = -3561394834369396614L;
    public static String DEFAULT_CONTEXT_PREFIX = "";
    public static Integer DEFAULT_SECURITY_MODE = 3;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.2.1.3", index = true)
    private String snmpGroupName;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.1", index = true)
    private String snmpContextPrefix;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.2", index = true, type = "Integer32")
    private Integer snmpSecurityMode;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.3", index = true)
    private Integer snmpSecurityLevel;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.4", writable = true, type = "Integer32")
    private Integer snmpContextMatch;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.5", writable = true, type = "OctetString")
    private String snmpReadView;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.6", writable = true, type = "OctetString")
    private String snmpWriteView;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.7", writable = true, type = "OctetString")
    private String snmpNotifyView;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.8", writable = true, type = "Integer32")
    private Integer snmpAccessStorageType;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.4.1.9", writable = true, type = "Integer32")
    private Integer snmpAccessStatus;

    /**
     * @return the snmpGroupName
     */
    public String getSnmpGroupName() {
        return snmpGroupName;
    }

    /**
     * @param snmpGroupName
     *            the snmpGroupName to set
     */
    public void setSnmpGroupName(String snmpGroupName) {
        this.snmpGroupName = snmpGroupName;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the snmpContextPrefix
     */
    public String getSnmpContextPrefix() {
        return snmpContextPrefix;
    }

    /**
     * @param snmpContextPrefix
     *            the snmpContextPrefix to set
     */
    public void setSnmpContextPrefix(String snmpContextPrefix) {
        this.snmpContextPrefix = snmpContextPrefix;
    }

    /**
     * @return the snmpSecurityModel
     */
    public Integer getSnmpSecurityMode() {
        return snmpSecurityMode;
    }

    /**
     * @param snmpSecurityModel
     *            the snmpSecurityModel to set
     */
    public void setSnmpSecurityMode(Integer snmpSecurityMode) {
        this.snmpSecurityMode = snmpSecurityMode;
    }

    /**
     * @return the snmpSecurityLevel
     */
    public Integer getSnmpSecurityLevel() {
        return snmpSecurityLevel;
    }

    /**
     * @param snmpSecurityLevel
     *            the snmpSecurityLevel to set
     */
    public void setSnmpSecurityLevel(Integer snmpSecurityLevel) {
        this.snmpSecurityLevel = snmpSecurityLevel;
    }

    /**
     * @return the snmpContextMatch
     */
    public Integer getSnmpContextMatch() {
        return snmpContextMatch;
    }

    /**
     * @param snmpContextMatch
     *            the snmpContextMatch to set
     */
    public void setSnmpContextMatch(Integer snmpContextMatch) {
        this.snmpContextMatch = snmpContextMatch;
    }

    /**
     * @return the snmpReadView
     */
    public String getSnmpReadView() {
        return snmpReadView;
    }

    /**
     * @param snmpReadView
     *            the snmpReadView to set
     */
    public void setSnmpReadView(String snmpReadView) {
        this.snmpReadView = snmpReadView;
    }

    /**
     * @return the snmpWriteView
     */
    public String getSnmpWriteView() {
        return snmpWriteView;
    }

    /**
     * @param snmpWriteView
     *            the snmpWriteView to set
     */
    public void setSnmpWriteView(String snmpWriteView) {
        this.snmpWriteView = snmpWriteView;
    }

    /**
     * @return the snmpNotifyView
     */
    public String getSnmpNotifyView() {
        return snmpNotifyView;
    }

    /**
     * @param snmpNotifyView
     *            the snmpNotifyView to set
     */
    public void setSnmpNotifyView(String snmpNotifyView) {
        this.snmpNotifyView = snmpNotifyView;
    }

    /**
     * @return the snmpAccessStorageType
     */
    public Integer getSnmpAccessStorageType() {
        return snmpAccessStorageType;
    }

    /**
     * @param snmpAccessStorageType
     *            the snmpAccessStorageType to set
     */
    public void setSnmpAccessStorageType(Integer snmpAccessStorageType) {
        this.snmpAccessStorageType = snmpAccessStorageType;
    }

    /**
     * @return the snmpAccessStatus
     */
    public Integer getSnmpAccessStatus() {
        return snmpAccessStatus;
    }

    /**
     * @param snmpAccessStatus
     *            the snmpAccessStatus to set
     */
    public void setSnmpAccessStatus(Integer snmpAccessStatus) {
        this.snmpAccessStatus = snmpAccessStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VacmSnmpV3Access [entityId=");
        builder.append(entityId);
        builder.append(", snmpGroupName=");
        builder.append(snmpGroupName);
        builder.append(", snmpContextPrefix=");
        builder.append(snmpContextPrefix);
        builder.append(", snmpSecurityMode=");
        builder.append(snmpSecurityMode);
        builder.append(", snmpSecurityLevel=");
        builder.append(snmpSecurityLevel);
        builder.append(", snmpContextMatch=");
        builder.append(snmpContextMatch);
        builder.append(", snmpReadView=");
        builder.append(snmpReadView);
        builder.append(", snmpWriteView=");
        builder.append(snmpWriteView);
        builder.append(", snmpNotifyView=");
        builder.append(snmpNotifyView);
        builder.append(", snmpAccessStorageType=");
        builder.append(snmpAccessStorageType);
        builder.append(", snmpAccessStatus=");
        builder.append(snmpAccessStatus);
        builder.append("]");
        return builder.toString();
    }

}
