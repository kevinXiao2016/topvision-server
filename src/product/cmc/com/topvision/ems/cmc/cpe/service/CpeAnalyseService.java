/***********************************************************************
 * $ com.topvision.ems.cmc.service.CpeAnalyseService,v1.0 2013-6-20 13:54:46 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.service;

import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CmStatusPerfResult;
import com.topvision.ems.cmc.performance.domain.CpeStatusPerfResult;

import java.util.Map;

/**
 * @author jay
 * @created @2013-6-20-13:54:46
 */
public interface CpeAnalyseService {
    /**
     * 接收cm列表计算CM上下线行为
     * @param cmStatusPerfResult
     */
    void append(CmStatusPerfResult cmStatusPerfResult);

    /**
     * 接收cpe列表计算cpe上下线行为
     * @param cpeStatusPerfResult
     */
    void append(CpeStatusPerfResult cpeStatusPerfResult);

    /**
     * 获得全网有ip设备最后一次CmNum统计
     *
     * @return
     */
    Map<Long,CmNum> getLastCmNum();

    /**
     * 更新Device级别的CmNum最新一次统计数
     * @param cmNum
     */
    void updateDeviceCmNumLast(CmNum cmNum);

    /**
     * 更新Pon口级别的CmNum最新一次统计数
     * @param cmNum
     */
    void updatePonCmNumLast(CmNum cmNum);

    /**
     * 更新Cmts级别的CmNum最新一次统计数
     * @param cmNum
     */
    void updateCmtsCmNumLast(CmNum cmNum);

    /**
     * 更新Port级别的CmNum最新一次统计数
     * @param cmNum
     */
    void updatePortCmNumLast(CmNum cmNum);

    /**
     * 更新设备所有级别的用户数统计
     * @param entityId
     * @param cmtsListMap
     */
    void updateDeviceAllCmNumLast(Long entityId,Map<Long, Map<String, Map<Long, CmNum>>> cmtsListMap, Boolean allDevice);
}
