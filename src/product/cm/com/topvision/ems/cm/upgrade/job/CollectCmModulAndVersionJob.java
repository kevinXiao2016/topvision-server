/***********************************************************************
 * $Id: CollectCmModulAndVersionJob.java,v1.0 2016年12月7日 下午2:58:18 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.cm.upgrade.dao.CmUpgradeDao;
import com.topvision.ems.cm.upgrade.domain.CmcUpgradeInfo;
import com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Rod John
 * @created @2016年12月7日-下午2:58:18
 *
 */
public class CollectCmModulAndVersionJob extends AbstractJob {

    /* (non-Javadoc)
     * @see com.topvision.framework.scheduler.AbstractJob#doJob(org.quartz.JobExecutionContext)
     */
    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        EntityService entityService = getService(EntityService.class);
        CmUpgradeDao cmUpgradeDao = getService(CmUpgradeDao.class);
        List<CmcUpgradeInfo> entities = cmUpgradeDao.selectCmcUpgradeEntityInfo(new HashMap<String, Object>());
        CmUpgradeFacade cmUpgradeFacade = getService(FacadeFactory.class).getFacade(CmUpgradeFacade.class);
        Map<Long, List<TopCcmtsCmSwVersionTable>> result = new HashMap<>();
        for (CmcUpgradeInfo entity : entities) {
            try {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
                List<TopCcmtsCmSwVersionTable> versions = cmUpgradeFacade.getTopCcmtsCmSwVersionTable(snmpParam);
                result.put(entity.getEntityId(), versions);
            } catch (Exception e) {
                logger.info("CollectCmModulAndVersion error:" + entity.getIp());
            }
        }
        if (result != null && result.size() > 0) {
            cmUpgradeDao.syncCmModulSoftversion(result);
        }
    }

}
