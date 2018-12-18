/**
 * (c) Melexis Telecom and or Remote Operating Services B.V.
 *
 * Distributable under LGPL license
 * See terms of license at gnu.org
 */
package com.topvision.framework.tftp;

import java.net.InetAddress;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

import org.apache.log4j.Logger;

import com.topvision.framework.tftp.common.VirtualFileSystem;
import com.topvision.framework.tftp.server.EventListener;
import com.topvision.framework.tftp.server.TFTPServer;

/**
 * @author marco
 *         <p/>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go
 *         to Window>Preferences>Java>Code Generation.
 */
public class Server implements EventListener {
    /**
     * logger
     */
    private Logger log = Logger.getLogger(Server.class.getName());

    private TFTPServer tftpServer;
    private String root;
    private int port;

    private List<TftpEventListener> tftpEventListeners = Collections.synchronizedList(new ArrayList<TftpEventListener>());

    /**
     * Constructor for Server.
     */
    public Server(String root, int port) {
        VirtualFileSystem vfs = new FileSystem(root);
        tftpServer = new TFTPServer(vfs, this);
        tftpServer.setPoolSize(2);
        tftpServer.setPort(port);
    }

    public void connect() throws Exception {
        if (tftpServer == null)
            return;
        tftpServer.start();
    }

    public void disconnect() {
        if (tftpServer == null)
            return;
        tftpServer.stop();
    }

    public void onAfterDownload(InetAddress a, int p, String fileName, boolean ok) {
        if (ok) {
            log.debug("Send " + fileName + " sucessfully to client: " + a.getHostAddress() + " port: " + p);
        } else {
            log.debug("Send " + fileName + " file not sucessfully to client: " + a.getHostAddress() + " port: " + p);
        }
        for (TftpEventListener tftpEventListener : tftpEventListeners) {
            try {
                tftpEventListener.onAfterDownload(a, p, fileName, ok);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    public void onAfterUpload(InetAddress a, int p, String fileName, boolean ok) {
        if (ok) {
            log.debug("received " + fileName + " sucessfully from client: " + a.getHostAddress() + " port: " + p);
        } else {
            log.debug("received " + fileName + " file not sucessfully from client: " + a.getHostAddress() + " port: "
                    + p);
        }
        for (TftpEventListener tftpEventListener : tftpEventListeners) {
            try {
                tftpEventListener.onAfterUpload(a, p, fileName, ok);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    public void downloading(final InetAddress addr, final int port, final String fileName, final long filesize, final long complete) {
        new Thread() {
            public void run() {
                for (TftpEventListener tftpEventListener : tftpEventListeners) {
                    try {
                        tftpEventListener.downloading(addr, port, fileName, filesize, complete);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            }
        }.start();
    }

    public void addTftpEventListener(TftpEventListener tftpEventListener) {
        tftpEventListeners.add(tftpEventListener);
    }

    public void removeTftpEventListener(TftpEventListener tftpEventListener) {
        tftpEventListeners.remove(tftpEventListener);
    }
}
