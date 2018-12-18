package com.topvision.ems.cmc.ccmts.dao;

import java.util.List;

import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.network.domain.Port;
import com.topvision.framework.dao.BaseEntityDao;

public interface CmcChannelDao extends BaseEntityDao<Object> {
    /**
     * 获取cmts端口信息
     * 
     * @param entityId
     *            , ifIndex
     * @return port
     */
    Port selectCmtsPort(Long entityId, Long ifIndex);

    /**
     * 获取信道index
     * 
     * @param cmcPortId
     * @return
     */
    Long getChannelIndexByPortId(Long cmcPortId);

    /**
     * 获取快照中用到的端口信息
     * 
     * @param cmcId
     * @return
     */
    List<ChannelPerfInfo> getCmcChannelPerfInfoList(Long cmcId);

    /**
     * 批量更新通道管理状态
     * 
     * @param cmcId
     *            cmcId
     * @param ifIndexs
     *            ifIndexs
     * @param status
     *            status
     */
    void batchUpdateChannelAdminStatus(Long cmcId, List<Long> ifIndexs, Integer status);

    /**
     * 获取信道用户数统计信息
     * 
     * @param cmcId
     * @return
     */
    List<ChannelCmNum> getChannelCmNumList(Long cmcId, Integer channelType);

    /**
     * 更新ifTable（CmcPortAttribute）信息
     */
    void updateCmcPort(CmcPort cmcPort);
    
    void updateCmcPortIfName(String ifName, Long cmcPortId);
    
    void updatePortIfName(String ifName, Long cmcId, Long channelIndex);
}
