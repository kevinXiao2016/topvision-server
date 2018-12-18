/***********************************************************************
 * $Id: TopSIPSrvProfDaoImpl.java,v1.0 2017年6月21日 上午10:29:38 $
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

import com.topvision.ems.gpon.profile.dao.TopSIPSrvProfDao;
import com.topvision.ems.gpon.profile.facade.domain.TopSIPSrvProfInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年6月21日-上午10:29:38
 *
 */
@Repository("topSIPSrvProfDao")
public class TopSIPSrvProfDaoImpl extends MyBatisDaoSupport<Object> implements TopSIPSrvProfDao {
    @Override
    protected String getDomainName() {
        return "TopSIPSrvProf";
    }

    @Override
    public List<TopSIPSrvProfInfo> selectTopSIPSrvProfInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectTopSIPSrvProfInfoList"), entityId);
    }

    @Override
    public TopSIPSrvProfInfo selectTopSIPSrvProfInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("selectTopSIPSrvProfInfo"), map);
    }

    @Override
    public void insertTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo) {
        getSqlSession().insert(getNameSpace("insertTopSIPSrvProfInfo"), topSIPSrvProfInfo);
    }

    @Override
    public void updateTopSIPSrvProfInfo(TopSIPSrvProfInfo topSIPSrvProfInfo) {
        getSqlSession().update(getNameSpace("updateTopSIPSrvProfInfo"), topSIPSrvProfInfo);
    }

    @Override
    public void deleteTopSIPSrvProfInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        getSqlSession().delete(getNameSpace("deleteTopSIPSrvProfInfo"), map);
    }
}
