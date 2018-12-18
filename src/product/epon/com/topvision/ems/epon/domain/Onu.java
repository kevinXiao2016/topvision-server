/***********************************************************************
 * $Id: Onu.java,v1.0 2011-9-27 下午04:24:41 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OnuLinkInfo;
import com.topvision.ems.epon.onu.domain.OnuWanConnect;
import com.topvision.ems.epon.onu.domain.OnuWanSsid;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.portinfo.domain.OltPortOpticalInfo;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author loyal
 * @created @2011-9-27-下午04:24:41
 * 
 */
public class Onu implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 89409824616232213L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private String onuName;
    private Integer onuType;
    private String onuPreType;
    private String typeName;
    private String onuMac;
    private Long onuMacAddress;
    private Integer onuOperationStatus;
    private Integer onuAdminStatus;
    private String onuChipVendor;
    private String onuChipType;
    private String onuChipVersion;
    private String onuSoftwareVersion;
    private String onuFirmwareVersion;
    private Long onuTestDistance;
    private Long onuLlidId;
    private Long onuTimeSinceLastRegister;
    private Integer temperatureDetectEnable;
    private Integer topOnuCurrentTemperature;
    private Integer onuFecEnable;
    private Integer ponPerfStats15minuteEnable;
    private Integer ponPerfStats24hourEnable;
    private OnuPonPort onuPonPort;
    private String onuIcon;
    private List<OnuUniPort> onuUniPortList;
    private Timestamp changeTime;
    private Integer onuIsolationEnable;
    private Integer topOnuAction;
    private String topOnuHardwareVersion;
    private List<OltOnuComAttribute> onuElecComList;
    private OltPonOptical oltPonOptical;
    private String sysLocation;
    private String sysName;
    // 表示ONU下连的CMC设备
    private Entity cmcEntity;
    private Long typeId;
    private Timestamp lastDeregisterTime;
    private String onuRunTime;

    private String onuUniqueIdentification;
    private String onuEorG;
    // onu温度展示
    private Integer onuDisplayTemp;
    private Integer laserSwitch;
    private List<OnuWanConnect> onuWanConnectList;
    private List<OnuWanSsid> onuWanSsidList;
    private List<UniPort> eponUniPorts;
    private List<GponUniAttribute> gponUniPorts;
    private Integer cpeNum;
    private String location;
    private String topOnuExtAttr;// 用于判断ONU是否有CATV，WIFI能力
    private Integer catvCapability;// catv能力
    private Integer wlanCapability;// wifi能力
    private OnuLinkInfo onuLinkInfo;
    private OltPortOpticalInfo oltPortOpticalInfo;
    private SubDeviceCount subCount;
    protected Boolean attention;// 是否关注

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId
     *            the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuName
     */
    public String getOnuName() {
        return onuName;
    }

    /**
     * @param onuName
     *            the onuName to set
     */
    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    /**
     * @return the onuType
     */
    public Integer getOnuType() {
        return onuType;
    }

    /**
     * @param onuType
     *            the onuType to set
     */
    public void setOnuType(Integer onuType) {
        this.onuType = onuType;
    }

    /**
     * @return the onuMacAddress
     */
    public Long getOnuMacAddress() {
        return onuMacAddress;
    }

    /**
     * @param onuMacAddress
     *            the onuMacAddress to set
     */
    public void setOnuMacAddress(Long onuMacAddress) {
        this.onuMacAddress = onuMacAddress;
        this.onuMac = new MacUtils(onuMacAddress).toString(MacUtils.MAOHAO).toUpperCase();
    }

    /**
     * @return the onuOperationStatus
     */
    public int getOnuOperationStatus() {
        return onuOperationStatus;
    }

    /**
     * @param onuOperationStatus
     *            the onuOperationStatus to set
     */
    public void setOnuOperationStatus(int onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    /**
     * @return the onuChipVendor
     */
    public String getOnuChipVendor() {
        return onuChipVendor;
    }

    /**
     * @param onuChipVendor
     *            the onuChipVendor to set
     */
    public void setOnuChipVendor(String onuChipVendor) {
        this.onuChipVendor = onuChipVendor;
    }

    /**
     * @return the onuChipType
     */
    public String getOnuChipType() {
        return onuChipType;
    }

    /**
     * @param onuChipType
     *            the onuChipType to set
     */
    public void setOnuChipType(String onuChipType) {
        this.onuChipType = onuChipType;
    }

    /**
     * @return the onuChipVersion
     */
    public String getOnuChipVersion() {
        return onuChipVersion;
    }

    /**
     * @param onuChipVersion
     *            the onuChipVersion to set
     */
    public void setOnuChipVersion(String onuChipVersion) {
        this.onuChipVersion = onuChipVersion;
    }

    /**
     * @return the onuSoftwareVersion
     */
    public String getOnuSoftwareVersion() {
        return onuSoftwareVersion;
    }

    /**
     * @param onuSoftwareVersion
     *            the onuSoftwareVersion to set
     */
    public void setOnuSoftwareVersion(String onuSoftwareVersion) {
        this.onuSoftwareVersion = onuSoftwareVersion;
    }

    /**
     * @return the onuFirmwareVersion
     */
    public String getOnuFirmwareVersion() {
        return onuFirmwareVersion;
    }

    /**
     * @param onuFirmwareVersion
     *            the onuFirmwareVersion to set
     */
    public void setOnuFirmwareVersion(String onuFirmwareVersion) {
        this.onuFirmwareVersion = onuFirmwareVersion;
    }

    /**
     * @return the onuTestDistance
     */
    public Long getOnuTestDistance() {
        return onuTestDistance;
    }

    /**
     * @param onuTestDistance
     *            the onuTestDistance to set
     */
    public void setOnuTestDistance(Long onuTestDistance) {
        this.onuTestDistance = onuTestDistance;
    }

    /**
     * @return the onuLlidId
     */
    public Long getOnuLlidId() {
        return onuLlidId;
    }

    /**
     * @param onuLlidId
     *            the onuLlidId to set
     */
    public void setOnuLlidId(Long onuLlidId) {
        this.onuLlidId = onuLlidId;
    }

    /**
     * @return the onuTimeSinceLastRegister
     */
    public Long getOnuTimeSinceLastRegister() {
        return onuTimeSinceLastRegister;
    }

    /**
     * @param onuTimeSinceLastRegister
     *            the onuTimeSinceLastRegister to set
     */
    public void setOnuTimeSinceLastRegister(Long onuTimeSinceLastRegister) {
        this.onuTimeSinceLastRegister = onuTimeSinceLastRegister;
    }

    /**
     * @return the onuPonPort
     */
    public OnuPonPort getOnuPonPort() {
        return onuPonPort;
    }

    /**
     * @param onuPonPort
     *            the onuPonPort to set
     */
    public void setOnuPonPort(OnuPonPort onuPonPort) {
        this.onuPonPort = onuPonPort;
    }

    /**
     * @return the onuUniPortList
     */
    public List<OnuUniPort> getOnuUniPortList() {
        return onuUniPortList;
    }

    /**
     * @param uniPortList
     *            the onuUniPortList to set
     */
    public void setOnuUniPortList(List<OnuUniPort> uniPortList) {
        onuUniPortList = uniPortList;
    }

    public String getOnuMac() {
        return onuMac;
    }

    public void setOnuMac(String onuMac) {
        this.onuMac = onuMac;
        this.onuMacAddress = new MacUtils(onuMac).longValue();
    }

    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    public void setOnuAdminStatus(Integer onuAdminStatus) {
        this.onuAdminStatus = onuAdminStatus;
    }

    public Integer getOnuAdminStatus() {
        return onuAdminStatus;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getTemperatureDetectEnable() {
        return temperatureDetectEnable;
    }

    public void setTemperatureDetectEnable(Integer temperatureDetectEnable) {
        this.temperatureDetectEnable = temperatureDetectEnable;
    }

    public Integer getTopOnuCurrentTemperature() {
        return topOnuCurrentTemperature;
    }

    public void setTopOnuCurrentTemperature(Integer topOnuCurrentTemperature) {
        this.topOnuCurrentTemperature = topOnuCurrentTemperature;
        if (topOnuCurrentTemperature != null) {
            this.onuDisplayTemp = UnitConfigConstant.translateTemperature(topOnuCurrentTemperature);
        }
    }

    /**
     * @return the onuFecEnable
     */
    public Integer getOnuFecEnable() {
        return onuFecEnable;
    }

    /**
     * @param onuFecEnable
     *            the onuFecEnable to set
     */
    public void setOnuFecEnable(Integer onuFecEnable) {
        this.onuFecEnable = onuFecEnable;
    }

    public Integer getPonPerfStats15minuteEnable() {
        return ponPerfStats15minuteEnable;
    }

    public void setPonPerfStats15minuteEnable(Integer ponPerfStats15minuteEnable) {
        this.ponPerfStats15minuteEnable = ponPerfStats15minuteEnable;
    }

    public Integer getPonPerfStats24hourEnable() {
        return ponPerfStats24hourEnable;
    }

    public void setPonPerfStats24hourEnable(Integer ponPerfStats24hourEnable) {
        this.ponPerfStats24hourEnable = ponPerfStats24hourEnable;
    }

    /**
     * @return the onuIcon
     */
    public String getOnuIcon() {
        return onuIcon;
    }

    /**
     * @param onuIcon
     *            the onuIcon to set
     */
    public void setOnuIcon(String onuIcon) {
        this.onuIcon = onuIcon;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getOnuIsolationEnable() {
        return onuIsolationEnable;
    }

    public void setOnuIsolationEnable(Integer onuIsolationEnable) {
        this.onuIsolationEnable = onuIsolationEnable;
    }

    /**
     * @return the onuPreType
     */
    public String getOnuPreType() {
        return onuPreType;
    }

    /**
     * @param onuPreType
     *            the onuPreType to set
     */
    public void setOnuPreType(String onuPreType) {
        this.onuPreType = onuPreType;
    }

    public Integer getTopOnuAction() {
        return topOnuAction;
    }

    public void setTopOnuAction(Integer topOnuAction) {
        this.topOnuAction = topOnuAction;
    }

    public String getTopOnuHardwareVersion() {
        return topOnuHardwareVersion;
    }

    public void setTopOnuHardwareVersion(String topOnuHardwareVersion) {
        this.topOnuHardwareVersion = topOnuHardwareVersion;
    }

    /**
     * @return the onuElecComList
     */
    public List<OltOnuComAttribute> getOnuElecComList() {
        return onuElecComList;
    }

    /**
     * @param onuElecComList
     *            the onuElecComList to set
     */
    public void setOnuElecComList(List<OltOnuComAttribute> onuElecComList) {
        this.onuElecComList = onuElecComList;
    }

    public OltPonOptical getOltPonOptical() {
        return oltPonOptical;
    }

    public void setOltPonOptical(OltPonOptical oltPonOptical) {
        this.oltPonOptical = oltPonOptical;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public Integer getOnuDisplayTemp() {
        return onuDisplayTemp;
    }

    public void setOnuDisplayTemp(Integer onuDisplayTemp) {
        this.onuDisplayTemp = onuDisplayTemp;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Entity getCmcEntity() {
        return cmcEntity;
    }

    public void setCmcEntity(Entity cmcEntity) {
        this.cmcEntity = cmcEntity;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getOnuUniqueIdentification() {
        return onuUniqueIdentification;
    }

    public void setOnuUniqueIdentification(String onuUniqueIdentification) {
        this.onuUniqueIdentification = onuUniqueIdentification;
    }

    public String getOnuEorG() {
        return onuEorG;
    }

    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
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

    public Integer getLaserSwitch() {
        return laserSwitch;
    }

    public void setLaserSwitch(Integer laserSwitch) {
        this.laserSwitch = laserSwitch;
    }

    public List<OnuWanConnect> getOnuWanConnectList() {
        return onuWanConnectList;
    }

    public void setOnuWanConnectList(List<OnuWanConnect> onuWanConnectList) {
        this.onuWanConnectList = onuWanConnectList;
    }

    public List<OnuWanSsid> getOnuWanSsidList() {
        return onuWanSsidList;
    }

    public void setOnuWanSsidList(List<OnuWanSsid> onuWanSsidList) {
        this.onuWanSsidList = onuWanSsidList;
    }

    public Integer getCpeNum() {
        return cpeNum;
    }

    public void setCpeNum(Integer cpeNum) {
        this.cpeNum = cpeNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTopOnuExtAttr() {
        return topOnuExtAttr;
    }

    public void setTopOnuExtAttr(String topOnuExtAttr) {
        this.topOnuExtAttr = topOnuExtAttr;
        if (topOnuExtAttr != null) {
            catvCapability = EponUtil.getCatvCapability(topOnuExtAttr);
            wlanCapability = EponUtil.getWlanCapability(topOnuExtAttr);
        } else {
            catvCapability = 0;
            wlanCapability = 0;
        }
    }

    public Integer getCatvCapability() {
        return catvCapability;
    }

    public Integer getWlanCapability() {
        return wlanCapability;
    }

    public void setCatvCapability(Integer catvCapability) {
        this.catvCapability = catvCapability;
    }

    public void setWlanCapability(Integer wlanCapability) {
        this.wlanCapability = wlanCapability;
    }

    public OnuLinkInfo getOnuLinkInfo() {
        return onuLinkInfo;
    }

    public void setOnuLinkInfo(OnuLinkInfo onuLinkInfo) {
        this.onuLinkInfo = onuLinkInfo;
    }

    public OltPortOpticalInfo getOltPortOpticalInfo() {
        return oltPortOpticalInfo;
    }

    public void setOltPortOpticalInfo(OltPortOpticalInfo oltPortOpticalInfo) {
        this.oltPortOpticalInfo = oltPortOpticalInfo;
    }

    public SubDeviceCount getSubCount() {
        return subCount;
    }

    public void setSubCount(SubDeviceCount subCount) {
        this.subCount = subCount;
    }

    public Boolean getAttention() {
        return attention;
    }

    public void setAttention(Boolean attention) {
        this.attention = attention;
    }

    public List<UniPort> getEponUniPorts() {
        return eponUniPorts;
    }

    public void setEponUniPorts(List<UniPort> eponUniPorts) {
        this.eponUniPorts = eponUniPorts;
    }

    public List<GponUniAttribute> getGponUniPorts() {
        return gponUniPorts;
    }

    public void setGponUniPorts(List<GponUniAttribute> gponUniPorts) {
        this.gponUniPorts = gponUniPorts;
    }

    @Override
    public String toString() {
        return "Onu [entityId=" + entityId + ", onuId=" + onuId + ", onuIndex=" + onuIndex + ", onuName=" + onuName
                + ", onuType=" + onuType + ", onuPreType=" + onuPreType + ", typeName=" + typeName + ", onuMac="
                + onuMac + ", onuMacAddress=" + onuMacAddress + ", onuOperationStatus=" + onuOperationStatus
                + ", onuAdminStatus=" + onuAdminStatus + ", onuChipVendor=" + onuChipVendor + ", onuChipType="
                + onuChipType + ", onuChipVersion=" + onuChipVersion + ", onuSoftwareVersion=" + onuSoftwareVersion
                + ", onuFirmwareVersion=" + onuFirmwareVersion + ", onuTestDistance=" + onuTestDistance
                + ", onuLlidId=" + onuLlidId + ", onuTimeSinceLastRegister=" + onuTimeSinceLastRegister
                + ", temperatureDetectEnable=" + temperatureDetectEnable + ", topOnuCurrentTemperature="
                + topOnuCurrentTemperature + ", onuFecEnable=" + onuFecEnable + ", ponPerfStats15minuteEnable="
                + ponPerfStats15minuteEnable + ", ponPerfStats24hourEnable=" + ponPerfStats24hourEnable
                + ", onuPonPort=" + onuPonPort + ", onuIcon=" + onuIcon + ", onuUniPortList=" + onuUniPortList
                + ", changeTime=" + changeTime + ", onuIsolationEnable=" + onuIsolationEnable + ", topOnuAction="
                + topOnuAction + ", topOnuHardwareVersion=" + topOnuHardwareVersion + ", onuElecComList="
                + onuElecComList + ", oltPonOptical=" + oltPonOptical + ", sysLocation=" + sysLocation + ", sysName="
                + sysName + ", cmcEntity=" + cmcEntity + ", typeId=" + typeId + ", onuUniqueIdentification="
                + onuUniqueIdentification + ", onuEorG=" + onuEorG + ", onuDisplayTemp=" + onuDisplayTemp + "]";
    }

}
