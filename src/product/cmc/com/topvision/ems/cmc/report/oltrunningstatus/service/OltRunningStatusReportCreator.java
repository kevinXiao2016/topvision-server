/***********************************************************************
 * $Id: OltRunningStatusReportCreator.java,v1.0 2013-10-29 上午11:47:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.oltrunningstatus.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.OltRunningStatus;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-29-上午11:47:40
 * 
 */
public interface OltRunningStatusReportCreator extends ReportCreator {

    /**
     * 获取OLT运行状态信息
     * 
     * @param map
     * @return
     */
    Map<String, List<OltRunningStatus>> statOltRunningStatusReport(Map<String, Object> map);

    /**
     * 导出OLT运行状态
     * 
     * @param parMap
     * @param statDate
     * @param out
     */
    void exportOltRunningStatusToExcel(Map<String, Object> parMap, Date statDate, OutputStream out);

}
