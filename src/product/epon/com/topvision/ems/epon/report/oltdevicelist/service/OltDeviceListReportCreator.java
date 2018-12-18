/***********************************************************************
 * $Id: OltDeviceListReportCreator.java,v1.0 2013-10-26 下午2:50:47 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltdevicelist.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.DeviceListItem;
import com.topvision.ems.report.service.ReportCreator;

/**
 * @author haojie
 * @created @2013-10-26-下午2:50:47
 * 
 */
public interface OltDeviceListReportCreator extends ReportCreator {

    /**
     * 导出OLT设备清单到EXCEL
     * 
     * @param queryMap
     * @param statDate
     * @param out
     */
    void exportAsExcelFromRequest(Map<String, Object> queryMap, Date statDate, OutputStream out);

    /**
     * 获取OLT设备列表
     * 
     * @param map
     * @return
     */
    List<DeviceListItem> getDeviceListItem(Map<String, Object> map);
}
