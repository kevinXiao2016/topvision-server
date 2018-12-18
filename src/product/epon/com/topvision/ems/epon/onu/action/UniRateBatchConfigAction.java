/***********************************************************************
 * $Id: UniRateBatchConfigAction.java,v1.0 2014-5-15 上午10:54:23 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.batchdeploy.domain.Type;
import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.batchdeploy.service.EponBatchDeployService;
import com.topvision.ems.epon.onu.domain.UniRateLimitTemplate;
import com.topvision.ems.epon.onu.service.UniRateBatchConfigService;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.ems.facade.batchdeploy.domain.ResultBundle;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author flack
 * @created @2014-5-15-上午10:54:23
 *
 */
@Controller("uniRateBatchConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UniRateBatchConfigAction extends BaseAction {
    private static final long serialVersionUID = 8005070950444690889L;

    private Long entityId;
    private Integer templateId;
    private String templateName;
    private Integer portInLimitEnable;
    private Integer portInCIR;
    private Integer portInCBS;
    private Integer portInEBS;
    private Integer portOutLimtEnable;
    private Integer portOutCIR;
    private Integer portOutPIR;
    private String needBinds;
    private String excudeBinds;

    @Autowired
    private UniRateBatchConfigService uniRateBatchConfigService;
    @Autowired
    private EponBatchDeployService eponBatchDeployService;

    /**
     * 显示Uni口限速批量配置页面
     * 
     * @return
     */
    public String showUniRateBatchConfig() {
        return SUCCESS;
    }

    /**
     * 加载Uni口限速模板
     * 
     * @return
     * @throws IOException
     */
    public String loadUniRateTemplate() throws IOException {
        List<UniRateLimitTemplate> templateList = uniRateBatchConfigService.getTemplateList(entityId);
        JSONArray tempateArray = JSONArray.fromObject(templateList);
        tempateArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加Uni口限速模板
     * 
     * @return
     */
    public String addUniRateTemplate() {
        UniRateLimitTemplate rateTemplate = new UniRateLimitTemplate();
        rateTemplate.setEntityId(entityId);
        rateTemplate.setTemplateName(templateName);
        rateTemplate.setPortInLimitEnable(portInLimitEnable);
        rateTemplate.setPortInCIR(portInCIR);
        rateTemplate.setPortInCBS(portInCBS);
        rateTemplate.setPortInEBS(portInEBS);
        rateTemplate.setPortOutLimtEnable(portOutLimtEnable);
        rateTemplate.setPortOutCIR(portOutCIR);
        rateTemplate.setPortOutPIR(portOutPIR);
        rateTemplate.setCreateTime(new Date());
        rateTemplate.setUpdateTime(new Date());
        uniRateBatchConfigService.addTemplate(rateTemplate);
        return NONE;
    }

    /**
     * 修改Uni口限速模板
     * 
     * @return
     */
    public String modifyUniRateTemplate() {
        UniRateLimitTemplate rateTemplate = new UniRateLimitTemplate();
        rateTemplate.setTemplateId(templateId);
        rateTemplate.setEntityId(entityId);
        rateTemplate.setTemplateName(templateName);
        rateTemplate.setPortInLimitEnable(portInLimitEnable);
        rateTemplate.setPortInCIR(portInCIR);
        rateTemplate.setPortInCBS(portInCBS);
        rateTemplate.setPortInEBS(portInEBS);
        rateTemplate.setPortOutLimtEnable(portOutLimtEnable);
        rateTemplate.setPortOutCIR(portOutCIR);
        rateTemplate.setPortOutPIR(portOutPIR);
        rateTemplate.setUpdateTime(new Date());
        uniRateBatchConfigService.modifyTemplate(rateTemplate);
        return NONE;
    }

    public String deleteUniRateTemplate() {
        uniRateBatchConfigService.deleteTemplate(templateId);
        return NONE;
    }

    public String showUniRateApplyPage() {
        return SUCCESS;
    }

    public String batchConfigUniRateLimit() throws IOException {
        UniRateLimitTemplate profile = uniRateBatchConfigService.getTemplateById(entityId, templateId);
        List<String> needList = new ArrayList<String>();
        if (needBinds != null && !"".equals(needBinds)) {
            needList = Arrays.asList(needBinds.split("_"));
        }
        List<String> excludeList = new ArrayList<String>();
        if (excudeBinds != null && !"".equals(excudeBinds)) {
            excludeList = Arrays.asList(excudeBinds.split("_"));
        }
        // 调用接口提交绑定
        ResultBundle<Target> bindResult = eponBatchDeployService.batchDeploy(needList, excludeList, entityId, profile,
                "uniPortRateLimitExecutor", Type.UNI_PORT_RATELIMIT, getString("SERVICE.batchApply", "epon"));

        // 获得绑定结果
        List<UniRateLimitTemplate> applyList = new ArrayList<UniRateLimitTemplate>();
        UniRateLimitTemplate applyTable = null;
        JSONObject resultJson = new JSONObject();
        if (bindResult.getData().isEmpty()) {
            resultJson.put("successCount", 0);
            resultJson.put("failedCount", 0);
        } else {
            for (Result<Target> result : bindResult.getData()) {
                resultJson.put("successCount", result.getSuccessList().size());
                resultJson.put("failedCount", result.getFailureList().size());
                for (Target bindTarget : result.getSuccessList()) {
                    applyTable = new UniRateLimitTemplate();
                    applyTable.setEntityId(entityId);
                    applyTable.setUniIndex(bindTarget.getTargetIndex());
                    applyList.add(applyTable);
                }
            }
        }
        // 保存绑定结果到数据库
        if (!applyList.isEmpty()) {
            uniRateBatchConfigService.updateProtRateLimit(applyList, profile);
        }
        resultJson.write(response.getWriter());
        return NONE;
    }

    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getPortInLimitEnable() {
        return portInLimitEnable;
    }

    public void setPortInLimitEnable(Integer portInLimitEnable) {
        this.portInLimitEnable = portInLimitEnable;
    }

    public Integer getPortInCIR() {
        return portInCIR;
    }

    public void setPortInCIR(Integer portInCIR) {
        this.portInCIR = portInCIR;
    }

    public Integer getPortInCBS() {
        return portInCBS;
    }

    public void setPortInCBS(Integer portInCBS) {
        this.portInCBS = portInCBS;
    }

    public Integer getPortInEBS() {
        return portInEBS;
    }

    public void setPortInEBS(Integer portInEBS) {
        this.portInEBS = portInEBS;
    }

    public Integer getPortOutLimtEnable() {
        return portOutLimtEnable;
    }

    public void setPortOutLimtEnable(Integer portOutLimtEnable) {
        this.portOutLimtEnable = portOutLimtEnable;
    }

    public Integer getPortOutCIR() {
        return portOutCIR;
    }

    public void setPortOutCIR(Integer portOutCIR) {
        this.portOutCIR = portOutCIR;
    }

    public Integer getPortOutPIR() {
        return portOutPIR;
    }

    public void setPortOutPIR(Integer portOutPIR) {
        this.portOutPIR = portOutPIR;
    }

    public String getNeedBinds() {
        return needBinds;
    }

    public void setNeedBinds(String needBinds) {
        this.needBinds = needBinds;
    }

    public String getExcudeBinds() {
        return excudeBinds;
    }

    public void setExcudeBinds(String excudeBinds) {
        this.excudeBinds = excudeBinds;
    }

}
