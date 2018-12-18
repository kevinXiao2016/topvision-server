/***********************************************************************
 * $Id: LogicInterfaceFacade.java,v1.0 2016年10月14日 上午10:32:24 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.facede;

import java.util.List;

import com.topvision.ems.epon.logicinterface.domain.InterfaceIpV4Config;
import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:32:24
 *
 */
@EngineFacade(serviceName = "LogicInterfaceFacade", beanName = "logicInterfaceFacade")
public interface LogicInterfaceFacade {

    /**
     * 刷新LogicInterface信息
     * 
     * @param snmpParam
     * @return
     */
    List<LogicInterface> getLogicInterfaceList(SnmpParam snmpParam);

    /**
     * 刷新InterfaceIpV4Config信息
     * 
     * @param snmpParam
     * @return
     */
    List<InterfaceIpV4Config> getInterfaceIpV4ConfigList(SnmpParam snmpParam);

    LogicInterface addLogicInterface(SnmpParam snmpParam, LogicInterface logicInterface);

    LogicInterface getLogicInterface(SnmpParam snmpParam, Integer interfaceType, Integer interfaceId);

    void deleteLogicInterface(SnmpParam snmpParam, LogicInterface logicInterface);

    LogicInterface modifyLogicInterface(SnmpParam snmpParam, LogicInterface logicInterface);

    InterfaceIpV4Config addInterfaceIpV4Config(SnmpParam snmpParam, InterfaceIpV4Config interfaceIpV4Config);

    void deleteInterfaceIpV4Config(SnmpParam snmpParam, InterfaceIpV4Config interfaceIpV4Config);

    InterfaceIpV4Config modifyInterfaceIpV4Config(SnmpParam snmpParam, InterfaceIpV4Config interfaceIpV4Config);

}
