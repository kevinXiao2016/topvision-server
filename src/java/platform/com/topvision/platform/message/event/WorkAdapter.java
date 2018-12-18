/***********************************************************************
 * $ WorkAdapter.java,v1.0 2011-7-21 18:51:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author jay
 * @created @2011-7-21-18:51:05
 */
public class WorkAdapter implements WorkListener {

    // 监听器方法
    /**
     * 配置失败
     * 
     * @param event
     */
    public void failure(WorkEvent event) {
    }

    /**
     * 配置成功
     * 
     * @param event
     */
    public void success(WorkEvent event) {
    }

    /**
     * 结束配置流程
     * 
     * @param event
     */
    public void workEnd(WorkEvent event) {
    }

    /**
     * 开始配置流程
     * 
     * @param event
     */
    public void workStart(WorkEvent event) {
    }
}
