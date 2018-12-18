/***********************************************************************
 * $Id: DocsIfUpstreamChannel.java,v1.0 2013-4-28 上午10:20:21 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author haojie
 * @created @2013-4-28-上午10:20:21
 *
 */
public class DocsIfUpstreamChannel implements Serializable {
    private static final long serialVersionUID = -1371578805492462866L;
    //CM上行发射电频表
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.1")
    private Integer docsIfUpChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.2")
    private Long docsIfUpChannelFrequency;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.3")
    private Long docsIfUpChannelWidth;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.4")
    private Long docsIfUpChannelModulationProfile;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.5")
    private Long docsIfUpChannelSlotSize;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.6")
    private Long docsIfUpChannelTxTimingOffset;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.7")
    private Integer docsIfUpChannelRangingBackoffStart;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.8")
    private Integer docsIfUpChannelRangingBackoffEnd;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.9")
    private Integer docsIfUpChannelTxBackoffStart;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.10")
    private Integer docsIfUpChannelTxBackoffEnd;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.11")
    private Long docsIfUpChannelScdmaActiveCodes;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.12")
    private Integer docsIfUpChannelScdmaCodesPerSlot;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.13")
    private Integer docsIfUpChannelScdmaFrameSize;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.14")
    private Long docsIfUpChannelScdmaHoppingSeed;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.15")
    private Integer docsIfUpChannelType;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.16")
    private Integer docsIfUpChannelCloneFrom;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.17")
    private Integer docsIfUpChannelUpdate;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.18")
    private Integer docsIfUpChannelStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.19")
    private Integer docsIfUpChannelPreEqEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.2.1.1")
    private Integer docsIf3CmStatusUsTxPower;//3.0上行发射电频
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.2.2.1.3")
    private Integer docsIfCmStatusTxPower;//2.0上行发射电频
    private String txPower;//根据cm类型获取的电平值
    private Long upIfSpeed;
    private Long usBps = -1L;
    private String usBpsUnit;
    private Long usErrorRation = -1L;
    private String usErrorRationUnit;

    private String docsIfUpChannelWidthForUnit = "--";
    private String upIfSpeedForUnit = "--";
    private String docsIfUpChannelFrequencyForUnit = "--";
    private String docsIf3CmStatusUsTxPowerForUnit = "--";
    private String docsIfCmStatusTxPowerForUnit = "--";
    private String docsIfUpChannelIdString;
    //用作后台计算使用，不进行转换
    private String cm3UpChannelPower;
    private String upChannelPower;

    private final static DecimalFormat df = new DecimalFormat("0.00");

    public Integer getDocsIfUpChannelId() {
        return docsIfUpChannelId;
    }

    public void setDocsIfUpChannelId(Integer docsIfUpChannelId) {
        this.docsIfUpChannelId = docsIfUpChannelId;
        this.docsIfUpChannelIdString = docsIfUpChannelId.toString();
    }

    public Integer getDocsIfUpChannelRangingBackoffStart() {
        return docsIfUpChannelRangingBackoffStart;
    }

    public void setDocsIfUpChannelRangingBackoffStart(Integer docsIfUpChannelRangingBackoffStart) {
        this.docsIfUpChannelRangingBackoffStart = docsIfUpChannelRangingBackoffStart;
    }

    public Integer getDocsIfUpChannelRangingBackoffEnd() {
        return docsIfUpChannelRangingBackoffEnd;
    }

    public void setDocsIfUpChannelRangingBackoffEnd(Integer docsIfUpChannelRangingBackoffEnd) {
        this.docsIfUpChannelRangingBackoffEnd = docsIfUpChannelRangingBackoffEnd;
    }

    public Integer getDocsIfUpChannelTxBackoffStart() {
        return docsIfUpChannelTxBackoffStart;
    }

    public void setDocsIfUpChannelTxBackoffStart(Integer docsIfUpChannelTxBackoffStart) {
        this.docsIfUpChannelTxBackoffStart = docsIfUpChannelTxBackoffStart;
    }

    public Integer getDocsIfUpChannelTxBackoffEnd() {
        return docsIfUpChannelTxBackoffEnd;
    }

    public void setDocsIfUpChannelTxBackoffEnd(Integer docsIfUpChannelTxBackoffEnd) {
        this.docsIfUpChannelTxBackoffEnd = docsIfUpChannelTxBackoffEnd;
    }

    public Integer getDocsIfUpChannelScdmaCodesPerSlot() {
        return docsIfUpChannelScdmaCodesPerSlot;
    }

    public void setDocsIfUpChannelScdmaCodesPerSlot(Integer docsIfUpChannelScdmaCodesPerSlot) {
        this.docsIfUpChannelScdmaCodesPerSlot = docsIfUpChannelScdmaCodesPerSlot;
    }

    public Integer getDocsIfUpChannelScdmaFrameSize() {
        return docsIfUpChannelScdmaFrameSize;
    }

    public void setDocsIfUpChannelScdmaFrameSize(Integer docsIfUpChannelScdmaFrameSize) {
        this.docsIfUpChannelScdmaFrameSize = docsIfUpChannelScdmaFrameSize;
    }

    public Integer getDocsIfUpChannelType() {
        return docsIfUpChannelType;
    }

    public void setDocsIfUpChannelType(Integer docsIfUpChannelType) {
        this.docsIfUpChannelType = docsIfUpChannelType;
    }

    public Integer getDocsIfUpChannelCloneFrom() {
        return docsIfUpChannelCloneFrom;
    }

    public void setDocsIfUpChannelCloneFrom(Integer docsIfUpChannelCloneFrom) {
        this.docsIfUpChannelCloneFrom = docsIfUpChannelCloneFrom;
    }

    public Integer getDocsIfUpChannelUpdate() {
        return docsIfUpChannelUpdate;
    }

    public void setDocsIfUpChannelUpdate(Integer docsIfUpChannelUpdate) {
        this.docsIfUpChannelUpdate = docsIfUpChannelUpdate;
    }

    public Integer getDocsIfUpChannelStatus() {
        return docsIfUpChannelStatus;
    }

    public void setDocsIfUpChannelStatus(Integer docsIfUpChannelStatus) {
        this.docsIfUpChannelStatus = docsIfUpChannelStatus;
    }

    public Integer getDocsIfUpChannelPreEqEnable() {
        return docsIfUpChannelPreEqEnable;
    }

    public void setDocsIfUpChannelPreEqEnable(Integer docsIfUpChannelPreEqEnable) {
        this.docsIfUpChannelPreEqEnable = docsIfUpChannelPreEqEnable;
    }

    public Integer getDocsIf3CmStatusUsTxPower() {
        return docsIf3CmStatusUsTxPower;
    }

    public void setDocsIf3CmStatusUsTxPower(Integer docsIf3CmStatusUsTxPower) {
        this.docsIf3CmStatusUsTxPower = docsIf3CmStatusUsTxPower;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getDocsIfUpChannelFrequency() {
        return docsIfUpChannelFrequency;
    }

    public void setDocsIfUpChannelFrequency(Long docsIfUpChannelFrequency) {
        this.docsIfUpChannelFrequency = docsIfUpChannelFrequency;
    }

    public Long getDocsIfUpChannelWidth() {
        return docsIfUpChannelWidth;
    }

    public void setDocsIfUpChannelWidth(Long docsIfUpChannelWidth) {
        this.docsIfUpChannelWidth = docsIfUpChannelWidth;
    }

    public Long getDocsIfUpChannelModulationProfile() {
        return docsIfUpChannelModulationProfile;
    }

    public void setDocsIfUpChannelModulationProfile(Long docsIfUpChannelModulationProfile) {
        this.docsIfUpChannelModulationProfile = docsIfUpChannelModulationProfile;
    }

    public Long getDocsIfUpChannelSlotSize() {
        return docsIfUpChannelSlotSize;
    }

    public void setDocsIfUpChannelSlotSize(Long docsIfUpChannelSlotSize) {
        this.docsIfUpChannelSlotSize = docsIfUpChannelSlotSize;
    }

    public Long getDocsIfUpChannelTxTimingOffset() {
        return docsIfUpChannelTxTimingOffset;
    }

    public void setDocsIfUpChannelTxTimingOffset(Long docsIfUpChannelTxTimingOffset) {
        this.docsIfUpChannelTxTimingOffset = docsIfUpChannelTxTimingOffset;
    }

    public Long getDocsIfUpChannelScdmaActiveCodes() {
        return docsIfUpChannelScdmaActiveCodes;
    }

    public void setDocsIfUpChannelScdmaActiveCodes(Long docsIfUpChannelScdmaActiveCodes) {
        this.docsIfUpChannelScdmaActiveCodes = docsIfUpChannelScdmaActiveCodes;
    }

    public Long getDocsIfUpChannelScdmaHoppingSeed() {
        return docsIfUpChannelScdmaHoppingSeed;
    }

    public void setDocsIfUpChannelScdmaHoppingSeed(Long docsIfUpChannelScdmaHoppingSeed) {
        this.docsIfUpChannelScdmaHoppingSeed = docsIfUpChannelScdmaHoppingSeed;
    }

    public Long getUpIfSpeed() {
        return upIfSpeed;
    }

    public void setUpIfSpeed(Long upIfSpeed) {
        this.upIfSpeed = upIfSpeed;
    }

    public String getDocsIfUpChannelWidthForUnit() {
        if (this.getDocsIfUpChannelWidth() != null) {
            DecimalFormat df = new DecimalFormat("0.0");
            double ChannelWidth = docsIfUpChannelWidth;
            docsIfUpChannelWidthForUnit = df.format(ChannelWidth / 1000000);
        }
        return docsIfUpChannelWidthForUnit;
    }

    public void setDocsIfUpChannelWidthForUnit(String docsIfUpChannelWidthForUnit) {
        this.docsIfUpChannelWidthForUnit = docsIfUpChannelWidthForUnit;
    }

    public String getUpIfSpeedForUnit() {
        if (this.getUpIfSpeed() != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            double speed = this.upIfSpeed;
            this.upIfSpeedForUnit = df.format(speed / (1000 * 1000));
        }
        return upIfSpeedForUnit;
    }

    public void setUpIfSpeedForUnit(String upIfSpeedForUnit) {
        this.upIfSpeedForUnit = upIfSpeedForUnit;
    }

    public String getDocsIfUpChannelFrequencyForUnit() {
        if (this.getDocsIfUpChannelFrequency() != null) {
            DecimalFormat df = new DecimalFormat("0.0");
            double FrequencyForunit = docsIfUpChannelFrequency;
            docsIfUpChannelFrequencyForUnit = df.format(FrequencyForunit / 1000000);
        }
        return docsIfUpChannelFrequencyForUnit;
    }

    public void setDocsIfUpChannelFrequencyForUnit(String docsIfUpChannelFrequencyForUnit) {
        this.docsIfUpChannelFrequencyForUnit = docsIfUpChannelFrequencyForUnit;
    }

    public String getDocsIf3CmStatusUsTxPowerForUnit() {
        if (this.getDocsIf3CmStatusUsTxPower() != null) {
            double txPowerForUnit = docsIf3CmStatusUsTxPower;
            double powerValue = UnitConfigConstant.parsePowerValue(txPowerForUnit / 10);
            docsIf3CmStatusUsTxPowerForUnit = df.format(powerValue);
        }
        return docsIf3CmStatusUsTxPowerForUnit;
    }

    public void setDocsIf3CmStatusUsTxPowerForUnit(String docsIf3CmStatusUsTxPowerForUnit) {
        this.docsIf3CmStatusUsTxPowerForUnit = docsIf3CmStatusUsTxPowerForUnit;
    }

    public String getCm3UpChannelPower() {
        if (this.getDocsIf3CmStatusUsTxPower() != null) {
            double txPowerForUnit = docsIf3CmStatusUsTxPower;
            cm3UpChannelPower = df.format(txPowerForUnit / 10);
        }
        return cm3UpChannelPower;
    }

    public void setCm3UpChannelPower(String cm3UpChannelPower) {
        this.cm3UpChannelPower = cm3UpChannelPower;
    }

    public Integer getDocsIfCmStatusTxPower() {
        return docsIfCmStatusTxPower;
    }

    public void setDocsIfCmStatusTxPower(Integer docsIfCmStatusTxPower) {
        this.docsIfCmStatusTxPower = docsIfCmStatusTxPower;
    }

    public String getDocsIfCmStatusTxPowerForUnit() {
        Integer txPowerForUnitInteger = docsIfCmStatusTxPower;
        if (docsIfCmStatusTxPower == null) {
            txPowerForUnitInteger = docsIf3CmStatusUsTxPower;
        }
        if (txPowerForUnitInteger != null) {
            double txPowerForUnit = txPowerForUnitInteger;
            double powerValue = UnitConfigConstant.parsePowerValue(txPowerForUnit / 10);
            docsIfCmStatusTxPowerForUnit = df.format(powerValue);
        }
        return docsIfCmStatusTxPowerForUnit;
    }

    public void setDocsIfCmStatusTxPowerForUnit(String docsIfCmStatusTxPowerForUnit) {
        this.docsIfCmStatusTxPowerForUnit = docsIfCmStatusTxPowerForUnit;
    }

    public String getUpChannelPower() {
        if (this.getDocsIfCmStatusTxPower() != null) {
            double txPowerForUnit = docsIfCmStatusTxPower;
            upChannelPower = df.format(txPowerForUnit / 10);
        }
        return upChannelPower;
    }

    public void setUpChannelPower(String upChannelPower) {
        this.upChannelPower = upChannelPower;
    }

    public String getTxPower() {
        return txPower;
    }

    public void setTxPower(String txPower) {
        this.txPower = txPower;
    }
    
    public String getDocsIfUpChannelIdString() {
        return docsIfUpChannelIdString;
    }

    public void setDocsIfUpChannelIdString(String docsIfUpChannelIdString) {
        this.docsIfUpChannelIdString = docsIfUpChannelIdString;
    }

    public Long getUsBps() {
        return usBps;
    }

    public void setUsBps(Long usBps) {
        this.usBps = usBps;
    }

    public Long getUsErrorRation() {
        return usErrorRation;
    }

    public void setUsErrorRation(Long usErrorRation) {
        this.usErrorRation = usErrorRation;
    }

    public String getUsBpsUnit() {
        if (usBps == -1) {
            return "--";
        }
        return String.valueOf(usBps);
    }

    public void setUsBpsUnit(String usBpsUnit) {
        this.usBpsUnit = usBpsUnit;
    }

    public String getUsErrorRationUnit() {
        if (usErrorRation == -1) {
            return "--";
        }
        return String.valueOf(usErrorRation);
    }

    public void setUsErrorRationUnit(String usErrorRationUnit) {
        this.usErrorRationUnit = usErrorRationUnit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DocsIfUpstreamChannel [ifIndex=");
        builder.append(ifIndex);
        builder.append(", docsIfUpChannelId=");
        builder.append(docsIfUpChannelId);
        builder.append(", docsIfUpChannelFrequency=");
        builder.append(docsIfUpChannelFrequency);
        builder.append(", docsIfUpChannelWidth=");
        builder.append(docsIfUpChannelWidth);
        builder.append(", docsIfUpChannelModulationProfile=");
        builder.append(docsIfUpChannelModulationProfile);
        builder.append(", docsIfUpChannelSlotSize=");
        builder.append(docsIfUpChannelSlotSize);
        builder.append(", docsIfUpChannelTxTimingOffset=");
        builder.append(docsIfUpChannelTxTimingOffset);
        builder.append(", docsIfUpChannelRangingBackoffStart=");
        builder.append(docsIfUpChannelRangingBackoffStart);
        builder.append(", docsIfUpChannelRangingBackoffEnd=");
        builder.append(docsIfUpChannelRangingBackoffEnd);
        builder.append(", docsIfUpChannelTxBackoffStart=");
        builder.append(docsIfUpChannelTxBackoffStart);
        builder.append(", docsIfUpChannelTxBackoffEnd=");
        builder.append(docsIfUpChannelTxBackoffEnd);
        builder.append(", docsIfUpChannelScdmaActiveCodes=");
        builder.append(docsIfUpChannelScdmaActiveCodes);
        builder.append(", docsIfUpChannelScdmaCodesPerSlot=");
        builder.append(docsIfUpChannelScdmaCodesPerSlot);
        builder.append(", docsIfUpChannelScdmaFrameSize=");
        builder.append(docsIfUpChannelScdmaFrameSize);
        builder.append(", docsIfUpChannelScdmaHoppingSeed=");
        builder.append(docsIfUpChannelScdmaHoppingSeed);
        builder.append(", docsIfUpChannelType=");
        builder.append(docsIfUpChannelType);
        builder.append(", docsIfUpChannelCloneFrom=");
        builder.append(docsIfUpChannelCloneFrom);
        builder.append(", docsIfUpChannelUpdate=");
        builder.append(docsIfUpChannelUpdate);
        builder.append(", docsIfUpChannelStatus=");
        builder.append(docsIfUpChannelStatus);
        builder.append(", docsIfUpChannelPreEqEnable=");
        builder.append(docsIfUpChannelPreEqEnable);
        builder.append(", docsIf3CmStatusUsTxPower=");
        builder.append(docsIf3CmStatusUsTxPower);
        builder.append(", docsIfCmStatusTxPower=");
        builder.append(docsIfCmStatusTxPower);
        builder.append(", txPower=");
        builder.append(txPower);
        builder.append(", upIfSpeed=");
        builder.append(upIfSpeed);
        builder.append(", docsIfUpChannelWidthForUnit=");
        builder.append(docsIfUpChannelWidthForUnit);
        builder.append(", upIfSpeedForUnit=");
        builder.append(upIfSpeedForUnit);
        builder.append(", docsIfUpChannelFrequencyForUnit=");
        builder.append(docsIfUpChannelFrequencyForUnit);
        builder.append(", docsIf3CmStatusUsTxPowerForUnit=");
        builder.append(docsIf3CmStatusUsTxPowerForUnit);
        builder.append(", docsIfCmStatusTxPowerForUnit=");
        builder.append(docsIfCmStatusTxPowerForUnit);
        builder.append(", docsIfUpChannelIdString=");
        builder.append(docsIfUpChannelIdString);
        builder.append("]");
        return builder.toString();
    }

}
