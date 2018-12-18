/***********************************************************************
 * $Id: CmDailyNumStaticReportCreator.java,v1.0 2013-10-30 上午8:36:45 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmdailynumstatic.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-30-上午8:36:45
 * 
 */
public interface CmDailyNumStaticReportCreator extends ReportCreator {

    /**
     * 跳转cm 实时数量报表
     * 
     * @return
     */
    JSONObject loadFolderOltCmcLists();

    /**
     * 获取cm 实时数量报表数据
     * 
     * @param map
     * @return
     */
    JSONObject statCmDailyNumStaticReport(Map<String, Object> map);

}
