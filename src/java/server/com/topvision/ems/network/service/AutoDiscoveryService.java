/***********************************************************************
 * $Id: AutoDiscoveryService.java,v1.0 2014-5-11 下午4:36:01 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2014-5-11-下午4:36:01
 * 
 */
public interface AutoDiscoveryService {
    
    /**
     * 自动发现接口
     * 
     * @param ips
     * @param topoFolderId
     * @param snmpParams
     * @param types
     */
    void autoBatchDiscovery(List<String> ips, Long topoFolderId, List<SnmpParam> snmpParams, List<String> types);
    
    /**
     * 获得当前拓扑标志
     * 
     * @return
     */
    AtomicBoolean getTopoFlag();
    
    
    /**
     * Update Auto Discovery Config 
     * 
     * @param autoDiscoveryPeriodConfig
     */
    void reStartAutoDiscoveryJob();
    
    
    /**
     * Get Auto Discovery Status
     * 
     * @return
     */
    List<BatchAutoDiscoveryIps>  getAutoDiscoveryIpsDiscoveryStatus();
    
    
    /**
     * Get Next Fire Time
     * 
     * @return
     */
    Date getAutoDiscoveryNextFireTime();
    
}
