package com.topvision.ems.epon.ofa.dao;

import java.util.List;

import com.topvision.ems.epon.ofa.facade.domain.OfaAlarmThreshold;

/**
 * 
 * @author w1992wishes
 * @created @2017年10月13日-上午11:08:45
 *
 */
public interface OfaAlarmThresholdDao {

    /**
     * 批量插入或修改OFA告警阈值配置
     * 
     * @param ofaAlarmThreshold
     */
    void batchInsertOrUpdateOfaAlarm(List<OfaAlarmThreshold> ofaAlarmThresholds);

    /**
     * 插入或修改OFA告警阈值配置
     * 
     * @param ofaAlarmThreshold
     */
    void insertOrUpdateOfaAlarm(OfaAlarmThreshold ofaAlarmThreshold);

    /**
     * 获取OFA告警阈值配置
     * 
     * @param entityId
     * @return
     */
    OfaAlarmThreshold getOfaAlarmThresholdById(Long entityId);
}
