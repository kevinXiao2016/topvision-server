/***********************************************************************
 * $Id: CmDailyNumStaticReportDao.java,v1.0 2013-10-30 上午8:37:17 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmdailynumstatic.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CmDailyNumStaticReport;

/**
 * @author haojie
 * @created @2013-10-30-上午8:37:17
 * 
 */
public interface CmDailyNumStaticReportDao {

    /**
     * 获取cm实时数量信息
     * 
     * @param tableName
     * @param deviceType
     * @return
     */
    List<Map<String, String>> loadIdAndNamePairsFromTable(String tableName, Integer deviceType);

    /**
     * 获取cm数量最大值
     * 
     * @param map
     * @return
     */
    List<CmDailyNumStaticReport> loadCmcDailyMaxCmNum(Map<String, Object> map);

}
