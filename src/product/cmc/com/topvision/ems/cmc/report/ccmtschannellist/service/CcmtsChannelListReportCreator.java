/***********************************************************************
 * $Id: CcmtsChannelListReportCreator.java,v1.0 2013-10-29 上午8:46:31 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtschannellist.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CcmtsChannelReportLocation;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-29-上午8:46:31
 * 
 */
public interface CcmtsChannelListReportCreator extends ReportCreator {

    /**
     * 获取CCMTS信道报表
     * 
     * @param queryMap
     * @return
     */
    List<CcmtsChannelReportLocation> getCcmtsChannelUsageReport(Map<String, Object> queryMap);

    /**
     * 导出CCMTS信道报表
     * 
     * @param locations
     * @param statDate
     */
    void exportCcmtsDeviceListReportToExcel(List<CcmtsChannelReportLocation> locations, Date statDate);

}
