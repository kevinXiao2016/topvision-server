/***********************************************************************
 * $Id: SyslogListenportsService.java,v1.0 2013-4-1 下午3:27:43 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.SystemPreferences;

/**
 * @author flack
 * @created @2013-4-1-下午3:27:43
 *
 */
public interface SyslogListenportsService extends Service {
    /**
     * 根据name获取Syslog系统选项
     * @param name
     * @return
     */
    public SystemPreferences getSyslogPreferences(String name);

    /**
     * 保存用户设置的系统日志监听端口,同时进行系统日志监听端口的配置
     * @param syslogListenports
     */
    public void saveSyslogListenports(String syslogListenports);

    /**
     * 配置用户设置的系统日志监听端口，使用户设置的系统日志监听端口生效 
     * @param syslogListenports
     */
    public void configSyslogListenports(String syslogListenports);
}
