package com.topvision.ems.cmc.dhcp.service;

import java.util.List;

import com.topvision.ems.cmc.dhcp.domain.DhcpRelayConfig;
import com.topvision.ems.cmc.dhcp.domain.DhcpRelayConfigSetting;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpPacketVlan;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.framework.service.Service;

public interface CmcDhcpRelayService extends Service {
    /**
     * 获取DHCP Relay设置列表
     * 
     * @param entityId
     * @return
     */
    List<DhcpRelayConfig> getDhcpRelayConfigList(Long entityId);
    
    /**
     * 获取某个bundle下配置的虚拟IP
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<CmcDhcpIntIp> getDhcpVirtualIPList(Long entityId, String bundleInterface);

    /**
     * 添加一套DHCP Relay配置
     * 
     * @param entityId
     * @param setting
     *            包含bundleId、GiAddr List、HelpAddr List、Option60List
     */
    void addDhcpRelayConfig(Long entityId, DhcpRelayConfigSetting setting);

    /**
     * 修改DHCP Relay配置
     * 
     * @param entityId
     * @param addSetting
     * @param modifySetting
     * @param deleteSetting
     */
    void modifyDhcpRelayConfig(Long entityId, DhcpRelayConfigSetting setting);

    /**
     * 删除 DHCP Relay配置
     * 
     * @param entityId
     * @param bundleId
     */
    void deleteDhcpRelayConfig(Long entityId, String bundleId);

    /**
     * 添加一条option60配置
     * 
     * @param entityId 
     * @param option60
     */
    void addDhcpOption60(Long entityId, CmcDhcpOption60 option60);

    /**
     * 添加一条DHCP Server配置
     * 
     * @param entityId 
     * @param server
     */
    void addDhcpServer(Long entityId, CmcDhcpServerConfig server);
    
    /**
     * 修改DHCP 基本配置
     * @param cmcDhcpBaseConfigInfo
     */
    void modifyDhcpBaseConfig(CmcDhcpBaseConfig cmcDhcpBaseConfigInfo);
    
    /**
     * 获取bundle数据
     * @param entityId
     * @param bundleInterface
     * @return
     */
    CmcDhcpBundle getCmcDhcpBundle(Long entityId, String bundleInterface);
    
    /**
     * 获取Server列表
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<CmcDhcpServerConfig> getCmcDhcpServerList(Long entityId, String bundleInterface);
    
    /**
     * 获取Giaddr列表
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<CmcDhcpGiAddr> getCmcDhcpGiAddrList(Long entityId, String bundleInterface);
    
    /**
     * 获取虚拟IP列表
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<CmcDhcpIntIp> getCmcDhcpIntIpList(Long entityId, String bundleInterface);
    
    /**
     * 获取Option60列表
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<CmcDhcpOption60> getCmcDhcpOption60List(Long entityId, String bundleInterface);
    
    /**
     * 获取Packet Vlan
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<CmcDhcpPacketVlan> getCmcDhcpPacketVlanList(Long entityId, String bundleInterface);
    
    /**
     * 获取一台设备下的bundle号列表
     * @param entityId
     * @return
     */
    List<String> getCmcDhcpBundleEndList(Long entityId);
    
    /**
     * 获取一台设备下设置的虚拟IP列表
     * @param entityId
     * @return
     */
    List<CmcDhcpIntIp> getVirtulIpList(Long entityId);
    
    /**
     * 从设备获取DHCP 基本配置信息
     * @param entityId
     */
    void refreshDhcpBaseConfig(Long entityId);
    
    /**
     * 从设备获取DHCP Relay（bundle）配置信息
     * @param entityId
     */
    void refreshDhcpRelayConfig(Long entityId);
}
