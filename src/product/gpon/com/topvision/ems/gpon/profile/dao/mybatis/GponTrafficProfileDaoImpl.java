/***********************************************************************
 * $Id: GponTrafficProfileDaoImpl.java,v1.0 2016年12月20日 上午11:21:29 $
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

import com.topvision.ems.gpon.profile.dao.GponTrafficProfileDao;
import com.topvision.ems.gpon.profile.facade.domain.GponTrafficProfileInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016年12月20日-上午11:21:29
 *
 */
@Repository("gponTrafficProfileDao")
public class GponTrafficProfileDaoImpl extends MyBatisDaoSupport<Object> implements GponTrafficProfileDao {

    @Override
    protected String getDomainName() {
        return "GponTrafficProfile";
    }

    @Override
    public List<GponTrafficProfileInfo> selectGponTrafficProfileInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectGponTrafficProfileInfoList"), entityId);
    }

    @Override
    public GponTrafficProfileInfo selectGponTrafficProfileInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("selectGponTrafficProfileInfo"), map);
    }

    @Override
    public void insertGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo) {
        getSqlSession().insert(getNameSpace("insertGponTrafficProfileInfo"), gponTrafficProfileInfo);
    }

    @Override
    public void updateGponTrafficProfileInfo(GponTrafficProfileInfo gponTrafficProfileInfo) {
        getSqlSession().update(getNameSpace("updateGponTrafficProfileInfo"), gponTrafficProfileInfo);
    }

    @Override
    public void deleteGponTrafficProfileInfo(Long entityId, Integer gponTrafficProfileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("gponTrafficProfileId", gponTrafficProfileId);
        getSqlSession().delete(getNameSpace("deleteGponTrafficProfileInfo"), map);
    }

}
