/***********************************************************************
 * $Id: LongRequest.java,v1.0 2016年4月29日 下午1:46:55 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.message;

import com.topvision.platform.zetaframework.var.ZetaValueGetter;

/**
 * @author Bravin
 * @created @2016年4月29日-下午1:46:55
 *
 */
@ZetaValueGetter("LongRequest")
public class LongRequest {
    /**长时间请求的前端默认注册名*/
    public static final String LONG_REQUEST_REGISTER = "LongRequest";
    public static final Integer COMPLETE = 4;
    public static final Integer ERROR_INTERRUPT = 3;
    public static final Integer PROCESS = 2;
}
