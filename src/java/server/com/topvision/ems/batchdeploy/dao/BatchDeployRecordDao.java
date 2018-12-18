/***********************************************************************
 * $Id: BatchDeployRecordDao.java,v1.0 2013年12月13日 下午3:04:31 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.batchdeploy.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.batchdeploy.domain.BatchDeployRecord;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年12月13日-下午3:04:31
 *
 */
public interface BatchDeployRecordDao extends BaseEntityDao<Entity> {
    /**
     * 批量配置记录
     * @param entityId
     * @param operator
     */
    <T> void recordResult(BatchDeployRecord record);

    /**
     * @param condition
     * @return
     */
    List<BatchDeployRecord> loadRecords(Map<String, Object> condition);

    /**
     * 加载成功的目标列表
     * @param batchDeployId
     * @return
     */
    BatchDeployRecord loadFailures(Integer batchDeployId);

    /**
     * 加载成功的目标列表
     * @param batchDeployId
     * @return
     */
    BatchDeployRecord loadSuccess(Integer batchDeployId);

    /**
     * 获取查询记录的总数
     * @param queryCondition
     * @return
     */
    int loadRecordCount(Map<String, Object> queryCondition);
}
