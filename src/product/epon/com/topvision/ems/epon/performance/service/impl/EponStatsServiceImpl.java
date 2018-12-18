/***********************************************************************
 * $Id: EponStatsService.java,v1.0 2013-10-25 下午3:46:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.service.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.performance.dao.OltPerfDao;
import com.topvision.ems.epon.performance.domain.EponFlowQualityPerf;
import com.topvision.ems.epon.performance.domain.EponLinkQualityPerf;
import com.topvision.ems.epon.performance.domain.EponServiceQualityPerf;
import com.topvision.ems.epon.performance.facade.OltPortFlowCallback;
import com.topvision.ems.epon.performance.service.EponStatsService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceConstants;
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
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.message.event.EntityTypeChangeEvent;
import com.topvision.platform.message.event.EntityTypeChangeListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午3:46:42
 * 
 */
@Service("eponStatsService")
public class EponStatsServiceImpl extends BaseService implements EponStatsService, EntityTypeChangeListener,
        PerfTargetData, OltPortFlowCallback {
    @Autowired
    private PerformanceService<OperClass> performanceService;
    @Autowired
    private DevicePerfTargetService devicePerfTargetService;
    @Autowired
    private PerfThresholdService perfThresholdService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltPerfDao oltPerfDao;
    @Autowired
    private MessageService messageService;
    @Value("${epon.eponPeriod}")
    private Long eponPeriod;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(EntityTypeChangeListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(EntityTypeChangeListener.class, this);
    }

    /**
     * @return the eponPeriod
     */
    public Long getEponPeriod() {
        return eponPeriod;
    }

    /**
     * @param eponPeriod
     *            the eponPeriod to set
     */
    public void setEponPeriod(Long eponPeriod) {
        this.eponPeriod = eponPeriod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#startEponServiceQuality(java.lang.Long)
     */
    @Override
    public synchronized void startEponServiceQuality(Long entityId, Long typeId) {
        try {
            if (hasEponMonitor(entityId, "eponServiceQualityPerf")) {
                return;
            }
            logger.info("Begin to startEponServiceQuality: " + entityId);
            EponServiceQualityPerf eponServiceQualityPerf = new EponServiceQualityPerf();
            eponServiceQualityPerf.setEntityId(entityId);
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            // 修改为同步LIST
            CopyOnWriteArrayList<PerfTask> pTasks = new CopyOnWriteArrayList<PerfTask>();
            // 获取对应的全局性能指标数据
            List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                    PerfTargetConstants.OLT_SERVICE);
            Loop: for (DevicePerfTarget target : targetList) {
                if (devicePerfTargetService.isTargetDataExists(entityId, target.getPerfTargetName())) {
                    //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    continue;
                }
                if (PerfTargetConstants.OLT_CPUUSED.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        EponServiceQualityPerf eponServiceQualityPerfCustom = new EponServiceQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.OLT_SERVICE,
                                target.getGlobalInterval(), eponServiceQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                // 存在间隔相同的，采用相同的采集任务
                                EponServiceQualityPerf tmpPerf = (EponServiceQualityPerf) tmp.getOperClass();
                                tmpPerf.setIsCpuPerf(true);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        eponServiceQualityPerfCustom.setIsCpuPerf(true);
                        pTasks.add(task);
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
                if (PerfTargetConstants.OLT_MEMUSED.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        EponServiceQualityPerf eponServiceQualityPerfCustom = new EponServiceQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.OLT_SERVICE,
                                target.getGlobalInterval(), eponServiceQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                // 存在间隔相同的，采用相同的采集任务
                                EponServiceQualityPerf tmpPerf = (EponServiceQualityPerf) tmp.getOperClass();
                                tmpPerf.setIsMemPerf(true);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        eponServiceQualityPerfCustom.setIsMemPerf(true);
                        pTasks.add(task);
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
                if (PerfTargetConstants.OLT_FLASHUSED.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        EponServiceQualityPerf eponServiceQualityPerfCustom = new EponServiceQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.OLT_SERVICE,
                                target.getGlobalInterval(), eponServiceQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                // 存在间隔相同的，采用相同的采集任务
                                EponServiceQualityPerf tmpPerf = (EponServiceQualityPerf) tmp.getOperClass();
                                tmpPerf.setIsFlashPerf(true);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        eponServiceQualityPerfCustom.setIsFlashPerf(true);
                        pTasks.add(task);
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
                if (PerfTargetConstants.OLT_BOARDTEMP.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        EponServiceQualityPerf eponServiceQualityPerfCustom = new EponServiceQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.OLT_SERVICE,
                                target.getGlobalInterval(), eponServiceQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                // 存在间隔相同的，采用相同的采集任务
                                EponServiceQualityPerf tmpPerf = (EponServiceQualityPerf) tmp.getOperClass();
                                tmpPerf.setIsBoardTemp(true);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        eponServiceQualityPerfCustom.setIsBoardTemp(true);
                        pTasks.add(task);
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
                if (PerfTargetConstants.OLT_FANSPEED.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        EponServiceQualityPerf eponServiceQualityPerfCustom = new EponServiceQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.OLT_SERVICE,
                                target.getGlobalInterval(), eponServiceQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                // 存在间隔相同的，采用相同的采集任务
                                EponServiceQualityPerf tmpPerf = (EponServiceQualityPerf) tmp.getOperClass();
                                tmpPerf.setIsFanSpeed(true);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        eponServiceQualityPerfCustom.setIsFanSpeed(true);
                        pTasks.add(task);
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
            }
            for (PerfTask task : pTasks) {
                EponServiceQualityPerf custom = (EponServiceQualityPerf) task.getOperClass();
                custom.setEntityId(entityId);
                int targetEnableOnOrOff = PerfTargetConstants.TARGET_ENABLE_ON;
                try {
                    performanceService.startMonitor(snmpParam, custom, task.getInterval() * 60000L,
                            task.getInterval() * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                            PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                } catch (Exception e) {
                    targetEnableOnOrOff = PerfTargetConstants.TARGET_ENABLE_OFF;
                    logger.error(e.getMessage());
                } finally {
                    if (custom.getIsCpuPerf()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_CPUUSED, entityId, typeId,
                                targetEnableOnOrOff);
                    }
                    if (custom.getIsMemPerf()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_MEMUSED, entityId, typeId,
                                targetEnableOnOrOff);
                    }
                    if (custom.getIsFlashPerf()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_FLASHUSED, entityId, typeId,
                                targetEnableOnOrOff);
                    }
                    if (custom.getIsFanSpeed()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_FANSPEED, entityId, typeId,
                                targetEnableOnOrOff);
                    }
                    if (custom.getIsBoardTemp()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_BOARDTEMP, entityId, typeId,
                                targetEnableOnOrOff);
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("startEponServiceQuality[{}] failed: {}", entityId, exception);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#stopEponServiceQuality(java.lang.Long)
     */
    @Override
    public void stopEponServiceQuality(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<Integer> result = oltPerfDao.getEponPerformanceMonitorId(entityId, "eponServiceQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#startEponLinkQuality(java.lang.Long)
     */
    @Override
    public synchronized void startEponLinkQuality(Long entityId, Long typeId) {
        try {
            if (hasEponMonitor(entityId, "eponLinkQualityPerf")) {
                return;
            }
            if (devicePerfTargetService.isTargetDataExists(entityId, PerfTargetConstants.OLT_OPTLINK)) {
                //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            }
            logger.info("Begin to startEponLinkQuality: " + entityId);
            DevicePerfTarget target = devicePerfTargetService.getTargetByTypeIdAndName(typeId,
                    PerfTargetConstants.OLT_OPTLINK);
            if (target.getGlobalEnable() == 1) {
                EponLinkQualityPerf eponLinkQualityPerf = new EponLinkQualityPerf();
                eponLinkQualityPerf.setEntityId(entityId);
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                long period = target.getGlobalInterval() * 60000;
                try {
                    performanceService.startMonitor(snmpParam, eponLinkQualityPerf, period, period,
                            PerformanceConstants.PERFORMANCE_DOMAIN,
                            PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                    // 插入对应性能指标数据
                    this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                } catch (Exception e) {
                    // 开启失败情况下,将性能指标状态设置为关闭
                    this.handleSpecialTarget(target, entityId, typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                    logger.error(e.getMessage());
                }

            } else {
                // 全局没有开启的情况下也需要插入
                // 插入对应性能指标数据
                this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
            }
        } catch (Exception exception) {
            logger.error("startEponLinkQuality[{}] failed: {}", entityId, exception);
        }
    }

    /*
     * 注意流量的处理方式与处理指标FLAG的方式有很大区别
     * 
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#startEponFlowQuality(java.lang.Long)
     */
    @Override
    public synchronized void startEponFlowQuality(Long entityId, Long typeId) {
        try {
            if (hasEponMonitor(entityId, "eponFlowQualityPerf")) {
                return;
            }
            logger.info("Begin to startEponFlowQuality: " + entityId);
            EponFlowQualityPerf eponFlowQualityPerf = new EponFlowQualityPerf();
            eponFlowQualityPerf.setEntityId(entityId);
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            // 修改为同步LIST
            CopyOnWriteArrayList<PerfTask> pTasks = new CopyOnWriteArrayList<PerfTask>();
            List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                    PerfTargetConstants.OLT_FLOW);
            Loop: for (DevicePerfTarget target : targetList) {
                if (devicePerfTargetService.isTargetDataExists(entityId, target.getPerfTargetName())) {
                    //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    continue;
                }
                // 处理SNI口流量指标
                if (PerfTargetConstants.OLT_SNIFLOW.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        List<Long> sniIndexList = oltPerfDao.getOltSniIndexList(entityId);
                        EponFlowQualityPerf eponFlowQualityPerfCustom = new EponFlowQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.OLT_FLOW,
                                target.getGlobalInterval(), eponFlowQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                // 存在间隔相同的，采用相同的采集任务
                                EponFlowQualityPerf tmpPerf = (EponFlowQualityPerf) tmp.getOperClass();
                                tmpPerf.setSniIndexList(sniIndexList);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        eponFlowQualityPerfCustom.setSniIndexList(sniIndexList);
                        pTasks.add(task);
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
                // 处理PON口流量指标
                if (PerfTargetConstants.OLT_PONFLOW.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        List<Long> ponIndexList = oltPerfDao.getOltPonIndexList(entityId);
                        EponFlowQualityPerf eponFlowQualityPerfCustom = new EponFlowQualityPerf();
                        PerfTask task = new PerfTask(entityId, PerfTargetConstants.OLT_FLOW,
                                target.getGlobalInterval(), eponFlowQualityPerfCustom);
                        for (PerfTask tmp : pTasks) {
                            if (tmp.equals(task)) {
                                // 存在间隔相同的，采用相同的采集任务
                                EponFlowQualityPerf tmpPerf = (EponFlowQualityPerf) tmp.getOperClass();
                                tmpPerf.setPonIndexList(ponIndexList);
                                continue Loop;
                            }
                        }
                        // 不同时间间隔处理
                        eponFlowQualityPerfCustom.setPonIndexList(ponIndexList);
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
                EponFlowQualityPerf custom = (EponFlowQualityPerf) task.getOperClass();
                custom.setEntityId(entityId);
                custom.setIsNecessary(true);
                try {
                    performanceService.startMonitor(snmpParam, custom, task.getInterval() * 60000L,
                            task.getInterval() * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                            PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                    if (custom.getSniIndexList() != null && !custom.getSniIndexList().isEmpty()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_SNIFLOW, entityId, typeId,
                                PerfTargetConstants.TARGET_ENABLE_ON);
                    } else {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_SNIFLOW, entityId, typeId,
                                PerfTargetConstants.TARGET_ENABLE_OFF);
                    }
                    if (custom.getPonIndexList() != null && !custom.getPonIndexList().isEmpty()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_PONFLOW, entityId, typeId,
                                PerfTargetConstants.TARGET_ENABLE_ON);
                    } else {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_PONFLOW, entityId, typeId,
                                PerfTargetConstants.TARGET_ENABLE_OFF);
                    }
                } catch (Exception e) {
                    // 在开启采集失败的情况下,将性能指标开启状态置为关闭
                    if (custom.getSniIndexList() != null && !custom.getSniIndexList().isEmpty()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_SNIFLOW, entityId, typeId,
                                PerfTargetConstants.TARGET_ENABLE_OFF);
                    }
                    if (custom.getPonIndexList() != null && !custom.getPonIndexList().isEmpty()) {
                        this.handleDevicePerfTarget(targetList, PerfTargetConstants.OLT_PONFLOW, entityId, typeId,
                                PerfTargetConstants.TARGET_ENABLE_OFF);
                    }
                    logger.error(e.getMessage());
                }

            }
        } catch (Exception exception) {
            logger.error("startEponFlowQuality[{}] failed: {}", entityId, exception);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#modifyEponTargetQuality(java.lang.Long,
     * java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public void modifyEponTargetQuality(Long entityId, String targetName, Integer lastInterval, Integer newInterval) {
        /*
         * SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId); String category =
         * PerfTargetConstants.getPerfTargetCategory(targetName, PerfTargetConstants.OLT);
         * List<Long> indexList = performanceService.updatePerformanceTask(snmpParam, category,
         * targetName, entityId, lastInterval, newInterval, indexList);
         */
    }

    /*
     * 主要用于开启性能采集时候判断是否存在
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#hasEponMonitor(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public boolean hasEponMonitor(Long entityId, String category) {
        List<Integer> result = oltPerfDao.getEponPerformanceMonitorId(entityId, category);
        if (result == null || result.size() == 0) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#stopEponLinkQuality(java.lang.Long)
     */
    @Override
    public void stopEponLinkQuality(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<Integer> result = oltPerfDao.getEponPerformanceMonitorId(entityId, "eponLinkQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#stopEponFlowQuality(java.lang.Long)
     */
    @Override
    public void stopEponFlowQuality(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<Integer> result = oltPerfDao.getEponPerformanceMonitorId(entityId, "eponFlowQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.EponStatsService#getModifyOltTargetIndexs(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public List<Long> getModifyOltTargetIndexs(Long entityId, String targetName) {
        List<Long> indexList = null;
        if (PerfTargetConstants.OLT_SNIFLOW.equals(targetName)) {
            indexList = oltPerfDao.getOltSniIndexList(entityId);
        } else if (PerfTargetConstants.OLT_PONFLOW.equals(targetName)) {
            indexList = oltPerfDao.getOltPonIndexList(entityId);
        } else if (PerfTargetConstants.ONUPON_FLOW.equals(targetName)) {
            indexList = oltPerfDao.getOltOnuPonIndexList(entityId);
        } else if (PerfTargetConstants.UNI_FLOW.equals(targetName)) {
            indexList = oltPerfDao.getOltUniIndexList(entityId);
        }
        return indexList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityTypeChangeListener#entityTypeChange(com.topvision
     * .platform.message.event.EntityTypeChangeEvent)
     */
    @Override
    public void entityTypeChange(EntityTypeChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity.getIp() == null || entity.getIp().equals("0.0.0.0")) {
            return;
        }
        if (entity != null && entityTypeService.isOlt(entity.getTypeId())) {
            Long entityId = entity.getEntityId();
            try {
                PerfGlobal global = perfThresholdService.getOltPerfGlobal();
                PerfThresholdTemplate template = perfThresholdService
                        .getDefaultTemplateByEntityType(entity.getTypeId());
                //判断当前是否存在设备与模板关联记录,如果存在，则不处理;如果不存在，刚根据全局的配置进行添加设备与阈值模板的关联关系。
                boolean isEntityApplyTemplate = perfThresholdService.isEntityApplyTemplate(entity.getEntityId());
                if (!isEntityApplyTemplate) {
                    // 全局配置_是否关联默认阈值模板
                    if (global.getIsRelationWithDefaultTemplate() == 1) {//关联默认阈值模板
                        perfThresholdService.applyEntityThresholdTemplate(entityId, template.getTemplateId(),
                                global.getIsPerfThreshold());
                    } else {
                        perfThresholdService.applyEntityThresholdTemplate(entityId,
                                EponConstants.TEMPLATE_ENTITY_UNLINK, EponConstants.PERF_PTHRESHOLD_OFF);
                    }
                }
                // 全局配置_是否启动性能采集,全局配置必须放在最后，保证上述两个全局配置生效
                if (global.getIsPerfOn() != 1) {
                    return;
                }
                // 开启设备板卡温度探测使能
                List<OltSlotAttribute> slotAttributes = oltSlotService.getOltSlotList(entity.getEntityId());
                try {
                    // 开启设备板卡温度探测使能
                    oltSlotService.updataEntitySlotTempDetectEnable(slotAttributes);
                } catch (Exception e) {
                    logger.info("open device port SlotTempDetectEnable error");
                }
                logger.info("Begin to start performance collection: " + entityId);
                startEponOnlineQuality(entityId, entity.getTypeId());
                // 服务质量处理
                startEponServiceQuality(entityId, entity.getTypeId());
                // 光链路质量处理
                startEponLinkQuality(entityId, entity.getTypeId());
                // 端口流量
                startEponFlowQuality(entityId, entity.getTypeId());
                logger.info("End to start performance collection: " + entityId);
            } catch (Exception exception) {
                logger.error("Start olt[{}] perf collect failed : {}", entityId, exception);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.service.PerfTargetData#perfTargetChangeData(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public Object perfTargetChangeData(Long entityId, String targetName) {
        return getModifyOltTargetIndexs(entityId, targetName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.performance.service.EponStatsService#startEponOnlineQuality(java.lang
     * .Long)
     */
    @Override
    public synchronized void startEponOnlineQuality(Long entityId, Long typeId) {
        try {
            if (hasEponMonitor(entityId, "onlinePerf")) {
                return;
            }
            logger.info("Begin to startEponOnlineQuality: " + entityId);
            List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                    PerfTargetConstants.OLT_DEVICESTATUS);
            for (DevicePerfTarget target : targetList) {
                if (devicePerfTargetService.isTargetDataExists(entityId, target.getPerfTargetName())) {
                    //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    continue;
                }
                if (PerfTargetConstants.OLT_ONLINESTATUS.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        OnlinePerf onlinePerf = new OnlinePerf();
                        onlinePerf.setEntityId(entityId);
                        onlinePerf.setIpAddress(entityService.getEntity(entityId).getIp());
                        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                        long period = target.getGlobalInterval() * 60000;
                        try {
                            performanceService.startMonitor(snmpParam, onlinePerf, 0L, period,
                                    PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 全局开启的情况下在性能采集开启成功后再插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 开启性能采集出现异常的情况下,将指标状态置为关闭
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                            logger.error(e.getMessage());
                        }
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("startEponOnlineQuality[{}] failed: {}", entityId, exception);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.performance.service.EponStatsService#stopEponOnlineQuality(java.lang
     * .Long)
     */
    @Override
    public void stopEponOnlineQuality(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<Integer> result = oltPerfDao.getEponPerformanceMonitorId(entityId, "onlinePerf");
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
    public List<Long> getOltSniIndexList(Long entityId) {
        return oltPerfDao.getOltSniIndexList(entityId);
    }

    @Override
    public List<Long> getOltPonIndexList(Long entityId) {
        return oltPerfDao.getOltPonIndexList(entityId);
    }

    @Override
    public void startOltPerfCollect(Entity entity) {
        Long entityId = entity.getEntityId();
        try {
            PerfGlobal global = perfThresholdService.getOltPerfGlobal();
            PerfThresholdTemplate template = perfThresholdService.getDefaultTemplateByEntityType(entity.getTypeId());
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
            // 开启设备板卡温度探测使能
            List<OltSlotAttribute> slotAttributes = oltSlotService.getOltSlotList(entity.getEntityId());
            try {
                // 开启设备板卡温度探测使能
                oltSlotService.updataEntitySlotTempDetectEnable(slotAttributes);
            } catch (Exception e) {
                logger.info("open device port SlotTempDetectEnable error");
            }
            logger.info("Begin to start performance collection: " + entityId);
            startEponOnlineQuality(entityId, entity.getTypeId());
            // 服务质量处理
            startEponServiceQuality(entityId, entity.getTypeId());
            // 光链路质量处理
            startEponLinkQuality(entityId, entity.getTypeId());
            // 端口流量
            startEponFlowQuality(entityId, entity.getTypeId());
            logger.info("End to start performance collection: " + entityId);
        } catch (Exception e) {
            logger.error("Start Olt[{}] perf collect failed: {}", entityId, e);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.performance.service.EponStatsService#stopOltPerfCollect(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void stopOltPerfCollect(Entity entity) {
        Long entityId = entity.getEntityId();
        if (entity != null && entityTypeService.isOlt(entity.getTypeId())) {
            try {
                // 关闭在线状态性能采集
                stopEponOnlineQuality(entityId);
            } catch (Exception e) {
                logger.error("stopEponOnlineQuality", e);
            }
            try {
                // 关闭服务质量性能采集
                stopEponServiceQuality(entityId);
            } catch (Exception e) {
                logger.error("stopEponServiceQuality", e);
            }
            try {
                // 关闭链路质量性能采集
                stopEponLinkQuality(entityId);
            } catch (Exception e) {
                logger.error("stopEponLinkQuality", e);
            }
            try {
                // 关闭流量性能采集
                stopEponFlowQuality(entityId);
            } catch (Exception e) {
                logger.error("stopEponFlowQuality", e);
            }
        }
    }

}
