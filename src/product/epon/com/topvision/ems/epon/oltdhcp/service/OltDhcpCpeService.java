/***********************************************************************
 * $Id: OltDhcpCpeService.java,v1.0 2017年11月21日 下午1:10:35 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpCpeInfo;

/**
 * @author haojie
 * @created @2017年11月21日-下午1:10:35
 *
 */
public interface OltDhcpCpeService {

    /**
     * 获取CPE列表信息
     * 
     * @param entityId
     * @return
     */
    List<TopOltDhcpCpeInfo> getOltDhcpCpeInfo(Map<String, Object> queryMap);

    /**
     * 获取CPE信息数量
     * 
     * @param entityId
     * @return
     */
    Long getOltDhcpCpeInfoCount(Map<String, Object> queryMap);

    /**
     * 刷新CPE列表信息
     * 
     * @param entityId
     */
    void refreshOltDhcpCpeInfo(Long entityId);

}
