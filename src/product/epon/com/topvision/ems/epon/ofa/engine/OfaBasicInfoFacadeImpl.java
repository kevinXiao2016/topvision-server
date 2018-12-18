package com.topvision.ems.epon.ofa.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.ofa.facade.OfaBasicInfoFacade;
import com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

@Facade("ofaBasicInfoFacade")
public class OfaBasicInfoFacadeImpl extends EmsFacade implements OfaBasicInfoFacade {

    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OfaBasicInfo modifyOfaBasicInfo(SnmpParam snmpParam, OfaBasicInfo ofaBasicInfo) {
        return snmpExecutorService.setData(snmpParam, ofaBasicInfo);
    }

    @Override
    public List<OfaBasicInfo> getOfaBasicInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OfaBasicInfo.class);
    }

}
