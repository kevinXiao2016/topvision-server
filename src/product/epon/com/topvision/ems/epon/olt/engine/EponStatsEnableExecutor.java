/***********************************************************************
 * $Id: EponStatsEnableExecutor.java,v1.0 2013-12-13 上午11:03:11 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.batchdeploy.domain.Level;
import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.facade.batchdeploy.BatchDeployExecutor;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2013-12-13-上午11:03:11
 *
 */
@Service("eponStatsEnableExecutor")
public class EponStatsEnableExecutor implements BatchDeployExecutor<Target, Integer> {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public boolean deploy(Target target, Integer status, SnmpParam snmpParam) {
        Integer level = target.getLevel();
        if (level == Level.SNI) {
            OltSniAttribute sniAttribute = new OltSniAttribute();
            sniAttribute.setSniIndex(target.getTargetIndex());
            sniAttribute.setSniPerfStats15minuteEnable(status);
            snmpExecutorService.setData(snmpParam, sniAttribute);
        } else if (level == Level.PON) {
            OltPonAttribute oltPonAttribute = new OltPonAttribute();
            oltPonAttribute.setPonIndex(target.getTargetIndex());
            oltPonAttribute.setPerfStats15minuteEnable(status);
            snmpExecutorService.setData(snmpParam, oltPonAttribute);
        } else if (level == Level.ONU) {
            OltTopOnuCapability oltTopOnuCapability = new OltTopOnuCapability();
            oltTopOnuCapability.setOnuIndex(target.getTargetIndex());
            oltTopOnuCapability.setPonPerfStats15minuteEnable(status);
            snmpExecutorService.setData(snmpParam, oltTopOnuCapability);
        } else if (level == Level.UNI) {
            OltUniExtAttribute oltTopUniAttribute = new OltUniExtAttribute();
            oltTopUniAttribute.setUniIndex(target.getTargetIndex());
            oltTopUniAttribute.setPerfStats15minuteEnable(status);
            snmpExecutorService.setData(snmpParam, oltTopUniAttribute);
        }
        return true;
    }
}
