/**
 * 
 */
package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.HostService;
import com.topvision.ems.network.domain.EntityAvailableStat;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.PortPerfEx;
import com.topvision.ems.network.domain.ScanOptions;
import com.topvision.ems.network.domain.ServerTypeStat;
import com.topvision.ems.network.domain.Services;
import com.topvision.ems.network.domain.StateStat;
import com.topvision.framework.service.Service;

/**
 * 网络服务, 主要执行网络任务.
 * 
 * @author niejun
 * 
 */
public interface NetworkService extends Service {

    /**
     * 扫描给定的网络设备.
     * 
     * @param entity
     * @throws Exception
     */
    void scanEntity(Entity entity, ScanOptions options) throws Exception;

    /**
     * 扫描多个网络设备.
     * 
     * @param entities
     * @throws Exception
     */
    void scanEntity(List<Entity> entities, ScanOptions options) throws Exception;

    /**
     * 扫描给定的目标.
     * 
     * @param target
     * @throws Exception
     */
    void scanTarget(String target, ScanOptions options) throws Exception;

    /**
     * 得到所有需要被管理的主机服务.
     * 
     * @return
     * @throws Exception
     */
    List<HostService> getHostService() throws Exception;

    /**
     * 得到所有支持的服务列表.
     * 
     * @return
     * @throws Exception
     */
    List<Services> getNetworkServices() throws Exception;

    int getResponseTimeByEntity(String ip) throws Exception;

    /**
     * 设备响应排名.
     * 
     * @return
     * @throws Exception
     */
    List<EntitySnap> getDeviceDelayTop(Map<String, Object> map) throws Exception;

    Integer getDeviceDelayNum(Map<String, Object> map);

    /**
     * 设备响应超时.
     * 
     * @return
     * @throws Exception
     */
    List<EntitySnap> getDeviceDelayOut(Map<String, Object> map);

    Integer getDeviceDelayOutNum(Map<String, Object> map);

    /**
     * 关注设备列表
     * 
     * @return
     * @throws Exception
     */
    List<EntitySnap> getDeviceAttentionList(Map<String, Object> map);

    Integer getDeviceAttentionCount(Map<String, Object> map);

    @SuppressWarnings("rawtypes")
    List<PortPerfEx> getPortFlowTop(Map map) throws Exception;

    @SuppressWarnings("rawtypes")
    List<PortPerfEx> getPortRateTop(Map map) throws Exception;

    /**
     * 获取网络设备的负载排行.
     * 
     * @param map
     * @return
     * @throws Exception
     */
    List<EntitySnap> getNetworkDeviceLoadingTop(Map<String, Object> map);

    /**
     * 获取网络设备的负载排行.
     * 
     * @param map
     * @return
     * @throws Exception
     */
    List<EntitySnap> getNetworkDeviceLoadingList(Map<String, Object> map);

    /**
     * 获取网络设备数目
     * 
     * @param map
     * @return
     */
    Integer getNetworkDeviceCount(Map<String, Object> map);

    /**
     * 通过设备在线状态统计全网设备数量.
     * 
     * @return
     * @throws Exception
     */
    List<StateStat> statEntityCountByState() throws Exception;

    // LegalAddrBook getLegalAddrBook(String mac);
    //
    // void registerLegalAddrBook(LegalAddrBook book);

    // void unregisterLegalAddrBook(String mac);

    // /**
    // * 根据MAC地址获取AgentAddressBook.
    // *
    // * @param mac
    // * @return
    // */
    // AgentAddressBook getAgentAddressBook(String mac);
    //
    // /**
    // * 注册终端Agent地址簿.
    // *
    // * @param book
    // */
    // void registerAgentAddressBook(AgentAddressBook book);
    //
    // void registerAgentMacBook(AgentMacBook mac);

    // /**
    // * 注销终端Agent地址簿.
    // *
    // * @param agentId
    // */
    // void unregisterAgentAddressBook(long agentId);
    //
    // /**
    // * 设置终端最近活动时间, 反返回true表示成功, false表示没有找到agentId对应的记录
    // *
    // * @param agentId
    // * @param activeTime
    // */
    // boolean setAgentActiveTime(long agentId, long activeTime);

    /**
     * 得到服务器负载排行.
     * 
     * @return
     * @throws Exception
     */
    List<EntitySnap> getServerLoadingTop(String str) throws Exception;

    /**
     * 统计服务器的可用性.
     * 
     * @return
     * @throws Exception
     */
    List<EntityAvailableStat> statServerAvailable() throws Exception;

    /**
     * 按操作系统统计服务器.
     * 
     * @return
     * @throws Exception
     */
    List<ServerTypeStat> statServerByOs() throws Exception;

    // void probeService(Entity entity) throws Exception;

    // void probeService(List<Entity> entities) throws Exception;

}
