/***********************************************************************
 * $Id: OltPonPortReportCreator.java,v1.0 2013-10-28 上午9:58:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponport.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-28-上午9:58:30
 * 
 */
public interface OltPonPortReportCreator extends ReportCreator {

    /**
     * 获取PON口报表
     * 
     * @param map
     * @return
     */
    List<OltPonAttribute> getPonPortList(Map<String, Object> map);

    /**
     * 导出PON口报表Excel
     * 
     * @param ponlist
     * @param statDate
     */
    void exportOltPonPortReportToExcel(List<OltPonAttribute> ponlist, Date statDate);

}
