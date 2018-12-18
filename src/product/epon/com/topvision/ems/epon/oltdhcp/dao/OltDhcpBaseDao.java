/***********************************************************************
 * $Id: OltDhcpBaseDao.java,v1.0 2017年11月22日 上午8:49:07 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

import java.util.List;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:49:07
 *
 */
public interface OltDhcpBaseDao extends BaseEntityDao<Object> {

    /**
     * 获取全局配置信息，包括PPPoE配置，Option82配置，防静态开关，DHCP全局开关
     * 
     * @param entityId
     * @return
     */
    TopOltDhcpGlobalObjects getOltDhcpBaseCfg(Long entityId);

    /**
     * 更新DHCP总开关
     * 
     * @param entityId
     * @param status
     */
    void updateOltDhcpEnable(Long entityId, Integer status);

    /**
     * 更新DHCP全局参数
     * 
     * @param globalObjects
     */
    void updateOltDhcpGlobalObjects(TopOltDhcpGlobalObjects globalObjects);

    /**
     * 更新Option82配置
     * 
     * @param globalObjects
     */
    void updateOltDhcpOption82Cfg(TopOltDhcpGlobalObjects globalObjects);

    /**
     * 更新PPPoE开关
     * 
     * @param entityId
     * @param status
     */
    void updateOltPppoeEnable(Long entityId, Integer status);

    /**
     * 更新PPPoE全局配置，包括开关，标签处理策略，标签格式
     * 
     * @param globalObjects
     */
    void updateOltPppoeGlobalObjects(TopOltDhcpGlobalObjects globalObjects);

    /**
     * 更新级联口/信任口配置
     * 
     * @param entityId
     * @param portProtIndex
     * @param portAttributes
     */
    void updatePortAttributes(Long entityId, Integer portProtIndex, List<TopOltDhcpPortAttribute> portAttributes);

    /**
     * 更新PPPoE配置
     * 
     * @param globalObjects
     */
    void updateOltPppoeCfg(TopOltDhcpGlobalObjects globalObjects);

    /**
     * 更新防静态IP开关
     * 
     * @param entityId
     * @param status
     */
    void updateOltDhcpSourceVerifyEnable(Long entityId, Integer status);

}
