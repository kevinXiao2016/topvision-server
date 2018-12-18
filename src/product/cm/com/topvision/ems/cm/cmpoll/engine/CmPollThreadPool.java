package com.topvision.ems.cm.cmpoll.engine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cm.cmpoll.engine.dao.CmPollHsqlDao;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPoll3DsRemoteQuery;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPoll3UsRemoteQuery;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollNormalTask;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollRemoteQueryTask;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollStatus;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.ems.cm.cmpoll.facade.domain.DocsIf3CmPollUsStatus;
import com.topvision.ems.cm.cmpoll.facade.domain.DownstreamChannelInfo;
import com.topvision.ems.cm.cmpoll.facade.domain.UpstreamChannelInfo;
import com.topvision.ems.facade.callback.CmPollCallback;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

@Engine
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmPollThreadPool implements Runnable {
    private Logger logger = LoggerFactory.getLogger(CmPollThreadPool.class);
    private static int CM20 = 1;
    private static int CM30 = 2;
    @Autowired
    private SnmpExecutorService snmpExecutorService;
    @Autowired
    private PingExecutorService pingExecutorService;
    @Autowired
    private CmPollHsqlDao cmPollHsqlDao;
    private CmPollTask cmPollTask;
    private Integer engineId;
    private SnmpParam snmpParam;
    private CmPollCallback cmPollCallback;
    private Long time;
    private static Long nextResultId = 1L;
    private static Object next = new Object();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Long nextResultId() {
        synchronized (next) {
            return nextResultId++;
        }
    }

    public static void resetNextResultId() {
        synchronized (next) {
            nextResultId = 1L;
        }
    }

    @Override
    public void run() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmPollThreadPool.run cmPollTask:" + cmPollTask);
        }
        CmPollResult cmPollResult = new CmPollResult();
        cmPollResult.setCheckStatus(CmPollResult.CHECKSUCCESS);
        cmPollResult.setStatusValue(cmPollTask.getStatusValue());
        if (cmPollTask instanceof CmPollRemoteQueryTask) {
            CmPollRemoteQueryTask cmPollRemoteQueryTask = (CmPollRemoteQueryTask) cmPollTask;
            cmPollResult.setCmRemoteQueryStatus(cmPollRemoteQueryTask.getCmRemoteQueryStatus());
            makeCmPollResult(cmPollResult, cmPollRemoteQueryTask, cmPollRemoteQueryTask.getCm3DsRemoteQueryList(),
                    cmPollRemoteQueryTask.getCm3UsRemoteQueryList());

            if (logger.isTraceEnabled()) {
                logger.trace("CmPollThreadPool.run time:" + sdf.format(new Date(time)));
            }
            if (logger.isTraceEnabled()) {
                logger.trace("CmPollThreadPool.run cmPollRemoteQueryTask.getDownstreamChannelList():"
                        + cmPollRemoteQueryTask.getDownstreamChannelList());
            }
            if (logger.isTraceEnabled()) {
                logger.trace("CmPollThreadPool.run cmPollRemoteQueryTask.getUpstreamChannelInfo():"
                        + cmPollRemoteQueryTask.getUpstreamChannelInfo());
            }
            if (logger.isTraceEnabled()) {
                logger.trace("CmPollThreadPool.run getCm3DsRemoteQueryList():"
                        + cmPollRemoteQueryTask.getCm3DsRemoteQueryList());
            }
            if (logger.isTraceEnabled()) {
                logger.trace("CmPollThreadPool.run getCm3UsRemoteQueryList():"
                        + cmPollRemoteQueryTask.getCm3UsRemoteQueryList());
            }
            if (logger.isTraceEnabled()) {
                logger.trace("CmPollThreadPool.run cmPollResult:" + cmPollResult);
            }
            cmPollHsqlDao.saveLocalRecord(time, cmPollResult);
        } else if (cmPollTask instanceof CmPollNormalTask) {
            CmPollNormalTask cmPollNormalTask = (CmPollNormalTask) cmPollTask;
            if (CmAttribute.isCmOnline(cmPollNormalTask.getStatusValue())) {
                String ip = cmPollTask.getCmIp();
                boolean pingCheck = checkPingReachable(ip);
                cmPollResult.setPingCheck(pingCheck);
                if (pingCheck) {
                    snmpParam.setIpAddress(cmPollTask.getCmIp());
                    boolean snmpCheck = checkSnmpReachable(snmpParam);
                    cmPollResult.setSnmpCheck(snmpCheck);
                    if (snmpCheck) {
                        try {
                            CmPollStatus cmPollStatus = snmpExecutorService.getData(snmpParam, CmPollStatus.class);
                            int cmtype = checkCmType(cmPollStatus);
                            List<UpstreamChannelInfo> docsIfUpstreamChannelList = snmpExecutorService.getTable(
                                    snmpParam, UpstreamChannelInfo.class);
                            List<DownstreamChannelInfo> docsIfDownstreamChannelList = snmpExecutorService.getTable(
                                    snmpParam, DownstreamChannelInfo.class);
                            if (cmtype == CM30) {
                                for (UpstreamChannelInfo aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
                                    aDocsIfUpstreamChannelList.setTxPower(aDocsIfUpstreamChannelList
                                            .getDocsIf3CmStatusUsTxPower());
                                }
                            } else {
                                for (UpstreamChannelInfo aDocsIfUpstreamChannelList : docsIfUpstreamChannelList) {
                                    aDocsIfUpstreamChannelList.setTxPower(aDocsIfUpstreamChannelList
                                            .getDocsIfCmStatusTxPower());
                                }
                            }
                            makeCmPollResult(cmPollResult, cmPollNormalTask, docsIfUpstreamChannelList,
                                    docsIfDownstreamChannelList);
                            if (logger.isTraceEnabled()) {
                                logger.trace("CmPollThreadPool.run time:" + sdf.format(new Date(time)));
                            }
                            if (logger.isTraceEnabled()) {
                                logger.trace("CmPollThreadPool.run cmPollNormalTask.getDownstreamChannelList():"
                                        + cmPollNormalTask.getDownstreamChannelList());
                            }
                            if (logger.isTraceEnabled()) {
                                logger.trace("CmPollThreadPool.run cmPollNormalTask.getUpstreamChannelInfo():"
                                        + cmPollNormalTask.getUpstreamChannelInfo());
                            }
                            if (logger.isTraceEnabled()) {
                                logger.trace("CmPollThreadPool.run docsIfUpstreamChannelList:"
                                        + docsIfUpstreamChannelList);
                            }
                            if (logger.isTraceEnabled()) {
                                logger.trace("CmPollThreadPool.run docsIfDownstreamChannelList:"
                                        + docsIfDownstreamChannelList);
                            }
                            if (logger.isTraceEnabled()) {
                                logger.trace("CmPollThreadPool.run cmPollResult:" + cmPollResult);
                            }
                            cmPollHsqlDao.saveLocalRecord(time, cmPollResult);
                        } catch (Exception e) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Collect " + ip, e);
                            }
                        }
                    } else {
                        makeCmPollResult(cmPollResult, cmPollTask);
                        cmPollHsqlDao.saveLocalRecord(time, cmPollResult);
                    }
                } else {
                    makeCmPollResult(cmPollResult, cmPollTask);
                    cmPollHsqlDao.saveLocalRecord(time, cmPollResult);
                }
            } else {
                cmPollResult.setPingCheck(false);
                makeCmPollResult(cmPollResult, cmPollTask);
                cmPollHsqlDao.saveLocalRecord(time, cmPollResult);
            }
        }
        getCmPollCallback().completeTask(engineId, cmPollTask.getTaskId());
        logger.debug("CmPollThreadPool.run end");
    }

    private int checkCmType(CmPollStatus cmPollStatus) {
        switch (cmPollStatus.getDocsIfDocsisBaseCapability()) {
        case 4:// 3.0cm
               // 注册模式3.0
            if (cmPollStatus.getDocsIf3CmCapabilitiesRsp() != null) {
                if (getCmDocsisRegVersion(cmPollStatus.getDocsIf3CmCapabilitiesRsp()) == 3) {
                    return CM30;
                }
            }
        }
        // 其他情况都是注册模式2.0
        return CM20;
    }

    private boolean checkSnmpReachable(SnmpParam snmpParam) {
        try {
            snmpExecutorService.get(snmpParam, ".1.3.6.1.2.1.1.3.0");
            return true;
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Check Snmp" + snmpParam.getIpAddress(), e);
            }
        }
        return false;
    }

    private void makeCmPollResult(CmPollResult cmPollResult, CmPollTask cmPollTask) {
        Long resultId = nextResultId();
        cmPollResult.setResultId(resultId);
        cmPollResult.setEntityId(cmPollTask.getEntityId());
        cmPollResult.setCmcId(cmPollTask.getCmcId());
        cmPollResult.setCmId(cmPollTask.getCmId());
        cmPollResult.setCmcIndex(cmPollTask.getCmcIndex());
        cmPollResult.setCmIndex(cmPollTask.getCmIndex());
        cmPollResult.setCmIp(cmPollTask.getCmIp());
        cmPollResult.setCmMac(cmPollTask.getCmMac());
        cmPollResult.setUpChIds("");
        cmPollResult.setUpChFreqs("");
        cmPollResult.setUpChSnrs("");
        cmPollResult.setUpChRxPowers("");
        cmPollResult.setUpChTxPowers("");
        cmPollResult.setDownChIds("");
        cmPollResult.setDownChFreqs("");
        cmPollResult.setDownChSnrs("");
        cmPollResult.setDownChRxPowers("");
        cmPollResult.setCollectTime(System.currentTimeMillis());
    }

    private void makeCmPollResult(CmPollResult cmPollResult, CmPollRemoteQueryTask cmPollRemoteQueryTask,
            List<CmPoll3DsRemoteQuery> cm3DsRemoteQueryList, List<CmPoll3UsRemoteQuery> cm3UsRemoteQueryList) {
        Long resultId = nextResultId();
        cmPollResult.setResultId(resultId);
        cmPollResult.setEntityId(cmPollRemoteQueryTask.getEntityId());
        cmPollResult.setCmcId(cmPollRemoteQueryTask.getCmcId());
        cmPollResult.setCmId(cmPollRemoteQueryTask.getCmId());
        cmPollResult.setCmcIndex(cmPollRemoteQueryTask.getCmcIndex());
        cmPollResult.setCmIndex(cmPollRemoteQueryTask.getCmIndex());
        cmPollResult.setCmIp(cmPollRemoteQueryTask.getCmIp());
        cmPollResult.setCmMac(cmPollRemoteQueryTask.getCmMac());
        List<Integer> upChIds = new ArrayList<Integer>();
        Map<Integer, Long> upChSnrs = new HashMap<Integer, Long>();
        Map<Integer, Long> upChFrqs = new HashMap<Integer, Long>();
        Map<Integer, Long> upChRxPowers = new HashMap<Integer, Long>();
        Map<Integer, Long> upChTxPowers = new HashMap<Integer, Long>();
        if (cmPollRemoteQueryTask.getDocsIf3CmPollUsStatusList() == null) {
            cmPollRemoteQueryTask.setDocsIf3CmPollUsStatusList(new ArrayList<DocsIf3CmPollUsStatus>());
        }
        Map<Long, Integer> upIndexToId = new HashMap<Long, Integer>();
        for (UpstreamChannelInfo upstreamChannelInfo : cmPollRemoteQueryTask.getUpstreamChannelInfo()) {
            upIndexToId.put(upstreamChannelInfo.getIfIndex(), upstreamChannelInfo.getDocsIfUpChannelId());
            upChFrqs.put(upstreamChannelInfo.getDocsIfUpChannelId(), upstreamChannelInfo.getDocsIfUpChannelFrequency());
        }
        for (DocsIf3CmPollUsStatus docsIf3CmPollUsStatus : cmPollRemoteQueryTask.getDocsIf3CmPollUsStatusList()) {
            upChIds.add(upIndexToId.get(docsIf3CmPollUsStatus.getCmUsStatusChIfIndex()));
            upChSnrs.put(upIndexToId.get(docsIf3CmPollUsStatus.getCmUsStatusChIfIndex()),
                    docsIf3CmPollUsStatus.getCmUsStatusSignalNoise());
            upChRxPowers.put(upIndexToId.get(docsIf3CmPollUsStatus.getCmUsStatusChIfIndex()),
                    docsIf3CmPollUsStatus.getCmUsStatusRxPower());
        }

        if (cm3UsRemoteQueryList == null) {
            cm3UsRemoteQueryList = new ArrayList<CmPoll3UsRemoteQuery>();
        }
        for (CmPoll3UsRemoteQuery cmPoll3UsRemoteQuery : cm3UsRemoteQueryList) {
            upChTxPowers.put(cmPoll3UsRemoteQuery.getCmUsId(), cmPoll3UsRemoteQuery.getCmUsTxPower());
        }
        StringBuilder upChIndexStr = new StringBuilder();
        StringBuilder upChFreqStr = new StringBuilder();
        StringBuilder upChSnrStr = new StringBuilder();
        StringBuilder upChRxPowerStr = new StringBuilder();
        StringBuilder upChTxPowerStr = new StringBuilder();
        if (!upChIds.isEmpty()) {
            for (Integer upChId : upChIds) {
                Long snr = upChSnrs.get(upChId);
                Long freq = upChFrqs.get(upChId);
                Long rxPower = upChRxPowers.get(upChId);
                Long txPower = upChTxPowers.get(upChId);
                upChIndexStr.append(upChId);
                upChIndexStr.append(",");
                upChFreqStr.append(freq == null ? CmPollResult.NULL : freq);
                upChFreqStr.append(",");
                upChSnrStr.append(snr == null ? CmPollResult.NULL : snr);
                upChSnrStr.append(",");
                upChRxPowerStr.append(rxPower == null ? CmPollResult.NULL : rxPower);
                upChRxPowerStr.append(",");
                upChTxPowerStr.append(txPower == null ? CmPollResult.NULL : txPower);
                upChTxPowerStr.append(",");
            }
            if (upChIndexStr.length() > 0) {
                upChIndexStr.deleteCharAt(upChIndexStr.length() - 1);
                upChFreqStr.deleteCharAt(upChFreqStr.length() - 1);
                upChSnrStr.deleteCharAt(upChSnrStr.length() - 1);
                upChRxPowerStr.deleteCharAt(upChRxPowerStr.length() - 1);
                upChTxPowerStr.deleteCharAt(upChTxPowerStr.length() - 1);
            }
        }
        cmPollResult.setUpChIds(upChIndexStr.toString());
        cmPollResult.setUpChFreqs(upChFreqStr.toString());
        cmPollResult.setUpChSnrs(upChSnrStr.toString());
        cmPollResult.setUpChRxPowers(upChRxPowerStr.toString());
        cmPollResult.setUpChTxPowers(upChTxPowerStr.toString());

        Map<Integer, Long> downChFrqs = new HashMap<Integer, Long>();
        if (cmPollRemoteQueryTask.getDownstreamChannelList() == null) {
            cmPollRemoteQueryTask.setDownstreamChannelList(new ArrayList<DownstreamChannelInfo>());
        }
        for (DownstreamChannelInfo downstreamChannelInfo : cmPollRemoteQueryTask.getDownstreamChannelList()) {
            downChFrqs.put(downstreamChannelInfo.getDocsIfDownChannelId(),
                    downstreamChannelInfo.getDocsIfDownChannelFrequency());
        }
        StringBuilder downChIndexStr = new StringBuilder();
        StringBuilder downChFreqStr = new StringBuilder();
        StringBuilder downChSnrStr = new StringBuilder();
        StringBuilder downChRxPowerStr = new StringBuilder();

        if (cm3DsRemoteQueryList == null) {
            cm3DsRemoteQueryList = new ArrayList<CmPoll3DsRemoteQuery>();
        }
        if (!cm3DsRemoteQueryList.isEmpty()) {
            for (CmPoll3DsRemoteQuery cmPoll3DsRemoteQuery : cm3DsRemoteQueryList) {
                Integer id = cmPoll3DsRemoteQuery.getCmDsId();
                Long freq = downChFrqs.get(id);
                Long snr = cmPoll3DsRemoteQuery.getCmDsSignalNoise();
                Long rxPower = cmPoll3DsRemoteQuery.getCmDsRxPower();
                downChIndexStr.append(id);
                downChIndexStr.append(",");
                downChFreqStr.append(freq == null ? CmPollResult.NULL : freq);
                downChFreqStr.append(",");
                downChSnrStr.append(snr == null ? CmPollResult.NULL : snr);
                downChSnrStr.append(",");
                downChRxPowerStr.append(rxPower == null ? CmPollResult.NULL : rxPower);
                downChRxPowerStr.append(",");
            }
            if (downChIndexStr.length() > 0) {
                downChIndexStr.deleteCharAt(downChIndexStr.length() - 1);
                downChFreqStr.deleteCharAt(downChFreqStr.length() - 1);
                downChSnrStr.deleteCharAt(downChSnrStr.length() - 1);
                downChRxPowerStr.deleteCharAt(downChRxPowerStr.length() - 1);
            }
        }
        cmPollResult.setDownChIds(downChIndexStr.toString());
        cmPollResult.setDownChFreqs(downChFreqStr.toString());
        cmPollResult.setDownChSnrs(downChSnrStr.toString());
        cmPollResult.setDownChRxPowers(downChRxPowerStr.toString());
        cmPollResult.setCollectTime(System.currentTimeMillis());
    }

    private void makeCmPollResult(CmPollResult cmPollResult, CmPollNormalTask cmPollNormalTask,
            List<UpstreamChannelInfo> upstreamChannelInfos, List<DownstreamChannelInfo> downstreamChannelInfos) {
        Long resultId = nextResultId();
        cmPollResult.setResultId(resultId);
        cmPollResult.setEntityId(cmPollNormalTask.getEntityId());
        cmPollResult.setCmcId(cmPollNormalTask.getCmcId());
        cmPollResult.setCmId(cmPollNormalTask.getCmId());
        cmPollResult.setCmcIndex(cmPollNormalTask.getCmcIndex());
        cmPollResult.setCmIndex(cmPollNormalTask.getCmIndex());
        cmPollResult.setCmIp(cmPollNormalTask.getCmIp());
        cmPollResult.setCmMac(cmPollNormalTask.getCmMac());
        List<Integer> upChIds = new ArrayList<Integer>();
        Map<Integer, Long> upChFreqs = new HashMap<Integer, Long>();
        Map<Integer, Long> upChSnrs = new HashMap<Integer, Long>();
        Map<Integer, Long> upChRxPowers = new HashMap<Integer, Long>();
        Map<Integer, Long> upChTxPowers = new HashMap<Integer, Long>();
        if (cmPollNormalTask.getDocsIf3CmPollUsStatusList() == null) {
            cmPollNormalTask.setDocsIf3CmPollUsStatusList(new ArrayList<DocsIf3CmPollUsStatus>());
        }

        if (cmPollNormalTask.getUpstreamChannelInfo() == null) {
            cmPollNormalTask.setUpstreamChannelInfo(new ArrayList<UpstreamChannelInfo>());
        }
        Map<Long, Integer> indexToId = new HashMap<Long, Integer>();
        for (UpstreamChannelInfo upstreamChannelInfo : cmPollNormalTask.getUpstreamChannelInfo()) {
            indexToId.put(upstreamChannelInfo.getIfIndex(), upstreamChannelInfo.getDocsIfUpChannelId());
            upChFreqs
                    .put(upstreamChannelInfo.getDocsIfUpChannelId(), upstreamChannelInfo.getDocsIfUpChannelFrequency());
        }
        for (DocsIf3CmPollUsStatus docsIf3CmPollUsStatus : cmPollNormalTask.getDocsIf3CmPollUsStatusList()) {
            upChIds.add(indexToId.get(docsIf3CmPollUsStatus.getCmUsStatusChIfIndex()));
            upChSnrs.put(indexToId.get(docsIf3CmPollUsStatus.getCmUsStatusChIfIndex()),
                    docsIf3CmPollUsStatus.getCmUsStatusSignalNoise());
            upChRxPowers.put(indexToId.get(docsIf3CmPollUsStatus.getCmUsStatusChIfIndex()),
                    docsIf3CmPollUsStatus.getCmUsStatusRxPower());
        }

        if (upstreamChannelInfos == null) {
            upstreamChannelInfos = new ArrayList<UpstreamChannelInfo>();
        }
        for (UpstreamChannelInfo upstreamChannelInfo : upstreamChannelInfos) {
            upChTxPowers.put(upstreamChannelInfo.getDocsIfUpChannelId(), upstreamChannelInfo.getTxPower());
        }
        StringBuilder upChIndexStr = new StringBuilder();
        StringBuilder upChFreqStr = new StringBuilder();
        StringBuilder upChSnrStr = new StringBuilder();
        StringBuilder upChRxPowerStr = new StringBuilder();
        StringBuilder upChTxPowerStr = new StringBuilder();
        if (!upChIds.isEmpty()) {
            for (Integer upChId : upChIds) {
                Long freq = upChFreqs.get(upChId);
                Long snr = upChSnrs.get(upChId);
                Long rxPower = upChRxPowers.get(upChId);
                Long txPower = upChTxPowers.get(upChId);
                upChIndexStr.append(upChId);
                upChIndexStr.append(",");
                upChFreqStr.append(freq == null ? CmPollResult.NULL : freq);
                upChFreqStr.append(",");
                upChSnrStr.append(snr == null ? CmPollResult.NULL : snr);
                upChSnrStr.append(",");
                upChRxPowerStr.append(rxPower == null ? CmPollResult.NULL : rxPower);
                upChRxPowerStr.append(",");
                upChTxPowerStr.append(txPower == null ? CmPollResult.NULL : txPower);
                upChTxPowerStr.append(",");
            }
            if (upChIndexStr.length() > 0) {
                upChIndexStr.deleteCharAt(upChIndexStr.length() - 1);
                upChFreqStr.deleteCharAt(upChFreqStr.length() - 1);
                upChSnrStr.deleteCharAt(upChSnrStr.length() - 1);
                upChRxPowerStr.deleteCharAt(upChRxPowerStr.length() - 1);
                upChTxPowerStr.deleteCharAt(upChTxPowerStr.length() - 1);

            }
        }
        cmPollResult.setUpChIds(upChIndexStr.toString());
        cmPollResult.setUpChFreqs(upChFreqStr.toString());
        cmPollResult.setUpChSnrs(upChSnrStr.toString());
        cmPollResult.setUpChRxPowers(upChRxPowerStr.toString());
        cmPollResult.setUpChTxPowers(upChTxPowerStr.toString());

        Map<Integer, Long> downChFrqs = new HashMap<Integer, Long>();
        if (cmPollNormalTask.getDownstreamChannelList() == null) {
            cmPollNormalTask.setDownstreamChannelList(new ArrayList<DownstreamChannelInfo>());
        }
        for (DownstreamChannelInfo downstreamChannelInfo : cmPollNormalTask.getDownstreamChannelList()) {
            downChFrqs.put(downstreamChannelInfo.getDocsIfDownChannelId(),
                    downstreamChannelInfo.getDocsIfDownChannelFrequency());
        }
        StringBuilder downChIndexStr = new StringBuilder();
        StringBuilder downChSnrStr = new StringBuilder();
        StringBuilder downChFreqStr = new StringBuilder();
        StringBuilder downChRxPowerStr = new StringBuilder();

        if (downstreamChannelInfos == null) {
            downstreamChannelInfos = new ArrayList<DownstreamChannelInfo>();
        }
        if (!downstreamChannelInfos.isEmpty()) {
            for (DownstreamChannelInfo downstreamChannelInfo : downstreamChannelInfos) {
                Integer id = downstreamChannelInfo.getDocsIfDownChannelId();
                Long freq = downChFrqs.get(id);
                Long snr = downstreamChannelInfo.getDocsIfSigQSignalNoise();
                Long rxPower = downstreamChannelInfo.getDocsIfDownChannelPower();
                downChIndexStr.append(id);
                downChIndexStr.append(",");
                downChFreqStr.append(freq == null ? CmPollResult.NULL : freq);
                downChFreqStr.append(",");
                downChSnrStr.append(snr == null ? CmPollResult.NULL : snr);
                downChSnrStr.append(",");
                downChRxPowerStr.append(rxPower == null ? CmPollResult.NULL : rxPower);
                downChRxPowerStr.append(",");
            }
            if (downChIndexStr.length() > 0) {
                downChIndexStr.deleteCharAt(downChIndexStr.length() - 1);
                downChFreqStr.deleteCharAt(downChFreqStr.length() - 1);
                downChSnrStr.deleteCharAt(downChSnrStr.length() - 1);
                downChRxPowerStr.deleteCharAt(downChRxPowerStr.length() - 1);
            }
        }
        cmPollResult.setDownChIds(downChIndexStr.toString());
        cmPollResult.setDownChFreqs(downChFreqStr.toString());
        cmPollResult.setDownChSnrs(downChSnrStr.toString());
        cmPollResult.setDownChRxPowers(downChRxPowerStr.toString());
        cmPollResult.setCollectTime(System.currentTimeMillis());
    }

    public CmPollTask getCmPollTask() {
        return cmPollTask;
    }

    public void setCmPollTask(CmPollTask cmPollTask) {
        this.cmPollTask = cmPollTask;
    }

    public CmPollCallback getCmPollCallback() {
        return cmPollCallback;
    }

    public void setCmPollCallback(CmPollCallback cmPollCallback) {
        this.cmPollCallback = cmPollCallback;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam.clone();
    }

    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    private boolean checkPingReachable(String ip) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ip, r);
        //TODO 需要使用数据库配置来进行控制
        //worker.setCount(count);
        //worker.setTimeout(timeout);
        try {
            Future<PingResult> f = pingExecutorService.submit(worker, r);
            return f.get().available();
        } catch (InterruptedException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (ExecutionException e) {
            logger.debug("checkPingReachable" + ip, e);
        } catch (Exception e) {
            logger.debug("checkPingReachable" + ip, e);
        }
        return false;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * 返回3.0CM的注册模式
     * 
     * @param tlv
     * @return -1：未找到注册模式的TLV，0：DOCSIS 1.0， 1：DOCSIS1.1 2：DOCSIS 2.0， 3：DOCSIS 3.0，4-255保留位
     */
    public int getCmDocsisRegVersion(String tlv) {
        List<String[]> list = getValueFromTlv(tlv);
        // 如果最外层不是只有一个TLV，并且该类型不是‘05’则返回异常数据
        if (list.size() != 1 || !list.get(0)[0].equals("05")) {
            return -1;
        }
        List<String[]> tlvList = getValueFromTlv(list.get(0)[1]);
        // 查找DOCSIS 注册模式type = '02'
        for (int i = 0; i < tlvList.size(); i++) {
            if (tlvList.get(i)[0].equals("02")) {
                return Integer.parseInt(tlvList.get(i)[1]);
            }
        }
        return -1;
    }

    /**
     * 解析TLV
     * 
     * @param tlv
     *            以冒号隔开的十六进制数“01:01:01”
     * @return Sting[0]:类型，String[1]值
     */
    public List<String[]> getValueFromTlv(String tlv) {
        List<String[]> list = new ArrayList<String[]>();
        String[] tlvArry = tlv.split(":");
        for (int i = 0; i < tlvArry.length;) {
            String[] s = new String[2];
            s[0] = tlvArry[i];
            int length = Integer.parseInt(tlvArry[i + 1], 16);
            StringBuilder sb = new StringBuilder();
            for (int j = i + 2; j < i + 2 + length; j++) {
                sb.append(tlvArry[j]).append(":");
            }
            s[1] = sb.substring(0, sb.length() - 1);
            list.add(s);
            i = i + 2 + length;
        }
        return list;
    }
}