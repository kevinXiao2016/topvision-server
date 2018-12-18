/***********************************************************************
 * $Id: CmcAutoClearJob.java,v1.0 2017年1月9日 上午10:11:04 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.job;

import java.sql.Timestamp;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.autoclear.dao.AutoClearDao;
import com.topvision.ems.autoclear.domain.AutoClearRecord;
import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author Rod John
 * @created @2017年1月9日-上午10:11:04
 *
 */
public class CmcAutoClearJob extends AbstractJob {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.scheduler.AbstractJob#doJob(org.quartz.JobExecutionContext)
     */
    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        EntityService entityService = getService(EntityService.class);
        EntityTypeService entityTypeService = getService(EntityTypeService.class);
        SystemPreferencesService systemPreferencesService = getService(SystemPreferencesService.class);
        CmcDao cmcDao = getService(CmcDao.class);
        CmcService cmcService = getService(CmcService.class);
        AutoClearDao autoClearDao = getService(AutoClearDao.class);

        Long threshold = Long.parseLong(systemPreferencesService.getSystemPreference("autoClearCmtsPeriod").getValue()) * 24 * 3600 * 1000;
        if (threshold > 0) {
            List<CmcAttribute> offlineCmc = cmcDao.getAllCmcForAutoClean();
            for (CmcAttribute offline : offlineCmc) {
                try {
                    if (offline.getLastDeregisterTime() != null) {
                        Long time = System.currentTimeMillis() - offline.getLastDeregisterTime().getTime();
                        if (time >= threshold) {
                            cmcService.cmcNoMacBind(offline.getCmcId());
                            // Record
                            Entity entity = entityService.getEntity(offline.getCmcId());
                            Entity oltEntity = entityService.getEntity(offline.getEntityId());
                            EntityType entityType = entityTypeService.getEntityType(entity.getTypeId());
                            AutoClearRecord record = new AutoClearRecord.Builder(oltEntity.getName(),
                                    oltEntity.getIp(), entity.getName(), offline.getInterfaceInfo())
                                    .onuMac(entity.getMac()).onuType(entityType.getDisplayName())
                                    .offlineTime(new Timestamp(offline.getLastDeregisterTime().getTime()))
                                    .clearTime(new Timestamp(System.currentTimeMillis())).build();

                            autoClearDao.insertAutoClearRecord(record);
                        }
                    }
                } catch (Exception e) {
                    logger.info("Auto Clean Cmc:" + offline.getOnuId() + " error:", e);
                }
            }

        }
    }
}