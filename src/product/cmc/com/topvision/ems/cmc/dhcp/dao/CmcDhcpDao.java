/***********************************************************************
 * $Id: CmcDhcpDao.java,v1.0 2012-2-13 下午04:18:34 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpPacketVlan;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * DHCP相关功能
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午04:18:34
 * 
 */
public interface CmcDhcpDao extends BaseEntityDao<CmcEntity> {
    /**
     * 获取DHCP Bundle
     * 
     * @return
     */
    CmcDhcpBundle getCmcDhcpBundle(Map<String, Object> map);

    /**
     * 修改一条DHCP Server
     * 
     * @param map
     */
    CmcDhcpServerConfig getCmcDhcpServerConfig(Map<String, Object> map);

    /**
     * 修改一条DHCP GiAddr
     * 
     * @param map
     */
    CmcDhcpGiAddr getCmcDhcpGiAddr(Map<String, Object> map);

    /**
     * 修改一条DHCP Option60
     * 
     * @param map
     */
    CmcDhcpOption60 getCmcDhcpOption60(Map<String, Object> map);

    /**
     * 获取DHCP Bundle 列表
     * 
     * @return
     */
    List<CmcDhcpBundle> getCmcDhcpBundleList(Long entityId);

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
    void addDhcpBundle(CmcDhcpBundle cmcDhcpBundle);

    /**
     * 修改一条DHCP Bundle
     * 
     * @param cmcDhcpBundle
     */
    void updateDhcpBundle(CmcDhcpBundle cmcDhcpBundle);

    /**
     * 获取DHCP Server 列表
     * 
     * @return
     */
    List<CmcDhcpServerConfig> getCmcDhcpServerList(Map<String, Object> map);

    /**
     * 删除一条DHCP Server
     * 
     * @param map
     */
    void deleteDhcpServer(Map<String, Object> map);

    /**
     * 新增一条DHCP Server
     * 
     * @param cmcDhcpServerConfig
     */
    void addDhcpServer(CmcDhcpServerConfig cmcDhcpServerConfig);

    /**
     * 修改一条DHCP Server
     * 
     * @param cmcDhcpServerConfig
     */
    void updateDhcpServer(CmcDhcpServerConfig cmcDhcpServerConfig);

    /**
     * 获取DHCP GiAddr 列表
     * 
     * @return
     */
    List<CmcDhcpGiAddr> getCmcDhcpGiAddrList(Map<String, Object> map);

    /**
     * 删除一条DHCP GiAddr
     * 
     * @param map
     */
    void deleteDhcpGiAddr(Map<String, Object> map);

    /**
     * 新增一条DHCP GiAddr
     * 
     * @param cmcDhcpGiAddr
     */
    void addDhcpGiAddr(CmcDhcpGiAddr cmcDhcpGiAddr);

    /**
     * 修改一条DHCP GiAddr
     * 
     * @param cmcDhcpGiAddr
     */
    void updateDhcpGiAddr(CmcDhcpGiAddr cmcDhcpGiAddr);

    /**
     * 获取DHCP IntIp 列表
     * 
     * @return
     */
    List<CmcDhcpIntIp> getCmcDhcpIntIpList(Long entityId);

    /**
     * 获取DHCP IntIp 列表
     * 
     * @return
     */
    List<CmcDhcpIntIp> getCmcDhcpIntIpListByMap(Map<String, Object> map);

    /**
     * 删除一条Virtual IP
     * @param entityId
     * @param bundleInterface
     * @param dhcpIntIndex
     */
    void deleteDhcpIntIp(Long entityId, String bundleInterface, String ip);

    /**
     * 新增一条DHCP IntIp
     * 
     * @param cmcDhcpIntIp
     */
    void addDhcpIntIp(CmcDhcpIntIp cmcDhcpIntIp);

    /**
     * 修改一条DHCP IntIp
     * 
     * @param cmcDhcpIntIp
     */
    void updateDhcpIntIp(CmcDhcpIntIp cmcDhcpIntIp);

    /**
     * 获取DHCP index列表
     * 
     * @param cmcId
     *            flag
     */
    List<Integer> getCmcDhcpIndexList(Long cmcId, String bundle, Integer type, Integer flag);

    /**
     * 获取DHCP ifIndex列表
     * 
     * @param cmcId
     */
    List<Long> getCmcDhcpIfIndex(Long cmcId);

    /**
     * 获取DHCP Option60 列表
     * 
     * @return
     */
    List<CmcDhcpOption60> getCmcDhcpOption60List(Map<String, Object> map);

    /**
     * 删除一条DHCP Option60
     * 
     * @param map
     */
    void deleteDhcpOption60(Map<String, Object> map);

    /**
     * 新增一条DHCP Option60
     * 
     * @param cmcDhcpOption60
     */
    void addDhcpOption60(CmcDhcpOption60 cmcDhcpOption60);

    /**
     * 修改一条DHCP Option60
     * 
     * @param cmcDhcpOption60
     */
    void updateDhcpOption60(CmcDhcpOption60 cmcDhcpOption60);

    /**
     * 获取CC的类型
     * 
     * @param cmcId
     */
    Long getCmcDeviceStyleByCmcId(Long cmcId);

    /**
     * 批量插入GiAddr
     * @param entityId
     * @param cmcDhcpGiAdrList
     */
    void batchInsertDhcpGiaddr(Long entityId, List<CmcDhcpGiAddr> cmcDhcpGiAdrList);

    /**
     * 批量更新GiAddr
     * @param entityId
     * @param cmcDhcpGiAdrList
     */
    void batchUpdateDhcpGiaddr(Long entityId, List<CmcDhcpGiAddr> cmcDhcpGiAdrList);

    /**
     * 批量删除GiAddr
     * @param entityId
     * @param cmcDhcpGiAdrList
     */
    void batchDeleteDhcpGiAddr(Long entityId, List<CmcDhcpGiAddr> cmcDhcpGiAdrList);

    /**
     * 批量插入Option60
     * @param entityId
     * @param cmcDhcpOption60List
     */
    void batchInsertDhcpOption60(Long entityId, List<CmcDhcpOption60> cmcDhcpOption60List);

    /**
     * 批量更新Option60
     * @param entityId
     * @param cmcDhcpOption60List
     */
    void batchUpdateDhcpOption60(Long entityId, List<CmcDhcpOption60> cmcDhcpOption60List);

    /**
     * 批量删除Option60
     * @param entityId
     * @param cmcDhcpOption60List
     */
    void batchDeleteDhcpOption60(Long entityId, List<CmcDhcpOption60> cmcDhcpOption60List);

    /**
     * 批量插入DHCP Server
     * @param entityId
     * @param cmcDhcpServerConfigList
     */
    void batchInsertDhcpServer(Long entityId, List<CmcDhcpServerConfig> cmcDhcpServerConfigList);

    /**
     * 批量更新DHCP Server
     * @param entityId
     * @param cmcDhcpServerConfigList
     */
    void batchUpdateDhcpServer(Long entityId, List<CmcDhcpServerConfig> cmcDhcpServerConfigList);

    /**
     * 批量删除DHCP Server
     * @param entityId
     * @param cmcDhcpServerConfigList
     */
    void batchDeleteDhcpServer(Long entityId, List<CmcDhcpServerConfig> cmcDhcpServerConfigList);

    /**
     * 获取Packet Vlan
     * @param entityId
     * @param bundleInterface
     * @param devicetype
     * @return
     */
    CmcDhcpPacketVlan selectDhcpPacketVlan(Long entityId, String bundleInterface, Integer devicetype);

    /**
     * 获取 Bundle的Packet Vlan
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<CmcDhcpPacketVlan> selectDhcpPacketVlanList(Long entityId, String bundleInterface);

    /**
     * 插入Packet Vlan
     * @param entityId
     * @param cmcDhcpPacketVlan
     */
    void insertDhcpPacketVlan(Long entityId, CmcDhcpPacketVlan cmcDhcpPacketVlan);

    /**
     * 更新Packet Vlan
     * @param entityId
     * @param cmcDhcpPacketVlan
     */
    void updateDhcpPacketVlan(Long entityId, CmcDhcpPacketVlan cmcDhcpPacketVlan);

    /**
     * 删除Packet Vlan
     * @param entityId
     * @param bundleInterface
     * @param deviceType
     */
    void deleteDhcpPacketVlan(Long entityId, String bundleInterface, Integer deviceType);

    /**
     * 获取GiAddr
     * @param entityId
     * @param bundleInterface
     * @param deviceType
     * @return
     */
    CmcDhcpGiAddr selectDhcpGiAddr(Long entityId, String bundleInterface, Integer deviceType);

    /**
     * 获取DHCP Server索引列表
     * @param entityId
     * @param bundleInterface
     * @param deviceType
     * @return
     */
    List<Integer> selectDhcpServerIndex(Long entityId, String bundleInterface, Integer deviceType);

    /**
     * 获取DHCP Option60索引列表
     * @param entityId
     * @param bundleInterface
     * @param deviceType
     * @return
     */
    List<Integer> selectDhcpOption60Index(Long entityId, String bundleInterface, Integer deviceType);

    /**
     * 插入Option60
     * @param entityId
     * @param option60
     */
    void insertDhcpOption60(Long entityId, CmcDhcpOption60 option60);

    /**
     * 插入Server
     * @param entityId
     * @param server
     */
    void insertDhcpServer(Long entityId, CmcDhcpServerConfig server);

    /**
     * 插入或更新CC的DHCP config
     */
    void batchInsertOrUpdateCC8800BCmcDhcpBaseConfig(final CmcDhcpBaseConfig cmcDhcpBaseConfig, Long cmcId);

    /**
     * 批量插入Packet VLAN配置
     * 
     * @param packetVlan
     * @param entityId
     */
    void batchInsertCcDhcpPacketVlanConfig(List<CmcDhcpPacketVlan> packetVlan, Long entityId);

    /**
     * 批量插入CC DHCP Ip配置
     * 
     * @param intIps
     * @param entityId
     */
    void batchInsertCcDhcpIntIpConfig(final List<CmcDhcpIntIp> intIps, final Long entityId);

    /**
     * 批量插入CC DHCP Option60配置
     * 
     * @param dhcpOption60s
     * @param cmcId
     * @param entityId
     */
    void batchInsertCcDhcpOption60Config(final List<CmcDhcpOption60> dhcpOption60s, final Long cmcId,
            final Long entityId);

    /**
     * 批量插入CC DHCP Bundle配置
     * 
     * @param dhcpBundles
     * @param cmcId
     * @param entityId
     */
    void batchInsertCcDhcpBundleConfig(final List<CmcDhcpBundle> dhcpBundles, final Long cmcId, final Long entityId);

    /**
     * 批量插入CC DHCP giaddress配置
     * 
     * @param giAddrs
     * @param cmcId
     * @param entityId
     */
    void batchInsertCcDhcpGiaddrConfig(final List<CmcDhcpGiAddr> giAddrs, final Long cmcId, final Long entityId);

    /**
     * 批量插入cc dhcp服务器配置
     * 
     * @param dhcpServerConfigs
     * @param cmcId
     * @param entityId
     *            TODO 这里统一需要将entityid 改为cmcid
     */
    void batchInsertCcDhcpServerConfig(final List<CmcDhcpServerConfig> dhcpServerConfigs, final Long cmcId,
            final Long entityId);
}
