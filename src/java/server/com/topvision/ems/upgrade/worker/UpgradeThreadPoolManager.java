/***********************************************************************
 * $Id: UpgradeThreadPoolManager.java,v1.0 2014年9月23日 下午7:12:03 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.worker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.topvision.ems.upgrade.domain.UpgradeEntity;

/**
 * @author loyal
 * @created @2014年9月23日-下午7:12:03
 * 
 */
public class UpgradeThreadPoolManager {
    private ThreadPoolExecutor threadPoolExecutor;

    public UpgradeThreadPoolManager(int threadCount) {
        int corePoolSize = threadCount / 3;
        int maximumPoolSize = threadCount;
        int keepAliveTime = 30;
        int queueSize = 1024;
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,
                    new ArrayBlockingQueue<Runnable>(queueSize));
        }
    }

    public Future<UpgradeEntity> process(UpgradeWorker upgradeWorker) {
        return threadPoolExecutor.submit(upgradeWorker);
    }

    public void destroy() {
        threadPoolExecutor.shutdown();
    }

}
