/***********************************************************************
 * $Id: CmtsDiscoveryServiceImpl.java,v1.0 2013-7-20 下午02:20:03 $
 *
 * @author: Rod John
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.service;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmts.topology.domain.CmtsDiscoveryData;
import com.topvision.ems.cmts.topology.facade.CmtsDiscoveryFacade;
import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2013-7-20-下午02:20:03
 */
@Service("bsr2000DiscoveryService")
public class Bsr2000DiscoveryServiceImpl extends CmtsDiscoveryServiceImpl<CmtsDiscoveryData> {
    
    
    @Override
    public void initialize() {
    }

    @Override
    public CmtsDiscoveryData discovery(SnmpParam snmpParam) {
        Entity entity = entityService.getEntity(snmpParam.getEntityId());
        CpuAndMemData cpuAndMemData = getCpuAndMemData(entity.getTypeId());
        CmtsDiscoveryData cmtsDiscoveryData = new CmtsDiscoveryData(cpuAndMemData);
        return getFacadeFactory().getFacade(snmpParam.getIpAddress(), CmtsDiscoveryFacade.class).discoveryBsr2000(
                snmpParam, cmtsDiscoveryData);
    }

}
