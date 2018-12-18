package com.topvision.ems.report.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.topvision.framework.domain.BaseEntity;

/**
 * 
 * @author Bravin
 * @created @2013-6-20-下午5:32:53
 * 
 */
public class ReportInstance extends BaseEntity {
    private static final long serialVersionUID = 5337564661968400302L;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long instanceId;
    private String instanceTitle;
    private String reportId;
    private String note;
    private String reportName;
    private Integer fileType;
    private String fileTypeString;
    private String filePath;
    private Long taskId;
    private String taskName;
    private Date createTime;
    private String createTimeString;
    private Long userId;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getFileType() {
        return fileType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreateTimeString() {
        createTimeString = "";
        if (this.createTime != null) {
            createTimeString = formatter.format(this.createTime);
        }
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    public String getFileTypeString() {
        fileTypeString = "";
        if (this.fileType != null) {
            switch (this.fileType) {
            case 0:
                fileTypeString = "EXCEL";
                break;
            case 1:
                fileTypeString = "PDF";
                break;
            case 2:
                fileTypeString = "HTML";
                break;
            default:
                break;
            }
        }
        return fileTypeString;
    }

    public void setFileTypeString(String fileTypeString) {
        this.fileTypeString = fileTypeString;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReportInstance [reportId=");
        builder.append(reportId);
        builder.append(", note=");
        builder.append(note);
        builder.append(", reportName=");
        builder.append(reportName);
        builder.append(", fileType=");
        builder.append(fileType);
        builder.append(", fileTypeString=");
        builder.append(fileTypeString);
        builder.append(", filePath=");
        builder.append(filePath);
        builder.append(", taskId=");
        builder.append(taskId);
        builder.append(", taskName=");
        builder.append(taskName);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", createTimeString=");
        builder.append(createTimeString);
        builder.append(", userId=");
        builder.append(userId);
        builder.append("]");
        return builder.toString();
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceTitle() {
        return instanceTitle;
    }

    public void setInstanceTitle(String instanceTitle) {
        this.instanceTitle = instanceTitle;
    }

}
