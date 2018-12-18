/***********************************************************************
 * $Id: PnmpCmDataDao.java,v1.0 2017年8月8日 下午2:58:34 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.pnmp.facade.domain.CorrelationGroup;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午2:58:34
 *
 */
public interface PnmpCmDataDao extends BaseEntityDao<Object> {

    /**
     * 获取指定CMTS的相似故障分组列表
     * 
     * @param cmcId
     */
    void selectCorrelationGroupsByCmcId(Long cmcId);

    /**
     * 获取指定CMTS的指定故障分组的CM列表
     * 
     * @param queryMap
     * @return
     */
    List<PnmpCmData> selectDataByGroup(Map<String, Object> queryMap);

    /**
     * 插入PNMP记录
     * 
     * @param data
     */
    void insertOrUpdateData(PnmpCmData data);

    /**
     * 获取指定CM在指定时间范围内的历史数据
     * 
     * @param queryMap
     * @return
     */
    List<PnmpCmData> selectHistoryData(Map<String, Object> queryMap);

    /**
     * 批量获取cm 频响曲线
     * 
     * @param cmMacList
     * @return
     */
    List<String> selectSpectrumResponsesByMac(List<String> cmMacList);

    /**
     * 查询最大upChannelWidth
     * 
     * @param cmcId
     * @return
     */
    Long selectMaxUpChannelWidthByCmcId(Long cmcId);

    /**
     * 查询出高速队列拥有的CMCID列表
     * 
     * @return
     */
    List<Long> selectHighIntervalCmcIds();

    /**
     * 查询出中速队列拥有的CMCID列表
     * 
     * @return
     */
    List<Long> selectMiddleIntervalCmcIds();

    /**
     * 查询cmts的故障分组
     * 
     * @param cmcId
     * @return
     */
    List<CorrelationGroup> selectCorrelationGroup(Long cmcId);

    /**
     * 通过CMCID查询高速队列出相关last结果(checkStatus为0,preEqualizationState为1)
     * 
     * @param cmcId
     * @return
     */
    List<CorrelationGroup> selectHighCorrelationGroups(Long cmcId);

    /**
     * 通过CMCID查询出中速队列相关last结果(checkStatus为0,preEqualizationState为1)
     * 
     * @param cmcId
     * @return
     */
    List<CorrelationGroup> selectMiddleCorrelationGroups(Long cmcId);

    /**
     * 更新last结果中的故障分组
     * 
     * @param groups
     */
    void updateCorrelationGroup(List<CorrelationGroup> groups);

    /**
     * 通过CC更新last结果中的故障分组
     * 
     * @param groups
     */
    void updateCorrelationGroupByCmcId(Long cmcId);

    PnmpCmData selectDataByGroupForMobile(Map<String, Object> queryMap);
}
