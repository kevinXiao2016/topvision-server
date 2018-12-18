/***********************************************************************
 * $Id: MobileOnu.java,v1.0 2016年7月18日 下午1:23:06 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;

/**
 * @author Administrator
 * @created @2015年4月23日-上午10:18:54
 *
 */
public class MUIOnu implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 6142156007195109043L;
    // realTime info
    private Long entityId;
    private Long onuId;
    private String ip;
    private String mac;
    private String sn;
    private String uniqueId;
    private String topOnuHardwareVersion;
    private String onuSoftwareVersion;
    private String onuChipVendor;
    private Float onuPonRevPower;
    private Float onuPonTransPower;
    private Float oltponrevpower;
    private Integer wanId;
    private Integer wanMode;
    private Integer wanStatus;
    private Integer wanErrorCode;
    private Integer ssid;
    private String ssidName;
    private Integer encryptMode;
    private String password;
    private String EorG;
    // 光机信息
    private Float onuCatvOrInfoRxPower;
    private Float onuCatvOrInfoRfOutVoltage;
    private Float onuCatvOrInfoVoltage;
    private Float onuCatvOrInfoTemperature;
    private String topOnuExtAttr;
    private Integer catvCap;
    private String onuTotalCatvNum;
    // NM3000 info
    // 设备名称
    private String name;
    private String location;
    private String contact;
    private Integer onuOperationStatus;
    // 启动时长，在线时间
    private String onuRunTime;
    private String onlineTime;
    private Long onuTimeSinceLastRegister;
    private Timestamp LastDeregisterTime;
    private Timestamp changeTime;
    private Integer onuTestDistance;
    private String oltName;
    private String oltIp;
    private String oltLocation;

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

    public Integer getEncryptMode() {
        return encryptMode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTopOnuHardwareVersion() {
        return topOnuHardwareVersion;
    }

    public void setTopOnuHardwareVersion(String topOnuHardwareVersion) {
        this.topOnuHardwareVersion = topOnuHardwareVersion;
    }

    public String getOnuSoftwareVersion() {
        return onuSoftwareVersion;
    }

    public void setOnuSoftwareVersion(String onuSoftwareVersion) {
        this.onuSoftwareVersion = onuSoftwareVersion;
    }

    public String getOnuChipVendor() {
        return onuChipVendor;
    }

    public void setOnuChipVendor(String onuChipVendor) {
        this.onuChipVendor = onuChipVendor;
    }

    public Float getOnuPonRevPower() {
        return onuPonRevPower;
    }

    public void setOnuPonRevPower(Float onuPonRevPower) {
        if (onuPonRevPower != null && !onuPonRevPower.equals(EponConstants.RE_POWER)) {
            this.onuPonRevPower = onuPonRevPower;
        } else {
            this.onuPonRevPower = null;
        }
    }

    public Float getOnuPonTransPower() {
        return onuPonTransPower;
    }

    public void setOnuPonTransPower(Float onuPonTransPower) {
        if (onuPonTransPower != null && !onuPonTransPower.equals(EponConstants.TX_POWER)) {
            this.onuPonTransPower = onuPonTransPower;
        } else {
            this.onuPonTransPower = null;
        }
    }

    public Float getOltponrevpower() {
        return oltponrevpower;
    }

    public void setOltponrevpower(Float oltponrevpower) {
        if (oltponrevpower != null && !oltponrevpower.equals(EponConstants.RE_POWER)) {
            this.oltponrevpower = oltponrevpower;
        } else {
            this.oltponrevpower = null;
        }

    }

    public Integer getWanId() {
        return wanId;
    }

    public void setWanId(Integer wanId) {
        this.wanId = wanId;
    }

    public Integer getWanMode() {
        return wanMode;
    }

    public void setWanMode(Integer wanMode) {
        this.wanMode = wanMode;
    }

    public Integer getWanStatus() {
        return wanStatus;
    }

    public void setWanStatus(Integer wanStatus) {
        this.wanStatus = wanStatus;
    }

    public Integer getWanErrorCode() {
        return wanErrorCode;
    }

    public void setWanErrorCode(Integer wanErrorCode) {
        this.wanErrorCode = wanErrorCode;
    }

    public void setEncryptMode(Integer encryptMode) {
        this.encryptMode = encryptMode;
    }

    public Integer getSsid() {
        return ssid;
    }

    public void setSsid(Integer ssid) {
        this.ssid = ssid;
    }

    public String getSsidName() {
        return ssidName;
    }

    public void setSsidName(String ssidName) {
        this.ssidName = ssidName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getOnuOperationStatus() {
        return onuOperationStatus;
    }

    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    public String getOnuRunTime() {
        return onuRunTime;
    }

    public void setOnuRunTime(String onuRunTime) {
        this.onuRunTime = onuRunTime;
    }

    public Timestamp getLastDeregisterTime() {
        return LastDeregisterTime;
    }

    public void setLastDeregisterTime(Timestamp lastDeregisterTime) {
        LastDeregisterTime = lastDeregisterTime;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
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

    public Integer getOnuTestDistance() {
        return onuTestDistance;
    }

    public void setOnuTestDistance(Integer onuTestDistance) {
        this.onuTestDistance = onuTestDistance;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public String getOltIp() {
        return oltIp;
    }

    public void setOltIp(String oltIp) {
        this.oltIp = oltIp;
    }

    public String getOltLocation() {
        return oltLocation;
    }

    public void setOltLocation(String oltLocation) {
        this.oltLocation = oltLocation;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Float getOnuCatvOrInfoRxPower() {
        return onuCatvOrInfoRxPower;
    }

    public void setOnuCatvOrInfoRxPower(Float onuCatvOrInfoRxPower) {
        this.onuCatvOrInfoRxPower = onuCatvOrInfoRxPower;
    }

    public Float getOnuCatvOrInfoRfOutVoltage() {
        return onuCatvOrInfoRfOutVoltage;
    }

    public void setOnuCatvOrInfoRfOutVoltage(Float onuCatvOrInfoRfOutVoltage) {
        this.onuCatvOrInfoRfOutVoltage = onuCatvOrInfoRfOutVoltage;
    }

    public Float getOnuCatvOrInfoVoltage() {
        return onuCatvOrInfoVoltage;
    }

    public void setOnuCatvOrInfoVoltage(Float onuCatvOrInfoVoltage) {
        this.onuCatvOrInfoVoltage = onuCatvOrInfoVoltage;
    }

    public Float getOnuCatvOrInfoTemperature() {
        return onuCatvOrInfoTemperature;
    }

    public void setOnuCatvOrInfoTemperature(Float onuCatvOrInfoTemperature) {
        this.onuCatvOrInfoTemperature = onuCatvOrInfoTemperature;
    }

    public String getTopOnuExtAttr() {
        return topOnuExtAttr;
    }

    public void setTopOnuExtAttr(String topOnuExtAttr) {
        this.topOnuExtAttr = topOnuExtAttr;
        if (topOnuExtAttr != null) {
            catvCap = EponUtil.getCatvCapability(topOnuExtAttr);
        } else {
            catvCap = 0;
        }
    }

    public Integer getCatvCap() {
        return catvCap;
    }

    public void setCatvCap(Integer catvCap) {
        this.catvCap = catvCap;
    }

    public String getOnuTotalCatvNum() {
        return onuTotalCatvNum;
    }

    public void setOnuTotalCatvNum(String onuTotalCatvNum) {
        this.onuTotalCatvNum = onuTotalCatvNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MUIOnu [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", sn=");
        builder.append(sn);
        builder.append(", uniqueId=");
        builder.append(uniqueId);
        builder.append(", topOnuHardwareVersion=");
        builder.append(topOnuHardwareVersion);
        builder.append(", onuSoftwareVersion=");
        builder.append(onuSoftwareVersion);
        builder.append(", onuChipVendor=");
        builder.append(onuChipVendor);
        builder.append(", onuPonRevPower=");
        builder.append(onuPonRevPower);
        builder.append(", onuPonTransPower=");
        builder.append(onuPonTransPower);
        builder.append(", oltponrevpower=");
        builder.append(oltponrevpower);
        builder.append(", wanId=");
        builder.append(wanId);
        builder.append(", wanMode=");
        builder.append(wanMode);
        builder.append(", wanStatus=");
        builder.append(wanStatus);
        builder.append(", wanErrorCode=");
        builder.append(wanErrorCode);
        builder.append(", ssid=");
        builder.append(ssid);
        builder.append(", ssidName=");
        builder.append(ssidName);
        builder.append(", encryptMode=");
        builder.append(encryptMode);
        builder.append(", password=");
        builder.append(password);
        builder.append(", onuCatvOrInfoRxPower=");
        builder.append(onuCatvOrInfoRxPower);
        builder.append(", onuCatvOrInfoRfOutVoltage=");
        builder.append(onuCatvOrInfoRfOutVoltage);
        builder.append(", onuCatvOrInfoVoltage=");
        builder.append(onuCatvOrInfoVoltage);
        builder.append(", onuCatvOrInfoTemperature=");
        builder.append(onuCatvOrInfoTemperature);
        builder.append(", topOnuExtAttr=");
        builder.append(topOnuExtAttr);
        builder.append(", catvCap=");
        builder.append(catvCap);
        builder.append(", name=");
        builder.append(name);
        builder.append(", location=");
        builder.append(location);
        builder.append(", contact=");
        builder.append(contact);
        builder.append(", onuOperationStatus=");
        builder.append(onuOperationStatus);
        builder.append(", onuRunTime=");
        builder.append(onuRunTime);
        builder.append(", onlineTime=");
        builder.append(onlineTime);
        builder.append(", onuTimeSinceLastRegister=");
        builder.append(onuTimeSinceLastRegister);
        builder.append(", LastDeregisterTime=");
        builder.append(LastDeregisterTime);
        builder.append(", changeTime=");
        builder.append(changeTime);
        builder.append(", onuTestDistance=");
        builder.append(onuTestDistance);
        builder.append(", oltName=");
        builder.append(oltName);
        builder.append(", oltIp=");
        builder.append(oltIp);
        builder.append(", oltLocation=");
        builder.append(oltLocation);
        builder.append("]");
        return builder.toString();
    }

    public String getEorG() {
        return EorG;
    }

    public void setEorG(String eorG) {
        EorG = eorG;
    }

}
