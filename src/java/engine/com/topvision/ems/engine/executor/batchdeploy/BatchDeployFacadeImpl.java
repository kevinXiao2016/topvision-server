/***********************************************************************
 * $Id: BatchDeployFacadeImpl.java,v1.0 2013年11月30日 下午7:40:01 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.batchdeploy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.facade.batchdeploy.BatchDeployExecutor;
import com.topvision.ems.facade.batchdeploy.BatchDeployFacade;
import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 
 * 采用批量配置框架来完成各设备中的请求下发调度，没有采用让snmpThreadPoolExecutor来管理，原因是
 * 1. 设备是单线程的，同时只能处理一个SNMP请求，所以网管一次丢太多的snmpworker下去会导致SNMP超时
 * 2. 如果在coreThread同时只允许一个设备的snmpworker run起来，所以必然需要在worker中等待该设备的锁被释放，那么该线程就被堵塞了
 * 3. 如果不在worker中使用锁，那么blockQuene中snmpworker将高频率的移动
 * 4. 如果采用各个设备维持一个列队，那么对于非批量的任务下发时将会被堵塞，被执行的时间将大大延长
 * 5. 所以采用在批量配置框架中完成调度，同一个设备同时只会下发一个SNMP请求，非批量配置的请求会被快速执行
 * 批量配置的coreSize和maxSize没有配置成和snmpThreadPoolExecutor中一致，相对较小，而queneSize远远大于snmpThreadPoolExecutor
 * 表示批量配置同时能支持10个设备最大20个设备的下发，如果单次配置多于20个设备，那么会有部分设备在排队。
 * 批量配置最多能接受的配置的的设备数量为：
 * queueSize + maximumPoolSize = 1044
 * 批量配置的线程池是可以超时的，当超过  keepAliveTime*TimeUnit.SECONDS 的时间后,如果没有排队的线程，那么运行着的线程数会减少直到0个
 * @author Bravin
 * @created @2013年11月30日-下午7:40:01
 *
 */
@Engine("batchDeployFacade")
public class BatchDeployFacadeImpl extends EmsFacade implements BatchDeployFacade {
    @Autowired
    private BeanFactory beanFactory;
    private ThreadPoolExecutor threadPoolExecutor;
    @Value("${batchdeploy.corePoolSize}")
    private Integer corePoolSize;
    @Value("${batchdeploy.maximumPoolSize}")
    private Integer maximumPoolSize;
    @Value("${batchdeploy.keepAliveTime}")
    private Integer keepAliveTime;
    @Value("${batchdeploy.queueSize}")
    private Integer queueSize;
    @Value("${batchdeploy.maxParallelWorkerSize}")
    private Integer maxParallelWorkerSize;

    @PostConstruct
    public void initialize() {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(queueSize));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
    }


    /* (non-Javadoc)
     * @see com.topvision.ems.facade.batchdeploy.BatchDeployFacade#execBatchDeploy(java.util.Map, java.lang.Object, com.topvision.ems.facade.batchdeploy.BatchDeployExecutor)
     */
    @Override
    public <T extends BatchRecordSupport, V> List<Result<T>> execBatchDeploy(Map<SnmpParam, List<T>> readyMap, V bundle,
            String executor) {
        List<Result<T>> resultList = new ArrayList<>();
        //由于不方便再新建一个Future类以便存储entityId,target相关的信息，所以使用一个map来存储信息，记录成功失败列表
        List<FutureWrapper<T>> futures = new ArrayList<>();
        Set<SnmpParam> keySet = readyMap.keySet();
        BatchDeployExecutor<?, ?> instance = beanFactory.getBean(executor, BatchDeployExecutor.class);
        for (SnmpParam snmpParam : keySet) {
            Long entityId = snmpParam.getEntityId();
            List<T> targets = readyMap.get(snmpParam);
            BatchDeployWorker<T, V> worker = new BatchDeployWorker<T, V>();
            worker.setBundle(bundle);
            worker.setEntityId(entityId);
            worker.setExecutor(instance);
            worker.setTargets(targets);
            worker.setSnmpParam(snmpParam);
            Result<T> result = new Result<>();
            worker.setResult(result);
            Future<Result<T>> future = threadPoolExecutor.submit(worker, result);
            futures.add(new FutureWrapper<T>(future, entityId, targets));
        }

        for (FutureWrapper<T> wrapper : futures) {
            Future<Result<T>> future = wrapper.getFuture();
            Long entityId = wrapper.getEntityId();
            try {
                Result<T> proxy = future.get();
                resultList.add(proxy);
            } catch (Exception e) {
                logger.error("deploy error:{}", e);
                Result<T> result = new Result<T>();
                result.setEntityId(entityId);
                result.setFailureList(wrapper.getTargets());
                resultList.add(result);
            }
        }
        return resultList;
    }

    /**
     * Future的包装类，方便获取future对应的属性
     * @param <T>
     */
    private class FutureWrapper<T> {
        private final Future<Result<T>> future;
        private final Long entityId;
        private final List<T> targets;

        /**
         * @param future2
         * @param entityId2
         * @param targets2
         */
        public FutureWrapper(Future<Result<T>> future, Long entityId, List<T> targets) {
            this.future = future;
            this.entityId = entityId;
            this.targets = targets;
        }

        public Future<Result<T>> getFuture() {
            return future;
        }

        public Long getEntityId() {
            return entityId;
        }

        public List<T> getTargets() {
            return targets;
        }

    }


    public void setMaxParallelWorkerSize(Integer maxParallelWorkerSize) {
        this.maxParallelWorkerSize = maxParallelWorkerSize;
        BatchDeployWorker.MAX_PARALLEL_WORKER_NUM = maxParallelWorkerSize;
    }

}
