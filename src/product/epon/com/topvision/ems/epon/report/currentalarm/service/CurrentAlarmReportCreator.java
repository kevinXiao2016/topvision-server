/***********************************************************************
 * $Id: CurrentAlarmReportCreator.java,v1.0 2013-10-29 下午2:36:29 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.currentalarm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.report.domain.CurrentAlarmDetail;
import com.topvision.ems.report.domain.FolderEntities;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-29-下午2:36:29
 * 
 */
public interface CurrentAlarmReportCreator extends ReportCreator {

    /**
     * 获取当前告警报表详细
     * 
     * @param map
     * @return
     */
    List<CurrentAlarmDetail> statCurrentAlarmDetailReport(Map<String, Object> map);

    /**
     * 获取当前告警报表
     * 
     * @param queryMap
     * @return
     */
    Map<Long, FolderEntities> statCurrentAlarmReport(Map<String, Object> queryMap);

    /**
     * 导出当前告警报表
     * 
     * @param currentAlarmReport
     * @param columnDisable
     * @param statDate
     * @param conditions
     */
    void exportCurAlertReportToExcel(Map<Long, FolderEntities> currentAlarmReport, Map<String, Boolean> columnDisable,
            Date statDate, String conditions);

    /**
     * 导出当前告警报表详细
     * 
     * @param currentAlarmDetails
     * @param statDate
     */
    void exportCurrentDetailReportToExcel(List<CurrentAlarmDetail> currentAlarmDetails, Date statDate);

}
