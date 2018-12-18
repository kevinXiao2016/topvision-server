/***********************************************************************
 * $Id: CmcFlapClearServiceImpl.java,v1.0 2017年2月6日 下午5:11:08 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Date;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcListDao;
import com.topvision.ems.cmc.ccmts.job.CmcFlapClearJob;
import com.topvision.ems.cmc.ccmts.service.CmcFlapClearService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author lizongtian
 * @created @2017年2月6日-下午5:11:08
 *
 */
@Service("cmcFlapClearService")
public class CmcFlapClearServiceImpl extends BaseService implements CmcFlapClearService {
    @Autowired
    private CmcService cmcService;
    @Autowired
    private CmcListDao cmcListDao;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private SchedulerService schedulerService;
    @Value("${cm.flap.clear.job.name:CmcFlapClearJob}")
    private String DEFAULT_CM_FLAP_CLEAR_JOB_NAME;

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void start() {
        try {
            newClearJob();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public void resetClearTrigger(String flapClearPeriod) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(DEFAULT_CM_FLAP_CLEAR_JOB_NAME);
        SimpleTrigger trigger = (SimpleTrigger) schedulerService.getTrigger(triggerKey);
        //如果job存在则修改定期执行时间，如果不存在则创建一个新的job
        int clearPeriod = Integer.parseInt(flapClearPeriod);
        if (trigger != null) {
            JobKey jobKey = trigger.getJobKey();
            if (clearPeriod > 0) {
                TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(jobKey.getName(), jobKey.getGroup())
                        .withSchedule(repeatSecondlyForever(clearPeriod * 24 * 3600));
                builder.startAt(new Date(System.currentTimeMillis() + clearPeriod * 24 * 3600 * 1000));
                schedulerService.modifyJobTrigger(builder.build());
            } else {
                schedulerService.pauseJob(jobKey);
            }
        } else {
            newClearJob();
        }
    }

    private void newClearJob() throws SchedulerException {
        JobDetail job = newJob(CmcFlapClearJob.class).withIdentity(DEFAULT_CM_FLAP_CLEAR_JOB_NAME).build();
        job.getJobDataMap().put(CmcService.class.getSimpleName(), cmcService);
        job.getJobDataMap().put(CmcListDao.class.getSimpleName(), cmcListDao);
        job.getJobDataMap().put(FacadeFactory.class.getSimpleName(), facadeFactory);
        Integer threshold = Integer.parseInt(systemPreferencesService.getSystemPreference("cmtsFlapClearPeriod")
                .getValue()) * 24 * 3600;
        if (threshold > 0) {
            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(),
                    job.getKey().getGroup()).withSchedule(repeatSecondlyForever(threshold));
            //系统启动间隔周期后执行清除
            builder.startAt(new Date(System.currentTimeMillis() + threshold * 1000));
            schedulerService.scheduleJob(job, builder.build());
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
