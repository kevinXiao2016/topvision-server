/***********************************************************************
 * $Id: CmcLinkQualityData.java,v1.0 2013-8-14 下午08:39:40 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.performance.facade.CmcLinkQuality;
import com.topvision.ems.cmc.performance.facade.CmcLinkQualityFor8800A;
import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2013-8-14-下午08:39:40
 *
 */
@Alias("cmcLinkQualityData")
public class CmcLinkQualityData implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1357078969749099497L;

    private String name;
    private Long cmcId;
    private Long portIndex;
    private String location;
    private Long optTemp;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.6.1.4")
    private Float optTempFloat;
    private Long optVoltage;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.6.1.3")
    private Float optVoltageFloat;
    private Long optCurrent;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.6.1.5")
    private Float optCurrentFloat;
    private Long optTxPower;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.6.1.1")
    private Float optTxPowerFloat;
    private Long optRePower;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.6.1.2")
    private Float optRePowerFloat;
    private Timestamp collectTime;
    private Long typeId;
    private String fromLastTime;
    private String mac;
    private String displayName;
    private String ip;
    private String uplinkDevice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        this.location = EponIndex.getOnuStringByIndex(portIndex).toString();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getOptTemp() {
        return optTemp;
    }

    public void setOptTemp(Long optTemp) {
        this.optTemp = optTemp;
    }

    public Long getOptVoltage() {
        return optVoltage;
    }

    public void setOptVoltage(Long optVoltage) {
        this.optVoltage = optVoltage;
    }

    public Long getOptCurrent() {
        return optCurrent;
    }

    public void setOptCurrent(Long optCurrent) {
        this.optCurrent = optCurrent;
    }

    public Long getOptTxPower() {
        return optTxPower;
    }

    public void setOptTxPower(Long optTxPower) {
        this.optTxPower = optTxPower;
    }

    public Long getOptRePower() {
        return optRePower;
    }

    public void setOptRePower(Long optRePower) {
        this.optRePower = optRePower;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public CmcLinkQualityData() {

    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getFromLastTime() {
        return fromLastTime;
    }

    public void setFromLastTime(String fromLastTime) {
        this.fromLastTime = fromLastTime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Float getOptTempFloat() {
        return optTempFloat;
    }

    public void setOptTempFloat(Float optTempFloat) {
        this.optTempFloat = optTempFloat;
    }

    public Float getOptVoltageFloat() {
        return optVoltageFloat;
    }

    public void setOptVoltageFloat(Float optVoltageFloat) {
        this.optVoltageFloat = optVoltageFloat;
    }

    public Float getOptCurrentFloat() {
        return optCurrentFloat;
    }

    public void setOptCurrentFloat(Float optCurrentFloat) {
        this.optCurrentFloat = optCurrentFloat;
    }

    public Float getOptTxPowerFloat() {
        return optTxPowerFloat;
    }

    public void setOptTxPowerFloat(Float optTxPowerFloat) {
        this.optTxPowerFloat = optTxPowerFloat;
    }

    public Float getOptRePowerFloat() {
        return optRePowerFloat;
    }

    public void setOptRePowerFloat(Float optRePowerFloat) {
        this.optRePowerFloat = optRePowerFloat;
    }

    public CmcLinkQualityData(CmcLinkQuality perf) {
        this.cmcId = perf.getCmcId();
        this.portIndex = perf.getCmcIndex();
        this.optCurrent = perf.getOpticalTransceiverBiasCurrent();
        this.optRePower = perf.getOpticalTransceiverRxPower();
        this.optTemp = perf.getOpticalTransceiverTemperature();
        this.optTxPower = perf.getOpticalTransceiverTxPower();
        this.optVoltage = perf.getOpticalTransceiverVoltage();
        this.collectTime = perf.getCollectTime();
    }

    public CmcLinkQualityData(CmcLinkQualityFor8800A perf) {
        this.cmcId = perf.getCmcId();
        this.portIndex = perf.getOnuIndex();
        this.optCurrent = perf.getOnuBiasCurrent();
        this.optRePower = perf.getOnuReceivedOpticalPower();
        this.optTemp = perf.getOnuWorkingTemperature();
        this.optTxPower = perf.getOnuTramsmittedOpticalPower();
        this.optVoltage = perf.getOnuWorkingVoltage();
        this.collectTime = perf.getCollectTime();
    }

    public String getDtStr() {
        return DateUtils.format(this.collectTime);
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcLinkQualityData [name=");
        builder.append(name);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", location=");
        builder.append(location);
        builder.append(", optTemp=");
        builder.append(optTemp);
        builder.append(", optVoltage=");
        builder.append(optVoltage);
        builder.append(", optCurrent=");
        builder.append(optCurrent);
        builder.append(", optTxPower=");
        builder.append(optTxPower);
        builder.append(", optRePower=");
        builder.append(optRePower);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", fromLastTime=");
        builder.append(fromLastTime);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append("]");
        return builder.toString();
    }

}
