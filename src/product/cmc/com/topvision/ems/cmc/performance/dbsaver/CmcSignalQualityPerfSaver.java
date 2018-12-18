/***********************************************************************
 * $Id: CmcSingleQualityPerfSaver.java,v1.0 2013-8-10 下午02:02:14 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.cmc.performance.handle.NoiseHandle;
import com.topvision.ems.cmc.performance.handle.UsBitErrorRateHandle;
import com.topvision.ems.cmc.performance.handle.UsBitUnErrorRateHandle;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-10-下午02:02:14
 * 
 */
@Engine("cmcSignalQualityPerfSaver")
public class CmcSignalQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcSignalQualityPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmcSignalQualityPerfSaver.class);
    private static Map<String, CmcSignalQuality> lastStaticMap = Collections
            .synchronizedMap(new HashMap<String, CmcSignalQuality>());
    private static Map<String, CmcSignalQuality> lastStaticSnrMap = Collections
            .synchronizedMap(new HashMap<String, CmcSignalQuality>());
    private static final Long WRONG_VALUE = -1L;
    private static final Long INVALID_VALUE = 0L;
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcSignalQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcSignalQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        CmcPerfDao perfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        Long cmcId = result.getEntityId();
        CmcSignalQualityPerf perf = (CmcSignalQualityPerf) result.getDomain();
        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();
        List<CmcSignalQuality> list = result.getPerfs();
        if (perf.getIsNoise()) {
            for (Iterator<CmcSignalQuality> snr = list.iterator(); snr.hasNext();) {
                CmcSignalQuality cmcSignalQuality = snr.next();
                String key = new StringBuilder(cmcSignalQuality.getCmcId().toString()).append("_")
                        .append(cmcSignalQuality.getCmcChannelIndex()).toString();
                CmcSignalQuality lastQuality = lastStaticSnrMap.get(key);
                lastStaticSnrMap.put(key, cmcSignalQuality);
                if (cmcSignalQuality.getDocsIfSigQSignalNoise() < 0) {
                    cmcSignalQuality.setDocsIfSigQSignalNoise(INVALID_VALUE);
                }
                cmcSignalQuality.setNoise(cmcSignalQuality.getDocsIfSigQSignalNoise() / 10F);
                PerformanceData signalQualityData = new PerformanceData(cmcId, NoiseHandle.NOISE_FLAG, cmcSignalQuality);
                perfDataList.add(signalQualityData);
                try {
                    redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.7.1.3", cmcSignalQuality.getNoise(), result,
                            cmcId, cmcSignalQuality.getCmcChannelIndex());
                } catch (Exception e) {
                    logger.info("CmcSignalquality snr redirctPerformanceData error", e);
                    logger.error("CmcSignalquality snr saver error, cmcSignalQuality: " + cmcSignalQuality);
                }
                /**
                 * modify by lzt 对CCMTS采集到的snr值如果突然的一次为0 则抛弃，解决内蒙反馈的snr骤降问题
                 * 
                 * modify time ： 2016-6-24
                 */
                if (INVALID_VALUE.equals(cmcSignalQuality.getDocsIfSigQSignalNoise())) {
                    if (lastQuality == null || !INVALID_VALUE.equals(lastQuality.getDocsIfSigQSignalNoise())) {
                        snr.remove();
                    }
                }
            }
            perfDao.insertCmcSignalQuality(cmcId, PerfTargetConstants.CMC_SNR, result);
        }
        if (perf.getIsUnerror()) {
            for (CmcSignalQuality cmcSignalQuality : list) {
                String key = new StringBuilder(cmcSignalQuality.getCmcId().toString()).append("_")
                        .append(cmcSignalQuality.getCmcChannelIndex()).toString();
                CmcSignalQuality lastQuality = lastStaticMap.get(key);
                lastStaticMap.put(key, cmcSignalQuality);
                // CC Code计算必须依靠上一次采集值
                if (lastQuality == null) {
                    continue;
                }
                // 可纠错Codewords数
                Long sigQCorrecteds = cmcSignalQuality.getDocsIfSigQCorrecteds()
                        - lastQuality.getDocsIfSigQCorrecteds();
                // 不可纠错Codewords数
                Long sigQUncorrectables = cmcSignalQuality.getDocsIfSigQUncorrectables()
                        - lastQuality.getDocsIfSigQUncorrectables();
                // 无纠错码数
                Long sigQUnerroreds = cmcSignalQuality.getDocsIfSigQUnerroreds()
                        - lastQuality.getDocsIfSigQUnerroreds();
                boolean isNormalFlag = true;
                if (sigQCorrecteds < 0) {
                    cmcSignalQuality.setSigQCorrecteds(WRONG_VALUE);
                    isNormalFlag = false;
                } else {
                    cmcSignalQuality.setSigQCorrecteds(sigQCorrecteds);
                }
                if (sigQUncorrectables < 0) {
                    cmcSignalQuality.setSigQUncorrectables(WRONG_VALUE);
                    isNormalFlag = false;
                } else {
                    cmcSignalQuality.setSigQUncorrectables(sigQUncorrectables);
                }
                if (sigQUnerroreds < 0) {
                    cmcSignalQuality.setSigQUnerroreds(WRONG_VALUE);
                    isNormalFlag = false;
                } else {
                    cmcSignalQuality.setSigQUnerroreds(sigQUnerroreds);
                }
                // Normal Date

                if (isNormalFlag) {
                    Long codeSum = sigQCorrecteds + sigQUncorrectables + sigQUnerroreds;
                    if (codeSum == 0) {
                        cmcSignalQuality.setSigQCorrectedRate(0F);
                        cmcSignalQuality.setSigQUncorrectedRate(0F);
                        cmcSignalQuality.setSigQUnerroredRate(0F);
                        cmcSignalQuality.setTotalCodeNum(codeSum);
                    } else {
                        cmcSignalQuality.setSigQCorrectedRate(sigQCorrecteds.floatValue() * 100 / codeSum);
                        cmcSignalQuality.setSigQUncorrectedRate(sigQUncorrectables.floatValue() * 100 / codeSum);
                        cmcSignalQuality.setSigQUnerroredRate(sigQUnerroreds.floatValue() * 100 / codeSum);
                        cmcSignalQuality.setTotalCodeNum(codeSum);
                    }
                } else {
                    Long codeSum = sigQCorrecteds + sigQUncorrectables + sigQUnerroreds;
                    cmcSignalQuality.setSigQCorrectedRate(WRONG_VALUE.floatValue());
                    cmcSignalQuality.setSigQUncorrectedRate(WRONG_VALUE.floatValue());
                    cmcSignalQuality.setSigQUnerroredRate(WRONG_VALUE.floatValue());
                    cmcSignalQuality.setTotalCodeNum(codeSum);
                }
                PerformanceData errorRateData = new PerformanceData(cmcId, UsBitErrorRateHandle.ERRORRATE_FLAG,
                        cmcSignalQuality);
                PerformanceData unErrorRateData = new PerformanceData(cmcId, UsBitUnErrorRateHandle.UN_ERRORRATE_FLAG,
                        cmcSignalQuality);
                perfDataList.add(errorRateData);
                perfDataList.add(unErrorRateData);
                try {
                    redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.7.1.1", cmcSignalQuality.getSigQCorrectedRate(),
                            result, cmcId, cmcSignalQuality.getCmcChannelIndex());
                    redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.7.1.2",
                            cmcSignalQuality.getSigQUncorrectedRate(), result, cmcId,
                            cmcSignalQuality.getCmcChannelIndex());
                } catch (Exception e) {
                    logger.info("CmcSignalquality ber redirctPerformanceData error", e);
                    logger.error("CmcSignalquality ber saver error, cmcSignalQuality: " + cmcSignalQuality);
                }
            }
            perfDao.insertCmcSignalQuality(cmcId, PerfTargetConstants.CMC_BER, result);
        }
        getCallback(PerformanceCallback.class).sendPerfomaceResult(perfDataList);
    }
}
