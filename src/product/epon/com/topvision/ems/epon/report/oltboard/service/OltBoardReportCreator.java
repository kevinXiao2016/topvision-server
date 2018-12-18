/***********************************************************************
 * $Id: OltBoardReportCreator.java,v1.0 2013-10-26 上午9:38:18 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltboard.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.EponBoardStatistics;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-26-上午9:38:18
 * 
 */
public interface OltBoardReportCreator extends ReportCreator {

    /**
     * 获取OLT板卡列表信息
     * 
     * @param map
     * @return
     */
    public List<EponBoardStatistics> getBoardList(Map<String, Object> map);

    /**
     * 导出OLT板卡使用情况报表EXCEL文件以供下载
     * 
     * @param eponBoardStatistics
     * @param columnDisable
     * @param statDate
     */
    void exportOltBoardReportToExcel(List<EponBoardStatistics> eponBoardStatistics, Map<String, Boolean> columnDisable,
            Date statDate);
}
