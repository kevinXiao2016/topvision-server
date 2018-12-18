/***********************************************************************
 * $Id: ReportColumnReferences.java,v1.0 2014-6-14 下午2:04:21 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

/**
 * @author Rod John
 * @created @2014-6-14-下午2:04:21
 * 
 */
public class ReportColumnReferences {

    public static final String SHOWCOLUMNS = "showColumns";

    private String id;
    private String name;
    private Integer width;
    private Integer excelwidth;
    private Integer level;
    private String linkId;
    private String linkName;
    private ReportGroup group;
    private Boolean needI18N;
    private Boolean nowrap;
    private String combination;
    private String color;
    private String render;

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
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the group
     */
    public ReportGroup getGroup() {
        return group;
    }

    /**
     * @param group
     *            the group to set
     */
    public void setGroup(ReportGroup group) {
        this.group = group;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public Boolean getNeedI18N() {
        return needI18N;
    }

    public void setNeedI18N(Boolean needI18N) {
        this.needI18N = needI18N;
    }

    public Boolean getNowrap() {
        return nowrap;
    }

    public void setNowrap(Boolean nowrap) {
        this.nowrap = nowrap;
    }

    /**
     * @return the combination
     */
    public String getCombination() {
        return combination;
    }

    /**
     * @param combination
     *            the combination to set
     */
    public void setCombination(String combination) {
        this.combination = combination;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getExcelwidth() {
        return excelwidth;
    }

    public void setExcelwidth(Integer excelwidth) {
        this.excelwidth = excelwidth;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

}
