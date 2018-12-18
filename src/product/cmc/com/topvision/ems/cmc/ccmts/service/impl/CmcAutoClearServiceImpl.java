/***********************************************************************
 * $Id: CmcAutoClearServiceImpl.java,v1.0 2016年11月14日 下午2:06:18 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.autoclear.dao.AutoClearDao;
import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.job.CmcAutoClearJob;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author Rod John
 * @created @2016年11月14日-下午2:06:18
 *
 */
@Service
public class CmcAutoClearServiceImpl extends BaseService {
    @Autowired
    private EntityService entityService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private CmcDao cmcDao;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private AutoClearDao autoClearDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private SchedulerService schedulerService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    public void initialize() {
        super.initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#start()
     */
    @Override
    public void start() {
        try {
            JobDetail job = newJob(CmcAutoClearJob.class).withIdentity("CmcAutoClearJob").build();
            job.getJobDataMap().put(EntityService.class.getSimpleName(), entityService);
            job.getJobDataMap().put(EntityTypeService.class.getSimpleName(), entityTypeService);
            job.getJobDataMap().put(SystemPreferencesService.class.getSimpleName(), systemPreferencesService);
            job.getJobDataMap().put(CmcDao.class.getSimpleName(), cmcDao);
            job.getJobDataMap().put(CmcService.class.getSimpleName(), cmcService);
            job.getJobDataMap().put(AutoClearDao.class.getSimpleName(), autoClearDao);

            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(),
                    job.getKey().getGroup()).withSchedule(repeatSecondlyForever(24 * 3600));
            builder.startAt(new Date(System.currentTimeMillis() + 24 * 3600 * 1000));
            schedulerService.scheduleJob(job, builder.build());
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    public void destroy() {
        super.destroy();
    }
}
