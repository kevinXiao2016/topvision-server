/***********************************************************************
 * $Id: BatchDeployRecordServiceImpl.java,v1.0 2013年12月13日 下午3:03:36 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.batchdeploy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.batchdeploy.dao.BatchDeployRecordDao;
import com.topvision.ems.batchdeploy.domain.BatchDeployRecord;
import com.topvision.ems.batchdeploy.service.BatchDeployRecordService;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2013年12月13日-下午3:03:36
 *
 */
@Service("batchDeployRecordService")
public class BatchDeployRecordServiceImpl extends BaseService implements BatchDeployRecordService {
    @Autowired
    private BatchDeployRecordDao batchDeployRecordDao;

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.service.BatchDeployRecordService#record(com.topvision.ems.batchdeploy.domain.BatchDeployRecord)
     */
    @Override
    public <T> void recordResult(BatchDeployRecord record) {
        batchDeployRecordDao.recordResult(record);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.service.BatchDeployRecordService#loadRecords(java.util.Map)
     */
    @Override
    public List<BatchDeployRecord> loadRecords(Map<String, Object> condition) {
        return batchDeployRecordDao.loadRecords(condition);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.service.BatchDeployRecordService#loadSuccess(java.lang.Integer)
     */
    @Override
    public BatchDeployRecord loadSuccess(Integer batchDeployId) {
        return batchDeployRecordDao.loadSuccess(batchDeployId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.service.BatchDeployRecordService#loadFailures(java.lang.Integer)
     */
    @Override
    public BatchDeployRecord loadFailures(Integer batchDeployId) {
        return batchDeployRecordDao.loadFailures(batchDeployId);
    }

    @Override
    public int loadRecordCount(Map<String, Object> queryCondition) {
        return batchDeployRecordDao.loadRecordCount(queryCondition);
    }

}
