/***********************************************************************
 * $Id: ReportInstanceAction.java,v1.0 2013-6-20 上午9:05:25 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.report.domain.ReportInstance;
import com.topvision.ems.report.service.ReportInstanceService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.FtpClientUtil;

import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2013-6-20-上午9:05:25
 * @modify by fanzidong 采用注解
 */
@Controller("reportInstanceAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReportInstanceAction extends BaseAction {
    private static final long serialVersionUID = -8584558812224067138L;
    private final Logger logger = LoggerFactory.getLogger(ReportInstanceAction.class);
    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    
    @Autowired
    private ReportInstanceService reportInstanceService;
    private Long instanceId;
    private Long taskId;
    private String reportId;
    private Long[] instanceIds;
    private String[] instancePaths;
    private int start;
    private int limit;

    /**
     * 显示报表实例列表
     * 
     * @return
     * @throws IOException
     */
    public String showAllReport() {
        return SUCCESS;
    }

    /**
     * 加载有任务的报表
     * 
     * @return
     * @throws IOException
     */
    public String loadReportWithTask() throws IOException {
        JSONObject reportJson = reportInstanceService.loadReportWithTask();
        reportJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取任务生成报表实例列表
     * 
     * @return
     * @throws IOException
     */
    public String getReportInstanceList() throws IOException {
        JSONObject json = new JSONObject();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (taskId != null && !taskId.equals(0L)) {
            queryMap.put("taskId", taskId);
        }
        if (reportId != null && !reportId.equals("0") && !reportId.equals("")) {
            queryMap.put("reportId", reportId);
        }
        queryMap.put("start", start);
        queryMap.put("limit", limit);
        List<ReportInstance> rs = reportInstanceService.getReportInstanceList(queryMap);
        Integer reportNum = reportInstanceService.getReportInstanceNum(queryMap);
        json.put("data", rs);
        json.put("rowCount", reportNum);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取该任务文件是否存在
     * 
     * @return
     * @throws IOException
     */
    public String isFileExist() throws IOException {
        JSONObject json = new JSONObject();
        ReportInstance reportInstance = reportInstanceService.getReportDetailByReportId(instanceId);
        String filePath = reportInstance.getFilePath();
        File file = new File(filePath);
        json.put("fileExist", file.exists()); // 向页面传值
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 下载单个报表
     * 
     * @return
     */
    public String downloadReport() {
        // 获取文件的储存路径
        ReportInstance reportInstance = reportInstanceService.getReportDetailByReportId(instanceId);
        String filePath = reportInstance.getFilePath();
        String taskName = reportInstance.getTaskName();
        String fileName = taskName + "-" +fileNameFormatter.format(reportInstance.getCreateTime()) + ".xls";
        
        // 根据filePath获取文件名
        File file = new File(filePath);
        FileInputStream fis = null;
        OutputStream out = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            ServletActionContext.getResponse().setContentType("application/x-download");
            // 转码文件名，解决中文名的问题
            fileName = new String(fileName.getBytes(FtpClientUtil.GBK), FtpClientUtil.ISO);
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            int i;
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
            logger.debug("", e);
            return "false";
        } finally {
            // 关闭流,并删除服务器端临时文件
            try {
                if (out != null)
                    out.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                logger.debug("", e);
            }
        }
        return NONE;
    }

    /**
     * 批量删除报表
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String deleteReport() {
        reportInstanceService.deleteReport(Arrays.asList(instanceIds));
        return NONE;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long[] getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(Long[] instanceIds) {
        this.instanceIds = instanceIds;
    }

    public String[] getInstancePaths() {
        return instancePaths;
    }

    public void setInstancePaths(String[] instancePaths) {
        this.instancePaths = instancePaths;
    }

}
