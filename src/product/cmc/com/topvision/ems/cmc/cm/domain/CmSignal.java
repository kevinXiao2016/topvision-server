/***********************************************************************
 * $Id: CmSignal.java,v1.0 2013-10-31 下午8:21:55 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.sql.Timestamp;

/**
 * @author YangYi
 * @created @2013-10-31-下午8:21:55
 *
 */
public class CmSignal {
    private Long cmId;
    private String downChannelSnr; // 下行SNR
    private String downChannelTx;// 下行电平
    private String upChannelTx;// 上行电平
    private String upChannelSnr; // 上行SNR
    private String downChannelFrequency; // 下行信道频率
    private String upChannelFrequency; // 上行信道频率
    private Timestamp collectTime;
    private String lastRefreshTime;

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

    public String getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(String lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
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

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public String getUpChannelSnr() {
        return upChannelSnr;
    }

    public void setUpChannelSnr(String upChannelSnr) {
        this.upChannelSnr = upChannelSnr;
    }

}
