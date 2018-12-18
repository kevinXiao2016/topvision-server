/***********************************************************************
 * $Id: BatchDeployRecordService.java,v1.0 2013年12月13日 下午2:04:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.batchdeploy.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.batchdeploy.domain.BatchDeployRecord;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年12月13日-下午2:04:37
 *
 */
public interface BatchDeployRecordService extends Service {

    /**
     * 批量配置记录
     * @param entityId
     * @param operator
     */
    <T> void recordResult(BatchDeployRecord record);

    /**
     * 查询批量配置记录
     * @param condition
     * @return
     */
    List<BatchDeployRecord> loadRecords(Map<String, Object> condition);

    /**
     * 展示成功的目标列表
     * @param batchDeployId
     * @return
     */
    BatchDeployRecord loadSuccess(Integer batchDeployId);

    /**
     * 展示失败的目标列表
     * @param batchDeployId
     * @return
     */
    BatchDeployRecord loadFailures(Integer batchDeployId);

    /**
     * 获取查询记录的总数
     * @param queryCondition
     * @return
     */
    int loadRecordCount(Map<String, Object> queryCondition);

}
