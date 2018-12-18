/***********************************************************************
 * $Id: PageBehavior.java,v1.0 2013-4-29 下午2:48:38 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.var;

/**
 * 页面功能一般分为增，删，改。为了便于页面复用，故用一个pageAction字段进行标识
 * @author Bravin
 * @created @2013-4-29-下午2:48:38
 *
 */
public class PageBehavior {
    public static final int CREATE = 1;
    public static final int MODIFY = 2;
    public static final int DELETE = 3;
}
