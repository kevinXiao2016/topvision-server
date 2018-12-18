/***********************************************************************
 * $ ConfigWorkAction.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.action;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.template.domain.ApConfigParam;
import com.topvision.ems.template.domain.ApWorkTemplate;
import com.topvision.ems.template.service.ConfigWorkService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
@Controller("configWorkAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConfigWorkAction extends BaseAction {
    private static final long serialVersionUID = -8416717315575449782L;
    @Autowired
    private ConfigWorkService configWorkService;
    private List<Long> entitys = null;
    private String workName = "";
    private JSONArray apWorkTemplates = null;
    private JSONArray apConfigParam = null;
    // 新建模板
    private boolean newMode = false;
    private String modeName = "";

    /**
     * 开始批量配置过程
     * 
     * @return
     */
    public String executeWorks() {
        ApConfigParam apConfigParam = null;
        // 保存模板
        if (newMode) {
            configWorkService.saveConfigParamMode(workName, modeName, apConfigParam);
        }
        // 使用模板进行配置
        if (entitys != null) {
            configWorkService.executeWorks(entitys, workName, apConfigParam);
        }
        return NONE;
    }

    /**
     * 显示快速设置页面
     * 
     * @return
     */
    public String showQuickSet() {
        Map<String, ApWorkTemplate> apWorkTemplateList = configWorkService.getApWorkTemplates();
        apWorkTemplates = JSONArray.fromObject(apWorkTemplateList);
        return SUCCESS;
    }

    public ConfigWorkService getConfigWorkService() {
        return configWorkService;
    }

    public void setConfigWorkService(ConfigWorkService configWorkService) {
        this.configWorkService = configWorkService;
    }

    public JSONArray getApConfigParam() {
        return apConfigParam;
    }

    public void setApConfigParam(JSONArray apConfigParam) {
        this.apConfigParam = apConfigParam;
    }

    public JSONArray getApWorkTemplates() {
        return apWorkTemplates;
    }

    public void setApWorkTemplates(JSONArray apWorkTemplates) {
        this.apWorkTemplates = apWorkTemplates;
    }

    public List<Long> getEntitys() {
        return entitys;
    }

    public void setEntitys(List<Long> entitys) {
        this.entitys = entitys;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public boolean isNewMode() {
        return newMode;
    }

    public void setNewMode(boolean newMode) {
        this.newMode = newMode;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }
}