/***********************************************************************
 * $Id: GponDbaProfileDaoImpl.java,v1.0 2016年12月20日 上午10:49:07 $
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

import com.topvision.ems.gpon.profile.dao.GponDbaProfileDao;
import com.topvision.ems.gpon.profile.facade.domain.GponDbaProfileInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016年12月20日-上午10:49:07
 *
 */
@Repository("gponDbaProfileDao")
public class GponDbaProfileDaoImpl extends MyBatisDaoSupport<Object> implements GponDbaProfileDao {

    @Override
    protected String getDomainName() {
        return "GponDbaProfile";
    }

    @Override
    public List<GponDbaProfileInfo> selectGponDbaProfileInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectGponDbaProfileInfoList"), entityId);
    }

    @Override
    public GponDbaProfileInfo selectGponDbaProfileInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("selectGponDbaProfileInfo"), map);
    }

    @Override
    public void insertGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo) {
        getSqlSession().insert(getNameSpace("insertGponDbaProfileInfo"), gponDbaProfileInfo);
    }

    @Override
    public void updateGponDbaProfileInfo(GponDbaProfileInfo gponDbaProfileInfo) {
        getSqlSession().update(getNameSpace("updateGponDbaProfileInfo"), gponDbaProfileInfo);
    }

    @Override
    public void deleteGponDbaProfileInfo(Long entityId, Integer gponDbaProfileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("gponDbaProfileId", gponDbaProfileId);
        getSqlSession().delete(getNameSpace("deleteGponDbaProfileInfo"), map);
    }

}
