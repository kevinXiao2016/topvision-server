/***********************************************************************
 * $Id: OnuInfo.java;v1.0 2015年4月23日 上午10:18:54 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;

/**
 * @author Administrator
 * @created @2015年4月23日-上午10:18:54
 *
 */
public class OnuInfo implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 6142156007195109043L;
    private Long onuId;
    private Long onuIndex;
    private Long entityId;
    private String entityIp;
    private String name;
    private String contact;
    private String location;
    private String note;
    private Integer onuType;
    private Integer onuPreType;
    private String typeName;
    private String onuMac;
    private Integer onuOperationStatus;
    private String onuSoftwareVersion;
    private String topOnuHardwareVersion;
    private String onuChipVendor;
    private Float onuPonRevPower;
    private Float onuPonTransPower;
    private Float oltponrevpower;
    private Integer onuIsolationEnable;
    private Integer temperatureDetectEnable;
    private Integer ponPerfStats15minuteEnable;
    private Long typeId;
    private String manageName;
    private String onuDesc;
    private Boolean attention;
    private Integer onuTestDistance;
    private String onuUniqueIdentification;
    private String onuEorG;
    private Integer onuLevel;

    private Long onuTimeSinceLastRegister;
    private Timestamp lastDeregisterTime;
    private Timestamp changeTime;
    private String onuRunTime;

    private Float minOnuPonRevPower;//最低光接收功率
    private String minPowerTimeStr;//最低光接收功率对应的时间（有多个，取24小时内的最新值）

    private Float onuCatvOrInfoRxPower;//CATV光接收功率
    private Float minCatvRevPower;//CATV最低光接收功率
    private String minCatvTimeStr;//最低光接收功率对应的时间（有多个，取24小时内的最新值）
    private Integer laserSwitch;//激光器开关

    private Integer topGponOnuCapOnuPotsNum;

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
    }

    /**
     * @return the onuEorG
     */
    public String getOnuEorG() {
        return onuEorG;
    }

    /**
     * @param onuEorG
     *            the onuEorG to set
     */
    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOnuType() {
        return onuType;
    }

    public void setOnuType(Integer onuType) {
        this.onuType = onuType;
    }

    public Integer getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(Integer onuPreType) {
        this.onuPreType = onuPreType;
    }

    public String getOnuMac() {
        return onuMac;
    }

    public void setOnuMac(String onuMac) {
        this.onuMac = onuMac;
    }

    public Integer getOnuOperationStatus() {
        return onuOperationStatus;
    }

    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
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

    public Integer getOnuIsolationEnable() {
        return onuIsolationEnable;
    }

    public void setOnuIsolationEnable(Integer onuIsolationEnable) {
        this.onuIsolationEnable = onuIsolationEnable;
    }

    public Integer getTemperatureDetectEnable() {
        return temperatureDetectEnable;
    }

    public void setTemperatureDetectEnable(Integer temperatureDetectEnable) {
        this.temperatureDetectEnable = temperatureDetectEnable;
    }

    public Integer getPonPerfStats15minuteEnable() {
        return ponPerfStats15minuteEnable;
    }

    public void setPonPerfStats15minuteEnable(Integer ponPerfStats15minuteEnable) {
        this.ponPerfStats15minuteEnable = ponPerfStats15minuteEnable;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public String getOnuDesc() {
        return onuDesc;
    }

    public void setOnuDesc(String onuDesc) {
        this.onuDesc = onuDesc;
    }

    public Boolean getAttention() {
        return attention;
    }

    public void setAttention(Boolean attention) {
        if (attention == null) {
            this.attention = false;
        } else {
            this.attention = true;
        }
    }

    public String getTopOnuHardwareVersion() {
        return topOnuHardwareVersion;
    }

    public void setTopOnuHardwareVersion(String topOnuHardwareVersion) {
        this.topOnuHardwareVersion = topOnuHardwareVersion;
    }

    public Integer getOnuTestDistance() {
        return onuTestDistance;
    }

    public void setOnuTestDistance(Integer onuTestDistance) {
        this.onuTestDistance = onuTestDistance;
    }

    public String getOnuUniqueIdentification() {
        return onuUniqueIdentification;
    }

    public void setOnuUniqueIdentification(String onuUniqueIdentification) {
        this.onuUniqueIdentification = onuUniqueIdentification;
    }

    public Long getOnuTimeSinceLastRegister() {
        return onuTimeSinceLastRegister;
    }

    public void setOnuTimeSinceLastRegister(Long onuTimeSinceLastRegister) {
        this.onuTimeSinceLastRegister = onuTimeSinceLastRegister;
    }

    public Timestamp getLastDeregisterTime() {
        return lastDeregisterTime;
    }

    public void setLastDeregisterTime(Timestamp lastDeregisterTime) {
        this.lastDeregisterTime = lastDeregisterTime;
    }

    public String getOnuRunTime() {
        return onuRunTime;
    }

    public void setOnuRunTime(String onuRunTime) {
        this.onuRunTime = onuRunTime;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getTopGponOnuCapOnuPotsNum() {
        return topGponOnuCapOnuPotsNum;
    }

    public void setTopGponOnuCapOnuPotsNum(Integer topGponOnuCapOnuPotsNum) {
        this.topGponOnuCapOnuPotsNum = topGponOnuCapOnuPotsNum;
    }

    public Integer getOnuLevel() {
        return onuLevel;
    }

    public void setOnuLevel(Integer onuLevel) {
        this.onuLevel = onuLevel;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuInfo [onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", name=");
        builder.append(name);
        builder.append(", contact=");
        builder.append(contact);
        builder.append(", location=");
        builder.append(location);
        builder.append(", note=");
        builder.append(note);
        builder.append(", onuType=");
        builder.append(onuType);
        builder.append(", onuPreType=");
        builder.append(onuPreType);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", onuMac=");
        builder.append(onuMac);
        builder.append(", onuOperationStatus=");
        builder.append(onuOperationStatus);
        builder.append(", onuSoftwareVersion=");
        builder.append(onuSoftwareVersion);
        builder.append(", topOnuHardwareVersion=");
        builder.append(topOnuHardwareVersion);
        builder.append(", onuChipVendor=");
        builder.append(onuChipVendor);
        builder.append(", onuPonRevPower=");
        builder.append(onuPonRevPower);
        builder.append(", onuPonTransPower=");
        builder.append(onuPonTransPower);
        builder.append(", oltponrevpower=");
        builder.append(oltponrevpower);
        builder.append(", onuIsolationEnable=");
        builder.append(onuIsolationEnable);
        builder.append(", temperatureDetectEnable=");
        builder.append(temperatureDetectEnable);
        builder.append(", ponPerfStats15minuteEnable=");
        builder.append(ponPerfStats15minuteEnable);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", manageName=");
        builder.append(manageName);
        builder.append(", onuDesc=");
        builder.append(onuDesc);
        builder.append(", attention=");
        builder.append(attention);
        builder.append(", onuTestDistance=");
        builder.append(onuTestDistance);
        builder.append(", onuUniqueIdentification=");
        builder.append(onuUniqueIdentification);
        builder.append(", onuEorG=");
        builder.append(onuEorG);
        builder.append(", onuTimeSinceLastRegister=");
        builder.append(onuTimeSinceLastRegister);
        builder.append(", lastDeregisterTime=");
        builder.append(lastDeregisterTime);
        builder.append(", changeTime=");
        builder.append(changeTime);
        builder.append(", onuRunTime=");
        builder.append(onuRunTime);
        builder.append(", topGponOnuCapOnuPotsNum=");
        builder.append(topGponOnuCapOnuPotsNum);
        builder.append("]");
        return builder.toString();
    }

    public Float getMinOnuPonRevPower() {
        return minOnuPonRevPower;
    }

    public void setMinOnuPonRevPower(Float minOnuPonRevPower) {
        this.minOnuPonRevPower = minOnuPonRevPower;
    }

    public Float getOnuCatvOrInfoRxPower() {
        return onuCatvOrInfoRxPower;
    }

    public void setOnuCatvOrInfoRxPower(Float onuCatvOrInfoRxPower) {
        this.onuCatvOrInfoRxPower = onuCatvOrInfoRxPower;
    }

    public Float getMinCatvRevPower() {
        return minCatvRevPower;
    }

    public void setMinCatvRevPower(Float minCatvRevPower) {
        this.minCatvRevPower = minCatvRevPower;
    }

    public String getMinPowerTimeStr() {
        return minPowerTimeStr;
    }

    public void setMinPowerTimeStr(String minPowerTimeStr) {
        this.minPowerTimeStr = minPowerTimeStr;
    }

    public String getMinCatvTimeStr() {
        return minCatvTimeStr;
    }

    public void setMinCatvTimeStr(String minCatvTimeStr) {
        this.minCatvTimeStr = minCatvTimeStr;
    }

    public Integer getLaserSwitch() {
        return laserSwitch;
    }

    public void setLaserSwitch(Integer laserSwitch) {
        this.laserSwitch = laserSwitch;
    }

} 
