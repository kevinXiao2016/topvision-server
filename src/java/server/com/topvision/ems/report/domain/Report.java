/***********************************************************************
 * $Id: Report.java,v1.0 2014-6-17 下午4:35:11 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.domain.TreeEntity;
import com.topvision.platform.ResourceManager;

/**
 * @author Rod John
 * @created @2014-6-17-下午4:35:11
 * 
 */
public class Report implements TreeEntity {
    private Logger logger = LoggerFactory.getLogger(Report.class);
    public static final String ALL_MODULE = "all";
    public static final String ALL_PROJECT = "all";
    private static final String SERVER_RESOURCE_PATH = "com.topvision.ems.report.resources";

    private String id;
    private String title;
    private String lazyLoad;
    private String path;
    private String type;
    private Integer pagination;
    private boolean combination = false;
    private boolean topLevel = false;
    private List<ReportColumnReferences> columnReferences = new ArrayList<ReportColumnReferences>();
    private ReportStructure reportStructure = new ReportStructure();
    private List<ReportCondition> reportConditions;
    private String linkReportId;
    private Report linkReport;
    private String module;
    private String project = ALL_PROJECT;
    private List<String> resourceModule = new ArrayList<>();

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
        resourceModule.add(String.format("com.topvision.report.%s.resources", this.getId().toLowerCase()));
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = globalization(title);
    }

    /**
     * @return the columnReferences
     */
    public List<ReportColumnReferences> getColumnReferences() {
        return columnReferences;
    }

    /**
     * @param columnReferences
     *            the columnReferences to set
     */
    public void setColumnReferences(List<ReportColumnReferences> columnReferences) {
        for (ReportColumnReferences tmp : columnReferences) {
            tmp.setName(globalization(tmp.getName()));
        }
        this.columnReferences = columnReferences;
    }

    public void addColumnReference(ReportColumnReferences columnReferences) {
        columnReferences.setName(globalization(columnReferences.getName()));
        columnReferences.setCombination(globalization(columnReferences.getCombination()));
        columnReferences.setLinkName(globalization(columnReferences.getLinkName()));
        this.columnReferences.add(columnReferences);
    }

    /**
     * @return the reportStructure
     */
    public ReportStructure getReportStructure() {
        return reportStructure;
    }

    /**
     * @param reportStructure
     *            the reportStructure to set
     */
    public void setReportStructure(ReportStructure reportStructure) {
        this.reportStructure = reportStructure;
    }

    /**
     * @return the reportCondition
     */
    public List<ReportCondition> getReportConditions() {
        return reportConditions;
    }

    /**
     * @param reportCondition
     *            the reportCondition to set
     */
    public void setReportConditions(List<ReportCondition> reportConditions) {
        if (reportConditions != null) {
            for (ReportCondition condition : reportConditions) {
                condition.setLabelName(globalization(condition.getLabelName()));
                condition.setPlaceHolder(globalization(condition.getPlaceHolder()));
            }
        }
        this.reportConditions = reportConditions;
    }

    /**
     * @return the lazyLoad
     */
    public String getLazyLoad() {
        return lazyLoad;
    }

    /**
     * @param lazyLoad
     *            the lazyLoad to set
     */
    public void setLazyLoad(String lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        // path中的第一个/前面的是type
        this.type = path.split("/")[0];
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the linkReport
     */
    public Report getLinkReport() {
        return linkReport;
    }

    /**
     * @param linkReport
     *            the linkReport to set
     */
    public void setLinkReport(Report linkReport) {
        this.linkReport = linkReport;
    }

    /**
     * @return the linkReportId
     */
    public String getLinkReportId() {
        return linkReportId;
    }

    /**
     * @param linkReportId
     *            the linkReportId to set
     */
    public void setLinkReportId(String linkReportId) {
        this.linkReportId = linkReportId;
    }

    /**
     * @return the topLevel
     */
    public boolean isTopLevel() {
        return topLevel;
    }

    /**
     * @param topLevel
     *            the topLevel to set
     */
    public void setTopLevel(boolean topLevel) {
        this.topLevel = topLevel;
    }

    /**
     * @return the combination
     */
    public boolean isCombination() {
        return combination;
    }

    /**
     * @param combination
     *            the combination to set
     */
    public void setCombination(boolean combination) {
        this.combination = combination;
    }

    public void addWithPackages(String withPackage) {
        resourceModule.add(withPackage.toLowerCase());
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    // 国际化方法
    public String getString(String key) {
        String result = null;
        for (String resourceTmp : resourceModule) {
            try {
                while ((result = getString(resourceTmp, key)) != null) {
                    return result;
                }
            } catch (Exception e) {
                logger.error("Report get resource key error : ", e);
            }
        }
        if (result == null) {
            return ResourceManager.getResourceManager(SERVER_RESOURCE_PATH).getNotNullString(key);
        }
        return key;
    }

    private String getString(String resourceModule, String key) {
        ResourceManager resourceManager = ResourceManager.getResourceManager(resourceModule);
        return resourceManager.getNullString(key);
    }

    public String globalization(String context) {
        if (context == null) {
            return context;
        }
        int wildcardStartIndex = context.indexOf("${");
        while (wildcardStartIndex != -1) {
            int wildcardEndIndex = wildcardStartIndex + context.substring(wildcardStartIndex).indexOf("}");
            String wildString = context.substring(wildcardStartIndex + 2, wildcardEndIndex);
            context = context.replace("${" + wildString + "}", getString(wildString));
            wildcardStartIndex = context.indexOf("${");
            if (wildcardStartIndex == -1) {
                break;
            }
        }
        return context;
    }

    public Integer getPagination() {
        return pagination;
    }

    public void setPagination(Integer pagination) {
        this.pagination = pagination;
    }

    @Override
    public String getParentId() {
        return path.equals(id) ? null : path;
    }

    @Override
    public String getText() {
        return title;
    }

}
