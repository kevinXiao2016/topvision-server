/***********************************************************************
 * $Id: PingWorker.java,v 1.1 May 27, 2008 3:54:13 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date May 27, 2008 3:54:13 PM
 * 
 * @author kelers
 * 
 */
public class PingWorker implements Runnable {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Ping ping;
    private String ip;
    private PingResult result;
    private int timeout = 4000;
    private int count = 4;
    private int retry = 0;

    public PingWorker(String ip, PingResult result) {
        this.ip = ip;
        this.result = result;
    }

    /**
     * @return the ping
     */
    public Ping getPing() {
        return ping;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            result.setIp(ip);
            result.setResult(ping.ping(ip, timeout, count, retry));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * @param ping
     *            the ping to set
     */
    public void setPing(Ping ping) {
        this.ping = ping;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
