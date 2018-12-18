/***********************************************************************
 * $Id: BatchDeployRecordDaoImpl.java,v1.0 2013年12月13日 下午3:05:12 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.batchdeploy.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.batchdeploy.dao.BatchDeployRecordDao;
import com.topvision.ems.batchdeploy.domain.BatchDeployRecord;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2013年12月13日-下午3:05:12
 *
 */
@Repository("batchDeployRecordDao")
public class BatchDeployRecordDaoImpl extends MyBatisDaoSupport<Entity> implements BatchDeployRecordDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.batchdeploy.domain.BatchDeploy";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.dao.BatchDeployRecordDao#recordResult(com.topvision.ems.batchdeploy.domain.BatchDeployRecord)
     */
    @Override
    public <T> void recordResult(BatchDeployRecord record) {
        //插入记录并且设置最新的batchId到record对象中，以便插入到详细表时使用
        getSqlSession().insert(getNameSpace("insertRecord"), record);
        getSqlSession().insert(getNameSpace("insertDetail"), record);

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.dao.BatchDeployRecordDao#loadRecords(java.util.Map)
     */
    @Override
    public List<BatchDeployRecord> loadRecords(Map<String, Object> condition) {
        return getSqlSession().selectList(getNameSpace("selectRecords"), condition);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.dao.BatchDeployRecordDao#loadFailures(java.lang.Integer)
     */
    @Override
    public BatchDeployRecord loadFailures(Integer batchDeployId) {
        return getSqlSession().selectOne(getNameSpace("selectFailure"), batchDeployId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.batchdeploy.dao.BatchDeployRecordDao#loadSuccess(java.lang.Integer)
     */
    @Override
    public BatchDeployRecord loadSuccess(Integer batchDeployId) {
        return getSqlSession().selectOne(getNameSpace("selectSuccess"), batchDeployId);
    }

    @Override
    public int loadRecordCount(Map<String, Object> queryCondition) {
        return getSqlSession().selectOne(getNameSpace("selectRecordCount"), queryCondition);
    }

}
