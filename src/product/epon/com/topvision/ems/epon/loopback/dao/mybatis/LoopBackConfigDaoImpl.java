/***********************************************************************
 * $Id: LoopBackConfigDaoImpl.java,v1.0 2013-11-16 上午11:48:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.loopback.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.loopback.dao.LoopBackConfigDao;
import com.topvision.ems.epon.loopback.domain.LoopbackConfigTable;
import com.topvision.ems.epon.loopback.domain.LoopbackSubIpTable;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-11-16-上午11:48:08
 * 
 */
@Repository("loopBackConfigDao")
public class LoopBackConfigDaoImpl extends MyBatisDaoSupport<LoopbackConfigTable> implements LoopBackConfigDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.loopback.domain.LoopBack";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.loopback.dao.LoopBackConfigDao#refreshLoopBackConfigTable(java.lang
     * .Long, java.util.List)
     */
    @Override
    public void refreshLoopBackConfigTable(Long entityId, List<LoopbackConfigTable> loopbackConfigTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteLoopBackConfig", entityId);
            for (LoopbackConfigTable configTable : loopbackConfigTables) {
                configTable.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertLoopBackConfig", configTable);
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
     * @see
     * com.topvision.ems.epon.loopback.dao.LoopBackConfigDao#refreshLoopBackSubTable(java.lang.Long,
     * java.util.List)
     */
    @Override
    public void refreshLoopBackSubTable(Long entityId, List<LoopbackSubIpTable> loopbackSubIpTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteLoopBackSub", entityId);
            for (LoopbackSubIpTable subIpTable : loopbackSubIpTables) {
                subIpTable.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertLoopBackSub", subIpTable);
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
    public void insertLoopBackInterface(LoopbackConfigTable loopBack) {
        this.getSqlSession().insert(getNameSpace("insertLoopBackConfig"), loopBack);
    }

    @Override
    public void deleteLoopBackInterface(LoopbackConfigTable loopBack) {
        this.getSqlSession().delete(getNameSpace("deleteLoopBackInterface"), loopBack);
    }

    @Override
    public List<LoopbackConfigTable> queryLoopbackList(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("selectLoopBackList"), entityId);
    }

    @Override
    public void insertLBSubIp(LoopbackSubIpTable subIpTable) {
        this.getSqlSession().insert(getNameSpace("insertLoopBackSub"), subIpTable);
    }

    @Override
    public void deleteLBSubIp(LoopbackSubIpTable subIpTable) {
        this.getSqlSession().delete(getNameSpace("deleteLBSubIp"), subIpTable);
    }

    @Override
    public List<LoopbackSubIpTable> querySubIpList(Long entityId, Integer interfaceIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("loopbackSubIpIndex", interfaceIndex);
        return this.getSqlSession().selectList(getNameSpace("querySubIpList"), paramMap);
    }

    @Override
    public void updateLoopBackInterface(LoopbackConfigTable loopBack) {
        this.getSqlSession().update(getNameSpace("updateLoopBackInterface"), loopBack);
    }

    @Override
    public void updateLBSubIp(LoopbackSubIpTable subIpTable) {
        this.getSqlSession().update(getNameSpace("updateLBSubIp"), subIpTable);
    }

}
