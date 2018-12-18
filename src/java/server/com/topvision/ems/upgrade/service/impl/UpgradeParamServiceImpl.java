/***********************************************************************
 * $Id: UpgradeParamServiceImpl.java,v1.0 2014年9月23日 下午2:46:26 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.upgrade.domain.UpgradeGlobalParam;
import com.topvision.ems.upgrade.domain.UpgradeJobInfo;
import com.topvision.ems.upgrade.service.UpgradeJobService;
import com.topvision.ems.upgrade.service.UpgradeParamService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;

/**
 * @author loyal
 * @created @2014年9月23日-下午2:46:26
 * 
 */
@Service("upgradeParamService")
public class UpgradeParamServiceImpl extends BaseService implements UpgradeParamService {
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    @Autowired
    private UpgradeJobService upgradeJobService;

    @Override
    public UpgradeGlobalParam getUpgradeGlobalParam() {
        UpgradeGlobalParam upgradeGlobalParam = new UpgradeGlobalParam();
        upgradeGlobalParam.setRetryInterval(getParams("retryInterval"));
        upgradeGlobalParam.setRetryTimes(getParams("retryTimes"));
        upgradeGlobalParam.setWriteConfig(getParams("writeConfig"));
        return upgradeGlobalParam;
    }

    @Override
    public void modifyUpgradeGlobalParam(UpgradeGlobalParam param) {
        modifyRetryInterval(param.getRetryInterval());
        mofidyWriteConfig(param.getWriteConfig());
        modifyRetryTimes(param.getRetryTimes());
    }

    private Long getParams(String name) {
        SystemPreferences perferences = systemPreferencesDao.selectByModuleAndName("upgrade", name);
        return new Long(perferences.getValue());
    }

    private void modifyRetryInterval(Long retryInterval) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("upgrade");
        systemPreferences.setName("retryInterval");
        systemPreferences.setValue(retryInterval.toString());
        systemPreferencesDao.updateEntity(systemPreferences);
        List<UpgradeJobInfo> jobs = upgradeJobService.getAllUpgradeJob();
        for (int i = 0; i < jobs.size(); i++) {
            upgradeJobService.modifyJobInterval(jobs.get(i), retryInterval);
        }
    }

    private void mofidyWriteConfig(Long writeConfig) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("upgrade");
        systemPreferences.setName("writeConfig");
        systemPreferences.setValue(writeConfig.toString());
        systemPreferencesDao.updateEntity(systemPreferences);
    }

    public void modifyRetryTimes(Long retryTimes) {
        SystemPreferences systemPreferences = new SystemPreferences();
        systemPreferences.setModule("upgrade");
        systemPreferences.setName("retryTimes");
        systemPreferences.setValue(retryTimes.toString());
        systemPreferencesDao.updateEntity(systemPreferences);
    }

}
