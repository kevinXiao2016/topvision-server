/***********************************************************************
 * $Id: UniVlanProfileBindExecutor.java,v1.0 2013-12-4 下午2:29:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.facade.batchdeploy.BatchDeployExecutor;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-12-4-下午2:29:42
 *
 */
@Service("uniVlanProfileBindExecutor")
public class UniVlanProfileBindExecutor implements BatchDeployExecutor<Target, UniVlanProfileTable> {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public boolean deploy(Target target, UniVlanProfileTable bundle, SnmpParam snmpParam) {
        UniVlanBindTable bindTable = new UniVlanBindTable();
        bindTable.setUniIndex(target.getTargetIndex());
        bindTable.setBindProfileId(bundle.getProfileId());
        bindTable.setBindPvid(bundle.getPvid());
        snmpExecutorService.setData(snmpParam, bindTable);
        return true;
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }
}
