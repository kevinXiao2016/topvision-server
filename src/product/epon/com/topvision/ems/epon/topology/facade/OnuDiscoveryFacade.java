/***********************************************************************
 * $Id: OnuDiscoveryFacade.java,v1.0 2015-8-5 下午2:18:26 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.facade;

import com.topvision.ems.epon.topology.domain.OnuDiscoveryData;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2015-8-5-下午2:18:26
 *
 */
@EngineFacade(serviceName = "OnuDiscoveryFacade", beanName = "onuDiscoveryFacade")
public interface OnuDiscoveryFacade extends Facade {

    /**
     * Onu Topo
     * 
     * @param param
     * @param onuDiscoveryData
     * @return
     */
    OnuDiscoveryData discovery(SnmpParam param, OnuDiscoveryData onuDiscoveryData);
    
    /**
     * Single Onu Topo
     * 
     * @param param
     * @param onuDiscoveryData
     * @return
     */
    OnuDiscoveryData discoveryOnu(SnmpParam param, OnuDiscoveryData onuDiscoveryData);
}
