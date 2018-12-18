/***********************************************************************
 * $Id: EngineThreadPool.java,v1.0 2015年5月30日 上午10:45:33 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.common;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * 公共线程池。
 * 
 * @author Victor
 * @created @2015年5月30日-上午10:45:33
 *
 */
public interface EngineThreadPool {
    public void execute(Runnable command);

    public List<Runnable> shutdownNow();

    public int getCorePoolSize();

    public int getMaximumPoolSize();

    public BlockingQueue<Runnable> getQueue();

    public int getPoolSize();

    public int getActiveCount();

    public int getLargestPoolSize();

    public long getTaskCount();

    public long getCompletedTaskCount();

    public Integer getQueueSize();
}
