/***********************************************************************
 * $Id: CmCpeCollectDataPolicyJob.java,v1.0 2013-12-7 上午11:27:54 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.service.job;

import java.util.Calendar;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.cmc.cpe.dao.CpeAnalyseDao;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmCpeCollectDataPolicy;
import com.topvision.framework.scheduler.AbstractJob;

/**
 * @author Victor
 * @created @2013-12-7-上午11:27:54
 * 
 */
public class CmCpeCollectDataPolicyJob extends AbstractJob {

    /* (non-Javadoc)
     * @see com.topvision.framework.scheduler.AbstractJob#doJob(org.quartz.JobExecutionContext)
     */
    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        CpeAnalyseDao cpeAnalyseDao = (CpeAnalyseDao) jobDataMap.get("cpeAnalyseDao");
        CpeService cpeService = (CpeService) jobDataMap.get("cpeService");
        Calendar ca = Calendar.getInstance();
        int hour = ca.get(Calendar.HOUR_OF_DAY);
        // 每天凌晨3点删除数据
        if (hour == 3) {
            CmCpeCollectDataPolicy cmCpeCollectDataPolicy = cpeService.getCmCpeCollectDataPolicy();
            logger.debug("delete InitialData before " + cmCpeCollectDataPolicy.getInitDataSavePolicy()
                    / (24 * 60 * 60 * 1000L) + " days");
            cpeAnalyseDao.deleteInitialData(cmCpeCollectDataPolicy.getInitDataSavePolicy());
            logger.debug("delete StatisticData before " + cmCpeCollectDataPolicy.getStatisticDataSavePolicy()
                    / (24 * 60 * 60 * 1000L) + " days");
            cpeAnalyseDao.deleteStatisticData(cmCpeCollectDataPolicy.getStatisticDataSavePolicy());
            logger.debug("delete ActionData before " + cmCpeCollectDataPolicy.getActionDataSavePolicy()
                    / (24 * 60 * 60 * 1000L) + " days");
            cpeAnalyseDao.deleteActionData(cmCpeCollectDataPolicy.getActionDataSavePolicy());
            logger.debug("delete CmHistoryData before " + cmCpeCollectDataPolicy.getCmHistorySavePolicy()
                    / (24 * 60 * 60 * 1000L) + " days");
            cpeAnalyseDao.deleteCmHistoryData(cmCpeCollectDataPolicy.getCmHistorySavePolicy());
        }
    }
}
