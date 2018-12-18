/***********************************************************************
 * $Id: OnuAutoCleanJob.java,v1.0 2017年1月9日 上午9:28:34 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.job;

import java.sql.Timestamp;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.autoclear.dao.AutoClearDao;
import com.topvision.ems.autoclear.domain.AutoClearRecord;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.service.GponOnuAuthService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author Rod John
 * @created @2017年1月9日-上午9:28:34
 *
 */
public class OnuAutoClearJob extends AbstractJob {

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
        OnuDao onuDao = getService(OnuDao.class);
        OnuAuthService onuAuthService = getService(OnuAuthService.class);
        AutoClearDao autoClearDao = getService(AutoClearDao.class);
        GponOnuAuthService gponOnuAuthService = getService(GponOnuAuthService.class);

        Long threshold = Long.parseLong(systemPreferencesService.getSystemPreference("autoClearOnuPeriod").getValue()) * 24 * 3600 * 1000;
        if (threshold > 0) {
            List<OltOnuAttribute> offlineOnus = onuDao.getAllOnuForAutoClean();
            for (OltOnuAttribute offline : offlineOnus) {
                try {
                    if (offline.getLastDeregisterTime() != null) {
                        Long time = System.currentTimeMillis() - offline.getLastDeregisterTime().getTime();
                        if (time >= threshold) {
                            if (EponConstants.EPON_ONU.equals(offline.getOnuEorG())) {
                                // GET ONU AUTH TYPE
                                OltAuthentication auth = onuAuthService.getOltAuthenticationByIndex(offline.getEntityId(), offline.getOnuIndex());
                                // Clean Onu
                                onuAuthService.deleteAuthenPreConfig(offline.getEntityId(), auth.getPonId(),offline.getOnuIndex(), auth.getAuthType());
                            } else {
                                GponOnuAuthConfig gponOnuAuthConfig = new GponOnuAuthConfig();
                                gponOnuAuthConfig.setOnuId(offline.getOnuId());
                                gponOnuAuthConfig.setEntityId(offline.getEntityId());
                                gponOnuAuthService.deleteGponOnuAuth(gponOnuAuthConfig);
                            }
                            // Record
                            //OltAttribute oltAttribute = oltDao.getOltAttribute(offline.getEntityId());
                            Entity entity = entityService.getEntity(offline.getEntityId());
                            EntityType entityType = entityTypeService.getEntityType(offline.getTypeId());
                            AutoClearRecord record = new AutoClearRecord.Builder(entity.getName(), entity.getIp(),offline.getName(), EponIndex.getOnuStringByIndex(offline.getOnuIndex()).toString())
                                                    .onuMac(offline.getOnuUniqueIdentification())
                                                    .onuType(entityType.getDisplayName())
                                                    .offlineTime(new Timestamp(offline.getLastDeregisterTime().getTime()))
                                                    .clearTime(new Timestamp(System.currentTimeMillis()))
                                                    .build();

                            autoClearDao.insertAutoClearRecord(record);
                        }
                    }
                } catch (Exception e) {
                    logger.info("Auto Clean Onu:" + offline.getOnuId() + " error", e);
                }
            }

        }
    }
}