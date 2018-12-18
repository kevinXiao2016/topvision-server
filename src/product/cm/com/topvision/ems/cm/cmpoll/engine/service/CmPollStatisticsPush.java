/***********************************************************************
 * $ CmPollStatisticsPush.java,v1.0 2012-5-2 10:39:52 $
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
public interface CmPollStatisticsPush {
    /**
     * 完成一轮统计
     * @param time
     */
    void completeRoundStatistics(Long time);
    /**
     * 开始一轮统计
     * @param time
     */
    void startRoundStatistics(Long time);

    /**
     * 接收推送的结果
     * @param time
     * @param cmPollResults
     */
    void pushResult(long time,List<CmPollResult> cmPollResults);

    /**
     * 接收结果的功能模块名称
     * @return
     */
    String moduleName();
}