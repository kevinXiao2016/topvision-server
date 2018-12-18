/***********************************************************************
 * $ CmPollTaskBuildService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.taskbuild.service;

import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.framework.service.Service;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
public interface CmPollTaskBuildService extends Service {

    /**
     * 开启一个定时器，开始CM轮询采集
     */
    public void startCmPollBuild();

    /**
     * 关闭一个定时器，停止CM轮询采集
     * 
     * @param interval
     */
    public void stopCmPollBuild();

    /**
     * 通知调度模块一轮轮询开始
     * 
     * @param collectTime
     */
    public void fireRoundStartMessage(Long collectTime, Long cmOnLine);

    /**
     * 通知调度模块一轮轮询结束
     * 
     * @param collectTime
     */
    public void fireRoundEndMessage(Long collectTime);

    /**
     * 判断上轮轮询是否结束
     * @return
     */
    Boolean isLastPollEnd();

    /**
     * 将任务放入阻塞队列
     * @param cmPollTask
     */
    void putTaskToQueue(CmPollTask cmPollTask);
    
    /**
     * 获取CM在线总数
     * @return
     */
    Long getCmOnLine();
}