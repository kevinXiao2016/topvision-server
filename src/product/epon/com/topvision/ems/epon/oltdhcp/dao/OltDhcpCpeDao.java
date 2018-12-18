/***********************************************************************
 * $Id: OltDhcpCpeDao.java,v1.0 2017年11月22日 上午8:49:28 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpCpeInfo;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:49:28
 *
 */
public interface OltDhcpCpeDao extends BaseEntityDao<Object>{

    /**
     * 获取CPE列表信息
     * 
     * @param queryMap
     * @return
     */
    List<TopOltDhcpCpeInfo> getOltDhcpCpeInfo(Map<String, Object> queryMap);

    /**
     * 获取CPE数量
     * 
     * @param queryMap
     * @return
     */
    Long getOltDhcpCpeInfoCount(Map<String, Object> queryMap);

}
