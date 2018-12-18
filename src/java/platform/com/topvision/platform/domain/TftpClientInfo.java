/***********************************************************************
 * $Id: TftpClientInfo.java,v1.0 2014-1-21 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/

package com.topvision.platform.domain;

public class TftpClientInfo {
    
    public static final String SYSTEM_MODULE_NAME = "TelnetClient";
    public static final String CLIENT_TYPE = "clientType";
    public static final String RECORD_STATE = "recordState";
    public static final String TIMEOUT = "timeout";
    public static final int RECORD_ON = 1;
    public static final int RECORD_OFF = 0;
    public static final int INNER_CLIENT = 1;
    public static final int EXTERNAL_CLIENT = 2;
    
    private String ip;
    private Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "TftpClientInfo [ip=" + ip + ", port=" + port + "]";
    }

}
