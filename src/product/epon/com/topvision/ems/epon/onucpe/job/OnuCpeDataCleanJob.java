/***********************************************************************
 * $Id: OnuCpeDataCleanJob.java,v1.0 2016年7月12日 下午3:43:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.epon.onucpe.service.OnuCpeService;

/**
 * @author Bravin
 * @created @2016年7月12日-下午3:43:37
 *
 */
public class OnuCpeDataCleanJob implements Job {

    /* (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        JobDataMap jobDataMap = ctx.getJobDetail().getJobDataMap();
        OnuCpeService onuCpeService = (OnuCpeService) jobDataMap.get("onuCpeService");
        onuCpeService.execDataClean();
    }
}
