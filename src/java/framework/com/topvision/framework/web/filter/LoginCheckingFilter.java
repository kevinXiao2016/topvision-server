/*
 * @(#)LoginCheckingFilter.java
 *
 * Copyright 2011 Topoview All rights reserved
 */

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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topvision.framework.domain.LocalThreadBean;

/**
 * <filter> <filter-name>LoginChecking</filter-name>
 * <filter-class>com.topvision.framework.servlet.LoginCheckingFilter</filter-class> <init-param>
 * <param-name>filterURL</param-name> <param-value>/index.jsp, /login/login.jsp,
 * /login/login.do</param-value> </init-param> <init-param> <param-name>loginURL</param-name>
 * <param-value>login/login.jsp</param-value> </init-param> <init-param>
 * <param-name>userKey</param-name> <param-value>UserContext</param-value> </init-param> </filter>
 * 
 * <filter-mapping> <filter-name>LoginChecking</filter-name> <url-pattern>*.jsp</url-pattern>
 * </filter-mapping>
 * 
 * @author niejun
 */
public class LoginCheckingFilter extends TemplateFilter {

    private String loginUrl = "login.jsp";
    private String userKey = "UserContext";

    private final Set<String> filterUrls = new HashSet<String>();
    private static LocalThreadBean localThreadBean = new LocalThreadBean();

    /*
     * (non-Javadoc)
     * 
     * @see com.zetaframework.web.filter.TemplateFilter#doPostProcessing(javax.servlet
     * .ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    public void doPostProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.zetaframework.web.filter.TemplateFilter#doPreProcessing(javax.servlet
     * .ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    public boolean doPreProcessing(ServletRequest request, ServletResponse response) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        request.setCharacterEncoding(STANDARD_ENCODING);
        if (session == null || session.getAttribute(userKey) == null) {
            localThreadBean.cleanContext();
            if (filterUrls.contains(httpRequest.getServletPath())) {
                return true;
            } else {
                if (!"XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("x-requested-with"))
                        && !httpRequest.getRequestURI().endsWith(".mb")) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/" + loginUrl);
                    return false;
                }
                return false;
            }
        }

        // modify by bravin@20140328
        localThreadBean.setContext(httpRequest.getSession());
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.zetaframework.web.filter.TemplateFilter#initParameter(javax.servlet .FilterConfig)
     */
    @Override
    public void initParameter(FilterConfig filterConfig) throws ServletException {
        loginUrl = filterConfig.getInitParameter("loginURL");
        userKey = filterConfig.getInitParameter("userKey");
        String urls = filterConfig.getInitParameter("filterURL");
        StringTokenizer st = new StringTokenizer(urls, ",");
        while (st.hasMoreTokens()) {
            filterUrls.add(st.nextToken().trim());
        }
    }

}
