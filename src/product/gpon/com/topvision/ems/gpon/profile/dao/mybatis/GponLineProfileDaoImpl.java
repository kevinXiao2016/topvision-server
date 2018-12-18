/***********************************************************************
 * $Id: GponLineProfileDaoImpl.java,v1.0 2016年12月20日 上午11:54:03 $
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

import com.topvision.ems.gpon.profile.dao.GponLineProfileDao;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGem;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileGemMap;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponLineProfileTcont;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016年12月20日-上午11:54:03
 *
 */
@Repository("gponLineProfileDao")
public class GponLineProfileDaoImpl extends MyBatisDaoSupport<Object> implements GponLineProfileDao {
    @Override
    protected String getDomainName() {
        return "GponLineProfile";
    }

    @Override
    public List<GponLineProfileInfo> selectGponLineProfileInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectGponLineProfileInfoList"), entityId);
    }

    @Override
    public List<GponLineProfileTcont> selectGponLineProfileTcontList(Long entityId, Integer profileIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        return getSqlSession().selectList(getNameSpace("selectGponLineProfileTcontList"), map);
    }

    @Override
    public List<GponLineProfileGem> selectGponLineProfileGemList(Long entityId, Integer profileIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        return getSqlSession().selectList(getNameSpace("selectGponLineProfileGemList"), map);
    }

    @Override
    public List<GponLineProfileGemMap> selectGponLineProfileGemMapList(Long entityId, Integer profileIndex,
            Integer gemIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("gemIndex", gemIndex);
        return getSqlSession().selectList(getNameSpace("selectGponLineProfileGemMapList"), map);
    }

    @Override
    public GponLineProfileInfo selectGponLineProfileInfo(Long entityId, Integer profileIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        return getSqlSession().selectOne(getNameSpace("selectGponLineProfileInfo"), map);
    }

    @Override
    public GponLineProfileTcont selectGponLineProfileTcont(Long entityId, Integer profileIndex, Integer tcontIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("tcontIndex", tcontIndex);
        return getSqlSession().selectOne(getNameSpace("selectGponLineProfileTcont"), map);
    }

    @Override
    public GponLineProfileGem selectGponLineProfileGem(Long entityId, Integer profileIndex, Integer gemIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("gemIndex", gemIndex);
        return getSqlSession().selectOne(getNameSpace("selectGponLineProfileGem"), map);
    }

    @Override
    public GponLineProfileGemMap selectGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex,
            Integer gemMapIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("gemIndex", gemIndex);
        map.put("gemMapIndex", gemMapIndex);
        return getSqlSession().selectOne(getNameSpace("selectGponLineProfileGemMap"), map);
    }

    @Override
    public void insertGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo) {
        getSqlSession().insert(getNameSpace("insertGponLineProfileInfo"), gponLineProfileInfo);
    }

    @Override
    public void insertGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont) {
        getSqlSession().insert(getNameSpace("insertGponLineProfileTcont"), gponLineProfileTcont);
    }

    @Override
    public void insertGponLineProfileGem(GponLineProfileGem gponLineProfileGem) {
        getSqlSession().insert(getNameSpace("insertGponLineProfileGem"), gponLineProfileGem);
    }

    @Override
    public void insertGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap) {
        getSqlSession().insert(getNameSpace("insertGponLineProfileGemMap"), gponLineProfileGemMap);
    }

    @Override
    public void updateGponLineProfileInfo(GponLineProfileInfo gponLineProfileInfo) {
        getSqlSession().update(getNameSpace("updateGponLineProfileInfo"), gponLineProfileInfo);
    }

    @Override
    public void updateGponLineProfileTcont(GponLineProfileTcont gponLineProfileTcont) {
        getSqlSession().update(getNameSpace("updateGponLineProfileTcont"), gponLineProfileTcont);
    }

    @Override
    public void updateGponLineProfileGem(GponLineProfileGem gponLineProfileGem) {
        getSqlSession().update(getNameSpace("updateGponLineProfileGem"), gponLineProfileGem);
    }

    @Override
    public void updateGponLineProfileGemMap(GponLineProfileGemMap gponLineProfileGemMap) {
        getSqlSession().update(getNameSpace("updateGponLineProfileGemMap"), gponLineProfileGemMap);
    }

    @Override
    public void deleteGponLineProfileInfo(Long entityId, Integer gponLineProfileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("gponLineProfileId", gponLineProfileId);
        getSqlSession().delete(getNameSpace("deleteGponLineProfileInfo"), map);
    }

    @Override
    public void deleteGponLineProfileTcont(Long entityId, Integer profileIndex, Integer tcontIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("tcontIndex", tcontIndex);
        getSqlSession().delete(getNameSpace("deleteGponLineProfileTcont"), map);
    }

    @Override
    public void deleteGponLineProfileGem(Long entityId, Integer profileIndex, Integer gemIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("gemIndex", gemIndex);
        getSqlSession().delete(getNameSpace("deleteGponLineProfileGem"), map);
    }

    @Override
    public void deleteGponLineProfileGemMap(Long entityId, Integer profileIndex, Integer gemIndex, Integer gemMapIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("gemIndex", gemIndex);
        map.put("gemMapIndex", gemMapIndex);
        getSqlSession().delete(getNameSpace("deleteGponLineProfileGemMap"), map);
    }

    @Override
    public Integer getMappingModeByProfileId(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("getMappingModeByProfileId"), map);
    }

}
