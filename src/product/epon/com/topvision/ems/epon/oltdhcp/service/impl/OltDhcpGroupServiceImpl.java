/***********************************************************************
 * $Id: OltDhcpGroupServiceImpl.java,v1.0 2017年11月22日 下午2:52:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpGroupDao;
import com.topvision.ems.epon.oltdhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpServerGroup;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpGroupService;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpRefreshService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年11月22日-下午2:52:39
 *
 */
@Service("oltDhcpGroupService")
public class OltDhcpGroupServiceImpl extends BaseService implements OltDhcpGroupService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpGroupDao oltDhcpGroupDao;
    @Autowired
    private OltDhcpRefreshService oltDhcpRefreshService;

    @Override
    public List<TopOltDhcpServerGroup> getOltDhcpServerGroup(Long entityId) {
        return oltDhcpGroupDao.getOltDhcpServerGroup(entityId);
    }

    @Override
    public void addOltDhcpServerGroup(TopOltDhcpServerGroup group) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(group.getEntityId());
        group.setTopOltDhcpServerRowStatus(RowStatus.CREATE_AND_GO);
        getOltDhcpFacade(snmpParam).setOltDhcpServerGroup(snmpParam, group);
        oltDhcpGroupDao.insertOltDhcpServerGroup(group);
    }

    @Override
    public void modifyOltDhcpServerGroup(TopOltDhcpServerGroup group) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(group.getEntityId());
        getOltDhcpFacade(snmpParam).setOltDhcpServerGroup(snmpParam, group);
        oltDhcpGroupDao.updateOltDhcpServerGroup(group);
    }

    @Override
    public void deleteOltDhcpServerGroup(Long entityId, Integer groupIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        TopOltDhcpServerGroup group = new TopOltDhcpServerGroup();
        group.setEntityId(entityId);
        group.setTopOltDhcpServerGroupIndex(groupIndex);
        group.setTopOltDhcpServerRowStatus(RowStatus.DESTORY);
        getOltDhcpFacade(snmpParam).setOltDhcpServerGroup(snmpParam, group);
        oltDhcpGroupDao.deleteOltDhcpServerGroup(entityId, groupIndex);
    }

    @Override
    public void refreshOltDhcpServerGroup(Long entityId) {
        oltDhcpRefreshService.refreshTopOltDhcpServerGroup(entityId);
    }

    private OltDhcpFacade getOltDhcpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OltDhcpFacade.class);
    }

    @Override
    public TopOltDhcpServerGroup getOltDhcpServerGroup(Long entityId, Integer groupIndex) {
        return oltDhcpGroupDao.getOltDhcpServerGroup(entityId, groupIndex);
    }
}
