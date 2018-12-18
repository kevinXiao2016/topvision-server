/***********************************************************************
 * $Id: CmLocation.java,v1.0 2013-11-2 下午1:28:12 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.text.DecimalFormat;

import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author haojie
 * @created @2013-11-2-下午1:28:12
 * 
 */
public class CmLocation {
    private String ip;// IP
    private String mac;// MAC
    private Integer state;// 在线状态
    private Integer maxAlarmLevel;// 最高告警等级
    private String downChannelSnr; // 下行SNR
    private String downChannelSnrForunit;
    private String downChannelTx;// 下行电平
    private String downChannelTxForunit;
    private String upChannelTx;// 上行电平
    private String upChannelTxForunit;
    private Long upChannelSnr;// 上行SNR
    private String upChannelSnrForunit;
    private String downChannelFrequency; // 下行信道频率
    private String downChannelFrequencyForunit;
    private String upChannelFrequency; // 上行信道频率
    private String upChannelFrequencyForunit;
    private Long cpeNum;// CPE数量,cmcpe表

    DecimalFormat df = new DecimalFormat("#.00");

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getMaxAlarmLevel() {
        return maxAlarmLevel;
    }

    public void setMaxAlarmLevel(Integer maxAlarmLevel) {
        this.maxAlarmLevel = maxAlarmLevel;
    }

    public String getDownChannelSnr() {
        return downChannelSnr;
    }

    public void setDownChannelSnr(String downChannelSnr) {
        this.downChannelSnr = downChannelSnr;
    }

    public String getDownChannelTx() {
        return downChannelTx;
    }

    public void setDownChannelTx(String downChannelTx) {
        this.downChannelTx = downChannelTx;
    }

    public String getUpChannelTx() {
        return upChannelTx;
    }

    public void setUpChannelTx(String upChannelTx) {
        this.upChannelTx = upChannelTx;
    }

    public Long getUpChannelSnr() {
        return upChannelSnr;
    }

    public void setUpChannelSnr(Long upChannelSnr) {
        this.upChannelSnr = upChannelSnr;
    }

    public String getDownChannelFrequency() {
        return downChannelFrequency;
    }

    public void setDownChannelFrequency(String downChannelFrequency) {
        this.downChannelFrequency = downChannelFrequency;
    }

    public String getUpChannelFrequency() {
        return upChannelFrequency;
    }

    public void setUpChannelFrequency(String upChannelFrequency) {
        this.upChannelFrequency = upChannelFrequency;
    }

    public Long getCpeNum() {
        return cpeNum;
    }

    public void setCpeNum(Long cpeNum) {
        this.cpeNum = cpeNum;
    }

    public String getDownChannelSnrForunit() {
        if (downChannelSnr != null) {
            downChannelSnrForunit = df.format(Double.valueOf(downChannelSnr)) + " dB";
        }
        return downChannelSnrForunit;
    }

    public void setDownChannelSnrForunit(String downChannelSnrForunit) {
        this.downChannelSnrForunit = downChannelSnrForunit;
    }

    public String getDownChannelTxForunit() {
        if (downChannelTx != null) {
            this.downChannelTxForunit = df.format(UnitConfigConstant.parsePowerValue(Double.valueOf(downChannelTx)))
                    + " " + UnitConfigConstant.get("elecLevelUnit");
        }
        return downChannelTxForunit;
    }

    public void setDownChannelTxForunit(String downChannelTxForunit) {
        this.downChannelTxForunit = downChannelTxForunit;
    }

    public String getUpChannelTxForunit() {
        if (upChannelTx != null) {
            this.upChannelTxForunit = df.format(UnitConfigConstant.parsePowerValue(Double.valueOf(upChannelTx))) + " "
                    + UnitConfigConstant.get("elecLevelUnit");
        }
        return upChannelTxForunit;
    }

    public void setUpChannelTxForunit(String upChannelTxForunit) {
        this.upChannelTxForunit = upChannelTxForunit;
    }

    public String getUpChannelSnrForunit() {
        if (upChannelSnr != null && upChannelSnr>0) {
            upChannelSnrForunit = df.format((double) upChannelSnr / 10) + " dB";
        }
        return upChannelSnrForunit;
    }

    public void setUpChannelSnrForunit(String upChannelSnrForunit) {
        this.upChannelSnrForunit = upChannelSnrForunit;
    }

    public String getDownChannelFrequencyForunit() {
        if (downChannelFrequency != null) {
            downChannelFrequencyForunit = df.format(Double.valueOf(downChannelFrequency)) + " MHz";
        }
        return downChannelFrequencyForunit;
    }

    public void setDownChannelFrequencyForunit(String downChannelFrequencyForunit) {
        this.downChannelFrequencyForunit = downChannelFrequencyForunit;
    }

    public String getUpChannelFrequencyForunit() {
        if (upChannelFrequency != null) {
            upChannelFrequencyForunit = df.format(Double.valueOf(upChannelFrequency)) + " MHz";
        }
        return upChannelFrequencyForunit;
    }

    public void setUpChannelFrequencyForunit(String upChannelFrequencyForunit) {
        this.upChannelFrequencyForunit = upChannelFrequencyForunit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmLocation [ip=");
        builder.append(ip);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", state=");
        builder.append(state);
        builder.append(", maxAlarmLevel=");
        builder.append(maxAlarmLevel);
        builder.append(", downChannelSnr=");
        builder.append(downChannelSnr);
        builder.append(", downChannelSnrForunit=");
        builder.append(downChannelSnrForunit);
        builder.append(", downChannelTx=");
        builder.append(downChannelTx);
        builder.append(", downChannelTxForunit=");
        builder.append(downChannelTxForunit);
        builder.append(", upChannelTx=");
        builder.append(upChannelTx);
        builder.append(", upChannelTxForunit=");
        builder.append(upChannelTxForunit);
        builder.append(", upChannelSnr=");
        builder.append(upChannelSnr);
        builder.append(", upChannelSnrForunit=");
        builder.append(upChannelSnrForunit);
        builder.append(", downChannelFrequency=");
        builder.append(downChannelFrequency);
        builder.append(", downChannelFrequencyForunit=");
        builder.append(downChannelFrequencyForunit);
        builder.append(", upChannelFrequency=");
        builder.append(upChannelFrequency);
        builder.append(", upChannelFrequencyForunit=");
        builder.append(upChannelFrequencyForunit);
        builder.append(", cpeNum=");
        builder.append(cpeNum);
        builder.append("]");
        return builder.toString();
    }

}
