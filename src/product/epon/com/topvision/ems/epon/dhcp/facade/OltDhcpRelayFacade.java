/***********************************************************************
 * $Id: OltDhcpFacade.java,v1.0 2013-10-25 下午5:56:36 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.facade;

import java.util.List;

import com.topvision.ems.epon.dhcp.domain.DhcpBundle;
import com.topvision.ems.epon.dhcp.domain.DhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpIntIpConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpOption60;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtDevice;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayExtServerConfig;
import com.topvision.ems.epon.dhcp.domain.DhcpRelayVlanMap;
import com.topvision.ems.epon.dhcp.domain.DhcpServerConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午5:56:36
 *
 */
@EngineFacade(serviceName = "OltDhcpRelayFacade", beanName = "oltDhcpRelayFacade")
public interface OltDhcpRelayFacade extends Facade {
    /**
     * 修改DHCP Bundle
     * 
     * @param snmpParam SnmpParam
     * @param dhcpBundle DhcpBundle
     * @return DhcpBundle
     */
    DhcpBundle modifyDhcpRelayBundleInfo(SnmpParam snmpParam, DhcpBundle dhcpBundle);

    /**
     * 修改DHCP 服务器
     * 
     * @param snmpParam SnmpParam
     * @param dhcpServerConfig DhcpServerConfig
     * @return   DhcpServerConfig
     */
    DhcpServerConfig modifyDhcpRelayServerInfo(SnmpParam snmpParam, DhcpServerConfig dhcpServerConfig);

    /**
     * 修改DHCP 中继
     * 
     * @param snmpParam SnmpParam
     * @param dhcpGiAddr  DhcpGiaddrConfig
     * @return   DhcpGiaddrConfig
     */
    DhcpGiaddrConfig modifyDhcpRelayGiAddrInfo(SnmpParam snmpParam, DhcpGiaddrConfig dhcpGiAddr);

    /**
     * 修改DHCP Option60
     * 
     * @param snmpParam SnmpParam
     * @param dhcpOption60 DhcpOption60
     * @return  DhcpOption60
     */
    DhcpOption60 modifyDhcpRelayOption60Info(SnmpParam snmpParam, DhcpOption60 dhcpOption60);

    /**
     * 修改虚拟IP
     * @param snmpParam
     * @param virtualIp
     * @return
     */
    DhcpIntIpConfig modifyDhcpRelayIntIp(SnmpParam snmpParam, DhcpIntIpConfig virtualIp);

    /**
     * 获取Dhcp Bundle信息
     * 
     * @param snmpParam SnmpParam
     * @return List<DhcpBundle>
     */
    List<DhcpBundle> getCmcDhcpRelayBundleInfo(SnmpParam snmpParam);

    /**
     * 获取Dhcp Server信息
     * 
     * @param snmpParam SnmpParam
     * @return List<DhcpServerConfig>
     */
    List<DhcpServerConfig> getDhcpRelayServerConfigInfo(SnmpParam snmpParam);

    /**
     * 获取Dhcp 中继信息
     * 
     * @param snmpParam SnmpParam
     * @return List<DhcpGiaddrConfig>
     */
    List<DhcpGiaddrConfig> getDhcpRelayGiAddrInfo(SnmpParam snmpParam);

    /**
     * 获取Dhcp Option60信息
     * 
     * @param snmpParam SnmpParam
     * @return List<DhcpOption60>
     */
    List<DhcpOption60> getDhcpRelayOption60Info(SnmpParam snmpParam);

    /**
     * 获取DHCP virtual IP信息
     * @param snmpParam
     * @return List<DhcpIntIpConfig>
     */
    List<DhcpIntIpConfig> getDhcpRelayIntIp(SnmpParam snmpParam);

    /**
     * 获取DHCP Relay开关状态
     * @param snmpParam
     * @return
     */
    Integer getDhcpRelaySwitch(SnmpParam snmpParam);

    /**
     * 修改DHCP Relay开关
     * @param snmpParam
     * @param dhcpRelaySwitch
     * @return
     */
    Integer modifyDhcpRelaySwitch(SnmpParam snmpParam, Integer dhcpRelaySwitch);

    /**
     * 修改DHCP Relay扩展设备Option
     * @param snmpParam
     * @param dhcpRelayExtDevice
     * @return
     */
    DhcpRelayExtDevice modifyDhcpRelayExtDeviceOption(SnmpParam snmpParam, DhcpRelayExtDevice dhcpRelayExtDevice);

    /**
     * 修改DHCP Relay扩展设备Giaddr
     * @param snmpParam
     * @param dhcpRelayExtDevice
     * @return
     */
    DhcpRelayExtGiaddrConfig modifyDhcpRelayExtGiaddr(SnmpParam snmpParam, DhcpRelayExtGiaddrConfig dhcpRelayExtGiaddr);

    /**
     * 修改DHCP Relay扩展设备Server
     * @param snmpParam
     * @param dhcpRelayExtServer
     * @return
     */
    DhcpRelayExtServerConfig modifyDhcpRelayExtServer(SnmpParam snmpParam, DhcpRelayExtServerConfig dhcpRelayExtServer);

    /**
     * 修改DHCP Relay VLAN map设置
     * @param snmpParam
     * @param dhcpRelayVlanMap
     * @return
     */
    DhcpRelayVlanMap modifyDhcpRelayVlanMap(SnmpParam snmpParam, DhcpRelayVlanMap dhcpRelayVlanMap);

    /**
     * 获取VLAN Map配置
     * @param snmpParam
     * @return
     */
    List<DhcpRelayVlanMap> getDhcpRelayVlanMaps(SnmpParam snmpParam);

    /**
     * 获取DHCP Relay 扩展设备Option60
     * @param snmpParam
     * @return
     */
    List<DhcpRelayExtDevice> getDhcpRelayExtDeviceOptions(SnmpParam snmpParam);

    /**
     * 获取DHCP Relay扩展设备Giaddr
     * @param snmpParam
     * @return
     */
    List<DhcpRelayExtGiaddrConfig> getDhcpRelayExtGiaddrs(SnmpParam snmpParam);

    /**
     * 获取Dhcp Relay 扩展设备Dhcp Server
     * @param snmpParam
     * @return
     */
    List<DhcpRelayExtServerConfig> getDhcpRelayExtServers(SnmpParam snmpParam);
}
