/***********************************************************************
 * $Id: AutoRefreshFacade.java,v1.0 2014-10-16 上午9:16:35 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.network;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Rod John
 * @created @2014-10-16-上午9:16:35
 * 
 */
@EngineFacade(serviceName = "AutoRefreshFacade", beanName = "autoRefreshFacade")
public interface AutoRefreshFacade extends Facade {

    @SuppressWarnings("rawtypes")
    DiscoveryData autoRefresh(SnmpWorker snmpWorker);

}
