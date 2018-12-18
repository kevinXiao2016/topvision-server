/***********************************************************************
 * $Id: PnmpMonitorCmDaoImpl.java,v1.0 2017年8月8日 下午4:21:37 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.pnmp.dao.PnmpMonitorCmDao;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:21:37
 *
 */
@Repository("pnmpMonitorCmDao")
public class PnmpMonitorCmDaoImpl extends MyBatisDaoSupport<Object> implements PnmpMonitorCmDao {

    @Override
    public List<String> selectAllMiddleMonitorCmMac() {
        return getSqlSession().selectList(getNameSpace() + "selectAllMiddleMonitorCmMac");
    }

    @Override
    public List<String> selectAllHighMonitorCmMac() {
        return getSqlSession().selectList(getNameSpace() + "selectAllHighMonitorCmMac");
    }

    @Override
    public Integer selectOnlineCmNum() {
        return getSqlSession().selectOne(getNameSpace() + "selectOnlineCmNum");
    }

    @Override
    public List<PnmpCmData> selectAllMiddleMonitorCmList() {
        return getSqlSession().selectList(getNameSpace() + "selectAllMiddleMonitorCmList");
    }

    @Override
    public List<PnmpCmData> selectAllHighMonitorCmList() {
        return getSqlSession().selectList(getNameSpace() + "selectAllHighMonitorCmList");
    }

    @Override
    public Integer selectAllHighMonitorCmListNum() {
        return getSqlSession().selectOne(getNameSpace() + "selectAllHighMonitorCmListNum");
    }

    @Override
    public Integer selectAllMiddleMonitorCmListNum() {
        return getSqlSession().selectOne(getNameSpace() + "selectAllMiddleMonitorCmListNum");
    }

    @Override
    public List<PnmpCmData> selectHighMonitorCmList(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace() + "selectHighMonitorCmList", queryMap);
    }

    @Override
    public Integer selectHighMonitorCmListNum(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace() + "selectHighMonitorCmListNum", queryMap);
    }

    @Override
    public void insertMiddleMonitorCm(String cmMac) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Long> result = getSqlSession().selectOne(getNameSpace() + "selectCmcIdEntityIdByMac", cmMac);
        map.put("cmmac", cmMac);
        if (result != null) {
            map.put("cmcId", result.get("cmcId"));
            map.put("entityId", result.get("entityId"));
        }
        getSqlSession().insert(getNameSpace() + "insertMiddleMonitorCm", map);
    }

    @Override
    public void deleteMiddleMonitorCm(String cmMac) {
        getSqlSession().delete(getNameSpace() + "deleteMiddleMonitorCm", cmMac);
    }

    @Override
    public void insertHighMonitorCm(String cmMac) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Long> result = getSqlSession().selectOne(getNameSpace() + "selectCmcIdEntityIdByMac", cmMac);
        map.put("cmMac", cmMac);
        if (result != null) {
            map.put("cmcId", result.get("cmcId"));
            map.put("entityId", result.get("entityId"));
        }
        getSqlSession().insert(getNameSpace() + "insertHighMonitorCm", map);
    }

    @Override
    public String selectHighMonitorCmByPK(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace() + "selectHighMonitorCmByPK", queryMap);
    }

    @Override
    public Map<String, Object> selectCmcAttributeByCmMac(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace() + "selectCmcAttributeByCmMac", queryMap);
    }

    @Override
    public void deleteHighMonitorCm(String cmMac) {
        getSqlSession().delete(getNameSpace() + "deleteHighMonitorCm", cmMac);
    }

    @Override
    public void batchDeleteHighMonitorCm(List<String> cmMacs) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (String cmMac : cmMacs) {
                sqlSession.delete(getNameSpace() + "deleteHighMonitorCm", cmMac);
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
    protected String getDomainName() {
        return PnmpCmData.class.getName();
    }

}
