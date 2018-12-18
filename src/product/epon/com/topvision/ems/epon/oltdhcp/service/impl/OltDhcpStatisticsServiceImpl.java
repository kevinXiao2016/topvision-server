/***********************************************************************
 * $Id: OltDhcpStatisticsServiceImpl.java,v1.0 2017年11月23日 下午1:31:55 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpStatisticsDao;
import com.topvision.ems.epon.oltdhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltPppoeStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpRefreshService;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpStatisticsService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年11月23日-下午1:31:55
 *
 */
@Service("oltDhcpStatisticsService")
public class OltDhcpStatisticsServiceImpl extends BaseService implements OltDhcpStatisticsService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpStatisticsDao oltDhcpStatisticsDao;
    @Autowired
    private OltDhcpRefreshService oltDhcpRefreshService;

    @Override
    public TopOltDhcpStatisticsObjects getOltDhcpStatistics(Long entityId) {
        return oltDhcpStatisticsDao.getOltDhcpStatistics(entityId);
    }

    @Override
    public void refreshOltDhcpStatistics(Long entityId) {
        oltDhcpRefreshService.refreshTopOltDhcpStatisticsObjects(entityId);
    }

    @Override
    public void clearOltDhcpStatistics(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltDhcpFacade(snmpParam).clearOltDhcpStatistics(snmpParam);
        refreshOltDhcpStatistics(entityId);
    }

    @Override
    public TopOltPppoeStatisticsObjects getOltPppoeStatistics(Long entityId) {
        return oltDhcpStatisticsDao.getOltPppoeStatistics(entityId);
    }

    @Override
    public void refreshOltPppoeStatistics(Long entityId) {
        oltDhcpRefreshService.refreshTopOltPppoeStatisticsObjects(entityId);
    }

    @Override
    public void clearOltPppoeStatistics(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltDhcpFacade(snmpParam).clearOltPppoeStatistics(snmpParam);
        refreshOltPppoeStatistics(entityId);
    }

    private OltDhcpFacade getOltDhcpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OltDhcpFacade.class);
    }
}
