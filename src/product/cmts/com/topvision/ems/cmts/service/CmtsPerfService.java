/***********************************************************************
 * $ com.topvision.ems.cmts.service.cmtsPerfService,v1.0 2013-8-17 15:41:38 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.annotation.DynamicDB;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author jay
 * @created @2013-8-17-15:41:38
 */
public interface CmtsPerfService extends Service {

    boolean hasCmtsMonitor(Long entityId, String category);

    /**
     * 开启Cmts基础数据更新监视器
     * 
     * @param cmcId
     * @param perfCollectPeriod
     * @param snmpParam
     */
    void startSystemCmtsPerfMonitor(Long cmcId, Long perfCollectPeriod, SnmpParam snmpParam);

    /**
     * 关闭CMTS系统监视器
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopSystemCmtsPerfMonitor(Long cmcId, SnmpParam snmpParam);

    /**
     * 开启上联口流量监视器
     * 
     * @param entityId
     * @param perfCollectPeriod
     * @param snmpParam
     */
    void startUplinkCmtsPerfMonitor(Long entityId, long perfCollectPeriod, SnmpParam snmpParam);

    /**
     * 关闭上联口流量系统监视器
     * 
     * @param entityId
     * @param snmpParam
     */

    void stopUplinkCmtsPerfMonitor(Long entityId, SnmpParam snmpParam);

    /**
     * 获取cmts信号质量信息
     * 
     * @param entityId
     * @param perfTarget
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    public List<Point> queryCmtsSignalQualityPoints(Long entityId, String perfTarget, Long channelIndex,
            String startTime, String endTime);

    /**
     * 获取cmts流量信息
     * 
     * @param entityId
     * @param perfTarget
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    public List<Point> queryCmtsFlowPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
            String endTime);

    /**
     * 开启CMTS在线状态性能采集(适用于独立IP)
     * 
     * @param cmcId
     * @param cmcIndex
     * @param snmpParam
     */
    void startCmtsOnlineQuality(Long entityId, Long typeId, SnmpParam snmpParam);

    /**
     * 关闭CMTS在线状态性能采集(适用于独立IP)
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmtsOnlineQuality(Long entityId, SnmpParam snmpParam);

    /**
     * 开启CMTS信号质量性能采集
     * 
     * @param entityId
     * @param snmpParam
     * @param typeId
     */
    void startCmtsSignalQuality(Long entityId, SnmpParam snmpParam, Long typeId);

    /**
     * 关闭CMTS信号质量性能采集
     * 
     * @param cmcId
     * @param snmpParam
     */
    void stopCmtsSignalQuality(Long entityId, SnmpParam snmpParam);

    /**
     * 开启CMTS流量性能采集
     * 
     * @param entityId
     * @param snmpParam
     * @param typeId
     */
    void startCmtsFlowQuality(Long entityId, SnmpParam snmpParam, Long typeId);

    /**
     * 关闭CMTS流量性能采集
     * 
     * @param entityId
     * @param snmpParam
     */
    void stopCmtsFlowQuality(Long entityId, SnmpParam snmpParam);

    /**
     * 获得CMTS的指标索引
     * 
     * @param entityId
     * @param targetName
     */
    public List<Long> getModifyCmtsTargetIndexs(Long entityId, String targetName);

    /**
     * 获得CMTS的响应延时
     * 
     * @param entityId
     * @param startTime
     * @param endTime
     */
    @DynamicDB
    public List<Point> queryCmtsRelayPerfPoints(Long entityId, String startTime, String endTime);

    /**
     * 内蒙需求,增加内存利用率和CPU的采集
     * @param entityId
     * @param snmpParam
     * @param typeId
     */
    public void startCmtsServiceQuality(Long entityId, SnmpParam snmpParam, long typeId);

    /**
     * 关闭CMTS内存利用率和CPU的采集
     * 
     * @param entityId
     * @param snmpParam
     */
    public void stopCmtsServiceQuality(Long entityId, SnmpParam snmpParam);

    /**
     * 开启Cmts性能采集
     * @param entity
     */
    public void startCmtsPerfCollect(Entity entity);

    /**
     * 获取CMTS服务质量数据
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Point> getCmtsServicePerfPoints(Long entityId, String perfTarget, String startTime, String endTime);

}
