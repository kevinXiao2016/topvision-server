/***********************************************************************
 * $Id: SecurityCenterAction.java,v 1.1 Oct 28, 2009 10:46:03 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.user.context.UserContextManager;

@Controller("securityCenterAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SecurityCenterAction extends BaseAction {
    private static final long serialVersionUID = -598905277160411193L;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private boolean allowIpBindLogon = false;
    private boolean allowRepeatedlyLogon = false;
    private boolean stopUserWhenErrors = false;
    private int stopUserWhenErrorNumber = 3;
    private boolean checkPasswdComplex = false;
    // lock time default 30 minutes
    private Integer lockTime = 30;
    @Autowired
    private UserContextManager ucm;

    /**
     * 显示安全中心
     * 
     * @return
     */
    public String showSecurityCenter() {
        //allowIpBindLogon = systemPreferencesService.isAllowIpBindLogon();
        allowRepeatedlyLogon = systemPreferencesService.isAllowRepeatedlyLogon();
        stopUserWhenErrors = systemPreferencesService.isStopUserWhenErrors();
        stopUserWhenErrorNumber = systemPreferencesService.getStopUserWhenErrorNumber();
        checkPasswdComplex = systemPreferencesService.isCheckPasswdComplex();
        lockTime = systemPreferencesService.getLockTime();
        return SUCCESS;
    }

    /**
     * 更新安全中心
     * 
     * @return
     */
    public String updateSecurityCenter() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences preference = new SystemPreferences();
        /*
        preference.setModule("logon");
        preference.setName("allowIpBindLogon");
        preference.setValue(String.valueOf(allowIpBindLogon));
        preferences.add(preference);
        preference = new SystemPreferences();
        preference.setModule("logon");
        preference.setName("allowRepeatedlyLogon");
        preference.setValue(String.valueOf(allowRepeatedlyLogon));
        preferences.add(preference);
        */
        preference = new SystemPreferences();
        preference.setModule("logon");
        preference.setName("stopUserWhenErrors");
        preference.setValue(String.valueOf(stopUserWhenErrors));
        preferences.add(preference);
        preference = new SystemPreferences();
        preference.setModule("logon");
        preference.setName("stopUserWhenErrorNumber");
        preference.setValue(String.valueOf(stopUserWhenErrorNumber));
        preferences.add(preference);
        preference = new SystemPreferences();
        preference.setModule("logon");
        preference.setName("checkPasswdComplex");
        preference.setValue(String.valueOf(checkPasswdComplex));
        preferences.add(preference);
        // save lockTime
        /*preference = new SystemPreferences();
        preference.setModule("logon");
        preference.setName("lockTime");
        preference.setValue(String.valueOf(lockTime));
        preferences.add(preference);*/
        // save to database
        systemPreferencesService.savePreferences(preferences);
        // update all the value in Service
        // systemPreferencesService.setAllowIpBindLogon(allowIpBindLogon);
        //systemPreferencesService.setAllowRepeatedlyLogon(allowRepeatedlyLogon);
        systemPreferencesService.setCheckPasswdComplex(checkPasswdComplex);
        systemPreferencesService.setStopUserWhenErrorNumber(stopUserWhenErrorNumber);
        systemPreferencesService.setStopUserWhenErrors(stopUserWhenErrors);
        systemPreferencesService.setLockTime(lockTime);
        ucm.setStopNumber(stopUserWhenErrorNumber);
        return NONE;
    }

    public int getStopUserWhenErrorNumber() {
        return stopUserWhenErrorNumber;
    }

    public boolean isAllowIpBindLogon() {
        return allowIpBindLogon;
    }

    public boolean isAllowRepeatedlyLogon() {
        return allowRepeatedlyLogon;
    }

    public boolean isCheckPasswdComplex() {
        return checkPasswdComplex;
    }

    public boolean isStopUserWhenErrors() {
        return stopUserWhenErrors;
    }

    public void setAllowIpBindLogon(boolean allowIpBindLogon) {
        this.allowIpBindLogon = allowIpBindLogon;
    }

    public void setAllowRepeatedlyLogon(boolean allowRepeatedlyLogon) {
        this.allowRepeatedlyLogon = allowRepeatedlyLogon;
    }

    public void setCheckPasswdComplex(boolean checkPasswdComplex) {
        this.checkPasswdComplex = checkPasswdComplex;
    }

    public void setStopUserWhenErrorNumber(int stopUserWhenErrorNumber) {
        this.stopUserWhenErrorNumber = stopUserWhenErrorNumber;
    }

    public void setStopUserWhenErrors(boolean stopUserWhenErrors) {
        this.stopUserWhenErrors = stopUserWhenErrors;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    public Integer getLockTime() {
        return lockTime;
    }

    public void setLockTime(Integer lockTime) {
        this.lockTime = lockTime;
    }
}
