/***********************************************************************
 * $Id: SnmpV3UserDaoImpl.java,v1.0 2013-1-9 下午2:21:13 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.dao.mybatis;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.snmpV3.dao.SnmpV3UserDao;
import com.topvision.ems.snmpV3.domain.SnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Access;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013-1-9-下午2:21:13
 * 
 */
@Repository("snmpV3UserDao")
public class SnmpV3UserDaoImpl extends MyBatisDaoSupport<BaseEntity> implements SnmpV3UserDao {
    @Override
    public String getDomainName() {
        return "com.topvision.ems.snmpV3.domain.SnmpV3User";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#selectSnmpV3UserList(java.lang.Long)
     */
    @Override
    public List<UsmSnmpV3User> selectSnmpV3UserList(Long entityId) throws SQLException {
        return getSqlSession().selectList(getNameSpace("selectSnmpV3UserList"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#selectSnmpV3ViewList(java.lang.Long)
     */
    @Override
    public List<VacmSnmpV3View> selectSnmpV3ViewList(Long entityId) throws SQLException {
        return getSqlSession().selectList(getNameSpace("selectSnmpV3ViewList"), entityId);
    }

    @Override
    public List<VacmSnmpV3View> selectSnmpV3ViewNameList(Long entityId) throws SQLException {
        return getSqlSession().selectList(getNameSpace("selectSnmpV3ViewNameList"), entityId);
    }

    @Override
    public VacmSnmpV3Access selectSnmpAccessInfo(Map<String, String> map) throws SQLException {
        return (VacmSnmpV3Access) getSqlSession().selectOne(getNameSpace("selectSnmpAccessInfo"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#batchInsertSnmpV3UserList(java.lang.Long,
     * java.util.List)
     */
    @Override
    public void batchInsertSnmpV3UserList(final Long entityId, final List<UsmSnmpV3User> snmpV3UserList) {
        SqlSession session = getBatchSession();
        try {
            List<UsmSnmpV3User> users = session.selectList(getNameSpace("selectSnmpV3UserList"), entityId);
            Map<String, UsmSnmpV3User> map = new HashMap<String, UsmSnmpV3User>();
            for (UsmSnmpV3User user : users) {
                String username = user.getSnmpUserName();
                // username 不可能为空，是主键
                map.put(username, user);
            }
            session.delete(getNameSpace("deleteAllUsmSnmpV3User"), entityId);
            for (UsmSnmpV3User user : snmpV3UserList) {
                // 如果 authkey/privKey为空，则不插入
                user.setEntityId(entityId);
                String username = user.getSnmpUserName();
                if (map.containsKey(username)) {
                    UsmSnmpV3User existedUser = map.get(username);
                    user.setSnmpAuthKeyChange(existedUser.getSnmpAuthKeyChange());
                    user.setSnmpPrivKeyChange(existedUser.getSnmpPrivKeyChange());
                }
                session.insert(getNameSpace("batchInsertSnmpV3UserList"), user);
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
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#batchInsertSnmpV3GroupList(java.lang.Long,
     * java.util.List)
     */
    @Override
    public void batchUpdateSnmpV3GroupList(final Long entityId, final List<VacmSnmpV3Group> snmpV3GroupList) {
        SqlSession session = getBatchSession();
        try {
            for (VacmSnmpV3Group group : snmpV3GroupList) {
                group.setEntityId(entityId);
                session.update(getNameSpace("batchUpdateSnmpV3GroupList"), group);
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
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#batchUpdateSnmpV3AccessList(java.lang.Long,
     * java.util.List)
     */
    @Override
    public void batchInsertSnmpV3AccessList(final Long entityId, final List<VacmSnmpV3Access> snmpV3AccessList) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteAllUsmSnmpV3Access"), entityId);
            for (VacmSnmpV3Access access : snmpV3AccessList) {
                access.setEntityId(entityId);
                session.update(getNameSpace("batchInsertSnmpV3AccessList"), access);
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
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#batchInsertSnmpV3ViewList(java.lang.Long,
     * java.util.List)
     */
    @Override
    public void batchInsertSnmpV3ViewList(final Long entityId, final List<VacmSnmpV3View> snmpV3ViewList) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteAllUsmSnmpV3View"), entityId);
            for (VacmSnmpV3View view : snmpV3ViewList) {
                view.setEntityId(entityId);
                session.insert(getNameSpace("batchInsertSnmpV3ViewList"), view);
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
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#deleteSnmpV3User(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteSnmpV3User(Long entityId, String v3Username) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#deleteSnmpV3Group(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteSnmpV3Group(Long entityId, String v3GroupName) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#deleteSnmpV3View(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteSnmpV3View(Long entityId, String v3ViewName) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#updateSnmpV3User(java.lang.Long,
     * com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public void updateSnmpV3UserParameters(Long entityId, UsmSnmpV3User snmpV3User) throws SQLException {
        snmpV3User.setEntityId(entityId);
        getSqlSession().update(getNameSpace("updateSnmpV3UserParameters"), snmpV3User);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#updateSnmpV3Group(java.lang.Long,
     * com.topvision.ems.snmpV3.domain.SnmpV3Group)
     */
    @Override
    public void updateSnmpV3Group(Long entityId, SnmpV3Group snmpV3Group) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#updateSnmpV3View(java.lang.Long,
     * com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View)
     */
    @Override
    public void updateSnmpV3View(Long entityId, VacmSnmpV3View snmpV3View) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#updateSnmpVersion(java.lang.Long, int,
     * com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public void updateSnmpVersion(Long entityId, int snmpVersion, UsmSnmpV3User snmpV3User) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#selectManagerV3UserInfo(java.lang.Long)
     */
    @Override
    public void selectManagerV3UserInfo(Long entityId) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.dao.SnmpV3UserDao#updateManagerV3UserInfo(java.lang.Long,
     * com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void updateManagerV3UserInfo(Long entityId, SnmpParam user) {
    }

    @Override
    public void deleteView(VacmSnmpV3View view) throws SQLException {
        getSqlSession().delete(getNameSpace("deleteView"), view);
    }

    @Override
    public void deleteAccess(VacmSnmpV3Access access) throws SQLException {
        getSqlSession().delete(getNameSpace("deleteAccess"), access);
    }

    @Override
    public void deleteUser(UsmSnmpV3User user) throws SQLException {
        getSqlSession().delete(getNameSpace("deleteUser"), user);
    }

    @Override
    public void insertVacmAccess(VacmSnmpV3Access access) throws SQLException {
        getSqlSession().insert(getNameSpace("batchInsertSnmpV3AccessList"), access);
    }

    @Override
    public void insertVacmView(VacmSnmpV3View view) throws SQLException {
        getSqlSession().insert(getNameSpace("batchInsertSnmpV3ViewList"), view);
    }

    @Override
    public UsmSnmpV3User selectUserByName(Long entityId, String snmpUserName, String snmpUserEngineId)
            throws SQLException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", entityId.toString());
        map.put("cloneUser", snmpUserName);
        map.put("snmpUserEngineId", snmpUserEngineId);
        return (UsmSnmpV3User) getSqlSession().selectOne(getNameSpace("selectUserByName"), map);
    }

    @Override
    public void insertSnmpV3User(UsmSnmpV3User snmpV3User) throws SQLException {
        getSqlSession().insert(getNameSpace("batchInsertSnmpV3UserList"), snmpV3User);
    }

    @Override
    public void updateVacmGroup(VacmSnmpV3Group vacmGroup) throws SQLException {
        getSqlSession().insert(getNameSpace("batchUpdateSnmpV3GroupList"), vacmGroup);
    }

    @Override
    public List<UsmSnmpV3User> queryAvaiableCloneUserList(UsmSnmpV3User user) throws SQLException {
        return getSqlSession().selectList(getNameSpace("queryAvaiableCloneUserList"), user);
    }

    @Override
    public void updateVacmAccess(VacmSnmpV3Access access) throws SQLException {
        getSqlSession().update(getNameSpace("updateVacmAccess"), access);
    }

    @Override
    public void updateSnmpV3User(UsmSnmpV3User snmpV3User) throws SQLException {
        getSqlSession().update(getNameSpace("updateSnmpV3User"), snmpV3User);
    }

    @Override
    public VacmSnmpV3View querySnmpViewInfo(VacmSnmpV3View view) throws SQLException {
        return (VacmSnmpV3View) getSqlSession().selectOne(getNameSpace("querySnmpViewInfo"), view);
    }

    @Override
    public List<UsmSnmpV3User> queryAvaiableCloneEngineList(UsmSnmpV3User user) throws SQLException {
        return getSqlSession().selectList(getNameSpace("queryAvaiableCloneEngineList"), user);
    }

    @Override
    public List<VacmSnmpV3Access> selectSnmpV3AccessList(Long entityId, Integer snmpSecurityLevel) throws SQLException {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        if (snmpSecurityLevel != null) {
            map.put("snmpSecurityLevel", snmpSecurityLevel.longValue());
        }
        return getSqlSession().selectList(getNameSpace("selectSnmpV3AccessList"), map);
    }

}
