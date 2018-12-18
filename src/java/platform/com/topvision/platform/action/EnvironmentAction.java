/***********************************************************************
 * $Id: EnvironmentAction.java,v 1.1 Aug 15, 2009 7:34:18 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;

@Controller("environmentAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EnvironmentAction extends BaseAction {
    private static final long serialVersionUID = 7856303593419171577L;

    private Properties prop;
    private Map<String, String> env;

    /**
     * 获得系统环境
     * 
     * @return ACTION返回字符串
     */
    public String getenv() {
        env = System.getenv();
        return "environment";
    }

    /**
     * 获得系统参数
     * 
     * @return ACTION返回字符串
     */
    public String getProperties() {
        prop = System.getProperties();
        env = System.getenv();
        return "environment";
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public Properties getProp() {
        return prop;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }
}
