/***********************************************************************
 * $Id: gponProfileDaoImpl.java,v1.0 2016年10月30日 下午5:50:06 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.gpon.profile.dao.GponProfileDao;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanAggregation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTranslation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTrunk;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2016年10月30日-下午5:50:06
 *
 */
@Repository("gponProfileDao")
public class GponProfileDaoImpl extends MyBatisDaoSupport<Object> implements GponProfileDao {
    @Override
    protected String getDomainName() {
        return "GponProfile";
    }

    @Override
    public List<GponSrvProfilePortVlanAggregation> selectGponSrvProfilePortVlanAggregationList(Long entityId) {
        SqlSession sqlSession = getSqlSession();
        return sqlSession.selectList(getNameSpace("selectGponSrvProfilePortVlanAggregationList"), entityId);
    }

    @Override
    public List<GponSrvProfilePortNumProfile> selectGponSrvProfilePortNumProfileList(Long entityId) {
        SqlSession sqlSession = getSqlSession();
        return sqlSession.selectList(getNameSpace("selectGponSrvProfilePortNumProfileList"), entityId);
    }

    @Override
    public List<GponSrvProfileCfg> selectGponSrvProfileCfgList(Long entityId) {
        SqlSession sqlSession = getSqlSession();
        return sqlSession.selectList(getNameSpace("selectGponSrvProfileCfgList"), entityId);
    }

    @Override
    public List<GponSrvProfilePortVlanTrunk> selectGponSrvProfilePortVlanTrunkList(Long entityId) {
        SqlSession sqlSession = getSqlSession();
        return sqlSession.selectList(getNameSpace("selectGponSrvProfilePortVlanTrunkList"), entityId);
    }

    @Override
    public List<GponSrvProfilePortVlanTranslation> selectGponSrvProfilePortVlanTranslationList(Long entityId) {
        SqlSession sqlSession = getSqlSession();
        return sqlSession.selectList(getNameSpace("selectGponSrvProfilePortVlanTranslationList"), entityId);
    }

    @Override
    public void deleteGponSrvProfilePortNumProfile(GponSrvProfilePortNumProfile object) {
        SqlSession sqlSession = getSqlSession();
        sqlSession.delete(getNameSpace("deleteGponSrvProfilePortNumProfile"), object);
    }

    @Override
    public void deleteGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig object) {
        SqlSession sqlSession = getSqlSession();
        sqlSession.delete(getNameSpace("deleteGponSrvProfileEthPortConfig"), object);
    }

    @Override
    public void deleteGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg object) {
        SqlSession sqlSession = getSqlSession();
        sqlSession.delete(getNameSpace("deleteGponSrvProfilePortVlanCfg"), object);
    }

    @Override
    public void deleteGponSrvProfileCfg(GponSrvProfileCfg object) {
        SqlSession sqlSession = getSqlSession();
        sqlSession.delete(getNameSpace("deleteGponSrvProfileCfg"), object);
    }
    
}
