/***********************************************************************
 * $Id: CmReportDao.java,v1.0 2013-10-29 下午4:06:19 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cm.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.facade.domain.CmAttribute;

/**
 * @author haojie
 * @created @2013-10-29-下午4:06:19
 * 
 */
public interface CmReportDao {

    /**
     * 获取CM资源报表
     * 
     * @param map
     * @return
     */
    List<CmAttribute> getCmBasicInfoList(Map<String, Object> map);

    /**
     * 获取设备下拉框数据
     * 
     * @param tableName
     * @param deviceType
     * @return
     */
    List<Map<String, String>> loadIdAndNamePairsFromTable(String tableName, Long deviceType);

}
