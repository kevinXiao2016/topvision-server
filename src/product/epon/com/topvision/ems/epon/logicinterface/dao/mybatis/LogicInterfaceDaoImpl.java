/***********************************************************************
 * $Id: LogicInterfaceDaoImpl.java,v1.0 2016年10月14日 上午10:09:04 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.logicinterface.dao.LogicInterfaceDao;
import com.topvision.ems.epon.logicinterface.domain.InterfaceIpV4Config;
import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:09:04
 *
 */
@Repository("logicInterfaceDao")
public class LogicInterfaceDaoImpl extends MyBatisDaoSupport<LogicInterface> implements LogicInterfaceDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.logicinterface.domain.LogicInterface";
    }

    @Override
    public void updateLogicInterfaceList(Long entityId, List<LogicInterface> logicInterfaceList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteEntityLogicInterface", entityId);
            for (LogicInterface logicInterface : logicInterfaceList) {
                logicInterface.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertLogicInterface", logicInterface);
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
    public void updateLogicInterfaceIpV4List(Long entityId, List<InterfaceIpV4Config> logicInterfaceIpList) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteEntityInterfaceIpV4Config", entityId);
            for (InterfaceIpV4Config interfaceIpConfig : logicInterfaceIpList) {
                interfaceIpConfig.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertInterfaceIpV4Config", interfaceIpConfig);
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
    public List<LogicInterface> getOltLogicInterfaceByType(Map<String, Object> map) {
        return this.getSqlSession().selectList(getNameSpace("getOltLogicInterfaceByType"), map);
    }

    @Override
    public int getOltLogicInterfaceByTypeCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("getOltLogicInterfaceByTypeCount"), map);
    }

    @Override
    public LogicInterface getOltLogicInterface(Long entityId, Integer interfaceType, Integer interfaceId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("interfaceType", interfaceType);
        paramMap.put("interfaceId", interfaceId);
        return this.getSqlSession().selectOne(getNameSpace("getOltLogicInterface"), paramMap);
    }

    @Override
    public void insertLogicInterface(LogicInterface logicInterface) {
        this.getSqlSession().insert(getNameSpace() + "insertLogicInterface", logicInterface);
    }

    @Override
    public void updateLogicInterface(LogicInterface logicInterface) {
        this.getSqlSession().update(getNameSpace("updateLogicInterface"), logicInterface);
    }

    @Override
    public void deleteLogicInterface(Long entityId, Integer interfaceType, Integer interfaceId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("interfaceType", interfaceType);
        paramMap.put("interfaceId", interfaceId);
        this.getSqlSession().delete(getNameSpace("deleteLogicInterface"), paramMap);
    }

    @Override
    public void deleteIpV4ConfigByInterface(Long entityId, Integer interfaceType, Integer interfaceId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("interfaceType", interfaceType);
        paramMap.put("interfaceId", interfaceId);
        this.getSqlSession().delete(getNameSpace("deleteIpV4ConfigByInterface"), paramMap);
    }

    @Override
    public List<InterfaceIpV4Config> getInterfaceIpList(Map<String, Object> map) {
        return this.getSqlSession().selectList(getNameSpace("getInterfaceIpList"), map);
    }

    @Override
    public InterfaceIpV4Config getInterfaceIpV4Config(Long entityId, Integer ipV4ConfigIndex, String ipV4Addr) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("ipV4ConfigIndex", ipV4ConfigIndex);
        paramMap.put("ipV4Addr", ipV4Addr);
        return this.getSqlSession().selectOne(getNameSpace("getInterfaceIpV4Config"), paramMap);
    }

    public void insertInterfaceIpV4Config(InterfaceIpV4Config ipV4Config) {
        this.getSqlSession().insert(getNameSpace() + "insertInterfaceIpV4Config", ipV4Config);
    }

    @Override
    public void updateInterfaceIpV4Config(InterfaceIpV4Config ipV4Config) {
        // this.getSqlSession().update(getNameSpace("updateInterfaceIpV4Config"), ipV4Config);
    }

    @Override
    public void deleteInterfaceIpV4Config(Long entityId, Integer ipV4ConfigIndex, String ipV4Addr) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("ipV4ConfigIndex", ipV4ConfigIndex);
        paramMap.put("ipV4Addr", ipV4Addr);
        this.getSqlSession().update(getNameSpace("deleteInterfaceIpV4Config"), paramMap);
    }

    @Override
    public void deletePriIpV4Config(Long entityId, Integer ipV4ConfigIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("ipV4ConfigIndex", ipV4ConfigIndex);
        this.getSqlSession().update(getNameSpace("deletePriIpV4Config"), paramMap);
    }

}
