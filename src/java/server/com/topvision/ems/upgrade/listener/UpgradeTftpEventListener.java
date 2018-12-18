/***********************************************************************
 * $ JsDeriverTftpEventListener.java,v1.0 2013-4-27 15:26:48 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.listener;

import java.net.InetAddress;

import com.topvision.framework.tftp.TftpEventListener;

/**
 * @author jay
 * @created @2013-4-27-15:26:48
 */
public class UpgradeTftpEventListener implements TftpEventListener {
    private String ip;
    private String fileName;
    private double filesize = 0;
    private double complete = 0;

    public UpgradeTftpEventListener(String ip) {
        this.ip = ip;
    }

    public void onAfterDownload(InetAddress addr, int port, String fileName, boolean ok) {
        if (ip.equalsIgnoreCase(addr.getHostAddress())) {
            this.fileName = fileName;
            if (ok) {
                this.complete = filesize;
            } else {
                this.complete = 0;
            }
        }
    }

    public void onAfterUpload(InetAddress addr, int port, String fileName, boolean ok) {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    public void downloading(InetAddress addr, int port, String fileName, long filesize, long complete) {
        if (ip.equalsIgnoreCase(addr.getHostAddress())) {
            this.fileName = fileName;
            this.filesize = filesize;
            this.complete = complete;
        }
    }

    public double getComplete() {
        return complete;
    }

    public void setComplete(double complete) {
        this.complete = complete;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getFilesize() {
        return filesize;
    }

    public void setFilesize(double filesize) {
        this.filesize = filesize;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
