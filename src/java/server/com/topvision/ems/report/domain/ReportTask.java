/**
 *
 */
package com.topvision.ems.report.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;

import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 */
public class ReportTask extends BaseEntity {
    private static final long serialVersionUID = 5399648908708415402L;
    private static final Logger logger = LoggerFactory.getLogger(ReportTask.class);
    /**
     * 以下2个字段用于构建Quatz的JobKey
     */
    public static final String REPORT_TASK = "reportTask-";
    public static final String REPORT_TASK_GROUP = "reportTask";

    private String reportId;
    private String reportName;
    private long taskId;
    /**
     * task的（上次）启动时间
     */
    private Timestamp startTime;
    /**
     * task的创建时间
     */
    private Timestamp createTime;

    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 报表模板类型ID
     */
    private long templateId;
    private String templateName;
    /**
     * 报表类型
     */
    private int cycleType;
    private String cycleTypeString;
    /**
     * 报表的备注
     */
    private String note;

    /**
     * 任务的限制条件，比如查询的设备，查询的地域，查询的时间等
     */
    private Map<String, Object> condition;

    /**
     * condition在数据库中的序列化
     */
    private byte[] conditionBlob;

    /**
     * 发送报表的的邮件地址列表。 TODO 配置的时候要检查邮件服务器是否有配置
     */
    private String[] emailList;
    private String email;

    /**
     * 导出类型excel,pdf,html
     */
    private boolean pdfEnabled;
    private boolean excelEnabled;
    private boolean htmlEnabled;

    /**
     * 执行时间用于前台展示
     */
    private String executorTimeString;

    /**
     * 统计时间段用于前台展示
     */
    private String statTimeString;

    /**
     * 任务执行时间的表达式，由 statStartTime 与 statEndTime 一起计算而来
     */
    private String cronExpression;
    /**
     * 执行报表构建任务的处理器
     */
    private String reportCreatorBeanName;

    /**
     * true表示启用, false表示停用
     */
    private boolean state;

    /**
     * 报表任务的用户信息
     */
    private Long userId;

    public ReportTask() {
        condition = new HashMap<String, Object>();
    }

    public Object getCondition(String condition) {
        return this.condition.get(condition);
    }

    /**
     * 得到可显示的列
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getDisplayColumns() {
        if (condition != null) {
            String conditionString = (String) condition.get("displayColumns");
            if (conditionString != null) {
                return Arrays.asList(conditionString.split(","));
            }
        }
        return null;
    }

    /**
     * 判断是否包含某列
     * 
     * @param column
     * @return
     */
    public boolean containsColumn(String column) {
        if (condition != null) {
            String conditionString = (String) condition.get("displayColumns");
            if (conditionString != null) {
                return Arrays.asList(conditionString.split(",")).contains(column);
            }
        }
        return false;
    }

    /**
     * 得到用户签名
     * 
     * @return
     */
    public String getAuthor() {
        if (condition != null) {
            return (String) condition.get("author");
        }
        return null;
    }

    /**
     * 得到报表标题
     * 
     * @return
     */
    public String getTitle() {
        if (condition != null) {
            return (String) condition.get("title");
        }
        return null;
    }

    /**
     * 得到排序的列
     * 
     * @return
     */
    public String getSortInfo() {
        if (condition != null) {
            return (String) condition.get("sortInfo");
        }
        return null;
    }

    /**
     * @return the taskId
     */
    public long getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     *            the taskId to set
     */
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    /**
     * @return the startTime
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName
     *            the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the templateId
     */
    public long getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId
     *            the templateId to set
     */
    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the cycleType
     */
    public int getCycleType() {
        return cycleType;
    }

    /**
     * @param cycleType
     *            the cycleType to set
     */
    public void setCycleType(int cycleType) {
        this.cycleType = cycleType;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the pdfEnabled
     */
    public boolean isPdfEnabled() {
        return pdfEnabled;
    }

    /**
     * @param pdfEnabled
     *            the pdfEnabled to set
     */
    public void setPdfEnabled(boolean pdfEnabled) {
        this.pdfEnabled = pdfEnabled;
    }

    /**
     * @return the excelEnabled
     */
    public boolean isExcelEnabled() {
        return excelEnabled;
    }

    /**
     * @param excelEnabled
     *            the excelEnabled to set
     */
    public void setExcelEnabled(boolean excelEnabled) {
        this.excelEnabled = excelEnabled;
    }

    /**
     * @return the htmlEnabled
     */
    public boolean isHtmlEnabled() {
        return htmlEnabled;
    }

    /**
     * @param htmlEnabled
     *            the htmlEnabled to set
     */
    public void setHtmlEnabled(boolean htmlEnabled) {
        this.htmlEnabled = htmlEnabled;
    }

    /**
     * @return the cronExpression
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * @param cronExpression
     *            the cronExpression to set
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * @return the state
     */
    public boolean isState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * @return the condition
     */
    public Map<String, Object> getCondition() {
        return condition;
    }

    /**
     * @param condition
     *            the condition to set
     */
    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream ois = null;
        try {
            bos = new ByteArrayOutputStream();
            ois = new ObjectOutputStream(bos);
            ois.writeObject(this.condition);
            this.conditionBlob = bos.toByteArray();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(bos);
            FileUtils.closeQuitely(ois);
        }
    }

    /**
     * 添加一个条件
     * 
     * @param condition
     * @param value
     */
    public void addCondition(String condition, Object value) {
        this.condition.put(condition, value);
    }

    /**
     * 删除一个条件
     * 
     * @param condition
     */
    public void removeCondition(String condition) {
        this.condition.remove(condition);
    }

    /**
     * @return the reportCreatorBeanName
     */
    public String getReportCreatorBeanName() {
        return reportCreatorBeanName;
    }

    /**
     * @param reportCreatorBeanName
     *            the reportCreatorBeanName to set
     */
    public void setReportCreatorBeanName(String reportCreatorBeanName) {
        this.reportCreatorBeanName = reportCreatorBeanName;
    }

    /**
     * @return the conditionBlob
     */
    public byte[] getConditionBlob() {
        return conditionBlob;
    }

    /**
     * @param conditionBlob
     *            the conditionBlob to set
     */
    @SuppressWarnings("unchecked")
    public void setConditionBlob(byte[] conditionBlob) {
        this.conditionBlob = conditionBlob;
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new SerialBlob(conditionBlob).getBinaryStream());
            this.condition = (Map<String, Object>) is.readObject();
        } catch (IOException e) {
            logger.error("", e);
        } catch (SQLException e) {
            logger.error("", e);
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(is);
        }
    }

    /**
     * @return the emailList
     */
    public String getEmail() {
        if (emailList != null) {
            StringBuilder sb = new StringBuilder();
            for (String email_ : emailList) {
                sb.append(",").append(email_);
            }
            this.email = sb.substring(1);
        }
        return email;
    }

    /**
     * @param emailList
     *            the emailList to set
     */
    public void setEmail(String email) {
        this.email = email;
        if (emailList != null) {
            this.emailList = email.split(",");
        }
    }

    /**
     * @return the emailList
     */
    public String[] getEmailList() {
        return emailList;
    }

    /**
     * @param emailList
     *            the emailList to set
     */
    public void setEmailList(String[] emailList) {
        this.emailList = emailList;
    }

    /**
     * 获取Quatz的jobKey
     * 
     * @return
     */
    public JobKey getJobKey() {
        return getJobKey(taskId);
    }

    public TriggerKey getTriggerKey() {
        return new TriggerKey(REPORT_TASK + taskId, REPORT_TASK_GROUP);
    }

    /**
     * 获取Quatz的jobKey
     * 
     * @return
     */
    public static JobKey getJobKey(Long taskId_) {
        return new JobKey(REPORT_TASK + taskId_, REPORT_TASK_GROUP);
    }

    public String getExecutorTimeString() {
        return executorTimeString;
    }

    public void setExecutorTimeString(String executorTimeString) {
        this.executorTimeString = executorTimeString;
    }

    public String getStatTimeString() {
        return statTimeString;
    }

    public void setStatTimeString(String statTimeString) {
        this.statTimeString = statTimeString;
    }

    public String getCycleTypeString() {
        cycleTypeString = "";
        switch (cycleType) {
        case 0:
            cycleTypeString = ReportTaskUtil.getString("report.daily", "report");
            break;
        case 1:
            cycleTypeString = ReportTaskUtil.getString("report.week7", "report");
            break;
        case 2:
            cycleTypeString = ReportTaskUtil.getString("report.month", "report");
            break;
        default:
            break;
        }
        return cycleTypeString;
    }

    public void setCycleTypeString(String cycleTypeString) {
        this.cycleTypeString = cycleTypeString;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ReportTask [taskId=" + taskId + ", startTime=" + startTime + ", createTime=" + createTime
                + ", taskName=" + taskName + ", templateId=" + templateId + ", cycleType=" + cycleType
                + ", cycleTypeString=" + cycleTypeString + ", note=" + note + ", condition=" + condition
                + ", conditionBlob=" + java.util.Arrays.toString(conditionBlob) + ", emailList="
                + java.util.Arrays.toString(emailList) + ", email=" + email + ", pdfEnabled=" + pdfEnabled
                + ", excelEnabled=" + excelEnabled + ", htmlEnabled=" + htmlEnabled + ", executorTimeString="
                + executorTimeString + ", statTimeString=" + statTimeString + ", cronExpression=" + cronExpression
                + ", reportCreatorBeanName=" + reportCreatorBeanName + ", state=" + state + ", userId=" + userId + "]";
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = ReportTaskUtil.getString(templateName, "report");
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

}
