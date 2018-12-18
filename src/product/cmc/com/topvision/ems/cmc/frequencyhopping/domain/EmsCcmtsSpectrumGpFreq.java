/***********************************************************************
 * $Id: EmsCcmtsSpectrumGpFreq.java,v1.0 2013-8-2 上午11:20:21 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;

/**
 * @author haojie
 * @created @2013-8-2-上午11:20:21
 * 
 */
public class EmsCcmtsSpectrumGpFreq implements Serializable {
    private static final long serialVersionUID = 5613300952663764317L;

    private Long emsGroupId;
    private Integer freqIndex;
    private Integer freqFrequency;
    private Integer freqMaxWidth;
    private Integer freqPower;

    public Long getEmsGroupId() {
        return emsGroupId;
    }

    public void setEmsGroupId(Long emsGroupId) {
        this.emsGroupId = emsGroupId;
    }

    public Integer getFreqIndex() {
        return freqIndex;
    }

    public void setFreqIndex(Integer freqIndex) {
        this.freqIndex = freqIndex;
    }

    public Integer getFreqFrequency() {
        return freqFrequency;
    }

    public void setFreqFrequency(Integer freqFrequency) {
        this.freqFrequency = freqFrequency;
    }

    public Integer getFreqMaxWidth() {
        return freqMaxWidth;
    }

    public void setFreqMaxWidth(Integer freqMaxWidth) {
        this.freqMaxWidth = freqMaxWidth;
    }

    public Integer getFreqPower() {
        return freqPower;
    }

    public void setFreqPower(Integer freqPower) {
        this.freqPower = freqPower;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EmsCcmtsSpectrumGpFreq [emsGroupId=");
        builder.append(emsGroupId);
        builder.append(", freqIndex=");
        builder.append(freqIndex);
        builder.append(", freqFrequency=");
        builder.append(freqFrequency);
        builder.append(", freqMaxWidth=");
        builder.append(freqMaxWidth);
        builder.append(", freqPower=");
        builder.append(freqPower);
        builder.append("]");
        return builder.toString();
    }

}
