/***********************************************************************
 * $Id: UplinkSpeedStaticPerfDBSaver.java,v1.0 2012-7-15 上午11:26:01 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.dbsaver;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmts.dao.CmtsPerfDao;
import com.topvision.ems.cmts.facade.domain.UplinkSpeedPerf;
import com.topvision.ems.cmts.performance.domain.IfUtilization;
import com.topvision.ems.cmts.performance.domain.UplinkSpeedStatic;
import com.topvision.ems.cmts.performance.domain.UplinkSpeedStaticPerfResult;
import com.topvision.ems.cmts.performance.handle.CmtsUpLinkInSpeedHandle;
import com.topvision.ems.cmts.performance.handle.CmtsUpLinkOutSpeedHandle;
import com.topvision.ems.cmts.performance.handle.CmtsUpLinkUsedHandle;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author jay
 * @created @2012-7-15-上午11:26:01
 */
@Engine("uplinkSpeedStaticPerfDBSaver")
public class UplinkSpeedStaticPerfDBSaver extends BaseEngine implements
        PerfEngineSaver<UplinkSpeedStaticPerfResult, OperClass> {
    private final Logger logger = LoggerFactory.getLogger(UplinkSpeedStaticPerfDBSaver.class);
    private static Map<Long, UplinkSpeedStaticPerfResult> lastStaticMap = Collections
            .synchronizedMap(new HashMap<Long, UplinkSpeedStaticPerfResult>());
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(UplinkSpeedStaticPerfResult uplinkSpeedStaticPerfResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("UplinkSpeedStaticPerfDBSaver identifyKey["
                    + uplinkSpeedStaticPerfResult.getDomain().getIdentifyKey() + "] exec start.");
        }
        CmtsPerfDao cmtsPerfDao = engineDaoFactory.getEngineDao(CmtsPerfDao.class);
        List<UplinkSpeedPerf> uplinkSpeedPerfs = uplinkSpeedStaticPerfResult.getUplinkSpeedPerf();
        long entityId = uplinkSpeedStaticPerfResult.getEntityId();
        if (uplinkSpeedPerfs != null) {
            UplinkSpeedStaticPerfResult lastChannelSpeedStaticPerfResult = lastStaticMap.get(entityId);
            lastStaticMap.put(entityId, uplinkSpeedStaticPerfResult);
            if (lastChannelSpeedStaticPerfResult != null) {
                float period = uplinkSpeedStaticPerfResult.getDt() - lastChannelSpeedStaticPerfResult.getDt();
                if (period > 0) {
                    for (UplinkSpeedPerf uplinkSpeedPerf : uplinkSpeedPerfs) {
                        long ifIndex = uplinkSpeedPerf.getIfIndex();
                        // 更新通道的状态
                        cmtsPerfDao.updateCmtsPortStatus(entityId, uplinkSpeedPerf);
                        UplinkSpeedPerf lastPortPerf = null;
                        for (UplinkSpeedPerf portPerf : lastChannelSpeedStaticPerfResult.getUplinkSpeedPerf()) {
                            if (portPerf.getIfIndex().equals(ifIndex)) {
                                lastPortPerf = portPerf;
                            }
                        }
                        if (lastPortPerf != null) {

                            float inOctets = uplinkSpeedPerf.getIfInOctets() - lastPortPerf.getIfInOctets();
                            float outOctets = uplinkSpeedPerf.getIfOutOctets() - lastPortPerf.getIfOutOctets();
                            long ifSpeed = uplinkSpeedPerf.getIfSpeed();
                            float inUtilization;
                            float outUtilization;
                            if (inOctets < 0) {
                                // 判断是否异常翻转，现在只能识别出由于异常翻转导致速率大于带宽的情况
                                //                 (Byte * 8)     < bps     * （ms/1000)(s)
                                if ((4294967295L + inOctets) * 8 < ifSpeed * (period / 1000)) {
                                    inOctets = 4294967295L + inOctets;
                                } else {
                                    continue;
                                }

                            }
                            // 计算上行通道的通道利用率
                            if (ifSpeed != 0) {
                                //            （(Byte * 8)=bits      / 秒             ) / bps      * 100 = x%
                                inUtilization = Math.abs(inOctets) * 8 / (period / 1000) / ifSpeed * 100;
                            } else {
                                inUtilization = 0;
                            }
                            if (outOctets < 0) {
                                // 判断是否异常翻转，现在只能识别出由于异常翻转导致速率大于带宽的情况
                                //                 (Byte * 8)     < bps     * （ms/1000)(s)
                                if ((4294967295L + outOctets) * 8 < ifSpeed * (period / 1000)) {
                                    outOctets = 4294967295L + outOctets;
                                } else {
                                    continue;
                                }
                            }
                            // 计算下行通道的通道利用率
                            if (ifSpeed != 0) {
                                //            （(Byte * 8)=bits      / 秒             ) / bps      * 100 = x%
                                outUtilization = Math.abs(outOctets) * 8 / (period / 1000) / ifSpeed * 100;
                            } else {
                                outUtilization = 0;
                            }
                            Timestamp dt = new Timestamp(uplinkSpeedStaticPerfResult.getDt());
                            UplinkSpeedStatic uplinkSpeedStatic = new UplinkSpeedStatic();
                            uplinkSpeedStatic.setEntityId(entityId);
                            uplinkSpeedStatic.setDt(dt);
                            uplinkSpeedStatic.setIfIndex(ifIndex);
                            uplinkSpeedStatic.setIfInOctets(uplinkSpeedPerf.getIfInOctets());
                            uplinkSpeedStatic.setIfOutOctets(uplinkSpeedPerf.getIfOutOctets());
                            //modify by loyal 修改为bit/s
                            uplinkSpeedStatic.setIfInOctetsRate(inOctets * 8 / (period / 1000));
                            uplinkSpeedStatic.setIfOutOctetsRate(outOctets * 8 / (period / 1000));
                            uplinkSpeedStatic.setIfOctetsRate(uplinkSpeedStatic.getIfInOctetsRate()
                                    + uplinkSpeedStatic.getIfOutOctetsRate());
                            IfUtilization ifUtilization = new IfUtilization();
                            ifUtilization.setEntityId(entityId);
                            ifUtilization.setIfIndex(ifIndex);
                            ifUtilization.setDt(dt);
                            ifUtilization.setIfUtilization((double) (inUtilization > outUtilization ? inUtilization
                                    : outUtilization));
                            cmtsPerfDao.recordIfSpeedStatic(uplinkSpeedStatic);
                            // 记录通道利用率
                            cmtsPerfDao.recordIfUtilization(ifUtilization);
                            // 更新通道的ifSpeed
                            cmtsPerfDao.updateIfSpeed(entityId, ifIndex, ifSpeed);
                            //信道速率性能数据 Send TO 性能处理中心
                            List<PerformanceData> pList = new ArrayList<PerformanceData>();
                            PerformanceData cmtsLinkInSpeedData = new PerformanceData(uplinkSpeedStatic.getEntityId(),
                                    CmtsUpLinkInSpeedHandle.CMTS_UPLINK_INSPEED_FLAG, uplinkSpeedStatic);
                            PerformanceData cmtsLinkOutSpeedData = new PerformanceData(uplinkSpeedStatic.getEntityId(),
                                    CmtsUpLinkOutSpeedHandle.CMTS_UPLINK_OUTSPEED_FLAG, uplinkSpeedStatic);
                            PerformanceData cmtsLinkUsedData = new PerformanceData(ifUtilization.getEntityId(),
                                    CmtsUpLinkUsedHandle.CMTS_UPLINK_UESD_FLAG, ifUtilization);
                            pList.add(cmtsLinkInSpeedData);
                            pList.add(cmtsLinkOutSpeedData);
                            pList.add(cmtsLinkUsedData);
                            getCallback(PerformanceCallback.class).sendPerfomaceResult(pList);

                            if (logger.isTraceEnabled()) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                logger.trace("entityId:" + entityId + ";ifIndex:" + ifIndex + ";ifOutOctets="
                                        + uplinkSpeedPerf.getIfOutOctets() + ";ifInOctets="
                                        + uplinkSpeedPerf.getIfInOctets() + ";ifOutSpeed=" + outOctets + ";ifInSpeed="
                                        + inOctets + ";ifOperStatus=" + uplinkSpeedPerf.getIfOperStatus() + ";dt="
                                        + sdf.format(new Date(uplinkSpeedStaticPerfResult.getDt())));
                            }

                            //
                        }
                    }
                }
            }
        }
    }
}