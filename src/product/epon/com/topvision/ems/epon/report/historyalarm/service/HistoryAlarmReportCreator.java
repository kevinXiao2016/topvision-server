/***********************************************************************
 * $Id: HistoryAlarmReportCreator.java,v1.0 2013-10-29 下午3:19:54 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.historyalarm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.report.domain.HistoryAlarmDetail;
import com.topvision.ems.report.domain.FolderEntities;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-29-下午3:19:54
 * 
 */
public interface HistoryAlarmReportCreator extends ReportCreator {

    /**
     * 获取历史告警报表详细
     * 
     * @param map
     * @return
     */
    List<HistoryAlarmDetail> statHistoryAlarmDetailReport(Map<String, Object> map);

    /**
     * 导出历史告警报表详细
     * 
     * @param historyAlarmDetails
     * @param statDate
     */
    void exportHistoryDetailReportToExcel(List<HistoryAlarmDetail> historyAlarmDetails, Date statDate);

    /**
     * 获取历史告警报表
     * 
     * @param queryMap
     * @return
     */
    Map<Long, FolderEntities> statHistoryAlarmReport(Map<String, Object> queryMap);

    /**
     * 导出历史告警报表
     * 
     * @param historyAlarmReport
     * @param columnDisable
     * @param statDate
     * @param conditions
     */
    void exportHistoryAlertReportToExcel(Map<Long, FolderEntities> historyAlarmReport,
            Map<String, Boolean> columnDisable, Date statDate, String conditions);

}
