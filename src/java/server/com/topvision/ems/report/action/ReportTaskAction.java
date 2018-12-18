/***********************************************************************
 * $Id: ReportTaskAction.java,v1.0 2013-5-23 上午10:52:30 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.report.domain.ReportColumnReferences;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.ems.report.service.ReportCoreService;
import com.topvision.ems.report.service.ReportTaskService;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.JSONUtil;

/**
 * 所有的报表任务的action层处理在此
 * 
 * @author Bravin
 * @created @2013-5-23-上午10:52:30
 * 
 */
@Controller("reportTaskAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReportTaskAction extends BaseAction {
    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(ReportTaskAction.class);
    private static final long serialVersionUID = 2055070274035519305L;
    private static final String CONFIG_JSP = "config.jsp";
    @Autowired
    private ReportTaskService reportTaskService;
    @Autowired
    private ReportCoreService reportCoreService;
    private Long taskId;
    private Long[] taskIdList;
    private int templateId;
    private String taskName;
    private int cycleType;
    private int excutorType;
    private int[] weekDaySelected;
    private int excutorDay;
    private int excutorHour;
    private int excutorMin;
    private boolean pdfSupport;
    private boolean excelSupport;
    private boolean htmlSupport;
    private String name;
    private String note;
    private String[] email;
    private Map<String, Object> condition;
    private String conditions;
    private Integer state;
    private String reportCreatorBeanName;
    private JSONObject reportTaskJson;
    private JSONObject policyJson;
    private JSONArray taskNamesJson;
    private int start;
    private int limit;
    private Integer weekStartDay;
    private String reportId;
    private JSONArray queryCondition;
    private String reportName;
    private String detailReportInitCondition;
    // 报表内容
    private JSONObject reportContent;
    private JSONArray allColumns;

    /**
     * 显示报表的任务
     * 
     * @return
     * @throws IOException
     */
    public String showTaskSchedule() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        setWeekStartDay(uc.getWeekStartDay());
        // 获取数据库中的所有报表任务的名称
        List<String> taskNames = reportTaskService.loadAllTaskNames(uc.getUserId());
        taskNamesJson = JSONArray.fromObject(taskNames);
        return SUCCESS;
    }

    /**
     * 显示报表任务列表
     * 
     * @return
     * @throws IOException
     */
    public String showAllReportTask() {
        return SUCCESS;
    }

    /**
     * 显示报表的配置界面
     * 
     * @return
     * @throws IOException
     */
    public String showTaskReportConfig() throws IOException {
        response.sendRedirect(reportCreatorBeanName.concat("/").concat(CONFIG_JSP));
        return NONE;
    }

    public String showReportTaskCondition() throws Exception {
        List<ReportColumnReferences> columnReferences = reportCoreService.getReportColumns(reportId);
        setAllColumns(JSONArray.fromObject(columnReferences));
        return SUCCESS;
    }

    /**
     * 创建一个定时报表任务
     * 
     * @return
     * @throws Exception
     */
    public String createReportTask() throws Exception {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        ReportTask reportTask = new ReportTask();
        reportTask.setUserId(CurrentRequest.getCurrentUser().getUserId());
        reportTask.setTaskName(taskName);
        reportTask.setReportId(reportId);
        reportTask.setReportName(reportName);
        reportTask.setCycleType(cycleType);
        // reportTask.setReportCreatorBeanName(reportCreatorBeanName);
        if (cycleType == ReportTaskUtil.WEEKLY_REPORT) {
            condition.put("weekStartDay", uc.getWeekStartDay());
            excutorDay = uc.getWeekStartDay();
        } else {
            excutorDay = 1;
        }
        /** 使用任务创建者的权限统计数据 **/
        condition.put("userId", CurrentRequest.getCurrentUser().getUserId());
        reportTask.setCondition(condition);
        String cronExpress = ReportTaskUtil.encodeCronExpression(cycleType, excutorDay, 0, 0);
        reportTask.setCronExpression(cronExpress);
        reportTask.setPdfEnabled(pdfSupport);
        reportTask.setExcelEnabled(excelSupport);
        reportTask.setHtmlEnabled(htmlSupport);
        reportTask.setNote(note);
        reportTask.setEmailList(email);
        reportTaskService.createReportTask(reportTask);
        return NONE;
    }

    /**
     * 跳转到修改报表任务的界面
     * 
     * @return
     */
    public String showModifyReprotTask() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        setWeekStartDay(uc.getWeekStartDay());
        ReportTask reportTask = reportTaskService.getReportTaskByTaskId(taskId);
        // ExcutorTimePolicy policy = ReportTaskUtil.decodeCronExpression(reportTask);
        // 获取数据库中的所有报表任务的名称
        List<String> taskNames = reportTaskService.loadAllTaskNames(uc.getUserId());
        reportTaskJson = JSONObject.fromObject(reportTask);
        // policyJson = JSONObject.fromObject(policy);
        taskNamesJson = JSONArray.fromObject(taskNames);
        return SUCCESS;
    }

    /**
     * 修改报表任务
     * 
     * @return
     */
    public String modifyReportTask() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        ReportTask reportTask = reportTaskService.getReportTaskByTaskId(taskId);
        // 修改taskname、note、email
        reportTask.setTaskName(taskName);
        reportTask.setNote(note);
        reportTask.setEmailList(email);
        reportTaskService.modifyReportTask(reportTask);
        return NONE;
    }

    /**
     * 获取所有报表任务详情
     * 
     * @return
     * @throws IOException
     */
    public String getReportTaskByDetail() throws IOException {
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", state);
        map.put("start", start);
        map.put("limit", limit);
        map.put("userId", CurrentRequest.getCurrentUser().getUserId());
        List<ReportTask> ts = reportTaskService.getReportTaskList(map);
        Integer reportTaskNum = reportTaskService.getReportTaskListNum(map);
        // ReportUtils.formatDataString(ts);
        json.put("totalProperty", reportTaskNum);
        json.put("data", ts);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载报表任务列表.如果state为null，则表示取出所有，否则表示取暂停的(0)，启动的(1)
     * 
     * @return
     * @throws IOException
     */
    public String loadReportTaskList() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", state);
        map.put("userId", CurrentRequest.getCurrentUser().getUserId());
        List<ReportTask> reportTaskList = reportTaskService.getReportTaskList(map);
        JSONObject json = new JSONObject();
        json.put("data", reportTaskList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 从DB中删除一批任务报表，并移除任务，传递的是一批id的Stringlist 如果只传递一个taskId也需要是stringList
     * 
     * @return
     * @throws IOException
     */
    public String deleteReprotTask() throws IOException {
        List<Long> failedList = reportTaskService.deleteReportTask(Arrays.asList(taskIdList));
        JSONObject json = new JSONObject();
        json.put("failedList", failedList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 恢复一个被暂停的报表任务的执行
     * 
     * @return
     * @throws IOException
     */
    public String enableReportTask() throws IOException {
        List<Long> failedList = reportTaskService.enableReportTask(Arrays.asList(taskIdList));
        JSONObject json = new JSONObject();
        json.put("failedList", failedList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 暂停一个报表的执行，但是不删除它
     * 
     * @return
     * @throws IOException
     */
    public String disableReportTask() throws IOException {
        List<Long> failedList = reportTaskService.disableReportTask(Arrays.asList(taskIdList));
        JSONObject json = new JSONObject();
        json.put("failedList", failedList);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取某报表任务生成的报表详细列表
     * 
     * @return
     */
    public String loadReportDetailListByTaskId() {
        // TODO
        return NONE;
    }

    /**
     * 获取给定任务ID的报表任务
     * 
     * @return
     */
    public String getReportTaskByTaskId() {
        // TODO
        return NONE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getCycleType() {
        return cycleType;
    }

    public void setCycleType(int cycleType) {
        this.cycleType = cycleType;
    }

    public int getExcutorType() {
        return excutorType;
    }

    public void setExcutorType(int excutorType) {
        this.excutorType = excutorType;
    }

    public int[] getWeekDaySelected() {
        return weekDaySelected;
    }

    public void setWeekDaySelected(int[] weekDaySelected) {
        this.weekDaySelected = weekDaySelected;
    }

    public int getExcutorDay() {
        return excutorDay;
    }

    public void setExcutorDay(int excutorDay) {
        this.excutorDay = excutorDay;
    }

    public int getExcutorHour() {
        return excutorHour;
    }

    public void setExcutorHour(int excutorHour) {
        this.excutorHour = excutorHour;
    }

    public int getExcutorMin() {
        return excutorMin;
    }

    public void setExcutorMin(int excutorMin) {
        this.excutorMin = excutorMin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public String getConditions() {
        return conditions;
    }

    @SuppressWarnings("unchecked")
    public void setConditions(String conditions) {
        this.conditions = conditions;
        this.condition = JSONUtil.deserialize(conditions);
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long[] getTaskIdList() {
        return taskIdList;
    }

    public void setTaskIdList(Long[] taskIdList) {
        this.taskIdList = taskIdList;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getReportCreatorBeanName() {
        return reportCreatorBeanName;
    }

    public void setReportCreatorBeanName(String reportCreatorBeanName) {
        this.reportCreatorBeanName = reportCreatorBeanName;
    }

    public boolean isPdfSupport() {
        return pdfSupport;
    }

    public void setPdfSupport(boolean pdfSupport) {
        this.pdfSupport = pdfSupport;
    }

    public boolean isExcelSupport() {
        return excelSupport;
    }

    public void setExcelSupport(boolean excelSupport) {
        this.excelSupport = excelSupport;
    }

    public boolean isHtmlSupport() {
        return htmlSupport;
    }

    public void setHtmlSupport(boolean htmlSupport) {
        this.htmlSupport = htmlSupport;
    }

    public JSONObject getReportTaskJson() {
        return reportTaskJson;
    }

    public void setReportTaskJson(JSONObject reportTaskJson) {
        this.reportTaskJson = reportTaskJson;
    }

    public JSONObject getPolicyJson() {
        return policyJson;
    }

    public void setPolicyJson(JSONObject policyJson) {
        this.policyJson = policyJson;
    }

    public JSONArray getTaskNamesJson() {
        return taskNamesJson;
    }

    public void setTaskNamesJson(JSONArray taskNamesJson) {
        this.taskNamesJson = taskNamesJson;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Integer getWeekStartDay() {
        return weekStartDay;
    }

    public void setWeekStartDay(Integer weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public JSONArray getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(JSONArray queryCondition) {
        this.queryCondition = queryCondition;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getDetailReportInitCondition() {
        return detailReportInitCondition;
    }

    public void setDetailReportInitCondition(String detailReportInitCondition) {
        this.detailReportInitCondition = detailReportInitCondition;
    }

    public JSONObject getReportContent() {
        return reportContent;
    }

    public void setReportContent(JSONObject reportContent) {
        this.reportContent = reportContent;
    }

    public JSONArray getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(JSONArray allColumns) {
        this.allColumns = allColumns;
    }

}
