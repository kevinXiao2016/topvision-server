/***********************************************************************
 * $Id: EntityExportDao.java,v1.0 2013-11-1 上午8:35:31 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2013-11-1-上午8:35:31
 * 
 */
public interface EntityExportDao extends BaseEntityDao<Entity> {
    /**
     * 查询设备
     * 
     * @param map
     * @param start
     * @param limit
     * @return
     */
    List<Entity> selectEntity(Map<String, Object> map, int start, int limit);
    
    /**
     * 查询设备数量
     * 
     * @param map
     * @return
     */
    Long selectEntityNum(Map<String, Object> map);
    
    /**
     * 查询设备所在地域
     * 
     * @param long
     * @return
     */
    List<String> selectEntityFolder(Long entityId);
}
