/***********************************************************************
 * $ PerformanceDao.java,v1.0 2012-5-3 9:34:10 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.performance.domain.ExecutorThreadSnap;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author jay
 * @created @2012-5-3-9:34:10
 */
public interface PerformanceDao extends BaseEntityDao<ScheduleMessage<OperClass>> {

    /**
     * 更新性能采集对象
     * 
     * @param scheduleMessage
     */
    void updateScheduleDomain(ScheduleMessage<OperClass> scheduleMessage);

    /**
     * 查询对应的性能采集MonitorId
     * 
     * @param identify
     * @param category
     * @return
     */
    ScheduleMessage<OperClass> selectByIdentifyAndCategory(Long identify, String category);

    /**
     * 查询对应的性能采集任务
     * 
     * @param identify
     * @param category
     * @param period
     * @return
     */
    ScheduleMessage<OperClass> selectByIdentifyAndCategoryAndPeriod(Long identify, String category, Integer period);

    /**
     * 更新性能采集时间
     * 
     * @param identify
     * @param category
     */
    void updateScheduleModifyTime(Long identify, String category);

    /**
     * 在Saver端记录性能采集任务的执行时间
     * 
     * @param monitorId
     * @param time
     */
    void updateScheduleCollectTime(long monitorId, long time);

    void updateSchedulePeriod(Long cmCollectPeriod, String category);

    /**
     * Add by Rod 在Engine端回调记录性能采集任务的执行时间
     * 
     * @param monitorId
     * @param time
     */
    void updateScheduleTaskRunTime(Long monitorId, Timestamp time);

    /**
     * 获得冗余的采集任务列表
     * 
     * @return
     */
    List<Integer> getRedundancyPerfMonitor();

    /**
     * 清除冗余的采集任务列表
     * 
     */
    void cleanRedundancyPerfMonitor();

    Map<String, Map<String, String>> getCategoryCounts();

    /**
     * 查询指定设备的性能采集器信息
     * @param entityId
     * @return
     */
    List<ScheduleMessage<OperClass>> queryScheduleByEntityId(Long entityId);

    /**
     * 查询指定Id列表的设备性能采集器信息
     * @param paramsMap
     * @return
     */
    List<ScheduleMessage<OperClass>> queryScheduleByIdList(Map<String, Object> paramsMap);
    
    /**
     * 查询指定设备类型的设备性能采集器信息
     * @param paramsMap
     * @return
     */
    List<ScheduleMessage<OperClass>> queryScheduleByEntityType(Map<String, Object> paramsMap);
    
    
    /**
     * 
     * 
     * @param monitorId
     * @return
     */
    ScheduleMessage<OperClass> getScheduleMessageForAdminAction(Long monitorId);

    List<ExecutorThreadSnap> queryEngineThreadStatistic(Map<String, Object> queryMap);

    List<ScheduleMessage<?>> loadDelayedPerfMonitors(Integer periodCount);

}
