/***********************************************************************
 * $Id: MakeSendConfigArrayJob.java,v1.0 2014-5-11 下午2:28:34 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.topvision.ems.network.service.CommandSendService;
import com.topvision.ems.network.service.CommonConfigService;

/**
 * 
 * @author jay
 * @created @2014-5-11-下午2:28:34
 * 
 */
@PersistJobDataAfterExecution
public class MakeSendConfigArrayJob implements Job {
    // private static Logger logger = LoggerFactory.getLogger(MakeSendConfigArrayJob.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        JobDataMap jobDataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
        CommandSendService commandSendService = (CommandSendService) jobDataMap.get("commandSendService");
        CommonConfigService commonConfigService = (CommonConfigService) jobDataMap.get("commonConfigService");
        commandSendService.makeUnstartSendConfigArray();
        // 只有开启重新下发失败设备的开关才下发失败的设备
        if (commonConfigService.loadFailAutoSendConfigSwitch()) {
            commandSendService.makeFailedSendConfigArray();
        }
    }

}
