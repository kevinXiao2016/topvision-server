/***********************************************************************
 * $Id: PerfThresholdServiceImpl.java,v1.0 2013-6-8 下午03:51:59 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.zetaframework.util.Validator;

/**
 * @author Rod John
 * @created @2013-6-8-下午03:51:59
 * 
 */
@Service("perfThresholdService")
public class PerfThresholdServiceImpl extends BaseService implements PerfThresholdService {
    @Autowired
    private PerfThresholdDao perfThresholdDao;
    @Autowired
    private EntityTypeService entityTypeService;
    //设备entity与模板的缓存Map
    private Map<Long, PerfThresholdEntity> entityPerfTemplateCache;
    //模板与阈值指标规则的缓存Map
    private Map<String, List<PerfThresholdRule>> templateTargetRuleCache;

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        //创建缓存对应的Map
        entityPerfTemplateCache = new ConcurrentHashMap<Long, PerfThresholdEntity>();
        templateTargetRuleCache = new ConcurrentHashMap<String, List<PerfThresholdRule>>();
    }

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        //清空缓存Map
        entityPerfTemplateCache.clear();
        templateTargetRuleCache.clear();
        entityPerfTemplateCache = null;
        templateTargetRuleCache = null;
    }

    @Override
    public PerfThresholdEntity selectPerfTemplateByEntityId(Long entityId) {
        if (!entityPerfTemplateCache.containsKey(entityId)) { //判断缓存中是否存在对应的关联关系
            //缓存中不存在则查询数据库添加到缓存中
            PerfThresholdEntity pEnrtity = perfThresholdDao.selectEntityThresholdTemplateByEntityId(entityId);
            if (pEnrtity != null && pEnrtity.getTemplateId() != -1) {//-1表示未关联阈值模板
                entityPerfTemplateCache.put(entityId,
                        perfThresholdDao.selectEntityThresholdTemplateByEntityId(entityId));
            }
        }
        return entityPerfTemplateCache.get(entityId);
    }

    @Override
    public List<PerfThresholdRule> selectTargetRulesByTargetId(Integer templateId, String targetId) {
        String ruleKey = templateId + "_" + targetId;//指标与阈值规则的key的组成方式为模板Id和指标名称的组合
        if (!templateTargetRuleCache.containsKey(ruleKey)) {//判断缓存中是否存在阈值指标的规则，不存在则查询数据库添加到缓存中
            templateTargetRuleCache.put(ruleKey, perfThresholdDao.selectThresholdRulesByTargetId(templateId, targetId));
        }
        return templateTargetRuleCache.get(ruleKey);
    }

    @Override
    public Map<Long, PerfThresholdEntity> getEntityTemplateCache() {
        //获得设备与模板关联关系的缓存
        return entityPerfTemplateCache;
    }

    @Override
    public Map<String, List<PerfThresholdRule>> getTemplateTargetRuleCache() {
        //获得阈值指标规则的缓存
        return templateTargetRuleCache;
    }

    @Override
    public List<PerfThresholdEntity> selectEntityPerfThresholdTmeplate(Map<String, Object> map) {
        return perfThresholdDao.selectEntityPerfThresholdTmeplate(map);
    }

    @Override
    public Long selectPerfTmeplateCount(Map<String, Object> map) {
        return perfThresholdDao.selectPerfTmeplateCount(map);
    }

    @Override
    public List<PerfThresholdTemplate> loadThresholdTemplateList(Integer parentType) {
        return perfThresholdDao.selectThresholdTemplateList(parentType);
    }

    @Override
    public PerfThresholdTemplate getThresholdTemplate(Integer templateId) {
        return perfThresholdDao.selectThresholdTemplateById(templateId);
    }

    @Override
    public PerfThresholdTemplate getThresholdTemplateByName(String templateName) {
        return perfThresholdDao.selectThresholdTemplateByName(templateName);
    }

    @Override
    public List<PerfThresholdRule> loadThresholdTemplateRules(Integer templateId) {
        return perfThresholdDao.selectThresholdTemplateRulesByTemplateId(templateId);
    }

    @Override
    public void deleteEntityThresholdTemplate(Long entityId) {
        //删除设备和阈值模板的关联关系同时清除缓存
        perfThresholdDao.deleteEntityThresholdTemplate(entityId);
        entityPerfTemplateCache.remove(entityId);
    }

    @Override
    public void addThresholdTemplate(PerfThresholdTemplate template) {
        //增加阈值模板
        perfThresholdDao.insertThresholdTemplate(template);
    }

    @Override
    public void modifyThresholdTemplate(PerfThresholdTemplate template) {
        //更新阈值模板信息
        perfThresholdDao.updateThresholdTemplate(template);
    }

    @Override
    public void deleteThresholdTemplate(Integer templateId) {
        //删除阈值模板 流程为先删除阈值模板关联的阈值规则后，再删除阈值模板
        perfThresholdDao.deleteThresholdTemplate(templateId);
        for (String ruleKey : templateTargetRuleCache.keySet()) {
            String ruleTemplateIdKey = ruleKey.split("_")[0];
            if (templateId.equals(ruleTemplateIdKey)) {//判断缓存中是否存在该模板的规则
                //删除模板关联的阈值规则map
                templateTargetRuleCache.remove(ruleKey);
            }
        }

        //删除模板后设备默认所关联的阈值模板Id被改为-1，同时阈值告警开关被置为false
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("templateId", templateId);
        paraMap.put("newTemplateId", EponConstants.TEMPLATE_ENTITY_UNLINK);
        paraMap.put("isPerfThreshold", EponConstants.PERF_PTHRESHOLD_OFF);

        perfThresholdDao.updateRelationByTemplateId(paraMap);
        //删除设备阈值模板的关联关系
        for (Long entityId : entityPerfTemplateCache.keySet()) {
            PerfThresholdEntity perfThresholdEntity = entityPerfTemplateCache.get(entityId);
            if (perfThresholdEntity != null && perfThresholdEntity.getTemplateId().equals(templateId)) {
                entityPerfTemplateCache.remove(entityId);
            }
        }

    }

    @Override
    public void addThresholdRule(PerfThresholdRule thresholdRule) {
        List<PerfThresholdRule> ruleList = new ArrayList<PerfThresholdRule>();
        ruleList.add(thresholdRule);
        String ruleKey = thresholdRule.getTemplateId() + "_" + thresholdRule.getTargetId();
        perfThresholdDao.insertThresholdRule(thresholdRule);
        templateTargetRuleCache.put(ruleKey, ruleList);
    }

    @Override
    public void batchAddThresholdRule(List<PerfThresholdRule> perfThresholdRules) {
        perfThresholdDao.batchInsertThresholdRule(perfThresholdRules);
        for (PerfThresholdRule perfThresholdRule : perfThresholdRules) {
            List<PerfThresholdRule> ruleList = new ArrayList<PerfThresholdRule>();
            ruleList.add(perfThresholdRule);
            String ruleKey = perfThresholdRule.getTemplateId() + "_" + perfThresholdRule.getTargetId();
            templateTargetRuleCache.put(ruleKey, ruleList);
        }
    }

    @Override
    public PerfThresholdRule getThresholdRuleById(Integer ruleId) {
        return perfThresholdDao.selectThresholdRuleById(ruleId);
    }

    @Override
    public PerfThresholdRule getThresholdRuleByName(Integer templateId, String targetId) {
        return perfThresholdDao.selectThresholdRuleByName(templateId, targetId);
    }

    @Override
    public void modifyThresholdRule(PerfThresholdRule thresholdRule) {
        PerfThresholdTemplate template = perfThresholdDao.selectThresholdTemplateById(thresholdRule.getTemplateId());
        java.sql.Timestamp modifyTime = new java.sql.Timestamp(System.currentTimeMillis());
        template.setModifyTime(modifyTime);
        //更新模板属性
        perfThresholdDao.updateThresholdTemplate(template);
        List<PerfThresholdRule> ruleList = new ArrayList<PerfThresholdRule>();
        ruleList.add(thresholdRule);
        String ruleKey = thresholdRule.getTemplateId() + "_" + thresholdRule.getTargetId();
        perfThresholdDao.updateThresholdRule(thresholdRule);
        templateTargetRuleCache.put(ruleKey, ruleList);
    }

    @Override
    public void applyEntityThresholdTemplate(Long entityId, Integer templateId, Integer isPerfThreshold) {
        PerfThresholdEntity perfThresholdEntity = new PerfThresholdEntity();
        perfThresholdEntity.setEntityId(entityId);
        perfThresholdEntity.setTemplateId(templateId);
        perfThresholdEntity.setIsPerfThreshold(Validator.isTrue(isPerfThreshold));
        perfThresholdDao.applyEntityThresholdTemplate(entityId, templateId, isPerfThreshold);
        entityPerfTemplateCache.put(entityId, perfThresholdEntity);
    }

    @Override
    public void startEntityThreshold(Long entityId) {
        PerfThresholdEntity perfThresholdEntity = entityPerfTemplateCache.get(entityId);
        if (perfThresholdEntity == null) {
            perfThresholdEntity = perfThresholdDao.selectEntityThresholdTemplateByEntityId(entityId);
        }
        perfThresholdEntity.setIsPerfThreshold(true);
        perfThresholdDao.startEntityThreshold(entityId);
        entityPerfTemplateCache.put(entityId, perfThresholdEntity);
    }

    @Override
    public void stopEntityThreshold(Long entityId) {
        PerfThresholdEntity perfThresholdEntity = entityPerfTemplateCache.get(entityId);
        if (perfThresholdEntity == null) {
            perfThresholdEntity = perfThresholdDao.selectEntityThresholdTemplateByEntityId(entityId);
        }
        perfThresholdEntity.setIsPerfThreshold(false);
        perfThresholdDao.stopEntityThreshold(entityId);
        entityPerfTemplateCache.put(entityId, perfThresholdEntity);
    }

    @Override
    public PerfGlobal loadPerfGolbalByType(Long type) {
        return perfThresholdDao.loadPerfGolbalByType(type);
    }

    @Override
    public void modifyPerfGlobal(PerfGlobal perfGlobal) {
        perfThresholdDao.updatePerfGlobal(perfGlobal);
    }

    @Override
    public List<PerfThresholdEntity> loadRelaTemplateEntityList(PerfThresholdEntity perfThresholdEntity) {
        return perfThresholdDao.getRelaTemplateEntityList(perfThresholdEntity);
    }

    @Override
    public Long loadRelaTemplateCount(PerfThresholdEntity perfThresholdEntity) {
        return perfThresholdDao.getRelaTemplateCount(perfThresholdEntity);
    }

    @Override
    public List<PerfThresholdEntity> loadNoRelaTemplateEntityList(PerfThresholdEntity perfThresholdEntity) {
        return perfThresholdDao.getNoRelaTemplateEntityList(perfThresholdEntity);
    }

    @Override
    public Long loadNoRelaTemplateCount(PerfThresholdEntity perfThresholdEntity) {
        return perfThresholdDao.getNoRelaTemplateCount(perfThresholdEntity);
    }

    @Override
    public List<PerfTarget> loadPerfTargetList(Integer targetType) {
        return perfThresholdDao.getPerfTargetListByType(targetType);
    }

    @Override
    public List<PerfTarget> getAllPerfTargets() {
        return perfThresholdDao.getAllPerfTargets();
    }

    @Override
    public PerfGlobal getOltPerfGlobal() {
        Long type = entityTypeService.getOltType();
        return perfThresholdDao.selectPerfGlobal(type);
    }

    @Override
    public PerfGlobal getCmtsPerfGlobal() {
        Long type = entityTypeService.getCcmtsandcmtsType();
        return perfThresholdDao.selectPerfGlobal(type);
    }

    @Override
    public Entity getEntityById(Long entityId) {
        return perfThresholdDao.getEntityById(entityId);
    }

    @Override
    public JSONArray loadEntitySubType(Integer entityType) {
        JSONArray jsonArray = new JSONArray();
        List<EntityType> subTypes = perfThresholdDao.loadEntitySubType(entityType);
        // 转换
        for (EntityType subType : subTypes) {
            JSONObject json = new JSONObject();
            json.put("typeId", subType.getTypeId());
            json.put("displayName", subType.getDisplayName());
            jsonArray.add(json);
        }
        return jsonArray;
    }

    @Override
    public List<EntityTypeTemplateRelation> loadEntityTypeTemplateRelation() {
        List<EntityTypeTemplateRelation> entityTypeTemplateRelations = perfThresholdDao
                .loadEntityTypeTemplateRelation();
        for (EntityTypeTemplateRelation relation : entityTypeTemplateRelations) {
            PerfThresholdTemplate pt = perfThresholdDao
                    .getDefaultTemplateByEntityType(relation.getTypeId().longValue());
            if (pt != null) {
                relation.setHasDefaultTemplate(true);
            }
            if (entityTypeService.isBaseType(relation.getTypeId().longValue())) {
                EntityType entityType = entityTypeService.getNetWorkGroupType(relation.getTypeId().longValue());
                if (entityType != null) {
                    relation.setParentTypeId(entityType.getTypeId());
                    relation.setIsSubType(true);
                }
            } else {
                relation.setParentTypeId(relation.getTypeId().longValue());
                relation.setIsSubType(false);
            }
        }
        return entityTypeTemplateRelations;
    }

    @Override
    public void deleteThresholdRulesByTemplateId(Integer templateId) {
        perfThresholdDao.deleteThresholdRulesByTemplateId(templateId);
        for (String ruleKey : templateTargetRuleCache.keySet()) {
            Integer ruleKeyTemplateId = Integer.parseInt(ruleKey.split("_")[0]);
            if (templateId.equals(ruleKeyTemplateId)) {
                templateTargetRuleCache.remove(ruleKey);
            }
        }
    }

    @Override
    public JSONArray loadSubTypeWhichHasDefaultTemp() {
        List<Integer> subTypes = perfThresholdDao.loadSubTypeWhichHasDefaultTemp();
        JSONArray json = new JSONArray();
        json = JSONArray.fromObject(subTypes);
        return json;
    }

    @Override
    public void bindTemplateToEntity(List<PerfTempEntityRelation> perftemplateentityrelations) {
        perfThresholdDao.batchUpdateTempEntityRelation(perftemplateentityrelations);
        for (PerfTempEntityRelation perfTempEntityRelation : perftemplateentityrelations) {
            Long entityId = perfTempEntityRelation.getEntityId();
            Integer templateId = perfTempEntityRelation.getTemplateId();
            Boolean isPerfThreshold = Validator.isTrue(perfTempEntityRelation.getIsPerfThreshold());
            PerfThresholdEntity perfThresholdEntity = new PerfThresholdEntity();
            perfThresholdEntity.setTemplateId(templateId);
            perfThresholdEntity.setEntityId(entityId);
            perfThresholdEntity.setIsPerfThreshold(isPerfThreshold);
            entityPerfTemplateCache.put(entityId, perfThresholdEntity);
        }
    }

    @Override
    public String getDisplayNameByEntityTypeId(Integer entityTypeId) {
        return perfThresholdDao.getDisplayNameByEntityTypeId(entityTypeId);
    }

    @Override
    public PerfThresholdTemplate getDefaultTemplateByEntityType(Long typeId) {
        PerfThresholdTemplate template = perfThresholdDao.getDefaultTemplateByEntityType(typeId);
        if (template == null) {
            Long type = null;
            if (entityTypeService.isCcmtsAndCmts(typeId)) {
                type = entityTypeService.getCcmtsandcmtsType();
            } else if (entityTypeService.isOlt(typeId)) {
                type = entityTypeService.getOltType();
            } else if (entityTypeService.isOnu(typeId)) {
                type = entityTypeService.getOnuType();
            }
            template = perfThresholdDao.getDefaultTemplateByEntityType(type);
            return template;
        } else {
            return template;
        }
    }

    @Override
    public List<Integer> getDefaultTemplateList(Integer typeId) {
        return perfThresholdDao.getDefaultTemplateList(typeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.service.PerfThresholdService#isEntityApplyTemplate(java.lang
     * .Long, java.lang.Integer)
     */
    @Override
    public boolean isEntityApplyTemplate(Long entityId) {
        PerfThresholdEntity perfThresholdEntity = perfThresholdDao.selectEntityThresholdTemplateByEntityId(entityId);
        if (perfThresholdEntity != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void modifyTemplateEntityRelation(PerfTempEntityRelation entityTemplate) {
        Long entityId = entityTemplate.getEntityId();
        Integer templateId = entityTemplate.getTemplateId();
        Boolean isPerfThreshold = Validator.isTrue(entityTemplate.getIsPerfThreshold());
        PerfThresholdEntity perfThresholdEntity = null;
        if (entityPerfTemplateCache.containsKey(entityId)) {
            perfThresholdEntity = entityPerfTemplateCache.get(entityId);
        } else {
            perfThresholdEntity = new PerfThresholdEntity();
        }
        perfThresholdEntity.setTemplateId(templateId);
        perfThresholdEntity.setEntityId(entityId);
        perfThresholdEntity.setIsPerfThreshold(isPerfThreshold);
        perfThresholdDao.updateTemplateEntityRelation(entityTemplate);
        entityPerfTemplateCache.put(entityId, perfThresholdEntity);
    }

    @Override
    public void updateTargetStatus(PerfTarget target) {
        perfThresholdDao.updateTargetStatus(target);
    }

    @Override
    public PerfTarget getPerfTarget(String targetId) {
        return perfThresholdDao.queryPerfTarget(targetId);
    }

    @Override
    public void updateTargetValue(PerfTarget target) {
        perfThresholdDao.updateTargetValue(target);
    }

    @Override
    public Map<String, Object> getAllTargetCount() {
        return perfThresholdDao.getAllTargetCount();
    }

    @Override
    public int getTargetCountByType(Integer targetType) {
        return perfThresholdDao.queryTargetCountByType(targetType);
    }

    @Override
    public int getTargetReRuleCount(String targetId) {
        return perfThresholdDao.queryTargetReRuleCount(targetId);
    }

    @Override
    public PerfGlobal getPerfGlobalByType(Long type) {
        return perfThresholdDao.selectPerfGlobal(type);
    }

    @Override
    public void unLinkEntityTemplate(List<PerfTempEntityRelation> entityTempList) {
        perfThresholdDao.batchUpdateTempEntityRelation(entityTempList);
        for (PerfTempEntityRelation perfTempEntityRelation : entityTempList) {
            entityPerfTemplateCache.remove(perfTempEntityRelation.getEntityId());
        }
    }

    @Override
    public void insertOrUpdateAlertValue(Long entityId, String source, Integer alertEventId, Float perfValue,
            Integer levelId) {
        ThresholdAlertValue thresholdAlertValue = new ThresholdAlertValue();
        thresholdAlertValue.setEntityId(entityId);
        thresholdAlertValue.setSource(source);
        thresholdAlertValue.setAlertEventId(alertEventId);
        thresholdAlertValue.setLevelId(levelId);
        thresholdAlertValue.setPerfValue(perfValue);
        perfThresholdDao.insertOrUpdateAlertValue(thresholdAlertValue);
    }

    @Override
    public List<ThresholdAlertValue> queryLastedAlertValue() {
        return perfThresholdDao.selectLastedAlertValue();
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.service.PerfThresholdService#removeLastedAlertData(java.lang.Long, java.lang.Long, java.lang.Integer)
     */
    @Override
    public void removeLastedAlertData(Long entityId, String source, Integer alertEventId) {
        ThresholdAlertValue thresholdAlertValue = new ThresholdAlertValue();
        thresholdAlertValue.setEntityId(entityId);
        thresholdAlertValue.setSource(source);
        thresholdAlertValue.setAlertEventId(alertEventId);
        perfThresholdDao.deleteLastedAlertData(thresholdAlertValue);
    }

    @Override
    public void removeLastedAlertData(List<Alert> list) {
        perfThresholdDao.batchDeleteLastedAlertData(list);
    }

}
