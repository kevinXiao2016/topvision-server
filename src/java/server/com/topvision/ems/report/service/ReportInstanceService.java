/***********************************************************************
 * $Id: ReportInstanceService.java,v1.0 2013-6-18 下午5:34:28 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.topvision.ems.report.domain.ReportInstance;
import com.topvision.ems.report.domain.ReportTask;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013-6-18-下午5:34:28
 * 
 */
public interface ReportInstanceService extends Service {

    /**
     * 获取报表详细
     * 
     * @param reportId
     * @return
     */
    ReportInstance getReportDetailByReportId(Long reportId);

    /**
     * 删除报表任务生成的报表.
     * 
     * @param reportIds
     */
    void deleteReport(List<Long> reportIds);

    /**
     * 添加一个报表结果
     * 
     * @param filePath
     * @param fileType
     * @param task
     */
    void addReportInstance(String filePath, int fileType, ReportTask task);

    /**
     * 加载拥有任务的报表
     * 
     * @return
     */
    JSONObject loadReportWithTask();

    /**
     * 加载生成报表实例列表
     * 
     * @param queryMap
     * @return
     */
    List<ReportInstance> getReportInstanceList(Map<String, Object> queryMap);

    /**
     * 获取实例数目
     * 
     * @param queryMap
     * @return
     */
    Integer getReportInstanceNum(Map<String, Object> queryMap);

}
