/***********************************************************************
 * $Id: CmcPerfGraphService.java,v1.0 2013-8-8 上午09:58:49 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.service;

import java.util.List;

import com.topvision.ems.cmc.domain.CmcChannelBasic;
import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.framework.annotation.DynamicDB;
import com.topvision.framework.service.Service;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author lizongtian
 * @created @2013-8-8-上午09:58:49
 *
 */
public interface CmcPerfGraphService extends Service {

    /**
     * 获得信道列表
     * 
     * @param entityId
     * @param type
     * @return
     */
    List<CmcChannelBasic> queryCmcChannelList(Long entityId, String type);

    /**
     * 获得CMC服务质量历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryCmcServicePerfPoints(Long entityId, String perfTarget, String startTime, String endTime);

    /**
     * 获得CMC信号质量历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryCmcSignalQualityPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
            String endTime);

    /**
     * 获得CMC信道CM数量历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryCmcChannelCmNumberPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
            String endTime);

    /**
     * 获得CMC光口信息历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryCmcOptLinkPerfPoints(Long entityId, String perfTarget, String startTime, String endTime);

    /**
     * 获得CMC上联口流量历史性能数据
     * 
     * @param entityId
     * @param index
     * @param direction
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryCmcUpLinkFlowPoints(Long entityId, Integer index, Integer direction, String startTime,
            String endTime, String perfTarget);

    /**
     * 获得CMC信道速率与利用率历史性能数据
     * 
     * @param entityId
     * @param channelIndex
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryCmcChannelSpeedPoints(Long entityId, Long channelIndex, String perfTarget, String startTime,
            String endTime);

    /**
     * 获得CMC Mac域流量与利用率历史性能数据
     * 
     * @param entityId
     * @param channelIndex
     * @param direction
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryCmcMacFlowPoints(Long entityId, Long channelIndex, Integer direction, String perfTarget,
            String startTime, String endTime);

    /**
     * 获得CMC温度历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<Point> queryCmcTempPoints(Long entityId, String perfTarget, String startTime, String endTime);

    /**
     * 获得光机电压历史数据
     * 
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> queryCmcVoltagePoints(Long entityId, String perfTarget, String startTime, String endTime);

    /**
     * 获得CMC电源模块温度历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    @DynamicDB
    List<CmcTempQualityFor8800B> queryCmcPowerModuleTemp(Long entityId, String perfTarget, String startTime,
            String endTime);

    /**
     * 获得端口流量性能指标数据列表
     * 
     * @param entityId
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    @DynamicDB
    List<Point> queryCmcRelayPerfPoints(Long entityId, String startTime, String endTime);

    /**
     * 获得上联口列表
     * 
     * @param entityId
     * @return
     */
    List<CmcPhyConfig> queryCmcUpLinkPorts(Long entityId);

    /**
     * 获取网络概况数据
     * 
     * @param
     * @return List<Point>
     */
    List<Point> queryCmcNetInfoPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
            String endTime);

    /**
     * 在线cm数
     * 
     * @param
     * @return List<Point>
     */
    List<Point> queryCmcOnlineCmPoints(Long entityId, Long cmcIndex, String startTime, String endTime);

    /**
     * 总cm数
     * 
     * @param
     * @return List<Point>
     */
    List<Point> queryCmcAllCmPoints(Long entityId, Long cmcIndex, String startTime, String endTime);
}
