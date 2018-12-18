/***********************************************************************
 * $Id: OnuFlowQualitySaver.java,v1.0 2015-5-7 上午11:31:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.epon.performance.domain.OnuFlowCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuFlowQualityResult;
import com.topvision.ems.epon.performance.engine.OnuPerfDao;
import com.topvision.ems.epon.performance.handle.OnuPonInSpeedHandle;
import com.topvision.ems.epon.performance.handle.OnuPonOutSpeedHandle;
import com.topvision.ems.epon.performance.handle.OnuUniInSpeedHandle;
import com.topvision.ems.epon.performance.handle.OnuUniOutSpeedHandle;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.annotation.Engine;

/**
 * @author flack
 * @created @2015-5-7-上午11:31:05
 *
 */
@Engine("onuFlowQualitySaver")
public class OnuFlowQualitySaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<OnuFlowQualityResult, OperClass> {
    private static Map<String, OnuFlowCollectInfo> lastFlowMap = new ConcurrentHashMap<String, OnuFlowCollectInfo>();
    private static final float WRONG_VALUE = -1f;
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(OnuFlowQualityResult result) {
        OnuPerfDao onuPerfDao = engineDaoFactory.getEngineDao(OnuPerfDao.class);
        if (result != null) {
            Long entityId = result.getEntityId();
            Long onuId = result.getOnuId();
            Long onuIndex = result.getOnuIndex();
            List<OnuFlowCollectInfo> finalResultList = new ArrayList<OnuFlowCollectInfo>();
            List<PerformanceData> perfList = new ArrayList<PerformanceData>();
            try {
                OnuFlowCollectInfo lastFlow = null;
                for (OnuFlowCollectInfo onuFlow : result.getOnuFlowPerfs()) {
                    String key = new StringBuilder(onuId.toString()).append("_").append(onuFlow.getPortIndex())
                            .toString();
                    lastFlow = lastFlowMap.get(key);
                    try {
                        lastFlowMap.put(key, onuFlow);
                        if (lastFlow == null) {
                            continue;
                        }
                        onuFlow.setEntityId(entityId);
                        onuFlow.setOnuId(onuId);
                        onuFlow.setOnuIndex(onuIndex);
                        long period = onuFlow.getCollectTime().getTime() - lastFlow.getCollectTime().getTime();
                        if (period == 0) {
                            continue;//从广州现网反馈中发现两次采集的周期间隔为0的异常,对重复的数据已经计算,此次不需要再进行计算
                        }
                        long inOctets = onuFlow.getPortInOctets() - lastFlow.getPortInOctets();
                        long outOctets = onuFlow.getPortOutOctets() - lastFlow.getPortOutOctets();
                        if (inOctets < 0) {
                            onuFlow.setPortInSpeed(WRONG_VALUE);
                        } else {
                            // 单位是bps
                            float portInSpeed = (float) inOctets * 8 / (period / 1000);
                            onuFlow.setPortInSpeed(portInSpeed);
                        }
                        if (outOctets < 0) {
                            onuFlow.setPortOutSpeed(WRONG_VALUE);
                        } else {
                            // 单位是bps
                            float portOutSpeed = (float) outOctets * 8 / (period / 1000);
                            onuFlow.setPortOutSpeed(portOutSpeed);
                        }
                        if (PerfTargetConstants.PORTTYPE_PON.equals(onuFlow.getPortType())) {
                            if (onuFlow.getPortInSpeed() != WRONG_VALUE) {
                                PerformanceData ponInSpeedData = new PerformanceData(onuId,
                                        OnuPonInSpeedHandle.ONU_PON_IN_SPEED, onuFlow);
                                perfList.add(ponInSpeedData);
                            }
                            if (onuFlow.getPortOutSpeed() != WRONG_VALUE) {
                                PerformanceData ponOutSpeedData = new PerformanceData(onuId,
                                        OnuPonOutSpeedHandle.ONU_PON_OUT_SPEED, onuFlow);
                                perfList.add(ponOutSpeedData);
                            }
                        }
                        if (PerfTargetConstants.PORTTYPE_UNI.equals(onuFlow.getPortType())) {
                            if (onuFlow.getPortInSpeed() != WRONG_VALUE) {
                                PerformanceData uniInSpeedData = new PerformanceData(onuId,
                                        OnuUniInSpeedHandle.ONU_UNI_IN_SPEED, onuFlow);
                                perfList.add(uniInSpeedData);
                            }
                            if (onuFlow.getPortOutSpeed() != WRONG_VALUE) {
                                PerformanceData uniOutSpeedData = new PerformanceData(onuId,
                                        OnuUniOutSpeedHandle.ONU_UNI_OUT_SPEED, onuFlow);
                                perfList.add(uniOutSpeedData);
                            }
                        }
                        finalResultList.add(onuFlow);
                        try {
                            redirctPerformanceData(onuFlow, result, entityId, onuFlow.getPortIndex());
                        } catch (Exception e) {
                            logger.info("onuflowquality redirctPerformanceData error", e);
                            logger.info("onuflowquality redirctPerformanceData onuFlow: " + onuFlow);
                        }
                    } catch (Exception e) {
                        // 记录lastQuality和quality
                        logger.info("onuflowquality saver error: ", e);
                        logger.info("onuflowquality saver error, lastFlow: " + lastFlow);
                        logger.info("onuflowquality saver error, onuFlow: " + onuFlow);

                    }
                }
                onuPerfDao.batchInsertOnuFlowQuality(finalResultList);
                getCallback(PerformanceCallback.class).sendPerfomaceResult(perfList);
            } catch (Exception e) {
                logger.error("Onu[{}] flow data save failed : {}", onuId, e);
            }
        }
    }
}
