/***********************************************************************
 * $Id: PerfTargetService.java,v1.0 2013-8-1 上午08:42:34 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

import java.util.List;
import java.util.Map;

/**
 * @author lizongtian
 * @created @2013-8-1-上午08:42:34
 * 
 */
public interface PerfTargetService {

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
     * @param entityType
     * @return
     */
    List<String> getOnuPerfTargetGroupsByDeviceType(Long entityType);

    /**
     * 根据组名获取其下的指标名称
     * 
     * @param groupName
     * @param typeId
     * @return
     */
    List<String> getPerfTargetNamesByGroup(String groupName, Long typeId);

    /**
     * 根据ONU组名获取其下的指标名称
     * 
     * @param groupName
     * @param entityType
     * @return
     */
    List<String> getOnuPerfTargetNamesByGroup(String groupName, Long entityType);

    /**
     * 获取指定设备类型的所有性能指标的名称
     * 
     * @param entityType
     * @return
     */
    List<String> getPerfTargetNameList(Long entityType);

    Integer loadDevicePerfTargetListNum(Map<String, Object> queryMap);

    /**
     * 根据指标名称和设备类型获取对应的采集任务名称
     * @param perfTarget
     * @param type
     * @return
     */
    String getPerfTargetCategory(String perfTarget, Long type);

}
