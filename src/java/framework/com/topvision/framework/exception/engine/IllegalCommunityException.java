/***********************************************************************
 * $Id: IllegalCommunityException.java,v1.0 2011-3-31 下午05:14:17 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Victor
 * 
 */
public class IllegalCommunityException extends SnmpException {
    private static final long serialVersionUID = 7852239830273682131L;
    private String ip;

    private String community;

    public IllegalCommunityException() {
        super();
    }

    public IllegalCommunityException(String message) {
        super(message);
    }

    public IllegalCommunityException(String ip, String community) {
        this.ip = ip;
        this.community = community;
    }

    public IllegalCommunityException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalCommunityException(Throwable cause) {
        super(cause);
    }

    /**
     * @return the community
     */
    public String getCommunity() {
        return community;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param community
     *            the community to set
     */
    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
}
