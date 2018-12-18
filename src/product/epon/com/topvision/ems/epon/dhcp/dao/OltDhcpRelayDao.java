/***********************************************************************
 * $Id: OltDhcpDao.java,v1.0 2013-10-25 下午5:44:34 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.dhcp.domain.DhcpBundle;
import com.topvision.ems.epon.dhcp.domain.DhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpIntIpConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpOption60;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayVlanMap;
import com.topvision.ems.epon.dhcp.domain.DhcpServerConfig;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-下午5:44:34
 *
 */

public interface OltDhcpRelayDao extends BaseEntityDao<Object> {

    /**
     * 获取DHCP Bundle
     * 
     * @return
     */
    DhcpBundle getDhcpRelayBundle(Map<String, Object> map);

    /**
     * 修改一条DHCP Server
     * 
     * @param map
     */
    DhcpServerConfig getDhcpRelayServerConfig(Map<String, Object> map);

    /**
     * 获取DHCP Bundle 列表
     * 
     * @return
     */
    List<DhcpBundle> getDhcpRelayBundleList(Long entityId);

    /**
     * 删除一条DHCP Bundle
     * 
     * @param map
     */
    void deleteDhcpBundle(Map<String, Object> map);

    /**
     * 新增一条DHCP Bundle
     * 
     * @param cmcDhcpBundle
     */
    void addDhcpBundle(DhcpBundle cmcDhcpBundle);

    /**
     * 修改一条DHCP Bundle
     * 
     * @param cmcDhcpBundle
     */
    void updateDhcpBundle(DhcpBundle cmcDhcpBundle);

    /**
     * 获取DHCP Server 列表
     * 
     * @return
     */
    List<DhcpServerConfig> getDhcpRelayServerList(Map<String, Object> map);

    /**
     * 新增一条DHCP Server
     * 
     * @param cmcDhcpServerConfig
     */
    void addDhcpServer(DhcpServerConfig cmcDhcpServerConfig);

    /**
     * 获取DHCP GiAddr 列表
     * 
     * @return
     */
    List<DhcpGiaddrConfig> getDhcpRelayGiAddrList(Map<String, Object> map);

    /**
     * 获取DHCP IntIp 列表
     * 
     * @return
     */
    List<DhcpIntIpConfig> getDhcpRelayIntIpList(Long entityId);

    /**
     * 获取DHCP IntIp 列表
     * 
     * @return
     */
    List<DhcpIntIpConfig> getDhcpRelayIntIpListByMap(Map<String, Object> map);

    /**
     * 删除一条Virtual IP
     * @param entityId
     * @param bundleInterface
     * @param dhcpIntIndex
     */
    void deleteDhcpRelayIntIp(Long entityId, String bundleInterface, String ip);

    /**
     * 新增一条DHCP IntIp
     * 
     * @param DhcpIntIpConfig
     */
    void addDhcpRelayIntIp(DhcpIntIpConfig cmcDhcpIntIp);

    /**
     * 修改一条DHCP IntIp
     * 
     * @param cmcDhcpIntIp
     */
    void updateDhcpRelayIntIp(DhcpIntIpConfig cmcDhcpIntIp);

    /**
     * 修改一条DHCP Option60
     * 
     * @param map
     */
    DhcpOption60 getDhcpRelayOption60(Map<String, Object> map);

    /**
     * 获取DHCP Option60 列表
     * 
     * @return
     */
    List<DhcpOption60> getDhcpRelayOption60List(Map<String, Object> map);

    /**
     * 新增一条DHCP Option60
     * 
     * @param cmcDhcpOption60
     */
    void addDhcpRelayOption60(DhcpOption60 cmcDhcpOption60);

    /**
     * 批量插入GiAddr
     * @param entityId
     * @param cmcDhcpGiAdrList
     */
    void batchInsertDhcpRelayGiaddr(Long entityId, List<DhcpGiaddrConfig> cmcDhcpGiAdrList);

    /**
     * 批量更新GiAddr
     * @param entityId
     * @param cmcDhcpGiAdrList
     */
    void batchUpdateDhcpRelayGiaddr(Long entityId, List<DhcpGiaddrConfig> cmcDhcpGiAdrList);

    /**
     * 批量删除GiAddr
     * @param entityId
     * @param cmcDhcpGiAdrList
     */
    void batchDeleteDhcpRelayGiAddr(Long entityId, List<DhcpGiaddrConfig> cmcDhcpGiAdrList);

    /**
     * 批量插入Option60
     * @param entityId
     * @param cmcDhcpOption60List
     */
    void batchInsertDhcpRelayOption60(Long entityId, List<DhcpOption60> cmcDhcpOption60List);

    /**
     * 批量更新Option60
     * @param entityId
     * @param cmcDhcpOption60List
     */
    void batchUpdateDhcpRelayOption60(Long entityId, List<DhcpOption60> cmcDhcpOption60List);

    /**
     * 批量删除Option60
     * @param entityId
     * @param cmcDhcpOption60List
     */
    void batchDeleteDhcpRelayOption60(Long entityId, List<DhcpOption60> cmcDhcpOption60List);

    /**
     * 批量插入DHCP Server
     * @param entityId
     * @param cmcDhcpServerConfigList
     */
    void batchInsertDhcpRelayServer(Long entityId, List<DhcpServerConfig> cmcDhcpServerConfigList);

    /**
     * 批量更新DHCP Server
     * @param entityId
     * @param cmcDhcpServerConfigList
     */
    void batchUpdateDhcpRelayServer(Long entityId, List<DhcpServerConfig> cmcDhcpServerConfigList);

    /**
     * 批量删除DHCP Server
     * @param entityId
     * @param cmcDhcpServerConfigList
     */
    void batchDeleteDhcpRelayServer(Long entityId, List<DhcpServerConfig> cmcDhcpServerConfigList);

    /**
     * 获取GiAddr
     * @param entityId
     * @param bundleInterface
     * @param deviceType
     * @return
     */
    DhcpGiaddrConfig selectDhcpRelayGiAddr(Long entityId, String bundleInterface, Integer deviceType);

    /**
     * 获取DHCP Server索引列表
     * @param entityId
     * @param bundleInterface
     * @param deviceType
     * @return
     */
    List<Integer> selectDhcpRelayServerIndex(Long entityId, String bundleInterface, Integer deviceType,
            String deviceTypeStr);

    /**
     * 获取DHCP Option60索引列表
     * @param entityId
     * @param bundleInterface
     * @param deviceType
     * @return
     */
    List<Integer> selectDhcpRelayOption60Index(Long entityId, String bundleInterface, Integer deviceType,
            String deviceTypeStr);

    /**
     * 插入Option60
     * @param entityId
     * @param option60
     */
    void insertDhcpRelayOption60(Long entityId, DhcpOption60 option60);

    /**
     * 插入Server
     * @param entityId
     * @param server
     */
    void insertDhcpRelayServer(Long entityId, DhcpServerConfig server);

    /**
     * 批量插入cc dhcp服务器配置
     * 
     * @param dhcpServerConfigs
     * @param cmcId
     * @param entityId
     *            TODO 这里统一需要将entityid 改为cmcid
     */
    void batchInsertDhcpRelayServerConfig(final List<DhcpServerConfig> dhcpServerConfigs, final Long entityId);

    /**
     * 批量插入CC DHCP giaddress配置
     * 
     * @param giAddrs
     * @param cmcId
     * @param entityId
     */
    void batchInsertDhcpRelayGiaddrConfig(final List<DhcpGiaddrConfig> giAddrs, final Long entityId);

    /**
     * 批量插入CC DHCP Bundle配置
     * 
     * @param dhcpBundles
     * @param cmcId
     * @param entityId
     */
    void batchInsertDhcpRelayBundleConfig(final List<DhcpBundle> dhcpBundles, final Long entityId);

    /**
     * 批量插入CC DHCP Option60配置
     * 
     * @param dhcpOption60s
     * @param cmcId
     * @param entityId
     */
    void batchInsertDhcpRelayOption60Config(final List<DhcpOption60> dhcpOption60s, final Long entityId);

    /**
     * 批量插入CC DHCP Ip配置
     * 
     * @param intIps
     * @param entityId
     */
    void batchInsertDhcpRelayIntIpConfig(final List<DhcpIntIpConfig> intIps, final Long entityId);

    /**
     * 获取bundle中的设备类型
     * @param entityId
     * @param bundleInterface
     * @return 
     */
    List<String> selectDeviceTypes(Long entityId, String bundleInterface);

    /**
     * 更新 DhcpRelay Vlan map
     * @param entityId
     * @param dhcpRelayVlanMap
     */
    void updateDhcpRelayVlanMap(Long entityId, DhcpRelayVlanMap dhcpRelayVlanMap);

    /**
     * 批量更新VLAN MAP
     * @param entityId
     * @param dhcpRelayVlanMap
     */
    void batchUpdateDhcpRelayVlanMap(Long entityId, List<DhcpRelayVlanMap> dhcpRelayVlanMap);

    /**
     * 获取DHCP Relay开关状态
     * @param entityId
     * @return
     */
    Integer selectDhcpRelaySwitch(Long entityId);

    /**
     * 更新 DHCP Relay开关
     * @param entityId
     * @param relaySwitch
     */
    void updateDhcpRelaySwitch(Long entityId, Integer relaySwitch);

    /**
     * 插入DHCP Relay开关
     * @param entityId
     * @param relaySwitch
     */
    void insertDhcpRelaySwitch(Long entityId, Integer relaySwitch);

}
