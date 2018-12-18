/***********************************************************************
 * $Id: VacmSnmpV3View.java,v1.0 2013-1-9 上午10:17:05 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.facade.domain;

import java.util.regex.Pattern;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.snmpV3.util.SnmpV3Util;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2013-1-9-上午10:17:05
 * 
 */
@Alias("vacmSnmpV3View")
public class VacmSnmpV3View implements AliasesSuperType {
    private static final long serialVersionUID = 2661853352752499026L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.5.2.1.1", index = true)
    private String snmpViewName;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.5.2.1.2", index = true, type = "OBJECT IDENTIFIER")
    private String snmpViewSubtree;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.5.2.1.3", writable = true, type = "OctetString")
    private byte[] snmpViewMaskOct;
    private String snmpViewMask;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.5.2.1.4", writable = true, type = "Integer32")
    private Integer snmpViewMode;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.5.2.1.5", writable = true, type = "Integer32")
    private Integer snmpViewStorageType;
    @SnmpProperty(oid = "1.3.6.1.6.3.16.1.5.2.1.6", writable = true, type = "Integer32")
    private Integer snmpViewStatus;

    public static String getOidStringFromNoISOControl(String noISOControlString) {
        Pattern hexStringPattern = Pattern.compile("^([0-9a-fA-F]{2})([:][0-9a-fA-F]{2})+$");
        if (!hexStringPattern.matcher(noISOControlString).matches()) {
            String result = "";
            char[] charArray = noISOControlString.toCharArray();
            for (char c : charArray) {
                result += Integer.toHexString(c) + ".";
            }
            return result.substring(0, result.length() - 1);
        }
        return noISOControlString;
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
     * @return the snmpViewName
     */
    public String getSnmpViewName() {
        return snmpViewName;
    }

    /**
     * @param snmpViewName
     *            the snmpViewName to set
     */
    public void setSnmpViewName(String snmpViewName) {
        this.snmpViewName = snmpViewName;
    }

    /**
     * @return the snmpViewSubtree
     */
    public String getSnmpViewSubtree() {
        return snmpViewSubtree;
    }

    /**
     * @param snmpViewSubtree
     *            the snmpViewSubtree to set
     */
    public void setSnmpViewSubtree(String snmpViewSubtree) {
        this.snmpViewSubtree = snmpViewSubtree;
    }

    /**
     * @return the snmpViewMaskOct
     */
    public byte[] getSnmpViewMaskOct() {
        if (snmpViewMaskOct == null) {
            if (snmpViewMask != null) {
                snmpViewMaskOct = SnmpV3Util.number2Byte(snmpViewMask);
            }
        }
        return snmpViewMaskOct;
    }

    /**
     * @param snmpViewMaskOct
     *            the snmpViewMaskOct to set
     */
    public void setSnmpViewMaskOct(byte[] snmpViewMaskOct) {
        this.snmpViewMaskOct = snmpViewMaskOct;
        this.snmpViewMask = SnmpV3Util.byte2number(snmpViewMaskOct);
    }

    /**
     * @return the snmpViewMask
     */
    public String getSnmpViewMask() {
        return snmpViewMask;
    }

    /**
     * @param snmpViewMask
     *            the snmpViewMask to set
     */
    public void setSnmpViewMask(String snmpViewMask) {
        this.snmpViewMask = snmpViewMask;
    }

    /**
     * @return the snmpViewMode
     */
    public Integer getSnmpViewMode() {
        return snmpViewMode;
    }

    /**
     * @param snmpViewMode
     *            the snmpViewMode to set
     */
    public void setSnmpViewMode(Integer snmpViewMode) {
        this.snmpViewMode = snmpViewMode;
    }

    /**
     * @return the snmpViewStorageType
     */
    public Integer getSnmpViewStorageType() {
        return snmpViewStorageType;
    }

    /**
     * @param snmpViewStorageType
     *            the snmpViewStorageType to set
     */
    public void setSnmpViewStorageType(Integer snmpViewStorageType) {
        this.snmpViewStorageType = snmpViewStorageType;
    }

    /**
     * @return the snmpViewStatus
     */
    public Integer getSnmpViewStatus() {
        return snmpViewStatus;
    }

    /**
     * @param snmpViewStatus
     *            the snmpViewStatus to set
     */
    public void setSnmpViewStatus(Integer snmpViewStatus) {
        this.snmpViewStatus = snmpViewStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UsmSnmpV3User [entityId=");
        builder.append(entityId);
        builder.append(", snmpViewName=");
        builder.append(snmpViewName);
        builder.append(", snmpViewSubtree=");
        builder.append(snmpViewSubtree);
        builder.append(", snmpViewMask=");
        builder.append(snmpViewMask);
        builder.append(", snmpViewMode=");
        builder.append(snmpViewMode);
        builder.append(", snmpViewStorageType=");
        builder.append(snmpViewStorageType);
        builder.append(", snmpViewStatus=");
        builder.append(snmpViewStatus);
        builder.append("]");
        return builder.toString();
    }

}
