/***********************************************************************
 * $Id: OltDhcpRefreshService.java,v1.0 2017年11月17日 上午10:44:13 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

/**
 * @author haojie
 * @created @2017年11月17日-上午10:44:13
 *
 */
public interface OltDhcpRefreshService {

    /**
     * 刷新CPE信息
     * 
     * @param entityId
     */
    public void refreshTopOltDhcpCpeInfo(Long entityId);

    /**
     * 刷新全局配置，包括DHCP开关/Option82/防静态IP开关/PPPoE+
     * 
     * @param entityId
     */
    public void refreshTopOltDhcpGlobalObjects(Long entityId);

    /**
     * 刷新级联口/信任口信息
     * 
     * @param entityId
     */
    public void refreshTopOltDhcpPortAttribute(Long entityId);

    /**
     * 刷新服务器组信息
     * 
     * @param entityId
     */
    public void refreshTopOltDhcpServerGroup(Long entityId);

    /**
     * 刷新静态IP信息
     * 
     * @param entityId
     */
    public void refreshTopOltDhcpStaticIp(Long entityId);

    /**
     * 刷新DHCP报文统计信息
     * 
     * @param entityId
     */
    public void refreshTopOltDhcpStatisticsObjects(Long entityId);

    /**
     * 刷新RELAY规则信息
     * 
     * @param entityId
     */
    public void refreshTopOltDhcpVifCfg(Long entityId);

    /**
     * 刷新DHCP模式信息
     * 
     * @param entityId
     */
    public void refreshTopOltDhcpVLANCfg(Long entityId);

    /**
     * 刷新PPPoE+报文统计信息
     * 
     * @param entityId
     */
    public void refreshTopOltPppoeStatisticsObjects(Long entityId);
}
