/***********************************************************************
 * $Id: OltDiscoveryFacadeImpl.java,v1.0 2013-10-26 上午10:19:58 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.engine;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.epon.topology.facade.OltDiscoveryFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author flack
 * @created @2013-10-26-上午10:19:58
 * 
 */
@Facade("oltDiscoveryFacade")
public class OltDiscoveryFacadeImpl extends EmsFacade implements OltDiscoveryFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OltDiscoveryData discovery(SnmpParam param, OltDiscoveryData oltDiscoveryData) {
        SnmpWorker<OltDiscoveryData> worker = new OltDiscoverySnmpWorker<OltDiscoveryData>(param);
        return snmpExecutorService.execute(worker, oltDiscoveryData);
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
     * com.topvision.ems.epon.topology.facade.OltDiscoveryFacade#autoDiscovery(com.topvision.framework
     * .snmp.SnmpParam)
     */
    @Override
    public OltDiscoveryData autoDiscovery(SnmpParam param, OltDiscoveryData data) {
        SnmpWorker<OltDiscoveryData> worker = new BaseOltDiscoverySnmpWorker<OltDiscoveryData>(param);
        return snmpExecutorService.execute(worker, data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.topology.facade.OltDiscoveryFacade#getOnuMacAddress(com.topvision.
     * framework.snmp.SnmpParam)
     */
    @Override
    public Map<String, String> getOnuMacAddress(SnmpParam snmpParam) {
        return snmpExecutorService.execute(new SnmpWorker<Map<String, String>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getListWithOid("1.3.6.1.4.1.17409.2.3.4.1.1.7");
            }
        }, null);
    }
}
