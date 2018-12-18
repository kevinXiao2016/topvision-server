package com.topvision.ems.cm.cmtsInfoSummary.engine;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsEngineService;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsPush;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.cm.cmtsInfoSummary.domain.CmMainChannel;
import com.topvision.ems.cm.cmtsInfoSummary.domain.CmSignalWithCmcId;
import com.topvision.ems.cm.cmtsInfoSummary.domain.CmtsInfoStatistics;
import com.topvision.ems.cm.cmtsInfoSummary.engine.dao.CmtsInfoSummaryDao;
import com.topvision.ems.cm.cmtsInfoSummary.facade.CmtsInfoSummaryFacade;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoThreshold;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.framework.annotation.Engine;

@Engine("cmtsInfoSummaryFacade")
public class CmtsInfoSummaryEngineImpl extends BaseEngine implements CmtsInfoSummaryFacade, CmPollStatisticsPush {
    @Autowired
    private CmPollStatisticsEngineService cmPollStatisticsEngineService;
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    private CmtsInfoSummaryDao cmtsInfoSummaryDao;

    private List<CmSignalWithCmcId> cmSignalWithCmcIds;
    private List<Long> cmtsList = new ArrayList<Long>();
    // engine端接收的CM轮询数据
    private LinkedBlockingQueue<CmPollResult> cmQueue = null;
    // CM信号质量处理线程
    private Thread caculateThread = null;
    // 本轮开启时间
    private Timestamp currentTime;
    private CmtsInfoThreshold cmtsInfoThreshold;
    private List<CmMainChannel> cmMainChannel;
    private List<CmtsInfoStatistics> cmtsInfoStatistics = new ArrayList<CmtsInfoStatistics>();
    private Map<Long, Map<String, Double>> cmtsDistMap = null;

    public static final String AVGUPSNR = "avgUpSnr";
    public static final String AVGUPPOWER = "avgUpPower";
    public static final String AVGDOWNPOWER = "avgDownPower";
    public static final String AVGDOWNSNR = "avgDownSnr";
    public static final String CM_TOTAL = "cmTotal";
    public static final String UPSNROUTRANGE = "upSnrOutRange";
    public static final String UPPOWEROUTRANGE = "upPowerOutRange";
    public static final String DOWNPOWEROUTRANGE = "downPowerOutRange";
    public static final String DOWNSNROUTRANGE = "downSnrOutRange";
    public static final String ONLINE_CM_NUM = "onlineCmNum";

    @Override
    public void connected() {
        super.connected();
        if (cmtsInfoSummaryDao == null) {
            try {
                cmtsInfoSummaryDao = engineDaoFactory.getEngineDao(CmtsInfoSummaryDao.class);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("cannot get cmtsInfoSummaryDao.");
                }
            }
        }
        // 如果没有开启处理线程，则开启
        if (caculateThread == null) {
            startCacuThread();
        }
    }

    public void doSummary() {
        if (cmtsList.size() != 0) {
            for (int i = 0; i < cmtsList.size(); i++) {
                Long cmcId = cmtsList.get(i);
                CmtsInfoStatistics cmts = new CmtsInfoStatistics();
                int countUpSnr = 0;
                int countDownSnr = 0;
                int countUpTxPower = 0;
                int countDownRePower = 0;
                double totalUpSnr = 0.0;
                double totalDownSnr = 0.0;
                double totalUpTx = 0.0;
                double totalDownRe = 0.0;
                int onlineCmNum = 0;
                int ccTotalCm = 0;
                for (CmSignalWithCmcId cs : cmSignalWithCmcIds) {
                    try {
                        if (cs.getCmcId().equals(cmcId)) {
                            if (cs.getUpChannelSnr() != null) {
                                totalUpSnr += cs.getUpChannelSnr();
                            }
                            if (cs.getDownChannelSnr() != null) {
                                totalDownSnr += cs.getDownChannelSnr();
                            }
                            if (cs.getUpChannelTx() != null) {
                                totalUpTx += cs.getUpChannelTx();
                            }
                            if (cs.getDownChannelTx() != null) {
                                totalDownRe += cs.getDownChannelTx();
                            }
                            if (CmAttribute.isCmOnline(cs.getStateValue())) {
                                onlineCmNum++;
                            }
                            ccTotalCm++;
                            if (cs.getUpChannelSnr() == null) {
                                countUpSnr++;
                            } else {
                                if (cmtsInfoThreshold.getUpSnrMax() != null) {
                                    if (cs.getUpChannelSnr() > cmtsInfoThreshold.getUpSnrMax()) {
                                        countUpSnr++;
                                    }
                                }
                                if (cmtsInfoThreshold.getUpSnrMin() != null) {
                                    if (cs.getUpChannelSnr() < cmtsInfoThreshold.getUpSnrMin()) {
                                        countUpSnr++;
                                    }
                                }
                            }
                            if (cs.getDownChannelSnr() == null) {
                                countDownSnr++;
                            } else {
                                if (cmtsInfoThreshold.getDownSnrMax() != null) {
                                    if (cs.getDownChannelSnr() > cmtsInfoThreshold.getDownSnrMax()) {
                                        countDownSnr++;
                                    }
                                }
                                if (cmtsInfoThreshold.getDownSnrMin() != null) {
                                    if (cs.getDownChannelSnr() < cmtsInfoThreshold.getDownSnrMin()) {
                                        countDownSnr++;
                                    }
                                }
                            }
                            if (cs.getUpChannelTx() == null) {
                                countUpTxPower++;
                            } else {
                                if (cmtsInfoThreshold.getUpPowerMax() != null) {
                                    if (cs.getUpChannelTx() > cmtsInfoThreshold.getUpPowerMax()) {
                                        countUpTxPower++;
                                    }
                                }
                                if (cmtsInfoThreshold.getUpPowerMin() != null) {
                                    if (cs.getUpChannelTx() < cmtsInfoThreshold.getUpPowerMin()) {
                                        countUpTxPower++;
                                    }
                                }
                            }
                            if (cs.getDownChannelTx() == null) {
                                countDownRePower++;
                            } else {
                                if (cmtsInfoThreshold.getDownPowerMax() != null) {
                                    if (cs.getDownChannelTx() > cmtsInfoThreshold.getDownPowerMax()) {
                                        countDownRePower++;
                                    }
                                }
                                if (cmtsInfoThreshold.getDownPowerMin() != null) {
                                    if (cs.getDownChannelTx() < cmtsInfoThreshold.getDownPowerMin()) {
                                        countDownRePower++;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // 处理CM元素出错，继续处理下一个数据
                        logger.debug("cmtsSummary handle failed cmId " + cs.getCmId(), e.getMessage());
                    }

                }
                if (ccTotalCm != 0) {
                    cmts.setCmcId(cmcId);
                    cmts.setUpSnrOutRange(((double) countUpSnr / (double) ccTotalCm));
                    cmts.setDownSnrOutRange(((double) countDownSnr / (double) ccTotalCm));
                    cmts.setUpTxOutRange(((double) countUpTxPower / (double) ccTotalCm));
                    cmts.setDownReOutRange(((double) countDownRePower / (double) ccTotalCm));
                    cmts.setUpSnrAvg(((double) totalUpSnr / (double) ccTotalCm));
                    cmts.setDownSnrAvg(((double) totalDownSnr / (double) ccTotalCm));
                    cmts.setUpTxAvg(((double) totalUpTx / (double) ccTotalCm));
                    cmts.setOnlineCmNum(onlineCmNum);
                    cmts.setTotalCmNum(ccTotalCm);
                    cmts.setDownReAvg(((double) totalDownRe / (double) ccTotalCm));
                }
                cmts.setCollectTime(currentTime);
                cmtsInfoStatistics.add(cmts);
            }
            if (cmtsInfoSummaryDao != null) {
                cmtsInfoSummaryDao.insertCmtsInfoSummary(cmtsInfoStatistics);
                cmtsInfoSummaryDao.updateCmtsInfoSummaryLst(cmtsInfoStatistics);
            }
        }
    }

    public void summaryJobBegin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 处理数据
                    doSummary();
                } catch (Exception e) {
                    logger.debug("" + e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void completeRoundStatistics(Long time) {
        // summaryJobBegin();
    }

    @Override
    public void startRoundStatistics(Long time) {
        logger.info("cmtsInfo-startRoundStatistics");
        cmSignalWithCmcIds.clear();
        cmtsDistMap.clear();
        cmQueue.clear();
        cmtsInfoStatistics.clear();
        cmtsList.clear();
        currentTime = new Timestamp(System.currentTimeMillis());
        if (cmtsInfoSummaryDao != null) {
            cmtsInfoThreshold = cmtsInfoSummaryDao.selectLocalThreshold();
            cmMainChannel = cmtsInfoSummaryDao.selectCmAttr();
        }
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
        logger.debug("cmtsInfoEngine-cmtsDistMap:"+cmtsDistMap+ " -cmPollResult:" + cmPollResults);
    }

    private void startCacuThread() {
        logger.info("cmtsInfo-startCacuThread");
        caculateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                CmPollResult cm;
                while (cmQueue != null) {
                    try {
                        cm = cmQueue.take();
                        // 整理数据
                        arrangeData(cm);
                    } catch (Exception e) {
                        // 处理CM元素出错，继续处理下一个数据
                        logger.debug("cmtsInfo-arragedataError" + e.getMessage());
                    }
                }

            }
        });
        caculateThread.start();
    }

    private void arrangeData(CmPollResult cm) {
        String[] upChIds = cm.getUpChIds().split(","); // 上行信道
        String[] upChSnrs = cm.getUpChSnrs().split(","); // 上行信道SNR
        String[] upChTxPowers = cm.getUpChTxPowers().split(","); // 上行信道电平

        String[] downChIds = cm.getDownChIds().split(",");// 下行信道
        String[] downChannelSnrs = cm.getDownChSnrs().split(",");// 下行信道SNR
        String[] downChRxPowers = cm.getDownChRxPowers().split(",");// 下行信道电平

        if (!cmtsList.contains(cm.getCmcId())) {
            cmtsList.add(cm.getCmcId());
        }
        try {
            CmSignalWithCmcId cmSignal = new CmSignalWithCmcId();
            cmSignal.setCmId(cm.getCmId());
            cmSignal.setCmcId(cm.getCmcId());
            cmSignal.setCollectTime(currentTime);
            cmSignal.setStateValue(cm.getStatusValue());

            if (upChIds.length > 1) {
                for (int i = 0, len = upChIds.length; i < len; i++) {
                    for (CmMainChannel cmmc : cmMainChannel) {
                        if (cm.getCmId().equals(cmmc.getCmId())) {
                            Integer upMainChannelId = cmmc.getCmMainUpChannel();
                            if (upChIds[i].equals(upMainChannelId.toString())) {
                                if (!CmPollResult.NULL.equals(upChSnrs[i]) && !"".equals(upChSnrs[i])) {
                                    cmSignal.setUpChannelSnr(Double.valueOf(upChSnrs[i]) / 10);
                                }
                                if (!CmPollResult.NULL.equals(upChTxPowers[i]) && !"".equals(upChTxPowers[i])) {
                                    cmSignal.setUpChannelTx(Double.valueOf(upChTxPowers[i]) / 10);
                                }
                            }
                        }
                    }
                }
            } else {
                if (!CmPollResult.NULL.equals(upChSnrs[0]) && !"".equals(upChSnrs[0])) {
                    cmSignal.setUpChannelSnr(Double.valueOf(upChSnrs[0]) / 10);
                }
                if (!CmPollResult.NULL.equals(upChTxPowers[0]) && !"".equals(upChTxPowers[0])) {
                    cmSignal.setUpChannelTx(Double.valueOf(upChTxPowers[0]) / 10);
                }
            }

            if (downChIds.length > 1) {
                for (int i = 0, len = downChIds.length; i < len; i++) {
                    for (CmMainChannel cmmc : cmMainChannel) {
                        if (cm.getCmId().equals(cmmc.getCmId())) {
                            Integer downMainChannelId = cmmc.getCmMainDownChannel();
                            if (downChIds[i].equals(downMainChannelId.toString())) {
                                if (!CmPollResult.NULL.equals(downChannelSnrs[i]) && !"".equals(downChannelSnrs[i])) {
                                    cmSignal.setDownChannelSnr(Double.valueOf(downChannelSnrs[i]) / 10);
                                }
                                if (!CmPollResult.NULL.equals(downChRxPowers[i]) && !"".equals(downChRxPowers[i])) {
                                    cmSignal.setDownChannelTx(Double.valueOf(downChRxPowers[i]) / 10);
                                }
                            }
                        }
                    }
                }
            } else {
                if (!CmPollResult.NULL.equals(downChannelSnrs[0]) && !"".equals(downChannelSnrs[0])) {
                    cmSignal.setDownChannelSnr(Double.valueOf(downChannelSnrs[0]) / 10);
                }
                if (!CmPollResult.NULL.equals(downChRxPowers[0]) && !"".equals(downChRxPowers[0])) {
                    cmSignal.setDownChannelTx(Double.valueOf(downChRxPowers[0]) / 10);
                }
            }
            cmSignalWithCmcIds.add(cmSignal);
            if (!cmtsDistMap.containsKey(cm.getCmcId())) {
                Map<String, Double> mapTemp = new HashMap<String, Double>();
                mapTemp.put(AVGUPSNR, 0d);
                mapTemp.put(AVGUPPOWER, 0d);
                mapTemp.put(AVGDOWNPOWER, 0d);
                mapTemp.put(AVGDOWNSNR, 0d);
                mapTemp.put(CM_TOTAL, 0d);
                mapTemp.put(UPSNROUTRANGE, 0d);
                mapTemp.put(UPPOWEROUTRANGE, 0d);
                mapTemp.put(DOWNPOWEROUTRANGE, 0d);
                mapTemp.put(DOWNSNROUTRANGE, 0d);
                mapTemp.put(ONLINE_CM_NUM, 0d);
                cmtsDistMap.put(cm.getCmcId(), mapTemp);
            }
            Map<String, Double> curCmc = cmtsDistMap.get(cm.getCmcId());
            if (cmSignal.getUpChannelSnr() != null) {
                curCmc.put(AVGUPSNR, (curCmc.get(AVGUPSNR) * curCmc.get(CM_TOTAL) + cmSignal.getUpChannelSnr())
                        / (curCmc.get(CM_TOTAL) + 1));
            }
            if (cmSignal.getUpChannelTx() != null) {
                curCmc.put(AVGUPPOWER, (curCmc.get(AVGUPPOWER) * curCmc.get(CM_TOTAL) + cmSignal.getUpChannelTx())
                        / (curCmc.get(CM_TOTAL) + 1));
            }
            if (cmSignal.getDownChannelTx() != null) {
                curCmc.put(
                        AVGDOWNPOWER,
                        (curCmc.get(AVGDOWNPOWER) * curCmc.get(CM_TOTAL) + cmSignal.getDownChannelTx())
                                / (curCmc.get(CM_TOTAL) + 1));
            }
            if (cmSignal.getDownChannelSnr() != null) {
                curCmc.put(AVGDOWNSNR, (curCmc.get(AVGDOWNSNR) * curCmc.get(CM_TOTAL) + cmSignal.getDownChannelSnr())
                        / (curCmc.get(CM_TOTAL) + 1));
            }
            // 上行snr不合格率
            if (cmSignal.getUpChannelSnr() == null) {
                curCmc.put(UPSNROUTRANGE,
                        (curCmc.get(UPSNROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
            } else {
                if (cmtsInfoThreshold.getUpSnrMax() != null && cmtsInfoThreshold.getUpSnrMin() != null) {
                    if (cmSignal.getUpChannelSnr() < cmtsInfoThreshold.getUpSnrMax()
                            && cmSignal.getUpChannelSnr() >= cmtsInfoThreshold.getUpSnrMin()) {
                        curCmc.put(UPSNROUTRANGE,
                                (curCmc.get(UPSNROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(UPSNROUTRANGE,
                                (curCmc.get(UPSNROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else if (cmtsInfoThreshold.getUpSnrMax() != null && cmtsInfoThreshold.getUpSnrMin() == null) {
                    if (cmSignal.getUpChannelSnr() < cmtsInfoThreshold.getUpSnrMax()) {
                        curCmc.put(UPSNROUTRANGE,
                                (curCmc.get(UPSNROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(UPSNROUTRANGE,
                                (curCmc.get(UPSNROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else if (cmtsInfoThreshold.getUpSnrMax() == null && cmtsInfoThreshold.getUpSnrMin() != null) {
                    if (cmSignal.getUpChannelSnr() >= cmtsInfoThreshold.getUpSnrMin()) {
                        curCmc.put(UPSNROUTRANGE,
                                (curCmc.get(UPSNROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(UPSNROUTRANGE,
                                (curCmc.get(UPSNROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else {
                    curCmc.put(UPSNROUTRANGE,
                            (curCmc.get(UPSNROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                }
            }
            // 下行snr不合格率
            if (cmSignal.getDownChannelSnr() == null) {
                curCmc.put(DOWNSNROUTRANGE,
                        (curCmc.get(DOWNSNROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
            } else {
                if (cmtsInfoThreshold.getDownSnrMax() != null && cmtsInfoThreshold.getDownSnrMin() != null) {
                    if (cmSignal.getDownChannelSnr() < cmtsInfoThreshold.getDownSnrMax()
                            && cmSignal.getDownChannelSnr() >= cmtsInfoThreshold.getDownSnrMin()) {
                        curCmc.put(DOWNSNROUTRANGE,
                                (curCmc.get(DOWNSNROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(DOWNSNROUTRANGE,
                                (curCmc.get(DOWNSNROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else if (cmtsInfoThreshold.getDownSnrMax() != null && cmtsInfoThreshold.getDownSnrMin() == null) {
                    if (cmSignal.getDownChannelSnr() < cmtsInfoThreshold.getDownSnrMax()) {
                        curCmc.put(DOWNSNROUTRANGE,
                                (curCmc.get(DOWNSNROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(DOWNSNROUTRANGE,
                                (curCmc.get(DOWNSNROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else if (cmtsInfoThreshold.getDownSnrMax() == null && cmtsInfoThreshold.getDownSnrMin() != null) {
                    if (cmSignal.getDownChannelSnr() >= cmtsInfoThreshold.getDownSnrMin()) {
                        curCmc.put(DOWNSNROUTRANGE,
                                (curCmc.get(DOWNSNROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(DOWNSNROUTRANGE,
                                (curCmc.get(DOWNSNROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else {
                    curCmc.put(DOWNSNROUTRANGE,
                            (curCmc.get(DOWNSNROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                }
            }
            // 上行电平不合格率
            if (cmSignal.getUpChannelTx() == null) {
                curCmc.put(UPPOWEROUTRANGE,
                        (curCmc.get(UPPOWEROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
            } else {
                if (cmtsInfoThreshold.getUpPowerMax() != null && cmtsInfoThreshold.getUpPowerMin() != null) {
                    if (cmSignal.getUpChannelTx() < cmtsInfoThreshold.getUpPowerMax()
                            && cmSignal.getUpChannelTx() >= cmtsInfoThreshold.getUpPowerMin()) {
                        curCmc.put(UPPOWEROUTRANGE,
                                (curCmc.get(UPPOWEROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(UPPOWEROUTRANGE,
                                (curCmc.get(UPPOWEROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else if (cmtsInfoThreshold.getUpPowerMax() != null && cmtsInfoThreshold.getUpPowerMin() == null) {
                    if (cmSignal.getUpChannelTx() < cmtsInfoThreshold.getUpPowerMax()) {
                        curCmc.put(UPPOWEROUTRANGE,
                                (curCmc.get(UPPOWEROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(UPPOWEROUTRANGE,
                                (curCmc.get(UPPOWEROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else if (cmtsInfoThreshold.getUpPowerMax() == null && cmtsInfoThreshold.getUpPowerMin() != null) {
                    if (cmSignal.getUpChannelTx() >= cmtsInfoThreshold.getUpPowerMin()) {
                        curCmc.put(UPPOWEROUTRANGE,
                                (curCmc.get(UPPOWEROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(UPPOWEROUTRANGE,
                                (curCmc.get(UPPOWEROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else {
                    curCmc.put(UPPOWEROUTRANGE,
                            (curCmc.get(UPPOWEROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                }
            }
            // 下行电平不合格率
            if (cmSignal.getDownChannelTx() == null) {
                curCmc.put(DOWNPOWEROUTRANGE,
                        (curCmc.get(DOWNPOWEROUTRANGE) * curCmc.get(CM_TOTAL) + 1) / (curCmc.get(CM_TOTAL) + 1));
            } else {
                if (cmtsInfoThreshold.getDownPowerMax() != null && cmtsInfoThreshold.getDownPowerMin() != null) {
                    if (cmSignal.getDownChannelTx() < cmtsInfoThreshold.getDownPowerMax()
                            && cmSignal.getDownChannelTx() >= cmtsInfoThreshold.getDownPowerMin()) {
                        curCmc.put(DOWNPOWEROUTRANGE,
                                (curCmc.get(DOWNPOWEROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(DOWNPOWEROUTRANGE, (curCmc.get(DOWNPOWEROUTRANGE) * curCmc.get(CM_TOTAL) + 1)
                                / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else if (cmtsInfoThreshold.getDownPowerMax() != null && cmtsInfoThreshold.getDownPowerMin() == null) {
                    if (cmSignal.getDownChannelTx() < cmtsInfoThreshold.getDownPowerMax()) {
                        curCmc.put(DOWNPOWEROUTRANGE,
                                (curCmc.get(DOWNPOWEROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(DOWNPOWEROUTRANGE, (curCmc.get(DOWNPOWEROUTRANGE) * curCmc.get(CM_TOTAL) + 1)
                                / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else if (cmtsInfoThreshold.getDownPowerMax() == null && cmtsInfoThreshold.getDownPowerMin() != null) {
                    if (cmSignal.getDownChannelTx() >= cmtsInfoThreshold.getDownPowerMin()) {
                        curCmc.put(DOWNPOWEROUTRANGE,
                                (curCmc.get(DOWNPOWEROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                    } else {
                        curCmc.put(DOWNPOWEROUTRANGE, (curCmc.get(DOWNPOWEROUTRANGE) * curCmc.get(CM_TOTAL) + 1)
                                / (curCmc.get(CM_TOTAL) + 1));
                    }
                } else {
                    curCmc.put(DOWNPOWEROUTRANGE,
                            (curCmc.get(DOWNPOWEROUTRANGE) * curCmc.get(CM_TOTAL)) / (curCmc.get(CM_TOTAL) + 1));
                }
            }
            if (CmAttribute.isCmOnline(cmSignal.getStateValue())) {
                curCmc.put(ONLINE_CM_NUM, curCmc.get(ONLINE_CM_NUM) + 1);
            }
            curCmc.put(CM_TOTAL, curCmc.get(CM_TOTAL) + 1);
            logger.debug("cmtsInfoEngine-arrangeCmEnd:"+cm);
        } catch (Exception e) {
            logger.debug("cmtsSummary sum error"+e.getMessage());
            logger.error(String.format("cmtsSummary single cm error: cmId: %s", cm.getCmId()));
        }
    }

    @Override
    public String moduleName() {
        return "CmtsInfoSummaryEngineImpl";
    }

    @Override
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} initialize invoked.", getClass());
        }
        logger.info("cmtsSummary-Initialize");
        cmPollStatisticsEngineService.registCmPollStatisticsPush(this);
        cmSignalWithCmcIds = new ArrayList<CmSignalWithCmcId>();
        cmtsDistMap = new HashMap<Long, Map<String, Double>>();
        cmQueue = new LinkedBlockingQueue<CmPollResult>();
        currentTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public void destroy() {
        cmtsDistMap.clear();
        cmQueue.clear();
        cmSignalWithCmcIds = null;
    }

    @Override
    public Map<Long, Map<String, Double>> getCmtsInfoDist() {
        while (!cmQueue.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        logger.debug("push cmtsInfo data to server.");
        return cmtsDistMap;
    }

}
