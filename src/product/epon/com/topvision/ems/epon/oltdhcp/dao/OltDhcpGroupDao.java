/***********************************************************************
 * $Id: OltDhcpGroupDao.java,v1.0 2017年11月22日 上午8:50:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

import java.util.List;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpServerGroup;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:50:40
 *
 */
public interface OltDhcpGroupDao extends BaseEntityDao<Object> {

    /**
     * 获取服务器组列表信息
     * 
     * @param entityId
     * @return
     */
    List<TopOltDhcpServerGroup> getOltDhcpServerGroup(Long entityId);

    /**
     * 插入服务器组
     * 
     * @param group
     */
    void insertOltDhcpServerGroup(TopOltDhcpServerGroup group);

    /**
     * 更新服务器组
     * 
     * @param group
     */
    void updateOltDhcpServerGroup(TopOltDhcpServerGroup group);

    /**
     * 删除服务器组
     * 
     * @param entityId
     * @param groupIndex
     */
    void deleteOltDhcpServerGroup(Long entityId, Integer groupIndex);

    /**
     * 获取单条服务器组信息
     * 
     * @param entityId
     * @param groupIndex
     * @return
     */
    TopOltDhcpServerGroup getOltDhcpServerGroup(Long entityId, Integer groupIndex);

}
