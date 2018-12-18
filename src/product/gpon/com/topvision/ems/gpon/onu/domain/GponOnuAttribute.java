/***********************************************************************
 * $Id: GponOltOnuAttribute.java,v1.0 2016年10月15日 下午3:30:36 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016年10月15日-下午3:30:36
 *
 */
public class GponOnuAttribute extends OltOnuAttribute implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -4849236384562196222L;
    public static final Map<String, Long> GPON_CC_TYPE = new HashMap<String, Long>();

    static {
        GPON_CC_TYPE.put("880E", EponConstants.CMC_E);
        GPON_CC_TYPE.put("880F", EponConstants.CMC_F);
        GPON_CC_TYPE.put("88CE", EponConstants.CMC_C_E);
        GPON_CC_TYPE.put("88DE", EponConstants.CMC_D_E);
    }

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.1", index = true)
    private Long onuMibIndex;
    private Long onuIndex;
    // onuName由网管侧维护
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.2", writable = true, type = "OctetString")
    private String onuName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.3", isHex = true)
    private String onuSerialNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.4")
    private Integer onuType;
    private String onuTypeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.5")
    private String onuVendorID;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.6")
    private String onuEquipmentID;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.7")
    private Integer onuOperationStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.8")
    private Integer onuAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.9")
    private Integer onuTestDistance;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.10", writable = true, type = "Integer32")
    private Integer resetONU;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.11", writable = true, type = "Integer32")
    private Integer onuDeactive;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.12")
    private Long onuTimeSinceLastRegister;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.13")
    private Long onuSysUpTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.14")
    private String onuHardwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.15", writable = true, type = "Integer32")
    private Integer onuPerfStats15minuteEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.16", writable = true, type = "Integer32")
    private Integer onuPerfStats24hourEnable;

    /**
     * @return the onuMibIndex
     */
    public Long getOnuMibIndex() {
        return onuMibIndex;
    }

    /**
     * @param onuMibIndex
     *            the onuMibIndex to set
     */
    public void setOnuMibIndex(Long onuMibIndex) {
        this.onuMibIndex = onuMibIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(onuMibIndex);
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
     * @return the onuSerialNum
     */
    public String getOnuSerialNum() {
        return onuSerialNum;
    }

    /**
     * @param onuSerialNum
     *            the onuSerialNum to set
     */
    public void setOnuSerialNum(String onuSerialNum) {
        super.setOnuSerialNum(onuSerialNum);
        this.onuSerialNum = onuSerialNum;
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
     * @return the onuTypeString
     */
    public String getOnuTypeString() {
        return onuTypeString;
    }

    /**
     * @param onuTypeString
     *            the onuTypeString to set
     */
    public void setOnuTypeString(String onuTypeString) {
        this.onuTypeString = onuTypeString;
    }

    /**
     * @return the onuVendorID
     */
    public String getOnuVendorID() {
        return onuVendorID;
    }

    /**
     * @param onuVendorID
     *            the onuVendorID to set
     */
    public void setOnuVendorID(String onuVendorID) {
        this.onuVendorID = onuVendorID;
    }

    /**
     * @return the onuEquipmentID
     */
    public String getOnuEquipmentID() {
        return onuEquipmentID;
    }

    /**
     * @param onuEquipmentID
     *            the onuEquipmentID to set
     */
    public void setOnuEquipmentID(String onuEquipmentID) {
        this.onuEquipmentID = onuEquipmentID;
    }

    /**
     * @return the onuOperationStatus
     */
    public Integer getOnuOperationStatus() {
        return onuOperationStatus;
    }

    /**
     * @param onuOperationStatus
     *            the onuOperationStatus to set
     */
    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    /**
     * @return the onuAdminStatus
     */
    public Integer getOnuAdminStatus() {
        return onuAdminStatus;
    }

    /**
     * @param onuAdminStatus
     *            the onuAdminStatus to set
     */
    public void setOnuAdminStatus(Integer onuAdminStatus) {
        this.onuAdminStatus = onuAdminStatus;
    }

    /**
     * @return the onuTestDistance
     */
    public Integer getOnuTestDistance() {
        return onuTestDistance;
    }

    /**
     * @param onuTestDistance
     *            the onuTestDistance to set
     */
    public void setOnuTestDistance(Integer onuTestDistance) {
        this.onuTestDistance = onuTestDistance;
    }

    /**
     * @return the resetONU
     */
    public Integer getResetONU() {
        return resetONU;
    }

    /**
     * @param resetONU
     *            the resetONU to set
     */
    public void setResetONU(Integer resetONU) {
        this.resetONU = resetONU;
    }

    /**
     * @return the onuDeactive
     */
    public Integer getOnuDeactive() {
        return onuDeactive;
    }

    /**
     * @param onuDeactive
     *            the onuDeactive to set
     */
    public void setOnuDeactive(Integer onuDeactive) {
        this.onuDeactive = onuDeactive;
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
     * @return the onuSysUpTime
     */
    public Long getOnuSysUpTime() {
        return onuSysUpTime;
    }

    /**
     * @param onuSysUpTime
     *            the onuSysUpTime to set
     */
    public void setOnuSysUpTime(Long onuSysUpTime) {
        this.onuSysUpTime = onuSysUpTime;
    }

    public Integer getOnuPerfStats15minuteEnable() {
        return onuPerfStats15minuteEnable;
    }

    public void setOnuPerfStats15minuteEnable(Integer onuPerfStats15minuteEnable) {
        this.onuPerfStats15minuteEnable = onuPerfStats15minuteEnable;
    }

    public Integer getOnuPerfStats24hourEnable() {
        return onuPerfStats24hourEnable;
    }

    public void setOnuPerfStats24hourEnable(Integer onuPerfStats24hourEnable) {
        this.onuPerfStats24hourEnable = onuPerfStats24hourEnable;
    }

    public String getOnuHardwareVersion() {
        return onuHardwareVersion;
    }

    public void setOnuHardwareVersion(String onuHardwareVersion) {
        this.onuHardwareVersion = onuHardwareVersion;
        this.topOnuHardwareVersion = onuHardwareVersion;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

}
