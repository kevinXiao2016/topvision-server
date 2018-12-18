/***********************************************************************
 * $Id: CmDownChannelSignal.java,v1.0 2016年9月27日 上午11:01:20 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.sql.Timestamp;

/**
 * @author vanzand
 * @created @2016年9月27日-上午11:01:20
 *
 */
public class CmDownChannelSignal {
    private Long cmId;
    private Long channelId;
    private Long channelIndex;
    private Float snr;
    private Float power;
    private Float frequency;
    private Timestamp collectTime;

    @Override
    public int hashCode() {
        final int prime = 66;
        int result = 1;
        result = prime * result + ((cmId == null) ? 0 : cmId.hashCode());
        result = prime * result + ((channelId == null) ? 0 : channelId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != CmDownChannelSignal.class) {
            return false;
        }

        CmDownChannelSignal other = (CmDownChannelSignal) obj;
        if (this.cmId != null && this.channelId != null && other.cmId != null && other.channelId != null) {
            return this.cmId.equals(other.cmId) && this.channelId.equals(other.channelId);
        } else {
            return false;
        }
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public Float getSnr() {
        return snr;
    }

    public void setSnr(Float snr) {
        this.snr = snr;
    }

    public Float getPower() {
        return power;
    }

    public void setPower(Float power) {
        this.power = power;
    }

    public Float getFrequency() {
        return frequency;
    }

    public void setFrequency(Float frequency) {
        this.frequency = frequency;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }
}
