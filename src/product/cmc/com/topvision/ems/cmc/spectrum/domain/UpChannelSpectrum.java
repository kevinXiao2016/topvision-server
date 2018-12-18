/***********************************************************************
 * $Id: UpChannelSpectrum.java,v1.0 2014年12月24日 上午10:15:00 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.domain;

/**
 * @author loyal
 * @created @2014年12月24日-上午10:15:00
 * 
 */
public class UpChannelSpectrum {
    private String cmtsId;
    private String centerFreq;
    private String avg;
    private String min;
    private String max;

    public String getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(String cmtsId) {
        this.cmtsId = cmtsId;
    }

    public String getCenterFreq() {
        return centerFreq;
    }

    public void setCenterFreq(String centerFreq) {
        this.centerFreq = centerFreq;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

}
