/***********************************************************************
 * $Id: BatchDeployServiceImpl.java,v1.0 2013年11月30日 下午2:58:57 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.batchdeploy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.batchdeploy.service.BatchDeployService;
import com.topvision.ems.facade.batchdeploy.BatchDeployFacade;
import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.ems.facade.batchdeploy.domain.ResultBundle;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Bravin
 * @created @2013年11月30日-下午2:58:58
 *
 */
@Service("batchDeployService")
public class BatchDeployServiceImpl extends BaseService implements BatchDeployService {
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.service.BatchDeployService#batchDeploy(java.util.List, java.lang.Long, java.lang.Object, com.topvision.ems.facade.batchDeploy.BatchDeployExecutor)
     */
    @Override
    public <T extends BatchRecordSupport, V> ResultBundle<T> batchDeploy(List<T> targetList, Long entityId, V bundle,
            String executor) {
        Map<Long, List<T>> map = new HashMap<Long, List<T>>();
        map.put(entityId, targetList);
        return batchDeploy(map, bundle, executor);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.service.BatchDeployService#batchDeploy(java.util.Map, java.lang.Object, com.topvision.ems.facade.batchDeploy.BatchDeployExecutor)
     */
    @Override
    public <T extends BatchRecordSupport, V> ResultBundle<T> batchDeploy(Map<Long, List<T>> multiEntityList, V bundle,
            String executor) {
        ResultBundle<T> resultBundle = new ResultBundle<T>();
        Long startTime = System.currentTimeMillis();
        /****此处支持多个设备的并行下发，当处于engine分布式部署时，不同的engine可以处理不同的设备配置下发，当然，也可能多个设备在同一个engine上下发，
         *   所以这里既不能对设备进行串行的处理，也不能对每一个设备各自开线程进行并行处理，这里采用如下策略：
         *   1. 求解上各自engine对应的需要下发的设备映射关系表
         *   2. 对每一个engine各自开启一个线程进行并发下载
         *   3. 在engine内部，对待下发的设备进行串行下发
         ***/
        List<String> ips = new ArrayList<String>();
        List<String> notExistedEntitys = new ArrayList<String>();
        Map<String, SnmpParam> ip2IdMaps = new HashMap<String, SnmpParam>();
        for (Long entityId : multiEntityList.keySet()) {
            SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
            String ip = snmpParam.getIpAddress();
            //CC8800A有entityId，但是没有ip，防止用户批量下发错误，故检测
            if (ip != null) {
                //add by bravin:20131130其实这里ips用 ip2IdMaps.keySet()就可以解决问题了
                ips.add(ip);
                ip2IdMaps.put(ip, snmpParam);
            } else {
                notExistedEntitys.add(ip);
            }
        }
        //TODO 网管会有多个engineserver，每个enginesever可以处理多个设备.如果让单个engineserver内部串行处理各设备，浪费时间，不如各设备间都并行处理
        //但是如果同时让所有设备都执行的话不方便控制，可能会导致engineserver CPU突然剧增，并持续很久时间，如果在单个engineserver内部做控制的话，可以限制每次至多操作多少个设备
        //Modify by Victor@20150418 采用ZooKeeper框架，zookeeper负责负载均衡处理，不在业务部分进行负载均衡处理
        List<BatchDeployTask<List<Result<T>>>> futureList = new ArrayList<>();
        Map<SnmpParam, List<T>> readyMap = new HashMap<SnmpParam, List<T>>();
        List<Long> ids = new ArrayList<>();
        for (String ip : ips) {
            SnmpParam snmpParam = ip2IdMaps.get(ip);
            readyMap.put(snmpParam, multiEntityList.get(snmpParam.getEntityId()));
            ids.add(snmpParam.getEntityId());
        }
        BatchDeployTask<List<Result<T>>> future = batchDeployInEngineServer(readyMap, bundle, executor, ids);
        futureList.add(future);

        /**由于批量配置可以多设备一起下发,考虑到时间较长,用户体验较差,所以此处可以在某一个engine下的设备下发完成时,发送一个消息给用户告诉他部分设备下发完毕。
         * 此处采用如下的算法：
         * 1.  每过1秒询问所有futureTask是否完成，如果完成或者取消了则删除，合并各engineserver的操作结果
         * 2.  如果所有futureTask都执行完毕了，则表示整个任务执行完毕
         */
        List<Result<T>> data = new ArrayList<>();
        List<Long> canceledList = new ArrayList<>();
        while (futureList.size() > 0) {
            //由于此处存在删除的情况,考虑到数组的安全性，所以从后往前删
            for (int i = futureList.size() - 1; i > -1; i--) {
                try {
                    BatchDeployTask<List<Result<T>>> futureTask = futureList.get(i);
                    if (futureTask.isDone()) {
                        futureList.remove(i);
                        data.addAll(futureTask.get());
                        //此处发消息到前端
                    } else if (futureTask.isCancelled()) {
                        futureList.remove(i);
                        //如果任务被取消的话，有可能其中的部分配置已经下发,所以可以记录下设备的ID列表,然后告诉调用者,哪些设备配置失败,需要刷新设备
                        canceledList.addAll(futureTask.getEntityIds());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("", e);
                    futureList.remove(i);
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        Long executeTime = System.currentTimeMillis() - startTime;
        resultBundle.setExecuteTime(executeTime);
        resultBundle.setData(data);
        resultBundle.setCanceledList(canceledList);
        return resultBundle;
    }

    /**
     * 每一个engineserver就是一个FutureTask
     * @param engineServer
     * @param readyMap
     * @param bundle
     * @param executor
     */
    private <T extends BatchRecordSupport, V> BatchDeployTask<List<Result<T>>> batchDeployInEngineServer(
            final Map<SnmpParam, List<T>> readyMap, final V bundle, final String executor, List<Long> entityIds) {
        final BatchDeployFacade batchDeployFacade = facadeFactory.getFacade(BatchDeployFacade.class);
        BatchDeployTask<List<Result<T>>> future = new BatchDeployTask<>(new Callable<List<Result<T>>>() {

            @Override
            public List<Result<T>> call() throws Exception {
                return batchDeployFacade.execBatchDeploy(readyMap, bundle, executor);
            }
        });
        future.setEntityIds(entityIds);
        new Thread(future).start();
        return future;
    }

    private class BatchDeployTask<T> extends FutureTask<T> {
        private List<Long> entityIds;

        /**
         * @param callable
         */
        public BatchDeployTask(Callable<T> callable) {
            super(callable);
        }

        public List<Long> getEntityIds() {
            return entityIds;
        }

        public void setEntityIds(List<Long> entityIds) {
            this.entityIds = entityIds;
        }

    }
}
