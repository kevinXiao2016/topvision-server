/***********************************************************************
 * $Id: NbiDaoImpl.java,v1.0 2016-3-16 上午10:28:05 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.nbi.dao.NbiDao;
import com.topvision.ems.nbi.domain.NbiBaseConfig;
import com.topvision.ems.nbi.domain.NbiTarget;
import com.topvision.ems.nbi.domain.NbiTargetGroup;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.performance.nbi.api.NbiMultiPeriod;

/**
 * @author lizongtian
 * @created @2016-3-16-上午10:28:05
 *
 */
@Repository("nbiDao")
public class NbiDaoImpl extends MyBatisDaoSupport<Entity> implements NbiDao {

    @Override
    public NbiBaseConfig getNbiBaseConfig() {
        return getSqlSession().selectOne(getNameSpace("getNbiBaseConfig"));
    }

    @Override
    public NbiMultiPeriod getNbiMultiPeriod() {
        return getSqlSession().selectOne(getNameSpace("getNbiMultiPeriodConfig"));
    }

    @Override
    public List<NbiTargetGroup> getNbiTargetGroup(String module) {
        return getSqlSession().selectList(getNameSpace("selectNbiTargetGroupList"), module);
    }

    @Override
    public List<NbiTarget> getNbiTargetListByGroup(Integer groupId) {
        return getSqlSession().selectList(getNameSpace("selectNbiTargetListByGroup"), groupId);
    }

    @Override
    public void updateNbiBaseConfig(NbiBaseConfig baseConfig) {
        getSqlSession().delete(getNameSpace("deleteNbiBaseConfig"));
        getSqlSession().update(getNameSpace("insertNbiBaseConfig"), baseConfig);
    }

    @Override
    public void updateNbiMultiPeriod(NbiMultiPeriod nbiMultiPeriod) {
        getSqlSession().update(getNameSpace("updateNbiExportConfig"), nbiMultiPeriod);
    }

    @Override
    public void updateNbiTarget(List<NbiTarget> targets) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (NbiTarget target : targets) {
                sqlSession.update(getNameSpace("updateNbiTargetSelected"), target);
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
        return "com.topvision.ems.nbi.domain.Nbi";
    }

}
