/***********************************************************************
 * $Id: AutoRefreshFacadeImpl.java,v1.0 2014-10-16 上午9:31:11 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.network;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.network.AutoRefreshFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Rod John
 * @created @2014-10-16-上午9:31:11
 * 
 */
@Facade("autoRefreshFacade")
public class AutoRefreshFacadeImpl extends EmsFacade implements AutoRefreshFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService = null;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.facade.network.AutoRefreshFacade#autoRefresh(com.topvision.framework.snmp
     * .SnmpWorker)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public DiscoveryData autoRefresh(SnmpWorker snmpWorker) {
        DiscoveryData data = null;
        data = snmpExecutorService.execute(snmpWorker, data);
        return data;
    }

}
