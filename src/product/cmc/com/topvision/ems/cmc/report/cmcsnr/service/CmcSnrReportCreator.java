/***********************************************************************
 * $Id: CmcSnrReportCreator.java,v1.0 2013-10-29 下午4:37:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcsnr.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CmcSnrReportDetail;
import com.topvision.ems.cmc.report.domain.CmcSnrReportStatistics;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-29-下午4:37:59
 * 
 */
public interface CmcSnrReportCreator extends ReportCreator {

    /**
     * 获取cmc snr报表
     * 
     * @param map
     * @return
     */
    Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport(Map<String, Object> map);

    /**
     * 获取cmc snr报表详细
     * 
     * @param map
     * @return
     */
    List<CmcSnrReportDetail> getSnrReportDetail(Map<String, Object> map);

    /**
     * 导出cmc snr报表excel
     * 
     * @param parMap
     * @param statDate
     * @param out
     */
    void exportAsExcel(Map<String, Object> parMap, Date statDate, OutputStream out);

    /**
     * 导出CMC snr报表详细
     * 
     * @param parMap
     * @param statDate
     * @param out
     */
    void exportSnrDetailReportToExcel(Map<String, Object> parMap, Date statDate, OutputStream out);

}
