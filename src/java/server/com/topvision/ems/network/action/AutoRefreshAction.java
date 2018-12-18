/***********************************************************************
 * $Id: AutoRefreshAction.java,v1.0 2014-10-17 下午2:11:51 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.AutoRefreshConfig;
import com.topvision.ems.network.service.AutoRefreshService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author Rod John
 * @created @2014-10-17-下午2:11:51
 * 
 */
@Controller("autoRefreshAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AutoRefreshAction extends BaseAction {
    private static final long serialVersionUID = -7338727528024489019L;
    private Boolean autoRefreshSwitch;
    private Integer autoRefreshInterval;
    @Autowired
    private AutoRefreshService autoRefreshService;

    /**
     * 
     * 
     * @return
     */
    public String showAutoRefreshConfig() {
        AutoRefreshConfig config = autoRefreshService.getAutoRefreshConfig();
        this.autoRefreshSwitch = config.getAutoRefreshSwitch();
        this.autoRefreshInterval = config.getAutoRefreshInterval();
        return SUCCESS;
    }

    /**
     * update Auto Refresh Config
     * 
     * @return
     * @throws IOException
     */
    public String saveAutoRefreshConfig() throws IOException {
        JSONObject resultMsg = new JSONObject();
        try {
            AutoRefreshConfig config = new AutoRefreshConfig(autoRefreshSwitch, autoRefreshInterval);
            autoRefreshService.updateAutoRefreshConfig(config);
        } catch (Exception e) {
            resultMsg.put("error", e.getMessage());
        }
        resultMsg.write(response.getWriter());
        return NONE;
    }

    /**
     * @return the autoRefreshSwitch
     */
    public Boolean getAutoRefreshSwitch() {
        return autoRefreshSwitch;
    }

    /**
     * @param autoRefreshSwitch
     *            the autoRefreshSwitch to set
     */
    public void setAutoRefreshSwitch(Boolean autoRefreshSwitch) {
        this.autoRefreshSwitch = autoRefreshSwitch;
    }

    /**
     * @return the autoRefreshInterval
     */
    public Integer getAutoRefreshInterval() {
        return autoRefreshInterval;
    }

    /**
     * @param autoRefreshInterval
     *            the autoRefreshInterval to set
     */
    public void setAutoRefreshInterval(Integer autoRefreshInterval) {
        this.autoRefreshInterval = autoRefreshInterval;
    }

}
