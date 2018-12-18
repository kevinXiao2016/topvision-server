/***********************************************************************
 * $Id: EntityExportService.java,v1.0 2013-11-1 上午8:34:09 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2013-11-1-上午8:34:09
 * 
 */
public interface EntityExportService extends Service {
    /**
     * 查询设备
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<Entity> getEntity(Map<String, Object> map, int start, int limit);

    /**
     * 查询设备数量
     * 
     * @param map
     * @return
     */
    Long getEntityNum(Map<String, Object> map);

    /**
     * 导出设备到excel
     * 
     * @param entityList
     */
    void exportEntityToExcel(List<Entity> entityList);
    
    /**
     * 查询设备所在地域
     * 
     * @param long
     * @return
     */
    List<String> getEntityFolder(Long entityId);
}
