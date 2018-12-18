/***********************************************************************
 * $Id: CmReportService.java,v1.0 2013-10-29 下午3:59:34 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-29-下午3:59:34
 * 
 */
public interface CmReportCreator extends ReportCreator {

    /**
     * 获取CM报表
     * 
     * @param map
     * @return
     */
    List<CmAttribute> statCmReport(Map<String, Object> map);

    /**
     * 导出CM报表
     * 
     * @param cmAttributes
     * @param columnDisable
     * @param statDate
     */
    void exportCmReportToExcel(List<CmAttribute> cmAttributes, Map<String, Boolean> columnDisable, Date statDate);

    /**
     * 加载设备选择框数据
     * @return
     */
    JSONObject loadFolderOltCmcLists();

}
