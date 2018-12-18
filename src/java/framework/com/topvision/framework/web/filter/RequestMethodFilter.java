/***********************************************************************
 * $Id: RequestMethodFilter.java,v1.0 2015-9-7 上午9:46:14 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.web.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author fanzidong
 * @created @2015-9-7-上午9:46:14
 *
 */
public class RequestMethodFilter extends TemplateFilter{
    
    private final Set<String> forbiddenUrls = new HashSet<String>();
    private final Set<String> postUrls = new HashSet<String>();

    @Override
    public void doPostProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    }

    @Override
    public boolean doPreProcessing(ServletRequest request, ServletResponse response) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        //StringEscapeUtils.escapeHtml(arg0)
        //HttpServletResponse httpResponse = (HttpServletResponse) response;
        String method = httpRequest.getMethod();
        String path = httpRequest.getServletPath();
        if("post".equalsIgnoreCase(method)){
            return true;
        }
        //TODO 对get请求做处理
        if(forbiddenUrls.contains(path) || postUrls.contains(path)){
            request.getRequestDispatcher("/forbidden.jsp").forward(request, response);
        }
        return true;
    }

    @Override
    public void initParameter(FilterConfig filterConfig) throws ServletException {
        String urls = filterConfig.getInitParameter("forbiddenURL");
        StringTokenizer st = new StringTokenizer(urls, ",");
        while (st.hasMoreTokens()) {
            forbiddenUrls.add(st.nextToken().trim());
        }
        urls = filterConfig.getInitParameter("postURL");
        StringTokenizer pSt = new StringTokenizer(urls, ",");
        while (pSt.hasMoreTokens()) {
            postUrls.add(pSt.nextToken().trim());
        }
    }

}
