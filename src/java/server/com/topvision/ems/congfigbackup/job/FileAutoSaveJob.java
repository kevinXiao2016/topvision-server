/***********************************************************************
 * $Id: FileAutoSaveJob.java,v1.0 2013-10-26 上午10:12:43 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.congfigbackup.job;

import java.util.List;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.congfigbackup.service.ConfigBackupService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author flack
 * @created @2013-10-26-上午10:12:43
 *
 */
public class FileAutoSaveJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(FileAutoSaveJob.class);

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        JobDataMap $jobDataMap = ctx.getJobDetail().getJobDataMap();
        ConfigBackupService configBackupService = (ConfigBackupService) $jobDataMap.get("configBackupService");
        SystemPreferencesService systemPreferencesService = (SystemPreferencesService) $jobDataMap
                .get("systemPreferencesService");
        List<Entity> entityList = configBackupService.getEntitList();
        Properties properties = systemPreferencesService.getModulePreferences("platform");
        String $autoWrite = properties.getProperty("file.autoWrite");
        String $saveBeforeWrite = properties.getProperty("file.saveBeforeWrite");
        boolean saveBeforeWrite = EponConstants.FILE_AUTOBACK.equals($saveBeforeWrite);
        boolean autoWrite = EponConstants.FILE_AUTOWRITE.equals($autoWrite);
        /* 遍历设备列表,依次进行保存 */
        logger.debug("begin to save and download startConfig");
        for (Entity entity : entityList) {
            long entityId = entity.getEntityId();
            try {
                if (saveBeforeWrite) {
                    configBackupService.saveConfigFile(entityId, entity.getTypeId(), entity.getIp());
                }
                if (autoWrite) {
                    configBackupService.downloadConfigFile(entityId, entity.getTypeId(), entity.getIp());
                }
            } catch (Exception e) {
                logger.error("failed to save entity [{}] 's config: {}", entity.getName(), e);
            }
        }
        logger.debug("finished to save entity's config");
    }

}
