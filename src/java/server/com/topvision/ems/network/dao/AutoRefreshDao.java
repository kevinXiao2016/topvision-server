/***********************************************************************
 * $Id: AutoRefreshDao.java,v1.0 2014-10-17 上午10:44:18 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.AutoRefreshConfig;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Rod John
 * @created @2014-10-17-上午10:44:18
 * 
 */
public interface AutoRefreshDao extends BaseEntityDao<Entity> {

    /**
     * Get Auto Refresh Config
     * 
     * @return
     */
    AutoRefreshConfig getAutoRefreshConfig();

    /**
     * Update Auto Refresh Config
     * 
     * @param config
     */
    void updateAutoRefreshConfig(AutoRefreshConfig config);

}
