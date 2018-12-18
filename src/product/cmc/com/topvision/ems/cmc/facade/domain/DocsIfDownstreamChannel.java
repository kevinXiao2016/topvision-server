/***********************************************************************
 * $Id: DocsIfDownstreamChannel.java,v1.0 2013-4-28 上午10:13:43 $
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
 * @created @2013-4-28-上午10:13:43
 *
 */
public class DocsIfDownstreamChannel implements Serializable {
    private static final long serialVersionUID = 7034829237649438771L;
    //下行发射电频表
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.1")
    private Long docsIfDownChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.2")
    private Long docsIfDownChannelFrequency;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.3")
    private Long docsIfDownChannelWidth;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.4")
    private Integer docsIfDownChannelModulation;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.5")
    private Integer docsIfDownChannelInterleave;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.6")
    private Long docsIfDownChannelPower;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.7")
    private Integer docsIfDownChannelAnnex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.8")
    private Integer docsIfDownChannelStorageType;
    private Long downIfSpeed;//下行带宽
    private Long dsBps = -1L;
    private String dsBpsUnit = "--";
    private Long dsErrorRation = -1L;
    private String dsErrorRationUnit = "--";

    private final static DecimalFormat df = new DecimalFormat("0.00");
    private String docsIfDownChannelWidthForUnit = "--";
    private String downIfSpeedForUnit = "--";
    private String docsIfDownChannelFrequencyForUnit = "--";
    private String docsIfDownChannelPowerForUnit = "--";
    private String docsIfDownChannelModulationForUnit = "--";
    private String docsIfDownChannelIdString; //add by loyal 添加cmts下cm支持
    //用作后台计算用
    private String downChannelPower;

    private final static String[] DOWNMODULATIONTYPES = { "", "unknown", "QAM1024", "QAM64", "QAM256" };

    public Integer getDocsIfDownChannelModulation() {
        return docsIfDownChannelModulation;
    }

    public void setDocsIfDownChannelModulation(Integer docsIfDownChannelModulation) {
        this.docsIfDownChannelModulation = docsIfDownChannelModulation;
    }

    public Integer getDocsIfDownChannelInterleave() {
        return docsIfDownChannelInterleave;
    }

    public void setDocsIfDownChannelInterleave(Integer docsIfDownChannelInterleave) {
        this.docsIfDownChannelInterleave = docsIfDownChannelInterleave;
    }

    public Integer getDocsIfDownChannelAnnex() {
        return docsIfDownChannelAnnex;
    }

    public void setDocsIfDownChannelAnnex(Integer docsIfDownChannelAnnex) {
        this.docsIfDownChannelAnnex = docsIfDownChannelAnnex;
    }

    public Integer getDocsIfDownChannelStorageType() {
        return docsIfDownChannelStorageType;
    }

    public void setDocsIfDownChannelStorageType(Integer docsIfDownChannelStorageType) {
        this.docsIfDownChannelStorageType = docsIfDownChannelStorageType;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getDocsIfDownChannelId() {
        return docsIfDownChannelId;
    }

    public void setDocsIfDownChannelId(Long docsIfDownChannelId) {
        this.docsIfDownChannelId = docsIfDownChannelId;
        this.docsIfDownChannelIdString = docsIfDownChannelId.toString();
    }

    public Long getDocsIfDownChannelFrequency() {
        return docsIfDownChannelFrequency;
    }

    public void setDocsIfDownChannelFrequency(Long docsIfDownChannelFrequency) {
        this.docsIfDownChannelFrequency = docsIfDownChannelFrequency;
    }

    public Long getDocsIfDownChannelWidth() {
        return docsIfDownChannelWidth;
    }

    public void setDocsIfDownChannelWidth(Long docsIfDownChannelWidth) {
        this.docsIfDownChannelWidth = docsIfDownChannelWidth;
    }

    public Long getDocsIfDownChannelPower() {
        return docsIfDownChannelPower;
    }

    public void setDocsIfDownChannelPower(Long docsIfDownChannelPower) {
        this.docsIfDownChannelPower = docsIfDownChannelPower;
    }

    public Long getDownIfSpeed() {
        return downIfSpeed;
    }

    public void setDownIfSpeed(Long downIfSpeed) {
        this.downIfSpeed = downIfSpeed;
    }

    public String getDocsIfDownChannelWidthForUnit() {
        if (this.getDocsIfDownChannelWidth() != null) {
            double ChannelWidth = docsIfDownChannelWidth;
            docsIfDownChannelWidthForUnit = df.format(ChannelWidth / 1000000) /*+ " MHz"*/;
        }
        return docsIfDownChannelWidthForUnit;
    }

    public void setDocsIfDownChannelWidthForUnit(String docsIfDownChannelWidthForUnit) {
        this.docsIfDownChannelWidthForUnit = docsIfDownChannelWidthForUnit;
    }

    public String getDownIfSpeedForUnit() {
        if (this.getDownIfSpeed() != null) {
            double speed = this.downIfSpeed;
            this.downIfSpeedForUnit = df.format(speed / (1000 * 1000)) /*+ " Mbps"*/;
        }
        return downIfSpeedForUnit;
    }

    public void setDownIfSpeedForUnit(String downIfSpeedForUnit) {
        this.downIfSpeedForUnit = downIfSpeedForUnit;
    }

    public String getDocsIfDownChannelFrequencyForUnit() {
        if (this.docsIfDownChannelFrequency != null) {
            double downChannelFrequency = this.docsIfDownChannelFrequency;
            docsIfDownChannelFrequencyForUnit = df.format(downChannelFrequency / 1000000) /*+ " MHz"*/;
        }
        return docsIfDownChannelFrequencyForUnit;
    }

    public void setDocsIfDownChannelFrequencyForUnit(String docsIfDownChannelFrequencyForUnit) {
        this.docsIfDownChannelFrequencyForUnit = docsIfDownChannelFrequencyForUnit;
    }

    public String getDocsIfDownChannelPowerForUnit() {
        if (this.getDocsIfDownChannelPower() != null) {
            double downChannelPowerForUnit = docsIfDownChannelPower;
            double powerValue = UnitConfigConstant.parsePowerValue(downChannelPowerForUnit / 10);
            docsIfDownChannelPowerForUnit = df.format(powerValue) /*+ " dBmV"*/;
        }
        return docsIfDownChannelPowerForUnit;
    }

    public void setDocsIfDownChannelPowerForUnit(String docsIfDownChannelPowerForUnit) {
        this.docsIfDownChannelPowerForUnit = docsIfDownChannelPowerForUnit;
    }

    public String getDownChannelPower() {
        if (this.getDocsIfDownChannelPower() != null) {
            double downChannelPowerForUnit = docsIfDownChannelPower;
            downChannelPower = df.format(downChannelPowerForUnit / 10) /*+ " dBmV"*/;
        }
        return downChannelPower;
    }

    public void setDownChannelPower(String downChannelPower) {
        this.downChannelPower = downChannelPower;
    }

    public String getDocsIfDownChannelModulationForUnit() {
        if (docsIfDownChannelModulation != null && docsIfDownChannelModulation < DOWNMODULATIONTYPES.length) {
            docsIfDownChannelModulationForUnit = DOWNMODULATIONTYPES[docsIfDownChannelModulation];
        }
        return docsIfDownChannelModulationForUnit;
    }

    public void setDocsIfDownChannelModulationForUnit(String docsIfDownChannelModulationForUnit) {
        this.docsIfDownChannelModulationForUnit = docsIfDownChannelModulationForUnit;
    }

    public String getDocsIfDownChannelIdString() {
        return docsIfDownChannelIdString;
    }

    public void setDocsIfDownChannelIdString(String docsIfDownChannelIdString) {
        this.docsIfDownChannelIdString = docsIfDownChannelIdString;
    }

    public Long getDsBps() {
        return dsBps;
    }

    public void setDsBps(Long dsBps) {
        this.dsBps = dsBps;
    }

    public Long getDsErrorRation() {
        return dsErrorRation;
    }

    public void setDsErrorRation(Long dsErrorRation) {
        this.dsErrorRation = dsErrorRation;
    }

    public String getDsBpsUnit() {
        if (dsBps == -1) {
            return "--";
        }
        return String.valueOf(dsBps);
    }

    public void setDsBpsUnit(String dsBpsUnit) {
        this.dsBpsUnit = dsBpsUnit;
    }

    public String getDsErrorRationUnit() {
        if (dsErrorRation == -1) {
            return "--";
        }
        return String.valueOf(dsErrorRation);
    }

    public void setDsErrorRationUnit(String dsErrorRationUnit) {
        this.dsErrorRationUnit = dsErrorRationUnit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DocsIfDownstreamChannel [ifIndex=");
        builder.append(ifIndex);
        builder.append(", docsIfDownChannelId=");
        builder.append(docsIfDownChannelId);
        builder.append(", docsIfDownChannelFrequency=");
        builder.append(docsIfDownChannelFrequency);
        builder.append(", docsIfDownChannelWidth=");
        builder.append(docsIfDownChannelWidth);
        builder.append(", docsIfDownChannelModulation=");
        builder.append(docsIfDownChannelModulation);
        builder.append(", docsIfDownChannelInterleave=");
        builder.append(docsIfDownChannelInterleave);
        builder.append(", docsIfDownChannelPower=");
        builder.append(docsIfDownChannelPower);
        builder.append(", docsIfDownChannelAnnex=");
        builder.append(docsIfDownChannelAnnex);
        builder.append(", docsIfDownChannelStorageType=");
        builder.append(docsIfDownChannelStorageType);
        builder.append(", downIfSpeed=");
        builder.append(downIfSpeed);
        builder.append(", docsIfDownChannelWidthForUnit=");
        builder.append(docsIfDownChannelWidthForUnit);
        builder.append(", downIfSpeedForUnit=");
        builder.append(downIfSpeedForUnit);
        builder.append(", docsIfDownChannelFrequencyForUnit=");
        builder.append(docsIfDownChannelFrequencyForUnit);
        builder.append(", docsIfDownChannelPowerForUnit=");
        builder.append(docsIfDownChannelPowerForUnit);
        builder.append(", docsIfDownChannelModulationForUnit=");
        builder.append(docsIfDownChannelModulationForUnit);
        builder.append(", docsIfDownChannelIdString=");
        builder.append(docsIfDownChannelIdString);
        builder.append("]");
        return builder.toString();
    }

}
