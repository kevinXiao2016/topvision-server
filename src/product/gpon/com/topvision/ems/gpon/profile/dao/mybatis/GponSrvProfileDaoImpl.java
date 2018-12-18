/***********************************************************************
 * $Id: GponSrvProfileDaoImpl.java,v1.0 2016年12月20日 下午2:16:09 $
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

import com.topvision.ems.gpon.profile.dao.GponSrvProfileDao;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileInfo;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanAggregation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTranslation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTrunk;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvPotsInfo;
import com.topvision.ems.gpon.profile.facade.domain.TopGponSrvProfile;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2016年12月20日-下午2:16:09
 *
 */
@Repository("gponSrvProfileDao")
public class GponSrvProfileDaoImpl extends MyBatisDaoSupport<Object> implements GponSrvProfileDao {
    @Override
    protected String getDomainName() {
        return "GponSrvProfile";
    }

    @Override
    public List<GponSrvProfileInfo> selectGponSrvProfileInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectGponSrvProfileInfoList"), entityId);
    }

    @Override
    public List<GponSrvProfileEthPortConfig> selectGponSrvProfileEthPortConfigList(Long entityId,
            Integer profileIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        return getSqlSession().selectList(getNameSpace("selectGponSrvProfileEthPortConfigList"), map);
    }

    @Override
    public List<GponSrvProfilePortVlanCfg> selectGponSrvProfilePortVlanCfgList(Long entityId, Integer profileIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        return getSqlSession().selectList(getNameSpace("selectGponSrvProfilePortVlanCfgList"), map);
    }

    @Override
    public GponSrvProfileInfo selectGponSrvProfileInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace("selectGponSrvProfileInfo"), map);
    }

    @Override
    public GponSrvProfileEthPortConfig selectGponSrvProfileEthPortConfig(Long entityId, Integer profileIndex,
            Integer ethPortIdIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("ethPortIdIndex", ethPortIdIndex);
        return getSqlSession().selectOne(getNameSpace("selectGponSrvProfileEthPortConfig"), map);
    }

    @Override
    public GponSrvProfilePortVlanCfg selectGponSrvProfilePortVlanCfg(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("portIndex", portIndex);
        return getSqlSession().selectOne(getNameSpace("selectGponSrvProfilePortVlanCfg"), map);
    }

    @Override
    public List<GponSrvProfilePortVlanTranslation> selectGponSrvProfilePortVlanTranslation(Long entityId,
            Integer profileIndex, Integer portTypeIndex, Integer portIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("portIndex", portIndex);
        return getSqlSession().selectList(getNameSpace("selectGponSrvProfilePortVlanTranslation"), map);
    }

    @Override
    public List<GponSrvProfilePortVlanAggregation> selectGponSrvProfilePortVlanAggregation(Long entityId,
            Integer profileIndex, Integer portTypeIndex, Integer portIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("portIndex", portIndex);
        return getSqlSession().selectList(getNameSpace("selectGponSrvProfilePortVlanAggregation"), map);
    }

    @Override
    public List<GponSrvProfilePortVlanTrunk> selectGponSrvProfilePortVlanTrunk(Long entityId, Integer profileIndex,
            Integer portTypeIndex, Integer portIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("portIndex", portIndex);
        return getSqlSession().selectList(getNameSpace("selectGponSrvProfilePortVlanTrunk"), map);
    }

    @Override
    public void insertGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo) {
        getSqlSession().insert(getNameSpace("insertGponSrvProfileInfo"), gponSrvProfileInfo);
    }

    @Override
    public void insertGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig gponSrvProfileEthPortConfig) {
        getSqlSession().insert(getNameSpace("insertGponSrvProfileEthPortConfig"), gponSrvProfileEthPortConfig);
    }

    @Override
    public void insertGponSrvProfilePortVlanTranslation(
            GponSrvProfilePortVlanTranslation gponSrvProfilePortVlanTranslation) {
        getSqlSession().insert(getNameSpace("insertGponSrvProfilePortVlanTranslation"),
                gponSrvProfilePortVlanTranslation);
    }

    @Override
    public void insertGponSrvProfilePortVlanAggregation(
            GponSrvProfilePortVlanAggregation gponSrvProfilePortVlanAggregation) {
        getSqlSession().insert(getNameSpace("insertGponSrvProfilePortVlanAggregation"),
                gponSrvProfilePortVlanAggregation);
    }

    @Override
    public void insertGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg) {
        getSqlSession().insert(getNameSpace("insertGponSrvProfilePortVlanCfg"), gponSrvProfilePortVlanCfg);
    }

    @Override
    public void insertGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk gponSrvProfilePortVlanTrunk) {
        getSqlSession().insert(getNameSpace("insertGponSrvProfilePortVlanTrunk"), gponSrvProfilePortVlanTrunk);
    }

    @Override
    public void updateGponSrvProfileInfo(GponSrvProfileInfo gponSrvProfileInfo) {
        getSqlSession().update(getNameSpace("updateGponSrvProfileInfo"), gponSrvProfileInfo);
    }

    @Override
    public void updateGponSrvProfileCfg(GponSrvProfileCfg gponSrvProfileCfg) {
        getSqlSession().update(getNameSpace("updateGponSrvProfileCfg"), gponSrvProfileCfg);
    }

    @Override
    public void updateGponSrvProfilePortNumProfile(GponSrvProfilePortNumProfile gponSrvProfilePortNumProfile) {
        getSqlSession().update(getNameSpace("updateGponSrvProfilePortNumProfile"), gponSrvProfilePortNumProfile);
    }

    @Override
    public void updateGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig gponSrvProfileEthPortConfig) {
        getSqlSession().update(getNameSpace("updateGponSrvProfileEthPortConfig"), gponSrvProfileEthPortConfig);
    }

    @Override
    public void updateGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg) {
        getSqlSession().update(getNameSpace("updateGponSrvProfilePortVlanCfg"), gponSrvProfilePortVlanCfg);
    }

    @Override
    public void updateGponSrvProfilePortVlanMode(GponSrvProfilePortVlanCfg gponSrvProfilePortVlanCfg) {
        getSqlSession().update(getNameSpace("updateGponSrvProfilePortVlanMode"), gponSrvProfilePortVlanCfg);
    }

    @Override
    public void deleteGponSrvProfileInfo(Long entityId, Integer profileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileId", profileId);
        getSqlSession().delete(getNameSpace("deleteGponSrvProfileInfo"), map);
    }

    @Override
    public void deleteGponSrvProfilePortVlanTranslation(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("portIndex", portIndex);
        map.put("vlanIndex", vlanIndex);
        getSqlSession().delete(getNameSpace("deleteGponSrvProfilePortVlanTranslation"), map);
    }

    @Override
    public void deleteGponSrvProfilePortVlanAggregation(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("portIndex", portIndex);
        map.put("vlanIndex", vlanIndex);
        getSqlSession().delete(getNameSpace("deleteGponSrvProfilePortVlanAggregation"), map);
    }

    @Override
    public void deleteGponSrvProfilePortVlanTrunk(Long entityId, Integer profileIndex, Integer portTypeIndex,
            Integer portIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("portIndex", portIndex);
        getSqlSession().delete(getNameSpace("deleteGponSrvProfilePortVlanTrunk"), map);
    }

    @Override
    public void deletePortVlanRule(Long entityId, Integer profileIndex, Integer portTypeIndex, Integer portIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("portIndex", portIndex);
        getSqlSession().delete(getNameSpace("deleteGponSrvProfilePortVlanTranslation"), map);
        getSqlSession().delete(getNameSpace("deleteGponSrvProfilePortVlanAggregation"), map);
        getSqlSession().delete(getNameSpace("deleteGponSrvProfilePortVlanTrunk"), map);
    }

    @Override
    public void updateGponSrvProfilePortVlanAggregation(GponSrvProfilePortVlanAggregation aggregation) {
        getSqlSession().update(getNameSpace("updateGponSrvProfilePortVlanAggregation"), aggregation);
    }

    @Override
    public void updateGponSrvProfilePortVlanTrunk(GponSrvProfilePortVlanTrunk trunk) {
        getSqlSession().update(getNameSpace("updateGponSrvProfilePortVlanTrunk"), trunk);
    }

    @Override
    public void updateGponSrvProfilePortVlanTranslation(GponSrvProfilePortVlanTranslation translation) {
        getSqlSession().update(getNameSpace("updateGponSrvProfilePortVlanTranslation"), translation);
    }

    @Override
    public void updateTopGponSrvProfile(TopGponSrvProfile topGponSrvProfile) {
        getSqlSession().update(getNameSpace("updateTopGponSrvProfile"), topGponSrvProfile);
    }

    @Override
    public List<TopGponSrvPotsInfo> selectTopGponSrvPotsInfoList(Long entityId, Integer profileIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        return getSqlSession().selectList(getNameSpace("selectTopGponSrvPotsInfoList"), map);
    }

    @Override
    public TopGponSrvPotsInfo selectTopGponSrvPotsInfo(Long entityId, Integer profileIndex, Integer potsIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("profileIndex", profileIndex);
        map.put("potsIndex", potsIndex);
        return getSqlSession().selectOne(getNameSpace("selectTopGponSrvPotsInfo"), map);
    }

    @Override
    public void updateTopGponSrvPotsInfo(TopGponSrvPotsInfo topGponSrvPotsInfo) {
        getSqlSession().update(getNameSpace("updateTopGponSrvPotsInfo"), topGponSrvPotsInfo);
    }
}
