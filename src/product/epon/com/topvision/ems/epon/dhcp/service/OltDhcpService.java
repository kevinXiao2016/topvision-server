/***********************************************************************
 * $Id: OltDhcpService.java,v1.0 2013-10-25 下午5:42:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.service;

import java.util.List;

import com.topvision.ems.epon.dhcp.domain.OltDhcpBaseConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacStatic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpServerConfig;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午5:42:08
 *
 */
public interface OltDhcpService extends Service {

    /**
     * 获得DHCP 基本配置
     * 
     * @param entityId
     * @return
     */
    OltDhcpBaseConfig getDhcpBaseConfig(Long entityId);

    /**
     * 获得DHCP Server配置
     * 
     * @param entityId
     * @return
     */
    List<OltDhcpServerConfig> getDhcpServerConfigs(Long entityId);

    /**
     * 获得DHCP 网关配置
     * 
     * @param entityId
     * @return
     */
    List<OltDhcpGiaddrConfig> getDhcpGiaddrConfigs(Long entityId);

    /**
     * 获得DHCP 静态绑定配置
     * 
     * @param entityId
     * @return
     */
    List<OltDhcpIpMacStatic> getDhcpIpMacStatics(Long entityId);

    /**
     * 删除DHCP Server配置
     * 
     * @param entityId
     * @param dhcpServerIndex
     */
    void deleteDhcpServerConfigs(Long entityId, Integer dhcpServerIndex);

    /**
     * 新增DHCP Server配置
     * 
     * @param dhcpServerConfig
     */
    void insertDhcpServerConfigs(OltDhcpServerConfig dhcpServerConfig);

    /**
     * 更新DHCP Server配置
     * 
     * @param dhcpServerConfig
     */
    void updateDhcpServerConfigs(OltDhcpServerConfig dhcpServerConfig);

    /**
     * 更新DHCP 基本配置
     * 
     * @param dhcpBaseConfig
     */
    void modifyDhcpBaseConfigs(OltDhcpBaseConfig dhcpBaseConfig);

    /**
     * 删除DHCP 网关配置
     * 
     * @param entityId
     * @param dhcpGiaddrIndex
     */
    void deleteDhcpGiaddrConfigs(Long entityId, Integer dhcpGiaddrIndex);

    /**
     * 新增DHCP 网关配置
     * 
     * @param dhcpGiaddrConfig
     */
    void insertDhcpGiaddrConfigs(OltDhcpGiaddrConfig dhcpGiaddrConfig);

    /**
     * 删除DHCP 静态绑定配置
     * 
     * @param entityId
     * @param topOltDHCPIpMacIdx
     */
    void deleteDhcpIpMacStatic(Long entityId, Integer topOltDHCPIpMacIdx);

    /**
     * 新增DHCP 静态绑定配置
     * 
     * @param dhcpIpMacStatic
     */
    void insertDhcpIpMacStatic(OltDhcpIpMacStatic dhcpIpMacStatic);

    /**
     * 更新DHCP 网关配置
     * 
     * @param dhcpGiaddrConfig
     */
    void updateDhcpGiaddrConfigs(OltDhcpGiaddrConfig dhcpGiaddrConfig);

    /**
     * 刷新设备DHCP 基本配置
     * 
     * @param snmpParam
     */
    void refreshOltDhcpBaseConfig(Long entityId);

    /**
     * 刷新设备DHCP Server配置
     * 
     * @param snmpParam
     */
    void refreshOltDhcpServerConfig(Long entityId);

    /**
     * 刷新设备DHCP 网关配置
     * 
     * @param snmpParam
     */
    void refreshOltDhcpGiaddrConfig(Long entityId);

    /**
     * 刷新设备DHCP 静态绑定配置
     * 
     * @param snmpParam
     */
    void refreshOltDhcpIpMacStaticConfig(Long entityId);

    Integer getCountForIpMacStatic(Long entityId, Long topOltDHCPIpAddrLong, Long topOltDHCPMacAddrLong);

}
