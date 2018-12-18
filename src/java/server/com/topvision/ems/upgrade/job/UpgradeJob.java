/***********************************************************************
 * $Id: UpgradeJob.java,v1.0 2014年9月23日 下午7:15:12 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.job;

import java.util.List;
import java.util.concurrent.Future;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeJobInfo;
import com.topvision.ems.upgrade.service.UpgradeJobService;

/**
 * @author loyal
 * @created @2014年9月23日-下午7:15:12
 * 
 */
@PersistJobDataAfterExecution
public class UpgradeJob implements Job {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public static String TFTP = "tftp";
    public static String FTP = "ftp";

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        logger.debug("start run job");
        UpgradeJobInfo upgradeJobInfo = (UpgradeJobInfo) ctx.getJobDetail().getJobDataMap().get("jobInfo");
        BeanFactory beanFactory = (BeanFactory) ctx.getJobDetail().getJobDataMap().get("beanFactory");
        UpgradeJobService upgradeJobService = (UpgradeJobService) beanFactory.getBean("upgradeJobService");
        @SuppressWarnings("unchecked")
        List<UpgradeEntity> upgradeEntityList = (List<UpgradeEntity>) ctx.getJobDetail().getJobDataMap()
                .get("entityList");

        logger.debug("upgradeJobService.upgradeEntityList[" + upgradeJobInfo.getJobId() + "] size:"
                + upgradeEntityList.size());
        List<Future<UpgradeEntity>> futures = upgradeJobService.upgradeEntityList(upgradeJobInfo, upgradeEntityList);
        upgradeJobService.putFutureJob(upgradeJobInfo.getJobId(), futures);
        ctx.getJobDetail().getJobDataMap().put("entityList", upgradeEntityList);
    }

}
