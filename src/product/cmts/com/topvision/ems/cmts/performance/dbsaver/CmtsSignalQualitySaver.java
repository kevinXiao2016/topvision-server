/***********************************************************************
 * $Id: CmtsSignalQualitySaver.java,v1.0 2014-4-16 上午11:45:00 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.dbsaver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.cmc.performance.handle.NoiseHandle;
import com.topvision.ems.cmc.performance.handle.UsBitErrorRateHandle;
import com.topvision.ems.cmc.performance.handle.UsBitUnErrorRateHandle;
import com.topvision.ems.cmts.performance.domain.CmtsSignalQualityPerf;
import com.topvision.ems.cmts.performance.engine.CmtsPerfDao;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2014-4-16-上午11:45:00
 * 
 */
@Engine("cmtsSignalQualitySaver")
public class CmtsSignalQualitySaver extends BaseEngine implements
        PerfEngineSaver<CmcSignalQualityPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmtsSignalQualitySaver.class);
    private static Map<String, CmcSignalQuality> lastStaticMap = Collections
            .synchronizedMap(new HashMap<String, CmcSignalQuality>());
    private static final Long WRONG_VALUE = -1L;
    private static final Long INVALID_VALUE = 0L;
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcSignalQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmtsSignalQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        CmtsPerfDao perfDao = engineDaoFactory.getEngineDao(CmtsPerfDao.class);
        Long entityId = result.getEntityId();
        CmtsSignalQualityPerf perf = (CmtsSignalQualityPerf) result.getDomain();
        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();
        List<CmcSignalQuality> list = result.getPerfs();
        if (perf.getIsNoise()) {
            for (CmcSignalQuality cmcSignalQuality : list) {
                if (cmcSignalQuality.getDocsIfSigQSignalNoise() < 0) {
                    cmcSignalQuality.setDocsIfSigQSignalNoise(INVALID_VALUE);
                }
                cmcSignalQuality.setNoise(cmcSignalQuality.getDocsIfSigQSignalNoise() / 10F);
                PerformanceData signalQualityData = new PerformanceData(entityId, NoiseHandle.NOISE_FLAG,
                        cmcSignalQuality);
                perfDataList.add(signalQualityData);
            }
            perfDao.insertCmtsSignalQuality(entityId, PerfTargetConstants.CMC_SNR, result);
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
                        cmcSignalQuality.setSigQUnerroredRate(0F);
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
                    cmcSignalQuality.setSigQUnerroredRate(WRONG_VALUE.floatValue());
                    cmcSignalQuality.setTotalCodeNum(codeSum);
                }
                cmcSignalQuality.setSchedulerType(CmcSignalQuality.CMTS_SCHEDULER);
                PerformanceData errorRateData = new PerformanceData(entityId, UsBitErrorRateHandle.ERRORRATE_FLAG,
                        cmcSignalQuality);
                PerformanceData unErrorRateData = new PerformanceData(entityId,
                        UsBitUnErrorRateHandle.UN_ERRORRATE_FLAG, cmcSignalQuality);
                perfDataList.add(errorRateData);
                perfDataList.add(unErrorRateData);
            }
            perfDao.insertCmtsSignalQuality(entityId, PerfTargetConstants.CMC_BER, result);
        }
        getCallback(PerformanceCallback.class).sendPerfomaceResult(perfDataList);
    }
}
