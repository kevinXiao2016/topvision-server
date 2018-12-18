/***********************************************************************
 * $Id: MyThreadFactory.java,v1.0 2013-11-28 下午5:51:17 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import org.snmp4j.SNMP4JSettings;
import org.snmp4j.util.ThreadFactory;
import org.snmp4j.util.WorkerTask;

/**
 * @author Victor
 * @created @2013-11-28-下午5:51:17
 *
 */
public class MyThreadFactory implements ThreadFactory {
    private long joinTimeout;
    final ThreadGroup group;

    public MyThreadFactory() {
        joinTimeout = SNMP4JSettings.getThreadJoinTimeout();
        group = new ThreadGroup("SnmpUDPTransportMapping");
    }

    /**
     * Creates a new thread of execution for the supplied task.
     *
     * @param name the name of the execution thread.
     * @param task the task to be executed in the new thread.
     * @return the <code>WorkerTask</code> wrapper to control start and
     *   termination of the thread.
     */
    public WorkerTask createWorkerThread(String name, WorkerTask task, boolean daemon) {
        WorkerThread wt = new WorkerThread(group, task, name);
        wt.thread.setDaemon(daemon);
        return wt;
    }

    /**
     * Sets the maximum time to wait when joining a worker task thread.
     * @param millis
     *    the time to wait. 0 waits forever.
     * @since 1.10.2
     */
    public void setThreadJoinTimeout(long millis) {
        this.joinTimeout = millis;
    }

    public class WorkerThread implements WorkerTask {

        private Thread thread;
        private WorkerTask task;
        private boolean started = false;

        public WorkerThread(ThreadGroup group, WorkerTask task, String name) {
            this.thread = new Thread(group, task, name);
            this.task = task;
        }

        public void terminate() {
            task.terminate();
        }

        public void join() throws InterruptedException {
            task.join();
            thread.join(joinTimeout);
        }

        public void run() {
            if (!started) {
                started = true;
                thread.start();
            } else {
                thread.run();
            }
        }

        public void interrupt() {
            task.interrupt();
            thread.interrupt();
        }
    }
}
