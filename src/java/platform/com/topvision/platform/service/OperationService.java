/***********************************************************************
 * $Id: OperationService.java,v1.0 2011-11-20 下午04:31:32 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.OperationLog;

/**
 * @author huqiao
 * @created @2011-11-20-下午04:31:32
 * 
 */
public interface OperationService extends Service {

    /**
     * 插入操作日志记录
     * 
     * @param operationLog
     */
    void insertOperationLog(OperationLog operationLog);

    /**
     * 获得所有的操作日志纪录
     * 
     * @return list
     */
    List<OperationLog> getAllOperationLog();

    /**
     * 获得某个设备的操作日志纪录
     * 
     * @param entityId
     * @return list
     */
    List<OperationLog> getOperationLogByEntityId(Long entityId);

    /**
     * 获得某种状态的操作日志记录
     * 
     * @param status
     * @return list
     */
    List<OperationLog> getOperationLogByStatus(Integer status);

    /**
     * 根据查询参数获得操作日志记录
     * 
     * @param entityIp
     * @param clientIp
     * @param operName
     * @param status
     * @return
     */
    List<OperationLog> getOperationLogByParams(String entityIp, String clientIp, String operName, String status,
            Integer start, Integer limit);

    /**
     * 根据查询参数获得操作日志记录总记录数
     * 
     * @param entityIp
     * @param clientIp
     * @param operName
     * @param status
     * @return
     */
    List<OperationLog> getOperationLogByParamsCounts(String entityIp, String clientIp, String operName, String status);

    /**
     * 获得操作日志中可过滤的客户端IP地址
     * 
     * @return
     */
    List<String> getOperationLogClientIp();

    /**
     * 获得操作日志中可过滤的用户登录名
     * 
     * @return
     */
    List<String> getOperationLogUserName();

    /**
     * 获得操作日志中可过滤的设备IP地址
     * 
     * @return
     */
    List<String> getOperationLogEntityIp();
    
    /**
     * 获得指定日志
     * 
     * @param mac
     * @param typeName
     * @param version
     * @param clazz
     * @return
     */
    String getEntityReplaceLog(String mac, String typeName, String version, Class clazz);

}
