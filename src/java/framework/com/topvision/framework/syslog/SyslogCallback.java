/***********************************************************************
 * $Id: SyslogCallback.java,v 1.1 2009-9-30 下午02:09:49 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.syslog;

import com.topvision.framework.annotation.Callback;

/**
 * @Create Date 2009-9-30 下午02:09:49
 * 
 * @author kelers
 * 
 */
@Callback(beanName = "syslogService", serviceName = "syslogService")
public interface SyslogCallback {
    /**
     * 用户收取Trap后处理
     * 
     * @param trap
     */
    void onSyslog(Syslog syslog);
}
