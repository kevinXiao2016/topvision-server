/***********************************************************************
 * $ PnmpPollConfigServiceImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service.impl;

import com.topvision.ems.cm.pnmp.domain.PnmpPollCollectParam;
import com.topvision.ems.cm.pnmp.service.PnmpPollConfigService;
import com.topvision.ems.cm.pnmp.service.PnmpTaskBuildService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Service("pnmpPollConfigService")
public class PnmpPollConfigServiceImpl extends BaseService implements PnmpPollConfigService {
    private static String SWITCH_CLOSE_INT = "0";
    private static String SWITCH_OPEN_INT = "1";
    @Autowired
    private PnmpTaskBuildService pnmpPollTaskBuildService;
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean isPnmpPollClose() {
        String pnmpPollStatus = getParams("pnmpPoll", "pnmpPollStatus");
        return pnmpPollStatus != null && (pnmpPollStatus.equalsIgnoreCase(SWITCH_CLOSE_INT));
    }

    @Override
    public PnmpPollCollectParam getPnmpPollCollectParam() {
        PnmpPollCollectParam pnmpPollCollectParam = new PnmpPollCollectParam();
        pnmpPollCollectParam.setSendCmCount(getParamsIntegerValue("pnmpPoll", "cmCount", 10));
        pnmpPollCollectParam.setMaxPoolSize(getParamsIntegerValue("pnmpPoll", "maxCollectThread", 10));
        SnmpParam snmpParam = new SnmpParam();
        snmpParam.setCommunity(getParamsStringValue("cmcTerminal", "cmReadCommunity", "public"));
        snmpParam.setWriteCommunity(getParamsStringValue("cmcTerminal", "cmWriteCommunity", "private"));
        // TODO 从数据库获取
        snmpParam.setTimeout(5000L);
        snmpParam.setRetry((byte) 2);
        pnmpPollCollectParam.setSnmpParam(snmpParam);
        return pnmpPollCollectParam;
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

    private void startPnmpPoll(String switchNameInt) {
        if (isPnmpPollClose()) {
            pnmpPollTaskBuildService.startPnmp();
            modifySystemPreference("pnmpPoll", "pnmpPollStatus", SWITCH_OPEN_INT);
        }
    }

    private void stopPnmpPoll() {
        if (!isPnmpPollClose()) {
            pnmpPollTaskBuildService.stopPnmp();
            modifySystemPreference("pnmpPoll", "pnmpPollStatus", SWITCH_CLOSE_INT);
        }
    }
    
    private String getPnmpPollSwitchNameInt() {
        String pnmpPollStatus = getParams("pnmpPoll", "pnmpPollStatus");
        return pnmpPollStatus;
    }

}