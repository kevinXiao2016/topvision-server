/***********************************************************************
 * $Id: ReportTaskDaoImpl.java,v1.0 2013-10-11 上午11:22:02 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.report.dao.ReportTaskDao;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author fanzidong
 * @created @2013-10-11-上午11:22:02
 * 
 */
@Repository("reportTaskDao")
public class ReportTaskDaoImpl extends MyBatisDaoSupport<ReportTask> implements ReportTaskDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.report.domain.ReportTask";
    }

    @Override
    public Long insertReportTask(ReportTask task) {
        getSqlSession().insert(getNameSpace("insertReportTask"), task);
        return task.getTaskId();
    }

    @Override
    public void deleteReportTask(Long taskId) {
        // 删除报表任务
        getSqlSession().delete(getNameSpace("deleteReportTask"), taskId);
        // 将详细报表记录中相关taskId设置为-1
        getSqlSession().update(getNameSpace("updateReportInstanceTask"), taskId);
    }

    @Override
    public void modifyReportTask(Long taskId, int enabled) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", taskId);
        map.put("state", enabled);
        getSqlSession().update(getNameSpace("modifyReportTask"), map);
    }

    @Override
    public List<ReportTask> selectReportTaskList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectReportTaskList"), map);
    }

    @Override
    public List<ReportTask> getReportTaskListByTemplateId(long templateId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("templateId", templateId);
        map.put("userId", CurrentRequest.getCurrentUser().getUserId());
        return getSqlSession().selectList(getNameSpace("selectReportTaskListByTemplateId"), map);
    }

    @Override
    public ReportTask getReportTaskByTaskId(Long taskId) {
        return getSqlSession().selectOne(getNameSpace("getReportTaskByTaskId"), taskId);
    }

    @Override
    public void modifyReprotTask(ReportTask task) {
        getSqlSession().update(getNameSpace("modifyReprotTaskByTask"), task);
    }

    @Override
    public List<String> loadAllTaskNames(Long userId) {
        return getSqlSession().selectList(getNameSpace("loadAllTaskNames"), userId);
    }

    @Override
    public Integer getReportTaskListNum(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("getReportTaskListNum"), map);
    }

    @Override
    public List<ReportTask> selectReportTaskList() {
        return getSqlSession().selectList(getNameSpace("selectReportTaskListForTask"));
    }

}
