/***********************************************************************
 * $Id: LoopBackFacadeImpl.java,v1.0 2013-11-16 上午11:54:27 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.loopback.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.loopback.domain.LoopbackConfigTable;
import com.topvision.ems.epon.loopback.domain.LoopbackSubIpTable;
import com.topvision.ems.epon.loopback.facade.LoopBackFacade;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-11-16-上午11:54:27
 * 
 */
public class LoopBackFacadeImpl extends EmsFacade implements LoopBackFacade {
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.loopback.facade.LoopBackFacade#getLoopbackConfigTables(com.topvision
     * .framework.snmp.SnmpParam)
     */
    @Override
    public List<LoopbackConfigTable> getLoopbackConfigTables(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, LoopbackConfigTable.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.loopback.facade.LoopBackFacade#getLoopbackSubIpTables(com.topvision
     * .framework.snmp.SnmpParam)
     */
    @Override
    public List<LoopbackSubIpTable> getLoopbackSubIpTables(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, LoopbackSubIpTable.class);
    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public LoopbackConfigTable addLoopBackInterface(SnmpParam snmpParam, LoopbackConfigTable loopBack) {
        loopBack.setLoopbackRowStatus(EponConstants.TB_CREATEANDGO);
        return snmpExecutorService.setData(snmpParam, loopBack);
    }

    @Override
    public void deleteLoopBackInterface(SnmpParam snmpParam, LoopbackConfigTable loopBack) {
        loopBack.setLoopbackRowStatus(EponConstants.TB_DROP);
        snmpExecutorService.setData(snmpParam, loopBack);
    }

    @Override
    public LoopbackSubIpTable addLoopBackSubIp(SnmpParam snmpParam, LoopbackSubIpTable subIpTable) {
        subIpTable.setLoopbackSubIpRowStatus(EponConstants.TB_CREATEANDGO);
        return snmpExecutorService.setData(snmpParam, subIpTable);
    }

    @Override
    public void deleteLoopBackSubIp(SnmpParam snmpParam, LoopbackSubIpTable subIpTable) {
        subIpTable.setLoopbackSubIpRowStatus(EponConstants.TB_DROP);
        snmpExecutorService.setData(snmpParam, subIpTable);
    }

    @Override
    public LoopbackConfigTable modifyLoopBackInterface(SnmpParam snmpParam, LoopbackConfigTable loopBack) {
        return snmpExecutorService.setData(snmpParam, loopBack);
    }

    @Override
    public LoopbackSubIpTable modifyLoopBackSubIp(SnmpParam snmpParam, LoopbackSubIpTable subIpTable) {
        return snmpExecutorService.setData(snmpParam, subIpTable);
    }

}
