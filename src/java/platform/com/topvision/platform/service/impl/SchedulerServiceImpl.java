/***********************************************************************
 * $Id: SchedulerServiceImpl.java,v1.0 2013-3-5 下午2:35:10 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.StringMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.topvision.platform.SystemConstants;
import com.topvision.platform.service.SchedulerService;

/**
 * @author Victor
 * @created @2013-3-5-下午2:35:10
 * 
 */
@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {
    protected static final Logger logger = LoggerFactory.getLogger(SchedulerServiceImpl.class);
    private static final boolean REPLACE_EXISTED_JOB = true;
    private Scheduler sched = null;

    @PostConstruct
    public void initialize() {
        logger.info("SchedulerService initialize");
        try {
            SchedulerFactory sf = new StdSchedulerFactory(SystemConstants.WEB_INF_REAL_PATH + "/conf/quartz.properties");
            sched = sf.getScheduler();
            sched.start();
        } catch (Exception ex) {
            logger.debug("SchedulerService initialize error", ex);
        }
    }

    @PreDestroy
    public void destroy() {
        logger.info("SchedulerService destroy");
        try {
            sched.shutdown();
        } catch (SchedulerException ex) {
            logger.debug("SchedulerService destroy error", ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#getJobs()
     */
    @Override
    public List<Map<String, String>> getJobs() {
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        try {
            for (String name : sched.getTriggerGroupNames()) {
                for (TriggerKey key : sched.getTriggerKeys(new GroupMatcher<TriggerKey>(name,
                        StringMatcher.StringOperatorName.CONTAINS) {
                    private static final long serialVersionUID = 2340465047338422972L;
                })) {
                    Trigger trigger = sched.getTrigger(key);
                    JobDetail job = sched.getJobDetail(trigger.getJobKey());
                    Map<String, String> data = new HashMap<String, String>();
                    datas.add(data);
                    data.put("group", name);
                    data.put("name", job.getKey().getName());
                    data.put("NextFireTime", trigger.getNextFireTime() == null ? "\u505c\u6b62\u6267\u884c"
                            : DateFormat.getDateTimeInstance().format(trigger.getNextFireTime()));
                    data.put("PreviousFireTime",
                            trigger.getPreviousFireTime() == null ? "\u8fd8\u6ca1\u6709\u5f00\u59cb\u6267\u884c"
                                    : DateFormat.getDateTimeInstance().format(trigger.getPreviousFireTime()));
                    data.put("job", job.getJobClass().getName());
                    data.put("trigger", trigger.toString());
                }
            }
        } catch (SchedulerException e) {
            logger.error("", e);
        }
        return datas;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#deleteJob(org.quartz.JobKey)
     */
    @Override
    public void deleteJob(JobKey key) throws SchedulerException {
        sched.deleteJob(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#scheduleJob(org.quartz.JobDetail,
     * org.quartz.Trigger)
     */
    @Override
    public void scheduleJob(JobDetail job, Trigger trigger) throws SchedulerException {
        sched.scheduleJob(job, trigger);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#getContext()
     */
    @Override
    public SchedulerContext getContext() throws SchedulerException {
        return sched.getContext();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#pauseJob(org.quartz.JobKey)
     */
    @Override
    public void pauseJob(JobKey key) throws SchedulerException {
        sched.pauseJob(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#resumeJob(org.quartz.JobKey)
     */
    @Override
    public void resumeJob(JobKey key) throws SchedulerException {
        sched.resumeJob(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#getJobDetail(org.quartz.JobKey)
     */
    @Override
    public JobDetail getJobDetail(JobKey key) throws SchedulerException {
        return sched.getJobDetail(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#triggerJob(org.quartz.JobKey)
     */
    @Override
    public void triggerJob(JobKey key) throws SchedulerException {
        sched.triggerJob(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#getTrigger(org.quartz.TriggerKey)
     */
    @Override
    public Trigger getTrigger(TriggerKey triggerKey) throws SchedulerException {
        return sched.getTrigger(triggerKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#modifySchedualJob(org.quartz.JobDetail,
     * org.quartz.Trigger)
     */
    @Override
    public void modifySchedualJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        modifyJobTrigger(trigger);
        /*
         * Map<JobDetail, List<Trigger>> map = new HashMap<JobDetail, List<Trigger>>();
         * List<Trigger> list = new ArrayList<Trigger>(); list.add(trigger); map.put(jobDetail,
         * list); sched.scheduleJobs(map, REPLACE_EXISTED_JOB);
         */
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#modifyJobDetail(org.quartz.JobDetail)
     */
    @Override
    public void modifyJobDetail(JobDetail jobDetail) throws SchedulerException {
        JobKey jobKey = jobDetail.getKey();
        Trigger trigger = getTrigger(new TriggerKey(jobKey.getName(), jobKey.getGroup()));
        modifySchedualJob(jobDetail, trigger);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#modifyJobDataMap(org.quartz.JobKey,
     * org.quartz.JobDataMap)
     */
    @Override
    public void modifyJobDataMap(JobKey jobKey, JobDataMap jobDataMap) throws SchedulerException {
        Trigger trigger = getTrigger(new TriggerKey(jobKey.getName(), jobKey.getGroup()));
        JobDetail jobDetail = getJobDetail(jobKey);
        modifySchedualJob(jobDetail, trigger);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#modifyJobTrigger(org.quartz.Trigger)
     */
    @Override
    public void modifyJobTrigger(Trigger trigger) throws SchedulerException {
        /*
         * sched.unscheduleJob(trigger.getKey()); sched.scheduleJob(trigger);
         */
        sched.rescheduleJob(trigger.getKey(), trigger);
    }

    @Override
    public List<Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException {
        return (List<Trigger>) sched.getTriggersOfJob(jobKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.service.SchedulerService#getJobKeys(org.quartz.impl.matchers.GroupMatcher
     * )
     */
    @Override
    public Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) throws SchedulerException {
        return sched.getJobKeys(matcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.SchedulerService#deleteByGroupName(java.lang.String)
     */
    @Override
    public void deleteByGroupName(String groupName) {
        GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(groupName);
        try {
            Set<JobKey> jobKeys = getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

}
