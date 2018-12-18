/***********************************************************************
 * $Id: CmtsFlowQualitySaver.java,v1.0 2014-4-17 上午10:16:46 $
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

import com.topvision.ems.cmc.performance.facade.CmtsFlowQuality;
import com.topvision.ems.cmc.performance.handle.ChannelSpeedHandle;
import com.topvision.ems.cmc.performance.handle.ChannelUtilizationHandle;
import com.topvision.ems.cmc.performance.handle.CmcUpLinkInUsedHandle;
import com.topvision.ems.cmc.performance.handle.CmcUpLinkOutUsedHandle;
import com.topvision.ems.cmts.performance.domain.CmtsFlowQualityPerfResult;
import com.topvision.ems.cmts.performance.engine.CmtsPerfDao;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2014-4-17-上午10:16:46
 * 
 */
@Engine("cmtsFlowQualitySaver")
public class CmtsFlowQualitySaver extends BaseEngine implements PerfEngineSaver<CmtsFlowQualityPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmtsFlowQualitySaver.class);
    private static Map<String, CmtsFlowQuality> lastStaticMap = Collections
            .synchronizedMap(new HashMap<String, CmtsFlowQuality>());
    private static final long WRONG_VALUE = -1L;
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    public void save(CmtsFlowQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmtsFlowQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        CmtsPerfDao cmtsPerfDao = engineDaoFactory.getEngineDao(CmtsPerfDao.class);
        Long cmcId = result.getEntityId();
        List<CmtsFlowQuality> finalResult = new ArrayList<CmtsFlowQuality>();
        List<PerformanceData> pList = new ArrayList<PerformanceData>();
        try {
            String collectType = result.getCollectType();
            // 采样算法完全参考原始代码的算法
            if (collectType.equals(CmtsFlowQualityPerfResult.SAMPLE_COLLECT)) {
                for (CmtsFlowQuality sampleQuality : result.getCmtsFlowQualities()) {
                    finalResult.add(sampleQuality);
                    // 性能阈值数据处理
                    if (sampleQuality.getIfIndexType().equals(CmtsFlowQuality.UPLINK_CMTS)) {
                        PerformanceData cmtsLinkInUsedData = new PerformanceData(cmcId,
                                CmcUpLinkInUsedHandle.CC_UPLINK_IN_UESD_FLAG, sampleQuality);
                        PerformanceData cmtsLinkOutUsedData = new PerformanceData(cmcId,
                                CmcUpLinkOutUsedHandle.CC_UPLINK_OUT_UESD_FLAG, sampleQuality);
                        pList.add(cmtsLinkInUsedData);
                        pList.add(cmtsLinkOutUsedData);
                    }
                    if (sampleQuality.getIfIndexType().equals(CmtsFlowQuality.CHANNEL_CMTS_UP)
                            || sampleQuality.getIfIndexType().equals(CmtsFlowQuality.CHANNEL_CMTS_DOWN)) {
                        PerformanceData cmtsChannelInSpeedData = new PerformanceData(cmcId,
                                ChannelSpeedHandle.SPEED_FLAG, sampleQuality);
                        PerformanceData cmtsUtilizationData = new PerformanceData(cmcId,
                                ChannelUtilizationHandle.UTILIZATION_FLAG, sampleQuality);
                        pList.add(cmtsChannelInSpeedData);
                        pList.add(cmtsUtilizationData);
                    }
                }
            } else {
                // 更新信道质量
                for (CmtsFlowQuality quality : result.getCmtsFlowQualities()) {
                    String key = new StringBuilder(quality.getCmcId().toString()).append("_")
                            .append(quality.getIfIndex()).toString();
                    CmtsFlowQuality lastQuality = lastStaticMap.get(key);
                    lastStaticMap.put(key, quality);
                    // CC信道速率计算必须依靠上一次采集值
                    if (lastQuality == null) {
                        continue;
                    }
                    long period = 0;
                    long inOctets_ = 0;
                    long outOctets_ = 0;
                    try {
                        period = quality.getCollectTime().getTime() - lastQuality.getCollectTime().getTime();
                        inOctets_ = quality.getIfInOctets() - lastQuality.getIfInOctets();
                        outOctets_ = quality.getIfOutOctets() - lastQuality.getIfOutOctets();
                    } catch (Exception e) {
                        logger.debug("period {0}", period);
                        logger.debug("quality.getIfInOctets() {0}", quality.getIfInOctets());
                        logger.debug("quality.getIfOutOctets() {0}", quality.getIfOutOctets());
                        logger.debug("lastQuality.getIfInOctets() {0}", lastQuality.getIfInOctets());
                        logger.debug("lastQuality.getIfOutOctets() {0}", lastQuality.getIfOutOctets());
                        logger.debug("ifIndex {0}", quality.getIfIndex());
                    }
                    if (inOctets_ < 0) {
                        inOctets_ = WRONG_VALUE;
                    }
                    if (outOctets_ < 0) {
                        outOctets_ = WRONG_VALUE;
                    }
                    CmtsFlowQuality cmtsFlowQuality = new CmtsFlowQuality();
                    cmtsFlowQuality.setCmcId(quality.getCmcId());
                    cmtsFlowQuality.setIfSpeed(quality.getIfSpeed());
                    cmtsFlowQuality.setEntityId(quality.getEntityId());
                    cmtsFlowQuality.setIfIndex(quality.getIfIndex());
                    cmtsFlowQuality.setInOctets(inOctets_);
                    cmtsFlowQuality.setOutOctets(outOctets_);
                    cmtsFlowQuality.setIfIndexType(quality.getIfIndexType());
                    cmtsFlowQuality.setCollectTime(quality.getCollectTime());
                    if (inOctets_ == WRONG_VALUE) {
                        cmtsFlowQuality.setIfInSpeed(new Long(WRONG_VALUE).floatValue());
                        cmtsFlowQuality.setIfUtilization(new Long(WRONG_VALUE).floatValue());
                    } else if (outOctets_ == WRONG_VALUE) {
                        cmtsFlowQuality.setIfOutSpeed(new Long(WRONG_VALUE).floatValue());
                        cmtsFlowQuality.setIfUtilization(new Long(WRONG_VALUE).floatValue());
                    } else {
                        cmtsFlowQuality.setIfOctets(inOctets_ + outOctets_);
                        // modify by lzt 单位统一为bps
                        float ifInSpeed = (float) inOctets_ * 8 / (period / 1000);
                        float ifOutSpeed = (float) outOctets_ * 8 / (period / 1000);
                        float ifRealSpeed = (inOctets_ * 8 + outOctets_ * 8) / (period / 1000);
                        cmtsFlowQuality.setIfInSpeed(ifInSpeed);
                        cmtsFlowQuality.setIfOutSpeed(ifOutSpeed);
                        if (quality.getIfSpeed() != 0) {
                            Float usage = ifRealSpeed * 100 / quality.getIfSpeed();
                            if (usage > 100) {
                                cmtsFlowQuality.setIfUtilization(-1F);
                            } else {
                                cmtsFlowQuality.setIfUtilization(usage);
                            }
                            cmtsFlowQuality.setIfInUsed(ifInSpeed * 100 / quality.getIfSpeed());
                            cmtsFlowQuality.setIfOutUsed(ifOutSpeed * 100 / quality.getIfSpeed());
                        } else {
                            cmtsFlowQuality.setIfUtilization(0F);
                            cmtsFlowQuality.setIfInUsed(0F);
                            cmtsFlowQuality.setIfOutUsed(0F);
                        }
                    }
                    finalResult.add(cmtsFlowQuality);
                    // 性能阈值数据处理
                    if (cmtsFlowQuality.getIfIndexType().equals(CmtsFlowQuality.UPLINK_CMTS)) {
                        PerformanceData cmtsLinkInUsedData = new PerformanceData(cmcId,
                                CmcUpLinkInUsedHandle.CC_UPLINK_IN_UESD_FLAG, cmtsFlowQuality);
                        PerformanceData cmtsLinkOutUsedData = new PerformanceData(cmcId,
                                CmcUpLinkOutUsedHandle.CC_UPLINK_OUT_UESD_FLAG, cmtsFlowQuality);
                        pList.add(cmtsLinkInUsedData);
                        pList.add(cmtsLinkOutUsedData);
                    }
                    if (cmtsFlowQuality.getIfIndexType().equals(CmtsFlowQuality.CHANNEL_CMTS_UP)
                            || cmtsFlowQuality.getIfIndexType().equals(CmtsFlowQuality.CHANNEL_CMTS_DOWN)) {
                        PerformanceData cmtsChannelInSpeedData = new PerformanceData(cmcId,
                                ChannelSpeedHandle.SPEED_FLAG, cmtsFlowQuality);
                        PerformanceData cmtsUtilizationData = new PerformanceData(cmcId,
                                ChannelUtilizationHandle.UTILIZATION_FLAG, cmtsFlowQuality);
                        pList.add(cmtsChannelInSpeedData);
                        pList.add(cmtsUtilizationData);
                    }
                }
            }
            cmtsPerfDao.insertCmtsFlowQuality(finalResult);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(pList);
        } catch (Exception e) {
            logger.debug("", e);
        }
    }
}
