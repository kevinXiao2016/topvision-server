/***********************************************************************
 * LowSpeedTaskBuildJob.java,v1.0 17-8-8 上午10:27 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.job;


import com.topvision.ems.cm.pnmp.facade.domain.CmtsCm;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollHighSpeedEndTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollHighSpeedTask;
import com.topvision.ems.cm.pnmp.service.PnmpTaskBuildService;
import com.topvision.framework.common.CollectTimeUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jay
 * @created 17-8-8 上午10:27
 */
public class HighSpeedTaskBuildJob extends TaskBuildJob {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private PnmpTaskBuildService pnmpTaskBuildService;
    private List<CmtsCm> cmList = new ArrayList<CmtsCm>();
    private Long collectTime;

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        logger.info("HighSpeedTaskBuildJob.execute");
        // 获取变量及bean
        getBeanAndValue(ctx);
        // 判断上轮轮询是否结束
        if (pnmpTaskBuildService.isLastPollEnd(new PnmpPollHighSpeedTask())) {
            logger.trace("Task Start" + new Date().toString());
            setCollectTime();
            Long highCmNum = pnmpTaskBuildService.getHighCmNum();
            // 通知轮询开始
            pnmpTaskBuildService.fireRoundStartMessage(new PnmpPollHighSpeedTask(),System.currentTimeMillis(),highCmNum);
            cmList = pnmpTaskBuildService.getHighCmList();

            for (CmtsCm cmtsCm : cmList) {
                try {
                    PnmpPollHighSpeedTask pnmpPollHighSpeedTask = createPnmpPollHighSpeedTask(cmtsCm);
                    pnmpTaskBuildService.putTaskToQueue(pnmpPollHighSpeedTask);
                } catch (Exception e) {
                    logger.error("HighSpeedTaskBuildJob.data format error", e);
                }
            }
            PnmpPollHighSpeedEndTask pnmpPollHighSpeedEndTask = createPnmpPollHighSpeedEndTask();
            pnmpTaskBuildService.putTaskToQueue(pnmpPollHighSpeedEndTask);
            pnmpTaskBuildService.fireRoundEndMessage(new PnmpPollHighSpeedTask(),collectTime);
            logger.trace("PnmpPollHighSpeedTask End" + new Date().toString());
        } else {
            logger.info("Last PnmpPollHighSpeedTask Build Is Running, PnmpPollHighSpeedTask End" + new Date().toString());
        }
//
        logger.info("PnmpPollHighSpeedTask.execute end");
    }

    private PnmpPollHighSpeedTask createPnmpPollHighSpeedTask(CmtsCm cmtsCm) {
        PnmpPollHighSpeedTask pnmpPollHighSpeedTask = new PnmpPollHighSpeedTask();
        pnmpPollHighSpeedTask.setBaseTaskInfo(cmtsCm);
        pnmpPollHighSpeedTask.setCmtsSnmpParam(cmtsCm.getSnmpParam());
        return pnmpPollHighSpeedTask;
    }

    private PnmpPollHighSpeedEndTask createPnmpPollHighSpeedEndTask() {
        return new PnmpPollHighSpeedEndTask();
    }

    /**
     * 获取bean及变量
     *
     * @param ctx
     */
    private void getBeanAndValue(JobExecutionContext ctx) {
        BeanFactory beanFactory = (BeanFactory) ctx.getJobDetail().getJobDataMap().get("beanFactory");
        pnmpTaskBuildService = (PnmpTaskBuildService) beanFactory.getBean("pnmpTaskBuildService");
    }

    /**
     * 设置采集时间
     */
    private void setCollectTime() {
        collectTime = CollectTimeUtil.getCollectTimeUtil("PnmpHigh").getCollectTime();
    }
}
