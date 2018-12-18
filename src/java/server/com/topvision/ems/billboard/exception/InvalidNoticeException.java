/***********************************************************************
 * $Id: InvalidNoticeException.java,v1.0 2013年9月25日 上午11:27:48 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.billboard.exception;

/**
 * 公告无效的异常。当 当前时间  > 截止时间 时抛出
 * @author Bravin
 * @created @2013年9月25日-上午11:27:48
 *
 */
public class InvalidNoticeException extends RuntimeException{

    private static final long serialVersionUID = -6506644420930847107L;

}
