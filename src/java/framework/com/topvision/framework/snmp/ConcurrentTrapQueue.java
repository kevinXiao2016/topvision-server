/***********************************************************************
 * $Id: ConcurrentTrapQueue.java,v1.0 Jun 13, 2017 6:18:09 PM $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteLookupFailureException;

/**
 * @author Victorli
 * @created @Jun 13, 2017-6:18:09 PM
 *
 */
public class ConcurrentTrapQueue extends ConcurrentLinkedQueue<Trap> implements Runnable {
    private static final long serialVersionUID = -2489625713216801386L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ReentrantLock doTrapThreadLock;
    private Set<TrapCallback> listeners;
    private String address;

    public ConcurrentTrapQueue(Set<TrapCallback> listeners, String address) {
        this.address = address;
        this.listeners = listeners;
        doTrapThreadLock = new ReentrantLock();
    }

    boolean isLocked() {
        return doTrapThreadLock.isLocked();
    }

    void lock() {
        doTrapThreadLock.lock();
    }

    @Override
    public void run() {
        if (isLocked()) {
            return;
        }
        try {
            lock();
            if (logger.isDebugEnabled()) {
                logger.debug("Do trap from {}", address);
            }
            // 增加同步来保证同一设备来的trap顺序执行
            while (!isEmpty()) {
                Trap trap = poll();
                if (trap == null) {
                    continue;
                }
                for (Iterator<TrapCallback> itr = listeners.iterator(); itr.hasNext();) {
                    try {
                        itr.next().onTrap(trap);
                    } catch (RemoteLookupFailureException e) {
                        itr.remove();
                    }
                }
                if (logger.isDebugEnabled()) {
                    long spendTime = System.currentTimeMillis() - trap.getTrapTime().getTime();
                    logger.debug("Do trap from {} used {}", trap.getAddress(), spendTime);
                    if (spendTime > 1000) {
                        logger.debug("Parse the trap spend: {}\n{}", spendTime, trap);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Trap error:{}", e.getMessage());
            logger.debug("Trap error", e);
        } finally {
            doTrapThreadLock.unlock();
        }
    }
}
