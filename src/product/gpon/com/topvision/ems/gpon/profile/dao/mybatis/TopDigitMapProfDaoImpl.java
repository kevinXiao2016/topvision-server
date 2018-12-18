/***********************************************************************
 * $Id: TopDigitMapProfDaoImpl.java,v1.0 2017年6月21日 下午1:37:59 $
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

import com.topvision.ems.gpon.profile.dao.TopDigitMapProfDao;
import com.topvision.ems.gpon.profile.facade.domain.TopDigitMapProfInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年6月21日-下午1:37:59
 *
 */
@Repository("topDigitMapProfDao")
public class TopDigitMapProfDaoImpl extends MyBatisDaoSupport<Object> implements TopDigitMapProfDao {
    @Override
    protected String getDomainName() {
        return "TopDigitMapProf";
    }

    @Override
    public List<TopDigitMapProfInfo> selectTopDigitMapProfInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectTopDigitMapProfInfoList"), entityId);
    }

    @Override
    public TopDigitMapProfInfo selectTopDigitMapProfInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("selectTopDigitMapProfInfo"), map);
    }

    @Override
    public void insertTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo) {
        getSqlSession().insert(getNameSpace("insertTopDigitMapProfInfo"), topDigitMapProfInfo);
    }

    @Override
    public void updateTopDigitMapProfInfo(TopDigitMapProfInfo topDigitMapProfInfo) {
        getSqlSession().update(getNameSpace("updateTopDigitMapProfInfo"), topDigitMapProfInfo);
    }

    @Override
    public void deleteTopDigitMapProfInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        getSqlSession().delete(getNameSpace("deleteTopDigitMapProfInfo"), map);
    }
}
