/***********************************************************************
 * $Id: LoggerTreeEntry.java,v1.0 2014-4-27 下午5:23:38 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.domain;

import java.util.Iterator;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

import com.topvision.framework.domain.TreeEntity;

/**
 * @author Victor
 * @created @2014-4-27-下午5:23:38
 *
 */
public class LoggerTreeEntry implements TreeEntity {
    private Logger logger;
    private String name;

    public LoggerTreeEntry(Logger logger) {
        this.logger = logger;
        this.name = logger.getName();
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String getParentId() {
        if ("ROOT".equals(name)) {
            return null;
        }
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return "ROOT";
        }
        return name.substring(0, index);
    }

    @Override
    public String getText() {
        int index = name.lastIndexOf(".");
        StringBuilder text = new StringBuilder();
        if (index == -1) {
            text.append(name);
        } else {
            text.append(name.substring(index + 1));
        }
        text.append("[").append(logger.getEffectiveLevel()).append("]");
        for (Iterator<Appender<ILoggingEvent>> itr = logger.iteratorForAppenders(); itr.hasNext();) {
            Appender<ILoggingEvent> appender = itr.next();
            text.append("{").append(appender.getName()).append("}");
        }
        return text.toString();
    }
}
