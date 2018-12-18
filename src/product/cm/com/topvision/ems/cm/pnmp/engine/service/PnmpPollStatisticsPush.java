/***********************************************************************
 * $ CmPollStatisticsPush.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.engine.service;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;

import java.util.List;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface PnmpPollStatisticsPush {
    /**
     * 完成一轮统计
     * @param time
     * @param pnmpPollTask
     */
    void completeRoundStatistics(Long time, PnmpPollTask pnmpPollTask);
    /**
     * 开始一轮统计
     * @param time
     */
    void startRoundStatistics(Long time);

    /**
     * 接收推送的结果
     * @param time
     * @param pnmpPollResults
     * @param pnmpPollTask
     */
    void pushResult(long time, List<PnmpPollResult> pnmpPollResults, PnmpPollTask pnmpPollTask);

    /**
     * 接收结果的功能模块名称
     * @return
     */
    String moduleName();
}