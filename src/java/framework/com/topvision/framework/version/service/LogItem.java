/***********************************************************************
 * $Id: LogItem.java,v 1.1 2010-1-17 下午06:48:50 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.version.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date 2010-1-17 下午06:48:50
 * 
 * @author kelers
 * 
 */
public class LogItem {
    protected static final Logger logger = LoggerFactory.getLogger(LogItem.class);
    String prefix = "";
    String info = "";
    Throwable throwable = null;

    public LogItem(String prefix, String info) {
        this.prefix = prefix;
        this.info = info;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (prefix == null && prefix.length() == 0) {
            sb.append(info);
        } else {
            sb.append("[").append(prefix).append("] - ").append(info);
        }
        if (throwable != null) {
            StringWriter writer = new StringWriter();
            throwable.printStackTrace(new PrintWriter(writer, true));
            sb.append('\n').append(writer.getBuffer().toString());
        }
        return sb.toString();
    }
}
