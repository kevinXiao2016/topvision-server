/***********************************************************************
 * $Id: EponFlowQualityPerfSaver.java,v1.0 2013-8-7 下午04:42:37 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dbsaver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.epon.performance.domain.EponFlowQualityPerfResult;
import com.topvision.ems.epon.performance.engine.OltPerfDao;
import com.topvision.ems.epon.performance.facade.OltFlowQuality;
import com.topvision.ems.epon.performance.handle.OltPonPortInSpeedHandle;
import com.topvision.ems.epon.performance.handle.OltPonPortInUsedHandle;
import com.topvision.ems.epon.performance.handle.OltPonPortOutSpeedHandle;
import com.topvision.ems.epon.performance.handle.OltPonPortOutUsedHandle;
import com.topvision.ems.epon.performance.handle.OltSniPortInSpeedHandle;
import com.topvision.ems.epon.performance.handle.OltSniPortInUsedHandle;
import com.topvision.ems.epon.performance.handle.OltSniPortOutSpeedHandle;
import com.topvision.ems.epon.performance.handle.OltSniPortOutUsedHandle;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-7-下午04:42:37
 * 
 */
@Engine("eponFlowQualityPerfSaver")
public class EponFlowQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<EponFlowQualityPerfResult, OperClass> {
    private static Map<String, OltFlowQuality> lastStaticMap = Collections
            .synchronizedMap(new HashMap<String, OltFlowQuality>());
    private static final long WRONG_VALUE = -1L;
    private static final long UNIT_M = 1000 * 1000L;
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(EponFlowQualityPerfResult result) {
        OltPerfDao perfDao = engineDaoFactory.getEngineDao(OltPerfDao.class);
        Long entityId = result.getEntityId();
        List<PerformanceData> pList = new ArrayList<PerformanceData>();
        List<OltFlowQuality> finalResult = new ArrayList<OltFlowQuality>();
        try {
            // 更新信道质量
            for (OltFlowQuality quality : result.getFlowPerfs()) {
                String key = new StringBuilder(quality.getEntityId().toString()).append("_")
                        .append(quality.getIfIndex()).toString();
                OltFlowQuality lastQuality = lastStaticMap.get(key);
                lastStaticMap.put(key, quality);
                OltFlowQuality oltFlowQuality = new OltFlowQuality();
                // 速率计算必须依靠上一次采集值
                try {
                    if (lastQuality == null) {
                        continue;
                    }
                    long period = quality.getCollectTime().getTime() - lastQuality.getCollectTime().getTime();
                    long inOctets_ = quality.getIfHCInOctets() - lastQuality.getIfHCInOctets();
                    long outOctets_ = quality.getIfHCOutOctets() - lastQuality.getIfHCOutOctets();
                    if (inOctets_ < 0) {
                        inOctets_ = WRONG_VALUE;
                    }
                    if (outOctets_ < 0) {
                        outOctets_ = WRONG_VALUE;
                    }

                    oltFlowQuality.setEntityId(entityId);
                    oltFlowQuality.setIfIndex(quality.getIfIndex());
                    oltFlowQuality.setPortIndex(quality.getPortIndex());
                    oltFlowQuality.setIfInOctets(inOctets_);
                    oltFlowQuality.setIfOutOctets(outOctets_);
                    oltFlowQuality.setPortType(quality.getPortType());
                    oltFlowQuality.setCollectStatus(quality.getCollectStatus());
                    oltFlowQuality.setCollectTime(quality.getCollectTime());
                    oltFlowQuality.setPortBandwidth(quality.getIfHighSpeed());
                    oltFlowQuality.setDsBandwidth(quality.getDsBandwidth());
                    oltFlowQuality.setUsBandwidth(quality.getUsBandwidth());
                    if (inOctets_ == WRONG_VALUE || outOctets_ == WRONG_VALUE) {
                        oltFlowQuality.setIfInSpeed(new Long(WRONG_VALUE).floatValue());
                        oltFlowQuality.setIfOutSpeed(new Long(WRONG_VALUE).floatValue());
                        oltFlowQuality.setPortInUsed(-1F);
                        oltFlowQuality.setPortOutUsed(-1F);
                    } else {
                        float ifInSpeed = (float) inOctets_ * 8 / (period / 1000);
                        float ifOutSpeed = (float) outOctets_ * 8 / (period / 1000);
                        // modify by lzt EMS-10435
                        // modify by ls EMS-15576
                        if(quality.getPortType()=="PON"){
                            if (ifInSpeed >= quality.getUsBandwidth()*1000
                                    || ifOutSpeed >= quality.getDsBandwidth()*1000) {
                                ifInSpeed = 0;
                                ifOutSpeed = 0;
                            }
                        }else{
                            if (ifInSpeed >= (quality.getIfHighSpeed()*UNIT_M)
                                    || ifOutSpeed >= (quality.getIfHighSpeed()*UNIT_M)) {
                                ifInSpeed = 0;
                                ifOutSpeed = 0;
                            }
                        }
                        
                        float portInUsed = 0F;
                        float portOutUsed = 0F;
                        if(quality.getPortType()=="PON"){
                            if (quality.getDsBandwidth() != 0) {
                                portInUsed = ifInSpeed / (quality.getUsBandwidth()*1000);
                            }
                            if(quality.getUsBandwidth() != 0){
                                portOutUsed = ifOutSpeed / (quality.getDsBandwidth()*1000);
                            }
                        }else{
                            if (quality.getIfHighSpeed() != 0) {
                                portInUsed = ifInSpeed / (quality.getIfHighSpeed()*UNIT_M);
                                portOutUsed = ifOutSpeed / (quality.getIfHighSpeed()*UNIT_M);
                            }
                        }
                        
                        // float ifRealSpeed = (inOctets_ * 8 + outOctets_ * 8) / (period / 1000);//
                        // ifRealSpeed的单位为bps,modified
                        oltFlowQuality.setIfInSpeed(ifInSpeed);
                        oltFlowQuality.setPortInUsed(portInUsed);
                        oltFlowQuality.setIfOutSpeed(ifOutSpeed);
                        oltFlowQuality.setPortOutUsed(portOutUsed);
                    }
                    if (oltFlowQuality.getPortType().equals("SNI")) {
                        PerformanceData sniInSpeedData = new PerformanceData(entityId,
                                OltSniPortInSpeedHandle.SNI_INSPPED_FLAG, oltFlowQuality);
                        PerformanceData sniOutSpeedData = new PerformanceData(entityId,
                                OltSniPortOutSpeedHandle.SNI_OUTSPPED_FLAG, oltFlowQuality);
                        PerformanceData sniInUsedData = new PerformanceData(entityId,
                                OltSniPortInUsedHandle.SNI_INUSED_FLAG, oltFlowQuality);
                        PerformanceData sniOutUsedData = new PerformanceData(entityId,
                                OltSniPortOutUsedHandle.SNI_OUTUSED_FLAG, oltFlowQuality);
                        pList.add(sniInSpeedData);
                        pList.add(sniOutSpeedData);
                        pList.add(sniInUsedData);
                        pList.add(sniOutUsedData);
                    } else if (oltFlowQuality.getPortType().equals("PON")) {
                        PerformanceData ponInSpeedData = new PerformanceData(entityId,
                                OltPonPortInSpeedHandle.PON_INSPPED_FLAG, oltFlowQuality);
                        PerformanceData ponOutSpeedData = new PerformanceData(entityId,
                                OltPonPortOutSpeedHandle.PON_OUTSPPED_FLAG, oltFlowQuality);
                        PerformanceData ponInUsedData = new PerformanceData(entityId,
                                OltPonPortInUsedHandle.PON_INUSED_FLAG, oltFlowQuality);
                        PerformanceData ponOutUsedData = new PerformanceData(entityId,
                                OltPonPortOutUsedHandle.PON_OUTUSED_FLAG, oltFlowQuality);
                        pList.add(ponInSpeedData);
                        pList.add(ponOutSpeedData);
                        pList.add(ponInUsedData);
                        pList.add(ponOutUsedData);
                    }
                    try {
                        redirctPerformanceData(oltFlowQuality, result, entityId, quality.getPortIndex());
                    } catch (Exception exp) {
                        logger.info("Eponflowquality redirctPerformanceData info : " + oltFlowQuality);
                        logger.info("Eponflowquality redirctPerformanceData error ", exp);
                    }
                    finalResult.add(oltFlowQuality);
                } catch (Exception e) {
                    // 记录lastQuality和quality
                    logger.error("oltflowquality saver error, lastQuality: " + lastQuality);
                    logger.error("oltflowquality saver error, quality: " + quality);
                    logger.error("oltflowquality saver error: ", e);
                    try {
                        finalResult.remove(oltFlowQuality);//如果在计算过程中出现异常的数据,最后不会推送到dao进行存储
                    } catch (Exception exp) {
                        logger.info("finalResult remove object error: ", exp);
                    }
                }
            }
            perfDao.insertEponFlowQuality(entityId, finalResult);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(pList);
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

}
