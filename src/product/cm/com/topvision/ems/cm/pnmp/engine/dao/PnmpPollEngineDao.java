/***********************************************************************
 * $Id: PnmpPollEngineDao.java,v1.0 2017-8-15 下午2:27:37 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.engine.dao;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCalculationResult;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollResult;

import java.util.List;

/**
 * @author jay
 * @created @2017-8-15-下午2:27:37
 *
 */
public interface PnmpPollEngineDao {

    void insertPnmpCalculationResult(PnmpCalculationResult pnmpCalculationResult);

    void insertLowPnmpCalculationResult(PnmpCalculationResult pnmpCalculationResult);

    /**
     * 获取mtr的级别（0：health； 1：marginal； 2：bad）
     * @param mtr
     * @return
     */
    Integer getMtrLevel(Double mtr);

    /**
     * 获取上一次mtr的级别（0：health； 1：marginal； 2：bad）
     * @param cmMac
     * @return
     */
    Integer getLastMtrLevel(String cmMac);

    /**
     * 从中频队列移除CM
     * 
     * @param cmMac
     */
    void deleteMiddleMonitorCm(String cmMac);

    /**
     * 增加CM到中频队列
     * 
     * @param cmMac
     */
    void insertMiddleMonitorCm(PnmpPollResult pnmpPollResult);

    /**
     * 从高频队列移除CM
     * 
     * @param cmMac
     */
    void deleteHighMonitorCm(String cmMac);

    /**
     * 增加CM到高频队列
     * 
     * @param cmMac
     */
    void insertHighMonitorCm(PnmpPollResult pnmpPollResult);

    List<PnmpCmData> loadCmDataList(Long cmcId, String cmMac, long from);

    void insertPnmpVariance(PnmpCalculationResult pnmpCalculationResult);
}
