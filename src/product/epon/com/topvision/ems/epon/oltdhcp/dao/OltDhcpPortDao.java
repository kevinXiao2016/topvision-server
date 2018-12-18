/***********************************************************************
 * $Id: OltDhcpPortDao.java,v1.0 2017年11月22日 上午8:51:25 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

import java.util.List;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:51:25
 *
 */
public interface OltDhcpPortDao extends BaseEntityDao<Object> {

    /**
     * 获取DHCP/PPPoE 级联口/信任口信息
     * 
     * @param entityId
     * @param portProtIndex
     * @return
     */
    List<TopOltDhcpPortAttribute> getPortAttribute(Long entityId, Integer portProtIndex);

    /**
     * 更新级联口/信任口配置
     * 
     * @param port
     */
    void updatePortAttribute(TopOltDhcpPortAttribute port);

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

}
