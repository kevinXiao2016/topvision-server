/***********************************************************************
 * $Id: PnmpCmDataService.java,v1.0 2017年8月8日 下午4:36:45 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.pnmp.facade.domain.CorrelationGroup;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:36:45
 *
 */
public interface PnmpCmDataService {

    /**
     * 获取指定CMTS的相似故障分组列表
     * 
     * @param cmcId
     */
    void getCorrelationGroupsByCmcId(Long cmcId);

    /**
     * 获取指定CMTS的指定故障分组的CM列表
     * 
     * @param queryMap
     * @return
     */
    List<PnmpCmData> getDataByGroup(Map<String, Object> queryMap);

    /**
     * 插入PNMP记录
     * 
     * @param data
     */
    void insertData(PnmpCmData data);

    /**
     * 获取指定CM在指定时间范围内的历史数据
     * 
     * @param queryMap
     * @return
     */
    List<PnmpCmData> getHistoryData(Map<String, Object> queryMap);

    /**
     * 批量获取cm 频响曲线
     * 
     * @param cmMacList
     * @return
     */
    List<String> getSpectrumResponsesByMac(List<String> cmMacList);

    /**
     * 获取最大upChannelWidth
     * 
     * @param cmcId
     * @return
     */
    Long getMaxUpChannelWidthByCmcId(Long cmcId);

    /**
     * 获取CMTS的故障分组
     * 
     * @param cmcId
     * @return
     */
    List<CorrelationGroup> getCorrelationGroup(Long cmcId);

    PnmpCmData getDataByGroupForMobile(Map<String, Object> queryMap);

    /**
     * 到设备上执行ping docsis命令
     * @param cmcId
     * @param cmMac
     * @return
     */
    String pingDocsis(Long cmtsId,String cmMac);

    /**
     * CM信道迁移
     * @param cmtsId
     * @param channelId
     * @param cmMac
     * @return
     */
    String moveCmChannel(Long cmtsId,Long channelId,String cmMac);

    /**
     * 获取pnmp实时数据
     * @param cmtsId
     * @param cmMac
     * @return
     */
    PnmpCmData realPnmp(Long cmtsId, String cmMac);
}
