/***********************************************************************
 * $ PnmpPollStatisticsEngineServiceImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.engine.service.impl;

import java.util.*;
import java.util.concurrent.DelayQueue;

import com.topvision.ems.cm.pnmp.facade.domain.*;
import com.topvision.platform.domain.SystemPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cm.pnmp.engine.dao.PnmpPollEngineDao;
import com.topvision.ems.cm.pnmp.engine.service.PnmpPollStatisticsEngineService;
import com.topvision.ems.cm.pnmp.engine.service.PnmpPollStatisticsPush;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Engine
public class PnmpPollStatisticsEngineServiceImpl extends BaseEngine implements PnmpPollStatisticsEngineService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static int CM20 = 1;
    private static int CM30 = 2;
    @Autowired
    private SnmpExecutorService snmpExecutorService;
    @Autowired
    private PingExecutorService pingExecutorService;
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    private PnmpPollEngineDao pnmpPollEngineDao;
    private SnmpParam snmpParam;
    @Value("${PNMP.varianceInterval:604800000}")
    private Long varianceInterval;

    private List<PnmpPollStatisticsPush> pnmpPollStatisticsPushs = Collections
            .synchronizedList(new ArrayList<PnmpPollStatisticsPush>());

    private static DelayQueue<DelayItem<String>> toMiddleRateDelayQueue = new DelayQueue<DelayItem<String>>();
    private static Object middleRateSy = new Object();
    private static DelayQueue<DelayItem<String>> toLowRateDelayQueue = new DelayQueue<DelayItem<String>>();
    private static Object lowRateSy = new Object();

    private Long middleToLow = 48 * 60 * 60 * 1000L;
    private Long highToMiddle = 2 * 60 * 60 * 1000L;
    private Long highToLow = 2 * 60 * 60 * 1000L;

    private String debugCmMac;
    private Double debugMtr;

    private int executeCalculationCounts = 0;
    private static Object executeCalculationCountsFlag = new Object();
    private static Map<Long,Map<String,List<PnmpCmData>>> cmHisData = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void initialize() {
        super.initialize();

    }

    private void marginalToHealth(String cmMac) {
        logger.trace("PnmpPollStatisticsEngineServiceImpl.marginalToHealth");
        DelayItem<String> di = new DelayItem<String>(cmMac);
        synchronized (lowRateSy) {
            if (!toLowRateDelayQueue.contains(di)) {
                di.setTimeout(middleToLow);
                toLowRateDelayQueue.add(di);
            }
        }
    }

    private void healthToMarginal(PnmpPollResult pnmpPollResult) {
        logger.trace("PnmpPollStatisticsEngineServiceImpl.healthToMarginal");
        DelayItem<String> di = new DelayItem<String>(pnmpPollResult.getCmMac());
        synchronized (middleRateSy) {
            if (!toMiddleRateDelayQueue.contains(di)) {
                pnmpPollEngineDao.insertMiddleMonitorCm(pnmpPollResult);
            }
        }
        synchronized (lowRateSy) {
            try {
                toLowRateDelayQueue.remove(di);
            } catch (Exception e) {
                logger.trace("", e);
            }
        }
    }

    private void badToMarginal(String cmMac) {
        logger.trace("PnmpPollStatisticsEngineServiceImpl.badToMarginal");
        DelayItem<String> di = new DelayItem<String>(cmMac);
        di.setTimeout(highToMiddle);
        synchronized (middleRateSy) {
            toMiddleRateDelayQueue.add(di);
        }
    }

    private void badToHealth(String cmMac) {
        logger.trace("PnmpPollStatisticsEngineServiceImpl.badToHealth");
        DelayItem<String> di = new DelayItem<String>(cmMac);
        di.setTimeout(highToLow);
        synchronized (lowRateSy) {
            toLowRateDelayQueue.add(di);
        }
    }

    private void anyToBad(PnmpPollResult pnmpPollResult) {
        logger.trace("PnmpPollStatisticsEngineServiceImpl.anyToBad");
        DelayItem<String> di = new DelayItem<String>(pnmpPollResult.getCmMac());
        try {
            pnmpPollEngineDao.deleteMiddleMonitorCm(pnmpPollResult.getCmMac());
        } catch (Exception e) {
            logger.trace("", e);
        }
        try {
            pnmpPollEngineDao.insertHighMonitorCm(pnmpPollResult);
        } catch (Exception e) {
            logger.trace("", e);
        }
        synchronized (lowRateSy) {
            try {
                toLowRateDelayQueue.remove(di);
            } catch (Exception e) {
                logger.trace("", e);
            }
        }
        synchronized (middleRateSy) {
            try {
                toMiddleRateDelayQueue.remove(di);
            } catch (Exception e) {
                logger.trace("", e);
            }
        }
    }

    @Override
    public void registPnmpPollStatisticsPush(PnmpPollStatisticsPush pnmpPollStatisticsPush) {
        pnmpPollStatisticsPushs.add(pnmpPollStatisticsPush);
    }

    @Override
    public void sendResult(Long time, List<PnmpPollResult> pnmpPollResults, PnmpPollTask pnmpPollTask) {
        logger.trace("PnmpPollStatisticsEngineServiceImpl.sendResult");
        for (PnmpPollStatisticsPush pnmpPollStatisticsPush : pnmpPollStatisticsPushs) {
            try {
                pnmpPollStatisticsPush.pushResult(time, pnmpPollResults, pnmpPollTask);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("sendResult error[" + pnmpPollStatisticsPush.moduleName() + "]", e);
                }
            }
        }
    }

    @Override
    public void completeRoundStatistics(Long time, PnmpPollTask pnmpPollTask) {
        logger.trace("PnmpPollStatisticsEngineServiceImpl.completeRoundStatistics ");
        for (PnmpPollStatisticsPush pnmpPollStatisticsPush : pnmpPollStatisticsPushs) {
            try {
                pnmpPollStatisticsPush.completeRoundStatistics(time, pnmpPollTask);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("completeRoundStatistics error[" + pnmpPollStatisticsPush.moduleName() + "]", e);
                }
            }
        }
    }

    @Override
    public void startRoundStatistics(Long time, PnmpPollTask pnmpPollTask) {
        logger.trace("PnmpPollStatisticsEngineServiceImpl.startRoundStatistics ");
        for (PnmpPollStatisticsPush pnmpPollStatisticsPush : pnmpPollStatisticsPushs) {
            try {
                pnmpPollStatisticsPush.startRoundStatistics(time);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("startRoundStatistics error[" + pnmpPollStatisticsPush.moduleName() + "]", e);
                }
            }
        }
    }

    @Override
    public void insertPnmpCalculationResult(PnmpCalculationResult pnmpCalculationResult, Boolean isLowQueue) {
        Integer mtrLevel = pnmpPollEngineDao.getMtrLevel(pnmpCalculationResult.getMtr());
        if (mtrLevel != pnmpCalculationResult.NULL) {
            pnmpCalculationResult.setMtrLevel(mtrLevel);
        }
        if (isLowQueue) {
            pnmpPollEngineDao.insertLowPnmpCalculationResult(pnmpCalculationResult);
        } else {
            pnmpPollEngineDao.insertPnmpCalculationResult(pnmpCalculationResult);
            pnmpPollEngineDao.insertPnmpVariance(pnmpCalculationResult);
        }
    }

    @Override
    public void dispachPnmpPoll(PnmpPollResult pnmpPollResult, Double mtr) {
        Integer lastMtrLevel = pnmpPollEngineDao.getLastMtrLevel(pnmpPollResult.getCmMac());
        Integer mtrLevel = pnmpPollEngineDao.getMtrLevel(mtr);
        if (logger.isTraceEnabled()) {
            logger.trace("dispachPnmpPoll mtr:[" + mtr + "] mtrLevel[" + mtrLevel + "] lastMtrLevel[" + lastMtrLevel
                    + "]");
        }

        if (mtrLevel != PnmpCalculationResult.NULL) {
            if (mtrLevel == PnmpCalculationResult.BAD) {
                anyToBad(pnmpPollResult);
            }
            if (mtrLevel == PnmpCalculationResult.MARGINAL) {
                if (lastMtrLevel == PnmpCalculationResult.HEALTH) {
                    healthToMarginal(pnmpPollResult);
                }
                if (lastMtrLevel == PnmpCalculationResult.BAD) {
                    badToMarginal(pnmpPollResult.getCmMac());
                }
            }
            if (mtrLevel == PnmpCalculationResult.HEALTH) {
                if (lastMtrLevel == PnmpCalculationResult.MARGINAL) {
                    marginalToHealth(pnmpPollResult.getCmMac());
                }
                if (lastMtrLevel == PnmpCalculationResult.BAD) {
                    badToHealth(pnmpPollResult.getCmMac());
                }
            }
        }
    }

    @Override
    public void modifyDebugEntity(String cmMac, Double mtr) {
        this.debugCmMac = cmMac;
        this.debugMtr = mtr;
    }

    @Override
    public void connected() {
        super.connected();
        pnmpPollEngineDao = engineDaoFactory.getEngineDao(PnmpPollEngineDao.class);
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DelayItem<String> di = toLowRateDelayQueue.take();
                        try {
                            pnmpPollEngineDao.deleteMiddleMonitorCm(di.getItem());
                        } catch (Exception e) {
                            logger.trace("", e);
                        }
                        try {
                            pnmpPollEngineDao.deleteHighMonitorCm(di.getItem());
                        } catch (Exception e) {
                            logger.trace("", e);
                        }
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DelayItem<String> di = toMiddleRateDelayQueue.take();
                        try {
                            pnmpPollEngineDao.deleteHighMonitorCm(di.getItem());
                        } catch (Exception e) {
                            logger.trace("", e);
                        }
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }.start();
    }

    @Override
    public void disconnected() {
        super.disconnected();
    }

    public String getDebugCmMac() {
        return debugCmMac;
    }

    public void setDebugCmMac(String debugCmMac) {
        this.debugCmMac = debugCmMac;
    }

    public Double getDebugMtr() {
        return debugMtr;
    }

    @Override
    public boolean isCalculationFinished() {
        synchronized (executeCalculationCountsFlag) {
            return executeCalculationCounts == 0;
        }
    }

    @Override
    public void addCalculationCount() {
        synchronized (executeCalculationCountsFlag) {
            executeCalculationCounts++;
        }
    }

    @Override
    public void delCalculationCount() {
        synchronized (executeCalculationCountsFlag) {
            executeCalculationCounts--;
        }
    }

    @Override
    public List<PnmpCmData> loadCmDataList(Long cmcId, String cmMac) {
        long from = System.currentTimeMillis() - varianceInterval;
        return pnmpPollEngineDao.loadCmDataList(cmcId,cmMac,from);
    }

    public void setDebugMtr(Double debugMtr) {
        this.debugMtr = debugMtr;
    }

    public List<PnmpCmData> getPnmpCmDataList(Long cmcId, String cmMac) {
        List<PnmpCmData> re = null;
        Map<String,List<PnmpCmData>> cmMap = null;
        if (cmHisData.containsKey(cmcId)) {
            cmMap = cmHisData.get(cmcId);
        } else {
            cmMap = Collections.synchronizedMap(new HashMap<>());
            cmHisData.put(cmcId,cmMap);
        }
        if (cmMap.containsKey(cmMac)) {
            re = cmMap.get(cmMac);
        } else {
            re = loadCmDataList(cmcId,cmMac);
            cmMap.put(cmMac,re);
        }
        return re;
    }

    public void delCmHisData(Long cmcId, String cmMac) {
        if (cmHisData.containsKey(cmcId)) {
            Map<String,List<PnmpCmData>> cmMap = cmHisData.get(cmcId);
            if (cmMap.containsKey(cmMac)) {
                cmMap.remove(cmMac);
            }
        }
    }
    public void modifyCmHisData(Long cmcId, String cmMac,List<PnmpCmData> cmDataList) {
        Map<String,List<PnmpCmData>> cmMap = null;
        if (cmHisData.containsKey(cmcId)) {
            cmMap = cmHisData.get(cmcId);
        } else {
            cmMap = Collections.synchronizedMap(new HashMap<>());
            cmHisData.put(cmcId,cmMap);
        }
        cmMap.put(cmMac,cmDataList);
    }
}