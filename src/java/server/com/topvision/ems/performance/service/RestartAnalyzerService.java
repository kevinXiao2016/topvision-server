/***********************************************************************
 * $Id: RestartAnalyzerService.java,v1.0 2013-2-21 下午4:48:21 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.domain.RestartCount;
import com.topvision.ems.performance.domain.RestartRecord;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013-2-21-下午4:48:21
 * 
 */
public interface RestartAnalyzerService extends Service {

    /**
     * 加载某一台设备的重启统计
     * @param map
     * @return
     * @throws SQLException
     */
    List<RestartCount> loadRestartStasticData(Map<String, String> map) throws SQLException;

    /**
     * 加载EPON设备列表
     * @return
     * @throws SQLException
     */
    List<Entity> loadEponDeviceList() throws SQLException;

    /**
     * 加载设备重启详细
     * @param map
     * @return
     * @throws SQLException
     */
    List<RestartRecord> loadRestartRecords(Map<String, String> map) throws SQLException;

    /**
     * 加载设备重启的报表
     * @param map
     * @return
     * @throws SQLException 
     */
    List<RestartCount> loadRestartStatistic(Map<String, String> map) throws SQLException;

}
