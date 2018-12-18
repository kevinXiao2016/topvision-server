/***********************************************************************
 * $Id: OnuDiscoveryFacadeImpl.java,v1.0 2015-8-5 下午3:39:20 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.engine;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.topology.domain.OnuDiscoveryData;
import com.topvision.ems.epon.topology.facade.OnuDiscoveryFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Rod John
 * @created @2015-8-5-下午3:39:20
 *
 */
@Facade("onuDiscoveryFacade")
public class OnuDiscoveryFacadeImpl extends EmsFacade implements OnuDiscoveryFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.topology.facade.OnuDiscoveryFacade#discovery(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.topology.domain.OnuDiscoveryData)
     */
    @Override
    public OnuDiscoveryData discovery(SnmpParam param, OnuDiscoveryData onuDiscoveryData) {
        SnmpWorker<OnuDiscoveryData> worker = new OnuDiscoverySnmpWorker<OnuDiscoveryData>(param);
        return snmpExecutorService.execute(worker, onuDiscoveryData);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.topology.facade.OnuDiscoveryFacade#discoveryOnu(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.epon.topology.domain.OnuDiscoveryData)
     */
    @Override
    public OnuDiscoveryData discoveryOnu(SnmpParam param, OnuDiscoveryData onuDiscoveryData) {
        SnmpWorker<OnuDiscoveryData> worker = new OnuRefreshSnmpWorker<OnuDiscoveryData>(param);
        return snmpExecutorService.execute(worker, onuDiscoveryData);
    }

}
