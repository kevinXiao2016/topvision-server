/***********************************************************************
 * $Id: CmtsDeviceListReportDao.java,v1.0 2013-11-18 下午1:46:35 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.report.cmtsdevicelist.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;

/**
 * @author haojie
 * @created @2013-11-18-下午1:46:35
 * 
 */
public interface CmtsDeviceListReportDao {

    /**
     * 获取cmts报表数据
     * @param map
     * @return
     */
    List<CmcAttribute> getDeviceListItem(Map<String, Object> map);

}
