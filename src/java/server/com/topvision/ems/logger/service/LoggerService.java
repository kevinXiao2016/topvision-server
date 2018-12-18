/***********************************************************************
 * $Id: LoggerService.java,v1.0 2012-11-28 下午1:56:25 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.logger.service;

import java.util.List;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2012-11-28-下午1:56:25
 * 
 */
public interface LoggerService extends Service {
    /**
     * 配置日志级别
     * 
     * @param level
     */
    public void configLoggerLevel(String level);

    /**
     * 恢复为出厂设置
     * 
     * @return
     * 
     */
    boolean restoreFactoryConfig();

    /**
     * 使用一个镜像logback文件，以便加入新特性
     * 
     * @return
     * 
     */
    boolean useMirrorLogback();

    /**
     * 得到当前logger的等级
     * 
     * @return
     */
    String getCurrentLevel();

    void configLogger(List<String> includeLogs, List<String> setParam);


    public void addAppenderToContext(Appender<ILoggingEvent> loggerExporterImpl);

    /**
     * 移除并停止appender
     * @param appender
     */
    void detachAndStoAppender(Appender<ILoggingEvent> appender);
}
