package com.topvision.ems.mobile.engine;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.mobile.facade.MCmcFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

@Facade("mCmcFacade")
public class MCmcFacadeImpl implements MCmcFacade {

    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public Integer getDocsisMode(SnmpParam snmpParam, Long cmIndex) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.10.127.21.3.1.1." + cmIndex));
    }

    @Override
    public Integer getRealtimeStatusValue(SnmpParam snmpParam, Long cmIndex) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.10.127.1.3.3.1.9." + cmIndex));
    }

}
