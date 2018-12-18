/***********************************************************************
 * $Id: LoggerServiceImpl.java,v1.0 2012-11-26 下午5:50:45 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.logger.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.TurboFilterList;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.FilterReply;

import com.topvision.ems.logger.service.LoggerService;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2012-11-29-下午1:27:22
 * 
 */
@Service("loggerService")
public class LoggerServiceImpl extends BaseService implements LoggerService {
    private final String LOGBACK_FILE = "/com/topvision/ems/logger/logback.xml";
    private LoggerContext loggerContext;
    private boolean mirrorLock;
    private String defaultRootLv;
    private String defaultLoggerLv;

    @Override
    @PostConstruct
    public void initialize() {
        loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.logger.service.LoggerService#useMirrorLogback()
     */
    @Override
    public boolean useMirrorLogback() {
        // 如果当前使用的是镜像logback文件，则直接返回，否则使用镜像logback文件完毕后，开启mirrorLock
        if (mirrorLock) {
            return Boolean.TRUE;
        }
        loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Level loggerLv = loggerContext.getLogger("com.topvision").getLevel();
        Level rootLv = loggerContext.getLogger("ROOT").getLevel();
        if (loggerLv == null) {
            loggerLv = Level.INFO;
        }
        if (rootLv == null) {
            rootLv = Level.INFO;
        }
        defaultLoggerLv = loggerLv.toString();
        defaultRootLv = rootLv.toString();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            InputStream stream = getClass().getResourceAsStream(LOGBACK_FILE);
            if (stream != null) {
                loggerContext.reset();
                configurator.setContext(loggerContext);
                configurator.doConfigure(stream);
                // 难以通过properties的方法进行这样的设置，所以采用在代码中修改
                loggerContext.getLogger("com.topvision").setLevel(Level.toLevel(defaultLoggerLv));
                loggerContext.getLogger("ROOT").setLevel(Level.toLevel(defaultRootLv));
                // 开启mirrorLock
                mirrorLock = Boolean.TRUE;
            }
        } catch (JoranException e) {
            logger.error("", e);
            return Boolean.FALSE;
        } catch (Exception el) {
            logger.error("", el);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.logger.service.LoggerService#restoreFactoryConfig()
     */
    @Override
    public boolean restoreFactoryConfig() {
        InputStream stream = null;
        try {
            try {
                Class<?> clazz = Class.forName("com.topvision.console.Version");
                stream = clazz.getResourceAsStream("/logback-test.xml");
                if (stream == null) {
                    stream = clazz.getResourceAsStream("/logback.xml");
                }
            } catch (ClassNotFoundException e) {
                logger.error("com.topvision.console.Version", e);
            }
            if (stream != null) {
                JoranConfigurator configurator = new JoranConfigurator();
                loggerContext.reset();
                configurator.setContext(loggerContext);
                configurator.doConfigure(stream);
                // 恢复出厂设置后把mirrorLock解锁
                mirrorLock = Boolean.FALSE;
            }
        } catch (JoranException e) {
            logger.error("", e);
            return Boolean.FALSE;
        } catch (Exception el) {
            logger.error("", el);
            return Boolean.FALSE;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
        return Boolean.TRUE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.LoggerService#configLoggerLevel(java.lang.String)
     */
    @Override
    public void configLoggerLevel(String level) {
        // 不允许修改root logger
        loggerContext.getLogger("com.topvision").setLevel(Level.toLevel(level));
        loggerContext.getLogger("ROOT").setLevel(Level.INFO);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.logger.service.LoggerService#getCurrentLevel()
     */
    @Override
    public String getCurrentLevel() {
        Level lv = loggerContext.getLogger("com.topvision").getLevel();
        return lv.toString();
    }

    @Override
    public void configLogger(List<String> includeLogs, List<String> setParam) {
        List<Logger> loggers = loggerContext.getLoggerList();
        for (Logger logger : loggers) {
            String $name = logger.getName();
            for (String $include : includeLogs) {
                if (matchLogger($name, $include)) {
                    for (String param : setParam) {
                        String[] $kv = param.trim().split("=");
                        String key = $kv[0].trim();
                        String value = $kv[1].trim();
                        switch (key) {
                        case "LEVEL":
                            /*loggerContext.addTurboFilter(new TurboFilter() {
                                
                                @Override
                                public FilterReply decide(Marker arg0, Logger logger, Level level, String arg3, Object[] arg4, Throwable arg5) {
                                    if(logger.getLevel() == null){
                                        return FilterReply.NEUTRAL;
                                    }
                                    if(level.isGreaterOrEqual(logger.getLevel())  ){
                                        return FilterReply.ACCEPT;
                                    }else {
                                        return FilterReply.DENY;
                                    }
                                }
                            });*/
                            logger.setLevel(Level.toLevel(value));
                            break;
                        default:
                            break;
                        }
                    }
                }
            }

        }
        
    }

    public static boolean matchLogger(String $name, String $include) {
        $include = $include.replaceAll("\\.", "[.]{1}").replaceAll("\\*", ".*");
        $include = "^" + $include;
        return $name.matches($include);
    }

    @Override
    public void addAppenderToContext(Appender<ILoggingEvent> appender) {
        String appenderName = appender.getName();
        Logger rootlogger = loggerContext.getLogger("com.topvision");
        rootlogger.detachAppender(appenderName);
        rootlogger.addAppender(appender);
        appender.start();
    }

    public void detachAndStoAppender(Appender<ILoggingEvent> appender) {
        String appenderName = appender.getName();
        Logger rootlogger = loggerContext.getLogger("com.topvision");
        rootlogger.detachAppender(appenderName);
        appender.stop();
    }
}
