/***********************************************************************
 * $Id: OltCpuReportDao.java,v1.0 2013-10-26 上午11:36:45 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltcpu.dao;

import java.util.List;

import com.topvision.ems.epon.domain.PerforamanceRank;

/**
 * @author haojie
 * @created @2013-10-26-上午11:36:45
 * 
 */
public interface OltCpuReportDao {

    /**
     * 获取OLT CPU排行信息
     * @param type
     * @return
     */
    List<PerforamanceRank> getEponCpuRank(Long type);

}
