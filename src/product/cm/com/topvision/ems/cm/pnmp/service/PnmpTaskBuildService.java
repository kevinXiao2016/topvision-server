/***********************************************************************
 * PnmpTaskBuildService.java,v1.0 17-8-8 上午8:36 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service;

import com.topvision.ems.cm.pnmp.facade.domain.CmtsCm;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;

import java.util.List;
import java.util.Map;

/**
 * @author jay
 * @created 17-8-8 上午8:36
 */
public interface PnmpTaskBuildService {

    /**
     * 开启PNMP
     */
    public void startPnmp();

    /**
     * 关闭PNMP
     *
     */
    public void stopPnmp();

    /**
     * 通知调度模块一轮轮询开始
     *
     * @param pnmpPollTask
     * @param collectTime
     */
    public void fireRoundStartMessage(PnmpPollTask pnmpPollTask, Long collectTime, Long cmOnLine);

    /**
     * 通知调度模块一轮轮询结束
     *
     * @param pnmpPollTask
     * @param collectTime
     */
    public void fireRoundEndMessage(PnmpPollTask pnmpPollTask, Long collectTime);

    /**
     * 判断上轮轮询是否结束
     * @return
     * @param pnmpPollTask
     */
    Boolean isLastPollEnd(PnmpPollTask pnmpPollTask);

    /**
     * 将任务放入阻塞队列
     * @param pnmpPollTask
     */
    void putTaskToQueue(PnmpPollTask pnmpPollTask);

    /**
     * 获取CM在线总数
     * @return
     */
    Map<String,Long> getCmOnLine();

    List<CmtsCm> walkCmtsCm(Long entityId);

    Long getCmOnLineNum(List<Long> entityIdList);

    Long getMiddleCmNum();

    List<CmtsCm> getMiddleCmList();

    Long getHighCmNum();

    List<CmtsCm> getHighCmList();
}
