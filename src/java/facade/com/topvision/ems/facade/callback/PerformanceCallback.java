/***********************************************************************
 * $ com.topvision.ems.performance.service.PerformanceCallback,v1.0 2012-5-6 9:05:15 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.callback;

import java.sql.Timestamp;
import java.util.EventObject;
import java.util.List;

import com.topvision.ems.facade.domain.ConnectivityStrategy;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Callback;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author jay
 * @created @2012-5-6-9:05:15
 */
@Callback(beanName = "performanceService", serviceName = "performanceService")
public interface PerformanceCallback {
    /**
     * sendPerfomaceResult
     * 
     * @param result
     */
    public void sendPerfomaceResult(PerformanceData result);

    /**
     * sendPerfomaceResult
     * 
     * @param result
     */
    public void sendPerfomaceResult(List<PerformanceData> result);

    /**
     * sendPerfomaceResult
     * 
     * @param result
     */
    public void sendRealtimePerfomaceResult(PerformanceData result);

    /**
     * sendPerfomaceResult
     * 
     * @param result
     */
    public void sendRealtimePerfomaceResult(List<PerformanceData> result);

    /**
     * 通过entityId获取snmpParam
     * 
     * @param entityId
     *            entityId
     * @return SnmpParam
     */
    SnmpParam getSnmpParamByEntity(Long entityId);

    List<ConnectivityStrategy> getUsingConnectivityStrategy();

    /**
     * 性能采集任务回调更新执行时间，用于查看性能任务是否执行
     * 
     * @param monitorId
     * @param scheduleRunTime
     */
    void recordTaskCollectTime(Long monitorId, Timestamp scheduleRunTime);

    void test();

    /**
     *
     * @param typeId
     * @return
     */
    // TODO Victor@20150623不建议把类似Cmts模块的东西放到Server端来实现，需要找时间修改
    @Deprecated
    Integer getCmtsSampleCollect(Long typeId);

    /**
     * 
     * @param cmcId
     * @return
     */
    // TODO Victor@20150623为了解决分布式临时解决办法
    Boolean isCmts(Long cmcId);

    /**
     * 从engine端发送消息到server进行处理
     * 
     * @param <T>
     * @param <M>
     * @param event
     */
    <T extends EventObject> void addServerMessage(T event);

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
    void updateScheduleCollectTime(Long monitorId, Long time);

    /**
     * Engine重新连接时调用，Server端找出所有这个engineId对应的monitor重新发送给engine。
     * 
     * @param engineId
     */
    void restartMonitor(Integer engineId);

    /**
     *
     * @param entityId
     * @return
     */
    Boolean isCcmts(Long entityId);

    /**
     *
     * @param entityId
     * @return
     */
    Boolean isOlt(Long entityId);

    /**
     *
     * @param entityId
     * @return
     */
    Boolean isFunctionSupported(Long entityId,String func);
}
