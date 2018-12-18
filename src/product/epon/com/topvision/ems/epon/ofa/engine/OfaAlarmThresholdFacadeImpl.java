package com.topvision.ems.epon.ofa.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.ofa.facade.OfaAlarmThresholdFacade;
import com.topvision.ems.epon.ofa.facade.domain.OfaAlarmThreshold;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 
 * @author w1992wishes
 * @created @2017年10月13日-上午11:29:19
 *
 */
@Facade("ofaAlarmThresholdFacade")
public class OfaAlarmThresholdFacadeImpl extends EmsFacade implements OfaAlarmThresholdFacade {

    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OfaAlarmThreshold modifyOfaAlarmThreshold(SnmpParam snmpParam, OfaAlarmThreshold ofaAlarmThreshold) {
        return snmpExecutorService.setData(snmpParam, ofaAlarmThreshold);
    }

    @Override
    public List<OfaAlarmThreshold> getOfaAlarmThreshold(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OfaAlarmThreshold.class);
    }

}
