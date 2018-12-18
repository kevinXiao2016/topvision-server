/***********************************************************************
 * $Id: SessionTimeoutFilter.java,v1.0 2014年6月9日 上午11:21:32 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.filter;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topvision.framework.web.filter.TemplateFilter;

/**
 * @author Bravin
 * @created @2014年6月9日-上午11:21:32
 * 
 */
public class SessionTimeoutFilter extends TemplateFilter {
	private static final String loginUrl = "login.jsp";

	@Override
	public void doPostProcessing(ServletRequest request,
			ServletResponse response) throws IOException, ServletException {
	}

	@Override
	public boolean doPreProcessing(ServletRequest request,
			ServletResponse response) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		if (session == null) {
			return true;
		}
		/** 实现其超时锁定功能 **/
		if (httpRequest.getParameter("ignoreTimeout") == null) {
			int maxSessionInactiveInterval = session.getMaxInactiveInterval();
			long current = System.currentTimeMillis();
			if (maxSessionInactiveInterval != -1) {
				/** session.invalidate()之后，lastAccessTime就取不到了 **/
				long lastAccessTime;
				Object lastAccessTimeRw = session
						.getAttribute("lastAccessTime");
				if (lastAccessTimeRw != null) {
					lastAccessTime = (Long) lastAccessTimeRw;
					long minius = current - lastAccessTime;
					if (minius > maxSessionInactiveInterval * 1000) {
						/** 强制session失效 **/
						session.invalidate();
						/** 重定向，如果此时不重定向，不会及时跳转到登陆页面，而要等到下次才能跳转 **/
						httpResponse.sendRedirect(httpRequest.getContextPath()
								+ "/" + loginUrl);
						/**
						 * 重定向后一定要及时的返回，如果不返回，则一定要执行session.setAttribute(
						 * "lastAccessTime", currentTimeMillis) 但是此时 session
						 * 正在invalidate，所以就会抛出IllegalStateException异常
						 */
						return false;
					}
				}
			}
			session.setAttribute("lastAccessTime", current);
		}

		return true;
	}

	@Override
	public void initParameter(FilterConfig filterConfig)
			throws ServletException {
	}

}
