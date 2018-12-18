/***********************************************************************
 * $Id: OnuBaseInfo.java,v1.0 2014-10-14 下午3:43:20 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2014-10-14-下午3:43:20
 *
 */
public class OnuBaseInfo implements AliasesSuperType {
    private static final long serialVersionUID = 8080999841856694771L;

    private Long entityId;
    private String entityIp;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.1", index = true)
    private Long onuMibIndex;
    // onuName由网管侧维护
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.2", writable = true, type = "OctetString")
    private String onuName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.7")
    private String onuMac;
    private Long onuMacAddress;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.8")
    private Integer onuOperationStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.9", writable = true, type = "Integer32")
    private Integer onuAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.10")
    private String onuChipVendor;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.11")
    private String onuChipType;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.12")
    private String onuChipVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.13")
    private String onuSoftwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.15")
    private Integer onuTestDistance;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.16")
    private Integer onuLlidId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.18")
    private Long onuTimeSinceLastRegister;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public String getOnuMac() {
        return onuMac;
    }

    public void setOnuMac(String onuMac) {
        this.onuMac = onuMac;
        this.onuMacAddress = new MacUtils(this.onuMac).longValue();
    }

    public Long getOnuMacAddress() {
        return onuMacAddress;
    }

    public void setOnuMacAddress(Long onuMacAddress) {
        this.onuMacAddress = onuMacAddress;
    }

    public Integer getOnuOperationStatus() {
        return onuOperationStatus;
    }

    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    public Integer getOnuAdminStatus() {
        return onuAdminStatus;
    }

    public void setOnuAdminStatus(Integer onuAdminStatus) {
        this.onuAdminStatus = onuAdminStatus;
    }

    public String getOnuChipVendor() {
        return onuChipVendor;
    }

    public void setOnuChipVendor(String onuChipVendor) {
        this.onuChipVendor = onuChipVendor;
    }

    public String getOnuChipType() {
        return onuChipType;
    }

    public void setOnuChipType(String onuChipType) {
        this.onuChipType = onuChipType;
    }

    public String getOnuChipVersion() {
        return onuChipVersion;
    }

    public void setOnuChipVersion(String onuChipVersion) {
        this.onuChipVersion = onuChipVersion;
    }

    public String getOnuSoftwareVersion() {
        return onuSoftwareVersion;
    }

    public void setOnuSoftwareVersion(String onuSoftwareVersion) {
        this.onuSoftwareVersion = onuSoftwareVersion;
    }

    public Integer getOnuTestDistance() {
        return onuTestDistance;
    }

    public void setOnuTestDistance(Integer onuTestDistance) {
        this.onuTestDistance = onuTestDistance;
    }

    public Integer getOnuLlidId() {
        return onuLlidId;
    }

    public void setOnuLlidId(Integer onuLlidId) {
        this.onuLlidId = onuLlidId;
    }

    public Long getOnuTimeSinceLastRegister() {
        return onuTimeSinceLastRegister;
    }

    public void setOnuTimeSinceLastRegister(Long onuTimeSinceLastRegister) {
        this.onuTimeSinceLastRegister = onuTimeSinceLastRegister;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuBaseInfo [entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuMibIndex=");
        builder.append(onuMibIndex);
        builder.append(", onuName=");
        builder.append(onuName);
        builder.append(", onuMac=");
        builder.append(onuMac);
        builder.append(", onuMacAddress=");
        builder.append(onuMacAddress);
        builder.append(", onuOperationStatus=");
        builder.append(onuOperationStatus);
        builder.append(", onuAdminStatus=");
        builder.append(onuAdminStatus);
        builder.append(", onuChipVendor=");
        builder.append(onuChipVendor);
        builder.append(", onuChipType=");
        builder.append(onuChipType);
        builder.append(", onuChipVersion=");
        builder.append(onuChipVersion);
        builder.append(", onuSoftwareVersion=");
        builder.append(onuSoftwareVersion);
        builder.append(", onuTestDistance=");
        builder.append(onuTestDistance);
        builder.append(", onuLlidId=");
        builder.append(onuLlidId);
        builder.append("]");
        return builder.toString();
    }

}

