/***********************************************************************
 * $Id: CmHistoryFacadeImpl.java,v1.0 2015年4月11日 下午4:41:03 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmhistory.engine;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cm.cmhistory.engine.dao.CmHistoryEngineDao;
import com.topvision.ems.cm.cmhistory.engine.domain.CmHistory;
import com.topvision.ems.cm.cmhistory.facade.CmHistoryFacade;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsEngineService;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.framework.annotation.Engine;

/**
 * @author YangYi
 * @created @2015年4月11日-下午4:41:03
 * 
 */
@Engine("cmHistoryFacade")
public class CmHistoryFacadeImpl extends BaseEngine implements CmHistoryFacade, CmPollStatisticsPush {
    private LinkedBlockingQueue<CmPollResult> cmQueue = new LinkedBlockingQueue<CmPollResult>();;
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    @Autowired
    private CmPollStatisticsEngineService cmPollStatisticsEngineService;
    private CmHistoryEngineDao cmHistoryEngineDao;
    private final static DecimalFormat df = new DecimalFormat("0.0");

    @Override
    public void initialize() {
        cmPollStatisticsEngineService.registCmPollStatisticsPush(this);
    }

    @Override
    public void connected() {
        super.connected();
        cmHistoryEngineDao = engineDaoFactory.getEngineDao(CmHistoryEngineDao.class);
    }

    /**
     * 保存CmHistory进入数据库
     * 
     * @param CmHistory
     */
    private void saveCmHisotry(CmPollResult c) {
        CmHistory cmHistory = new CmHistory();
        cmHistory.setCmId(c.getCmId());
        Long time = c.getCollectTime() != null ? c.getCollectTime() : System.currentTimeMillis();
        cmHistory.setCollectTime(new Timestamp(time));
        cmHistory.setStatusValue(c.getStatusValue());
        cmHistory.setCheckStatus(c.getCheckStatus());
        cmHistory.setUpChannelId(c.getUpChIds());
        cmHistory.setDownChannelId(c.getDownChIds().length() == 0 ? "-" : c.getDownChIds());
        cmHistory.setUpChannelFreq(this.divide(c.getUpChFreqs(), 1000000));
        cmHistory.setDownChannelFreq(this.divide(c.getDownChFreqs(), 1000000));
        cmHistory.setUpRecvPower(this.divide(c.getUpChRxPowers(), 10));
        cmHistory.setUpSnr(this.divide(c.getUpChSnrs(), 10));
        cmHistory.setDownSnr(this.divide(c.getDownChSnrs(), 10));
        cmHistory.setUpSendPower(this.divide(c.getUpChTxPowers(), 10));
        cmHistory.setDownRecvPower(this.divide(c.getDownChRxPowers(), 10));
        try {
            cmHistoryEngineDao.insertCmHistory(cmHistory);
        } catch (Exception e) {
            logger.debug("",e);
        }
        //插入指定CM列表的最新数据
        try {
            cmHistoryEngineDao.insertSpecifiedCmListLast(cmHistory);
        } catch (Exception e) {
            logger.debug("",e);
        }
    }

    /**
     * 将str中每一项除以divider
     * 
     * @param str
     *            x1,x2,x3,x4
     * @param divider
     *            d
     * @return x1/d,x2/d,x3/d,x4/d
     */
    private String divide(String str, int divider) {
        String r = "";
        if (str != null && str.length() > 0) {
            String[] s = str.split(",");
            for (int i = 0; i < s.length; i++) {
                if (!CmPollResult.NULL.equals(s[i])) {
                    double d = Double.valueOf(s[i]) / divider;
                    r += df.format(d);
                } else {
                    r += "-";
                }
                r += ",";
            }
            r = r.substring(0, r.length() - 1);
        }else{
            r = "-";
        }
        return r;
    }

    @Override
    public void completeRoundStatistics(Long time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CmPollResult cm = null;
                while (!cmQueue.isEmpty()) {
                    try {
                        cm = cmQueue.take();
                        saveCmHisotry(cm);
                    } catch (Exception e) {
                        // 处理CM元素出错，继续处理下一个数据
                        logger.debug("cmHisotry save failed cmId = " + cm.getCmId(), e.getMessage());
                    }
                }
            }
        }).start();
    }

    @Override
    public void startRoundStatistics(Long time) {
    }

    @Override
    public void pushResult(long time, List<CmPollResult> cmPollResults) {
        cmQueue.addAll(cmPollResults);
    }

    @Override
    public String moduleName() {
        return "CmHistory";
    }

}
