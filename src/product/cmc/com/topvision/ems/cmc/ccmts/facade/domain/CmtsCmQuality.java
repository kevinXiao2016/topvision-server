/***********************************************************************
 * $ CmtsCmQuality.java,v1.0 14-5-11 下午2:46 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author jay
 * @created @14-5-11-下午2:46
 */
@Alias("cmtsCmQuality")
public class CmtsCmQuality implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long statusIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.4")
    private Long statusDownChannelIfIndex; // 下行通道索引
//    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.6")
    // private Long statusRxPower; // CMTS侧接收电平
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.9")
    private Integer statusValue; // 当前状态 1: other 2: ranging 3:
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.13")
    private Long statusSignalNoise; // CMTS侧信噪比

    public Long getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    public Long getStatusDownChannelIfIndex() {
        return statusDownChannelIfIndex;
    }

    public void setStatusDownChannelIfIndex(Long statusDownChannelIfIndex) {
        this.statusDownChannelIfIndex = statusDownChannelIfIndex;
    }

//    public Long getStatusRxPower() {
//        return statusRxPower;
//    }
//
//    public void setStatusRxPower(Long statusRxPower) {
//        this.statusRxPower = statusRxPower;
//    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public Long getStatusSignalNoise() {
        return statusSignalNoise;
    }

    public void setStatusSignalNoise(Long statusSignalNoise) {
        this.statusSignalNoise = statusSignalNoise;
    }

    @Override
    public String toString() {
        return "CmtsCmQuality{" +
                "statusIndex=" + statusIndex +
                ", statusDownChannelIfIndex=" + statusDownChannelIfIndex +
                ", statusValue=" + statusValue +
                ", statusSignalNoise=" + statusSignalNoise +
                '}';
    }
}
