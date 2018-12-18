/***********************************************************************
 * $Id: PerfThresholdDao.java,v1.0 2013-6-8 下午02:47:17 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.performance.domain.EntityTypeTemplateRelation;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.domain.PerfTarget;
import com.topvision.ems.performance.domain.PerfTempEntityRelation;
import com.topvision.ems.performance.domain.PerfThresholdEntity;
import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.ems.performance.domain.PerfThresholdTemplate;
import com.topvision.ems.performance.domain.ThresholdAlertValue;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Rod John
 * @created @2013-6-8-下午02:47:17
 *
 */
public interface PerfThresholdDao extends BaseEntityDao<Object> {

    /**
     * 获取设备阈值模板内容
     * 
     * @param entityId
     * @return
     */
    PerfThresholdEntity selectEntityThresholdTemplateByEntityId(Long entityId);

    /**
     * 根据查询条件获取相应的设备阈值模板情况
     * @param map
     * @return
     */
    List<PerfThresholdEntity> selectEntityPerfThresholdTmeplate(Map<String, Object> map);

    /**
     * 根据查询条件获取相应的设备阈值模总数
     * @param map
     * @return
     */
    Long selectPerfTmeplateCount(Map<String, Object> map);

    /**
     * 加载对应类型的阈值模板列表
     * 
     * @return
     */
    List<PerfThresholdTemplate> selectThresholdTemplateList(Integer parentType);

    /**
     * 加载阈值模板
     * 
     * @param templateId
     * @return
     */
    PerfThresholdTemplate selectThresholdTemplateById(Integer templateId);

    /**
     * 加载阈值模板
     * 
     * @param templateName
     * @return
     */
    PerfThresholdTemplate selectThresholdTemplateByName(String templateName);

    /**
     * 加载模板规则
     * 
     * @param templateId
     * @return
     */
    List<PerfThresholdRule> selectThresholdTemplateRulesByTemplateId(Integer templateId);

    /**
     * 加载特定指标模板规则
     * 
     * @param templateId
     * @return
     */
    List<PerfThresholdRule> selectThresholdRulesByTargetId(Integer templateId, String targetId);

    /**
     * 解关联设备阈值模板
     * 
     * @param entityId
     */
    void deleteEntityThresholdTemplate(Long entityId);

    /**
     * 新增阈值模板
     * 
     * @param template
     */
    void insertThresholdTemplate(PerfThresholdTemplate template);

    /**
     * 修改阈值模板
     * 
     * @param template
     */
    void updateThresholdTemplate(PerfThresholdTemplate template);

    /**
     * 删除阈值模板
     * 
     * @param templateId
     */
    void deleteThresholdTemplate(Integer templateId);

    /**
     * 新增阈值规则
     * 
     * @param thresholdRule
     */
    void insertThresholdRule(PerfThresholdRule thresholdRule);

    /**
     * 批量插入阈值规则
     * @param perfThresholdRules
     * @author fanzidong
     */
    void batchInsertThresholdRule(List<PerfThresholdRule> perfThresholdRules);

    /**
     * 获得阈值规则
     * 
     * @param ruleId
     */
    PerfThresholdRule selectThresholdRuleById(Integer ruleId);

    /**
     * 获得阈值规则
     * 
     * @param templateId
     * @param targetId
     */
    PerfThresholdRule selectThresholdRuleByName(Integer templateId, String targetId);

    /**
     * 修改阈值规则
     * 
     * @param thresholdRule
     */
    void updateThresholdRule(PerfThresholdRule thresholdRule);

    /**
     * 应用模板到设备
     * 
     * @param entityId
     * @param templateId
     * @param isPerfThreshold
     */
    void applyEntityThresholdTemplate(Long entityId, Integer templateId, Integer isPerfThreshold);

    /**
     * 开启设备性能阈值告警
     * 
     * @param entityId
     */
    void startEntityThreshold(Long entityId);

    /**
     * 关闭设备性能阈值告警
     * 
     * @param entityId
     */
    void stopEntityThreshold(Long entityId);

    /**
     * 加载系统性能全局配置
     * 
     * @return
     */
    PerfGlobal selectPerfGlobal(Long type);

    /**
     * 修改系统性能全局配置
     * 
     * @param perfGlobal
     */
    void updatePerfGlobal(PerfGlobal perfGlobal);

    /**
     * 加载已关联阈值模板设备列表
     * @param perfThresholdEntity
     * @return
     */
    List<PerfThresholdEntity> getRelaTemplateEntityList(PerfThresholdEntity perfThresholdEntity);

    /**
     * 加载已关联阈值模板设备总数
     * @param perfThresholdEntity
     * @return
     */
    Long getRelaTemplateCount(PerfThresholdEntity perfThresholdEntity);

    /**
     * 加载未关联阈值模板设备列表
     * 
     * @param perfThresholdEntity
     * @return
     */
    List<PerfThresholdEntity> getNoRelaTemplateEntityList(PerfThresholdEntity perfThresholdEntity);

    /**
     * 获得未关联阈值模板设备总数
     * 
     * @param perfThresholdEntity
     * @return
     */
    Long getNoRelaTemplateCount(PerfThresholdEntity perfThresholdEntity);

    /**
     * 加载未关联阈值模板设备列表
     * 
     * @param typeId
     * @param templateId
     * @return
     */
    List<PerfThresholdEntity> getNoRelaTemplateEntityList(Integer typeId, Integer templateId, String entityName,
            String mac, Integer entityType);

    /**
     * 获得模板的性能指标列表
     * 
     * @param targetType
     * @return
     */
    List<PerfTarget> getPerfTargetListByType(Integer targetType);

    /**
     * 获取所有的性能阈值指标列表
     * @return
     */
    List<PerfTarget> getAllPerfTargets();

    /**
     * 获取设备属性
     * @param entityId
     * @return
     */
    Entity getEntityById(Long entityId);

    /**
     * 加载指定设备大类型下的设备子类型
     * @return
     */
    List<EntityType> loadEntitySubType(Integer entityType);

    /**
     * 加载设备类型与模板之间的关系
     * @return
     */
    List<EntityTypeTemplateRelation> loadEntityTypeTemplateRelation();

    /**
     * 删除指定模板的所有指标规则
     * @param templateId
     */
    void deleteThresholdRulesByTemplateId(Integer templateId);

    /**
     * 获取拥有默认模板的设备子类型
     * @return
     */
    List<Integer> loadSubTypeWhichHasDefaultTemp();

    /**
     * 批量为设备绑定模板
     * @param perftemplateentityrelations
     */
    void bindTemplateToEntity(List<PerfTempEntityRelation> perftemplateentityrelations);

    /**
     * 根据设备类型ID获取显示名
     * @param parentType
     * @return
     */
    String getDisplayNameByEntityTypeId(Integer entityTypeId);

    /**
     * 加载指定设备类型的性能全局配置
     * @param type
     * @return
     */
    PerfGlobal loadPerfGolbalByType(Long type);

    /**
     * 加载指定设备类型的性能全局配置
     * @param typeId
     * @return PerfThresholdTemplate
     */
    PerfThresholdTemplate getDefaultTemplateByEntityType(Long typeId);

    /**
     * 加载指定设备类型的性能全局配置
     * @param typeId
     * @return 
     */
    List<Integer> getDefaultTemplateList(Integer typeId);

    /**
     * 更新设备与阈值模板关联关系表
     * @param entityTemplate
     */
    void updateTemplateEntityRelation(PerfTempEntityRelation entityTemplate);

    /**
     * 批量更新设备与阈值模板关联关系表
     * @param entityTempList
     */
    void batchUpdateTempEntityRelation(List<PerfTempEntityRelation> entityTempList);

    /**
     * 查询设备与模板关联关系
     */
    PerfTempEntityRelation queryEntityRelationTemp(Long entityId);

    /**
     * 在删除模板时将关联关系置为默认值
     * @param map
     */
    void updateRelationByTemplateId(Map<String, Object> map);

    /**
     * 更新阈值指标状态
     * @param target
     */
    void updateTargetStatus(PerfTarget target);

    /**
     * 查询指定的阈值指标
     * @param targetId
     * @return
     */
    PerfTarget queryPerfTarget(String targetId);

    /**
     * 更新阈值指标的阈值范围
     * @param target
     */
    void updateTargetValue(PerfTarget target);

    /**
     * 获取所有类型与指标数量的关系
     * @return
     */
    Map<String, Object> getAllTargetCount();

    /**
     * 根据类型查询对应的指标数量
     * @param targetType
     * @return
     */
    int queryTargetCountByType(Integer targetType);

    /**
     * 查询包含指定指标的阈值规则数量
     * @param targetId
     * @return
     */
    int queryTargetReRuleCount(String targetId);

    /**
     * @param map
     * @return
     */
    List<ThresholdAlertValue> selectLastedAlertValue();

    /**
     * @param thresholdAlertValue
     */
    void insertOrUpdateAlertValue(ThresholdAlertValue thresholdAlertValue);

    /**
     * @param map
     */
    void deleteLastedAlertData(ThresholdAlertValue thresholdAlertValue);

    void batchDeleteLastedAlertData(List<Alert> list);

}
