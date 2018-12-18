/***********************************************************************
 * $Id: SyslogListenportsAction.java,v1.0 2013-3-21 下午4:10:02 $
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
import com.topvision.platform.service.SyslogListenportsService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 系统日志监听端口配置
 * 
 * @author flack
 * @created @2013-3-21-下午4:10:02
 * 
 */
@Controller("syslogListenports")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SyslogListenportsAction extends BaseAction {
    private static final long serialVersionUID = 59491606187151200L;

    private String syslogListenports;
    @Autowired
    private SyslogListenportsService syslogListenportsService;

    /**
     * 显示系统日志监听端口配置页面
     * 
     * @return
     */
    public String showSyslogListenports() {
        return SUCCESS;
    }

    /**
     * 加载系统日志监听端口配置数据
     * 
     * @return
     * @throws Exception
     */
    public String loadSyslogListenports() throws Exception {
        JSONArray syslogArray = new JSONArray();
        // get syslogListenports data from database
        SystemPreferences syslogPreferences = syslogListenportsService.getSyslogPreferences("syslog.listenPorts");
        syslogListenports = syslogPreferences.getValue();
        JSONObject json = null;
        // 如果syslogListenports值不为空，则进行解析
        if (syslogListenports.length() > 0) {
            for (String syslogPort : syslogListenports.split(",")) {
                json = new JSONObject();
                json.put("port", syslogPort);
                syslogArray.add(json);
            }
        }
        writeDataToAjax(syslogArray);
        return NONE;
    }

    /**
     * 获取用户设置的系统日志监听端口并保存到数据库
     * 
     * @return
     * @throws IOException
     */
    public String saveSyslogListenports() {
        // save syslog.listenPorts
        syslogListenportsService.saveSyslogListenports(syslogListenports);
        return NONE;
    }

    public String getSyslogListenports() {
        return syslogListenports;
    }

    public void setSyslogListenports(String syslogListenports) {
        this.syslogListenports = syslogListenports;
    }

    public SyslogListenportsService getSyslogListenportsService() {
        return syslogListenportsService;
    }

    public void setSyslogListenportsService(SyslogListenportsService syslogListenportsService) {
        this.syslogListenportsService = syslogListenportsService;
    }
}
