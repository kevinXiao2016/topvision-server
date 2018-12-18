/***********************************************************************
 * $Id: UniVlanProfileDaoImpl.java,v1.0 2013-11-28 上午10:13:07 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.univlanprofile.dao.UniVlanProfileDao;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanRuleTable;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-11-28-上午10:13:07
 *
 */
@Repository("uniVlanProfileDao")
public class UniVlanProfileDaoImpl extends MyBatisDaoSupport<Object> implements UniVlanProfileDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.univlanprofile.domain.UniVlanProfile";
    }

    @Override
    public void batchInsertUniVlanProfiles(List<UniVlanProfileTable> uniVlanProfiles, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllUniVlanProfiles", entityId);
            for (UniVlanProfileTable uniVlanProfile : uniVlanProfiles) {
                uniVlanProfile.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertUniVlanProfile", uniVlanProfile);
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
    public List<UniVlanProfileTable> getUniVlanProfiles(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getUniVlanProfileList", entityId);
    }

    @Override
    public UniVlanProfileTable getUniVlanProfileById(Long entityId, Integer profileId) {
        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("profileId", profileId);
        return getSqlSession().selectOne(getNameSpace() + "getUniVlanProfile", paramMap);
    }

    @Override
    public void insertUniVlanProfile(UniVlanProfileTable uniVlanProfile) {
        this.getSqlSession().insert(getNameSpace("insertUniVlanProfile"), uniVlanProfile);
    }

    @Override
    public void updateUniVlanProfile(UniVlanProfileTable uniVlanProfile) {
        getSqlSession().update(getNameSpace() + "updateUniVlanProfile", uniVlanProfile);
    }

    @Override
    public void updateProfileRefCnt(UniVlanProfileTable uniVlanProfile) {
        this.getSqlSession().update(getNameSpace("updateProfileRefCnt"), uniVlanProfile);
    }

    @Override
    public void deleteUniVlanProfile(UniVlanProfileTable uniVlanProfile) {
        getSqlSession().delete(getNameSpace() + "deleteUniVlanProfile", uniVlanProfile);
    }

    @Override
    public void batchInsertUniVlanRules(List<UniVlanRuleTable> uniVlanRules, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllUniVlanRules", entityId);
            for (UniVlanRuleTable uniVlanRule : uniVlanRules) {
                uniVlanRule.setEntityId(entityId);
                sqlSession.insert(getNameSpace() + "insertUniVlanRule", uniVlanRule);
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
    public List<UniVlanRuleTable> getProfileVlanRules(Long entityId, Integer ruleProfileIndex) {
        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("ruleProfileIndex", ruleProfileIndex);
        return getSqlSession().selectList(getNameSpace() + "getProfileVlanRuleList", paramMap);
    }

    @Override
    public UniVlanRuleTable getUniVlanRuleById(UniVlanRuleTable uniVlanRule) {
        return getSqlSession().selectOne(getNameSpace() + "getProfileVlanRule", uniVlanRule);
    }

    @Override
    public void insertUniVlanRule(UniVlanRuleTable uniVlanRule) {
        getSqlSession().insert(getNameSpace() + "insertUniVlanRule", uniVlanRule);
    }

    @Override
    public void updateUniVlanRule(UniVlanRuleTable uniVlanRule) {
        getSqlSession().update(getNameSpace() + "updateUniVlanRule", uniVlanRule);
    }

    @Override
    public void deleteUniVlanRule(UniVlanRuleTable uniVlanRule) {
        getSqlSession().delete(getNameSpace() + "deleteUniVlanRule", uniVlanRule);
    }

    @Override
    public void deleteProfileRelRules(Long entityId, Integer profileId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("ruleProfileIndex", profileId);
        this.getSqlSession().delete(getNameSpace("deleteProfileRelRule"), param);
    }

    @Override
    public void batchInsertUniVlanBind(List<UniVlanBindTable> uniVlanBinds, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (UniVlanBindTable uniVlanBind : uniVlanBinds) {
                uniVlanBind.setEntityId(entityId);
                sqlSession.delete(getNameSpace() + "deleteAllUniVlanBinds", uniVlanBind);
                sqlSession.insert(getNameSpace() + "insertUniVlanBind", uniVlanBind);
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
    public UniVlanBindTable getUniVlanBindById(UniVlanBindTable uniVlanBind) {
        return getSqlSession().selectOne(getNameSpace() + "getUniVlanBind", uniVlanBind);
    }

    @Override
    public void updateUniVlanBind(UniVlanBindTable uniVlanBind) {
        getSqlSession().update(getNameSpace() + "updateUniVlanBind", uniVlanBind);
    }

    @Override
    public void batchUpdateVlanBind(List<UniVlanBindTable> bindList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (UniVlanBindTable uniVlanBind : bindList) {
                sqlSession.update(getNameSpace() + "updateUniVlanBind", uniVlanBind);
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
    public List<Long> queryProfileBindList(Integer profileIndex, Long entityId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("entityId", entityId);
        param.put("profileIndex", profileIndex);
        return this.getSqlSession().selectList(getNameSpace("getProfileBindList"), param);
    }

    @Override
    public UniVlanBindTable queryUniBindInfo(UniVlanBindTable bindTable) {
        return this.getSqlSession().selectOne(getNameSpace("getUniBindInfo"), bindTable);
    }

    @Override
    public void updateUniPvid(UniVlanBindTable bindTable) {
        this.getSqlSession().update(getNameSpace("updateUniPvid"), bindTable);
    }

    @Override
    public void updateUniBindInfo(UniVlanBindTable bindTable) {
        this.getSqlSession().update(getNameSpace("updateUniBindInfo"), bindTable);
    }

    @Override
    public List<UniVlanBindTable> queryEntityBindList(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("queryEntityBindList"), entityId);
    }

    @Override
    public void updateVlanBind(UniVlanBindTable table) {
        getSqlSession().update(getNameSpace() + "updateUniVlanBind", table);
    }

}
