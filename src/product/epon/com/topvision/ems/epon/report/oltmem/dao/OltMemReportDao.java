/***********************************************************************
 * $Id: OltMemReportDao.java,v1.0 2013-10-26 下午5:11:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltmem.dao;

import java.util.List;

import com.topvision.ems.epon.domain.PerforamanceRank;

/**
 * @author haojie
 * @created @2013-10-26-下午5:11:30
 * 
 */
public interface OltMemReportDao {

    /**
     * 获取OLT 内存利用率报表
     * @param type
     * @return
     */
    List<PerforamanceRank> getEponMemRank(Long type);

}
