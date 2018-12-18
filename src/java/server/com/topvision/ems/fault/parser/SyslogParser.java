/***********************************************************************
 * $Id: SyslogParser.java,v1.0 2012-1-13 下午01:38:41 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.parser;

import com.topvision.framework.syslog.Syslog;

/**
 * @author Victor
 * @created @2012-1-13-下午01:38:41
 * 
 */
public interface SyslogParser {
    /**
     * syslog解析程序
     * 
     * @param entityId
     *            来源设备ID
     * @param syslog
     *            接收的syslog
     * @return 是否继续
     */
    public boolean parse(Long entityId, Syslog syslog);
    
    /**
     * syslog 处理器的优先级
     * 
     * @return
     */
    public Integer getSyslogCos();
}
