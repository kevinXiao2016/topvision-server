/***********************************************************************
 * $Id: ReportCoreService.java,v1.0 2014-6-18 上午9:11:45 $
 * 
 * @author:  Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.topvision.ems.report.domain.Report;
import com.topvision.ems.report.domain.ReportColumnReferences;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.report.core.domain.SelectModel;

/**
 * @author Rod John
 * @created @2014-6-18-上午9:11:45
 * 
 */
public interface ReportCoreService {

    /**
     * Fetch Report Info For URL In Report Navigation
     * 
     * @return
     */
    List<Report> loadAvailableReports();
    
    /**
     * Fetch Report Info For license apply
     * 
     * @return
     */
    List<Report> loadAllReports();

    /**
     * Fetch Report Info
     * 
     * contains titile columns groups and structure
     * 
     * 
     * @param reportId
     */
    Report fetchReportStructureInfo(String reportId);

    /**
     * 根据指定的reportId获取对应的查询条件结构
     * 
     * @param reportId
     * @return
     */
    JSONArray getQueryConditionById(String reportId);

    /**
     * 根据指定的reportId的所有列
     * 
     * @param reportId
     * @return
     */
    List<ReportColumnReferences> getReportColumns(String reportId);

    /**
     * 获取报表初始化内容
     * 
     * @param reportId
     * @return
     */
    JSONObject initReportContent(String reportId, Map<String, String> queryMap) throws Exception;

    /**
     * 查询对应报表在一定条件下的报表内容
     * 
     * @param reportId
     * @param queryMap
     * @return
     */
    JSONObject getReportContent(String reportId, Map<String, String> queryMap) throws Exception;

    /**
     * 生成报表并返回文件名
     * 
     * @param reportId
     * @param queryMap
     */
    String generateExcelFile(String reportId, Map<String, String> queryMap) throws Exception;

    /**
     * Download Excel File
     * 
     * @param fileName
     * @throws IOException
     */
    void downloadExcelFile(String fileName) throws IOException;

    /**
     * 生成任务报表
     * 
     * @param task
     */
    void generateTaskFile(ReportTask task) throws Exception;

    /**
     * 获取指定报表的指定查询条件的下拉内容
     * 
     * @param reportId
     * @param conditionId
     */
    List<SelectModel> fetchSelectConditionList(String reportId, String conditionId);

    /**
     * 获取指定报表
     * 
     * @param reportId
     * @return
     */
    Report fetchReportOrReloadReport(String reportId);
    
    /**
     * 获取指定报表,包括license不支持的报表
     * 
     * @param reportId
     * @return
     */
    Report fetchReportById(String reportId);

}
