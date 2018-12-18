/***********************************************************************
 * $Id: OltSniPortReportCreator.java,v1.0 2013-10-28 下午2:01:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniport.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-28-下午2:01:43
 * 
 */
public interface OltSniPortReportCreator extends ReportCreator {

    /**
     * 获取SNI端口报表
     * 
     * @param map
     * @return
     */
    List<OltSniAttribute> getSniPortList(Map<String, Object> map);

    /**
     * SNI端口报表导出Excel
     * 
     * @param snilist
     * @param statDate
     */
    void exportOltSniPortReportToExcel(List<OltSniAttribute> snilist, Date statDate);

}
