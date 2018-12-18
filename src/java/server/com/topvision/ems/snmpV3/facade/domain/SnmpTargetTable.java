/***********************************************************************
 * $Id: SnmpTargetTable.java,v1.0 2013-01-09 上午9:39:57 $
 * 
 * @author: yq
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.facade.domain;

import java.util.regex.Pattern;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * SNMP V3 Target
 * 
 * @author yq
 * 
 */
@Alias("snmpTargetTable")
public class SnmpTargetTable implements AliasesSuperType {
    private static final long serialVersionUID = 509527653328524066L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.1", index = true)
    private String targetName;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.2", writable = true, type = "OBJECT IDENTIFIER")
    private String targetDomain;
    private String targetDomainString;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.3", writable = true, type = "OctetString")
    private String targetAddress;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.4", writable = true, type = "Integer32")
    private Long targetTimeout;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.5", writable = true, type = "Integer32")
    private Integer targetRetryCount;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.6", writable = true, type = "OctetString")
    private String targetTagList;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.7", writable = true, type = "OctetString")
    private String targetParams;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.8", writable = true, type = "Integer32")
    private Integer targetStorageType;
    @SnmpProperty(oid = "1.3.6.1.6.3.12.1.2.1.9", writable = true, type = "Integer32")
    private Integer targetRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetDomain() {
        return targetDomain;
    }

    public void setTargetDomain(String targetDomain) {
        this.targetDomain = targetDomain;
    }

    public String getTargetDomainString() {
        return targetDomainString;
    }

    public void setTargetDomainString(String targetDomainString) {
        this.targetDomainString = targetDomainString;
    }

    public String getTargetAddress() {
        if (targetAddress != null) {
            if (targetAddress.split(":").length == 6) {
                return targetAddress;
            }
            return getStringFromNoISOControl(targetAddress);
        } else {
            return null;
        }
    }

    private String getStringFromNoISOControl(String noISOControlString) {
        Pattern hexStringPattern = Pattern.compile("^([0-9a-fA-F]{2})([:][0-9a-fA-F]{2})+$");
        if (!hexStringPattern.matcher(noISOControlString).matches()) {
            String result = "";
            char[] charArray = noISOControlString.toCharArray();
            for (char c : charArray) {
                result += Integer.toHexString(c) + ":";
            }
            return result.substring(0, result.length() - 1);
        }
        return noISOControlString;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public Long getTargetTimeout() {
        return targetTimeout;
    }

    public void setTargetTimeout(Long targetTimeout) {
        this.targetTimeout = targetTimeout;
    }

    public Integer getTargetRetryCount() {
        return targetRetryCount;
    }

    public void setTargetRetryCount(Integer targetRetryCount) {
        this.targetRetryCount = targetRetryCount;
    }

    public String getTargetTagList() {
        return targetTagList;
    }

    public void setTargetTagList(String targetTagList) {
        this.targetTagList = targetTagList;
    }

    public String getTargetParams() {
        return targetParams;
    }

    public void setTargetParams(String targetParams) {
        this.targetParams = targetParams;
    }

    public Integer getTargetStorageType() {
        return targetStorageType;
    }

    public void setTargetStorageType(Integer targetStorageType) {
        this.targetStorageType = targetStorageType;
    }

    public Integer getTargetRowStatus() {
        return targetRowStatus;
    }

    public void setTargetRowStatus(Integer targetRowStatus) {
        this.targetRowStatus = targetRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SnmpTargetTable [entityId=").append(entityId).append(", targetName=").append(targetName)
                .append(", targetDomain=").append(targetDomain).append(", targetDomainString=")
                .append(targetDomainString).append(", targetAddress=").append(targetAddress).append(", targetTimeout=")
                .append(targetTimeout).append(", targetRetryCount=").append(targetRetryCount)
                .append(", targetTagList=").append(targetTagList).append(", targetParams=").append(targetParams)
                .append(", targetStorageType=").append(targetStorageType).append(", targetRowStatus=")
                .append(targetRowStatus).append("]");
        return sb.toString();
    }

}
