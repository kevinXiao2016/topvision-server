/***********************************************************************
 * $Id: CmcFlapClearAction.java,v1.0 2017年2月6日 下午1:40:33 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcFlapClearService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author lizongtian
 * @created @2017年2月6日-下午1:40:33
 *
 */
@Controller("cmcFlapClearAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcFlapClearAction extends BaseAction {

    private static final long serialVersionUID = 368473111636204358L;
    private String cmtsFlapClearPeriod;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private CmcFlapClearService cmcFlapClearService;

    public String showCmtsFlapClearConfig() {
        Properties properties = systemPreferencesService.getModulePreferences("autoClear");
        cmtsFlapClearPeriod = properties.getProperty("cmtsFlapClearPeriod");
        return SUCCESS;
    }

    public String saveCmtsFlapClearConfig() throws SchedulerException {
        Properties properties = systemPreferencesService.getModulePreferences("autoClear");
        String oldClearPeriod = properties.getProperty("cmtsFlapClearPeriod");
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences cmtsFlapClearPreference = new SystemPreferences();
        cmtsFlapClearPreference.setName("cmtsFlapClearPeriod");
        cmtsFlapClearPreference.setValue(cmtsFlapClearPeriod);
        cmtsFlapClearPreference.setModule("autoClear");
        preferences.add(cmtsFlapClearPreference);
        systemPreferencesService.savePreferences(preferences);
        if (!cmtsFlapClearPeriod.equalsIgnoreCase(oldClearPeriod)) {
            cmcFlapClearService.resetClearTrigger(cmtsFlapClearPeriod);
        }
        return NONE;
    }

    public String getCmtsFlapClearPeriod() {
        return cmtsFlapClearPeriod;
    }

    public void setCmtsFlapClearPeriod(String cmtsFlapClearPeriod) {
        this.cmtsFlapClearPeriod = cmtsFlapClearPeriod;
    }

}
