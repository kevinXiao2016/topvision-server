/***********************************************************************
 * $Id: Eqam.java,v1.0 2016年5月6日 下午5:12:54 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author loyal
 * @created @2016年5月6日-下午5:12:54
 * 
 */
@Alias("ipqam")
public class Ipqam implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7908261744517193499L;
    public static final String[] ANNEXTYPES = { "", "unknown", "other", "annexA", "annexB", "annexC" };
    public static final String[] MODULATIONTYPES = { "", "unknown", "QAM1024", "QAM64", "QAM256" };
    public static final String[] INTERLEAVETYPES = { "", "unknown", "other", "(8, 16)", "(16, 8)", "(32, 4)",
            "(64, 2)", "(128, 1)", "(12, 17)" };
    public static final DecimalFormat df = new DecimalFormat("#.000");
    private Long entityId;
    private Long cmcId;
    private Long portId;
    private Long ifIndex;
    private Long channelId;
    private Integer qamChannelFrequency; // 频点
    private String ifDescr; // 信道描述
    private Integer qamChannelModulationFormat; // 调制方式
    private Integer qamChannelInterleaverMode; // 交织深度
    private Integer qamChannelPower; // 载波电平
    private Integer qamChannelAnnexMode; // annnex
    private Long qamChannelCommonOutputBw; // 信道总带宽
    private Long qamChannelCommonUtilization; // 信道利用率
    private Long qamChannelSymbolRate; // 符号率
    private Long qamChannelCommonOutputBwUsed; // 已使用带宽

    private String qamChannelFrequencyForunit;
    private String qamChannelAnnexModeName;
    private String qamChannelModulationFormatName;
    private String qamChannelInterleaverModeName;
    private String qamChannelPowerForunit;

    private String qamChannelCommonUtilizationString;
    private String qamChannelCommonOutputBwString;
    private String qamChannelCommonOutputBwUsedString;
    private String qamChannelSymbolRateString;

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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Integer getQamChannelFrequency() {
        return qamChannelFrequency;
    }

    public void setQamChannelFrequency(Integer qamChannelFrequency) {
        this.qamChannelFrequency = qamChannelFrequency;
        if (this.qamChannelFrequency != null) {
            double frequency = this.qamChannelFrequency;
            String freInfo = frequency / 1000000 + "";
            if (freInfo.matches("^(([0-9]+)[.]+([0-9]{0,6}))$")) {
                freInfo = freInfo + " MHz";
            } else {
                DecimalFormat df = new DecimalFormat("0.000000");
                double info = Double.parseDouble(df.format(frequency / 1000000));
                freInfo = info + " MHz";
            }
            this.setQamChannelFrequencyForunit(freInfo);
        }
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public Integer getQamChannelModulationFormat() {
        return qamChannelModulationFormat;
    }

    public void setQamChannelModulationFormat(Integer qamChannelModulationFormat) {
        this.qamChannelModulationFormat = qamChannelModulationFormat;
    }

    public Integer getQamChannelInterleaverMode() {
        return qamChannelInterleaverMode;
    }

    public void setQamChannelInterleaverMode(Integer qamChannelInterleaverMode) {
        this.qamChannelInterleaverMode = qamChannelInterleaverMode;
    }

    public Integer getQamChannelPower() {
        return qamChannelPower;
    }

    public void setQamChannelPower(Integer qamChannelPower) {
        if (qamChannelPower != null) {
            this.qamChannelPower = qamChannelPower;
            double powerValue = UnitConfigConstant.parsePowerValue((double) qamChannelPower / 10);
            this.qamChannelPowerForunit = powerValue + " " + UnitConfigConstant.get("elecLevelUnit");
        }
    }

    public Integer getQamChannelAnnexMode() {
        return qamChannelAnnexMode;
    }

    public void setQamChannelAnnexMode(Integer qamChannelAnnexMode) {
        this.qamChannelAnnexMode = qamChannelAnnexMode;
    }

    public Long getQamChannelCommonOutputBw() {
        return qamChannelCommonOutputBw;
    }

    public void setQamChannelCommonOutputBw(Long qamChannelCommonOutputBw) {
        this.qamChannelCommonOutputBw = qamChannelCommonOutputBw;
        if (qamChannelCommonOutputBw != null) {
            qamChannelCommonOutputBwString = NumberUtils.getIfSpeedStrTwoDot(qamChannelCommonOutputBw);
        }
    }

    public Long getQamChannelCommonUtilization() {
        return qamChannelCommonUtilization;
    }

    public void setQamChannelCommonUtilization(Long qamChannelCommonUtilization) {
        this.qamChannelCommonUtilization = qamChannelCommonUtilization;
        if (qamChannelCommonUtilization > 0) {
            qamChannelCommonUtilizationString = String.valueOf(Double.parseDouble(qamChannelCommonUtilization
                    .toString()) / 10) + Symbol.PERCENT;
        } else {
            qamChannelCommonUtilizationString = 0 + Symbol.PERCENT;
        }
    }

    public Long getQamChannelSymbolRate() {
        return qamChannelSymbolRate;
    }

    public void setQamChannelSymbolRate(Long qamChannelSymbolRate) {
        this.qamChannelSymbolRate = qamChannelSymbolRate;
        if (qamChannelSymbolRate != null) {
            qamChannelSymbolRateString = df.format(Double.parseDouble(qamChannelSymbolRate.toString()) / 1000000)
                    + " Msym/s";
        }
    }

    public Long getQamChannelCommonOutputBwUsed() {
        return qamChannelCommonOutputBwUsed;
    }

    public void setQamChannelCommonOutputBwUsed(Long qamChannelCommonOutputBwUsed) {
        this.qamChannelCommonOutputBwUsed = qamChannelCommonOutputBwUsed;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public String getQamChannelFrequencyForunit() {
        return qamChannelFrequencyForunit;
    }

    public void setQamChannelFrequencyForunit(String qamChannelFrequencyForunit) {
        this.qamChannelFrequencyForunit = qamChannelFrequencyForunit;
    }

    public String getQamChannelAnnexModeName() {
        if (qamChannelAnnexMode != null) {
            this.qamChannelAnnexModeName = ANNEXTYPES[qamChannelAnnexMode];
        }
        return qamChannelAnnexModeName;
    }

    public void setQamChannelAnnexModeName(String qamChannelAnnexModeName) {
        this.qamChannelAnnexModeName = qamChannelAnnexModeName;
    }

    public String getQamChannelModulationFormatName() {
        if (qamChannelModulationFormat != null) {
            this.qamChannelModulationFormatName = MODULATIONTYPES[qamChannelModulationFormat];
        }
        return qamChannelModulationFormatName;
    }

    public void setQamChannelModulationFormatName(String qamChannelModulationFormatName) {
        this.qamChannelModulationFormatName = qamChannelModulationFormatName;
    }

    public String getQamChannelInterleaverModeName() {
        if (qamChannelInterleaverMode != null) {
            this.qamChannelInterleaverModeName = INTERLEAVETYPES[qamChannelInterleaverMode];
        }
        return qamChannelInterleaverModeName;
    }

    public void setQamChannelInterleaverModeName(String qamChannelInterleaverModeName) {
        this.qamChannelInterleaverModeName = qamChannelInterleaverModeName;
    }

    public String getQamChannelPowerForunit() {
        return qamChannelPowerForunit;
    }

    public void setQamChannelPowerForunit(String qamChannelPowerForunit) {
        this.qamChannelPowerForunit = qamChannelPowerForunit;
    }

    public String getQamChannelCommonUtilizationString() {
        return qamChannelCommonUtilizationString;
    }

    public void setQamChannelCommonUtilizationString(String qamChannelCommonUtilizationString) {
        this.qamChannelCommonUtilizationString = qamChannelCommonUtilizationString;
    }

    public String getQamChannelCommonOutputBwString() {
        return qamChannelCommonOutputBwString;
    }

    public void setQamChannelCommonOutputBwString(String qamChannelCommonOutputBwString) {
        this.qamChannelCommonOutputBwString = qamChannelCommonOutputBwString;
    }

    public String getQamChannelCommonOutputBwUsedString() {
        if (qamChannelCommonOutputBw != null && qamChannelCommonUtilization != null) {
            Double bwUsed = Double.parseDouble(qamChannelCommonOutputBw.toString()) * qamChannelCommonUtilization
                    / 1000;
            qamChannelCommonOutputBwUsedString = NumberUtils.getIfSpeedStrTwoDot(bwUsed);
        }
        return qamChannelCommonOutputBwUsedString;
    }

    public void setQamChannelCommonOutputBwUsedString(String qamChannelCommonOutputBwUsedString) {
        this.qamChannelCommonOutputBwUsedString = qamChannelCommonOutputBwUsedString;
    }

    public String getQamChannelSymbolRateString() {
        return qamChannelSymbolRateString;
    }

    public void setQamChannelSymbolRateString(String qamChannelSymbolRateString) {
        this.qamChannelSymbolRateString = qamChannelSymbolRateString;
    }

}
