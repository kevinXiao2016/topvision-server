/***********************************************************************
 * $Id: AutoRefreshServiceImpl.java,v1.0 2014-10-15 下午1:59:34 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.dao.AutoRefreshDao;
import com.topvision.ems.network.domain.AutoRefreshConfig;
import com.topvision.ems.network.domain.TopologyRefreshLock;
import com.topvision.ems.network.service.AutoRefreshService;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Rod John
 * @created @2014-10-15-下午1:59:34
 * 
 */
@Service("autoRefreshService")
public class AutoRefreshServiceImpl extends BaseService implements AutoRefreshService, BeanFactoryAware, EntityListener {

    private static Logger logger = LoggerFactory.getLogger(AutoRefreshServiceImpl.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    private BeanFactory beanFactory;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    @Autowired
    private AutoRefreshDao autoRefreshDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private DiscoveryService discoveryService;
    @Value("${topology.autoRefreshPoolSize:10}")
    private Integer autoRefreshPoolSize;
    private Boolean autoRefreshSwitch;
    private Integer autoRefreshInterval;

    // 调度线程实例集合
    @SuppressWarnings("rawtypes")
    private ConcurrentHashMap<Long, ScheduledFuture> scheduledFutureConcurrentHashMaps = new ConcurrentHashMap<Long, ScheduledFuture>();

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(EntityListener.class, this);
        ThreadFactory threadFactory = new ThreadFactory() {
            private ThreadGroup threadGroup = new ThreadGroup("AutoRefresh");

            public Thread newThread(Runnable r) {
                return new Thread(threadGroup, r);
            }
        };
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(autoRefreshPoolSize, threadFactory);
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
        messageService.removeListener(EntityListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#start()
     */
    @Override
    public void start() {
        AutoRefreshConfig config = getAutoRefreshConfig();
        this.autoRefreshSwitch = config.getAutoRefreshSwitch();
        this.autoRefreshInterval = config.getAutoRefreshInterval();

        // 开启所有设备的自动刷新任务
        startAutoRefreshTasks();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#stop()
     */
    @Override
    public void stop() {
        this.scheduledThreadPoolExecutor.shutdownNow();
        this.scheduledFutureConcurrentHashMaps.clear();
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

    /**
     * 
     * Auto Refresh Task
     * 
     * @created @2014-10-16-上午9:39:24
     * 
     */
    private class AutoRefreshTask implements Runnable {
        private Entity entity;
        @SuppressWarnings("rawtypes")
        private DiscoveryService discoveryService;

        @SuppressWarnings("rawtypes")
        public AutoRefreshTask(Entity entity, DiscoveryService discoveryService) {
            this.entity = entity;
            this.discoveryService = discoveryService;
        }

        @Override
        public void run() {
            if (discoveryService != null) {
                ReentrantLock lock = TopologyRefreshLock.getReentrantLock(entity.getEntityId());
                if (lock.tryLock()) {
                    try {
                        // 在刷新之前确定下，设备类型是否发生了变化
                        long oldType = entity.getTypeId();
                        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
                        EntityType type = entityService.topoEntityTypeId(entity, snmpParam);
                        if (type != null && type.getTypeId() != EntityType.UNKNOWN_TYPE && oldType != type.getTypeId()) {
                            // 發生了變化，需要重新設置discoveryService
                            String discoveryServiceStr = String.format("%sDiscoveryService", type.getName());
                            if (!beanFactory.containsBean(discoveryServiceStr)) {
                                discoveryServiceStr = type.getDiscoveryBean();
                            }
                            discoveryService = (DiscoveryService) beanFactory.getBean(discoveryServiceStr);
                            if (discoveryService == null) {
                                logger.error("cannot get DiscoveryService of " + type.getName());
                                return;
                            }
                            entity.setTypeId(type.getTypeId());
                            discoveryService.refresh(entity.getEntityId());
                        } else {
                            // 没有发生变化，直接调用自动刷新
                            discoveryService.autoRefresh(entity);
                        }
                    } catch (Exception e) {
                        logger.error("AutoRefresh " + entity.getIp() + " error:", e);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.AutoRefreshService#getAutoRefreshConfig()
     */
    @Override
    public AutoRefreshConfig getAutoRefreshConfig() {
        return autoRefreshDao.getAutoRefreshConfig();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.AutoRefreshService#updateAutoRefreshConfig(com.topvision
     * .ems.network.domain.AutoRefreshConfig)
     */
    @Override
    public void updateAutoRefreshConfig(AutoRefreshConfig config) {
        autoRefreshDao.updateAutoRefreshConfig(config);

        Boolean oldSwitch = this.autoRefreshSwitch;
        Integer oldInterval = this.autoRefreshInterval;
        Boolean newSwitch = config.getAutoRefreshSwitch();
        Integer newInterval = config.getAutoRefreshInterval();

        this.autoRefreshSwitch = newSwitch;
        this.autoRefreshInterval = newInterval;

        // 更新自动刷新配置分为如下情况：
        // 1. 从开启到关闭 ：关闭所有自动刷新任务即可
        // 2. 从关闭到开启 ：开启所有自动刷新任务即可
        // 3. 保持开启，更改时间 ：先停掉，再重新启动
        // 4. 保持关闭，更改时间：不做操作
        if (oldSwitch.equals(true) && newSwitch.equals(false)) {
            // 1. 从开启到关闭
            stopAutoRefreshTasks();
        } else if (oldSwitch.equals(false) && newSwitch.equals(true)) {
            // 2. 从关闭到开启
            startAutoRefreshTasks();
        } else if (oldSwitch.equals(true) && newSwitch.equals(true) && !oldInterval.equals(newInterval)) {
            // 3. 保持开启，更改时间
            stopAutoRefreshTasks();
            startAutoRefreshTasks();
        }

    }

    /**
     * check entity type
     * 
     * @param entity
     * @return
     */
    private boolean checkEntityType(Entity entity) {
        Long typeId = entity.getTypeId();
        if (entityTypeService.isOlt(typeId) || entityTypeService.isCcmtsWithAgent(typeId)
                || entityTypeService.isCmts(typeId)) {
            return true;
        }
        return false;
    }

    public Integer getAutoRefreshPoolSize() {
        return autoRefreshPoolSize;
    }

    @Override
    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }

    @Override
    public void entityAdded(EntityEvent event) {
        // 设备添加时要加上该设备的自动刷新
        Entity entity = event.getEntity();
        if (checkEntityType(entity)) {
            addEntityAutoRefreshTask(entity, this.autoRefreshInterval);
        }
    }

    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    @Override
    public void entityChanged(EntityEvent event) {
    }

    @Override
    public void entityRemoved(EntityEvent event) {
        // 停掉对应设备的自动刷新
        Entity entity = event.getEntity();
        if (checkEntityType(entity)) {
            stopEntityAutoRefreshTask(entity.getEntityId());
        }
    }

    @Override
    public void managerChanged(EntityEvent event) {
    }

    /**
     * 启动所有设备的刷新任务
     */
    private void startAutoRefreshTasks() {
        logger.info("startAutoRefreshTasks");
        if (!autoRefreshSwitch) {
            // 没有开启自动刷新
            return;
        }
        // 获取所有设备，并筛选出需要刷新的设备
        List<Entity> entities = entityService.getEntity();
        List<Entity> refreshEntities = new ArrayList<Entity>();
        for (Entity entity : entities) {
            if (checkEntityType(entity)) {
                refreshEntities.add(entity);
            }
        }
        if (refreshEntities.size() == 0) {
            logger.info("no entity needs to auto refresh");
            return;
        }
        logger.info("there are " + refreshEntities.size() + " entities needs to auto refresh");
        // 计算间隔，分发任务
        int delayInterval = this.autoRefreshInterval / refreshEntities.size();
        int delay = 0;
        for (Entity entity : refreshEntities) {
            addEntityAutoRefreshTask(entity, delay);
            delay += delayInterval;
        }
    }

    /**
     * 为单台设备创建自动刷新任务，并加入线程池
     * 
     * @param entity
     * @param delay
     */
    @SuppressWarnings("rawtypes")
    private void addEntityAutoRefreshTask(Entity entity, int delay) {
        if (!autoRefreshSwitch) {
            // 没有开启自动刷新
            return;
        }
        try {
            DiscoveryService ds = null;
            EntityType entityType = entityTypeService.getEntityType(entity.getTypeId());
            String discoveryService = String.format("%sDiscoveryService", entityType.getName());
            if (!beanFactory.containsBean(discoveryService)) {
                discoveryService = entityType.getDiscoveryBean();
            }
            ds = (DiscoveryService) beanFactory.getBean(discoveryService);
            if (ds == null) {
                logger.error("cannot get DiscoveryService of " + entityType.getName());
                return;
            }

            AutoRefreshTask task = new AutoRefreshTask(entity, ds);
            ScheduledFuture<?> future = scheduledThreadPoolExecutor.scheduleAtFixedRate(task, delay,
                    this.autoRefreshInterval, TimeUnit.MILLISECONDS);
            scheduledFutureConcurrentHashMaps.put(entity.getEntityId(), future);
        } catch (BeansException e) {
            logger.error("", e);
        } catch (Exception e) {
            logger.error("cannot addEntityAutoRefreshTask" + entity, e);
        }
    }

    /**
     * 停止所有设备的自动刷新
     */
    private void stopAutoRefreshTasks() {
        // 遍历future，cacel处理
        for (Long entityId : scheduledFutureConcurrentHashMaps.keySet()) {
            stopEntityAutoRefreshTask(entityId);
        }
        scheduledThreadPoolExecutor.purge();
        scheduledFutureConcurrentHashMaps.clear();
    }

    /**
     * 停止单台设备的自动刷新
     * 
     * @param entityId
     */
    private void stopEntityAutoRefreshTask(Long entityId) {
        try {
            if (!scheduledFutureConcurrentHashMaps.containsKey(entityId)) {
                return;
            }
            ScheduledFuture<?> future = scheduledFutureConcurrentHashMaps.get(entityId);
            future.cancel(true);
            scheduledFutureConcurrentHashMaps.remove(entityId);
        } catch (Exception e) {
            logger.error("cannot stopEntityAutoRefreshTask: " + entityId);
        }
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

}
