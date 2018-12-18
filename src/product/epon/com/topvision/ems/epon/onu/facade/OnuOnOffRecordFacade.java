package com.topvision.ems.epon.onu.facade;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OnuEventLogEntry;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 
 * @author w1992wishes
 * @created @2017年6月15日-下午4:20:52
 *
 */
@EngineFacade(serviceName = "OnuOnOffRecordFacade", beanName = "onuOnOffRecordFacade")
public interface OnuOnOffRecordFacade {
    
    /**
     * 获取ONU上下线历史记录,返回的是字节数（12字节*条数）
     * 
     * @param snmpParam
     * @param onuEventLogEntry
     * @return
     */
    OnuEventLogEntry getOnuOnOffRecords(SnmpParam snmpParam, OnuEventLogEntry onuEventLogEntry);
    
    /**
     * 获取OLT下所有Onu上下线记录，返回的是字节数（12字节*条数）
     * 
     * @param snmpParam
     * @return
     */
    List<OnuEventLogEntry> getOnuOnOffRecordsAll(SnmpParam snmpParam);
}
