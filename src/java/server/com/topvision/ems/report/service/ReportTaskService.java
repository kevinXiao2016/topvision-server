/***********************************************************************
 * $Id: ReportTaskService.java,v1.0 2013-6-18 下午5:33:52 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.report.domain.ReportTask;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013-6-18-下午5:33:52
 * 
 */
public interface ReportTaskService extends Service {

    /**
     * 新建一个报表任务.
     * 
     * @param task
     */
    void createReportTask(ReportTask task);

    /**
     * 修改报表任务
     * 
     * @param task
     */
    void modifyReportTask(ReportTask task);

    /**
     * 获取报表任务列表
     * 
     * @param state
     * @return
     */
    List<ReportTask> getReportTaskList(Map<String, Object> map);

    /**
     * 删除给定任务ID集合的报表任务.
     * 
     * @param taskIds
     * @return
     */
    List<Long> deleteReportTask(List<Long> taskIds);

    /**
     * 获取给定任务ID的报表任务.
     * 
     * @param taskId
     * @return
     */
    ReportTask getReportTaskByTaskId(Long taskId);

    /**
     * 启动给定的报表任务.
     * 
     * @param taskIds
     * @return
     */
    List<Long> enableReportTask(List<Long> taskIds);

    /**
     * 停用给定的报表任务.
     * 
     * @param taskIds
     * @return
     */
    List<Long> disableReportTask(List<Long> taskIds);

    /**
     * 通过templateId获取任务
     * 
     * @param templateId
     * @return
     */
    List<ReportTask> getReportTaskListByTemplateId(long templateId);

    /**
     * 获取数据库中的所有报表任务的名称
     * 
     * @return
     */
    List<String> loadAllTaskNames(Long userId);

    /**
     * 获取报表任务分页数据
     * 
     * @param map
     * @return
     */
    Integer getReportTaskListNum(Map<String, Object> map);
}
