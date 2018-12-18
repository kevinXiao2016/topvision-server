/***********************************************************************
 * $ CmPollStatisticsEngineService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.engine.service;

import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;

import java.util.List;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface CmPollStatisticsEngineService  {


    /**
     * 注册推送实例
     * @param cmPollStatisticsPush
     */
    void registCmPollStatisticsPush(CmPollStatisticsPush cmPollStatisticsPush);

    /**
     * 推送结果给各个功能模块
     * @param time
     * @param cmPollResults
     */
    void sendResult(Long time,List<CmPollResult> cmPollResults);
    /**
     * 完成一轮统计推送
     * @param time
     */
    void completeRoundStatistics(Long time);
    /**
     * 开始一轮统计推送
     * @param time
     */
    void startRoundStatistics(Long time);
}