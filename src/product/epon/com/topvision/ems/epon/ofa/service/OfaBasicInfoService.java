package com.topvision.ems.epon.ofa.service;

import com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo;

public interface OfaBasicInfoService {

    /**
     * 获取OFA基本信息
     * 
     * @param entityId
     * @return
     */
	OfaBasicInfo getOfaBasicInfo(Long entityId);

    /**
     * 修改OFA基本信息
     * 
     * @param topOFAAlarmThresholdEntry
     */
    void modifyOfaBasicInfo(OfaBasicInfo ofaBasicInfo, Long entityId);

    /**
     * 刷新TopOFABasicInfoEntry
     * 
     * @param entityId
     * @return
     */
    void refreshOfaBasicInfo(Long entityId);
}
