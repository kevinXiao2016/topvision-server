/***********************************************************************
 * $ CmPollSchedulerService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.scheduler.service;

import java.util.List;

import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.ems.cm.cmpoll.scheduler.domain.CmPollCollector;
import com.topvision.framework.service.Service;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface CmPollSchedulerService extends Service {
    /**
     * 接收任务生成模块传递过来的任务，启动调度
     * 
     * @param engineId
     * @param cmPollTasks
     * @return
     */
    void appendTask(Integer engineId, Long time, List<CmPollTask> cmPollTasks);

    /**
     * 开始一轮采集
     */
    void roundStart(Long time);

    /**
     * 完成任务推送
     */
    void roundFinished(Long time);

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
    List<CmPollCollector> getCmPollCollectorList();

    /**
     * 更新engine状态
     */
    void notifyEngineServerChange();
}