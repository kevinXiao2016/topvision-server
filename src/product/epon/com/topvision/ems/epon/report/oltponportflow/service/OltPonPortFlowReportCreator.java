/***********************************************************************
 * $Id: OltPonPortFlowReportCreator.java,v1.0 2013-10-28 上午10:48:09 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponportflow.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.report.domain.OltPonPortFlowDetail;
import com.topvision.ems.epon.report.domain.OltPonPortFlowStastic;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-28-上午10:48:09
 * 
 */
public interface OltPonPortFlowReportCreator extends ReportCreator {

    /**
     * 获取PON流量报表
     * 
     * @param map
     * @return
     */
    Map<String, List<OltPonPortFlowStastic>> statPonFlowReport(Map<String, Object> map);

    /**
     * 导出PON流量报表EXCEL
     * 
     * @param parMap
     * @param statDate
     * @param out
     */
    void exportPonPortFlowPortToExcel(Map<String, Object> parMap, Date statDate, OutputStream out);

    /**
     * 获取PON流量报表详细
     * 
     * @param map
     * @return
     */
    List<OltPonPortFlowDetail> getPonFlowDetail(Map<String, Object> map);

    /**
     * 导出PON口流量详细EXCEL
     * 
     * @param map
     * @param statDate
     * @param out
     */
    void exportPonFlowDetailToExcel(Map<String, Object> map, Date statDate, OutputStream out);

}
