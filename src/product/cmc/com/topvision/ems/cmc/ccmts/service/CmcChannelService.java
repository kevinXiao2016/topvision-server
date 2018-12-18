/***********************************************************************
 * $Id: CmcService.java,v1.0 2011-10-25 下午04:29:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.network.domain.Port;
import com.topvision.framework.service.Service;

/**
 * 基本功能
 * 
 * @author loyal
 * @created @2011-10-25-下午04:29:45
 * 
 */
public interface CmcChannelService extends Service {
    /**
     * 获取cmts端口信息
     * 
     * @param entityId
     *            , ifIndex
     * 
     * @return port
     */
    Port getCmtsPort(Long entityId, Long ifIndex);

    /**
     * 刷新cmts端口信息
     * 
     * @param cmcId
     *            cmcId
     */
    void refreshCmtsPorts(Long cmcId);

    /**
     * 获取信道index
     * 
     * @param cmcPortId
     *            Long
     * @return Long
     */
    Long getChannelIndexByPortId(Long cmcPortId);

    /**
     * 获取快照中用到的端口信息
     * 
     * @param cmcId
     *            Long
     * @return List<ChannelPerfInfo>
     */
    List<ChannelPerfInfo> getCmcChannelPerfInfoList(Long cmcId);

    /**
     * 批量修改信道状态
     * 
     * @param cmcId
     *            cmcId
     * @param channelIndexs
     *            信道的ifIndex
     * @param status
     *            要修改的状态
     * @return 修改失败的列表
     */
    List<Long> batchChangeChannelAdminstatus(Long cmcId, List<Long> channelIndexs, Integer status);

    /**
     * 获取信道用户数统计信息
     * 
     * @param cmcId
     *            Long
     * @return List<ChannelCmNum>
     */
    List<ChannelCmNum> getChannelCmNumListNew(Long cmcId, Integer channelType);

    /**
     * 获取信道用户数统计信息
     *
     * @param cmcId
     *            Long
     * @return List<ChannelCmNum>
     */
    @Deprecated
    List<ChannelCmNum> getChannelCmNumList(Long cmcId);

    /**
     * 获取所有开启信道的中心频率（开启的docsis和IPQAM信道）
     * 获取除cmcPortId指定的通道外CC的通道（排除下行信道cmcPortId）
     * @param cmcPortId
     * @param cmcId
     * 
     */
    Map<String, List<?>> getDSChannelFrequencyListByPortId(Long cmcPortId, Long cmcId);

    /**
     * 获取所有开启信道的中心频率（开启的docsis和IPQAM信道）
     * 获取除cmcPortId指定的通道外CC上的通道（排除上行信道cmcPortId）
     * @param cmcPortId
     * @param cmcId
     * 
     */
    Map<String, List<?>> getUSChannelFrequencyListByPortId(Long cmcPortId, Long cmcId);

}
