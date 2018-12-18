/***********************************************************************
 * $Id: PerfThresholdDaoImpl.java,v1.0 2013-6-8 下午02:56:11 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.performance.dao.PerfThresholdDao;
import com.topvision.ems.performance.domain.EntityTypeTemplateRelation;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.domain.PerfTarget;
import com.topvision.ems.performance.domain.PerfTempEntityRelation;
import com.topvision.ems.performance.domain.PerfThresholdEntity;
import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.ems.performance.domain.PerfThresholdTemplate;
import com.topvision.ems.performance.domain.ThresholdAlertValue;
import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author Rod John
 * @created @2013-6-8-下午02:56:11
 * 
 */
@Repository("perfThresholdDao")
public class PerfThresholdDaoImpl extends MyBatisDaoSupport<Object> implements PerfThresholdDao {

    @Override
    public List<PerfThresholdEntity> selectEntityPerfThresholdTmeplate(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectEntityThresholdTemplate"), map);
    }

    /**
     * 根据查询条件获取相应的设备阈值模总数
     * @param perfThresholdEntity
     * @return
     */
    @Override
    public Long selectPerfTmeplateCount(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectPerfTmeplateCount"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.dao.PerfThresholdDao#selectEntityThresholdTemplate()
     */
    @Override
    public List<PerfThresholdTemplate> selectThresholdTemplateList(Integer parentType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentTypeId", parentType);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectThresholdTemplateList"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#selectThresholdTemplateById(java.lang.
     * Integer)
     */
    @Override
    public PerfThresholdTemplate selectThresholdTemplateById(Integer templateId) {
        return getSqlSession().selectOne(getNameSpace("selectThresholdTemplateById"), templateId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#selectThresholdTemplateByName(java.lang
     * .String)
     */
    @Override
    public PerfThresholdTemplate selectThresholdTemplateByName(String templateName) {
        return getSqlSession().selectOne(getNameSpace("selectThresholdTemplateByName"), templateName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#selectThresholdTemplateRules(java.lang
     * .Integer)
     */
    @Override
    public List<PerfThresholdRule> selectThresholdTemplateRulesByTemplateId(Integer templateId) {
        List<PerfThresholdRule> rules = getSqlSession().selectList(
                getNameSpace("selectThresholdTemplateRulesByTemplateId"), templateId);
        return setDefaultClearRule(rules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#deleteEntityThresholdTemplate(java.lang
     * .Long)
     */
    @Override
    public void deleteEntityThresholdTemplate(Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteEntityThresholdTemplate", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#insertThresholdTemplate(com.topvision.
     * ems.performance.domain.PerfThresholdTemplate)
     */
    @Override
    public void insertThresholdTemplate(PerfThresholdTemplate template) {
        getSqlSession().insert(getNameSpace("insertThresholdTemplate"), template);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#updateThresholdTemplate(com.topvision.
     * ems.performance.domain.PerfThresholdTemplate)
     */
    @Override
    public void updateThresholdTemplate(PerfThresholdTemplate template) {
        getSqlSession().update(getNameSpace() + "updateThresholdTemplate", template);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#deleteThresholdTemplate(java.lang.Integer)
     */
    @Override
    public void deleteThresholdTemplate(Integer templateId) {
        getSqlSession().delete(getNameSpace() + "deleteThresholdTemplate", templateId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#insertThresholdRule(com.topvision.ems.
     * performance.domain.PerfThresholdRule)
     */
    @Override
    public void insertThresholdRule(PerfThresholdRule thresholdRule) {
        getSqlSession().insert(getNameSpace() + "insertThresholdRule", thresholdRule);
    }

    @Override
    public void batchInsertThresholdRule(final List<PerfThresholdRule> perfThresholdRules) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (PerfThresholdRule perfThresholdRule : perfThresholdRules) {
                sqlSession.insert(getNameSpace() + "insertThresholdRule", perfThresholdRule);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#selectThresholdRuleById(java.lang.Integer)
     */
    @Override
    public PerfThresholdRule selectThresholdRuleById(Integer ruleId) {
        return getSqlSession().selectOne(getNameSpace("selectThresholdRuleById"), ruleId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#selectThresholdRuleByName(java.lang.Integer
     * , java.lang.String)
     */
    @Override
    public PerfThresholdRule selectThresholdRuleByName(Integer templateId, String targetId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("targetId", targetId);
        map.put("templateId", templateId);
        return getSqlSession().selectOne(getNameSpace("selectThresholdRuleByName"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#updateThresholdRule(com.topvision.ems.
     * performance.domain.PerfThresholdRule)
     */
    @Override
    public void updateThresholdRule(PerfThresholdRule thresholdRule) {
        getSqlSession().update(getNameSpace() + "updateThresholdRule", thresholdRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#applyEntityThresholdTemplate(java.lang
     * .Long, java.lang.Integer)
     */
    @Override
    public void applyEntityThresholdTemplate(Long entityId, Integer templateId, Integer isPerfThreshold) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("templateId", templateId);
        map.put("isPerfThreshold", isPerfThreshold);
        getSqlSession().insert(getNameSpace("applyEntityThresholdTemplate"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.dao.PerfThresholdDao#startEntityThreshold(java.lang.Long)
     */
    @Override
    public void startEntityThreshold(Long entityId) {
        getSqlSession().update(getNameSpace() + "startEntityThreshold", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.dao.PerfThresholdDao#stopEntityThreshold(java.lang.Long)
     */
    @Override
    public void stopEntityThreshold(Long entityId) {
        getSqlSession().update(getNameSpace() + "stopEntityThreshold", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#selectEntityThresholdTemplateByEntityId
     * (java.lang.Long)
     */
    @Override
    public PerfThresholdEntity selectEntityThresholdTemplateByEntityId(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("selectEntityThresholdTemplateByEntityId"), entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.performance.dao.PerfThresholdDao#selectPerfGlobal(java.lang.Long)
     */
    @Override
    public PerfGlobal selectPerfGlobal(Long type) {
        return getSqlSession().selectOne(getNameSpace("selectPerfGlobal"), type);
    }

    @Override
    public PerfGlobal loadPerfGolbalByType(Long type) {
        return getSqlSession().selectOne(getNameSpace("loadPerfGolbalByType"), type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#updatePerfGlobal(com.topvision.ems.performance
     * .domain.PerfGlobal)
     */
    @Override
    public void updatePerfGlobal(PerfGlobal perfGlobal) {
        getSqlSession().update(getNameSpace() + "updatePerfGlobal", perfGlobal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#selectThresholdRulesByTargetId(java.lang
     * .Integer, java.lang.String)
     */
    @Override
    public List<PerfThresholdRule> selectThresholdRulesByTargetId(Integer templateId, String targetId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("targetId", targetId);
        map.put("templateId", templateId);
        List<PerfThresholdRule> ruleList = getSqlSession().selectList(getNameSpace("selectThresholdRulesByTargetId"),
                map);
        return setDefaultClearRule(ruleList);
    }

    /**
     * 对匹配动作进行反向处理
     * 
     * @param action
     * @return
     */
    private int reverseAction(int action) {
        switch (action) {
        case 5:
            return 2;
        case 4:
            return 1;
        case 3:
            return 5;
        case 2:
            return 5;
        case 1:
            return 4;
        default:
            return 5;
        }
    }

    /**
     * 处理恢复阈值为空的性能指标，默认为空将自动设置为反向为清除阈值
     * 
     * @param ruleList
     * @return
     */
    private List<PerfThresholdRule> setDefaultClearRule(List<PerfThresholdRule> ruleList) {
        for (PerfThresholdRule rule : ruleList) {
            String clearRule = rule.getClearRules();
            String clearValue = "";
            if (clearRule == null || "".equals(clearRule)) {
                String[] thresholds = rule.getThresholds().split("#");
                for (int i = 0; i < thresholds.length; i++) {
                    int action = Integer.parseInt(thresholds[i].split("_")[0]);
                    int value = Integer.parseInt(thresholds[i].split("_")[1]);
                    clearValue = clearValue + reverseAction(action) + "_" + value + "#";
                }
                rule.setClearRules(clearValue.substring(0, clearValue.length() - 1));
            }
        }
        return ruleList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#getRelaTemplateEntityList(java.lang.Integer
     * , java.lang.Integer)
     */
    @Override
    public List<PerfThresholdEntity> getRelaTemplateEntityList(PerfThresholdEntity perfThresholdEntity) {
        HashMap<String, Object> authorityHashMap = PerfTargetUtil.transfer_object_to_hashMap(perfThresholdEntity);
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getEntityRelatedToTemplate"), authorityHashMap);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.performance.dao.PerfThresholdDao#getRelaTemplateCount(com.topvision.ems.performance.domain.PerfThresholdEntity)
     */
    @Override
    public Long getRelaTemplateCount(PerfThresholdEntity perfThresholdEntity) {
        Map<String, Object> map = PerfTargetUtil.transfer_object_to_hashMap(perfThresholdEntity);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getRelaTemplateCount"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#getNoRelaTemplateEntityList(java.lang.
     * Integer, java.lang.Integer)
     */
    @Override
    public List<PerfThresholdEntity> getNoRelaTemplateEntityList(PerfThresholdEntity perfThresholdEntity) {
        Map<String, Object> map = PerfTargetUtil.transfer_object_to_hashMap(perfThresholdEntity);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getEntityNotRelatedToTemplate"), map);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.performance.dao.PerfThresholdDao#getNoRelaTemplateEntityList(com.topvision.ems.performance.domain.PerfThresholdEntity)
     */
    @Override
    public Long getNoRelaTemplateCount(PerfThresholdEntity perfThresholdEntity) {
        Map<String, Object> map = PerfTargetUtil.transfer_object_to_hashMap(perfThresholdEntity);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("getNoRelaTemplateCount"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#getNoRelaTemplateEntityList(java.lang.
     * Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer)
     */
    @Override
    public List<PerfThresholdEntity> getNoRelaTemplateEntityList(Integer typeId, Integer templateId, String entityName,
            String mac, Integer entityType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityName", entityName);
        map.put("templateId", templateId);
        map.put("mac", mac);
        map.put("entityType", entityType);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getCCNoRelaTemplateList"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.dao.PerfThresholdDao#getPerfTargetList(java.lang.Integer)
     */
    @Override
    public List<PerfTarget> getPerfTargetListByType(Integer targetType) {
        return getSqlSession().selectList(getNameSpace("getPerfTargetListByType"), targetType);
    }

    @Override
    public List<PerfTarget> getAllPerfTargets() {
        return getSqlSession().selectList(getNameSpace("getAllPerfTargets"));
    }

    @Override
    public Entity getEntityById(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getEntityById"), entityId);
    }

    @Override
    public List<EntityType> loadEntitySubType(Integer entityType) {
        return getSqlSession().selectList(getNameSpace("loadEntitySubType"), entityType);
    }

    @Override
    public List<EntityTypeTemplateRelation> loadEntityTypeTemplateRelation() {
        return getSqlSession().selectList(getNameSpace("loadEntityTypeTemplateRelation"));
    }

    @Override
    public void deleteThresholdRulesByTemplateId(Integer templateId) {
        getSqlSession().delete(getNameSpace() + "deleteThresholdRulesByTemplateId", templateId);
    }

    @Override
    public List<Integer> loadSubTypeWhichHasDefaultTemp() {
        return getSqlSession().selectList(getNameSpace("loadSubTypeWhichHasDefaultTemp"));
    }

    @Override
    public void bindTemplateToEntity(final List<PerfTempEntityRelation> perftemplateentityrelations) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (PerfTempEntityRelation perftemplateentityrelation : perftemplateentityrelations) {
                sqlSession.insert(getNameSpace() + "bindTemplateToEntity", perftemplateentityrelation);
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
    public String getDisplayNameByEntityTypeId(Integer entityTypeId) {
        return getSqlSession().selectOne(getNameSpace("getDisplayNameByEntityTypeId"), entityTypeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerfThresholdDao#getDefaultTemplateByEntityType(java.lang
     * .Integer)
     */
    @Override
    public PerfThresholdTemplate getDefaultTemplateByEntityType(Long typeId) {
        return getSqlSession().selectOne(getNameSpace("selectDefaultTemplateByTypeId"), typeId);
    }

    @Override
    public List<Integer> getDefaultTemplateList(Integer typeId) {
        return getSqlSession().selectList(getNameSpace("selectDefaultTemplateListByType"), typeId);
    }

    @Override
    public void updateTargetStatus(PerfTarget target) {
        this.getSqlSession().update(getNameSpace("updateTargetStatus"), target);
    }

    @Override
    public PerfTarget queryPerfTarget(String targetId) {
        return this.getSqlSession().selectOne(getNameSpace("queryPerfTarget"), targetId);
    }

    @Override
    public void updateTargetValue(PerfTarget target) {
        this.getSqlSession().update(getNameSpace("updateTargetValue"), target);
    }

    @Override
    public Map<String, Object> getAllTargetCount() {
        List<Map<String, Object>> targetCounts = this.getSqlSession().selectList(getNameSpace("getAllTargetCount"));
        Map<String, Object> countMap = new HashMap<String, Object>();
        String targetType = null;
        Object typeCount = null;
        for (Map<String, Object> map : targetCounts) {
            for (Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("targetType")) {
                    targetType = String.valueOf(entry.getValue());
                } else if (entry.getKey().equals("targetNum")) {
                    typeCount = entry.getValue();
                }
            }
            countMap.put(targetType, typeCount);
        }
        return countMap;
    }

    @Override
    public int queryTargetCountByType(Integer targetType) {
        return this.getSqlSession().selectOne(getNameSpace("queryTargetCountByType"), targetType);
    }

    @Override
    public int queryTargetReRuleCount(String targetId) {
        return this.getSqlSession().selectOne(getNameSpace("queryTargetReRuleCount"), targetId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.performance.domain.PerfThreshold";
    }

    @Override
    public void updateTemplateEntityRelation(PerfTempEntityRelation entityTemplate) {
        this.getSqlSession().update(getNameSpace("updateTemplateEntityRelation"), entityTemplate);
    }

    @Override
    public void batchUpdateTempEntityRelation(List<PerfTempEntityRelation> entityTempList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (PerfTempEntityRelation entityTemp : entityTempList) {
                sqlSession.update(getNameSpace("bindTemplateToEntity"), entityTemp);
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
    public PerfTempEntityRelation queryEntityRelationTemp(Long entityId) {
        return this.getSqlSession().selectOne(getNameSpace("queryEntityRelationTemp"), entityId);
    }

    @Override
    public void updateRelationByTemplateId(Map<String, Object> map) {
        this.getSqlSession().update(getNameSpace("updateRelationByTemplateId"), map);
    }

    @Override
    public List<ThresholdAlertValue> selectLastedAlertValue() {
        return getSqlSession().selectList(getNameSpace("selectLastedAlertValue"));
    }

    @Override
    public void insertOrUpdateAlertValue(ThresholdAlertValue thresholdAlertValue) {
        getSqlSession().insert(getNameSpace("insertOrUpdateAlertValue"), thresholdAlertValue);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.dao.PerfThresholdDao#deleteLastedAlertData(java.util.Map)
     */
    @Override
    public void deleteLastedAlertData(ThresholdAlertValue thresholdAlertValue) {
        getSqlSession().delete(getNameSpace("deleteLastedAlertData"), thresholdAlertValue);
    }

    @Override
    public void batchDeleteLastedAlertData(List<Alert> list) {
        SqlSession batchSession = getBatchSession();
        for (Alert alert : list) {
            ThresholdAlertValue thresholdAlertValue = new ThresholdAlertValue();
            thresholdAlertValue.setEntityId(alert.getEntityId());
            thresholdAlertValue.setSource(alert.getSource());
            thresholdAlertValue.setAlertEventId((int) alert.getLevelId());
            batchSession.delete(getNameSpace("deleteLastedAlertData"), thresholdAlertValue);
        }
        batchSession.commit();
    }

}
