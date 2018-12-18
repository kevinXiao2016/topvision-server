package com.topvision.ems.cmc.ccmts.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.utils.EponConstants;

public class CmcOpticalInfo implements Serializable {

    private static final long serialVersionUID = -7091522581315853757L;
    private Long cmcId;
    private Long cmcIndex;
    private String ip;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.1", index = true)
    private Long transceiverIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.4")
    private Integer waveLen;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.9")
    private Long rxPower;
    private Float rxPowerFloat;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.10")
    private Long txPower;
    private Float txPowerFloat;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.11")
    private Long biasCurrent;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.12")
    private Long voltage;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.11.1.13")
    private Long temperature;
    private boolean pingCheck = false;
    private boolean snmpCheck = false;
    //用于单位转换时的温度展示
    private Integer cmcOpticalTemp;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getTransceiverIndex() {
        return transceiverIndex;
    }

    public void setTransceiverIndex(Long transceiverIndex) {
        this.transceiverIndex = transceiverIndex;
    }

    public Long getRxPower() {
        return rxPower;
    }

    public void setRxPower(Long rxPower) {
        if (rxPower != null && rxPower != 0 && !rxPower.equals(EponConstants.RE_POWER)) {
            this.rxPowerFloat = rxPower / 100F;
        } else {
            this.rxPowerFloat = EponConstants.INVALID_VALUE;
        }
        this.rxPower = rxPower;
    }

    public Long getTxPower() {
        return txPower;
    }

    public void setTxPower(Long txPower) {
        if (txPower != null && txPower != 0 && !txPower.equals(EponConstants.TX_POWER)) {
            this.txPowerFloat = txPower / 100F;
        } else {
            this.txPowerFloat = EponConstants.INVALID_VALUE;
        }
        this.txPower = txPower;
    }

    public Long getBiasCurrent() {
        return biasCurrent;
    }

    public void setBiasCurrent(Long biasCurrent) {
        if (biasCurrent != null && biasCurrent != 0 && !biasCurrent.equals(EponConstants.OPT_CURRENT)) {
            this.biasCurrent = biasCurrent / 100;
        } else {
            this.biasCurrent = null;
        }
    }

    public Long getVoltage() {
        return voltage;
    }

    public void setVoltage(Long voltage) {
        if (voltage != null && voltage != 0 && !voltage.equals(EponConstants.OPT_VOLTAGE)) {
            this.voltage = voltage / 100;
        } else {
            this.voltage = null;
        }
    }

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long temperature) {
        if (temperature != null && temperature != 0 && !temperature.equals(EponConstants.OPT_TEMP)) {
            this.temperature = temperature / 100;
        } else {
            this.temperature = null;
        }
    }

    public boolean isPingCheck() {
        return pingCheck;
    }

    public void setPingCheck(boolean pingCheck) {
        this.pingCheck = pingCheck;
    }

    public boolean isSnmpCheck() {
        return snmpCheck;
    }

    public void setSnmpCheck(boolean snmpCheck) {
        this.snmpCheck = snmpCheck;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Float getRxPowerFloat() {
        return rxPowerFloat;
    }

    public void setRxPowerFloat(Float rxPowerFloat) {
        this.rxPowerFloat = rxPowerFloat;
    }

    public Float getTxPowerFloat() {
        return txPowerFloat;
    }

    public void setTxPowerFloat(Float txPowerFloat) {
        this.txPowerFloat = txPowerFloat;
    }

    public Integer getWaveLen() {
        return waveLen;
    }

    public void setWaveLen(Integer waveLen) {
        this.waveLen = waveLen;
    }

    public Integer getCmcOpticalTemp() {
        return cmcOpticalTemp;
    }

    public void setCmcOpticalTemp(Integer cmcOpticalTemp) {
        this.cmcOpticalTemp = cmcOpticalTemp;
    }
}
