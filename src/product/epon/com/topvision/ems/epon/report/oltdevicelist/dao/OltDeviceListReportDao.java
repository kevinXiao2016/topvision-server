/***********************************************************************
 * $Id: OltDeviceListReportDao.java,v1.0 2013-10-26 下午2:57:29 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltdevicelist.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.DeviceListItem;

/**
 * @author haojie
 * @created @2013-10-26-下午2:57:29
 * 
 */
public interface OltDeviceListReportDao {

    /**
     * 获取OLT列表信息
     * 
     * @param map
     * @return
     */
    List<DeviceListItem> getDeviceListItem(Map<String, Object> map);

}
