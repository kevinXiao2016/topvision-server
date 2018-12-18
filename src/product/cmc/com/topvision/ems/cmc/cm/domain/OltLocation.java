/***********************************************************************
 * $Id: OltLocation.java,v1.0 2013-11-2 下午1:25:11 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import com.topvision.framework.common.NumberUtils;

/**
 * @author haojie
 * @created @2013-11-2-下午1:25:11
 * 
 */
public class OltLocation {
    private String name;// 别名
    private String ip;// IP
    private Integer state;// 在线状态
    private Integer maxAlarmLevel;// 最高告警等级
    private Double ponOutSpeed;// 连接CCMTS的PON口发送速率,perfeponflowqualitylast表里的portOutSpeed，单位bps
    private String ponOutSpeedForunit;

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

    public Double getPonOutSpeed() {
        return ponOutSpeed;
    }

    public void setPonOutSpeed(Double ponOutSpeed) {
        this.ponOutSpeed = ponOutSpeed;
        if (ponOutSpeed != null) {
            this.ponOutSpeedForunit = NumberUtils.getIfSpeedStr(ponOutSpeed);
        }
    }

    public String getPonOutSpeedForunit() {
        return ponOutSpeedForunit;
    }

    public void setPonOutSpeedForunit(String ponOutSpeedForunit) {
        this.ponOutSpeedForunit = ponOutSpeedForunit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltLocation [name=");
        builder.append(name);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", state=");
        builder.append(state);
        builder.append(", maxAlarmLevel=");
        builder.append(maxAlarmLevel);
        builder.append(", ponOutSpeed=");
        builder.append(ponOutSpeed);
        builder.append(", ponOutSpeedForunit=");
        builder.append(ponOutSpeedForunit);
        builder.append("]");
        return builder.toString();
    }

}
