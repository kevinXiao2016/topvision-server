package com.topvision.ems.network.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.Port;
import com.topvision.framework.dao.BaseEntityDao;

public interface PortDao extends BaseEntityDao<Port> {
    /**
     * 获取指定IP地址的端口信息.
     * 
     * @param ip
     * @return
     */
    Port getConnectedPortByIp(String ip);

    /**
     * 获取指定设备ID及端口号的端口信息.
     * 
     * @param entityId
     * @param ifIndex
     * @return
     */
    Port getPortByIfIndex(Long entityId, Long ifIndex);

    /**
     * 获取指定设备ID的端口和坐标信息.
     * 
     * @param entityId
     * @return
     */
    List<Port> getPortCoord(Long entityId);

    /**
     * 获取指定设备ID的端口信息.
     * 
     * @param entityId
     * @return
     */
    List<Port> getPortsByEntityId(Long entityId);

    /**
     * 批量插入/更新端口信息.
     * 
     * @param ports
     */
    void insertOrUpdateEntity(List<Port> ports);

    /**
     * 插入/更新端口信息.
     * 
     * @param port
     */
    void insertOrUpdateEntity(Port port);

    /**
     * 保存坐标信息.
     * 
     * @param ports
     */
    void savePortCoord(List<Port> ports);

    /**
     * 修改指定端口的基本信息.
     * 
     * @param port
     */
    void updatePortOutline(Port port);

    /**
     * 修改指定端口的状态信息.
     * 
     * @param port
     */
    void updatePortStatus(Port port);

    /**
     * 判断端口是否存在
     * 
     * @param port
     * @return
     */
    Long isPortExists(Port port);

    /**
     * 获取指定设备ID及端口号的端口信息.
     * 
     * @param entityId
     * @param ifIndex
     * @return
     */
    Map<String, Port> getPortCaches();

    /**
     * 更新端口信息.
     * 
     * @param port
     * @return
     */
    void updateEntity(Port port);

    /**
     * 获取指定设备ID及端口号的端口信息.
     * 
     * @param port
     * @return Port
     */
    Port getPortFromDB(Port port);
}
