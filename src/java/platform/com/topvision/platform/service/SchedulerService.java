/***********************************************************************
 * $Id: SchedulerService.java,v1.0 2013-2-27 下午4:01:39 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * 工程中所有的任务都通过这个服务管理。统一管理便于任务通过admin查看。
 * 
 * @author Victor
 * @created @2013-2-27-下午4:01:39
 * 
 */
public interface SchedulerService {
    /**
     * 
     * @return 任务列表
     */
    List<Map<String, String>> getJobs();

    /**
     * 删除任务
     * 
     * @param key
     * @throws SchedulerException
     */
    void deleteJob(JobKey key) throws SchedulerException;

    /**
     * 新建一个任务
     * 
     * @param job
     * @param trigger
     * @throws SchedulerException
     */
    void scheduleJob(JobDetail job, Trigger trigger) throws SchedulerException;

    /**
     * @return 返回SchedulerContext
     */
    SchedulerContext getContext() throws SchedulerException;

    /**
     * 暂停一个任务
     * 
     * @param key
     */
    void pauseJob(JobKey key) throws SchedulerException;

    /**
     * 重新启动一个任务
     * 
     * @param key
     */
    void resumeJob(JobKey key) throws SchedulerException;

    /**
     * 获取任务
     * 
     * @param key
     * @return
     */
    JobDetail getJobDetail(JobKey key) throws SchedulerException;

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
    void triggerJob(JobKey key) throws SchedulerException;

    /**
     * 获取trigger
     * 
     * @param key
     * @throws SchedulerException 
     */
    List<Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException;

    /**
     * 查询JobKeys
     * @param matcher
     * @return
     * @throws SchedulerException
     */
    Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) throws SchedulerException;

    /**
     * 按组删除
     * @param groupName
     */
    void deleteByGroupName(String groupName);

}
