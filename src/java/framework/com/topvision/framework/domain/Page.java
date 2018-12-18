/*
 * @(#)Page.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.domain;

/**
 * 分页的封装.
 * 
 * @author niejun
 * @version 1.0, 2007-08-23
 */
public class Page extends BaseEntity {
    private static final long serialVersionUID = 4288119671497399515L;
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    public static int getGroupPages() {
        return 10;
    }

    // 目标显示页码
    private int page = 1;

    // 每页显示的记录数
    private int pageSize = 25;

    // 记录总数
    private int rowCount = 0;

    // 当前显示页码
    private int currPage = 1;

    // 当前显示组数
    private int currGroup = 1;

    // 目标显示组数
    private int group = 1;

    private int startPosition = 1;

    private int endPosition = 1;

    private String sortName = null;

    private String sortDirection = null;

    private String groupBy = null;

    /**
     * 获取当前组
     * 
     * @return
     */
    public int getCurrGroup() {
        return this.currGroup;
    }

    /**
     * 获取当前页
     * 
     * @return
     */
    public int getCurrPage() {
        return currPage;
    }

    public int getEndPosition() {
        return endPosition;
    }

    /**
     * 获取目标组
     * 
     * @return
     */
    public int getGroup() {
        return this.group;
    }

    public String getGroupBy() {
        return groupBy;
    }

    /**
     * 获取每组分页行数
     * 
     * @return
     */
    public int getGroupPageSize() {
        return this.pageSize * getGroupPages();
    }

    /**
     * 获取目标显示页码
     * 
     * @return
     */
    public int getPage() {
        return page;
    }

    /**
     * 获取分页行数
     * 
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 获取数据总行数
     * 
     * @return
     */
    public int getRowCount() {
        return rowCount;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public String getSortName() {
        return sortName;
    }

    public int getStartPosition() {
        return startPosition;
    }

    /**
     * 
     * @param currGroup
     */
    public void setCurrGroup(int currGroup) {
        this.currGroup = currGroup;
    }

    /**
     * 
     * @param currPage
     */
    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * 
     * @param group
     */
    public void setGroup(int group) {
        this.group = group;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 
     * @param rowCount
     */
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    /**
     * 设置排序方向, ASC|DESC.
     * 
     * @param sortDirection
     */
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * 设置排序列名称.
     * 
     * @param sortName
     */
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }
}
