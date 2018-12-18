/***********************************************************************
 * $Id: ReportCoreAction.java,v1.0 2014-6-18 上午11:57:14 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.report.domain.Report;
import com.topvision.ems.report.domain.ReportColumnReferences;
import com.topvision.ems.report.service.ReportCoreService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.report.core.domain.SelectModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author vanzand
 * @created @2014-6-18-上午11:57:14
 * 
 */
@Controller("reportCoreAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReportCoreAction extends BaseAction {
    private static final long serialVersionUID = 4128368417963028735L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String reportId;
    @Autowired
    private ReportCoreService reportCoreService;

    private Long folderId;
    // 详细报表初始化查询条件
    private String detailReportInitCondition;
    private String fileName;
    private String conditionId;

    /**
     * 加载可用的报表
     * 
     * @return
     * @throws IOException
     */
    public String loadAvailableReports() throws IOException {
        List<Report> availableReports = reportCoreService.loadAvailableReports();
        JSONArray reportArray = new JSONArray();
        JSONObject json;
        for(Report report : availableReports){
            json = new JSONObject();
            json.put("title", report.getTitle());
            json.put("path", report.getPath());
            json.put("type", report.getType());
            json.put("id", report.getId());
            reportArray.add(json);
        }
        writeDataToAjax(reportArray);
        return NONE;
    }

    /**
     * 打开指定报表页面
     * 
     * @return
     * @throws Exception
     */
    public String showSingleReport() throws Exception {
        return SUCCESS;
    }

    public void fetchConditionAndColumn() throws IOException {
        JSONObject ret = new JSONObject();
        JSONArray queryCondition = reportCoreService.getQueryConditionById(reportId);
        List<ReportColumnReferences> columnReferences = reportCoreService.getReportColumns(reportId);
        JSONArray allColumns = JSONArray.fromObject(columnReferences);
        ret.put("queryCondition", queryCondition);
        ret.put("allColumns", allColumns);
        writeDataToAjax(ret);
    }

    public void initContent() throws Exception {
        Map<String, String> queryMap = packageQueryMap();
        // 判断该报表是否需要限制前端限制行数
        Report report = reportCoreService.fetchReportOrReloadReport(reportId);
        if (report.getPagination() != null && report.getPagination() > 0) {
            queryMap.put("start", "0");
            queryMap.put("limit", report.getPagination().toString());
        }
        JSONObject reportContent = reportCoreService.initReportContent(reportId, queryMap);
        writeDataToAjax(reportContent);
    }

    /**
     * 获取指定报表指定查询条件下的报表内容
     * 
     * @throws Exception
     */
    public void loadReportContent() throws Exception {
        Map<String, String> queryMap = packageQueryMap();
        // 判断该报表是否需要限制前端限制行数
        Report report = reportCoreService.fetchReportOrReloadReport(reportId);
        if (report.getPagination() != null && report.getPagination() > 0) {
            queryMap.put("start", "0");
            queryMap.put("limit", report.getPagination().toString());
        }
        // 查询数据
        JSONObject reportContent = reportCoreService.getReportContent(reportId, queryMap);
        // 返回数据
        writeDataToAjax(reportContent);
    }

    /**
     * 包装查询条件
     * 
     * @return
     */
    private Map<String, String> packageQueryMap() {
        Map<String, String> queryMap = new HashMap<String, String>();
        // 在action层中将当前用户权限信息放入查询条件中
        queryMap.put("userId", String.valueOf(CurrentRequest.getCurrentUser().getUserId()));
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        queryMap.put("topoAuthority", CurrentRequest.getUserAuthorityFolderName());
        // 填入查询条件
        if (detailReportInitCondition != null && detailReportInitCondition != "") {
            JSONObject queryObj = JSONObject.fromObject(detailReportInitCondition);
            Iterator<?> it = queryObj.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                queryMap.put(key, queryObj.getString(key));
            }
        }
        return queryMap;
    }

    /**
     * 获取下拉菜单查询条件的内容
     * 
     * @throws IOException
     */
    public void fetchSelectConditionList() throws IOException {
        List<SelectModel> selectModels = reportCoreService.fetchSelectConditionList(reportId, conditionId);
        writeDataToAjax(JSONArray.fromObject(selectModels));
    }

    /**
     * 生成EXCEL文件
     * 
     * @return
     * @throws Exception
     */
    public String generateExcelFile() throws Exception {
        Map<String, String> queryMap = packageQueryMap();
        JSONObject resultObj = new JSONObject();
        // 生成excel文件并返回文件路径
        String fileName;
        try {
            fileName = reportCoreService.generateExcelFile(reportId, queryMap);
            resultObj.put("fileName", fileName);
        } catch (Exception e) {
            resultObj.put("error", e.getMessage());
        }
        writeDataToAjax(resultObj);
        return NONE;
    }

    /**
     * 下载生成的EXCEL文件
     * 
     * @return
     * @throws IOException
     */
    public String downloadGenerateFile() throws IOException {
        reportCoreService.downloadExcelFile(fileName);
        return NONE;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getDetailReportInitCondition() {
        return detailReportInitCondition;
    }

    public void setDetailReportInitCondition(String detailReportInitCondition) {
        this.detailReportInitCondition = detailReportInitCondition;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

}
