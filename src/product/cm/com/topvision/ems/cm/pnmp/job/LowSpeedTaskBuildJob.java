/***********************************************************************
 * LowSpeedTaskBuildJob.java,v1.0 17-8-8 上午10:27 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.job;


import com.topvision.ems.cm.cmpoll.taskbuild.dao.CmPollTaskBuildDao;
import com.topvision.ems.cm.pnmp.facade.domain.CmtsCm;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollLowSpeedEndTask;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollLowSpeedTask;
import com.topvision.ems.cm.pnmp.service.PnmpTaskBuildService;
import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.CollectTimeUtil;
import com.topvision.framework.utils.CmcIndexUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.util.*;

/**
 * @author jay
 * @created 17-8-8 上午10:27
 */
public class LowSpeedTaskBuildJob extends TaskBuildJob {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private EntityTypeService entityTypeService;
    private CmcDao cmcDao;
    private PnmpTaskBuildService pnmpTaskBuildService;
    private CmPollTaskBuildDao cmPollTaskBuildDao;
    private List<Long> entityIdList = new ArrayList<Long>();
    private List<CmtsCm> cmList = new ArrayList<CmtsCm>();
    private Long collectTime;

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        logger.info("LowSpeedTaskBuildJob.execute");
        // 获取变量及bean
        getBeanAndValue(ctx);
        // 判断上轮轮询是否结束
        if (pnmpTaskBuildService.isLastPollEnd(new PnmpPollLowSpeedTask())) {
            logger.trace("Task Start" + new Date().toString());
            List<Long> cmcEntityIdList = cmPollTaskBuildDao.selectEntityIdWithIp(entityTypeService
                    .getCcmtswithagentType());
            List<Long> oltEntityIdList = cmPollTaskBuildDao.selectEntityIdWithIp(entityTypeService.getOltType());
            if (cmcEntityIdList != null) {
                entityIdList.addAll(cmcEntityIdList);
            }
            if (oltEntityIdList != null) {
                entityIdList.addAll(oltEntityIdList);
            }

            setCollectTime();
            Long cmOnLine = pnmpTaskBuildService.getCmOnLineNum(entityIdList);
            // 通知轮询开始
            pnmpTaskBuildService.fireRoundStartMessage(new PnmpPollLowSpeedTask(),System.currentTimeMillis(),cmOnLine);
            Map<String,Long> map = new HashMap<>();

            for (Long entityId : entityIdList) {
                cmList = pnmpTaskBuildService.walkCmtsCm(entityId);
                logger.info("pnmpTaskBuildService.entityId:" + entityId + " cmlist:" + cmList);
                for (CmtsCm cmtsCm : cmList) {
                    Long cmcIndex = CmcIndexUtils.getCmcIndexFromCmIndex(cmtsCm.getStatusIndex().longValue());
                    String key = entityId + "-" + cmcIndex;
                    Long cmcId;
                    if (map.containsKey(key)) {
                        cmcId = map.get(key);
                    } else {
                        cmcId = cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId,cmcIndex);
                        map.put(key,cmcId);
                    }
                    if (cmcId == null) {
                        logger.info("Cmts is out off service. entityId[" + entityId + "] cmcIndex[" + cmcIndex + "]");
                        continue;
                    } else {
                        cmtsCm.setEntityId(entityId);
                        cmtsCm.setCmcId(cmcId);
                        cmtsCm.setCmcIndex(cmcIndex);
                    }
                    try {
                        PnmpPollLowSpeedTask pnmpPollLowSpeedTask = createPnmpPollLowSpeedTask(cmtsCm);
                        pnmpTaskBuildService.putTaskToQueue(pnmpPollLowSpeedTask);
                    } catch (Exception e) {
                        logger.error("LowSpeedTaskBuildJob.data format error", e);
                    }
                }
            }
            // 构造结束任务
            PnmpPollLowSpeedEndTask pnmpPollLowSpeedEndTask = createPnmpPollLowSpeedEndTask();
            pnmpTaskBuildService.putTaskToQueue(pnmpPollLowSpeedEndTask);
            pnmpTaskBuildService.fireRoundEndMessage(new PnmpPollLowSpeedTask(),collectTime);
            logger.trace("PnmpPollLowSpeedTask End" + new Date().toString());
        } else {
            logger.info("Last PnmpPollLowSpeedTask Build Is Running, PnmpPollLowSpeedTask End" + new Date().toString());
        }
//
        logger.info("PnmpPollLowSpeedTask.execute end");
    }

    private PnmpPollLowSpeedEndTask createPnmpPollLowSpeedEndTask() {
        return new PnmpPollLowSpeedEndTask();
    }

    private PnmpPollLowSpeedTask createPnmpPollLowSpeedTask(CmtsCm cmtsCm) {
        PnmpPollLowSpeedTask pnmpPollLowSpeedTask = new PnmpPollLowSpeedTask();
        pnmpPollLowSpeedTask.setBaseTaskInfo(cmtsCm);
        return pnmpPollLowSpeedTask;
    }

    /**
     * 获取bean及变量
     *
     * @param ctx
     */
    private void getBeanAndValue(JobExecutionContext ctx) {
        BeanFactory beanFactory = (BeanFactory) ctx.getJobDetail().getJobDataMap().get("beanFactory");
        entityTypeService = (EntityTypeService) beanFactory.getBean("entityTypeService");
        cmcDao = (CmcDao) beanFactory.getBean("cmcDao");
        pnmpTaskBuildService = (PnmpTaskBuildService) beanFactory.getBean("pnmpTaskBuildService");
        cmPollTaskBuildDao = (CmPollTaskBuildDao) beanFactory.getBean("cmPollTaskBuildDao");
    }

    /**
     * 设置采集时间
     */
    private void setCollectTime() {
        collectTime = CollectTimeUtil.getCollectTimeUtil("PnmpLow").getCollectTime();
    }
}
