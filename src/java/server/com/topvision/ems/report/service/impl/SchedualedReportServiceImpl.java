/***********************************************************************
 * $Id: SchedualedReportServiceImpl.java,v1.0 2013-5-22 下午4:23:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.ems.report.dao.ReportTaskDao;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.jobs.SchedualedReportJob;
import com.topvision.ems.report.service.ReportCoreService;
import com.topvision.ems.report.service.SchedualedReportService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.SchedulerService;

/**
 * @author Bravin
 * @created @2013-5-22-下午4:23:51
 * 
 */
@Service("schedualedReportService")
public class SchedualedReportServiceImpl extends BaseService implements SchedualedReportService, BeanFactoryAware {
    @Autowired
    private ReportTaskDao reportTaskDao;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private ReportCoreService reportCoreService;
    private BeanFactory beanFactory;

    /**
     * 初始化,从数据库中读取当前所有的报表任务，并且启动
     */
    @Override
    @PostConstruct
    public void initialize() {
        // 启动定时报表
        try {
            List<ReportTask> reportTasks = reportTaskDao.selectReportTaskList();
            int size = reportTasks.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    ReportTask task = null;
                    task = reportTasks.get(i);
                    try {
                        // 保持服务器停止前的任务状态，启动的任务恢复执行
                        addSchedualedReportTask(task);
                    } catch (Exception e) {
                        getLogger().error("Create Report Job By ReportTasks:", e);
                    }
                }
            }
        } catch (DataAccessException ex) {
            getLogger().error("Get ReportTasks:", ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.report.service.SchedualedReportService#addSchedualedReportTask(com.topvision
     * .ems.report.domain.ReportTask)
     */
    @Override
    public void addSchedualedReportTask(ReportTask task) {
        JobKey jobKey = task.getJobKey();
        JobDetail job = newJob(SchedualedReportJob.class).withIdentity(jobKey).build();
        job.getJobDataMap().put("report.reportTask", task);
        job.getJobDataMap().put("reportCoreService", reportCoreService);
        Trigger trg = null;
        try {
            trg = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(cronSchedule(task.getCronExpression())).build();
            schedulerService.scheduleJob(job, trg);
            // 判断任务的暂停与否，所以在任务新加入的时候就必须制定 state=1，设置其开启状态
            if (!task.isState()) {
                schedulerService.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.report.service.SchedualedReportService#deleteSchedualedReportTask(java.
     * lang.Long)
     */
    @Override
    public void deleteSchedualedReportTask(Long taskId) throws SchedulerException {
        JobKey key = ReportTask.getJobKey(taskId);
        schedulerService.deleteJob(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.report.service.SchedualedReportService#disableSchedualedReportTask(java
     * .lang.Long)
     */
    @Override
    public void disableSchedualedReportTask(Long taskId) throws SchedulerException {
        JobKey key = ReportTask.getJobKey(taskId);
        schedulerService.pauseJob(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.report.service.SchedualedReportService#enableSchedualedReportTask(java.
     * lang.Long)
     */
    @Override
    public void enableSchedualedReportTask(Long taskId) throws SchedulerException {
        JobKey key = ReportTask.getJobKey(taskId);
        schedulerService.resumeJob(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.report.service.SchedualedReportService#modifyReportTask(com.topvision.ems
     * .report.domain.ReportTask)
     */
    @Override
    public void modifyReportTask(ReportTask task) {
        TriggerKey tk = task.getTriggerKey();
        try {
            CronTriggerImpl oldTrigger = (CronTriggerImpl) schedulerService.getTrigger(tk);
            String cronExpression = task.getCronExpression();
            if (oldTrigger != null && oldTrigger.getCronExpression().equals(cronExpression)) {
                return;
            }
            Trigger trigger = newTrigger().withIdentity(tk.getName(), tk.getGroup())
                    .withSchedule(cronSchedule(cronExpression)).build();
            // modify by bravin@20140327: 如果2个cron表达式一致则不作修改，否则将会产生立即触发job被执行的BUG
            JobDetail jobDetail = schedulerService.getJobDetail(task.getJobKey());
            jobDetail.getJobDataMap().put("report.reportTask", task);
            schedulerService.modifySchedualJob(jobDetail, trigger);
            // 判断任务的暂停与否，所以在任务新加入的时候就必须制定 state=1，设置其开启状态
            if (!task.isState()) {
                schedulerService.pauseJob(task.getJobKey());
            }
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
