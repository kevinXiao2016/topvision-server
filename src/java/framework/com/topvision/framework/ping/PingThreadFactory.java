/***********************************************************************
 * $Id: PingThreadFactory.java,v 1.1 May 27, 2008 3:43:21 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ping;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date May 27, 2008 3:43:21 PM
 * 
 * @author kelers
 * 
 */
public class PingThreadFactory implements ThreadFactory {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * Comment for <code>osname</code>.
     */
    public static String osname = System.getProperty("os.name");

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    PingThreadFactory() {
        //SecurityManager s = System.getSecurityManager();
        //group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        group = new ThreadGroup("PingThreadGroup");
        namePrefix = "Ping.pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ThreadFactory# newThread(java.lang.Runnable)
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (r instanceof PingThreadPoolExecutor.Worker) {
            try {
                CmdPing ping = new CmdPing();
                ((PingThreadPoolExecutor.Worker) r).setPing(ping);
            } catch (Exception e) {
            }
        }
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
