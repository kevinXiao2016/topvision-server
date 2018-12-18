/***********************************************************************
 * $Id: OltMemReportCreator.java,v1.0 2013-10-26 下午5:10:44 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltmem.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-26-下午5:10:44
 * 
 */
public interface OltMemReportCreator extends ReportCreator {

    /**
     * 获取OLT 内存利用率报表
     * 
     * @return
     */
    List<PerforamanceRank> getEponMemRank();

    /**
     * 导出EXCEL
     * 
     * @param statDate
     * @param out
     */
    void exportAsExcel(Date statDate, OutputStream out);

}
