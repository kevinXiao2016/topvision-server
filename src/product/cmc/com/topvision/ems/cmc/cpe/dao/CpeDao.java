/***********************************************************************
 * $Id: CmcCpeDao.java,v1.0 2013-6-18 下午1:14:42 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.domain.Area;
import com.topvision.ems.cmc.domain.CmCmcRunningInfo;
import com.topvision.ems.cmc.domain.CmCpeNumInArea;
import com.topvision.ems.cmc.domain.CmOltRunningInfo;
import com.topvision.ems.cmc.domain.CmcCmReatimeNum;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.domain.CmcUserNumHisPerf;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.performance.domain.CmCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author loyal
 * @created @2013-6-18-下午1:14:42
 * 
 */
public interface CpeDao extends BaseEntityDao<CmcEntity> {
    /**
     * 获取所有片区
     * 
     * @return
     */
    List<Area> selectAllAreas();

    /**
     * 获取带IP设备类型
     * @param type
     * @return
     */
    List<Entity> selectEntityWithIp(Long type);

    /**
     * 获取集成型CCMTS
     * 
     * @return
     */
    List<CmcEntity> selectCcmstWithOutAgent(Long type);

    /**
     * 获取全局用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> selectAllUserNumHis(Map<String, Object> queryMap);

    /**
     * 获取地域用户数历史统计
     * 
     * @param areaId
     * @return
     */
    List<CmcUserNumHisPerf> selectUserNumHisByArea(Long regionId);

    /**
     * 获取OLT用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> selectUserNumHisByDevice(Map<String, Object> queryMap);

    /**
     * 获取CC用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> selectUserNumHisByCmc(Map<String, Object> queryMap);

    /**
     * 获取信道用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> selectUserNumHisByChannel(Map<String, Object> queryMap);

    /**
     * 获取集成型CC用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> selectUserNumHisByCmcWithOutAgent(Map<String, Object> queryMap);

    /**
     * 获取片区cm cpe在线数
     * 
     * @return
     */
    List<CmCpeNumInArea> selectCmCpeNumInRegion();

    /**
     * 查询设备实时cm数
     * 
     * @param map
     * @return
     */
    List<CmNum> selectAllAllDeviceCmNum(Map<String, Object> map);

    /**
     * 通过cmc Index 及entityId获取cmc
     * 
     * @param cmcIndex
     * @param entityId
     * @return
     */
    CmcEntityRelation selectCmcByIndexAndEntityId(Long cmcIndex, Long entityId);

    /**
     * 获取CC cm用户数相关信息
     * 
     * @param entityId
     * @return
     */
    List<CmcCmReatimeNum> selectCcmtsCmNumInfo(Long entityId);

    /**
     * 获取CC cpu利用率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectCpuHisPerf(Map<String, Object> queryMap);

    /**
     * 通过CMMAC CPEMAC CPEIP查询出cm cpe列表
     * 
     * @param cmMac
     * @param cpeMac
     * @param cpeIp
     * @param cmLocation
     * @return
     */
    List<CmCpeAttribute> queryCmCpeListByCondition(String cmMac, String cpeMac, String cpeIp, String cmLocation);
    /**
     * 通过CMMAC CPEMAC CPEIP查询出cm cpe列表
     *
     * @param cmMac
     * @param cpeMacLong
     * @param cpeIpLong
     * @param cmLocation
     * @return
     */
    List<CmCpeAttribute> queryCmCpeListByConditionHis(String cmMac, Long cpeMacLong, Long cpeIpLong, String cmLocation);

    /**
     * 获取信道snr历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectSnrHisPerf(Map<String, Object> queryMap);

    /**
     * 获取信道入流量历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectUpChannelFlowHisPerf(Map<String, Object> queryMap);

    /**
     * 获取信道出流量历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectDownChannelFlowHisPerf(Map<String, Object> queryMap);
    
    /**
     * 获取信道出流量历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectCmtsDownChannelFlowHisPerf(Map<String, Object> queryMap);

    /**
     * 获取cm 所在cc运行信息
     * 
     * @param cmId
     * @return
     */
    CmCmcRunningInfo selectCmCmcRunningInfo(Long cmId);
    
    /**
     * 获取cm 所在cmts运行信息
     * 
     * @param cmId
     * @return
     */
    CmCmcRunningInfo selectCmCmtsRunningInfo(Long cmId);

    /**
     * 获取可纠错误码率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectBitErrorRateHisPerf(Map<String, Object> queryMap);
    
    /**
     * 获取可纠错误码率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectCmtsBitErrorRateHisPerf(Map<String, Object> queryMap);

    /**
     * 获取不可纠错误码率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectUnBitErrorRateHisPerf(Map<String, Object> queryMap);
    
    /**
     * 获取Cmts不可纠错误码率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectCmtsUnBitErrorRateHisPerf(Map<String, Object> queryMap);

    /**
     * 获取cm在线数历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectCmOnlineNumHisPerf(Map<String, Object> queryMap);

    /**
     * 获取cm下线数历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectCmOfflineNumHisPerf(Map<String, Object> queryMap);

    /**
     * 获取cm 所在olt运行信息
     * 
     * @param cmId
     * @return
     */
    CmOltRunningInfo selectCmOltRunningInfo(Long cmId);
    
    /**
     * 获取CMTS信道snr历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectCmtsSnrHisPerf(Map<String, Object> queryMap);

    /**
     * 获取CMTS信道入流量历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> selectCmtsUpChannelFlowHisPerf(Map<String, Object> queryMap);

    /**
     * 获取CMC用户数数据
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByCmcNew(Map<String, Object> queryMap);

    /**
     * 获取PON口用户数数据
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByPon(Map<String, Object> queryMap);

    /**
     * 通过PONID获取PONINDEX
     * @param ponId
     * @return
     */
    Long getPonIndexByPonId(Long ponId);

    /**
     * 通过CmcId获取CmcIndex
     * @param cmcId
     * @return
     */
    Long getCmcIndexByCmcId(Long cmcId);

    /**
     * 获得Snap表中所有在线设备的entityd
     * @return
     */
    List<Long> getAllOnlineIds();

    /**
     * 获得Cm下 CPE列表
     * @return
     */
    List<CmCpe> getCpeListByCmId(Long cmId);

}
