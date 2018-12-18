/***********************************************************************
 * $Id: CmcAlertClearServiceImpl.java,v1.0 2015-11-20 上午10:44:16 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.callback.CmcAlertClearCallback;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.dao.AlertDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.parser.TrapConstants;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.service.CmcAlertClearService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ResourceManager;

/**
 * @author Administrator
 * @created @2015-11-20-上午10:44:16
 *
 */
@Service("cmcAlertClearService")
public class CmcAlertClearServiceImpl extends BaseService implements CmcAlertClearService, CmcAlertClearCallback {
    @Autowired
    private AlertDao alertDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private AlertService alertService;

    @Override
    public void clearCmcAlert(Long cmcId) {
        Entity cmc = entityService.getEntity(cmcId);
        if (cmc != null) {
            List<Alert> cmcAlertList = alertDao.getSpecialCmcAlertList(cmcId, TrapConstants.CCMTS_ALETR_LIST);
            for (Alert alert : cmcAlertList) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if (timestamp.getTime() > alert.getLastTime().getTime()) {
                    alert.setClearTime(timestamp);
                    alertService.txClearAlert(alert, getString("Perf.cmtsOnline", cmc.getMac()));
                }
            }
        }
    }

    private String getString(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.performance.resources")
                    .getString(key, strings);
        } catch (Exception e) {
            logger.debug("", e);
            return key;
        }
    }
}
