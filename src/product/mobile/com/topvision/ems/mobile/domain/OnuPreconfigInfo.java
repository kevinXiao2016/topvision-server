package com.topvision.ems.mobile.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class OnuPreconfigInfo implements AliasesSuperType {
    private static final long serialVersionUID = -216896861184240527L;

    private String uniqueId;// 唯一标识，mac或者sn
    private String name; // 别名
    private Integer wanId;
    private String pppoeName;
    private String pppoePwd;
    private Integer ssid;
    private String wifiName;
    private String wifiPwd;
    private String contact;
    private String phoneNo;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long time;
    private Long modifyTime;
    private Boolean preconfig;
    private Boolean current;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWanId() {
        return wanId;
    }

    public void setWanId(Integer wanId) {
        this.wanId = wanId;
    }

    public String getPppoeName() {
        return pppoeName;
    }

    public void setPppoeName(String pppoeName) {
        this.pppoeName = pppoeName;
    }

    public String getPppoePwd() {
        return pppoePwd;
    }

    public void setPppoePwd(String pppoePwd) {
        this.pppoePwd = pppoePwd;
    }

    public Integer getSsid() {
        return ssid;
    }

    public void setSsid(Integer ssid) {
        this.ssid = ssid;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiPwd() {
        return wifiPwd;
    }

    public void setWifiPwd(String wifiPwd) {
        this.wifiPwd = wifiPwd;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getTime() {
        return time;
    }

    public Timestamp getTimeStamp() {
        return new Timestamp(time);
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public Timestamp getModifyTimeStamp() {
        return new Timestamp(modifyTime);
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Boolean getPreconfig() {
        return preconfig;
    }

    public void setPreconfig(Boolean preconfig) {
        this.preconfig = preconfig;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuPreconfigInfo [uniqueId=");
        builder.append(uniqueId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", wanId=");
        builder.append(wanId);
        builder.append(", pppoeName=");
        builder.append(pppoeName);
        builder.append(", pppoePwd=");
        builder.append(pppoePwd);
        builder.append(", ssid=");
        builder.append(ssid);
        builder.append(", wifiName=");
        builder.append(wifiName);
        builder.append(", wifiPwd=");
        builder.append(wifiPwd);
        builder.append(", contact=");
        builder.append(contact);
        builder.append(", phoneNo=");
        builder.append(phoneNo);
        builder.append(", address=");
        builder.append(address);
        builder.append(", latitude=");
        builder.append(latitude);
        builder.append(", longitude=");
        builder.append(longitude);
        builder.append(", time=");
        builder.append(time);
        builder.append(", modifyTime=");
        builder.append(modifyTime);
        builder.append(", preconfig=");
        builder.append(preconfig);
        builder.append(", current=");
        builder.append(current);
        builder.append("]");
        return builder.toString();
    }

}
