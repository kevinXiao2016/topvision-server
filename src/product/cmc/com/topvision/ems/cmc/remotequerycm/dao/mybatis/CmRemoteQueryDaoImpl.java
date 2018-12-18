/***********************************************************************
 * $Id: CmRemoteQueryDaoImpl.java,v1.0 2014-2-10 下午4:50:51 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.remotequerycm.dao.CmRemoteQueryDao;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author YangYi
 * @created @2014-2-10-下午4:50:51
 *
 */
@Repository("cmRemoteQueryDao")
public class CmRemoteQueryDaoImpl extends MyBatisDaoSupport<Object> implements CmRemoteQueryDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.remotequerycm.domain.CmRemoteQuery";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.remotequerycm.dao.CmRemoteQueryDao#getUpChanInfo(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public CmcUpChannelBaseInfo getUpChanInfo(Long cmcId, Long upChanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("channelIndex", upChanIndex);
        return (CmcUpChannelBaseInfo) getSqlSession().selectOne(getNameSpace() + "getUpChanInfo", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.remotequerycm.dao.CmRemoteQueryDao#getDownChanInfo(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public CmcDownChannelBaseInfo getDownChanInfo(Long cmcId, Long downChanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("channelIndex", downChanIndex);
        return (CmcDownChannelBaseInfo) getSqlSession().selectOne(getNameSpace() + "getDownChanInfo", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.remotequerycm.dao.CmRemoteQueryDao#getUpChanInfoByChanId(java.lang.
     * Long, java.lang.Long)
     */
    @Override
    public CmcUpChannelBaseInfo getUpChanInfoByChanId(Long cmcId, Long upChanId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("channelId", upChanId);
        return (CmcUpChannelBaseInfo) getSqlSession().selectOne(getNameSpace() + "getUpChanInfoByChanId", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.remotequerycm.dao.CmRemoteQueryDao#getDownChanInfoByChanId(java.lang
     * .Long, java.lang.Long)
     */
    @Override
    public CmcDownChannelBaseInfo getDownChanInfoByChanId(Long cmcId, Long downChanId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("channelId", downChanId);
        return (CmcDownChannelBaseInfo) getSqlSession().selectOne(getNameSpace() + "getDownChanInfoByChanId", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.remotequerycm.dao.CmRemoteQueryDao#getOltIpByOnuId(java.lang.Long)
     */
    @Override
    public String getOltIpByOnuId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "getOltIpByOnuId", cmcId);
    }

}
