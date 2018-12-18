/***********************************************************************
 * $ CmPollSchedulerService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;
import com.topvision.ems.cm.pnmp.domain.PnmpPollCollector;
import com.topvision.framework.service.Service;

import java.util.List;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface PnmpSchedulerService extends Service {
    /**
     * 接收任务生成模块传递过来的任务，启动调度
     * 
     * @param engineId
     * @param cmPollTasks
     * @return
     */
    void appendTask(Integer engineId, Long time, List<PnmpPollTask> cmPollTasks);

    /**
     * 开始一轮采集
     */
    void roundStart(Long time, PnmpPollTask pnmpPollTask);

    /**
     * 完成任务推送
     */
    void roundFinished(Long time, PnmpPollTask pnmpPollTask);

    /**
     * 是否有空闲采集器
     */
    Integer isAnyIdle();

    /**
     * 是否有空闲采集器
     */
    Integer idleTaskCount(Integer engineId);

    /**
     * 获取CM轮询采集器列表
     */
    List<PnmpPollCollector> getPnmpPollCollectorList();

    void notifyEngineServerChange();
}