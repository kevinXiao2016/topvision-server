/***********************************************************************
 * $Id: OltRstpFacadeImpl.java,v1.0 2013-10-25 下午5:37:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.rstp.domain.OltStpGlobalConfig;
import com.topvision.ems.epon.rstp.domain.OltStpPortConfig;
import com.topvision.ems.epon.rstp.facade.OltRstpFacade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午5:37:33
 *
 */
public class OltRstpFacadeImpl extends EmsFacade implements OltRstpFacade {
    private SnmpExecutorService snmpExecutorService;

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltStpFacade#getOltStpGlobalConfig(com.topvision.framework.
     * snmp.SnmpParam)
     */
    @Override
    public OltStpGlobalConfig getOltStpGlobalConfig(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, OltStpGlobalConfig.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltStpFacade#getOltStpPortConfigs(com.topvision.framework.snmp
     * .SnmpParam)
     */
    @Override
    public List<OltStpPortConfig> getOltStpPortConfigs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltStpPortConfig.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltStpFacade#updateOltStpGlobalEnable(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltStpGlobalConfig)
     */
    @Override
    public void updateOltStpGlobalEnable(SnmpParam snmpParam, OltStpGlobalConfig globalConfig) {
        snmpExecutorService.setData(snmpParam, globalConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltStpFacade#updateOltStpGlobalConfig(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltStpGlobalConfig)
     */
    @Override
    public void updateOltStpGlobalConfig(SnmpParam snmpParam, OltStpGlobalConfig globalConfig) {
        snmpExecutorService.setData(snmpParam, globalConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.facade.OltStpFacade#updateOltStpPortConfig(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.epon.facade.domain.OltStpPortConfig)
     */
    @Override
    public void updateOltStpPortConfig(SnmpParam snmpParam, OltStpPortConfig portConfig) {
        snmpExecutorService.setData(snmpParam, portConfig);
    }

}
