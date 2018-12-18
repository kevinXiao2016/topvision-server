/***********************************************************************
 * $Id: OltDhcpRefreshDao.java,v1.0 2017年11月17日 上午11:34:06 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

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
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月17日-上午11:34:06
 *
 */
public interface OltDhcpRefreshDao extends BaseEntityDao<Object> {

    /**
     * 批量更新CPE信息
     * 
     * @param entityId
     * @param topOltDhcpCpeInfos
     */
    void batchInsertTopOltDhcpCpeInfos(Long entityId, List<TopOltDhcpCpeInfo> topOltDhcpCpeInfos);

    /**
     * 更新DHCP基本配置信息
     * 
     * @param entityId
     * @param topOltDhcpGlobalObjects
     */
    void insertTopOltDhcpGlobalObjects(Long entityId, TopOltDhcpGlobalObjects topOltDhcpGlobalObjects);

    /**
     * 更新级联口/信任口信息
     * 
     * @param entityId
     * @param topOltDhcpPortAttributes
     */
    void batchInsertTopOltDhcpPortAttributes(Long entityId,
            List<TopOltDhcpPortAttribute> topOltDhcpPortAttributes);

    /**
     * 更新服务器组信息
     * 
     * @param entityId
     * @param topOltDhcpServerGroups
     */
    void batchInsertTopOltDhcpServerGroups(Long entityId, List<TopOltDhcpServerGroup> topOltDhcpServerGroups);

    /**
     * 更新静态IP信息
     * 
     * @param entityId
     * @param topOltDhcpStaticIps
     */
    void batchInsertTopOltDhcpStaticIps(Long entityId, List<TopOltDhcpStaticIp> topOltDhcpStaticIps);

    /**
     * 更新DHCP报文统计信息
     * 
     * @param entityId
     * @param topOltDhcpGlobalObjects
     */
    void insertTopOltDhcpStatisticsObjects(Long entityId, TopOltDhcpStatisticsObjects topOltDhcpGlobalObjects);

    /**
     * 更新RELAY规则信息
     * 
     * @param entityId
     * @param topOltDhcpVifCfgs
     */
    void batchInsertTopOltDhcpVifCfgs(Long entityId, List<TopOltDhcpVifCfg> topOltDhcpVifCfgs);

    /**
     * 更新模式配置信息
     * 
     * @param entityId
     * @param topOltDhcpVLANCfgs
     */
    void batchInsertTopOltDhcpVLANCfgs(Long entityId, List<TopOltDhcpVLANCfg> topOltDhcpVLANCfgs);

    /**
     * 更新PPPoE+报文统计信息
     * 
     * @param entityId
     * @param topOltPppoeStatisticsObjects
     */
    void insertTopOltPppoeStatisticsObjects(Long entityId,
            TopOltPppoeStatisticsObjects topOltPppoeStatisticsObjects);

}
