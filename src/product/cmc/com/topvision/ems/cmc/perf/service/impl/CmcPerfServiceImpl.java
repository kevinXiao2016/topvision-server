/***********************************************************************
 * $Id: CmcPerfServiceImpl.java,v1.0 2012-5-8 上午10:53:54 $
 *
 * @author: loyal
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcChannelDao;
import com.topvision.ems.cmc.ccmts.domain.Cmc;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.domain.CmcChanelName;
import com.topvision.ems.cmc.domain.CmcCmNumStatic;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.domain.CpeCollectConfig;
import com.topvision.ems.cmc.domain.PortalChannelUtilizationShow;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.cmc.perf.domain.CmcOpticalTemp;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.perf.service.job.CmcPerfServiceMonitorJob;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.performance.domain.ChannelCmNumPerf;
import com.topvision.ems.cmc.performance.domain.ChannelSpeedStaticPerf;
import com.topvision.ems.cmc.performance.domain.ChannelUtilization;
import com.topvision.ems.cmc.performance.domain.CmFlapMonitor;
import com.topvision.ems.cmc.performance.domain.CmStatusPerf;
import com.topvision.ems.cmc.performance.domain.CmcDorLinePowerQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcDorOptTempQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcFlowQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.cmc.performance.domain.CmcLinkQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcOpticalReceiverPerf;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcTempQualityPerf;
import com.topvision.ems.cmc.performance.domain.CpeStatusPerf;
import com.topvision.ems.cmc.performance.domain.NoisePerf;
import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.cmc.performance.domain.SystemPerf;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.cmc.performance.domain.UsBitErrorRatePerf;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.sni.dao.CmcSniDao;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceConstants;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.dao.DevicePerfTargetDao;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.performance.domain.OnlinePerf;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.domain.PerfTask;
import com.topvision.ems.performance.domain.PerfThresholdTemplate;
import com.topvision.ems.performance.service.DevicePerfTargetService;
import com.topvision.ems.performance.service.PerfTargetData;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.MonitorServiceException;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SchedulerService;

/**
 * @author loyal
 * @created @2012-5-8-上午10:53:54
 * 
 * 
 * @modify by Rod 性能采集重构
 */
@Service("cmcPerfService")
public class CmcPerfServiceImpl extends CmcBaseCommonService implements CmcPerfService, PerfTargetData {
    @Resource(name = "performanceService")
    private PerformanceService<OperClass> performanceService;
    @Resource(name = "messageService")
    private MessageService messageService;
    @Resource(name = "performanceDao")
    private PerformanceDao performanceDao;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "cmcPerfDao")
    private CmcPerfDao cmcPerfDao;
    @Autowired
    private CmcChannelDao cmcChannelDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "perfTargetService")
    private PerfTargetService perfTargetService;
    @Autowired
    private DevicePerfTargetService devicePerfTargetService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    @Autowired
    private SchedulerService schedulerService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private CmcSniDao cmcSniDao;
    @Resource(name = "perfThresholdService")
    private PerfThresholdService perfThresholdService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private CpeService cpeService;

    @Autowired
    private DevicePerfTargetDao devicePerfTargetDao;

    // private Object mutex = new Object();

    @PostConstruct
    public void initialize() {

    }

    public void start() {
        try {
            JobDetail job = newJob(CmcPerfServiceMonitorJob.class).withIdentity("CmcPerfServiceMonitorJob", "Default")
                    .build();
            job.getJobDataMap().put("performanceDao", performanceDao);
            job.getJobDataMap().put("performanceService", performanceService);
            job.getJobDataMap().put("entityService", entityService);

            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(),
                    job.getKey().getGroup()).withSchedule(repeatSecondlyForever(60));
            schedulerService.scheduleJob(job, builder.build());
        } catch (SchedulerException se) {
            logger.error("", se);
        }

        //add by lzt 增加对光机类型设备的光机温度和电压的采集初始化处理
        List<CmcAttribute> cmcList = cmcService.getContainOptDorCmcList();
        for (CmcAttribute cmcAttribute : cmcList) {
            try {
                //判断是否添加过光机温度指标
                if (!devicePerfTargetService.isTargetDataExists(cmcAttribute.getCmcId(),
                        PerfTargetConstants.CMC_DOROPTTEMP)) {
                    DevicePerfTarget target = new DevicePerfTarget();
                    target.setEntityId(cmcAttribute.getCmcId());
                    target.setPerfTargetName(PerfTargetConstants.CMC_DOROPTTEMP);
                    target.setCollectInterval(15);
                    target.setTargetEnable(0);
                    target.setParentType(60000L);
                    target.setEntityType(30000L);
                    target.setTypeId(cmcAttribute.getCmcDeviceStyle());
                    devicePerfTargetDao.insertPerfTarget(target);
    }
            } catch (Exception e) {
                logger.info("cmc_dorOptTemp add error", e);
            }

            try {
                //判断是否添加过光机电压指标
                if (!devicePerfTargetService.isTargetDataExists(cmcAttribute.getCmcId(),
                        PerfTargetConstants.CMC_DORLINEPOWER)) {
                    DevicePerfTarget target = new DevicePerfTarget();
                    target.setEntityId(cmcAttribute.getCmcId());
                    target.setPerfTargetName(PerfTargetConstants.CMC_DORLINEPOWER);
                    target.setCollectInterval(15);
                    target.setTargetEnable(0);
                    target.setParentType(60000L);
                    target.setEntityType(30000L);
                    target.setTypeId(cmcAttribute.getCmcDeviceStyle());
                    devicePerfTargetDao.insertPerfTarget(target);
                }
            } catch (Exception e) {
                logger.info("cmc_dorLinePower add error", e);
            }
        }
    }

    public void stop() {

    }

    @Override
    public List<Cmc> getNetworkCcmtsDeviceLoadingTop(String item) {
        return cmcPerfDao.getNetworkCcmtsDeviceLoadingTop(item);
    }

    @Override
    public synchronized void startSNRMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (hasSNRMonitor(cmcId)) {
            // throw new MonitorExistException();
        } else {
            NoisePerf noisePerf = new NoisePerf();
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    Long lCmcType = cmcEntityRelation.getCmcType().longValue();
                    if (entityTypeService.isCcmtsWithAgent(lCmcType)) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(lCmcType)) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            //
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfo = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            List<Long> channelIndexs = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo aCmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfo) {
                channelIndexs.add(aCmcUpChannelBaseShowInfo.getChannelIndex());
            }
            if (snmpParam != null) {
                noisePerf.setEntityId(snmpParam.getEntityId());
            }
            noisePerf.setCmcId(cmcId);
            noisePerf.setIfIndexs(channelIndexs);
            performanceService.startMonitor(snmpParam, noisePerf, period, period, PerformanceConstants.PERFORMANCE_OID,
                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }
    }

    @Override
    public boolean hasSNRMonitor(Long cmcId) {
        Long monitorId = cmcPerfDao.getSNRMonitorId(cmcId);
        return monitorId != null && monitorId > 0;
    }

    @Override
    public void stopSNRMonitor(Long cmcId, SnmpParam snmpParam) {
        if (!hasSNRMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
            Long entityId;
            if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                Long lCmcType = cmcEntityRelation.getCmcType().longValue();
                if (entityTypeService.isCcmtsWithAgent(lCmcType)) {
                    snmpParam = getSnmpParamByCmcId(cmcId);
                } else if (entityTypeService.isCcmtsWithoutAgent(lCmcType)) {
                    snmpParam = getSnmpParamByEntityId(entityId);
                } else {
                    snmpParam = getSnmpParamByEntityId(entityId);
                }
            }
            Long monitorId = cmcPerfDao.getSNRMonitorId(cmcId);
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    @Override
    public void resetSNRMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (!hasSNRMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    Long lCmcType = cmcEntityRelation.getCmcType().longValue();
                    if (entityTypeService.isCcmtsWithAgent(lCmcType)) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(lCmcType)) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            Long monitorId = cmcPerfDao.getSNRMonitorId(cmcId);
            performanceService.reStartMonitor(snmpParam, monitorId, period);
        }
    }

    /**
     * @return the performanceService
     */
    @SuppressWarnings("rawtypes")
    public PerformanceService getPerformanceService() {
        return performanceService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * @return the cmcChannelDao
     */
    public CmcChannelDao getCmcChannelDao() {
        return cmcChannelDao;
    }

    /**
     * @param cmcChannelDao
     *            the cmcChannelDao to set
     */
    public void setCmcChannelDao(CmcChannelDao cmcChannelDao) {
        this.cmcChannelDao = cmcChannelDao;
    }

    /**
     * @return the cmcPerfDao
     */
    public CmcPerfDao getCmcPerfDao() {
        return cmcPerfDao;
    }

    /**
     * @param cmcPerfDao
     *            the cmcPerfDao to set
     */
    public void setCmcPerfDao(CmcPerfDao cmcPerfDao) {
        this.cmcPerfDao = cmcPerfDao;
    }

    @Override
    public List<Integer> getIfIndexByCmcId(Long cmcId, Integer type) {
        return cmcPerfDao.getIfIndexByCmcId(cmcId, type);
    }

    @Override
    public List<SingleNoise> getSnrData(Map<String, Object> map, Timestamp timeStart, Timestamp timeEnd) {
        return cmcPerfDao.getSnrData(map, timeStart, timeEnd);
    }

    @Override
    public List<SingleNoise> getSnrData(Map<String, Object> map, Integer size) {
        return cmcPerfDao.getSnrData(map, size);
    }

    @Override
    public Integer getSnrPeriod(Long cmcId) {
        return cmcPerfDao.getSnrPeriod(cmcId);
    }

    @Override
    public List<ChannelUtilization> getUtilizationData(Map<String, Object> map, Timestamp timeStart, Timestamp timeEnd) {
        return cmcPerfDao.getUtilizationData(map, timeStart, timeEnd);
    }

    @Override
    public List<ChannelUtilization> getUtilizationData(Map<String, Object> map, Integer size) {
        return cmcPerfDao.getUtilizationData(map, size);
    }

    @Override
    public List<PortalChannelUtilizationShow> getNetworkCcmtsDeviceLoadingTop(Map<String, Object> map) {
        return cmcPerfDao.getNetworkCcmtsDeviceLoadingTop(map);
    }

    @Override
    public boolean hasChannelCmNumMonitor(Long cmcId) {
        Long monitorId = cmcPerfDao.getChannelCmNumMonitorId(cmcId);
        return monitorId != null && monitorId > 0;
    }

    @Override
    public synchronized void startSystemPerfMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (hasCcmtsSystemMonitor(cmcId)) {
            // throw new MonitorServiceException();
        } else {
            SystemPerf systemPerf = new SystemPerf();
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
            List<Integer> ifIndexs = new ArrayList<Integer>();
            ifIndexs.add((int) cmcIndex);
            systemPerf.setCmcId(cmcId);
            systemPerf.setEntityId(snmpParam.getEntityId());
            systemPerf.setIfIndexs(ifIndexs);
            performanceService.startMonitor(snmpParam, systemPerf, period, period,
                    PerformanceConstants.PERFORMANCE_OID, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }

    }

    @Override
    public void resetCcmtsSystemMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        // 判断是否存在CPU、内存利用率性能采集监视器，
        if (!hasCcmtsSystemMonitor(cmcId)) {
            // 若不存在，抛出异常
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            // 监视器ID
            Long monitorId = cmcPerfDao.getMonitorId(cmcId, "CC_SYSTEM");
            // 重启监视器
            performanceService.reStartMonitor(snmpParam, monitorId, period);
        }
    }

    @Override
    public void stopCcmtsSystemMonitor(Long cmcId, SnmpParam snmpParam) {
        // 判断是否存在CPU、内存利用率性能采集监视器
        if (!hasCcmtsSystemMonitor(cmcId)) {
            // 若不存在，抛出异常
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            // 监视器ID
            Long monitorId = cmcPerfDao.getMonitorId(cmcId, "CC_SYSTEM");
            // 停止监视器
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    @Override
    public boolean hasCcmtsSystemMonitor(Long cmcId) {
        // 从数据库中取出monitorId
        Long monitorId = cmcPerfDao.getMonitorId(cmcId, "CC_SYSTEM");
        // 根据monitorId的值判断是否存在Monitor
        return monitorId != null && monitorId > 0;
    }

    @Override
    public Integer getCcmtsSystemPeriod(Long cmcId) {
        // 从数据库中取出监视器的周期
        return cmcPerfDao.getCcmtsSystemPeriod(cmcId, "CC_SYSTEM");
    }

    @Override
    public synchronized void startChannelCmMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (hasChannelCmNumMonitor(cmcId)) {
            // throw new MonitorExistException();
        } else {
            // TODO 暂未实现
            ChannelCmNumPerf channelCmNumPerf = new ChannelCmNumPerf();
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfo = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfo = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(cmcId);
            List<Long> channelIndexs = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo aCmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfo) {
                channelIndexs.add(aCmcUpChannelBaseShowInfo.getChannelIndex());
            }
            for (CmcDownChannelBaseShowInfo aCmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfo) {
                channelIndexs.add(aCmcDownChannelBaseShowInfo.getChannelIndex());
            }
            if (snmpParam != null) {
                channelCmNumPerf.setEntityId(snmpParam.getEntityId());
            }
            channelCmNumPerf.setCmcId(cmcId);
            channelCmNumPerf.setChannelIndex(channelIndexs);
            performanceService.startMonitor(snmpParam, channelCmNumPerf, period, period,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }
    }

    @Override
    public void resetChannelCmMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (!hasChannelCmNumMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            Long monitorId = cmcPerfDao.getChannelCmNumMonitorId(cmcId);
            performanceService.reStartMonitor(snmpParam, monitorId, period);
        }
    }

    @Override
    public void stopChannelCmMonitor(Long cmcId, SnmpParam snmpParam) {
        if (!hasChannelCmNumMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            Long monitorId = cmcPerfDao.getChannelCmNumMonitorId(cmcId);
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    @Override
    public Integer getChannelCmPeriod(Long cmcId) {
        return cmcPerfDao.getChannelCmPeriod(cmcId);
    }

    @Override
    public boolean hasUsBitErrorRateMonitor(Long cmcId) {
        Long monitorId = cmcPerfDao.getUsBitErrorRateMonitorId(cmcId);
        return monitorId != null && monitorId > 0;
    }

    @Override
    public synchronized void startUsBitErrorRateMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (hasUsBitErrorRateMonitor(cmcId)) {
            // throw new MonitorExistException();
        } else {
            UsBitErrorRatePerf usBitErrorRatePerf = new UsBitErrorRatePerf();
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfo = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            List<Long> channelIndexs = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo aCmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfo) {
                channelIndexs.add(aCmcUpChannelBaseShowInfo.getChannelIndex());
            }
            if (snmpParam != null) {
                usBitErrorRatePerf.setEntityId(snmpParam.getEntityId());
            }
            usBitErrorRatePerf.setCmcId(cmcId);
            usBitErrorRatePerf.setChannelIndex(channelIndexs);
            performanceService.startMonitor(snmpParam, usBitErrorRatePerf, period, period,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }
    }

    @Override
    public void resetUsBitErrorRateMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (!hasUsBitErrorRateMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            Long monitorId = cmcPerfDao.getUsBitErrorRateMonitorId(cmcId);
            performanceService.reStartMonitor(snmpParam, monitorId, period);
        }
    }

    @Override
    public void stopUsBitErrorRateMonitor(Long cmcId, SnmpParam snmpParam) {
        if (!hasUsBitErrorRateMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            Long monitorId = cmcPerfDao.getUsBitErrorRateMonitorId(cmcId);
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    @Override
    public Integer getUsBitErrorRatePeriod(Long cmcId) {
        return cmcPerfDao.getUsBitErrorRatePeriod(cmcId);
    }

    @Override
    public boolean hasChannelSpeedStaticMonitor(Long cmcId) {
        Long monitorId = cmcPerfDao.getChannelSpeedStaticMonitorId(cmcId);
        return monitorId != null && monitorId > 0;
    }

    @Override
    public synchronized void startChannelSpeedStaticMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (hasChannelSpeedStaticMonitor(cmcId)) {
            // throw new MonitorExistException();
        } else {
            ChannelSpeedStaticPerf channelSpeedStaticPerf = new ChannelSpeedStaticPerf();
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfo = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfo = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(cmcId);
            // add by loyal 上下行分别采用不同domain
            List<Long> channelIndexs = new ArrayList<Long>();
            List<Long> upChannelIndex = new ArrayList<Long>();
            List<Long> downChannelIndex = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo aCmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfo) {
                channelIndexs.add(aCmcUpChannelBaseShowInfo.getChannelIndex());
                upChannelIndex.add(aCmcUpChannelBaseShowInfo.getChannelIndex());
            }
            for (CmcDownChannelBaseShowInfo aCmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfo) {
                channelIndexs.add(aCmcDownChannelBaseShowInfo.getChannelIndex());
                downChannelIndex.add(aCmcDownChannelBaseShowInfo.getChannelIndex());
            }
            if (snmpParam != null) {
                channelSpeedStaticPerf.setEntityId(snmpParam.getEntityId());
            }
            channelSpeedStaticPerf.setCmcId(cmcId);
            channelSpeedStaticPerf.setChannelIndex(channelIndexs);
            channelSpeedStaticPerf.setUpChannelIndex(upChannelIndex);
            channelSpeedStaticPerf.setDownChannelIndex(downChannelIndex);
            performanceService.startMonitor(snmpParam, channelSpeedStaticPerf, period, period,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }
    }

    @Override
    public void resetChannelSpeedStaticMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (!hasChannelSpeedStaticMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            Long monitorId = cmcPerfDao.getChannelSpeedStaticMonitorId(cmcId);
            performanceService.reStartMonitor(snmpParam, monitorId, period);
        }
    }

    @Override
    public void stopChannelSpeedStaticMonitor(Long cmcId, SnmpParam snmpParam) {
        if (!hasChannelSpeedStaticMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    } else {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            Long monitorId = cmcPerfDao.getChannelSpeedStaticMonitorId(cmcId);
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    @Override
    public Integer getChannelSpeedStaticPeriod(Long cmcId) {
        return cmcPerfDao.getChannelSpeedStaticPeriod(cmcId);
    }

    @Override
    public boolean hasCmStatusMonitor(Long entityId) {
        Long monitorId = cmcPerfDao.getCmStatusMonitorId(entityId);
        return monitorId != null && monitorId > 0;
    }

    @Override
    public List<ChannelCmNum> getNetworkCcmtsDeviceUsersLoadingTop(Map<String, Object> map) {
        return cmcPerfDao.getNetworkCcmtsDeviceUsersLoadingTop(map);
    }

    @Override
    public List<ChannelCmNum> getCcmtsDeviceUsersList(Map<String, Object> map) {
        return cmcPerfDao.getCcmtsDeviceUsersList(map);
    }

    @Override
    public Integer getCcmtsDeviceUsersCount(Map<String, Object> map) {
        return cmcPerfDao.getCcmtsDeviceUsersCount(map);
    }

    @Override
    public List<CmcChanelName> getCmcChanelNames(Long cmcId) {
        List<CmcChanelName> re = new ArrayList<CmcChanelName>();
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(cmcId);
        for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
            CmcChanelName cmcChanelName = new CmcChanelName();
            cmcChanelName.setCmcId(cmcId);
            cmcChanelName.setChanelIndex(cmcDownChannelBaseShowInfo.getChannelIndex());
            cmcChanelName.setChanelName(makePortName(cmcDownChannelBaseShowInfo.getChannelIndex()));
            re.add(cmcChanelName);
        }
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(cmcId);
        for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfoList) {
            CmcChanelName cmcChanelName = new CmcChanelName();
            cmcChanelName.setCmcId(cmcId);
            cmcChanelName.setChanelIndex(cmcUpChannelBaseShowInfo.getChannelIndex());
            cmcChanelName.setChanelName(makePortName(cmcUpChannelBaseShowInfo.getChannelIndex()));
            re.add(cmcChanelName);
        }
        return re; // To change body of implemented methods use File | Settings
                   // | File Templates.
    }

    @Override
    public synchronized void startCmStatusMonitor(Long entityId, Long period, SnmpParam snmpParam) {
        try {
            if (hasCmStatusMonitor(entityId)) {
                Long monitorId = cmcPerfDao.getCmStatusMonitorId(entityId);
                snmpParam = getSnmpParamByEntityId(entityId);
                // performanceService.reStartMonitor(snmpParam, monitorId, period);
                performanceService.stopMonitor(snmpParam, monitorId);
            }
            CmStatusPerf cmStatusPerf = new CmStatusPerf();
            snmpParam = getSnmpParamByEntityId(entityId);
            cmStatusPerf.setEntityId(entityId);
            performanceService.startMonitor(snmpParam, cmStatusPerf, period, period,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        } catch (Exception exception) {
            logger.error("startCmStatusMonitor[{}] failed: {}", entityId, exception);
        }
    }

    @Override
    public List<UsBitErrorRate> getTopPortletErrorCodesLoading(Map<String, Object> paramMap) {
        return cmcPerfDao.getTopPortletErrorCodesLoading(paramMap);
    }

    @Override
    public List<UsBitErrorRate> getChannelBerRate(Map<String, Object> paramMap) {
        return cmcPerfDao.getChannelBerRate(paramMap);
    }

    @Override
    public Integer getChannelBerRateCount(Map<String, Object> paramMap) {
        return cmcPerfDao.getChannelBerRateCount(paramMap);
    }

    @Override
    public UsBitErrorRate getErrorCodesByPortId(Long cmcId, Long portId, String targetName) {
        Long cmcPortIndex = cmcChannelDao.getChannelIndexByPortId(portId);
        return cmcPerfDao.getErrorCodesByPortId(cmcId, cmcPortIndex, targetName);
    }

    @Override
    public List<SingleNoise> getTopLowNoiseLoading(Map<String, Object> paramMap) {
        return cmcPerfDao.getTopLowNoiseLoading(paramMap);
    }

    private String makePortName(Long channelIndex) {
        String type = CmcIndexUtils.getChannelType(channelIndex) == 0 ? "US" : "DS";
        Long slotNo = CmcIndexUtils.getSlotNo(channelIndex);
        Long ponNo = CmcIndexUtils.getPonNo(channelIndex);
        Long cmcNo = CmcIndexUtils.getCmcId(channelIndex);
        Long chNo = CmcIndexUtils.getChannelId(channelIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ").append(slotNo).append(Symbol.SLASH).append(ponNo).append(Symbol.SLASH)
                .append(cmcNo).append(Symbol.SLASH).append(chNo);
        return sb.toString();
    }

    @Override
    public CmcCmNumStatic getCmcCmNumStatic(Long cmcId) {
        return cmcPerfDao.getCmcCmNumStatic(cmcId);
    }

    @Override
    public boolean isExistCmc(Long cmcId) {
        return cmcPerfDao.isExistCmc(cmcId);
    }

    @Override
    public synchronized void startSystem8800BPerfMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        if (hasCcmtsSystemMonitor(cmcId)) {
            // throw new MonitorServiceException();
        } else {
            SystemPerf systemPerf = new SystemPerf();
            if (snmpParam == null) {
                CmcEntityRelation cmcEntityRelation = cmcDao.getCmcEntityRelationByCmcId(cmcId);
                if (cmcEntityRelation != null && cmcEntityRelation.getCmcType() != null) {
                    Long entityId = cmcEntityRelation.getCmcEntityId();// cmcDao.getEntityIdByCmcId(cmcId);
                    if (entityTypeService.isCcmtsWithAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByCmcId(cmcId);
                    } else if (entityTypeService.isCcmtsWithoutAgent(cmcEntityRelation.getCmcType())) {
                        snmpParam = getSnmpParamByEntityId(entityId);
                    }
                }
            }
            long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
            List<Integer> ifIndexs = new ArrayList<Integer>();
            ifIndexs.add((int) cmcIndex);
            systemPerf.setCmcId(cmcId);
            systemPerf.setEntityId(snmpParam.getEntityId());
            systemPerf.setIfIndexs(ifIndexs);
            performanceService.startMonitor(snmpParam, systemPerf, period, period,
                    PerformanceConstants.PERFORMANCE_OID, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }
    }

    @Override
    public void stopCmStatusMonitor(Long entityId, SnmpParam snmpParam) {
        if (!hasCmStatusMonitor(entityId)) {
            throw new MonitorServiceException();
        } else {
            Long monitorId = cmcPerfDao.getCmStatusMonitorId(entityId);
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    @Override
    public List<SingleNoise> getNoiseRate(String name, Long deviceType, String sort, String dir, int start, int limit) {
        return this.cmcPerfDao.selectNoiseRate(name, deviceType, sort, dir, start, limit);
    }

    @Override
    public Long getNoiseRateCount(String name, Long deviceType) {
        return this.cmcPerfDao.selectNoiseRateCount(name, deviceType);
    }

    @Override
    public List<PortalChannelUtilizationShow> getChannelUsed(String name, Long deviceType, String sort, String dir,
            int start, int limit) {
        return this.cmcPerfDao.selectChannelUsed(name, deviceType, sort, dir, start, limit);
    }

    @Override
    public Long getChannelUsedCount(String name, Long deviceType) {
        return this.cmcPerfDao.selectChannelUsedCount(name, deviceType);
    }

    @Override
    public synchronized void startCpeStatusMonitor(Long cmcId, long period, SnmpParam snmpParam) {
        try {
            if (hasCpeStatusMonitor(cmcId)) {
                Long monitorId = cmcPerfDao.getCpeStatusMonitorId(cmcId);
                snmpParam = getSnmpParamByEntityId(cmcId);
                // performanceService.reStartMonitor(snmpParam, monitorId, period);
                performanceService.stopMonitor(snmpParam, monitorId);
            }
            CpeStatusPerf cpeStatusPerf = new CpeStatusPerf();
            snmpParam = getSnmpParamByEntityId(cmcId);
            cpeStatusPerf.setEntityId(cmcId);
            performanceService.startMonitor(snmpParam, cpeStatusPerf, period, period,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        } catch (Exception e) {
            logger.error("Start Cmc[{}] CpeStatusMonitor failed:{}", cmcId, e);
        }
    }

    @Override
    public boolean hasCpeStatusMonitor(Long entityId) {
        Long monitorId = cmcPerfDao.getCpeStatusMonitorId(entityId);
        return monitorId != null && monitorId > 0;
    }

    @Override
    public void stopCpeStatusMonitor(Long cmcId, SnmpParam snmpParam) {
        if (!hasCpeStatusMonitor(cmcId)) {
            throw new MonitorServiceException();
        } else {
            Long monitorId = cmcPerfDao.getCpeStatusMonitorId(cmcId);
            performanceService.stopMonitor(snmpParam, monitorId);
        }
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public PerformanceDao getPerformanceDao() {
        return performanceDao;
    }

    public void setPerformanceDao(PerformanceDao performanceDao) {
        this.performanceDao = performanceDao;
    }

    @Override
    public synchronized void startCmcServiceQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "cmcServiceQualityPerf")) {
                return;
            } else {
                CmcServiceQualityPerf cmcServiceQualityPerf = new CmcServiceQualityPerf();
                cmcServiceQualityPerf.setCmcId(cmcId);
                cmcServiceQualityPerf.setCmcIndex(cmcIndex);
                // 修改为同步LIST
                CopyOnWriteArrayList<PerfTask> pTasks = new CopyOnWriteArrayList<PerfTask>();
                // 获取全局性能指标数据
                List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                        PerfTargetConstants.CMC_SERVICE);
                Loop: for (DevicePerfTarget target : targetList) {
                    if (devicePerfTargetService.isPerfTargetDisabled(cmcId, target.getPerfTargetName())) {
                        // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                        continue;
                    }
                    if (PerfTargetConstants.CMC_CPUUSED.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmcServiceQualityPerf cmcServiceQualityPerfCustom = new CmcServiceQualityPerf();
                            PerfTask task = new PerfTask(cmcId, PerfTargetConstants.CMC_SERVICE,
                                    target.getGlobalInterval(), cmcServiceQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmcServiceQualityPerf tmpPerf = (CmcServiceQualityPerf) tmp.getOperClass();
                                    tmpPerf.setIsCpuPerf(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmcServiceQualityPerfCustom.setIsCpuPerf(true);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    }
                    if (PerfTargetConstants.CMC_MEMUSED.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmcServiceQualityPerf cmcServiceQualityPerfCustom = new CmcServiceQualityPerf();
                            PerfTask task = new PerfTask(cmcId, PerfTargetConstants.CMC_SERVICE,
                                    target.getGlobalInterval(), cmcServiceQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmcServiceQualityPerf tmpPerf = (CmcServiceQualityPerf) tmp.getOperClass();
                                    tmpPerf.setIsMemPerf(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmcServiceQualityPerfCustom.setIsMemPerf(true);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    }
                    if (PerfTargetConstants.CMC_FLASHUSED.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmcServiceQualityPerf cmcServiceQualityPerfCustom = new CmcServiceQualityPerf();
                            PerfTask task = new PerfTask(cmcId, PerfTargetConstants.CMC_SERVICE,
                                    target.getGlobalInterval(), cmcServiceQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmcServiceQualityPerf tmpPerf = (CmcServiceQualityPerf) tmp.getOperClass();
                                    tmpPerf.setIsFlashPerf(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmcServiceQualityPerfCustom.setIsFlashPerf(true);
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
                    CmcServiceQualityPerf custom = (CmcServiceQualityPerf) task.getOperClass();
                    custom.setCmcId(cmcId);
                    custom.setCmcIndex(cmcIndex);
                    try {
                        performanceService.startMonitor(snmpParam, custom, task.getInterval() * 60000L,
                                task.getInterval() * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                        // 插入性能指标数据
                        if (custom.getIsCpuPerf()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_CPUUSED, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_ON);
                        }
                        if (custom.getIsMemPerf()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_MEMUSED, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_ON);
                        }
                        if (custom.getIsFlashPerf()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_FLASHUSED, entityId,
                                    typeId, PerfTargetConstants.TARGET_ENABLE_ON);
                        }
                    } catch (Exception e) {
                        // 采集任务开启失败的情况下,将性能指标置为关闭
                        if (custom.getIsCpuPerf()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_CPUUSED, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                        if (custom.getIsMemPerf()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_MEMUSED, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                        if (custom.getIsFlashPerf()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_FLASHUSED, entityId,
                                    typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                        logger.error(e.getMessage());
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("startCmcServiceQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public synchronized void startCmcLinkQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "cmcLinkQualityPerf")) {
                return;
            } else if (devicePerfTargetService.isPerfTargetDisabled(cmcId, PerfTargetConstants.CMC_OPTLINK)) {
                // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            } else {
                DevicePerfTarget target = devicePerfTargetService.getTargetByTypeIdAndName(typeId,
                        PerfTargetConstants.CMC_OPTLINK);
                if (target != null) {
                    if (target.getGlobalEnable() == 1) {
                        CmcLinkQualityPerf cmcLinkQualityPerf = new CmcLinkQualityPerf();
                        cmcLinkQualityPerf.setCmcId(cmcId);
                        cmcLinkQualityPerf.setCmcIndex(cmcIndex);
                        cmcLinkQualityPerf.setCcmtSwithoutAgent(false);
                        cmcLinkQualityPerf.setTypeId(typeId);
                        long collect_time = target.getGlobalInterval();
                        try {
                            performanceService.startMonitor(snmpParam, cmcLinkQualityPerf, collect_time * 60000,
                                    collect_time * 60000, PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 采集开启失败的情况下,将性能指标置为关闭
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
            logger.error("startCmcLinkQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public synchronized void startCmcLinkQualityFor8800A(Long cmcId, Long cmcIndex, Long onuIndex, SnmpParam snmpParam,
            Long entityId, Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "cmcLinkQualityPerf")) {
                return;
            } else if (devicePerfTargetService.isPerfTargetDisabled(cmcId, PerfTargetConstants.CMC_OPTLINK)) {
                // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            } else {
                DevicePerfTarget target = devicePerfTargetService.getTargetByTypeIdAndName(typeId,
                        PerfTargetConstants.CMC_OPTLINK);
                if (target != null) {
                    if (target.getGlobalEnable() == 1) {
                        CmcLinkQualityPerf cmcLinkQualityPerf = new CmcLinkQualityPerf();
                        cmcLinkQualityPerf.setCmcId(cmcId);
                        cmcLinkQualityPerf.setCmcIndex(cmcIndex);
                        cmcLinkQualityPerf.setOnuIndex(onuIndex);
                        cmcLinkQualityPerf.setCcmtSwithoutAgent(true);
                        cmcLinkQualityPerf.setTypeId(typeId);
                        long collect_time = target.getGlobalInterval();
                        try {
                            performanceService.startMonitor(snmpParam, cmcLinkQualityPerf, collect_time * 60000,
                                    collect_time * 60000, PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 开启性能采集出现异常的情况下,将指标状态置为关闭
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
            logger.error("startCmcLinkQualityFor8800A[{}] failed: {}", cmcId, exception);
        }
    }

    /**
     * @return the perfTargetService
     */
    public PerfTargetService getPerfTargetService() {
        return perfTargetService;
    }

    /**
     * @param perfTargetService
     *            the perfTargetService to set
     */
    public void setPerfTargetService(PerfTargetService perfTargetService) {
        this.perfTargetService = perfTargetService;
    }

    @Override
    public synchronized void startCmcSignalQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "cmcSignalQualityPerf")) {
                return;
            } else {
                CmcSignalQualityPerf cmcSignalQualityPerf = new CmcSignalQualityPerf();
                cmcSignalQualityPerf.setCmcId(cmcId);
                // 修改为同步LIST
                CopyOnWriteArrayList<PerfTask> pTasks = new CopyOnWriteArrayList<PerfTask>();
                // 获取全局性能指标数据
                List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                        PerfTargetConstants.CMC_SIGNALQUALITY);
                // 获得CMC的上行信道
                List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                        .getUpChannelBaseShowInfoList(cmcId);
                List<Long> cmcChannelIndexs = new ArrayList<Long>();
                for (CmcUpChannelBaseShowInfo info : cmcUpChannelBaseShowInfoList) {
                    cmcChannelIndexs.add(info.getChannelIndex());
                }
                Loop: for (DevicePerfTarget target : targetList) {
                    if (devicePerfTargetService.isPerfTargetDisabled(cmcId, target.getPerfTargetName())) {
                        // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                        continue;
                    }
                    if (PerfTargetConstants.CMC_SNR.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmcSignalQualityPerf cmcSignalQualityPerfCustom = new CmcSignalQualityPerf();
                            PerfTask task = new PerfTask(cmcId, PerfTargetConstants.CMC_SIGNALQUALITY,
                                    target.getGlobalInterval(), cmcSignalQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmcSignalQualityPerf tmpPerf = (CmcSignalQualityPerf) tmp.getOperClass();
                                    tmpPerf.setIsNoise(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmcSignalQualityPerfCustom.setIsNoise(true);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    } else if (PerfTargetConstants.CMC_BER.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmcSignalQualityPerf cmcSignalQualityPerfCustom = new CmcSignalQualityPerf();
                            PerfTask task = new PerfTask(cmcId, PerfTargetConstants.CMC_SIGNALQUALITY,
                                    target.getGlobalInterval(), cmcSignalQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    CmcSignalQualityPerf tmpPerf = (CmcSignalQualityPerf) tmp.getOperClass();
                                    tmpPerf.setIsUnerror(true);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmcSignalQualityPerfCustom.setIsUnerror(true);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    }
                }
                for (PerfTask task : pTasks) {
                    CmcSignalQualityPerf custom = (CmcSignalQualityPerf) task.getOperClass();
                    custom.setCmcId(cmcId);
                    custom.setCmcChannelIndexs(cmcChannelIndexs);
                    try {
                        performanceService.startMonitor(snmpParam, custom, task.getInterval() * 60000L,
                                task.getInterval() * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                        // 插入性能指标数据
                        if (custom.getIsNoise()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_SNR, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_ON);
                        }
                        if (custom.getIsUnerror()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_BER, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_ON);
                        }
                    } catch (Exception e) {
                        // 开启性能采集出现异常的情况下,将指标状态置为关闭
                        if (custom.getIsNoise()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_SNR, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                        if (custom.getIsUnerror()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_BER, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                        logger.error(e.getMessage());
                    }

                }
            }
        } catch (Exception exception) {
            logger.error("startCmcSignalQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public synchronized void startCmcFlowQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "cmcFlowQualityPerf")) {
                return;
            } else {
                CmcFlowQualityPerf cmcFlowQualityPerf = new CmcFlowQualityPerf();
                cmcFlowQualityPerf.setCmcId(cmcId);
                CopyOnWriteArrayList<PerfTask> pTasks = new CopyOnWriteArrayList<PerfTask>();
                // 获取全局性能指标数据
                List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                        PerfTargetConstants.CMC_FLOW);
                Long macDomainIndex = cmcDao.getCmcIndexByCmcId(cmcId);
                // Long upLinkIndex = 1L;
                // 获得CMC的上行信道
                List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                        .getUpChannelBaseShowInfoList(cmcId);
                // 获得CMC的下行信道
                List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfo = cmcDownChannelService
                        .getDownChannelBaseShowInfoList(cmcId);
                List<Long> cmcChannelIndexs = new ArrayList<Long>();
                for (CmcUpChannelBaseShowInfo info : cmcUpChannelBaseShowInfoList) {
                    cmcChannelIndexs.add(info.getChannelIndex());
                }
                for (CmcDownChannelBaseShowInfo info : cmcDownChannelBaseShowInfo) {
                    cmcChannelIndexs.add(info.getChannelIndex());
                }
                Loop: for (DevicePerfTarget target : targetList) {
                    if (devicePerfTargetService.isPerfTargetDisabled(cmcId, target.getPerfTargetName())) {
                        // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                        continue;
                    }
                    // 处理MAC域流量指标
                    if (PerfTargetConstants.CMC_MACFLOW.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmcFlowQualityPerf cmcFlowQualityPerfCustom = new CmcFlowQualityPerf();

                            PerfTask task = new PerfTask(cmcId, PerfTargetConstants.CMC_FLOW,
                                    target.getGlobalInterval(), cmcFlowQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmcFlowQualityPerf tmpPerf = (CmcFlowQualityPerf) tmp.getOperClass();
                                    tmpPerf.addIndex(macDomainIndex);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmcFlowQualityPerfCustom.addIndex(macDomainIndex);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    } else if (PerfTargetConstants.CMC_UPLINKFLOW.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            // 获取上联口的index
                            List<CmcPhyConfig> indexList = cmcSniDao.queryCmcPhyConfigList(cmcId);
                            // 自定义任务
                            CmcFlowQualityPerf cmcFlowQualityPerfCustom = new CmcFlowQualityPerf();
                            PerfTask task = new PerfTask(cmcId, PerfTargetConstants.CMC_FLOW,
                                    target.getGlobalInterval(), cmcFlowQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmcFlowQualityPerf tmpPerf = (CmcFlowQualityPerf) tmp.getOperClass();
                                    // tmpPerf.addIndex(upLinkIndex);
                                    for (CmcPhyConfig indexConfig : indexList) {
                                        tmpPerf.addIndex(indexConfig.getPhyIndex().longValue());
                                    }
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            // cmcFlowQualityPerfCustom.addIndex(upLinkIndex);
                            for (CmcPhyConfig indexConfig : indexList) {
                                cmcFlowQualityPerfCustom.addIndex(indexConfig.getPhyIndex().longValue());
                            }
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    } else if (PerfTargetConstants.CMC_CHANNELSPEED.equals(target.getPerfTargetName())) {
                        if (target.getGlobalEnable() == 1) {
                            CmcFlowQualityPerf cmcFlowQualityPerfCustom = new CmcFlowQualityPerf();
                            PerfTask task = new PerfTask(cmcId, PerfTargetConstants.CMC_FLOW,
                                    target.getGlobalInterval(), cmcFlowQualityPerfCustom);
                            for (PerfTask tmp : pTasks) {
                                if (tmp.equals(task)) {
                                    // 存在间隔相同的，采用相同的采集任务
                                    CmcFlowQualityPerf tmpPerf = (CmcFlowQualityPerf) tmp.getOperClass();
                                    tmpPerf.addIndex(cmcChannelIndexs);
                                    continue Loop;
                                }
                            }
                            // 不同时间间隔处理
                            cmcFlowQualityPerfCustom.addIndex(cmcChannelIndexs);
                            pTasks.add(task);
                        } else {
                            // 全局没有开启的情况下也需要插入
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        }
                    }
                }
                for (PerfTask task : pTasks) {
                    CmcFlowQualityPerf custom = (CmcFlowQualityPerf) task.getOperClass();
                    custom.setCmcId(cmcId);
                    try {
                        performanceService.startMonitor(snmpParam, custom, task.getInterval() * 60000L,
                                task.getInterval() * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                        // 插入性能指标数据
                        if (custom.getIsMacFlow()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_MACFLOW, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_ON);
                        }
                        if (custom.getIsUpLinkFlow()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_UPLINKFLOW, entityId,
                                    typeId, PerfTargetConstants.TARGET_ENABLE_ON);
                        }
                        if (custom.getIsChannelFlow()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_CHANNELSPEED, entityId,
                                    typeId, PerfTargetConstants.TARGET_ENABLE_ON);
                        }
                    } catch (Exception e) {
                        // 开启性能采集出现异常的情况下,将指标状态置为关闭
                        if (custom.getIsMacFlow()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_MACFLOW, entityId, typeId,
                                    PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                        if (custom.getIsUpLinkFlow()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_UPLINKFLOW, entityId,
                                    typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                        if (custom.getIsChannelFlow()) {
                            this.handleDevicePerfTarget(targetList, PerfTargetConstants.CMC_CHANNELSPEED, entityId,
                                    typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                        logger.error(e.getMessage());
                    }
                }
            }
        } catch (Exception exception) {
            logger.error("startCmcFlowQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public List<Long> getModifyCmcTargetIndexs(Long cmcId, String targetName) {
        // CC8800A与CC8800B的采集需要区分，获得Entity typeId进行判断
        Entity entity = entityDao.selectByPrimaryKey(cmcId);
        List<Long> indexList = null;
        // CMC需要处理流量质量和信号质量的INDEX
        if (targetName.equals(PerfTargetConstants.CMC_MACFLOW)) {
            Long macDomainIndex = cmcDao.getCmcIndexByCmcId(cmcId);
            indexList = new ArrayList<Long>();
            indexList.add(macDomainIndex);
        } else if (targetName.equals(PerfTargetConstants.CMC_CHANNELSPEED)) {
            // 获得CMC的上行信道
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            // 获得CMC的下行信道
            List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfo = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(cmcId);
            indexList = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo info : cmcUpChannelBaseShowInfoList) {
                indexList.add(info.getChannelIndex());
            }
            for (CmcDownChannelBaseShowInfo info : cmcDownChannelBaseShowInfo) {
                indexList.add(info.getChannelIndex());
            }
        } else if (targetName.equals(PerfTargetConstants.CMC_UPLINKFLOW)) {
            indexList = new ArrayList<Long>();
            // indexList.add(1L);
            // 获取上联口的index
            List<CmcPhyConfig> upLinkIndex = cmcSniDao.queryCmcPhyConfigList(cmcId);
            for (CmcPhyConfig config : upLinkIndex) {
                indexList.add(config.getPhyIndex().longValue());
            }
        } else if (targetName.equals(PerfTargetConstants.CMC_SNR) || targetName.equals(PerfTargetConstants.CMC_BER)) {
            // 获得CMC的上行信道
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(cmcId);
            indexList = new ArrayList<Long>();
            for (CmcUpChannelBaseShowInfo info : cmcUpChannelBaseShowInfoList) {
                indexList.add(info.getChannelIndex());
            }
        } else if (targetName.equals(PerfTargetConstants.CMC_CPUUSED)
                || targetName.equals(PerfTargetConstants.CMC_FLASHUSED)
                || targetName.equals(PerfTargetConstants.CMC_MEMUSED)) {
            Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
            indexList = new ArrayList<Long>();
            indexList.add(cmcIndex);
        } else if (targetName.equals(PerfTargetConstants.CMC_OPTLINK)) {
            Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
            Long onuIndex = cmcDao.getOnuIndexByCmcId(cmcId);
            indexList = new ArrayList<Long>();
            indexList.add(onuIndex);
            indexList.add(cmcIndex);
            if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
                indexList.add(1L);
            } else {
                indexList.add(0L);
            }
            // indexList.add(entity.getTypeId());
        } else if (targetName.equals(PerfTargetConstants.CMC_OPTICALRECEIVER)) {
            Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
            indexList = new ArrayList<Long>();
            indexList.add(cmcIndex);
        } else if (targetName.equals(PerfTargetConstants.CMC_CMFLAP)) {
            Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
            indexList = new ArrayList<Long>();
            indexList.add(cmcIndex);
        } else if (targetName.equals(PerfTargetConstants.CMC_MODULETEMP)
                || PerfTargetConstants.CMC_DOROPTTEMP.equals(targetName)
                || PerfTargetConstants.CMC_DORLINEPOWER.equals(targetName)) {
            Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
            indexList = new ArrayList<Long>();
            indexList.add(cmcIndex);
        }
        return indexList;
    }

    @Override
    public void stopCmcServiceQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmcServiceQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public void stopCmcLinkQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmcLinkQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public void stopCmcSignalQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmcSignalQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public void stopCmcFlowQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmcFlowQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    /**
     * 获得指定category的性能采集(用于删除性能采集)
     * 
     * @param cmcId
     * @param category
     * @return
     */
    private List<Integer> getCmcMonitor(Long cmcId, String category) {
        return cmcPerfDao.getCmcPerformanceMonitorId(cmcId, category);
    }

    /**
     * 判断是否存在指定category的性能采集(用于开启性能采集)
     * 
     * @param cmcId
     * @param category
     * @return
     */
    @Override
    public boolean hasCmcMonitor(Long cmcId, String category) {
        List<Integer> result = getCmcMonitor(cmcId, category);
        if (result == null || result.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public synchronized void startCmcTempQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "cmcTempQualityPerf")) {
                return;
            } else if (devicePerfTargetService.isPerfTargetDisabled(cmcId, PerfTargetConstants.CMC_MODULETEMP)) {
                // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            } else {
                DevicePerfTarget target = devicePerfTargetService.getTargetByTypeIdAndName(typeId,
                        PerfTargetConstants.CMC_MODULETEMP);
                // 如果支持CMC_MODULETEMP
                if (target != null) {
                    if (target.getGlobalEnable() == 1) {
                        CmcTempQualityPerf cmcTempQualityPerf = new CmcTempQualityPerf();
                        cmcTempQualityPerf.setCmcId(cmcId);
                        cmcTempQualityPerf.setCmcIndex(cmcIndex);
                        cmcTempQualityPerf.setIsNecessary(true);
                        long collect_time = target.getGlobalInterval();
                        try {
                            performanceService.startMonitor(snmpParam, cmcTempQualityPerf, collect_time * 60000,
                                    collect_time * 60000, PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 开启性能采集出现异常的情况下,将指标状态置为关闭
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
            logger.error("startCmcTempQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public void stopCmcTempQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmcTempQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public synchronized void startOpticalReceiverMonitor(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            boolean opticalReceiverSupport = deviceVersionService.isFunctionSupported(cmcId, "opticalReceiverRead");
            boolean newOpticalReceiverSupport = deviceVersionService.isFunctionSupported(cmcId, "opticalReceiverNew");
            if (opticalReceiverSupport || newOpticalReceiverSupport) {
                if (hasCmcMonitor(cmcId, "cmcOpticalReceiverPerf")) {
                    return;
                } else if (devicePerfTargetService.isPerfTargetDisabled(cmcId, PerfTargetConstants.CMC_OPTICALRECEIVER)) {
                    // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    return;
                } else {
                    DevicePerfTarget target = devicePerfTargetService.getTargetByTypeIdAndName(typeId,
                            PerfTargetConstants.CMC_OPTICALRECEIVER);
                    if (target.getGlobalEnable() == 1) {
                        CmcOpticalReceiverPerf receiverPerf = new CmcOpticalReceiverPerf();
                        receiverPerf.setCmcId(cmcId);
                        receiverPerf.setEntityId(cmcId);
                        receiverPerf.setCmcIndex(cmcIndex);
                        long collect_time = target.getGlobalInterval();
                        try {
                            performanceService.startMonitor(snmpParam, receiverPerf, collect_time * 60000,
                                    collect_time * 60000, PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 开启性能采集出现异常的情况下,将指标状态置为关闭
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
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
            logger.error("startOpticalReceiverMonitor[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public void resetOpticalReceiverMonitor(Long cmcId, Long period, SnmpParam snmpParam) {
        // TODO
    }

    @Override
    public void stopOpticalReceiverMonitor(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmcOpticalReceiverPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public synchronized void startCCCmFlapQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            boolean cmFlapSupport = deviceVersionService.isFunctionSupported(cmcId, "flap");
            if (cmFlapSupport) {
                if (hasCmcMonitor(cmcId, "cmFlapMonitor")) {
                    return;
                } else if (devicePerfTargetService.isPerfTargetDisabled(cmcId, PerfTargetConstants.CMC_CMFLAP)) {
                    // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    return;
                } else {
                    DevicePerfTarget target = devicePerfTargetService.getGlobalTargetByType(
                            PerfTargetConstants.CMC_CMFLAP, entityTypeService.getCcmtsType());
                    if (target.getGlobalEnable() == 1) {
                        CmFlapMonitor cmflapMonitor = new CmFlapMonitor();
                        cmflapMonitor.setEntityId(cmcId);// B型设备
                        // cmflapMonitor.setEntityId(cycle.getEntityId());
                        // cmflapMonitor.setCmcIndex(cmcIndex);
                        cmflapMonitor.setIsInsGrow(true);
                        long collect_time = target.getGlobalInterval();
                        try {
                            performanceService.startMonitor(snmpParam, cmflapMonitor, collect_time * 60000,
                                    collect_time * 60000, PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 开启性能采集出现异常的情况下,将指标状态置为关闭
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
            logger.error("startCCCmFlapQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public void stopCCCmFlapQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmFlapMonitor");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public List<CmFlap> getTopPortletFlapInsGrowthLoading(Map<String, Object> paramMap) {
        return cmcPerfDao.getTopPortletFlapInsGrowthLoading(paramMap);
    }

    @Override
    public List<CmFlap> loadCmFlapIns(Map<String, Object> paramMap) {
        return cmcPerfDao.getCmFlapIns(paramMap);
    }

    @Override
    public Integer getCmFlapInsCount(Map<String, Object> paramMap) {
        return cmcPerfDao.getCmFlapInsCount(paramMap);
    }

    @Override
    public Object perfTargetChangeData(Long entityId, String targetName) {
        return getModifyCmcTargetIndexs(entityId, targetName);
    }

    @Override
    public synchronized void startCmcOnlineQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "onlinePerf")) {
                return;
            }
            List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                    PerfTargetConstants.CMC_DEVICESTATUS);
            for (DevicePerfTarget target : targetList) {
                if (devicePerfTargetService.isPerfTargetDisabled(cmcId, target.getPerfTargetName())) {
                    // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    continue;
                }
                if (PerfTargetConstants.CMC_ONLINESTATUS.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == 1) {
                        OnlinePerf onlinePerf = new OnlinePerf();
                        onlinePerf.setEntityId(cmcId);
                        onlinePerf.setIpAddress(entityService.getEntity(cmcId).getIp());
                        long period = target.getGlobalInterval() * 60000;
                        try {
                            performanceService.startMonitor(snmpParam, onlinePerf, 0L, period,
                                    PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 采集开启失败的情况下，将性能指标状态置为关闭
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
            logger.error("startCmcOnlineQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public void stopCmcOnlineQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "onlinePerf");
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
    public ChannelCmNum getChannelCmNum(Long cmcPortId) {
        return cmcPerfDao.getChannelCmNum(cmcPortId);
    }

    @Override
    public List<CmcLinkQualityData> getCmcOpticalInfo(Map<String, Object> paramMap) {
        return cmcPerfDao.queryCmcOpticalInfo(paramMap);
    }

    @Override
    public int getCmcOpticalNum(Map<String, Object> paramMap) {
        return cmcPerfDao.queryCmcOpticalNum(paramMap);
    }

    @Override
    public void startCmcPerfCollect(Entity entity) {
        Long entityId = entity.getEntityId();
        Long typeId = entity.getTypeId();
        try {
            Long cmcIndex = cmcDao.getCmcIndexByCmcId(entityId);
            SnmpParam param = null;
            if (entityTypeService.isCcmtsWithoutAgent(typeId)) {
                param = cmcService.getSnmpParamByEntityId(entity.getParentId());
            } else {
                param = cmcService.getSnmpParamByEntityId(entityId);
            }
            // 开启性能相关采集
            PerfGlobal global = perfThresholdService.getCmtsPerfGlobal();
            PerfThresholdTemplate template = perfThresholdService.getDefaultTemplateByEntityType(typeId);
            // 判断当前是否存在设备与模板关联记录,如果存在，则不处理;如果不存在，刚根据全局的配置进行添加设备与阈值模板的关联关系。
            boolean isEntityApplyTemplate = perfThresholdService.isEntityApplyTemplate(entityId);
            if (!isEntityApplyTemplate) {
                // 全局配置_是否关联默认阈值模板
                if (global.getIsRelationWithDefaultTemplate() == 1) {// 关联默认阈值模板
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
            // 开启服务质量性能采集
            this.startCmcServiceQuality(entityId, cmcIndex, param, entityId, typeId);
            // 开启信号质量性能采集
            this.startCmcSignalQuality(entityId, cmcIndex, param, entityId, typeId);
            // 开启信道流量性能采集
            this.startCmcFlowQuality(entityId, cmcIndex, param, entityId, typeId);
            // 模块温度性能采集(类A型中目前C-A支持)
            this.startCmcTempQuality(entityId, cmcIndex, param, entityId, typeId);
            // CC上的CM FLAP性能采集
            this.startCCCmFlapQuality(entityId, cmcIndex, param, entityId, typeId);
            // Added by huangdongsheng 增加光机接收功率新能采集，并进行版本控制
            this.startOpticalReceiverMonitor(entityId, cmcIndex, param, entityId, typeId);

            // Added by lzt 增加光机温度性能采集，并进行版本控制
            this.startCmcDorOptTempQuality(entityId, cmcIndex, param, entityId, typeId);
            // Added by lzt 增加光机电压能采集，并进行版本控制
            this.startCmcDorLinePowerQuality(entityId, cmcIndex, param, entityId, typeId);

            // 开启CM,CPE采集
            CmCollectConfig cmCollectConfig = cpeService.getCmCollectConfig();
            boolean cmStatusMonitor = CmCollectConfig.START.intValue() == cmCollectConfig.getCmCollectStatus()
                    .intValue();
            CpeCollectConfig cpeCollectConfig = cpeService.getCpeCollectConfig();
            boolean cpeStatusMonitor = CmCollectConfig.START.intValue() == cpeCollectConfig.getCpeCollectStatus()
                    .intValue();
            if (entityTypeService.isCcmtsWithoutAgent(typeId)) {
                if (cmStatusMonitor) {
                    // 启动CM采集
                    this.startCmStatusMonitor(entity.getParentId(), cmCollectConfig.getCmCollectPeriod(), param);
                }
                if (cpeStatusMonitor) {
                    // 启动CPE采集
                    this.startCpeStatusMonitor(entity.getParentId(), cpeCollectConfig.getCpeCollectPeriod(), param);
                }
                Long onuIndex = cmcDao.getOnuIndexByCmcId(entityId);
                // 开启链路质量性能采集
                this.startCmcLinkQualityFor8800A(entityId, cmcIndex, onuIndex, param, entityId, typeId);
            } else {
                if (cmStatusMonitor) {
                    // 启动CM采集
                    this.startCmStatusMonitor(entityId, cmCollectConfig.getCmCollectPeriod(), param);
                }
                if (cpeStatusMonitor) {
                    // 启动CPE采集
                    this.startCpeStatusMonitor(entityId, cpeCollectConfig.getCpeCollectPeriod(), param);
                }
                this.startCmcOnlineQuality(entityId, cmcIndex, param, entityId, typeId);
                // 链路质量性能采集
                this.startCmcLinkQuality(entityId, cmcIndex, param, entityId, typeId);
            }
        } catch (Exception e) {
            logger.error("Start Cmc[{}] perf collect failed: {}", entityId, e);
        }
    }

    @Override
    public void startCmcPerfMonitorForA(Long cmcId, Long cmcIndex, Long onuIndex, SnmpParam snmpParam, Long entityId,
            Long typeId) {
        try {
            PerfGlobal global = perfThresholdService.getCmtsPerfGlobal();
            PerfThresholdTemplate template = perfThresholdService.getDefaultTemplateByEntityType(typeId);
            // 判断当前是否存在设备与模板关联记录,如果存在，则不处理;如果不存在，刚根据全局的配置进行添加设备与阈值模板的关联关系。
            boolean isEntityApplyTemplate = perfThresholdService.isEntityApplyTemplate(cmcId);
            if (!isEntityApplyTemplate) {
                // 全局配置_是否关联默认阈值模板
                if (global.getIsRelationWithDefaultTemplate() == 1) {// 关联默认阈值模板
                    perfThresholdService.applyEntityThresholdTemplate(cmcId, template.getTemplateId(),
                            global.getIsPerfThreshold());
                } else {
                    perfThresholdService.applyEntityThresholdTemplate(cmcId, EponConstants.TEMPLATE_ENTITY_UNLINK,
                            EponConstants.PERF_PTHRESHOLD_OFF);
                }
            }
            // 全局配置_是否启动性能采集,全局配置必须放在最后，保证上述两个全局配置生效
            if (global.getIsPerfOn() != 1) {
                return;
            }
            startCmcServiceQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            startCmcLinkQualityFor8800A(cmcId, cmcIndex, onuIndex, snmpParam, cmcId, typeId);
            startCmcSignalQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            startCmcFlowQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            startCmcTempQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            startCmcDorOptTempQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            startCmcDorLinePowerQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            startCCCmFlapQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            boolean opticalReceiverSupport = deviceVersionService.isFunctionSupported(cmcId, "opticalReceiverRead");
            boolean newOpticalReceiverSupport = deviceVersionService.isFunctionSupported(cmcId, "opticalReceiverNew");
            if (opticalReceiverSupport || newOpticalReceiverSupport) {
                startOpticalReceiverMonitor(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            }
        } catch (Exception e) {
            logger.error("Start cmc[{}] perf collect failed: {}", cmcId, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.service.CmcPerfService#startCmCpePerfMonitor(java.lang.Long,
     * com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void startCmCpePerfMonitor(Long entityId, SnmpParam param) {
        CmCollectConfig cmCollectConfig = cpeService.getCmCollectConfig();
        boolean cmStatusMonitor = CmCollectConfig.START.intValue() == cmCollectConfig.getCmCollectStatus().intValue();
        if (cmStatusMonitor) {
            // 启动CM采集
            if (!hasCmStatusMonitor(entityId)) {
                startCmStatusMonitor(entityId, cmCollectConfig.getCmCollectPeriod(), param);
            }
        }
        CpeCollectConfig cpeCollectConfig = cpeService.getCpeCollectConfig();
        boolean cpeStatusMonitor = CmCollectConfig.START.intValue() == cpeCollectConfig.getCpeCollectStatus()
                .intValue();
        if (cpeStatusMonitor) {
            // 启动CPE采集
            if (!hasCpeStatusMonitor(entityId)) {
                startCpeStatusMonitor(entityId, cpeCollectConfig.getCpeCollectPeriod(), param);
            }
        }
    }

    @Override
    public void startCmcPerfMonitorForB(Long cmcId, Long cmcIndex, Long typeId, SnmpParam snmpParam) {
        try {
            // 开启性能相关采集
            PerfGlobal global = perfThresholdService.getCmtsPerfGlobal();
            PerfThresholdTemplate template = perfThresholdService.getDefaultTemplateByEntityType(typeId);
            // 判断当前是否存在设备与模板关联记录,如果存在，则不处理;如果不存在，刚根据全局的配置进行添加设备与阈值模板的关联关系。
            boolean isEntityApplyTemplate = perfThresholdService.isEntityApplyTemplate(cmcId);
            if (!isEntityApplyTemplate) {
                // 全局配置_是否关联默认阈值模板
                if (global.getIsRelationWithDefaultTemplate() == 1) {// 关联默认阈值模板
                    perfThresholdService.applyEntityThresholdTemplate(cmcId, template.getTemplateId(),
                            global.getIsPerfThreshold());
                } else {
                    perfThresholdService.applyEntityThresholdTemplate(cmcId, EponConstants.TEMPLATE_ENTITY_UNLINK,
                            EponConstants.PERF_PTHRESHOLD_OFF);
                }
            }
            // 全局配置_是否启动性能采集,全局配置必须放在最后，保证上述两个全局配置生效
            if (global.getIsPerfOn() != 1) {
                return;
            }
            startCmcOnlineQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // 服务质量性能采集
            startCmcServiceQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // 链路质量性能采集
            startCmcLinkQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // 信号质量性能采集
            startCmcSignalQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // 信道流量性能采集
            startCmcFlowQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // 模块温度性能采集
            startCmcTempQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // 光机温度性能采集
            startCmcDorOptTempQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // 光机电压性能采集
            startCmcDorLinePowerQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // CC上的CM FLAP性能采集
            startCCCmFlapQuality(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            // Added by huangdongsheng 增加光机接收功率新能采集，并进行版本控制
            boolean opticalReceiverSupport = deviceVersionService.isFunctionSupported(cmcId, "opticalReceiverRead");
            boolean newOpticalReceiverSupport = deviceVersionService.isFunctionSupported(cmcId, "opticalReceiverNew");
            if (opticalReceiverSupport || newOpticalReceiverSupport) {
                startOpticalReceiverMonitor(cmcId, cmcIndex, snmpParam, cmcId, typeId);
            }
        } catch (Exception e) {
            logger.error("Start cmc[{}] perf collect failed: {}", cmcId, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.service.CmcPerfService#stopCmcPerfMonitorForA(java.lang.Long,
     * com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void stopCmcPerfMonitorForA(Long cmcId, SnmpParam snmpParam) {
        if (!isExistCmc(cmcId)) {
            return;
        }
        // 关闭服务质量性能采集
        try {
            stopCmcServiceQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcServiceQuality", e);
        }
        // 关闭链路质量性能采集
        try {
            stopCmcLinkQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcLinkQuality", e);
        }
        // 关闭信号质量性能采集
        try {
            stopCmcSignalQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcSignalQuality", e);
        }
        // 关闭信道流量性能采集
        try {
            stopCmcFlowQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcFlowQuality", e);
        }
        // 关闭光机接收功率性能采集 Added by huangdongsheng
        try {
            stopOpticalReceiverMonitor(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopOpticalReceiverMonitor", e);
        }
        // 关闭CC上的CM FLAP性能采集
        try {
            stopCCCmFlapQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCCCmFlapQuality", e);
        }
        // 关闭CC的模块温度采集
        try {
            stopCmcTempQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcTempQuality", e);
        }
        // 关闭CC光机温度采集
        try {
            stopCmcDorOptTempQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcDorOptTempQuality", e);
    }
        // 关闭CC光机60V电压采集
        try {
            stopCmcDorLinePowerQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcDorLinePowerQuality", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.service.CmcPerfService#stopCmCpePerfMonitor(java.lang.Long,
     * com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void stopCmCpePerfMonitor(Long entityId, SnmpParam snmpParam) {
        try {
            stopCmStatusMonitor(entityId, snmpParam);
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            stopCpeStatusMonitor(entityId, snmpParam);
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.service.CmcPerfService#stopCmcPerfMonitorForB(java.lang.Long,
     * com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void stopCmcPerfMonitorForB(Long cmcId, SnmpParam snmpParam) {
        Long entityId = cmcId;
        try {
            stopCmStatusMonitor(entityId, snmpParam);
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            stopCpeStatusMonitor(entityId, snmpParam);
        } catch (Exception e) {
            logger.debug("", e);
        }
        // 关闭在线状态性能采集
        try {
            stopCmcOnlineQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.error("stopCmcOnlineQuality", e);
        }
        try {
            // 关闭CMC服务质量性能采集
            stopCmcServiceQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopSNRMonitor", e);
        }
        try {
            // 关闭链路质量性能采集
            stopCmcLinkQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCcmtsSystemMonitor", e);
        }
        try {
            // 关闭信号质量性能采集
            stopCmcSignalQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopChannelCmMonitor", e);
        }
        try {
            // 关闭信道流量性能采集
            stopCmcFlowQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopUsBitErrorRateMonitor", e);
        }
        try {
            // 关闭模块温度性能采集
            stopCmcTempQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopUsBitErrorRateMonitor", e);
        }

        try {
            // 关闭光机性能采集 Added by huangdongsheng 2013-12-17
            stopOpticalReceiverMonitor(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopOpticalReceiverMonitor", e);
        }
        try {
            // 关闭CC的CMFLAP性能采集
            stopCCCmFlapQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCCCmFlapQuality", e);
        }

        // 关闭CC光机温度采集
        try {
            stopCmcDorOptTempQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcDorOptTempQuality", e);
    }
        // 关闭CC光机60V电压采集
        try {
            stopCmcDorLinePowerQuality(cmcId, snmpParam);
        } catch (Exception e) {
            logger.debug("stopCmcDorLinePowerQuality", e);
        }
    }

    @Override
    public void changeCmStatusOffine(long cmcId) {
        cmcPerfDao.changeCmStatusOffine(cmcId);
    }

    @Override
    public void changeCmc8800BStatus(Long cmcId, Boolean status) {
        cmcPerfDao.changeCmc8800BStatus(cmcId, status);
    }

    @Override
    public void startCmcDorOptTempQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "cmcDorOptTempQualityPerf")) {
                return;
            } else if (devicePerfTargetService.isPerfTargetDisabled(cmcId, PerfTargetConstants.CMC_DOROPTTEMP)) {
                // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            } else {
                DevicePerfTarget target = devicePerfTargetService.getTargetByTypeIdAndName(typeId,
                        PerfTargetConstants.CMC_DOROPTTEMP);
                // 如果支持CMC_DOROPTTEMP
                if (target != null) {
                    if (target.getGlobalEnable() == 1) {
                        CmcDorOptTempQualityPerf cmcDorOptTempQualityPerf = new CmcDorOptTempQualityPerf();
                        cmcDorOptTempQualityPerf.setCmcId(cmcId);
                        cmcDorOptTempQualityPerf.setCmcIndex(cmcIndex);
                        cmcDorOptTempQualityPerf.setIsNecessary(true);
                        long collect_time = target.getGlobalInterval();
                        try {
                            performanceService.startMonitor(snmpParam, cmcDorOptTempQualityPerf, collect_time * 60000,
                                    collect_time * 60000, PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 开启性能采集出现异常的情况下,将指标状态置为关闭
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
            logger.error("startCmcDorOptTempQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public void stopCmcDorOptTempQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmcDorOptTempQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public void startCmcDorLinePowerQuality(Long cmcId, Long cmcIndex, SnmpParam snmpParam, Long entityId, Long typeId) {
        try {
            if (hasCmcMonitor(cmcId, "cmcDorLinePowerQualityPerf")) {
                return;
            } else if (devicePerfTargetService.isPerfTargetDisabled(cmcId, PerfTargetConstants.CMC_DORLINEPOWER)) {
                // 如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            } else {
                DevicePerfTarget target = devicePerfTargetService.getTargetByTypeIdAndName(typeId,
                        PerfTargetConstants.CMC_DORLINEPOWER);
                // 如果支持CMC_DOROPTTEMP
                if (target != null) {
                    if (target.getGlobalEnable() == 1) {
                        CmcDorLinePowerQualityPerf cmcDorLinePowerQualityPerf = new CmcDorLinePowerQualityPerf();
                        cmcDorLinePowerQualityPerf.setCmcId(cmcId);
                        cmcDorLinePowerQualityPerf.setCmcIndex(cmcIndex);
                        cmcDorLinePowerQualityPerf.setIsNecessary(true);
                        long collect_time = target.getGlobalInterval();
                        try {
                            performanceService.startMonitor(snmpParam, cmcDorLinePowerQualityPerf,
                                    collect_time * 60000, collect_time * 60000,
                                    PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, entityId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 开启性能采集出现异常的情况下,将指标状态置为关闭
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
            logger.error("startCmcDorLinePowerQuality[{}] failed: {}", cmcId, exception);
        }
    }

    @Override
    public void stopCmcDorLinePowerQuality(Long cmcId, SnmpParam snmpParam) {
        List<Integer> result = getCmcMonitor(cmcId, "cmcDorLinePowerQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, Long.valueOf(tmp));
            }
        }
    }

    @Override
    public List<CmcOpticalTemp> getCmcOpticalTempInfo(Map<String, Object> paramMap) {
        return cmcPerfDao.queryCmcOpticalTempInfo(paramMap);
    }

    @Override
    public int getCmcOpticalTempNum(Map<String, Object> paramMap) {
        return cmcPerfDao.queryCmcOpticalTempNum(paramMap);
    }

} 
