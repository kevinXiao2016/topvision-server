/***********************************************************************
 * $Id: UniPortRateLimitExecutor.java,v1.0 2014-5-19 下午2:29:42 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.UniRateLimitTemplate;
import com.topvision.ems.facade.batchdeploy.BatchDeployExecutor;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2014-5-19-下午2:29:42
 *
 */

@Service("uniPortRateLimitExecutor")
public class UniPortRateLimitExecutor implements BatchDeployExecutor<Target, UniRateLimitTemplate> {

    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public boolean deploy(Target target, UniRateLimitTemplate bundle, SnmpParam snmpParam) {
        OltUniPortRateLimit portRateLimit = new OltUniPortRateLimit();
        portRateLimit.setEntityId(bundle.getEntityId());
        portRateLimit.setUniIndex(target.getTargetIndex());
        portRateLimit.setUniPortInCBS(bundle.getPortInCBS());
        portRateLimit.setUniPortInCIR(bundle.getPortInCIR());
        portRateLimit.setUniPortInEBS(bundle.getPortInEBS());
        portRateLimit.setUniPortInRateLimitEnable(bundle.getPortInLimitEnable());
        portRateLimit.setUniPortOutCIR(bundle.getPortOutCIR());
        portRateLimit.setUniPortOutPIR(bundle.getPortOutPIR());
        portRateLimit.setUniPortOutRateLimitEnable(bundle.getPortOutLimtEnable());
        snmpExecutorService.setData(snmpParam, portRateLimit);
        return true;
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
