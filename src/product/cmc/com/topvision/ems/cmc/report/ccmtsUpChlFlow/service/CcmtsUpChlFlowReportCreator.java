/***********************************************************************
 * $Id: CcmtsUpChlFlowReportService.java,v1.0 2014-3-24 下午3:25:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsUpChlFlow.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CcmtsChlFlowDetail;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowStatic;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2014-3-24-下午3:25:08
 * 
 */
public interface CcmtsUpChlFlowReportCreator extends ReportCreator {

    /**
     * 获取上行流量统计
     * 
     * @param map
     * @return
     */
    Map<String, List<CcmtsChlFlowStatic>> statUpChlFlowStatic(Map<String, Object> map);

    /**
     * 获取上行流量详细
     * 
     * @param map
     * @return
     */
    Map<String, List<CcmtsChlFlowDetail>> statUpChlFlowDetail(Map<String, Object> map);

    /**
     * 导出excel
     * 
     * @param statUpChlFlowStatic
     * @param statDate
     */
    void exportReportToExcel(Map<String, List<CcmtsChlFlowStatic>> statUpChlFlowStatic, Date statDate, String conditions);

    /**
     * 导出上行流量详细表到excel
     * @param map
     */
    void exportCcmtsUpChlDetailToExcel(Map<String, Object> map);

}
