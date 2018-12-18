package com.topvision.ems.cmc.downchannel.service;

import java.util.List;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamBaseInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamISInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamMappings;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamOSInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamStatusInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcIpqamInfo;
import com.topvision.ems.cmc.downchannel.domain.TxPowerLimit;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelStaticInfo;

/**
 * CC 设备对ACL的功能管理 目前只有CC8800B支持ACL功能
 * 
 * @author lzs
 * @created @2013-4-20-下午04:01:41
 * 
 */
public interface CmcDownChannelService {
    
    /**
     * 获取该CC当前的下行电平限制
     * @param cmcId
     * @return
     */
    List<TxPowerLimit> getDownChannelTxPowerLimt(Long cmcId, Long cmcIndex);

    /**
     * 获取下行信道基本信息列表
     * 
     * @param cmcId
     *            CMC ID
     * @return List<CmcDownChannelBaseShowInfo> 下行信道基本信息
     */
    List<CmcDownChannelBaseShowInfo> getDownChannelBaseShowInfoList(Long cmcId);

    /**
     * 获取下行信道基本信息
     * 
     *
     *
     * @param cmcAttribute
     * @param portId
     *            port ID
     * @return CmcDownChannelBaseShowInfo 下行信道基本信息
     */
    CmcDownChannelBaseShowInfo getDownChannelBaseShowInfo(CmcAttribute cmcAttribute, Long portId);

    /**
     * 修改下行信道基本信息
     * 
     * @param cmcDownChannelBaseShowInfo
     *            所要修改的下行信道基本信息
     * @param productType
     *            Integer
     */
    void modifyDownChannelBaseShowInfo(CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo, Integer productType);

    /**
     * 获取下行信道统计信息列表
     * 
     * @param cmcId
     *            CMC ID
     * @return List<CmcDownChannelStaticInfo> 下行信道统计信息列表
     */
    List<CmcDownChannelStaticInfo> getDownChannelStaticInfoList(Long cmcId);

    /**
     * 刷新下行通道基本信息
     * 
     * @param cmcId
     *            Long
     * @param productType
     *            Integer
     */
    void refreshDownChannelBaseInfo(Long cmcId, Integer productType);

    /**
     * 刷新下行通道统计信息
     * 
     * @param cmcId
     *            Long
     * @param productType
     *            Integer
     */
    void refreshDownChannelStaticInfo(Long cmcId, Integer productType);

    /**
     * 获取除cmcPortId指定的通道外CC上的通道
     * 
     * @param cmcPortId
     *            Long
     * @return List<CmcDownChannelBaseShowInfo>
     */
    List<CmcDownChannelBaseShowInfo> getDownChannelListByPortId(Long cmcPortId);

    /**
     * 得到下行信道开启的数量
     * 
     * @param cmcId
     *            Long
     * @return Long
     */
    Long getDownChannelAdminStatusUpNum(Long cmcId);

    /**
     * 获取下行信道+IPQAM基本信息
     * 
     * @param cmcPortId
     * @return CmcDSIpqamBaseInfo 下行信道+IPQAM基本信息
     */
    CmcDSIpqamBaseInfo getDownChannelIPQAMInfo(Long cmcPortId);

    /**
     * 获取下行信道IPQAM信息数目
     * 
     * @param cmcId
     * 
     * @return
     */
    Integer getDownChannelIPQAMListSize(Long cmcId);

    /**
     * 修改下行信道管理状态信息,IPQAM
     * 
     * @param cmcId
     * @param channelIds
     *            --多个channelId按位计算值
     * @param status
     * @return
     */
    String setChannelsAdminStatus(Long cmcId, Integer channelIds, Integer status);

    /**
     * 修改CC8800B下行信道IPQAM基本信息 必须配置DOCSIS部分
     * 
     * @param cmcId
     * @param ipqam
     * @return
     */
    String modifyChannelIpqamBaseInfo(Long cmcId, CmcDSIpqamBaseInfo ipqam);

    /**
     * 获取下行信道IPQAM信息列表
     * 
     * @param cmcId
     * 
     * @return
     */
    List<CmcDSIpqamBaseInfo> getDownChannelIPQAMInfoList(Long cmcId);

    /**
     * 获取IPQAM信道状态信息列表
     * 
     * @param cmcId
     * 
     * @return
     */
    List<CmcDSIpqamStatusInfo> queryCCIpqamOutPutStatusList(Long cmcId);

    /**
     * 获取IPQAM信道映射信息列表
     * 
     * @param cmcId
     * 
     * @return
     */
    List<CmcDSIpqamMappings> queryIpqamStreamMappingsList(Long cmcId);

    /**
     * 获取IPQAM信道映射信息
     * 
     * @param mappingId
     * 
     * @return
     */
    CmcDSIpqamMappings queryIpqamStreamMappingsById(Long mappingId);

    /**
     * 刷新IPQAM信道状态信息及映射信息
     * 
     * @param cmcId
     */
    void refreshIpQamMappingsStatus(Long cmcId);

    /**
     * 修改IPQAM信道映射信息 action 1:删除 2:修改 3:增加
     * 
     * @param cmcId
     * @param mappingsList
     */
    void modifyIpqamMappingsList(Long cmcId, List<CmcDSIpqamMappings> mappingsList, Integer action);

    /**
     * 获取IPQAM信道输入节目流信息
     * 
     * @param cmcId
     * @return
     */
    List<CmcDSIpqamISInfo> showIpqamInputStreamInfoList(Long cmcId);

    /**
     * 获取IPQAM信道输出节目流信息
     * 
     * @param cmcId
     * @return
     */
    List<CmcDSIpqamOSInfo> showIpqamOutputStreamInfoList(Long cmcId);

    /**
     * 刷新IPQAM信道节目流信息
     * 
     * @param cmcId
     */
    void refreshIpqamStreamInfoList(Long cmcId);

    /**
     * 获取所有下行开启信道的中心频率（开启的docsis和IPQAM信道）
     * 
     * @param cmcId
     * 
     */
    List<CmcDownChannelBaseShowInfo> getDownChannelFrequencyListByCmcId(Long cmcId);

    /**
     * 获取IPQAM的IP类信息
     * 
     * @param cmcId
     * @return
     */
    CmcIpqamInfo queryCCIpqamIpInfo(Long cmcId);

    /**
     * 修改IPQAM的IP类信息
     * 
     * @param cmcIpqamInfo
     * @param cmcId
     * @return
     */
    String modifyCCIpqamIpInfo(CmcIpqamInfo cmcIpqamInfo, Long cmcId);

}
