package com.topvision.ems.mobile.domain;

import java.util.List;

public class FlatnessInfo {
    private FrequencyInfo minPower;
    private FrequencyInfo maxPower;
    private Double avg;
    private Double flatness;
    private List<FrequencyInfo> flatnessInfos;

    public FrequencyInfo getMinPower() {
        return minPower;
    }

    public void setMinPower(FrequencyInfo minPower) {
        this.minPower = minPower;
    }

    public FrequencyInfo getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(FrequencyInfo maxPower) {
        this.maxPower = maxPower;
    }

    public Double getFlatness() {
        return flatness;
    }

    public void setFlatness(Double flatness) {
        this.flatness = flatness;
    }

    public List<FrequencyInfo> getFlatnessInfos() {
        return flatnessInfos;
    }

    public void setFlatnessInfos(List<FrequencyInfo> flatnessInfos) {
        this.flatnessInfos = flatnessInfos;
    }

	public Double getAvg() {
		return avg;
	}

	public void setAvg(Double avg) {
		this.avg = avg;
	}

}
