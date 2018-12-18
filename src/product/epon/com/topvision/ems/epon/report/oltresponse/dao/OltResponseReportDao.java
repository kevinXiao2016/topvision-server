/***********************************************************************
 * $Id: OltResponseReportDao.java,v1.0 2013-10-28 上午11:40:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltresponse.dao;

import java.util.List;

import com.topvision.ems.epon.domain.PerforamanceRank;

/**
 * @author haojie
 * @created @2013-10-28-上午11:40:39
 * 
 */
public interface OltResponseReportDao {

    /**
     * 获取设备延迟报表
     * @param type
     * @return
     */
    List<PerforamanceRank> getEponDelayRank(Long type);

}
