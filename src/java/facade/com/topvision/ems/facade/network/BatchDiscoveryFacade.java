/***********************************************************************
 * $Id: BatchDiscoveryFacade.java,v1.0 2012-11-16 上午10:51:34 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.network;

import java.util.List;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.DwrInfo;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author RodJohn
 * @created @2012-11-16-上午10:51:34
 * 
 */
@EngineFacade(serviceName = "BatchDiscoveryFacade", beanName = "batchDiscoveryFacade")
public interface BatchDiscoveryFacade extends Facade {

    /**
     * 多设备发现的接口方法
     * 
     * @param ips
     * @param snmpParams
     * @param types
     * @param dwrInfo
     * @param pingTimeout
     */
    void batchDiscovery(List<String> ips, List<String> entityNames, List<String> topoNames, List<SnmpParam> snmpParams,
            List<EntityType> allTypes, List<String> types, DwrInfo dwrInfo, Integer pingTimeout, Integer pingCount, Integer pingRetry);

    /**
     * 拓扑过程中停止
     * 
     */
    void shutDownDiscovery();

}
