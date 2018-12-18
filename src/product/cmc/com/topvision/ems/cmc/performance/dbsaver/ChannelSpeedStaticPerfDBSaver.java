/***********************************************************************
 * $Id: ChannelSpeedStaticPerfDBSaver.java,v1.0 2012-7-15 上午11:26:01 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

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

import com.topvision.ems.cmc.facade.domain.CmcDownPortMonitorDomain;
import com.topvision.ems.cmc.facade.domain.CmcPortMonitorDomain;
import com.topvision.ems.cmc.facade.domain.CmcUpPortMonitorDomain;
import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.ChannelSpeedStatic;
import com.topvision.ems.cmc.performance.domain.ChannelSpeedStaticPerfResult;
import com.topvision.ems.cmc.performance.domain.ChannelUtilization;
import com.topvision.ems.cmc.performance.handle.ChannelSpeedHandle;
import com.topvision.ems.cmc.performance.handle.ChannelUtilizationHandle;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author loyal
 * @created @2012-7-15-上午11:26:01
 * 
 */
@Engine("channelSpeedStaticPerfDBSaver")
public class ChannelSpeedStaticPerfDBSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<ChannelSpeedStaticPerfResult, OperClass> {
    private final Logger logger = LoggerFactory.getLogger(ChannelSpeedStaticPerfDBSaver.class);
    public static String CMTS_SPEED_FLAG = "CMTS_SPEED";
    public static String CMTS_UTILIZATION_FLAG = "CMTS_UTILIZATION";
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    private static Map<Long, ChannelSpeedStaticPerfResult> lastStaticMap = Collections
            .synchronizedMap(new HashMap<Long, ChannelSpeedStaticPerfResult>());

    @Override
    public void save(ChannelSpeedStaticPerfResult channelSpeedStaticPerfResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("ChannelCmNumPerfDBSaver identifyKey["
                    + channelSpeedStaticPerfResult.getDomain().getIdentifyKey() + "] exec start.");
        }
        CmcPerfDao cmcPerfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        //modify by loyal 上下行分开采集
        List<CmcUpPortMonitorDomain> cmcUpPortPerfs = channelSpeedStaticPerfResult.getCmcUpPortPerfs();
        List<CmcDownPortMonitorDomain> cmcDownPortPerfs = channelSpeedStaticPerfResult.getCmcDownPortPerfs();
        List<CmcPortMonitorDomain> cmcPortPerfs = new ArrayList<CmcPortMonitorDomain>();

        if (cmcUpPortPerfs != null) {
            for (CmcUpPortMonitorDomain cmcUpPortMonitorDomain : cmcUpPortPerfs) {
                CmcPortMonitorDomain cmcPortMonitorDomain = new CmcPortMonitorDomain();
                cmcPortMonitorDomain.setCmcId(cmcUpPortMonitorDomain.getCmcId());
                cmcPortMonitorDomain.setCmcPortId(cmcUpPortMonitorDomain.getCmcPortId());
                cmcPortMonitorDomain.setIfAdminStatus(cmcUpPortMonitorDomain.getIfAdminStatus());
                cmcPortMonitorDomain.setIfIndex(cmcUpPortMonitorDomain.getIfIndex());
                cmcPortMonitorDomain.setIfInOctets(cmcUpPortMonitorDomain.getIfInOctets());
                cmcPortMonitorDomain.setIfOutOctets(0l);
                cmcPortMonitorDomain.setIfOperStatus(cmcUpPortMonitorDomain.getIfOperStatus());
                cmcPortMonitorDomain.setIfSpeed(cmcUpPortMonitorDomain.getIfSpeed());
                cmcPortMonitorDomain.setIfType(cmcUpPortMonitorDomain.getIfType());
                cmcPortPerfs.add(cmcPortMonitorDomain);
            }
        }

        if (cmcDownPortPerfs != null) {
            for (CmcDownPortMonitorDomain cmcDownPortMonitorDomain : cmcDownPortPerfs) {
                CmcPortMonitorDomain cmcPortMonitorDomain = new CmcPortMonitorDomain();
                cmcPortMonitorDomain.setCmcId(cmcDownPortMonitorDomain.getCmcId());
                cmcPortMonitorDomain.setCmcPortId(cmcDownPortMonitorDomain.getCmcPortId());
                cmcPortMonitorDomain.setIfAdminStatus(cmcDownPortMonitorDomain.getIfAdminStatus());
                cmcPortMonitorDomain.setIfIndex(cmcDownPortMonitorDomain.getIfIndex());
                cmcPortMonitorDomain.setIfInOctets(0l);
                cmcPortMonitorDomain.setIfOutOctets(cmcDownPortMonitorDomain.getIfOutOctets());
                cmcPortMonitorDomain.setIfOperStatus(cmcDownPortMonitorDomain.getIfOperStatus());
                cmcPortMonitorDomain.setIfSpeed(cmcDownPortMonitorDomain.getIfSpeed());
                cmcPortMonitorDomain.setIfType(cmcDownPortMonitorDomain.getIfType());
                cmcPortPerfs.add(cmcPortMonitorDomain);
            }
        }
        channelSpeedStaticPerfResult.setCmcPortPerfs(cmcPortPerfs);

        long entityId = channelSpeedStaticPerfResult.getEntityId();
        Long cmcId = channelSpeedStaticPerfResult.getCmcId();
        //TODO Victor临时解决办法
        //Long type = entityDao.selectByPrimaryKey(cmcId).getTypeId();
        //boolean isCmts = entityTypeService.isCmts(type);
        boolean isCmts = getCallback(PerformanceCallback.class).isCmts(cmcId);
        if (cmcPortPerfs != null) {
            ChannelSpeedStaticPerfResult lastChannelSpeedStaticPerfResult = lastStaticMap.get(cmcId);
            lastStaticMap.put(cmcId, channelSpeedStaticPerfResult);
            if (lastChannelSpeedStaticPerfResult != null) {
                float period = channelSpeedStaticPerfResult.getDt() - lastChannelSpeedStaticPerfResult.getDt();
                if (period > 0) {
                    for (CmcPortMonitorDomain cmcPortPerf : cmcPortPerfs) {
                        long ifIndex = cmcPortPerf.getIfIndex();
                        // 更新通道的状态
                        cmcPerfDao.updateCcmtsPortStatus(cmcId, cmcPortPerf);
                        CmcPortMonitorDomain lastPortPerf = null;
                        for (CmcPortMonitorDomain portPerf : lastChannelSpeedStaticPerfResult.getCmcPortPerfs()) {
                            if (portPerf.getIfIndex().equals(ifIndex)) {
                                lastPortPerf = portPerf;
                            }
                        }
                        if (lastPortPerf != null) {

                            float inOctets = cmcPortPerf.getIfInOctets() - lastPortPerf.getIfInOctets();
                            float outOctets = cmcPortPerf.getIfOutOctets() - lastPortPerf.getIfOutOctets();
                            //采用iftype来判断上下行 所以屏蔽了
                            Long channelType = (cmcPortPerf.getIfType() != null && cmcPortPerf.getIfType() != 128L) ? 0L
                                    : 1L;
                            long ifSpeed = cmcPortPerf.getIfSpeed();
                            float utilization;
                            if (channelType.equals(0L)) {
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
                                //            （(Byte * 8)=bits      / 秒             ) / bps      * 100 = x%
                                utilization = (Math.abs(inOctets) * 8 / (period / 1000)) / ifSpeed * 100;
                            } else {
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
                                //            （(Byte * 8)=bits      / 秒             ) / bps      * 100 = x%
                                utilization = Math.abs(outOctets) * 8 / (period / 1000) / ifSpeed * 100;
                            }
                            Timestamp dt = new Timestamp(channelSpeedStaticPerfResult.getDt());
                            ChannelSpeedStatic channelSpeedStatic = new ChannelSpeedStatic();
                            channelSpeedStatic.setEntityId(entityId);
                            channelSpeedStatic.setCmcId(cmcId);
                            channelSpeedStatic.setDt(dt);
                            channelSpeedStatic.setChannelIndex(ifIndex);
                            channelSpeedStatic.setChannelInOctets(cmcPortPerf.getIfInOctets());
                            channelSpeedStatic.setChannelOutOctets(cmcPortPerf.getIfOutOctets());
                            channelSpeedStatic.setChannelType(channelType.intValue());
                            // 处理翻转后如果还小于0,则直接丢弃
                            if (inOctets >= 0) {
                                //jay 全部统一成保存bit/s
                                //                                        (Byte * 8)(bit) /(ms /1000)(s) = bps
                                channelSpeedStatic.setChannelInOctetsRate(inOctets * 8 / (period / 1000));
                            }
                            if (outOctets >= 0) {
                                //jay 全部统一成保存bit/s
                                //                                        (Byte * 8)(bit) /(ms /1000)(s) = bps
                                channelSpeedStatic.setChannelOutOctetsRate(outOctets * 8 / (period / 1000));
                            }
                            if (channelSpeedStatic.getChannelInOctetsRate()
                                    + channelSpeedStatic.getChannelOutOctetsRate() >= 0) {
                                channelSpeedStatic.setChannelOctetsRate(channelSpeedStatic.getChannelInOctetsRate()
                                        + channelSpeedStatic.getChannelOutOctetsRate());
                            }
                            ChannelUtilization channelUtilization = new ChannelUtilization();
                            channelUtilization.setEntityId(entityId);
                            channelUtilization.setCmcId(cmcId);
                            channelUtilization.setChannelIndex(ifIndex);
                            channelUtilization.setDt(dt);
                            channelUtilization.setChannelUtilization(CmcUtil.hundredPercentLimit((int) Math
                                    .rint(utilization)));
                            cmcPerfDao.recordChannelSpeedStatic(channelSpeedStatic);
                            // 信道速率性能数据 Send TO 性能处理中心
                            if (isCmts) {
                                PerformanceData speedData = new PerformanceData(channelSpeedStatic.getEntityId(),
                                        CMTS_SPEED_FLAG, channelSpeedStatic);
                                getCallback(PerformanceCallback.class).sendPerfomaceResult(speedData);
                            } else {
                                PerformanceData speedData = new PerformanceData(channelSpeedStatic.getEntityId(),
                                        ChannelSpeedHandle.SPEED_FLAG, channelSpeedStatic);
                                getCallback(PerformanceCallback.class).sendPerfomaceResult(speedData);
                            }
                            // 记录通道利用率
                            cmcPerfDao.recordChannelUtilization(channelUtilization);
                            // 信道利用率性能数据 Send TO 性能处理中心
                            if (isCmts) {
                                PerformanceData utilizationData = new PerformanceData(channelUtilization.getEntityId(),
                                        CMTS_UTILIZATION_FLAG, channelUtilization);
                                getCallback(PerformanceCallback.class).sendPerfomaceResult(utilizationData);
                            } else {
                                PerformanceData utilizationData = new PerformanceData(channelUtilization.getEntityId(),
                                        ChannelUtilizationHandle.UTILIZATION_FLAG, channelUtilization);
                                getCallback(PerformanceCallback.class).sendPerfomaceResult(utilizationData);
                            }
                            // 更新通道的ifSpeed
                            cmcPerfDao.updateIfSpeed(cmcId, ifIndex, ifSpeed);
                            if (logger.isTraceEnabled()) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                logger.trace("cmcId:" + cmcId + ";ifIndex:" + ifIndex + ";ifOutOctets="
                                        + cmcPortPerf.getIfOutOctets() + ";ifInOctets=" + cmcPortPerf.getIfInOctets()
                                        + ";ifOutSpeed=" + outOctets + ";ifInSpeed=" + inOctets + ";ifOperStatus="
                                        + cmcPortPerf.getIfOperStatus() + ";dt="
                                        + sdf.format(new Date(channelSpeedStaticPerfResult.getDt())));
                            }
                        }
                    }
                }
            }
            // 更新portperf表
            // 去掉cmportperf表的更新操作 这张表没有使用
            // cmcDiscoveryDao.batchInsertCmcPortPerfInfo(cmcPortPerfs,
            // channelSpeedStaticPerfResult.getEntityId());
        }

        // getCallback(PerformanceCallback.class).sendPerfomaceResult(performanceResult);
    }
}