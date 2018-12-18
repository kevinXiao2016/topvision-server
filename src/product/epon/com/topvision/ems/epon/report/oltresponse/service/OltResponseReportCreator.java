/***********************************************************************
 * $Id: OltResponseReportCreator.java,v1.0 2013-10-28 上午11:40:04 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltresponse.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-28-上午11:40:04
 * 
 */
public interface OltResponseReportCreator extends ReportCreator {

    /**
     * 获取OLT 设备响应报表
     * 
     * @return
     */
    List<PerforamanceRank> getEponDelayRank();

    /**
     * 导出OLT 设备响应报表
     * 
     * @param statDate
     * @param out
     */
    void exportAsExcel(Date statDate, OutputStream out);

}
