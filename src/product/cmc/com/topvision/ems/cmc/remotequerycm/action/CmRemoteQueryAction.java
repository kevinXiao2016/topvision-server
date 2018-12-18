/***********************************************************************
 * $Id: CmRemoteQueryAction.java,v1.0 2014-1-28 下午2:34:30 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.remotequerycm.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author YangYi
 * @created @2014-1-28-下午2:34:30
 *
 */
@Controller("cmRemoteQueryAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmRemoteQueryAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    private Integer cmCollectMode; // CM 采集方式，1为直接从CM采集，2为RemoteQuery方式
    private Integer cmPingMode; //CM PING方式， 1为从网管服务器PING， 2为从上联设备PING
    @Resource(name = "systemPreferencesService")
    private SystemPreferencesService systemPreferencesService;

    /**
     * 显示CM采集方式配置页面
     * 
     * @return
     */
    public String showCmRemoteQueryConfig() {
        Properties properties = systemPreferencesService.getModulePreferences("RemoteQuery");
        String mode = properties.getProperty("RemoteQueryCmMode");
        if (mode != null) {
            cmCollectMode = Integer.valueOf(mode);
        }
        properties = systemPreferencesService.getModulePreferences("toolPing");
        mode = properties.getProperty("Ping.cmping");
        if (mode != null) {
            cmPingMode = Integer.valueOf(mode);
        }
        return SUCCESS;
    }

    /**
     * 保存CM采集方式
     * 
     * @return
     */
    public String saveCmRemoteQueryConfig() {
        List<SystemPreferences> preferences = new ArrayList<SystemPreferences>();
        SystemPreferences pingPreference = new SystemPreferences();
        pingPreference.setName("RemoteQueryCmMode");
        pingPreference.setValue(String.valueOf(this.cmCollectMode));
        pingPreference.setModule("RemoteQuery");
        preferences.add(pingPreference);
        // CM PING
        SystemPreferences cmPingPreference = new SystemPreferences();
        cmPingPreference.setName("Ping.cmping");
        cmPingPreference.setValue(String.valueOf(this.cmPingMode));
        cmPingPreference.setModule("toolPing");
        preferences.add(cmPingPreference);
        // save to database
        systemPreferencesService.savePreferences(preferences);
        return NONE;
    }

    public Integer getCmCollectMode() {
        return cmCollectMode;
    }

    public void setCmCollectMode(Integer cmCollectMode) {
        this.cmCollectMode = cmCollectMode;
    }

    /**
     * @return the cmPingMode
     */
    public Integer getCmPingMode() {
        return cmPingMode;
    }

    /**
     * @param cmPingMode the cmPingMode to set
     */
    public void setCmPingMode(Integer cmPingMode) {
        this.cmPingMode = cmPingMode;
    }

}
