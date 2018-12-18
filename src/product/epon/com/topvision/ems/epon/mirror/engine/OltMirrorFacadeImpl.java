/***********************************************************************
 * $Id: OltMirrorFacadeImpl.java,v1.0 2013-10-25 下午2:22:20 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.mirror.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.ems.epon.mirror.facade.OltMirrorFacade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-下午2:22:20
 *
 */
public class OltMirrorFacadeImpl extends EmsFacade implements OltMirrorFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OltSniMirrorConfig modifyMirrorName(SnmpParam snmpParam, OltSniMirrorConfig oltSniMirrorConfig) {
        return snmpExecutorService.setData(snmpParam, oltSniMirrorConfig);
    }

    @Override
    public OltSniMirrorConfig updateMirrorPortList(SnmpParam snmpParam, OltSniMirrorConfig oltSniMirrorConfig) {
        return snmpExecutorService.setData(snmpParam, oltSniMirrorConfig);
    }
    
    @Override
    public List<OltSniMirrorConfig> getOltMirrorConfigs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltSniMirrorConfig.class);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
