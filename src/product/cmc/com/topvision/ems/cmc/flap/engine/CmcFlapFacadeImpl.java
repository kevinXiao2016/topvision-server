/***********************************************************************
 * $Id: CmcFacadeImpl.java,v1.0 2011-7-1 下午02:56:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.flap.engine;

import javax.annotation.Resource;

import com.topvision.ems.cmc.flap.facade.CmcFlapFacade;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-7-1-下午02:56:35
 * 
 */
@Facade("cmcFlapFacade")
public class CmcFlapFacadeImpl extends EmsFacade implements CmcFlapFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    @Override
    public String getTopCmFlapInterval(SnmpParam snmpParam) {
        return snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.11.2.0");
    }

    @Override
    public void setTopCmFlapInterval(SnmpParam snmpParam, Integer topCmFlapInterval) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.11.2.0", topCmFlapInterval.toString());
    }

    @Override
    public void resetFlapCounters(SnmpParam snmpParam, Long cmcIndex) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.2.4.1.3." + cmcIndex, "1");

    }

}
