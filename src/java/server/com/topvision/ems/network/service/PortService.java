package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.Port;
import com.topvision.exception.service.PortServiceException;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.InterceptorLog;

public interface PortService extends Service {
    /**
     * 获取给定IP的设备连接的端口.
     * 
     * @param ip
     * @return
     */
    Port getConnectedPortByIp(String ip);

    /**
     * 获取指定端口ID的端口信息.
     * 
     * @param portId
     * @return
     */
    Port getPort(Long portId);

    /**
     * 获取指定设备ID及端口号的端口信息.
     * 
     * @param entityId
     * @param ifIndex
     * @return
     */
    Port getPort(Long entityId, Long ifIndex);

    /**
     * 获取设备的所有端口.
     * 
     * @param entityId
     * @return
     */
    List<Port> getPortByEntity(Long entityId);

    /**
     * 获取指定设备ID的端口信息.
     * 
     * @param entityId
     * @return
     */
    List<Port> getPortCoord(Long entityId);

    /**
     * 保存面板图上的端口位置.
     * 
     * @param ports
     */
    void savePortCoord(List<Port> ports);

    /**
     * 关闭端口.
     * 
     * @param port
     * @param log
     * @return
     * @throws PortServiceException
     */
    Boolean txClosePort(Port port, InterceptorLog log) throws PortServiceException;

    /**
     * 打开端口.
     * 
     * @param port
     * @param log
     * @return
     * @throws PortServiceException
     */
    Boolean txOpenPort(Port port, InterceptorLog log) throws PortServiceException;

    /**
     * 更新端口信息.
     * 
     * @param port
     */
    void updatePort(Port port);

    Map<String, Port> getPortCaches();

}
