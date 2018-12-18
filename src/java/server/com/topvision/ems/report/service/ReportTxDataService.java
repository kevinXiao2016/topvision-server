/***********************************************************************
 * $Id: ReportTxDataService.java,v1.0 2015-4-28 上午9:03:48 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service;

import java.util.List;
import java.util.Map;

import com.topvision.framework.annotation.DynamicDB;
import com.topvision.report.core.ReportDataService;
import com.topvision.report.core.domain.DescrptionModel;
import com.topvision.report.core.domain.SelectModel;

/**
 * @author Rod John
 * @created @2015-4-28-上午9:03:48
 *
 */
public interface ReportTxDataService {

    @DynamicDB
    List<Object> txFetchReportData(ReportDataService reportDataService, Map<String, String> query);

    List<DescrptionModel> fetchReportDescription(ReportDataService reportDataService, Map<String, String> query);

    List<SelectModel> fetchSelectConditionList(ReportDataService reportDataService, String conditionId);
}
