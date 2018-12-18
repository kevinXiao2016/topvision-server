/***********************************************************************
 * $Id: IsolationGroupConfigDaoImpl.java,v1.0 2014-12-18 上午11:34:21 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portisolation.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.portisolation.dao.IsolationGroupConfigDao;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGroup;
import com.topvision.ems.epon.portisolation.domain.PortIsolationGrpMember;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2014-12-18-上午11:34:21
 *
 */
@Repository("isolationGroupConfigDao")
public class IsolationGroupConfigDaoImpl extends MyBatisDaoSupport<Object> implements IsolationGroupConfigDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.portisolation.domain.PortIsolationGroup";
    }

    @Override
    public void insertGroup(PortIsolationGroup group) {
        this.getSqlSession().insert(getNameSpace("insertGroup"), group);
    }

    @Override
    public void batchInsertGroup(List<PortIsolationGroup> groupList, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteGroupByEntityId"), entityId);
            for (PortIsolationGroup group : groupList) {
                group.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertGroup"), group);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updateGroup(PortIsolationGroup group) {
        this.getSqlSession().update(getNameSpace("updateGroup"), group);
    }

    @Override
    public void deleteGroup(PortIsolationGroup group) {
        this.getSqlSession().delete(getNameSpace("deleteGroup"), group);
    }

    @Override
    public PortIsolationGroup queryGroup(Long entityId, Integer groupIndex) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("groupIndex", groupIndex);
        return this.getSqlSession().selectOne(getNameSpace("queryGroup"), params);
    }

    @Override
    public List<PortIsolationGroup> queryGroupList(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("queryGroupList"), entityId);
    }

    @Override
    public void insertGroupMemeber(PortIsolationGrpMember groupMember) {
        this.getSqlSession().insert(getNameSpace("insertGroupMemeber"), groupMember);
    }

    @Override
    public void batchInsertMemberWithDelete(List<PortIsolationGrpMember> memberList, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteMemberByEntityId"), entityId);
            for (PortIsolationGrpMember member : memberList) {
                member.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertGroupMemeber"), member);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertGroupMember(List<PortIsolationGrpMember> memberList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (PortIsolationGrpMember member : memberList) {
                sqlSession.insert(getNameSpace("insertGroupMemeber"), member);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void deleteGroupMember(PortIsolationGrpMember groupMember) {
        this.getSqlSession().delete(getNameSpace("deleteGroupMember"), groupMember);
    }

    @Override
    public void deleteMemberByGroup(PortIsolationGrpMember groupMember) {
        this.getSqlSession().delete(getNameSpace("deleteMemberByGroup"), groupMember);
    }

    @Override
    public void deleteMemberByPorts(Long entityId, Integer groupIndex, String portsList) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("groupIndex", groupIndex);
        params.put("portsList", portsList);
        this.getSqlSession().delete(getNameSpace("deleteMemberByPorts"), params);
    }

    @Override
    public List<PortIsolationGrpMember> queryGroupMember(Long entityId, Integer groupIndex) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("groupIndex", groupIndex);
        return this.getSqlSession().selectList(getNameSpace("queryGroupMember"), params);
    }

    @Override
    public List<PortIsolationGrpMember> queryMemberByPorts(Long entityId, Integer groupIndex, String portsList) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("groupIndex", groupIndex);
        params.put("portsList", portsList);
        return this.getSqlSession().selectList(getNameSpace("queryMemberByPorts"), params);
    }

}
