package com.topvision.platform.service;

import java.util.List;
import java.util.Map;

import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.SystemLog;

public interface SystemLogService extends Service {
    /**
     * 删除给定 id 集合的系统日志.
     * 
     * @param logIds
     */
    void deleteSystemLog(List<Long> logIds);

    /**
     * 删除给定 id 的系统日志.
     * 
     * @param logId
     */
    void deleteSystemLog(Long logId);

    /**
     * 得到系统日志实例.
     * 
     * @param start
     * @param limit
     * @return
     */
    List<SystemLog> getUserLogByParams(Integer start, Integer limit);

    /**
     * 得到系统日志实例.
     * 
     * @return
     */
    List<SystemLog> getUserLogCounts();

    /**
     * 得到给定 id 的系统日志实例.
     * 
     * @param logId
     * @return
     */
    SystemLog getSystemLog(Long logId);

    /**
     * 获取某页日志数据.
     * 
     * @param map
     * @param page
     * @return
     */
    PageData<SystemLog> getSystemLog(Map<String, String> map, Page page);

    /**
     * 插入一个系统日志.
     * 
     * @param sysLog
     */
    void insertSystemLog(SystemLog sysLog);
}
