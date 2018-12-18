/***********************************************************************
 * $Id: LoggerExporter.java,v1.0 2012-11-25 下午4:13:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.logger.service.impl;


import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.topvision.ems.logger.domain.LoggerMessage;

/**
 * @author Bravin
 * @created @2012-11-25-下午4:13:47
 * 
 */
public class LoggerAppender extends AppenderBase<ILoggingEvent> {
    private PatternLayoutEncoder encoder;

    public LoggerAppender() {
        setName("LoggerAppender");
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        setContext(loggerContext);
        encoder = new PatternLayoutEncoder();
        encoder.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        encoder.setContext(loggerContext);
        //encoder.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        LoggerMessage log = new LoggerMessage();
        log.setData(encoder.getLayout().doLayout(event));
        log.setLevel(event.getLevel().toInt());
        log.setThread(event.getThreadName());
        log.setName(event.getLoggerName());
        LoggerConnector.appendLogger(log);
    }

    @Override
    public void start() {
        super.start();
        encoder.start();
    }

    /**
     * @return the encoder
     */
    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    /**
     * @param encoder
     *            the encoder to set
     */
    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

}
