/***********************************************************************
 * $Id: CmcDiscoveryFacade.java,v1.0 2011-11-13 下午01:50:07 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.facade;

import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-11-13-下午01:50:07
 * 
 */
@EngineFacade(serviceName = "CmcDiscoveryFacade", beanName = "cmcDiscoveryFacade")
public interface CmcDiscoveryFacade extends Facade {

    /**
     * 
     * @param param
     * @param cmcDiscoveryData
     * @return
     */
    CmcDiscoveryData discoveryCmc(SnmpParam param, CmcDiscoveryData cmcDiscoveryData);
    
    /**
     * 
     * @param param
     * @param cmcDiscoveryData
     * @return
     */
    CmcDiscoveryData discoveryCm(SnmpParam param, CmcDiscoveryData cmcDiscoveryData);

    /**
     * 刷新单个cc8800a设备信息
     * 
     * @return
     */
    CmcDiscoveryData refreshCC8800A(SnmpParam param, CmcDiscoveryData cmcDiscoveryData);

    /**
     * 自动刷新单个8800a
     * 
     * @param param
     * @param cmcDiscoveryData
     * @return
     */
    CmcDiscoveryData autoDiscoveryCC8800A(SnmpParam param, CmcDiscoveryData cmcDiscoveryData);
    
    /**
     * 自动刷新单个8800b
     * 
     * @param param
     * @param cmcDiscoveryData
     * @return
     */
    CmcDiscoveryData autoDiscoveryCC8800B(SnmpParam param, CmcDiscoveryData cmcDiscoveryData);
    
    String getMacAddress(SnmpParam param);

}
