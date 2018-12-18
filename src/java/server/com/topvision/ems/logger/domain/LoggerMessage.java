/***********************************************************************
 * $Id: LoggerMessage.java,v1.0 2012-11-25 下午4:15:55 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.logger.domain;

import java.io.Serializable;

/**
 * @author Bravin
 * @created @2012-11-25-下午4:15:55
 * 
 */
public class LoggerMessage implements Serializable{
    private static final long serialVersionUID = -802361152015098798L;
    private String data;
    private int level;
    private String thread;
    //@Deprecated 用于标注当前这个日志是属于哪一个设备的
    private long entityId;
    //@Deprecated 用于标注当前这个日志是属于哪一个用户所有的
    private long userId;
    private String name;

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the thread
     */
    public String getThread() {
        return thread;
    }

    /**
     * @param thread
     *            the thread to set
     */
    public void setThread(String thread) {
        this.thread = thread;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
