package com.topvision.ems.cmc.ccmts.service.impl;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import com.topvision.ems.autoclear.dao.AutoClearDao;
import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.job.CmciAutoClearJob;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;

@Service("cmciAutoClearService")
public class CmciAutoClearServiceImpl extends BaseService{
    
    private static final String NAME = "cmciAutoClearJob";
    private static final String GROUP = "cmciAutoClearGroup";
    
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcDao cmcDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private AutoClearDao autoClearDao;
    
    @Override
    public void initialize() {
        super.initialize();
    }
    
    @Override
    public void start() {
        try{
            JobDetail job = newJob(CmciAutoClearJob.class).withIdentity(NAME,GROUP).build();
            job.getJobDataMap().put(EntityTypeService.class.getSimpleName(), entityTypeService);
            job.getJobDataMap().put(SystemPreferencesService.class.getSimpleName(), systemPreferencesService);
            job.getJobDataMap().put(EntityService.class.getSimpleName(), entityService);
            job.getJobDataMap().put(CmcDao.class.getSimpleName(), cmcDao);
            job.getJobDataMap().put(AutoClearDao.class.getSimpleName(), autoClearDao);
            
            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(),
                    job.getKey().getGroup()).withSchedule(repeatSecondlyForever(24 * 3600));
            builder.startAt(new Date(System.currentTimeMillis() + 24 * 3600 * 1000));
            schedulerService.scheduleJob(job, builder.build());
        } catch (Exception e) {
            logger.error("", e);
        }
    }
    
    @Override
    public void destroy() {
        super.destroy();
    }
}
