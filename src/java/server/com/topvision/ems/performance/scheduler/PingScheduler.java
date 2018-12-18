/***********************************************************************
  * $Id: PingScheduler.java,v1.0 2014-1-7 上午9:52:46 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.scheduler;

import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.performance.domain.PingParam;
import com.topvision.ems.performance.domain.PingPerf;
import com.topvision.ems.performance.engine.PingParamDao;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.exception.engine.PingException;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.ping.PingResult;
import com.topvision.framework.ping.PingWorker;

/**
 * @author Rod John
 * @created @2014-1-7-上午9:52:46
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("pingScheduler")
@Deprecated
public class PingScheduler extends AbstractExecScheduler<PingPerf> {
    @Autowired
    private PingExecutorService pingExecutorService;
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("PingScheduler  entityId[" + operClass.getEntityId() + "] exec start.");
        }
        try {
            long entityId = operClass.getEntityId();
            Timestamp collectTime = new Timestamp(System.currentTimeMillis());
            // Add by Rod 增加在Engine端回调记录性能任务执行时间
            try {
                if (operClass.getMonitorId() != null) {
                    getCallback().recordTaskCollectTime(operClass.getMonitorId(), collectTime);
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            Integer delay = -1;
            delay = ping(operClass.getIpAddress());
            com.topvision.ems.performance.domain.PingResult pingResult = new com.topvision.ems.performance.domain.PingResult(
                    operClass);
            pingResult.setEntityId(entityId);
            pingResult.setDelay(delay);
            pingResult.setCollectTime(collectTime);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(pingResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("PingScheduler exec end.");
    }

    private int ping(String ipAddress) {
        PingResult r = new PingResult();
        PingWorker worker = new PingWorker(ipAddress, r);
        PingParamDao pingParamDao = engineDaoFactory.getEngineDao(PingParamDao.class);
        List<PingParam> pingParamList = pingParamDao.queryPingParamByModule("ping");
        Properties props = new Properties();
        for (PingParam pref : pingParamList) {
            if (pref != null && pref.getName() != null && pref.getValue() != null) {
                props.put(pref.getName(), pref.getValue());
            }
        }
        worker.setTimeout(Integer.parseInt(props.getProperty("Ping.timeout")));
        worker.setCount(Integer.parseInt(props.getProperty("Ping.count")));
        worker.setRetry(Integer.parseInt(props.getProperty("Ping.retry")));
        Future<PingResult> f = pingExecutorService.submit(worker, r);
        try {
            int value = f.get().getResult();
            if (value > 0) {
                return value;
            } else if (value == 0) {
                return 1;
            }
        } catch (InterruptedException e) {
            logger.debug(e.toString(), e);
            throw new PingException(e);
        } catch (ExecutionException e) {
            throw new PingException(e);
        }
        return -1;
    }
}
