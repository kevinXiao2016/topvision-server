/***********************************************************************
 * $Id: ValueSummary.java,v 1.1 Apr 16, 2009 10:03:56 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.report.jobs.DailySummaryJob;
import com.topvision.ems.report.jobs.HourlySummaryJob;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SchedulerService;

/**
 * @Create Date Apr 16, 2009 10:03:56 PM
 * 
 * @author kelers
 * 
 */
@Service("valueSummary")
public class ValueSummary extends BaseService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private MessageService messageService;

    @Override
    public void destroy() {
    }

    @Override
    @PostConstruct
    public void initialize() {
        /*JobDetail job = newJob(HourlySummaryJob.class).withIdentity("hourly", "summary").build();
        job.getJobDataMap().put("messageService", messageService);
        Trigger trg = null;
        try {
            trg = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(cronSchedule("0 1 * * * ?")).build();
            schedulerService.scheduleJob(job, trg);
        } catch (SchedulerException e) {
            logger.debug(e.getMessage(), e);
        }*/
        JobDetail job = newJob(DailySummaryJob.class).withIdentity("daily", "summary").build();
        job.getJobDataMap().put("messageService", messageService);
        try {
            Trigger trg = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(cronSchedule("0 0 1 * * ?")).build();
            schedulerService.scheduleJob(job, trg);
            schedulerService.triggerJob(job.getKey());
        } catch (SchedulerException e) {
            logger.debug(e.getMessage(), e);
        }
    }

}
