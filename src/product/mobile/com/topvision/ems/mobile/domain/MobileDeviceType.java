package com.topvision.ems.mobile.domain;

public class MobileDeviceType {
    private Long typeId;
    private String deviceType;
    private String corporation;
    private String frequency;
    private String powerlevel;
    private Boolean isDefault;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPowerlevel() {
        return powerlevel;
    }

    public void setPowerlevel(String powerlevel) {
        this.powerlevel = powerlevel;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

}
