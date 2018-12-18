/***********************************************************************
 * $Id: DispersionDaoImpl.java,v1.0 2015-3-12 下午2:19:11 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.dispersion.dao.DispersionDao;
import com.topvision.ems.cm.dispersion.domain.Dispersion;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author fanzidong
 * @created @2015-3-12-下午2:19:11
 * 
 */
@Repository("dispersionDao")
public class DispersionDaoImpl extends MyBatisDaoSupport<Dispersion> implements DispersionDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.dispersion.domain.Dispersion";
    }

    @Override
    public List<Dispersion> selectDispersionList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        List<Dispersion> dispersions = getSqlSession().selectList(getNameSpace("selectDispersionList"), queryMap);
        return dispersions;
    }

    @Override
    public Integer getDispersionListNum(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getDispersionListNum"), queryMap);
    }

    @Override
    public List<Dispersion> selectDispersionsByIdAndRange(Long opticalNodeId, String startTime, String endTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("opticalNodeId", opticalNodeId);
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace("selectDispersionsByIdAndRange"), queryMap);
    }

    @Override
    public Dispersion selectDispersionByIdAndTime(Long opticalNodeId, String exactTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("opticalNodeId", opticalNodeId);
        queryMap.put("exactTime", exactTime);
        return getSqlSession().selectOne(getNameSpace("selectDispersionByIdAndTime"), queryMap);
    }

    @Override
    public void batchInsertDispersions(List<Dispersion> dispersions) {
        SqlSession batchSession = getBatchSession();
        try {
            for (Dispersion dispersion : dispersions) {
                batchSession.insert(getNameSpace("insertDispersion"), dispersion);
            }
            batchSession.commit();
        } catch (Exception e) {
            logger.error("batch insert dispersions error:", e);
            batchSession.rollback();
        } finally {
            batchSession.close();
        }
    }

    @Override
    public Dispersion getDispersionById(Long opticalNodeId) {
        return getSqlSession().selectOne(getNameSpace("getDispersionById"), opticalNodeId);
    }

}
