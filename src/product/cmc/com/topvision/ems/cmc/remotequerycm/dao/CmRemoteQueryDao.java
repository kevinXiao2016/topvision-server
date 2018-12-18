/***********************************************************************
 * $Id: CmRemoteQueryDao.java,v1.0 2014-2-10 下午4:50:25 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.dao;

import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;

/**
 * @author YangYi
 * @created @2014-2-10-下午4:50:25
 *
 */
public interface CmRemoteQueryDao {


    /**
     * 根据CmcId和上行信道Index 查询上行信道基本信息
     * 
     * @param cmcId
     * @param upChanIndex
     * @return
     */
    CmcUpChannelBaseInfo getUpChanInfo(Long cmcId, Long upChanIndex);

    /**
     * 根据CmcId和上行信道Id 查询上行信道基本信息
     * 
     * @param cmcId
     * @param upChanIndex
     * @return
     */
    CmcUpChannelBaseInfo getUpChanInfoByChanId(Long cmcId, Long upChanId);

    /**
     * 根据CmcId和下行信道Index 查询下行信道基本信息
     * 
     * @param cmcId
     * @param downChanIndex
     * @return
     */
    CmcDownChannelBaseInfo getDownChanInfo(Long cmcId, Long downChanIndex);

    /**
     * 根据CmcId和下行信道Id 查询下行信道基本信息
     * 
     * @param cmcId
     * @param downChanIndex
     * @return
     */
    CmcDownChannelBaseInfo getDownChanInfoByChanId(Long cmcId, Long downChanId);

    /**
     * 根据8800A的CMCID查询其OLT的IP
     * 
     * @param cmcId
     * @return
     */
    String getOltIpByOnuId(Long cmcId);

}
