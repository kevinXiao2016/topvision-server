/***********************************************************************
 * $Id: CmSignalEngineImpl.java,v1.0 2015-4-11 上午10:29:58 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmsignal.engine;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsEngineService;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.cm.cmsignal.domain.Cm3Signal;
import com.topvision.ems.cm.cmsignal.domain.CmSignal;
import com.topvision.ems.cm.cmsignal.engine.dao.CmSignalEngineDao;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.framework.annotation.Engine;

/**
 * @author fanzidong
 * @created @2015-4-11-上午10:29:58
 * 
 */
@Engine("cmSignalEngine")
public class CmSignalEngineImpl extends BaseEngine implements CmPollStatisticsPush {

    @Autowired
    private CmPollStatisticsEngineService cmPollStatisticsEngineService;
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    private CmSignalEngineDao cmSignalEngineDao;

    // engine端接收的CM轮询数据
    private LinkedBlockingQueue<CmPollResult> cmQueue = null;
    // CM信号质量处理线程
    private Thread caculateThread = null;
    // 本轮开启时间
    private Timestamp currentTime;

    @Override
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} initialize invoked.", getClass());
        }
        cmPollStatisticsEngineService.registCmPollStatisticsPush(this);
        cmQueue = new LinkedBlockingQueue<CmPollResult>();
        currentTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public void destroy() {
        cmQueue.clear();
    }

    @Override
    public void connected() {
        super.connected();
        if (cmSignalEngineDao == null) {
            try {
                cmSignalEngineDao = engineDaoFactory.getEngineDao(CmSignalEngineDao.class);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("cannot get CmSignalEngineDao.");
                }
            }
        }
        // 如果没有开启处理线程，则开启
        if (caculateThread == null) {
            startCacuThread();
        }
    }

    @Override
    public void completeRoundStatistics(Long time) {
        // 完成一轮采集，存入数据
    }

    @Override
    public void startRoundStatistics(Long time) {
        // 开启一轮新的采集处理，初始化数据
        cmQueue.clear();
        currentTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public void pushResult(long time, List<CmPollResult> cmPollResults) {
        for (Iterator<CmPollResult> iterator = cmPollResults.iterator(); iterator.hasNext();) {
            CmPollResult cmPollResult = iterator.next();
            if (!CmAttribute.isCmOnline(cmPollResult.getStatusValue())) {
                iterator.remove();
            }
        }
        // 接收到engine端的CM轮询数据，进行保存
        cmQueue.addAll(cmPollResults);
    }

    @Override
    public String moduleName() {
        return "CmSignalEngineImpl";
    }

    /**
     * 开启线程处理CM数据
     */
    private void startCacuThread() {
        caculateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                CmPollResult cm;
                while (cmQueue != null) {
                    try {
                        cm = cmQueue.take();
                        // 整理数据，整理成离散度需要数据
                        // modify by fanzidong,需要支持3.0CM多信道存储
                        arrangeData(cm);
                    } catch (Exception e) {
                        // 处理CM元素出错，继续处理下一个数据
                        logger.debug("" + e.getMessage());
                    }
                }

            }
        });
        caculateThread.start();
    }

    /**
     * 整理原始数据，输出离散度结构数据,支持3.0CM多信道
     * 
     * @param cm
     * @return
     */
    private void arrangeData(CmPollResult cm) {
        if (logger.isTraceEnabled()) {
            logger.trace("{}",cm);
        }
        String[] upChIds = cm.getUpChIds().split(","); // 上行信道
        String[] upChSnrs = cm.getUpChSnrs().split(","); // 上行信道SNR
        String[] upChTxPowers = cm.getUpChTxPowers().split(","); // 上行信道电平
        String[] upChFreqs = cm.getUpChFreqs().split(","); // 上行信道频率

        String[] downChIds = cm.getDownChIds().split(",");// 下行信道
        String[] downChannelSnrs = cm.getDownChSnrs().split(",");// 下行信道SNR
        String[] downChRxPowers = cm.getDownChRxPowers().split(",");// 下行信道电平
        String[] downChFreqs = cm.getDownChFreqs().split(",");// 下行信道频率

        // 信道与信号质量之间的数组长度一定是一致的，这是CmPollResult保证的

        // 封装3.0信号质量对象
        List<Cm3Signal> cm3SignalList = new ArrayList<Cm3Signal>();
        Cm3Signal upCm3Singal;
        for (int i = 0; i < upChIds.length; i++) {
            try {
                upCm3Singal = new Cm3Signal(cm.getCmId(), Long.valueOf(upChIds[i]), Cm3Signal.CHANNEL_TYPE_UP);
                upCm3Singal.setCollectTime(currentTime);
                if (!CmPollResult.NULL.equals(upChSnrs[i])) {
                    upCm3Singal.setUpChannelSnr(String.valueOf(Double.valueOf(upChSnrs[i]) / 10));
                }
                if (!CmPollResult.NULL.equals(upChTxPowers[i])) {
                    upCm3Singal.setUpChannelTx(String.valueOf(Double.valueOf(upChTxPowers[i]) / 10));
                }
                if (!CmPollResult.NULL.equals(upChFreqs[i])) {
                    upCm3Singal.setUpChannelFrequency(String.valueOf(Double.valueOf(upChFreqs[i]) / 1000000));
                }
                cm3SignalList.add(upCm3Singal);
            } catch (Exception e) {
                logger.error(String.format("convert to upCm3Singal error: cmId: %s, channelId:%s", cm.getCmId(),
                        upChIds[i]));
            }
        }

        Cm3Signal downCm3Singal;
        for (int i = 0; i < downChIds.length; i++) {
            try {
                downCm3Singal = new Cm3Signal(cm.getCmId(), Long.valueOf(downChIds[i]), Cm3Signal.CHANNEL_TYPE_DOWN);
                downCm3Singal.setCollectTime(currentTime);
                if (!CmPollResult.NULL.equals(downChannelSnrs[i])) {
                    downCm3Singal.setDownChannelSnr(String.valueOf(Double.valueOf(downChannelSnrs[i]) / 10));
                }
                if (!CmPollResult.NULL.equals(downChRxPowers[i])) {
                    downCm3Singal.setDownChannelTx(String.valueOf(Double.valueOf(downChRxPowers[i]) / 10));
                }
                if (!CmPollResult.NULL.equals(downChFreqs[i])) {
                    downCm3Singal.setDownChannelFrequency(String.valueOf(Double.valueOf(downChFreqs[i]) / 1000000));
                }
                cm3SignalList.add(downCm3Singal);
            } catch (Exception e) {
                logger.error(String.format("convert to upCm3Singal error: cmId: %s, channelId:%s", cm.getCmId(),
                        downChIds[i]));
            }
        }

        // 封装2.0信号质量对象
        try {
            CmSignal cmSignal = new CmSignal();
            cmSignal.setCmId(cm.getCmId());
            cmSignal.setCollectTime(currentTime);
            if (!CmPollResult.NULL.equals(upChSnrs[0])) {
                cmSignal.setUpChannelSnr(Double.valueOf(upChSnrs[0]) / 10);
            }
            if (!CmPollResult.NULL.equals(upChTxPowers[0])) {
                cmSignal.setUpChannelTx(Double.valueOf(upChTxPowers[0]) / 10);
            }
            if (!CmPollResult.NULL.equals(upChFreqs[0])) {
                cmSignal.setUpChannelFrequency(Double.valueOf(upChFreqs[0]) / 1000000);
            }
            if (!CmPollResult.NULL.equals(downChannelSnrs[0])) {
                cmSignal.setDownChannelSnr(Double.valueOf(downChannelSnrs[0]) / 10);
            }
            if (!CmPollResult.NULL.equals(downChRxPowers[0])) {
                cmSignal.setDownChannelTx(Double.valueOf(downChRxPowers[0]) / 10);
            }
            if (!CmPollResult.NULL.equals(downChFreqs[0])) {
                cmSignal.setDownChannelFrequency(Double.valueOf(downChFreqs[0]) / 1000000);
            }

            // 入库
            if (cmSignalEngineDao != null) {
                try {
                    cmSignalEngineDao.insertCmSignal(cmSignal);
                } catch (Exception sqlError) {
                    logger.error(String.format("insertCmSignal error: cmId: %s", cm.getCmId()));
                }
                try {
                    cmSignalEngineDao.insertCm3Signal(cm3SignalList);
                } catch (Exception sqlError) {
                    logger.error(String.format("insertCm3Signal error: cmId: %s", cm.getCmId()));
                }

            }
        } catch (Exception e) {
            logger.error(String.format("convert to cmSignal error: cmId: %s", cm.getCmId()));
        }
    }

}
