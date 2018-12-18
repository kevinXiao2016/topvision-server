/***********************************************************************
 * $Id: TimeScheduler.java,v 1.1 Aug 19, 2009 3:29:07 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.scheduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Create Date Aug 19, 2009 3:29:07 PM
 * 
 * @author kelers
 * 
 */
public class TimeScheduler {

    private static TimeScheduler scheduler = new TimeScheduler();

    public static TimeScheduler getInstance() {
        return scheduler;
    }

    private Timer timer = null;

    private TimeScheduler() {
        timer = new Timer();
    }

    public void close() {
        timer.cancel();
    }

    /**
     * 给定某个时间点开始调度任务.
     * 
     * @param task
     * @param firstTime
     * @param period
     */
    public void schedule(TimerTask task, Date firstTime, long period) {
        timer.schedule(task, firstTime, period);
    }

    public void schedule(TimerTask task, long delay) {
        timer.schedule(task, delay);
    }

    /**
     * 延迟调度任务.
     * 
     * @param task
     * @param delay
     * @param period
     */
    public void schedule(TimerTask task, long delay, long period) {
        timer.schedule(task, delay, period);
    }

}
