/***********************************************************************
 * $Id: HistoryAlarmReportDao.java,v1.0 2013-10-29 下午3:20:27 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.historyalarm.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.report.domain.HistoryAlarmDetail;
import com.topvision.ems.report.domain.FolderEntities;

/**
 * @author haojie
 * @created @2013-10-29-下午3:20:27
 * 
 */
public interface HistoryAlarmReportDao {

    /**
     * 获取历史告警报表详细
     * 
     * @param map
     * @return
     */
    List<HistoryAlarmDetail> statEntityHistoryAlarmDetail(Map<String, Object> map);

    /**
     * 获取历史告警报表
     * 
     * @param folderMap
     * @param map
     * @return
     */
    Map<Long, FolderEntities> statHistoryAlarmReport(Map<Long, FolderEntities> folderMap, Map<String, Object> map);

}
