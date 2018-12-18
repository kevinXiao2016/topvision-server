/***********************************************************************
 * $Id: CcmtsChannelListReportDao.java,v1.0 2013-10-29 上午8:47:07 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtschannellist.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CcmtsChannelUsageDetail;

/**
 * @author haojie
 * @created @2013-10-29-上午8:47:07
 * 
 */
public interface CcmtsChannelListReportDao {

    /**
     * 获取CCMTS信道报表
     * 
     * @param queryMap
     * @return
     */
    List<CcmtsChannelUsageDetail> getCcmtsChannelUsageReport(Map<String, Object> queryMap);

}
