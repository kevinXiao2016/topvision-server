/***********************************************************************
 * $Id: CmRealTimeUserStaticReporCreator.java,v1.0 2013-10-30 上午10:10:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmrealtimeuserstatic.service;

import java.util.Date;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.FolderOltRelation;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-30-上午10:10:30
 * 
 */
public interface CmRealTimeUserStaticReportCreator extends ReportCreator {

    /**
     * 获取cm实时用户统计报表
     * 
     * @return
     */
    Map<String, FolderOltRelation> statCmRealTimeUserStaticReport(Map<String, Object> queryMap);

    /**
     * 导出cm实时用户统计报表
     * 
     * @param cmRealTimeUserStaticReport
     * @param date
     */
    void exportReportToExcel(Map<String, FolderOltRelation> cmRealTimeUserStaticReport, Date date);

}
