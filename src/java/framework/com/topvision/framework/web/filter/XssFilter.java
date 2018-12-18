/***********************************************************************
 * $Id: XssFilter.java,v1.0 2015-9-9 下午2:38:02 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author fanzidong
 * @created @2015-9-9-下午2:38:02
 * 
 */
public class XssFilter implements Filter {
    
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if(httpRequest.getContentType()!=null && httpRequest.getContentType().indexOf("multipart/form-data")!=-1){
            chain.doFilter(request, response);  
        }else{
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);  
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}
