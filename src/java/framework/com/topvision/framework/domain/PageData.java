/*
 * @(#)PageData.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类主要对记录结果集进行分页处理，并获取分页中的记录位置。
 * 
 * @author niejun
 * @version 1.0, 2007-08-23
 */
public class PageData<T> extends BaseEntity {
    private static final long serialVersionUID = -6846229826227916287L;

    // 每页显示的记录数
    protected int pageSize = 20;

    // 记录总数
    protected int rowCount = 0;

    // 总页数
    protected int pageCount = 0;

    // 当前显示页码
    protected int currPage = 1;

    // 开始的位置
    protected int startPosition = 0;

    // 结束的位置
    protected int endPosition = 0;

    // 数据列表
    protected List<T> data;

    // 传递附加对象
    protected Object appendix;

    // 当前显示组码
    protected int currGroup = 1;

    protected int oldGroup = 1;

    // 分组显示的组页数
    protected int groupPage = 0;

    /**
     * 保护构造函数
     */
    public PageData() {
        data = new ArrayList<T>();
    }

    /**
     * 默认构造函数
     * 
     * @param currPage
     *            当前页数
     * @param pageSize
     *            每页显示的记录数
     * @param rowCount
     *            记录总数
     * @param list
     *            数据列表结果集
     */
    public PageData(int currPage, int pageSize, int rowCount, List<T> list) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        this.rowCount = rowCount;
        this.data = list;
        init();
    }

    /**
     * 构造函数
     * 
     * @param currPage
     *            当前页数
     * @param pageSize
     *            每页显示的记录数
     * @param list
     *            数据列表结果集
     */
    public PageData(int currPage, int pageSize, List<T> list) {
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.data = setData(list);
    }

    /**
     * 
     * 默认构造函数
     * 
     * @param currPage
     *            当前页数
     * @param pageSize
     *            每页显示的记录数
     * @param tempList
     * @param groupPage
     * @param oldGroup
     * @param currGroup
     */
    public PageData(int currPage, int pageSize, List<T> tempList, int groupPage, int oldGroup, int currGroup) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        this.oldGroup = oldGroup;
        this.currGroup = currGroup;
        this.rowCount = tempList.size();
        this.groupPage = groupPage;
        int startLine = 0;
        if (groupPage > 0)
            startLine = (currPage - 1) % groupPage * pageSize;
        int endLine = startLine + pageSize;
        int len = tempList.size();
        if (endLine > len)
            endLine = len;
        List<T> dataList = new ArrayList<T>();
        for (int i = startLine; i < endLine; i++) {
            dataList.add(tempList.get(i));
        }

        if (startLine >= len && len > 10) {
            for (int i = len - 10; i < len; i++) {
                dataList.add(tempList.get(i));
            }
            this.currPage -= 1;
        }
        this.data = dataList;
        // 计算记录位置
        if (currPage <= 0)
            currPage = 1;
        int start = (currPage - 1) * pageSize; // 定位
        int end = start + pageSize;
        if (end > rowCount + (currGroup - 1) * pageSize * groupPage) {
            end = rowCount + (currGroup - 1) * pageSize * groupPage;
        }
        if (rowCount != 0) {
            startPosition = start + 1;
            endPosition = end;
        }
    }

    /*
     * 默认构造函数 @param page 也参数 @param list 数据列表结果集
     */
    public PageData(Page page, List<T> list) {
        this.currPage = page.getPage();
        this.pageSize = page.getPageSize();
        this.rowCount = page.getRowCount();
        this.data = list;
        init();
    }

    public Object getAppendix() {
        return this.appendix;
    }

    /**
     * 获取当前组
     * 
     * @return
     */
    public int getCurrGroup() {
        return currGroup;
    }

    /**
     * 获取当前页
     * 
     * @return
     */
    public int getCurrPage() {
        return currPage;
    }

    /**
     * 获取数据列表
     * 
     * @return
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 获取终止位置
     * 
     * @return
     */
    public int getEndPosition() {
        return this.endPosition;
    }

    /**
     * 
     * @return
     */
    public int getOldGroup() {
        return oldGroup;
    }

    /**
     * 获取总页数
     * 
     * @return
     */
    public int getPageCount() {
        return pageCount;
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

    /**
     * 获取起始位置
     * 
     * @return
     */
    public int getStartPosition() {
        return this.startPosition;
    }

    /**
     * 系统初始化，初始化所有参数。
     */
    private void init() {
        if (rowCount != 0)
            pageCount = rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1;
        if (currPage <= 0)
            currPage = 1;
        if (currPage > pageCount)
            currPage = pageCount;
        int start = (currPage - 1) * pageSize; // 定位
        int end = start + pageSize;
        if (end > rowCount) {
            end = rowCount;
        }
        if (rowCount != 0) {
            startPosition = start + 1;
            endPosition = end;
        }
    }

    public void setAppendix(Object appendix) {
        this.appendix = appendix;
    }

    /**
     * 将数据列表结果集进行分页显示
     * 
     * @param list
     *            数据列表结果集
     */
    private List<T> setData(List<T> list) {
        rowCount = list.size();
        pageCount = rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1;
        if (currPage <= 0)
            currPage = 1;
        if (pageCount > 0 && currPage > pageCount)
            currPage = pageCount;
        int absolute = (currPage - 1) * pageSize; // 定位
        int end = absolute + pageSize;
        if (end > rowCount) {
            end = rowCount;
        }
        startPosition = absolute + 1;
        endPosition = end;
        return list.subList(absolute, end);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("pageSize = ").append(pageSize).append("; \r\n");
        sb.append("rowCount = ").append(rowCount).append("; \r\n");
        sb.append("pageCount = ").append(pageCount).append("; \r\n");
        sb.append("currPage = ").append(currPage).append("; \r\n");
        sb.append("startPosition = ").append(startPosition).append("; \r\n");
        sb.append("endPosition = ").append(endPosition).append("; \r\n");
        sb.append("list = \r\n").append(data).append("; \r\n");
        return sb.toString();
    }
}
