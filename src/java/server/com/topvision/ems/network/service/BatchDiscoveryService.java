/***********************************************************************
 * $Id: BatchDiscoveryService.java,v1.0 2012-10-31 下午04:50:01 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.topvision.ems.network.parser.TopologyHandle;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.UserContext;

/**
 * @author RodJohn
 * @created @2012-10-31-下午04:50:01
 * 
 */
public interface BatchDiscoveryService extends Service {

    /**
     * 多设备发现的接口方法
     * 
     * @param dwrId
     * @param sessionId
     * @param ips
     * @param entityNames
     * @param snmpParams
     * @param types
     */
    void batchDiscovery(List<Long> ids, String dwrId, String jconnectID, List<String> ips, List<String> entityNames,
            List<String> topoNames, List<SnmpParam> snmpParams, List<String> types, UserContext userContext);

    /**
     * 关闭设备采集的接口方法
     * 
     */
    void shutDownBatchDiscovery();

    /**
     * 获得当前拓扑标志
     * 
     * @return
     */
    AtomicBoolean getTopoFlag();

    /**
     * 
     * 
     * @param sysOid
     * @param handle
     */
    void registerTopoHandler(String sysOid, TopologyHandle handle);

    /**
     * 
     * 
     * @param sysOid
     */
    void unregisterTopoHandler(String sysOid);

    /**
     * 
     * @param sysOid
     * @return
     */
    TopologyHandle getTopologyHandle(String sysOid);

}
