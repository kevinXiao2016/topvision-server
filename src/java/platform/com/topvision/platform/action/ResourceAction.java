package com.topvision.platform.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.UserService;

import net.sf.json.JSONObject;

@Controller("resourceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResourceAction extends BaseAction {
    private Logger logger = LoggerFactory.getLogger(ResourceAction.class);
    private static final long serialVersionUID = 7675538787028585221L;
    private String modulePath;
    private long userId;
    private String prefix;
    private String lang;
    private JSONObject i18n;
    @Autowired
    private UserService userService;
    private String defaultLang;

    /**
     * 载入所有部门列表
     * 
     * @return Action指定字符串
     * @throws Exception
     */
    public String i18n() throws Exception {
        String[] modules = modulePath.split(",");
        Map<String, Object> re = new HashMap<String, Object>();
        for (String module : modules) {
            try {
                ResourceManager resourceManager = ResourceManager.getResourceManager(module);
                Properties properties = resourceManager.getProperties();
                for (String key : properties.stringPropertyNames()) {
                    String value = properties.getProperty(key, key);
                    makeReMap(key, value, re);
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        i18n = JSONObject.fromObject(re);
        /*
         * if (prefix != null && !prefix.equalsIgnoreCase("")) { return prefix; } else { return
         * SUCCESS; }
         */
        return SUCCESS;
    }

    /**
     * 显示语言设置页面
     * 
     * @return
     */
    public String showLanguageConfig() {
        UserContext uc;
        uc = (UserContext) getSession().get(UserContext.KEY);
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
        SystemConstants sc = SystemConstants.getInstance();
        defaultLang = sc.getStringParam("language", "en_US");
        if ("en_US".equals(defaultLang)) {
            defaultLang = resourceManager.getString("SYSTEM.USenglish");
        } else {
            defaultLang = resourceManager.getString("SYSTEM.simpleChinese");
        }
        if (uc != null && uc.getUser() != null) {
            lang = uc.getUser().getLanguage();
        }
        if (lang == null || "".equals(lang)) {
            lang = "zh_CN";
        }
        return SUCCESS;
    }

    /**
     * 修改语言
     * 
     * @return Action指定字符串
     * @throws Exception
     */
    public String changeLanguage() throws Exception {
        UserContext uc;
        uc = (UserContext) getSession().get(UserContext.KEY);
        if (uc != null && uc.getUser() != null) {
            uc.getUser().setLanguage(lang);
        }
        getSession().put(UserContext.KEY, uc);
        userService.updateLanguage(userId, lang);
        return NONE;
    }

    @SuppressWarnings("unchecked")
    private void makeReMap(String key, String value, Map<String, Object> p) {
        int i = 0;
        String[] keys = key.split("\\.");
        for (; i < keys.length - 1; i++) {
            Map<String, Object> tmp;
            if (p.containsKey(keys[i]) && p.get(keys[i]) instanceof Map) {
                tmp = (Map<String, Object>) p.get(keys[i]);
            } else {
                tmp = new HashMap<String, Object>();
                p.put(keys[i], tmp);
            }
            p = tmp;
        }
        p.put(keys[i], value);
    }

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public JSONObject getI18n() {
        return i18n;
    }

    public void setI18n(JSONObject i18n) {
        this.i18n = i18n;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
    }
}