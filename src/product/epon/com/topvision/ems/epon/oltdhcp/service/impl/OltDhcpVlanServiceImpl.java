/***********************************************************************
 * $Id: OltDhcpVlanServiceImpl.java,v1.0 2017年11月23日 下午6:37:16 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.logicinterface.service.LogicInterfaceService;
import com.topvision.ems.epon.oltdhcp.dao.OltDhcpVlanDao;
import com.topvision.ems.epon.oltdhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVLANCfg;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpRefreshService;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpVlanService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年11月23日-下午6:37:16
 *
 */
@Service("oltDhcpVlanService")
public class OltDhcpVlanServiceImpl extends BaseService implements OltDhcpVlanService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpVlanDao oltDhcpVlanDao;
    @Autowired
    private OltDhcpRefreshService oltDhcpRefreshService;
    @Autowired
    private LogicInterfaceService logicInterfaceService;

    @Override
    public List<TopOltDhcpVLANCfg> getOltDhcpVlanCfg(Long entityId) {
        return oltDhcpVlanDao.getOltDhcpVlanCfg(entityId);
    }

    @Override
    public void modifyOltDhcpVLANCfg(TopOltDhcpVLANCfg vlanCfg) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanCfg.getEntityId());
        getOltDhcpFacade(snmpParam).setOltDhcpVLANCfg(snmpParam, vlanCfg);
        oltDhcpVlanDao.updateOltDhcpVLANCfg(vlanCfg);
    }

    @Override
    public void refreshOltDhcpVLANCfg(Long entityId) {
        oltDhcpRefreshService.refreshTopOltDhcpVLANCfg(entityId);
        
    }

    private OltDhcpFacade getOltDhcpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OltDhcpFacade.class);
    }

    @Override
    public TopOltDhcpVLANCfg getOltDhcpVlanCfg(Long entityId, Integer vlanIndex) {
        return oltDhcpVlanDao.getOltDhcpVlanCfg(entityId, vlanIndex);
    }

}
