/***********************************************************************
 * $Id: CmcUserFlowReportCreator.java,v1.0 2013-10-29 下午5:06:51 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcuserflow.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CmcUserFlowReportDetail;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-29-下午5:06:51
 * 
 */
public interface CmcUserFlowReportCreator extends ReportCreator {

    /**
     * 获取cmc 用户数报表
     * 
     * @param map
     * @return
     */
    Map<String, Object> statCmcUserFlowReport(Map<String, Object> map);

    /**
     * 获取cmc 用户数报表详细
     * 
     * @param map
     * @return
     */
    Map<String, List<CmcUserFlowReportDetail>> getUserFlowDetail(Map<String, Object> map);

    /**
     * 导出cmc 用户数报表
     * 
     * @param parMap
     * @param statDate
     * @param out
     */
    void exportFlowReportToExcel(Map<String, Object> parMap, Date statDate, OutputStream out);

    /**
     * 导出cmc 用户数详细报表
     * @param parMap
     * @param statDate
     * @param out
     */
    void exportCmcUserFlowReportToExcel(Map<String, Object> parMap, Date statDate, OutputStream out);

}
