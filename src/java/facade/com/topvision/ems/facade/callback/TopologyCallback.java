/***********************************************************************
 * $Id: TopologyCallback.java,v 1.1 Aug 13, 2009 10:14:12 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.callback;

import com.topvision.ems.facade.domain.TopologyResult;
import com.topvision.framework.annotation.Callback;

/**
 * @Create Date Aug 13, 2009 10:14:12 PM
 * 
 * @author kelers
 * 
 */
@Callback(beanName = "topologyMgr", serviceName = "topologyMgr")
public interface TopologyCallback {
    /**
     * 添加一个等待任务
     */
    void addTask();

    /**
     * 清除所有等待任务
     */
    void clearTasks();

    /**
     * @return 添加数目的任务是否完成
     */
    Boolean isFinished();

    /**
     * @param result
     *            拓扑数据
     * @return 是否处理成功
     */
    Boolean onData(TopologyResult result);

    /**
     * 是否发现非snmp设备
     * 
     * @param onlyDiscoverySnmp
     */
    public void setOnlyDiscoverySnmp(Boolean onlyDiscoverySnmp);
}
