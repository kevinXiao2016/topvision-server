/***********************************************************************
 * $Id: EponBatchDeployServiceImpl.java,v1.0 2013年12月2日 下午2:24:48 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.batchdeploy.domain.BatchDeployRecord;
import com.topvision.ems.batchdeploy.service.BatchDeployRecordService;
import com.topvision.ems.batchdeploy.service.BatchDeployService;
import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.batchdeploy.service.EponBatchDeployService;
import com.topvision.ems.epon.batchdeploy.service.EponExpressionService;
import com.topvision.ems.facade.batchdeploy.domain.BatchRecordSupport;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.ems.facade.batchdeploy.domain.ResultBundle;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author Bravin
 * @created @2013年12月2日-下午2:24:48
 *
 */
@Service("eponBatchDeployService")
public class EponBatchDeployServiceImpl extends BaseService implements EponBatchDeployService {
    @Autowired
    private EponExpressionService eponExpressionService;
    @Autowired
    private BatchDeployService batchDeployService;
    @Autowired
    private BatchDeployRecordService recordService;

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.batchdeploy.service.EponBatchDeployService#batchDeploy(java.util.List, java.util.List, java.lang.Long, java.lang.Object, java.lang.String)
     */
    @Override
    public <V> ResultBundle<Target> batchDeploy(List<String> targetList, List<String> excludeList, Long entityId,
            V bundle, String executor) {
        Map<Long, List<String>> map = new HashMap<Long, List<String>>();
        map.put(entityId, targetList);
        Map<Long, List<String>> excludeMap = new HashMap<Long, List<String>>();
        excludeMap.put(entityId, excludeList);
        return batchDeploy(map, excludeMap, bundle, executor);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.batchdeploy.service.EponBatchDeployService#batchDeploy(java.util.Map, java.util.Map, java.lang.Object, java.lang.String)
     */
    @Override
    public <V> ResultBundle<Target> batchDeploy(Map<Long, List<String>> multiTargetList,
            Map<Long, List<String>> multiExcludeList, V bundle, String executor) {
        Map<Long, List<Target>> map = eponExpressionService.getBatchDeployTargets(multiTargetList, multiExcludeList);
        return batchDeployService.batchDeploy(map, bundle, executor);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.batchdeploy.service.EponBatchDeployService#batchDeploy(java.util.List, java.util.List, java.lang.Long, java.lang.Object, java.lang.String, java.lang.Integer, java.lang.String)
     */
    @Override
    public <V> ResultBundle<Target> batchDeploy(List<String> targetList, List<String> excludeList, Long entityId,
            V bundle, String executor, Integer typeId, String comment) {
        Map<Long, List<String>> map = new HashMap<Long, List<String>>();
        map.put(entityId, targetList);
        Map<Long, List<String>> excludeMap = new HashMap<Long, List<String>>();
        excludeMap.put(entityId, excludeList);
        ResultBundle<Target> results = batchDeploy(map, excludeMap, bundle, executor);
        List<Target> successlist = new ArrayList<>();
        List<Target> failurelist = new ArrayList<>();
        List<Result<Target>> data = results.getData();
        for (Result<Target> result : data) {
            List<Target> sl = result.getSuccessList();
            List<Target> fl = result.getFailureList();
            successlist.addAll(sl);
            failurelist.addAll(fl);
        }
        BatchDeployRecord record = new BatchDeployRecord();
        String operator = CurrentRequest.getCurrentUser().getUser().getUserName();
        record.setOperator(operator);
        record.setComment(comment);
        record.setTypeId(typeId);
        record.setFailures(failurelist);
        record.setSuccess(successlist);
        record.setDuration(results.getExecuteTime());
        record.setEntityId(entityId);
        record.setMatchList(targetList);
        recordService.recordResult(record);
        return results;
    }

    @Override
    public <T extends BatchRecordSupport, V> ResultBundle<T> batchDeploy(List<T> targetList, Long entityId, V bundle,
            String executor, Integer typeId, String comment) {
        ResultBundle<T> results = batchDeployService.batchDeploy(targetList, entityId, bundle, executor);
        List<T> successlist = new ArrayList<>();
        List<T> failurelist = new ArrayList<>();
        List<Result<T>> data = results.getData();
        for (Result<T> result : data) {
            List<T> sl = result.getSuccessList();
            List<T> fl = result.getFailureList();
            successlist.addAll(sl);
            failurelist.addAll(fl);
        }
        BatchDeployRecord record = new BatchDeployRecord();
        String operator = CurrentRequest.getCurrentUser().getUser().getUserName();
        record.setOperator(operator);
        record.setComment(comment);
        record.setTypeId(typeId);
        record.setFailures(failurelist);
        record.setSuccess(successlist);
        record.setDuration(results.getExecuteTime());
        record.setEntityId(entityId);
        record.setMatchList(null);
        recordService.recordResult(record);
        return results;
    }

}
