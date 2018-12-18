/***********************************************************************
 * $Id: CmcPerfGraphDao.java,v1.0 2013-8-8 上午10:01:17 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.dao;

import java.util.List;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.domain.CmcChannelBasic;
import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author lizongtian
 * @created @2013-8-8-上午10:01:17
 *
 */
public interface CmcPerfGraphDao extends BaseEntityDao<Entity> {

    /**
     * 获得CMC信道列表
     * 
     * @param entityId
     * @param type
     * @return
     */
    List<CmcChannelBasic> selectCmcChannelList(Long entityId, String type);

    /**
     * 获得CPU利用率历史性能数据
     * 
     * @param entityId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcCpuUsedPerfPoints(Long entityId, String startTime, String endTime);

    /**
     * 获得内存利用率历史性能数据
     * 
     * @param entityId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcMemUsedPerfPoints(Long entityId, String startTime, String endTime);

    /**
     * 获得Flash利用率历史性能数据
     * 
     * @param entityId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcFlashUsedPerfPoints(Long entityId, String startTime, String endTime);

    /**
     * 获得CMC信道SNR历史性能数据
     * 
     * @param entityId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcSnrPerfPoints(Long entityId, Long channelIndex, String startTime, String endTime);

    /**
     * 获得CMC信道不可纠错码率历史性能数据
     * 
     * @param entityId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcUcerPerfPoints(Long entityId, Long channelIndex, String startTime, String endTime);

    /**
     * 获得CMC信道可纠错码率历史性能数据
     * 
     * @param entityId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcCcerPerfPoints(Long entityId, Long channelIndex, String startTime, String endTime);

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
    List<Point> selectCmcChannelCmNumberPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
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
    List<Point> selectCmcOptLinkPerfPoints(Long entityId, String perfTarget, String startTime, String endTime);

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
    List<Point> selectCmcUpLinkFlowPoints(Long entityId, Integer index, Integer direction, String startTime,
            String endTime);

    /**
     * 获得CMC上联口利用率历史性能数据
     * 
     * @param entityId
     * @param index
     * @param direction
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcUpLinkUsedPoints(Long entityId, Integer index, Integer direction, String startTime,
            String endTime);

    /**
     * 获得CMC信道速率和利用率历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param channnelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcChannelSpeedPoints(Long entityId, Long channelIndex, String perfTarget, String startTime,
            String endTime);

    /**
     * 获得CMC Mac域流量历史性能数据
     * 
     * @param entityId
     * @param channnelIndex
     * @param direction
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcMacFlowPoints(Long entityId, Long channelIndex, Integer direction, String startTime,
            String endTime);

    /**
     * 获得CMC温度历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcTempPoints(Long entityId, String perfTarget, String startTime, String endTime);

    /**
     * 获得CMC电源模块温度历史性能数据
     * 
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    List<CmcTempQualityFor8800B> selectCmcPowerModuleTemp(Long entityId, String perfTarget, String startTime,
            String endTime);

    /**
     * 获得CMC信道状态信息
     * 
     * @param entityId
     * @return
     */
    List<CmcPort> selectCmcportList(Long entityId);

    /**
     * 加载设备响应延时
     * 
     * @param entityId
     * @param startTime
     * @param endTime
     * @return List<Point>
     */
    List<Point> queryCmcRelayPerfPoints(Long entityId, String startTime, String endTime);

    /**
     * 加载设备上联口列表
     * 
     * @param entityId
     * @return List<CmcPort>
     */
    List<CmcPhyConfig> queryCmcUpLinkPorts(Long entityId);

    /**
     * 加载网络概况列表
     * 
     * @param
     * @return List<Point>
     */
    List<Point> queryCmcNetInfoPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
            String endTime);

    /**
     * 在线cm
     * 
     * @param
     * @return List<Point>
     */
    List<Point> queryCmcOnlineCmPoints(Long entityId, Long cmcIndex, String startTime, String endTime);

    /**
     * 总cm
     * 
     * @param
     * @return List<Point>
     */
    List<Point> queryCmcAllCmPoints(Long entityId, Long cmcIndex, String startTime, String endTime);

    /**
     * 获取光机电压历史信息
     * 
     * @param entityId
     * @param perfTarget
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> selectCmcVoltagePoints(Long entityId, String perfTarget, String startTime, String endTime);
}
