/***********************************************************************
 * CmWebProxyConfigServiceImpl.java,v1.0 17-4-24 下午3:03 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.webproxy.service.impl;


import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.webproxy.domain.CmWebProxyModule;
import com.topvision.ems.cmc.webproxy.service.CmWebProxyConfigService;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jay
 * @created 17-4-24 下午3:03
 */
@Service("cmWebProxyConfigService")
public class CmWebProxyConfigServiceImpl extends CmcBaseCommonService implements CmWebProxyConfigService {
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;

    @Override
    public Integer loadCmWebJumpModule() {
        SystemPreferences systemPreferences = systemPreferencesDao.selectByModuleAndName("WebProxy", "webproxy.jumpModule");
        if (systemPreferences == null) {
            return CmWebProxyModule.DIRECTJUMP;
        } else {
            return Integer.parseInt(systemPreferences.getValue());
        }
    }

    @Override
    public String loadNatServerIp() {
        SystemPreferences systemPreferences = systemPreferencesDao.selectByModuleAndName("WebProxy", "webproxy.natIp");
        if (systemPreferences == null) {
            return "";
        } else {
            return systemPreferences.getValue();
        }
    }

    @Override
    public void configCmWebProxy(Integer cmWebJumpModule, String natIp) {
        configSystemPreferences("webproxy.jumpModule","" + cmWebJumpModule);
        configSystemPreferences("webproxy.natIp", natIp);
    }

    private void configSystemPreferences(String key,String value) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("WebProxy");
        systemPreferences.setName(key);
        systemPreferences.setValue(value);
        SystemPreferences old = systemPreferencesDao.selectByModuleAndName("WebProxy", key);
        if (old == null) {
            systemPreferencesDao.insertEntity(systemPreferences);
        } else {
            systemPreferencesDao.updateEntity(systemPreferences);
        }
    }
}
