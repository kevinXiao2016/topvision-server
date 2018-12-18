/***********************************************************************
 * $Id: TrapListenportsServiceImpl.java,v1.0 2013-4-1 下午3:55:04 $
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

import com.topvision.ems.facade.fault.TrapFacade;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.TrapServerParam;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.service.TrapListenportsService;

/**
 * @author flack
 * @created @2013-4-1-下午3:55:04
 * 
 */
@Service("trapListenportsService")
public class TrapListenportsServiceImpl extends BaseService implements TrapListenportsService {
    private String listenAddress = "0.0.0.0";
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    @Override
    public SystemPreferences getTrapPreferences(String name) {
        return systemPreferencesService.getSystemPreference(name);
    }

    @Override
    public void saveTrapListenports(String trapListenports) {
        // save trap.listenPorts to database
        SystemPreferences trapPreference = new SystemPreferences();
        trapPreference.setName("trap.listenPorts");
        trapPreference.setValue(trapListenports);
        trapPreference.setModule("trap");
        systemPreferencesService.savePreferences(trapPreference);
        // config trapListenports to system
        this.configTrapListenports(trapListenports);
    }

    @Override
    public void configTrapListenports(String trapListenports) {
        List<Integer> listenPorts = new ArrayList<Integer>();
        if (trapListenports != null && trapListenports.length() > 0) {
            for (String port : trapListenports.split(",")) {
                listenPorts.add(Integer.parseInt(port));
            }
        }
        listenAddress = systemPreferencesService.getSystemPreference("trap.listenAddress").getValue();
        List<TrapFacade> facades = facadeFactory.getAllFacade(TrapFacade.class);
        for (TrapFacade facade : facades) {
            TrapServerParam param = new TrapServerParam();
            param.setListenPorts(listenPorts);
            param.setListenAddress(listenAddress);
            facade.setTrapServerParam(param);
        }
    }

    public String getListenAddress() {
        return listenAddress;
    }

    public void setListenAddress(String listenAddress) {
        this.listenAddress = listenAddress;
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
