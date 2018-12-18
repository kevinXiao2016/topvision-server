/***********************************************************************
 * $Id: DiscoveryServiceImpl.java,v1.0 2011-6-28 下午07:09:40 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.connectivity.service.ConnectivityService;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.PortEntity;
import com.topvision.ems.facade.network.DiscoveryFacade;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityAttributeDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.LinkDao;
import com.topvision.ems.network.dao.PortDao;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.TopologyRefreshLock;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.OnlineService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.MacConflictException;
import com.topvision.exception.service.NetworkException;
import com.topvision.exception.service.UnknownEntityTypeException;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.exception.EntityRefreshException;
import com.topvision.framework.exception.engine.PingException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.EntityTypeChangeEvent;
import com.topvision.platform.message.event.EntityTypeChangeListener;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.OperationService;

/**
 * @author Victor
 * @created @2011-6-28-下午07:09:40
 * 
 */
@Service("discoveryService")
public class DiscoveryServiceImpl<T extends DiscoveryData> extends BaseService
        implements DiscoveryService<T>, EntityListener, BeanFactoryAware {
    @Autowired
    protected MessageService messageService;
    @Autowired
    protected OperationService operationService;
    @Resource(name = "entityDao")
    protected EntityDao entityDao;
    @Autowired
    protected EntityAddressDao entityAddressDao;
    @Autowired
    protected PortDao portDao;
    @Autowired
    protected LinkDao linkDao;
    @Autowired
    protected EntityAttributeDao entityAttributeDao;
    @Autowired
    protected EntityTypeService entityTypeService;
    @Autowired
    protected EntityService entityService;
    @Autowired
    protected FacadeFactory facadeFactory;
    protected BeanFactory beanFactory;
    @Autowired
    protected OnlineService onlineService;
    @Autowired
    protected ConnectivityService connectivityService;

    // private final static Integer RSTATUS = 1;// 设备刷新状态

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(EntityListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(EntityListener.class, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entity refresh(Long entityId) {
        ReentrantLock lock = TopologyRefreshLock.getReentrantLock(entityId);
        if (lock.tryLock()) {
            try {
                SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
                Entity entity = entityDao.selectByPrimaryKey(entityId);
                boolean entityTypeChangeFlag = false;
                EntityType entityType = entityTypeService.getEntityType(entity.getTypeId());
                DiscoveryService<T> ds = getCorrectDiscoveryService(entityType);
                try {
                    int delay = ds.checkConnectivity(entity, snmpParam);

                    if (delay < 0) {
                        updateEntityOfflineSnap(entity);
                        throw new PingException("discoveryPing");
                    }
                    updateEntityOnlineSnap(entity);
                } catch (MacConflictException e) {
                    // 在MAC唯一性下发生了MAC地址变化冲突
                    logger.debug("discoveryPing", e);
                    updateEntityOfflineSnap(entity);
                    throw e;
                } catch (Exception e) {
                    logger.debug("discoveryPing", e);
                    updateEntityOfflineSnap(entity);
                    throw new PingException("discoveryPing");
                }
                if (entity.getTypeId() == EntityType.UNKNOWN_TYPE) {
                    snmpParam.setTimeout(2000);
                    DiscoveryData sysData = discovery(snmpParam);
                    if (sysData.getStackTrace() != null) {
                        // entityDao.updateEntityType(entityId,
                        // EntityType.UNKNOWN_TYPE + 0L,
                        // EntityType.UNKNOWN_TYPE);
                    } else {
                        entityType = entityTypeService.getEntityTypeBySysObjId(sysData.getSystem().getSysObjectID());
                        if (entityType != null) {
                            entityDao.updateEntityType(entityId, entityType.getTypeId());
                            entity = entityDao.selectByPrimaryKey(entityId);
                            // 设备类型变化的标示
                            entityTypeChangeFlag = true;
                        } else {
                            throw new UnknownEntityTypeException(entity.getIp() + " EntityType Unknown");
                        }
                    }
                    entity = entityDao.selectByPrimaryKey(entityId);
                } else {
                    // 重建设备
                    EntityType type = entityService.topoEntityTypeId(entity, snmpParam);
                    entity = entityDao.selectByPrimaryKey(entityId);
                    if (type != null && type.getTypeId() != EntityType.UNKNOWN_TYPE
                            && type.getTypeId() != entity.getTypeId()) {
                        // First remove
                        List<Long> entityIds = new ArrayList<>();
                        entityIds.add(entityId);
                        entityService.removeEntity(entityIds);
                        // Second add
                        entity.setCorpId(entityTypeService.getCorpBySysObjId(type.getSysObjectID()));
                        entity.setTypeId(type.getTypeId());
                        entity.setIcon48(type.getIcon48());
                        entityService.txCreateEntity(entity);
                        entityService.txCreateMessage(entity);
                        // Log
                        String log = operationService.getEntityReplaceLog(entity.getMac(),
                                entityTypeService.getEntityType(entity.getTypeId()).getDisplayName(),
                                entity.getSysDescr(), this.getClass());
                        OperationLog operationLog = new OperationLog("System", "127.0.0.1", 1, entity.getEntityId(),
                                log.toString());
                        operationService.insertOperationLog(operationLog);
                    }
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("DiscoveryService.entityAdded:" + entity.getIp());
                }
                entityType = entityTypeService.getEntityType(entity.getTypeId());
                T data = null;
                // entityType修改后,
                // 每个子类必须有自己的拓扑发现实现类，通过继承关系来使用主类型的发现方法
                ds = getCorrectDiscoveryService(entityType);
                // 查找拓扑实现
                Long timeTmp = LoggerUtil.topoStartTimeLog(entity.getIp(), "Refresh");
                data = ds.discovery(snmpParam);
                if (logger.isDebugEnabled()) {
                    logger.debug(data.toString());
                }
                if (data.getStackTrace() != null) {
                    throw new NetworkException(data.getStackTrace());
                }
                entity.setParam(data.getSnmpParam());
                entity.setSnmpSupport(true);
                Timestamp lastRefreshTime = new Timestamp(System.currentTimeMillis());
                entity.setLastRefreshTime(lastRefreshTime);
                ds.updateEntity(entity, data);
                ds.syncPerfMoniotr(entity);
                // Modify by Rod 设备类型变换消息
                if (entityTypeChangeFlag) {
                    EntityTypeChangeEvent event = new EntityTypeChangeEvent(entity);
                    event.setEntity(entity);
                    event.setOldEntityTypeId(EntityType.UNKNOWN_TYPE);
                    event.setNewEntityTypeId(entityType.getTypeId());
                    event.setActionName("entityTypeChange");
                    event.setListener(EntityTypeChangeListener.class);
                    messageService.addMessage(event);
                }

                // 发送设备改变消息
                try {
                    EntityEvent entityEvent = new EntityEvent(entity);
                    entityEvent.setEntity(entity);
                    entityEvent.setActionName("entityChanged");
                    entityEvent.setListener(EntityListener.class);
                    messageService.addMessage(entityEvent);
                } catch (Exception e) {
                    logger.debug("send entityChanged message error", e);
                }

                LoggerUtil.topoEndTimeLog(entity.getIp(), "Refresh", timeTmp);
                return entity;
            } catch (Exception e) {
                throw e;
            } finally {
                lock.unlock();
            }
        } else {
            logger.info("Entity " + entityId + " is Refreshing");
            throw new EntityRefreshException("Entity[" + entityId + "] is refreshing.");
        }
    }

    private DiscoveryService<T> getCorrectDiscoveryService(EntityType entityType) {
        DiscoveryService<T> ds = null;
        // Modify by Victor@20150618如果单独设备类型拓扑不存在，则使用设置的拓扑程序
        String discoveryService = String.format("%sDiscoveryService", entityType.getName());
        if (!beanFactory.containsBean(discoveryService)) {
            discoveryService = entityType.getDiscoveryBean();
        }
        ds = (DiscoveryService<T>) beanFactory.getBean(discoveryService);
        if (ds == null) {
            ds = this;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Discovery instance:{}", ds);
        }
        return ds;
    }

    @Override
    public Entity autoRefresh(Entity entity) {
        return entity;
    }

    @Override
    public int checkConnectivity(Entity entity, SnmpParam snmpParam) {
        return connectivityService.checkConnectivityUsingEntityUnique(entity, null, null);
    }

    public void updateEntityOnlineSnap(Entity entity) {
        EntityValueEvent event = new EntityValueEvent(entity.getEntityId());
        event.setEntityId(entity.getEntityId());
        event.setState(true);
        event.setActionName("performanceChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);

    }

    public void updateEntityOfflineSnap(Entity entity) {
        EntityValueEvent event = new EntityValueEvent(entity.getEntityId());
        event.setEntityId(entity.getEntityId());
        event.setState(false);
        event.setActionName("performanceChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);

    }

    @SuppressWarnings("unchecked")
    @Override
    public T discovery(SnmpParam snmpParam) {
        return (T) getDiscoveryFacade(snmpParam.getIpAddress()).discover(snmpParam);
    }

    @Override
    public void updateEntity(Entity entity, T data) {
        try {
            entity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (data.getSystem() != null) {
                entity.setSysDescr(data.getSystem().getSysDescr());
                entity.setSysObjectID(data.getSystem().getSysObjectID());
                entity.setSysUpTime(data.getSystem().getSysUpTime());
                entity.setSysContact(data.getSystem().getSysContact());
                entity.setSysName(data.getSystem().getSysName());
                entity.setSysLocation(data.getSystem().getSysLocation());
                entity.setSysServices(data.getSystem().getSysServices());
            }
            entityDao.updateEntity(entity);
            // updatePort(data, entity.getEntityId());
            logger.info("DiscoveryService updateEntity done");
        } catch (Exception e) {
            logger.error("DiscoveryService updateEntity error:", e);
        }
    }

    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    @Override
    public void entityAdded(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        // Add by Rod
        Entity entity = null;
        try {
            entity = refresh(entityId);
        } catch (Exception e) {
            logger.error("refresh entity error", e);
        }
        EntityEvent evt = new EntityEvent(entity);
        evt.setEntity(entity);
        evt.setActionName("entityDiscovered");
        evt.setListener(EntityListener.class);
        messageService.addMessage(evt);
        if (logger.isDebugEnabled()) {
            logger.debug("topologyImmediately successful!!!");
        }
    }

    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    @Override
    public void entityChanged(EntityEvent event) {
    }

    @Override
    public void entityRemoved(EntityEvent event) {
    }

    @Override
    public void managerChanged(EntityEvent event) {
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * @return the DiscoveryFacade
     */
    protected DiscoveryFacade getDiscoveryFacade(String ip) {
        return facadeFactory.getFacade(ip, DiscoveryFacade.class);
    }

    protected void updatePort(DiscoveryData data, Long entityId) {
        if (data.getInterfaces() != null) {
            for (PortEntity portEntity : data.getInterfaces()) {
                Port port = new Port(entityId, portEntity);
                Long portId = portDao.isPortExists(port);
                if (portId != null) {
                    port.setPortId(portId);
                    portDao.updateEntity(port);
                } else {
                    portDao.insertEntity(port);
                }
            }
        }
    }

    protected void updatePort(List<Port> ports) {
        for (Port port : ports) {
            Long portId = portDao.isPortExists(port);
            if (portId != null) {
                port.setPortId(portId);
                portDao.updateEntity(port);
            } else {
                portDao.insertEntity(port);
            }
        }
    }

    protected void updateLink(List<Link> links) {
        for (Link link : links) {
            Long linkId = linkDao.isLinkExists(link);
            if (linkId != null) {
                link.setLinkId(linkId);
                linkDao.updateEntity(link);
            } else {
                linkDao.insertEntity(link);
            }
        }
    }

    protected void updateEntityAddress(Long entityId, List<EntityAddress> list) {
        entityAddressDao.deleteByEntityId(entityId);
        for (EntityAddress address : list) {
            try {
                entityAddressDao.insertEntity(address);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    @Override
    public void syncEntityInfo(Entity entity, T data) {
    }

    @Override
    public void sendSynchronizedEvent(Long entityId, T data) {
    }

    @Override
    public void syncPerfMoniotr(Entity entity) {
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

}
