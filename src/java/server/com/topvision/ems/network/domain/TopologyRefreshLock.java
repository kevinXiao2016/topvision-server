/***********************************************************************
 * $Id: TopologyRefreshCache.java,v1.0 2015-7-6 上午11:36:01 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Rod John
 * @created @2015-7-6-上午11:36:01
 *
 */
public class TopologyRefreshLock {

    static final ConcurrentMap<Long, ReentrantLock> lockHolder = new ConcurrentHashMap<Long, ReentrantLock>();

    /**
     * Get the cache use specified entityId and 
     * Create cache if not existed yet.
     */
    public static ReentrantLock getReentrantLock(Long entityId) {
        ReentrantLock lock = lockHolder.get(entityId);
        if (lock != null) {
            return lock;
        }
        lockHolder.putIfAbsent(entityId, new ReentrantLock());
        return lockHolder.get(entityId);
    }

}
