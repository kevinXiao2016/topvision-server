/***********************************************************************
 * $Id: TelnetLoginDaoImpl.java,v1.0 2014年7月16日 上午9:13:02 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.TelnetLoginDao;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2014年7月16日-上午9:13:02
 *
 */
@Repository("telnetLoginDao")
public class TelnetLoginDaoImpl extends MyBatisDaoSupport<TelnetLogin> implements TelnetLoginDao{
    /*
        alter table telnetLoginConfig add column isAAA int(1) default 0;
     */
    @Override
    protected String getDomainName() {
        return TelnetLogin.class.getName();
    }
    @Override
    public Long getTelnetLoginConfigCount(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getTelnetLoginConfigCount"), map);
    }
    
    @Override
    public List<TelnetLogin> getTelnetLoginConfig(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("getTelnetLoginConfig"), map);
    }

    @Override
    public void deleteTelnetLogin(Long ip) {
        getSqlSession().delete(getNameSpace("deleteTelnetLogin"), ip);
    }


    @Override
    public void addTelnetLogin(TelnetLogin telnetLogin) {
        getSqlSession().insert(getNameSpace("addTelnetLogin"), telnetLogin);
    }

    @Override
    public void modifyTelnetLogin(TelnetLogin telnetLogin) {
        getSqlSession().update(getNameSpace("modifyTelnetLogin"), telnetLogin);
    }

    @Override
    public TelnetLogin getTelnetLoginConfigByIp(Long ip) {
        return getSqlSession().selectOne(getNameSpace("getTelnetLoginConfigByIp"), ip);
    }
    
    @Override
    public void batchInsertTelnetLogin(List<TelnetLogin> importTelnetLogins) {
        SqlSession session = getBatchSession();
        try {
            for (int i = 0; i < importTelnetLogins.size(); i++) {
                if(getTelnetLoginConfigByIp(importTelnetLogins.get(i).getIp()) == null){
                    getSqlSession().insert(getNameSpace("addTelnetLogin"), importTelnetLogins.get(i));
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
    public void batchInsertOrUpdateTelnetLogin(List<TelnetLogin> importTelnetLogins) {
        SqlSession session = getBatchSession();
        try {
            for (int i = 0; i < importTelnetLogins.size(); i++) {
                getSqlSession().delete(getNameSpace("deleteTelnetLogin"), importTelnetLogins.get(i).getIp());
                getSqlSession().insert(getNameSpace("addTelnetLogin"), importTelnetLogins.get(i));
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }
    
}
