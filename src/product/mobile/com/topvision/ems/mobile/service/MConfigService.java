package com.topvision.ems.mobile.service;

import java.util.List;

import com.topvision.ems.mobile.domain.AttenuationInfo;
import com.topvision.ems.mobile.domain.FlatnessInfo;
import com.topvision.ems.mobile.domain.MobileDeviceType;

public interface MConfigService {

    /**
     * @return
     */
    List<MobileDeviceType> getMobileDeviceTypeList();

    MobileDeviceType getMobileDeviceTypeByTypeId(Long typeId);

    /**
     * @param typeId
     * @param frequency
     * @param powerlevel
     */
    void modifyMobileDeviceType(Long typeId, String frequency, String powerlevel);

    /**
     * @param typeId
     */
    void delMobileDeviceType(Long typeId);

    /**
     * @param typeId
     */
    void setDefaultMobileDeviceType(Long typeId);

    /**
     * @param deviceType
     * @param corporation
     * @param frequency
     * @param powerlevel
     */
    void addMobileDeviceType(String deviceType, String corporation, String frequency, String powerlevel);

    /**
     * @return
     */
    Double getDefaultPowerLevel();

    /**
     * @param defaultPowerLevel
     */
    void modifyDefaultPowerLevel(Double defaultPowerLevel);

    /**
     * @param cmcId
     * @param typeId
     * @param deviceSetPowerLevel
     * @param defaultPowerLevel
     * @return callbackId
     */
    Long startCalculateAttenuation(Long cmcId, Long typeId, Double deviceSetPowerLevel, Double defaultPowerLevel);

    /**
     * @param callbackId
     */
    void stopCalculateAttenuation(Long callbackId);

    /**
     */
    void putAttenuationInfo(Long callbackId, AttenuationInfo attenuationInfo);

    /**
     */
    AttenuationInfo pickAttenuationInfo(Long callbackId);
    /**
     */
    void putAttenuationInfoSimple(Long callbackId, AttenuationInfo attenuationInfoSimple);
    /**
     */
    AttenuationInfo pickAttenuationInfoSimple(Long callbackId);

    /**
     * @return
     */
    Integer getTimeInterval();

    Long startFlatnessInfo(Long cmcId, Long typeId);

    void putFlatnessInfo(Long callbackId, FlatnessInfo flatnessInfo);

    void stopFlatnessInfo(Long callbackId);

    FlatnessInfo pickFlatnessInfo(Long callbackId);

    void resetFlatnessInfo(Long callbackId);
}
