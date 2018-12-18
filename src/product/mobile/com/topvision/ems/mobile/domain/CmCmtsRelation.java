package com.topvision.ems.mobile.domain;

public class CmCmtsRelation {
    private Long cmId;
    private Long cmtsId;
    private Long cmStatusIndex; // CM的StatusIndex
    private String cmMac; // CM的MAC
    private Long cmtsDeviceStyle; // CM上联CMTS的DeviceStyle

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
    }

    public Long getCmStatusIndex() {
        return cmStatusIndex;
    }

    public void setCmStatusIndex(Long cmStatusIndex) {
        this.cmStatusIndex = cmStatusIndex;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public Long getCmtsDeviceStyle() {
        return cmtsDeviceStyle;
    }

    public void setCmtsDeviceStyle(Long cmtsDeviceStyle) {
        this.cmtsDeviceStyle = cmtsDeviceStyle;
    }

}
