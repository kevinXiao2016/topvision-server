package com.topvision.ems.epon.onu.engine;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.onu.domain.OnuEventLogEntry;
import com.topvision.ems.epon.onu.facade.OnuOnOffRecordFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 
 * @author w1992wishes
 * @created @2017年6月15日-下午4:23:12
 *
 */
@Facade("onuOnOffRecordFacade")
public class OnuOnOffRecordFacadeImpl extends EmsFacade implements OnuOnOffRecordFacade{
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public OnuEventLogEntry getOnuOnOffRecords(SnmpParam snmpParam, OnuEventLogEntry onuEventLogEntry) {
        return snmpExecutorService.getTableLine(snmpParam, onuEventLogEntry);
    }

    @Override
    public List<OnuEventLogEntry> getOnuOnOffRecordsAll(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OnuEventLogEntry.class);
    }
}
