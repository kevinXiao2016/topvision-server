/***********************************************************************
 * $Id: PnmpCmDataDaoImpl.java,v1.0 2017年8月8日 下午4:20:32 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.pnmp.dao.PnmpCmDataDao;
import com.topvision.ems.cm.pnmp.facade.domain.CorrelationGroup;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:20:32
 *
 */
@Repository("pnmpCmDataDao")
public class PnmpCmDataDaoImpl extends MyBatisDaoSupport<Object> implements PnmpCmDataDao {
    
    private ConcurrentHashMap<String, PnmpCmData>cacheMap=new ConcurrentHashMap<String, PnmpCmData>();

    @Override
    public void selectCorrelationGroupsByCmcId(Long cmcId) {
    }

    @Override
    public List<PnmpCmData> selectDataByGroup(Map<String, Object> queryMap) {
        if (queryMap == null) {
            queryMap = new HashMap<>();
        }
        queryMap.put("sort","A.correlationGroup");
        queryMap.put("dir","desc");
        return getSqlSession().selectList(getNameSpace() + "selectDataByGroup", queryMap);
    }

    @Override
    public void insertOrUpdateData(PnmpCmData data) {
    }

    @Override
    public List<PnmpCmData> selectHistoryData(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace() + "selectHistoryData", queryMap);
    }

    @Override
    public List<String> selectSpectrumResponsesByMac(List<String> cmMacList) {
        SqlSession sqlSession = getBatchSession();
        List<String> results = new ArrayList<String>();
        try {
            for (String cmMac : cmMacList) {
                results.add(sqlSession.selectOne(getNameSpace() + "selectSpectrumResponseByMac", cmMac));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        return results;
    }

    @Override
    public Long selectMaxUpChannelWidthByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "selectMaxUpChannelWidthByCmcId", cmcId);
    }

    @Override
    public List<Long> selectHighIntervalCmcIds() {
        return getSqlSession().selectList(getNameSpace() + "selectHighIntervalCmcIds");
    }

    @Override
    public List<CorrelationGroup> selectCorrelationGroup(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "selectCorrelationGroup", cmcId);
    }

    @Override
    public List<Long> selectMiddleIntervalCmcIds() {
        return getSqlSession().selectList(getNameSpace() + "selectMiddleIntervalCmcIds");
    }

    @Override
    public void updateCorrelationGroup(List<CorrelationGroup> groups) {
        SqlSession sqlSession = getBatchSession();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            for (CorrelationGroup group : groups) {
                map.put("cmMac", group.getCmMac());
                map.put("cmcId", group.getCmcId());
                map.put("correlationGroup", group.getGroupId());
                sqlSession.update(getNameSpace() + "updateCorrelationGroup", map);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("updateCorrelationGroup error", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public List<CorrelationGroup> selectHighCorrelationGroups(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "selectHighCorrelationGroups", cmcId);
    }

    @Override
    public List<CorrelationGroup> selectMiddleCorrelationGroups(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "selectMiddleCorrelationGroups", cmcId);
    }

    @Override
    public void updateCorrelationGroupByCmcId(Long cmcId) {
        getSqlSession().update(getNameSpace() + "updateCorrelationGroupByCmcId", cmcId);
    }

    @Override
    protected String getDomainName() {
        return PnmpCmData.class.getName();
    }

    @Override
    public PnmpCmData selectDataByGroupForMobile(Map<String, Object> queryMap) {
        PnmpCmData temp=getSqlSession().selectOne(getNameSpace() + "selectDataByGroupForMobile", queryMap);
        if (!CmAttribute.isCmOnline(temp.getStatusValue().intValue())) {
            if(cacheMap.containsKey(queryMap.get("deviceId"))){
                return cacheMap.get(queryMap.get("deviceId"));
            }else 
                return null;
        }else{
            cacheMap.put(queryMap.get("deviceId").toString(), temp);
        }
        return temp;
    }
}
