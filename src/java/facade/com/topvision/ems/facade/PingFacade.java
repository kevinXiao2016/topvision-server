/***********************************************************************
 * $Id: PingFacade.java,v 1.1 Jul 23, 2008 11:08:47 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.PingResultsEntry;
import com.topvision.exception.facade.DeviceNotExistException;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @Create Date Jul 23, 2008 11:08:47 PM
 * 
 * @author kelers
 * 
 */
@EngineFacade(serviceName = "PingFacade", beanName = "pingFacade")
public interface PingFacade extends Facade {
    /**
     * 
     * @param ip
     * @param timeout
     * @param count
     * @return
     * @throws DeviceNotExistException
     * @throws Exception
     */
    int ping(String ip, Integer timeout, Integer count) throws DeviceNotExistException, NetworkException;

    /**
     * 
     * @param ip
     * @param timeout
     * @param count
     * @param result
     * @return
     * @throws DeviceNotExistException
     * @throws Exception
     */
    PingResult ping(String ip, Integer timeout, Integer count, PingResult result)
            throws DeviceNotExistException, Exception;

    /**
     * SNMP PING 
     * 
     * DISMAN-PING-MIB pingCtlEntry
     * 
     * @param snmpParam
     * @param entityId
     * @param destIp
     * @param srcIp
     * @param timeout
     * @param count
     * @return
     */
    PingResultsEntry snmpPing(SnmpParam snmpParam, Long entityId, String destIp, String srcIp, Integer timeout,
            Integer count);

    /**
     * Map<String,String>=Map<ip,mac>
     * 
     * @param ips
     * @param timeout
     * @param count
     * @return map<ip,mac> of pinged
     * @throws Exception
     */
    Map<String, String> scan(List<String> ips, Integer timeout, Integer count) throws Exception;

    /**
     * Map<String,String>=Map<ip,mac>
     * 
     * @param target
     * @param timeout
     * @param count
     * @return map<ip,mac> of pinged
     * @throws Exception
     */
    Map<String, String> scan(String target, Integer timeout, Integer count) throws Exception;
}
