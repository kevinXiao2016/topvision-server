/***********************************************************************
 * $Id: TryPasswordTimeoutJob.java,v1.0 2014年3月6日 下午3:42:11 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.user.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.platform.user.context.UserContextManager;

/**
 * @author Bravin
 * @created @2014年3月6日-下午3:42:11
 *
 */
public class TryPasswordCleanJob implements Job {

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        System.out.println("try-------------------------");
        UserContextManager manager = (UserContextManager) ctx.getJobDetail().getJobDataMap().get("manager");
        manager.cleanUser();
    }

}
