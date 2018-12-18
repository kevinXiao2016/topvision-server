/***********************************************************************
 * $Id: CmPollFacadeImpl.java,v1.0 2011-7-1 下午02:56:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.engine;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.topvision.framework.utils.CmcIndexUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cm.cmpoll.config.domain.CmPollCollectParam;
import com.topvision.ems.cm.cmpoll.engine.dao.CmPollHsqlDao;
import com.topvision.ems.cm.cmpoll.engine.service.CmPollStatisticsEngineService;
import com.topvision.ems.cm.cmpoll.facade.CmPollFacade;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPoll3DsRemoteQuery;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPoll3UsRemoteQuery;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollCmRemoteQuery;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollEndTask;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.ems.cm.cmpoll.facade.domain.DocsIf3CmPollUsStatus;
import com.topvision.ems.cm.cmpoll.facade.domain.DownstreamChannelInfo;
import com.topvision.ems.cm.cmpoll.facade.domain.UpstreamChannelInfo;
import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.facade.callback.CmPollCallback;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-7-1-下午02:56:35
 * 
 */
@Facade("cmPollFacade")
public class CmPollFacadeImpl extends BaseEngine implements CmPollFacade, BeanFactoryAware {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;
    private CmPollCallback cmPollCallback;
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private CmPollHsqlDao cmPollHsqlDao;
    @Autowired
    private CmPollStatisticsEngineService cmPollStatisticsEngineService;

    private BeanFactory beanFactory;
    private Integer sendCmCount;
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
    public boolean appendTesk(Long time, List<CmPollTask> cmPollTask) {
        if (getId() == null && !isInitAug) {
            return false;
        }
        for (CmPollTask pollTask : cmPollTask) {
            if (pollTask instanceof CmPollEndTask) {
                continue;
            }
            boolean full = false;
            do {
                try {
                    CmPollThreadPool cmPollThreadPool = (CmPollThreadPool) beanFactory.getBean("cmPollThreadPool");
                    cmPollThreadPool.setTime(time);
                    cmPollThreadPool.setCmPollTask(pollTask);
                    cmPollThreadPool.setEngineId(getId());
                    cmPollThreadPool.setSnmpParam(cmCollectSnmpParam);
                    cmPollThreadPool.setCmPollCallback(cmPollCallback);
                    threadPoolExecutor.execute(cmPollThreadPool);
                    full = false;
                } catch (Exception e) {
                    logger.debug("CmPollFacadeImpl.appendTesk.Exception", e);
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
    public void appendEndTask(Long time, CmPollTask cmPollTask) {
        roundFinished(time);
    }

    @Override
    public void roundStart(Long time) {
        cmPollHsqlDao.createRoundTable(time);
    }

    @Override
    public void roundFinished(final Long time) {
        logger.debug("CmPollFacadeImpl.roundFinished");
        final Integer engineId = getId();
        new Thread() {
            public void run() {
                int activeCount = threadPoolExecutor.getActiveCount();
                while (activeCount != 0) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                    activeCount = threadPoolExecutor.getActiveCount();
                }
                cmPollStatisticsEngineService.startRoundStatistics(time);
                CmPollThreadPool.resetNextResultId();
                int startAt = 1;
                List<CmPollResult> cmPollResults;
                while (true) {
                    cmPollResults = cmPollHsqlDao.readLocalRecords(time, startAt, sendCmCount);
                    logger.trace("send cmPollResults -- " + "size---" + cmPollResults.size() + "------" + cmPollResults.toString());
                    startAt = startAt + cmPollResults.size();
                    if (!cmPollResults.isEmpty()) {
                        cmPollStatisticsEngineService.sendResult(time, cmPollResults);
                    } else {
                        cmPollStatisticsEngineService.completeRoundStatistics(time);
                        cmPollCallback.completeRoundStatistics(engineId, time);
                        break;
                    }
                }
                logger.debug("CmPollFacadeImpl.roundFinished end");
            }
        }.start();
    }

    @Override
    public void heartBeat() {

    }

    @Override
    public void initRunAug(CmPollCollectParam cmPollCollectParam) {
        isInitAug = true;
        this.sendCmCount = cmPollCollectParam.getSendCmCount();
        this.cmCollectSnmpParam = cmPollCollectParam.getCmCollectSnmpParam();
        int maxPoolsize = cmPollCollectParam.getMaxPoolSize();
        threadPoolExecutor = new ThreadPoolExecutor(maxPoolsize, maxPoolsize, maxPoolsize, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(maxPoolsize * 10));

        cmPollCallback = getCallback(CmPollCallback.class);
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

    @Override
    public List<CmPoll3DsRemoteQuery> getCm3DsSignal(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmPoll3DsRemoteQuery.class);
    }

    @Override
    public List<CmPoll3UsRemoteQuery> getCm3UsSignal(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmPoll3UsRemoteQuery.class);
    }

    @Override
    public List<DocsIf3CmPollUsStatus> getDocsIf3CmPollUsStatus(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DocsIf3CmPollUsStatus.class);
    }

    @Override
    public List<UpstreamChannelInfo> getUpstreamChannelInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, UpstreamChannelInfo.class);
    }

    @Override
    public List<DownstreamChannelInfo> getDownstreamChannelInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DownstreamChannelInfo.class);
    }

    @Override
    public List<CmPollCmRemoteQuery> getCmPollCmRemoteQueryList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmPollCmRemoteQuery.class);
    }

    @Override
    public List<DocsIf3CmPollUsStatus> getDocsIf3CmPollUsStatusByCmIndex(SnmpParam snmpParam, Long statusIndex) {
        DocsIf3CmPollUsStatus docsIf3CmPollUsStatus = new DocsIf3CmPollUsStatus();
        docsIf3CmPollUsStatus.setCmRegStatusId(statusIndex);
        return snmpExecutorService.getTableRangeLine(snmpParam, docsIf3CmPollUsStatus, 0, Long.MAX_VALUE);
    }

    @Override
    public CmPollCmRemoteQuery getCmPollCmRemoteQueryByCmIndex(SnmpParam snmpParam, Long statusIndex) {
        CmPollCmRemoteQuery cmPollCmRemoteQuery = new CmPollCmRemoteQuery();
        cmPollCmRemoteQuery.setCmIndex(statusIndex);
        return snmpExecutorService.getTableLine(snmpParam, cmPollCmRemoteQuery);
    }

    @Override
    public List<CmPoll3UsRemoteQuery> getCm3UsSignalByCmIndex(SnmpParam snmpParam, Long statusIndex) {
        CmPoll3UsRemoteQuery cmPoll3UsRemoteQuery = new CmPoll3UsRemoteQuery();
        cmPoll3UsRemoteQuery.setCmIndex(statusIndex);
        return snmpExecutorService.getTableRangeLine(snmpParam, cmPoll3UsRemoteQuery, 0, Long.MAX_VALUE);
    }

    @Override
    public List<CmPoll3DsRemoteQuery> getCm3DsSignalByCmIndex(SnmpParam snmpParam, Long statusIndex) {
        CmPoll3DsRemoteQuery cmPoll3DsRemoteQuery = new CmPoll3DsRemoteQuery();
        cmPoll3DsRemoteQuery.setCmIndex(statusIndex);
        return snmpExecutorService.getTableRangeLine(snmpParam, cmPoll3DsRemoteQuery, 0, Long.MAX_VALUE);
    }

}
