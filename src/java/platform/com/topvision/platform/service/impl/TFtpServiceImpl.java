/***********************************************************************
 * $Id: TFtpServiceImpl.java,v1.0 2012-3-5 下午04:47:49 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import com.topvision.framework.service.BaseService;
import com.topvision.framework.tftp.Server;
import com.topvision.platform.service.TFtpService;

/**
 * @author huqiao
 * @created @2012-3-5-下午04:47:49
 * 
 */
public class TFtpServiceImpl extends BaseService implements TFtpService {
    private Server server;
    private int port;
    private String tftproot;
    private boolean tftpEnable;

    /**
     * @return the server
     */
    public Server getServer() {
        return server;
    }

    /**
     * @param server
     *            the server to set
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the tftproot
     */
    public String getTftproot() {
        return tftproot;
    }

    /**
     * @param tftproot
     *            the tftproot to set
     */
    public void setTftproot(String tftproot) {
        this.tftproot = tftproot;
    }

    /**
     * @return the tftpEnable
     */
    public boolean isTftpEnable() {
        return tftpEnable;
    }

    /**
     * @param tftpEnable
     *            the tftpEnable to set
     */
    public void setTftpEnable(boolean tftpEnable) {
        this.tftpEnable = tftpEnable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    public void destroy() {
        server.disconnect();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    public void initialize() {

        /*server = new Server(SystemConstants.ROOT_REAL_PATH + tftproot, port);
        try {
            server.connect();
            tftpEnable = true;
            logger.info("TFtpServer initialized");
        } catch (Exception e) {
            logger.info("TFtpServer can't be initialized");
            tftpEnable = false;
        }*/

    }
}
