/***********************************************************************
 * $Id: AutoRefreshDaoImpl.java,v1.0 2014-10-17 上午11:10:22 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.AutoRefreshDao;
import com.topvision.ems.network.domain.AutoRefreshConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Rod John
 * @created @2014-10-17-上午11:10:22
 * 
 */
@Repository("autoRefreshDao")
public class AutoRefreshDaoImpl extends MyBatisDaoSupport<Entity> implements AutoRefreshDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.AutoRefreshDao#getAutoRefreshConfig()
     */
    @Override
    public AutoRefreshConfig getAutoRefreshConfig() {
        return this.getSqlSession().selectOne(getNameSpace("getAutoRefreshConfig"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.AutoRefreshDao#updateAutoRefreshConfig(com.topvision.ems.network
     * .domain.AutoRefreshConfig)
     */
    @Override
    public void updateAutoRefreshConfig(AutoRefreshConfig config) {
        this.getSqlSession().update(getNameSpace("updateAutoRefreshConfig"), config);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.AutoRefreshConfig";
    }

}
