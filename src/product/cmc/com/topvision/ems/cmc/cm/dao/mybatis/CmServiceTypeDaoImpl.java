/***********************************************************************
 * $Id: CmServiceTypeDaoImpl.java,v1.0 2016年11月3日 上午10:31:26 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cm.dao.CmServiceTypeDao;
import com.topvision.ems.cmc.cm.domain.CmFileNameChangeLog;
import com.topvision.ems.cmc.cm.domain.CmServiceType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016年11月3日-上午10:31:26
 *
 */
@Repository("cmServiceTypeDao")
public class CmServiceTypeDaoImpl extends MyBatisDaoSupport<Object> implements CmServiceTypeDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cm.dao.mybatis.CmServiceTypeDaoImpl";
    }

    @Override
    public List<CmServiceType> getCmServiceTypeList() {
        return getSqlSession().selectList(getNameSpace("getCmServiceTypeList"));
    }

    @Override
    public void insertCmServiceType(CmServiceType cmServiceType) {
        getSqlSession().insert(getNameSpace("insertCmServiceType"), cmServiceType);
    }

    @Override
    public CmServiceType getCmServiceTypeById(String fileName) {
        return getSqlSession().selectOne(getNameSpace("getCmServiceTypeById"), fileName);
    }

    @Override
    public void updateCmServiceType(CmServiceType cmServiceType) {
        getSqlSession().update(getNameSpace("updateCmServiceType"), cmServiceType);
    }

    @Override
    public void deleteCmServiceType(String fileName) {
        getSqlSession().delete(getNameSpace("deleteCmServiceType"), fileName);
    }

    @Override
    public void deleteCmServiceType() {
        getSqlSession().delete(getNameSpace("clearCmServiceType"));
    }

    @Override
    public void batchInsertOrUpdateCmServiceType(List<CmServiceType> cmServiceTypes) {
        SqlSession session = getBatchSession();
        try {
            for (CmServiceType cmServiceType : cmServiceTypes) {
                session.insert(getNameSpace("insertCmServiceType"), cmServiceType);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("batchInsertOrUpdateCmServiceType error");
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<CmFileNameChangeLog> getlogs(Long cmId) {
        return getSqlSession().selectList(getNameSpace("getlogs"), cmId);
    }

}
