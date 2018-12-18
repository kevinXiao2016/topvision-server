/***********************************************************************
 * $ FlatnessCallback.java,v1.0 2016-11-12 9:58:19 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.service.impl;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.spectrum.service.impl.MobileCallback;
import com.topvision.ems.mobile.domain.FlatnessInfo;
import com.topvision.ems.mobile.domain.FrequencyInfo;
import com.topvision.ems.mobile.service.MConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @created @2014-1-18-9:58:19
 */
@Service("flatnessCallback")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlatnessCallback extends MobileCallback {
    private final Logger logger = LoggerFactory.getLogger(FlatnessCallback.class);
    @Autowired
    private MConfigService mConfigService;
    @Value("${calculateAttenuation.offset:0}")
    protected Double offset;
    @Value("${calculateAttenuation.offset5_20:0}")
    protected Double offset5_20;
    @Value("${calculateAttenuation.offset21_59:0}")
    protected Double offset21_59;
    @Value("${calculateAttenuation.offset60_64:0}")
    protected Double offset60_64;
    @Value("${calculateAttenuation.offset65:0}")
    protected Double offset65;

    private Long callbackId = -1L;
    private Long cmcId;
    private List<Double> frequencys;

    public FlatnessCallback() {
    }

    public FlatnessCallback(Long cmcId,List<Double> frequencys) {
        this.cmcId = cmcId;
        this.frequencys = frequencys;

    }

    private Double getOffsetByFreq(Double freq) {
        if(freq >= 5.0d && freq < 21.0d ){
            return offset5_20;
        } else if (freq >= 21.0d && freq < 60.0d) {
            return offset21_59;
        } else if (freq >= 60.0d && freq < 65.0d) {
            return offset60_64;
        } else if (freq >= 65.0d) {
            return offset65;
        } else {
            return 0d;
        }
    }

    @Override
    public void appendResult(Long entityId, Long cmcId, Long startFreq, Long endFreq, List<List<Number>> list, Long dt) {
        if (this.cmcId.equals(cmcId) && list != null && !list.isEmpty()) {
            FlatnessInfo flatnessInfo = new FlatnessInfo();
            List<FrequencyInfo> powerInfo = new ArrayList<>();
            flatnessInfo.setFlatnessInfos(powerInfo);

            for (List<Number> numbers : list) {
                Double frequency = numbers.get(0).doubleValue();
                Double powerLevel = numbers.get(1).doubleValue();
                if (frequencys.contains(frequency)) {
                    double power = powerLevel - getOffsetByFreq(frequency) - 60;
                    FrequencyInfo frequencyInfo = new FrequencyInfo();
                    frequencyInfo.setFrequency(frequency);
                    frequencyInfo.setPower(power);
                    powerInfo.add(frequencyInfo);
                    if (flatnessInfo.getMinPower() != null) {
                        if (flatnessInfo.getMinPower().getPower() > frequencyInfo.getPower()) {
                            flatnessInfo.setMinPower(frequencyInfo);
                        }
                    } else {
                        flatnessInfo.setMinPower(frequencyInfo);
                    }
                    if (flatnessInfo.getMaxPower() != null) {
                        if (flatnessInfo.getMaxPower().getPower() < frequencyInfo.getPower()) {
                            flatnessInfo.setMaxPower(frequencyInfo);
                        }
                    } else {
                        flatnessInfo.setMaxPower(frequencyInfo);
                    }
                }
            }
            //maxPower + minPower / 2
            Double avg = (flatnessInfo.getMaxPower().getPower() + flatnessInfo.getMinPower().getPower()) / 2;
            flatnessInfo.setAvg(avg);
            //abs(maxPower - minPower) / 2
            Double flatness = Math.abs(flatnessInfo.getMaxPower().getPower() - flatnessInfo.getMinPower().getPower()) / 2;
            flatnessInfo.setFlatness(flatness);
            mConfigService.putFlatnessInfo(callbackId, flatnessInfo);
        }
    }

    @Override
    public SpectrumVideo finish() {
        return null;
    }

    @Override
    public Long getCmcId() {
        return cmcId;
    }

    @Override
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    @Override
    public String getTerminalIp() {
        return null;
    }

    @Override
    public void setTerminalIp(String terminalIp) {

    }

    @Override
    public Long getCallbackId() {
        return callbackId;
    }

    @Override
    public void setCallbackId(Long callbackId) {
        this.callbackId = callbackId;
    }

    public List<Double> getFrequencys() {
        return frequencys;
    }

    public void setFrequencys(List<Double> frequencys) {
        this.frequencys = frequencys;
    }

}
