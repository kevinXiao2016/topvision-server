package com.topvision.ems.mobile.facade;

import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

@EngineFacade(serviceName = "MCmcFacade", beanName = "mCmcFacade")
public interface MCmcFacade extends Facade {

    public Integer getDocsisMode(SnmpParam snmpParam, Long cmIndex);

    public Integer getRealtimeStatusValue(SnmpParam snmpParam, Long cmIndex);

}
