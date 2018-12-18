/***********************************************************************
 * $Id: RestartAnalyzerDaoImpl.java,v1.0 2013-2-21 下午4:53:22 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao.mybatis;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.dao.RestartAnalyzerDao;
import com.topvision.ems.performance.domain.RestartCount;
import com.topvision.ems.performance.domain.RestartRecord;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2013-2-21-下午4:53:22
 * 
 */
@Repository("restartAnalyzerDao")
public class RestartAnalyzerDaoImpl extends MyBatisDaoSupport<Entity> implements RestartAnalyzerDao {

    @Override
    public List<RestartCount> selectRestartStasticData(final Map<String, String> map) throws SQLException {
        return getSqlSession().selectList(getNameSpace("selectRestartStasticData"), map);
    }

    @Override
    public List<Entity> selectEponDeviceList(Long type) throws SQLException {
        return getSqlSession().selectList(getNameSpace("selectEponDeviceList"), type);
    }

    @Override
    public List<RestartRecord> selectRestartRecords(Map<String, String> map) throws SQLException {
        return getSqlSession().selectList(getNameSpace("selectRestartRecords"), map);
    }

    @Override
    public List<RestartCount> loadRestartStatistic(Map<String, String> map) throws SQLException {
        return getSqlSession().selectList(getNameSpace("selectRestartStatics"), map);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.performance.domain.RestartAnalyzer";
    }
}
