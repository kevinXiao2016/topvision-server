/***********************************************************************
 * $Id: C3p0PoolService.java,v1.0 2014-5-17 上午10:49:43 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.service;

import java.sql.SQLException;
import java.util.List;

import com.topvision.ems.admin.domain.DataSourceInfo;

/**
 * @author Rod John
 * @created @2014-5-17-上午10:49:43
 * 
 */
public interface C3p0PoolService {
    /**
     * Get Database Pool Info
     * 
     * @return
     * @throws SQLException
     */
    List<String> databasePool() throws SQLException;

    /**
     * Get Show Full Processlist Info
     * 
     * @return
     */
    List<DataSourceInfo> getDataSourceInfos();
}
