/***********************************************************************
 * $ com.topvision.ems.cmts.dao.CmtsPerfDao,v1.0 2013-8-15 18:14:46 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.dao;

import java.util.List;

import com.topvision.ems.cmc.performance.domain.CmcSignalQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmtsFlowQuality;
import com.topvision.ems.cmts.facade.domain.UplinkSpeedPerf;
import com.topvision.ems.cmts.performance.domain.CmtsServiceQualityResult;
import com.topvision.ems.cmts.performance.domain.IfUtilization;
import com.topvision.ems.cmts.performance.domain.UplinkSpeedStatic;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author jay
 * @created @2013-8-15-18:14:46
 */
public interface CmtsPerfDao extends BaseEntityDao<Entity> {
    /**
     * 获取cmts snr
     * 
     * @param entityId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmtsSnrPoints(Long entityId, Long channelIndex, String startTime, String endTime);

    /**
     * 获取cmts ber
     * 
     * @param entityId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmtsCcerPoints(Long entityId, Long channelIndex, String startTime, String endTime);

    /**
     * 获取cmts ucer
     * 
     * @param entityId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmtsUcerPoints(Long entityId, Long channelIndex, String startTime, String endTime);

    /**
     * 获取cmts 速率
     * 
     * @param entityId
     * @param channelIndex
     * @param perfTarget
     * @param startTim
     * @param endTime
     * @return
     */
    List<Point> selectCmtsChannelSpeedPoints(Long entityId, Long channelIndex, String perfTarget, String startTime,
            String endTime);

    /**
     * 获取上联口入流量
     * 
     * @param entityId
     * @param channelIndex
     * @param perfTarget
     * @param startTim
     * @param endTime
     * @return
     */
    List<Point> selectCmtsUpLinkSpeedPoints(Long entityId, Long channelIndex, String perfTarget, String startTime,
            String endTime);

    /**
     * 获取上联口入流量
     * 
     * @param entityId
     * @param channelIndex
     * @param perfTarget
     * @param startTim
     * @param endTime
     * @return
     */
    List<Point> selectCmtsPortUsedPoints(Long entityId, Long channelIndex, String perfTarget, String startTime,
            String endTime);

    /**
     * 更新CMTS的上联口状态
     * 
     * @param entityId
     * @param uplinkSpeedPerf
     */
    void updateCmtsPortStatus(long entityId, UplinkSpeedPerf uplinkSpeedPerf);

    /**
     * 记录上联口速率统计结果
     * 
     * @param uplinkSpeedStatic
     */
    void recordIfSpeedStatic(UplinkSpeedStatic uplinkSpeedStatic);

    /**
     * 记录上联口利用率
     * 
     * @param ifUtilization
     */
    void recordIfUtilization(IfUtilization ifUtilization);

    /**
     * 更新上联口速率
     * 
     * @param entityId
     * @param ifIndex
     * @param ifSpeed
     */
    void updateIfSpeed(long entityId, long ifIndex, long ifSpeed);

    /**
     * 获得Cmts当前模块的性能处理
     * 
     * @param entityId
     * @param category
     * @return
     */
    List<Integer> getCmtsPerformanceMonitorId(Long entityId, String category);

    /**
     * 更新CMTS信号质量性能数据
     * 
     * @param cmcSingleQualities
     */
    void insertCmtsSignalQuality(Long entityId, String targetName, CmcSignalQualityPerfResult result);

    /**
     * 更新CMTS流量性能数据
     * 
     * @param flowQualities
     */
    void insertCmtsFlowQuality(List<CmtsFlowQuality> flowQualities);

    /**
     * 获取设备响应延时
     * 
     * @param entityId
     * @param startTim
     * @param endTime
     * @return
     */
    List<Point> queryCmtsRelayPerfPoints(Long entityId, String startTime, String endTime);

    /**
     * 插入CMTS设备CPU利用率
     * @param serviceResult
     */
    void insertCmtsCpuQuality(CmtsServiceQualityResult serviceResult);

    /**
     * 插入CMTS设备内存利用率
     * @param serviceResult
     */
    void insertCmtsMemQuality(CmtsServiceQualityResult serviceResult);
    
    /**
     * 查询cmts的CPU性能数据
     * @param entityId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> queryCmtsCpuData(Long entityId, String startTime, String endTime);

    /**
     * 查询Cmts的内存利用率数据
     * @param entityId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> queryCmtsMemData(Long entityId, String startTime, String endTime);
}