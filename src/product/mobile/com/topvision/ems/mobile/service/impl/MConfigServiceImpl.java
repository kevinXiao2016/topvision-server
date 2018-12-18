package com.topvision.ems.mobile.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.mobile.domain.FlatnessInfo;
import com.topvision.ems.mobile.domain.FrequencyInfo;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.service.SpectrumConfigService;
import com.topvision.ems.cmc.spectrum.service.SpectrumHeartbeatService;
import com.topvision.ems.mobile.dao.MConfigDao;
import com.topvision.ems.mobile.domain.AttenuationInfo;
import com.topvision.ems.mobile.domain.MobileDeviceType;
import com.topvision.ems.mobile.service.MConfigService;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

@Service("mConfigService")
public class MConfigServiceImpl extends CmcBaseCommonService implements MConfigService {
    @Resource(name = "mConfigDao")
    private MConfigDao mConfigDao;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private SpectrumHeartbeatService spectrumHeartbeatService;
    @Autowired
    private SpectrumConfigService spectrumConfigService;
    @Autowired
    private BeanFactory beanFactory;
    private Map<Long, AttenuationInfo> attenuationInfoMap = Collections
            .synchronizedMap(new HashMap<Long, AttenuationInfo>());
    private Map<Long, AttenuationInfo> attenuationInfoMapSimple = Collections
            .synchronizedMap(new HashMap<Long, AttenuationInfo>());
    private Map<Long, FlatnessInfo> flatnessInfoMap = Collections
            .synchronizedMap(new HashMap<Long, FlatnessInfo>());

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#getMobileDeviceTypeList()
     */
    @Override
    public List<MobileDeviceType> getMobileDeviceTypeList() {
        return mConfigDao.getMobileDeviceTypeList();
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#getMobileDeviceTypeList()
     */
    @Override
    public MobileDeviceType getMobileDeviceTypeByTypeId(Long typeId) {
        return mConfigDao.getMobileDeviceTypeByTypeId(typeId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#modifyMobileDeviceType()
     */
    @Override
    public void modifyMobileDeviceType(Long typeId, String frequency, String powerlevel) {
        mConfigDao.modifyMobileDeviceType(typeId, frequency, powerlevel);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#modifyMobileDeviceType()
     */
    @Override
    public void addMobileDeviceType(String deviceType, String corporation, String frequency, String powerlevel) {
        mConfigDao.addMobileDeviceType(deviceType, corporation, frequency, powerlevel);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#delMobileDeviceType()
     */
    @Override
    public void delMobileDeviceType(Long typeId) {
        mConfigDao.delMobileDeviceType(typeId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#setDefaultMobileDeviceType()
     */
    @Override
    public void setDefaultMobileDeviceType(Long typeId) {
        mConfigDao.setDefaultMobileDeviceType(typeId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#setDefaultMobileDeviceType()
     */
    @Override
    public Double getDefaultPowerLevel() {
        SystemPreferences defaultPowerLevel = systemPreferencesService.getSystemPreference("Mobile.DefaultPowerLevel");
        return Double.parseDouble(defaultPowerLevel.getValue());
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#setDefaultMobileDeviceType()
     */
    @Override
    public void modifyDefaultPowerLevel(Double defaultPowerLevel) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("mobile");
        systemPreferences.setName("Mobile.DefaultPowerLevel");
        systemPreferences.setValue("" + defaultPowerLevel);
        systemPreferencesService.savePreferences(systemPreferences);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#setDefaultMobileDeviceType()
     */
    @Override
    public Long startCalculateAttenuation(Long cmcId, Long typeId, Double deviceSetPowerLevel, Double defaultPowerLevel) {
        MobileDeviceType mobileDeviceType = getMobileDeviceTypeByTypeId(typeId);
        String[] fres = mobileDeviceType.getFrequency().split("/");
        List<Double> frequencys = new ArrayList<Double>();
        for (String fre : fres) {
            try {
                frequencys.add(Double.parseDouble(fre));
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        ;
        CalculateAttenuationCallback calculateAttenuationCallback = (CalculateAttenuationCallback) beanFactory
                .getBean("calculateAttenuationCallback");
        calculateAttenuationCallback.setCmcId(cmcId);
        calculateAttenuationCallback.setDefaultPowerLevel(defaultPowerLevel);
        calculateAttenuationCallback.setDeviceSetPowerLevel(deviceSetPowerLevel);
        calculateAttenuationCallback.setFrequencys(frequencys);

        Long callbackId = spectrumHeartbeatService.addHeartbeat(cmcId, calculateAttenuationCallback);
        calculateAttenuationCallback.setCallbackId(callbackId);
        return callbackId;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#setDefaultMobileDeviceType()
     */
    @Override
    public void stopCalculateAttenuation(Long callbackId) {
        spectrumHeartbeatService.delHeartbeat(callbackId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#setDefaultMobileDeviceType()
     */
    @Override
    public void putAttenuationInfo(Long callbackId, AttenuationInfo attenuationInfo) {
        attenuationInfoMap.put(callbackId, attenuationInfo);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#setDefaultMobileDeviceType()
     */
    @Override
    public AttenuationInfo pickAttenuationInfo(Long callbackId) {
        spectrumHeartbeatService.heartbeat(callbackId);
        return attenuationInfoMap.get(callbackId);
    }

    @Override
    public void putAttenuationInfoSimple(Long callbackId, AttenuationInfo attenuationInfoSimple) {
        attenuationInfoMapSimple.put(callbackId, attenuationInfoSimple);
    }

    @Override
    public AttenuationInfo pickAttenuationInfoSimple(Long callbackId) {
        spectrumHeartbeatService.heartbeat(callbackId);
        return attenuationInfoMapSimple.get(callbackId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.mobile.service.MConfigService#setDefaultMobileDeviceType()
     */
    @Override
    public Integer getTimeInterval() {
        return spectrumConfigService.getTimeInterval();
    }

    @Override
    public Long startFlatnessInfo(Long cmcId, Long typeId) {
        MobileDeviceType mobileDeviceType = getMobileDeviceTypeByTypeId(typeId);
        String[] fres = mobileDeviceType.getFrequency().split("/");
        List<Double> frequencys = new ArrayList<Double>();
        for (String fre : fres) {
            try {
                frequencys.add(Double.parseDouble(fre));
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        FlatnessCallback flatnessCallback = (FlatnessCallback) beanFactory
                .getBean("flatnessCallback");
        flatnessCallback.setCmcId(cmcId);
        flatnessCallback.setFrequencys(frequencys);

        Long callbackId = spectrumHeartbeatService.addHeartbeat(cmcId, flatnessCallback);
        flatnessCallback.setCallbackId(callbackId);
        return callbackId;
    }

    @Override
    public void stopFlatnessInfo(Long callbackId) {
        spectrumHeartbeatService.delHeartbeat(callbackId);
    }

    @Override
    public void putFlatnessInfo(Long callbackId, FlatnessInfo flatnessInfo) {
        flatnessInfoMap.put(callbackId, flatnessInfo);
    }


    @Override
    public FlatnessInfo pickFlatnessInfo(Long callbackId) {
        spectrumHeartbeatService.heartbeat(callbackId);
        return flatnessInfoMap.get(callbackId);
    }

    @Override
    public void resetFlatnessInfo(Long callbackId) {
        FlatnessInfo flatnessInfo = flatnessInfoMap.get(callbackId);
        flatnessInfo.setAvg(null);
        flatnessInfo.setFlatness(null);
        flatnessInfo.setMaxPower(null);
        flatnessInfo.setMinPower(null);
        List<FrequencyInfo> flatnessInfos = flatnessInfo.getFlatnessInfos();
        for (FrequencyInfo info : flatnessInfos) {
            info.setPower(null);
        }
    }
}
