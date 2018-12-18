/***********************************************************************
 * $Id: OltDhcpPortServiceImpl.java,v1.0 2017年11月22日 下午3:58:55 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpBaseDao;
import com.topvision.ems.epon.oltdhcp.dao.OltDhcpPortDao;
import com.topvision.ems.epon.oltdhcp.facade.OltDhcpFacade;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpPortService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2017年11月22日-下午3:58:55
 *
 */
@Service("oltDhcpPortService")
public class OltDhcpPortServiceImpl extends BaseService implements OltDhcpPortService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltDhcpPortDao oltDhcpPortDao;
    @Autowired
    private OltDhcpBaseDao oltDhcpBaseDao;

    @Override
    public List<TopOltDhcpPortAttribute> getPortAttribute(Long entityId, Integer portProtIndex) {
        return oltDhcpPortDao.getPortAttribute(entityId, portProtIndex);
    }

    @Override
    public void modifyPortAttribute(TopOltDhcpPortAttribute port) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(port.getEntityId());
        getOltDhcpFacade(snmpParam).modifyPortAttribute(snmpParam, port);
        oltDhcpPortDao.updatePortAttribute(port);
    }

    @Override
    public void refreshPortAttribute(Long entityId, Integer portProtIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<TopOltDhcpPortAttribute> ports = getOltDhcpFacade(snmpParam).getTopOltDhcpPortAttributes(snmpParam);
        if (ports != null) {
            oltDhcpBaseDao.updatePortAttributes(entityId, portProtIndex, ports);
        }
    }

    private OltDhcpFacade getOltDhcpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OltDhcpFacade.class);
    }

    @Override
    public TopOltDhcpPortAttribute getPortAttribute(Long entityId, Integer portProtIndex, Integer portTypeIndex,
            Integer slotIndex, Integer portIndex) {
        return oltDhcpPortDao.getPortAttribute(entityId, portProtIndex, portTypeIndex, slotIndex, portIndex);
    }

}
