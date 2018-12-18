/***********************************************************************
 * $Id: CmcDiscoveryFacadeImpl.java,v1.0 2011-11-13 下午01:52:18 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.engine;

import javax.annotation.Resource;

import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.cmc.topology.facade.CmcDiscoveryFacade;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Victor
 * @created @2011-11-13-下午01:52:18
 * 
 */
@Facade("cmcDiscoveryFacade")
public class CmcDiscoveryFacadeImpl extends EmsFacade implements CmcDiscoveryFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    @Override
    public CmcDiscoveryData discoveryCmc(SnmpParam param, CmcDiscoveryData cmcDiscoveryData) {
        SnmpWorker<CmcDiscoveryData> worker = new CmcDiscoverySnmpWorker<CmcDiscoveryData>(param);
        return snmpExecutorService.execute(worker, cmcDiscoveryData);
    }

    @Override
    public CmcDiscoveryData refreshCC8800A(SnmpParam snmpParam, CmcDiscoveryData cmcDiscoveryData) {
        SnmpWorker<CmcDiscoveryData> worker = new CmcRefreshCC8800ASnmpWorker<CmcDiscoveryData>(snmpParam);
        return snmpExecutorService.execute(worker, cmcDiscoveryData);
    }

    @Override
    public CmcDiscoveryData autoDiscoveryCC8800A(SnmpParam snmpParam, CmcDiscoveryData cmcDiscoveryData) {
        SnmpWorker<CmcDiscoveryData> worker = new BaseCmcRefreshCC8800ASnmpWorker<CmcDiscoveryData>(snmpParam);
        return snmpExecutorService.execute(worker, cmcDiscoveryData);
    }

    @Override
    public CmcDiscoveryData autoDiscoveryCC8800B(SnmpParam snmpParam, CmcDiscoveryData cmcDiscoveryData) {
        SnmpWorker<CmcDiscoveryData> worker = new BaseCmcRefreshCC8800ASnmpWorker<CmcDiscoveryData>(snmpParam);
        return snmpExecutorService.execute(worker, cmcDiscoveryData);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.topology.facade.CmcDiscoveryFacade#discoveryCm(com.topvision.framework.
     * snmp.SnmpParam, com.topvision.ems.cmc.topology.domain.CmcDiscoveryData)
     */
    @Override
    public CmcDiscoveryData discoveryCm(SnmpParam param, CmcDiscoveryData cmcDiscoveryData) {
        SnmpWorker<CmcDiscoveryData> worker = new CmDiscoverySnmpWorker<CmcDiscoveryData>(param);
        return snmpExecutorService.execute(worker, cmcDiscoveryData);
    }

    @Override
    public String getMacAddress(SnmpParam snmpParam) {
        return snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.12.142671872");
    }

}
