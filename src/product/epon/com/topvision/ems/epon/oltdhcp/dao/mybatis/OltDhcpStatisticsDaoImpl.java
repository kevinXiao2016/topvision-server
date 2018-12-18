/***********************************************************************
 * $Id: OltDhcpStatisticsDaoImpl.java,v1.0 2017年11月22日 上午8:52:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpStatisticsDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltPppoeStatisticsObjects;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:52:40
 *
 */
@Repository("oltDhcpStatisticsDao")
public class OltDhcpStatisticsDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpStatisticsDao {

    @Override
    protected String getDomainName() {
        return "OltDhcpStatistics";
    }

    @Override
    public TopOltDhcpStatisticsObjects getOltDhcpStatistics(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getOltDhcpStatistics"), entityId);
    }

    @Override
    public TopOltPppoeStatisticsObjects getOltPppoeStatistics(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getOltPppoeStatistics"), entityId);
    }

}
