/***********************************************************************
 * $Id: CmcEqamStatus.java,v1.0 2016年5月3日 上午11:01:15 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2016年5月3日-上午11:01:15
 * 
 */
@Alias("cmcEqamStatus")
public class CmcEqamStatus implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 2001676303782701712L;
    private Long entityId;
    private Long cmcId;
    private Long portId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.1.1.1")
    private Long qamChannelFrequency; // 频点
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.1.1.2")
    private Integer qamChannelModulationFormat; // 调制方式
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.1.1.4")
    private Integer qamChannelInterleaverMode; // 交织深度
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.1.1.5")
    private Integer qamChannelPower; // 载波电平
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.1.1.6")
    private Integer qamChannelSquelch; // 信道是否静音
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.1.1.7")
    private Integer qamChannelContWaveMode; // CW模式是否开启
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.1.1.8")
    private Integer qamChannelAnnexMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.2.1.1")
    private Long qamChannelCommonOutputBw; // 信道总带宽
    @SnmpProperty(oid = "1.3.6.1.4.1.5591.1.11.5.3.1.1.2.1.2")
    private Long qamChannelCommonUtilization; // 信道利用率
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.24.1.1.2")
    private Long qamChannelSymbolRate; // 信道符号率

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getQamChannelFrequency() {
        return qamChannelFrequency;
    }

    public void setQamChannelFrequency(Long qamChannelFrequency) {
        this.qamChannelFrequency = qamChannelFrequency;
    }

    public Long getQamChannelCommonOutputBw() {
        return qamChannelCommonOutputBw;
    }

    public void setQamChannelCommonOutputBw(Long qamChannelCommonOutputBw) {
        this.qamChannelCommonOutputBw = qamChannelCommonOutputBw;
    }

    public Long getQamChannelCommonUtilization() {
        return qamChannelCommonUtilization;
    }

    public void setQamChannelCommonUtilization(Long qamChannelCommonUtilization) {
        this.qamChannelCommonUtilization = qamChannelCommonUtilization;
    }

    public Long getQamChannelSymbolRate() {
        return qamChannelSymbolRate;
    }

    public void setQamChannelSymbolRate(Long qamChannelSymbolRate) {
        this.qamChannelSymbolRate = qamChannelSymbolRate;
    }

    public Integer getQamChannelModulationFormat() {
        return qamChannelModulationFormat;
    }

    public void setQamChannelModulationFormat(Integer qamChannelModulationFormat) {
        this.qamChannelModulationFormat = qamChannelModulationFormat;
    }

    public Integer getQamChannelPower() {
        return qamChannelPower;
    }

    public void setQamChannelPower(Integer qamChannelPower) {
        this.qamChannelPower = qamChannelPower;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Integer getQamChannelInterleaverMode() {
        return qamChannelInterleaverMode;
    }

    public void setQamChannelInterleaverMode(Integer qamChannelInterleaverMode) {
        this.qamChannelInterleaverMode = qamChannelInterleaverMode;
    }

    public Integer getQamChannelSquelch() {
        return qamChannelSquelch;
    }

    public void setQamChannelSquelch(Integer qamChannelSquelch) {
        this.qamChannelSquelch = qamChannelSquelch;
    }

    public Integer getQamChannelContWaveMode() {
        return qamChannelContWaveMode;
    }

    public void setQamChannelContWaveMode(Integer qamChannelContWaveMode) {
        this.qamChannelContWaveMode = qamChannelContWaveMode;
    }

    public Integer getQamChannelAnnexMode() {
        return qamChannelAnnexMode;
    }

    public void setQamChannelAnnexMode(Integer qamChannelAnnexMode) {
        this.qamChannelAnnexMode = qamChannelAnnexMode;
    }

}
