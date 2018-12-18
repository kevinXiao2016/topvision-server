/***********************************************************************
 * $Id: OnuCatvInfo.java,v1.0 2016-4-26 下午2:30:17 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author haojie
 * @created @2016-4-26-下午2:30:17
 *
 */
public class OnuCatvInfo implements AliasesSuperType {
    private static final long serialVersionUID = -6161693453816020121L;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    private Long entityId;
    private Long onuId;
    private Long onuIndex;

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.1", index = true)
    private Long onuCatvOrInfoDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.2")
    private Integer onuCatvOrInfoRxPower;// 接收功率
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.3")
    private Integer onuCatvOrInfoRfOutVoltage;// 输出电平
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.4")
    private Integer onuCatvOrInfoVoltage;// 工作电压
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.5")
    private Integer onuCatvOrInfoTemperature;// 工作温度
    private Timestamp collectTime;

    private String rxPowerForunit;
    private String rfOutVoltageForunit;
    private String voltageForunit;
    private String temperatureForunit;

    public String getRxPowerForunit() {
        if (onuCatvOrInfoRxPower != null) {
            if ((Double.parseDouble(onuCatvOrInfoRxPower.toString()) / 10) < -20) {
                rxPowerForunit = "--";
            } else {
                rxPowerForunit = df.format(Double.parseDouble(onuCatvOrInfoRxPower.toString()) / 10) + " dBm";
            }
        } else {
            rxPowerForunit = "--";
        }
        return rxPowerForunit;
    }

    public void setRxPowerForunit(String rxPowerForunit) {
        this.rxPowerForunit = rxPowerForunit;
    }

    public String getRfOutVoltageForunit() {
        return rfOutVoltageForunit;
    }

    public void setRfOutVoltageForunit(String rfOutVoltageForunit) {
        this.rfOutVoltageForunit = rfOutVoltageForunit;
    }

    public String getVoltageForunit() {
        if (onuCatvOrInfoVoltage != null) {
            voltageForunit = df.format(Double.parseDouble(onuCatvOrInfoVoltage.toString()) / 10) + " V";
        } else {
            voltageForunit = "--";
        }
        return voltageForunit;
    }

    public void setVoltageForunit(String voltageForunit) {
        this.voltageForunit = voltageForunit;
    }

    public String getTemperatureForunit() {
        return temperatureForunit;
    }

    public void setTemperatureForunit(String temperatureForunit) {
        this.temperatureForunit = temperatureForunit;
    }

    public Long getOnuCatvOrInfoDeviceIndex() {
        return onuCatvOrInfoDeviceIndex;
    }

    public void setOnuCatvOrInfoDeviceIndex(Long onuCatvOrInfoDeviceIndex) {
        this.onuCatvOrInfoDeviceIndex = onuCatvOrInfoDeviceIndex;
        if (onuCatvOrInfoDeviceIndex != null) {
            onuIndex = EponIndex.getOnuIndexByMibIndex(onuCatvOrInfoDeviceIndex);
        }
    }

    public Integer getOnuCatvOrInfoRxPower() {
        return onuCatvOrInfoRxPower;
    }

    public void setOnuCatvOrInfoRxPower(Integer onuCatvOrInfoRxPower) {
        this.onuCatvOrInfoRxPower = onuCatvOrInfoRxPower;
    }

    public Integer getOnuCatvOrInfoRfOutVoltage() {
        return onuCatvOrInfoRfOutVoltage;
    }

    public void setOnuCatvOrInfoRfOutVoltage(Integer onuCatvOrInfoRfOutVoltage) {
        this.onuCatvOrInfoRfOutVoltage = onuCatvOrInfoRfOutVoltage;
    }

    public Integer getOnuCatvOrInfoVoltage() {
        return onuCatvOrInfoVoltage;
    }

    public void setOnuCatvOrInfoVoltage(Integer onuCatvOrInfoVoltage) {
        this.onuCatvOrInfoVoltage = onuCatvOrInfoVoltage;
    }

    public Integer getOnuCatvOrInfoTemperature() {
        return onuCatvOrInfoTemperature;
    }

    public void setOnuCatvOrInfoTemperature(Integer onuCatvOrInfoTemperature) {
        this.onuCatvOrInfoTemperature = onuCatvOrInfoTemperature;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
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
            onuCatvOrInfoDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuCatvInfo [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuCatvOrInfoDeviceIndex=");
        builder.append(onuCatvOrInfoDeviceIndex);
        builder.append(", onuCatvOrInfoRxPower=");
        builder.append(onuCatvOrInfoRxPower);
        builder.append(", onuCatvOrInfoRfOutVoltage=");
        builder.append(onuCatvOrInfoRfOutVoltage);
        builder.append(", onuCatvOrInfoVoltage=");
        builder.append(onuCatvOrInfoVoltage);
        builder.append(", onuCatvOrInfoTemperature=");
        builder.append(onuCatvOrInfoTemperature);
        builder.append(", rxPowerForunit=");
        builder.append(rxPowerForunit);
        builder.append(", rfOutVoltageForunit=");
        builder.append(rfOutVoltageForunit);
        builder.append(", voltageForunit=");
        builder.append(voltageForunit);
        builder.append(", temperatureForunit=");
        builder.append(temperatureForunit);
        builder.append("]");
        return builder.toString();
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

}
