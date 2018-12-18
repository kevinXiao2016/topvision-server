/***********************************************************************
 * $Id: DiscoveryFacadeImpl.java,v1.0 2011-6-28 下午08:26:31 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.network;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.IpAddressTable;
import com.topvision.ems.facade.network.DiscoveryFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-6-28-下午08:26:31
 * 
 */
@Facade("discoveryFacade")
public class DiscoveryFacadeImpl extends EmsFacade implements DiscoveryFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.network.DiscoveryFacade#discover(com.topvision
     * .framework.snmp.SnmpParam )
     */
    @Override
    public DiscoveryData discover(SnmpParam snmpParam) throws SnmpException {
        DiscoveryData data = new DiscoveryData();
        return snmpExecutorService.execute(new DiscoverySnmpWorker<DiscoveryData>(snmpParam), data);
    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.facade.network.DiscoveryFacade#discoverySysObjectID(com.topvision.framework
     * .snmp.SnmpParam)
     */
    @Override
    public String discoverySysObjectID(SnmpParam snmpParam) throws SnmpException {
        String result = snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.1.2.0");
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.facade.network.DiscoveryFacade#discoveryIpTableInfo(com.topvision.framework
     * .snmp.SnmpParam)
     */
    @Override
    public List<IpAddressTable> discoveryIpTableInfo(SnmpParam snmpParam) throws SnmpException {
        return snmpExecutorService.getTable(snmpParam, IpAddressTable.class);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.network.DiscoveryFacade#discoverySysName(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public String discoverySysName(SnmpParam snmpParam) throws SnmpException {
        String result = snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.1.5.0");
        return result;
    }
 
}
