package com.topvision.ems.network.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.EntityAvailableStat;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.PortPerfEx;
import com.topvision.ems.network.domain.StateStat;
import com.topvision.framework.dao.Dao;

public interface NetworkDao extends Dao {
    /**
     * 获取网络设备的延迟排行.
     * 
     * @return
     */
    List<EntitySnap> getDeviceDelayTop(Long type);

    /**
     * 获取网络设备的延迟列表
     * 
     * @return
     */
    List<EntitySnap> getDeviceDelayList(Map<String, Object> map);

    /**
     * 获得响应延时更多时的设备数量
     * 
     */
    Integer getDeviceDelayNum(Map<String, Object> map);

    /**
     * 获取网络设备响应超时
     * 
     * @return
     */
    List<EntitySnap> getDeviceDelayOut(Long type);

    /**
     * 获取网络设备响应超时列表
     * 
     * @return
     */
    List<EntitySnap> getDeviceDelayOutList(Map<String, Object> map);

    /**
     * 获得响应超时更多时的设备数量
     * 
     */
    Integer getDeviceDelayOutNum(Map<String, Object> map);

    /**
     * 获取网络设备关注列表
     * 
     * @return
     */
    List<EntitySnap> getDeviceAttentionList(Map<String, Object> map);

    /**
     * 获得关注设备数量
     * 
     */
    Integer getDeviceAttentionCount(Map<String, Object> map);

    /**
     * 获取网络设备的负载排行.
     * 
     * @param item
     * @return
     */
    List<EntitySnap> getNetworkDeviceLoadingTop(Map<String, Object> map);

    /**
     * 获取网络设备的负载排行.
     * 
     * @param item
     * @return
     */
    List<EntitySnap> getNetworkDeviceLoadingList(Map<String, Object> map);

    Integer getNetworkDeviceCount(Map<String, Object> map);

    /**
     * 获取端口的流量排行
     * 
     * @param map
     * @return
     */
    // FIXME 该方法没有被调用，并且从network.xml中对应sql来看不需要传递参数
    List<PortPerfEx> getPortFlowTop(Map<String, Long> map);

    /**
     * 获取端口的速度排行
     * 
     * @param map
     * @return
     */
    // FIXME 该方法没有被调用，并且从network.xml中对应sql来看不需要传递参数
    List<PortPerfEx> getPortRateTop(Map<String, Long> map);

    /**
     * 获取不同类型(cpu负载/内存负载/ping延时)的负载排行
     * 
     * @param str
     * @return
     */
    List<EntitySnap> getServerLoadingTop(String str);

    /**
     * 获取处于不同状态的设备数
     * 
     * @return
     */
    List<StateStat> statEntityCountByState();

    /**
     * 获取处于不同状态的可用设备数
     * 
     * @return
     */
    List<EntityAvailableStat> statServerAvailable();
}
