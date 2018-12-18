/***********************************************************************
 * $Id: OltDhcpGroupService.java,v1.0 2017年11月21日 下午1:08:27 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

import java.util.List;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpServerGroup;

/**
 * @author haojie
 * @created @2017年11月21日-下午1:08:27
 *
 */
public interface OltDhcpGroupService {

    /**
     * 获取服务器组列表数据
     * 
     * @param entityId
     * @return
     */
    List<TopOltDhcpServerGroup> getOltDhcpServerGroup(Long entityId);

    /**
     * 获取单条服务器组信息
     * 
     * @param entityId
     * @param groupIndex
     * @return
     */
    TopOltDhcpServerGroup getOltDhcpServerGroup(Long entityId, Integer groupIndex);

    /**
     * 新增服务器组
     * 
     * @param topOltDhcpServerGroup
     */
    void addOltDhcpServerGroup(TopOltDhcpServerGroup group);

    /**
     * 修改服务器组
     * 
     * @param topOltDhcpServerGroup
     */
    void modifyOltDhcpServerGroup(TopOltDhcpServerGroup group);

    /**
     * 删除服务器组
     * 
     * @param entityId
     * @param groupIndex
     */
    void deleteOltDhcpServerGroup(Long entityId, Integer groupIndex);

    /**
     * 刷新服务器组列表信息
     * 
     * @param entityId
     */
    void refreshOltDhcpServerGroup(Long entityId);

}
