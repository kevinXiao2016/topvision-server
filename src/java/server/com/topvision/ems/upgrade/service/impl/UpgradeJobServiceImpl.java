/***********************************************************************
 * $Id: UpgradeJobServiceImpl.java,v1.0 2014年9月23日 下午3:54:51 $
 *
 * @author: loyal
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.dao.UpgradeJobDao;
import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeGlobalParam;
import com.topvision.ems.upgrade.domain.UpgradeJobInfo;
import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.ems.upgrade.job.UpgradeJob;
import com.topvision.ems.upgrade.service.UpgradeCheckService;
import com.topvision.ems.upgrade.service.UpgradeJobService;
import com.topvision.ems.upgrade.service.UpgradeParamService;
import com.topvision.ems.upgrade.service.UpgradeRecordService;
import com.topvision.ems.upgrade.utils.UpgradeStatusConstants;
import com.topvision.ems.upgrade.worker.UpgradeThreadPoolManager;
import com.topvision.ems.upgrade.worker.UpgradeWorker;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.FtpConnectService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.TftpClientService;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:54:51
 */
@Service("upgradeJobService")
public class UpgradeJobServiceImpl extends BaseService implements UpgradeJobService, BeanFactoryAware {
    private Map<Long, JobDetail> jobs = new HashMap<Long, JobDetail>();
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private UpgradeJobDao upgradeJobDao;
    @Autowired
    private UpgradeParamService upgradeParamService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private UpgradeRecordService upgradeRecordService;
    @Value("${batchUpgrade.maxPollSize}")
    private Integer maxPollSize;

    private UpgradeThreadPoolManager upgradeThreadPoolManager;

    private Map<Long, List<Future<UpgradeEntity>>> jobFutrueMap = new HashMap<Long, List<Future<UpgradeEntity>>>();

    @PreDestroy
    public void destroy() {
        super.destroy();
        upgradeThreadPoolManager.destroy();
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        logger.debug("UpgradeJobServiceImpl initialize");
        upgradeThreadPoolManager = new UpgradeThreadPoolManager(maxPollSize);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addJob(UpgradeJobInfo jobInfo) {
        try {
            Long jobId = upgradeJobDao.insertJob(jobInfo);
            UpgradeGlobalParam upgradeGlobalParam = upgradeParamService.getUpgradeGlobalParam();
            Long repeatInterval = upgradeGlobalParam.getRetryInterval() / 1000;
            String entityIds = jobInfo.getEntityIds();
            List<UpgradeEntity> upgradeEntityList = new ArrayList<UpgradeEntity>();
            if (entityIds != null) {
                String[] entityIdsArrayString = entityIds.split("\\$");
                for (String anEntityIdsArrayString : entityIdsArrayString) {
                    UpgradeEntity upgradeEntity = new UpgradeEntity();
                    upgradeEntity.setEntityId(new Long(anEntityIdsArrayString));
                    Entity entity = entityService.getEntity(upgradeEntity.getEntityId());
                    SnmpParam snmpParam = entityService.getSnmpParamByEntity(upgradeEntity.getEntityId());
                    upgradeEntity.setJobId(jobId);
                    upgradeEntity.setTypeId(entity.getTypeId());
                    upgradeEntity.setIp(entity.getIp());
                    upgradeEntity.setParam(snmpParam);
                    upgradeEntity.setUpgradeVersion(jobInfo.getVersionName());
                    upgradeEntity.setRetryTimes(0L);
                    upgradeEntity.setRetry(true);
                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_NOSTART);
                    upgradeEntity.setStartTime(jobInfo.getStartTime());
                    upgradeEntityList.add(upgradeEntity);
                }
                upgradeJobDao.addJobEntity(upgradeEntityList);
            }

            TriggerBuilder<SimpleTrigger> builder;
            jobInfo.setJobId(jobId);
            JobDetail job = newJob((Class<Job>) Class.forName(jobInfo.getJobClass())).withIdentity(
                    jobInfo.getJobId().toString(), "Upgrade." + jobInfo.getName()).build();
            job.getJobDataMap().put("upgradeThreadPoolManager", upgradeThreadPoolManager);
            job.getJobDataMap().put("entityUpgradeMonitor", job);
            job.getJobDataMap().put("entityList", upgradeEntityList);
            job.getJobDataMap().put("jobInfo", jobInfo);
            job.getJobDataMap().put("putFuture", true);
            job.getJobDataMap().put("beanFactory", beanFactory);
            if (jobInfo.getType() > 0) {
                builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                        .startAt(jobInfo.getStartTime()).withSchedule(repeatSecondlyForever(repeatInterval.intValue()));
            } else {
                builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup()).startNow()
                        .withSchedule(repeatSecondlyForever(repeatInterval.intValue()));
            }

            schedulerService.scheduleJob(job, builder.build());
            jobs.put(jobInfo.getJobId(), job);
        } catch (ClassNotFoundException e) {
            logger.error("device monitor not found error:", e);
        } catch (SchedulerException e) {
            logger.error("add device monitor:", e);
        }
    }

    private void startJob(UpgradeJobInfo jobInfo) {
        try {
            UpgradeGlobalParam upgradeGlobalParam = upgradeParamService.getUpgradeGlobalParam();
            Long repeatInterval = upgradeGlobalParam.getRetryInterval() / 1000;
            String entityIds = jobInfo.getEntityIds();
            List<UpgradeEntity> upgradeEntityList = new ArrayList<UpgradeEntity>();
            if (entityIds != null) {
                String[] entityIdsArrayString = entityIds.split(",");
                for (String anEntityIdsArrayString : entityIdsArrayString) {
                    Long entityId = new Long(anEntityIdsArrayString.trim());
                    UpgradeEntity upgradeEntity = upgradeJobDao.selectUpgradeEntityByEntityId(entityId,
                            jobInfo.getJobId());
                    SnmpParam snmpParam = entityService.getSnmpParamByEntity(upgradeEntity.getEntityId());
                    upgradeEntity.setParam(snmpParam);
                    upgradeEntityList.add(upgradeEntity);
                }
            }

            TriggerBuilder<SimpleTrigger> builder;
            jobInfo.setJobId(jobInfo.getJobId());
            @SuppressWarnings("unchecked")
            JobDetail job = newJob((Class<Job>) Class.forName(jobInfo.getJobClass())).withIdentity(
                    jobInfo.getJobId().toString(), "Upgrade." + jobInfo.getName()).build();
            job.getJobDataMap().put("upgradeThreadPoolManager", upgradeThreadPoolManager);
            job.getJobDataMap().put("entityUpgradeMonitor", job);
            job.getJobDataMap().put("entityList", upgradeEntityList);
            job.getJobDataMap().put("jobInfo", jobInfo);
            job.getJobDataMap().put("putFuture", true);
            job.getJobDataMap().put("beanFactory", beanFactory);
            if (jobInfo.getType() > 0) {
                builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                        .startAt(jobInfo.getStartTime()).withSchedule(repeatSecondlyForever(repeatInterval.intValue()));
            } else {
                builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup()).startNow()
                        .withSchedule(repeatSecondlyForever(repeatInterval.intValue()));
            }

            schedulerService.scheduleJob(job, builder.build());
            jobs.put(jobInfo.getJobId(), job);
        } catch (ClassNotFoundException e) {
            logger.error("device monitor not found error:", e);
        } catch (SchedulerException e) {
            logger.error("add device monitor:", e);
        }
    }

    @Override
    public void deleteJob(Long jobId) throws SchedulerException {
        List<Future<UpgradeEntity>> futures = getFutureJob(jobId);
        if (futures != null) {
            for (Future<UpgradeEntity> future : futures) {
                future.cancel(false);
            }
        }
        upgradeJobDao.deleteJob(jobId);
        JobDetail job = jobs.get(jobId);
        if (job == null) {
            return;
        }
        schedulerService.deleteJob(job.getKey());
        jobs.remove(jobId);
    }

    @Override
    public void modifyJobInterval(UpgradeJobInfo jobInfo, Long interval) {
        interval = interval / 1000;
        JobDetail job = jobs.get(jobInfo.getJobId());
        try {
            Trigger trigger = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .startAt(jobInfo.getStartTime()).withSchedule(repeatSecondlyForever(interval.intValue())).build();
            schedulerService.modifySchedualJob(job, trigger);
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    public void start() {
        List<UpgradeJobInfo> jobInfos = getAllUpgradeJob();
        for (UpgradeJobInfo jobInfo : jobInfos) {
            startJob(jobInfo);
        }
    }

    @Override
    public List<UpgradeJobInfo> getAllUpgradeJob() {
        List<UpgradeJobInfo> upgradeJobInfos = upgradeJobDao.selectAllJob();
        for (UpgradeJobInfo upgradeJobInfo : upgradeJobInfos) {
            List<Long> upgradeEntityList = upgradeJobDao.selectUpgradeEntityIdList(upgradeJobInfo.getJobId());
            String ids = "";
            if (upgradeEntityList != null) {
                String tmpstring = upgradeEntityList.toString();
                ids = upgradeEntityList.toString().substring(1, tmpstring.length() - 1);
            }
            upgradeJobInfo.setEntityIds(ids);
        }
        return upgradeJobInfos;
    }

    @Override
    public List<Entity> getEntity(Map<String, Object> map, Long type) {
        if (type != null && type.equals(entityTypeService.getOltType())) {
            return upgradeJobDao.selectOltEntity(map);
        } else {
            return upgradeJobDao.selectCcmtsEntity(map);
        }

    }

    @Override
    public Long getEntityNum(Map<String, Object> map) {
        return upgradeJobDao.selectEntityNum(map);
    }

    @Override
    public List<UpgradeEntity> getUpgradeEntity(Map<String, Object> map) {
        return upgradeJobDao.selectUpgradeEntity(map);
    }

    @Override
    public Long getUpgradeEntityNum(Map<String, Object> map) {
        return upgradeJobDao.selectUpgradeEntityNum(map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addEntityToJob(String entityIds, Long jobId) {
        JobDetail job = jobs.get(jobId);
        UpgradeJobInfo upgradeJobInfo = upgradeJobDao.getJob(jobId);
        JobKey jobKey = job.getKey();
        List<Trigger> triggers;
        Date date = new Date();
        // 获取任务下次开始执行时间
        try {
            triggers = (List<Trigger>) schedulerService.getTriggersOfJob(jobKey);
            date = triggers.get(0).getNextFireTime();
        } catch (SchedulerException e) {
            logger.error("", e);
        }
        if (entityIds != null) {
            String[] entityIdsArrayString = entityIds.split("\\$");
            List<UpgradeEntity> addEntityList = new ArrayList<UpgradeEntity>();
            List<UpgradeEntity> upgradeEntityList = (List<UpgradeEntity>) job.getJobDataMap().get("entityList");
            List<Long> oldEntityId = new ArrayList<>();
            for (UpgradeEntity upgradeEntity : upgradeEntityList) {
                logger.debug("upgradeEntityList.upgradeEntity getEntityId:" + upgradeEntity.getEntityId()
                        + " getRetry:" + upgradeEntity.getRetry());
                oldEntityId.add(upgradeEntity.getEntityId());
            }
            for (String anEntityIdsArrayString : entityIdsArrayString) {
                long entityId = 0;
                try {
                    entityId = Long.parseLong(anEntityIdsArrayString);
                } catch (NumberFormatException e) {
                    logger.error("", e);
                    continue;
                }
                if (oldEntityId.contains(entityId)) {
                    logger.debug("skip entityId:" + entityId);
                    continue;
                }
                UpgradeEntity upgradeEntity = upgradeJobDao.selectUpgradeEntityByEntityId(entityId, jobId);
                if (upgradeEntity == null) {
                    upgradeEntity = new UpgradeEntity();
                    upgradeEntity.setEntityId(entityId);
                    Entity entity = entityService.getEntity(upgradeEntity.getEntityId());
                    upgradeEntity.setJobId(jobId);
                    upgradeEntity.setTypeId(entity.getTypeId());
                    upgradeEntity.setIp(entity.getIp());
                    upgradeEntity.setUpgradeVersion(upgradeJobInfo.getVersionName());
                    upgradeEntity.setRetryTimes(0L);
                    upgradeEntity.setRetry(true);
                    upgradeEntity.setStartTime(new Timestamp(date.getTime()));
                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_NOSTART);
                }
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(upgradeEntity.getEntityId());
                upgradeEntity.setParam(snmpParam);
                upgradeEntityList.add(upgradeEntity);
                addEntityList.add(upgradeEntity);
            }
            job.getJobDataMap().put("entityList", upgradeEntityList);
            upgradeJobDao.addJobEntity(addEntityList);
            // 有新增设备 需要设置putFuture为true
            job.getJobDataMap().put("putFuture", true);
            try {
                schedulerService.modifySchedualJob(job, schedulerService.getTrigger(new TriggerKey(job.getKey().getName(), job.getKey().getGroup())));
            } catch (SchedulerException e) {
                logger.debug("", e);
            }
        }
    }

    @Override
    public void deleteJobEntity(String entityIds, Long jobId) {
        logger.debug("UpgradeJob deleteJobEntity start ");
        JobDetail job = jobs.get(jobId);
        @SuppressWarnings("unchecked")
        List<UpgradeEntity> upgradeEntityList = (List<UpgradeEntity>) job.getJobDataMap().get("entityList");
        if (entityIds != null) {
            String[] entityIdsArrayString = entityIds.split("\\$");
            List<UpgradeEntity> deleteEntityList = new ArrayList<UpgradeEntity>();
            for (String anEntityIdsArrayString : entityIdsArrayString) {
                try {
                    UpgradeEntity upgradeEntity = new UpgradeEntity();
                    Long entityId = new Long(anEntityIdsArrayString);
                    upgradeEntity.setJobId(jobId);
                    upgradeEntity.setEntityId(entityId);
                    deleteEntityList.add(upgradeEntity);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }

            for (Iterator<UpgradeEntity> iterator = upgradeEntityList.iterator(); iterator.hasNext();) {
                UpgradeEntity upgradeEntity = iterator.next();
                if (deleteEntityList.contains(upgradeEntity)) {
                    iterator.remove();
                }
            }
            logger.debug("UpgradeJob deleteJobEntity delete.size: " + deleteEntityList.size()
                    + " upgradeEntityList.size: " + upgradeEntityList.size());
            job.getJobDataMap().put("entityList", upgradeEntityList);
            upgradeJobDao.deleteJobEntity(deleteEntityList);
            logger.debug("UpgradeJob deleteJobEntity end ");

        }
    }

    @Override
    public void upgradeNow(List<Long> entityIdList, Long jobId) {
        for (Long entityId : entityIdList) {
            upgradeSingleNow(entityId, jobId);
        }
    }

    @Override
    public void upgradeSingleNow(Long entityId, Long jobId) {
        UpgradeJobInfo upgradeJobInfo = upgradeJobDao.getJob(jobId);
        UpgradeEntity upgradeEntity = upgradeJobDao.selectUpgradeEntityByEntityId(entityId, jobId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        upgradeEntity.setParam(snmpParam);
        upgradeEntity.setUpgradeVersion(upgradeJobInfo.getVersionName());
        upgradeEntity.setJobId(jobId);
        upgradeEntity.setForceUpgrade(true);
        List<UpgradeEntity> upgradeEntityList = new ArrayList<UpgradeEntity>();
        upgradeEntityList.add(upgradeEntity);
        upgradeEntityList(upgradeJobInfo, upgradeEntityList);
    }

    @Override
    public synchronized List<Future<UpgradeEntity>> upgradeEntityList(UpgradeJobInfo upgradeJobInfo,
            List<UpgradeEntity> upgradeEntityList) {
        List<Future<UpgradeEntity>> re = new ArrayList<>();
        // List<Future<UpgradeEntity>> jobFutrues = jobFutrueMap.get(upgradeJobInfo.getJobId());
        UpgradeGlobalParam upgradeGlobalParam = upgradeParamService.getUpgradeGlobalParam();
        Long glbalRetryTimes = upgradeGlobalParam.getRetryTimes();
        logger.debug("UpgradeJob glbalRetryTimes " + glbalRetryTimes + " TransferType "
                + upgradeJobInfo.getTransferType());

        for (UpgradeEntity upgradeEntity : upgradeEntityList) {
            boolean retry = false;
            Long retryTimes = upgradeEntity.getRetryTimes();
            if (retryTimes + 1 <= glbalRetryTimes) {
                retry = true;
            }
            boolean isUpgrading = UpgradeStatusConstants.isUpgradeNow(upgradeEntity.getUpgradeStatus());
            // boolean existFuture = isFutureExist(upgradeEntity, jobFutrues);
            boolean upgradeSuccess = UpgradeStatusConstants.isUpgradeSuccess(upgradeEntity.getUpgradeStatus());
            logger.debug("UpgradeJob.upgradeEntity getForceUpgrade " + upgradeEntity.getForceUpgrade()
                    + "upgradeStatus(Success=1):" + upgradeEntity.getUpgradeStatus() + " getRetry " + retry
                    + " isUpgrading " + isUpgrading);

            // 每次升级前先清空之前的UpgradeNote
            upgradeEntity.setUpgradeNote(null);
            // 强制升级、不存在正在执行的Future并且可以被重试,并且之前没有升级成功过，则生成对应的worker
            if ((upgradeEntity.getForceUpgrade() || (!isUpgrading && retry && !upgradeSuccess))) {
                if (upgradeJobInfo.getTransferType().equals(UpgradeJob.TFTP)) {
                    TftpClientService tftpClientService = (TftpClientService) beanFactory.getBean("tftpClientService");
                    String tftpIp = tftpClientService.getTftpClientInfo().getIp();
                    UpgradeCheckService upgradeCheckService = (UpgradeCheckService) beanFactory
                            .getBean("upgradeCheckC_BService");
                    boolean tftpStatus = upgradeCheckService.checkInerTftp();
                    String ip = upgradeEntity.getIp();
                    Long entityId = upgradeEntity.getEntityId();
                    SnmpParam snmpParam = upgradeEntity.getParam();
                    Entity entity = entityService.getEntity(upgradeEntity.getEntityId());
                    String originVersion = upgradeRecordService.getCmcVersionByEntityId(upgradeEntity.getEntityId());
                    UpgradeRecord upgradeRecord = new UpgradeRecord();
                    upgradeRecord.setEntityId(entityId);
                    upgradeRecord.setManageIp(ip);
                    upgradeRecord.setStartTime(new Timestamp(System.currentTimeMillis()));
                    upgradeRecord.setEntityName(entity.getName());
                    upgradeRecord.setTypeId(entity.getTypeId());
                    upgradeRecord.setTypeName(entity.getTypeName());
                    upgradeRecord.setMac(entity.getMac());
                    upgradeRecord.setRetryTimes(retryTimes + 1);
                    upgradeRecord.setJobName(upgradeJobInfo.getName());
                    upgradeEntity.setRetryTimes(retryTimes + 1);
                    // upgradeEntity.setNote("");
                    upgradeRecord.setUpgradeVersion(upgradeJobInfo.getVersionName());
                    upgradeRecord.setUpLinkEntityName(entity.getName());
                    upgradeRecord.setOriginVersion(originVersion);
                    if (retryTimes + 1 < glbalRetryTimes) {
                        upgradeEntity.setRetry(true);
                    } else {
                        upgradeEntity.setRetry(false);
                    }
                    logger.debug("UpgradeJob." + "upgradeEntity.retryTimes " + upgradeEntity.getRetryTimes()
                            + "upgradeEntity.getRetry " + upgradeEntity.getRetry() + " upgradeRecord.getRetryTimes "
                            + upgradeRecord.getRetryTimes());
                    upgradeEntity.setStartTime(new Timestamp(System.currentTimeMillis()));
                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECHTFTP_NOW);
                    updateUpgradeEntity(upgradeEntity);

                    if (!tftpStatus) {
                        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECHTFTP_ERROR);
                        upgradeEntity.setRetry(false);
                        updateUpgradeEntity(upgradeEntity);

                        upgradeRecord.setStatus(UpgradeStatusConstants.CHECHTFTP_ERROR);
                        upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                        upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                        logger.debug("TFTP Server Can NOT CONNECT:CC IP[" + ip + "]" + "[" + entityId + "]");
                        continue;
                    } else {
                        logger.debug("UpgradeJob.Entity[" + entity.getEntityId() + "] tftp ok");
                    }

                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKPING_NOW);
                    updateUpgradeEntity(upgradeEntity);
                    if (!upgradeCheckService.checkPing(ip)) {
                        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKPING_ERROR);
                        updateUpgradeEntity(upgradeEntity);

                        upgradeRecord.setStatus(UpgradeStatusConstants.CHECKPING_ERROR);
                        upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                        upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                        logger.debug("PING test failed:CC IP[" + ip + "]" + "[" + entityId + "]");
                        continue;
                    } else {
                        logger.debug("UpgradeJob.Entity[" + entity.getEntityId() + "] ping ok");
                    }

                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKSNMP_NOW);
                    updateUpgradeEntity(upgradeEntity);
                    if (!upgradeCheckService.checkSnmp(snmpParam)) {
                        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKSNMP_ERROR);
                        updateUpgradeEntity(upgradeEntity);

                        upgradeRecord.setStatus(UpgradeStatusConstants.CHECKSNMP_ERROR);
                        upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                        upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                        logger.debug("SNMP test failed:CC IP[" + ip + "]" + "[" + entityId + "]");
                        continue;
                    } else {
                        logger.debug("UpgradeJob.Entity[" + entity.getEntityId() + "] snmp ok");
                    }

                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKVERSION_NOW);
                    updateUpgradeEntity(upgradeEntity);
                    if (!upgradeCheckService.checkVersion(snmpParam, upgradeEntity.getUpgradeVersion())) {
                        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKVERSION_ERROR);
                        upgradeEntity.setRetry(false);
                        updateUpgradeEntity(upgradeEntity);

                        upgradeRecord.setStatus(UpgradeStatusConstants.CHECKVERSION_ERROR);
                        upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                        upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                        logger.debug("The current version is consistent with the upgrade version:CC IP[" + ip + "]"
                                + "[" + entityId + "]");
                        continue;
                    } else {
                        logger.debug("UpgradeJob.Entity[" + entity.getEntityId() + "] version ok");
                    }

                    try {
                        UpgradeWorker upgradeWorker = (UpgradeWorker) Class.forName(upgradeJobInfo.getWorkerClass())
                                .newInstance();
                        upgradeWorker.setUpgradeEntity(upgradeEntity);
                        upgradeWorker.setSnmpParam(snmpParam);
                        upgradeWorker.setTftpIp(tftpIp);
                        upgradeWorker.setImageFile(upgradeJobInfo.getImageFile());
                        upgradeWorker.setUbootVersion("");
                        upgradeWorker.setUpgradeRecord(upgradeRecord);
                        upgradeWorker.setBeanFactory(beanFactory);
                        upgradeWorker.setWriteConfig(upgradeGlobalParam.getWriteConfig());
                        Future<UpgradeEntity> future = upgradeThreadPoolManager.process(upgradeWorker);
                        re.add(future);
                        logger.debug("UpgradeJob.Entity[" + entity.getEntityId() + "] start upgradeWorker");
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        logger.error("Create worker error---------" + upgradeEntity.getIp(), e);
                    }
                } else if (upgradeJobInfo.getTransferType().equals(UpgradeJob.FTP)) {
                    FtpConnectService ftpConnectService = (FtpConnectService) beanFactory.getBean("ftpConnectService");
                    FtpConnectInfo ftpConnectAttr = ftpConnectService.getFtpConnectAttr();
                    String ftpIp = ftpConnectAttr.getIp();
                    String ftpUserName = ftpConnectAttr.getUserName();
                    String ftpPassword = ftpConnectAttr.getPwd();
                    UpgradeCheckService upgradeCheckService = (UpgradeCheckService) beanFactory
                            .getBean("upgradeCheckC_AService");
                    boolean ftpStatus = upgradeCheckService.checkInerFtp();

                    String ip = upgradeEntity.getIp();
                    Long entityId = upgradeEntity.getEntityId();
                    Entity entity = entityService.getEntity(entityId);
                    UpgradeRecord upgradeRecord = new UpgradeRecord();
                    upgradeRecord.setEntityId(upgradeEntity.getEntityId());
                    upgradeRecord.setManageIp(upgradeEntity.getIp());
                    upgradeRecord.setStartTime(new Timestamp(System.currentTimeMillis()));
                    upgradeRecord.setEntityName(entity.getName());
                    upgradeRecord.setTypeId(entity.getTypeId());
                    upgradeRecord.setTypeName(entity.getTypeName());
                    upgradeRecord.setMac(entity.getMac());
                    upgradeRecord.setRetryTimes(retryTimes + 1);
                    upgradeRecord.setJobName(upgradeJobInfo.getName());
                    upgradeEntity.setRetryTimes(retryTimes + 1);
                    // upgradeEntity.setNote("");
                    upgradeRecord.setUpgradeVersion(upgradeJobInfo.getVersionName());
                    upgradeRecord.setUpLinkEntityName(entity.getName());
                    logger.debug("UpgradeJob." + "upgradeEntity.retryTimes " + upgradeEntity.getRetryTimes()
                            + "upgradeEntity.getRetry " + upgradeEntity.getRetry() + " upgradeRecord.getRetryTimes "
                            + upgradeRecord.getRetryTimes());
                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKFTP_NOW);
                    updateUpgradeEntity(upgradeEntity);
                    if (!ftpStatus) {
                        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKFTP_ERROR);
                        upgradeEntity.setRetry(false);
                        updateUpgradeEntity(upgradeEntity);

                        upgradeRecord.setStatus(UpgradeStatusConstants.CHECKFTP_ERROR);
                        upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                        upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                        logger.debug("FTP CAN NOT CONNECT:[OLT ip:" + ip + "]" + "[" + entityId + "]");
                        continue;
                    } else {
                        logger.debug("UpgradeJob.Entity[" + entity.getEntityId() + "] ftp ok");
                    }
                    try {
                        UpgradeWorker upgradeWorker = (UpgradeWorker) Class.forName(upgradeJobInfo.getWorkerClass())
                                .newInstance();
                        if (upgradeJobInfo.getSubType() != null && !"".equals(upgradeJobInfo.getSubType())) {
                            upgradeWorker.setSubType(upgradeJobInfo.getSubType());
                        }
                        upgradeWorker.setUpgradeEntity(upgradeEntity);
                        upgradeWorker.setFtpIp(ftpIp);
                        upgradeWorker.setFtpUserName(ftpUserName);
                        upgradeWorker.setFtpPassword(ftpPassword);
                        upgradeWorker.setImageFile(upgradeJobInfo.getImageFile());
                        upgradeWorker.setBeanFactory(beanFactory);
                        upgradeWorker.setUpgradeRecord(upgradeRecord);
                        Future<UpgradeEntity> future = upgradeThreadPoolManager.process(upgradeWorker);
                        re.add(future);
                        logger.debug("UpgradeJob.Entity[" + entity.getEntityId() + "] start upgradeWorker");
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        logger.error("Create worker error---------" + upgradeEntity.getIp(), e);
                    }
                }
            }
        }
        return re;
    }

    /*
     * private boolean isFutureExist(UpgradeEntity upgradeEntity, List<Future<UpgradeEntity>>
     * jobFutrues) { if (jobFutrues == null) { return false; } for (Future<UpgradeEntity> jobFutrue
     * : jobFutrues) { try { UpgradeEntity ue = jobFutrue.get(); boolean isOver =
     * jobFutrue.isCancelled() || jobFutrue.isDone(); //
     * 如果存在没有执行完并且，并且与upgradeEntity匹配上的就认为存在，其他都认为不存在 if (!isOver && ue.equals(upgradeEntity)) {
     * return true; } else { return false; } } catch (InterruptedException e) { logger.debug("", e);
     * } catch (ExecutionException e) { logger.debug("", e); } } return false; }
     */

    @Override
    public void putFutureJob(Long jobId, List<Future<UpgradeEntity>> jobFuture) {
        jobFutrueMap.put(jobId, jobFuture);
    }

    @Override
    public List<Future<UpgradeEntity>> getFutureJob(Long jobId) {
        return jobFutrueMap.get(jobId);
    }

    @Override
    public List<UpgradeJobInfo> getJobByVersionName(String versionName) {
        return upgradeJobDao.selectJobByVersionName(versionName);
    }

    @Override
    public UpgradeJobInfo getJobByName(String jobName) {
        return upgradeJobDao.selectJobByName(jobName);
    }

    @Override
    public boolean isJobExist(String jobName) {
        return getJobByName(jobName) != null ? true : false;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public UpgradeJobInfo getJobById(Long jobId) {
        return upgradeJobDao.getJobById(jobId);
    }

    @Override
    public void updateUpgradeEntity(UpgradeEntity upgradeEntity) {
        upgradeJobDao.updateUpgradeEntity(upgradeEntity);
    }

    @Override
    public UpgradeEntity getUpgradeEntityByEntityId(Long entityId, Long jobId) {
        return upgradeJobDao.selectUpgradeEntityByEntityId(entityId, jobId);
    }

    @Override
    public Boolean slotTypeIsGpua(Map<String, Object> map) {
        Integer preConfigType = upgradeJobDao.selectBdPreConfigType(map);
        return preConfigType == null ? false : preConfigType == EponConstants.BOARD_PRECONFIG_GPUA;
    }

    @Override
    public Boolean hasCmtsType(Long entityId, Long typeId) {
        List<Long> typeIds = upgradeJobDao.selectCmcTypeIdList(entityId);
        if (typeIds.isEmpty()) {
            return false;
        }
        for (Long cmcType : typeIds) {
            if (cmcType.intValue() == typeId.intValue()) {
                return true;
            }
        }
        return false;
    }

    public Integer getMaxPollSize() {
        return maxPollSize;
    }

    public void setMaxPollSize(Integer maxPollSize) {
        this.maxPollSize = maxPollSize;
    }

}
