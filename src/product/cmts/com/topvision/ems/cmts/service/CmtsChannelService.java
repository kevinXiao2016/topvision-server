/***********************************************************************
 * $Id: CmtsChannelService.java,v1.0 2013-8-8 上午11:51:29 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service;

import java.util.List;

import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmts.domain.CmtsUpLinkPort;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2013-8-8-上午11:51:29
 * 
 */
public interface CmtsChannelService extends Service {
    /**
     * 获取cmts上联口(iftype = 6 or 117)
     * 
     * @param cmcId
     * @return
     */
    List<CmtsUpLinkPort> getUpLinkPortList(Long cmcId);

    /**
     * 获取快照中用到的端口信息
     * 
     * @param cmcId
     *            Long
     * @return List<ChannelPerfInfo>
     */
    List<ChannelPerfInfo> getCmtsChannelPerfInfoList(Long cmcId);

    /**
     * 获取快照中用到的上行信道信息
     * 
     * @param cmcId
     * @return
     */
    List<CmcUpChannelBaseShowInfo> getUpChannelBaseShowInfoList(Long cmcId);

    /**
     * 修改上行信道基本信息
     * 
     * @param cmcUpChannelBaseShowInfo
     */
    void modifyUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo);

    /**
     * 修改下行信道基本信息
     * 
     * @param cmcDownChannelBaseShowInfo
     */
    void modifyDownChannelBaseShowInfo(CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo);
}
