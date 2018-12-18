/***********************************************************************
 * $Id: DispersionFacadeImpl.java,v1.0 2015-3-25 上午11:53:12 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsEngineService;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.cm.dispersion.engine.dao.DispersionEngineDao;
import com.topvision.ems.cm.dispersion.engine.domain.DispersionRawData;
import com.topvision.ems.cm.dispersion.engine.domain.OpticalNodeRelation;
import com.topvision.ems.cm.dispersion.facade.DispersionFacade;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.framework.annotation.Engine;

/**
 * @author fanzidong
 * @created @2015-3-25-上午11:53:12
 * 
 */
@Engine("dispersionFacade")
public class DispersionFacadeImpl extends BaseEngine implements DispersionFacade, CmPollStatisticsPush {

    @Autowired
    private CmPollStatisticsEngineService cmPollStatisticsEngineService;
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    // engine端的离散度分布情况
    private Map<Long, Map<String, Map<Integer, Integer>>> distMap = null;

    // engine端接收的CM轮询数据
    private LinkedBlockingQueue<CmPollResult> cmQueue = null;

    // 光节点关系数据
    private List<OpticalNodeRelation> opticalNodeRelations = null;

    // 离散度分布情况处理线程
    private Thread caculateThread = null;

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
        distMap = new HashMap<Long, Map<String, Map<Integer, Integer>>>();
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
        opticalNodeRelations = null;
        caculateThread = null;
    }

    @Override
    public void connected() {
        super.connected();
        // 获取光节点对应关系表
        DispersionEngineDao dispersionEngineDao = engineDaoFactory.getEngineDao(DispersionEngineDao.class);
        if (dispersionEngineDao == null) {
            // 未获取到dao,用默认的cmtsId=opticalNodeId来保证，毕竟目前只有小C
            if (logger.isDebugEnabled()) {
                logger.debug("cannot get DispersionEngineDao.");
            }
        } else {
            try {
                List<OpticalNodeRelation> relations = dispersionEngineDao.selectOpticalNodeRelation();
                if (relations != null) {
                    opticalNodeRelations = relations;
                }
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("cannot get OpticalNodeRelation: " + e.getMessage());
                }
            }

        }
        // 如果没有开启处理线程，则开启
        if (caculateThread == null) {
            startCacuThread();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cm.dispersion.facade.DispersionFacade#getDistributionData()
     */
    @Override
    public Map<Long, Map<String, Map<Integer, Integer>>> getDistributionData() {
        // 如果还有未处理的数据，等待处理完成
        while (!cmQueue.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        logger.debug("push dispersion data to server.");
        return distMap;
    }

    @Override
    public void completeRoundStatistics(Long time) {
        // 完成一轮处理，暂时无需做额外操作
    }

    @Override
    public void startRoundStatistics(Long time) {
        // 开启一轮新的采集处理，初始化数据
        distMap.clear();
        cmQueue.clear();
    }

    @Override
    public void pushResult(long time, List<CmPollResult> cmPollResults) {
        logger.trace("dispersion receive cmPollResults size----" + cmPollResults.size());
        for (Iterator<CmPollResult> iterator = cmPollResults.iterator();iterator.hasNext();) {
            CmPollResult cmPollResult = iterator.next();
            if (!CmAttribute.isCmOnline(cmPollResult.getStatusValue())) {
                iterator.remove();
            }
        }
        // 接收到engine端的CM轮询数据，进行保存
        logger.trace("dispersion receive cmPollResults online size----" + cmPollResults.size());
        logger.debug("dispersion receive data.");
        cmQueue.addAll(cmPollResults);
    }

    @Override
    public String moduleName() {
        return "DispersionFacadeImpl";
    }

    /**
     * 开启线程处理CM数据
     */
    private void startCacuThread() {
        caculateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                CmPollResult cm;
                DispersionRawData arrangedData;
                while (cmQueue != null) {
                    try {
                        cm = cmQueue.take();
                        // 整理数据，整理成离散度需要数据
                        arrangedData = arrangeData(cm);
                        // 更新分布情况
                        if (arrangedData != null) {
                            updateDistMap(arrangedData);
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
     * 整理原始数据，输出离散度结构数据
     * 
     * @param cm
     * @return
     */
    private DispersionRawData arrangeData(CmPollResult cm) {
        if (cm.getUpChIds() == null || cm.getUpChIds().equals("")) {
            // 未采集到有效数据，丢弃该数据
            return null;
        }

        DispersionRawData data = new DispersionRawData();
        data.setCmId(cm.getCmId());
        data.setCmtsId(cm.getCmcId());
        String[] upChls = cm.getUpChIds().split(",");
        String[] upSnrs = cm.getUpChSnrs().split(",");
        String[] upPowers = cm.getUpChTxPowers().split(",");
        // SNR取多信道最小值，电平取平均值
        Double minUpSnr = null;
        Double totalUpPower = 0d;
        int upPowerCount = 0;
        for (int i = 0, len = upChls.length; i < len; i++) {
            if (!upSnrs[i].equals(CmPollResult.NULL) && (minUpSnr == null || minUpSnr > Double.valueOf(upSnrs[i]))) {
                minUpSnr = Double.valueOf(upSnrs[i]);
            }
            if (!upPowers[i].equals(CmPollResult.NULL)) {
                totalUpPower += Double.valueOf(upPowers[i]);
                upPowerCount++;
            }
        }
        Double avgUpPower = 0d;
        if (upPowerCount != 0d) {
            avgUpPower = totalUpPower / upPowerCount;
        }
        data.setChannelIndex(Long.valueOf(upChls[0]));
        data.setUpSnr(minUpSnr / 10);
        data.setUpPower(avgUpPower / 10);

        return data;
    }

    /**
     * 更新分布情况
     * 
     * @param arrangedData
     */
    private void updateDistMap(DispersionRawData arrangedData) {
        // 找到对应的光节点
        Long opticalNodeId = getOpticalNodeId(arrangedData.getCmtsId(), arrangedData.getChannelIndex());
        if (opticalNodeId == null) {
            return;
        }
        // 找到对应的光节点信息
        Map<String, Map<Integer, Integer>> curOptical;
        Map<Integer, Integer> curUpSnr;
        Map<Integer, Integer> curUpPower;
        Integer curSnr;
        Integer curPower;
        if (distMap.containsKey(opticalNodeId)) {
            curOptical = distMap.get(opticalNodeId);
        } else {
            curOptical = new HashMap<String, Map<Integer, Integer>>();
            distMap.put(opticalNodeId, curOptical);
        }
        // 找到对应光节点的SNR分布情况和电平分布情况
        if (curOptical.containsKey(DispersionRawData.UP_SNR)) {
            curUpSnr = curOptical.get(DispersionRawData.UP_SNR);
        } else {
            curUpSnr = new HashMap<Integer, Integer>();
            curOptical.put(DispersionRawData.UP_SNR, curUpSnr);
        }
        if (curOptical.containsKey(DispersionRawData.UP_POWER)) {
            curUpPower = curOptical.get(DispersionRawData.UP_POWER);
        } else {
            curUpPower = new HashMap<Integer, Integer>();
            curOptical.put(DispersionRawData.UP_POWER, curUpPower);
        }
        // 更新SNR分布情况
        curSnr = arrangedData.getUpSnr().intValue();
        if (curUpSnr.containsKey(curSnr)) {
            curUpSnr.put(curSnr, curUpSnr.get(curSnr) + 1);
        } else {
            curUpSnr.put(curSnr, 1);
        }
        // 更新电平分布情况
        curPower = arrangedData.getUpPower().intValue();
        if (curUpPower.containsKey(curPower)) {
            curUpPower.put(curPower, curUpPower.get(curPower) + 1);
        } else {
            curUpPower.put(curPower, 1);
        }
    }

    /**
     * 根据cmtsId和channelIndex找到对应的光节点ID，如果没有数据，则用cmtsId取代
     * 
     * @param cmtsId
     * @param channelIndex
     * @return
     */
    private Long getOpticalNodeId(Long cmtsId, Long channelIndex) {
        if (opticalNodeRelations == null || cmtsId == null || channelIndex == null) {
            return cmtsId;
        }
        for (OpticalNodeRelation opticalNodeRelation : opticalNodeRelations) {
            if (cmtsId.equals(opticalNodeRelation.getCmtsId())
                    && channelIndex.equals(opticalNodeRelation.getChannelIndex())) {
                return opticalNodeRelation.getOpticalNodeId();
            }
        }
        return cmtsId;
    }
}
