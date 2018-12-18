/***********************************************************************
 * $Id: OltDhcpVlanService.java,v1.0 2017年11月21日 下午1:07:41 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

import java.util.List;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVLANCfg;

/**
 * @author haojie
 * @created @2017年11月21日-下午1:07:41
 *
 */
public interface OltDhcpVlanService {

    /**
     * 获取模式配置列表信息
     * 
     * @param entityId
     * @return
     */
    List<TopOltDhcpVLANCfg> getOltDhcpVlanCfg(Long entityId);

    /**
     * 获取单条模式配置
     * 
     * @param entityId
     * @param vlanIndex
     * @return
     */
    TopOltDhcpVLANCfg getOltDhcpVlanCfg(Long entityId, Integer vlanIndex);

    /**
     * 修改模式配置信息
     * 
     * @param vlanCfg
     */
    void modifyOltDhcpVLANCfg(TopOltDhcpVLANCfg vlanCfg);

    /**
     * 刷新OLT下所有模式配置信息
     * 
     * @param entityId
     */
    void refreshOltDhcpVLANCfg(Long entityId);

}
