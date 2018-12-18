/***********************************************************************
 * $Id: CmtsDiscoveryFacadeImpl.java,v1.0 2013-7-20 下午02:30:59 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.engine;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmts.domain.CmtsBaseInfo;
import com.topvision.ems.cmts.topology.domain.CmtsDiscoveryData;
import com.topvision.ems.cmts.topology.facade.CmtsDiscoveryFacade;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.executor.CpuAndMemSnmpWorker;
import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Rod John
 * @created @2013-7-20-下午02:30:59
 * 
 */
@Facade("cmtsDiscoveryFacade")
public class CmtsDiscoveryFacadeImpl extends BaseEngine implements CmtsDiscoveryFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public CmtsDiscoveryData discovery(SnmpParam param, CmtsDiscoveryData cmtsDiscoveryData) {
        SnmpWorker<CmtsDiscoveryData> worker = new CmtsDiscoverySnmpWorker<CmtsDiscoveryData>(param);
        SnmpWorker<CpuAndMemData> cpuWorker = new CpuAndMemSnmpWorker<>(param);
        try {
            snmpExecutorService.execute(cpuWorker, cmtsDiscoveryData.getCpuAndMemData());
        } catch (Exception e) {
        }
        return snmpExecutorService.execute(worker, cmtsDiscoveryData);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmts.facade.CmtsDiscoveryFacade#discovery(com.topvision.framework.snmp.
     * SnmpParam)
     */
    @Override
    public CmtsDiscoveryData discoveryBsr2000(SnmpParam param, CmtsDiscoveryData cmtsDiscoveryData) {
        SnmpWorker<CmtsDiscoveryData> worker = new Bsr2000DiscoverySnmpWorker<CmtsDiscoveryData>(param);
        SnmpWorker<CpuAndMemData> cpuWorker = new CpuAndMemSnmpWorker<>(param);
        snmpExecutorService.execute(cpuWorker, cmtsDiscoveryData.getCpuAndMemData());
        return snmpExecutorService.execute(worker, cmtsDiscoveryData);
    }

    @Override
    public CmtsBaseInfo getCmtsBaseInfo(SnmpParam param) {
        return snmpExecutorService.getData(param, CmtsBaseInfo.class);
    }

    @Override
    public CmtsDiscoveryData autoDiscovery(SnmpParam param, CmtsDiscoveryData cmtsDiscoveryData) {
        SnmpWorker<CmtsDiscoveryData> worker = new BaseCmtsDiscoverySnmpWorker<CmtsDiscoveryData>(param);
        SnmpWorker<CpuAndMemData> cpuWorker = new CpuAndMemSnmpWorker<>(param);
        snmpExecutorService.execute(cpuWorker, cmtsDiscoveryData.getCpuAndMemData());
        return snmpExecutorService.execute(worker, cmtsDiscoveryData);
    }

}
