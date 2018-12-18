/***********************************************************************
 * $Id: OltDhcpService.java,v1.0 2013-10-25 下午5:42:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.service;

import java.util.List;

import com.topvision.ems.epon.dhcp.domain.DhcpBundle;
import com.topvision.ems.epon.dhcp.domain.DhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpIntIpConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpOption60;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayConfigSetting;
import com.topvision.ems.epon.dhcp.domain.DhcpServerConfig;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午5:42:08
 *
 */
public interface OltDhcpRelayService extends Service {
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
    List<DhcpIntIpConfig> getDhcpVirtualIPList(Long entityId, String bundleInterface);

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
    void addDhcpOption60(Long entityId, DhcpOption60 option60);

    /**
     * 添加一条DHCP Server配置
     * 
     * @param entityId 
     * @param server
     */
    void addDhcpServer(Long entityId, DhcpServerConfig server);

    /**
     * 获取bundle数据
     * @param entityId
     * @param bundleInterface
     * @return
     */
    DhcpBundle getCmcDhcpBundle(Long entityId, String bundleInterface);

    /**
     * 获取Server列表
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<DhcpServerConfig> getCmcDhcpServerList(Long entityId, String bundleInterface);

    /**
     * 获取Giaddr列表
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<DhcpGiaddrConfig> getCmcDhcpGiAddrList(Long entityId, String bundleInterface);

    /**
     * 获取虚拟IP列表
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<DhcpIntIpConfig> getCmcDhcpIntIpList(Long entityId, String bundleInterface);

    /**
     * 获取Option60列表
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<DhcpOption60> getCmcDhcpOption60List(Long entityId, String bundleInterface);

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
    List<DhcpIntIpConfig> getVirtulIpList(Long entityId);

    /**
     * 从设备获取DHCP Relay（bundle）配置信息
     * @param entityId
     */
    void refreshDhcpRelayConfig(Long entityId);

    /**
     * 修改DHCP Relay开关
     * @param entityId
     * @param dhcpRelaySwitch
     */
    void modifyDhcpRelaySwitch(Long entityId, Integer dhcpRelaySwitch);

    /**
     * 获取DHCP Relay开关
     * @param entityId
     * @param dhcpRelaySwitch
     * @return
     */
    Integer getDhcpRelaySwitch(Long entityId);

    /**
     * 获取bundle中的自定义设备类型
     * @param entityId
     * @param bundleInterface
     * @return
     */
    List<String> getDeviceTypes(Long entityId, String bundleInterface);
}
