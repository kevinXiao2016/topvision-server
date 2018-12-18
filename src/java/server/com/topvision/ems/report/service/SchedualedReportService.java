/***********************************************************************
 * $Id: SchedualedReportService.java,v1.0 2013-5-22 下午4:23:35 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service;

import org.quartz.SchedulerException;

import com.topvision.ems.report.domain.ReportTask;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013-5-22-下午4:23:35
 * 
 */
public interface SchedualedReportService extends Service {

    /**
     * 添加一个定时任务
     * 
     * @param task
     * @return
     */
    void addSchedualedReportTask(ReportTask task);

    /**
     * 删除一个定时报表任务
     * 
     * @param key
     * @throws SchedulerException
     */
    void deleteSchedualedReportTask(Long taskId) throws SchedulerException;

    /**
     * 恢复执行一个暂停的报表任务的执行
     * 
     * @param taskId
     * @throws SchedulerException
     */
    void enableSchedualedReportTask(Long taskId) throws SchedulerException;

    /**
     * 暂停一个报表任务的执行
     * 
     * @param taskId
     * @throws SchedulerException
     */
    void disableSchedualedReportTask(Long taskId) throws SchedulerException;

    /**
     * 修改一个定时报表任务
     * 
     * @param task
     */
    void modifyReportTask(ReportTask task);

}
