package com.topvision.ems.network.dao.mybatis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.Snap;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntityFolderRela;
import com.topvision.ems.network.domain.EntityImport;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.exception.service.OutOfLicenseException;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.User;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.StringUtil;

@Repository("entityDao")
public class EntityDaoImpl extends MyBatisDaoSupport<Entity> implements EntityDao {
    private final Logger logger = LoggerFactory.getLogger(EntityDaoImpl.class);
    private ConcurrentHashMap<Long, Entity> entityCaches = new ConcurrentHashMap<Long, Entity>();
    private ConcurrentHashMap<Long, SnmpParam> snmpParamCaches = new ConcurrentHashMap<Long, SnmpParam>();

    @Override
    public Entity selectByPrimaryKey(Long entityId) {
        if (entityId == null) {
            return null;
        }
        if (!entityCaches.containsKey(entityId)) {
            Entity entity = getSqlSession().selectOne(getNameSpace("selectByPrimaryKey"), entityId);
            if (entity == null) {
                return null;
            }
            entityCaches.put(entityId, entity);
        }
        return entityCaches.get(entityId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#deleteEntity(java.util.List)
     */
    @Override
    public void deleteEntity(final List<Entity> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entities.size();
            List<Long> relateEntityIds = new ArrayList<Long>();
            for (int i = 0; i < size; i++) {
                // 找到对应设备的关联设备Id
                relateEntityIds.addAll(getEntityIdsUnderEntity(entities.get(i).getEntityId()));
                // 删除该设备
                sqlSession.update(getNameSpace() + "deleteEntity", entities.get(i));
            }
            sqlSession.commit();
            // 删掉该设备及下级设备缓存
            if (relateEntityIds != null && relateEntityIds.size() > 0) {
                for (Long entityId : relateEntityIds) {
                    entityCaches.remove(entityId);
                }
                for (Long entityId : relateEntityIds) {
                    snmpParamCaches.remove(entityId);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        List<Long> entityIds = new ArrayList<Long>();
        for (Entity entity : entities) {
            entityIds.add(entity.getEntityId());
        }
        reOrganizedAuthority(entityIds);
    }

    private List<Long> getEntityIdsUnderEntity(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getEntityIdsUnderEntity"), entityId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#disableManagement(Long)
     */
    @Override
    public void disableManagement(Long entityId) {
        Entity entity = new Entity();
        entity.setEntityId(entityId);
        entity.setOffManagementTime(System.currentTimeMillis());
        getSqlSession().update(this.getNameSpace() + "disableManagement", entity);
        entityCaches.remove(entityId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#emptyRecyle()
     */
    @Override
    public void emptyRecyle() {
        getSqlSession().update(this.getNameSpace() + "emptyRecyle");
        entityCaches.clear();
        snmpParamCaches.clear();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#enableManagement(java.util.List)
     */
    @Override
    public void enableManagement(final List<Long> entityIds) {
        if (entityIds == null || entityIds.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entityIds.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "enableManagement", entityIds.get(i));
            }
            sqlSession.commit();
            for (int i = 0; i < size; i++) {
                entityCaches.remove(entityIds.get(i));
            }
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityAgentSupported(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> getEntityAgentSupported(Page p) {
        return selectByMap(getNameSpace() + "getEntityCountAgentSupported", getNameSpace() + "getEntityAgentSupported",
                null, p);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityByIp(java.util.List)
     */
    @Override
    public List<Entity> getEntityByIp(List<String> ips) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ips.size(); i++) {
            sb.append(i == 0 ? "'" : ", '");
            sb.append(ips.get(i));
            sb.append("'");
        }
        return getSqlSession().selectList(getNameSpace("getEntityByIp"), sb.toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getSubEntityByEntityId(java.lang. Long)
     */
    @Override
    public List<Entity> getSubEntityByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getSubEntityByEntityId"), entityId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityByType(Long)
     */
    @Override
    public List<Entity> getEntityByType(Long type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getEntityByType2"), map);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityByType(com.topvision.framework.domain.Page,
     *      Long)
     */
    @Override
    public PageData<Entity> getEntityByType(Page p, Long type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", String.valueOf(type));
        return selectByMap(getNameSpace() + "getEntityCountByType", getNameSpace() + "getEntityByType", map, p);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityCoordByFolderId(Long)
     */
    @Override
    public List<Entity> getEntityCoordByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getEntityCoordByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityCount()
     */
    @Override
    public Long getEntityCount() {
        return getSqlSession().selectOne(getNameSpace("getEntityCount"));
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityByFolderId(java.util.List, Long)
     */
    @Override
    public List<Entity> getEntityByFolderId(List<Long> ids, Long folderId) {
        // List<Long> folders =
        // super.getSqlMapClientTemplate().queryForList(this.getNameSpace() +
        // "getEntityBySubFolder",
        // folderId);
        // if (folders == null) {
        // folders = new ArrayList<Long>();
        // }
        // folders.add(folderId);
        if (ids == null || ids.size() == 0) {
            return null;
        }
        String s = ids.toString();
        s = s.substring(1, s.length() - 1);
        Map<String, String> map = new HashMap<String, String>();
        map.put("ids", s);
        // s = folders.toString();
        // s = s.substring(1, s.length() - 1);
        map.put("folderIds", "" + folderId);
        return getSqlSession().selectList(getNameSpace("getEntityByFolderAndEntityId"), map);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityByFolderId(Long, java.lang.String)
     */
    @Override
    public List<Entity> getEntityByFolderId(Long folderId, String sortName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("folderId", String.valueOf(folderId));
        map.put("sortName", sortName);
        return getSqlSession().selectList(getNameSpace("getEntityByFolderId"), map);
    }

    @Override
    public List<Entity> getAllEntityByFolderId(Long folderId) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        String folderString = "t_entity_" + folderId.toString();
        authorityHashMap.put("Authority", folderString);
        return getSqlSession().selectList(getNameSpace("getAllEntityByFolderId"), authorityHashMap);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityByIp(java.lang.String)
     */
    @Override
    public Entity getEntityByIp(String ip) {
        return getSqlSession().selectOne(getNameSpace("getEntityByIp"), ip);
    }

    /**
     * (non-Javadoc)
     */
    @Override
    public Entity getEntityInFolder(Entity entity) {
        return getSqlSession().selectOne(getNameSpace("getEntityInFolder"), entity);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityIdByFolderId(Long)
     */
    @Override
    public List<Long> getEntityIdByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getEntityIdByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityInLonely()
     */
    @Override
    public List<Entity> getEntityInLonely() {
        return getSqlSession().selectList(getNameSpace("getAllEntityInLonely"));
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityInLonely(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> getEntityInLonely(Page page) {
        return super.selectByMap(getNameSpace() + "getEntityCountInLonely", getNameSpace() + "getEntityInLonely", null,
                page);
    }

    /**
     * (non-Javadoc)
     */
    @Override
    public List<Entity> getEntityInOffManagement() {
        return getSqlSession().selectList(getNameSpace("getEntityInOffManagement"));
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntitySnap(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> getEntitySnap(Page page) {
        return super.selectByMap(getNameSpace() + "countEntitySnap", getNameSpace() + "getEntitySnapList", null, page);
    }

    @Override
    public EntitySnap getEntitySnapById(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getEntitySnapById"), entityId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntitySnmpSupported(com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> getEntitySnmpSupported(Page p) {
        return selectByMap(getNameSpace() + "getEntityCountSnmpSupported", getNameSpace() + "getEntitySnmpSupported",
                null, p);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getSnmpEntityByType(Long)
     */
    @Override
    public List<Entity> getSnmpEntityByType(Long typeId) {
        return getSqlSession().selectList(getNameSpace("getSnmpEntityByType"), typeId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getSnmpParamByEntityId(Long)
     */
    @Override
    public SnmpParam getSnmpParamByEntityId(Long entityId) {
        if (!snmpParamCaches.containsKey(entityId)) {
            SnmpParam snmpParam = getSqlSession().selectOne(getNameSpace("selectSnmpParam"), entityId);
            snmpParamCaches.put(entityId, snmpParam);
        }
        return snmpParamCaches.get(entityId);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#handleEntitySnap(com.topvision.framework.domain.Page,
     *      java.util.Map, com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void handleEntitySnap(Page page, Map<String, String> map, MyResultHandler handler) {
        try {
            super.selectWithRowHandler(getNameSpace() + "countEntitySnap", getNameSpace() + "getEntitySnapList", map,
                    page, handler);
        } catch (TransientDataAccessException e) {
            // 如果发生snapTime转换异常，则不取字段snapTime
            super.selectWithRowHandler(getNameSpace() + "countEntitySnap", getNameSpace()
                    + "getEntitySnapListException", map, page, handler);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#handleEntitySnap(com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void handleEntitySnap(MyResultHandler handler) {
        super.selectWithRowHandler(getNameSpace() + "getAllEntitySnap", handler);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#insertEntity(java.lang.Object)
     */
    @Override
    public void insertEntity(Entity entity) throws OutOfLicenseException {
        // add by fanzidong,需要在入库之前格式化MAC地址
        String formatedMac = MacUtils.convertToMaohaoFormat(entity.getMac());
        entity.setMac(formatedMac);
        entity.setName(StringUtil.stripSpecialChar(entity.getName()));
        getSqlSession().insert(getNameSpace() + "insertEntity", entity);
        // TODO update entityfolderrela
        if (entity.getFolderId() != null) {
            getSqlSession().update(getNameSpace() + "updateEntityFolderRela", entity);
        }
        Long entityId = entity.getEntityId();
        getSqlSession().insert(getNameSpace() + "insertSnmpParam", entity.getParam());
        // 更新权限表
        try {
            Thread.sleep(1000);
            List<Long> entityIds = new ArrayList<Long>();
            entityIds.add(entityId);
            reOrganizedAuthority(entityIds);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
        }
    }

    /**
     * Add by Rod 只用于添加ONU的时候使用 (non-Javadoc)
     *
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#insertEntity(java.lang.Object)
     */
    @Override
    public Long insertEntityWithId(Entity entity, Integer onuOperationStatus) throws OutOfLicenseException {
        // add by fanzidong,需要在入库之前格式化MAC地址
        String formatedMac = MacUtils.convertToMaohaoFormat(entity.getMac());
        entity.setMac(formatedMac);
        getSqlSession().insert(getNameSpace() + "insertEntity", entity);
        // insert entitySnap
        EntitySnap entitySnap = new EntitySnap();
        entitySnap.setState(onuOperationStatus == 1 ? true : false);
        entitySnap.setEntityId(entity.getEntityId());
        getSqlSession().insert(getNameSpace() + "insertEntitySnap", entitySnap);
        Long entityId = entity.getEntityId();
        return entityId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#syncOnuAuthority(java.util.List)
     */
    @Override
    public void syncOnuAuthority(List<Long> entityIds) {
        reOrganizedAuthority(entityIds);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#insertEntity(java.util.List)
     */
    @Override
    public void insertEntity(final List<Entity> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                // add by fanzidong,需要在入库之前格式化MAC地址
                String formatedMac = MacUtils.convertToMaohaoFormat(entities.get(i).getMac());
                entities.get(i).setMac(formatedMac);
                sqlSession.insert(getNameSpace() + "insertEntity", entities.get(i));
                sqlSession.insert(getNameSpace() + "insertSnmpParam", entities.get(i).getParam());
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#moveEntity(Long, Long, java.util.List)
     */
    @Override
    public void moveEntity(final Long srcFolderId, final Long destFolderId, final List<Long> entityIds) {
        if (entityIds == null || entityIds.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entityIds.size();
            for (int i = 0; i < size; i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("srcFolderId", String.valueOf(srcFolderId));
                map.put("destFolderId", String.valueOf(destFolderId));
                map.put("entityId", String.valueOf(entityIds.get(i)));
                sqlSession.update(getNameSpace() + "moveEntityById", map);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        reOrganizedAuthority(entityIds);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#moveEntityToRecyle(java.util.List)
     */
    @Override
    public void moveEntityToRecyle(final List<Long> entityIds) {
        if (entityIds == null || entityIds.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entityIds.size();
            List<Long> relateEntityIds = new ArrayList<Long>();
            for (int i = 0; i < size; i++) {
                // add by fanzidong 找到对应设备的关联设备Id
                relateEntityIds.addAll(getEntityIdsUnderEntity(entityIds.get(i)));
                // NM3000框架改进后,不再放入回收站，而是直接删除。拓扑图只是删除关系 author:bravin
                // 删除entity表中的记录
                sqlSession.update(getNameSpace() + "removeEntity", entityIds.get(i));
                // 删除entity-folder-relation
                sqlSession.update(getNameSpace() + "moveEntityToRecyle", entityIds.get(i));
            }
            sqlSession.commit();
            // 删掉该设备及下级设备缓存
            if (relateEntityIds != null && relateEntityIds.size() > 0) {
                for (Long entityId : relateEntityIds) {
                    entityCaches.remove(entityId);
                }
                for (Long entityId : relateEntityIds) {
                    snmpParamCaches.remove(entityId);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        reOrganizedAuthority(entityIds);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#queryEntity(java.util.Map,
     *      com.topvision.framework.domain.Page)
     */
    @Override
    public PageData<Entity> queryEntity(Map<String, String> map, Page page) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return super.selectByMap(getNameSpace() + "queryCountEntity", getNameSpace() + "queryEntity", map, page);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#queryEntityByFolderIdAndType(java.util.Map)
     */
    @Override
    public List<Entity> queryEntityByFolderIdAndType(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("queryEntityByFolderIdAndType"), map);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#queryEntityForAc(java.lang.String)
     */
    @Override
    public List<Entity> queryEntityForAc(String query) {
        return getSqlSession().selectList(getNameSpace("queryEntityForAc"), query);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#updateEntity(java.lang.Object)
     */
    @Override
    public void updateEntity(Entity obj) {
        // add by fanzidong,需要在入库之前格式化MAC地址
        String mac = MacUtils.convertToMaohaoFormat(obj.getMac());
        obj.setMac(mac);
        getSqlSession().update(getNameSpace("updateEntity"), obj);
        entityCaches.remove(obj.getEntityId());
        // Modify by Rod 设备刷新后不更新SNMP超时重试等信息
        // this.updateSnmpParam(obj.getParam());
        this.updateSnmpParamMibs(obj.getParam());
    }

    private void updateSnmpParamMibs(SnmpParam snmpParam) {
        getSqlSession().update(getNameSpace("updateSnmpParamMibs"), snmpParam);
        snmpParamCaches.remove(snmpParam.getEntityId());
    }

    /**
     * (non-Javadoc)
     */
    @Override
    public void updateEntityFixed(Entity entity) {
        getSqlSession().update(this.getNameSpace() + "updateEntityFixed", entity);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityFixed(java.util.List)
     */
    @Override
    public void updateEntityFixed(final List<Entity> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "updateEntityFixed", entities.get(i));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * (non-Javadoc)
     */
    @Override
    public void updateEntityGroup(Entity entity) {
        getSqlSession().update(this.getNameSpace() + "updateEntityGroup", entity);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityIcon(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void updateEntityIcon(Entity entity) {
        getSqlSession().update(this.getNameSpace() + "updateEntityIcon", entity);
        entityCaches.remove(entity.getEntityId());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityOutline(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void updateEntityOutline(Entity entity) {
        getSqlSession().update(this.getNameSpace() + "updateEntityOutline", entity);
        entityCaches.remove(entity.getEntityId());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityType(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void updateEntityType(Entity entity) {
        getSqlSession().update(this.getNameSpace() + "updateEntityType", entity);
        entityCaches.remove(entity.getEntityId());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityUrl(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void updateEntityUrl(Entity entity) {
        getSqlSession().update(this.getNameSpace() + "updateEntityUrl", entity);
        entityCaches.remove(entity.getEntityId());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateSnmpParam(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void updateSnmpParam(SnmpParam snmpParam) {
        getSqlSession().update(this.getNameSpace() + "updateSnmpParam", snmpParam);
        snmpParamCaches.put(snmpParam.getEntityId(), selectSnmpParamFromDB(snmpParam.getEntityId()));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#insertEntity(java .util.List)
     */
    @Override
    public void txMoveEntity(final List<Entity> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                sqlSession.insert(getNameSpace() + "entity2Folder", entities.get(i));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        List<Long> entityIds = new ArrayList<Long>();
        for (Entity entity : entities) {
            entityIds.add(entity.getEntityId());
        }
        reOrganizedAuthority(entityIds);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#copyEntity(com.topvision.ems.
     * facade.domain.Entity)
     */
    @Override
    public void copyEntity(Entity entity) {
        getSqlSession().insert(getNameSpace() + "entity2Folder", entity);
        List<Long> entityIds = new ArrayList<Long>();
        entityIds.add(entity.getEntityId());
        reOrganizedAuthority(entityIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#copyEntity(java.util.List)
     */
    @Override
    public void copyEntity(final List<Entity> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                sqlSession.insert(getNameSpace() + "entity2Folder", entities.get(i));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        List<Long> entityIds = new ArrayList<Long>();
        for (Entity entity : entities) {
            entityIds.add(entity.getEntityId());
        }
        reOrganizedAuthority(entityIds);
    }

    @Override
    public List<User> getEntityRelationUsers(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getEntityRelationUsers"), entityId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#deleteRelationUser(java.lang. Long)
     */
    @Override
    public void deleteRelationUser(Long entityId) {
        getSqlSession().update(this.getNameSpace() + "deleteEntityRelationUsers", entityId);
    }

    @Override
    public void updateRelationUser(final Long entityId, final List<Long> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = userIds.size();
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < size; i++) {
                map.put("entityId", String.valueOf(entityId));
                map.put("userId", String.valueOf(userIds.get(i)));
                sqlSession.insert(getNameSpace() + "insertEntityRelationUsers", map);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityPerf(com.topvision
     * .ems.network.domain .EntitySnap)
     */
    @Override
    public void updateEntityPerf(EntitySnap snap) {
        getSqlSession().update(getNameSpace() + "updateEntityPerf", snap);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#renameEntity(java.lang.Long, java.lang.String)
     */
    @Override
    public void modifyEntityInfo(Long entityId, String name, String location, String contact, String note) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("name", name);
        params.put("location", location);
        params.put("contact", contact);
        params.put("note", note);
        getSqlSession().update(getNameSpace() + "modifyDeviceInfo", params);
        synchronized (entityCaches) {
            // entityCaches.put(entityId, selectEntityFromDB(entityId));
            entityCaches.remove(entityId);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#renameEntity(java.lang.Long, java.lang.String)
     */
    @Override
    public void renameEntity(Long entityId, String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("name", name);
        getSqlSession().update(getNameSpace() + "renameEntity", params);
        entityCaches.remove(entityId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getRefreshStatus(java.lang.Long)
     */
    @Override
    public Integer getRefreshStatus(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getRefreshStatus"), entityId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateRefreshStatus(java.lang .Integer)
     */
    @Override
    public void updateRefreshStatus(Long entityId, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("status", status);
        getSqlSession().update(getNameSpace() + "updateRefreshStatus", params);
        entityCaches.remove(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#resetRefreshStatus()
     */
    @Override
    public void resetRefreshStatus() {
        getSqlSession().update(getNameSpace() + "resetRefreshStatus");
        entityCaches.clear();
    }

    @Override
    public int loadEntitySnapCount(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("loadEntitySnapCount"), map);
    }

    @Override
    public List<Snap> loadEntitySnapList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("loadEntitySnapList"), map);
    }

    @Override
    public void cancelAttention(final long userId, final long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("userId", userId + "");
            map.put("entityId", entityId + "");
            sqlSession.delete(getNameSpace() + "cancelAttention", map);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void pushAttention(final long userId, final long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("userId", userId + "");
            map.put("entityId", entityId + "");
            sqlSession.delete(getNameSpace() + "cancelAttention", map);
            sqlSession.delete(getNameSpace() + "pushAttention", map);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<Snap> getAttentionEntityList(Map<String, String> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getAttentionEntityList"), map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#insertEntitySnap(com.topvision.
     * ems.network.domain .EntitySnap)
     */
    @Override
    public void insertEntitySnap(EntitySnap entitySnap) {
        getSqlSession().insert(getNameSpace() + "insertEntitySnap", entitySnap);
    }

    @Override
    public void updateEntitySnap(EntitySnap entitySnap) {
        EntitySnap snap = getEntitySnapById(entitySnap.getEntityId());
        if (snap == null) {
            getSqlSession().insert(getNameSpace() + "insertEntitySnap", entitySnap);
        } else {
            getSqlSession().update(getNameSpace() + "updateEntitySnap", entitySnap);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.VirtualNetDao#isProductExistsInTopo(java. lang.Long,
     * java.lang.Long)
     */
    @Override
    public boolean isProductExistsInTopo(Long productId, Long productType, Long folderId) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("productId", productId);
        params.put("productType", productType);
        params.put("folderId", folderId);
        Long count = getSqlSession().selectOne(getNameSpace("isProductExistsInTopo"), params);
        return count > 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateUnSupportEntityType(java. lang.Long)
     */
    @Override
    public void updateEntityType(Long entityId, Long typeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("typeId", typeId);
        getSqlSession().update(getNameSpace() + "updateEntityTypeAndTypeId", params);
        entityCaches.remove(entityId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#selectEntityFromView(java.lang. String)
     */
    @Override
    public List<Entity> selectEntityFromView() {
        String viewName = CurrentRequest.getUserAuthorityViewName();
        return getSqlSession().selectList(getNameSpace("selectEntityFromView"), viewName);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getUserAuthorityEntity(java.lang. String)
     */
    @Override
    public List<Entity> getUserAuthorityEntity(String authorityView) {
        return getSqlSession().selectList(getNameSpace("getUserAuthorityEntity"), authorityView);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getDeviceVersion(java.lang.Long,
     * java.lang.Integer)
     */
    @Override
    public String getDeviceVersion(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getDeviceVersion"), entityId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityIpAddress(java.lang. Long,
     * java.lang.String)
     */
    @Override
    public void updateEntityIpAddress(Long entityId, String ip) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("ipAddress", ip);
        getSqlSession().update(getNameSpace() + "updateEntityIpAddress", params);
        entityCaches.remove(entityId);
        snmpParamCaches.remove(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityAddressTable(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void updateEntityAddressTable(Long entityId, String ip) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("ip", ip);
        getSqlSession().update(getNameSpace() + "updateEntityAddressTable", params);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#selectEntityByIp(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public Entity selectEntityByIp(Long entityId, String ip) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("ip", ip);
        return getSqlSession().selectOne(getNameSpace("getEntityByIpAndId"), params);
    }

    @Override
    public Entity selectEntityBySingleIp(String ip) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ip", ip);
        return getSqlSession().selectOne(getNameSpace("getEntityBySingleIp"), params);
    }

    /**
     * 权限管理核心方法 重构所有地域设备表
     *
     * @param entityIds
     */
    @Override
    public void reOrganizedAuthority(List<Long> entityIds) {
        List<String> tList = getSqlSession().selectList(getNameSpace() + "getAuthotityTable");
        for (Long entityId : entityIds) {
            // 第一步删除所有权限表中关于此设备的记录
            for (String tableName : tList) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("entityId", entityId);
                params.put("tableName", tableName);
                getSqlSession().delete(getNameSpace() + "deleteAuthorityByEntityId", params);
            }
            List<Long> relas = getSqlSession().selectList(getNameSpace() + "getEntityFolderRelaListByEntityId",
                    entityId);
            for (String tableName : tList) {
                // 循环每个地域，递归去重组权限表
                Integer folderId = Integer.parseInt(tableName.substring("t_entity_".length()));
                String nextFolderString = (String) getSqlSession().selectOne(
                        getNameSpace() + "getNextFolderByFolderId", folderId);
                List<String> nextFolderList = Arrays.asList(nextFolderString.split(","));
                for (Long actualFolderId : relas) {
                    // if (nextFolderString.indexOf(actualFolderId + "") != -1)
                    // {
                    if (nextFolderList.contains(actualFolderId.toString())) {
                        // 代表此权限表的下级地域包括该设备
                        try {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("entityId", entityId);
                            params.put("authorityTable", tableName);
                            getSqlSession().insert(getNameSpace() + "insertAuthority", params);
                            // 每个权限地域最多只需要该设备的一条记录
                            break;
                        } catch (Exception e) {
                            logger.equals(e.getStackTrace());
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#updateSnmpConfig(com.topvision.framework.snmp.
     * SnmpParam )
     */
    @Override
    public void updateSnmpConfig(SnmpParam snmpParam) {
        getSqlSession().update(getNameSpace() + "updateSnmpConfig", snmpParam);
        if (snmpParam.getEntityId() != null) {
        snmpParamCaches.put(snmpParam.getEntityId(), selectSnmpParamFromDB(snmpParam.getEntityId()));
    }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#updateSnmpV2Param(com.topvision.framework.snmp.
     * SnmpParam )
     */
    @Override
    public void updateSnmpV2Param(SnmpParam snmpParam) {
        getSqlSession().update(getNameSpace() + "updateSnmpV2Param", snmpParam);
        snmpParamCaches.put(snmpParam.getEntityId(), selectSnmpParamFromDB(snmpParam.getEntityId()));
    }

    @Override
    public Long selectEntityByMac(String mac) {
        mac = MacUtils.convertToMaohaoFormat(mac);
        return (Long) getSqlSession().selectOne(getNameSpace() + "getEntityByMac", mac);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityState(com.topvision.
     * ems.network.domain .EntitySnap)
     */
    @Override
    public void updateEntityState(EntitySnap snap) {
        getSqlSession().update(getNameSpace() + "updateEntityState", snap);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.Entity";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityByTypeId(java.lang.Long)
     */
    @Override
    public List<Entity> getEntityByTypeId(Long typeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", typeId);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getEntityByTypeId"), map);
    }

    @Override
    public List<Entity> selectCentralEntity() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectCentralEntity"), map);
    }

    @Override
    public List<Long> selectEntityIdsByType(Long type) {
        return getSqlSession().selectList(getNameSpace("selectEntityIdsByType"), type);
    }

    @Override
    public List<Long> getEntityIdsByAuthority(Long type) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("type", type);
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getEntityIdsByAuthority"), queryMap);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#moveEntityForBatchDiscovery(java. lang.Long,
     * java.lang.Long)
     */
    @Override
    public void moveEntityForBatchDiscovery(Long entityId, List<Long> destFolderId) {
        // 其他模块不可使用该方法，仅适合BatchDiscovery
        getSqlSession().delete(getNameSpace() + "deleteEntityAllFolderRelation", entityId);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        for (Long destTmp : destFolderId) {
            param.put("destFolderId", destTmp);
            getSqlSession().insert(getNameSpace() + "insertEntityFolder", param);
        }
        List<Long> entityIds = new ArrayList<Long>();
        entityIds.add(entityId);
        reOrganizedAuthority(entityIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#getAllUnknownEntity()
     */
    @Override
    public List<Entity> getAllUnknownEntity() {
        return getSqlSession().selectList(getNameSpace() + "getAllUnknownEntity");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityIpListById(java.lang. Long)
     */
    @Override
    public List<String> getEntityIpListById(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getEntityIpList", entityId);
    }

    @Override
    public List<Entity> getEntityListByType(Long type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getEntityListByType"), map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#getEntityWithMacAndTypeId(java. lang.String,
     * java.lang.Long)
     */
    @Override
    public Entity getEntityWithMacAndTypeId(String macAddress, Long typeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("macAddress", macAddress);
        map.put("typeId", typeId);
        return getSqlSession().selectOne(getNameSpace("getEntityWithMacAndTypeId"), map);
    }

    @Override
    public List<Entity> getEntityListWithMacAndTypeId(String macAddress, Long typeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("macAddress", macAddress);
        map.put("typeId", typeId);
        return getSqlSession().selectList(getNameSpace("getEntityListWithMacAndTypeId"), map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#replaceEntity(java.lang.Long, java.lang.String)
     */
    @Override
    public void replaceEntity(Long preEntityId, String ip) {
        replaceEntity(preEntityId, ip, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#replaceEntity(java.lang.Long, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void replaceEntity(Long preEntityId, String ip, String mac) {
        Entity entity = getSqlSession().selectOne(getNameSpace("selectByPrimaryKey"), preEntityId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("preEntityId", preEntityId);
        map.put("ip", ip);
        map.put("mac", mac);
        getSqlSession().update(getNameSpace("replaceEntity"), map);

        // EMS-12350 杭州华数 - 设备重启更换IP后，设备会显示不在线，但是显示设备IP已经为新的，手动刷新后过段时间会恢复
        entityCaches.remove(preEntityId);
        snmpParamCaches.remove(preEntityId);

        Map<String, Object> address = new HashMap<String, Object>();
        address.put("preIpAddress", entity.getIp());
        address.put("replaceIpAddress", ip);
        address.put("entityId", preEntityId);
        if (ip == null) {
            getSqlSession().delete(getNameSpace("deleteEntityAddress"), address);
        } else if (entity.getIp() == null) {
            // 当前设备IP是NULL，entityAddress表没有相关记录，添加IP记录
            getSqlSession().insert(getNameSpace("insertEntityAddress"), address);
        } else {
        getSqlSession().update(getNameSpace("replaceEntityAddress"), address);
        }

        Map<String, Object> record = new HashMap<String, Object>();
        record.put("entityId", preEntityId);
        record.put("macAddress", entity.getMac());
        record.put("preIpAddress", entity.getIp());
        record.put("replaceIpAddress", ip);
        record.put("replaceTime", new Timestamp(System.currentTimeMillis()));
        getSqlSession().insert(getNameSpace("insertReplaceEntityRecord"), record);
    }

    @Override
    public void updateEntityDeviceName(Long entityId, String deviceName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("deviceName", deviceName);
        getSqlSession().update(getNameSpace("updateEntityDeviceName"), map);
        entityCaches.remove(entityId);
    }

    @Override
    public void updateSnapSysUptime(Long entityId, Long sysUptime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("sysUptime", sysUptime);
        getSqlSession().update(getNameSpace("updateSnapSysUptime"), map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateEntityLastRefreshTime(java. lang.Long,
     * java.sql.Timestamp)
     */
    @Override
    public void updateEntityLastRefreshTime(Long entityId, java.sql.Timestamp lastRefreshTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("lastRefreshTime", lastRefreshTime);
        getSqlSession().update(getNameSpace("updateEntityLastRefreshTime"), map);
        entityCaches.remove(entityId);
    }

    @Override
    public void updateEntityNameAndMac(Long entityId, String deviceName, String mac) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("deviceName", deviceName);
        map.put("mac", mac);
        getSqlSession().update(getNameSpace("updateEntityNameAndMac"), map);
        entityCaches.remove(entityId);
    }

    @Override
    public void setEntityLocatedFolders(Long entityId, List<Long> folderList, List<Long> authFolderIds) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("entityId", entityId);
        queryMap.put("authFolderIds", list2String(authFolderIds));
        SqlSession session = getBatchSession();
        try {
            // 先移除该设备在该用户范围内的所有地域联系
            session.delete(getNameSpace("deleteEntityFolderRela"), queryMap);
            // 批量插入
            for (Long folderId : folderList) {
                queryMap.put("folderId", folderId);
                queryMap.put("x", Math.random() * 600);
                queryMap.put("y", Math.random() * 400);
                session.insert(getNameSpace("addEntityFolderRela"), queryMap);
            }
            session.commit();
            // 更新权限表
            try {
                // 上面使用统一的权限核心方法进行重组，下面注释的方法是使用存储过程单独进入插入的重组(两个方法均可)
                Thread.sleep(1000);
                List<Long> entityIds = new ArrayList<Long>();
                entityIds.add(entityId);
                reOrganizedAuthority(entityIds);
            } catch (Exception e) {
                logger.error(e.getStackTrace().toString());
            }
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void setEntitiesLocatedFolders(List<Long> entityIds, List<Long> folderIds, List<Long> authFolderIds) {
        String entityIdStr = list2String(entityIds);
        String authFolderIdStr = list2String(authFolderIds);

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("entityIdStr", entityIdStr);
        queryMap.put("authFolderIdStr", authFolderIdStr);

        SqlSession session = getBatchSession();
        try {
            // 删除权限地域下所有设备的关联关系
            session.delete(getNameSpace("deleteEntitiesFoldersRela"), queryMap);
            // 插入所选设备的所选地域的关系
            for (Long entityId : entityIds) {
                for (Long folderId : folderIds) {
                    queryMap.put("entityId", entityId);
                    queryMap.put("folderId", folderId);
                    queryMap.put("x", Math.random() * 600);
                    queryMap.put("y", Math.random() * 400);
                    session.insert(getNameSpace("addEntityFolderRela"), queryMap);
                }
            }
            session.commit();
            // 更新权限表
            try {
                Thread.sleep(1000);
                reOrganizedAuthority(entityIds);
            } catch (Exception e) {
                logger.error(e.getStackTrace().toString());
            }
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void addEntitiesToFolders(List<Long> entityIds, List<Long> folderIds) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            // 插入所选设备的所选地域的关系
            for (Long entityId : entityIds) {
                for (Long folderId : folderIds) {
                    queryMap.put("entityId", entityId);
                    queryMap.put("folderId", folderId);
                    queryMap.put("x", Math.random() * 600);
                    queryMap.put("y", Math.random() * 400);
                    session.insert(getNameSpace("addEntityFolderRela"), queryMap);
                }
            }
            session.commit();
            // 更新权限表
            try {
                Thread.sleep(1000);
                reOrganizedAuthority(entityIds);
            } catch (Exception e) {
                logger.error(e.getStackTrace().toString());
            }
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void removeEntitiesFromFolders(List<Long> entityIds, List<Long> folderIds) {
        SqlSession session = getBatchSession();
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            // 插入所选设备的所选地域的关系
            for (Long entityId : entityIds) {
                for (Long folderId : folderIds) {
                    queryMap.put("entityId", entityId);
                    queryMap.put("folderId", folderId);
                    session.delete(getNameSpace("removeEntityFromFolder"), queryMap);
                }
            }
            session.commit();
            // 更新权限表
            try {
                Thread.sleep(1000);
                reOrganizedAuthority(entityIds);
            } catch (Exception e) {
                logger.error(e.getStackTrace().toString());
            }
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Entity> getEntityExportInfo(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getAllEntityExportInfo"), queryMap);
    }

    @Override
    public Integer getExportEntityNum(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getExportEntityNum"), queryMap);
    }

    @Override
    public List<EntityFolderRela> getAllEntityFolders() {
        return getSqlSession().selectList(getNameSpace("getAllEntityFolders"));
    }

    private String list2String(List<?> list) {
        String rlt = "";
        if (list == null || list.size() == 0) {
            return rlt;
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            sb.append(o.toString()).append(",");
        }
        rlt = sb.toString();
        rlt = rlt.substring(0, rlt.length() - 1);
        return rlt;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityDao#updateOnuEntitySnap(com.topvision
     * .ems.network.domain .EntitySnap)
     */
    @Override
    public void updateOnuEntitySnap(EntitySnap entitySnap) {
        getSqlSession().update(getNameSpace() + "updateOnuEntityState", entitySnap);
        synchronized (entityCaches) {
            // entityCaches.put(entityId, selectEntityFromDB(entityId));
            entityCaches.remove(entitySnap.getEntityId());
        }
    }

    @Override
    public Map<Long, Entity> getEntityCaches() {
        return entityCaches;
    }

    @Override
    public void updateEntityMac(Long entityId, String entityMac) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("mac", entityMac);
        getSqlSession().update(getNameSpace() + "modifyEntityMac", map);
        entityCaches.remove(entityId);
    }

    @Override
    public void updateEntityParentId(Long entityId, Long parentId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("parentId", parentId);
        getSqlSession().update(getNameSpace() + "updateEntityParentId", map);
        entityCaches.remove(entityId);
    }

    @Override
    public void deleteTopoEntityByEntityIdAndType(Long type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("typeId", type.toString());
        getSqlSession().delete(getNameSpace() + "deleteTopoEntityByEntityIdAndType", map);
        entityCaches.clear();
    }

    @Override
    public void updateEntityInfo(Entity entity) {
        this.getSqlSession().update(getNameSpace("updateEntityOltInfo"), entity);
        entityCaches.remove(entity.getEntityId());
    }

    @Override
    public void removeProductFromTopoGraph(Long entityId) {
        // 删除entity表中数据，entityId根据productId与entityId关系表得到
        getSqlSession().delete(getNameSpace() + "removeProductFromTopoGraph", entityId);
        entityCaches.remove(entityId);
    }

    @Override
    public void updateEntityConnectInfo(Entity entity) {
        getSqlSession().update(getNameSpace() + "updateEntityConnectInfo", entity);
        entityCaches.remove(entity.getEntityId());
    }

    @Override
    public Entity selectEntityFromDB(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "selectByPrimaryKey", entityId);
    }

    @Override
    public void updateEmsSnmpparam(SnmpParam snmpParam) {
        getSqlSession().update(getNameSpace("updateEmsSnmpparam"), snmpParam);
        snmpParamCaches.remove(snmpParam.getEntityId());
    }

    @Override
    public Map<Long, SnmpParam> getSnmpParamCaches() {
        return snmpParamCaches;
    }

    @Override
    public SnmpParam selectSnmpParamFromDB(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "selectSnmpParam", entityId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.network.dao.EntityImportDao#batchUpdateEntity(java.util .List)
     */
    public void batchUpdateEntity(List<EntityImport> entities) {
        SqlSession session = getBatchSession();
        try {
            for (EntityImport entityImport : entities) {
                session.update(getNameSpace("updateEntityName"), entityImport);
                entityCaches.remove(entityImport.getEntityId());
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void entityAliasBak() {
        List<Entity> entities = getSqlSession().selectList(getNameSpace("getAllEntity"));
        long entitySize = entities.size();
        long aliasLogSize = getSqlSession().selectOne(getNameSpace("getAliasLogCount"));
        if (entitySize > aliasLogSize) {
            batchInsertEntityAlias(entities);
        }
    }

    private void batchInsertEntityAlias(List<Entity> entities) {
        SqlSession session = getBatchSession();
        try {
            for (Entity entity : entities) {
                session.insert(getNameSpace("insertEntityAlias"), entity);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public String selectHistoryAlias(Long entityId, String mac, String index, String sn) {
        String alias = null;
        alias = getSqlSession().selectOne(getNameSpace() + "selectHistoryAliasByMac", mac);
        if (alias == null) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("entityId", entityId);
            queryMap.put("index", index);
            alias = getSqlSession().selectOne(getNameSpace() + "selectHistoryAliasByIndex", queryMap);
        }
        if (alias == null) {
            // modify by haojie gpon onu have no mac,support sn
            alias = getSqlSession().selectOne(getNameSpace() + "selectHistoryAliasBySn", sn);
        }
        return alias;
    }

    @Override
    public List<Snap> loadUserAttentionList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("loadUserAttentionList"), map);
    }

    @Override
    public int userAttentionCount(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("userAttentionCount"), map);
    }

    @Override
    public List<Entity> getEntityWithIp() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getEntityWithIp"), map);
    }

    @Override
    public Long getEntityIndexOfOlt(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getEntityIndexOfOlt"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityDao#isEntityInAuthorityTable(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public Boolean isEntityInAuthorityTable(Long entityId, String tableName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("tableName", tableName);
        Integer count = getSqlSession().selectOne(getNameSpace() + "isEntityInAuthorityTable", map);
        return count == 1 ? true : false;
    }

    @Override
    public void modifyEntityContactAndLocation(Long cmcId, String ccSysLocation, String ccSysContact) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("ccSysLocation", ccSysLocation);
        map.put("ccSysContact", ccSysContact);
        getSqlSession().update(getNameSpace("updateEntityLocationAndContact"), map);
    }

    @Override
    public String getOnuEorG(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getOnuEorG"), entityId);
    }

    @Override
    public Long getEntityRelaOnlineCm(Long entityId) {
        Long cmNum = getSqlSession().selectOne(getNameSpace("getEntityRelaOnlineCm"), entityId);
        return cmNum == null ? 0 : cmNum;
    }
    @Override
    public Integer getOnuLevel(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getOnuLevel"), entityId);
    }

}