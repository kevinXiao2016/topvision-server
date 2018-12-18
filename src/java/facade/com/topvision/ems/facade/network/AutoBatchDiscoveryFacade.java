/***********************************************************************
 * $Id: AutoBatchDiscoveryFacade.java,v1.0 2014-5-11 下午3:47:34 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.network;

import java.util.List;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2014-5-11-下午3:47:34
 * 
 */
@EngineFacade(serviceName = "AutoBatchDiscoveryFacade", beanName = "autoBatchDiscoveryFacade")
public interface AutoBatchDiscoveryFacade extends Facade {
    /**
     * 自动发现需求接口方法
     * 
     * @param ips
     * @param folderId
     * @param snmpParams
     * @param types
     * @param callBackURL
     * @param pingTimeout
     * @param pingCount
     */
    void autoBatchDiscovery(List<String> ips, Long folderId, List<SnmpParam> snmpParams, List<EntityType> allTypes,
            List<String> types, String callBackURL, Integer pingTimeout, Integer pingCount, Integer pingRetry);
}
