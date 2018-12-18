/***********************************************************************
 * LowSpeedTaskBuildJob.java,v1.0 17-8-8 上午10:27 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.job;


import com.topvision.ems.cm.pnmp.facade.domain.CmtsCm;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollMiddleSpeedEndTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollMiddleSpeedTask;
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
public class MiddleSpeedTaskBuildJob extends TaskBuildJob {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private PnmpTaskBuildService pnmpTaskBuildService;
    private List<CmtsCm> cmList = new ArrayList<CmtsCm>();
    private Long collectTime;

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        logger.info("MiddleSpeedTaskBuildJob.execute");
        // 获取变量及bean
        getBeanAndValue(ctx);
        // 判断上轮轮询是否结束
        if (pnmpTaskBuildService.isLastPollEnd(new PnmpPollMiddleSpeedTask())) {
            logger.trace("Task Start" + new Date().toString());
            setCollectTime();
            Long middleCmNum = pnmpTaskBuildService.getMiddleCmNum();
            // 通知轮询开始
            pnmpTaskBuildService.fireRoundStartMessage(new PnmpPollMiddleSpeedTask(),System.currentTimeMillis(),middleCmNum);
            cmList = pnmpTaskBuildService.getMiddleCmList();

            for (CmtsCm cmtsCm : cmList) {
                try {
                    PnmpPollMiddleSpeedTask pnmpPollMiddleSpeedTask = createPnmpPollMiddleSpeedTask(cmtsCm);
                    pnmpTaskBuildService.putTaskToQueue(pnmpPollMiddleSpeedTask);
                } catch (Exception e) {
                    logger.error("MiddleSpeedTaskBuildJob.data format error", e);
                }
            }
            PnmpPollMiddleSpeedEndTask pnmpPollMiddleSpeedEndTask = createPnmpPollMiddleSpeedEndTask();
            pnmpTaskBuildService.putTaskToQueue(pnmpPollMiddleSpeedEndTask);
            pnmpTaskBuildService.fireRoundEndMessage(new PnmpPollMiddleSpeedTask(),collectTime);
            logger.trace("PnmpPollMiddleSpeedTask End" + new Date().toString());
        } else {
            logger.info("Last PnmpPollMiddleSpeedTask Build Is Running, PnmpPollMiddleSpeedTask End" + new Date().toString());
        }
//
        logger.info("PnmpPollMiddleSpeedTask.execute end");
    }

    private PnmpPollMiddleSpeedTask createPnmpPollMiddleSpeedTask(CmtsCm cmtsCm) {
        PnmpPollMiddleSpeedTask pnmpPollMiddleSpeedTask = new PnmpPollMiddleSpeedTask();
        pnmpPollMiddleSpeedTask.setBaseTaskInfo(cmtsCm);
        pnmpPollMiddleSpeedTask.setCmtsSnmpParam(cmtsCm.getSnmpParam());
        return pnmpPollMiddleSpeedTask;
    }

    private PnmpPollMiddleSpeedEndTask createPnmpPollMiddleSpeedEndTask() {
        return new PnmpPollMiddleSpeedEndTask();
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
        collectTime = CollectTimeUtil.getCollectTimeUtil("PnmpMiddle").getCollectTime();
    }
}
