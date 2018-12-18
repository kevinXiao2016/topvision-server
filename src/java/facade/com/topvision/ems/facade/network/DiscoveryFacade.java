/***********************************************************************
 * $Id: DiscoveryFacade.java,v1.0 2011-6-28 下午07:30:31 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.network;

import java.util.List;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.IpAddressTable;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-6-28-下午07:30:31
 * 
 */
@EngineFacade(serviceName = "DiscoveryFacade", beanName = "discoveryFacade")
public interface DiscoveryFacade extends Facade {
    /**
     * 标准设备发现，仅发现基本信息（RFC1213-MIB中的system，ifTable)
     * 
     * @param snmpParam
     * @return
     * @throws SnmpException
     */
    DiscoveryData discover(SnmpParam snmpParam) throws SnmpException;
    
    
    /**
     * 标准设备发现，发现SYSOID节点
     * 
     * @param snmpParam
     * @return
     * @throws SnmpException
     */
    String discoverySysObjectID(SnmpParam snmpParam) throws SnmpException;
            
    /**
     * Topo SysName
     * 
     * @param snmpParam
     * @return
     * @throws SnmpException
     */
    String discoverySysName(SnmpParam snmpParam) throws SnmpException;
    
    /**
     * 标准IP信息表发现
     * 
     * @param snmpParam
     * @return
     * @throws SnmpException
     */
    List<IpAddressTable> discoveryIpTableInfo(SnmpParam snmpParam) throws SnmpException;
    
}
