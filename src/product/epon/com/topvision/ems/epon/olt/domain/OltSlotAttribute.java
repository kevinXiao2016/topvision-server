/***********************************************************************
 * $Id: OltSlotAttribute.java,v1.0 2011-9-26 上午09:03:57 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * SLOT属性
 * 
 * @author zhanglongyang
 * 
 */
@TableProperty(tables = { "default", "top", "bmap" })
public class OltSlotAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8164936072011338326L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.1,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.1", index = true)
    private Long deviceNo = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.2,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.2", index = true)
    private Long slotNo;
    private String slotNoStr;
    // @SnmpProperty(table="bmap", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.2.1.1", index = true)
    private Long bmapSlotPhyNo;
    // @SnmpProperty(table="bmap", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.2.1.2")
    private Long bmapSlotLogNo;
    private Long slotId;
    private Long slotIndex;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.1", index = true)
    private Long topSysBdSlotNo;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.2", writable = true, type = "Integer32")
    private Integer topSysBdPreConfigType;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.3")
    private Integer topSysBdActualType;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.10", writable = true, type = "Integer32")
    private Integer topSysBdTempDetectEnable;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.12", writable = true, type = "Integer32")
    private Integer topSysBdReset;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.4,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.4")
    private Integer bAttribute;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.5,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.5")
    private Integer bOperationStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.6,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.6", writable = true, type = "Integer32")
    private Integer bAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.7,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.7")
    private String bHardwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.8,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.8")
    private String bFirmwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.9,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.9")
    private String bSoftwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.10,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.10")
    private Long bUpTime;
    // private String sUpTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.12,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.12")
    private String bSerialNumber;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.14,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.14")
    private String bName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.15,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.15")
    private Integer bPresenceStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.3,V1.10.0.1:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.3")
    private Integer bType;

    public Long getBmapSlotLogNo() {
        switch (bAttribute) {
        case 1: {
            bmapSlotLogNo = 0L;
            break;
        }
        case 2: {
            bmapSlotLogNo = 255L;
            break;
        }
        default: {
            bmapSlotLogNo = slotNo;
        }
        }
        return bmapSlotLogNo;
    }

    public void setBmapSlotLogNo(Long bmapSlotLogNo) {
        this.bmapSlotLogNo = bmapSlotLogNo;
    }

    public Long getBmapSlotPhyNo() {
        return bmapSlotPhyNo;
    }

    public void setBmapSlotPhyNo(Long bmapSlotPhyNo) {
        this.bmapSlotPhyNo = bmapSlotPhyNo;
    }

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
     * @return the slotId
     */
    public Long getSlotId() {
        return slotId;
    }

    /**
     * @param slotId
     *            the slotId to set
     */
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    /**
     * @return the slotIndex
     */
    public Long getSlotIndex() {
        slotIndex = EponIndex.getSlotIndex(getBmapSlotLogNo().intValue());
        return slotIndex;
    }

    /**
     * @param slotIndex
     *            the slotIndex to set
     */
    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
        bmapSlotLogNo = EponIndex.getSlotNo(slotIndex);
        if (bmapSlotLogNo != 0) {
            slotNo = topSysBdSlotNo = bmapSlotPhyNo = bmapSlotLogNo;
        }
    }

    /**
     * @return the topSysBdPreConfigType
     */
    public Integer getTopSysBdPreConfigType() {
        return topSysBdPreConfigType;
    }

    /**
     * @param topSysBdPreConfigType
     *            the topSysBdPreConfigType to set
     */
    public void setTopSysBdPreConfigType(Integer topSysBdPreConfigType) {
        this.topSysBdPreConfigType = topSysBdPreConfigType;
    }

    /**
     * @return the topSysBdActualType
     */
    public Integer getTopSysBdActualType() {
        return topSysBdActualType;
    }

    /**
     * @param topSysBdActualType
     *            the topSysBdActualType to set
     */
    public void setTopSysBdActualType(Integer topSysBdActualType) {
        this.topSysBdActualType = topSysBdActualType;
    }

    /**
     * @return the bAttribute
     */
    public Integer getbAttribute() {
        return bAttribute;
    }

    /**
     * @param bAttribute
     *            the bAttribute to set
     */
    public void setbAttribute(Integer bAttribute) {
        this.bAttribute = bAttribute;
    }

    /**
     * @return the bHardwareVersion
     */
    public String getbHardwareVersion() {
        return bHardwareVersion;
    }

    /**
     * @param bHardwareVersion
     *            the bHardwareVersion to set
     */
    public void setbHardwareVersion(String bHardwareVersion) {
        this.bHardwareVersion = bHardwareVersion;
    }

    /**
     * @return the bFirmwareVersion
     */
    public String getbFirmwareVersion() {
        return bFirmwareVersion;
    }

    /**
     * @param bFirmwareVersion
     *            the bFirmwareVersion to set
     */
    public void setbFirmwareVersion(String bFirmwareVersion) {
        this.bFirmwareVersion = bFirmwareVersion;
    }

    /**
     * @return the bSoftwareVersion
     */
    public String getbSoftwareVersion() {
        return bSoftwareVersion;
    }

    /**
     * @param bSoftwareVersion
     *            the bSoftwareVersion to set
     */
    public void setbSoftwareVersion(String bSoftwareVersion) {
        this.bSoftwareVersion = bSoftwareVersion;
    }

    /**
     * @return the bUpTime
     */
    public Long getbUpTime() {
        return bUpTime;
    }

    /**
     * @param bUpTime
     *            the bUpTime to set
     */
    public void setbUpTime(Long bUpTime) {
        this.bUpTime = bUpTime;
        // this.sUpTime = String.valueOf(bUpTime);
    }

    // /**
    // * @return the sUpTime
    // */
    // public String getsUpTime() {
    // return sUpTime;
    // }
    //
    // /**
    // * @param sUpTime
    // * the sUpTime to set
    // */
    // public void setsUpTime(String sUpTime) {
    // this.sUpTime = sUpTime;
    // this.bUpTime = Long.parseLong(sUpTime);
    // }

    /**
     * @return the bSerialNumber
     */
    public String getbSerialNumber() {
        return bSerialNumber;
    }

    /**
     * @param bSerialNumber
     *            the bSerialNumber to set
     */
    public void setbSerialNumber(String bSerialNumber) {
        this.bSerialNumber = bSerialNumber;
    }

    /**
     * @return the bName
     */
    public String getbName() {
        return bName;
    }

    /**
     * @param bName
     *            the bName to set
     */
    public void setbName(String bName) {
        this.bName = bName;
    }

    /**
     * @return the bPresenceStatus
     */
    public Integer getbPresenceStatus() {
        return bPresenceStatus;
    }

    /**
     * @param bPresenceStatus
     *            the bPresenceStatus to set
     */
    public void setbPresenceStatus(Integer bPresenceStatus) {
        this.bPresenceStatus = bPresenceStatus;
    }

    /**
     * @return the deviceNo
     */
    public Long getDeviceNo() {
        return deviceNo;
    }

    /**
     * @param deviceNo
     *            the deviceNo to set
     */
    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    /**
     * @return the slotNo
     */
    public Long getSlotNo() {
        if (slotNo != null) {
            return slotNo;
        }
        return topSysBdSlotNo;
    }

    /**
     * @param slotNo
     *            the slotNo to set
     */
    public void setSlotNo(Long slotNo) {
        this.slotNo = this.topSysBdSlotNo = this.bmapSlotPhyNo = slotNo;
    }

    public Long getTopSysBdSlotNo() {
        return topSysBdSlotNo;
    }

    public void setTopSysBdSlotNo(Long topSysBdSlotNo) {
        this.topSysBdSlotNo = topSysBdSlotNo;
    }

    public Integer getTopSysBdReset() {
        return topSysBdReset;
    }

    public void setTopSysBdReset(Integer topSysBdReset) {
        this.topSysBdReset = topSysBdReset;
    }

    public Integer getBAttribute() {
        return bAttribute;
    }

    public void setBAttribute(Integer bAttribute) {
        this.bAttribute = bAttribute;
    }

    public String getBFirmwareVersion() {
        return bFirmwareVersion;
    }

    public void setBFirmwareVersion(String bFirmwareVersion) {
        this.bFirmwareVersion = bFirmwareVersion;
    }

    public String getBHardwareVersion() {
        return bHardwareVersion;
    }

    public void setBHardwareVersion(String bHardwareVersion) {
        this.bHardwareVersion = bHardwareVersion;
    }

    public String getBName() {
        return bName;
    }

    public void setBName(String bName) {
        this.bName = bName;
    }

    public Integer getBPresenceStatus() {
        return bPresenceStatus;
    }

    public void setBPresenceStatus(Integer bPresenceStatus) {
        this.bPresenceStatus = bPresenceStatus;
    }

    public String getBSerialNumber() {
        return bSerialNumber;
    }

    public void setBSerialNumber(String bSerialNumber) {
        this.bSerialNumber = bSerialNumber;
    }

    public String getBSoftwareVersion() {
        return bSoftwareVersion;
    }

    public void setBSoftwareVersion(String bSoftwareVersion) {
        this.bSoftwareVersion = bSoftwareVersion;
    }

    public Long getBUpTime() {
        return bUpTime;
    }

    public void setBUpTime(Long bUpTime) {
        this.bUpTime = bUpTime;
    }

    // public String getSUpTime() {
    // return sUpTime;
    // }
    //
    // public void setSUpTime(String sUpTime) {
    // this.sUpTime = sUpTime;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSlotAttribute [entityId=");
        builder.append(entityId);
        builder.append(", deviceNo=");
        builder.append(deviceNo);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", slotId=");
        builder.append(slotId);
        builder.append(", slotIndex=");
        builder.append(slotIndex);
        builder.append(", topSysBdSlotNo=");
        builder.append(topSysBdSlotNo);
        builder.append(", topSysBdPreConfigType=");
        builder.append(topSysBdPreConfigType);
        builder.append(", topSysBdActualType=");
        builder.append(topSysBdActualType);
        builder.append(", topSysBdTempDetectEnable=");
        builder.append(topSysBdTempDetectEnable);
        builder.append(", topSysBdReset=");
        builder.append(topSysBdReset);
        builder.append(", bAttribute=");
        builder.append(bAttribute);
        builder.append(", bOperationStatus=");
        builder.append(bOperationStatus);
        builder.append(", bAdminStatus=");
        builder.append(bAdminStatus);
        builder.append(", bHardwareVersion=");
        builder.append(bHardwareVersion);
        builder.append(", bFirmwareVersion=");
        builder.append(bFirmwareVersion);
        builder.append(", bSoftwareVersion=");
        builder.append(bSoftwareVersion);
        builder.append(", bUpTime=");
        builder.append(bUpTime);
        builder.append(", bSerialNumber=");
        builder.append(bSerialNumber);
        builder.append(", bName=");
        builder.append(bName);
        builder.append(", bPresenceStatus=");
        builder.append(bPresenceStatus);
        builder.append("]");
        return builder.toString();
    }

    /**
     * @return the topSysBdTempDetectEnable
     */
    public Integer getTopSysBdTempDetectEnable() {
        return topSysBdTempDetectEnable;
    }

    /**
     * @param topSysBdTempDetectEnable
     *            the topSysBdTempDetectEnable to set
     */
    public void setTopSysBdTempDetectEnable(Integer topSysBdTempDetectEnable) {
        this.topSysBdTempDetectEnable = topSysBdTempDetectEnable;
    }

    /**
     * @return the bAdminStatus
     */
    public Integer getbAdminStatus() {
        return bAdminStatus;
    }

    /**
     * @param bAdminStatus
     *            the bAdminStatus to set
     */
    public void setbAdminStatus(Integer bAdminStatus) {
        this.bAdminStatus = bAdminStatus;
    }

    /**
     * @return the bAdminStatus
     */
    public Integer getBAdminStatus() {
        return bAdminStatus;
    }

    /**
     * @param bAdminStatus
     *            the bAdminStatus to set
     */
    public void setBAdminStatus(Integer bAdminStatus) {
        this.bAdminStatus = bAdminStatus;
    }

    /**
     * @return the bOperationStatus
     */
    public Integer getbOperationStatus() {
        return bOperationStatus;
    }

    /**
     * @param bOperationStatus
     *            the bOperationStatus to set
     */
    public void setbOperationStatus(Integer bOperationStatus) {
        this.bOperationStatus = bOperationStatus;
    }

    public Integer getbType() {
        return bType;
    }

    public void setbType(Integer bType) {
        this.bType = bType;
    }

    public String getSlotNoStr() {
        return slotNoStr;
    }

    public void setSlotNoStr(String slotNoStr) {
        this.slotNoStr = slotNoStr;
    }

}
