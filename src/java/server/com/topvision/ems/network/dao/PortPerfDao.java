package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.PortPerf;
import com.topvision.framework.dao.BaseEntityDao;

public interface PortPerfDao extends BaseEntityDao<PortPerf> {
    /**
     * 获取指定设备ID的端口状态信息.
     * 
     * @param entityId
     * @return
     */
    List<Port> getPortStatusByEntityId(Long entityId);

    /**
     * 批量修改指定端口的端口状态信息.
     * 
     * @param ports
     */
    void updatePortStatus(List<Port> ports);

    /**
     * 修改指定端口的端口状态信息.
     * 
     * @param port
     */
    void updatePortStatus(Port port);
}
