/***********************************************************************
 * $Id: OnuAutoCleanServiceImpl.java,v1.0 2016年11月10日 下午3:59:49 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

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
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.job.AutoRefreshLastDeregisterTimeJob;
import com.topvision.ems.epon.onu.job.OnuAutoClearJob;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.gpon.onuauth.service.GponOnuAuthService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author Rod John
 * @created @2016年11月10日-下午3:59:49
 *
 */
@Service
public class OnuAutoClearServiceImpl extends BaseService {
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private AutoClearDao autoClearDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private GponOnuAuthService gponOnuAuthService;

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
        // Auto Clear Onu Job
        try {
            JobDetail job = newJob(OnuAutoClearJob.class).withIdentity("OnuAutoClearJob").build();
            job.getJobDataMap().put(EntityService.class.getSimpleName(), entityService);
            job.getJobDataMap().put(EntityTypeService.class.getSimpleName(), entityTypeService);
            job.getJobDataMap().put(SystemPreferencesService.class.getSimpleName(), systemPreferencesService);
            job.getJobDataMap().put(OnuDao.class.getSimpleName(), onuDao);
            job.getJobDataMap().put(OnuAuthService.class.getSimpleName(), onuAuthService);
            job.getJobDataMap().put(AutoClearDao.class.getSimpleName(), autoClearDao);
            job.getJobDataMap().put(FacadeFactory.class.getSimpleName(), facadeFactory);
            job.getJobDataMap().put(GponOnuAuthService.class.getSimpleName(), gponOnuAuthService);

            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup()).withSchedule(repeatSecondlyForever(24 * 3600));
            builder.startAt(new Date(System.currentTimeMillis() + 24 * 3600 * 1000));
            schedulerService.scheduleJob(job, builder.build());
        } catch (Exception e) {
            logger.error("", e);
        }

        // Auto Refresh Last Deregister Time Job
        try {
            JobDetail job = newJob(AutoRefreshLastDeregisterTimeJob.class).withIdentity("AutoRefreshLastDeregisterTime").build();
            job.getJobDataMap().put(EntityService.class.getSimpleName(), entityService);
            job.getJobDataMap().put(OnuDao.class.getSimpleName(), onuDao);
            job.getJobDataMap().put(FacadeFactory.class.getSimpleName(), facadeFactory);

            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup()).withSchedule(repeatSecondlyForever(3600));
            builder.startAt(new Date(System.currentTimeMillis() + 3600 * 1000));
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
