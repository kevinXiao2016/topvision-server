/*
 * @(#)CharacterEncodingFilter.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

package com.topvision.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 设置请求主体的字符集.
 * 
 * @author 聂军
 * @version 1.0, 2006-4-26
 */

public class CharacterEncodingFilter extends TemplateFilter {

    private String encoding = null;

    private boolean ignore = true;

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        encoding = null;
        super.destroy();
    }

    /**
     * @see com.run.common.servlet.TemplateFilter#doPostProcessing(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    @Override
    public void doPostProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    }

    /**
     * @see com.run.common.servlet.TemplateFilter#doPreProcessing(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    @Override
    public boolean doPreProcessing(ServletRequest request, ServletResponse response) throws IOException,
            ServletException {

        if (ignore || (request.getCharacterEncoding() == null)) {
            if (encoding != null) {
                request.setCharacterEncoding(encoding);
            }
        }
        return true;
    }

    /**
     * @see com.run.common.servlet.TemplateFilter#initFilter(javax.servlet.FilterConfig)
     */
    @Override
    public void initParameter(FilterConfig filterConfig) throws ServletException {

        encoding = filterConfig.getInitParameter("encoding");
        if ("false".equals(filterConfig.getInitParameter("ignore"))) {
            ignore = false;
        }
    }

}
