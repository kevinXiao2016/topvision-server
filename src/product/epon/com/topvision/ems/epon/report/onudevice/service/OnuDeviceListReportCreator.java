/***********************************************************************
 * $Id: OnuDeviceListReportCreator.java,v1.0 2013-10-28 下午4:43:53 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.onudevice.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-28-下午4:43:53
 * 
 */
public interface OnuDeviceListReportCreator extends ReportCreator {

    /**
     * 获取ONU报表
     * 
     * @param map
     * @return
     */
    List<OltOnuAttribute> getOnuDeviceListItem(Map<String, Object> map);

    /**
     * 导出ONU报表
     * 
     * @param deviceListItems
     * @param columnDisable
     * @param statDate
     */
    void exportOnuDeviceListReportToExcel(List<OltOnuAttribute> deviceListItems, Map<String, Boolean> columnDisable,
            Date statDate);

}
