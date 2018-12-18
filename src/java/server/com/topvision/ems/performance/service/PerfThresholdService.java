/***********************************************************************
 * $Id: PerfThresholdService.java,v1.0 2013-6-8 下午02:34:42 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.performance.domain.EntityTypeTemplateRelation;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.domain.PerfTarget;
import com.topvision.ems.performance.domain.PerfTempEntityRelation;
import com.topvision.ems.performance.domain.PerfThresholdEntity;
import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.ems.performance.domain.PerfThresholdTemplate;
import com.topvision.ems.performance.domain.ThresholdAlertValue;
import com.topvision.framework.service.Service;

/**
 * @author Rod John
 * @created @2013-6-8-下午02:34:42
 * 
 */
public interface PerfThresholdService extends Service {

    /**
     * 根据entityId获取相应的设备阈值模板情况
     * @param map
     * @return PerfThresholdEntity
     */
    PerfThresholdEntity selectPerfTemplateByEntityId(Long entityId);

    /**
     * 根据阈值指标与模板查询对应的规则
     * @param map
     * @return PerfThresholdRule
     */
    List<PerfThresholdRule> selectTargetRulesByTargetId(Integer templateId, String targetId);

    /**
     * 根模板与设备的关联关系缓存
     * @return Map<Long, PerfThresholdEntity>
     */
    Map<Long, PerfThresholdEntity> getEntityTemplateCache();

    /**
     * 获得阈值指标与规则的关联关系缓存
     * @return Map<String, List<PerfThresholdRule>>
     */
    Map<String, List<PerfThresholdRule>> getTemplateTargetRuleCache();

    /**
     * 根据查询条件获取相应的设备阈值模板情况
     * @param map
     * @return
     */
    List<PerfThresholdEntity> selectEntityPerfThresholdTmeplate(Map<String, Object> map);

    /**
     * 根据查询条件获取相应的设备阈值模板总数
     * @param map
     * @return
     */
    Long selectPerfTmeplateCount(Map<String, Object> map);

    /**
     * 加载模板列表
     * 
     * @return
     */
    List<PerfThresholdTemplate> loadThresholdTemplateList(Integer parentType);

    /**
     * 获得模板
     * @param templateId
     * @return
     */
    PerfThresholdTemplate getThresholdTemplate(Integer templateId);

    /**
     * 获得模板
     * @param templateName
     * @return
     */
    PerfThresholdTemplate getThresholdTemplateByName(String templateName);

    /**
     * 加载模板规则
     * 
     * @param templateId
     * @return
     */
    List<PerfThresholdRule> loadThresholdTemplateRules(Integer templateId);

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
    void addThresholdTemplate(PerfThresholdTemplate template);

    /**
     * 修改阈值模板
     * 
     * @param template
     */
    void modifyThresholdTemplate(PerfThresholdTemplate template);

    /**
     * 删除阈值模板
     * 
     * @param templateId
     */
    void deleteThresholdTemplate(Integer templateId);

    /**
     * 批量添加阈值规则
     * @param perfThresholdRules
     * @author fanzidong
     */
    void batchAddThresholdRule(List<PerfThresholdRule> perfThresholdRules);

    /**
     * 新增阈值规则
     * 
     * @param thresholdRule
     */
    void addThresholdRule(PerfThresholdRule thresholdRule);

    /**
     * 获得阈值规则
     * 
     * @param ruleId
     */
    PerfThresholdRule getThresholdRuleById(Integer ruleId);

    /**
     * 获得阈值规则
     * 
     * @param templateId
     * @param targetId
     */
    PerfThresholdRule getThresholdRuleByName(Integer templateId, String targetId);

    /**
     * 修改阈值规则
     * 
     * @param thresholdRule
     */
    void modifyThresholdRule(PerfThresholdRule thresholdRule);

    /**
     * 应用模板到设备
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
     * 加载指定设备类型的性能全局配置
     * @param type
     * @return
     */
    PerfGlobal loadPerfGolbalByType(Long type);

    /**
     * 加载OLT的全局配置
     * 
     * @return
     */
    PerfGlobal getOltPerfGlobal();

    /**
     * 加载CMTS&CC的全局配置
     * 
     * @return
     */
    PerfGlobal getCmtsPerfGlobal();

    /**
     * 修改系统性能全局配置
     * 
     * @param perfGlobal
     */
    void modifyPerfGlobal(PerfGlobal perfGlobal);

    /**
     * 获得已关联阈值模板设备列表
     * 
     * @param perfThresholdEntity
     * @return
     */
    List<PerfThresholdEntity> loadRelaTemplateEntityList(PerfThresholdEntity perfThresholdEntity);

    /**
     * 获得已关联阈值模板设备总数
     * 
     * @param perfThresholdEntity
     * @return
     */
    Long loadRelaTemplateCount(PerfThresholdEntity perfThresholdEntity);

    /**
     * 获得未关联阈值模板设备列表
     * 
     * @param perfThresholdEntity
     * @return
     */
    List<PerfThresholdEntity> loadNoRelaTemplateEntityList(PerfThresholdEntity perfThresholdEntity);

    /**
     * 获得未关联阈值模板设备总数
     * 
     * @param perfThresholdEntity
     * @return
     */
    Long loadNoRelaTemplateCount(PerfThresholdEntity perfThresholdEntity);

    /**
     * 获得模板的性能指标列表
     * 
     * @param targetType
     * 
     * @return
     */
    List<PerfTarget> loadPerfTargetList(Integer targetType);

    /**
     * 获取所有的阈值指标
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
     * 加载设备子类型
     * @return
     */
    JSONArray loadEntitySubType(Integer entityType);

    /**
     * 加载所有设备类型(加上是否为子类型标记)
     * @return
     */
    List<EntityTypeTemplateRelation> loadEntityTypeTemplateRelation();

    /**
     * 删除指定模板的所有指标规则
     * @param templateId
     */
    void deleteThresholdRulesByTemplateId(Integer templateId);

    /**
     * 获取拥有默认模板的设备子类型(返回的是子类型typeId的列表)
     * @return
     */
    JSONArray loadSubTypeWhichHasDefaultTemp();

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
     * 根据设备类型 获得默认模板
     * @param typeId
     * @return
     */
    PerfThresholdTemplate getDefaultTemplateByEntityType(Long typeId);

    /**
     * 判断设备是否已关联模板
     * @param entityId
     * @return
     */
    boolean isEntityApplyTemplate(Long entityId);

    /* *//**
                                             * 判断设备是否开启阈值告警
                                             * @param entityId
                                             * @return
                                             */
    /*
    boolean isEntityStartThreshold(Long entityId);*/

    /**
     * 默认阈值模版类型
     * @param templateType
     * @return
     */
    List<Integer> getDefaultTemplateList(Integer templateType);

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
    PerfTarget getPerfTarget(String targetId);

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
    int getTargetCountByType(Integer targetType);

    /**
     * 获取包含指定指标的阈值规则数目
     * @param targetId
     * @return
     */
    int getTargetReRuleCount(String targetId);

    /**
     * 更新设备与阈值模板关联关系表
     * @param entityTemplate
     */
    void modifyTemplateEntityRelation(PerfTempEntityRelation entityTemplate);

    /**
     * 获取指定类型的性能全局配置
     * @param type
     * @return
     */
    PerfGlobal getPerfGlobalByType(Long type);

    void unLinkEntityTemplate(List<PerfTempEntityRelation> entityTempList);

    /**
     * 
     * @param entityId
     * @param source
     * @param alertEventId
     * @param perfValue
     * @param levelId 
     */
    void insertOrUpdateAlertValue(Long entityId, String source, Integer alertEventId, Float perfValue, Integer levelId);

    /**
     * 
     * @return
     */
    List<ThresholdAlertValue> queryLastedAlertValue();

    /**
     * 
     * @param entityId
     * @param source
     * @param alertEventId
     */
    void removeLastedAlertData(Long entityId, String source, Integer alertEventId);

    void removeLastedAlertData(List<Alert> list);
}
