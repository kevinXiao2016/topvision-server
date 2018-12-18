/***********************************************************************
 * $Id: OltSniPortFlowReportCreator.java,v1.0 2013-10-28 下午3:20:11 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniportflow.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.report.domain.OltSniPortFlowDetail;
import com.topvision.ems.epon.report.domain.OltSniPortFlowStastic;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-28-下午3:20:11
 * 
 */
public interface OltSniPortFlowReportCreator extends ReportCreator {

    /**
     * 获取SNI流量报表
     * 
     * @param map
     * @return
     */
    Map<String, List<OltSniPortFlowStastic>> statSniFlowReport(Map<String, Object> map);

    /**
     * 导出SNI流量报表
     * 
     * @param parMap
     * @param statDate
     * @param out
     */
    void exportSniPortFlowPortToExcel(Map<String, Object> parMap, Date statDate, OutputStream out);

    /**
     * 获取SNI流量详细
     * 
     * @param map
     * @return
     */
    List<OltSniPortFlowDetail> getSniFlowDetail(Map<String, Object> map);

    /**
     * 导出SNI流量详细
     * 
     * @param map
     * @param statDate
     * @param out
     */
    void exportSniFlowDetailToExcel(Map<String, Object> map, Date statDate, OutputStream out);

}
