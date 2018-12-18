/***********************************************************************
 * $Id: LocalThreadBean.java,v1.0 2013-3-26 下午2:55:21 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

/**
 * @author Victor
 * @created @2013-3-26-下午2:55:21
 * 
 * 
 *          在当前线程中保持request和session信息，为了service和dao层能够取到当前登录用户信息
 * 
 *          注意：在使用中不直接使用本类来获取当前当前登录用户信息，而是用platform中封装的CurrentRequest来获取
 */
public class LocalThreadBean implements Serializable {
    private static final long serialVersionUID = 8470359191177229998L;
    private static ThreadLocal<HttpSession> threadLocal = new ThreadLocal<HttpSession>();
    //Modify by Rod 使用下列方式，Jetty服务器抛出NoSessionManage错误 
    //private static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();

    public HttpSession getRequest() {
        return threadLocal.get();
    }

    public HttpSession getSession() {
        return threadLocal.get();
    }

    public void setContext(HttpSession request) {
        threadLocal.set(request);
    }

    public void cleanContext() {
        threadLocal.set(null);
    }
}
