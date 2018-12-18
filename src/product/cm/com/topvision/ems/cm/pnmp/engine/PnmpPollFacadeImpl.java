/***********************************************************************
 * $Id: PnmpPollFacadeImpl.java,v1.0 2011-7-1 下午02:56:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.engine;

import com.topvision.ems.cm.cmpoll.facade.domain.Complex;
import com.topvision.ems.cm.pnmp.facade.domain.CmtsCm;
import com.topvision.ems.cm.pnmp.domain.PnmpPollCollectParam;
import com.topvision.ems.cm.pnmp.engine.dao.PnmpPollHsqlDao;
import com.topvision.ems.cm.pnmp.engine.service.PnmpPollStatisticsEngineService;
import com.topvision.ems.cm.pnmp.facade.PnmpPollFacade;
import com.topvision.ems.cm.pnmp.facade.domain.*;
import com.topvision.ems.cm.pnmp.facade.utils.PnmpUtils;
import com.topvision.ems.cm.pnmp.facade.utils.PreEqualizationParam;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.cm.pnmp.facade.callback.PnmpPollCallback;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.upgrade.telnet.CcmtsTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Victor
 * @created @2011-7-1-下午02:56:35
 * 
 */
@Facade("pnmpPollFacade")
public class PnmpPollFacadeImpl extends BaseEngine implements PnmpPollFacade, BeanFactoryAware {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;
    @Autowired
    private PnmpPollHsqlDao pnmpPollHsqlDao;
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private PnmpPollStatisticsEngineService pnmpPollStatisticsEngineService;
    @Autowired
    private PingExecutorService pingExecutorService;

    private PnmpPollCallback pnmpPollCallback;

    private BeanFactory beanFactory;
    private Integer sendPnmpCount;
    private SnmpParam cmCollectSnmpParam;
    private boolean isInitAug = false;

    /**
     * 接收采集调度任务
     * 
     * @param cmPollTask
     *            performanceResult
     * @return 可以接收 true 不可接收 false
     */
    @Override
    public boolean appendTesk(Long time, List<PnmpPollTask> cmPollTask) {
        if (getId() == null && !isInitAug) {
            return false;
        }
        for (PnmpPollTask pollTask : cmPollTask) {
            if (pollTask instanceof PnmpPollLowSpeedEndTask
                    || pollTask instanceof PnmpPollMiddleSpeedEndTask
                    || pollTask instanceof PnmpPollHighSpeedEndTask) {
                continue;
            }
            boolean full = false;
            do {
                try {
                    PnmpPollThreadPool pnmpPollThreadPool = (PnmpPollThreadPool) beanFactory.getBean("pnmpPollThreadPool");
                    pnmpPollThreadPool.setTime(time);
                    pnmpPollThreadPool.setPnmpPollTask(pollTask);
                    pnmpPollThreadPool.setEngineId(getId());
                    pnmpPollThreadPool.setSnmpParam(cmCollectSnmpParam);
                    pnmpPollThreadPool.setPnmpPollCallback(pnmpPollCallback);
                    threadPoolExecutor.execute(pnmpPollThreadPool);
                    full = false;
                } catch (Exception e) {
                    logger.debug("PnmpPollFacadeImpl.appendTesk.Exception", e);
                    full = true;
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e1) {

                    }
                }
            } while (full);
        }
        return true;
    }

    @Override
    public void appendEndTask(Long time, PnmpPollTask cmPollTask) {
        roundFinished(time,cmPollTask);
    }

    @Override
    public List<CmtsCm> getCmtsCmList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam,CmtsCm.class);
    }

    @Override
    public void setCollectSnmpParam(SnmpParam snmpParam) {
        cmCollectSnmpParam = snmpParam;
    }

    @Override
    public PnmpCmData realPnmp(Long cmtsId,SnmpParam snmpParam, SnmpParam cmSnmpParam, String cmMac) {
        CMMacToIndex cmMacToIndex = new CMMacToIndex();
        cmMacToIndex.setMac(new PhysAddress(cmMac));
        try {
            cmMacToIndex = snmpExecutorService.getTableLine(
                    snmpParam, cmMacToIndex);
        } catch (Exception e) {
            logger.debug("", e);
        }
        CmtsCm cmtsCm = new CmtsCm();
        cmtsCm.setStatusIndex(cmMacToIndex.getCmIndex());
        if (cmMacToIndex != null && cmMacToIndex.getCmIndex() != null) {
            try {
                cmtsCm = snmpExecutorService.getTableLine(
                        snmpParam, cmtsCm);
            } catch (Exception e) {
                logger.debug("",e);
            }
            if (cmtsCm != null) {
                PnmpCmData pnmpCmData = new PnmpCmData();
                if (cmtsCm.getStatusSignalNoise() != null) {
                    pnmpCmData.setUpSnr(cmtsCm.getStatusSignalNoise() / 10D);
                }
                String upSignalNoiseStr = null;
                try {
                    upSignalNoiseStr = snmpExecutorService.get(snmpParam,"1.3.6.1.2.1.10.127.1.3.3.1.13." + cmMacToIndex.getCmIndex());
                } catch (Exception e) {
                    logger.debug("",e);
                }
                if (upSignalNoiseStr != null) {
                    Double upSignalNoise = Double.parseDouble(upSignalNoiseStr) / 10D;
                    pnmpCmData.setUpSnr(upSignalNoise);
                }
                pnmpCmData.setStatusValue(cmtsCm.getStatusValue().shortValue());
                if (CmAttribute.isCmOnline(cmtsCm.getStatusValue())) {
                    String cmIp = cmtsCm.getStatusIpAddress();
                    cmSnmpParam.setIpAddress(cmIp);
                    String upchannelIdStr = null;
                    pnmpCmData.setCmMac(cmtsCm.getStatusMacAddress());
                    try {
                        upchannelIdStr = snmpExecutorService.getNext(cmSnmpParam,"1.3.6.1.2.1.10.127.1.1.2.1.1");
                    } catch (Exception e) {
                        logger.debug("",e);
                    }
                    if (upchannelIdStr != null) {
                        Integer upchannelId = Integer.parseInt(upchannelIdStr);
                        pnmpCmData.setUpChannelId(upchannelId);
                    }
                    String upchannelFreqStr = null;
                    try {
                        upchannelFreqStr = snmpExecutorService.getNext(cmSnmpParam,"1.3.6.1.2.1.10.127.1.1.2.1.2");
                    } catch (Exception e) {
                        logger.debug("",e);
                    }
                    if (upchannelFreqStr != null) {
                        Long upchannelFreq = Long.parseLong(upchannelFreqStr);
                        pnmpCmData.setUpChannelFreq(upchannelFreq);
                    }
                    String upchannelWidthStr = null;
                    try {
                        upchannelWidthStr = snmpExecutorService.getNext(cmSnmpParam,"1.3.6.1.2.1.10.127.1.1.2.1.3");
                    } catch (Exception e) {
                        logger.debug("",e);
                    }
                    if (upchannelWidthStr != null) {
                        Long upchannelWidth = Long.parseLong(upchannelWidthStr);
                        pnmpCmData.setUpChannelWidth(upchannelWidth);
                    }
                    String upTxPowerStr = null;
                    try {
                        upTxPowerStr = snmpExecutorService.getNext(cmSnmpParam,"1.3.6.1.2.1.10.127.1.2.2.1.3");
                    } catch (Exception e) {
                        logger.debug("",e);
                    }
                    if (upTxPowerStr != null) {
                        Double upTxPower = Double.parseDouble(upTxPowerStr) / 10D;
                        pnmpCmData.setUpTxPower(upTxPower);
                    }
                    String equalizationData = null;
                    try {
                        equalizationData = snmpExecutorService.getNext(cmSnmpParam,"1.3.6.1.2.1.10.127.1.2.2.1.17");
                    } catch (Exception e) {
                        logger.debug("",e);
                    }
                    if (equalizationData != null) {
                        PreEqualizationParam pField = new PreEqualizationParam();
                        pField.parse(equalizationData);
                        pnmpCmData.setPreEqualizationState(!pField.isEmpty());
                        if (!pField.isEmpty()) {
                            Complex[] taps = pField.toArray();
                            Double mtr = PnmpUtils.mtr(taps);
                            pnmpCmData.setMtr(mtr);
                            if (logger.isTraceEnabled()) {
                                logger.trace("taps----------------------" + PnmpUtils.arrayToString(taps));
                            }
                            Double[] tapAmplitudes = PnmpUtils.amplitudes(taps);
                            pnmpCmData.setTapCoefficient(PnmpUtils.arrayToString(tapAmplitudes));
                            Double[] freqResponse = PnmpUtils.freqResponse(taps);
                            pnmpCmData.setSpectrumResponse(PnmpUtils.arrayToString(freqResponse));

                            List<PnmpCmData> cmDataList = pnmpPollStatisticsEngineService.getPnmpCmDataList(cmtsId, cmMac);

                            Double mtrVariance = null;
                            if (mtr != null) {
                                mtrVariance = PnmpUtils.mtrVariance(mtr,cmDataList);
                            }
                            Double upSnrVariance = null;
                            if (pnmpCmData.getUpSnr() != null) {
                                upSnrVariance = PnmpUtils.upSnrVariance(pnmpCmData.getUpSnr(), cmDataList);
                            }
                            pnmpCmData.setMtrVariance(mtrVariance);
                            pnmpCmData.setUpSnrVariance(upSnrVariance);
                            Double mtrToUpSnrSimilarity = PnmpUtils.mtrToUpSnrSimilarity(cmDataList);
                            pnmpCmData.setMtrToUpSnrSimilarity(mtrToUpSnrSimilarity);
                        }
                    }
                }
                return pnmpCmData;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void roundStart(Long time, PnmpPollTask pnmpPollTask) {
        pnmpPollHsqlDao.createRoundTable(time,pnmpPollTask);
    }

    @Override
    public void roundFinished(final Long time, PnmpPollTask pnmpPollTask) {
        logger.debug("PnmpPollFacadeImpl.roundFinished");
        final Integer engineId = getId();
        new Thread() {
            public void run() {
                int activeCount = threadPoolExecutor.getActiveCount();
                while (activeCount != 0) {
                    logger.debug("PnmpPollFacadeImpl.roundFinished activeCount[" + activeCount + "]");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                    activeCount = threadPoolExecutor.getActiveCount();
                }
                pnmpPollStatisticsEngineService.startRoundStatistics(time,pnmpPollTask);
                PnmpPollThreadPool.resetNextResultId();
                int startAt = 1;
                List<PnmpPollResult> pnmpPollResults;
                while (true) {
                    pnmpPollResults = pnmpPollHsqlDao.readLocalRecords(time, startAt, sendPnmpCount,pnmpPollTask);
                    logger.trace("send pnmpPollResults -- " + "size---" + pnmpPollResults.size() + "------" + pnmpPollResults.toString());
                    startAt = startAt + pnmpPollResults.size();
                    if (!pnmpPollResults.isEmpty()) {
                    	pnmpPollStatisticsEngineService.sendResult(time, pnmpPollResults, pnmpPollTask);
                    } else {
                        while (!pnmpPollStatisticsEngineService.isCalculationFinished()) {
                            logger.trace("PnmpPollFacadeImpl.calculation not finished");
                            try {
                                sleep(100L);
                            } catch (InterruptedException e) {
                            }
                        }
                        pnmpPollStatisticsEngineService.completeRoundStatistics(time,pnmpPollTask);
                        pnmpPollCallback.completeRoundStatistics(engineId, time, pnmpPollTask);
                        logger.debug("PnmpPollFacadeImpl.calculationFinished");
                        break;
                    }
                }
                logger.debug("PnmpPollFacadeImpl.roundFinished end");
            }
        }.start();
    }

    @Override
    public void heartBeat() {

    }

    @Override
    public void initRunAug(PnmpPollCollectParam pnmpPollCollectParam) {
        isInitAug = true;
        this.sendPnmpCount = pnmpPollCollectParam.getSendCmCount();
        this.cmCollectSnmpParam = pnmpPollCollectParam.getSnmpParam();
        int maxPoolsize = pnmpPollCollectParam.getMaxPoolSize();
        threadPoolExecutor = new ThreadPoolExecutor(maxPoolsize, maxPoolsize, maxPoolsize, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(maxPoolsize * 10));

        pnmpPollCallback = getCallback(PnmpPollCallback.class);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void connected() {
        super.connected();
    }

    @Override
    public void disconnected() {
        super.disconnected();
        isInitAug = false;
        setId(null);
    }


}
