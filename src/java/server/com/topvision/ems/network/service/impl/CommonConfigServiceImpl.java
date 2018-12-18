/***********************************************************************
 * $Id: CommonConfigServiceImpl.java,v1.0 2014年7月16日 下午3:55:11 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.dao.CommonConfigDao;
import com.topvision.ems.network.service.CommandSendService;
import com.topvision.ems.network.service.CommonConfigService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;

/**
 * @author loyal
 * @created @2014年7月16日-下午3:55:11
 *
 */
@Service("commonConfigService")
public class CommonConfigServiceImpl extends BaseService implements CommonConfigService {
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    @Autowired
    private CommonConfigDao commonConfigDao;
    @Autowired
    private CommandSendService commandSendService;

    @Override
    public Long getSendCommandInterval() {
        Long sendCommandInterval = 500L;
        List<SystemPreferences> perferences = systemPreferencesDao.selectByModule("telnet");
        if (perferences != null && perferences.size() > 0) {
            for (SystemPreferences systemPreferences : perferences) {
                if ("sendCommandInterval".equals(systemPreferences.getName())) {
                    sendCommandInterval = new Long(systemPreferences.getValue());
                }
            }
        }
        return sendCommandInterval;
    }

    @Override
    public Long getPollInterval() {
        Long pollInterval = 1800000L;
        List<SystemPreferences> perferences = systemPreferencesDao.selectByModule("telnet");
        if (perferences != null && perferences.size() > 0) {
            for (SystemPreferences systemPreferences : perferences) {
                if ("pollInterval".equals(systemPreferences.getName())) {
                    pollInterval = new Long(systemPreferences.getValue());
                }
            }
        }
        return pollInterval;
    }

    @Override
    public void modifySendCommandInterval(Long sendCommandInterval) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("telnet");
        systemPreferences.setName("sendCommandInterval");
        systemPreferences.setValue(sendCommandInterval.toString());
        commandSendService.modifySendCommandInterval(sendCommandInterval);
        systemPreferencesDao.updateEntity(systemPreferences);
    }

    @Override
    public void modifyPollInterval(Long pollInterval) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("telnet");
        systemPreferences.setName("pollInterval");
        systemPreferences.setValue(pollInterval.toString());
        commandSendService.restartMakeSendConfigArrayJob(pollInterval);
        systemPreferencesDao.updateEntity(systemPreferences);
    }

    @Override
    public List<String> getConfigs(Long type) {
        return commonConfigDao.getCommonConfigs(type);
    }

    @Override
    public List<String> txGetConfigs(Long typeId) {
        return commonConfigDao.getCommonConfigs(typeId);
    }

    @Override
    public List<String> txGetConfigs(Long typeId, Long folderId) {
        return commonConfigDao.getCommonConfigs(typeId, folderId);
    }

    @Override
    public String readCommonConfig(Long type) {
        List<String> configs = commonConfigDao.getCommonConfigs(type);
        StringBuilder sb = new StringBuilder();
        for (String config : configs) {
            sb.append(config);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String readCommonConfig(Long type, Long folderId) {
        List<String> configs = commonConfigDao.getCommonConfigs(type, folderId);
        StringBuilder sb = new StringBuilder();
        for (String config : configs) {
            sb.append(config);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void saveCommonConfig(String textArea, Long type) {
        String[] configs = textArea.split("<br/>");
        commonConfigDao.clearCommonConfig(type);
        for (String config : configs) {
            if (!"".equals(config.trim())) {
                commonConfigDao.addCommonConfig(config, type);
            }
        }
    }

    @Override
    public void saveCommonConfig(String textArea, Long type, Long folderId) {
        String[] configs = textArea.split("<br/>");
        commonConfigDao.clearCommonConfig(type, folderId);
        for (String config : configs) {
            if (!"".equals(config.trim())) {
                commonConfigDao.addCommonConfig(config, type, folderId);
            }
        }
    }

    @Override
    public void saveCommonConfig(List<String> configs, Long type) {
        commonConfigDao.clearCommonConfig(type);
        commonConfigDao.addCommonConfig(configs, type);
    }

    @Override
    public void saveCommonConfig(List<String> configs, Long type, Long folderId) {
        commonConfigDao.clearCommonConfig(type, folderId);
        commonConfigDao.addCommonConfig(configs, type, folderId);
    }

    @Override
    public Boolean loadAutoSendConfigSwitch() {
        SystemPreferences systemPreferences = systemPreferencesDao.selectByModuleAndName("sendConfig",
                "autoSendConfigSwitch");
        if (systemPreferences == null) {
            return false;
        } else {
            return Boolean.parseBoolean(systemPreferences.getValue());
        }
    }

    @Override
    public Boolean loadFailAutoSendConfigSwitch() {
        SystemPreferences systemPreferences = systemPreferencesDao.selectByModuleAndName("sendConfig",
                "failAutoSendConfigSwitch");
        if (systemPreferences == null) {
            return false;
        } else {
            return Boolean.parseBoolean(systemPreferences.getValue());
        }
    }

    @Override
    public void configAutoSendConfigSwitch(Boolean switchStatus) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("sendConfig");
        systemPreferences.setName("autoSendConfigSwitch");
        systemPreferences.setValue(switchStatus.toString());
        SystemPreferences old = systemPreferencesDao.selectByModuleAndName("sendConfig", "autoSendConfigSwitch");
        if (old == null) {
            systemPreferencesDao.insertEntity(systemPreferences);
        } else {
            systemPreferencesDao.updateEntity(systemPreferences);
        }
    }

    @Override
    public void configFailAutoSendConfigSwitch(Boolean switchStatus) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("sendConfig");
        systemPreferences.setName("failAutoSendConfigSwitch");
        systemPreferences.setValue(switchStatus.toString());
        SystemPreferences old = systemPreferencesDao.selectByModuleAndName("sendConfig", "failAutoSendConfigSwitch");
        if (old == null) {
            systemPreferencesDao.insertEntity(systemPreferences);
        } else {
            systemPreferencesDao.updateEntity(systemPreferences);
        }
    }
}
