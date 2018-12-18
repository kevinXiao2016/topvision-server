/***********************************************************************
 * $Id: LogicInterfaceFacadeImpl.java,v1.0 2016年10月14日 上午10:31:42 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.logicinterface.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.logicinterface.domain.InterfaceIpV4Config;
import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.ems.epon.logicinterface.facede.LogicInterfaceFacade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;

/**
 * @author lzt
 * @created @2016年10月14日-上午10:31:42
 *
 */
public class LogicInterfaceFacadeImpl extends EmsFacade implements LogicInterfaceFacade {
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public List<LogicInterface> getLogicInterfaceList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, LogicInterface.class);
    }

    @Override
    public List<InterfaceIpV4Config> getInterfaceIpV4ConfigList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, InterfaceIpV4Config.class);
    }

    @Override
    public LogicInterface addLogicInterface(SnmpParam snmpParam, LogicInterface logicInterface) {
        logicInterface.setInterfaceRowStatus(EponConstants.TB_CREATEANDGO);
        return snmpExecutorService.setData(snmpParam, logicInterface);
    }

    @Override
    public LogicInterface getLogicInterface(SnmpParam snmpParam, Integer interfaceType, Integer interfaceId) {
        LogicInterface logicInterface = new LogicInterface();
        logicInterface.setInterfaceType(interfaceType);
        logicInterface.setInterfaceId(interfaceId);
        return snmpExecutorService.getTableLine(snmpParam, logicInterface);
    }

    @Override
    public void deleteLogicInterface(SnmpParam snmpParam, LogicInterface logicInterface) {
        logicInterface.setInterfaceRowStatus(EponConstants.TB_DROP);
        snmpExecutorService.setData(snmpParam, logicInterface);
    }

    @Override
    public LogicInterface modifyLogicInterface(SnmpParam snmpParam, LogicInterface logicInterface) {
        return snmpExecutorService.setData(snmpParam, logicInterface);
    }

    @Override
    public InterfaceIpV4Config addInterfaceIpV4Config(SnmpParam snmpParam, InterfaceIpV4Config interfaceIpV4Config) {
        interfaceIpV4Config.setIpV4ConfigRowStatus(EponConstants.TB_CREATEANDGO);
        return snmpExecutorService.setData(snmpParam, interfaceIpV4Config);
    }

    @Override
    public void deleteInterfaceIpV4Config(SnmpParam snmpParam, InterfaceIpV4Config interfaceIpV4Config) {
        interfaceIpV4Config.setIpV4ConfigRowStatus(EponConstants.TB_DROP);
        snmpExecutorService.setData(snmpParam, interfaceIpV4Config);
    }

    @Override
    public InterfaceIpV4Config modifyInterfaceIpV4Config(SnmpParam snmpParam, InterfaceIpV4Config interfaceIpV4Config) {
        return snmpExecutorService.setData(snmpParam, interfaceIpV4Config);
    }

}
