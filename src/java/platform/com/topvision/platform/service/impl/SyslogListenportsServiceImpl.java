/***********************************************************************
 * $Id: SyslogListenportsServiceImpl.java,v1.0 2013-4-2 上午9:57:22 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.fault.SyslogFacade;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.syslog.SyslogServerParam;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SyslogListenportsService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author flack
 * @created @2013-4-2-上午9:57:22
 * 
 */
@Service("syslogListenportsService")
public class SyslogListenportsServiceImpl extends BaseService implements SyslogListenportsService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    @Override
    public SystemPreferences getSyslogPreferences(String name) {
        return systemPreferencesService.getSystemPreference(name);
    }

    @Override
    public void saveSyslogListenports(String syslogListenports) {
        // save syslog.listenPorts
        SystemPreferences syslogPreference = new SystemPreferences();
        syslogPreference.setName("syslog.listenPorts");
        syslogPreference.setValue(syslogListenports);
        syslogPreference.setModule("syslog");
        systemPreferencesService.savePreferences(syslogPreference);
        // config syslog.listenPorts to system
        this.configSyslogListenports(syslogListenports);

    }

    @Override
    public void configSyslogListenports(String syslogListenports) {
        List<Integer> syslogPorts = new ArrayList<Integer>();
        if (syslogListenports != null && syslogListenports.length() > 0) {
            for (String port : syslogListenports.split(",")) {
                syslogPorts.add(Integer.parseInt(port));
            }
        }
        List<SyslogFacade> facades = facadeFactory.getAllFacade(SyslogFacade.class);
        for (SyslogFacade facade : facades) {
            SyslogServerParam param = new SyslogServerParam();
            param.setListenPorts(syslogPorts);
            facade.setSyslogServerParam(param);
        }

    }

    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public SystemPreferencesService getSystemPreferencesService() {
        return systemPreferencesService;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

}
