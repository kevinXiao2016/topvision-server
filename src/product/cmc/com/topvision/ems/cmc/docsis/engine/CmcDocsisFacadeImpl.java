/***********************************************************************
 * $Id: CmcDocsisFacadeImpl.java,v1.0 2013-4-27 上午9:06:24 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.docsis.engine;

import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.docsis.facade.CmcDocsisFacade;
import com.topvision.ems.cmc.docsis.facade.domain.CmcDocsisConfig;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-4-27-上午9:06:24
 *
 */
@Facade("cmcDocsisFacade")
public class CmcDocsisFacadeImpl extends EmsFacade implements CmcDocsisFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcDocsisFacade#updateDocsisToFacility(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcDocsisConfig)
     */
    @Override
    public CmcDocsisConfig updateDocsisToFacility(SnmpParam snmpParam, CmcDocsisConfig cmcDocsis) {
        return snmpExecutorService.setData(snmpParam, cmcDocsis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcDocsisFacade#getDocsisFromFacility(com.topvision.framework
     * .snmp.SnmpParam, java.lang.Long)
     */
    @Override
    public CmcDocsisConfig getDocsisFromFacility(SnmpParam snmpParam, Long cmcIndex) {
        CmcDocsisConfig docsis = new CmcDocsisConfig();
        docsis.setIfIndex(cmcIndex);
        return snmpExecutorService.getTableLine(snmpParam, docsis);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public List<CmcDocsisConfig> getDocsisconfigList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcDocsisConfig.class);
    }

}
