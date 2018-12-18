/***********************************************************************
 * $Id: AutoClearConfigAction.java,v1.0 2016年11月12日 下午5:00:21 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.autoclear.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.autoclear.domain.AutoClearCmciRecord;
import com.topvision.ems.autoclear.domain.AutoClearRecord;
import com.topvision.ems.autoclear.service.AutoClearService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONObject;

/**
 * @author Rod John
 * @created @2016年11月12日-下午5:00:21
 *
 */
@Controller("autoClearAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AutoClearAction extends BaseAction {

    private static final long serialVersionUID = 3618911903198255824L;
    private String autoClearCmtsPeriod;
    private String autoClearOnuPeriod;
    private String autoClearCmciPeriod;
    @Autowired
    private AutoClearService autoClearService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    /**
     *  
     * 
     * @return
     */
    public String showAutoClearConfig() {
        // get data from database
        Properties properties = systemPreferencesService.getModulePreferences("autoClear");
        autoClearCmtsPeriod = properties.getProperty("autoClearCmtsPeriod");
        autoClearCmciPeriod = properties.getProperty("autoClearCmciPeriod");
        autoClearOnuPeriod = properties.getProperty("autoClearOnuPeriod");
        return SUCCESS;
    }

    /**
     * 查询自动清除CC历史记录，分页显示
     */
    public String loadAutoClearHistory() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<AutoClearRecord> records = autoClearService.loadAutoClearRecord(start, limit);
        Integer count = autoClearService.loadAutoClearRecordCount();
        json.put("data", records);
        json.put("rowCount", count);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }
    
    /**
     * 查询自动清除CMCI历史记录，分页显示
     */
    public String loadAutoClearCmciHistory() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<AutoClearCmciRecord> records = autoClearService.loadAutoClearCmciRecord(start, limit);
        Integer count = autoClearService.loadAutoClearCmciRecordCount();
        json.put("data", records);
        json.put("rowCount", count);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 
     * 
     * @return
     */
    public String showAutoClearHistory() {
        return SUCCESS;
    }

    /**
     * 存储自动清除离线CC配置
     * 
     * @return
     */
    public String saveAutoClearConfig() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences cmtsClearPreference = new SystemPreferences();
        // save autoClearPeriod - CMTS
        cmtsClearPreference.setName("autoClearCmtsPeriod");
        cmtsClearPreference.setValue(autoClearCmtsPeriod);
        cmtsClearPreference.setModule("autoClear");
        preferences.add(cmtsClearPreference);
        
        SystemPreferences cmciClearPreference = new SystemPreferences();
        // save autoClearPeriod - cmci
        cmciClearPreference.setName("autoClearCmciPeriod");
        cmciClearPreference.setValue(autoClearCmciPeriod);
        cmciClearPreference.setModule("autoClear");
        preferences.add(cmciClearPreference);

        SystemPreferences onuClearPreference = new SystemPreferences();
        // save autoClearPeriod - ONU
        onuClearPreference.setName("autoClearOnuPeriod");
        onuClearPreference.setValue(autoClearOnuPeriod);
        onuClearPreference.setModule("autoClear");
        preferences.add(onuClearPreference);
        // save to database
        systemPreferencesService.savePreferences(preferences);
        return NONE;
    }

    /**
     * @return the autoClearCmtsPeriod
     */
    public String getAutoClearCmtsPeriod() {
        return autoClearCmtsPeriod;
    }

    /**
     * @param autoClearCmtsPeriod the autoClearCmtsPeriod to set
     */
    public void setAutoClearCmtsPeriod(String autoClearCmtsPeriod) {
        this.autoClearCmtsPeriod = autoClearCmtsPeriod;
    }

    /**
     * @return the autoClearOnuPeriod
     */
    public String getAutoClearOnuPeriod() {
        return autoClearOnuPeriod;
    }

    /**
     * @param autoClearOnuPeriod the autoClearOnuPeriod to set
     */
    public void setAutoClearOnuPeriod(String autoClearOnuPeriod) {
        this.autoClearOnuPeriod = autoClearOnuPeriod;
    }

    public SystemPreferencesService getSystemPreferencesService() {
        return systemPreferencesService;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    /**
     * @return the autoClearService
     */
    public AutoClearService getAutoClearService() {
        return autoClearService;
    }

    /**
     * @param autoClearService the autoClearService to set
     */
    public void setAutoClearService(AutoClearService autoClearService) {
        this.autoClearService = autoClearService;
    }

    public String getAutoClearCmciPeriod() {
        return autoClearCmciPeriod;
    }

    public void setAutoClearCmciPeriod(String autoClearCmciPeriod) {
        this.autoClearCmciPeriod = autoClearCmciPeriod;
    }
}
