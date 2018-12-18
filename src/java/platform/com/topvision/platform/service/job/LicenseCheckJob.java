/***********************************************************************
 * $Id: LicenseCheckJob.java,v1.0 2014年10月21日 上午9:10:30 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.license.parser.LicenseIf;

/**
 * @author Victor
 * @created @2014年10月21日-上午9:10:30
 *
 */
public class LicenseCheckJob extends AbstractJob {
    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        LicenseIf licenseIf = (LicenseIf) jobDataMap.get(LicenseIf.class.getName());
        licenseIf.checkLicense();
    }
}
