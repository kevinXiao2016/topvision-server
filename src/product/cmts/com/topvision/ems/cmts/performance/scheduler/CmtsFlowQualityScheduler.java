/***********************************************************************
 * $Id: CmtsFlowQualityScheduler.java,v1.0 2014-4-16 上午11:13:14 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.performance.facade.CmtsFlowQuality;
import com.topvision.ems.cmc.performance.facade.CmtsXFlowQuality;
import com.topvision.ems.cmts.performance.domain.CmtsFlowQualityPerf;
import com.topvision.ems.cmts.performance.domain.CmtsFlowQualityPerfResult;
import com.topvision.ems.cmts.performance.engine.CmtsPerfDao;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;

/**
 * @author Rod John
 * @created @2014-4-16-上午11:13:14
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmtsFlowQualityScheduler")
public class CmtsFlowQualityScheduler extends AbstractExecScheduler<CmtsFlowQualityPerf> {
    private static final long MAX_IFTABLE = 4294967295L;
    private Logger collectLogger = LoggerUtil.sampleCollectLogger;

    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void exec() {
        Thread.currentThread().setName("CmtsFlowQualityScheduler_" + operClass.getEntityId());
        if (logger.isDebugEnabled()) {
            logger.debug("CmtsFlowQualityScheduler entityId[" + operClass.getEntityId() + "] exec start.");
        }
        // Add by Rod 增加在Engine端回调记录性能任务执行时间
        try {
            if (operClass.getMonitorId() != null) {
                getCallback()
                        .recordTaskCollectTime(operClass.getMonitorId(), new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            long entityId = operClass.getEntityId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            List<CmtsFlowQuality> cmtsFlowQualities = new ArrayList<>();
            CmtsFlowQualityPerfResult flowQualityPerfResult = new CmtsFlowQualityPerfResult(operClass);
            try {
                //TODO Victor 需要验证是否正确
                //Modify by Victor@20150618原来是通过回调接口调用performanceDao对应方法，现在移动到engine端CmtsPerfDao中
                CmtsPerfDao perfDao = engineDaoFactory.getEngineDao(CmtsPerfDao.class);
                Timestamp collectTime = new Timestamp(System.currentTimeMillis());
                @SuppressWarnings("deprecation")
                Integer sampleCollect = getCallback().getCmtsSampleCollect(operClass.getTypeId());
                collectLogger.info("Uplink Collect: " + operClass.getIsUpLinkFlow() + " Channel Collect: "
                        + operClass.getIsChannelFlow());
                if (operClass.getIsUpLinkFlow()) {
                    List<Long> upLinkIndexs = perfDao.getUpLinkIndex(entityId);
                    collectLogger.info("Uplink Index: " + upLinkIndexs);
                    if (sampleCollect != null && sampleCollect > 0) {
                        collectLogger.info("Start cmts uplink flow sample collect");
                        flowQualityPerfResult = cmtsFlowQualityPerfResultOnSampleCollect(operClass, upLinkIndexs,
                                CmtsFlowQuality.UPLINK_CMTS, sampleCollect);
                        flowQualityPerfResult.setCollectType(CmtsFlowQualityPerfResult.SAMPLE_COLLECT);
                    } else {
                        flowQualityPerfResult = new CmtsFlowQualityPerfResult(operClass);
                        CmtsFlowQuality upFlowQuality = null;
                        // UPLINK FLOW
                        for (Long ifIndex : upLinkIndexs) {
                            upFlowQuality = this.collectCmtsFlowQuality(entityId, ifIndex, CmtsFlowQuality.UPLINK_CMTS,
                                    collectTime);
                            if (upFlowQuality != null) {
                                cmtsFlowQualities.add(upFlowQuality);
                            }
                        }
                        flowQualityPerfResult.addCmtsFlowQualites(cmtsFlowQualities);
                        flowQualityPerfResult.setCollectType(CmtsFlowQualityPerfResult.INTERVAL_COLLECT);
                    }
                }
                if (operClass.getIsChannelFlow()) {
                    List<Long> upChannelIndexs = perfDao.getUpChannelIndex(entityId);
                    List<Long> downChannelIndexs = perfDao.getDownChannelIndex(entityId);
                    if (sampleCollect != null && sampleCollect > 0) {
                        collectLogger.info("Start cmts channel speed sample collect");
                        flowQualityPerfResult = new CmtsFlowQualityPerfResult(operClass);
                        CmtsFlowQualityPerfResult upChannelFlowResult = cmtsFlowQualityPerfResultOnSampleCollect(
                                operClass, upChannelIndexs, CmtsFlowQuality.CHANNEL_CMTS_UP, sampleCollect);
                        CmtsFlowQualityPerfResult downChannelFlowResult = cmtsFlowQualityPerfResultOnSampleCollect(
                                operClass, downChannelIndexs, CmtsFlowQuality.CHANNEL_CMTS_DOWN, sampleCollect);
                        flowQualityPerfResult.addCmtsFlowQualites(upChannelFlowResult.getCmtsFlowQualities());
                        flowQualityPerfResult.addCmtsFlowQualites(downChannelFlowResult.getCmtsFlowQualities());
                        flowQualityPerfResult.setCollectType(CmtsFlowQualityPerfResult.SAMPLE_COLLECT);
                    } else {
                        flowQualityPerfResult = new CmtsFlowQualityPerfResult(operClass);
                        CmtsFlowQuality channelSpeedQuality = null;
                        // CHANNEL FLOW
                        for (Long ifIndex : upChannelIndexs) {
                            channelSpeedQuality = this.collectCmtsFlowQuality(entityId, ifIndex,
                                    CmtsFlowQuality.CHANNEL_CMTS_UP, collectTime);
                            cmtsFlowQualities.add(channelSpeedQuality);
                        }
                        for (Long ifIndex : downChannelIndexs) {
                            channelSpeedQuality = this.collectCmtsFlowQuality(entityId, ifIndex,
                                    CmtsFlowQuality.CHANNEL_CMTS_DOWN, collectTime);
                            cmtsFlowQualities.add(channelSpeedQuality);
                        }
                        flowQualityPerfResult.addCmtsFlowQualites(cmtsFlowQualities);
                        flowQualityPerfResult.setCollectType(CmtsFlowQualityPerfResult.INTERVAL_COLLECT);
                        //flowQualityPerfResult.addCmtsXFlowQualites(cmtsXFlowQualities);
                    }
                }
            } catch (Exception e) {
                logger.info("", e);
            }
            logger.trace("CmtsFlowQualityPerfSchedual write result to file.");
            flowQualityPerfResult.setEntityId(entityId);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(flowQualityPerfResult);
        } catch (Exception e) {
            logger.info("", e);
        }
        logger.debug("CmtsFlowQualityPerfSchedual exec end.");
    }

    /**
     * 进行CMTS速率采样
     * @param operClass
     * @param channelIndexs
     * @param indexType
     * @param collectInterval
     * @return
     */
    private CmtsFlowQualityPerfResult cmtsFlowQualityPerfResultOnSampleCollect(CmtsFlowQualityPerf operClass,
            List<Long> channelIndexs, Integer indexType, Integer collectInterval) {
        CmtsFlowQualityPerfResult flowQualityPerfResult = new CmtsFlowQualityPerfResult(operClass);
        List<CmtsFlowQuality> firstFlowQualities = new ArrayList<>();
        Timestamp firstCollectTime = new Timestamp(System.currentTimeMillis());
        CmtsFlowQuality firstCollect = null;
        for (Long ifIndex : channelIndexs) {
            firstCollect = this.collectCmtsFlowQuality(operClass.getEntityId(), ifIndex, indexType, firstCollectTime);
            if (firstCollect != null) {
                firstFlowQualities.add(firstCollect);
            }
        }
        try {
            Thread.sleep(collectInterval * 1000);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
        List<CmtsFlowQuality> resultFlowQualities = new ArrayList<>();
        CmtsFlowQuality resultFlow = null;
        CmtsFlowQuality secondFlow = null;
        Timestamp secondCollectTime = new Timestamp(System.currentTimeMillis());
        for (CmtsFlowQuality firstFlow : firstFlowQualities) {
            secondFlow = this.collectCmtsFlowQuality(operClass.getEntityId(), firstFlow.getIfIndex(), indexType,
                    secondCollectTime);
            if (secondFlow != null) {
                resultFlow = this.getSampleCmtsFlowQuality(firstFlow, secondFlow);
                if (resultFlow != null) {
                    resultFlowQualities.add(resultFlow);
                }
            }
        }
        flowQualityPerfResult.setCmtsFlowQualities(resultFlowQualities);
        return flowQualityPerfResult;
    }

    /**
     * 获取采样的CmtsFlowQuality结果
     * 根据两次采样的结果进行计算
     * @param first
     * @param second
     * @return
     */
    private CmtsFlowQuality getSampleCmtsFlowQuality(CmtsFlowQuality first, CmtsFlowQuality second) {
        try {
            CmtsFlowQuality flowQuality = new CmtsFlowQuality();
            flowQuality.setEntityId(second.getCmcId());
            flowQuality.setCmcId(second.getCmcId());
            flowQuality.setIfIndex(second.getIfIndex());
            flowQuality.setIfIndexType(second.getIfIndexType());
            flowQuality.setCollectTime(second.getCollectTime());
            long inOctets_ = second.getIfInOctets() - first.getIfInOctets();
            long outOctets_ = second.getIfOutOctets() - first.getIfOutOctets();
            long interval = (second.getCollectTime().getTime() - first.getCollectTime().getTime()) / 1000;
            if (inOctets_ < 0) {
                if (MAX_IFTABLE + inOctets_ < second.getIfSpeed() * interval / 8) {
                    inOctets_ = MAX_IFTABLE + inOctets_;
                } else {
                    return null;
                }
            }
            if (outOctets_ < 0) {
                if (MAX_IFTABLE + outOctets_ < second.getIfSpeed() * interval / 8) {
                    outOctets_ = MAX_IFTABLE + outOctets_;
                } else {
                    return null;
                }
            }
            flowQuality.setInOctets(inOctets_);
            flowQuality.setOutOctets(outOctets_);
            flowQuality.setIfOctets(inOctets_ + outOctets_);
            // modify by lzt 单位统一为bps
            float ifInSpeed = (float) inOctets_ * 8 / interval;
            float ifOutSpeed = (float) outOctets_ * 8 / interval;
            flowQuality.setIfInSpeed(ifInSpeed);
            flowQuality.setIfOutSpeed(ifOutSpeed);
            flowQuality.setIfAdminStatus(second.getIfAdminStatus());
            flowQuality.setIfSpeed(second.getIfSpeed());
            flowQuality.setIfOperStatus(second.getIfOperStatus());
            collectLogger.info("Sample collect result: " + flowQuality);
            if (flowQuality.getIfSpeed() != 0) {
                float maxChannelSpeed = Math.max(ifInSpeed, ifOutSpeed);
                float utilization = maxChannelSpeed * 100 / flowQuality.getIfSpeed();
                // 按照李永成要求去掉超过100的利用率数据
                if (utilization >= 100) {
                    flowQuality.setIfUtilization(-1F);
                }
                flowQuality.setIfUtilization(utilization);
                flowQuality.setIfInUsed(ifInSpeed * 100 / flowQuality.getIfSpeed());
                flowQuality.setIfOutUsed(ifOutSpeed * 100 / flowQuality.getIfSpeed());
            } else {
                flowQuality.setIfUtilization(0F);
                flowQuality.setIfInUsed(0F);
                flowQuality.setIfOutUsed(0F);
            }
            return flowQuality;
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 从设备采集CmtsFlowQuality
     * 因为在多处使用，所以单独使用一个方法处理
     * @param entityId
     * @param channelIndex
     * @param indexType
     * @param collectTime
     * @return
     */
    private CmtsFlowQuality collectCmtsFlowQuality(Long entityId, Long channelIndex, Integer indexType,
            Timestamp collectTime) {
        try {
            try {
                CmtsXFlowQuality xquality = new CmtsXFlowQuality();
                xquality.setEntityId(entityId);
                xquality.setCmcId(entityId);
                xquality.setIfIndex(channelIndex);
                xquality.setIfIndexType(indexType);
                xquality.setCollectTime(collectTime);
                List<String> excludeOids = null;
                if (indexType.equals(CmtsFlowQuality.UPLINK_CMTS)) {
                    xquality = snmpExecutorService.getTableLine(snmpParam, xquality);
                } else if (indexType.equals(CmtsFlowQuality.CHANNEL_CMTS_UP)) {
                    excludeOids = new ArrayList<String>();
                    excludeOids.add("1.3.6.1.2.1.31.1.1.1.10");
                    xquality = snmpExecutorService.getTableLine(snmpParam, xquality, excludeOids);
                    xquality.setIfHCOutOctets(0L);
                } else if (indexType.equals(CmtsFlowQuality.CHANNEL_CMTS_DOWN)) {
                    excludeOids = new ArrayList<String>();
                    excludeOids.add("1.3.6.1.2.1.31.1.1.1.6");
                    xquality = snmpExecutorService.getTableLine(snmpParam, xquality, excludeOids);
                    xquality.setIfHCInOctets(0L);
                }
                return new CmtsFlowQuality(xquality);
            } catch (SnmpNoSuchInstanceException e) {
                CmtsFlowQuality quality = new CmtsFlowQuality();
                quality.setEntityId(entityId);
                quality.setCmcId(entityId);
                quality.setIfIndex(channelIndex);
                quality.setIfIndexType(indexType);
                quality.setCollectTime(collectTime);
                List<String> excludeOids = null;
                if (indexType.equals(CmtsFlowQuality.UPLINK_CMTS)) {
                    quality = snmpExecutorService.getTableLine(snmpParam, quality);
                } else if (indexType.equals(CmtsFlowQuality.CHANNEL_CMTS_UP)) {
                    excludeOids = new ArrayList<String>();
                    excludeOids.add("1.3.6.1.2.1.2.2.1.16");
                    quality = snmpExecutorService.getTableLine(snmpParam, quality, excludeOids);
                    quality.setIfOutOctets(0L);
                } else if (indexType.equals(CmtsFlowQuality.CHANNEL_CMTS_DOWN)) {
                    excludeOids = new ArrayList<String>();
                    excludeOids.add("1.3.6.1.2.1.2.2.1.10");
                    quality = snmpExecutorService.getTableLine(snmpParam, quality, excludeOids);
                    quality.setIfInOctets(0L);
                }
                return quality;
            }
        } catch (Exception excep) {
            logger.error("", excep);
            return null;
        }
    }
}
