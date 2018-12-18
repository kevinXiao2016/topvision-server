/***********************************************************************
 * $Id: CmcSyslogConfigDaoImpl.java,v1.0 2013-4-26 下午4:55:18 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.syslog.dao.mybatis;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordTypeII;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.syslog.dao.CmcSyslogConfigDao;
import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordType;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfig;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfigForA;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author fanzidong
 * @created @2013-4-26-下午4:55:18
 * 
 */
@Repository("cmcSyslogConfigDao")
public class CmcSyslogConfigDaoImpl extends MyBatisDaoSupport<CmcSyslogConfig>
        implements CmcSyslogConfigDao {

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.syslog.domain.CmcSyslogConfig";
    }

    @Override
    public void insertCmcAllRecordTypes(
            final List<CmcSyslogRecordType> cmcSyslogRecordTypes) {

        SqlSession sqlSession = getBatchSession();
        try {

            for (CmcSyslogRecordType cmcSyslogRecordType : cmcSyslogRecordTypes) {
                sqlSession.insert(getNameSpace() + "insertCmcAllRecordTypes",
                        cmcSyslogRecordType);
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
     * @see com.topvision.ems.cmc.dao.CmcSyslogConfigDao#deleteCmcRecordType(java .lang.Long)
     */
    @Override
    public void deleteCmcRecordType(Long entityId) {
        getSqlSession()
                .delete(getNameSpace() + "deleteCmcRecordType", entityId);
    }

    @Override
    public List<CmcSyslogRecordType> getCmcRecordTypeMinLvl(Long entityId) {
        return getSqlSession().selectList(
                getNameSpace() + "getCmcRecordTypeMinLvl", entityId);
    }

    @Override
    public List<CmcSyslogRecordTypeII> getCmcRecordTypeMinLvlII(Long entityId) {
        return getSqlSession().selectList(
                getNameSpace() + "getCmcRecordTypeMinLvlII", entityId);
    }

    @Override
    public void updateRcdTypeMinEvtLvlII(CmcSyslogRecordTypeII cmcSyslogRecordTypeII) {
        // 判断该条记录是否存在，如果存在就更新，不存在就添加
        int count = getSqlSession().selectOne(getNameSpace() + "getRcdTypeMinEvtLvlII",
                cmcSyslogRecordTypeII);
        if (count > 0) {
            getSqlSession().update(getNameSpace() + "updateRcdTypeMinEvtLvlII",
                    cmcSyslogRecordTypeII);
        } else {
            getSqlSession().insert(getNameSpace() + "insertCmcAllRecordTypesII",
                    cmcSyslogRecordTypeII);
        }

    }

    @Override
    public void updateRcdTypeMinEvtLvl(CmcSyslogRecordType cmcSyslogRecordType) {
        // 判断该条记录是否存在，如果存在就更新，不存在就添加
        int count = getSqlSession().selectOne(getNameSpace() + "getRcdTypeMinEvtLvl",
                cmcSyslogRecordType);
        if (count > 0) {
            getSqlSession().update(getNameSpace() + "updateRcdTypeMinEvtLvl",
                    cmcSyslogRecordType);
        } else {
            getSqlSession().insert(getNameSpace() + "insertCmcAllRecordTypes",
                    cmcSyslogRecordType);
        }
    }

    @Override
    public void undoAllMinEvtLvls(Long entityId) {
        getSqlSession().update(getNameSpace() + "undoAllMinEvtLvls", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcSyslogConfigDao#updateCmcSyslogConfigParams
     * (com.topvision.ems.cmc.facade.domain.CmcSyslogConfig)
     */
    @Override
    public void updateCmcSyslogConfigParams(CmcSyslogConfig cmcSyslogConfig) {
        getSqlSession().update(getNameSpace() + "updateCmcSyslogConfigParams",
                cmcSyslogConfig);
    }


    @Override
    public void insertCmcSyslogConfig(CmcSyslogConfig cmcSyslogConfig) {
        deleteCmcSyslogConfig(cmcSyslogConfig.getEntityId());
        getSqlSession().insert(getNameSpace() + "insertCmcSyslogConfig",
                cmcSyslogConfig);
    }

    @Override
    public void insertCmcSyslogConfigForA(CmcSyslogConfigForA cmcSyslogConfig) {
        // 首先，删除该CMC的4条记录方式及配置记录
        deleteCmcSyslogConfig(cmcSyslogConfig.getEntityId());

        getSqlSession().insert(getNameSpace() + "insertCmcSyslogConfig",
                cmcSyslogConfig);
    }

    @Override
    public void deleteCmcSyslogConfig(Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteCmcSyslogConfig",
                entityId);
    }

    @Override
    public CmcSyslogConfig getCmcSyslogConfig(Long entityId) {
        return (CmcSyslogConfig) getSqlSession().selectOne(
                getNameSpace() + "getCmcSyslogConfig", entityId);
    }

}
