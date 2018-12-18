/***********************************************************************
 * $Id: OnuDeviceListReportDao.java,v1.0 2013-10-28 下午4:44:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.onudevice.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;

/**
 * @author haojie
 * @created @2013-10-28-下午4:44:43
 * 
 */
public interface OnuDeviceListReportDao {

    /**
     * 获取ONU报表
     * 
     * @param map
     * @return
     */
    List<OltOnuAttribute> getOnuDeviceListItem(Map<String, Object> map);

}
