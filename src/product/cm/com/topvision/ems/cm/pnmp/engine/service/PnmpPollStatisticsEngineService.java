/***********************************************************************
 * $ CmPollStatisticsEngineService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.engine.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCalculationResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface PnmpPollStatisticsEngineService {

    /**
     * 注册推送实例
     * @param pnmpPollStatisticsPush
     */
    void registPnmpPollStatisticsPush(PnmpPollStatisticsPush pnmpPollStatisticsPush);

    /**
     * 推送结果给各个功能模块
     * @param time
     * @param pnmpPollResults
     * @param pnmpPollTask
     */
    void sendResult(Long time, List<PnmpPollResult> pnmpPollResults, PnmpPollTask pnmpPollTask);

    /**
     * 完成一轮统计推送
     * @param time
     * @param pnmpPollTask
     */
    void completeRoundStatistics(Long time, PnmpPollTask pnmpPollTask);

    /**
     * 开始一轮统计推送
     * @param time
     * @param pnmpPollTask
     */
    void startRoundStatistics(Long time, PnmpPollTask pnmpPollTask);

    void insertPnmpCalculationResult(PnmpCalculationResult pnmpCalculationResult, Boolean isLowQueue);

    void dispachPnmpPoll(PnmpPollResult pnmpPollResult, Double mtr);

    void modifyDebugEntity(String cmMac, Double mtr);

    String getDebugCmMac();

    Double getDebugMtr();

    boolean isCalculationFinished();

    void addCalculationCount();

    void delCalculationCount();

    List<PnmpCmData> loadCmDataList(Long cmcId, String cmMac);

    List<PnmpCmData> getPnmpCmDataList(Long cmcId, String cmMac);

    void delCmHisData(Long cmcId, String cmMac);

    void modifyCmHisData(Long cmcId, String cmMac,List<PnmpCmData> cmDataList) ;
}