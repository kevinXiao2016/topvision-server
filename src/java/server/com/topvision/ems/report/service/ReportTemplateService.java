/***********************************************************************
 * $Id: ReportTemplateService.java,v1.0 2013-6-19 下午3:50:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.topvision.ems.report.domain.ReportTemplate;

/**
 * @author Bravin
 * @created @2013-6-19-下午3:50:00
 * 
 */
public interface ReportTemplateService {

    /**
     * 加载所有的报表模板
     * 
     * @return
     */
    JSONArray loadAllReportTemplate(Map<String, Boolean> hasSupportMap);

    /**
     * 加载可显示的报表模板
     * 
     * @return
     */
    JSONArray loadDisplayReportTemplate(Map<String, Boolean> hasSupportMap);

    /**
     * 加载可以定制的报表模版
     * 
     * @return
     */
    JSONArray getTaskableReportTemplate(Map<String, Boolean> hasSupportMap);

    /**
     * 加载任务树数据
     * 
     * @return
     * @throws JSONException
     */
    JSONArray loadReportTaskTreeData();

    /**
     * 更新报表模版的显示
     * 
     * @param reportTemplates
     */
    void updateReportDisplay(List<ReportTemplate> reportTemplates);

}
