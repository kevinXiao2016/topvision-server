/***********************************************************************
 * $ com.topvision.framework.tftp.TftpEventListener,v1.0 2013-4-27 9:48:11 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.tftp;

import java.net.InetAddress;

/**
 * @author Administrator
 * @created @2013-4-27-9:48:11
 */
public interface TftpEventListener {
    public void onAfterDownload(InetAddress addr, int port, String fileName, boolean ok);
    public void onAfterUpload(InetAddress addr, int port, String fileName, boolean ok);
    public void downloading(InetAddress addr, int port, String fileName, long filesize, long complete);
}
