/***********************************************************************
 * $Id: PnmpTargetThresholdDaoImpl.java,v1.0 2017年8月8日 下午4:22:41 $
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

import com.topvision.ems.cm.pnmp.dao.PnmpTargetThresholdDao;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:22:41
 *
 */
@Repository("pnmpTargetThresholdDao")
public class PnmpTargetThresholdDaoImpl extends MyBatisDaoSupport<Object> implements PnmpTargetThresholdDao {

    @Override
    public List<PnmpTargetThreshold> selectAllThresholds() {
        return getSqlSession().selectList(getNameSpace("selectAllPnmpTargetThresholds"));
    }

    @Override
    public List<PnmpTargetThreshold> selectLevelThresholds(String level) {
        return getSqlSession().selectList(getNameSpace("selectLevelPnmpTargetThresholds"), level);
    }

    @Override
    public List<PnmpTargetThreshold> selectThresholdsByTarget(String targetName) {
        return getSqlSession().selectList(getNameSpace("selectThresholdsByTargetName"), targetName);
    }

    @Override
    public PnmpTargetThreshold selectThresholdByTargetAndName(String targetName, String thresholdName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("targetName", targetName);
        map.put("thresholdName", thresholdName);
        return getSqlSession().selectOne(getNameSpace("selectThresholdByTargetAndName"), map);
    }

    @Override
    public void updateThresholds(List<PnmpTargetThreshold> thresholds) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (PnmpTargetThreshold pnmpTargetThreshold : thresholds) {
                sqlSession.update(getNameSpace("updatePnmpTargetThreshold"), pnmpTargetThreshold);
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
    public void updateThreshold(PnmpTargetThreshold threshold) {
        getSqlSession().update(getNameSpace("updateThreshold"), threshold);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold";
    }

}
