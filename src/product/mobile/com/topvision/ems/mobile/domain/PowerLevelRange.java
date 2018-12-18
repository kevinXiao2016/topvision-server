package com.topvision.ems.mobile.domain;

public class PowerLevelRange {
    private Double lowerLimit;
    private Double upperLimit;
    private Double gain;

    public Double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public Double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Double getGain() {
        return gain;
    }

    public void setGain(Double gain) {
        this.gain = gain;
    }

    public Boolean isInRange(Double powerLevel) {
        if (powerLevel >= lowerLimit && powerLevel <= upperLimit) {
            return true;
        } else {
            return false;
        }
    }

}
