/***********************************************************************
 * $Id: OltDhcpPortService.java,v1.0 2017年11月21日 下午2:53:49 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

import java.util.List;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;

/**
 * @author haojie
 * @created @2017年11月21日-下午2:53:49
 *
 */
public interface OltDhcpPortService {

    /**
     * 获取级联口/信任口列表信息（区分DHCP/PPPoE）
     * 
     * @param entityId
     * @param portProtIndex
     * @return
     */
    List<TopOltDhcpPortAttribute> getPortAttribute(Long entityId, Integer portProtIndex);

    /**
     * 获取单条级联口/信任口配置
     * 
     * @param entityId
     * @param portProtIndex
     * @param portTypeIndex
     * @param slotIndex
     * @param portIndex
     * @return
     */
    TopOltDhcpPortAttribute getPortAttribute(Long entityId, Integer portProtIndex, Integer portTypeIndex,
            Integer slotIndex, Integer portIndex);

    /**
     * 修改级联口/信任口配置
     * 
     * @param port
     */
    void modifyPortAttribute(TopOltDhcpPortAttribute port);

    /**
     * 刷新级联口/信任口配置（区分DHCP/PPPoE）
     * 
     * @param entityId
     * @param portProtIndex
     */
    void refreshPortAttribute(Long entityId, Integer portProtIndex);

}
