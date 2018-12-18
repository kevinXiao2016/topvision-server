/***********************************************************************
 * $Id: reportService.java,v1.0 2013-5-28 上午10:41:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013-5-28-上午10:41:00
 * 
 */
public interface StatReportService extends Service {

    /**
     * 查询用户管辖的所有地域的与设备的关系列表
     * 
     * @param entityType
     * @param map
     * @return
     */
    List<TopoEntityStastic> getTopoEntityRelation(Long entityType, Map<String, Object> map);

    /**
     * 通过查询条件中的folderIds获取所有权限folderIds（包含子地域）
     * @param folderIds
     * @return
     */
    List<Long> getAuthFolderIds(List<Long> folderIds);

}
