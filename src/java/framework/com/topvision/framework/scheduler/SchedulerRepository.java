/**
 * 
 */
package com.topvision.framework.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author kelers
 * 
 */
public class SchedulerRepository {

    private static SchedulerRepository repository;

    public static final String DEFAULT_SCHEDULER_NAME = "DefaultScheduler";

    public static SchedulerRepository getInstance() {
        if (repository == null) {
            repository = new SchedulerRepository();
        }
        return repository;
    }

    private SchedulerFactory sf;
    private Map<String, Scheduler> schedulers = new HashMap<String, Scheduler>();

    private SchedulerRepository() {
        sf = new StdSchedulerFactory();
        try {
            schedulers.put("DEFAULT_SCHEDULER_NAME", StdSchedulerFactory.getDefaultScheduler());
        } catch (SchedulerException se) {
        }
    }

    /**
     * 得到默认的任务调度器。
     * 
     * @return
     * @throws SchedulerException
     */
    public Scheduler getDefaultScheduler() throws SchedulerException {
        return getScheduler(DEFAULT_SCHEDULER_NAME);
    }

    /**
     * 得到给定名称的任务调度器。
     * 
     * @param name
     * @return
     * @throws SchedulerException
     */
    public Scheduler getScheduler(String name) throws SchedulerException {
        Scheduler s = schedulers.get(name);
        if (s == null) {
            s = sf.getScheduler();
            schedulers.put(name, s);
        }
        return s;
    }

    /**
     * 得到调度器工厂。
     * 
     * @return
     */
    public SchedulerFactory getSchedulerFactory() {
        return sf;
    }

    public Map<String, Scheduler> getSchedulers() {
        return schedulers;
    }

}
