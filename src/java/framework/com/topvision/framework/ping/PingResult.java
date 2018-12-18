/***********************************************************************
 * $Id: PingResult.java,v 1.1 May 27, 2008 4:34:02 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.ping;

import java.io.Serializable;

/**
 * @Create Date May 27, 2008 4:34:02 PM
 * 
 * @author kelers
 * 
 */
public class PingResult implements Serializable {
    private static final long serialVersionUID = 7116359800092945420L;
    private String ip = null;
    private int result = -1;

    /**
     * @return the result
     */
    public boolean available() {
        return result >= 0;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the result
     */
    public int getResult() {
        return result;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(ip).append("] ping ").append(result);
        return sb.toString();
    }
}
