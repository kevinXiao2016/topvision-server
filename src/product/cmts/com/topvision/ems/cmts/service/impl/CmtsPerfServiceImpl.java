/***********************************************************************
 * $ CmtsPerfServiceImpl.java,v1.0 2013-8-17 15:45:23 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmts.dao.CmtsPerfDao;
import com.topvision.ems.cmts.domain.CmtsUpLinkPort;
import com.topvision.ems.cmts.performance.domain.CmtsFlowQualityPerf;
import com.topvision.ems.cmts.performance.domain.CmtsSignalQualityPerf;
import com.topvision.ems.cmts.performance.domain.CmtsSystemPerf;
import com.topvision.ems.cmts.performance.domain.UplinkSpeedStaticPerf;
import com.topvision.ems.cmts.service.CmtsChannelService;
import com.topvision.ems.cmts.service.CmtsPerfService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceConstants;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.performance.domain.OnlinePerf;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.domain.PerfTask;
import com.topvision.ems.performance.domain.PerfThresholdTemplate;
import com.topvision.ems.performance.service.DevicePerfTargetService;
import com.topvision.ems.performance.service.PerfTargetData;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.exception.service.MonitorServiceException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author jay
 * @created @2013-8-17-15:45:23
 */
@Service("cmtsPerfService")
public class CmtsPerfServiceImpl extends CmcBaseCommonService implements CmtsPerfService, PerfTargetData {
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "performanceService")
    private PerformanceService<OperClass> performanceService;
    @Resource(name = "messageService")
    private MessageService messageService;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;
    @Resource(name = "cmcPerfDao")
    private CmcPerfDao cmcPerfDao;
    @Resource(name = "cmtsPerfDao")
    private CmtsPerfDao cmtsPerfDao;
    @Resource(name = "cmtsChannelService")
    private CmtsChannelService cmtsChannelService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private CmcDownChannelService cmcDownChannelService;
    @Autowired
    private DevicePerfTargetService devicePerfTargetService;
    @Resource(name = "perfThresholdService")
    private PerfThresholdService perfThresholdService;
    @Autowired
    private CpeService cpeService;

    @Override
    @PostConstruct
    public void initialize() {

    }

    @Override
    public synchronized void startUplinkCmtsPerfMonitor(Long entityId, long perfCollectPeriod, SnmpParam snmpParam) {
        if (hasCmtsUplinkFlowMonitor(entityId)) {
            // throw new MonitorServiceException();
        } else {
            UplinkSpeedStaticPerf uplinkSpeedStaticPerf = new UplinkSpeedStaticPerf();
            if (snmpParam == null) {
                snmpParam = getSnmpParamByEntityId(entityId);
            }
            List<CmtsUpLinkPort> uplPorts = cmtsChannelService.getUpLinkPortList(entityId);
            List<Long> ifIndexs = new ArrayList<Long>();
            for (Port port : uplPorts) {
                ifIndexs.add((long) port.getIfIndex());
            }
            uplinkSpeedStaticPerf.setEntityId(snmpParam.getEntityId());
            uplinkSpeedStaticPerf.setIfIndex(ifIndexs);
            performanceService.startMonitor(snmpParam, uplinkSpeedStaticPerf, perfCollectPeriod, perfCollectPeriod,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }
    }

    @Override
    public void stopUplinkCmtsPerfMonitor(Long entityId, SnmpParam snmpParam) {
        if (!hasCmtsUplinkFlowMonitor(entityId)) {
            // 若不存在，抛出异常
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                snmpParam = getSnmpParamByEntityId(entityId);
            }
            // 监视器ID
            Long monitorId = cmcPerfDao.getMonitorId(entityId, PerfTargetConstants.UPLINK_FLOW_CATEGORY);
            // 停止监视器
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    @Override
    public synchronized void startSystemCmtsPerfMonitor(Long cmcId, Long perfCollectPeriod, SnmpParam snmpParam) {
        if (hasCmtsSystemMonitor(cmcId)) {
            // throw new MonitorServiceException();
        } else {
            CmtsSystemPerf systemPerf = new CmtsSystemPerf();
            CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
            Long entityId = cmcEntityRelation.getCmcEntityId();
            Entity entity = entityService.getEntity(entityId);
            if (snmpParam == null) {
                // cmcDao.getEntityIdByCmcId(cmcId);
                snmpParam = getSnmpParamByEntityId(entityId);
            }
            systemPerf.setTypeId(entity.getTypeId());
            systemPerf.setCmcId(cmcId);
            systemPerf.setEntityId(snmpParam.getEntityId());
            performanceService.startMonitor(snmpParam, systemPerf, perfCollectPeriod, perfCollectPeriod,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }
    }

    @Override
    public void stopSystemCmtsPerfMonitor(Long cmcId, SnmpParam snmpParam) {
        // 判断是否存在CPU、内存利用率性能采集监视器
        if (!hasCmtsSystemMonitor(cmcId)) {
            // 若不存在，抛出异常
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                snmpParam = getSnmpParamByEntityId(entityId);
            }
            // 监视器ID
            Long monitorId = cmcPerfDao.getMonitorId(cmcId, PerfTargetConstants.SYSUPTIME_CATEGORY);
            // 停止监视器
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    private boolean hasCmtsSystemMonitor(Long cmcId) {
        // 从数据库中取出monitorId
        Long monitorId = cmcPerfDao.getMonitorId(cmcId, PerfTargetConstants.SYSUPTIME_CATEGORY);
        // 根据monitorId的值判断是否存在Monitor
        return monitorId != null && monitorId > 0;
    }

    private boolean hasCmtsUplinkFlowMonitor(Long cmcId) {
        // 从数据库中取出monitorId
        Long monitorId = cmcPerfDao.getMonitorId(cmcId, PerfTargetConstants.UPLINK_FLOW_CATEGORY);
        // 根据monitorId的值判断是否存在Monitor
        return monitorId != null && monitorId > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmts.service.CmtsPerfService#queryCmtsSignalQualityPoints(java.lang.Long,
     * java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmtsSignalQualityPoints(Long entityId, String perfTarget, Long channelIndex,
            String startTime, String endTime) {
        List<Point> points = new ArrayList<Point>();
        if (PerfTargetConstants.CMC_SNR.equals(perfTarget)) {
            points = cmtsPerfDao.selectCmtsSnrPoints(entityId, channelIndex, startTime, endTime);
            for (Point p : points) {
                p.setY(p.getY() / 10);
            }
        } else if (PerfTargetConstants.CCER.equals(perfTarget)) {
            points = cmtsPerfDao.selectCmtsCcerPoints(entityId, channelIndex, startTime, endTime);
        } else if (PerfTargetConstants.UCER.equals(perfTarget)) {
            points = cmtsPerfDao.selectCmtsUcerPoints(entityId, channelIndex, startTime, endTime);
        }
        return points;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsPerfService#queryCmtsFlowPoints(java.lang.Long,
     * java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmtsFlowPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
            String endTime) {
        List<Point> points = new ArrayList<Point>();
        if (PerfTargetConstants.UPCHANNEL_SPEED.equals(perfTarget)
                || PerfTargetConstants.DOWNCHANNEL_SPEED.equals(perfTarget)) {
            points = cmtsPerfDao.selectCmtsChannelSpeedPoints(entityId, channelIndex, perfTarget, startTime, endTime);
            /*for (Point p : points) {
                Double data = Double.parseDouble(NumberUtils.TWODOT_FORMAT.format((p.getY() / NumberUtils.M10)));
                p.setY(data);
            }*/
        } else if (PerfTargetConstants.UPLINK_IN_FLOW.equals(perfTarget)
                || PerfTargetConstants.UPLINK_OUT_FLOW.equals(perfTarget)) {
            points = cmtsPerfDao.selectCmtsUpLinkSpeedPoints(entityId, channelIndex, perfTarget, startTime, endTime);
            /*for (Point p : points) {
                Double data = Double.parseDouble(NumberUtils.TWODOT_FORMAT.format((p.getY() / NumberUtils.M10)));
                p.setY(data);
            }*/
        } else if (PerfTargetConstants.UPLINK_USED.equals(perfTarget)
                || PerfTargetConstants.CHANNEL_USED.equals(perfTarget)) {
            points = cmtsPerfDao.selectCmtsPortUsedPoints(entityId, channelIndex, perfTarget, startTime, endTime);
        }
        return points;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public synchronized void startCmtsOnlineQuality(Long entityId, Long typeId, SnmpParam snmpParam) {
        try {
            if (hasCmtsMonitor(entityId, "onlinePerf")) {
                return;
            }
            List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                    PerfTargetConstants.CMC_DEVICESTATUS);
            for (DevicePerfTarget target : targetList) {
                if (devicePerfTargetService.isTargetDataExists(entityId, target.getPerfTargetName())) {
                    //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    continue;
                }
                if (PerfTargetConstants.CMC_ONLINESTATUS.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        OnlinePerf onlinePerf = new OnlinePerf();
                        onlinePerf.setEntityId(entityId);
                        onlinePerf.setIpAddress(entityService.getEntity(entityId).getIp());
                        long period = target.getGlobalInterval() * 60000;
                        try {
                            performanceService.startMonitor(snmpParam, onlinePerf, 0L, period,
                                    PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            // 开启性能采集出现异常的情况下,将指标状态置为关闭
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("startCmtsOnlineQuality[{}] failed: {}", entityId, exception);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsPerfService#stopCmtsOnlineQuality(java.lang.Long,
     * com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void stopCmtsOnlineQuality(Long entityId, SnmpParam snmpParam) {
        List<Integer> result = getCmtsMonitor(entityId, "onlinePerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsPerfService#hasCmtsMonitor(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public boolean hasCmtsMonitor(Long entityId, String category) {
        List<Integer> result = getCmtsMonitor(entityId, category);
        if (result == null || result.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 获得指定category的性能采集
     * 
     * @param cmcId
     * @param category
     * @return
     */
    private List<Integer> getCmtsMonitor(Long entityId, String category) {
        return cmtsPerfDao.getCmtsPerformanceMonitorId(entityId, category);
    }

    @Override
    public synchronized void startCmtsSignalQuality(Long entityId, SnmpParam snmpParam, Long typeId) {
        try {
            if (hasCmtsMonitor(entityId, "cmtsSignalQualityPerf")) {
                return;
            }
            CmtsSignalQualityPerf cmtsSignalQualityPerf = new CmtsSignalQualityPerf();
            cmtsSignalQualityPerf.setEntityId(entityId);
            // 修改为同步LIST
            CopyOnWriteArrayList<PerfTask> pTasks = new CopyOnWriteArrayList<PerfTask>();
            // 获取全局性能指标数据
            List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                    PerfTargetConstants.CMC_SIGNALQUALITY);
            // 获得CMC的上行信道
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmtsChannelService
                    .getUpChannelBaseShowInfoList(entityId);
            List<Long> channelIndexs = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo info : cmcUpChannelBaseShowInfoList) {
                channelIndexs.add(info.getChannelIndex());
            }
            Loop: for (DevicePerfTarget target : targetList) {
                if (devicePerfTargetService.isTargetDataExists(entityId, target.getPerfTargetName())) {
                    //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    continue;
                }
                if (PerfTargetConstants.CMC_SNR.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        CmtsSignalQualityPerf cmtsSignalQualityPerfCustom = new CmtsSignalQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.CMC_SIGNALQUALITY,
                                target.getGlobalInterval(), cmtsSignalQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                // 存在间隔相同的，采用相同的采集任务
                                CmtsSignalQualityPerf tmpPerf = (CmtsSignalQualityPerf) tmp.getOperClass();
                                tmpPerf.setIsNoise(true);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        cmtsSignalQualityPerfCustom.setIsNoise(true);
                        pTasks.add(task);
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                } else if (PerfTargetConstants.CMC_BER.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        CmtsSignalQualityPerf cmtsSignalQualityPerfCustom = new CmtsSignalQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.CMC_SIGNALQUALITY,
                                target.getGlobalInterval(), cmtsSignalQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                CmtsSignalQualityPerf tmpPerf = (CmtsSignalQualityPerf) tmp.getOperClass();
                                tmpPerf.setIsUnerror(true);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        cmtsSignalQualityPerfCustom.setIsUnerror(true);
                        pTasks.add(task);
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
            }
            for (PerfTask task : pTasks) {
                CmtsSignalQualityPerf custom = (CmtsSignalQualityPerf) task.getOperClass();
                custom.setEntityId(entityId);
                custom.setChannelIndexs(channelIndexs);
                int targetEnable = PerfTargetConstants.TARGET_ENABLE_ON;
                try {
                    performanceService.startMonitor(snmpParam, custom, task.getInterval() * 60000L,
                            task.getInterval() * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                            PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                } catch (Exception e) {
                    targetEnable = PerfTargetConstants.TARGET_ENABLE_OFF;
                    logger.error(e.getMessage());
                } finally {
                    // 插入性能指标数据
                    if (custom.getIsNoise()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_SNR, entityId, typeId,
                                targetEnable);
                    }
                    if (custom.getIsUnerror()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_BER, entityId, typeId,
                                targetEnable);
                    }
                }

            }
        } catch (Exception exception) {
            logger.error("startCmtsSignalQuality[{}] failed: {}", entityId, exception);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsPerfService#stopCmtsSignalQuality(java.lang.Long,
     * com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void stopCmtsSignalQuality(Long entityId, SnmpParam snmpParam) {
        List<Integer> result = getCmtsMonitor(entityId, "cmtsSignalQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public synchronized void startCmtsFlowQuality(Long entityId, SnmpParam snmpParam, Long typeId) {
        try {
            if (hasCmtsMonitor(entityId, "cmtsFlowQualityPerf")) {
                return;
            } else {
                CmtsFlowQualityPerf cmtsFlowQualityPerf = new CmtsFlowQualityPerf();
                cmtsFlowQualityPerf.setEntityId(entityId);
                CopyOnWriteArrayList<PerfTask> pTasks = new CopyOnWriteArrayList<PerfTask>();
                // 获取全局性能指标数据
                List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                        PerfTargetConstants.CMC_FLOW);
                List<Long> upLinkIndexs = new ArrayList<Long>();
                List<Long> channelIndexs = new ArrayList<Long>();
                // UpLink Channel
                List<CmtsUpLinkPort> uplPorts = cmtsChannelService.getUpLinkPortList(entityId);
                // 获得CMTS的上行信道
                List<CmcUpChannelBaseShowInfo> cmtsUpChannelBaseShowInfoList = cmcUpChannelService
                        .getUpChannelBaseShowInfoList(entityId);
                // 获得CMTS的下行信道
                List<CmcDownChannelBaseShowInfo> cmtsDownChannelBaseShowInfo = cmcDownChannelService
                        .getDownChannelBaseShowInfoList(entityId);
                for (Port port : uplPorts) {
                    upLinkIndexs.add((long) port.getIfIndex());
                }
                for (CmcUpChannelBaseShowInfo info : cmtsUpChannelBaseShowInfoList) {
                    channelIndexs.add(info.getChannelIndex());
                }
                for (CmcDownChannelBaseShowInfo info : cmtsDownChannelBaseShowInfo) {
                    channelIndexs.add(info.getChannelIndex());
                }
                Loop: for (DevicePerfTarget target : targetList) {
                    if (devicePerfTargetService.isTargetDataExists(entityId, target.getPerfTargetName())) {
                        //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                        continue;
                    }
                    if (PerfTargetConstants.CMC_UPLINKFLOW.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmtsFlowQualityPerf cmtsFlowQualityPerfCustom = new CmtsFlowQualityPerf();
                            PerfTask task = new PerfTask(entityId, PerfTargetConstants.CMC_FLOW,
                                    target.getGlobalInterval(), cmtsFlowQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmtsFlowQualityPerf tmpPerf = (CmtsFlowQualityPerf) tmp.getOperClass();
                                    tmpPerf.setUpLinkIndexs(upLinkIndexs);
                                    tmpPerf.setIsUpLinkFlow(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmtsFlowQualityPerfCustom.setUpLinkIndexs(upLinkIndexs);
                            cmtsFlowQualityPerfCustom.setIsUpLinkFlow(true);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    } else if (PerfTargetConstants.CMC_CHANNELSPEED.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmtsFlowQualityPerf cmtsFlowQualityPerfCustom = new CmtsFlowQualityPerf();
                            PerfTask task = new PerfTask(entityId, PerfTargetConstants.CMC_FLOW,
                                    target.getGlobalInterval(), cmtsFlowQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmtsFlowQualityPerf tmpPerf = (CmtsFlowQualityPerf) tmp.getOperClass();
                                    tmpPerf.setChannelIndexs(channelIndexs);
                                    tmpPerf.setIsChannelFlow(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmtsFlowQualityPerfCustom.setChannelIndexs(channelIndexs);
                            cmtsFlowQualityPerfCustom.setIsChannelFlow(true);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    }
                }
                for (PerfTask task : pTasks) {
                    CmtsFlowQualityPerf custom = (CmtsFlowQualityPerf) task.getOperClass();
                    custom.setEntityId(entityId);
                    custom.setTypeId(typeId);
                    int tagetEnableOnOrOff = PerfTargetConstants.TARGET_ENABLE_ON;
                    try {
                        performanceService.startMonitor(snmpParam, custom, task.getInterval() * 60000L,
                                task.getInterval() * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                    } catch (Exception e) {
                        tagetEnableOnOrOff = PerfTargetConstants.TARGET_ENABLE_OFF;
                        logger.error(e.getMessage());
                    } finally {
                        // 开启性能采集出现异常的情况下,将指标状态置为关闭
                        if (custom.getIsUpLinkFlow()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_UPLINKFLOW, entityId,
                                    typeId, tagetEnableOnOrOff);
                        }
                        if (custom.getIsChannelFlow()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_CHANNELSPEED, entityId,
                                    typeId, tagetEnableOnOrOff);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("startCmtsFlowQuality[{}] failed: {}", entityId, exception);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsPerfService#stopCmtsFlowQuality(java.lang.Long,
     * com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void stopCmtsFlowQuality(Long entityId, SnmpParam snmpParam) {
        List<Integer> result = getCmtsMonitor(entityId, "cmtsFlowQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    /**
     * 处理设备性能指标的插入 主要是处理与组相关采集任务开启的情况
     * 
     * @param targetList
     * @param targetName
     * @param entityId
     * @param typeId
     * @param targetEnable
     */
    private void handleDevicePerfTarget(List<DevicePerfTarget> targetList, String targetName, Long entityId,
            Long typeId, Integer targetEnable) {
        for (DevicePerfTarget target : targetList) {
            if (targetName.equals(target.getPerfTargetName())) {
                // 插入对应性能指标数据
                DevicePerfTarget deviceTarget = new DevicePerfTarget(entityId, targetName, target.getParentType(),
                        target.getEntityType(), typeId, target.getGlobalInterval(), targetEnable);
                devicePerfTargetService.addDevicePerfTarget(deviceTarget);
            }
        }
    }

    /**
     * 处理设备性能指标的插入 主要是处理单个性能指标插入的情况
     * 
     * @param target
     * @param entityId
     * @param typeId
     * @param targetEnable
     */
    private void handleSpecialTarget(DevicePerfTarget target, Long entityId, Long typeId, Integer targetEnable) {
        DevicePerfTarget deviceTarget = new DevicePerfTarget(entityId, target.getPerfTargetName(),
                target.getParentType(), target.getEntityType(), typeId, target.getGlobalInterval(), targetEnable);
        devicePerfTargetService.addDevicePerfTarget(deviceTarget);
    }

    @Override
    public Object perfTargetChangeData(Long entityId, String targetName) {
        return getModifyCmtsTargetIndexs(entityId, targetName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsPerfService#getModifyCmtsTargetIndexs(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public List<Long> getModifyCmtsTargetIndexs(Long entityId, String targetName) {
        List<Long> indexList = null;
        if (targetName.equals(PerfTargetConstants.CMC_UPLINKFLOW)) {
            // UpLink Channel
            List<CmtsUpLinkPort> uplPorts = cmtsChannelService.getUpLinkPortList(entityId);
            indexList = new ArrayList<Long>();
            for (Port port : uplPorts) {
                indexList.add((long) port.getIfIndex());
            }
        } else if (targetName.equals(PerfTargetConstants.CMC_CHANNELSPEED)) {
            // 获得CMTS的上行信道
            List<CmcUpChannelBaseShowInfo> cmtsUpChannelBaseShowInfoList = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(entityId);
            // 获得CMTS的下行信道
            List<CmcDownChannelBaseShowInfo> cmtsDownChannelBaseShowInfo = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(entityId);
            indexList = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo info : cmtsUpChannelBaseShowInfoList) {
                indexList.add(info.getChannelIndex());
            }
            for (CmcDownChannelBaseShowInfo info : cmtsDownChannelBaseShowInfo) {
                indexList.add(info.getChannelIndex());
            }
        } else if (targetName.equals(PerfTargetConstants.CMC_SNR) || targetName.equals(PerfTargetConstants.CMC_BER)) {
            // 获得CMTS的上行信道
            List<CmcUpChannelBaseShowInfo> cmtsUpChannelBaseShowInfoList = cmtsChannelService
                    .getUpChannelBaseShowInfoList(entityId);
            indexList = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo info : cmtsUpChannelBaseShowInfoList) {
                indexList.add(info.getChannelIndex());
            }
        }
        return indexList;
    }

    @Override
    public List<Point> queryCmtsRelayPerfPoints(Long entityId, String startTime, String endTime) {
        return cmtsPerfDao.queryCmtsRelayPerfPoints(entityId, startTime, endTime);
    }

    @Override
    public synchronized void startCmtsServiceQuality(Long entityId, SnmpParam snmpParam, long typeId) {
        try {
            if (this.hasCmtsMonitor(entityId, "cmtsSystemPerf")) {
                return;
            } else {
                // 修改为同步LIST
                CopyOnWriteArrayList<PerfTask> pTasks = new CopyOnWriteArrayList<PerfTask>();
                // 获取全局性能指标数据
                List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                        PerfTargetConstants.CMC_SERVICE);
                Loop: for (DevicePerfTarget target : targetList) {
                    if (PerfTargetConstants.CMC_CPUUSED.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmtsSystemPerf systemPerfCustom = new CmtsSystemPerf();
                            PerfTask task = new PerfTask(entityId, PerfTargetConstants.CMC_SERVICE,
                                    target.getGlobalInterval(), systemPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmtsSystemPerf tmpPerf = (CmtsSystemPerf) tmp.getOperClass();
                                    tmpPerf.setIsCpuPerf(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            systemPerfCustom.setIsCpuPerf(true);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    }
                    if (PerfTargetConstants.CMC_MEMUSED.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmtsSystemPerf systemPerfCustom = new CmtsSystemPerf();
                            PerfTask task = new PerfTask(entityId, PerfTargetConstants.CMC_SERVICE,
                                    target.getGlobalInterval(), systemPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmtsSystemPerf tmpPerf = (CmtsSystemPerf) tmp.getOperClass();
                                    tmpPerf.setIsMemPerf(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            systemPerfCustom.setIsMemPerf(true);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    }
                }
                // 采集任务调度
                for (PerfTask task : pTasks) {
                    CmtsSystemPerf custom = (CmtsSystemPerf) task.getOperClass();
                    custom.setEntityId(entityId);
                    custom.setTypeId(typeId);
                    custom.setCmcId(entityId);
                    int tagetEnable = PerfTargetConstants.TARGET_ENABLE_ON;
                    try {
                        performanceService.startMonitor(snmpParam, custom, task.getInterval() * 60000L,
                                task.getInterval() * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                    } catch (Exception e) {
                        tagetEnable = PerfTargetConstants.TARGET_ENABLE_OFF;
                        logger.error("Start CmtsSystemPerf Error: {}", e.getMessage());
                    } finally {
                        if (custom.getIsCpuPerf()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_CPUUSED, entityId, typeId,
                                    tagetEnable);
                        }
                        if (custom.getIsMemPerf()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_MEMUSED, entityId, typeId,
                                    tagetEnable);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("startCmtsServiceQuality[{}] failed: {}", entityId, exception);
        }
    }

    @Override
    public void stopCmtsServiceQuality(Long entityId, SnmpParam snmpParam) {
        List<Integer> result = getCmtsMonitor(entityId, "cmtsSystemPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public void startCmtsPerfCollect(Entity entity) {
        Long entityId = entity.getEntityId();
        Long typeId = entity.getTypeId();
        try {
            PerfGlobal global = perfThresholdService.getCmtsPerfGlobal();
            PerfThresholdTemplate template = perfThresholdService.getDefaultTemplateByEntityType(typeId);
            //判断当前是否存在设备与模板关联记录,如果存在，则不处理;如果不存在，刚根据全局的配置进行添加设备与阈值模板的关联关系。
            boolean isEntityApplyTemplate = perfThresholdService.isEntityApplyTemplate(entity.getEntityId());
            if (!isEntityApplyTemplate) {
                // 全局配置_是否关联默认阈值模板
                if (global.getIsRelationWithDefaultTemplate() == 1) {//关联默认阈值模板
                    perfThresholdService.applyEntityThresholdTemplate(entityId, template.getTemplateId(),
                            global.getIsPerfThreshold());
                } else {
                    perfThresholdService.applyEntityThresholdTemplate(entityId, EponConstants.TEMPLATE_ENTITY_UNLINK,
                            EponConstants.PERF_PTHRESHOLD_OFF);
                }
            }
            // 全局配置_是否启动性能采集,全局配置必须放在最后，保证上述两个全局配置生效
            if (global.getIsPerfOn() != 1) {
                return;
            }
            SnmpParam perfSnmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
            // 在线状态
            this.startCmtsOnlineQuality(entityId, typeId, perfSnmpParam);
            // 信号质量
            this.startCmtsSignalQuality(entityId, perfSnmpParam, typeId);
            // 信道速率
            this.startCmtsFlowQuality(entityId, perfSnmpParam, typeId);
            // cpu 内存
            this.startCmtsServiceQuality(entityId, perfSnmpParam, typeId);
            //CM采集
            CmCollectConfig cmCollectConfig = cpeService.getCmCollectConfig();
            boolean cmStatusMonitor = CmCollectConfig.START.intValue() == cmCollectConfig.getCmCollectStatus()
                    .intValue();
            if (cmStatusMonitor) {
                // 启动CM采集
                cmcPerfService.startCmStatusMonitor(entityId, cmCollectConfig.getCmCollectPeriod(), perfSnmpParam);
            }
        } catch (Exception e) {
            logger.error("Start Cmts[{}] perf collect failed: {}", entityId, e);
        }
    }

    @Override
    public List<Point> getCmtsServicePerfPoints(Long entityId, String perfTarget, String startTime, String endTime) {
        if (PerfTargetConstants.CMC_CPUUSED.equals(perfTarget)) {
            return cmtsPerfDao.queryCmtsCpuData(entityId, startTime, endTime);
        }
        if (PerfTargetConstants.CMC_MEMUSED.equals(perfTarget)) {
            return cmtsPerfDao.queryCmtsMemData(entityId, startTime, endTime);
        }
        return new ArrayList<Point>();
    }
}