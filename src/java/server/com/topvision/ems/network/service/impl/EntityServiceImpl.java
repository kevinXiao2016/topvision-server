package com.topvision.ems.network.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.CmdFacade;
import com.topvision.ems.facade.PingFacade;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityAttribute;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.IpAddressTable;
import com.topvision.ems.facade.domain.PingResultsEntry;
import com.topvision.ems.facade.domain.Snap;
import com.topvision.ems.facade.network.DiscoveryFacade;
import com.topvision.ems.fault.dao.AlertDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityAttributeDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.LinkDao;
import com.topvision.ems.network.dao.TopoFolderDao;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.FolderCategory;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.template.dao.EntityTypeDao;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.ExistEntityException;
import com.topvision.exception.service.OutOfLicenseException;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.license.common.domain.Module;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.domain.User;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.LicenseService;
import com.topvision.platform.service.SystemPreferencesService;

@Service("entityService")
public class EntityServiceImpl extends BaseService implements EntityService {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private EntityAddressDao entityAddressDao;
    @Autowired
    private PerformanceService<?> performanceService;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private EntityAttributeDao entityAttributeDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityTypeDao entityTypeDao;
    @Autowired
    private MessageService messageService;
    private byte snmpRetry;
    private int snmpPort;
    private long snmpTimeout;
    @Autowired
    private FacadeFactory facadeFactory;
    private static final String TABLE_NAME_PRE = "t_entity_";
    private static final String ADMIN_USER = "admin";
    private static final String ENTITY = "entity";
    // add by victor@20131112保持cmd命令的缓存
    private final Map<String, CmdFacade> cmdCaches = new HashMap<String, CmdFacade>();
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private TopoFolderDao topoFolderDao;
    @Autowired
    private AlertDao alertDao;
    @Autowired
    private AlertService alertService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#clearCache(java.lang.Long)
     */
    @Override
    public void clearCache(Long entityId) {
        entityDao.getEntityCaches().remove(entityId);
        entityDao.getSnmpParamCaches().remove(entityId);
    }

    /**
     * 从回收站删除设备.
     * 
     * @param entityIds
     */
    public void deleteEntityFromRecyle(List<Long> entityIds) {
        // 手动删除连接关系
        linkDao.deleteLinkByEntityId(entityIds);
        // 外键级联删除关联的端口关系
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("deleteEntityFromRecyle:" + entityIds);
        }
        for (Long id : entityIds) {
            Entity entity = entityDao.selectByPrimaryKey(id);
            EntityEvent e = new EntityEvent(entity);
            e.setEntity(entity);
            e.setActionName("entityRemoved");
            e.setListener(EntityListener.class);
            messageService.fireMessage(e);
            entityCache.remove(id);
        }
        entityDao.deleteByPrimaryKey(entityIds);
    }

    protected void firedEntityAdded(EntityEvent event) {
        logger.info("EntityService begin to Send Topology Message entityId:" + event.getEntity().getEntityId());
        event.setActionName("entityAdded");
        event.setListener(EntityListener.class);
        messageService.addMessage(event);
        // messageService.fireMessage(event);
    }

    protected void firedEntityRenamed(EntityEvent event) {
        event.setActionName("entityChanged");
        event.setListener(EntityListener.class);
        messageService.addMessage(event);
    }

    protected void firedEntityUpdated(EntityEvent event) {
        event.setActionName("entityChanged");
        event.setListener(EntityListener.class);
        messageService.addMessage(event);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getAgentEntityStateByPage(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<EntitySnap> getAgentEntityStateByPage(Page P) {
        // return entitySnapDao.getAgentEntityStateByPage(P);
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getAllEntityType()
     */
    @Override
    public List<EntityType> getAllEntityType() {
        return entityTypeDao.selectByMap(null);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntity(Long)
     */
    @Override
    public Entity getEntity(Long entityId) {
        return entityDao.selectByPrimaryKey(entityId);
    }

    /**
     * @return the entityAddressDao
     */
    public EntityAddressDao getEntityAddressDao() {
        return entityAddressDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityAgentSupported(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> getEntityAgentSupported(Page p) {
        return entityDao.getEntityAgentSupported(p);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityAttribute(java.lang.Long)
     */
    @Override
    public List<EntityAttribute> getEntityAttribute(Long entityId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        List<EntityAttribute> re = entityAttributeDao.selectByMap(map);
        return re;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityAttribute(java.lang.Long,java.lang.String)
     */
    @Override
    public List<EntityAttribute> getEntityAttribute(Long entityId, String group) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("group", group);
        List<EntityAttribute> re = entityAttributeDao.selectByMap(map);
        return re;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityByIp(java.util.List)
     */
    @Override
    public List<Entity> getEntityByIp(List<String> ips) {
        return entityDao.getEntityByIp(ips);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getSubEntityByEntityId(java.lang.Long)
     */
    @Override
    public List<Entity> getSubEntityByEntityId(Long entityId) {
        return entityDao.getSubEntityByEntityId(entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityByType(Long)
     */
    @Override
    public List<Entity> getEntityByType(Long type) {
        return entityDao.getEntityByType(type);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityByType(com.topvision.framework.domain.Page,
     *      Long)
     */
    @Override
    public PageData<Entity> getEntityByType(Page p, Long typeId) {
        return entityDao.getEntityByType(p, typeId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntity()
     */
    @Override
    public List<Entity> getEntity() {
        return entityDao.selectByMap(null);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntity(Long, Long)
     */
    @Override
    public Entity getEntity(Long entityId, Long folderId) {
        Entity entity = new Entity();
        entity.setEntityId(entityId);
        entity.setFolderId(folderId);
        return entityDao.getEntityInFolder(entity);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntity(com.topvision.framework.domain.Page)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#getEntity(com.topvision.framework.domain.
     * Page)
     */
    @Override
    public PageData<Entity> getEntity(Page p) {
        return entityDao.selectByMap(null, p);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityByFolderId(Long,
     *      java.lang.String)
     */
    @Override
    public List<Entity> getEntityByFolderId(Long folderId, String sortName) {
        return entityDao.getEntityByFolderId(folderId, sortName);
    }

    @Override
    public List<Entity> getAllEntityByFolderId(Long folderId) {
        return entityDao.getAllEntityByFolderId(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityByIp(java.lang.String)
     */
    @Override
    public Entity getEntityByIp(String ip) {
        return entityDao.getEntityByIp(ip);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityIdByIp(java.lang.String)
     */
    @Override
    public Long getEntityIdByIp(String ip) {
        return entityAddressDao.selectByAddress(ip).getEntityId();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityInLonely(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> getEntityInLonely(Page page) {
        return entityDao.getEntityInLonely(page);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityInOffManagement()
     */
    @Override
    public List<Entity> getEntityInOffManagement() {
        return entityDao.getEntityInOffManagement();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntitySnap(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> getEntitySnap(Page p) {
        return entityDao.getEntitySnap(p);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntitySnmpSupported(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> getEntitySnmpSupported(Page p) {
        return entityDao.getEntitySnmpSupported(p);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityStateByPage(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<EntitySnap> getEntityStateByPage(Page p) {
        // return entitySnapDao.getEntityStateByPage(p);
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getSnmpEntityByType(Long)
     */
    @Override
    public List<Entity> getSnmpEntityByType(Long typeId) {
        return entityDao.getSnmpEntityByType(typeId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getSnmpEntityStateByPage(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<EntitySnap> getSnmpEntityStateByPage(Page P) {
        // return entitySnapDao.getSnmpEntityStateByPage(P);
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getSnmpParamByEntity(Long)
     */
    @Override
    public SnmpParam getSnmpParamByEntity(Long entityId) {
        return entityDao.getSnmpParamByEntityId(entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getTypeEntityStateByPage(com.topvision.framework.domain.Page,
     *      Long)
     */
    @Override
    public PageData<EntitySnap> getTypeEntityStateByPage(Page P, Long typeId) {
        // return entitySnapDao.getTypeEntityStateByPage(P, typeId);
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#handleEntitySnap(com.topvision.framework.domain.Page,
     *      java.util.Map, com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void handleEntitySnap(Page p, Map<String, String> map, MyResultHandler handler) {
        entityDao.handleEntitySnap(p, map, handler);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#queryEntity(java.util.Map)
     */
    @Override
    public List<Entity> queryEntity(Map<String, String> map) {
        return entityDao.queryEntityByFolderIdAndType(map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#queryEntity(java.util.Map,
     *      com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> queryEntity(Map<String, String> map, Page p) {
        return entityDao.queryEntity(map, p);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#queryEntityByFolderIdAndType(Long, Long,
     *      Boolean)
     */
    @Override
    public List<Entity> queryEntityByFolderIdAndType(Long folderId, Long typeId, Boolean snmpSupport) {
        Map<String, String> map = new HashMap<String, String>();
        if (folderId > 1) {
            map.put("folderId", String.valueOf(folderId));
        }
        if (typeId > 0) {
            map.put("typeId", String.valueOf(typeId));
        }
        if (snmpSupport) {
            map.put("snmpSupport", String.valueOf(snmpSupport));
        }

        return entityDao.queryEntityByFolderIdAndType(map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#queryEntityForAc(java.lang.String)
     */
    @Override
    public List<Entity> queryEntityForAc(String query) {
        return entityDao.queryEntityForAc(query);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#removeEntity(java.util.List)
     */
    @Override
    public void removeEntity(List<Long> entityIds) {
        // 触发器级联删除关联的文件夹关系
        // 手动删除连接关系
        linkDao.deleteLinkByEntityId(entityIds);
        // 外键级联删除关联的端口关系
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("deleteEntityFromRecyle:" + entityIds);
        }

        for (Long id : entityIds) {
            // add by fanzidong @20170622 为珠江数码增加在设备删除时发送相关告警的清除事件
            try {
                // 找到该设备对应的所有当前告警，
                List<Alert> alertList = alertDao.loadEntityAndSubAlert(id);
                // 清除这些告警
                for (Alert alert : alertList) {
                    try {
                        alert.setClearTime(new Timestamp(System.currentTimeMillis()));
                        alertService.txClearAlert(alert, "设备删除,清除相关告警");
                    } catch (Exception e) {
                        logger.debug("clear alert error:", e);
                    }
                }
            } catch (Exception e) {
                logger.debug("loadEntityAndSubAlert error:", e);
            }

            Entity entity = entityDao.selectByPrimaryKey(id);
            getLogger().debug("" + entityCache.size());
            entityCache.remove(id);
            getLogger().debug("" + entityCache.size());
            EntityEvent e = new EntityEvent(entity);
            e.setEntity(entity);
            e.setActionName("entityRemoved");
            e.setListener(EntityListener.class);
            messageService.fireMessage(e);
        }
        entityDao.deleteByPrimaryKey(entityIds);
        // 强制性清除entityAddress缓存
        entityAddressDao.clearCache();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#restoreEntityCurrentState(com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void restoreEntityCurrentState(MyResultHandler handler) {
        entityDao.handleEntitySnap(handler);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#txCancelManagement(java.util.List,
     *      Boolean)
     */
    @Override
    public void txCancelManagement(List<Long> entityIds, Boolean enable) {
        if (enable) {
            entityDao.enableManagement(entityIds);
        } else {
            entityDao.disableManagement(entityIds.get(0));
        }
        for (Long entityId : entityIds) {
            Entity entity = new Entity();
            entity.setEntityId(entityId);
            EntityEvent event = new EntityEvent(entity);
            event.setEntity(entity);
            event.setEnableManager(enable);
            event.setListener(EntityListener.class);
            event.setActionName("managerChanged");
            messageService.addMessage(event);
        }
    }

    /**
     * (non-Javadoc)
     * 
     */
    @Override
    public void txCreateEntity(Entity entity) throws ExistEntityException {
        if (!"0.0.0.0".equals(entity.getIp()) && entityAddressDao.selectByAddress(entity.getIp()) != null) {
            throw new ExistEntityException("Exist Entity:" + entity.getIp());
        }
        Long entityLicenseGroupId = entityTypeService.getEntityLicenseGroupIdByEntityTypeId(entity.getTypeId());
        // 判断License限制设备数量
        if (!checkEntityLimitInLicense(entityLicenseGroupId)) {
            throw new OutOfLicenseException();
        }
        Timestamp st = new Timestamp(System.currentTimeMillis());
        entity.setCreateTime(st);
        entity.setModifyTime(st);
        entity.setVirtualNetworkStatus(1);
        SnmpParam param = entity.getParam();
        snmpRetry = Byte
                .parseByte(systemPreferencesService.getModulePreferences("Snmp").getProperty("Snmp.retries", "0"));
        snmpTimeout = Integer
                .parseInt(systemPreferencesService.getModulePreferences("Snmp").getProperty("Snmp.timeout", "10000"));
        snmpPort = Integer
                .parseInt(systemPreferencesService.getModulePreferences("Snmp").getProperty("Snmp.port", "161"));
        param.setRetry(snmpRetry);
        param.setTimeout(snmpTimeout);
        param.setPort(snmpPort);
        entityDao.insertEntity(entity);
        // insert address
        EntityAddress ea = new EntityAddress();
        ea.setEntityId(entity.getEntityId());
        ea.setIp(entity.getIp());
        entityAddressDao.insertEntity(ea);
        // insert entitySnap
        EntitySnap entitySnap = new EntitySnap();
        // 初始化的时候默认不连通
        entitySnap.setState(false);
        entitySnap.setEntityId(entity.getEntityId());
        entityDao.insertEntitySnap(entitySnap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#txCreateMessage(com.topvision.ems.facade.
     * domain.Entity)
     */
    @Override
    public void txCreateMessage(Entity entity) {
        EntityEvent evt = new EntityEvent(this);
        evt.setEntity(entity);
        evt.setAction(EntityEvent.INSERTED);
        this.firedEntityAdded(evt);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#txEmptyRecyle(com.topvision.platform.domain.SystemLog)
     */
    @Override
    public void txEmptyRecyle(SystemLog sysLog) {
        List<Entity> list = entityDao.getEntityInLonely();
        int size = list == null ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            Entity entity = list.get(i);
            EntityEvent e = new EntityEvent(entity);
            e.setEntity(entity);
            e.setActionName("entityRemoved");
            e.setListener(EntityListener.class);
            messageService.fireMessage(e);
        }
        linkDao.emptyLinkByRecyle();
        entityDao.emptyRecyle();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#txMoveEntityToRecyle(java.util.List)
     */
    @Override
    public void txMoveEntityToRecyle(List<Long> entityIds) {
        for (Long id : entityIds) {
            // add by fanzidong @20170622 为珠江数码增加在设备删除时发送相关告警的清除事件
            try {
                // 找到该设备对应的所有当前告警，
                List<Alert> alertList = alertDao.loadEntityAndSubAlert(id);
                // 清除这些告警
                for (Alert alert : alertList) {
                    try {
                        alertService.txClearAlert(alert, "设备删除,清除相关告警");
                    } catch (Exception e) {
                        logger.debug("clear alert error:", e);
                    }
                }
            } catch (Exception e) {
                logger.debug("loadEntityAndSubAlert error:", e);
            }

            Entity entity = entityDao.selectByPrimaryKey(id);
            EntityEvent e = new EntityEvent(entity);
            e.setEntity(entity);
            e.setActionName("entityRemoved");
            e.setListener(EntityListener.class);
            messageService.fireMessage(e);
        }
        entityDao.moveEntityToRecyle(entityIds);
        entityAddressDao.clearCache();
    }

    /**
     * (non-Javadoc)
     * 
     */
    @Override
    public void updateEntityFixed(Entity entity) {
        entityDao.updateEntityFixed(entity);
        /*
         * Modify by Rod@EMS-5923&4620 EntityEvent evt = new EntityEvent(this);
         * evt.setEntity(entity); evt.setAction(EntityEvent.FIXED);
         * 
         * this.firedEntityUpdated(evt);
         */
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#updateEntityFixed(java.util.List)
     */
    @Override
    public void updateEntityFixed(List<Entity> entities) {
        entityDao.updateEntityFixed(entities);
    }

    /**
     * (non-Javadoc)
     * 
     */
    @Override
    public void updateEntityOutline(Entity entity) throws ExistEntityException {
        if (!"0.0.0.0".equals(entity.getIp()) && !entity.getIp().equals(entity.getOldIp())
                && entityAddressDao.selectByAddress(entity.getIp()) != null) {
            throw new ExistEntityException("Exist Entity Address:" + entity.getIp());
        }
        entityDao.updateEntityOutline(entity);
        if (!entity.getIp().equals(entity.getOldIp())) {
            entityAddressDao.updateAddress(entity.getEntityId(), entity.getIp(), entity.getOldIp());
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#updateEntityType(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void updateEntityType(Entity entity) {
        entityDao.updateEntityType(entity);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#updateEntityUrl(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void updateEntityUrl(Entity entity) {
        entityDao.updateEntityUrl(entity);
        /*
         * Modify by Rod@EMS-5923&4620 EntityEvent evt = new EntityEvent(this);
         * evt.setEntity(entity); evt.setAction(EntityEvent.URL);
         * 
         * this.firedEntityUpdated(evt);
         */
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#updateSnmpParam(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void updateSnmpParam(SnmpParam snmpParam) {
        entityDao.updateSnmpParam(snmpParam);
    }

    @Override
    public List<User> getEntityRelationUsers(Long entityId) {
        List<User> users = entityDao.getEntityRelationUsers(entityId);
        if (users != null && users.get(0).getUserId() != 0) {
            return users;
        }
        return new ArrayList<User>();
    }

    @Override
    public void updateEntityRelationUsers(Long entityId, String[] userIds) {
        entityDao.deleteRelationUser(entityId);
        List<Long> userIdList = new ArrayList<Long>();
        if (userIds != null) {
            for (int i = 0; i < userIds.length; i++) {
                userIdList.add(i, Long.valueOf(userIds[i]));
            }
            entityDao.updateRelationUser(entityId, userIdList);
        }
    }

    @Override
    public Entity selectEntityId(Long entityId, String ip) {
        return entityDao.selectEntityByIp(entityId, ip);
    }

    /**
     * @return the snmpRetry
     */
    public byte getSnmpRetry() {
        return snmpRetry;
    }

    /**
     * @param snmpRetry
     *            the snmpRetry to set
     */
    public void setSnmpRetry(byte snmpRetry) {
        this.snmpRetry = snmpRetry;
    }

    /**
     * @return the snmpPort
     */
    public int getSnmpPort() {
        return snmpPort;
    }

    /**
     * @param snmpPort
     *            the snmpPort to set
     */
    public void setSnmpPort(int snmpPort) {
        this.snmpPort = snmpPort;
    }

    /**
     * @return the snmpTimeout
     */
    public long getSnmpTimeout() {
        return snmpTimeout;
    }

    /**
     * @param snmpTimeout
     *            the snmpTimeout to set
     */
    public void setSnmpTimeout(long snmpTimeout) {
        this.snmpTimeout = snmpTimeout;
    }

    @Override
    public int loadEntitySnapCount(Map<String, Object> map) {
        Long typeWithIp = entityTypeService.getEntitywithipType();
        map.put("typeWithIp", typeWithIp.toString());
        return entityDao.loadEntitySnapCount(map);
    }

    @Override
    public List<Snap> loadEntitySnapList(Map<String, Object> map) {
        Long typeWithIp = entityTypeService.getEntitywithipType();
        map.put("typeWithIp", typeWithIp.toString());
        return entityDao.loadEntitySnapList(map);
    }

    @Override
    public List<Entity> loadEntityList(Map<String, String> map) {
        return topoFolderDao.loadEntitiesWithFolderInfo(map);
    }

    @Override
    public Long getEntityListCount(Map<String, String> map) {
        return topoFolderDao.getEntityListCount(map);
    }

    @Override
    public void cancelAttention(long userId, long entityId) {
        entityDao.cancelAttention(userId, entityId);
    }

    @Override
    public void pushAttention(long userId, long entityId) {
        entityDao.pushAttention(userId, entityId);
    }

    @Override
    public List<Snap> getAttentionEntityList(Map<String, String> map) {
        return entityDao.getAttentionEntityList(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#insertEntityAddress(com.topvision.ems.network
     * .domain.EntityAddress, java.util.List)
     */
    @Override
    public void insertEntityAddress(Long entityId, List<EntityAddress> entityAddresses) {
        entityAddressDao.deleteEntityAddressWithoutEntityIp(entityId);
        entityAddressDao.insertEntity(entityAddresses);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#topoEntityTypeId(com.topvision.framework.
     * snmp.SnmpParam)
     */
    @Override
    public EntityType topoEntityTypeId(Entity entity, SnmpParam snmpParam) {
        SnmpParam sp = snmpParam.clone();
        sp.setRetry((byte) 0);
        sp.setTimeout(2000);
        snmpPort = Integer
                .parseInt(systemPreferencesService.getModulePreferences("Snmp").getProperty("Snmp.port", "161"));
        sp.setPort(snmpPort);
        sp.setIpAddress(entity.getIp());
        try {
            String sysObjectID = facadeFactory.getFacade(entity.getIp(), DiscoveryFacade.class)
                    .discoverySysObjectID(sp);
            EntityType entityType = entityTypeService.getEntityTypeBySysObjId(sysObjectID);
            if (entityType == null) {
                return entityTypeService.getEntityType(EntityType.UNKNOWN_TYPE);
            } else {
                return entityType;
            }
        } catch (Exception e) {
            return entityTypeService.getEntityType(EntityType.UNKNOWN_TYPE);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#topoEntitySysName(com.topvision.ems.facade.
     * domain.Entity, com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public String topoEntitySysName(Entity entity, SnmpParam snmpParam) {
        try {
            return facadeFactory.getFacade(entity.getIp(), DiscoveryFacade.class).discoverySysName(snmpParam);
        } catch (Exception e) {
            logger.error("topoEntitySysName error", e);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityJson()
     */
    @Override
    public List<Entity> getEntityJson() {
        List<Entity> entities = entityDao.selectEntityFromView();
        return entities;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#getUserAuthorityEntity(com.topvision.platform
     * .domain.User)
     */
    @Override
    public List<Entity> getUserAuthorityEntity(User user) {
        String authorityView = TABLE_NAME_PRE + user.getUserGroupId();
        if (user.getUserName().equals(ADMIN_USER)) {
            authorityView = ENTITY;
        }
        return entityDao.getUserAuthorityEntity(authorityView);
    }

    @Override
    public String getDeviceVersion(Long entityId) {
        Entity entity = getEntity(entityId);
        return getDeviceVersion(entity);
    }

    @Override
    public String getDeviceVersion(Entity entity) {
        if (entity != null && (entityTypeService.isOlt(entity.getTypeId()))) {
            // OLT 设备
            return entityDao.getDeviceVersion(entity.getEntityId());
        } else {
            // TODO 扩展接口，预留给其它设备类型
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#updateEntityIpInfo(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public String updateEntityIpInfo(Long entityId, String oldIp, String newIp) {
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        // 修改Ip存在三种情况
        // 1、修改的IP在entityAddress表中不存在，更新entityAddress、entity表中的IP字段
        // 2、修改的IP在entityAddress表存在并且是本设备其它接口的IP，只更新entity表中的IP字段
        // 3、修改的IP在entityAddress表存在并且不是本设备的接口IP，提示IP已经存在，不能做修改
        // 修改EntityAddress表
        List<EntityAddress> entityAddresses = entityAddressDao.selectByAddressList(newIp);
        if (entityAddresses.size() == 0) {// 第一种场景 entityAddress表中不存在此IP
            entityAddressDao.updateAddress(entityId, newIp, entity.getIp());
            // 修改Entity表
            entityDao.updateEntityIpAddress(entityId, newIp);
            return "success";
        } else if (entityAddresses.size() == 1) {
            if (entityAddresses.get(0).getEntityId().equals(entityId)) {
                entityDao.updateEntityIpAddress(entityId, newIp);
                return "success";
            } else {
                return "ipExistDevice";
            }
        } else {
            return "ipExistMore";
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#selectEntityAddressList(java.lang.String)
     */
    @Override
    public List<EntityAddress> selectEntityAddressList(String ip) {
        return entityAddressDao.selectByAddressList(ip);
    }

    @Override
    public void modifyEntityInfo(Long entityId, String name, String location, String contact, String note) {
        entityDao.modifyEntityInfo(entityId, name, location, contact, note);
    }

    @Override
    public void renameEntity(Long entityId, String name) {
        entityDao.renameEntity(entityId, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#updateEntityAddress(java.lang.Long)
     */
    @Override
    public void updateEntityAddress(Long entityId) {
        SnmpParam snmpParam = getSnmpParamByEntity(entityId);
        List<IpAddressTable> ipAddressTables = facadeFactory.getFacade(snmpParam.getIpAddress(), DiscoveryFacade.class)
                .discoveryIpTableInfo(snmpParam);
        List<EntityAddress> addresses = new ArrayList<EntityAddress>();
        for (IpAddressTable tmp : ipAddressTables) {
            EntityAddress address = new EntityAddress();
            address.setEntityId(entityId);
            String ipAddress = tmp.getIpAdEntAddr();
            if (!ipAddress.equals("127.0.0.1")) {
                address.setIp(ipAddress);
                addresses.add(address);
            }
        }
        // 需要保证INSERT异常仍然执行成功
        entityAddressDao.deleteEntityAddressWithoutEntityIp(entityId);
        for (EntityAddress address : addresses) {
            try {
                entityAddressDao.insertEntity(address);
            } catch (Exception e) {
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityByTypeId(java.lang.Long)
     */
    @Override
    public List<Entity> getEntityByTypeId(Long typeId) {
        return entityDao.getEntityByTypeId(typeId);
    }

    @Override
    public List<Entity> getCentralEntity() {
        return entityDao.selectCentralEntity();
    }

    @Override
    public String cmd(String sessionId, String cmd, String ip) {
        CmdFacade cmdFacade;
        if (ip == null) {
            cmdFacade = cmdCaches.get(sessionId);
        } else {
            cmdFacade = facadeFactory.getFacade(ip, CmdFacade.class);
            cmdCaches.put(sessionId, cmdFacade);
        }
        if (cmdFacade == null) {
            return null;
        }
        if (cmd == null) {
            String r = cmdFacade.getResult();
            if (r != null && r.endsWith("#OK#")) {
                cmdCaches.remove(sessionId);
            }
            return r;
        } else if (cmd.equals("ping")) {
            // TODO 获取工具ping的参数
            Properties pingProperty = systemPreferencesService.getModulePreferences("toolPing");
            Integer pingCount = Integer.parseInt(pingProperty.getProperty("Ping.count"));
            pingCount = pingCount != null ? pingCount : 4;
            Integer pingTimeout = Integer.parseInt(pingProperty.getProperty("Ping.timeout"));
            pingTimeout = pingTimeout != null ? pingTimeout : 3000;
            return cmdFacade.ping(ip, pingTimeout, pingCount);
        } else if (cmd.equals("tracert") || cmd.equals("traceroute")) {
            return cmdFacade.tracert(ip);
        } else {
            String r = cmdFacade.getResult();
            if (r != null && r.endsWith("#OK#")) {
                cmdCaches.remove(sessionId);
            }
            return r;
        }
    }

    @Override
    public List<Long> getEntityIdsByAuthority(Long type) {
        return entityDao.getEntityIdsByAuthority(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#checkEntityLimitInLicense(java.lang.Long)
     */
    @Override
    public boolean checkEntityLimitInLicense(Long type) {
        int count = entityDao.selectEntityIdsByType(type).size();
        Module lm = licenseService.getLicenseModule(type);
        if (lm != null) {
            if (count + 1 > Integer.valueOf(lm.getNumberOfEntities())) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#getEntityIpList(java.lang.Long)
     */
    @Override
    public List<String> getEntityIpList(Long entityId) {
        // modify by lzt
        // 删除组播地址224.0.0.5 224.0.0.6 程序中直接判断非 D E类地址
        // 删除内部管理地址192.168.254.*
        List<String> ips = entityDao.getEntityIpListById(entityId);
        List<String> ipList = new ArrayList<String>();
        for (String ip : ips) {
            if (!IpUtils.D_CLASS.equals(IpUtils.getIpClass(ip)) && !IpUtils.E_CLASS.equals(IpUtils.getIpClass(ip))
                    && ip.indexOf("192.168.254") == -1 && ip.indexOf("0.0.0.0") == -1) {
                ipList.add(ip);
            }
        }
        return ipList;
    }

    @Override
    public List<Entity> getEntityListByType(Long type) {
        return entityDao.getEntityListByType(type);
    }

    @Override
    public EntitySnap getEntitySnapById(Long entityId) {
        return entityDao.getEntitySnapById(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#applySnmpConfig(java.lang.Integer,
     * java.lang.Integer, java.lang.Integer)
     */
    @Override
    public void applySnmpConfig(Integer timeout, byte retry, Integer port) {
        SnmpParam snmpParam = new SnmpParam();
        snmpParam.setTimeout(timeout);
        snmpParam.setRetry(retry);
        snmpParam.setPort(port);
        entityDao.updateSnmpConfig(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#getEntityWithMacAndTypeId(java.lang.String,
     * java.lang.Long)
     */
    @Override
    public Entity getEntityWithMacAndTypeId(String macAddress, Long typeId) {
        return entityDao.getEntityWithMacAndTypeId(macAddress, typeId);
    }

    @Override
    public List<Entity> getEntityListWithMacAndTypeId(String macAddress, Long typeId) {
        return entityDao.getEntityListWithMacAndTypeId(macAddress, typeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityService#replaceEntity(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void replaceEntity(Long preEntityId, String ip) {
        entityDao.replaceEntity(preEntityId, ip);
        entityAddressDao.clearCache();

        // 发出entityReplaced事件
        Entity entity = getEntity(preEntityId);

        EntityEvent event = new EntityEvent(entity);
        event.setEntity(entity);
        event.setActionName("entityReplaced");
        event.setListener(EntityListener.class);
        messageService.addMessage(event);
    }

    private Map<Long, Entity> entityCache = new HashMap<Long, Entity>();

    @Override
    public void updateEntityDeviceName(Long entityId, String deviceName) {
        entityDao.updateEntityDeviceName(entityId, deviceName);
    }

    @Override
    public void updateSnapSysUptime(Long entityId, Long sysUptime) {
        entityDao.updateSnapSysUptime(entityId, sysUptime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityService#updateEntityLastRefreshTime(java.lang.Long,
     * java.sql.Timestamp)
     */
    @Override
    public void updateEntityLastRefreshTime(Long entityId, Timestamp lastRefreshTime) {
        entityDao.updateEntityLastRefreshTime(entityId, lastRefreshTime);
    }

    @Override
    public void setEntityLocatedFolders(Long userId, Long entityId, List<Long> folderList) {
        List<TopoFolder> list = topologyService.loadMyTopoFolder(userId, FolderCategory.CLASS_NETWORK);
        List<Long> authFolderIds = new ArrayList<Long>();
        for (TopoFolder folder : list) {
            authFolderIds.add(Long.valueOf(folder.getId()));
            topologyService.refreshTopoFolder(Long.valueOf(folder.getId()));
        }
        entityDao.setEntityLocatedFolders(entityId, folderList, authFolderIds);
    }

    @Override
    public void setEntitiesLocatedFolders(long userId, List<Long> entityIds, List<Long> folderList) {
        // 获取当前用户有权限的所有地域列表
        List<TopoFolder> list = topologyService.loadMyTopoFolder(userId, FolderCategory.CLASS_NETWORK);
        List<Long> authFolderIds = new ArrayList<Long>();
        for (TopoFolder folder : list) {
            authFolderIds.add(Long.valueOf(folder.getId()));
        }
        entityDao.setEntitiesLocatedFolders(entityIds, folderList, authFolderIds);
    }

    @Override
    public void addEntitiesToFolders(List<Long> entityIds, List<Long> folderIds) {
        entityDao.addEntitiesToFolders(entityIds, folderIds);
    }

    @Override
    public void removeEntitiesFromFolders(List<Long> entityIds, List<Long> folderIds) {
        entityDao.removeEntitiesFromFolders(entityIds, folderIds);
    }

    @Override
    public Boolean canRemoveEntityFromFolder(Long entityId, List<Long> removedFolderIds) {
        List<Long> folderIds = topoFolderDao.getEntityLocatedFolderIds(entityId);
        // 判断该设备所在的地域是不是都在被选中删除地域中，如果在，则表示不能删除
        boolean ret = false;
        for (Long folderId : folderIds) {
            if (!removedFolderIds.contains(folderId)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    @Override
    public List<String> getEntityNamesByIds(List<Long> entityIds) {
        if (entityIds == null || entityIds.size() == 0) {
            return null;
        }
        return topoFolderDao.getEntityNamesByIds(entityIds);
    }

    @Override
    public SnmpParam getCmSnmpParam() {
        return null;
    }

    @Override
    public void modifyCmSnmpParam(SnmpParam snmpParam) {
    }

    @Override
    public Map<Long, Entity> getEntityCaches() {
        return entityDao.getEntityCaches();
    }

    @Override
    public Entity getEntityFromCache(Long entityId) {
        return entityDao.selectByPrimaryKey(entityId);
    }

    @Override
    public Map<Long, SnmpParam> getSnmpParamCaches() {
        return entityDao.getSnmpParamCaches();
    }

    @Override
    public void updateEntitySnap(EntitySnap entitySnap) {
        entityDao.updateEntitySnap(entitySnap);
    }

    @Override
    public List<Snap> loadUserAttentionList(Map<String, Object> map) {
        return entityDao.loadUserAttentionList(map);
    }

    @Override
    public int userAttentionCount(Map<String, Object> map) {
        return entityDao.userAttentionCount(map);
    }

    @Override
    public Entity getEntityFromDB(Long entityId) {
        return entityDao.selectEntityFromDB(entityId);
    }

    @Override
    public PingResultsEntry snmpPing(Long entityId, String ip, Integer pingCount, Integer pingTimeout) {
        Properties pingProperty = systemPreferencesService.getModulePreferences("toolPing");
        if (pingCount == null) {
            pingCount = Integer.parseInt(pingProperty.getProperty("Ping.count"));
        }
        if (pingTimeout == null) {
            pingTimeout = Integer.parseInt(pingProperty.getProperty("Ping.timeout"));
        }
        PingFacade pingFacade = facadeFactory.getFacade(PingFacade.class);
        SnmpParam snmpParam = getSnmpParamByEntity(entityId);
        if (snmpParam == null) {
            Entity entity = getEntity(entityId);
            snmpParam = getSnmpParamByEntity(entity.getParentId());
        }
        PingResultsEntry entry = pingFacade.snmpPing(snmpParam, entityId, ip, null, pingTimeout, pingCount);
        return entry;
    }

    @Override
    public List<Entity> getEntityWithIp() {
        return entityDao.getEntityWithIp();
    }

    @Override
    public Long getEntityIndexOfOlt(Long entityId) {
        return entityDao.getEntityIndexOfOlt(entityId);
    }

    @Override
    public void modifyEntityContactAndLocation(Long cmcId, String ccSysLocation, String ccSysContact) {
        entityDao.modifyEntityContactAndLocation(cmcId, ccSysLocation, ccSysContact);
    }

    @Override
    public String getOnuEorG(Long entityId) {
        return entityDao.getOnuEorG(entityId);
    }

    @Override
    public Integer getOnuLevel(Long entityId) {
        return entityDao.getOnuLevel(entityId);
    }

}
