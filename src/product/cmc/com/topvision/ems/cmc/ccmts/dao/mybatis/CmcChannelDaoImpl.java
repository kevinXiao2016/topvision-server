/***********************************************************************
 * $Id: CmcAclDaoImpl.java,v1.0 2013-5-3 下午01:40:57 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.dao.CmcChannelDao;
import com.topvision.ems.cmc.ccmts.domain.ChannelPerfInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.network.domain.Port;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lzs
 * @created @2013-5-3-下午01:40:57
 * 
 */
@Repository("cmcChannelDao")
public class CmcChannelDaoImpl extends MyBatisDaoSupport<Object> implements CmcChannelDao {
    private Logger logger = LoggerFactory.getLogger(CmcChannelDaoImpl.class);

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.ccmts.domain.CmcChannel";
    }

    public Port selectCmtsPort(Long entityId, Long ifIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("ifIndex", ifIndex);
        return (Port) getSqlSession().selectOne(getNameSpace() + "selectCmtsPort", map);
    }

    @Override
    public Long getChannelIndexByPortId(Long cmcPortId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getChannelIndexByPortId", cmcPortId);
    }

    @Override
    public List<ChannelPerfInfo> getCmcChannelPerfInfoList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcChannelPerfInfoList", cmcId);
    }

    @Override
    public void batchUpdateChannelAdminStatus(final Long cmcId, final List<Long> ifIndexs, final Integer status) {
        SqlSession session = this.getBatchSession();
        try {
            for (Long ifIndex : ifIndexs) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cmcId", cmcId);
                map.put("ifIndex", ifIndex);
                map.put("status", status);
                session.update(getNameSpace() + "updateChannelAdminStatus", map);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public List<ChannelCmNum> getChannelCmNumList(Long cmcId, Integer channelType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("channelType", channelType);
        return getSqlSession().selectList(getNameSpace() + "getChannelCmNumList", map);
    }
    
    @Override
    public void updateCmcPortIfName(String ifName, Long cmcPortId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ifName", ifName);
        map.put("cmcPortId", cmcPortId);
        getSqlSession().update(getNameSpace() + "updateCmcPortIfName", map);
    }
    
    @Override
    public void updatePortIfName(String ifName, Long cmcId, Long channelIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ifName", ifName);
        map.put("cmcId", cmcId);
        map.put("channelIndex", channelIndex);
        getSqlSession().update(getNameSpace() + "updatePortIfName", map);
    }

    @Override
    public void updateCmcPort(CmcPort updatePortInfo) {
        getSqlSession().update(getNameSpace() + "updatePortInfo", updatePortInfo);
    }

}
