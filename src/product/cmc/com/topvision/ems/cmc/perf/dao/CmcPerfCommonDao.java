/***********************************************************************
 * $Id: CmcPerfCommonDao.java,v1.0 2013-12-2 上午09:34:27 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.dao;

import java.util.List;

import com.topvision.ems.cmc.performance.domain.CmcChannelStaticInfo;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityStatic;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityStatic;
import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Rod John
 * @created @2013-12-2-上午09:34:27
 * 
 */
public interface CmcPerfCommonDao extends BaseEntityDao<Object> {
    /**
     * 获得数据库中的最近服务质量采集记录
     * 
     * @param cmcId
     * @return
     * 
     */
    CmcServiceQualityStatic loadCmcServiceQuality(Long cmcId);

    /**
     * 从数据库获得最新信号质量采集记录
     * 
     * @param cmcId
     * @return
     */
    List<CmcSignalQualityStatic> loadCmcSignalQuality(Long cmcId);

    /**
     * 从数据库获得最新信号质量采集记录
     * 
     * @param cmcId
     * @param channelIndex
     * @return
     */
    CmcSignalQualityStatic loadCmcSignalQuality(Long cmcId, Long channelIndex);

    /**
     * 获得特定设备的所有信道的流量的最近记录
     * 
     * @param cmcId
     * @return
     */
    List<CmcFlowQuality> loadCmcFlowQuality(Long cmcId);

    /**
     * 获得特定设备的某个信道的流量的最近记录
     * 
     * @param cmcId
     * @param channelIndex
     * @return
     */
    CmcFlowQuality loadCmcFlowQuality(Long cmcId, Long channelIndex);

    /**
     * 获得特定设备的某个信道的信道利用率统计信息
     * 
     * @param cmcId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    CmcChannelStaticInfo loadCmcChannelFlowStatic(Long cmcId, Long channelIndex, String startTime, String endTime);

    /**
     * 获得特定设备的某个信道的信噪比统计信息
     * 
     * @param cmcId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    CmcChannelStaticInfo loadCmcChannelSignalStatic(Long cmcId, Long channelIndex, String startTime, String endTime);
}
