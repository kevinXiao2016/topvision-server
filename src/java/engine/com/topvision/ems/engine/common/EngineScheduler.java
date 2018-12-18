/***********************************************************************
 * $Id: EngineScheduler.java,v1.0 2013-2-27 下午4:01:39 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.common;

import java.util.List;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

/**
 * 工程中所有的任务都通过这个服务管理。统一管理便于任务通过admin查看。
 * 
 * @author Victor
 * @created @2013-2-27-下午4:01:39
 * 
 */
public interface EngineScheduler {
    /**
     * 
     * @return 任务列表
     */
    public List<Map<String, String>> getJobs();

    /**
     * 删除任务
     * 
     * @param key
     * @throws SchedulerException
     */
    public void deleteJob(JobKey key) throws SchedulerException;

    /**
     * 新建一个任务
     * 
     * @param job
     * @param trigger
     * @throws SchedulerException
     */
    public void scheduleJob(JobDetail job, Trigger trigger) throws SchedulerException;

    /**
     * @return 返回SchedulerContext
     */
    public SchedulerContext getContext() throws SchedulerException;

    /**
     * 暂停一个任务
     * 
     * @param key
     */
    public void pauseJob(JobKey key) throws SchedulerException;

    /**
     * 重新启动一个任务
     * 
     * @param key
     */
    public void resumeJob(JobKey key) throws SchedulerException;

    /**
     * 获取任务
     * 
     * @param key
     * @return
     */
    public JobDetail getJobDetail(JobKey key) throws SchedulerException;

    /**
     * 得到Job的执行策略Trigger
     * @param triggerKey
     * @return
     * @throws SchedulerException 
     */
    Trigger getTrigger(TriggerKey triggerKey) throws SchedulerException;

    /**
     * 重新调度一个任务，包含Job的数据以及Job的执行策略.推荐先取出JobDetail然后再modifyJobDetail()进行修改
     * @param jobDetail
     * @param trigger
     * @throws SchedulerException 
     */
    void modifySchedualJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException;

    /**
     * 修改JobDetail.推荐先取出JobDetail然后再modifyJobDetail()进行修改
     * @param jobDetail
     * @throws SchedulerException 
     */
    void modifyJobDetail(JobDetail jobDetail) throws SchedulerException;

    /**
     * 修改JobDataMap中的数据，由于JobDataMap在无状态时任何修改都无效，有状态时只能在job.execute()方法内部修改才能生效，
     * 所以job.execute()方法外部无法通过取出jobDataMap直接进行操作以期修改jobDataMap，推荐先取出JobDetail然后再modifyJobDetail()进行修改
     * @param jobKey
     * @param jobDataMap
     * @throws SchedulerException 
     */
    void modifyJobDataMap(JobKey jobKey, JobDataMap jobDataMap) throws SchedulerException;

    /**
     * 修改Job的执行时间，建议使用的时候先调用 getTrigger(TriggerKey)得到trigger，修改后进行modify，而不是新建一个Trigger
     * @param trigger
     * @throws SchedulerException 
     */
    void modifyJobTrigger(Trigger trigger) throws SchedulerException;

    /**
     * 立即执行一个任务
     * 
     * @param key
     */
    public void triggerJob(JobKey key) throws SchedulerException;
    
    /**
     * 获取trigger
     * 
     * @param key
     * @throws SchedulerException 
     */
    public List<Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException;
}
