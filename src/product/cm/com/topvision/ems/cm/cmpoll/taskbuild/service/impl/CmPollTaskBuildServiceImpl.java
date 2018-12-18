/***********************************************************************
 * $ CmPollTaskBuildServiceImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.taskbuild.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.topvision.framework.common.CollectTimeUtil;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.cmpoll.config.service.CmPollConfigService;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollEndTask;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.ems.cm.cmpoll.scheduler.service.CmPollSchedulerService;
import com.topvision.ems.cm.cmpoll.taskbuild.service.CmPollTaskBuildService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.SchedulerService;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Service("cmPollTaskBuildService")
public class CmPollTaskBuildServiceImpl extends BaseService implements CmPollTaskBuildService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private CmPollConfigService cmPollConfigService;
    @Autowired
    private CmPollSchedulerService cmPollSchedulerService;
    private JobDetail job;
    private ArrayBlockingQueue<CmPollTask> cmPollTaskList = new ArrayBlockingQueue<CmPollTask>(100);
    private Integer status = 0;// 0:上轮轮询已结束，1：上轮轮询未结束
    private Long cmOnLine; // 在线CM总数，提供计算预计完成时间使用
    private Long collectTime;

    @Override
    public void initialize() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void start() {
        logger.debug("CmPollTaskBuildServiceImpl.start");
        startCmPollBuild();
        new TaskAppendThread().start();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startCmPollBuild() {
        logger.debug("CmPollTaskBuildServiceImpl.startCmPollBuild");
        Long interval = cmPollConfigService.getCmPollInterval();
        CollectTimeUtil.createCollectTimeUtil("cmPoll", System.currentTimeMillis(), interval);
        TriggerBuilder<SimpleTrigger> builder;
        if (job != null) {
            stopCmPollBuild();
        }
        try {
            job = newJob((Class<Job>) Class.forName("com.topvision.ems.cm.cmpoll.taskbuild.CmPollTaskBuildJob"))
                    .withIdentity("CmPoll.TaskBuild").build();
            job.getJobDataMap().put("beanFactory", beanFactory);
            job.getJobDataMap().put("interval", interval);
            builder = newTrigger().withIdentity(job.getKey().getName(), job.getKey().getGroup()).startNow()
                    .withSchedule(repeatSecondlyForever(interval.intValue()));
            schedulerService.scheduleJob(job, builder.build());
        } catch (ClassNotFoundException e) {
            if (logger.isErrorEnabled()) {
                logger.error("cmPollTaskBuildJob----ClassNotFoundException");
            }
        } catch (SchedulerException e) {
            if (logger.isErrorEnabled()) {
                logger.error("cmPollTaskBuildJob----SchedulerException");
            }
        }
    }

    @Override
    public void stopCmPollBuild() {
        logger.debug("CmPollTaskBuildServiceImpl.stopCmPollBuild");
        try {
            if (job != null) {
                schedulerService.deleteJob(job.getKey());
                this.status = 0;
                // 构造结束任务
                CmPollTask cmPollTask = new CmPollEndTask();
                putTaskToQueue(cmPollTask);
                fireRoundEndMessage(collectTime);
            }
            job = null;
        } catch (SchedulerException e) {
            if (logger.isErrorEnabled()) {
                logger.error("cmPollTaskBuildJob----stopCmPollBuildFail", e);
            }
        }
    }

    /**
     * 任务分配线程类
     * 
     * @author loyal
     * @created @2015年3月11日-下午2:38:54
     * 
     */
    class TaskAppendThread extends Thread {
        @Override
        public void run() {
            this.setName("TaskAppendThread");
            logger.debug("TaskAppendThread.run start");
            while (true) {
                try {
                    Integer engineId = cmPollSchedulerService.isAnyIdle();
                    List<CmPollTask> appendCmPollTaskList = new ArrayList<CmPollTask>();
                    Integer taskCount = cmPollTaskList.size();
                    if (logger.isDebugEnabled()) {
                        logger.debug("TaskAppendThread.run taskCount:" + taskCount);
                    }

                    if (engineId < 0 || cmPollTaskList.isEmpty()) {
                        // 如果没有空闲采集器，等待五秒
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        continue;
                    }
                    Integer appendCmCount = cmPollSchedulerService.idleTaskCount(engineId);
                    int count = 0;
                    if (appendCmCount >= taskCount) {
                        count = taskCount;
                    } else {
                        count = appendCmCount;
                    }
                    for (int i = 0; i < count; i++) {
                        try {
                            appendCmPollTaskList.add(cmPollTaskList.take());
                        } catch (InterruptedException e) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("", e);
                            }
                        }
                    }
                    if (appendCmPollTaskList.size() > 0) {
                        cmPollSchedulerService.appendTask(engineId, collectTime, appendCmPollTaskList);
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("TaskAppendThread while", e);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
        };
    };

    @Override
    public void putTaskToQueue(CmPollTask cmPollTask) {
        try {
            this.cmPollTaskList.put(cmPollTask);
        } catch (InterruptedException e) {
            if (logger.isErrorEnabled()) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void fireRoundStartMessage(Long collectTime, Long cmOnLine) {
        this.collectTime = collectTime;
        this.cmOnLine = cmOnLine;
        cmPollSchedulerService.roundStart(collectTime);
        this.status = 1;
    }

    @Override
    public void fireRoundEndMessage(Long collectTime) {
        cmPollSchedulerService.roundFinished(collectTime);
        this.status = 0;
    }

    @Override
    public Boolean isLastPollEnd() {
        return this.status == 0 ? true : false;
    }

    @Override
    public Long getCmOnLine() {
        return this.cmOnLine;
    }

}