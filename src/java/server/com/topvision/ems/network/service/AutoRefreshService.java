/***********************************************************************
 * $Id: AutoRefreshService.java,v1.0 2014-10-15 下午1:58:43 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.topvision.ems.network.domain.AutoRefreshConfig;

/**
 * @author Rod John
 * @created @2014-10-15-下午1:58:43
 * 
 */
public interface AutoRefreshService {

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

    // Add by Victor@20160829增加方法用于admin状态查询
    public Integer getAutoRefreshPoolSize();
    
    ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor();

}
