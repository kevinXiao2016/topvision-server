/***********************************************************************
 * $Id: BatchDeployWorker.java,v1.0 2013年11月30日 下午7:52:33 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.batchdeploy;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.batchdeploy.BatchDeployExecutor;
import com.topvision.ems.facade.batchdeploy.domain.Reason;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 如果每一个设备都是单线程下发的话，那么会有大量的时间耗费在调用executor,线程池调度，PDU组装与下发，设备接受请求的过程中
 * 采用多线程同时下发同一设备，虽然设备是单线程处理，但是在处理某一个SNMP任务的时候，网管另外一个线程就可以建立连接直接等待设备执行任务而无需浪费时间在建立连接等步骤上
 * 2个线程一般就可以满足我们的需求，如果设备处理非常快，可以适当调优，增加并行线程数
 * @author Bravin
 * @created @2013年11月30日-下午7:52:33
 *
 */
public class BatchDeployWorker<T, V> implements Runnable {
    protected final static Logger logger = LoggerFactory.getLogger(BatchDeployWorker.class);
    public static int MAX_PARALLEL_WORKER_NUM = 2;
    private List<T> targets;
    private V bundle;
    private BatchDeployExecutor<T, V> executor;
    private SnmpParam snmpParam;
    private Long entityId;
    private volatile Result<T> result;
    private AtomicInteger counter;
    private final ReentrantLock rtLock = new ReentrantLock();
    private final Condition condition = rtLock.newCondition();

    private class ParallelInnerWoker extends Thread {

        @Override
        public void run() {
            T target = null;
            int retry = 0;
            for (;;) {
                if (retry == 1) {
                    retry++;
                } else {
                    synchronized (targets) {
                        //只有同步块里面
                        if (targets.isEmpty()) {
                            break;
                        }
                        target = targets.remove(0);
                    }
                }
                try {
                    //调用单个目标的下发
                    if (executor.deploy(target, bundle, snmpParam)) {
                        result.addSuccess(target);
                    } else {
                        setReason(target, Reason.SERVICE_CONFLICT);
                        result.addFailure(target);
                    }
                } catch (SnmpNoResponseException e) {
                    //此部分为临时处理SNMP4J在首次下发设备时，SNMP请求不能下发导致超时的问题，所以这里做下特殊处理
                    retry++;
                    if (retry != 1) {
                        logger.error("deploy target error:{}", e);
                        setReason(target, Reason.SNMP_NO_RESPONSE);
                    }
                } catch (SnmpSetException e) {
                    logger.error("deploy target error:{}", e);
                    setReason(target, Reason.SERVICE_CONFLICT);
                    result.addFailure(target);
                } catch (SnmpException e) {
                    logger.error("deploy target error:{}", e);
                    setReason(target, Reason.LINK_ERROR);
                    result.addFailure(target);
                } catch (Exception e) {
                    logger.error("deploy target error:{}", e);
                    setReason(target, Reason.INTERVAL_ERROR);
                    result.addFailure(target);
                }
            }
            //只有当所有的线程都执行完毕后,counter才会0
            if (counter.decrementAndGet() == 0) {
                rtLock.lock();
                try {
                    condition.signalAll();
                    rtLock.unlock();
                } catch (Exception e) {
                    logger.error("signal error:{}", e);
                }
            }
        }

    }

    
    /**
     * 设置配置失败原因
     * @param target
     * @param reason
     */
    public void setReason(T target, int reason) {
        Field field;
        try {
            field = target.getClass().getSuperclass().getDeclaredField("reason");
            field.setAccessible(true);
            field.setInt(target, reason);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            logger.error("", e);
        }
    };
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        rtLock.lock();
        result.setEntityId(entityId);
        counter = new AtomicInteger(MAX_PARALLEL_WORKER_NUM);
        //snmpParam.setTimeout(snmpParam.getTimeout() * MAX_PARALLEL_WORKER_NUM);
        for (int i = 0; i < MAX_PARALLEL_WORKER_NUM; i++) {
            new ParallelInnerWoker().start();
        }
        try {
            if (counter.get() > 0) {
                condition.await();
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            rtLock.unlock();
        }
    }

    public List<T> getTargets() {
        return targets;
    }

    public void setTargets(List<T> targets) {
        this.targets = targets;
    }

    public V getBundle() {
        return bundle;
    }

    public void setBundle(V bundle) {
        this.bundle = bundle;
    }

    public BatchDeployExecutor<T, V> getExecutor() {
        return executor;
    }

    public void setExecutor(BatchDeployExecutor executor) {
        this.executor = executor;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.entityId = snmpParam.getEntityId();
        this.snmpParam = snmpParam;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Result<T> getResult() {
        return result;
    }

    public void setResult(Result<T> result) {
        this.result = result;
    }



}