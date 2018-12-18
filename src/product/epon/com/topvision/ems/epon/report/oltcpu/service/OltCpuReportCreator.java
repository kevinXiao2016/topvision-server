/***********************************************************************
 * $Id: OltCpuReportCreator.java,v1.0 2013-10-26 上午11:36:11 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltcpu.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-26-上午11:36:11
 * 
 */
public interface OltCpuReportCreator extends ReportCreator {

    /**
     * 获取OLT CPU排行统计
     * 
     * @return
     */
    List<PerforamanceRank> getEponCpuRank();

    /**
     * 导出到客户端
     * 
     * @param statDate
     * @param out
     */
    void exportAsExcel(Date statDate, OutputStream out);
}
