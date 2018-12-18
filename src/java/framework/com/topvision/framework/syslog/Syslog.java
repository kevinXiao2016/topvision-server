/***********************************************************************
 * $Id: Syslog.java,v 1.1 2009-9-30 下午02:11:07 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.syslog;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @Create Date 2009-9-30 下午02:11:07
 * 
 * @author kelers
 * 
 */
public class Syslog implements AliasesSuperType {
    private static final long serialVersionUID = -6071423629083577826L;
    public static String FACILITIES[] = { "Kernel", "User", "Mail", "Daemon", "Auth", "Syslog", "Lpr", "News", "Uucp",
            "Cron", "Reserved10", "Reserver11", "Reserved12", "Reserved13", "Reserved14", "Reserved15", "Local0",
            "Local1", "Local2", "Local3", "Local4", "Local5", "Local6", "Local7" };
    public static String LEVEL[] = { "Emerg", "Alert", "Critical", "Error", "Warning", "Notice", "Info", "Debug" };

    private String date = null;
    private String message = null;
    private int facility = -1;
    private int level = -1;
    private String host;
    private int port;
    // 原始log
    private String text;

    public String date() {
        return date;
    }

    public String facility() {
        return facility > FACILITIES.length || facility < 0 ? "ERROR" : FACILITIES[facility];
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the facility
     */
    public int getFacility() {
        return facility;
    }

    public String getFacility(int priority) {
        return FACILITIES[priority / 8];
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    public String getLevel(int priority) {
        return LEVEL[priority & 7];
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    public int getNumLevel() {
        return LEVEL.length;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    public String host() {
        return host;
    }

    public String level() {
        return level > LEVEL.length || level < 0 ? "ERROR" : LEVEL[level];
    }

    public String level(int i) {
        return LEVEL[i];
    }

    public String message() {
        return message;
    }

    public void message(String date, String host, int priority, String msg) {
        this.date = date;
        this.host = host;
        this.level = priority & 7;
        this.facility = priority / 8;
        this.message = msg;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param facility
     *            the facility to set
     */
    public void setFacility(int facility) {
        this.facility = facility;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        data.append(host);
        data.append(" - ");
        data.append(date);
        data.append(" - ");
        data.append(facility());
        data.append(" - ");
        data.append(level());
        data.append(" - ");
        data.append(message);
        return data.toString();
    }
}
