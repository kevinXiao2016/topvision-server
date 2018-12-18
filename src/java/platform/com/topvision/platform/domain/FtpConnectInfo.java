/***********************************************************************
 * $Id: FtpClientException.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import com.topvision.framework.domain.BaseEntity;

/** FTP连接信息domain类
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 */
public class FtpConnectInfo extends BaseEntity {

    private static final long serialVersionUID = 3644470859695210902L;

    private int ftpId;
    private String ip;
    private int port;
    private String userName;
    private String pwd;
    private String remotePath;
    private boolean readable;
    private boolean writeable;
    private boolean reachable;
    
    public FtpConnectInfo() {
        super();
    }

    public FtpConnectInfo(String ip, int port, String userName, String pwd) {
        super();
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.pwd = pwd;
    }

    public FtpConnectInfo(String ip, int port, String userName, String pwd, String remotePath) {
        super();
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.pwd = pwd;
        this.remotePath = remotePath;
    }

    @Override
    public String toString() {
        String str = "ip=" + ip + " port=" + port + " userName=" + userName 
                + " pwd=" + pwd + " remotePath=" + remotePath + " readable=" + readable 
                + " writeable=" + writeable + " reachable=" + reachable;
        return str;
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

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
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
}
