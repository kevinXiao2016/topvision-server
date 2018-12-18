/***********************************************************************
 * $Id: OltOnuPonAttribute.java,v1.0 2011-9-26 上午09:09:57 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.ResourceManager;

/**
 * Onu的Pon口的光传输属性
 * 
 * @author zhanglongyang
 * 
 */
public class OltOnuPonAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7148649845575861200L;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private Long entityId;
    private Long onuId;
    private Long onuPonId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.1", index = true)
    private Long onuMibIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.2", index = true)
    private Long onuCardNo;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.3", index = true)
    private Long onuPonNo;
    private Long onuPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.4")
    private Integer onuReceivedOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.5")
    private Integer onuTramsmittedOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.6")
    private Integer onuBiasCurrent;
    private String biasCurrentStr; // 手机网管中使用
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.7")
    private Integer onuWorkingVoltage;
    private String workingVoltageStr; // 手机网管中使用
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.2.1.8")
    private Integer onuWorkingTemperature;
    private String workTempStr; // 手机网管中使用
    private Float oltPonRevPower;
    private String oltPonRevPowerStr; // 手机网管中使用
    private Float onuPonRevPower;
    private String onuPonRevPowerStr; // 手机网管中使用
    private Float onuPonTransPower;
    private String onuPonTransPowerStr; // 手机网管中使用
    private Float oltPonTransPower;
    private String oltPonTransPowerStr; // 手机网管中使用
    private Integer onuStatus;

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
     * @return the onuPonIndex
     */
    public Long getOnuPonIndex() {
        if (onuPonIndex == null) {
            onuPonIndex = EponIndex.getOnuPonIndexByMibIndex(onuMibIndex, onuCardNo, onuPonNo);
        }
        return onuPonIndex;
    }

    /**
     * @param onuPonIndex
     *            the onuPonIndex to set
     */
    public void setOnuPonIndex(Long onuPonIndex) {
        this.onuPonIndex = onuPonIndex;
        onuMibIndex = EponIndex.getOnuMibIndexByIndex(onuPonIndex);
        onuCardNo = EponIndex.getOnuCardNo(onuPonIndex);
        onuPonNo = EponIndex.getUniNo(onuPonIndex);
    }

    /**
     * @return the onuPonId
     */
    public Long getOnuPonId() {
        return onuPonId;
    }

    /**
     * @param onuPonId
     *            the onuPonId to set
     */
    public void setOnuPonId(Long onuPonId) {
        this.onuPonId = onuPonId;
    }

    /**
     * @return the onuReceivedOpticalPower
     */
    public Integer getOnuReceivedOpticalPower() {
        return onuReceivedOpticalPower;
    }

    /**
     * @param onuReceivedOpticalPower
     *            the onuReceivedOpticalPower to set
     */
    public void setOnuReceivedOpticalPower(Integer onuReceivedOpticalPower) {
        if (EponConstants.RE_POWER != onuReceivedOpticalPower) {
            this.onuReceivedOpticalPower = onuReceivedOpticalPower;
        } else {
            this.onuReceivedOpticalPower = null;
        }

        if (onuReceivedOpticalPower != null && EponConstants.RE_POWER != onuReceivedOpticalPower) {
            onuPonRevPowerStr = df.format(onuReceivedOpticalPower / 100F) + " dBm";
        }
    }

    /**
     * @return the onuTramsmittedOpticalPower
     */
    public Integer getOnuTramsmittedOpticalPower() {
        return onuTramsmittedOpticalPower;
    }

    /**
     * @param onuTramsmittedOpticalPower
     *            the onuTramsmittedOpticalPower to set
     */
    public void setOnuTramsmittedOpticalPower(Integer onuTramsmittedOpticalPower) {
        if (EponConstants.TX_POWER != onuTramsmittedOpticalPower) {
            this.onuTramsmittedOpticalPower = onuTramsmittedOpticalPower;
        } else {
            this.onuTramsmittedOpticalPower = null;
        }

        if (onuTramsmittedOpticalPower != null && EponConstants.TX_POWER != onuTramsmittedOpticalPower) {
            onuPonTransPowerStr = df.format(onuTramsmittedOpticalPower / 100F) + " dBm";
        }
    }

    /**
     * @return the onuBiasCurrent
     */
    public Integer getOnuBiasCurrent() {
        return onuBiasCurrent;
    }

    /**
     * @param onuBiasCurrent
     *            the onuBiasCurrent to set
     */
    public void setOnuBiasCurrent(Integer onuBiasCurrent) {
        if (EponConstants.OPT_CURRENT != onuBiasCurrent) {
            this.onuBiasCurrent = onuBiasCurrent;
        } else {
            this.onuBiasCurrent = null;
        }

        if (onuBiasCurrent != null && EponConstants.OPT_CURRENT != onuBiasCurrent) {
            biasCurrentStr = df.format(onuBiasCurrent / 100F) + " mA";
        }
    }

    /**
     * @return the onuWorkingVoltage
     */
    public Integer getOnuWorkingVoltage() {
        return onuWorkingVoltage;
    }

    /**
     * @param onuWorkingVoltage
     *            the onuWorkingVoltage to set
     */
    public void setOnuWorkingVoltage(Integer onuWorkingVoltage) {
        if (EponConstants.OPT_VOLTAGE != onuWorkingVoltage) {
            this.onuWorkingVoltage = onuWorkingVoltage;
        } else {
            this.onuWorkingVoltage = null;
        }

        if (onuWorkingVoltage != null && EponConstants.OPT_VOLTAGE != onuWorkingVoltage) {
            workingVoltageStr = df.format(onuWorkingVoltage / 100000F) + " V";
        }
    }

    /**
     * @return the onuWorkingTemperature
     */
    public Integer getOnuWorkingTemperature() {
        return onuWorkingTemperature;
    }

    private ResourceManager getResourece(){
        return ResourceManager.getResourceManager("com.topvision.ems.epon.onu.resources");
    }
    /**
     * @param onuWorkingTemperature
     *            the onuWorkingTemperature to set
     */
    public void setOnuWorkingTemperature(Integer onuWorkingTemperature) {
        if (EponConstants.OPT_TEMP != onuWorkingTemperature) {
            this.onuWorkingTemperature = onuWorkingTemperature;
        } else {
            this.onuWorkingTemperature = null;
        }

        if (onuWorkingTemperature != null && EponConstants.OPT_TEMP != onuWorkingTemperature) {
            workTempStr = df.format(onuWorkingTemperature / 100F) + getResourece().getString("onu.sheshidu");
        }
    }

    public Long getOnuCardNo() {
        return onuCardNo;
    }

    public void setOnuCardNo(Long onuCardNo) {
        this.onuCardNo = onuCardNo;
    }

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
    }

    public Long getOnuPonNo() {
        return onuPonNo;
    }

    public void setOnuPonNo(Long onuPonNo) {
        this.onuPonNo = onuPonNo;
    }

    public Float getOltPonRevPower() {
        return oltPonRevPower;
    }

    public void setOltPonRevPower(Float oltPonRevPower) {
        this.oltPonRevPower = oltPonRevPower;
        if (oltPonRevPower != null) {
            oltPonRevPowerStr = df.format(oltPonRevPower) + "dBm";
        }

    }

    public Float getOnuPonRevPower() {
        return onuPonRevPower;
    }

    public void setOnuPonRevPower(Float onuPonRevPower) {
        this.onuPonRevPower = onuPonRevPower;
    }

    public Float getOnuPonTransPower() {
        return onuPonTransPower;
    }

    public void setOnuPonTransPower(Float onuPonTransPower) {
        this.onuPonTransPower = onuPonTransPower;
    }

    public String getBiasCurrentStr() {
        return biasCurrentStr;
    }

    public void setBiasCurrentStr(String biasCurrentStr) {
        this.biasCurrentStr = biasCurrentStr;
    }

    public String getWorkingVoltageStr() {
        return workingVoltageStr;
    }

    public void setWorkingVoltageStr(String workingVoltageStr) {
        this.workingVoltageStr = workingVoltageStr;
    }

    public String getWorkTempStr() {
        return workTempStr;
    }

    public void setWorkTempStr(String workTempStr) {
        this.workTempStr = workTempStr;
    }

    public String getOltPonRevPowerStr() {
        return oltPonRevPowerStr;
    }

    public void setOltPonRevPowerStr(String oltPonRevPowerStr) {
        this.oltPonRevPowerStr = oltPonRevPowerStr;
    }

    public String getOnuPonRevPowerStr() {
        return onuPonRevPowerStr;
    }

    public void setOnuPonRevPowerStr(String onuPonRevPowerStr) {
        this.onuPonRevPowerStr = onuPonRevPowerStr;
    }

    public String getOnuPonTransPowerStr() {
        return onuPonTransPowerStr;
    }

    public void setOnuPonTransPowerStr(String onuPonTransPowerStr) {
        this.onuPonTransPowerStr = onuPonTransPowerStr;
    }

    public Float getOltPonTransPower() {
        return oltPonTransPower;
    }

    public void setOltPonTransPower(Float oltPonTransPower) {
        this.oltPonTransPower = oltPonTransPower;
    }

    public String getOltPonTransPowerStr() {
        return oltPonTransPowerStr;
    }

    public void setOltPonTransPowerStr(String oltPonTransPowerStr) {
        this.oltPonTransPowerStr = oltPonTransPowerStr;
    }

    public Integer getOnuStatus() {
        return onuStatus;
    }

    public void setOnuStatus(Integer onuStatus) {
        this.onuStatus = onuStatus;
    }

    @Override
    public String toString() {
        return "OltOnuPonAttribute [entityId=" + entityId + ", onuId=" + onuId + ", onuPonId=" + onuPonId
                + ", onuMibIndex=" + onuMibIndex + ", onuCardNo=" + onuCardNo + ", onuPonNo=" + onuPonNo
                + ", onuPonIndex=" + onuPonIndex + ", onuReceivedOpticalPower=" + onuReceivedOpticalPower
                + ", onuTramsmittedOpticalPower=" + onuTramsmittedOpticalPower + ", onuBiasCurrent=" + onuBiasCurrent
                + ", onuWorkingVoltage=" + onuWorkingVoltage + ", onuWorkingTemperature=" + onuWorkingTemperature
                + ", oltPonRevPower=" + oltPonRevPower + ", onuPonRevPower=" + onuPonRevPower + ", onuPonTransPower="
                + onuPonTransPower + "]";
    }

}
