/***********************************************************************
 * $Id: CmcUpChannelDaoImpl.java,v1.0 2013-10-31 上午9:42:29 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.upchannel.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.upchannel.dao.CmcUpChannelDao;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.domain.CmcUsSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelCounterInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelRanging;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author dosion
 * @created @2013-10-31-上午9:42:29
 * 
 */
@Repository("upChannelDao")
public class CmcUpChannelDaoImpl extends MyBatisDaoSupport<Object> implements CmcUpChannelDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.upchannel.domain.CmcUpChannel";
    }

    @Override
    public List<CmcUpChannelBaseShowInfo> getUpChannelBaseShowInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getUpChannelBaseShowInfoList", cmcId);
    }

    @Override
    public CmcUpChannelBaseShowInfo getUpChannelBaseShowInfo(Long cmcPortId) {
        return (CmcUpChannelBaseShowInfo) getSqlSession().selectOne(getNameSpace() + "getUpChannelBaseShowInfo",
                cmcPortId);
    }

    @Override
    public void updateUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo) {
        getSqlSession().update(getNameSpace() + "updateUpChannelBaseShowInfo", cmcUpChannelBaseShowInfo);
    }

    @Override
    public List<CmcUpChannelCounterInfo> getUpChannelStaticInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getUpChannelStaticInfoList", cmcId);
    }

    @Override
    public List<CmcUpChannelSignalQualityInfo> getUpChannelSignalQualityInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getUpChannelSignalQualityInfoList", cmcId);
    }

    @Override
    public void updateUpChannelBaseInfo(CmcUpChannelBaseInfo cmcUpChannelBaseInfo) {
        getSqlSession().update(getNameSpace() + "updateUpChannelBaseInfo", cmcUpChannelBaseInfo);
    }

    @Override
    public List<CmcUpChannelBaseShowInfo> getUpChannelListByPortId(Long cmcPortId) {
        return getSqlSession().selectList(getNameSpace() + "getUpChannelListByPortId", cmcPortId);
    }

    @Override
    public List<CmcUsSignalQualityInfo> getUsSignalQualityInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getUsSignalQualityInfoList", cmcId);
    }

    @Override
    public List<CmcUpChannelBaseShowInfo> getUpChannelOnListByCmcId(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getUpChannelOnListByCmcId", cmcId);
    }

    @Override
    public Long getChannleIndex(Long cmcId, Integer channleId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("channelId", channleId);
        return getSqlSession().selectOne(getNameSpace("getChannleIndex"), map);
    }

    @Override
    public Long getPortId(Long cmcId, Long channelIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("channelIndex", channelIndex);
        return getSqlSession().selectOne(getNameSpace("getPortId"), map);
    }

    @Override
    public void updateUpChannelSignalQuality(CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcPortId", cmcUpChannelSignalQualityInfo.getCmcPortId());
        map.put("docsIf3SignalPower", cmcUpChannelSignalQualityInfo.getDocsIf3SignalPower());
        getSqlSession().update(getNameSpace() + "updateUpChannelSignalQuality", map);
    }

    @Override
    public void updateUpChannelRanging(CmcUpChannelRanging cmcUpChannelRanging) {
        getSqlSession().update(getNameSpace() + "updateUpChannelRanging", cmcUpChannelRanging);
    }

    @Override
    public CmcUpChannelBaseShowInfo selectUpChannelBaseInfo(Long cmcId, Long upChannleIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("upChannleIndex", upChannleIndex);
        return getSqlSession().selectOne(getNameSpace() + "selectUpChannelBaseInfo", map);
    }

}
