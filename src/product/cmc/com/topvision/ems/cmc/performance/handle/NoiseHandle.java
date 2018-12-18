/***********************************************************************
 * $Id: NoiseHandle.java,v1.0 2013-6-13 下午02:55:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmc.util.CmtsUtils;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author Rod John
 * @created @2013-6-13-下午02:55:06
 * 
 */
@Service("cmcNoiseHandle")
public class NoiseHandle extends CmcPerformanceHandle {
    public static String NOISE_FLAG = "CC_NOISE";
    public static Integer NOISE_ALERT_ID = -800;
    public static Integer NOISE_EVENT_ID = -801;
    public static Float IGNORE_NOISE_VALUE = 0F;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(NOISE_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(NOISE_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        CmcSignalQuality noise = (CmcSignalQuality) data.getPerfData();
        long cmcId = noise.getCmcId();
        Long upChannleIndex = noise.getCmcChannelIndex();
        CmcUpChannelBaseShowInfo upChannelBaseInfo = cmcUpChannelService.getUpChannelBaseInfo(cmcId, upChannleIndex);
        if (CmcSignalQuality.CMC_SCHEDULER.equals(noise.getSchedulerType())) {
            String channelModulationProfile = upChannelBaseInfo.getDocsIfUpChannelModulationProfileName();
            if (channelModulationProfile.startsWith("QPSK")) {
                return "CC_NOISE_QPSK";
            } else if (channelModulationProfile.startsWith("QAM8")) {
                return "CC_NOISE_QAM8";
            } else if (channelModulationProfile.startsWith("QAM16")) {
                return "CC_NOISE_QAM16";
            } else if (channelModulationProfile.startsWith("QAM32")) {
                return "CC_NOISE_QAM32";
            } else if (channelModulationProfile.startsWith("QAM64")) {
                return "CC_NOISE_QAM64";
            } else if (channelModulationProfile.startsWith("QAM128")) {
                return "CC_NOISE_QAM128";
            } else if (channelModulationProfile.startsWith("QAM256")) {
                return "CC_NOISE_QAM256";
            } else {
                return "CC_NOISE_OTHER";
            }
        } else {
            Integer cmtsChannelModulationProfile = upChannelBaseInfo.getChannelModulationProfile().intValue();
            switch (cmtsChannelModulationProfile) {
            case 1:
                return "CC_NOISE_OTHER";
            case 2:
                return "CC_NOISE_QPSK";
            case 3:
                return "CC_NOISE_QAM16";
            case 4:
                return "CC_NOISE_QAM8";
            case 5:
                return "CC_NOISE_QAM32";
            case 6:
                return "CC_NOISE_QAM64";
            case 7:
                return "CC_NOISE_QAM128";
            default:
                return "CC_NOISE_OTHER";
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams()
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcSignalQuality noise = (CmcSignalQuality) data.getPerfData();
        Long cmcId = noise.getCmcId();
        Long upChannleIndex = noise.getCmcChannelIndex();
        CmcUpChannelBaseShowInfo upChannelBaseInfo = cmcUpChannelService.getUpChannelBaseInfo(cmcId, upChannleIndex);
        if (CmcSignalQuality.CMC_SCHEDULER.equals(noise.getSchedulerType())) {
            //中心频率
            String frequencyForunit = upChannelBaseInfo.getDocsIfUpChannelFrequencyForunit();
            //频宽
            String channelWidth = upChannelBaseInfo.getDocsIfUpChannelWidthForunit();
            //调制模式
            String modulationName = upChannelBaseInfo.getDocsIfUpChannelModulationProfileName();

            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(NOISE_ALERT_ID);
            param.setClearEventId(NOISE_EVENT_ID);
            // CMC SOURCE
            param.setSource(getCmcSourceString(noise.getCmcChannelIndex().longValue()));
            // Modify by Rod @2013-12-23 广州需求修改
            if (IGNORE_NOISE_VALUE.equals(noise.getNoise())) {
                return null;
            }
            param.setPerfValue(noise.getNoise());
            param.setTargetId(NOISE_FLAG);
            param.setTargetIndex(noise.getCmcChannelIndex());
            param.setMessage("CMTS[" + getMac(data.getEntityId()) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.noise", "cmc") + "[" + getString("PerformanceAlert.noiseMes", "cmc")
                    + ": " + noise.getNoise().intValue() + "dB" + "/" + frequencyForunit + "/" + channelWidth + "/"
                    + modulationName + "]");
            return param;
        } else if (CmcSignalQuality.CMTS_SCHEDULER.equals(noise.getSchedulerType())) {
            Integer modulationId = upChannelBaseInfo.getChannelModulationProfile().intValue();
            //中心频率
            String frequencyForunit = upChannelBaseInfo.getDocsIfUpChannelFrequencyForunit();
            //频宽
            String channelWidth = upChannelBaseInfo.getDocsIfUpChannelWidthForunit();
            //调制模式
            String modulationName = CmtsUtils.getCmtsUpChannelMode(modulationId);
            Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(NOISE_ALERT_ID);
            param.setClearEventId(NOISE_EVENT_ID);
            // CCMTS SOURCE
            param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(), noise
                    .getCmcChannelIndex().longValue()));
            // Modify by Rod @2013-12-23 广州需求修改
            if (IGNORE_NOISE_VALUE.equals(noise.getNoise())) {
                return null;
            }
            param.setPerfValue(noise.getNoise());
            param.setTargetId(NOISE_FLAG);
            param.setTargetIndex(noise.getCmcChannelIndex());
            param.setMessage("CMTS[" + getAdditionText(entity) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.noise", "cmc") + "[" + getString("PerformanceAlert.noiseMes", "cmc")
                    + ": " + noise.getNoise().intValue() + "dB" + "/" + frequencyForunit + "/" + channelWidth + "/"
                    + modulationName + "]");
            return param;
        }
        return null;
    }
}
