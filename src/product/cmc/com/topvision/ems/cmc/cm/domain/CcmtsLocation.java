/***********************************************************************
 * $Id: CcmtsLocation.java,v1.0 2013-11-2 下午1:26:25 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.text.DecimalFormat;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author haojie
 * @created @2013-11-2-下午1:26:25
 * 
 */
public class CcmtsLocation {
    private String name;// 别名
    private String mac;// mac
    private String ip;// IP
    private Integer state;// 在线状态
    private Long cmcIndex;// CMC_INDEX;
    private String interfaceInfo;// 机架号
    private Integer maxAlarmLevel;// 最高告警等级
    private Long upChannelSnr;// 连接CM的上行信道信噪,perfcmcsignalqualitylast表中的targetValue-snr
    private String upChannelSnrForunit;
    private Double upChannelInSpeed;// 连接CM的上行信道发送速率,perfcmcflowqualitylast表中的channelInSpeed
    private String upChannelInSpeedForunit;
    private Integer cmNumOnline;// CM在线数,perfchannelcmnumlast
    private Integer cmNumTotal;// CM总数, perfchannelcmnumlast

    DecimalFormat df = new DecimalFormat("#.00");

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public String getInterfaceInfo() {
        return interfaceInfo;
    }

    public void setCmcIndex(Long cmcIndex) {
        if (cmcIndex != null) {
            this.cmcIndex = cmcIndex;
            this.interfaceInfo = CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getCmcId(cmcIndex).toString();
        }
    }

    public void setInterfaceInfo(String interfaceInfo) {
        this.interfaceInfo = interfaceInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public Long getUpChannelSnr() {
        return upChannelSnr;
    }

    public void setUpChannelSnr(Long upChannelSnr) {
        this.upChannelSnr = upChannelSnr;
    }

    public Double getUpChannelInSpeed() {
        return upChannelInSpeed;
    }

    public void setUpChannelInSpeed(Double upChannelInSpeed) {
        this.upChannelInSpeed = Double.valueOf(df.format(upChannelInSpeed));
        if (upChannelInSpeed != null && upChannelInSpeed > 0) {
            if (upChannelInSpeed < 1000d) {
                this.upChannelInSpeedForunit = df.format(upChannelInSpeed) + "bps";
            } else if (upChannelInSpeed < 1000000d) {
                this.upChannelInSpeedForunit = df.format(upChannelInSpeed / 1000) + "Kbps";
            } else {
                this.upChannelInSpeedForunit = df.format(upChannelInSpeed / 1000000) + "Mbps";
            }
        } else if (upChannelInSpeed == 0) {
            this.upChannelInSpeedForunit = "0 bps";
        }
    }

    public Integer getCmNumOnline() {
        return cmNumOnline;
    }

    public void setCmNumOnline(Integer cmNumOnline) {
        this.cmNumOnline = cmNumOnline;
    }

    public Integer getCmNumTotal() {
        return cmNumTotal;
    }

    public void setCmNumTotal(Integer cmNumTotal) {
        this.cmNumTotal = cmNumTotal;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUpChannelSnrForunit() {
        if (upChannelSnr != null && upChannelSnr > 0) {
            Double value = Double.parseDouble(upChannelSnr.toString()) / 10;
            upChannelSnrForunit = df.format(value) + " dB";
        }
        return upChannelSnrForunit;
    }

    public void setUpChannelSnrForunit(String upChannelSnrForunit) {
        this.upChannelSnrForunit = upChannelSnrForunit;
    }

    public String getUpChannelInSpeedForunit() {
        return upChannelInSpeedForunit;
    }

    public void setUpChannelInSpeedForunit(String upChannelInSpeedForunit) {
        this.upChannelInSpeedForunit = upChannelInSpeedForunit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsLocation [name=");
        builder.append(name);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", state=");
        builder.append(state);
        builder.append(", maxAlarmLevel=");
        builder.append(maxAlarmLevel);
        builder.append(", upChannelSnr=");
        builder.append(upChannelSnr);
        builder.append(", upChannelSnrForunit=");
        builder.append(upChannelSnrForunit);
        builder.append(", upChannelInSpeed=");
        builder.append(upChannelInSpeed);
        builder.append(", upChannelInSpeedForunit=");
        builder.append(upChannelInSpeedForunit);
        builder.append(", cmNumOnline=");
        builder.append(cmNumOnline);
        builder.append(", cmNumTotal=");
        builder.append(cmNumTotal);
        builder.append("]");
        return builder.toString();
    }

}
