/***********************************************************************
 * $Id: TopSIPAgentProfDaoImpl.java,v1.0 2017年5月5日 下午2:00:01 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.gpon.profile.dao.TopSIPAgentProfDao;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPAgentProfInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年5月5日-下午2:00:01
 *
 */
@Repository("topSIPAgentProfDao")
public class TopSIPAgentProfDaoImpl extends MyBatisDaoSupport<Object> implements TopSIPAgentProfDao {

    @Override
    protected String getDomainName() {
        return "TopSIPAgentProf";
    }

    @Override
    public List<TopSIPAgentProfInfo> selectTopSIPAgentProfInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectTopSIPAgentProfInfoList"), entityId);
    }

    @Override
    public TopSIPAgentProfInfo selectTopSIPAgentProfInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("selectTopSIPAgentProfInfo"), map);
    }

    @Override
    public void insertTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo) {
        getSqlSession().insert(getNameSpace("insertTopSIPAgentProfInfo"), topSIPAgentProfInfo);
    }

    @Override
    public void updateTopSIPAgentProfInfo(TopSIPAgentProfInfo topSIPAgentProfInfo) {
        getSqlSession().update(getNameSpace("updateTopSIPAgentProfInfo"), topSIPAgentProfInfo);
    }

    @Override
    public void deleteTopSIPAgentProfInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        getSqlSession().delete(getNameSpace("deleteTopSIPAgentProfInfo"), map);
    }

}
