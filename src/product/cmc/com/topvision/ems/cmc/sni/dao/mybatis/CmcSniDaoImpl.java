/***********************************************************************
 * $Id: CmcSniDaoImpl.java,v1.0 2013-4-23 下午4:57:24 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.sni.dao.CmcSniDao;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.sni.facade.domain.CmcRateLimit;
import com.topvision.ems.cmc.sni.facade.domain.CmcSniConfig;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2013-4-23-下午4:57:24
 * 
 */
@Repository("cmcSniDao")
public class CmcSniDaoImpl extends MyBatisDaoSupport<Entity> implements CmcSniDao {

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.sni.domain.CmcSni";
    }

    @Override
    public CmcRateLimit queryCmcRateLimit(Long cmcId) {
        return (CmcRateLimit) getSqlSession().selectOne(getNameSpace() + "getCmcRateLimit", cmcId);
    }

    @Override
    public void updateCmcCpuPortRateLimit(CmcRateLimit cmcRateLimit) {
        getSqlSession().update(getNameSpace() + "updateCmcCpuPortRateLimit", cmcRateLimit);
    }

    @Override
    public void updateCmcSniRateLimit(CmcRateLimit cmcRateLimit) {
        getSqlSession().update(getNameSpace() + "updateCmcSniRateLimit", cmcRateLimit);

    }

    @Override
    public List<CmcPhyConfig> queryCmcPhyConfigList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcPhyConfigList", cmcId);
    }

    @Override
    public void batchUpdateCmcSniPhyConfig(final List<CmcPhyConfig> cmcPhyConfigList) {

        SqlSession sqlSession = getBatchSession();
        try {

            if (cmcPhyConfigList != null && cmcPhyConfigList.size() > 0) {
                for (CmcPhyConfig phyConfig : cmcPhyConfigList) {
                    sqlSession.update(getNameSpace() + "updateCmcSniPhyConfig", phyConfig);
                }
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
    public CmcSniConfig queryCmcSniConfig(Long cmcId) {
        return (CmcSniConfig) getSqlSession().selectOne(getNameSpace() + "getCmcSniConfig", cmcId);
    }

    @Override
    public void updateSniLoopbackStatus(CmcSniConfig cmcSniConfig) {
        getSqlSession().update(getNameSpace() + "updateSniLoopbackStatus", cmcSniConfig);

    }

    @Override
    public void updateStormLimitConfig(CmcRateLimit cmcRateLimit) {
        getSqlSession().update(getNameSpace() + "updateCmcStormLimit", cmcRateLimit);
    }

    @Override
    public void batchInsertOrUpdatePhyConfig(final List<CmcPhyConfig> cmcPhyConfigList) {
        SqlSession session = getBatchSession();
        try {
            for (CmcPhyConfig cmcPhyConfig : cmcPhyConfigList) {
                if (((Long) getSqlSession().selectOne(getNameSpace() + "getCmcPhyConfig", cmcPhyConfig)) != null) {
                    getSqlSession().update(getNameSpace() + "updateCmcPhyConfig", cmcPhyConfig);
                } else {
                    session.insert(getNameSpace() + "insertCmcPhyConfig", cmcPhyConfig);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertOrUpdateCmcRateLimit(CmcRateLimit cmcRateLimit) {
        if (getSqlSession().selectOne(getNameSpace() + "getCmcRateLimit", cmcRateLimit.getCmcId()) != null) {
            getSqlSession().update(getNameSpace() + "updateCmcRateLimit", cmcRateLimit);
        } else {
            getSqlSession().insert(getNameSpace() + "insertCmcRateLimit", cmcRateLimit);
        }
    }

    @Override
    public void insertOrUpdateCmcSniConfig(CmcSniConfig cmcSniConfig) {
        if (getSqlSession().selectOne(getNameSpace() + "getCmcSniConfig", cmcSniConfig.getCmcId()) != null) {
            getSqlSession().update(getNameSpace() + "updateCmcSniConfig", cmcSniConfig);
        } else {
            getSqlSession().insert(getNameSpace() + "insertCmcSniConfig", cmcSniConfig);
        }
    }

}
