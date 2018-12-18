package com.topvision.ems.cm.pnmp.engine;

import com.topvision.ems.cm.pnmp.engine.dao.PnmpPollHsqlDao;
import com.topvision.ems.cm.pnmp.facade.domain.*;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cm.pnmp.facade.callback.PnmpPollCallback;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Engine
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PnmpPollThreadPool implements Runnable {
    private Logger logger = LoggerFactory.getLogger(PnmpPollThreadPool.class);
    @Autowired
    private SnmpExecutorService snmpExecutorService;
    @Autowired
    private PingExecutorService pingExecutorService;
    @Autowired
    private PnmpPollHsqlDao pnmpPollHsqlDao;
    private PnmpPollTask pnmpPollTask;
    private Integer engineId;
    private SnmpParam snmpParam;
    private PnmpPollCallback pnmpPollCallback;
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
            logger.debug("PnmpPollThreadPool.run pnmpPollTask:" + pnmpPollTask);
        }
        PnmpPollResult pnmpPollResult = new PnmpPollResult();
        Long resultId = nextResultId();
        pnmpPollResult.setResultId(resultId);
        pnmpPollResult.setEntityId(pnmpPollTask.getEntityId());
        pnmpPollResult.setCmcId(pnmpPollTask.getCmcId());
        pnmpPollResult.setCmIp(pnmpPollTask.getCmIp());
        pnmpPollResult.setCmMac(pnmpPollTask.getCmMac());

        if (CmAttribute.isCmOnline(pnmpPollTask.getStatusValue()) || pnmpPollTask.getStatusValue() == null) {
            String ip = pnmpPollTask.getCmIp();
            boolean pingCheck = checkPingReachable(ip);
            pnmpPollResult.setPingCheck(pingCheck);
            if (pingCheck) {
                snmpParam.setIpAddress(pnmpPollResult.getCmIp());
                boolean snmpCheck = checkSnmpReachable(snmpParam);
                pnmpPollResult.setSnmpCheck(snmpCheck);
                if (snmpCheck) {
                    try {
                        List<CmEqualizationData> cmEqualizationDatas = snmpExecutorService.getTable(snmpParam, CmEqualizationData.class);
                        List<CmUpChannel> cmUpChannels = snmpExecutorService.getTable(snmpParam, CmUpChannel.class);
                        if (cmEqualizationDatas != null && !cmEqualizationDatas.isEmpty()) {
                            pnmpPollResult.setEqualizationData(cmEqualizationDatas.get(0).getEqualizationData());
                        } else {
                            pnmpPollResult.setEqualizationData("null");
                        }

                        if (cmUpChannels != null && !cmUpChannels.isEmpty()) {
                            pnmpPollResult.setUpChannelId(cmUpChannels.get(0).getUpChannelId());
                            pnmpPollResult.setUpChannelFreq(cmUpChannels.get(0).getUpChannelFreq());
                            pnmpPollResult.setUpChannelWidth(cmUpChannels.get(0).getUpChannelWidth());
                        } else {
                            pnmpPollResult.setUpChannelId(-1);
                            pnmpPollResult.setUpChannelFreq(-1L);
                            pnmpPollResult.setUpChannelWidth(-1L);
                        }

                        if (pnmpPollTask instanceof PnmpPollMiddleSpeedTask
                                || pnmpPollTask instanceof PnmpPollHighSpeedTask) {
                            logger.debug("pnmpPollTask go into Middle or High speed: "+pnmpPollResult );
                            if (cmUpChannels != null && !cmUpChannels.isEmpty()) {
                                pnmpPollResult.setUpTxPower(cmUpChannels.get(0).getUpTxPower());
                            }
                            try {
                                List<CmDownChannel> cmDownChannels = snmpExecutorService.getTable(
                                        snmpParam, CmDownChannel.class);
                                if (cmDownChannels != null && !cmDownChannels.isEmpty()) {
                                    pnmpPollResult.setDownRxPower(cmDownChannels.get(0).getDocsIfDownChannelPower());
                                }
                                if (cmDownChannels != null && !cmDownChannels.isEmpty()) {
                                    pnmpPollResult.setDownSnr(cmDownChannels.get(0).getDocsIfSigQSignalNoise());
                                }
                            } catch (Exception e) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Collect DownRxPower and DownSnr " + ip, e);
                                }
                            }
                            try {
                                SnmpParam cmtsSnmpParam = pnmpPollTask.getCmtsSnmpParam();
                                CMMacToIndex cmMacToIndex = new CMMacToIndex();
                                cmMacToIndex.setMac(new PhysAddress(pnmpPollTask.getCmMac()));
                                cmMacToIndex = snmpExecutorService.getTableLine(
                                        cmtsSnmpParam, cmMacToIndex);
                                CmtsCm cmtsCm = new CmtsCm();
                                cmtsCm.setStatusIndex(cmMacToIndex.getCmIndex());
                                if (cmMacToIndex != null && cmMacToIndex.getCmIndex() != null) {
                                    Long cmIndex = cmMacToIndex.getCmIndex();
                                    pnmpPollResult.setCmIndex(cmIndex);
                                    pnmpPollResult.setCmcIndex(CmcIndexUtils.getCmcIndexFromCmIndex(cmIndex));
                                    cmtsCm = snmpExecutorService.getTableLine(
                                            cmtsSnmpParam, cmtsCm);
                                    if (cmtsCm != null) {
                                        pnmpPollResult.setUpSnr(cmtsCm.getStatusSignalNoise());
                                        pnmpPollResult.setStatusValue(cmtsCm.getStatusValue());
                                        pnmpPollResult.setCheckStatus(PnmpPollResult.CHECKSUCCESS);
                                    } else {
                                        pnmpPollResult.setStatusValue(1);
                                        pnmpPollResult.setCheckStatus(PnmpPollResult.CHECKSNMPFALSE);
                                    }
                                } else {
                                    pnmpPollResult.setCmIndex(-1L);
                                    pnmpPollResult.setCmcIndex(-1L);
                                    pnmpPollResult.setCheckStatus(PnmpPollResult.CHECKSNMPFALSE);
                                    pnmpPollResult.setStatusValue(1);
                                }


                            } catch (Exception e) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Collect UpSnr " + ip, e);
                                }
                                pnmpPollResult.setCmIndex(-1L);
                                pnmpPollResult.setCmcIndex(-1L);
                                pnmpPollResult.setCheckStatus(PnmpPollResult.CHECKSNMPFALSE);
                                pnmpPollResult.setStatusValue(1);
                            }
                        } else {
                            logger.debug("pnmpPollTask go into low speed: "+pnmpPollResult );
                            pnmpPollResult.setCmcIndex(pnmpPollTask.getCmcIndex());
                            pnmpPollResult.setCmIndex(pnmpPollTask.getStatusIndex());
                            pnmpPollResult.setCheckStatus(PnmpPollResult.CHECKSUCCESS);
                            pnmpPollResult.setStatusValue(pnmpPollTask.getStatusValue());
                        }

                        logger.info("pnmpPollResult = " + pnmpPollResult);
                        pnmpPollHsqlDao.saveLocalRecord(time, pnmpPollResult, pnmpPollTask);
                    } catch (Exception e) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Collect EqualizationData " + ip, e);
                        }
                    }
                } else {
                    pnmpPollResult.setCmIndex(-1L);
                    pnmpPollResult.setCmcIndex(-1L);
                    pnmpPollResult.setCheckStatus(PnmpPollResult.CHECKSNMPFALSE);
                    pnmpPollResult.setStatusValue(1);
                    pnmpPollHsqlDao.saveLocalRecord(time, pnmpPollResult,pnmpPollTask);
                }
            } else {
                pnmpPollResult.setCmIndex(-1L);
                pnmpPollResult.setCmcIndex(-1L);
                pnmpPollResult.setCheckStatus(PnmpPollResult.CHECKPINGFALSE);
                pnmpPollResult.setStatusValue(1);
                pnmpPollHsqlDao.saveLocalRecord(time, pnmpPollResult, pnmpPollTask);
            }
        } else {
            pnmpPollResult.setCmIndex(-1L);
            pnmpPollResult.setCmcIndex(-1L);
            pnmpPollResult.setCheckStatus(PnmpPollResult.CHECKPINGFALSE);
            pnmpPollResult.setStatusValue(1);
            pnmpPollHsqlDao.saveLocalRecord(time, pnmpPollResult, pnmpPollTask);
        }
        getPnmpPollCallback().completeTask(engineId, pnmpPollTask.getTaskId());
        logger.debug("PnmpPollThreadPool.run end");
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

    public PnmpPollTask getPnmpPollTask() {
        return pnmpPollTask;
    }

    public void setPnmpPollTask(PnmpPollTask pnmpPollTask) {
        this.pnmpPollTask = pnmpPollTask;
    }

    public PnmpPollCallback getPnmpPollCallback() {
        return pnmpPollCallback;
    }

    public void setPnmpPollCallback(PnmpPollCallback pnmpPollCallback) {
        this.pnmpPollCallback = pnmpPollCallback;
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

}