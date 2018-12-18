/***********************************************************************
 * $Id: CmPollConfigDaoImpl.java,v1.0 2017年1月14日 下午8:19:46 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.config.dao.mybatis;

import com.topvision.ems.cm.cmpoll.config.dao.CmPollConfigDao;
import com.topvision.ems.cm.cmpoll.config.domain.CmPollCollectParam;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jay
 * @created @2017年1月14日-下午8:19:46
 * 
 */
@Repository("cmPollConfigDao")
public class CmPollConfigDaoImpl extends MyBatisDaoSupport<CmPollCollectParam> implements CmPollConfigDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.cmpoll.config.domain.CmPollCollectParam";
    }

    @Override
    public void batchInsertSpecifiedCmList(List<String> macList) {
        getSqlSession().delete(getNameSpace("deleteSpecifiedCmList"));

        SqlSession batchSession = getBatchSession();
        try {
            for (String mac : macList) {
                batchSession.insert(getNameSpace("insertSpecifiedCmList"), mac);
            }
            batchSession.commit();
        } catch (Exception e) {
            logger.error("batch insert specified cm mac error:", e);
            batchSession.rollback();
        } finally {
            batchSession.close();
        }
    }

    @Override
    public List<String> selectSpecifiedCmList() {
        List<String> list = getSqlSession().selectList(getNameSpace("selectSpecifiedCmList"));
        return list;
    }
}
