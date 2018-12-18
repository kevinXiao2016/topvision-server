/*
 * @(#)TemplateFilter.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 模板过滤器.
 * 
 * @author 聂军
 * @version 1.0, 2006-4-26
 */

public abstract class TemplateFilter implements Filter {

    private FilterConfig filterConfig;
    protected final String STANDARD_ENCODING = "UTF-8";

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        filterConfig = null;
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        if (doPreProcessing(request, response)) {
            /**
             * 此时一定要验证 response是否已经 commited 了，如果已经commited了还要继续执行filter，则会抛出
             * java.lang.IllegalStateException: Committed 异常 ... reponse.resetBuffer()..
             */
            if (!response.isCommitted()) {
                chain.doFilter(request, response);
            }
            doPostProcessing(request, response);
        }
    }

    /**
     * 过滤器的后处理操作.
     * 
     * @param request
     *            客户端请求.
     * @param response
     *            客户端响应.
     */
    public abstract void doPostProcessing(ServletRequest request, ServletResponse response) throws IOException,
            ServletException;

    /**
     * 过滤器的预处理操作.
     * 
     * @param request
     *            客户端请求.
     * @param response
     *            客户端响应.
     */
    public abstract boolean doPreProcessing(ServletRequest request, ServletResponse response) throws IOException,
            ServletException;

    /**
     * @return javax.servlet.FilterConfig
     */
    protected final FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        initParameter(filterConfig);
    }

    /**
     * 初始化过滤器.
     */
    public abstract void initParameter(FilterConfig filterConfig) throws ServletException;

}
