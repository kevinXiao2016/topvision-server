package com.topvision.ems.performance.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topvision.ems.performance.domain.Monitor;
import com.topvision.ems.performance.domain.MonitorValue;
import com.topvision.framework.dao.BaseEntityDao;

public interface MonitorDao extends BaseEntityDao<Monitor> {
    /**
     * 判断设备监视器是否存在.
     * 
     * @param entityId
     * @param category
     * @return
     */
    Boolean existMonitor(Long entityId, String category);

    /**
     * 获取所有设备监视器.
     * 
     * @return
     */
    List<Monitor> getAllMonitor();

    /**
     * 获取给定设备的所有设备监视器.
     * 
     * @param entityId
     * @return
     */
    List<Monitor> getMonitorByEntity(Long entityId);

    /**
     * 获取给定设备类型的所有设备监视器.
     * 
     * @param type
     * @return
     */
    List<Monitor> getMonitorByEntityType(Integer type);

    /**
     * 获取给定监视类型的所有设备监视器.
     * 
     * @param type
     * @return
     */
    List<Monitor> getMonitorByType(String type);

    /**
     * 获取最后得到的设备监视器信息.
     * 
     * @param monitorId
     * @return
     */
    List<MonitorValue> getLastValuesByMonitor(Long monitorId);

    /**
     * 将设备监视器信息插入连通表.
     * 
     * @param value
     */
    void insertConnectivity(MonitorValue value);

    /**
     * 将设备监视器信息批量插入连通表.
     * 
     * @param values
     */
    void insertConnectivity(List<MonitorValue> values);

    /**
     * 插入设备监视器信息.
     * 
     * @param value
     */
    void insertValue(MonitorValue value);

    /**
     * 批量插入设备监视器信息.
     * 
     * @param values
     */
    void insertValue(List<MonitorValue> values);

    /**
     * 修改给定设备监视器轮询时间.
     * 
     * @param ids
     * @param intervalOfNormal
     * @param intervalAfterError
     */
    void updatePollingInterval(List<Long> ids, Long intervalOfNormal, Long intervalAfterError);

    /**
     * 根据类型批量修改轮询时间.
     * 
     * @param map
     */
    void updatePollingInterval(Map<String, String> map);

    /**
     * 修改监视器状态.
     * 
     * @param ids
     * @param status
     */
    void updateStatus(Long id, Boolean status);

    /**
     * 修改监视器状态.
     * 
     * @param ids
     * @param status
     */
    void updateStatus(List<Long> ids, Boolean status);

    /**
     * 更新监视器状态
     * 
     * @param monitor
     */
    void updateHealthy(Monitor monitor);

    /**
     * 仅根据category即类型查询Monitor
     * 
     * @param category
     * @return
     */
    List<Monitor> getMonitorByCategory(String category);

    /**
     * 更新设备在线信息
     * 
     * @param entityId
     * @param delay
     * @param collectTime
     */
    void insertPerfConnectivity(Long entityId, Integer delay, Timestamp collectTime);
}
