/***********************************************************************
 * $Id: AbstractJob.java,v1.0 2013-12-6 下午5:14:45 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twaver.base.A.E.T;

/**
 * @author Victor
 * @created @2013-12-6-下午5:14:45
 *
 */
public abstract class AbstractJob implements Job {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected JobDataMap jobDataMap;

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        String name = Thread.currentThread().getName();
        Thread.currentThread().setName(ctx.getJobDetail().getKey().getName());
        jobDataMap = ctx.getJobDetail().getJobDataMap();
        doJob(ctx);
        Thread.currentThread().setName(name);
    }

    public abstract void doJob(JobExecutionContext ctx) throws JobExecutionException;

    @SuppressWarnings({ "unchecked", "hiding" })
    protected <T> T getService(Class<T> clazz) {
        T t = (T) jobDataMap.get(clazz.getSimpleName());
        if (t == null) {
            t = (T) jobDataMap.get(clazz.getSimpleName() + "Impl");
        }
        return t;
    }
}
