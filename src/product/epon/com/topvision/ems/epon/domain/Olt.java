/***********************************************************************
 * $Id: Vertex.java,v1.0 2011-9-26 下午01:17:34 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.facade.domain.Entity;

/**
 * OLT关系
 * 
 * @author lizongtian
 * 
 */
public class Olt implements Serializable {
    private static final long serialVersionUID = -445471800563591334L;

    private Long entityId;
    private String oltName;
    private String oltType;
    private Long oltDeviceUpTime;
    private String ip;
    private String location;
    private String sysLocation;
    private String locationCnName;

    private String vendorName;
    private Integer topSysSnmpVersion;
    private Integer oltDeviceStyle;
    private Integer topSysOltRackNum;
    private Integer topSysOltFrameNum;
    private String systemOUI;
    private String sysDescr;
    private Integer oltDeviceNumOfTotalPowerSlot;
    private Integer oltDeviceNumOfTotalFanSlot;
    private Integer oltDeviceNumOfTotalServiceSlot;
    private List<Slot> slotList = new ArrayList<Slot>();
    private List<Fan> fanList = new ArrayList<Fan>();
    private List<Power> powerList = new ArrayList<Power>();
    private Integer onuAuthenticationPolicy;
    private Integer stpGlobalSetEnable;
    private Integer systemRogueCheck;

    public void setAttribute(Entity entity, OltAttribute oltAttribute) {
        Long tempTime = System.currentTimeMillis();
        this.entityId = entity.getEntityId();
        this.oltName = oltAttribute.getOltName();
        this.oltType = oltAttribute.getOltType();
        this.oltDeviceUpTime = tempTime - oltAttribute.getOltDeviceUpTime();
        this.ip = entity.getIp();
        this.location = entity.getLocation();
        this.sysLocation = entity.getSysLocation();
        this.vendorName = oltAttribute.getVendorName();
        this.topSysSnmpVersion = oltAttribute.getTopSysSnmpVersion();
        this.oltDeviceStyle = oltAttribute.getOltDeviceStyle();
        this.topSysOltRackNum = oltAttribute.getTopSysOltRackNum();
        this.topSysOltFrameNum = oltAttribute.getTopSysOltFrameNum();
        this.sysDescr = entity.getSysDescr();
        this.systemOUI = oltAttribute.getSystemOui();
        this.oltDeviceNumOfTotalFanSlot = oltAttribute.getOltDeviceNumOfTotalFanSlot();
        this.oltDeviceNumOfTotalPowerSlot = oltAttribute.getOltDeviceNumOfTotalPowerSlot();
        this.oltDeviceNumOfTotalServiceSlot = oltAttribute.getOltDeviceNumOfTotalServiceSlot();
        this.onuAuthenticationPolicy = oltAttribute.getOnuAuthenticationPolicy();
        this.systemRogueCheck = oltAttribute.getSystemRogueCheck();
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public List<Slot> getSlotList() {
        return slotList;
    }

    public void setSlotList(List<Slot> slotList) {
        this.slotList = slotList;
    }

    public String getOltType() {
        return oltType;
    }

    public void setOltType(String oltType) {
        this.oltType = oltType;
    }

    public Long getOltDeviceUpTime() {
        return oltDeviceUpTime;
    }

    public void setOltDeviceUpTime(Long oltDeviceUpTime) {
        this.oltDeviceUpTime = oltDeviceUpTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getTopSysSnmpVersion() {
        return topSysSnmpVersion;
    }

    public void setTopSysSnmpVersion(Integer topSysSnmpVersion) {
        this.topSysSnmpVersion = topSysSnmpVersion;
    }

    public Integer getOltDeviceStyle() {
        return oltDeviceStyle;
    }

    public void setOltDeviceStyle(Integer oltDeviceStyle) {
        this.oltDeviceStyle = oltDeviceStyle;
    }

    public Integer getTopSysOltRackNum() {
        return topSysOltRackNum;
    }

    public void setTopSysOltRackNum(Integer topSysOltRackNum) {
        this.topSysOltRackNum = topSysOltRackNum;
    }

    public Integer getTopSysOltFrameNum() {
        return topSysOltFrameNum;
    }

    public void setTopSysOltFrameNum(Integer topSysOltFrameNum) {
        this.topSysOltFrameNum = topSysOltFrameNum;
    }

    public String getSystemOUI() {
        return systemOUI;
    }

    public void setSystemOUI(String systemOUI) {
        this.systemOUI = systemOUI;
    }

    public String getSysDescr() {
        return sysDescr;
    }

    public void setSysDescr(String sysDescr) {
        this.sysDescr = sysDescr;
    }

    public Integer getOltDeviceNumOfTotalPowerSlot() {
        return oltDeviceNumOfTotalPowerSlot;
    }

    public void setOltDeviceNumOfTotalPowerSlot(Integer oltDeviceNumOfTotalPowerSlot) {
        this.oltDeviceNumOfTotalPowerSlot = oltDeviceNumOfTotalPowerSlot;
    }

    public Integer getOltDeviceNumOfTotalFanSlot() {
        return oltDeviceNumOfTotalFanSlot;
    }

    public void setOltDeviceNumOfTotalFanSlot(Integer oltDeviceNumOfTotalFanSlot) {
        this.oltDeviceNumOfTotalFanSlot = oltDeviceNumOfTotalFanSlot;
    }

    public Integer getOltDeviceNumOfTotalServiceSlot() {
        return oltDeviceNumOfTotalServiceSlot;
    }

    public void setOltDeviceNumOfTotalServiceSlot(Integer oltDeviceNumOfTotalServiceSlot) {
        this.oltDeviceNumOfTotalServiceSlot = oltDeviceNumOfTotalServiceSlot;
    }

    public List<Fan> getFanList() {
        return fanList;
    }

    public void setFanList(List<Fan> fanList) {
        this.fanList = fanList;
    }

    public List<Power> getPowerList() {
        return powerList;
    }

    public void setPowerList(List<Power> powerList) {
        this.powerList = powerList;
    }

    public Integer getOnuAuthenticationPolicy() {
        return onuAuthenticationPolicy;
    }

    public void setOnuAuthenticationPolicy(Integer onuAuthenticationPolicy) {
        this.onuAuthenticationPolicy = onuAuthenticationPolicy;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Olt");
        sb.append("{entityId=").append(entityId);
        sb.append(", oltName='").append(oltName).append('\'');
        sb.append(", oltType='").append(oltType).append('\'');
        sb.append(", oltDeviceUpTime='").append(oltDeviceUpTime).append('\'');
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", vendorName='").append(vendorName).append('\'');
        sb.append(", onuAuthenticationPolicy='").append(onuAuthenticationPolicy).append('\'');
        sb.append(", topSysSnmpVersion='").append(topSysSnmpVersion).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * @return the locationCnName
     */
    public String getLocationCnName() {
        return locationCnName;
    }

    /**
     * @param locationCnName
     *            the locationCnName to set
     */
    public void setLocationCnName(String locationCnName) {
        this.locationCnName = locationCnName;
    }

    public Integer getStpGlobalSetEnable() {
        return stpGlobalSetEnable;
    }

    public void setStpGlobalSetEnable(Integer stpGlobalSetEnable) {
        this.stpGlobalSetEnable = stpGlobalSetEnable;
    }

    public Integer getSystemRogueCheck() {
        return systemRogueCheck;
    }

    public void setSystemRogueCheck(Integer systemRogueCheck) {
        this.systemRogueCheck = systemRogueCheck;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

}
