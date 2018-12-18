/***********************************************************************
 * $Id: CmSignalTargetThresholdDaoImpl.java,v1.0 2017年8月8日 下午4:19:40 $
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

import com.topvision.ems.cm.pnmp.dao.CmSignalTargetThresholdDao;
import com.topvision.ems.cm.pnmp.facade.domain.CmTargetThreshold;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:19:40
 *
 */

@Repository("cmSignalTargetThresholdDao")
public class CmSignalTargetThresholdDaoImpl extends MyBatisDaoSupport<Object> implements CmSignalTargetThresholdDao {

    @Override
    public List<CmTargetThreshold> selectAllThresholds() {
        return getSqlSession().selectList(getNameSpace("selectAllCmTargetThresholds"));
    }

    @Override
    public List<CmTargetThreshold> selectLevelThresholds(String level) {
        return getSqlSession().selectList(getNameSpace("selectLevelTargetThresholds"), level);
    }

    @Override
    public List<CmTargetThreshold> selectThresholdsByTarget(String targetName) {
        return getSqlSession().selectList(getNameSpace("selectThresholdsByTargetName"), targetName);
    }

    @Override
    public CmTargetThreshold selectThresholdByTargetAndName(String targetName, String thresholdName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("targetName", targetName);
        map.put("thresholdName", thresholdName);
        return getSqlSession().selectOne(getNameSpace("selectThresholdByTargetAndName"), map);
    }

    @Override
    public void updateThresholds(List<CmTargetThreshold> thresholds) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (CmTargetThreshold cmTargetThreshold : thresholds) {
                sqlSession.update(getNameSpace("updateCmTargetThreshold"), cmTargetThreshold);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("updateThresholds error", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void updateThreshold(CmTargetThreshold threshold) {
        getSqlSession().update(getNameSpace("updateThreshold"), threshold);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.pnmp.facade.domain.CmTargetThreshold";
    }

}
