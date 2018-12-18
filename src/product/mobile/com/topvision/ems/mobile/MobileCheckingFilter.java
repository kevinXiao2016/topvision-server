/***********************************************************************
 * $Id: MobileCheckingFilter.java,v1.0 2014年6月19日 上午11:04:20 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.topvision.framework.web.filter.TemplateFilter;

/**
 * @author YangYi
 * @created @2014年6月19日-上午11:04:20
 * 
 */
public class MobileCheckingFilter extends TemplateFilter {

    @Override
    public void doPostProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    }

    @Override
    public boolean doPreProcessing(ServletRequest request, ServletResponse response) throws IOException,
            ServletException {
        return true;
    }

    @Override
    public void initParameter(FilterConfig filterConfig) throws ServletException {
    }

}
