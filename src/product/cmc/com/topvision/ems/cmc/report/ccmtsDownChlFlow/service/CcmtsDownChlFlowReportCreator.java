/***********************************************************************
 * $Id: CcmtsDownChlFlowReportService.java,v1.0 2014-3-24 下午3:28:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsDownChlFlow.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CcmtsChlFlowDetail;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowStatic;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2014-3-24-下午3:28:59
 * 
 */
public interface CcmtsDownChlFlowReportCreator extends ReportCreator {

    /**
     * 获取下行信道流量信息
     * 
     * @param map
     * @return
     */
    Map<String, List<CcmtsChlFlowStatic>> statDownChlFlowStatic(Map<String, Object> map);

    /**
     * 导出主表excel
     * 
     * @param statDownChlFlowStatic
     * @param statDate
     */
    void exportReportToExcel(Map<String, List<CcmtsChlFlowStatic>> statDownChlFlowStatic, Date statDate,
            String conditions);

    /**
     * 获取下行信道流量详细
     * 
     * @param map
     * @return
     */
    Map<String, List<CcmtsChlFlowDetail>> statDownChlFlowDetail(Map<String, Object> map);

    void exportCcmtsDownChlDetailToExcel(Map<String, Object> map);

}
