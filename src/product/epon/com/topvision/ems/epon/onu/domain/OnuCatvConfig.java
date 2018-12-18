/***********************************************************************
 * $Id: OnuCatvConfig.java,v1.0 2016-4-26 下午2:28:51 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author haojie
 * @created @2016-4-26-下午2:28:51
 *
 */
public class OnuCatvConfig implements AliasesSuperType {
    private static final long serialVersionUID = -1601043916287912381L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.1", index = true)
    private Long onuCatvOrConfigDeviceIndex;
    // -----------------增益控制设置------------------
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.2", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigSwitch;// CATV光机开关 1: on(1)2: off(2)
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.3", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigGainControlType;// ONU CATV接口输出增益类型,1：AGC，光自动增益控制；2：MGC，手动增益控制
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.4", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigAGCUpValue;// 输入光功率上限值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.5", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigAGCRange;// 输入光功率范围值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.6", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigMGCTxAttenuation;// 输出电平衰减值

    // -----------------告警阈值设置------------------
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.7", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigInputLO;// 接收功率低告警阀值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.8", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigInputHI;// 接收功率高告警阀值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.9", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigOutputLO;// 输出电平低告警阀值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.10", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigOutputHI;// 输出电平高告警阀值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.11", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigVoltageHI;// 工作电压高告警阀值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.12", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigVoltageLO;// 工作电压低告警阀值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.13", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigTemperatureHI;// 工作温度高告警阀值
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.1.1.14", writable = true, type = "Integer32")
    private Integer onuCatvOrConfigTemperatureLO;// 工作温度低告警阀值

    private String collectTime;

    public Long getOnuCatvOrConfigDeviceIndex() {
        return onuCatvOrConfigDeviceIndex;
    }

    public void setOnuCatvOrConfigDeviceIndex(Long onuCatvOrConfigDeviceIndex) {
        this.onuCatvOrConfigDeviceIndex = onuCatvOrConfigDeviceIndex;
        if (onuCatvOrConfigDeviceIndex != null) {
            onuIndex = EponIndex.getOnuIndexByMibIndex(onuCatvOrConfigDeviceIndex);
        }
    }

    public Integer getOnuCatvOrConfigSwitch() {
        return onuCatvOrConfigSwitch;
    }

    public void setOnuCatvOrConfigSwitch(Integer onuCatvOrConfigSwitch) {
        this.onuCatvOrConfigSwitch = onuCatvOrConfigSwitch;
    }

    public Integer getOnuCatvOrConfigGainControlType() {
        return onuCatvOrConfigGainControlType;
    }

    public void setOnuCatvOrConfigGainControlType(Integer onuCatvOrConfigGainControlType) {
        this.onuCatvOrConfigGainControlType = onuCatvOrConfigGainControlType;
    }

    public Integer getOnuCatvOrConfigAGCUpValue() {
        return onuCatvOrConfigAGCUpValue;
    }

    public void setOnuCatvOrConfigAGCUpValue(Integer onuCatvOrConfigAGCUpValue) {
        this.onuCatvOrConfigAGCUpValue = onuCatvOrConfigAGCUpValue;
    }

    public Integer getOnuCatvOrConfigAGCRange() {
        return onuCatvOrConfigAGCRange;
    }

    public void setOnuCatvOrConfigAGCRange(Integer onuCatvOrConfigAGCRange) {
        this.onuCatvOrConfigAGCRange = onuCatvOrConfigAGCRange;
    }

    public Integer getOnuCatvOrConfigMGCTxAttenuation() {
        return onuCatvOrConfigMGCTxAttenuation;
    }

    public void setOnuCatvOrConfigMGCTxAttenuation(Integer onuCatvOrConfigMGCTxAttenuation) {
        this.onuCatvOrConfigMGCTxAttenuation = onuCatvOrConfigMGCTxAttenuation;
    }

    public Integer getOnuCatvOrConfigInputLO() {
        return onuCatvOrConfigInputLO;
    }

    public void setOnuCatvOrConfigInputLO(Integer onuCatvOrConfigInputLO) {
        this.onuCatvOrConfigInputLO = onuCatvOrConfigInputLO;
    }

    public Integer getOnuCatvOrConfigInputHI() {
        return onuCatvOrConfigInputHI;
    }

    public void setOnuCatvOrConfigInputHI(Integer onuCatvOrConfigInputHI) {
        this.onuCatvOrConfigInputHI = onuCatvOrConfigInputHI;
    }

    public Integer getOnuCatvOrConfigOutputLO() {
        return onuCatvOrConfigOutputLO;
    }

    public void setOnuCatvOrConfigOutputLO(Integer onuCatvOrConfigOutputLO) {
        this.onuCatvOrConfigOutputLO = onuCatvOrConfigOutputLO;
    }

    public Integer getOnuCatvOrConfigOutputHI() {
        return onuCatvOrConfigOutputHI;
    }

    public void setOnuCatvOrConfigOutputHI(Integer onuCatvOrConfigOutputHI) {
        this.onuCatvOrConfigOutputHI = onuCatvOrConfigOutputHI;
    }

    public Integer getOnuCatvOrConfigVoltageHI() {
        return onuCatvOrConfigVoltageHI;
    }

    public void setOnuCatvOrConfigVoltageHI(Integer onuCatvOrConfigVoltageHI) {
        this.onuCatvOrConfigVoltageHI = onuCatvOrConfigVoltageHI;
    }

    public Integer getOnuCatvOrConfigVoltageLO() {
        return onuCatvOrConfigVoltageLO;
    }

    public void setOnuCatvOrConfigVoltageLO(Integer onuCatvOrConfigVoltageLO) {
        this.onuCatvOrConfigVoltageLO = onuCatvOrConfigVoltageLO;
    }

    public Integer getOnuCatvOrConfigTemperatureHI() {
        return onuCatvOrConfigTemperatureHI;
    }

    public void setOnuCatvOrConfigTemperatureHI(Integer onuCatvOrConfigTemperatureHI) {
        this.onuCatvOrConfigTemperatureHI = onuCatvOrConfigTemperatureHI;
    }

    public Integer getOnuCatvOrConfigTemperatureLO() {
        return onuCatvOrConfigTemperatureLO;
    }

    public void setOnuCatvOrConfigTemperatureLO(Integer onuCatvOrConfigTemperatureLO) {
        this.onuCatvOrConfigTemperatureLO = onuCatvOrConfigTemperatureLO;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            onuCatvOrConfigDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuCatvConfig [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuCatvOrConfigDeviceIndex=");
        builder.append(onuCatvOrConfigDeviceIndex);
        builder.append(", onuCatvOrConfigSwitch=");
        builder.append(onuCatvOrConfigSwitch);
        builder.append(", onuCatvOrConfigGainControlType=");
        builder.append(onuCatvOrConfigGainControlType);
        builder.append(", onuCatvOrConfigAGCUpValue=");
        builder.append(onuCatvOrConfigAGCUpValue);
        builder.append(", onuCatvOrConfigAGCRange=");
        builder.append(onuCatvOrConfigAGCRange);
        builder.append(", onuCatvOrConfigMGCTxAttenuation=");
        builder.append(onuCatvOrConfigMGCTxAttenuation);
        builder.append(", onuCatvOrConfigInputLO=");
        builder.append(onuCatvOrConfigInputLO);
        builder.append(", onuCatvOrConfigInputHI=");
        builder.append(onuCatvOrConfigInputHI);
        builder.append(", onuCatvOrConfigOutputLO=");
        builder.append(onuCatvOrConfigOutputLO);
        builder.append(", onuCatvOrConfigOutputHI=");
        builder.append(onuCatvOrConfigOutputHI);
        builder.append(", onuCatvOrConfigVoltageHI=");
        builder.append(onuCatvOrConfigVoltageHI);
        builder.append(", onuCatvOrConfigVoltageLO=");
        builder.append(onuCatvOrConfigVoltageLO);
        builder.append(", onuCatvOrConfigTemperatureHI=");
        builder.append(onuCatvOrConfigTemperatureHI);
        builder.append(", onuCatvOrConfigTemperatureLO=");
        builder.append(onuCatvOrConfigTemperatureLO);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
