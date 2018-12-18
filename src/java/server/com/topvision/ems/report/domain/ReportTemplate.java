package com.topvision.ems.report.domain;

import java.util.List;

import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 */
public class ReportTemplate extends BaseEntity {
    private static final long serialVersionUID = 2816150080424194687L;
    private long templateId;
    private long superiorId;
    private String name;
    private String displayName;
    private String note;
    private String icon16;
    private String icon48;
    private String path;
    private String url;
    private boolean taskable;
    private boolean display;

    private List<ReportTask> rsList;

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon16() {
        return icon16;
    }

    public String getIcon48() {
        return icon48;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getPath() {
        return path;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public long getTemplateId() {
        return templateId;
    }

    public String getUrl() {
        return url;
    }

    public boolean isTaskable() {
        return taskable;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIcon16(String icon16) {
        this.icon16 = icon16;
    }

    public void setIcon48(String icon48) {
        this.icon48 = icon48;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    public void setTaskable(boolean taskable) {
        this.taskable = taskable;
    }

    public void setTemplateId(long categoryId) {
        this.templateId = categoryId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ReportTask> getRsList() {
        return rsList;
    }

    public void setRsList(List<ReportTask> rsList) {
        this.rsList = rsList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReportTemplate [templateId=");
        builder.append(templateId);
        builder.append(", superiorId=");
        builder.append(superiorId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", note=");
        builder.append(note);
        builder.append(", icon16=");
        builder.append(icon16);
        builder.append(", icon48=");
        builder.append(icon48);
        builder.append(", path=");
        builder.append(path);
        builder.append(", url=");
        builder.append(url);
        builder.append(", taskable=");
        builder.append(taskable);
        builder.append(", rsList=");
        builder.append(rsList);
        builder.append("]");
        return builder.toString();
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

}