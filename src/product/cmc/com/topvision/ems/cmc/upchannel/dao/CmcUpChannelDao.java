/***********************************************************************
 * $Id: CmcUpChannelDao.java,v1.0 2013-10-31 上午9:41:22 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.upchannel.dao;

import java.util.List;

import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUsSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelCounterInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelRanging;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author dosion
 * @created @2013-10-31-上午9:41:22
 * 
 */
public interface CmcUpChannelDao extends BaseEntityDao<Object> {

    /**
     * 获取上行信道基本信息列表
     * 
     * @param cmcId
     *            cmc设备id
     * @return List<CmcUpChannelBaseShowInfo> 上行信道基本信息列表
     */
    List<CmcUpChannelBaseShowInfo> getUpChannelBaseShowInfoList(Long cmcId);

    /**
     * 获取上行信道基本信息
     * 
     * @param portId
     *            单条上行信道数据库ID
     * @return getUpChannelBaseShowInfo 单条上行信道基本信息
     */
    CmcUpChannelBaseShowInfo getUpChannelBaseShowInfo(Long portId);

    /**
     * 修改上行信道基本信息
     * 
     * @param cmcUpChannelBaseShowInfo
     *            单条上行信道基本信息
     * 
     */
    void updateUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo);

    /**
     * 修改上行信道DOCS基本信息
     * 
     * @param cmcUpChannelBaseInfo
     *            单条上行信道基本信息
     * 
     */
    void updateUpChannelBaseInfo(CmcUpChannelBaseInfo cmcUpChannelBaseInfo);

    /**
     * 获取上行信道统计信息列表
     * 
     * @param cmcId
     *            cmc设备id
     * @return List<CmcUpChannelCounterInfo> 上行信道统计信息列表
     */
    List<CmcUpChannelCounterInfo> getUpChannelStaticInfoList(Long cmcId);

    /**
     * 获取上行信道信号质量列表
     * 
     * @param cmcId
     *            cmc设备id
     * @return List<CmcUsSignalQualityInfo> 上行信道信号质量列表
     */
    List<CmcUsSignalQualityInfo> getUsSignalQualityInfoList(Long cmcId);

    /**
     * 获取上行信道信号质量列表
     * 
     * @param cmcId
     *            cmc设备id
     * @return List<CmcUsSignalQualityInfo> 上行信道信号质量列表
     */
    List<CmcUpChannelSignalQualityInfo> getUpChannelSignalQualityInfoList(Long cmcId);

    /**
     * 获取除cmcPortId指定的通道外CC上的通道
     * 
     * @param cmcPortId
     * @return
     */
    List<CmcUpChannelBaseShowInfo> getUpChannelListByPortId(Long cmcPortId);

    /**
     * 获取除CC上在线的信道
     * 
     * @param cmcPortId
     * @return
     */
    List<CmcUpChannelBaseShowInfo> getUpChannelOnListByCmcId(Long cmcId);

    /**
     * 获取channelIndex
     * 
     * @param cmcId
     * @param channleId
     * @return
     */
    Long getChannleIndex(Long cmcId, Integer channleId);

    /**
     * 根据cmcId和channelIndex获取对应的portId
     * 
     * @param cmcId
     * @param channelIndex
     * @return
     */
    Long getPortId(Long cmcId, Long channelIndex);

    /**
     * 更新上行信道电平
     * 
     * @param cmcUpChannelSignalQualityInfo
     */
    void updateUpChannelSignalQuality(CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo);

    /**
     * 更行上行信道测距
     * 
     * @param cmcUpChannelRanging
     */
    void updateUpChannelRanging(CmcUpChannelRanging cmcUpChannelRanging);

    /**
     * 通过cmcId和channelIndex获取上行信道的基本信息
     * @param cmcId
     * @param upChannleIndex
     * @return
     */
    CmcUpChannelBaseShowInfo selectUpChannelBaseInfo(Long cmcId, Long upChannleIndex);
}
