/***********************************************************************
 * $Id: SchedualedReportJob.java,v1.0 2013-5-22 下午3:42:25 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportCoreService;

/**
 * 所有自动报表任务的载体
 * 
 * @author Bravin
 * @created @2013-5-22-下午3:42:25
 * 
 */
public class SchedualedReportJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(SchedualedReportJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        ReportTask task = (ReportTask) jobDataMap.get("report.reportTask");
        ReportCoreService reportCoreService = (ReportCoreService) jobDataMap.get("reportCoreService");
        if (logger.isDebugEnabled()) {
            logger.debug("begin to do taskReport :{}", task.getTaskName());
        }
        // 使用jar包的方式去进行生成excel报表
        try {
            reportCoreService.generateTaskFile(task);
        } catch (Exception e) {
            logger.error("build report task file failed", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("finish to do taskReport :{}", task.getTaskName());
        }
        // BeanFactory beanFactory = (BeanFactory) jobDataMap.get("beanFactory");
        // ReportCreator reportCreator = (ReportCreator)
        // beanFactory.getBean(task.getReportCreatorBeanName());

        // reportCreator.bulidReport(task);

    }

}
