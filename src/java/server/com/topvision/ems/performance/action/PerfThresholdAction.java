/***********************************************************************
 * $Id: PerfThresholdAction.java,v1.0 2013-9-25 下午8:25:00 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.performance.domain.EntityTypeTemplateRelation;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.domain.PerfTarget;
import com.topvision.ems.performance.domain.PerfTempEntityRelation;
import com.topvision.ems.performance.domain.PerfThresholdEntity;
import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.ems.performance.domain.PerfThresholdTemplate;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author fanzidong
 * @created @2013-9-25-下午8:25:00
 * 性能阈值告警主action
 */
@Controller("perfThresholdAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PerfThresholdAction extends BaseAction {
    private static final long serialVersionUID = 7742253693250396939L;
    @Autowired
    protected PerfThresholdService perfThresholdService;
    @Autowired
    protected EntityTypeService entityTypeService;

    protected List<PerfThresholdTemplate> perfTemplateList;
    protected List<PerfThresholdEntity> perfThresholdList;
    protected PerfThresholdTemplate perfThresholdTemplate;

    protected JSONArray allEntityTypes;
    protected JSONArray perfThresholdTargets;
    protected JSONObject perfThresholdTemplateJson;
    protected JSONArray perfThresholdRuleJson;
    protected JSONObject perfTargets;

    protected String templateIds;
    protected String templateName;
    protected String perfTargetList;
    protected String entityName;
    protected String entityIp;
    protected String mac;
    protected String entityIds;
    protected Integer templateId;
    protected Integer templateType;
    protected Integer subTemplateType;
    protected Integer entityType;
    protected Integer typeId;
    protected Integer parentType;
    protected Boolean saveAsDefaultTemplate;
    protected Boolean basicTemplate;
    protected Integer start;
    protected Integer limit;
    private JSONObject perfTargetJson;
    private List<Integer> defaultTemplateType = new ArrayList<Integer>();

    private String originalFrame; //源frame，用于标识添加/修改性能指标时从哪个frame取值
    //用于保存阈值类型与数量的关系
    private JSONObject targetCountJson;
    private String targetId;

    /**
     * 显示性能模板界面
     * @return
     */
    public String showPerfTemplate() {
        return SUCCESS;
    }

    /**
     * 加载性能阈值模板列表
     * @return
     * @throws IOException 
     */
    public String loadTemplateList() throws IOException {
        JSONObject json = new JSONObject();
        perfTemplateList = perfThresholdService.loadThresholdTemplateList(null);
        //json.put("rowCount", perfTemplateList.size());
        json.put("data", perfTemplateList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 打开新建阈值模板界面
     * 创建模板页面需要知道的信息：模板的类型、子类型、有哪些子类型有默认模板、不同类型模板的指标数量
     * 整理成如下信息，传递给前端：模板类型列表(包括其是否为子类型，及其是否有默认模板)、指标数量(包括其属于的模板类型)
     * @return
     * @throws IOException 
     */
    public String showCreateTemplate() {
        //获取类型列表(包括其是否为子类型，及其是否有默认模板)
        List<EntityTypeTemplateRelation> entityTypeTemplateRelations = perfThresholdService
                .loadEntityTypeTemplateRelation();
        allEntityTypes = JSONArray.fromObject(entityTypeTemplateRelations);
        //获取指标类型与指标数量对应关系
        Map<String, Object> typeCount = perfThresholdService.getAllTargetCount();
        targetCountJson = JSONObject.fromObject(typeCount);
        return SUCCESS;
    }

    /**
     * 展示修改阈值模板界面
     * 修改模板页面需要知道的信息：该模板的信息、模板的类型、子类型、有哪些子类型有默认模板、不同类型模板的指标列表
     * 整理成如下信息，传递给前端：模板类型列表(包括其是否为子类型，及其是否有默认模板)、指标列表(包括其属于的模板类型)、该模板的参数信息
     * @return
     */
    public String showModifyTemplate() {
        PerfThresholdTemplate template = perfThresholdService.getThresholdTemplate(templateId);
        //获得该模板的基本参数信息
        perfThresholdTemplateJson = JSONObject.fromObject(template);
        //获取该模板的指标信息(包括其显示名称)
        List<PerfThresholdRule> perfThresholdRules = perfThresholdService.loadThresholdTemplateRules(templateId);
        //转换温度阈值
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (tempUnit != null && UnitConfigConstant.FAHR_TEMP_UNIT.equals(tempUnit)) {
            for (PerfThresholdRule rule : perfThresholdRules) {
                if (UnitConfigConstant.TEMPRELATED_THRESHOLDS.contains(rule.getTargetId())) {
                    rule.setTargetUnit(tempUnit);
                    rule.setThresholds(UnitConfigConstant.parseThresholdRuleValue(rule.getThresholds()));
                }
            }
        }
        //转换电平阈值
        String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        if (powerUnit != null && UnitConfigConstant.MILLI_VOLT_UNIT.equals(powerUnit)) {
            for (PerfThresholdRule rule : perfThresholdRules) {
                if (UnitConfigConstant.ONU_CATV_RF.equals(rule.getTargetId())) {
                    rule.setTargetUnit(powerUnit);
                    rule.setThresholds(UnitConfigConstant.parseRuleDBμVToDBmV(rule.getThresholds()));
                }
            }
        }
        perfThresholdRuleJson = JSONArray.fromObject(perfThresholdRules);
        //获取模板类型列表(包括其是否为子类型，及其是否有默认模板)
        List<EntityTypeTemplateRelation> entityTypeTemplateRelations = perfThresholdService
                .loadEntityTypeTemplateRelation();
        allEntityTypes = JSONArray.fromObject(entityTypeTemplateRelations);
        //保持与新建模板时指标类型与指标数量关系的一致性结构,在使用时可以直接根据类型查找
        targetCountJson = new JSONObject();
        targetCountJson.put(template.getParentType(),
                perfThresholdService.getTargetCountByType(template.getParentType()));
        return SUCCESS;
    }

    /**
     * 修改模板
     * @return
     */
    public String modifyTemplate() {
        //创建模板对象
        PerfThresholdTemplate perfThresholdTemplate = new PerfThresholdTemplate();
        perfThresholdTemplate.setTemplateId(templateId);
        perfThresholdTemplate.setTemplateName(templateName);
        perfThresholdTemplate.setModifyTime(new java.sql.Timestamp(System.currentTimeMillis()));
        //
        if (!basicTemplate) {
            perfThresholdTemplate.setIsDefaultTemplate(saveAsDefaultTemplate);
            if (saveAsDefaultTemplate) {
                perfThresholdTemplate.setTemplateType(subTemplateType);
            } else {
                perfThresholdTemplate.setTemplateType(templateType);
            }
        } else {
            perfThresholdTemplate.setIsDefaultTemplate(true);
            perfThresholdTemplate.setTemplateType(templateType);
        }
        perfThresholdService.modifyThresholdTemplate(perfThresholdTemplate);
        //删除该模板相关的所有指标，再重新添加
        perfThresholdService.deleteThresholdRulesByTemplateId(templateId);
        //构建当前新建模板所包含的指标
        if (perfTargetList != null && !perfTargetList.equals("")) {
            List<PerfThresholdRule> perfThresholdRules = new ArrayList<PerfThresholdRule>();
            String[] strs = perfTargetList.split("%");
            for (String str : strs) {
                PerfThresholdRule perfThresholdRule = PerfThresholdRule.formatStrToObject(str);
                perfThresholdRule.setTemplateId(templateId);
                perfThresholdRules.add(perfThresholdRule);
            }
            perfThresholdService.batchAddThresholdRule(perfThresholdRules);
        }
        return NONE;
    }

    /**
     * 删除性能阈值模板
     * 
     * @return
     * @throws Exception
     */
    public String deletePerfTemplate() {
        String[] strs = templateIds.split(",");
        for (int i = 0; i < strs.length; i++) {
            perfThresholdService.deleteThresholdTemplate(Integer.valueOf(strs[i]));
        }
        return NONE;
    }
    
    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.performance.resources");
    }
    
    /**
     * 显示为模板添加性能指标页面
     * @return
     */
    public String addPerfTargetsToTemplate() {
        //获得指定设备类型对应的所有指标
        List<PerfTarget> perfTargetList = perfThresholdService.loadPerfTargetList(parentType);
        //将指标按照分组封装成合适的结构，以生成指标选择下拉结构
        perfTargets = PerfTargetUtil.groupPerfThresholdTarget(perfTargetList);
        //转换温度单位
        String tempUnit = (String) UnitConfigConstant.get("tempUnit");
        if (tempUnit != null && getResourceManager().getString("PerformanceAlert.fahrenheit").equals(tempUnit)) {
            for (PerfTarget perfTarget : perfTargetList) {
                if (UnitConfigConstant.TEMPRELATED_THRESHOLDS.contains(perfTarget.getTargetId())) {
                    perfTarget.setUnit(tempUnit);
                    perfTarget.setMaxNum(new BigDecimal(UnitConfigConstant.translateTemperature(perfTarget.getMaxNum()
                            .doubleValue())));
                    perfTarget.setMinNum(new BigDecimal(UnitConfigConstant.translateTemperature(perfTarget.getMinNum()
                            .doubleValue())));
                }
            }
        }
        //转换电平阈值
        String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        if (powerUnit != null && UnitConfigConstant.MILLI_VOLT_UNIT.equals(powerUnit)) {
            for (PerfTarget target : perfTargetList) {
                if (UnitConfigConstant.ONU_CATV_RF.equals(target.getTargetId())) {
                    target.setUnit(powerUnit);
                    target.setMaxNum(new BigDecimal(UnitConfigConstant
                            .transDBμVToDBmV(target.getMaxNum().doubleValue())));
                    target.setMinNum(new BigDecimal(UnitConfigConstant
                            .transDBμVToDBmV(target.getMinNum().doubleValue())));
                }
            }
        }
        //所有指标信息
        perfThresholdTargets = JSONArray.fromObject(perfTargetList);
        return SUCCESS;
    }

    /**
     * 校验模板名称是否存在
     * 
     * @return
     * @throws Exception
     */
    public String checkTemplateName() throws Exception {
        JSONObject json = new JSONObject();
        PerfThresholdTemplate perfThresholdTemplate = perfThresholdService.getThresholdTemplateByName(templateName);
        if (perfThresholdTemplate != null) {
            json.put("result", true);
        } else {
            json.put("result", false);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加模板
     * @return
     */
    public String addTemplate() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        //创建模板对象
        PerfThresholdTemplate perfThresholdTemplate = new PerfThresholdTemplate();
        perfThresholdTemplate.setTemplateName(templateName);
        perfThresholdTemplate.setIsDefaultTemplate(saveAsDefaultTemplate);
        perfThresholdTemplate.setParentType(templateType);
        if (saveAsDefaultTemplate) {
            perfThresholdTemplate.setTemplateType(subTemplateType);
        } else {
            perfThresholdTemplate.setTemplateType(templateType);
        }
        java.sql.Timestamp createTime = new java.sql.Timestamp(System.currentTimeMillis());
        perfThresholdTemplate.setCreateTime(createTime);
        perfThresholdTemplate.setModifyTime(createTime);
        perfThresholdTemplate.setCreateUser(uc.getUser().getUserName());
        perfThresholdService.addThresholdTemplate(perfThresholdTemplate);

        //为创建的模板添加指标
        Integer templateId = perfThresholdTemplate.getTemplateId();
        //构建当前新建模板所包含的指标
        if (perfTargetList != null && !perfTargetList.equals("")) {
            List<PerfThresholdRule> perfThresholdRules = new ArrayList<PerfThresholdRule>();
            String[] strs = perfTargetList.split("%");
            for (String str : strs) {
                PerfThresholdRule perfThresholdRule = PerfThresholdRule.formatStrToObject(str);
                perfThresholdRule.setTemplateId(templateId);
                perfThresholdRules.add(perfThresholdRule);
            }
            perfThresholdService.batchAddThresholdRule(perfThresholdRules);
        }
        return NONE;
    }

    /**
     * 显示为模板修改性能指标页面
     * @return
     */
    public String modifyTemplatePerfTargets() {
        PerfTarget editTarget = perfThresholdService.getPerfTarget(targetId);
        //转换温度单位
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (tempUnit != null && UnitConfigConstant.FAHR_TEMP_UNIT.equals(tempUnit)) {
            if (UnitConfigConstant.TEMPRELATED_THRESHOLDS.contains(editTarget.getTargetId())) {
                editTarget.setUnit(tempUnit);
                editTarget.setMaxNum(new BigDecimal(UnitConfigConstant.translateTemperature(editTarget.getMaxNum()
                        .doubleValue())));
                editTarget.setMinNum(new BigDecimal(UnitConfigConstant.translateTemperature(editTarget.getMinNum()
                        .doubleValue())));
            }
        }
        perfTargetJson = JSONObject.fromObject(editTarget);
        return SUCCESS;
    }

    /**
     * 显示应用阈值模板界面
     * @return
     */
    public String showApplyTemplateJsp() {
        PerfThresholdTemplate perfThresholdTemplate = perfThresholdService.getThresholdTemplate(templateId);
        Long templateType = perfThresholdTemplate.getTemplateType().longValue();
        if (entityTypeService.isCategoryType(templateType)) {
            parentType = templateType.intValue();
        } else {
            parentType = entityTypeService.getEntityNetworkGroupIdByEntityTypeId(
                    perfThresholdTemplate.getTemplateType().longValue()).intValue();
        }
        perfThresholdTemplate.setParentType(parentType);
        setPerfThresholdTemplate(perfThresholdTemplate);
        //获取所有设备类型
        List<EntityTypeTemplateRelation> entityTypeTemplateRelations = perfThresholdService
                .loadEntityTypeTemplateRelation();
        allEntityTypes = JSONArray.fromObject(entityTypeTemplateRelations);
        return SUCCESS;
    }

    /**
     * 加载未关联阈值模板设备列表
     * @throws IOException 
     * 
     */
    public String loadNoRelaTemplateEntityList() throws IOException {
        JSONObject json = new JSONObject();
        //组装查询数据
        PerfThresholdEntity perfThresholdEntity = new PerfThresholdEntity();
        perfThresholdEntity.setTemplateId(templateId);
        perfThresholdEntity.setEntityName(entityName);
        perfThresholdEntity.setEntityIp(entityIp);
        perfThresholdEntity.setMac(mac);
        perfThresholdEntity.setTypeId(typeId);
        perfThresholdEntity.setParentTypeId(parentType);
        perfThresholdEntity.setStart(start);
        perfThresholdEntity.setLimit(limit);
        //查询数据
        perfThresholdList = perfThresholdService.loadNoRelaTemplateEntityList(perfThresholdEntity);
        Long perfThresholdCount = perfThresholdService.loadNoRelaTemplateCount(perfThresholdEntity);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (PerfThresholdEntity thresholdEntity : perfThresholdList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(thresholdEntity.getMac(), macRule);
            thresholdEntity.setMac(formatedMac);
        }
        json.put("data", perfThresholdList);
        json.put("rowCountUn", perfThresholdCount);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载关联阈值模板设备列表
     * @throws IOException 
     * 
     */
    public String loadRelaTemplateEntityList() throws IOException {
        JSONObject json = new JSONObject();
        //组装查询数据
        PerfThresholdEntity perfThresholdEntity = new PerfThresholdEntity();
        perfThresholdEntity.setTemplateId(templateId);
        perfThresholdEntity.setEntityName(entityName);
        perfThresholdEntity.setEntityIp(entityIp);
        perfThresholdEntity.setMac(mac);
        perfThresholdEntity.setTypeId(typeId);
        perfThresholdEntity.setStart(start);
        perfThresholdEntity.setLimit(limit);
        //查询数据
        perfThresholdList = perfThresholdService.loadRelaTemplateEntityList(perfThresholdEntity);
        Long perfThresholdCount = perfThresholdService.loadRelaTemplateCount(perfThresholdEntity);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (PerfThresholdEntity thresholdEntity : perfThresholdList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(thresholdEntity.getMac(), macRule);
            thresholdEntity.setMac(formatedMac);
        }
        json.put("data", perfThresholdList);
        json.put("rowCountRe", perfThresholdCount);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 关联性能阈值模板
     * 
     * @return
     * @throws Exception 
     */
    public String saveEntityRelaTemplate() throws Exception {
        List<PerfTempEntityRelation> perftemplateentityrelations = new ArrayList<PerfTempEntityRelation>();
        Integer isPerfOn = 0;
        if (entityTypeService.getOltType().equals(templateType.longValue())) {
            isPerfOn = perfThresholdService.getOltPerfGlobal().getIsPerfThreshold();
        } else if (entityTypeService.getCcmtsandcmtsType().equals(templateType.longValue())) {
            isPerfOn = perfThresholdService.getCmtsPerfGlobal().getIsPerfThreshold();
        }
        String[] eIs = entityIds.split(",");
        for (String id : eIs) {
            PerfTempEntityRelation perftemplateentityrelation = new PerfTempEntityRelation();
            perftemplateentityrelation.setEntityId(Long.parseLong(id.trim()));
            perftemplateentityrelation.setTemplateId(templateId);
            perftemplateentityrelation.setIsPerfThreshold(isPerfOn);
            perftemplateentityrelations.add(perftemplateentityrelation);
        }
        perfThresholdService.bindTemplateToEntity(perftemplateentityrelations);
        return NONE;
    }

    /**
     * 解除设备性能阈值模板关联
     * 
     * @return
     * @throws Exception 
     */
    public String removeEntityRelaTemplate() throws Exception {
        String[] eIs = entityIds.split(",");
        List<PerfTempEntityRelation> entityTempList = new ArrayList<PerfTempEntityRelation>();
        PerfTempEntityRelation entityTemplate = null;
        for (String id : eIs) {
            entityTemplate = new PerfTempEntityRelation();
            entityTemplate.setEntityId(Long.parseLong(id.trim()));
            entityTemplate.setTemplateId(EponConstants.TEMPLATE_ENTITY_UNLINK);
            entityTemplate.setIsPerfThreshold(EponConstants.PERF_PTHRESHOLD_OFF);
            entityTempList.add(entityTemplate);
        }
        perfThresholdService.unLinkEntityTemplate(entityTempList);
        return NONE;
    }

    /**
     * 开启设备阈值告警
     * 
     * @return
     * @throws Exception 
     */
    public String openDeviceThreshold() {
        String[] eIs = entityIds.split(",");
        for (String id : eIs) {
            perfThresholdService.startEntityThreshold(Long.parseLong(id));
        }
        return NONE;
    }

    /**
     * 关闭设备阈值告警
     * 
     * @return
     * @throws Exception 
     */
    public String closeDeviceThreshold() {
        String[] eIs = entityIds.split(",");
        for (String id : eIs) {
            perfThresholdService.stopEntityThreshold(Long.parseLong(id));
        }
        return NONE;
    }

    /**
     * 显示对应设备类型的模板列表页面 
     * @return
     */
    public String showTemplateDetailList() {
        return SUCCESS;
    }

    /**
     * 加载指定类型的所有模板
     * @return
     * @throws IOException
     */
    public String loadTemplateListByType() throws IOException {
        JSONObject json = new JSONObject();
        perfTemplateList = perfThresholdService.loadThresholdTemplateList(templateType);
        json.put("rowCount", perfTemplateList.size());
        json.put("data", perfTemplateList);
        json.write(response.getWriter());
        return NONE;
    }

    public String bindTemplateToEntity() throws Exception {
        List<PerfTempEntityRelation> perftemplateentityrelations = new ArrayList<PerfTempEntityRelation>();
        //在绑定模板时,阈值告警开关使用全局配置
        Integer isPerfOn = 0;
        PerfGlobal perfGlobal = perfThresholdService.getPerfGlobalByType(templateType.longValue());
        if (perfGlobal != null) {
            isPerfOn = perfGlobal.getIsPerfThreshold();
        }
        /*if (entityTypeService.getOltType().equals(templateType.longValue())) {
            isPerfOn = perfThresholdService.getOltPerfGlobal().getIsPerfThreshold();
        } else if (entityTypeService.getCcmtsandcmtsType().equals(templateType.longValue())) {
            isPerfOn = perfThresholdService.getCmtsPerfGlobal().getIsPerfThreshold();
        }*/
        String[] eIs = entityIds.split(",");
        for (String id : eIs) {
            PerfTempEntityRelation perftemplateentityrelation = new PerfTempEntityRelation();
            perftemplateentityrelation.setEntityId(Long.parseLong(id));
            perftemplateentityrelation.setTemplateId(templateId);
            perftemplateentityrelation.setIsPerfThreshold(isPerfOn);
            perftemplateentityrelations.add(perftemplateentityrelation);
        }
        perfThresholdService.bindTemplateToEntity(perftemplateentityrelations);
        return NONE;
    }

    public String showTemplateDetail_readonly() {
        //获取该模板对应的模板信息及模板的指标信息
        PerfThresholdTemplate perfThresholdTemplate = perfThresholdService.getThresholdTemplate(templateId);
        perfThresholdTemplateJson = JSONObject.fromObject(perfThresholdTemplate);
        String moduleName = perfThresholdService.getDisplayNameByEntityTypeId(perfThresholdTemplate.getParentType());
        String subTypeName = perfThresholdService.getDisplayNameByEntityTypeId(perfThresholdTemplate.getTemplateType());
        perfThresholdTemplateJson.put("moduleName", moduleName);
        perfThresholdTemplateJson.put("subTypeName", subTypeName);
        List<PerfThresholdRule> perfThresholdRules = perfThresholdService.loadThresholdTemplateRules(templateId);
        perfThresholdRuleJson = JSONArray.fromObject(perfThresholdRules);
        //将所有的阈值指标的信息保存起来，以供查询单位等消息
        perfThresholdTargets = new JSONArray();
        perfTargetJson = new JSONObject();
        List<PerfTarget> oltPerfargets = perfThresholdService.loadPerfTargetList(entityTypeService.getOltType()
                .intValue());
        List<PerfTarget> cmcPerfargets = perfThresholdService.loadPerfTargetList(entityTypeService.getCcmtsType()
                .intValue());
        for (PerfTarget p : oltPerfargets) {
            perfTargetJson.put(p.getTargetId(), PerfTargetUtil.getString(p.getTargetDisplayName(), "performance"));
            perfThresholdTargets.add(JSONObject.fromObject(p));
        }
        for (PerfTarget p : cmcPerfargets) {
            perfTargetJson.put(p.getTargetId(), PerfTargetUtil.getString(p.getTargetDisplayName(), "performance"));
            perfThresholdTargets.add(JSONObject.fromObject(p));
        }
        return SUCCESS;
    }

    public JSONArray getAllEntityTypes() {
        return allEntityTypes;
    }

    public void setAllEntityTypes(JSONArray allEntityTypes) {
        this.allEntityTypes = allEntityTypes;
    }

    public List<PerfThresholdTemplate> getPerfTemplateList() {
        return perfTemplateList;
    }

    public void setPerfTemplateList(List<PerfThresholdTemplate> perfTemplateList) {
        this.perfTemplateList = perfTemplateList;
    }

    public JSONArray getPerfThresholdTargets() {
        return perfThresholdTargets;
    }

    public void setPerfThresholdTargets(JSONArray perfThresholdTargets) {
        this.perfThresholdTargets = perfThresholdTargets;
    }

    public String getTemplateIds() {
        return templateIds;
    }

    public void setTemplateIds(String templateIds) {
        this.templateIds = templateIds;
    }

    public PerfThresholdTemplate getPerfThresholdTemplate() {
        return perfThresholdTemplate;
    }

    public void setPerfThresholdTemplate(PerfThresholdTemplate perfThresholdTemplate) {
        this.perfThresholdTemplate = perfThresholdTemplate;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public JSONObject getPerfThresholdTemplateJson() {
        return perfThresholdTemplateJson;
    }

    public void setPerfThresholdTemplateJson(JSONObject perfThresholdTemplateJson) {
        this.perfThresholdTemplateJson = perfThresholdTemplateJson;
    }

    public JSONArray getPerfThresholdRuleJson() {
        return perfThresholdRuleJson;
    }

    public void setPerfThresholdRuleJson(JSONArray perfThresholdRuleJson) {
        this.perfThresholdRuleJson = perfThresholdRuleJson;
    }

    public JSONObject getPerfTargets() {
        return perfTargets;
    }

    public void setPerfTargets(JSONObject perfTargets) {
        this.perfTargets = perfTargets;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Boolean getSaveAsDefaultTemplate() {
        return saveAsDefaultTemplate;
    }

    public void setSaveAsDefaultTemplate(Boolean saveAsDefaultTemplate) {
        this.saveAsDefaultTemplate = saveAsDefaultTemplate;
    }

    public Integer getSubTemplateType() {
        return subTemplateType;
    }

    public void setSubTemplateType(Integer subTemplateType) {
        this.subTemplateType = subTemplateType;
    }

    public String getPerfTargetList() {
        return perfTargetList;
    }

    public void setPerfTargetList(String perfTargetList) {
        this.perfTargetList = perfTargetList;
    }

    public Boolean getBasicTemplate() {
        return basicTemplate;
    }

    public void setBasicTemplate(Boolean basicTemplate) {
        this.basicTemplate = basicTemplate;
    }

    public List<PerfThresholdEntity> getPerfThresholdList() {
        return perfThresholdList;
    }

    public void setPerfThresholdList(List<PerfThresholdEntity> perfThresholdList) {
        this.perfThresholdList = perfThresholdList;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public JSONObject getPerfTargetJson() {
        return perfTargetJson;
    }

    public void setPerfTargetJson(JSONObject perfTargetJson) {
        this.perfTargetJson = perfTargetJson;
    }

    public String getOriginalFrame() {
        return originalFrame;
    }

    public void setOriginalFrame(String originalFrame) {
        this.originalFrame = originalFrame;
    }

    public List<Integer> getDefaultTemplateType() {
        return defaultTemplateType;
    }

    public void setDefaultTemplateType(List<Integer> defaultTemplateType) {
        this.defaultTemplateType = defaultTemplateType;
    }

    public JSONObject getTargetCountJson() {
        return targetCountJson;
    }

    public void setTargetCountJson(JSONObject targetCountJson) {
        this.targetCountJson = targetCountJson;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

}
