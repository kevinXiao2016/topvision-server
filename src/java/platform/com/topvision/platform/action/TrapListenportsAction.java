/***********************************************************************
 * $Id: TrapListenportsAction.java,v1.0 2013-3-21 下午3:32:43 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.TrapListenportsService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Trap监听端口设置
 * 
 * @author flack
 * @created @2013-3-21-下午3:32:43
 * 
 */
@Controller("trapListenports")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrapListenportsAction extends BaseAction {
    private static final long serialVersionUID = -5066097950137583468L;

    private String trapListenports;
    @Autowired
    private TrapListenportsService trapListenportsService;

    /**
     * 显示trap监听端口配置页面
     * 
     * @return
     */
    public String showTrapListenports() {
        return SUCCESS;
    }

    /**
     * 加载Trap监听端口数据
     * 
     * @return
     * @throws Exception
     */
    public String loadTrapListenports() throws Exception {
        JSONArray jArray = new JSONArray();
        // get TrapListenports data from database
        SystemPreferences trapPreferences = trapListenportsService.getTrapPreferences("trap.listenPorts");
        trapListenports = trapPreferences.getValue();
        JSONObject json = null;
        // 如果trapListenports不为空，则进行解析
        if (trapListenports.length() > 0) {
            for (String portValue : trapListenports.split(",")) {
                json = new JSONObject();
                json.put("port", portValue);
                jArray.add(json);
            }
        }
        writeDataToAjax(jArray);
        return NONE;
    }

    /**
     * 获取用户设置的Trap监听端口值并保存到数据库
     * 
     * @return
     * @throws IOException
     */
    public String saveTrapListenports() {
        trapListenportsService.saveTrapListenports(trapListenports);
        return NONE;
    }

    public String getTrapListenports() {
        return trapListenports;
    }

    public void setTrapListenports(String trapListenports) {
        this.trapListenports = trapListenports;
    }

    public TrapListenportsService getTrapListenportsService() {
        return trapListenportsService;
    }

    public void setTrapListenportsService(TrapListenportsService trapListenportsService) {
        this.trapListenportsService = trapListenportsService;
    }

}
