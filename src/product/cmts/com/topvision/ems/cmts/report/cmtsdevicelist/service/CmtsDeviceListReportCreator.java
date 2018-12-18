/***********************************************************************
 * $Id: CmtsDeviceListReportCreator.java,v1.0 2013-11-18 下午1:45:31 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.report.cmtsdevicelist.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-11-18-下午1:45:31
 * 
 */
public interface CmtsDeviceListReportCreator extends ReportCreator {

    /**
     * 获取cmts报表数据
     * @param map
     * @return
     */
    List<CmcAttribute> getDeviceListItem(Map<String, Object> map);

    /**
     * 导出cmts报表到excel
     * @param deviceListItems
     * @param columnDisable
     * @param statDate
     */
    void exportCmtsDeviceListReportToExcel(List<CmcAttribute> deviceListItems, Map<String, Boolean> columnDisable,
            Date statDate);

}
