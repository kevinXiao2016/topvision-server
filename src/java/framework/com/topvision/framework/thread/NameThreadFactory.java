/***********************************************************************
 * $Id: NameThreadFactory.java,v1.0 2013-4-14 下午3:17:14 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Victor
 * @created @2013-4-14-下午3:17:14
 * 
 */
public class NameThreadFactory implements ThreadFactory {
    private String name;
    private AtomicInteger index = new AtomicInteger();

    public NameThreadFactory(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(String.format("%s-%d", name, index.getAndIncrement()));
        return thread;
    }
}
