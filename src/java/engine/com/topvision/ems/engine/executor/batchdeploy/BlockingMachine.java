/***********************************************************************
 * $Id: BlockingMachine.java,v1.0 2013年12月7日 上午10:17:41 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.batchdeploy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Bravin
 * @created @2013年12月7日-上午10:17:41
 *
 */
public class BlockingMachine {
    private AtomicInteger atom;
    private final ReentrantLock lock;
    private final ConcurrentHashMap<Integer, Condition> map;

    public BlockingMachine() {
        lock = new ReentrantLock();
        map = new ConcurrentHashMap<>();
    }

    public void compareOrWait(int value) throws InterruptedException {
        if (atom.get() != value) {
            if (map.contains(value)) {
                map.put(value, lock.newCondition());
            }
        }
    }

    public int getAndDecrement() {
        int value = atom.getAndDecrement() - 1;
        if (map.contains(value)) {
            Condition c = map.get(value);
            lock.lock();
            c.signalAll();
        }
        return value + 1;
    }

    public int getAndIncrement() {
        int value = atom.getAndIncrement() + 1;
        if (map.contains(value)) {
            Condition c = map.get(value);
            lock.lock();
            c.signalAll();
        }
        return value - 1;
    }
}
