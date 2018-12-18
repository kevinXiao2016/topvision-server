/***********************************************************************
 * $Id: OltDiscoveryServiceImpl.java,v1.0 2013-10-26 上午10:20:55 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.mirror.dao.OltMirrorDao;
import com.topvision.ems.epon.olt.dao.OltDao;
import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.dao.OltSniDao;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onuauth.dao.OnuAuthDao;
import com.topvision.ems.epon.optical.dao.OltOpticalDao;
import com.topvision.ems.epon.performance.service.EponStatsService;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.epon.trunk.dao.OltTrunkDao;
import com.topvision.ems.epon.trunk.service.OltTrunkService;
import com.topvision.ems.epon.vlan.dao.SniVlanDao;
import com.topvision.ems.epon.vlan.dao.UniVlanDao;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.impl.DiscoveryServiceImpl;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-26-上午10:20:55
 * 
 */
@Service("oltDiscoveryService")
public class OltDiscoveryServiceImpl extends DiscoveryServiceImpl<OltDiscoveryData> {
    @Autowired
    protected SniVlanDao sniVlanDao;
    @Autowired
    protected UniVlanDao uniVlanDao;
    @Autowired
    protected EntityService entityService;
    @Autowired
    protected OltService oltService;
    @Autowired
    protected OltOpticalDao oltOpticalDao;
    @Autowired
    protected EntityTypeService entityTypeService;
    @Autowired
    protected EponStatsService eponStatsService;
    @Autowired
    protected OnuService onuService;
    @Autowired
    protected OltTrunkService oltTrunkService;
    @Autowired
    protected OltSlotDao oltSlotDao;
    @Autowired
    protected OltSlotService oltSlotService;
    @Autowired
    protected OnuAuthDao onuAuthDao;
    @Autowired
    protected OnuDao onuDao;
    @Autowired
    protected OltTrunkDao oltTrunkDao;
    @Autowired
    protected OltPonDao oltPonDao;
    @Autowired
    protected OltDao oltDao;
    @Autowired
    protected OltSniDao oltSniDao;
    @Autowired
    protected OltMirrorDao oltMirrorDao;
    @Autowired
    protected UniDao uniDao;

    @Override
    public void initialize() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public OltDiscoveryData discovery(SnmpParam snmpParam) {
        return null;
    }

    @Override
    public void updateEntity(Entity entity, OltDiscoveryData data) {
        super.updateEntity(entity, data);
    }

    @Override
    protected void updatePort(DiscoveryData data, Long entityId) {
        // do nothing
    }
}