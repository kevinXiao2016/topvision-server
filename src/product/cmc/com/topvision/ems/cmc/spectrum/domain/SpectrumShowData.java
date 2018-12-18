package com.topvision.ems.cmc.spectrum.domain;

import java.io.Serializable;

/**
 * @author bryan
 */
public class SpectrumShowData implements Serializable {
    private static final long serialVersionUID = 3834292567903911530L;
    public static Integer RBW = 300;
    public static Integer VBW = 300;
    private Long cmcId;
    private Integer frequencyIndex;
    private double power;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getFrequencyIndex() {
        return frequencyIndex;
    }

    public void setFrequencyIndex(Integer frequencyIndex) {
        this.frequencyIndex = frequencyIndex;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSpectrumShowData [cmcId=");
        builder.append(cmcId);
        builder.append(", frequencyIndex=");
        builder.append(frequencyIndex);
        builder.append(", power=");
        builder.append(power);
        builder.append("]");
        return builder.toString();
    }

}
