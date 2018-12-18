/***********************************************************************
 * $Id: PerfTargetDao.java,v1.0 2013-8-1 上午09:23:32 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;

/**
 * @author lizongtian
 * @created @2013-8-1-上午09:23:32
 * 
 */
public interface PerfTargetDao {

    /**
     * 根据设备类型获取所有的指标分组
     * 
     * @param entityId
     * @return
     */
    List<String> getPerfTargetGroupsByDeviceType(Long entityId);

    /**
     * 根据ONU设备类型获取所有的指标分组
     * 
     * @param entityId
     * @return
     */
    List<String> getOnuPerfTargetGroupsByDeviceType(Long entityType);

    /**
     * 根据组名获取其下的指标名称
     * 
     * @param groupName
     * @param entityId
     * @return
     */
    List<String> getPerfNamesByGroup(String groupName, Long entityId);

    /**
     * 根据组名获取其下的指标名称
     * 
     * @param groupName
     * @param entityType
     * @return
     */
    List<String> getOnuPerfNamesByGroup(String groupName, Long entityType);

    /**
     * 获取指定设备类型的所有性能指标的名称
     * 
     * @param entityType
     * @return
     */
    List<String> getPerfTargetNameList(Long entityType);

    /**
     * 获取设备ID列表
     * 
     * @param type
     * @return
     */
    List<Long> getEntityIdList(Integer type);

    /**
     * 加载符合条件的设备
     * 
     * @param queryMap
     * @return
     */
    List<Entity> loadDevicesByMap(Map<String, Object> queryMap);

    Integer loadDevicesNumByMap(Map<String, Object> queryMap);

    /**
     * 根据typeid+perfIndex查询category大类
     * @param typeId
     * @param perfTarget
     * @return
     */
    String selectPerfTargetCategory(Long typeId, String perfTarget);

}
