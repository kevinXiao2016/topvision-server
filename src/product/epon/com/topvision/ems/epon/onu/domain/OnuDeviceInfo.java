package com.topvision.ems.epon.onu.domain;

import java.sql.Timestamp;

/**
 * Onu设备列表对应domain
 * 
 * @author w1992wishes
 * @created @2017年12月21日-上午10:04:52
 *
 */
public class OnuDeviceInfo extends OnuCommonInfo {

    private static final long serialVersionUID = 9086953508968114483L;

    private Long onuTimeSinceLastRegister;
    private Timestamp lastDeregisterTime;
    private Timestamp changeTime;
    private String onuRunTime;// 在线时长
    private String onuDesc;// 描述
    private String onuPonPattern;// pon模式
    private String typeName;// ONU TYPE displayName
    private String onuSoftwareVersion;// 软件版本
    private String topOnuHardwareVersion;// 硬件版本
    private String onuChipVendor;// 芯片厂商
    private String contact;// 联系人
    private String location;// 位置
    private String note;// 备注
    private Integer onuDeactive;

    public Long getOnuTimeSinceLastRegister() {
        return onuTimeSinceLastRegister;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public String getOnuRunTime() {
        return onuRunTime;
    }

    public String getOnuDesc() {
        return onuDesc;
    }

    public String getOnuPonPattern() {
        return onuPonPattern;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getOnuSoftwareVersion() {
        return onuSoftwareVersion;
    }

    public String getTopOnuHardwareVersion() {
        return topOnuHardwareVersion;
    }

    public String getOnuChipVendor() {
        return onuChipVendor;
    }

    public String getContact() {
        return contact;
    }

    public String getLocation() {
        return location;
    }

    public String getNote() {
        return note;
    }

    public void setOnuTimeSinceLastRegister(Long onuTimeSinceLastRegister) {
        this.onuTimeSinceLastRegister = onuTimeSinceLastRegister;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public void setOnuRunTime(String onuRunTime) {
        this.onuRunTime = onuRunTime;
    }

    public void setOnuDesc(String onuDesc) {
        this.onuDesc = onuDesc;
    }

    public void setOnuPonPattern(String onuPonPattern) {
        this.onuPonPattern = onuPonPattern;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setOnuSoftwareVersion(String onuSoftwareVersion) {
        this.onuSoftwareVersion = onuSoftwareVersion;
    }

    public void setTopOnuHardwareVersion(String topOnuHardwareVersion) {
        this.topOnuHardwareVersion = topOnuHardwareVersion;
    }

    public void setOnuChipVendor(String onuChipVendor) {
        this.onuChipVendor = onuChipVendor;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getLastDeregisterTime() {
        return lastDeregisterTime;
    }

    public void setLastDeregisterTime(Timestamp lastDeregisterTime) {
        this.lastDeregisterTime = lastDeregisterTime;
    }

    public Integer getOnuDeactive() {
        return onuDeactive;
    }

    public void setOnuDeactive(Integer onuDeactive) {
        this.onuDeactive = onuDeactive;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuDeviceInfo [onuTimeSinceLastRegister=");
        builder.append(onuTimeSinceLastRegister);
        builder.append(", lastDeregisterTime=");
        builder.append(lastDeregisterTime);
        builder.append(", changeTime=");
        builder.append(changeTime);
        builder.append(", onuRunTime=");
        builder.append(onuRunTime);
        builder.append(", onuDesc=");
        builder.append(onuDesc);
        builder.append(", onuPonPattern=");
        builder.append(onuPonPattern);
        builder.append(", typeName=");
        builder.append(typeName);
        builder.append(", onuSoftwareVersion=");
        builder.append(onuSoftwareVersion);
        builder.append(", topOnuHardwareVersion=");
        builder.append(topOnuHardwareVersion);
        builder.append(", onuChipVendor=");
        builder.append(onuChipVendor);
        builder.append(", contact=");
        builder.append(contact);
        builder.append(", location=");
        builder.append(location);
        builder.append(", note=");
        builder.append(note);
        builder.append(", onuDeactive=");
        builder.append(onuDeactive);
        builder.append("]");
        return builder.toString();
    }

}
