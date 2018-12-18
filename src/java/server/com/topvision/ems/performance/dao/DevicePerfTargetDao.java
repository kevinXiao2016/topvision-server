/***********************************************************************
 * $Id: DevicePerfTargetDao.java,v1.0 2014-3-12 下午2:18:06 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.performance.domain.DevicePerfTarget;

/**
 * @author flack
 * @created @2014-3-12-下午2:18:06
 *
 */
public interface DevicePerfTargetDao {

    /**
     * 先查询满足条件的设备
     * @param paramsMap
     * @return
     */
    List<Long> queryDevice(Map<String, Object> paramsMap);

    /**
     * 获取所有设备性能指标列表
     * @param paramsMap
     * @return
     */
    List<DevicePerfTarget> queryDevicePerfList(Map<String, Object> paramsMap);

    /**
     * 查询满足条件的设备数量
     * @param paramsMap
     * @return
     */
    int queryDeviceNum(Map<String, Object> paramsMap);

    /**
     * 获取指定设备的所有性能指标数据
     * @param entityId
     * @return
     */
    List<DevicePerfTarget> queryAllDevicePerfTarget(Long entityId);

    /**
     * 获取指定类型的全局性能指标
     * @param entityType
     * @return
     */
    List<DevicePerfTarget> queryGlobalPerfTargetList(Long entityType);

    /**
     * 查询设备的性能指标以及对应的全局性能指标
     * @param entityId
     * @return
     */
    List<DevicePerfTarget> queryDeviceGlobalPerfList(Long entityId);

    /**
     * 查询支持具体性能指标的所有设备 
     * @param paramsMap
     * @return
     */
    List<Long> querySupportTargetDevice(Map<String, Object> paramsMap);

    /**
     * 查询支持具体性能指标的所有设备数量
     * @param perfTargetName
     * @return
     */
    int querySupportTargetDeviceNum(Map<String, Object> paramsMap);

    /**
     * 查询具体设备类型支持的性能指标
     * @param typeId
     * @return
     */
    List<DevicePerfTarget> queryDeviceSupportTarget(Long typeId);

    /**
     * 根据具体的设备类型和组名查找性能指标
     * @param paramMap
     * @return
     */
    List<DevicePerfTarget> queryTargetByTypeIdAndGroup(Map<String, Object> paramMap);

    /**
     * 查询指定类型或指定子类的设备性能指标数据 
     * @param paramMap
     * @return
     */
    List<DevicePerfTarget> queryDevicePerfByType(Map<String, Object> paramMap);

    /**
     * 根据设备类型和组名查询全局性能指标
     * @param groupName
     * @param entityType
     * @return
     */
    List<DevicePerfTarget> queryGroupTargetByType(String groupName, Long entityType);

    /**
     * 根据设备类型和指标名称查询全局性能指标 
     * @param groupName
     * @param entityType
     * @return
     */
    DevicePerfTarget queryGlobalTargetByType(String targetName, Long entityType);

    /**
     * 查询指定设备的指定的性能指标数据 
     * @param paramMap
     * @return
     */
    List<DevicePerfTarget> queryDeviceSingleTarget(Map<String, Object> paramMap);

    /**
     * 更新某一类型的设备性能指标数据
     * @param paramMap
     */
    void batchUdpateDevicePerfByType(List<DevicePerfTarget> targetList);

    /**
     * 更新指定的子类型的设备性能指标数据 
     * @param perfTarget
     */
    void batchUpdateDevicePerfByTypeId(List<DevicePerfTarget> targetList);

    /**
     * 修改指定设备的性能指标值
     * @param perfTarget
     */
    void updateDevicePerfTarget(DevicePerfTarget perfTarget);

    /**
     * 批量更新设备性能指标值
     * @param perfTargetList
     */
    void batchUpdateDevicePerf(List<DevicePerfTarget> perfTargetList);

    /**
     * 批量更新设备性能指标值
     * @param perfTargetList
     * @param entityId
     */
    void batchUpdateDeviceTarget(List<DevicePerfTarget> perfTargetList);

    /**
     * 修改全局性能指标值
     * @param perfTarget
     */
    void updateGLobalPerfTarget(DevicePerfTarget perfTarget);

    /**
     * 批量更新全局性能指标值
     * @param perfTargetList
     */
    void batchUpdateGlobalPerf(List<DevicePerfTarget> perfTargetList);

    /**
     * 插入设备性能指标数据,传入数据来自对应全局数据
     * @param perfTarget
     */
    void insertDevicePerfData(DevicePerfTarget perfTarget);

    void insertPerfTarget(DevicePerfTarget perfTarget);

    /**
     * 插入设备性能指标数据
     * @param perfTarget
     */
    void insertOrUpdatePerfTarget(DevicePerfTarget perfTarget);

    /**
     * 批量插入设备性能指标数据
     * @param perfTargetList
     */
    void batchInsertDevicePerf(Long entityId, List<DevicePerfTarget> perfTargetList);

    /**
     * 根据具体设备类型和指标名称查询指标数据
     * @param typeId
     * @param targetName
     * @return
     */
    DevicePerfTarget queryTargetByTypeIdAndName(Long typeId, String targetName);

    /**
     * 根据设备ID和指标名称获取轮询间隔
     * @param entityId
     * @param targetName
     * @return
     */
    Integer queryColIntervalByIdAndName(Long entityId, String targetName);

    /**
     * 删除指定性能指标的采集配置数据
     * @param entityId
     * @param perfTargetName
     */
    void deleteTargetCollectTime(Long entityId, String perfTargetName);

    /**
     * 查询指定的性能指标数量
     * 用以判断指定的性能指标是否已经存在数据
     * @param entityId
     * @param perfTargetName
     * @return
     */
    int querySpecialTargetCount(Long entityId, String perfTargetName);

    /**
     * 查询管理Ip
     * @param entityId
     * @return
     */
    String getManageIpById(Long entityId);

    Boolean isPerfTargetDisabled(Long entityId, String perfTargetName);

}
