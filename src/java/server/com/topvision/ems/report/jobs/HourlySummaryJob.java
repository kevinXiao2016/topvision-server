/***********************************************************************
 * $Id: HourlySummaryJob.java,v 1.1 Apr 16, 2009 10:10:35 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.report.message.SummaryEvent;
import com.topvision.ems.report.message.SummaryListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @Create Date Apr 16, 2009 10:10:35 PM
 * 
 * @author kelers
 * 
 */
public class HourlySummaryJob implements Job {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private MessageService messageService;

    /**
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (logger.isDebugEnabled()) {
            StringBuilder jobInfo = new StringBuilder();
            jobInfo.append("\n=====================HourlySummaryJob==========================");
            jobInfo.append("\nJob:").append(context.getJobDetail().getKey());
            jobInfo.append("\ngetJobRunTime:").append(context.getJobRunTime());
            jobInfo.append("\ngetRefireCount:").append(context.getRefireCount());
            jobInfo.append("\ngetFireTime:").append(context.getFireTime());
            jobInfo.append("\ngetNextFireTime:").append(context.getNextFireTime());
            jobInfo.append("\ngetPreviousFireTime:").append(context.getPreviousFireTime());
            jobInfo.append("\ngetScheduledFireTime:").append(context.getScheduledFireTime());
            jobInfo.append("\n=====================HourlySummaryJob==========================");
            logger.debug(jobInfo.toString());
        }
        logger.info("begin to summary hourly...");
        messageService = (MessageService) context.getJobDetail().getJobDataMap().get("messageService");
        SummaryEvent event = new SummaryEvent(new Object());
        event.setActionName("execHourlySummary");
        event.setListener(SummaryListener.class);
        messageService.addMessage(event);
        logger.info("end of hourly summary!");
    }
}
