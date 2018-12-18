package com.topvision.ems.epon.ofa.service;

import java.util.List;

import com.topvision.ems.epon.ofa.facade.domain.OfaAlarmThreshold;

/**
 * 
 * @author w1992wishes
 * @created @2017年10月13日-上午11:01:34
 *
 */
public interface OfaAlarmThresholdService {

    /**
     * 修改OFA告警阈值配置
     * 
     * @param ofaAlarmThreshold
     */
    OfaAlarmThreshold modifyOfaAlarmThreshold(OfaAlarmThreshold ofaAlarmThreshold);

    /**
     * 获取OFA告警阈值配置
     * 
     * @param entityId
     * @return
     */
    OfaAlarmThreshold getOfaAlarmThresholdById(Long entityId);

    /**
     * 获取OFA告警阈值配置
     * 
     * @param entityId
     * @return
     */
    List<OfaAlarmThreshold> fetchOfaAlarmThreshold(Long entityId);
}
