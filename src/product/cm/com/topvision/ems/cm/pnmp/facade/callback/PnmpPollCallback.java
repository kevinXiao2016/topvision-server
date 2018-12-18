/***********************************************************************
 * $ com.topvision.ems.facade.callback.CmPollCallback,v1.0 2012-5-6 9:05:15 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.callback;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;
import com.topvision.framework.annotation.Callback;


/**
 * @author jay
 * @created @2012-5-6-9:05:15
 */
@Callback(beanName = "pnmpSchedulerService", serviceName = "pnmpSchedulerService")
public interface PnmpPollCallback {
    //回调连接检查
    void connectTest();

    /**
     * 完成一个task
     * @param taskId
     */
    void completeTask(Integer engineId, Long taskId);

    /**
     * 一个ENGINE完成一轮统计
     * @param engineId
     * @param pnmpPollTask
     */
    void completeRoundStatistics(Integer engineId, Long time, PnmpPollTask pnmpPollTask);

}
