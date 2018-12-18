/***********************************************************************
 * $Id: CmRealTimeUserStaticReporDao.java,v1.0 2013-10-30 上午10:13:50 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmrealtimeuserstatic.dao;

import java.util.List;

import com.topvision.ems.cmc.report.domain.CmRealTimeUserStaticReport;

/**
 * @author haojie
 * @created @2013-10-30-上午10:13:50
 * 
 */
public interface CmRealTimeUserStaticReportDao {

    /**
     * 获取cm实时用户报表
     * 
     * @return
     */
    List<CmRealTimeUserStaticReport> loadCmRealTimeUserStaticData();

}
