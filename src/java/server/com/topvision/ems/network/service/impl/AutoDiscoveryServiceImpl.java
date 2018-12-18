/***********************************************************************
 * $Id: AutoDiscoveryServiceImpl.java,v1.0 2014-5-11 下午4:25:53 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.callback.AutoDiscoveryCallBack;
import com.topvision.ems.facade.domain.AutoDiscoveryInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.TopoHandlerProperty;
import com.topvision.ems.facade.network.AutoBatchDiscoveryFacade;
import com.topvision.ems.network.dao.BatchAutoDiscoveryDao;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.AutoDiscoveryPeriodConfig;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.parser.TopologyHandle;
import com.topvision.ems.network.service.AutoDiscoveryService;
import com.topvision.ems.network.service.BatchDiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author Rod John
 * @created @2014-5-11-下午4:25:53
 * 
 */
@Service("autoDiscoveryService")
public class AutoDiscoveryServiceImpl extends BaseService
        implements AutoDiscoveryService, AutoDiscoveryCallBack, BeanFactoryAware {
    private static Logger logger = LoggerFactory.getLogger(BatchDiscoveryServiceImpl.class);
    private static final String AUTO_DISCOVERY_TRIGGER = "AutoDiscoveryTrigger";
    private static final String REFRESH_SUCCESS = "success";
    private static final Integer AUTO_WAIT = 5000;
    @Autowired
    private BatchDiscoveryService batchDiscoveryService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private EntityAddressDao entityAddressDao;
    @Autowired
    private EntityService entityService;
    @Value("${java.rmi.server.hostname}")
    private String callbackIp;
    @Value("${rmi.port}")
    private int callbackPort;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private BatchAutoDiscoveryDao autoDiscoveryDao;
    private BeanFactory beanFactory;
    // private Map<EngineServer, List<String>> eMap = new HashMap<EngineServer, List<String>>();
    private AtomicBoolean autoTopoFlag = new AtomicBoolean(false);
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private final Object mutex = new Object();
    private JobDetail job = null;
    @Autowired
    private EntityTypeService entityTypeService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#start()
     */
    @Override
    public void start() {
        startAutoJob();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#stop()
     */
    @Override
    public void stop() {
        super.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.facade.callback.AutoDiscoveryCallBack#discoveryExporter(com.topvision.ems
     * .facade.domain.AutoDiscoveryInfo)
     */
    @Override
    public void discoveryExporter(AutoDiscoveryInfo option) {
        try {
            refresh(option);
        } catch (Exception e) {
            logger.info("discovery entity:" + option.getIpAddress(), e);
        }
    }

    private String refresh(AutoDiscoveryInfo option) {
        // 组装设备结构数据
        Entity entity = packageEntity(option);

        // 判断License限制设备数量
        Long entityLicenseGroupId = entityTypeService.getEntityLicenseGroupIdByEntityTypeId(entity.getTypeId());
        if (!entityService.checkEntityLimitInLicense(entityLicenseGroupId)) {
            entity.setTopoInfo("LicenseLimit");
            return "LicenseLimit";
        }

        // ADD by RodJohn
        // modify by fanzidong@20170920 梳理TopologyHandle，业务逻辑处理移至内部，这里只将结果传递
        TopologyHandle handler = batchDiscoveryService.getTopologyHandle((option.getSysInfo()[0]));
        if (handler != null) {
            String handleResult = handler.handleTopoResult(option, entity);
            // 处理后的结果不是success，无需继续添加
            if (!TopologyHandle.SUCCESS.equals(handleResult)) {
                entity.setIp(option.getIpAddress());
                entity.setTopoInfo(handleResult);
                return REFRESH_SUCCESS;
            }
        }

        // 可以正常添加设备
        entityDao.insertEntity(entity);

        // 将设备移入批量拓扑设置的地域中
        try {
            entityDao.moveEntityForBatchDiscovery(entity.getEntityId(),
                    Arrays.asList(option.getTopoFolderId().longValue()));
        } catch (Exception e) {
            logger.error(e.toString());
        }
        
        // insert address
        EntityAddress ea = new EntityAddress();
        ea.setEntityId(entity.getEntityId());
        ea.setIp(entity.getIp());
        entityAddressDao.insertEntity(ea);
        
        // insert entitySnap
        EntitySnap entitySnap = new EntitySnap();
        // 初始化的时候默认连通
        entitySnap.setState(true);
        entitySnap.setEntityId(entity.getEntityId());
        entityDao.insertEntitySnap(entitySnap);
        
        // 前台推送
        entity.setTopoInfo("EntityTopoSuccess");
        // 采集数据 此处不能采用同步方式
        entityService.txCreateMessage(entity);
        return REFRESH_SUCCESS;
    }

    private Entity packageEntity(AutoDiscoveryInfo option) {
        Entity entity = new Entity();
        Timestamp st = new Timestamp(System.currentTimeMillis());
        entity.setIp(option.getIpAddress());
        entity.setCreateTime(st);
        entity.setModifyTime(st);
        // 设置设备的SNMP参数
        entity.setParam(option.getSnmpParam());
        // 设置其默认为可管理的。
        entity.setStatus(Boolean.TRUE);
        entity.setSysObjectID(option.getSysInfo()[0]);
        entity.setSysName(option.getSysInfo()[1]);
        // 如果设备名称超过63，则截取前63位为别名
        if (option.getSysInfo()[1].length() > 63) {
            entity.setName(option.getSysInfo()[1].substring(0, 63));
        } else {
            entity.setName(option.getSysInfo()[1]);
        }
        EntityType type = entityTypeService.getEntityTypeBySysObjId(entity.getSysObjectID());
        entity.setTypeId(type.getTypeId());
        option.setTypeId(type.getTypeId());
        entity.setTypeName(type.getDisplayName());

        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.callback.AutoDiscoveryCallBack#discoveryComplete()
     */
    @Override
    public void discoveryComplete() {
        topoUnlock();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.AutoDiscoveryService#autoBatchDiscovery(java.util.List,
     * java.lang.Long, java.util.List, java.util.List)
     */
    @Override
    public void autoBatchDiscovery(List<String> ips, Long topoFolderId, List<SnmpParam> snmpParams,
            List<String> types) {
        topoLock();
        StringBuilder callBackURL = new StringBuilder();
        if (callbackIp == null || callbackIp.equalsIgnoreCase("")) {
            callbackIp = EnvironmentConstants.getHostAddress();
        }
        callBackURL.append("rmi://").append(callbackIp).append(":").append(callbackPort)
                .append("/autoDiscoveryService");
        Integer pingTimeOut = Integer.parseInt(
                systemPreferencesService.getModulePreferences("Ping").getProperty("Ping.batchtopo.timeout", "4000"));
        Integer pingCount = Integer
                .parseInt(systemPreferencesService.getModulePreferences("Ping").getProperty("Ping.count", "1"));
        Integer pingRetry = Integer
                .parseInt(systemPreferencesService.getModulePreferences("Ping").getProperty("Ping.retry", "0"));
        
        List<EntityType> allSysObjectId = entityTypeService.getAllEntityTypeWithLicenseSupport();
        
        facadeFactory.getFacade(callbackIp, AutoBatchDiscoveryFacade.class).autoBatchDiscovery(ips, topoFolderId,
                snmpParams, allSysObjectId, types, callBackURL.toString(), pingTimeOut, pingCount, pingRetry);
        while (autoTopoFlag.get()) {
            try {
                synchronized (mutex) {
                    mutex.wait(AUTO_WAIT);
                }
            } catch (Exception e) {
                logger.info("", e);
                continue;
            }
        }
    }

    /**
     * 
     * 打开拓扑的开关标志
     * 
     */
    private void topoLock() {
        autoTopoFlag.set(true);
    }

    /**
     * 
     * 关闭设备采集的接口方法
     * 
     */
    private void topoUnlock() {
        autoTopoFlag.set(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.BatchDiscoveryService#getTopoFlag()
     */
    @Override
    public AtomicBoolean getTopoFlag() {
        return autoTopoFlag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans
     * .factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.AutoDiscoveryService#reStartAutoDiscoveryJob()
     */
    @Override
    public void reStartAutoDiscoveryJob() {
        stopAutoJob();
        startAutoJob();
    }

    /**
     * Stop Auto Job
     * 
     * @throws NetworkException
     */
    private void stopAutoJob() throws NetworkException {
        logger.info("Stop Auto Discovery Job");
        if (job == null) {
            return;
        }
        try {
            schedulerService.deleteJob(job.getKey());
        } catch (SchedulerException e) {
            logger.error("Stop Auto Discovery Job:", e);
            throw new NetworkException("Stop Auto Discovery Job:", e);
        }
    }

    /**
     * Start Auto Job
     * 
     */
    @SuppressWarnings("unchecked")
    private void startAutoJob() {
        try {
            job = newJob((Class<Job>) Class.forName("com.topvision.ems.network.service.impl.AutoDiscoveryJob"))
                    .withIdentity("BatchAutoDiscoveryJob").build();
            job.getJobDataMap().put("beanFactory", beanFactory);
            /*
             * job.getJobDataMap().put("facadeFactory", facadeFactory);
             * job.getJobDataMap().put("monitor", dm); job.getJobDataMap().put("onlineService",
             * onlineService); job.getJobDataMap().put("monitorService", this);
             * job.getJobDataMap().put("beanFactory", beanFactory);
             * job.getJobDataMap().put("messageService", messageService);
             * job.getJobDataMap().put("entityService", entityService);
             */
            List<AutoDiscoveryPeriodConfig> autoDiscoveryPeriodConfigs = autoDiscoveryDao
                    .getAutoDiscoveryPeriodConfigs();
            for (AutoDiscoveryPeriodConfig config : autoDiscoveryPeriodConfigs) {
                if (config.getActivating()) {
                    if (config.getPeriodType() == AutoDiscoveryPeriodConfig.PERIOD_REGULAR) {
                        SimpleTrigger trigger = newTrigger().withIdentity(AUTO_DISCOVERY_TRIGGER)
                                .withSchedule(repeatSecondlyForever(config.getPeriod())).build();
                        schedulerService.scheduleJob(job, trigger);
                        return;
                    } else if (config.getPeriodType() == AutoDiscoveryPeriodConfig.PERIOD_TIMING) {
                        CronTrigger trigger = newTrigger().withIdentity(AUTO_DISCOVERY_TRIGGER)
                                .withSchedule(CronScheduleBuilder.cronSchedule(config.getPeriodStartExpression()))
                                .build();
                        schedulerService.scheduleJob(job, trigger);
                        return;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.AutoDiscoveryService#getAutoDiscoveryIpsDiscoveryStatus()
     */
    @Override
    public List<BatchAutoDiscoveryIps> getAutoDiscoveryIpsDiscoveryStatus() {
        AutoDiscoveryStatus discoveryStatus = (AutoDiscoveryStatus) beanFactory.getBean("autoDiscoveryStatus");
        return discoveryStatus.getBatchAutoDiscoveryIps();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.AutoDiscoveryService#getAutoDiscoveryNextFireTime()
     */
    @Override
    public Date getAutoDiscoveryNextFireTime() {
        try {
            return schedulerService.getTrigger(TriggerKey.triggerKey(AUTO_DISCOVERY_TRIGGER)).getNextFireTime();
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.callback.AutoDiscoveryCallBack#searchProductTopoInfo(java.lang.
     * String)
     */
    @Override
    public TopoHandlerProperty searchProductTopoInfo(String sysOid) {
        TopologyHandle topologyHandle = batchDiscoveryService.getTopologyHandle(sysOid);
        if (topologyHandle != null) {
            return topologyHandle.getTopoInfo();
        }
        return null;
    }

}
