/***********************************************************************
 * $Id: ReportTemplateAction.java,v1.0 2013-5-23 上午10:52:30 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.report.domain.ReportTemplate;
import com.topvision.ems.report.service.ReportTemplateService;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * 所有的报表模板的action层处理在此
 * 
 * @author Bravin
 * @created @2013-5-23-上午10:52:30
 * 
 */
@Controller("reportTemplateAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReportTemplateAction extends BaseAction {
    private static final long serialVersionUID = -960957436669261014L;
    private static Logger logger = LoggerFactory.getLogger(ReportTemplateAction.class);
    @Autowired
    private ReportTemplateService reportTemplateService;
    @SuppressWarnings("unused")
    private static final String BASE_NODE_PATH = "/images/report/";
    private String node;
    private String reportDisplay;

    /**
     * 加载所有的报表模板
     * 
     * @return
     */
    public String loadAllReportTemplate() {
        try {
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            Map<String, Boolean> hasSupportMap = uc.getSupportModules();
            JSONArray array = reportTemplateService.loadAllReportTemplate(hasSupportMap);
            writeDataToAjax(array);
        } catch (Exception ex) {
            logger.error("Load Report Template:", ex);
        }
        return NONE;
    }

    public String loadDisplayReportTemplate() {
        try {
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            Map<String, Boolean> hasSupportMap = uc.getSupportModules();
            JSONArray array = reportTemplateService.loadDisplayReportTemplate(hasSupportMap);
            writeDataToAjax(array);
        } catch (Exception ex) {
            logger.error("Load Report Template:", ex);
        }
        return NONE;
    }

    /**
     * 加载可以报表定制的模板
     * 
     * @return
     * @throws Exception
     */
    public String loadReportTaskTemplate() throws Exception {
        try {
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            Map<String, Boolean> hasSupportMap = uc.getSupportModules();
            JSONArray array = reportTemplateService.getTaskableReportTemplate(hasSupportMap);
            writeDataToAjax(array);
        } catch (Exception ex) {
            logger.error("Load Report Template:", ex);
        }
        return NONE;
    }

    /**
     * 加载任务树数据
     * 
     * @return
     */
    public String loadReportTaskTreeData() {
        try {
            JSONArray array = reportTemplateService.loadReportTaskTreeData();
            writeDataToAjax(array);
        } catch (Exception e) {
            logger.error("loadReportTaskTreeData:", e);
        }
        return NONE;
    }

    public String updateReportDisplay() {
        net.sf.json.JSONArray displayArray = net.sf.json.JSONArray.fromObject(reportDisplay);
        List<ReportTemplate> reportTemplates = new ArrayList<ReportTemplate>();
        for (int i = 0; i < displayArray.size(); i++) {
            net.sf.json.JSONObject reportDisplay = displayArray.getJSONObject(i);
            ReportTemplate reportTemplate = new ReportTemplate();
            reportTemplate.setTemplateId(reportDisplay.getLong("id"));
            reportTemplate.setDisplay(reportDisplay.getBoolean("display"));
            reportTemplates.add(reportTemplate);
        }
        // 进行批量修改
        reportTemplateService.updateReportDisplay(reportTemplates);
        return NONE;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getReportDisplay() {
        return reportDisplay;
    }

    public void setReportDisplay(String reportDisplay) {
        this.reportDisplay = reportDisplay;
    }

}
