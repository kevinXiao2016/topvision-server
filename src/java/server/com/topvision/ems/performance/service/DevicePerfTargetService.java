/***********************************************************************
 * $Id: DevicePerfTargetService.java,v1.0 2014-3-12 下午2:24:43 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.performance.domain.DevicePerfTarget;

/**
 * @author flack
 * @created @2014-3-12-下午2:24:43
 *
 */
public interface DevicePerfTargetService {
    /**
     * 获取所有设备性能指标列表
     * @param paramsMap
     * @return
     */
    List<DevicePerfTarget> getDevicePerfList(Map<String, Object> paramsMap);

    /**
     * 查询满足条件的设备数量
     * @param paramsMap
     * @return
     */
    int getDeviceNum(Map<String, Object> paramsMap);

    /**
     * 获取指定设备的所有性能指标数据
     * @param entityId
     * @return
     */
    List<DevicePerfTarget> getAllDevicePerfTarget(Long entityId);

    /**
     * 获取指定类型的全局性能指标
     * @param entityType
     * @return
     */
    List<DevicePerfTarget> getGlobalPerfTargetList(Long entityType);

    /**
     * 查询设备的性能指标以及对应的全局性能指标
     * @param entityId
     * @return
     */
    List<DevicePerfTarget> getDeviceGlobalPerfList(Long entityId);

    /**
     * 查询支持具体性能指标的所有设备 
     * @param paramsMap
     * @return
     */
    List<DevicePerfTarget> getPerfTargetDeviceList(Map<String, Object> paramsMap);

    /**
     * 根据设备类型和组名查询全局性能指标
     * @param groupName
     * @param entityType
     * @return
     */
    List<DevicePerfTarget> getGroupTargetByType(String groupName, Long entityType);

    /**
     * 根据设备类型和指标名称查询全局性能指标
     * @param groupName
     * @param entityType
     * @return
     */
    DevicePerfTarget getGlobalTargetByType(String targetName, Long entityType);

    /**
     * 查询支持具体性能指标的所有设备数量
     * @param paramsMap
     * @return
     */
    int getPerfTargetDeviceNum(Map<String, Object> paramsMap);

    /**
     * 查询具体设备类型所支持的性能指标
     * @param typeId
     * @return
     */
    List<DevicePerfTarget> getDeviceSupportTarget(Long typeId);

    /**
     * 根据具体的设备类型和组名查找性能指标
     * @param typeId
     * @param targetGroup
     * @return
     */
    List<DevicePerfTarget> getTargetByTypeIdAndGroup(Long typeId, String targetGroup);

    /**
     * 修改指定设备的性能指标值
     * @param perfTargetList
     * @param entityId
     */
    void modifyDevicePerfTarget(List<DevicePerfTarget> perfTargetList, Long entityId);

    /**
     * 修改全局性能指标值
     * @param perfTarget
     */
    void modifyGlobalPerfTarget(List<DevicePerfTarget> perfTargetList);

    /**
     * 批量插入设备性能指标
     * @param entityId
     * @param perfTargetList
     */
    void batchInsertDeviceTarget(Long entityId, List<DevicePerfTarget> perfTargetList);

    /**
     * 添加单个设备性能指标数据
     * @param target
     */
    void addDevicePerfTarget(DevicePerfTarget target);

    /**
     * 将当前设备的性能指标应用到同类型的设备
     * typeId如果为null,则表示不区分小类型
     * @param targetList
     * @param entityType
     * @param typeId
     * @param saveGlobalFlag
     * @return
     */
    Map<String, Object> applyTargetToAllDevice(List<DevicePerfTarget> targetList, Long entityType, Long typeId,
            boolean saveGlobalFlag);

    /**
     * 更新指定设备的指定的性能指标数据 
     * @param newTarget
     * @param entityIds
     */
    Map<String, Object> modifyDeviceSingleTarget(DevicePerfTarget newTarget, String entityIds);

    /**
     * 根据具体设备类型和指标名称查询指标数据
     * @param typeId
     * @param targetName
     * @return
     */
    DevicePerfTarget getTargetByTypeIdAndName(Long typeId, String targetName);

    /**
     * 判断设备的性能指标数据是否已经存在
     * @param entityId
     * @param perfTargetName
     * @return
     */
    boolean isTargetDataExists(Long entityId, String perfTargetName);
    
    boolean isPerfTargetDisabled(Long entityId, String perfTargetName);

}
