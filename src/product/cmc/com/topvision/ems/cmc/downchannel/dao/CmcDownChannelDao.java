/***********************************************************************
 * $Id: CmcAclDao.java,v1.0 2013-5-3 下午01:25:02 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.dao;

import java.util.List;

import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.domain.*;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelStaticInfo;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lzs
 * @created @2013-5-3-下午01:25:02
 * 
 */
public interface CmcDownChannelDao extends BaseEntityDao<Object> {
    /**
     * 获取下行信道基本信息列表
     * 
     * @param cmcId
     *            cmc设备id
     * @return List<CmcDownChannelBaseShowInfo> 下行信道基本信息列表
     */
    List<CmcDownChannelBaseShowInfo> getDownChannelBaseShowInfoList(Long cmcId);

    /**
     * 获取下行信道基本信息
     * 
     * @param portId
     *            单条下行信道数据库id
     * @return CmcDownChannelBaseShowInfo 单条下行信道基本信息
     */
    CmcDownChannelBaseShowInfo getDownChannelBaseShowInfo(Long cmcPortId);

    /**
     * 修改下行信道基本信息
     * 
     * @param cmcDownChannelBaseShowInfo
     *            单条下行信道基本信息
     */
    void updateDownChannelBaseInfo(CmcDownChannelBaseInfo cmcDownChannelBaseInfo);

    /**
     * 获取下行信道统计信息列表
     * 
     * @param cmcId
     *            cmc设备id
     * @return List<CmcDownChannelStaticInfo> 下行信道统计信息列表
     */
    List<CmcDownChannelStaticInfo> getDownChannelStaticInfoList(Long cmcId);

    /**
     * 获取除cmcPortId指定的通道外CC上的下行通道
     * 
     * @param cmcPortId
     * @return
     */
    List<CmcDownChannelBaseShowInfo> getDownChannelListByPortId(Long cmcPortId);

    /**
     * 获取下行通道管理状态开启的数量
     * 
     * @param cmcId
     * @return
     */
    Long getDownChannelAdminStatusUpNum(Long cmcId);

    /**
     * 获取下行信道IPQAM信息列表
     * 
     * @param cmcId
     * @return
     */
    List<CmcDSIpqamBaseInfo> getDownChannelIPQAMInfoList(Long cmcId);

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
     * 获取IPQAM信道输出节目流信息
     * 
     * @param cmcId
     * @return
     */
    List<CmcDSIpqamOSInfo> showIpqamOutputStreamInfoList(Long cmcId);

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
     * 获取IPQAM信道输入节目流信息
     * 
     * @param cmcId
     * @return
     */
    List<CmcDSIpqamISInfo> showIpqamInputStreamInfoList(Long cmcId);

    /**
     * 获取CC在线的下行通道
     * 
     * @param cmcId
     * @return
     */
    List<CmcDownChannelBaseShowInfo> getDownChannelOnListByCmcId(Long cmcId);

    /**
     * 根据cmcId和channelIndex获取对应的portId
     * 
     * @param cmcId
     * @param channelIndex
     * @return
     */
    Long getPortId(Long cmcId, Long channelIndex);

    /**
     * 批量插入更新8800B设备下行信道IPQAM信息
     * 
     * @param cc8800bHttpDownChannelIPQAMs
     * @param cmcId
     */
    void batchInsertCC8800BDSIPQAMBaseList(final List<CmcDSIpqamBaseInfo> cc8800bHttpDownChannelIPQAMs, final Long cmcId);

    /**
     * 批量插入更新8800B设备IPQAM信道IPQAM状态信息
     * 
     * @param cc8800bHttpDSIpqamStatusInfos
     * @param cmcId
     */
    void batchInsertCC8800BDSIPQAMStatusList(final List<CmcDSIpqamStatusInfo> cc8800bHttpDSIpqamStatusInfos,
            final Long cmcId);

    /**
     * 批量插入更新8800B设备IPQAM信道映射信息信息
     * 
     * @param cc8800bHttpDSIpqamMappings
     * @param cmcId
     */
    void batchInsertCC8800BDSIPQAMMappingsList(final List<CmcDSIpqamMappings> cc8800bHttpDSIpqamMappings,
            final Long cmcId);

    /**
     * 批量插入更新8800B设备IPQAM信道输入流信息
     * 
     * @param cc8800bHttpIpQamISInfos
     * @param cmcId
     */
    void batchInsertCC8800BDSIPQAMISInfoList(final List<CmcDSIpqamISInfo> cc8800bHttpIpQamISInfos, final Long cmcId);

    /**
     * 批量插入更新8800B设备IPQAM信道输出流信息
     * 
     * @param cc8800bHttpIpQamOSInfos
     * @param cmcId
     */
    void batchInsertCC8800BDSIPQAMOsInfoList(final List<CmcDSIpqamOSInfo> cc8800bHttpIpQamOSInfos, final Long cmcId);

    /**
     * 插入或更新IPQAM的IP地址信息
     * 
     * @param cmcIpqamInfo
     * @param cmcId
     */
    void insertorUpdateCC8800BIPQAMIpInfo(CmcIpqamInfo cmcIpqamInfo, Long cmcId);

    /**
     * 获取IPQAM的IP地址信息
     * 
     * @param cmcId
     * @return
     */
    CmcIpqamInfo queryforCC8800BIPQAMIpInfo(Long cmcId);

    /**
     * 批量插入更新下行信道统计信息
     * 
     * @param cmcDownChannelStaticInfoList
     * @param cmcId
     */
    void batchInsertCmcDownChannelStaticInfo(final List<CmcDownChannelStaticInfo> cmcDownChannelStaticInfoList,
            Long cmcId);

    void batchRefreshCmcTxPowerLimit(List<TxPowerLimit> list, long entityId, Long cmcIndex);

    List<TxPowerLimit> getTxPowerLimit(Long entityId,Long cmcIndex);
}
