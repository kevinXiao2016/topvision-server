/*
 * @(#)BaseAction.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.web.struts2;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.utils.HttpUtils;

/**
 * Action的基类.
 * 
 * @author niejun
 * @version 1.0, 2007-08-23
 */
public abstract class BaseAction extends ActionSupport
        implements SessionAware, ServletResponseAware, ServletRequestAware {
    private static final long serialVersionUID = 974728685888862539L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Page page;

    /**
     * 注入request
     */
    protected HttpServletRequest request;
    /**
     * 注入response
     */
    protected HttpServletResponse response;
    private final String encoding = "UTF-8";

    /**
     * 分页结果.
     */
    @SuppressWarnings("rawtypes")
    protected PageData pageData;

    // 当前页码.
    private int curPage = 1;
    private int pageSize = 25;

    // ExtJs 的分页参数
    protected int start = 0;
    protected int limit = 25;
    protected String sort;
    protected String dir;
    private String groupBy;

    private boolean emptyAllRow;

    private Map<String, Object> session = null;

    public static final String FORBIDDEN = "forbidden";

    /**
     * 获取当前会话.
     * 
     * @return
     */
    public Map<String, Object> getSession() {
        return session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts2.interceptor.SessionAware#setSession(java.util.Map)
     */
    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    /**
     * 获取当前分页显示的结果.
     * 
     * @return
     */
    public int getCurPage() {
        return curPage;
    }

    public String getDir() {
        return dir;
    }

    public Page getExtPage() {
        if (page == null) {
            page = new Page();
        }
        page.setPageSize(limit);
        page.setPage((start / page.getPageSize()) + 1);
        page.setSortName(sort);
        page.setSortDirection(dir);
        page.setGroupBy(groupBy);
        return page;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public int getLimit() {
        return limit;
    }

    public Page getPage() {
        if (page == null) {
            page = new Page();
        }
        page.setPageSize(pageSize);
        page.setPage(curPage);
        page.setSortName(sort);
        page.setSortDirection(dir);
        page.setGroupBy(groupBy);
        return page;
    }

    /**
     * 得到分页结果.
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public PageData getPageData() {
        return pageData;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getSort() {
        return sort;
    }

    public int getStart() {
        return start;
    }

    public boolean isEmptyAllRow() {
        return emptyAllRow;
    }

    public void setCurPage(int page) {
        this.curPage = page;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setEmptyAllRow(boolean selectAll) {
        this.emptyAllRow = selectAll;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @SuppressWarnings("rawtypes")
    public void setPageData(PageData pd) {
        this.pageData = pd;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public void setServletRequest(HttpServletRequest req) {
        this.request = req;
    }

    @Override
    public void setServletResponse(HttpServletResponse resp) {
        this.response = resp;
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding(encoding);
            response.setHeader("Cache-Control", "no-cache");
            // this.writer = resp.getWriter();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 将数据传到前台页面
     * 
     * @param result
     * @param contentType
     */
    protected void write(Object result, String contentType) {
        HttpServletResponse response = ServletActionContext.getResponse();
        if (contentType == null) {
            contentType = HttpUtils.CONTENT_TYPE_JSON;
        }
        if (response != null) {
            response.setContentType(contentType);
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().print(result.toString());
            } catch (IOException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("", e);
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("%%%%%HttpServletResponse is null.");
            }
        }
    }

    /**
     * 将数据传到前台页面
     *
     * @param result
     *            Object
     */
    protected void writeDataToAjax(Object result) {
        // 特殊情况1：字符串/基本数据类型
        if (result instanceof String || result instanceof Boolean || result instanceof Integer
                || result instanceof Boolean || result instanceof Double || result instanceof Float) {
            writeTextToAjax((String) result);
        }
        // 特殊情况2：旧的json包
        else if (result instanceof net.sf.json.JSONArray || result instanceof net.sf.json.JSONObject) {
            writeJsonToAjax(result.toString());
            // add by
            // bravin@20131105:我也不知道为什么这里boolean类型的也用JSONArray,老代码里面就是这样用的，基于不修改原代码逻辑的原则，所以才有这样诡异的做法
        }
        // 统一处理java数组
        else if (result instanceof Collection<?> || result instanceof List) {
            writeJsonToAjax((JSONArray) JSONArray.toJSON(result));
        }
        // 统一处理java对象
        else {
            writeJsonToAjax((JSONObject) JSONObject.toJSON(result));
        }
    }

    /**
     * 将JSONObject数据传到前台页面
     *
     * @param json
     *            JSONObject
     */
    private void writeJsonToAjax(JSONObject json) {
        write(json, HttpUtils.CONTENT_TYPE_JSON);
    }

    /**
     * 将JSONArray数据传到前台页面
     *
     * @param json
     *            JSONArray
     */
    private void writeJsonToAjax(JSONArray json) {
        write(json, HttpUtils.CONTENT_TYPE_JSON);
    }

    /**
     * 将JSON字符串传到前台页面
     *
     * @param jsonString
     *            String
     */
    private void writeJsonToAjax(String jsonString) {
        write(jsonString, HttpUtils.CONTENT_TYPE_JSON);
    }

    /**
     * 将文本字符串传到前台页面
     *
     * @param text
     *            String
     */
    private void writeTextToAjax(String text) {
        write(text, HttpUtils.CONTENT_TYPE_PLAIN);
    }

    public File anlayseFileFromFlash() {
        File file = null;

        MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
        while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            String inputName = fileParameterNames.nextElement();
            String[] contentType = multiWrapper.getContentTypes(inputName);
            if (isNonEmpty(contentType)) {
                String[] fileName = multiWrapper.getFileNames(inputName);
                if (isNonEmpty(fileName)) {
                    File[] files = multiWrapper.getFiles(inputName);
                    if (files != null) {
                        for (File file1 : files) {
                            file = file1;
                        }
                    }
                }
            }
        }

        return file;
    }

    private boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int index = 0; index < objArray.length && !result; index++) {
            if (objArray[index] != null) {
                result = true;
            }
        }
        return result;
    }

}
