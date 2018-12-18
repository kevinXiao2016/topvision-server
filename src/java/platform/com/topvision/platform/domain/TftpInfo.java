/***********************************************************************
 * $Id: TftpInfo.java,v1.0 2013-1-26 下午3:42:31 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import java.io.File;

import com.topvision.framework.domain.BaseEntity;
import com.topvision.platform.SystemConstants;

/**
 * @author Administrator
 * @created @2013-1-26-下午3:42:31
 *
 */
public class TftpInfo extends BaseEntity {

    private static final long serialVersionUID = -266798225320830611L;
    public static final Integer ON = 1;
    public static final Integer OFF = 0;
    
    //创建TFTP服务器时的根路径
    public static final String TFTPROOTPATH = SystemConstants.ROOT_REAL_PATH + "/META-INF/tftpTemp";
    private String ip;
    private int port;
    private String rootPath;
    private int enable;
    /**
     * 
     */
    public TftpInfo() {
        super();
    }

    /**
     * @param ip
     * @param port
     */
    public TftpInfo(String ip, int port) {
        super();
        this.ip = ip;
        this.port = port;
    }

    /**
     * @param ip
     * @param port
     * @param rootPath
     */
    public TftpInfo(String ip, int port, String rootPath) {
        super();
        this.ip = ip;
        this.port = port;
        this.rootPath = rootPath;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String tftpInfo = "{ip:"+ip+", port:"+port+", rootPath:"+ rootPath+"}";
        return tftpInfo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
    
    public String getRealPath() {
        return TFTPROOTPATH + this.rootPath + File.separator;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static String getTftprootpath() {
        return TFTPROOTPATH;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

}
