/***********************************************************************
 * $Id: OltDhcpFacade.java,v1.0 2013-10-25 下午5:56:36 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.facade;

import java.util.List;

import com.topvision.ems.epon.dhcp.domain.OltDhcpBaseConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacDynamic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpIpMacStatic;
import com.topvision.ems.epon.dhcp.domain.OltDhcpServerConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午5:56:36
 *
 */
@EngineFacade(serviceName = "OltDhcpFacade", beanName = "oltDhcpFacade")
public interface OltDhcpFacade extends Facade {

    /**
     * 获得设备DHCP基本配置信息
     * 
     * @param snmpParam
     * @return OltDhcpBaseConfig
     */
    OltDhcpBaseConfig getDhcpBaseConfig(SnmpParam snmpParam);

    /**
     * 获得设备DHCP Server基本配置信息
     * 
     * @param snmpParam
     * @return list
     */
    List<OltDhcpServerConfig> getDhcpServerConfigs(SnmpParam snmpParam);

    /**
     * 获得设备DHCP 网关基本配置信息
     * 
     * @param snmpParam
     * @return list
     */
    List<OltDhcpGiaddrConfig> getDhcpGiaddrConfigs(SnmpParam snmpParam);

    /**
     * 获得设备DHCP 静态IP绑定信息
     * 
     * @param snmpParam
     * @return list
     */
    List<OltDhcpIpMacStatic> getDhcpIpMacStatics(SnmpParam snmpParam);

    /**
     * 获得设备DHCP 动态IP绑定信息
     * 
     * @param snmpParam
     * @return
     */
    List<OltDhcpIpMacDynamic> getDhcpIpMacDynamics(SnmpParam snmpParam);

    /**
     * 修改设备DHCP 基本配置
     * 
     * @param snmpParam
     * @param baseConfig
     * @return
     */
    OltDhcpBaseConfig modifyDhcpBaseConfig(SnmpParam snmpParam, OltDhcpBaseConfig baseConfig);

    /**
     * 增加设备DHCP Server配置
     * 
     * @param snmpParam
     * @param serverConfig
     * @return
     */
    OltDhcpServerConfig addDhcpServerConfig(SnmpParam snmpParam, OltDhcpServerConfig serverConfig);

    /**
     * 增加设备DHCP 网关配置
     * 
     * @param snmpParam
     * @param giaddrConfig
     * @return
     */
    OltDhcpGiaddrConfig addDhcpGiaddrConfig(SnmpParam snmpParam, OltDhcpGiaddrConfig giaddrConfig);

    /**
     * 增加设备DHCP 静态配置
     * 
     * @param snmpParam
     * @param ipMacStaticConfig
     * @return
     */
    OltDhcpIpMacStatic addDhcpIpMacStatic(SnmpParam snmpParam, OltDhcpIpMacStatic ipMacStaticConfig);

    /**
     * 删除设备DHCP Server配置
     * 
     * @param snmpParam
     * @param serverConfig
     */
    void deleteDhcpServerConfig(SnmpParam snmpParam, OltDhcpServerConfig serverConfig);

    /**
     * 删除设备DHCP 网关配置
     * 
     * @param snmpParam
     * @param giaddrConfig
     */
    void deleteDhcpGiaddrConfig(SnmpParam snmpParam, OltDhcpGiaddrConfig giaddrConfig);

    /**
     * 删除设备DHCP 静态配置
     * 
     * @param snmpParam
     * @param ipMacStaticConfig
     */
    void deleteDhcpIpMacStatic(SnmpParam snmpParam, OltDhcpIpMacStatic ipMacStaticConfig);

}
