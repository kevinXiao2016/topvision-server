/***********************************************************************
 * $Id: GponUniFilterMacFacadeImpl.java,v1.0 2016年12月24日 下午12:15:13 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.unifiltermac.engine;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.gpon.unifiltermac.facade.GponUniFilterMacFacade;
import com.topvision.ems.gpon.unifiltermac.facade.domain.GponUniFilterMac;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author jay
 * @created @2016年12月24日-下午12:15:13
 *
 */
@Facade("gponUniFilterMacFacade")
public class GponUniFilterMacFacadeImpl extends EmsFacade implements GponUniFilterMacFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;


    @Override
    public void addGponUniFilterMac(SnmpParam snmpParam, GponUniFilterMac gponUniFilterMac) {
        snmpExecutorService.setData(snmpParam,gponUniFilterMac);
    }

    @Override
    public void deleteGponUniFilterMac(SnmpParam snmpParam, GponUniFilterMac gponUniFilterMac) {
        snmpExecutorService.setData(snmpParam,gponUniFilterMac);
    }

    @Override
    public List<GponUniFilterMac> refreshGponUniFilterMacForUni(SnmpParam snmpParam, GponUniFilterMac gponUniFilterMac) {
        return snmpExecutorService.getTableLines(snmpParam,gponUniFilterMac,0,Integer.MAX_VALUE);
    }

    @Override
    public List<GponUniFilterMac> refreshGponUniFilterMacForEntity(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam,GponUniFilterMac.class);
    }
}
