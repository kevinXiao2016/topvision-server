/***********************************************************************
 * $Id: UserPreferenceAction.java,v1.0 2013年11月4日 下午3:18:22 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.user.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author Bravin
 * @created @2013年11月4日-下午3:18:22
 *
 */
@Controller("userPreferenceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserPreferenceAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    @Autowired
    private UserPreferencesService userPreferencesService;
    private String key;
    private Object value;
    private String module;

    /**
     * 通用的保存用户选项的action 存数据，更新UserContext
     * 
     * @return
     */
    public String saveUserPreference() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        String[] splits = new String[2];
        String $key, $module;
        if (key.contains(".")) {
            splits = key.split("\\.");
            $module = splits[0];
            $key = splits[1];
        } else {
            $module = module;
            $key = key;
        }
        userPreferencesService.saveModulePreferences($key, $module, value, uc);
        return NONE;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        String[] $value = (String[]) value;
        this.value = $value[0];
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
