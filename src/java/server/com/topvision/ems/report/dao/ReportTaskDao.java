/***********************************************************************
 * $Id: ReportTaskDao.java,v1.0 2013-6-18 下午2:46:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.report.domain.ReportTask;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013-6-18-下午2:46:51
 * 
 */
public interface ReportTaskDao extends BaseEntityDao<ReportTask> {
    /**
     * 在数据库中添加一个任务报表
     * 
     * @param task
     * @return
     */
    Long insertReportTask(ReportTask task);

    /**
     * 在数据库中删除一个任务报表
     * 
     * @param taskId
     */
    void deleteReportTask(Long taskId);

    /**
     * 在数据库中修改一个任务报表
     * 
     * @param taskId
     * @param enabled
     */
    void modifyReportTask(Long taskId, int enabled);

    /**
     * 获取所有的任务报表,如果state为null，则表示取出所有，否则表示取暂停的(0)，启动的(1)
     * 
     * @return
     */
    List<ReportTask> selectReportTaskList(Map<String, Object> map);

    /**
     * 通过templateId获取tasklist
     * 
     * @param templateId
     * @return
     */
    List<ReportTask> getReportTaskListByTemplateId(long templateId);

    /**
     * 通过taskID获取reporttask
     * 
     * @param taskId
     * @return
     */
    ReportTask getReportTaskByTaskId(Long taskId);

    /**
     * 修改reporttask
     * 
     * @param task
     */
    void modifyReprotTask(ReportTask task);

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

    /**
     * 为定时任务获取所有的报表任务
     * 
     * @return
     */
    List<ReportTask> selectReportTaskList();

}
