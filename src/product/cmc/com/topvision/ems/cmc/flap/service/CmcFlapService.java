/***********************************************************************
 * $Id: CmcPerService.java,v1.0 2012-5-8 上午10:53:22 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.flap.service;

import java.util.List;

import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.cmc.performance.facade.CmFlap;

/**
 * @author loyal
 * @created @2012-5-8-上午10:53:22
 * 
 */
public interface CmcFlapService {

    /**
     * 获取Flap时间
     * 
     * @param entityId
     * @param cmcId
     * @return
     */
    Integer getTopCmFlapInterval(Long cmcId);

    /**
     * 设置Flap时间
     * 
     * @param entityId
     * @param cmcId
     * @param topCmFlapInterval
     */
    void cmFlapConfig(Long cmcId, Integer topCmFlapInterval);

    void resetFlapCounters(Long cmcId);

    void modifyFlapMonitor(Long cmcId, Long interval);

    void stopFlapMonitor(Long cmcId);

    boolean hasFlapMonitor(Long cmcId);

    /**
     * 查询历史记录-modified by bryan time由Date改为String
     * 
     * @param cmMac
     * @param startTime
     * @param endTime
     * @return
     */
    List<CMFlapHis> queryOneCMFlapHis(String cmMac, Long startTime, Long endTime);

    /**
     * @deprecated 性能采集放入统一管理 获取CC上针对FLAP的采集器的采集间隔
     * @param cmMac
     * @param startTime
     * @param endTime
     * @return
     */
    Long queryCmcFlapMonitorInterval(Long cmcId);

    /**
     * 获取单台CM的FLAP数据统计信息
     * 
     * @param cmId
     * @return
     */
    CmFlap queryCmFlapInfo(Long cmId);

}