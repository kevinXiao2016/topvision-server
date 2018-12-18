/***********************************************************************
 * $Id: RestartAnalyzerDao.java,v1.0 2013-2-21 下午4:52:59 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.domain.RestartCount;
import com.topvision.ems.performance.domain.RestartRecord;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013-2-21-下午4:52:59
 * 
 */
public interface RestartAnalyzerDao extends BaseEntityDao<Entity> {

    /**
     * 加载某一台设备的重启统计
     * @param map
     * @return
     * @throws SQLException
     */
    List<RestartCount> selectRestartStasticData(Map<String, String> map) throws SQLException;

    /**
     * 加载EPON设备列表
     * @return
     * @throws SQLException
     */
    List<Entity> selectEponDeviceList(Long type) throws SQLException;

    /**
     * 加载设备重启详细
     * @param map
     * @return
     * @throws SQLException
     */
    List<RestartRecord> selectRestartRecords(Map<String, String> map) throws SQLException;

    /**
     * 加载设备重启的报表
     * @param map
     * @return
     * @throws SQLException 
     */
    List<RestartCount> loadRestartStatistic(Map<String, String> map) throws SQLException;;

}
