/***********************************************************************
 * $Id: OltRunningStatusReportDao.java,v1.0 2013-10-29 上午11:48:22 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.oltrunningstatus.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.OltRunningStatus;
import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2013-10-29-上午11:48:22
 * 
 */
public interface OltRunningStatusReportDao {

    /**
     * 获取OLT运行状态
     * 
     * @param relates
     * @param map
     * @return
     */
    Map<String, List<OltRunningStatus>> statOltRunningStatusReport(List<TopoEntityStastic> relates,
            Map<String, Object> map);

}
