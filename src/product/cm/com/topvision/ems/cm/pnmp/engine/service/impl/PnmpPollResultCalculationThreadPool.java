package com.topvision.ems.cm.pnmp.engine.service.impl;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.topvision.ems.cm.pnmp.facade.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cm.cmpoll.facade.domain.Complex;
import com.topvision.ems.cm.pnmp.engine.service.PnmpPollStatisticsEngineService;
import com.topvision.ems.cm.pnmp.facade.utils.PnmpUtils;
import com.topvision.ems.cm.pnmp.facade.utils.PreEqualizationParam;
import com.topvision.framework.annotation.Engine;

@Engine
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PnmpPollResultCalculationThreadPool implements Runnable {
    private Logger logger = LoggerFactory.getLogger(PnmpPollResultCalculationThreadPool.class);
    private PnmpPollStatisticsEngineService pnmpPollStatisticsEngineService;
    private PnmpPollTask pnmpPollTask;
    private PnmpPollResult pnmpPollResult;


            @Override
    public void run() {
        if (logger.isDebugEnabled()) {
            logger.debug("PnmpPollResultCalculationThreadPool.run pnmpPollResult:" + pnmpPollResult);
        }

        try {
            PnmpCalculationResult pnmpCalculationResult = new PnmpCalculationResult();
            pnmpCalculationResult.setEntityId(pnmpPollResult.getEntityId());
            pnmpCalculationResult.setCmcId(pnmpPollResult.getCmcId());
            pnmpCalculationResult.setCmcIndex(pnmpPollResult.getCmcIndex());
            pnmpCalculationResult.setCmIndex(pnmpPollResult.getCmIndex());
            pnmpCalculationResult.setCmIp(pnmpPollResult.getCmIp());
            pnmpCalculationResult.setCmMac(pnmpPollResult.getCmMac());
            pnmpCalculationResult.setStatusValue(pnmpPollResult.getStatusValue());
            pnmpCalculationResult.setCheckStatus(pnmpPollResult.getCheckStatus());
            pnmpCalculationResult.setOrginalValue(pnmpPollResult.getEqualizationData());
            pnmpCalculationResult.setUpChannelId(pnmpPollResult.getUpChannelId());
            pnmpCalculationResult.setUpChannelFreq(pnmpPollResult.getUpChannelFreq());
            pnmpCalculationResult.setUpChannelWidth(pnmpPollResult.getUpChannelWidth());
            Integer upSnr = pnmpPollResult.getUpSnr();
            if (upSnr != null) {
                pnmpCalculationResult.setUpSnr(upSnr / 10D);
            }
            Integer upTxPower = pnmpPollResult.getUpTxPower();
            if (upTxPower != null) {
                pnmpCalculationResult.setUpTxPower(upTxPower / 10D);
            }
            Integer downSnr = pnmpPollResult.getDownSnr();
            if (downSnr != null) {
                pnmpCalculationResult.setDownSnr(downSnr / 10D);
            }
            Integer downRxPower = pnmpPollResult.getDownRxPower();
            if (downRxPower != null) {
                pnmpCalculationResult.setDownRxPower(downRxPower / 10D);
            }
            PreEqualizationParam pField = new PreEqualizationParam();
            pField.parse(pnmpPollResult.getEqualizationData());
            pnmpCalculationResult.setPreEqualizationState(!pField.isEmpty());
            if (!pField.isEmpty()) {
                Complex[] taps = pField.toArray();
                Double mte = PnmpUtils.mte(taps);
                pnmpCalculationResult.setMte(mte);
                Double preMTE = PnmpUtils.preMTE(taps);
                pnmpCalculationResult.setPreMte(preMTE);
                Double postMTE = PnmpUtils.postMTE(taps);
                pnmpCalculationResult.setPostMte(postMTE);
                Double tte = PnmpUtils.tte(taps);
                pnmpCalculationResult.setTte(tte);
                Double mtc = PnmpUtils.mtc(taps);
                pnmpCalculationResult.setMtc(mtc);
                Double mtr = PnmpUtils.mtr(taps);

                if (pnmpPollResult.getCmMac().equalsIgnoreCase(pnmpPollStatisticsEngineService.getDebugCmMac())) {
                    mtr = pnmpPollStatisticsEngineService.getDebugMtr();
                    logger.trace("Debug mode Cmmac[" + pnmpPollStatisticsEngineService.getDebugCmMac() + "] " + "mtr["
                            + pnmpPollStatisticsEngineService.getDebugMtr() + "]");
                }
                pnmpCalculationResult.setMtr(mtr);
                Double mrLevel = PnmpUtils.mrLevel(taps);
                pnmpCalculationResult.setMrLevel(mrLevel);
                Double nmtTER = PnmpUtils.nmtTER(taps);
                pnmpCalculationResult.setNmtter(nmtTER);
                Double preMTTER = PnmpUtils.preMTTER(taps);
                pnmpCalculationResult.setPreMtter(preMTTER);
                Double postMTTER = PnmpUtils.postMTTER(taps);
                pnmpCalculationResult.setPostMtter(postMTTER);
                Double ppesr = PnmpUtils.ppesr(taps);
                pnmpCalculationResult.setPpesr(ppesr);
                if (pnmpPollTask instanceof PnmpPollMiddleSpeedEndTask || pnmpPollTask instanceof PnmpPollHighSpeedEndTask) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("taps----------------------" + PnmpUtils.arrayToString(taps));
                    }
                    Double[] tapAmplitudes = PnmpUtils.amplitudes(taps);
                    pnmpCalculationResult.setTapCoefficient(PnmpUtils.arrayToString(tapAmplitudes));
                    Double[] freqResponse = PnmpUtils.freqResponse(taps);
                    pnmpCalculationResult.setSpectrumResponse(PnmpUtils.arrayToString(freqResponse));
                    Double tdr = PnmpUtils.tdr(taps, pnmpPollResult.getUpChannelWidth());
                    pnmpCalculationResult.setTdr(tdr);
                    List<PnmpCmData> cmDataList = pnmpPollStatisticsEngineService.getPnmpCmDataList(pnmpPollResult.getCmcId(), pnmpPollResult.getCmMac());
                    Double mtrVariance = PnmpUtils.mtrVariance(mtr,cmDataList);
                    Double upSnrVariance = PnmpUtils.upSnrVariance(mtr,cmDataList);
                    pnmpCalculationResult.setMtrVariance(mtrVariance);
                    pnmpCalculationResult.setUpSnrVariance(upSnrVariance);
                    PnmpCmData pnmpCmData = new PnmpCmData();
                    pnmpCmData.setEntityId(pnmpPollResult.getEntityId());
                    pnmpCmData.setCmcId(pnmpPollResult.getCmcId());
                    pnmpCmData.setCmMac(pnmpPollResult.getCmMac());
                    pnmpCmData.setMtr(pnmpCalculationResult.getMtr());
                    pnmpCmData.setMtc(pnmpCalculationResult.getMtc());
                    pnmpCmData.setUpSnr(pnmpCalculationResult.getUpSnr());
                    cmDataList.add(pnmpCmData);
                    pnmpPollStatisticsEngineService.modifyCmHisData(pnmpPollResult.getCmcId(),pnmpPollResult.getCmMac(),cmDataList);
                    Double mtrToUpSnrSimilarity = PnmpUtils.mtrToUpSnrSimilarity(cmDataList);
                    pnmpCalculationResult.setMtrToUpSnrSimilarity(mtrToUpSnrSimilarity);
                } else {
                    pnmpPollStatisticsEngineService.delCmHisData(pnmpPollResult.getCmcId(),pnmpPollResult.getCmMac());
                }
                pnmpPollStatisticsEngineService.dispachPnmpPoll(pnmpPollResult, mtr);
            }
            pnmpCalculationResult.setCollectTime(new Timestamp(pnmpPollResult.getCollectTime()).toString());
            try {
                if (pnmpPollTask instanceof PnmpPollMiddleSpeedEndTask || pnmpPollTask instanceof PnmpPollHighSpeedEndTask) {
                    pnmpPollStatisticsEngineService.insertPnmpCalculationResult(pnmpCalculationResult, false);
                } else {
                    pnmpPollStatisticsEngineService.insertPnmpCalculationResult(pnmpCalculationResult, true);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
            logger.info("" + pnmpCalculationResult);
        } catch (Exception e) {
            logger.error("PnmpPollResultCalculationThreadPool",e);
        } finally {
            pnmpPollStatisticsEngineService.delCalculationCount();
            logger.debug("PnmpPollResultCalculationThreadPool.run end");
        }
    }

    public PnmpPollResult getPnmpPollResult() {
        return pnmpPollResult;
    }

    public void setPnmpPollResult(PnmpPollResult pnmpPollResult) {
        this.pnmpPollResult = pnmpPollResult;
    }

    public PnmpPollTask getPnmpPollTask() {
        return pnmpPollTask;
    }

    public void setPnmpPollTask(PnmpPollTask pnmpPollTask) {
        this.pnmpPollTask = pnmpPollTask;
    }

    public PnmpPollStatisticsEngineService getPnmpPollStatisticsEngineService() {
        return pnmpPollStatisticsEngineService;
    }

    public void setPnmpPollStatisticsEngineService(PnmpPollStatisticsEngineService pnmpPollStatisticsEngineService) {
        this.pnmpPollStatisticsEngineService = pnmpPollStatisticsEngineService;
    }
}