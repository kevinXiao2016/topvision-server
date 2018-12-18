/***********************************************************************
 * $Id: Ping.java,v 1.1 2007-3-7 上午11:26:02 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2006 WantTo All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.EnvironmentConstants;

/**
 * @Create Date 2007-3-7 上午11:26:02
 * 
 * @author kelers
 * 
 */
public abstract class Ping {
    protected static Logger logger = LoggerFactory.getLogger(Ping.class);
    private String host = null;
    protected int timeout = 4000;
    protected int count = 1;

    /**
     * 判断设备是否在线
     */
    public boolean online(String host, int timeout, int count) {
        return ping(host, timeout, count) >= 0;
    }

    /**
     * ping设备返回超时时间
     */
    public abstract int ping(String host, int timeout, int count);

    /**
     * ping 设备 ，增加重试次数
     * 
     * @param host
     * @param timeout
     * @param count
     * @param retry
     * @return
     */
    public abstract int ping(String host, int timeout, int count, int retry);

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

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        EnvironmentConstants.putEnv(EnvironmentConstants.DLL_HOME, "dll");
        Ping cmd = new CmdPing();
        System.out.println(cmd.ping("172.17.1.11", 100, 1, 1));
    }
}
