/***********************************************************************
 * $Id: CmtsChannelDao.java,v1.0 2013-8-8 上午11:53:17 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.dao;

import java.util.List;

import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmts.domain.CmtsUpLinkPort;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2013-8-8-上午11:53:17
 *
 */
public interface CmtsChannelDao extends BaseEntityDao<Entity>{
    /**
     * 获取cmts上联口(iftype = 6 or 117)
     * @param cmcId
     * @return
     */
    List<CmtsUpLinkPort> selectUpLinkPortList(Long cmcId); 
    
    /**
     * 获取快照中用到的端口信息
     * 
     * @param cmcId
     *            Long
     * @return List<ChannelPerfInfo>
     */
    List<ChannelPerfInfo> selectCmtsChannelPerfInfoList(Long cmcId);

    /**
     * 获取快照中用到的上行信道信息
     * @param cmcId
     * @return
     */
    List<CmcUpChannelBaseShowInfo> selectUpChannelBaseShowInfoList(Long cmcId);
}
