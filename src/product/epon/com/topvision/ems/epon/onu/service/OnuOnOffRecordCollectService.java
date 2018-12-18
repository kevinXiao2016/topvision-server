package com.topvision.ems.epon.onu.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;

import com.topvision.ems.epon.onu.domain.OnuEventLogEntry;
import com.topvision.ems.epon.onu.domain.OnuOnOffRecord;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 
 * @author w1992wishes
 * @created @2017年6月12日-下午2:17:21
 *
 */
public interface OnuOnOffRecordCollectService extends Service {
    /**
     * 更改自动采集时间
     * 
     * @param autoCollectTime
     * @throws SchedulerException
     * @throws ParseException
     */
    void resetCollectTrigger(String autoCollectTime) throws SchedulerException, ParseException;

    /**
     * 从数据库获取单个ONU上下线记录
     * 
     * @param ounId
     * @return
     */
    List<OnuOnOffRecord> getOnuOnOffRecords(Map<String, Object> param);

    /**
     * 获取单个ONU的上下线记录总数
     * 
     * @param onuId
     * @return
     */
    int getRecordCounts(Long onuId);

    /**
     * 刷新onu上下线历史记录
     * 
     * @param onuEventLogEntry
     */
    void refreshOnOffRecords(OnuEventLogEntry onuEventLogEntry);

    /**
     * 刷新onu上下线历史记录
     * 
     * @param snmpParam
     * @param onuEventLogEntry
     */
    void refreshOnOffRecords(SnmpParam snmpParam, OnuEventLogEntry onuEventLogEntry);

    /**
     * 刷新olt下所有onu上下线记录
     * 
     * @param entityId
     */
    void refreshOnOffRecordsAll(Long entityId);

    /**
     * 删除一段时间前的上下线记录
     */
    void deleteOnuOnOffRecords();
}
