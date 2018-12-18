/***********************************************************************
 * $Id: ReportInstanceServiceImpl.java,v1.0 2013-6-18 下午5:36:38 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service.impl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.report.dao.ReportInstanceDao;
import com.topvision.ems.report.dao.ReportTaskDao;
import com.topvision.ems.report.domain.ReportInstance;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2013-6-18-下午5:36:38
 * 
 */
@Service("reportInstanceService")
public class ReportInstanceServiceImpl extends BaseService implements ReportInstanceService {
    @Autowired
    private ReportInstanceDao reportInstanceDao;
    @Autowired
    private ReportTaskDao reportTaskDao;
    public static final String NO_TASK_REPORT = "noTaskReport";

    @Override
    public void addReportInstance(String filePath, int fileType, ReportTask task) {
        ReportInstance report = new ReportInstance();
        report.setFilePath(filePath);
        report.setFileType(fileType);
        report.setInstanceTitle((String) task.getCondition("title"));
        report.setNote(task.getNote());
        report.setTaskId(task.getTaskId());
        report.setUserId(task.getUserId());
        reportInstanceDao.insertReportInstance(report);
    }

    @Override
    public void deleteReport(List<Long> reportIds) {
        reportInstanceDao.deleteReport(reportIds);
    }

    @Override
    public ReportInstance getReportDetailByReportId(Long reportId) {
        return reportInstanceDao.getReportDetailByReportId(reportId);
    }

    @Override
    public JSONObject loadReportWithTask() {
        JSONObject reportJson = new JSONObject();
        // 读取所有的任务列表
        List<ReportTask> reportTasks = reportTaskDao.selectReportTaskList();
        // 遍历，将任务按照报表进行分类
        for (ReportTask reportTask : reportTasks) {
            // 获取当前任务的reportId
            String reportId = reportTask.getReportId();
            if (reportJson.containsKey(reportId)) {// 已经存在该类型报表，直接添加即可
                JSONArray certainReportTasks = reportJson.getJSONObject(reportId).getJSONArray("childrens");
                certainReportTasks.add(reportTask);
            } else {// 还未存在该类型报表，新建
                JSONArray certainReportTasks = new JSONArray();
                certainReportTasks.add(reportTask);
                JSONObject certainReport = new JSONObject();
                certainReport.put("childrens", certainReportTasks);
                certainReport.put("reportName", reportTask.getReportName());
                reportJson.put(reportId, certainReport);
            }
        }
        JSONObject certainReport = new JSONObject();
        certainReport.put("childrens", new JSONArray());
        certainReport.put("reportName", ReportTaskUtil.getString("report.outoftask", "report"));
        reportJson.put(NO_TASK_REPORT, certainReport);
        return reportJson;
    }

    @Override
    public List<ReportInstance> getReportInstanceList(Map<String, Object> queryMap) {
        return reportInstanceDao.getReportInstanceList(queryMap);
    }

    @Override
    public Integer getReportInstanceNum(Map<String, Object> queryMap) {
        return reportInstanceDao.getReportInstanceNum(queryMap);
    }

}
