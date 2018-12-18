/***********************************************************************
 * $Id: ReportTxDataServiceImpl.java,v1.0 2015-4-28 上午9:06:16 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.topvision.ems.report.service.ReportTxDataService;
import com.topvision.report.core.ReportDataService;
import com.topvision.report.core.domain.DescrptionModel;
import com.topvision.report.core.domain.SelectModel;

/**
 * @author Rod John
 * @created @2015-4-28-上午9:06:16
 *
 */
@Service("reportTxDataService")
public class ReportTxDataServiceImpl implements ReportTxDataService {

    /* (non-Javadoc)
     * @see com.topvision.ems.report.service.ReportTxDataService#txFetchReportData(com.topvision.report.core.ReportDataService, java.util.Map)
     */
    @Override
    public List<Object> txFetchReportData(ReportDataService reportDataService, Map<String, String> query) {
        return reportDataService.fetchReportData(query);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.report.service.ReportTxDataService#fetchReportDescription(com.topvision.report.core.ReportDataService, java.util.Map)
     */
    @Override
    public List<DescrptionModel> fetchReportDescription(ReportDataService reportDataService, Map<String, String> query) {
        return reportDataService.fetchReportDescription(query);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.report.service.ReportTxDataService#fetchSelectConditionList(com.topvision.report.core.ReportDataService, java.lang.String)
     */
    @Override
    public List<SelectModel> fetchSelectConditionList(ReportDataService reportDataService, String conditionId) {
        return reportDataService.fetchSelectConditionList(conditionId);
    }

}
