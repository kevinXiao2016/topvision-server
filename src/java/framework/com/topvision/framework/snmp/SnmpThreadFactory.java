/***********************************************************************
 * $Id: SnmpThreadFactory.java,v 1.1 May 27, 2008 3:43:21 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.exception.engine.SnmpException;

/**
 * @Create Date May 27, 2008 3:43:21 PM
 * 
 * @author kelers
 * 
 */
public class SnmpThreadFactory implements ThreadFactory {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public SnmpThreadFactory() {
        group = new ThreadGroup("SnmpThreadGroup");
    }

    public SnmpThreadFactory(String groupName) {
        group = new ThreadGroup(groupName);
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ThreadFactory# newThread(java.lang.Runnable)
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, group.getName() + "-" + threadNumber.getAndIncrement(), 0);
        if (r instanceof SnmpThreadPoolExecutor.Worker) {
            try {
                ((SnmpThreadPoolExecutor.Worker) r).setSnmpUtil(new SnmpUtil());
            } catch (SnmpException ex) {
                logger.debug(ex.getMessage(), ex);
            }
        }
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
