/***********************************************************************
 * $Id: CmcFlowQualityPerfSaver.java,v1.0 2013-8-13 下午03:21:34 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcFlowQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.ems.cmc.performance.handle.ChannelUtilizationHandle;
import com.topvision.ems.cmc.performance.handle.CmcMacInUsedHandle;
import com.topvision.ems.cmc.performance.handle.CmcMacOutUsedHandle;
import com.topvision.ems.cmc.performance.handle.CmcUpLinkInUsedHandle;
import com.topvision.ems.cmc.performance.handle.CmcUpLinkOutUsedHandle;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-13-下午03:21:34
 * 
 */
@Engine("cmcFlowQualityPerfSaver")
public class CmcFlowQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcFlowQualityPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmcFlowQualityPerfSaver.class);
    private static Map<String, CmcFlowQuality> lastStaticMap = Collections
            .synchronizedMap(new HashMap<String, CmcFlowQuality>());
    private static final long WRONG_VALUE = -1L;
    private DecimalFormat df = new DecimalFormat("#.00");
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcFlowQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcFlowQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey() + "] exec start.");
        }
        CmcPerfDao cmcPerfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        Long cmcId = result.getEntityId();
        List<CmcFlowQuality> finalResult = new ArrayList<CmcFlowQuality>();
        List<PerformanceData> pList = new ArrayList<PerformanceData>();
        try {
            // 更新信道状态
            cmcPerfDao.updateCcmtsPortStatus(cmcId, result.getFlowQualities());
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            // 更新信道质量
            for (CmcFlowQuality quality : result.getFlowQualities()) {
                String key = new StringBuilder(quality.getCmcId().toString()).append("_").append(quality.getIfIndex())
                        .toString();
                CmcFlowQuality lastQuality = lastStaticMap.get(key);
                CmcFlowQuality cmcFlowQuality = new CmcFlowQuality();
                try {
                    lastStaticMap.put(key, quality);
                    if (lastQuality == null) {
                        continue;
                    }
                    // CC信道速率计算必须依靠上一次采集值
                    // 当采集的值为0时需要废弃此采集点 同时清空缓存数据
                    if (lastQuality != null
                            && (lastQuality.getIfHCInOctets() != 0 || lastQuality.getIfHCOutOctets() != 0)) {
                        if (quality.getIfHCInOctets() == 0 && quality.getIfHCOutOctets() == 0) {
                            continue;
                        }
                    }
                    long period = quality.getCollectTime().getTime() - lastQuality.getCollectTime().getTime();
                    if (period == 0) {
                        continue;//从广州现网反馈中发现两次采集的周期间隔为0的异常,对重复的数据已经计算,此次不需要再进行计算
                    }
                    long inOctets_ = quality.getIfHCInOctets() - lastQuality.getIfHCInOctets();
                    long outOctets_ = quality.getIfHCOutOctets() - lastQuality.getIfHCOutOctets();
                    if (inOctets_ < 0) {
                        inOctets_ = WRONG_VALUE;
                    }
                    if (outOctets_ < 0) {
                        outOctets_ = WRONG_VALUE;
                    }
                    //对珠江数码发现的CC设备断电、断纤重新上线后信道速率值超过信道带宽的问题进行处理 
                    //处理方案:对上一次采集的字节数差值为0 本次采集的字节数差值不为0的情况丢弃
                    quality.setInOctets_(inOctets_);
                    quality.setOutOctets_(outOctets_);
                    //厦门出现三个周期内 第一个周期断网 第二个周期可以正常采集 被判断成为了翻转（WRONG_VALUE） 第三个周期又断线
                    //最后导致无法进入判断逻辑的问题
                    if (lastQuality.getInOctets_() == 0 && lastQuality.getInOctets_() == WRONG_VALUE && inOctets_ != 0) {
                        inOctets_ = 0;
                    }
                    if (lastQuality.getOutOctets_() == 0 && lastQuality.getOutOctets_() == WRONG_VALUE && outOctets_ != 0) {
                        outOctets_ = 0;
                    }

                    cmcFlowQuality.setCmcId(quality.getCmcId());
                    cmcFlowQuality.setCmcIndex(quality.getCmcIndex());
                    cmcFlowQuality.setIfIndex(quality.getIfIndex());
                    cmcFlowQuality.setIfInOctets(inOctets_);
                    cmcFlowQuality.setIfOutOctets(outOctets_);
                    cmcFlowQuality.setIfOctets(inOctets_ + outOctets_);
                    cmcFlowQuality.setCollectTime(quality.getCollectTime());
                    cmcFlowQuality.setIfSpeed(quality.getIfSpeed());
                    cmcFlowQuality.setIfType(quality.getIfType());
                    cmcFlowQuality.setIfHighSpeed(quality.getIfHighSpeed());
                    cmcFlowQuality.setQamChannelCommonUtilization(quality.getQamChannelCommonUtilization());
                    if (inOctets_ == WRONG_VALUE || outOctets_ == WRONG_VALUE) {
                        cmcFlowQuality.setIfInSpeed(new Long(WRONG_VALUE).floatValue());
                        cmcFlowQuality.setIfOutSpeed(new Long(WRONG_VALUE).floatValue());
                        cmcFlowQuality.setIfUtilization(new Long(WRONG_VALUE).floatValue());
                    } else {
                        cmcFlowQuality.setIfOctets(inOctets_ + outOctets_);
                        // modify by lzt 单位统一为bps
                        float ifInSpeed = (float) inOctets_ * 8 / (period / 1000);
                        float ifOutSpeed = (float) outOctets_ * 8 / (period / 1000);
                        float ifRealSpeed = (float) (inOctets_ * 8 + outOctets_ * 8) / (period / 1000);

                        cmcFlowQuality.setIfInSpeed(ifInSpeed);
                        cmcFlowQuality.setIfOutSpeed(ifOutSpeed);
                        if (quality.getIfSpeed() != 0) {
                            Float usage = ifRealSpeed * 100 / quality.getIfSpeed();
                            if (usage > 100) {
                                cmcFlowQuality.setIfUtilization(-1F);
                            } else {
                                cmcFlowQuality.setIfUtilization(usage);
                            }
                            cmcFlowQuality.setIfInUsed(ifInSpeed * 100 / quality.getIfSpeed());
                            cmcFlowQuality.setIfOutUsed(ifOutSpeed * 100 / quality.getIfSpeed());
                        } else {
                            cmcFlowQuality.setIfUtilization(0F);
                            cmcFlowQuality.setIfInUsed(0F);
                            cmcFlowQuality.setIfOutUsed(0F);
                        }
                    }

                    // add by loyal EQAM信道利用率
                    if (CmcPort.IF_TYPE_EQAM.equals(cmcFlowQuality.getIfType())
                            && cmcFlowQuality.getQamChannelCommonUtilization() != null) {
                        // 采集利用率
                        cmcFlowQuality
                                .setIfUtilization(cmcFlowQuality.getQamChannelCommonUtilization().floatValue() / 1000);
                    }

                    finalResult.add(cmcFlowQuality);
                    // 性能阈值数据处理
                    if (cmcFlowQuality.getIndexType().equals(CmcFlowQuality.MACDOMAIN_CMC)) {
                        // modify by lzt 由于MAC的ifspeed为0，需要重新计算MAC利用率后发给阈值处理中心
                        List<CmcPort> ports = cmcPerfDao.selectCmcportList(cmcId);
                        Float usBandWidth = 0F;
                        Float dsBandWidth = 0F;
                        for (CmcPort p : ports) {
                            if (p.getIfType() == 129) {
                                usBandWidth = usBandWidth + p.getIfSpeed();
                            } else {
                                dsBandWidth = dsBandWidth + p.getIfSpeed();
                            }
                        }

                        if (usBandWidth == 0) {
                            cmcFlowQuality.setIfInUsed(0F);
                        } else {
                            float macInUtilization = Float
                                    .parseFloat(df.format(100 * (cmcFlowQuality.getIfInSpeed() / usBandWidth)));
                            if (macInUtilization > 100) {// 超过100%为非正常值
                                cmcFlowQuality.setIfInUsed(0F);
                            } else {
                                cmcFlowQuality.setIfInUsed(macInUtilization);
                            }
                        }

                        if (dsBandWidth == 0) {
                            cmcFlowQuality.setIfOutUsed(0F);
                        } else {
                            float macOutUtilization = Float
                                    .parseFloat(df.format(100 * (cmcFlowQuality.getIfOutSpeed() / dsBandWidth)));
                            if (macOutUtilization > 100) {// 超过100%为非正常值
                                cmcFlowQuality.setIfOutUsed(0F);
                            } else {
                                cmcFlowQuality.setIfOutUsed(macOutUtilization);
                            }
                        }
                        /*
                         * PerformanceData cmcMacInSpeedData = new PerformanceData(cmcId,
                         * CmcMacInSpeedHandle.CC_MAC_INSPEED_FLAG, cmcFlowQuality); PerformanceData
                         * cmcMacOutSpeedData = new PerformanceData(cmcId,
                         * CmcMacOutSpeedHandle.CC_MAC_OUTSPEED_FLAG, cmcFlowQuality);
                         */
                        PerformanceData cmcMacInUtilizationData = new PerformanceData(cmcId,
                                CmcMacInUsedHandle.CC_MAC_IN_USED_FLAG, cmcFlowQuality);
                        PerformanceData cmcMacOutUtilizationData = new PerformanceData(cmcId,
                                CmcMacOutUsedHandle.CC_MAC_OUT_USED_FLAG, cmcFlowQuality);
                        // pList.add(cmcMacInSpeedData);
                        // pList.add(cmcMacOutSpeedData);
                        pList.add(cmcMacInUtilizationData);
                        pList.add(cmcMacOutUtilizationData);
                        try {
                            redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.9.1.1", cmcFlowQuality.getIfInSpeed(),
                                    result, cmcFlowQuality.getCmcId(), cmcFlowQuality.getIfIndex());
                            redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.9.1.2", cmcFlowQuality.getIfOutSpeed(),
                                    result, cmcFlowQuality.getCmcId(), cmcFlowQuality.getIfIndex());
                        } catch (Exception e) {
                            logger.info("MACDOMAIN_CMC redirctPerformanceData error", e);
                            logger.error("cmcflowquality saver error, lastQuality: " + lastQuality);
                            logger.error("cmcflowquality saver error, quality: " + quality);
                        }
                    }
                    if (cmcFlowQuality.getIndexType().equals(CmcFlowQuality.UPLINK_CMC)) {
                        /*
                         * PerformanceData cmcLinkInSpeedData = new PerformanceData(cmcId,
                         * CmcUpLinkInSpeedHandle.CC_UPLINK_INSPEED_FLAG, cmcFlowQuality);
                         * PerformanceData cmcLinkOutSpeedData = new PerformanceData(cmcId,
                         * CmcUpLinkOutSpeedHandle.CC_UPLINK_OUTSPEED_FLAG, cmcFlowQuality);
                         */
                        PerformanceData cmcLinkInUsedData = new PerformanceData(cmcId,
                                CmcUpLinkInUsedHandle.CC_UPLINK_IN_UESD_FLAG, cmcFlowQuality);
                        PerformanceData cmcLinkOutUsedData = new PerformanceData(cmcId,
                                CmcUpLinkOutUsedHandle.CC_UPLINK_OUT_UESD_FLAG, cmcFlowQuality);
                        // pList.add(cmcLinkInSpeedData);
                        // pList.add(cmcLinkOutSpeedData);
                        pList.add(cmcLinkInUsedData);
                        pList.add(cmcLinkOutUsedData);
                        try {
                            redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.9.1.4", cmcFlowQuality.getIfInSpeed(),
                                    result, cmcFlowQuality.getCmcId(), cmcFlowQuality.getIfIndex());
                            redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.9.1.5", cmcFlowQuality.getIfOutSpeed(),
                                    result, cmcFlowQuality.getCmcId(), cmcFlowQuality.getIfIndex());
                            redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.9.1.6", cmcFlowQuality.getIfInUsed(),
                                    result, cmcFlowQuality.getCmcId(), cmcFlowQuality.getIfIndex());
                            redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.9.1.7", cmcFlowQuality.getIfOutUsed(),
                                    result, cmcFlowQuality.getCmcId(), cmcFlowQuality.getIfIndex());
                        } catch (Exception e) {
                            logger.info("UPLINK_CMC redirctPerformanceData error", e);
                            logger.error("cmcflowquality saver error, lastQuality: " + lastQuality);
                            logger.error("cmcflowquality saver error, quality: " + quality);
                        }
                    }
                    if (cmcFlowQuality.getIndexType().equals(CmcFlowQuality.CHANNEL_CMC)) {
                        /*
                         * PerformanceData cmcChannelInSpeedData = new PerformanceData(cmcId,
                         * ChannelSpeedHandle.SPEED_FLAG, cmcFlowQuality);
                         */
                        PerformanceData cmcUtilizationData = new PerformanceData(cmcId,
                                ChannelUtilizationHandle.UTILIZATION_FLAG, cmcFlowQuality);
                        // pList.add(cmcChannelInSpeedData);
                        pList.add(cmcUtilizationData);
                        try {
                            redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.8.1.1",
                                    (cmcFlowQuality.getIfInSpeed() + cmcFlowQuality.getIfOutSpeed()), result,
                                    cmcFlowQuality.getCmcId(), cmcFlowQuality.getIfIndex());
                            redirctPerformanceData("1.3.6.1.4.1.32285.12.2.3.9.8.1.2",
                                    cmcFlowQuality.getIfUtilization(), result, cmcFlowQuality.getCmcId(),
                                    cmcFlowQuality.getIfIndex());
                        } catch (Exception e) {
                            logger.info("CHANNEL_CMC redirctPerformanceData error", e);
                            logger.error("cmcflowquality saver error, lastQuality: " + lastQuality);
                            logger.error("cmcflowquality saver error, quality: " + quality);
                        }
                    }
                } catch (Exception e) {
                    // 记录lastQuality和quality
                    logger.error("cmcflowquality saver error, lastQuality: " + lastQuality);
                    logger.error("cmcflowquality saver error, quality: " + quality);
                    logger.error("cmcflowquality saver error: ", e);
                    try {
                        finalResult.remove(cmcFlowQuality);//如果在计算过程中出现异常的数据,最后不会推送到dao进行存储
                    } catch (Exception exp) {
                        logger.info("finalResult remove object error: ", exp);
                    }
                }
            }
            cmcPerfDao.insertCmcFlowQuality(finalResult);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(pList);
        } catch (Exception e) {
            logger.debug("", e);
        }
    }
}
