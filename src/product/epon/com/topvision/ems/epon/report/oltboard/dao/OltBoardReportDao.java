/***********************************************************************
 * $Id: OltBoardReportDao.java,v1.0 2013-10-26 上午9:40:55 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltboard.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.EponBoardStatistics;

/**
 * @author haojie
 * @created @2013-10-26-上午9:40:55
 * 
 */
public interface OltBoardReportDao {

    /**
     * 获取OLT板卡列表信息
     * 
     * @param map
     * @return
     */
    public List<EponBoardStatistics> getBoardList(Map<String, Object> map);

}
