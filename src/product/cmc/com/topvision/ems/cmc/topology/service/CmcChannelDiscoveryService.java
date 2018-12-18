/***********************************************************************
 * $Id: CmcChannelDiscoveryService.java,v1.0 2017年6月3日 上午11:14:49 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.service;

import java.util.List;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;

/**
 * 拓扑时更新信道相关业务
 * 
 * @author vanzand
 * @created @2017年6月3日-上午11:14:49
 *
 */
public interface CmcChannelDiscoveryService {
    /**
     * 
     * 
     * @param cmcUpChannelBaseInfoList
     * @param cmcUpChannelSignalQualityInfos
     * @param cmcDownChannelBaseInfos
     * @param cmcPorts
     * @param entityId
     */
    void syncCmcChannelBaseInfo(List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfoList,
            List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos,
            List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos, List<CmcPort> cmcPorts, Long entityId);
}
