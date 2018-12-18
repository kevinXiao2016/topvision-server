/**
 * 
 */
package com.topvision.ems.realtime.service;

import java.util.List;

import com.topvision.ems.network.domain.Port;
import com.topvision.ems.realtime.domain.PortThresholdInfo;
import com.topvision.framework.service.Service;

/**
 * @author Administrator
 * 
 */
public interface PortRtStateService extends Service {

    /**
     * 得到设备的所有端口.
     * 
     * @param entityId
     * @return
     * @throws Exception
     */
    List<Port> getPortByEntity(long entityId) throws Exception;

    /**
     * 得到设备的端口阈值信息.
     * 
     * @param entityId
     * @return
     * @throws Exception
     */
    PortThresholdInfo getPortThresholdInfo(long entityId) throws Exception;

    /**
     * 注册给定设备ID的端口实时性能回调接口.
     * 
     * @param callback
     * @param entityId
     */
    void registerPortPerfCallback(PortPerfCallback callback, long entityId);

    /**
     * 注销给定设备ID的端口实时性能回调接口.
     * 
     * @param callback
     * @param entityId
     */
    void unregisterPortPerfCallback(PortPerfCallback callback, long entityId);

}
