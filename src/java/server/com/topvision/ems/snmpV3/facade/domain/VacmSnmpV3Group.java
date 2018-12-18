/***********************************************************************
 * $Id: UsmSnmpV3Group.java,v1.0 2013-1-9 上午9:23:52 $
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
 * @created @2013-1-9-上午9:23:52
 * 
 */
@Alias("vacmSnmpV3Group")
public class VacmSnmpV3Group implements AliasesSuperType {
    private static final long serialVersionUID = -1531268711266399530L;
    public static final Integer DEFAULT_SECURITY_MODE = 3;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.2.1.1", index = true)
    private Integer snmpSecurityMode;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.2.1.2", index = true)
    private String snmpSecurityName;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.2.1.3", writable = true, type = "OctetString")
    private String snmpGroupName;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.2.1.4", writable = true, type = "Integer32")
    private Integer snmpGroupStorageType;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.2.1.5", writable = true, type = "Integer32")
    private Integer snmpGroupStatus;

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
     * @return the snmpSecurityMode
     */
    public Integer getSnmpSecurityMode() {
        return snmpSecurityMode;
    }

    /**
     * @param snmpSecurityMode
     *            the snmpSecurityMode to set
     */
    public void setSnmpSecurityMode(Integer snmpSecurityMode) {
        this.snmpSecurityMode = snmpSecurityMode;
    }

    /**
     * @return the snmpSecurityName
     */
    public String getSnmpSecurityName() {
        return snmpSecurityName;
    }

    /**
     * @param snmpSecurityName
     *            the snmpSecurityName to set
     */
    public void setSnmpSecurityName(String snmpSecurityName) {
        this.snmpSecurityName = snmpSecurityName;
    }

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
     * @return the snmpGroupStorageType
     */
    public Integer getSnmpGroupStorageType() {
        return snmpGroupStorageType;
    }

    /**
     * @param snmpGroupStorageType
     *            the snmpGroupStorageType to set
     */
    public void setSnmpGroupStorageType(Integer snmpGroupStorageType) {
        this.snmpGroupStorageType = snmpGroupStorageType;
    }

    /**
     * @return the snmpGroupStatus
     */
    public Integer getSnmpGroupStatus() {
        return snmpGroupStatus;
    }

    /**
     * @param snmpGroupStatus
     *            the snmpGroupStatus to set
     */
    public void setSnmpGroupStatus(Integer snmpGroupStatus) {
        this.snmpGroupStatus = snmpGroupStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VacmSnmpV3Group [entityId=");
        builder.append(entityId);
        builder.append(", snmpSecurityMode=");
        builder.append(snmpSecurityMode);
        builder.append(", snmpSecurityName=");
        builder.append(snmpSecurityName);
        builder.append(", snmpGroupName=");
        builder.append(snmpGroupName);
        builder.append(", snmpGroupStorageType=");
        builder.append(snmpGroupStorageType);
        builder.append(", snmpGroupStatus=");
        builder.append(snmpGroupStatus);
        builder.append("]");
        return builder.toString();
    }

}
