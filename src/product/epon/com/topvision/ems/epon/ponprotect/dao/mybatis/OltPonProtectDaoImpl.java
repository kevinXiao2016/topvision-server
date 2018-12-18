/***********************************************************************
 * $Id: OltPonProtectDaoImpl.java,v1.0 2013-10-25 下午3:26:32 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.ponprotect.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.OltPonProtect;
import com.topvision.ems.epon.ponprotect.dao.OltPonProtectDao;
import com.topvision.ems.epon.ponprotect.domain.OltPonProtectConfig;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-下午3:26:32
 *
 */
@Repository("oltPonProtectDao")
public class OltPonProtectDaoImpl extends MyBatisDaoSupport<Object> implements OltPonProtectDao {
    @Override
    public List<OltPonProtect> getOltPonProtectList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getAllPonProtectList", entityId);
    }

    @Override
    public OltPonProtect getOltPonProtectById(Long entityId, Integer protectId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("protectId", protectId);
        return (OltPonProtect) getSqlSession().selectOne(getNameSpace() + "getOltPonProtectById", paramMap);
    }

    @Override
    public void addPonProtect(OltPonProtect oltPonProtect) {
        getSqlSession().insert(getNameSpace() + "insertPonProtect", oltPonProtect);
    }

    @Override
    public void updateOltPonProtectById(OltPonProtect oltPonProtect) {
        getSqlSession().update(getNameSpace() + "updateOltPonProtect", oltPonProtect);
    }
    @Override
    public void deleteOltPonProtectById(Long entityId, Integer protectId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("protectId", protectId);
        getSqlSession().delete(getNameSpace() + "deleteOltPonProtect", paramMap);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.ponprotect.domain.OltPonProtect";
    }

    @Override
    public List<OltPonProtectConfig> loadPPGList(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("loadPPGList"), entityId);
    }

    @Override
    public List<Integer> loadPPGArray(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("loadPPGArray"), entityId);
    }

    @Override
    public List<Long> loadAvialPorts(Map<String, String> map) {
        return this.getSqlSession().selectList(getNameSpace("loadAvialPorts"), map);
    }

    @Override
    public void deletePPG(Integer ppgId, long entityId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("ppgId", ppgId.longValue());
        map.put("entityId", entityId);
        this.getSqlSession().delete(getNameSpace("deletePPG"), map);
    }

    @Override
    public void deletePPGMembers(Integer ppgId) {
        this.getSqlSession().delete(getNameSpace("deletePPGMembers"), ppgId);
    }

    @Override
    public void batchInsertOltPonProtectConfigs(List<OltPonProtectConfig> ponProtectConfigs, long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllPonProtectConfigs"), entityId);
            for (OltPonProtectConfig config : ponProtectConfigs) {
                config.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertPonProtectConfig"), config);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertOltPonProtectConfig(OltPonProtectConfig ponProtectConfig) {
        this.getSqlSession().insert(getNameSpace("insertPonProtectConfig"), ponProtectConfig);
    }

    @Override
    public void setPonProtectAdmin(OltPonProtectConfig config) {
        this.getSqlSession().update(getNameSpace("updatePonProtectAdmin"), config);

    }

    @Override
    public void updateSwitchInfo(OltPonProtectConfig config) {
        this.getSqlSession().update(getNameSpace("updateSwitchInfo"), config);
    }

}
