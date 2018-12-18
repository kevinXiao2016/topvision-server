/***********************************************************************
 * $ PerformanceService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.performance.domain.PerfTaskUpdateInfo;
import com.topvision.ems.performance.domain.PingPerf;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface PerformanceService<T extends OperClass> extends Service {

    /**
     * 启动一个采集调度
     * 
     * @param domain
     *            调度需要采集的数据描述类
     * @param initialDelay
     *            初始启动间隔时间
     * @param period
     *            调度轮询周期时间
     * @param scheduleType
     *            采集方式，对象或者oid
     * @return 该monitor的id
     */
    public long startMonitor(SnmpParam snmpParam, OperClass domain, Long initialDelay, Long period,
            Integer scheduleType, Integer isStartUpWithServer);

    /**
     * 停止一个采集调度
     * 
     * @param monitorId
     *            监视器ID
     */
    public void stopMonitor(SnmpParam snmpParam, Long monitorId);

    /**
     * 重启监视器，同时支持重设轮询间隔
     * 
     * @param monitorId
     *            监视器ID
     * @param period
     *            调度轮询周期时间
     */
    public void reStartMonitor(SnmpParam snmpParam, Long monitorId, Long period);

    /**
     * 更新一个采集调度
     * 
     * @param snmpParam
     * @param monitorId
     * @param scheduleType
     * @param type
     */
    public void updateMonitor(SnmpParam snmpParam, Long monitorId, OperClass domain, Integer scheduleType,
            Integer type);

    /**
     * 注册一种指标的Viewer
     * 
     * @param viewer
     */
    public void registViewer(Viewer viewer);

    /**
     * 通过指标类型获取对应的Viewer
     * 
     * @param viewerParam
     *            Viewer的参数
     * @return Viewer
     * @throws com.topvision.exception.service.WrongPerfViewerParamException
     */
    public Viewer getViewerByType(ViewerParam viewerParam);

    /**
     * 更新设备的采集参数
     * 
     * @param perfTaskUpdateInfos
     */
    public void updatePerformanceTask(List<ScheduleMessage<OperClass>> messages,
            List<PerfTaskUpdateInfo> perfTaskUpdateInfos);

    /**
     * 通过identifyKey和category得到一个监视器的配置
     * 
     * @param identify
     * @param category
     * @return
     */
    public ScheduleMessage<?> getMonitorByIdentifyAndCategory(Long identify, String category);

    /**
     * 获得任务缓存
     * 
     * @return
     */
    public Map<Integer, Set<Long>> getMessageIdCache();

    /**
     * 开启连通性指标轮询
     * 
     * @param snmpParam
     * @param pingPerf
     */
    public void startPingMonitor(SnmpParam snmpParam, PingPerf pingPerf);

    /**
     * 获得修改性能指标需要的数据
     * 
     * @param entityId
     * @param targetName
     * @param type
     * @return
     */
    public Object getModifyTargetData(Long entityId, String targetName, Long type);

    /**
     * 获得不同的设备类型修改性能指标需要的数据
     * 
     * @param entityId
     * @param targetName
     * @param entityType
     * @return
     */
    public Object getModifyTargetDataByType(Long entityId, String targetName, Long entityType);

    void modifyCollectTimeUtil(String name, long startTime, Long period);

    /**
     * 重启指定设备的所有monitor
     * 
     * @param entityId
     */
    public void reStartMonitor(Long entityId);

}