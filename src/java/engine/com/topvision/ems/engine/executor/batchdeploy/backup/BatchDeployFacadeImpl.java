/***********************************************************************
 * $Id: BatchDeployFacadeImpl.java,v1.0 2013年12月2日 下午3:05:29 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.batchdeploy.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.facade.batchdeploy.BatchDeployExecutor;
import com.topvision.ems.facade.batchdeploy.BatchDeployFacade;
import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpDataProxy;
import com.topvision.framework.snmp.SnmpParam;

/**
 * Facade可以在每配置下发一个后发一个消息给server，server再推送一个消息到前端告诉用户现在配置完成了多个个，失败了多少个
 * @author Bravin
 * @created @2013年12月2日-下午3:05:29
 *
 */
public class BatchDeployFacadeImpl extends EmsFacade implements BatchDeployFacade {
    @Autowired
    private BeanFactory beanFactory;

    private class TargetInfo<T> {
        private T target;
        private SnmpParam snmpParam;

        public T getTarget() {
            return target;
        }

        public void setTarget(T target) {
            this.target = target;
        }

        public SnmpParam getSnmpParam() {
            return snmpParam;
        }

        public void setSnmpParam(SnmpParam snmpParam) {
            this.snmpParam = snmpParam;
        }

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.facade.batchdeploy.BatchDeployFacade#execBatchDeploy(java.util.Map, java.lang.Object, java.lang.String)
     */
    @Override
    public <T extends BatchRecordSupport, V> List<Result<T>> execBatchDeploy(Map<SnmpParam, List<T>> readyMap, V bundle,
            String executor) {
        Map<Long, Result<T>> resultMap = new HashMap<>();
        //由于不方便再新建一个Future类以便存储entityId,target相关的信息，所以使用一个map来存储信息，记录成功失败列表
        Map<TargetInfo<T>, Future<SnmpDataProxy<?>>> futureMap = new HashMap<>();
        for (SnmpParam snmpParam : readyMap.keySet()) {
            List<T> list = readyMap.get(snmpParam);
            for (T target : list) {
                try {
                    BatchDeployExecutor<?, ?> exec = beanFactory.getBean(executor, BatchDeployExecutor.class);
                    //bundle = bundle.
                    //Future<SnmpDataProxy<?>> future = exec.deploy(target, bundle, snmpParam);
                    TargetInfo<T> info = new TargetInfo<>();
                    info.setSnmpParam(snmpParam);
                    info.setTarget(target);
                    //futureMap.put(info, future);
                } catch (Exception e) {
                    logger.error("deploy error:{}", e);
                    Result<T> result = getResult(resultMap, snmpParam.getEntityId());
                    result.addFailure(target);
                }
            }
        }
        for (TargetInfo<T> info : futureMap.keySet()) {
            Future<SnmpDataProxy<?>> future = futureMap.get(info);
            SnmpParam snmpParam = info.getSnmpParam();
            Long entityId = snmpParam.getEntityId();
            T target = info.getTarget();
            Result<T> result = getResult(resultMap, entityId);
            try {
                SnmpDataProxy<?> proxy = future.get();
                if (proxy.getStacktrace() != null) {
                    if (proxy.getStacktrace() instanceof RuntimeException) {
                        throw (RuntimeException) proxy.getStacktrace();
                    } else {
                        throw new SnmpException(proxy.getStacktrace());
                    }
                }
                result.addSuccess(target);
            } catch (Exception e) {
                logger.error("deploy error:{}", e);
                result.addFailure(target);
            }
        }
        return new ArrayList<>(resultMap.values());
    }

    /**
     * 通过entityId获取Result，如果resultMap中国没有该entityId的键，则new一个Result。
     * @FIXME 使用map来获取这里可能存在Result和Map中的对象不一致，需要验证
     * @param resultMap
     * @param entityId
     * @return
     */
    private <T> Result<T> getResult(Map<Long, Result<T>> resultMap, Long entityId) {
        Result<T> result;
        if (resultMap.containsKey(entityId)) {
            result = resultMap.get(entityId);
        } else {
            result = new Result<>();
            result.setEntityId(entityId);
            resultMap.put(entityId, result);
        }
        return result;
    }
}
