/***********************************************************************
 * $Id: NbiCheckJob.java,v1.0 2016年3月17日 上午10:14:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.nbi.rmi.NbiRmiService;
import com.topvision.ems.nbi.service.NbiConnectionService;
import com.topvision.performance.nbi.api.Server2NbiInvoke;

/**
 * @author Bravin
 * @created @2016年3月17日-上午10:14:00
 *
 */
public class NbiCheckJob implements Job {
    protected final static Logger logger = LoggerFactory.getLogger(NbiCheckJob.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        logger.info("start to check performance NBI");
        JobDataMap $jobDataMap = ctx.getJobDetail().getJobDataMap();
        NbiConnectionService nbiConnectionService = (NbiConnectionService) $jobDataMap.get("nbiConnectionService");
        NbiRmiService nbiRmiService = (NbiRmiService) $jobDataMap.get("nbiRmiService");
        String nbiIpAddress = nbiConnectionService.getNbiIpAddress();
        Integer nbiPort = nbiConnectionService.getNbiPort();
        Server2NbiInvoke invoke = nbiRmiService.getNbiService(Server2NbiInvoke.class, nbiIpAddress, nbiPort);
        String serverIp = nbiConnectionService.getServerIp();
        int serverPort = nbiConnectionService.getServerPort();
        long checkNbiInterval = nbiConnectionService.getCheckNbiInterval() * 60000L;
        try {
            if (invoke.check(serverIp, serverPort, checkNbiInterval)) {
                nbiConnectionService.notifyEngineConnectNbi();
                logger.info("performance NBI is connected!");
                return;
            }
            logger.info("performance NBI has been checked!");
        } catch (Exception e) {
            logger.warn("performance NBI con't be connected", e);
            nbiConnectionService.incrementAndHandleDisconnected();
        }
    }

}
