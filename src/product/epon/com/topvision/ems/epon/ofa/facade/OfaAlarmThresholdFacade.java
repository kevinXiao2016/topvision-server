package com.topvision.ems.epon.ofa.facade;

import java.util.List;

import com.topvision.ems.epon.ofa.facade.domain.OfaAlarmThreshold;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 
 * @author w1992wishes
 * @created @2017年10月13日-上午11:24:45
 *
 */
@EngineFacade(serviceName = "OfaAlarmThresholdFacade", beanName = "ofaAlarmThresholdFacade")
public interface OfaAlarmThresholdFacade {

    /**
     * 修改OFA告警阈值配置
     * 
     * @param snmpParam
     * @param ofaAlarmThreshold
     * @return
     */
    OfaAlarmThreshold modifyOfaAlarmThreshold(SnmpParam snmpParam, OfaAlarmThreshold ofaAlarmThreshold);

    /**
     * 获取设备OFA告警阈值配置
     * 
     * @param snmpParam
     * @return
     */
    List<OfaAlarmThreshold> getOfaAlarmThreshold(SnmpParam snmpParam);
}
