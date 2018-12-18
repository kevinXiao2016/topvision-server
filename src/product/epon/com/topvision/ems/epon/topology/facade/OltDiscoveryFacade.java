/***********************************************************************
 * $Id: OltDiscoveryFacade.java,v1.0 2013-10-26 上午10:18:19 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.facade;

import java.util.Map;

import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-9-16-下午04:34:41
 * 
 */
@EngineFacade(serviceName = "OltDiscoveryFacade", beanName = "oltDiscoveryFacade")
public interface OltDiscoveryFacade extends Facade {

    /**
     * olt拓扑发现
     * 
     * @param param
     * @param oltDiscoveryData
     * @return
     */
    OltDiscoveryData discovery(SnmpParam param, OltDiscoveryData oltDiscoveryData);

    /**
     * olt 自动发现
     * 
     * @param param
     * @param oltDiscoveryData
     * @return
     */
    OltDiscoveryData autoDiscovery(SnmpParam param, OltDiscoveryData oltDiscoveryData);

    /**
     * 获取所有ONU的Mac地址
     * 
     * @param snmpParam
     * @return
     */
    Map<String, String> getOnuMacAddress(SnmpParam snmpParam);

}
