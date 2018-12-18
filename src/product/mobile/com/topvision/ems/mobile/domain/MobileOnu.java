/***********************************************************************
 * $Id: MobileOnu.java,v1.0 2016年7月18日 下午1:23:06 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.domain;

import java.sql.Timestamp;

import com.topvision.ems.mobile.util.MobileUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2016年7月18日-下午1:23:06
 *
 */
public class MobileOnu implements AliasesSuperType {
    private static final long serialVersionUID = -2616174079852934588L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    // 别名
    private String name;
    // mac
    private String mac;
    // 状态
    private Integer state;
    // 类型ID
    private Long typeId;
    // 类型名称
    private String onuType;
    // 管理IP
    private String manageIp;
    // 在线时间
    private Long sysUpTime;
    private String sysUpTimeString;
    // 设备位置
    private String location;
    // 芯片厂商
    private String chipVendor;
    // 芯片类型
    private String chipType;
    // 软件版本
    private String softVersion;
    // 硬件版本
    private String hardVersion;
    // LLID
    private Integer onuLlid;
    private String onuLlidStr;
    private Long onuTimeSinceLastRegister;
    private Timestamp changeTime;
    private String onuLocation;
    
    private String gponSN;
    
    private String eorG;
    private Integer adminStatus;
    private Integer onuTestDistance; 

    public String getSysUpTimeString() {
        return sysUpTimeString;
    }

    public void setSysUpTimeString(String sysUpTimeString) {
        this.sysUpTimeString = sysUpTimeString;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getOnuType() {
        return onuType;
    }

    public void setOnuType(String onuType) {
        this.onuType = onuType;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getChipVendor() {
        return chipVendor;
    }

    public void setChipVendor(String chipVendor) {
        if (chipVendor != null) {
            this.chipVendor = MobileUtil.convert2Bit(chipVendor);
        } else {
            this.chipVendor = chipVendor;
        }
    }

    public String getChipType() {
        return chipType;
    }

    public void setChipType(String chipType) {
        if (chipType != null) {
            this.chipType = MobileUtil.convert2Bit(chipType);
        } else {
            this.chipType = chipType;
        }
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getOnuLlid() {
        return onuLlid;
    }

    public void setOnuLlid(Integer onuLlid) {
        this.onuLlid = onuLlid;
    }

    public String getOnuLlidStr() {
        return onuLlidStr;
    }

    public void setOnuLlidStr(String onuLlidStr) {
        this.onuLlidStr = onuLlidStr;
    }

    public String getHardVersion() {
        return hardVersion;
    }

    public void setHardVersion(String hardVersion) {
        this.hardVersion = hardVersion;
    }

    public Long getOnuTimeSinceLastRegister() {
        return onuTimeSinceLastRegister;
    }

    public void setOnuTimeSinceLastRegister(Long onuTimeSinceLastRegister) {
        this.onuTimeSinceLastRegister = onuTimeSinceLastRegister;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MobileOnu [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", state=");
        builder.append(state);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", onuType=");
        builder.append(onuType);
        builder.append(", manageIp=");
        builder.append(manageIp);
        builder.append(", sysUpTime=");
        builder.append(sysUpTime);
        builder.append(", location=");
        builder.append(location);
        builder.append(", chipVendor=");
        builder.append(chipVendor);
        builder.append(", chipType=");
        builder.append(chipType);
        builder.append(", softVersion=");
        builder.append(softVersion);
        builder.append(", onuLlid=");
        builder.append(onuLlid);
        builder.append("]");
        return builder.toString();
    }

    public String getOnuLocation() {
        return onuLocation;
    }

    public void setOnuLocation(String onuLocation) {
        this.onuLocation = onuLocation;
    }

    public String getGponSN() {
        return gponSN;
    }

    public void setGponSN(String gponSN) {
        this.gponSN = gponSN;
    }

    public String getEorG() {
        return eorG;
    }

    public void setEorG(String eorG) {
        this.eorG = eorG;
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    public Integer getOnuTestDistance() {
        return onuTestDistance;
    }

    public void setOnuTestDistance(Integer onuTestDistance) {
        this.onuTestDistance = onuTestDistance;
    }

}
