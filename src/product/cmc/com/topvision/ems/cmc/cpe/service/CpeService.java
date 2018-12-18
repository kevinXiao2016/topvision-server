/***********************************************************************
 * $Id: CmcCpeService.java,v1.0 2013-6-18 下午1:13:43 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cm.domain.RealtimeCpe;
import com.topvision.ems.cmc.cpe.domain.CmFdbTable;
import com.topvision.ems.cmc.cpe.domain.CmFdbTableRemoteQuery;
import com.topvision.ems.cmc.cpe.domain.CmLocateInfo;
import com.topvision.ems.cmc.cpe.domain.CmServiceFlow;
import com.topvision.ems.cmc.domain.Area;
import com.topvision.ems.cmc.domain.CmCmcRunningInfo;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.domain.CmCpeCollectDataPolicy;
import com.topvision.ems.cmc.domain.CmCpeNumInArea;
import com.topvision.ems.cmc.domain.CmOltRunningInfo;
import com.topvision.ems.cmc.domain.CmcCmReatimeNum;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.domain.CmcUserNumHisPerf;
import com.topvision.ems.cmc.domain.CpeCollectConfig;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.performance.domain.CmCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author loyal
 * @created @2013-6-18-下午1:13:43
 * 
 */
public interface CpeService extends Service {
    public CmLocateInfo queryCmLocate(String cmMac);

    /**
     * 获取cm采集配置
     * 
     * @return
     */
    public CmCollectConfig getCmCollectConfig();

    /**
     * 获取cpe采集配置
     * 
     * @return
     */
    public CpeCollectConfig getCpeCollectConfig();

    /**
     * 获取cm cpe采集数据保存策略
     * 
     * @return
     */
    public CmCpeCollectDataPolicy getCmCpeCollectDataPolicy();

    /**
     * 修改cm采集配置
     * 
     * @param cmCollectConfig
     */
    public void modifyCmCollectConfig(CmCollectConfig cmCollectConfig);

    /**
     * 修改cpe采集配置
     * 
     * @param cpeCollectConfig
     */
    public void modifyCpeCollectConfig(CpeCollectConfig cpeCollectConfig);

    /**
     * 修改cm cpe采集数据保存策略
     * 
     * @param cmCpeCollectDataPolicy
     */
    public void modifyCmCpeCollectDataPolicy(CmCpeCollectDataPolicy cmCpeCollectDataPolicy);

    /**
     * 获取所有片区
     * 
     * @return
     */
    List<Area> getAllAreas();

    /**
     * 获取olt及拆分型CCMTS
     * 
     * @return
     */
    List<Entity> getEntityWithIp();

    /**
     * 获取集成型CCMTS
     * 
     * @return
     */
    List<CmcEntity> getCcmstWithOutAgent();

    /**
     * 获取全局用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getAllUserNumHis(Map<String, Object> queryMap);

    /**
     * 获取地域用户数历史统计
     * 
     * @param regionId
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByArea(Long regionId);

    /**
     * 获取OLT用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByDevice(Map<String, Object> queryMap);

    /**
     * 获取拆分型CC用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByCmc(Map<String, Object> queryMap);

    /**
     * 获取集成型CC用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByCmcWithOutAgent(Map<String, Object> queryMap);

    /**
     * 获取信道用户数历史统计
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByChannel(Map<String, Object> queryMap);

    /**
     * 获取片区cm cpe在线数
     * 
     * @return
     */
    List<CmCpeNumInArea> getCmCpeNumInRegion();

    /**
     * 通过cmc Index 及entityId获取cmc
     * 
     * @param cmcIndex
     * @param entityId
     * @return
     */
    CmcEntityRelation getCmcByIndexAndEntityId(Long cmcIndex, Long entityId);

    /**
     * 查询设备实时cm数
     * 
     * @param map
     * @return
     */
    List<CmNum> getAllAllDeviceCmNum(Map<String, Object> map);

    /**
     * 获取CC cm用户数相关信息
     * 
     * @param entityId
     * @return
     */
    List<CmcCmReatimeNum> getCcmtsCmNumInfo(Long entityId);

    /**
     * 获取CC cpu利用率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getCpuHisPerf(Map<String, Object> queryMap);

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
    List<Point> getSnrHisPerf(Map<String, Object> queryMap);

    /**
     * 获取信道入流量历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getUpChannelFlowHisPerf(Map<String, Object> queryMap);

    /**
     * 获取信道出流量历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getDownChannelFlowHisPerf(Map<String, Object> queryMap);

    /**
     * 获取cm 所在cc运行信息
     * 
     * @param cmId
     * @return
     */
    CmCmcRunningInfo getCmCmcRunningInfo(Long cmId);

    /**
     * 获取cm 所在cmts运行信息
     * 
     * @param cmId
     * @return
     */
    CmCmcRunningInfo getCmCmtsRunningInfo(Long cmId);

    /**
     * 获取可纠错误码率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getBitErrorRateHisPerf(Map<String, Object> queryMap);

    /**
     * 获取CMTS可纠错误码率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getCmtsBitErrorRateHisPerf(Map<String, Object> queryMap);

    /**
     * 获取不可纠错误码率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getUnBitErrorRateHisPerf(Map<String, Object> queryMap);

    /**
     * 获取Cmts不可纠错误码率历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getCmtsUnBitErrorRateHisPerf(Map<String, Object> queryMap);

    /**
     * 获取cm在线数历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getCmOnlineNumHisPerf(Map<String, Object> queryMap);

    /**
     * 获取cm下线数历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getCmOfflineNumHisPerf(Map<String, Object> queryMap);

    /**
     * 获取cm 所在olt运行信息
     * 
     * @param cmId
     * @return
     */
    CmOltRunningInfo getCmOltRunningInfo(Long cmId);

    /**
     * 获取CMTS信道snr历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getCmtsSnrHisPerf(Map<String, Object> queryMap);

    /**
     * 获取CMTS信道入流量历史数据
     * 
     * @param queryMap
     * @return
     */
    List<Point> getCmtsUpChannelFlowHisPerf(Map<String, Object> queryMap);

    /**
     * 获取CMC CM用户数数据
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByCmcNew(Map<String, Object> queryMap);

    /**
     * 获取PON口CM用户数数据
     * 
     * @param queryMap
     * @return
     */
    List<CmcUserNumHisPerf> getUserNumHisByPon(Map<String, Object> queryMap);

    /**
     * 通过PONID获取PONINDEX
     * 
     * @param ponId
     * @return
     */
    Long getPonIndexByPonId(Long ponId);

    /**
     * 通过CmcId获取CmcIndex
     * 
     * @param cmcId
     * @return
     */
    Long getCmcIndexByCmcId(Long cmcId);

    /**
     * 获得Snap表中所有在线设备的entityd
     * 
     * @return
     */
    List<Long> getAllOnlineIds();

    /**
     * 获得CM下CPE列表
     * 
     * @return
     */
    List<CmCpe> getCpeListByCmId(Long cmId);

    /**
     * 获得实时CM下CPE列表
     * 
     * @return
     */
    List<RealtimeCpe> getRealCpeListByCmMac(Long cmcId, String cmMac);

    /**
     * 获得实时CM下CPE列表
     * 
     * @return
     */
    List<RealtimeCpe> getRealCpeListByCmId(Long cmId);

    /**
     * 获取单个CPE的信息
     * 
     * @return
     */
    CmCpe getCpeByCpeMac(Long cmId, String cpeMac);

    /**
     * 获取单个CPE的信息
     * 
     * @return
     */
    CmCpe refreshCpeByCmcAndCpeMac(Long cmcId, String cpeMac);

    /**
     * CM轮询是否开启
     * 
     * @return
     */
    public boolean isCmPollStart();

    /**
     * 获取CM轮询间隔
     * 
     * @return
     */
    public Long getCmPollInterval();

    /**
     * 修改cm snmp参数
     * 
     * @param readConmunity
     * @param writeConmunity
     */
    public void modifyCmSnmpParamConfig(String readConmunity, String writeConmunity);

    /**
     * 获取CM读共同体名
     * 
     * @return
     */
    public String getCmReadCommunity();

    /**
     * 获取CM写共同体名
     * 
     * @return
     */
    public String getCmWriteCommunity();

    /**
     * 获取CM实时状态
     *
     * @param cmcId
     * @param cmMac
     * @return
     */
    Integer loadCmStatusOnCmts(Long cmcId, String cmMac);

    /**
     * 获取CM下线前状态
     *
     * @param cmcId
     * @param cmIndex
     * @return
     */
    Integer loadCmPreStatusOnCmts(Long cmcId, Long cmIndex);

    /**
     * 从CC上获取一个CM的所有服务流信息
     * @param cmId
     * @return
     */
    List<CmServiceFlow> getCmServiceFlowByCmId(Long cmId);

    /**
     * 获取Cm的CmFdbTableRemoteQuery列表
     * @param cmId
     * @return
     */
    List<CmFdbTableRemoteQuery> getFdbTableRemoteQuery(Long cmId);

    /**
     * 获取Cm的CmFdbTable列表
     * @param snmpParam
     * @return
     */
    List<CmFdbTable> getFdbTable(SnmpParam snmpParam);
}
