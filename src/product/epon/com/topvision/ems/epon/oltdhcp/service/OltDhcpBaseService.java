/***********************************************************************
 * $Id: OltDhcpBaseService.java,v1.0 2017年11月21日 下午1:06:42 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;

/**
 * @author haojie
 * @created @2017年11月21日-下午1:06:42
 *
 */
public interface OltDhcpBaseService {

    /**
     * 获取OLT DHCP全局配置信息，包括DHCP总开关，Option82配置信息，PPPoE配置信息，防静态IP开关
     * 
     * @param entityId
     * @return
     */
    TopOltDhcpGlobalObjects getOltDhcpBaseCfg(Long entityId);

    /**
     * 设置DHCP使能
     * 
     * @param entityId
     * @param status
     */
    void moidfyDhcpEnable(Long entityId, Integer status);

    /**
     * 同步DHCP数据，包括模式配置，RELAY规则配置，级联口/信任口，报文统计，CPE信息等
     * 
     * @param entityId
     */
    void refreshDhcpData(Long entityId);

    /**
     * 修改Option82配置信息
     * 
     * @param entityId
     * @param status
     * @param policy
     * @param format
     */
    void modifyOption82Cfg(Long entityId, Integer status, Integer policy, String format);

    /**
     * 刷新Option82配置信息,包括Option82使能，标签处理策略，标签格式
     * 
     * @param entityId
     */
    void refrshOption82Cfg(Long entityId);

    /**
     * 设置PPPoE使能
     * 
     * @param entityId
     * @param status
     */
    void modifyPppoeEnable(Long entityId, Integer status);

    /**
     * 同步PPPoE+数据,包括级联口/信任口信息，报文统计
     * 
     * @param entityId
     */
    void refreshPppoeData(Long entityId);

    /**
     * 修改PPPoE配置信息
     * 
     * @param entityId
     * @param policy
     * @param format
     */
    void modifyPppoeCfg(Long entityId, Integer policy, String format);

    /**
     * 刷新PPPoE配置信息，包括标签处理策略，标签格式
     * 
     * @param entityId
     */
    void refreshPppoeCfg(Long entityId);

    /**
     * 设置防静态IP使能
     * 
     * @param entityId
     * @param status
     */
    void modifyOltDhcpSourceVerifyEnable(Long entityId, Integer status);

}
