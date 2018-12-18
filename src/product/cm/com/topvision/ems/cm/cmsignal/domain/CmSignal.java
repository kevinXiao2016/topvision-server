/***********************************************************************
 * $Id: CmSignal.java,v1.0 2015-4-11 上午10:34:33 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmsignal.domain;

import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author vanzand
 * @created @2015-4-11-上午10:34:33
 * 
 */
public class CmSignal implements AliasesSuperType {
    private static final long serialVersionUID = -6494833052478187547L;
    private Long cmId;
    private Timestamp collectTime;
    private Double upChannelTx;// 上行电平
    private Double upChannelSnr; // 下行SNR
    private Double upChannelFrequency; // 上行信道频率
    private Double downChannelSnr; // 下行SNR
    private Double downChannelTx;// 下行电平
    private Double downChannelFrequency; // 下行信道频率

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public Double getDownChannelSnr() {
        return downChannelSnr;
    }

    public void setDownChannelSnr(Double downChannelSnr) {
        this.downChannelSnr = downChannelSnr;
    }

    public Double getDownChannelTx() {
        return downChannelTx;
    }

    public void setDownChannelTx(Double downChannelTx) {
        this.downChannelTx = downChannelTx;
    }

    public Double getUpChannelTx() {
        return upChannelTx;
    }

    public void setUpChannelTx(Double upChannelTx) {
        this.upChannelTx = upChannelTx;
    }

    public Double getDownChannelFrequency() {
        return downChannelFrequency;
    }

    public void setDownChannelFrequency(Double downChannelFrequency) {
        this.downChannelFrequency = downChannelFrequency;
    }

    public Double getUpChannelFrequency() {
        return upChannelFrequency;
    }

    public void setUpChannelFrequency(Double upChannelFrequency) {
        this.upChannelFrequency = upChannelFrequency;
    }

    public Double getUpChannelSnr() {
        return upChannelSnr;
    }

    public void setUpChannelSnr(Double upChannelSnr) {
        this.upChannelSnr = upChannelSnr;
    }

}
