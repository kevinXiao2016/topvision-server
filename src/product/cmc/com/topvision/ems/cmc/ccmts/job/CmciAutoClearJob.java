package com.topvision.ems.cmc.ccmts.job;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.autoclear.dao.AutoClearDao;
import com.topvision.ems.autoclear.domain.AutoClearCmciRecord;
import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmciAttribute;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.platform.service.SystemPreferencesService;

public class CmciAutoClearJob extends AbstractJob{

    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        EntityTypeService entityTypeService = getService(EntityTypeService.class);
        SystemPreferencesService systemPreferencesService = getService(SystemPreferencesService.class);
        CmcDao cmcDao = getService(CmcDao.class);
        AutoClearDao autoClearDao = getService(AutoClearDao.class);
        EntityService entityService = getService(EntityService.class);
        
        Long threshold = Long.parseLong(systemPreferencesService.getSystemPreference("autoClearCmciPeriod").getValue()) * 24 * 3600 * 1000;
        if (threshold > 0) {
            List<CmciAttribute> offlineCmci = cmcDao.getCmciForAutoClean();
            for (CmciAttribute offline : offlineCmci) {
                // 判断是否为cmci型设备
                if (entityTypeService.isEntityWithIp(offline.getCmcType())) {
                    try {
                        if (offline.getOfflinetime() != null) {
                            Long time = System.currentTimeMillis() - offline.getOfflinetime().getTime();
                            if (time >= threshold) {
                                // 删除离线cmci型设备
                                List<Long> entityIds = new ArrayList<>();
                                entityIds.add(offline.getCmcId());
                                entityService.removeEntity(entityIds);
                                // Record
                                AutoClearCmciRecord record = new AutoClearCmciRecord.Builder(offline.getCmcId(), offline.getCmcIndex(), offline.getCmcType(), offline.getCmcEntityId())
                                .alias(offline.getAlias())
                                .offlineTime(offline.getOfflinetime())
                                .clearTime(new Timestamp(new Date().getTime()))
                                .ipAddress(offline.getIpAddress())
                                .macAddr(offline.getMacAddr())
                                .typeName(offline.getTypeName())
                                .build();
                                autoClearDao.insertAutoClearCmciRecord(record);
                            }
                        }
                    } catch (Exception e) {
                        logger.info("Auto Clean Cmci:" + offline.getCmcId() + " error:", e);
                    }
                }
            }
        }
    }

}
