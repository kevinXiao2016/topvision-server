/***********************************************************************
 * $Id: UpgradeJobDaoImpl.java,v1.0 2014年9月23日 下午3:58:23 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.upgrade.dao.UpgradeJobDao;
import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeJobInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:58:23
 * 
 */
@Repository("upgradeJobDao")
public class UpgradeJobDaoImpl extends MyBatisDaoSupport<UpgradeJobInfo> implements UpgradeJobDao {

    @Override
    public List<UpgradeJobInfo> selectAllJob() {
        return getSqlSession().selectList(getNameSpace("selectAllJob"));
    }

    @Override
    public UpgradeJobInfo getJob(Long jobId) {
        return getSqlSession().selectOne(getNameSpace("getJobById"), jobId);
    }

    @Override
    public Long insertJob(UpgradeJobInfo job) {
        getSqlSession().insert(getNameSpace("insertJob"), job);
        return job.getJobId();
    }

    @Override
    public void deleteJob(Long jobId) {
        getSqlSession().delete(getNameSpace("deleteJob"), jobId);
    }

    @Override
    public List<Entity> selectOltEntity(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectOltEntity"), map);
    }

    @Override
    public List<Entity> selectCcmtsEntity(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectCcmtsEntity"), map);
    }

    @Override
    public Long selectEntityNum(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("selectEntityNum"), map);
    }

    @Override
    public void addJobEntity(List<UpgradeEntity> upgradeEntityList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (int i = 0; i < upgradeEntityList.size(); i++) {
                sqlSession.delete("deleteJobEntity", upgradeEntityList.get(i));
                sqlSession.insert("insertJobEntity", upgradeEntityList.get(i));
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
    public List<UpgradeEntity> selectUpgradeEntity(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectUpgradeEntity"), map);
    }

    @Override
    public Long selectUpgradeEntityNum(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("selectUpgradeEntityNum"), map);
    }

    @Override
    public void deleteJobEntity(List<UpgradeEntity> upgradeEntityList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (int i = 0; i < upgradeEntityList.size(); i++) {
                sqlSession.delete("deleteJobEntity", upgradeEntityList.get(i));
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
    public UpgradeJobInfo getJobById(Long jobId) {
        return getSqlSession().selectOne(getNameSpace("getJobById"), jobId);
    }

    @Override
    public void updateUpgradeEntity(UpgradeEntity upgradeEntity) {
        getSqlSession().update(getNameSpace("updateUpgradeEntity"), upgradeEntity);
    }

    @Override
    public UpgradeEntity selectUpgradeEntityByEntityId(Long entityId, Long jobId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("jobId", jobId);
        return getSqlSession().selectOne("selectUpgradeEntityByEntityId", map);
    }

    @Override
    public List<UpgradeEntity> selectUpgradeEntityList(Long jobId) {
        return getSqlSession().selectList(getNameSpace("selectUpgradeEntityList"), jobId);
    }

    @Override
    public List<Long> selectUpgradeEntityIdList(Long jobId) {
        return getSqlSession().selectList(getNameSpace("selectUpgradeEntityIdList"), jobId);
    }

    @Override
    public UpgradeJobInfo selectJobByName(String jobName) {
        return getSqlSession().selectOne("selectJobByName", jobName);
    }

    @Override
    public List<UpgradeJobInfo> selectJobByVersionName(String versionName) {
        return getSqlSession().selectList("selectJobByVersionName", versionName);
    }

    @Override
    public Integer selectBdPreConfigType(Map<String, Object> map) {
        return getSqlSession().selectOne("selectBdPreConfigType", map);
    }

    @Override
    public List<Long> selectCmcTypeIdList(Long cmcEntityId) {
        return getSqlSession().selectList("selectCmcTypeIdList", cmcEntityId);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.upgrade.domain.UpgradeJobInfo";
    }

}
