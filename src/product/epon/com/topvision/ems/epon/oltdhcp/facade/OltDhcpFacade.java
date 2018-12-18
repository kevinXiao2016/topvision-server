/***********************************************************************
 * $Id: DhcpFacade.java,v1.0 2017年11月17日 上午11:21:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.facade;

import java.util.List;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpCpeInfo;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpServerGroup;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStaticIp;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVLANCfg;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVifCfg;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltPppoeStatisticsObjects;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2017年11月17日-上午11:21:38
 *
 */
@EngineFacade(serviceName = "DhcpFacade", beanName = "dhcpFacade")
public interface OltDhcpFacade {
    /**
     * 获取CPE信息
     * 
     * @param snmpParam
     * @return
     */
    List<TopOltDhcpCpeInfo> getTopOltDhcpCpeInfos(SnmpParam snmpParam);

    /**
     * 获取全局配置参数
     * 
     * @param snmpParam
     * @return
     */
    TopOltDhcpGlobalObjects getTopOltDhcpGlobalObjects(SnmpParam snmpParam);

    /**
     * 获取级联口/信任口信息
     * 
     * @param snmpParam
     * @return
     */
    List<TopOltDhcpPortAttribute> getTopOltDhcpPortAttributes(SnmpParam snmpParam);

    /**
     * 获取服务器组信息
     * 
     * @param snmpParam
     * @return
     */
    List<TopOltDhcpServerGroup> getTopOltDhcpServerGroups(SnmpParam snmpParam);

    /**
     * 获取静态IP信息
     * 
     * @param snmpParam
     * @return
     */
    List<TopOltDhcpStaticIp> getTopOltDhcpStaticIps(SnmpParam snmpParam);

    /**
     * 获取RELAY规则信息
     * 
     * @param snmpParam
     * @return
     */
    List<TopOltDhcpVifCfg> getTopOltDhcpVifCfgs(SnmpParam snmpParam);

    /**
     * 获取模式配置信息
     * 
     * @param snmpParam
     * @return
     */
    List<TopOltDhcpVLANCfg> getTopOltDhcpVLANCfgs(SnmpParam snmpParam);

    /**
     * 获取PPPoE+报文统计信息
     * 
     * @param snmpParam
     * @return
     */
    TopOltPppoeStatisticsObjects getTopOltPppoeStatisticsObjects(SnmpParam snmpParam);

    /**
     * 获取DHCP报文统计信息
     * 
     * @param snmpParam
     * @return
     */
    TopOltDhcpStatisticsObjects getTopOltDhcpStatisticsObjects(SnmpParam snmpParam);

    /**
     * 修改OLT DHCP全局配置
     * 
     * @param snmpParam
     * @param globalObjects
     */
    void modifyOltDhcpGlobalObjects(SnmpParam snmpParam, TopOltDhcpGlobalObjects globalObjects);

    /**
     * 新增/修改/删除服务器组
     * 
     * @param snmpParam
     * @param group
     */
    void setOltDhcpServerGroup(SnmpParam snmpParam, TopOltDhcpServerGroup group);

    /**
     * 修改级联口/信任口配置
     * 
     * @param snmpParam
     * @param port
     */
    void modifyPortAttribute(SnmpParam snmpParam, TopOltDhcpPortAttribute port);

    /**
     * 新增/删除静态IP
     * 
     * @param snmpParam
     * @param staticIp
     */
    void setOltDhcpStaticIp(SnmpParam snmpParam, TopOltDhcpStaticIp staticIp);

    /**
     * 清除DHCP报文统计信息
     * 
     * @param snmpParam
     */
    void clearOltDhcpStatistics(SnmpParam snmpParam);

    /**
     * 清除DHCP报文统信息
     * 
     * @param snmpParam
     */
    void clearOltPppoeStatistics(SnmpParam snmpParam);

    /**
     * 新增/修改/删除RELAY规则
     * 
     * @param snmpParam
     * @param vif
     */
    void setOltDhcpVifCfg(SnmpParam snmpParam, TopOltDhcpVifCfg vif);

    /**
     * 修改DHCP模式配置
     * 
     * @param snmpParam
     * @param vlanCfg
     */
    void setOltDhcpVLANCfg(SnmpParam snmpParam, TopOltDhcpVLANCfg vlanCfg);

}
