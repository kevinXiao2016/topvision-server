/***********************************************************************
 * $Id: OltDhcpStatisticsDao.java,v1.0 2017年11月22日 上午8:52:30 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltPppoeStatisticsObjects;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:52:30
 *
 */
public interface OltDhcpStatisticsDao extends BaseEntityDao<Object> {

    /**
     * 获取DHCP报文统计信息
     * 
     * @param entityId
     * @return
     */
    TopOltDhcpStatisticsObjects getOltDhcpStatistics(Long entityId);

    /**
     * 获取PPPoE报文统计信息
     * 
     * @param entityId
     * @return
     */
    TopOltPppoeStatisticsObjects getOltPppoeStatistics(Long entityId);

}
