/***********************************************************************
 * $Id: CcmtsDeviceListReportDao.java,v1.0 2013-10-29 上午9:24:49 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsdevicelist.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;

/**
 * @author haojie
 * @created @2013-10-29-上午9:24:49
 * 
 */
public interface CcmtsDeviceListReportDao {

    /**
     * 获取CCMTS清单报表
     * 
     * @param map
     * @return
     */
    List<CmcAttribute> getDeviceListItem(Map<String, Object> map);

}
