/***********************************************************************
 * $Id: ReportTaskServiceImpl.java,v1.0 2013-6-18 下午5:35:41 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.report.dao.ReportTaskDao;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportTaskService;
import com.topvision.ems.report.service.SchedualedReportService;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2013-6-18-下午5:35:41
 * 
 */
@Service("reportTaskService")
public class ReportTaskServiceImpl extends BaseService implements ReportTaskService {
    private static final int ENABLED = 1;
    private static final int DISABLED = 0;
    public static final Integer WEEK_REPORT = 1;
    public static final Integer MONTH_REPORT = 2;
    private static final DateFormat stTimeFormatter_Daily = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    @Autowired
    private ReportTaskDao reportTaskDao;
    @Autowired
    private SchedualedReportService schedualedReportService;

    @Override
    public void createReportTask(ReportTask task) {
        // 将任务插入到数据库中并且得到自增长的taskId
        task.setState(true);
        Long taskId = reportTaskDao.insertReportTask(task);
        task.setTaskId(taskId);
        schedualedReportService.addSchedualedReportTask(task);
    }

    @Override
    public List<Long> deleteReportTask(List<Long> taskIds) {
        List<Long> failedTaskList = new ArrayList<Long>();
        for (Long taskId : taskIds) {
            try {
                schedualedReportService.deleteSchedualedReportTask(taskId);
                // 在任务中删除完毕的时候才将数据库中的删除掉
                reportTaskDao.deleteReportTask(taskId);
                failedTaskList.add(taskId);
            } catch (SchedulerException e) {
                logger.error("", e);
            }
        }
        return failedTaskList;
    }

    @Override
    public List<Long> enableReportTask(List<Long> taskIds) {
        List<Long> failedTaskList = new ArrayList<Long>();
        for (Long taskId : taskIds) {
            try {
                schedualedReportService.enableSchedualedReportTask(taskId);
                // 在任务中删除完毕的时候才将数据库中的删除掉
                reportTaskDao.modifyReportTask(taskId, ENABLED);
            } catch (SchedulerException e) {
                failedTaskList.add(taskId);
                logger.error("", e);
            }
        }
        return failedTaskList;
    }

    @Override
    public List<Long> disableReportTask(List<Long> taskIds) {
        List<Long> failedTaskList = new ArrayList<Long>();
        for (Long taskId : taskIds) {
            try {
                schedualedReportService.disableSchedualedReportTask(taskId);
                // 在任务中删除完毕的时候才将数据库中的删除掉
                reportTaskDao.modifyReportTask(taskId, DISABLED);
            } catch (SchedulerException e) {
                failedTaskList.add(taskId);
                logger.error("", e);
            }
        }
        return failedTaskList;
    }

    @Override
    public void modifyReportTask(ReportTask task) {
        reportTaskDao.modifyReprotTask(task);
        // schedualedReportService.modifyReportTask(task);
    }

    @Override
    public List<ReportTask> getReportTaskList(Map<String, Object> map) {
        List<ReportTask> taskList = reportTaskDao.selectReportTaskList(map);
        Calendar calendar = Calendar.getInstance();
        for (ReportTask task : taskList) {
            calendar.setTime(new Date());
            // 计算出下次执行时间
            Integer cycleType = task.getCycleType();
            if (WEEK_REPORT.equals(cycleType)) {
                // 下次执行时间为下周开始00：00：00
                calendar.add(Calendar.DATE, 7);
                Integer weekStartDay = (Integer) task.getCondition().get("weekStartDay");
                calendar.setFirstDayOfWeek(weekStartDay);
                calendar.set(Calendar.DAY_OF_WEEK, weekStartDay);
                task.setExecutorTimeString(stTimeFormatter_Daily.format(calendar.getTime()));
            } else if (MONTH_REPORT.equals(cycleType)) {
                // 下次执行时间为下月开始00：00：00
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DATE, 1);
                task.setExecutorTimeString(stTimeFormatter_Daily.format(calendar.getTime()));
            }
        }
        return taskList;
    }

    @Override
    public ReportTask getReportTaskByTaskId(Long taskId) {
        return reportTaskDao.getReportTaskByTaskId(taskId);
    }

    @Override
    public List<ReportTask> getReportTaskListByTemplateId(long templateId) {
        return reportTaskDao.getReportTaskListByTemplateId(templateId);
    }

    @Override
    public List<String> loadAllTaskNames(Long userId) {
        return reportTaskDao.loadAllTaskNames(userId);
    }

    @Override
    public Integer getReportTaskListNum(Map<String, Object> map) {
        return reportTaskDao.getReportTaskListNum(map);
    }

}
