/***********************************************************************
 * $Id: Ping.java,v 1.1 Apr 6, 2009 7:49:13 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.io.Serializable;

/**
 * @Create Date Apr 6, 2009 7:49:13 PM
 * 
 * @author kelers
 * 
 */
public class Ping implements Serializable {
    private static final long serialVersionUID = 3438867106504094451L;
    private int timeout = 1000;
    private int retries = 0;
    private int tcpPort = 23;
    private int udpPort = 161;

    /**
     * @return the retries
     */
    public int getRetries() {
        return retries;
    }

    /**
     * @return the tcpPort
     */
    public final int getTcpPort() {
        return tcpPort;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @return the udpPort
     */
    public final int getUdpPort() {
        return udpPort;
    }

    /**
     * @param retries
     *            the retries to set
     */
    public void setRetries(int retries) {
        this.retries = retries;
    }

    /**
     * @param tcpPort
     *            the tcpPort to set
     */
    public final void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    /**
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @param udpPort
     *            the udpPort to set
     */
    public final void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }
}
