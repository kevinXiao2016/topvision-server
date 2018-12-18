package com.topvision.framework.thread;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Poolable<T> extends Thread {
    protected Logger logger =  LoggerFactory.getLogger(Poolable.class);
    private boolean runningFlag;
    private T argument;

    public boolean isRunning() {
        return runningFlag;
    }

    public synchronized void setRunning(boolean flag) {
        runningFlag = flag;
        if (flag)
            this.notify();
    }

    public T getArgument() {
        return argument;
    }

    public void setArgument(T argument) {
        this.argument = argument;
    }

    public Poolable() {
        runningFlag = false;
    }

    public synchronized void run() {
        try {
            while (true) {
                if (!runningFlag) {
                    this.wait();
                } else {
                    process();
                    sleep(100);
                    logger.trace("Thread is sleeping...");
                    setRunning(false);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Interrupt",e);
        }
    }//end of run()

    protected abstract void process();
}