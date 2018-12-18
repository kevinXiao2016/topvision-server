/***********************************************************************
 * $ PerfExecutorService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.common.EngineScheduler;
import com.topvision.ems.engine.common.EngineThreadPool;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.dao.EngineStatisticsDao;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceConstants;
import com.topvision.ems.facade.domain.PerformanceResult;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.facade.exec.ExecScheduler;
import com.topvision.ems.facade.util.LocalFileData;
import com.topvision.exception.engine.NoSuchScheduleException;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 * 
 * 
 * @modify Rod 性能采集整体重构
 * 
 */
@Engine
public class PerfExecutorService extends BaseEngine implements BeanFactoryAware {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${PerfScheduledExecutorService.sleep}")
    private Integer sleep;
    @Autowired
    private EngineScheduler engineScheduler;
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    // 调度线程实例集合
    private ConcurrentHashMap<ScheduleMessage<OperClass>, PerfScheduledFuture> scheduledFutureConcurrentHashMaps = new ConcurrentHashMap<ScheduleMessage<OperClass>, PerfScheduledFuture>();
    private BeanFactory beanFactory;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    @Value("${PerfScheduledExecutorService.poolsize}")
    private int poolSize;
    private ScheduledThreadPoolExecutor realtimeScheduledThreadPoolExecutor;
    @Value("${RealtimePerfScheduledExecutorService.poolsize:10}")
    private int realtimePoolSize;
    @Autowired
    private EngineThreadPool engineThreadPool;
    // 增加标识，如果server没有连接，不进行投递，有些数据需要实时投递给server处理
    private boolean isConnected = false;
    @Value("${ExecScheduler.noFileCache:true}")
    private boolean noFileCache;
    private Map<String, CacheCount> cacheCount;

    @Override
    public void initialize() {
        isConnected = false;
        cacheCount = new HashMap<String, CacheCount>();
        final ThreadGroup group = new ThreadGroup("PerfExecutorService");
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(group, r);
                t.setName("Idle");
                return t;
            }
        });
        final ThreadGroup realtimeGroup = new ThreadGroup("RealtimePerfExecutorService");
        realtimeScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(realtimePoolSize, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(realtimeGroup, r);
                t.setName("Idle");
                return t;
            }
        });
    }

    // Modify by Victor@20151222改为server连接以后才能启用投递线程
    @Override
    public void connected() {
        isConnected = true;
        try {
            // 结果投递线程
            new Thread() {
                public void run() {
                    setName("PerfResultSend");
                    while (true) {
                        if (isConnected) {
                            try {
                                CacheCount count = new CacheCount(System.currentTimeMillis());
                                ConcurrentNavigableMap<String, LocalFileData<PerformanceResult<OperClass>>> instances = LocalFileData
                                        .getAllInstance();
                                Collection<LocalFileData<PerformanceResult<OperClass>>> lfds = instances.values();
                                for (LocalFileData<PerformanceResult<OperClass>> lfd : lfds) {
                                    // jay CM和CPE上下线行为加入了两个缓存队列，但是这两个队列不应该进入性能框架出来，这里就是将这两个队列进行了过滤
                                    if (lfd.getName().equalsIgnoreCase("CmAction")
                                            || lfd.getName().equalsIgnoreCase("CpeAction")) {
                                        continue;
                                    }
                                    PerformanceResult<OperClass> performanceResult = lfd.first();
                                    if (performanceResult != null) {
                                        count.increment();
                                        // Modify by
                                        // Victor@20151222去掉while，原来是为了解决网络问题，现在改为本地，只要获取就删除掉
                                        try {
                                            doResult(performanceResult);
                                            lfd.remove();
                                        } catch (Exception e) {
                                            logger.warn(lfd.getFilename(), e);
                                        }
                                    }
                                }
                                if (cacheCount.containsKey(count.getKey())) {
                                    CacheCount old = cacheCount.get(count.getKey());
                                    if ((old.getDate() != count.getDate())
                                            || old.getCount().longValue() < count.getCount().longValue()) {
                                        old.setTime(count.getTime());
                                        old.setCount(count.getCount());
                                    }
                                } else {
                                    cacheCount.put(count.getKey(), count);
                                }
                                CacheCount last = cacheCount.get("last");
                                if (last == null) {
                                    last = new CacheCount(count.getTime());
                                    last.setKey("last");
                                    cacheCount.put(last.getKey(), last);
                                }
                                last.setTime(count.getTime());
                                last.setCount(count.getCount());
                            } catch (Exception e) {
                                logger.error("", e);
                            }
                        }
                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException e) {
                            logger.error("", e);
                        }
                    }
                }
            }.start();

            // 任务清除线程
            EngineStatisticsDao engineStatisticsDao = engineDaoFactory.getEngineDao(EngineStatisticsDao.class);
            JobDetail job = newJob(PerfExecutorServiceCleanJob.class)
                    .withIdentity("PerfExecutorServiceCleanJob", "Default").build();
            job.getJobDataMap().put("poolSize", poolSize);
            job.getJobDataMap().put("engineId", this.getId());
            job.getJobDataMap().put("engineStatisticsDao", engineStatisticsDao);
            job.getJobDataMap().put("scheduledThreadPoolExecutor", scheduledThreadPoolExecutor);
            job.getJobDataMap().put("realtimeScheduledThreadPoolExecutor", realtimeScheduledThreadPoolExecutor);
            job.getJobDataMap().put("scheduledFutureConcurrentHashMaps", scheduledFutureConcurrentHashMaps);
            TriggerBuilder<SimpleTrigger> builder = newTrigger()
                    .withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(repeatSecondlyForever(300));
            engineScheduler.scheduleJob(job, builder.build());
        } catch (SchedulerException se) {
            logger.error("", se);
        }
        // 调用server端，要求重新发送性能采集
        PerformanceCallback callback = this.getCallback(PerformanceCallback.class);
        callback.restartMonitor(id);

        // 任务标示线程
        // new Thread() {
        // @SuppressWarnings("unchecked")
        // public void run() {
        // while (true) {
        // Iterator it = scheduledFutureConcurrentHashMaps.entrySet().iterator();
        // while (it.hasNext()) {
        // Map.Entry<ScheduleMessage, PerfScheduledFuture> tmp = (Entry<ScheduleMessage,
        // PerfScheduledFuture>) it
        // .next();
        // PerfScheduledFuture future = tmp.getValue();
        // if (future.getOperClass().isTaskCancle()) {
        // future.getFuture().cancel(true);
        // it.remove();
        // }
        // }
        // try {
        // Thread.sleep(60000);
        // } catch (InterruptedException e) {
        // logger.error("", e);
        // }
        // }
        // }
        // }.start();
    }

    /**
     * @param performanceResult
     */
    public void doResult(PerformanceResult<OperClass> performanceResult) throws Exception {
        OperClass operClass = performanceResult.getDomain();
        PerfEngineSaver<PerformanceResult<OperClass>, OperClass> dbSaver = getBean(operClass.getPerfService());
        PerfResultSendJob job = new PerfResultSendJob(performanceResult, dbSaver);
        engineThreadPool.execute(job);
    }

    @Override
    public void disconnected() {
        isConnected = false;
        cacheCount.clear();
    }

    @Override
    public void destroy() {
        isConnected = false;
        // 销毁每一个调度实例
        scheduledThreadPoolExecutor.shutdownNow();
        realtimeScheduledThreadPoolExecutor.shutdownNow();
        // 清除调度实例集合
        scheduledFutureConcurrentHashMaps.clear();
        // scheduleopOperClassConcurrentHashMap.clear();
        cacheCount.clear();
    }

    public Integer getFileCount() {
        int count = 0;
        ConcurrentNavigableMap<String, LocalFileData<PerformanceResult<OperClass>>> instances = LocalFileData
                .getAllInstance();
        Collection<LocalFileData<PerformanceResult<OperClass>>> lfds = instances.values();
        for (LocalFileData<PerformanceResult<OperClass>> lfd : lfds) {
            count += lfd.length();
        }
        return count;
    }

    // 管理接口 接收采集消息
    public Integer invoke(ScheduleMessage<OperClass> message) {
        logger.info("------------------" + message.getMonitorId());
        if (message.getAction() == ScheduleMessage.START) {
            startPerf(message.getSnmpParam(), message);
        } else if (message.getAction() == ScheduleMessage.STOP) {
            stopPerf(message);
        } else if (message.getAction() == ScheduleMessage.RESTART) {
            reStartPerf(message.getSnmpParam(), message);
        } else if (message.getAction() == ScheduleMessage.DELETE) {
            deletePerf(message);
        } else if (message.getAction() == ScheduleMessage.INSERT) {
            insertPerf(message);
        } else if (message.getAction() == ScheduleMessage.UPDATE_START) {
            updateStartPerf(message);
        } else if (message.getAction() == ScheduleMessage.UPDATE_SHUTDOWN) {
            updateShutdownPerf(message);
        }
        return this.id;
    }

    public void clear() {
        for (ScheduleMessage<OperClass> message : scheduledFutureConcurrentHashMaps.keySet()) {
            logger.info("Clear for stop:{}", message);
            stopPerf(message);
        }
    }

    private void reStartPerf(SnmpParam snmpParam, ScheduleMessage<OperClass> message) {
        if (scheduledFutureConcurrentHashMaps.containsKey(message)) {
            // 先关闭原先的任务
            PerfScheduledFuture perfScheduledFuture = scheduledFutureConcurrentHashMaps.get(message);
            OperClass operClass = perfScheduledFuture.getOperClass();
            ScheduledFuture<?> scheduledFuture = perfScheduledFuture.getFuture();
            scheduledFuture.cancel(true);
            // 再开启新设置的任务
            ExecScheduler<OperClass> run = getBean(message.getDomain().getScheduler());
            run.setSnmpParam(snmpParam);
            run.setOperClass(operClass);
            ScheduledFuture<?> newScheduledFuture = getScheduledThreadPoolExecutor(message).scheduleAtFixedRate(run,
                    message.getInitialDelay(), message.getPeriod(), TimeUnit.MILLISECONDS);
            perfScheduledFuture.setFuture(newScheduledFuture);
            scheduledFutureConcurrentHashMaps.put(message, perfScheduledFuture);
            // scheduleopOperClassConcurrentHashMap.put(message, operClass);
        }
    }

    private void stopPerf(ScheduleMessage<OperClass> message) {
        if (scheduledFutureConcurrentHashMaps.containsKey(message)) {
            PerfScheduledFuture perfScheduledFuture = scheduledFutureConcurrentHashMaps.get(message);
            ScheduledFuture<?> exec = perfScheduledFuture.getFuture();
            boolean b = false;
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("ScheduleMessage stop monitor IdentifyKey[" + message.getIdentifyKey() + "] category["
                            + message.getCategory() + "]");
                }
                b = exec.cancel(true);
            } catch (Throwable e) {
                logger.debug("", e);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("ScheduleMessage stop monitor IdentifyKey[" + message.getIdentifyKey() + "] category["
                        + message.getCategory() + "] cancel state[" + b + "] exec isCancel[" + exec.isCancelled()
                        + "] exec isDone[" + exec.isDone() + "]");
            }
            // 从调度实例集合中删除该消息描述的调度实例
            scheduledFutureConcurrentHashMaps.remove(message);
        } else {
            // 不存在消息描述的调度线程则抛出异常
            throw new NoSuchScheduleException();
        }
    }

    private void startPerf(SnmpParam snmpParam, ScheduleMessage<OperClass> message) {
        if (!scheduledFutureConcurrentHashMaps.containsKey(message)) {
            // 如果不存在消息描述的调度线程 则新建一个
            // 获得消息描述的调度执行类实例
            ExecScheduler<OperClass> run = getBean(message.getDomain().getScheduler());
            OperClass operClass = message.getDomain();
            operClass.setMonitorId(message.getMonitorId());
            // 设置snmp采集参数
            run.setSnmpParam(snmpParam);
            run.setOperClass(operClass);
            ScheduledFuture<?> exec = getScheduledThreadPoolExecutor(message).scheduleAtFixedRate(run,
                    message.getInitialDelay(), message.getPeriod(), TimeUnit.MILLISECONDS);
            PerfScheduledFuture perfScheduledFuture = new PerfScheduledFuture(exec, operClass);
            scheduledFutureConcurrentHashMaps.put(message, perfScheduledFuture);
        }
    }

    /**
     * 用于采集中删除对象
     * 
     * @param message
     */
    private void deletePerf(ScheduleMessage<OperClass> message) {
        // delete是用于区分一个采集调度中的采集对象减少的情况，用于区分stop
        // 采集对象的减少并不是stop 因为是在一个采集调度中，所以不能关闭采集调度
        if (scheduledFutureConcurrentHashMaps.containsKey(message)) {
            PerfScheduledFuture perfScheduledFuture = scheduledFutureConcurrentHashMaps.get(message);
            OperClass operClass = perfScheduledFuture.getOperClass();
            if (message.getScheduleType().equals(PerformanceConstants.PERFORMANCE_DOMAIN)) {
                operClass.makeDomains(message.getMonitorId(), message.getDomain().makeObjects(),
                        ScheduleMessage.DELETE);
            } else if (message.getScheduleType().equals(PerformanceConstants.PERFORMANCE_OID)) {
                operClass.makeOids(message.getMonitorId(), message.getDomain().makeOids(), ScheduleMessage.DELETE);
            }
        } else {
            // 不存在消息描述的调度线程则抛出异常
            throw new NoSuchScheduleException();
        }
    }

    /**
     * 用于采集中加入对象
     * 
     * @param message
     */
    private void insertPerf(ScheduleMessage<OperClass> message) {
        if (scheduledFutureConcurrentHashMaps.containsKey(message)) {
            PerfScheduledFuture perfScheduledFuture = scheduledFutureConcurrentHashMaps.get(message);
            OperClass operClass = perfScheduledFuture.getOperClass();
            if (message.getScheduleType().equals(PerformanceConstants.PERFORMANCE_DOMAIN)) {
                operClass.makeDomains(message.getMonitorId(), message.getDomain().makeObjects(),
                        ScheduleMessage.INSERT);
            } else if (message.getScheduleType().equals(PerformanceConstants.PERFORMANCE_OID)) {
                operClass.makeOids(message.getMonitorId(), message.getDomain().makeOids(), ScheduleMessage.STOP);
            }
        } else {
            // 不存在消息描述的调度线程则抛出异常
            throw new NoSuchScheduleException();
        }
    }

    /**
     * 更新任务采集对象(新增采集周期)
     * 
     * @param message
     */
    private void updateStartPerf(ScheduleMessage<OperClass> message) {
        if (scheduledFutureConcurrentHashMaps.containsKey(message)) {
            PerfScheduledFuture perfScheduledFuture = scheduledFutureConcurrentHashMaps.get(message);
            String targetName = message.getTargetName();
            Object data = message.getData();
            perfScheduledFuture.getOperClass().startUpTarget(targetName, data);
        } else {
            // 不存在消息描述的调度线程则抛出异常
            throw new NoSuchScheduleException();
        }
    }

    /**
     * 更新任务采集对象(删除采集指标)
     * 
     * @param message
     */
    private void updateShutdownPerf(ScheduleMessage<OperClass> message) {
        if (scheduledFutureConcurrentHashMaps.containsKey(message)) {
            PerfScheduledFuture perfScheduledFuture = scheduledFutureConcurrentHashMaps.get(message);
            String targetName = message.getTargetName();
            Object data = message.getData();
            perfScheduledFuture.getOperClass().shutdownTarget(targetName, data);
        } else {
            // 不存在消息描述的调度线程则抛出异常
            throw new NoSuchScheduleException();
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getRealtimePoolSize() {
        return realtimePoolSize;
    }

    public void setRealtimePoolSize(int realtimePoolSize) {
        this.realtimePoolSize = realtimePoolSize;
    }

    /**
     * @return the scheduledFutureConcurrentHashMaps
     */
    public ConcurrentHashMap<ScheduleMessage<OperClass>, PerfScheduledFuture> getScheduledFutureConcurrentHashMaps() {
        return scheduledFutureConcurrentHashMaps;
    }

    /**
     * @return the scheduledThreadPoolExecutor
     */
    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }

    /**
     * @return the scheduledThreadPoolExecutor
     */
    public ScheduledThreadPoolExecutor getRealtimeScheduledThreadPoolExecutor() {
        return realtimeScheduledThreadPoolExecutor;
    }

    @SuppressWarnings("unchecked")
    private <T> T getBean(String name) {
        return (T) beanFactory.getBean(name);
    }
    // Add by Victor@20160826 根据性能类别获取处理线程池

    private ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor(ScheduleMessage<OperClass> message) {
        return message.isRealtime() ? realtimeScheduledThreadPoolExecutor : scheduledThreadPoolExecutor;
    }

    public boolean isNoFileCache() {
        return noFileCache;
    }

    public void setNoFileCache(boolean noFileCache) {
        this.noFileCache = noFileCache;
    }

    public Map<String, CacheCount> getCacheCount() {
        return new HashMap<String, CacheCount>(cacheCount);
    }
}
