/***********************************************************************
 * $ RealtimeCallback.java,v1.0 2014-1-18 9:58:19 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.spectrum.service.impl.MobileCallback;
import com.topvision.ems.mobile.domain.FrequencyAttenuationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.spectrum.domain.SpectrumVideo;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.mobile.domain.AttenuationInfo;
import com.topvision.ems.mobile.service.MConfigService;
import com.topvision.ems.network.dao.EntityDao;

/**
 * @author jay
 * @created @2014-1-18-9:58:19
 */
@Service("calculateAttenuationCallback")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalculateAttenuationCallback extends MobileCallback {
    private final Logger logger = LoggerFactory.getLogger(CalculateAttenuationCallback.class);
    @Autowired
    private MConfigService mConfigService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDao")
    protected CmcDao cmcDao;
    @Resource(name = "entityDao")
    protected EntityDao entityDao;
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
    private Double defaultPowerLevel;
    private Double deviceSetPowerLevel;
    private List<Double> frequencys;

    public CalculateAttenuationCallback() {
    }

    public CalculateAttenuationCallback(Long cmcId, Double defaultPowerLevel, Double deviceSetPowerLevel,
            List<Double> frequencys) {
        this.cmcId = cmcId;
        this.defaultPowerLevel = defaultPowerLevel;
        this.deviceSetPowerLevel = deviceSetPowerLevel;
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
        if (this.cmcId.equals(cmcId) && list != null) {
            // 计算
            Double upChannelSetPowerLevel = getFirstUpChannelSetPowerLevel();
            if (upChannelSetPowerLevel != null) {
                // 复杂模式计算结果
                AttenuationInfo attenuationInfo = new AttenuationInfo();
                List<FrequencyAttenuationInfo> frequencyAttenuationInfos = new ArrayList<>();
                attenuationInfo.setFrequencyAttenuationInfos(frequencyAttenuationInfos);
                // 简单模式计算结果
                AttenuationInfo attenuationInfoSimple = new AttenuationInfo();
                List<FrequencyAttenuationInfo> frequencyAttenuationInfosSimple = new ArrayList<>();
                attenuationInfoSimple.setFrequencyAttenuationInfos(frequencyAttenuationInfosSimple);

                Double startFrequency = list.get(0).get(0).doubleValue();
                for (Double frequency : frequencys) {
                    List<Number> point = list.get(getFrequencyIndex(frequency, startFrequency));
                    if (frequency != null && frequency.equals(point.get(0))) {
                        double powerLevel = point.get(1).doubleValue();
                        double attenuation = calculate(deviceSetPowerLevel, powerLevel
                                - getOffsetByFreq(frequency),
                                defaultPowerLevel, upChannelSetPowerLevel);
                        double attenuationSimple = powerLevel - getOffsetByFreq(frequency) - 60;
                        FrequencyAttenuationInfo frequencyAttenuationInfo = new FrequencyAttenuationInfo();
                        frequencyAttenuationInfo.setFrequency(frequency);
                        frequencyAttenuationInfo.setAttenuation(attenuation);
                        frequencyAttenuationInfos.add(frequencyAttenuationInfo);

                        FrequencyAttenuationInfo frequencyAttenuationInfoSimple = new FrequencyAttenuationInfo();
                        frequencyAttenuationInfoSimple.setFrequency(frequency);
                        frequencyAttenuationInfoSimple.setAttenuation(attenuationSimple);
                        frequencyAttenuationInfosSimple.add(frequencyAttenuationInfoSimple);

                    }
                }
                Double total = 0d;
                for (FrequencyAttenuationInfo frequencyAttenuationInfo : frequencyAttenuationInfos) {
                    total = total + frequencyAttenuationInfo.getAttenuation();
                }
                Double globalAttenuation = total / frequencyAttenuationInfos.size();
                attenuationInfo.setGlobalAttenuation(globalAttenuation);

                attenuationInfoSimple.setGlobalAttenuation(0D);
                mConfigService.putAttenuationInfo(callbackId, attenuationInfo);
                mConfigService.putAttenuationInfoSimple(callbackId, attenuationInfoSimple);
            }
        }
    }

    private double calculate(Double deviceSetPowerLevel, Double powerLevel, Double defaultPowerLevel,
            Double upChannelSetPowerLevel) {
        // 信号发生器输出电平 - 上行芯片接收到的信号 - 射频平台衰减(规划电平– powerlevel设定值
        // 规划电平由用户设置，powerlevel选择第一个开启的信道电平（注意转化为dBuV为单位）) + 主板芯片增益（去掉）
    	// @jay 20160323 确认公式中需要去掉主板芯片增益
        double attenuation = deviceSetPowerLevel - powerLevel - (defaultPowerLevel - (upChannelSetPowerLevel + 60));
        if (logger.isTraceEnabled()) {
            logger.trace("attenuation[" + attenuation + "] = deviceSetPowerLevel[" + deviceSetPowerLevel.toString()
                    + "] - powerLevel[" + powerLevel.toString() + "] - (defaultPowerLevel["
                    + defaultPowerLevel.toString() + "] - (upChannelSetPowerLevel[" + upChannelSetPowerLevel.toString()
                    + "] + 60))");
        }
        return attenuation;
    }

    @Override
    public SpectrumVideo finish() {
        return null;
    }

    private Double getFirstUpChannelSetPowerLevel() {
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfos = cmcUpChannelService
                .getUpChannelBaseShowInfoList(cmcId);
        for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfos) {
            if (cmcUpChannelBaseShowInfo.getIfAdminStatus() == 1 && cmcUpChannelBaseShowInfo.getIfOperStatus() == 1) {
            	double d = (double)cmcUpChannelBaseShowInfo.getDocsIf3SignalPower();
                return d/10;
            }
        }
        return null;
    }

    private int getFrequencyIndex(Double frequency, Double startFrequency) {
        //如果用double来做除法会出现精度问题，所以需要强转成float
        return (int) ((float)(frequency - startFrequency) / 0.02f);
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

    public Double getDefaultPowerLevel() {
        return defaultPowerLevel;
    }

    public void setDefaultPowerLevel(Double defaultPowerLevel) {
        this.defaultPowerLevel = defaultPowerLevel;
    }

    public Double getDeviceSetPowerLevel() {
        return deviceSetPowerLevel;
    }

    public void setDeviceSetPowerLevel(Double deviceSetPowerLevel) {
        this.deviceSetPowerLevel = deviceSetPowerLevel;
    }

    public List<Double> getFrequencys() {
        return frequencys;
    }

    public void setFrequencys(List<Double> frequencys) {
        this.frequencys = frequencys;
    }

}
