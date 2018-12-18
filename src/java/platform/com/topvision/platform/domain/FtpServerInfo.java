/***********************************************************************
 * $Id: FtpClientException.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

/** FTP服务器信息domain类
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 */
public class FtpServerInfo extends BaseEntity {

    private static final long serialVersionUID = -2180197499735402131L;
    public static final Integer ON = 1;
    public static final Integer OFF = 0;

    private int ftpId;
    private String ip;
    private int port;
    private String userName;
    private String pwd;
    private String rootPath;
    private boolean writeable;
    private boolean started;
    private boolean reachable;
    private int enable;
    
    public FtpServerInfo() {
        super();
    }

    public FtpServerInfo(String ip, int port, String userName, String pwd) {
        super();
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.pwd = pwd;
    }
    
    public FtpServerInfo(String ip, int port, String userName, String pwd, String rootPath) {
        super();
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.pwd = pwd;
        this.rootPath = rootPath;
    }

    public FtpServerInfo(String ip, int port, String userName, String pwd, String rootPath, boolean writeable) {
        super();
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.pwd = pwd;
        this.rootPath = rootPath;
        this.writeable = writeable;
    }

    public int getFtpId() {
        return ftpId;
    }
    public void setFtpId(int ftpId) {
        this.ftpId = ftpId;
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
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getRootPath() {
        return rootPath;
    }
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
    public boolean isWriteable() {
        return writeable;
    }
    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }
    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
    public boolean isReachable() {
        return reachable;
    }
    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "FtpServerInfo [ftpId=" + ftpId + ", ip=" + ip + ", port=" + port + ", userName=" + userName + ", pwd="
                + pwd + ", rootPath=" + rootPath + ", writeable=" + writeable + ", started=" + started + ", reachable="
                + reachable + ", enable=" + enable + "]";
    }
    
}
