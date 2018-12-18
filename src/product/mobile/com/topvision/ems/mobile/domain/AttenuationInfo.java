package com.topvision.ems.mobile.domain;

import java.util.List;

public class AttenuationInfo {
    //
    private Double globalAttenuation;
    private List<FrequencyAttenuationInfo> frequencyAttenuationInfos;

    public Double getGlobalAttenuation() {
        return globalAttenuation;
    }

    public void setGlobalAttenuation(Double globalAttenuation) {
        this.globalAttenuation = globalAttenuation;
    }

    public List<FrequencyAttenuationInfo> getFrequencyAttenuationInfos() {
        return frequencyAttenuationInfos;
    }

    public void setFrequencyAttenuationInfos(List<FrequencyAttenuationInfo> frequencyAttenuationInfos) {
        this.frequencyAttenuationInfos = frequencyAttenuationInfos;
    }

}
