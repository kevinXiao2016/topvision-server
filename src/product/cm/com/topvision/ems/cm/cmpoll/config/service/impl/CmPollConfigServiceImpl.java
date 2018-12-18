/***********************************************************************
 * $ CmPollConfigServiceImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.config.service.impl;

import com.topvision.ems.cm.cmpoll.config.dao.CmPollConfigDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.cmpoll.config.domain.CmPollCollectParam;
import com.topvision.ems.cm.cmpoll.config.service.CmPollConfigService;
import com.topvision.ems.cm.cmpoll.taskbuild.service.CmPollTaskBuildService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;

import java.util.List;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Service("cmPollConfigService")
public class CmPollConfigServiceImpl extends BaseService implements CmPollConfigService {
    @Autowired
    private CmPollTaskBuildService cmPollTaskBuildService;
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    @Autowired
    private CmPollConfigDao cmPollConfigDao;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SWITCH_GLOBAL_CMLIST_INT = "1";
    private static final String SWITCH_SPECIFIED_CMLIST_INT = "2";
    private static final String SWITCH_CLOSE_CMPOLL_INT = "0";
    private static final String SWITCH_GLOBAL_CMLIST = "GlobalCmList";
    private static final String SWITCH_SPECIFIED_CMLIST = "SpecifiedCmList";
    private static final String SWITCH_CLOSE_CMPOLL = "CmPollClosed";


    @Override
    public void modifyCmPollInterval(Long interval) {
        modifySystemPreference("cmPoll", "cmPollInterval", interval.toString());
        if (isGlobalCmPoll() || isSpecifiedCmPoll()) {
        	//先将开关值保存下来
        	String switchNameInt = getCmPollSwitchNameInt();
        	//停止掉采集
            if (!isCmPollClose()) {
            	stopCmPoll();
            }
            //重新打开配置
        	startCmPoll(switchNameInt);
        }
    }

    @Override
    public boolean isCmPollClose() {
        String cmPollStatus = getParams("cmPoll", "cmPollStatus");
        return cmPollStatus != null && (cmPollStatus.equalsIgnoreCase(SWITCH_CLOSE_CMPOLL_INT));
    }

    @Override
    public boolean isGlobalCmPoll() {
        String cmPollStatus = getParams("cmPoll", "cmPollStatus");
        return cmPollStatus != null && (cmPollStatus.equalsIgnoreCase(SWITCH_GLOBAL_CMLIST_INT));
    }

    @Override
    public boolean isSpecifiedCmPoll() {
        String cmPollStatus = getParams("cmPoll", "cmPollStatus");
        return cmPollStatus != null && (cmPollStatus.equalsIgnoreCase(SWITCH_SPECIFIED_CMLIST_INT));
    }

    @Override
    public boolean isCmPollRemoteQuery() {
        return "2".equals(getParams("RemoteQuery", "RemoteQueryCmMode")) ? true : false;
    }

    @Override
    public Long getCmPollInterval() {
        return getParamsLongValue("cmPoll", "cmPollInterval", 4 * 60 * 60L);
    }

    @Override
    public CmPollCollectParam getCmPollCollectParam() {
        CmPollCollectParam cmPollCollectParam = new CmPollCollectParam();
        cmPollCollectParam.setSendCmCount(getParamsIntegerValue("cmPoll", "cmCount", 10));
        cmPollCollectParam.setMaxPoolSize(getParamsIntegerValue("cmPoll", "maxCollectThread", 10));
        SnmpParam snmpParam = new SnmpParam();
        snmpParam.setCommunity(getParamsStringValue("cmcTerminal", "cmReadCommunity", "public"));
        snmpParam.setWriteCommunity(getParamsStringValue("cmcTerminal", "cmWriteCommunity", "private"));
        // TODO 从数据库获取
        snmpParam.setTimeout(5000L);
        snmpParam.setRetry((byte) 2);
        cmPollCollectParam.setCmCollectSnmpParam(snmpParam);
        return cmPollCollectParam;
    }

    @Override
    public void changeSwitchForCmPoll(String switchName, Long cmPollInterval) {
        switch (switchName) {
            case SWITCH_CLOSE_CMPOLL :{
                stopCmPoll();
                break;
            }
            case SWITCH_GLOBAL_CMLIST :{
                modifyCmPollInterval(cmPollInterval);
                stopCmPoll();
                startCmPoll(SWITCH_GLOBAL_CMLIST_INT);
                break;
            }
            case SWITCH_SPECIFIED_CMLIST :{
                modifyCmPollInterval(cmPollInterval);
                stopCmPoll();
                startCmPoll(SWITCH_SPECIFIED_CMLIST_INT);
                break;
            }
        }
    }

    @Override
    public void importSpecifiedCmList(List<String> macList) {
        cmPollConfigDao.batchInsertSpecifiedCmList(macList);
    }

    @Override
    public List<String> getSpecifiedCmList() {
        return cmPollConfigDao.selectSpecifiedCmList();
    }

    @Override
    public String getCmPollSwitchName() {
        String cmPollStatus = getParams("cmPoll", "cmPollStatus");
        switch (cmPollStatus) {
            case SWITCH_CLOSE_CMPOLL_INT:
                return SWITCH_CLOSE_CMPOLL;
            case SWITCH_GLOBAL_CMLIST_INT:
                return SWITCH_GLOBAL_CMLIST;
            case SWITCH_SPECIFIED_CMLIST_INT:
                return SWITCH_SPECIFIED_CMLIST;
            default:
                return SWITCH_CLOSE_CMPOLL;
        }
    }

    private void modifySystemPreference(String module, String name, String value) {
        SystemPreferences perferences = new SystemPreferences();
        perferences.setModule(module);
        perferences.setName(name);
        perferences.setValue(value);
        systemPreferencesDao.updateEntity(perferences);
    }

    private String getParamsStringValue(String module, String name, String defaultString) {
        String str = getParams(module, name);
        if (str != null) {
            return str;
        } else {
            return defaultString;
        }
    }

    private Integer getParamsIntegerValue(String module, String name, Integer defaultInteger) {
        String str = getParams(module, name);
        if (str != null) {
            return Integer.parseInt(str);
        } else {
            return defaultInteger;
        }
    }

    private Long getParamsLongValue(String module, String name, Long defaultLong) {
        String str = getParams(module, name);
        if (str != null) {
            return Long.parseLong(str);
        } else {
            return defaultLong;
        }
    }

    private String getParams(String module, String name) {
        SystemPreferences perferences = systemPreferencesDao.selectByModuleAndName(module, name);
        if (perferences != null) {
            return perferences.getValue();
        } else {
            return null;
        }
    }

    private void startCmPoll(String switchNameInt) {
        if (isCmPollClose()) {
            cmPollTaskBuildService.startCmPollBuild();
            modifySystemPreference("cmPoll", "cmPollStatus", "" + switchNameInt);
        }
    }

    private void stopCmPoll() {
        if (!isCmPollClose()) {
            cmPollTaskBuildService.stopCmPollBuild();
            modifySystemPreference("cmPoll", "cmPollStatus", SWITCH_CLOSE_CMPOLL_INT);
        }
    }
    
    private String getCmPollSwitchNameInt() {
        String cmPollStatus = getParams("cmPoll", "cmPollStatus");
        return cmPollStatus;
    }

}