/***********************************************************************
 * $Id: Gpon.java,v1.0 2016年12月19日 上午10:40:45 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.domain;

import java.sql.Timestamp;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.gpon.utils.GponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Bravin
 * @created @2016年12月19日-上午10:40:45
 *
 */
public class GponOnuAutoFind implements AliasesSuperType {
    private static final long serialVersionUID = 7823604738268212271L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Long entityId;
    private Long ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.1", index = true, type = "Integer32")
    private Integer onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.2", type = "OctetString", writable = false)
    private String onuType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.3", isHex = true, writable = false)
    private String serialNumber;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.4", type = "OctetString", writable = false)
    private String password;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.5", type = "OctetString", writable = false)
    private String loid;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.6", type = "OctetString", writable = false)
    private String loidPassword;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.7", type = "DateAndTime", writable = false)
    private String autoFindTimeStr;
    private Long autoFindTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.8", type = "OctetString", writable = false)
    private String softwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.7.1.9", type = "OctetString", writable = false)
    private String hardwareVersion;
    private String entityName;
    private Integer ponOnuAuthenMode;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getLocation() {
        return GponIndex.getSlotNoFromMibIndex(onuIndex.longValue()) + "/"
                + GponIndex.getPonNoFromMibIndex(onuIndex.longValue()) + ":"
                + GponIndex.getOnuNoFromMibIndex(onuIndex.longValue());
    }

    public String getFindTime() {
        try {
            return DateUtils.parseToString(new Timestamp(autoFindTime));
        } catch (ParseException e) {
            logger.error("", e);
        }
        return null;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Integer onuIndex) {
        this.onuIndex = onuIndex;
        this.ponIndex = GponIndex.getPonIndexFromMibIndex(onuIndex);
    }

    public String getOnuType() {
        return onuType;
    }

    public void setOnuType(String onuType) {
        this.onuType = onuType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoid() {
        return loid;
    }

    public void setLoid(String loid) {
        this.loid = loid;
    }

    public String getLoidPassword() {
        return loidPassword;
    }

    public void setLoidPassword(String loidPassword) {
        this.loidPassword = loidPassword;
    }

    public Long getAutoFindTime() {
        return autoFindTime;
    }

    public void setAutoFindTime(Long autoFindTime) {
        this.autoFindTime = autoFindTime;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public String getAutoFindTimeStr() {
        return autoFindTimeStr;
    }

    public void setAutoFindTimeStr(String autoFindTimeStr) {
        this.autoFindTimeStr = autoFindTimeStr;
        this.autoFindTime = Long.parseLong(autoFindTimeStr);
    }

    @Override
    public String toString() {
        return "GponOnuAutoFind [entityId=" + entityId + ", ponIndex=" + ponIndex + ", onuIndex=" + onuIndex
                + ", onuType=" + onuType + ", serialNumber=" + serialNumber + ", password=" + password + ", loid="
                + loid + ", loidPassword=" + loidPassword + ", autoFindTime=" + autoFindTime + ", softwareVersion="
                + softwareVersion + ", hardwareVersion=" + hardwareVersion + "]";
    }

    public Integer getPonOnuAuthenMode() {
        return ponOnuAuthenMode;
    }

    public void setPonOnuAuthenMode(Integer ponOnuAuthenMode) {
        this.ponOnuAuthenMode = ponOnuAuthenMode;
    }

}
