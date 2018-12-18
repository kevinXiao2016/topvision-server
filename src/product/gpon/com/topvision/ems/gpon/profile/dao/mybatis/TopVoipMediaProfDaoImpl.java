/***********************************************************************
 * $Id: TopVoipMediaProfDaoImpl.java,v1.0 2017年6月21日 上午9:30:28 $
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

import com.topvision.ems.gpon.profile.dao.TopVoipMediaProfDao;
import com.topvision.ems.gpon.profile.facade.domain.TopVoipMediaProfInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年6月21日-上午9:30:28
 *
 */
@Repository("topVoipMediaProfDao")
public class TopVoipMediaProfDaoImpl extends MyBatisDaoSupport<Object> implements TopVoipMediaProfDao {

    @Override
    protected String getDomainName() {
        return "TopVoipMediaProf";
    }

    @Override
    public List<TopVoipMediaProfInfo> selectTopVoipMediaProfInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectTopVoipMediaProfInfoList"), entityId);
    }

    @Override
    public TopVoipMediaProfInfo selectTopVoipMediaProfInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("selectTopVoipMediaProfInfo"), map);
    }

    @Override
    public void insertTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo) {
        getSqlSession().insert(getNameSpace("insertTopVoipMediaProfInfo"), topVoipMediaProfInfo);
    }

    @Override
    public void updateTopVoipMediaProfInfo(TopVoipMediaProfInfo topVoipMediaProfInfo) {
        getSqlSession().update(getNameSpace("updateTopVoipMediaProfInfo"), topVoipMediaProfInfo);
    }

    @Override
    public void deleteTopVoipMediaProfInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        getSqlSession().delete(getNameSpace("deleteTopVoipMediaProfInfo"), map);
    }

}
