/***********************************************************************
 * $Id: OperationLogDaoImpl.java,v1.0 2011-11-20 下午05:04:10 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.OperationLogDao;
import com.topvision.platform.domain.OperationLog;

/**
 * @author huqiao
 * @created @2011-11-20-下午05:04:10
 * 
 */
@Repository("operationLogDao")
public class OperationLogDaoImpl extends MyBatisDaoSupport<OperationLog> implements OperationLogDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.MyBatisDaoSupport#getNameSpace()
     */
    @Override
    public String getDomainName() {
        return OperationLog.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.OperationLogDao#getAllOperationLog()
     */
    @Override
    public List<OperationLog> getAllOperationLog() {
        return getSqlSession().selectList(getNameSpace("getAllOperationLog"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.OperationLogDao#getOperationLogByEntityId(java.lang.Long)
     */
    @Override
    public List<OperationLog> getOperationLogByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getOperationLogByEntityId"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.OperationLogDao#getOperationLogByStatus(java.lang.Integer)
     */
    @Override
    public List<OperationLog> getOperationLogByStatus(Integer status) {
        return getSqlSession().selectList(getNameSpace("getOperationLogByStatus"), status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.OperationLogDao#getOperationLogByParams(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<OperationLog> getOperationLogByParams(String entityIp, String clientIp, String operName, String status,
            Integer start, Integer limit) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityIp", entityIp);
        paramMap.put("clientIpAddress", clientIp);
        paramMap.put("operName", operName);
        paramMap.put("status", status);
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("getOperationLogByParams"), paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.dao.OperationLogDao#getOperationLogByParamsCounts(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<OperationLog> getOperationLogByParamsCounts(String entityIp, String clientIp, String operName,
            String status) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityIp", entityIp);
        paramMap.put("clientIpAddress", clientIp);
        paramMap.put("operName", operName);
        paramMap.put("status", status);
        return getSqlSession().selectList(getNameSpace("getOperationLogByParamsCounts"), paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.OperationLogDao#getOperationLogClientIp()
     */
    @Override
    public List<String> getOperationLogClientIp() {
        return getSqlSession().selectList(getNameSpace("getOperationLogClientIp"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.OperationLogDao#getOperationLogUserName()
     */
    @Override
    public List<String> getOperationLogUserName() {
        return getSqlSession().selectList(getNameSpace("getOperationLogUserName"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.OperationLogDao#getOperationLogEntityIp()
     */
    @Override
    public List<String> getOperationLogEntityIp() {
        return getSqlSession().selectList(getNameSpace("getOperationLogEntityIp"));
    }
}
