package com.topvision.ems.cmc.upchannel.service;

import java.util.List;

import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUsSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelCounterInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmtsModulationEntry;

/**
 * CC 设备对ACL的功能管理 目前只有CC8800B支持ACL功能
 * 
 * @author lzs
 * @created @2013-4-20-下午04:01:41
 * 
 */
public interface CmcUpChannelService {

    /**
     * 获取上行信道基本信息列表
     * 
     * @param cmcId
     *            CMC ID
     * @return List<CmcUpChannelBaseShowInfo> 上行信道基本信息列表
     */
    List<CmcUpChannelBaseShowInfo> getUpChannelBaseShowInfoList(Long cmcId);

    /**
     * 获取上行信道基本信息
     * 
     * @param portId
     *            portId
     * @return getUpChannelBaseShowInfo 上行信道基本show信息
     */
    CmcUpChannelBaseShowInfo getUpChannelBaseShowInfo(Long portId);

    /**
     * 修改上行信道基本信息
     * 
     * @param cmcUpChannelBaseShowInfo
     * @param productType
     * @param chnlGroup
     */
    void modifyUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo, Integer productType,
            CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 获取上行信道统计信息列表
     * 
     * @param cmcId
     *            CMC ID
     * @return List<CmcUpChannelCounterInfo> 上行信道统计信息列表
     */
    List<CmcUpChannelCounterInfo> getUpChannelStaticInfoList(Long cmcId);

    /**
     * 获取上行信道信号质量列表
     * 
     * @param cmcId
     *            CMC ID
     * @return List<CmcUpChannelSignalQualityInfo> 上行信道信号质量列表
     */
    List<CmcUpChannelSignalQualityInfo> getUpChannelSignalQualityInfoList(Long cmcId);

    /**
     * 获取上行信道信号质量列表
     * 
     * @param cmcId
     *            CMC ID
     * @return List<CmcUsSignalQualityInfo> 上行信道信号质量列表
     */
    List<CmcUsSignalQualityInfo> getUsSignalQualityInfoList(Long cmcId);

    /**
     * 刷新cc上行信道基本信息
     * 
     * @param cmcId
     *            Long
     * @param productType
     *            Integer
     */
    void refreshUpChannelBaseInfo(Long cmcId, Integer productType, CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 刷新上行通道信号质量
     * 
     * @param cmcId
     *            Long
     * @param productType
     *            Integer
     */
    void refreshUpChannelSignalQualityInfo(Long cmcId, Integer productType);

    /**
     * 获取除cmcPortId指定的通道外CC上的通道
     * 
     * @param cmcPortId
     *            Long
     * @return List<CmcUpChannelBaseShowInfo>
     */
    List<CmcUpChannelBaseShowInfo> getUpChannelListByPortId(Long cmcPortId);

    /**
     * 获取所有上行开启信道的中心频率（开启的docsis和IPQAM信道） 获取除cmcPortId指定的通道外CC上的通道
     * 
     * @param cmcId
     * 
     */
    List<CmcUpChannelBaseShowInfo> getUpChannelFrequencyListByCmcId(Long cmcId);

    /**
     * 通过cmcId和channelId获取channleIndex
     * 
     * @param cmcId
     * @param channleId
     * @return
     */
    Long getChannleIndex(Long cmcId, Integer channleId);
    
    /**
     * 通过cmcId和channelIndex获取上行信道的基本信息
     * 
     * @param cmcId
     * @param upChannleIndex
     * @return
     */
    CmcUpChannelBaseShowInfo getUpChannelBaseInfo(Long cmcId, Long upChannleIndex);

    /**
     * 修改上行信道频点和频宽（实时频谱页面）
     * 
     * @param chl
     * @param productType
     */
    void modifyUpChannelForSpe(CmcUpChannelBaseInfo chl, Integer productType);

    List<CmtsModulationEntry> getDocsIfCmtsModTypeList(Long cmcId);
}
