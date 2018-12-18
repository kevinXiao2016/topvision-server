/***********************************************************************
 * $Id: SnmpV3DaoImpl.java,v1.0 2013-1-9 上午9:36:38 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.snmpV3.dao.SnmpV3Dao;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:36:38
 * 
 */
@Repository("snmpV3Dao")
public class SnmpV3DaoImpl extends MyBatisDaoSupport<BaseEntity> implements SnmpV3Dao {
    @Override
    public String getDomainName() {
        return "com.topvision.ems.snmpV3.domain.SnmpV3";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#insertTarget(com.topvision.ems
     * .snmpV3.facade.domain.SnmpTargetTable)
     */
    @Override
    public void insertTarget(SnmpTargetTable snmpTargetTable) {
        getSqlSession().insert(getNameSpace("insertTarget"), snmpTargetTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#updateTarget(com.topvision.ems
     * .snmpV3.facade.domain.SnmpTargetTable)
     */
    @Override
    public void updateTarget(SnmpTargetTable snmpTargetTable) {
        getSqlSession().update(getNameSpace("updateTarget"), snmpTargetTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteTarget(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteTarget(Long entityId, String targetName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("targetName", targetName);
        getSqlSession().delete(getNameSpace("deleteTarget"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3Dao#loadTarget(java.lang.Long)
     */
    @Override
    public List<SnmpTargetTable> loadTarget(Long entityId) {
        return getSqlSession().selectList(getNameSpace("loadTarget"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteAllTarget(java.lang.Long)
     */
    @Override
    public void deleteAllTarget(Long entityId) {
        getSqlSession().delete(getNameSpace("deleteAllTarget"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#insertTargetParams(com.topvision
     * .ems.snmpV3.facade.domain.SnmpTargetParams)
     */
    @Override
    public void insertTargetParams(SnmpTargetParams snmpTargetParams) {
        getSqlSession().insert(getNameSpace("insertTargetParams"), snmpTargetParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#updateTargetParams(com.topvision
     * .ems.snmpV3.facade.domain.SnmpTargetParams)
     */
    @Override
    public void updateTargetParams(SnmpTargetParams snmpTargetParams) {
        getSqlSession().update(getNameSpace("updateTargetParams"), snmpTargetParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteTargetParams(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteTargetParams(Long entityId, String targetParamsName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("targetParamsName", targetParamsName);
        getSqlSession().delete(getNameSpace("deleteTargetParams"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#loadTargetParams(java.lang.Long)
     */
    @Override
    public List<SnmpTargetParams> loadTargetParams(Long entityId) {
        return getSqlSession().selectList(getNameSpace("loadTargetParams"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteAllTargetParams(java.lang
     * .Long)
     */
    @Override
    public void deleteAllTargetParams(Long entityId) {
        getSqlSession().delete(getNameSpace("deleteAllTargetParams"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#insertNotify(com.topvision.ems
     * .snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void insertNotify(SnmpNotifyTable snmpNotifyTable) {
        getSqlSession().insert(getNameSpace("insertNotify"), snmpNotifyTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#updateNotify(com.topvision.ems
     * .snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void updateNotify(SnmpNotifyTable snmpNotifyTable) {
        getSqlSession().update(getNameSpace("updateNotify"), snmpNotifyTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteNotify(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteNotify(Long entityId, String notifyName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("notifyName", notifyName);
        getSqlSession().delete(getNameSpace("deleteNotify"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3Dao#loadNotify(java.lang.Long)
     */
    @Override
    public List<SnmpNotifyTable> loadNotify(Long entityId) {
        return getSqlSession().selectList(getNameSpace("loadNotify"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteAllNotify(java.lang.Long)
     */
    @Override
    public void deleteAllNotify(Long entityId) {
        getSqlSession().delete(getNameSpace("deleteAllNotify"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#insertNotifyProfile(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void insertNotifyProfile(SnmpNotifyFilterProfile snmpNotifyFilterProfile) {
        getSqlSession().insert(getNameSpace("insertNotifyProfile"), snmpNotifyFilterProfile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#updateNotifyProfile(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void updateNotifyProfile(SnmpNotifyFilterProfile snmpNotifyFilterProfile) {
        getSqlSession().update(getNameSpace("updateNotifyProfile"), snmpNotifyFilterProfile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteNotifyProfile(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void deleteNotifyProfile(SnmpNotifyFilterProfile snmpNotifyFilterProfile) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", snmpNotifyFilterProfile.getEntityId());
        map.put("targetParamsName", snmpNotifyFilterProfile.getTargetParamsName());
        getSqlSession().delete(getNameSpace("deleteNotifyProfile"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#loadNotifyFilterProfile(java.lang
     * .Long)
     */
    @Override
    public List<SnmpNotifyFilterProfile> loadNotifyFilterProfile(Long entityId) {
        return getSqlSession().selectList(getNameSpace("loadNotifyFilterProfile"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteAllNotifyProfile(java.lang
     * .Long)
     */
    @Override
    public void deleteAllNotifyProfile(Long entityId) {
        getSqlSession().delete(getNameSpace("deleteAllNotifyProfile"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#insertNotifyFilter(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyFilterTable)
     */
    @Override
    public void insertNotifyFilter(SnmpNotifyFilterTable snmpNotifyFilterTable) {
        getSqlSession().insert(getNameSpace("insertNotifyFilter"), snmpNotifyFilterTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#updateNotifyFilter(com.topvision
     * .ems.snmpV3.facade.domain.SnmpNotifyFilterTable)
     */
    @Override
    public void updateNotifyFilter(SnmpNotifyFilterTable snmpNotifyFilterTable) {
        getSqlSession().update(getNameSpace("updateNotifyFilter"), snmpNotifyFilterTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteNotifyFilter(java.lang.Long,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void deleteNotifyFilter(Long entityId, String notifyFilterProfileName, String notifyFilterSubtree) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("notifyFilterSubtree", notifyFilterSubtree);
        map.put("notifyFilterProfileName", notifyFilterProfileName);
        getSqlSession().delete(getNameSpace("deleteNotifyFilter"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#loadNotifyFilter(java.lang.Long)
     */
    @Override
    public List<SnmpNotifyFilterTable> loadNotifyFilter(Long entityId) {
        return getSqlSession().selectList(getNameSpace("loadNotifyFilter"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#deleteAllNotifyFilter(java.lang
     * .Long)
     */
    @Override
    public void deleteAllNotifyFilter(Long entityId) {
        getSqlSession().delete(getNameSpace("deleteAllNotifyFilter"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#batchInsertTarget(java.lang.Long,
     * java.util.List)
     */
    @Override
    public void batchInsertTarget(final Long entityId, final List<SnmpTargetTable> targets) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteAllTarget"), entityId);
            for (SnmpTargetTable target : targets) {
                target.setEntityId(entityId);
                session.insert(getNameSpace("insertTarget"), target);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#batchInsertTargetParams(java.lang
     * .Long, java.util.List)
     */
    @Override
    public void batchInsertTargetParams(final Long entityId, final List<SnmpTargetParams> targetParams) {
        getSqlSession().delete(getNameSpace("deleteAllTargetParams"), entityId);
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteAllTargetParams"), entityId);
            for (SnmpTargetParams targetParam : targetParams) {
                targetParam.setEntityId(entityId);
                session.insert(getNameSpace("insertTargetParams"), targetParam);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#batchInsertNotify(java.lang.Long,
     * java.util.List)
     */
    @Override
    public void batchInsertNotify(final Long entityId, final List<SnmpNotifyTable> notifys) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteAllNotify"), entityId);
            for (SnmpNotifyTable notify : notifys) {
                notify.setEntityId(entityId);
                session.insert(getNameSpace("insertNotify"), notify);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertNotifyFilterProfile(final Long entityId,
            final List<SnmpNotifyFilterProfile> notifyFilterProfiles) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteAllNotifyProfile"), entityId);
            for (SnmpNotifyFilterProfile o : notifyFilterProfiles) {
                o.setEntityId(entityId);
                session.insert(getNameSpace("insertNotifyProfile"), o);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.dao.SnmpV3Dao#batchInsertNotifyFilterTable(java
     * .lang.Long, java.util.List)
     */
    @Override
    public void batchInsertNotifyFilterTable(final Long entityId, final List<SnmpNotifyFilterTable> notifyFilterTables) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteAllNotifyFilter"), entityId);
            for (SnmpNotifyFilterTable o : notifyFilterTables) {
                o.setEntityId(entityId);
                session.insert(getNameSpace("insertNotifyFilter"), o);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

}
