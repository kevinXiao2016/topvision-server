/***********************************************************************
 * $Id: DispersionRawData.java,v1.0 2015-3-25 下午3:06:08 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.engine.domain;

/**
 * @author fanzidong
 * @created @2015-3-25-下午3:06:08
 * 
 */
public class DispersionRawData {
    public static final String UP_SNR = "upSnr";
    public static final String UP_POWER = "upPower";
    
    private Long cmId;
    private Long cmtsId;
    private Long channelIndex;
    private Double upSnr;
    private Double upPower;

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public Double getUpSnr() {
        return upSnr;
    }

    public void setUpSnr(Double upSnr) {
        this.upSnr = upSnr;
    }

    public Double getUpPower() {
        return upPower;
    }

    public void setUpPower(Double upPower) {
        this.upPower = upPower;
    }

}
