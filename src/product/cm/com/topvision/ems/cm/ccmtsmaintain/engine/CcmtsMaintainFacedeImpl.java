/***********************************************************************
 * $Id: CcmtsMaintainEngineImpl.java,v1.0 2015-5-26 下午6:49:17 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cm.ccmtsmaintain.engine.dao.CcmtsMaintainEngineDao;
import com.topvision.ems.cm.ccmtsmaintain.engine.domain.CcmtsChannel;
import com.topvision.ems.cm.ccmtsmaintain.facade.CcmtsMaintainFacade;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsEngineService;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.framework.annotation.Engine;

/**
 * @author fanzidong
 * @created @2015-5-26-下午6:49:17
 * 
 */
@Engine("ccmtsMaintainFacade")
public class CcmtsMaintainFacedeImpl extends BaseEngine implements CcmtsMaintainFacade, CmPollStatisticsPush {

    @Autowired
    private CmPollStatisticsEngineService cmPollStatisticsEngineService;
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    // engine端的分布情况
    private Map<Long, Map<Integer, Map<String, Double>>> distMap = null;

    // engine端接收的CM轮询数据
    private LinkedBlockingQueue<CmPollResult> cmQueue = null;

    // 处理线程
    private Thread caculateThread = null;

    private CcmtsMaintainEngineDao ccmtsMaintainEngineDao;

    // CCMTS信道频宽map
    private Map<Long, Map<Integer, Long>> chlWidthMap = null;

    public static final String AVGSNR = "avgSnr";
    public static final String LOW_SNR_CMNUM = "lowSnrCmNum";
    public static final String BIG_POWER_CMNUM = "bigPowerCmNum";
    public static final String CHL_WIDTH = "channelWidth";
    public static final String CM_TOTAL = "cmTotal";

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.engine.BaseEngine#initialize()
     */
    @Override
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} initialize invoked.", getClass());
        }
        cmPollStatisticsEngineService.registCmPollStatisticsPush(this);
        distMap = new HashMap<Long, Map<Integer, Map<String, Double>>>();
        cmQueue = new LinkedBlockingQueue<CmPollResult>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.engine.BaseEngine#destroy()
     */
    @Override
    public void destroy() {
        distMap.clear();
        cmQueue.clear();
        caculateThread = null;
    }

    @Override
    public void connected() {
        super.connected();
        // 获取CCMTS上行信道的频宽数据
        updateChlWidthMap();

        // 如果没有开启处理线程，则开启
        if (caculateThread == null) {
            startCacuThread();
        }
    }

    private void updateChlWidthMap() {
        if (ccmtsMaintainEngineDao == null) {
            ccmtsMaintainEngineDao = engineDaoFactory.getEngineDao(CcmtsMaintainEngineDao.class);
        }
        if (ccmtsMaintainEngineDao == null) {
            // 未获取到dao
            if (logger.isDebugEnabled()) {
                logger.debug("cannot get ccmtsMaintainEngineDao.");
            }
        } else {
            try {
                List<CcmtsChannel> ccmtsChannelList = ccmtsMaintainEngineDao.selectCcmtsChannels();
                if (ccmtsChannelList != null) {
                    // 转换成map
                    chlWidthMap = new HashMap<Long, Map<Integer, Long>>();
                    Map<Integer, Long> curCmc;
                    for (CcmtsChannel chl : ccmtsChannelList) {
                        if (!chlWidthMap.containsKey(chl.getCmcId())) {
                            chlWidthMap.put(chl.getCmcId(), new HashMap<Integer, Long>());
                        }
                        curCmc = chlWidthMap.get(chl.getCmcId());
                        curCmc.put(chl.getChannelId(), chl.getChannelWidth());
                    }
                }
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("cannot get ccmtsChannelList: " + e.getMessage());
                }
            }
        }
    }

    private Double getChannelWidth(Long cmcId, Integer channelId) {
        if (chlWidthMap.containsKey(cmcId)) {
            Map<Integer, Long> curCmc = chlWidthMap.get(cmcId);
            if (curCmc.containsKey(channelId)) {
                Long channelWidth = curCmc.get(channelId);
                if (channelWidth == null) {
                    return null;
                }
                return ((double) channelWidth) / 1000000;
            }
            return null;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush#startRoundStatistics(java
     * .lang.Long)
     */
    @Override
    public void startRoundStatistics(Long time) {
        // 开启一轮新的采集处理，初始化数据
        updateChlWidthMap();
        distMap.clear();
        cmQueue.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush#completeRoundStatistics(java
     * .lang.Long)
     */
    @Override
    public void completeRoundStatistics(Long time) {
        // 完成一轮处理，暂时无需做额外操作
    }

    @Override
    public Map<Long, Map<Integer, Map<String, Double>>> getDistributionData() {
        // 如果还有未处理的数据，等待处理完成
        while (!cmQueue.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        logger.debug("push ccmtsmaintain data to server.");
        return distMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush#pushResult(long,
     * java.util.List)
     */
    @Override
    public void pushResult(long time, List<CmPollResult> cmPollResults) {
        for (Iterator<CmPollResult> iterator = cmPollResults.iterator(); iterator.hasNext();) {
            CmPollResult cmPollResult = iterator.next();
            if (!CmAttribute.isCmOnline(cmPollResult.getStatusValue())) {
                iterator.remove();
            }
        }
        // 接收到engine端的CM轮询数据，进行保存
        logger.debug("ccmtsmaintain receive data.");
        cmQueue.addAll(cmPollResults);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush#moduleName()
     */
    @Override
    public String moduleName() {
        return "CcmtsMaintainEngineImpl";
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
                        // 更新分布情况
                        if (cm != null) {
                            updateDistMap(cm);
                        }
                    } catch (Exception e) {
                        // 处理CM元素出错，继续处理下一个数据
                        logger.debug("cacu cm error: " + e.getMessage());
                    }
                }

            }
        });
        caculateThread.start();
    }

    /**
     * 更新分布情况
     * 
     * @param cm
     */
    private void updateDistMap(CmPollResult cm) {
        // 找到对应的CC信息
        if (!distMap.containsKey(cm.getCmcId())) {
            distMap.put(cm.getCmcId(), new HashMap<Integer, Map<String, Double>>());
        }
        Map<Integer, Map<String, Double>> curCmc = distMap.get(cm.getCmcId());
        // 针对每一个信道，进行处理
        String chlStr = cm.getUpChIds();
        if (chlStr == null || "".equals(chlStr)) {
            return;
        }
        String[] chls = chlStr.split(",");
        String snrStr = cm.getUpChSnrs();
        String[] snrs = null;
        if (snrStr != null && !"".equals(snrStr)) {
            snrs = snrStr.split(",");
        }
        String sendPowerStr = cm.getUpChTxPowers();
        String[] snedPowers = null;
        if (sendPowerStr != null && !"".equals(sendPowerStr)) {
            snedPowers = sendPowerStr.split(",");
        }
        Integer chlId;
        Double upSnr;
        Double upSendPower;
        Map<String, Double> curChl;
        for (int i = 0; i < chls.length; i++) {
            chlId = null;
            upSnr = null;
            upSendPower = null;
            try {
                // 当前信道ID
                chlId = Integer.valueOf(chls[i]);
                // 当前信道的上行SNR值
                if (i < snrs.length && !CmPollResult.NULL.equals(snrs[i])) {
                    upSnr = Double.valueOf(snrs[i]) / 10;
                }
                // 当前信道的上行发送电平值
                if (i < snedPowers.length && !CmPollResult.NULL.equals(snedPowers[i])) {
                    upSendPower = Double.valueOf(snedPowers[i]) / 10;
                }
                // 找到对应的信道
                if (!curCmc.containsKey(chlId)) {
                    // 初始化当前信道的信息
                    curChl = new HashMap<String, Double>();
                    curChl.put(AVGSNR, 0d);
                    curChl.put(LOW_SNR_CMNUM, 0d);
                    curChl.put(BIG_POWER_CMNUM, 0d);
                    curChl.put(CHL_WIDTH, getChannelWidth(cm.getCmcId(), chlId));
                    curChl.put(CM_TOTAL, 0d);
                    curCmc.put(chlId, curChl);
                }
                curChl = curCmc.get(chlId);
                // 更新平均SNR
                if (upSnr != null) {
                    curChl.put(AVGSNR, (curChl.get(AVGSNR) * curChl.get(CM_TOTAL) + upSnr) / (curChl.get(CM_TOTAL) + 1));
                }
                // 更新SNR低于20dB的CM个数
                if (upSnr == null || upSnr < 20) {
                    curChl.put(LOW_SNR_CMNUM, curChl.get(LOW_SNR_CMNUM) + 1);
                }
                // 更新发送电平大于55dBmv的CM个数
                if (upSendPower != null && upSendPower >= 55) {
                    curChl.put(BIG_POWER_CMNUM, curChl.get(BIG_POWER_CMNUM) + 1);
                }
                // 更新CM总个数
                curChl.put(CM_TOTAL, curChl.get(CM_TOTAL) + 1);
            } catch (NumberFormatException e) {
                // 转换错误，跳过当前信道
                continue;
            }
        }
    }
}
