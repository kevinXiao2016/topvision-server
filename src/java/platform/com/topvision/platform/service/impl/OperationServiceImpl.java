/***********************************************************************
 * $Id: OperationServiceImpl.java,v1.0 2011-11-20 下午04:41:50 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.service.BaseService;
import com.topvision.platform.action.LogInterceptor;
import com.topvision.platform.dao.OperationLogDao;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.service.OperationService;

/**
 * @author huqiao
 * @created @2011-11-20-下午04:41:50
 * 
 */
@Service("operationService")
public class OperationServiceImpl extends BaseService implements OperationService {
    @Autowired
    private LogInterceptor logInterceptor;
    @Autowired
    private OperationLogDao operationLogDao;

    /**
     * @return the operationLogDao
     */
    public OperationLogDao getOperationLogDao() {
        return operationLogDao;
    }

    /**
     * @param operationLogDao
     *            the operationLogDao to set
     */
    public void setOperationLogDao(OperationLogDao operationLogDao) {
        this.operationLogDao = operationLogDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.OperationService#insertOperationLog(com.topvision.platform
     * .domain.OperationLog)
     */
    @Override
    public void insertOperationLog(OperationLog operationLog) {
        operationLogDao.insertEntity(operationLog);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.OperationService#getAllOperationLog()
     */
    @Override
    public List<OperationLog> getAllOperationLog() {
        return operationLogDao.getAllOperationLog();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.OperationService#getOperationLogByEntityId(java.lang.Long)
     */
    @Override
    public List<OperationLog> getOperationLogByEntityId(Long entityId) {
        return operationLogDao.getOperationLogByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.OperationService#getOperationLogByStatus(java.lang.Integer)
     */
    @Override
    public List<OperationLog> getOperationLogByStatus(Integer status) {
        return operationLogDao.getOperationLogByStatus(status);
    }

    
    /**
     * 获得设备替换指定的日志内容
     * 
     * @param mac
     * @param typeName
     * @param version
     * @param clazz
     * @return
     */
    @Override
    public String getEntityReplaceLog(String mac, String typeName, String version, Class clazz) {
        String replace = logInterceptor.getString("ReplaceEntity", clazz);
        String type = logInterceptor.getString("Type", clazz);
        String ver = logInterceptor.getString("Version", clazz);
        // Log 
        StringBuilder log = new StringBuilder(replace);
        // Mac
        log.append("[Mac:").append(mac);
        // Type
        log.append(",").append(type).append(":").append(typeName);
        // Version
        log.append(",").append(ver).append(":").append(version).append("]");
        return log.toString();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.OperationService#getOperationLogByParams(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<OperationLog> getOperationLogByParams(String entityIp, String clientIp, String operName, String status,
            Integer start, Integer limit) {
        if (entityIp == null || entityIp.length() == 0) {
            entityIp = null;
        }
        if (clientIp == null || clientIp.length() == 0) {
            clientIp = null;
        }
        if (operName == null || operName.length() == 0) {
            operName = null;
        }
        if (status == null || status.length() == 0 || "0".equals(status)) {
            status = null;
        }
        return operationLogDao.getOperationLogByParams(entityIp, clientIp, operName, status, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.OperationService#getOperationLogByParamsCounts(java.lang.String
     * , java.lang.String, java.lang.String)
     */
    @Override
    public List<OperationLog> getOperationLogByParamsCounts(String entityIp, String clientIp, String operName,
            String status) {
        if (entityIp == null || entityIp.length() == 0) {
            entityIp = null;
        }
        if (clientIp == null || clientIp.length() == 0) {
            clientIp = null;
        }
        if (operName == null || operName.length() == 0) {
            operName = null;
        }
        if (status == null || status.length() == 0 || "0".equals(status)) {
            status = null;
        }
        return operationLogDao.getOperationLogByParamsCounts(entityIp, clientIp, operName, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.OperationService#getOperationLogClientIp()
     */
    @Override
    public List<String> getOperationLogClientIp() {
        return operationLogDao.getOperationLogClientIp();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.OperationService#getOperationLogUserName()
     */
    @Override
    public List<String> getOperationLogUserName() {
        return operationLogDao.getOperationLogUserName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.OperationService#getOperationLogEntityIp()
     */
    @Override
    public List<String> getOperationLogEntityIp() {
        return operationLogDao.getOperationLogEntityIp();
    }

}
