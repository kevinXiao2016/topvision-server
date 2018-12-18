/***********************************************************************
 * $Id: OnuCatvConfigFacadeImpl.java,v1.0 2016-4-27 上午10:24:56 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.onu.domain.OnuCatvConfig;
import com.topvision.ems.epon.onu.facade.OnuCatvFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2016-4-27-上午10:24:56
 *
 */
@Facade("onuCatvConfigFacade")
public class OnuCatvFacadeImpl extends EmsFacade implements OnuCatvFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public void modifyOnuCatvConfig(SnmpParam snmpParam, OnuCatvConfig onuCatvConfig) {
        snmpExecutorService.setData(snmpParam, onuCatvConfig);
    }

    @Override
    public OnuCatvConfig getOnuCatvConfig(SnmpParam snmpParam, OnuCatvConfig onuCatvConfig) {
        return snmpExecutorService.getTableLine(snmpParam, onuCatvConfig);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public List<OnuCatvConfig> getOnuCatvConfigAll(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuCatvConfig.class);
    }

}
