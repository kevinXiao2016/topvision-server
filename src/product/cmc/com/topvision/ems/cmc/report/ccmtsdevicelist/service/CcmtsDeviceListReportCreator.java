/***********************************************************************
 * $Id: CcmtsDeviceListReportCreator.java,v1.0 2013-10-29 上午9:23:58 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsdevicelist.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-29-上午9:23:58
 * 
 */
public interface CcmtsDeviceListReportCreator extends ReportCreator {

    /**
     * 获取CCMTS清单报表
     * 
     * @param map
     * @return
     */
    List<CmcAttribute> getDeviceListItem(Map<String, Object> map);

    /**
     * 导出CCMTS清单报表
     * 
     * @param deviceListItems
     * @param columnDisable
     * @param statDate
     */
    void exportCcmtsDeviceListReportToExcel(List<CmcAttribute> deviceListItems, Map<String, Boolean> columnDisable,
            Date statDate);

}
