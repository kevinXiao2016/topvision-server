/***********************************************************************
 * $Id: OltDhcpStatisticsService.java,v1.0 2017年11月21日 下午2:54:41 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltPppoeStatisticsObjects;

/**
 * @author haojie
 * @created @2017年11月21日-下午2:54:41
 *
 */
public interface OltDhcpStatisticsService {

    /**
     * 获取DHCP报文统计信息
     * 
     * @param entityId
     * @return
     */
    TopOltDhcpStatisticsObjects getOltDhcpStatistics(Long entityId);

    /**
     * 刷新DHCP报文统计信息
     * 
     * @param entityId
     */
    void refreshOltDhcpStatistics(Long entityId);

    /**
     * DHCP报文统计清零
     * 
     * @param entityId
     */
    void clearOltDhcpStatistics(Long entityId);

    /**
     * 获取PPPoE报文统计信息
     * 
     * @param entityId
     * @return
     */
    TopOltPppoeStatisticsObjects getOltPppoeStatistics(Long entityId);

    /**
     * 刷新PPPoE报文统计信息
     * 
     * @param entityId
     */
    void refreshOltPppoeStatistics(Long entityId);

    /**
     * PPPoE报文统计信息清零
     * 
     * @param entityId
     */
    void clearOltPppoeStatistics(Long entityId);

}
