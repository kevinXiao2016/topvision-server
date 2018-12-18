/***********************************************************************
 * $Id: CurrentAlarmReportDao.java,v1.0 2013-10-29 下午2:36:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.currentalarm.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.report.domain.CurrentAlarmDetail;
import com.topvision.ems.report.domain.FolderEntities;

/**
 * @author haojie
 * @created @2013-10-29-下午2:36:59
 * 
 */
public interface CurrentAlarmReportDao {

    /**
     * 获取当前告警报表详细
     * 
     * @param map
     * @return
     */
    List<CurrentAlarmDetail> statEntityCurrentAlarmDetail(Map<String, Object> map);

    /**
     * 获取当前告警报表
     * 
     * @param folderMap
     * @param map
     * @return
     */
    Map<Long, FolderEntities> statCurrentAlarmReport(Map<Long, FolderEntities> folderMap, Map<String, Object> map);

}
